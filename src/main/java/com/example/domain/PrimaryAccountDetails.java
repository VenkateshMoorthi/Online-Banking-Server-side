package com.example.domain;

import java.util.List;

public class PrimaryAccountDetails {

	private String primaryAccountBalance;
	private List<PrimaryTransactions> primaryTransactionLists;
	
	public String getPrimaryAccountBalance() {
		return primaryAccountBalance;
	}
	public void setPrimaryAccountBalance(String primaryAccountBalance) {
		this.primaryAccountBalance = primaryAccountBalance;
	}
	public List<PrimaryTransactions> getPrimaryTransactionLists() {
		return primaryTransactionLists;
	}
	public void setPrimaryTransactionList(List<PrimaryTransactions> primaryTransactionList) {
		this.primaryTransactionLists = primaryTransactionList;
	}
	
	
	
}
