package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.AccountBalance;
import com.example.service.AccountService;

@RestController
public class UserFrontController {

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/userfront", method = RequestMethod.POST)
	public AccountBalance balanceDetails(@RequestBody Map<String,String> json){
		AccountBalance accountBalance = new AccountBalance();
		Long id=Long.parseLong(json.get("userid"));
		accountBalance=accountService.getAccountBalanceByUserId(id);
		return accountBalance;
	}
	
}
