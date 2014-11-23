package uk.co.bbc.fabric.interfaces.dao.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
	  static Connection conn;
	  
	  public static Connection getConnection(){ 
		  String driver = OnAirDerbyDaoImpl.driver;
		  String connectionURL = OnAirDerbyDaoImpl.connectionURL;
		  try {
			  Class.forName(driver);
		  } catch (java.lang.ClassNotFoundException e) {
			  e.printStackTrace();
		  }
		  try {
			  conn = DriverManager.getConnection(connectionURL);
			  
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return conn;
	  }
}
