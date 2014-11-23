package uk.co.bbc.fabric.interfaces.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JavaDBDemo {
  static Connection conn;

  public static void main(String[] args) {
    String driver = "org.apache.derby.jdbc.ClientDriver";
    String connectionURL = "jdbc:derby://localhost:1527/testdb";
    String createString = "CREATE TABLE Employee (NAME VARCHAR(32) NOT NULL, ADDRESS VARCHAR(50) NOT NULL)";
    try {
      Class.forName(driver);
    } catch (java.lang.ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
		Date currentDate = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String created = df.format(currentDate);
		System.out.println(" HI :"+created);
		Date d = new Date(created);
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(d));
		conn = DriverManager.getConnection(connectionURL);
		Statement stmt = conn.createStatement();
		// stmt.executeUpdate(createString);

		PreparedStatement psInsert = conn.prepareStatement("insert into persons values (?,?,?,?,?)");

		psInsert.setInt(1, 123);
		psInsert.setString(2, "Mandal");
		psInsert.setString(3, "Nabin");
		psInsert.setString(4, "Hi");
		psInsert.setString(5, "Kol");
      
      
     // psInsert.executeUpdate();

      Statement stmt2 = conn.createStatement();
      ResultSet rs = stmt2.executeQuery("select * from persons");
      int num = 0;
      while (rs.next()) {
        System.out.println(++num + ": Name: " + rs.getString(2) + "\n Address" + rs.getString(4));
      }
      rs.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}