package com.netzerohub.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netzerohub.auth.entity.Account;

public interface IAccountRepository extends JpaRepository<Account, Integer> {

}
