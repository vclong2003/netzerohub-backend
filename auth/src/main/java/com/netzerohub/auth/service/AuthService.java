package com.netzerohub.auth.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.netzerohub.auth.config.RabbitMQProperties;
import com.netzerohub.auth.config.SecurityProperties;
import com.netzerohub.auth.dto.ForgotPasswordEmailEvent;
import com.netzerohub.auth.dto.RegisterEmailEvent;
import com.netzerohub.auth.dto.TokenPairDto;
import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.form.ForgotPasswordForm;
import com.netzerohub.auth.form.LoginForm;
import com.netzerohub.auth.form.RegisterForm;
import com.netzerohub.auth.form.ResetPasswordForm;
import com.netzerohub.auth.form.VerifyAccountForm;
import com.netzerohub.auth.repository.IAccountRepository;
import com.netzerohub.auth.util.PasswordUtils;
import com.netzerohub.common.enums.AccountRole;
import com.netzerohub.common.enums.ErrorCode;
import com.netzerohub.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final IAccountRepository accountRepository;
    private final SecurityProperties securityProperties;
    private final PasswordUtils passwordUtils;
    private final TokenService tokenService;
    private final EmailOtpService emailOtpService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void register(RegisterForm form) {
        Account existingAccount = accountRepository.findOneByEmail(form.getEmail()).orElse(null);

        if (existingAccount != null && existingAccount.getIsVerified()) {
            throw new BadRequestException("Account already exists");
        }

        if (existingAccount == null) {
            Account account = new Account();
            account.setEmail(form.getEmail());
            account.setPassword(passwordUtils.hashPassword(form.getPassword()));
            account.setFirstName(form.getFirstName());
            account.setLastName(form.getLastName());
            accountRepository.save(account);
        }

        String rawOtp = emailOtpService.genAndSaveOtp(form.getEmail(), 60);
        RegisterEmailEvent emailEvent = new RegisterEmailEvent(form.getEmail(), rawOtp, 5);
        try {
            rabbitTemplate.convertAndSend(
                    rabbitMQProperties.exchange().notification(),
                    rabbitMQProperties.routingKey().notification().registration(),
                    emailEvent);
            log.info("added register evt");
        } catch (Exception e) {
            log.error("error adding register evt", e);
        }
    }

    public TokenPairDto login(LoginForm form) {
        Account account = accountRepository.findOneByEmail(form.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Invalid email or password"));

        if (!passwordUtils.verifyPassword(form.getPassword(), account.getPassword())) {
            throw new BadRequestException(ErrorCode.InvalidCredentials, "Invalid email or password");
        }

        if (!account.getIsVerified()) {
            throw new BadRequestException(ErrorCode.AccountNotVerified, "Account is not verified");
        }

        String refreshToken = tokenService.genAndSaveRefreshToken(account.getId(), account.getRole());
        String accessToken = tokenService.genAccessToken(account.getId(), account.getRole());

        return new TokenPairDto(refreshToken, accessToken);
    }

    public TokenPairDto refreshAccessToken(String refreshToken) {
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);
        return new TokenPairDto(null, newAccessToken);
    }

    public void verifyAccount(VerifyAccountForm form) {
        Account savedAccount = accountRepository.findOneByEmail(form.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));

        if (savedAccount.getIsVerified()) {
            return;
        }

        Boolean isOtpValid = emailOtpService.verifyOtp(form.getEmail(), form.getOtp());
        if (isOtpValid) {
            savedAccount.setIsVerified(true);
            accountRepository.save(savedAccount);
            return;
        }

        throw new BadRequestException(ErrorCode.InvalidInput, "Invalid OTP");
    }

    public void forgotPassword(ForgotPasswordForm form) {
        Account savedAccount = accountRepository.findOneByEmail(form.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));

        String rawOtp = emailOtpService.genAndSaveOtp(savedAccount.getEmail(), 60);

        ForgotPasswordEmailEvent emailEvent = new ForgotPasswordEmailEvent(savedAccount.getEmail(), rawOtp, 5);
        try {
            rabbitTemplate.convertAndSend(
                    rabbitMQProperties.exchange().notification(),
                    rabbitMQProperties.routingKey().notification().passwordReset(), emailEvent);
        } catch (Exception e) {
            log.error("error adding forgot password evt", e);
        }
    }

    public void resetPassword(ResetPasswordForm form) {
        Account savedAccount = accountRepository.findOneByEmail(form.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));

        Boolean isOtpValid = emailOtpService.verifyOtp(savedAccount.getEmail(), form.getOtp());
        if (isOtpValid) {
            savedAccount.setPassword(passwordUtils.hashPassword(form.getNewPassword()));
            accountRepository.save(savedAccount);
            return;
        }

        throw new BadRequestException(ErrorCode.InvalidInput, "Invalid OTP");
    }

    public void createAdminIfNotExist() {
        String adminEmail = securityProperties.admin().email();
        String adminPassword = securityProperties.admin().password();

        if (accountRepository.findOneByEmail(adminEmail).isEmpty()) {
            Account adminAccount = new Account();
            adminAccount.setFirstName("NetZeroHub");
            adminAccount.setLastName("Admin");
            adminAccount.setEmail(adminEmail);
            adminAccount.setPassword(passwordUtils.hashPassword(adminPassword));
            adminAccount.setRole(AccountRole.Admin);
            adminAccount.setIsVerified(true);
            accountRepository.save(adminAccount);
            log.info("default admin account created");
        }
    }

}
