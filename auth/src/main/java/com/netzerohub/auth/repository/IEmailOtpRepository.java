package com.netzerohub.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.netzerohub.auth.entity.EmailOtp;

public interface IEmailOtpRepository extends CrudRepository<EmailOtp, String> {

}
