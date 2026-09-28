package com.netzerohub.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netzerohub.auth.entity.Account;

public interface IAccountRepository extends JpaRepository<Account, Integer> {
    public Optional<Account> findOneByEmail(String email);

    public boolean existsByEmail(String email);
}
