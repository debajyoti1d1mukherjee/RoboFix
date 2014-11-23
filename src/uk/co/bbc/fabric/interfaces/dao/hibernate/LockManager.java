package uk.co.bbc.fabric.interfaces.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LockManager {
	
	public static boolean lock(){
		Connection con = ConnectionManager.getConnection();
		ResultSet rs=null;
		String lockVal=null;
		try {
			rs = con.createStatement().executeQuery("select * from lock");
			if(rs !=null){
				while (rs.next()) {
					lockVal = rs.getString(1);
					System.out.println("Lock Val: " + lockVal);
					break;
				}
			}
			if(rs!=null){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		 try {
			 if("OFF".equalsIgnoreCase(lockVal)){
				con.createStatement().executeUpdate("update lock set lockvalue='ON' where lockvalue='OFF'");
				return true;
			 } 
		 }
		 catch (SQLException e) {
				e.printStackTrace();
		 }finally{
			 if(con !=null){
					try {
						con.close();
					} catch (SQLException e) {
						return false;
					}
				}
		 }
		 
		return false;
	}
	
	public static boolean unlock(){
		Connection con = ConnectionManager.getConnection();
		try {
			con.createStatement().executeUpdate("update lock set lockvalue='OFF'");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(con !=null){
					con.close();
				} 
			}catch (SQLException e) {
				return false;
			}
		 }
		return false;
	}
}
