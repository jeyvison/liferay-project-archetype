package com.liferay.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.project.data.DAO;
import com.liferay.project.data.DAOFactory;
import com.liferay.project.data.valueobject.message.VOMessage;
import com.liferay.project.data.valueobject.user.VOUser;

public class ApplicationServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Hashtable<Long, Long> accessCounter = new Hashtable<Long, Long>();
	
	private final DAO dao;
	
	public ApplicationServlet() throws ReflectiveOperationException {
		dao = DAOFactory.getDAO(DAOFactory.DAOType.DB);
	}

    @Override
    public void service(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws IOException, ServletException {    	

        try {
        	long userId = readRequestUserParameters(httpServletRequest);
        	
        	if (userId > 0) {
        		
        		count(userId);
        		
	            String method = httpServletRequest.getMethod();
	            
	            PrintWriter printWriter = httpServletResponse.getWriter();
	
	            if (method.equals("GET")) {
	            	printWriter.print(dao.getMessages(new VOMessage()));	                
	            }
	            else if (method.equals("POST")) {
	            	//Do the business using the DAO separately
	            	
	            	VOMessage voMessage = new VOMessage();
	            	voMessage.setUserId(userId);
	            	voMessage.setBody(httpServletRequest.getParameter("message"));
	            	
	            	dao.addMessage(voMessage);
	            	
	            	//Suposing that user and body are not primary keys, you may have many equal messages with different IDs (that seem to be the primary key)
	            	ArrayList<VOMessage> result = dao.getMessages(voMessage);
	            	
	            	for (VOMessage voMessageResult : result) {
	            		printWriter.print(voMessageResult.getMessageId());
	            	}
	                
	            }
	            else if (method.equals("DELETE")) {
	            	VOMessage voMessage = new VOMessage();
	            	voMessage.setMessageId(Long.valueOf(httpServletRequest.getParameter("messageId")));
	            	
                    dao.deleteMessage(voMessage);
	            }
        	}
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }
    }
    
    private synchronized void count(Long userId) {
    	if (accessCounter.contains(userId)) {
   			
			Long counter = accessCounter.get(userId);
			
			counter = counter++;
			
			accessCounter.put(userId, counter);
		} else {
			accessCounter.put(userId, 1L);
		}
    }
    
    protected long readRequestUserParameters(HttpServletRequest httpServletRequest) {
    	long userId = 0;
    	
    	String method = httpServletRequest.getMethod();
    	
    	if (method.equals("GET") || method.equals("POST") || method.equals("DELETE")) {
    		String username = httpServletRequest.getParameter("username");
            String password = httpServletRequest.getParameter("password");

            userId = getUserId(username, password);
    	}
    	
    	return userId;
    }  

    protected String getMessages() {
        String strMessages = "[";
        String connector = "";
        
        try {
        	ArrayList<VOMessage> messages = dao.getMessages(new VOMessage());        	
        	
        	for (VOMessage message : messages) {
        		strMessages += connector + "{";
        		strMessages += "\"messageId\": " + message.getMessageId() + ",";
        		strMessages += "\"userId\": " + message.getUserId() + ",";
        		strMessages += "\"body\": " + message.getBody() + ",";
        		strMessages += "\"date\": " + message.getDate().toString();
        		strMessages += "}";
        		
        		connector = ";";
        	}

        	strMessages += "]";
        } catch (Exception exception) {
            _log.error(exception, exception);
        }

        return strMessages;
    }

    protected long getUserId(String username, String password) {
    	Long id = -1L;
    	
    	try {
    		VOUser user = new VOUser();
    		user.setUserName(username);
    		user.setPassword(password);
    		
           id = dao.getUserId(user);
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }

        return id;
    }

    private static final Log _log = LogFactory.getLog(ApplicationServlet.class);

    //Code for access counter by user ---------------------



}