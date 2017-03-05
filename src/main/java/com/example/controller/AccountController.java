package com.example.controller;

import java.security.Principal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.PrimaryAccountDetails;
import com.example.domain.PrimaryTransaction;
import com.example.domain.PrimaryTransactions;
import com.example.domain.SavingsAccountDetails;
import com.example.domain.SavingsTransaction;
import com.example.domain.SavingsTransactions;
import com.example.domain.User;
import com.example.service.AccountService;
import com.example.service.TransactionService;
import com.example.service.UserService;

@RestController
public class AccountController {
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	private AccountService accountservice;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String depositPOST(@RequestBody Map<String,String> json, Principal principal) {
        String accountType=json.get("accountType");
        double amount=Double.parseDouble(json.get("amount"));
        Long id=Long.parseLong(json.get("userId"));
        
        User user = new User();
        user=userservice.findByUserId(id);
        accountservice.deposit(accountType, amount, user);
        System.out.println(accountType+" "+amount+""+id);
        return "success";
    }
	
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@RequestBody Map<String,String> json, Principal principal) {
        String accountType=json.get("accountType");
        double amount=Double.parseDouble(json.get("amount"));
        Long id=Long.parseLong(json.get("userId"));
        
        User user = new User();
        user=userservice.findByUserId(id);
        accountservice.withdraw(accountType, amount, user);
        System.out.println(accountType+" "+amount+""+id);
        return "success";
    }
	
	@RequestMapping(value="/primaryAccount",method=RequestMethod.POST)
	public PrimaryAccountDetails primaryAccountDetails(@RequestBody Map<String,String> json){
		Long userId=Long.parseLong(json.get("userId"));
		
		PrimaryAccountDetails primaryAccountDetails = new PrimaryAccountDetails();
		primaryAccountDetails.setPrimaryAccountBalance(accountservice.getPrimaryAccountBalanceByUserId(userId));
		List<PrimaryTransactions> ptls = new ArrayList<PrimaryTransactions>();
		List<PrimaryTransaction> pts = transactionService.findPrimaryTransactionList(userId);
		for(PrimaryTransaction pt:pts){
			PrimaryTransactions pst = new PrimaryTransactions();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = formatter.format(pt.getDate());
			pst.setId(pt.getId());
			pst.setDate(s);
			pst.setDescription(pt.getDescription());
			pst.setType(pt.getType());
			pst.setStatus(pt.getStatus());
			pst.setAmount(Double.toString(pt.getAmount()));
			pst.setAvailableBalance(pt.getAvailableBalance().toPlainString());
			ptls.add(pst);
			
		}
		primaryAccountDetails.setPrimaryTransactionList(ptls);
		return primaryAccountDetails;
	}
	
	@RequestMapping(value="/savingsAccount",method=RequestMethod.POST)
	public SavingsAccountDetails savingsAccountDetails(@RequestBody Map<String,String> json){
		Long userId=Long.parseLong(json.get("userId"));
		
		SavingsAccountDetails savingsAccountDetails = new SavingsAccountDetails();
		savingsAccountDetails.setSavingsAccountBalance(accountservice.getSavingsAccountBalanceByUserId(userId));
		List<SavingsTransactions> ptls = new ArrayList<SavingsTransactions>();
		List<SavingsTransaction> pts = transactionService.findSavingsTransactionList(userId);
		for(SavingsTransaction pt:pts){
			SavingsTransactions pst = new SavingsTransactions();
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = formatter.format(pt.getDate());
			pst.setId(pt.getId());
			pst.setDate(s);
			pst.setDescription(pt.getDescription());
			pst.setType(pt.getType());
			pst.setStatus(pt.getStatus());
			pst.setAmount(Double.toString(pt.getAmount()));
			pst.setAvailableBalance(pt.getAvailableBalance().toPlainString());
			ptls.add(pst);
			
		}
		savingsAccountDetails.setSavingsTransactionLists(ptls);
		return savingsAccountDetails;
	}
}
