package com.example.service;

import java.util.Set;

import com.example.domain.User;
import com.example.domain.security.UserRole;

public interface UserService {
	
	User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
    
	void saveUser(User user, Set<UserRole> userRoles);
	
	User findByUserId(Long userId);
}
