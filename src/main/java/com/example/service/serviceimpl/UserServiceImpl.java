package com.example.service.serviceimpl;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.domain.User;
import com.example.domain.security.UserRole;
import com.example.service.AccountService;
import com.example.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired 
	private RoleDao roleDao;
	
	@Autowired
	private AccountService accountService;

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public boolean checkUserExists(String username, String email) {
		if(checkUsernameExists(username)||checkEmailExists(email)){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkUsernameExists(String username) {
		if(null!=findByUsername(username)){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkEmailExists(String email) {
		if(null!=findByEmail(email)){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public void saveUser(User user,Set<UserRole> userRoles) {
		
		for(UserRole ur: userRoles){
			roleDao.save(ur.getRole());
		}
		
		user.setPrimaryAccount(accountService.createPrimaryAccount());
		user.setSavingsAccount(accountService.createSavingsAccount());
		user.getUserRoles().addAll(userRoles);
		userDao.save(user);
	}

	@Override
	public User findByUserId(Long userId) {
		return userDao.findOne(userId);
	}

	
	
}
