package com.example.service.serviceimpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.PrimaryAccountDao;
import com.example.dao.PrimaryTransactionDao;
import com.example.dao.RecipientDao;
import com.example.dao.SavingsAccountDao;
import com.example.dao.SavingsTransactionDao;
import com.example.domain.PrimaryAccount;
import com.example.domain.PrimaryTransaction;
import com.example.domain.Recipient;
import com.example.domain.SavingsAccount;
import com.example.domain.SavingsTransaction;
import com.example.domain.User;
import com.example.service.UserService;
import com.example.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	UserService userService;
	
	@Autowired
	PrimaryTransactionDao primaryTransactionDao;
	
	@Autowired
	SavingsTransactionDao savingsTransactionDao;
	
	@Autowired
	PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	SavingsAccountDao savingsAccountDao;
	
	@Autowired 
	RecipientDao recipientDao;
	
	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(Long userId){
        User user = userService.findByUserId(userId);
        List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();

        return primaryTransactionList;
    }
	
	@Override
    public List<SavingsTransaction> findSavingsTransactionList(Long userId) {
        User user = userService.findByUserId(userId);
        List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

        return savingsTransactionList;
    }
    
	@Override
    public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

	@Override
    public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }
    
	@Override
    public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
        primaryTransactionDao.save(primaryTransaction);
    }

	@Override
    public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
        savingsTransactionDao.save(savingsTransaction);
    }

	@Override
	public String betweenAccountsTransfer(String transferFrom, String transferTo, String amount, Long userId) {
		User user = new User();
		user=userService.findByUserId(userId);
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		BigDecimal primaryBalance=primaryAccount.getAccountBalance();
		BigDecimal savingsBalance=savingsAccount.getAccountBalance();
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
            if(primaryBalance.compareTo(new BigDecimal(amount))>=0){
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
            return "Success";
            }else{
            	return "Invalid";
            }
        } else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
        	if(savingsBalance.compareTo(new BigDecimal(amount))>=0){
        	primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
            return "Success";
            }else{
            	return "Invalid";
            }
        } 
		return null;
	}

	@Override
	public void saveRecipient(Recipient recipient) {
		recipientDao.save(recipient);
	}

	@Override
	public List<Recipient> findRecipientList(Long userId) {
		User user = userService.findByUserId(userId);
		String username = user.getUsername();
        List<Recipient> recipientList = recipientDao.findAll().stream() 			//convert list to stream
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	//filters the line, equals to username
                .collect(Collectors.toList());
		
		return recipientList;
	}

	@Override
	public void deleteRecipientById(Long Id) {
		recipientDao.deleteById(Id);
	}

	@Override
	public Recipient findRecipientById(Long Id) {
		return recipientDao.findById(Id);
		
	}

	@Override
	public String toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
		
		BigDecimal primaryBalance=primaryAccount.getAccountBalance();
		BigDecimal savingsBalance=savingsAccount.getAccountBalance();
		
		if (accountType.equalsIgnoreCase("Primary")) {
			if(primaryBalance.compareTo(new BigDecimal(amount))>=0){
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
            return "Success";
			}else{
			return "Invaid";	
			}
        } else if (accountType.equalsIgnoreCase("Savings")) {
        	if(savingsBalance.compareTo(new BigDecimal(amount))>=0){
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
            return "Success";
            }else{
            	return "Invalid";
            }
        }
		return null;
	}
	
	
}
