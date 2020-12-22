package com.liferay.project.data;

import java.util.ArrayList;

import com.liferay.project.data.valueobject.message.VOMessage;
import com.liferay.project.data.valueobject.user.VOUser;

public interface DAO {
	
	public void addMessage(VOMessage voMessage) throws Exception;
	
	public void deleteMessage(VOMessage voMessage) throws Exception;
	
	public ArrayList<VOMessage> getMessages(VOMessage voMessage) throws Exception;
	
	public Long getUserId(VOUser voUser) throws Exception;
	
}
