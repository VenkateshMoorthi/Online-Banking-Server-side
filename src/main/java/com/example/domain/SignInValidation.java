package com.example.domain;

public class SignInValidation {
	
	boolean invlalidCredentials=true;
	String token=null;
	long userId;
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public boolean isInvlalidCredentials() {
		return invlalidCredentials;
	}
	public void setInvlalidCredentials(boolean invlalidCredentials) {
		this.invlalidCredentials = invlalidCredentials;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
