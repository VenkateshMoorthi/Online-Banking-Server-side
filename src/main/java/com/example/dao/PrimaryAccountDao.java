package com.example.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.PrimaryAccount;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount, Long> {

	PrimaryAccount findByAccountNumber(int accountNumber);
}
