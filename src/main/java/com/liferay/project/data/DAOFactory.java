package com.liferay.project.data;

public class DAOFactory {
	
	public enum DAOType {DB, TXT}

	public static DAO getDAO(DAOType daoType) throws ReflectiveOperationException {
		Class<?> classe = Class.forName("com.liferay.project.data." + daoType.toString().toLowerCase() + ".DAO" + daoType.toString().toUpperCase());
		
		return (DAO) classe.getConstructors()[0].newInstance();
	}
}
