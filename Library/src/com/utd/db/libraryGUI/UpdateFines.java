package com.utd.db.libraryGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.utd.db.LibraryDB.DbConnection;

public class UpdateFines {
	
	private static DbConnection db;
	private static Connection conn;
	private static ResultSet rs;
	private static ResultSet rs_inner;

	public static void updateFineTable(){
		int loan_id =0;
		float amt =0.0f;
		try{
			db = new DbConnection();
			conn = db.getConnection();
			
			//System.out.println("Database connected");
			
			String query = "select loan_id , DATEDIFF(sysdate(),due_date)*0.25 as amt from book_loans where date_in IS NULL AND DATEDIFF(sysdate(),due_date) > 0;";
			PreparedStatement pstat = conn.prepareStatement(query);
			rs = pstat.executeQuery();
			while(rs.next()){
				loan_id = rs.getInt("loan_id");
				amt = rs.getFloat("amt");
				
				query = "select * from fines where loan_id = ?";
				pstat = conn.prepareStatement(query);
				pstat.setInt(1, loan_id);
				rs_inner = pstat.executeQuery();
				if(!rs_inner.isBeforeFirst()){
					query = "Insert into fines(loan_id,fine_amt,paid) values(?,?,?)";
					pstat = conn.prepareStatement(query);
					pstat.setInt(1, loan_id);
					pstat.setFloat(2, amt);
					pstat.setBoolean(3, false);
					pstat.executeUpdate();
				}
				else{
					query = "update fines set fine_amt = ? where loan_id = ?";
					pstat = conn.prepareStatement(query);
					pstat.setFloat(1, amt);
					pstat.setInt(2, loan_id);
					
					pstat.executeUpdate();
				}
			}
				
				query = "select loan_id , DATEDIFF(date_in,due_date)*0.25 as amt from book_loans where date_in IS NOT NULL AND DATEDIFF(date_in,due_date)>0 and DATEDIFF(sysdate(),due_date) > 0;";
				pstat = conn.prepareStatement(query);
				rs = pstat.executeQuery();
				while(rs.next()){
					loan_id = rs.getInt("loan_id");
					amt = rs.getFloat("amt");
					
					query = "select * from fines where loan_id = ?";
					pstat = conn.prepareStatement(query);
					pstat.setInt(1, loan_id);
					rs_inner = pstat.executeQuery();
					if(!rs_inner.isBeforeFirst()){
						query = "Insert into fines(loan_id,fine_amt,paid) values(?,?,?)";
						pstat = conn.prepareStatement(query);
						pstat.setInt(1, loan_id);
						pstat.setFloat(2, amt);
						pstat.setBoolean(3, false);
						pstat.executeUpdate();
					}
					else{
						
					}
				}
			conn.close();
			
		}
		catch(SQLException e){
			System.out.print(e.getMessage());
		}
	
	}

}

