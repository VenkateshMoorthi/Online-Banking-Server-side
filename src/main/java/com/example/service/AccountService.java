 package com.example.service;

import java.math.BigDecimal;

import com.example.domain.AccountBalance;
import com.example.domain.PrimaryAccount;
import com.example.domain.SavingsAccount;
import com.example.domain.User;

public interface AccountService {
	PrimaryAccount createPrimaryAccount();
	SavingsAccount createSavingsAccount();
	AccountBalance getAccountBalanceByUserId(Long userId);
	String getPrimaryAccountBalanceByUserId(Long userId); 
	String getSavingsAccountBalanceByUserId(Long userId);
	public void deposit(String accountType,double amount,User user);
	public void withdraw(String accountType,double amount,User user);

}
