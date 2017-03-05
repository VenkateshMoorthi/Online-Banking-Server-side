package com.example.domain;

import java.util.List;

public class SavingsAccountDetails {

	private String savingsAccountBalance;
	private List<SavingsTransactions> savingsTransactionLists;
	public String getSavingsAccountBalance() {
		return savingsAccountBalance;
	}
	public void setSavingsAccountBalance(String savingsAccountBalance) {
		this.savingsAccountBalance = savingsAccountBalance;
	}
	public List<SavingsTransactions> getSavingsTransactionLists() {
		return savingsTransactionLists;
	}
	public void setSavingsTransactionLists(List<SavingsTransactions> savingsTransactionLists) {
		this.savingsTransactionLists = savingsTransactionLists;
	}
	
	
	
	
}
