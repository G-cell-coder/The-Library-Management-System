package com.utd.db.LibraryDB;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConnection {
	
	private final String url = "jdbc:mysql://localhost:3306/";
	private String username = "",password= "";
	
	public void getProperties(){
		try{
		Properties props = new Properties();
		InputStream input = new FileInputStream("/db.properties");
		props.load(input);
		username = props.getProperty("user");
		password = props.getProperty("password");
		System.out.println(username + password);
		}
		catch(IOException e){
			System.out.println("Exception In I/O : " + e.getMessage());
		}
	}
	
	public Connection getConnection(){
		Connection conn = null;
		//getProperties();
		try { 
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			conn = DriverManager.getConnection(url+"library","root","root"); 
			} 
		catch (Exception e) 
		{ 
			e.printStackTrace();
			}
		return conn;
	}

}
