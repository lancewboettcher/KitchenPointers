package com.kitchenpointers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCConnect {

	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
    	  		conn = DriverManager.getConnection("jdbc:mysql://aac3cbjoeutp2a.czmvfbntc6xb.us-west-2.rds.amazonaws.com:3306/kitchenPointers?user=kitchenpointers&password=fkurfess");
		} catch (SQLException ex) {ex.printStackTrace();}

		return conn;
	}
}
