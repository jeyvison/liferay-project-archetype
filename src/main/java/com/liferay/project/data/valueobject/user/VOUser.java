package com.liferay.project.data.valueobject.user;

import com.liferay.project.data.valueobject.ValueObject;

public class VOUser extends ValueObject {

	@Override
	public String getEntityName() {		
		return "user";
	}
	
	public Long getUserId() {
		return (Long) super.getColumnValue("userId");
	}
	
	public void setUserId(Long userId) {
		super.setColumn("userId", userId);
	}

	public String getUserName() {
		return (String) super.getColumnValue("userName");
	}
	
	public void setUserName(String userName) {
		super.setColumn("userName", userName);
	}
	
	public String getPassword() {
		return (String) super.getColumnValue("password");
	}
	
	public void setPassword(String password) {
		super.setColumn("password", password);
	}

}
