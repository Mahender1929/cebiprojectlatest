package com.cebi.utility;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {

	public static Connection getConnection() {
		Connection conn = null;

		String url = "jdbc:mysql://10.47.10.16:3306/cebidb";
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, "cebidevusr", "Admin@123");

		} catch (Exception e) {
		}
		return conn;
	}

}
