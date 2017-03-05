package com.example.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.RoleDao;
import com.example.domain.SignInValidation;
import com.example.domain.SignupValidation;
import com.example.domain.User;
import com.example.domain.security.UserRole;
import com.example.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	private UserService userService;

	private long EXPIRATIONTIME = 1000 * 60 * 60 * 24 * 10; // 10 days
    private String secret = "ThisIsASecret";
	
	
    @RequestMapping("/users") /* Maps to all HTTP actions by default (GET,POST,..)*/
    public @ResponseBody
    String getUsers() {
        return "{\"users\":[{\"firstname\":\"Richard\", \"lastname\":\"Feynman\"}," +
            "{\"firstname\":\"Marie\",\"lastname\":\"Curie\"}]}";
    }
    
    
    @RequestMapping("/login")
    public SignInValidation createPerson(@RequestBody Map<String, String> json) {
    	User user = new User();
    	SignInValidation  signinValidation = new SignInValidation();
    	
    	String username=json.get("username"); 
    	String password=json.get("password");
   
    	user=userService.findByUsername(username);
    	if(user==null){
    		signinValidation.setInvlalidCredentials(true);
    		signinValidation.setToken(null);
    		System.out.println("invalid user");
    		return signinValidation;
    	}else if(!user.getPassword().equals(password)){
    		signinValidation.setInvlalidCredentials(true);
    		signinValidation.setToken(null);
    		System.out.println(!user.getPassword().equals(password));
    		System.out.println("invalid password "+user.getPassword()+" "+password);
    		return signinValidation;
    	}else if(user.getPassword().equals(password) && user.getUsername().equals(username)){

    		String JWT = Jwts.builder()
                .setSubject("admin")
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    		System.out.println("Success");
    		
    		signinValidation.setInvlalidCredentials(false);
    		signinValidation.setToken(JWT);
    		signinValidation.setUserId(user.getUserId());
    		return signinValidation;
    	}
    	System.out.println("not success");
    	return signinValidation;
	}
    
    @RequestMapping(value="/signup",method=RequestMethod.POST)
    public SignupValidation Signup(@RequestBody Map<String,String> user){
		
    	SignupValidation signupValidation = new SignupValidation();
    	
    	if(userService.checkEmailExists(user.get("email"))){
    		signupValidation.setEmail(true);
    	}
    	
    	if(userService.checkUsernameExists(user.get("username"))){
    		signupValidation.setUsername(true);
    	}
    	
    	if (signupValidation.isEmail()||signupValidation.isUsername()){
    		
    		return signupValidation;
    	}else{
    		User saveuser = new User();
    		
    		Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(new UserRole(saveuser, roleDao.findByName("ROLE_USER")));
    		
    		saveuser.setFirstName(user.get("firstname"));
    		saveuser.setLastName(user.get("lastname"));
    		saveuser.setEmail(user.get("email"));
    		saveuser.setPassword(user.get("password"));
    		saveuser.setPhone(user.get("phone"));
    		saveuser.setUsername(user.get("username"));
    		
        	userService.saveUser(saveuser, userRoles);
    		return signupValidation;
    	}   	
    }
    
}
