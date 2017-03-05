package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Recipient;
import com.example.domain.RecipientDetails;
import com.example.domain.User;
import com.example.service.TransactionService;
import com.example.service.UserService;

@RestController
public class TransferController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
    public String betweenAccounts(@RequestBody Map<String,String> json){
		String transferFrom = json.get("transferfrom");
		String transferTo = json.get("transferto");
		String amount = json.get("amount");
		Long userId=Long.parseLong(json.get("userId"));
		
		return transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, userId);
	}
	
	@RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
    public String recipientPost(@RequestBody Map<String,String> json) {

		Recipient recipient = new Recipient();
		recipient.setName(json.get("name"));
		recipient.setEmail(json.get("email"));
		recipient.setPhone(json.get("phone"));
		recipient.setAccountNumber(json.get("accountNumber"));
		recipient.setDescription(json.get("description"));
        Long userId=Long.parseLong(json.get("userId"));
		User user = userService.findByUserId(userId);
        recipient.setUser(user);
        
        transactionService.saveRecipient(recipient);

        return "redirect:/transfer/recipient";
    }
	
	@RequestMapping(value = "/recipient/list", method = RequestMethod.POST)
    public List<Recipient> recipientList(@RequestBody Map<String,String> json){

		Long userId=Long.parseLong(json.get("userId"));
        List<Recipient> recipientList = transactionService.findRecipientList(userId);


        return recipientList;
    }
	@RequestMapping(value = "/recipient/edit", method = RequestMethod.POST)
    public List<Recipient> recipientEdit(@RequestBody RecipientDetails recipientDetails){
		
	   Recipient recipient = recipientDetails.getRecipient();
	   Long userId=Long.parseLong(recipientDetails.getUserId());
	   User user=userService.findByUserId(userId);
	   recipient.setUser(user);
	   transactionService.saveRecipient(recipient);
       List<Recipient> recipientList = transactionService.findRecipientList(userId);
       return recipientList;
    }
	
	@RequestMapping(value = "/recipient/delete", method = RequestMethod.POST)
    @Transactional
    public List<Recipient> recipientDelete(@RequestBody Map<String,String> json){
		
		Long userId = Long.parseLong(json.get("userId"));
		Long recipientId = Long.parseLong(json.get("recipientId"));
        transactionService.deleteRecipientById(recipientId);

        List<Recipient> recipientList = transactionService.findRecipientList(userId);

        return recipientList;
    }
	
	@RequestMapping(value = "/toSomeoneElse",method = RequestMethod.POST)
    public String toSomeoneElsePost(@RequestBody Map<String,String> json) {
        Long userId=Long.parseLong(json.get("userId"));
        Long recipientId=Long.parseLong(json.get("recipientId"));
        String amount =json.get("amount");
        String accountType=json.get("accountType");
		User user = userService.findByUserId(userId);
        Recipient recipient = transactionService.findRecipientById(recipientId);
        return transactionService.toSomeoneElseTransfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingsAccount());

        
	}	
}
