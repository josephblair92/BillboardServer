package edu.wm.billboardserver.dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.wm.billboardserver.HomeController;

public class PostgresDAO {

	public static Connection establishConnection() {
		// TODO Auto-generated method stub
		 
		try {
 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			HomeController.logger.info("Failed to load PostgreSQL JDBC Driver");
			e.printStackTrace();
 
		}
		
		Connection connection = null;
		 
		try {
 
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/BILLBOARD", "postgres", "chipman");
 
		} catch (SQLException e) {
 
			HomeController.logger.info("Connection to database failed");
			e.printStackTrace();
 
		}
 
		if (connection != null) {
			HomeController.logger.info("Database connection successful");
		} else {
			HomeController.logger.info("Connection to database failed");
		}
		
		return connection;
	}
	
	public static ResultSet execQuery(Connection con, String query) {
		Statement stmt = null;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			HomeController.logger.info(query);
			rs = stmt.executeQuery(query);
		}
		catch (SQLException e) {
			HomeController.logger.info("Query failed: " + e.getMessage());
			rs = null;
		}
		
	    //try { if (stmt != null) stmt.close(); } catch (Exception e1) {};
	    //try { if (con != null) con.close(); } catch (Exception e2) {};
		return rs;

		
	}
	
	public static boolean execUpdate(Connection con, String query) {
		Statement stmt = null;
		boolean returnVal;
		
		try {
			stmt = con.createStatement();
			HomeController.logger.info(query);
			stmt.executeUpdate(query);
			returnVal = true;
		}
		catch (SQLException e) {
			HomeController.logger.info("Query failed: " + e.getMessage());
			returnVal = false;
		}
		
	    //try { if (stmt != null) stmt.close(); } catch (Exception e1) {};
	    //try { if (con != null) con.close(); } catch (Exception e2) {};
	    return returnVal;

		
	}

}