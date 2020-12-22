package com.liferay.project.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.liferay.project.data.DAO;
import com.liferay.project.data.valueobject.ValueObject;
import com.liferay.project.data.valueobject.message.VOMessage;
import com.liferay.project.data.valueobject.user.VOUser;

public class DAODB implements DAO {
	
	private Connection getConnection() throws SQLException {
		Connection connection = null;		
		
		connection = DriverManager.getConnection(
		        "jdbc:mysql://localhost:3306/app", "root", "pa55w0rd");		
		
		return connection;
	}
	
	private PreparedStatement getPreparedStatement(Connection connection, String sql, ValueObject vo) throws SQLException {		
		PreparedStatement stmt = null;
		
		if (connection != null && sql != null && !sql.isEmpty()) {			
			stmt = connection.prepareStatement(sql);
			
			int index = 1;
			
			for (String column : vo.getColumnsNames()) {
				stmt.setObject(index, vo.getColumnValue(column));
				index++;
			}			
		}
		
		return stmt;
	}
	
	private void execute(Connection connection, PreparedStatement stmt) throws SQLException {
		try {
			stmt.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			closeResources(connection, stmt, null);
		}		
		
	}
	
	private void closeResources(Connection connection, PreparedStatement stmt, ResultSet rs) {
		//Closes resources in the right sequence		
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			if (stmt != null && !stmt.isClosed()) {
				stmt.close();							
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			if (connection != null && !connection.isClosed()) {				
				connection.close();				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Possible to add any Entity, but just the ones allowed are exposed in interface
	private void add(ValueObject vo) throws SQLException {
		//Uses StringBuilder to avoid creating many unnecessary String in memory
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(vo.getEntityName()).append(" (");
		
		String connector = "";
		
		for (String columnName : vo.getColumnsNames()) {
			sql.append(connector).append(columnName);
			connector = ", ";
		}
		
		sql.append(") VALUES (");
		
		connector = "";
		
		for (int i = 0; i < vo.getColumnsNames().size(); i++) {
			sql.append(connector).append("?");
			connector = ", ";
		}
		
		sql.append(")");
		
		Connection connection = getConnection();
		PreparedStatement stmt = getPreparedStatement(connection, sql.toString(), vo);
		
		execute(connection, stmt);
	}

	//Possible to delete any Entity, but just the ones allowed are exposed in interface
	private void delete(ValueObject vo) throws SQLException {
		//Uses StringBuilder to avoid creating many unnecessary String in memory
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(vo.getEntityName()).append(" WHERE ");
		
		String connector = "";
		
		for (String columnName : vo.getColumnsNames()) {
			sql.append(connector).append(columnName).append(" = ?");
			connector = " AND ";
		}		
		
		Connection connection = getConnection();
		PreparedStatement stmt = getPreparedStatement(connection, sql.toString(), vo);		
		stmt.execute();
		
		closeResources(connection, stmt, null);
	}
	
	//Possible to query any Entity, but just the ones allowed are exposed in interface
	@SuppressWarnings("unchecked")
	private <E extends ValueObject> ArrayList<E> query(E vo) throws SQLException, ReflectiveOperationException {
		ArrayList<E> result = new ArrayList<E>();
		
		//Uses StringBuilder to avoid creating many unnecessary String in memory
		StringBuilder sql = new StringBuilder();
		//It is possible to improve to select only the selected columns, gaining some time here =) 
		sql.append("SELECT * FROM ").append(vo.getEntityName());
		
		if (!vo.isEmpty()) {		
			sql.append(" WHERE ");
				
			String connector = "";
			
			for (String columnName : vo.getColumnsNames()) {
				sql.append(connector).append(columnName).append(" = ?");
				connector = " AND ";
			}		
		}
		
		Connection connection = getConnection();
		PreparedStatement stmt = getPreparedStatement(connection, sql.toString(), vo);
		
		ResultSet rs = stmt.executeQuery();
		ResultSetMetaData metadata = rs.getMetaData();
		
		E row = null;
				
		while (rs.next()) {
			row = (E) vo.getClass().getConstructors()[0].newInstance();
			
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				//For some databases is needed to adjust the conversion of some database types for java types
				//Assuming the simple types, CLOB, BLOB, etc. are not supported in this example
				row.setColumn(metadata.getColumnName(i), rs.getObject(i));
			}
			
			result.add(row);
        }        
		
		closeResources(connection, stmt, rs);
		
		return result;
	}
	
	@Override
	//Expose this entity to be allowed to add
	public void addMessage(VOMessage voMessage) throws SQLException {
		add(voMessage);
	}

	@Override
	//Expose this entity to be allowed to add
	public void deleteMessage(VOMessage voMessage) throws SQLException {
		delete(voMessage);		
	}

	@Override
	//Expose this entity to be allowed to query
	public ArrayList<VOMessage> getMessages(VOMessage voMessage) throws SQLException, ReflectiveOperationException {
		return query(voMessage);
	}

	@Override
	public Long getUserId(VOUser voUser) throws SQLException, ReflectiveOperationException {
		Long id = -1L;
		
		ArrayList<VOUser> results = query(voUser);
		
		if (!results.isEmpty()) {
			id = results.get(0).getUserId();
		}
		
		return id;
	}
			
}
