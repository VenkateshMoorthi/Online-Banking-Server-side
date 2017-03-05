package com.example.service;

import java.util.List;

import com.example.domain.PrimaryAccount;
import com.example.domain.PrimaryTransaction;
import com.example.domain.Recipient;
import com.example.domain.SavingsAccount;
import com.example.domain.SavingsTransaction;

public interface TransactionService {
	public List<PrimaryTransaction> findPrimaryTransactionList(Long userId);
	public List<SavingsTransaction> findSavingsTransactionList(Long userId);
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);
	public String betweenAccountsTransfer(String transferFrom, String transferTo, String amount, Long userId);
	public void saveRecipient(Recipient recipient);
	public List<Recipient> findRecipientList(Long userId);
	void deleteRecipientById(Long Id);
	public Recipient findRecipientById(Long Id);
	public String toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount);
}

