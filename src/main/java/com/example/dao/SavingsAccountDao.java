package com.example.dao;

import org.springframework.data.repository.CrudRepository;

import com.example.domain.SavingsAccount;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long> {

	SavingsAccount findByAccountNumber(int accountNumber);
}
