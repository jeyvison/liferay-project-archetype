package com.liferay.project.data.valueobject.message;

import java.sql.Date;

import com.liferay.project.data.valueobject.ValueObject;

public class VOMessage extends ValueObject {
	
	public String getEntityName() {
		return "message";
	}
	
	public Long getMessageId() {
		return (Long) super.getColumnValue("messageId");
	}
	
	public void setMessageId(Long messageId) {
		super.setColumn("messageId", messageId);
	}

	public Long getUserId() {
		return (Long) super.getColumnValue("userId");
	}
	
	public void setUserId(Long userId) {
		super.setColumn("userId", userId);
	}
	
	public String getBody() {
		return (String) super.getColumnValue("body");
	}
	
	public void setBody(String body) {
		super.setColumn("body", body);
	}
	
	public Date getDate() {
		return (Date) super.getColumnValue("date");
	}
	
	public void setDate(String date) {
		super.setColumn("date", date);
	}
}
