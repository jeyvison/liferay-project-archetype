package com.liferay.project;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationServlet extends HttpServlet {

    @Override
    public void service(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws IOException, ServletException {

        try {
            String method = httpServletRequest.getMethod();

            if (method.equals("GET")) {
                String username = httpServletRequest.getParameter("username");
                String password = httpServletRequest.getParameter("password");

                long userId = getUserId(username, password);

                if (userId > 0) {
                    PrintWriter printWriter = httpServletResponse.getWriter();

                    printWriter.print(getMessages());
                }
            }
            else if (method.equals("POST")) {
                String username = httpServletRequest.getParameter("username");
                String password = httpServletRequest.getParameter("password");

                long userId = getUserId(username, password);

                if (userId > 0) {
                    PrintWriter printWriter = httpServletResponse.getWriter();

                    printWriter.print(
                            addMessage(
                                    userId,
                                    httpServletRequest.getParameter("message")));
                }
            }
            else if (method.equals("DELETE")) {
                String username = httpServletRequest.getParameter("username");
                String password = httpServletRequest.getParameter("password");

                long userId = getUserId(username, password);

                if (userId > 0) {
                    deleteMessage(
                            Long.valueOf(
                                    httpServletRequest.getParameter("messageId")));
                }
            }
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }
    }

    protected long addMessage(long userId, String body) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "root", "pa55w0rd");

            Statement stmt = connection.createStatement();

            stmt.executeUpdate(
                    "INSERT INTO message (userId, body) VALUES(" + userId + ",'" +
                            body + "'");

            ResultSet rs = stmt.executeQuery(
                    "SELECT messsageId FROM message WHERE userId=" + userId +
                            " AND body=" + body);

            while (rs.next()) {
                return rs.getLong(1);
            }

            connection.close();
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }

        return -1;
    }

    protected void deleteMessage(long messageId) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "root", "pa55w0rd");

            Statement stmt = connection.createStatement();

            stmt.executeUpdate("DELETE FROM message WHERE id=" + messageId);

            connection.close();
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }
    }

    protected String getMessages() {
        String messages = "[";

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "root", "pa55w0rd");

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT messageId, userId, body, date FROM message");

            while (rs.next()) {
                if (messages.length() > 1) {
                    messages += ",";
                }

                messages += "{";
                messages += "\"messageId\": " + rs.getLong(1) + ",";
                messages += "\"userId\": " + rs.getLong(2) + ",";
                messages += "\"body\": " + rs.getString(3) + ",";
                messages += "\"date\": " + rs.getDate(4);
                messages += "}";
            }

            connection.close();
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }

        return messages += "]";
    }

    protected long getUserId(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "root", "pa55w0rd");

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT userId FROM user WHERE username=" + username +
                            " AND password=" + password);

            while (rs.next()) {
                return rs.getLong(1);
            }

            connection.close();
        }
        catch (Exception exception) {
            _log.error(exception, exception);
        }

        return -1;
    }

    private static final Log _log = LogFactory.getLog(ApplicationServlet.class);

    //Code for access counter by user ---------------------



}