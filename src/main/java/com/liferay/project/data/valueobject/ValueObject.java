package com.liferay.project.data.valueobject;

import java.util.Hashtable;
import java.util.TreeSet;

public abstract class ValueObject {
	
	private Hashtable<String, Object> columns = new Hashtable<String, Object>();
	
	public abstract String getEntityName();
	
	public TreeSet<String> getColumnsNames(){
		return new TreeSet<String>(columns.keySet());
	}
	
	public Object getColumnValue(String columnName) {
		return columns.get(columnName);
	}
	
	public void setColumn(String columnName, Object value) {
		columns.put(columnName, value);
	}
	
	public boolean isEmpty() {
		return columns.isEmpty();
	}

}
