package com.example.service.serviceimpl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.PrimaryAccountDao;
import com.example.dao.SavingsAccountDao;
import com.example.domain.AccountBalance;
import com.example.domain.PrimaryAccount;
import com.example.domain.PrimaryTransaction;
import com.example.domain.SavingsAccount;
import com.example.domain.SavingsTransaction;
import com.example.domain.User;
import com.example.service.AccountService;
import com.example.service.UserService;
import com.example.service.TransactionService;

@Service
public class AccountServiceImpl implements AccountService {

    private static int nextAccountNumber=11223145;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	@Autowired
	private UserService userservice;

	@Autowired
	private TransactionService transactionService;
	
	@Override
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());

		primaryAccountDao.save(primaryAccount);

		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
		
	}

	@Override
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen());

		savingsAccountDao.save(savingsAccount);

		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}
	
	private int accountGen() {
		return ++nextAccountNumber;
    }

	@Override
	public AccountBalance getAccountBalanceByUserId(Long userId) {
		User user=new User();
		AccountBalance accountBalance= new AccountBalance();
		user=userservice.findByUserId(userId);
		accountBalance.setPrimaryAccountBalance(user.getPrimaryAccount().getAccountBalance().toPlainString());
		accountBalance.setSavingsAccountBalance(user.getSavingsAccount().getAccountBalance().toPlainString());
		return accountBalance;
		
	}

	@Override
	public void deposit(String accountType, double amount, User user) {

		if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
	}

	@Override
	public void withdraw(String accountType, double amount, User user) {
		if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
		
		
		} else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction); 
		}		
	}

	@Override
	public String getPrimaryAccountBalanceByUserId(Long userId) {
		User user = new User();
		user=userservice.findByUserId(userId);
		return user.getPrimaryAccount().getAccountBalance().toPlainString();
	}

	@Override
	public String getSavingsAccountBalanceByUserId(Long userId) {
		User user = new User();
		user=userservice.findByUserId(userId);
		return user.getSavingsAccount().getAccountBalance().toPlainString();
	}

	

}
