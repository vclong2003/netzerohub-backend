package com.netzerohub.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.netzerohub.auth.entity.AccountToken;

public interface IAccountTokenRepository extends CrudRepository<AccountToken, String> {

}
