package com.utd.db.libraryGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.utd.db.LibraryDB.DbConnection;

@SuppressWarnings("serial")
public class Fine extends JPanel implements ActionListener{
	
	private JPanel up,down;
	private JButton refresh,showpaid,showunpaid,displayoncard;
	private DefaultTableModel tableModel;
	private JTable table;
	private DbConnection db;
	private Connection con;
	private ResultSet rs,rs_inner;
	private JPanel dialogPanel;
	private JLabel number;
	private JTextField dialogField;

	public Fine(){
		
		tableModel = new DefaultTableModel(new Object[] {"Borrower's Name","Card Number", "Unpaid Fine Amount($)" ,"Paid Fine Amount($)"}, 0);
		table = new JTable(tableModel);
		JScrollPane myPanel = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		table.setPreferredScrollableViewportSize(new Dimension(400,300));
		
		up = new JPanel();
		down = new JPanel();
		
		up.setLayout(new BorderLayout());
		down.setLayout(new FlowLayout());
		
		refresh = new JButton("Show/Refresh Fines");
		refresh.addActionListener(this);
		showpaid = new JButton("Show paid fines");
		showpaid.addActionListener(this);
		showunpaid = new JButton("Show unpaid fines");
		showunpaid.addActionListener(this);
		displayoncard = new JButton("Search Individual Borrower Fine");
		displayoncard.addActionListener(this);
		
		down.add(displayoncard);
		down.add(Box.createHorizontalStrut(10));
		down.add(refresh);
		down.add(Box.createHorizontalStrut(10));
		
		down.add(showpaid);
		down.add(Box.createHorizontalStrut(10));
		down.add(showunpaid);
		down.add(Box.createHorizontalStrut(10));
		
		JLabel label = new JLabel("View Fines",SwingConstants.CENTER);
		label.setFont(new Font("Sans Serif", Font.BOLD, 18));
		up.add(label,BorderLayout.NORTH);
		up.add(Box.createHorizontalStrut(100),BorderLayout.EAST);
		up.add(Box.createHorizontalStrut(100),BorderLayout.WEST);
		up.add(myPanel,BorderLayout.CENTER);
		up.add(down, BorderLayout.SOUTH);
		add(up);
	}
	
	public void show_fines(String cardnumber){
		try{
		clearTable();
		db = new DbConnection();
		con = db.getConnection();
		UpdateFines.updateFineTable();
		tableModel.setColumnIdentifiers(new Object[] {"Borrower's Name","Card Number", "Unpaid Fine Amount($)" ,"Paid Fine Amount($)"});
		String having = " HAVING book_loans.card_no =" + cardnumber +";";
		String query;
		if(cardnumber.equals("display")){
		query = "select fname,lname,paid,book_loans.card_no from borrower,book_loans,fines where fines.loan_id= book_loans.loan_id AND book_loans.card_no = borrower.card_no GROUP BY card_no;";
		}
		else{
			query = "select fname,lname,paid,book_loans.card_no from borrower,book_loans,fines where fines.loan_id= book_loans.loan_id AND book_loans.card_no = borrower.card_no GROUP BY card_no" + having +";";
		}
		PreparedStatement pstat = con.prepareStatement(query);
		rs = pstat.executeQuery();
		if(!rs.isBeforeFirst()){
			JOptionPane.showMessageDialog(null, "No Results Found");
			return;
		}
						
		while (rs.next()) {
			String card="",lname="",fname="";
			Vector<String> v = new Vector<String>();
			lname = rs.getString("lname");
			fname=rs.getString("fname");
			card = rs.getString("book_loans.card_no");
			
			//System.out.println(card + fname + lname +v.size());
				query = "select SUM(fine_amt) as sum,paid from borrower,book_loans,fines where fines.loan_id= book_loans.loan_id AND book_loans.card_no = borrower.card_no AND book_loans.card_no = ? AND paid=0;";
				pstat = con.prepareStatement(query);
				pstat.setString(1,card);
				rs_inner = pstat.executeQuery();
				while(rs_inner.next()){
					
					
					v.add( fname+ " " +lname );
					v.add(card);
					String amt = rs_inner.getString("sum"); 
					if(amt==null){
						v.add("0.00");
					}
					else{
						v.add(amt);
					}
					
				}
				query = "select SUM(fine_amt) as sum,paid from borrower,book_loans,fines where fines.loan_id= book_loans.loan_id AND book_loans.card_no = borrower.card_no AND book_loans.card_no = ? AND paid=1;";
				pstat = con.prepareStatement(query);
				pstat.setString(1,card);
				rs_inner = pstat.executeQuery();
				while(rs_inner.next()){
					
					String amt = rs_inner.getString("sum"); 
					if(amt==null){
						v.add("0.00");
					}
					else{
						v.add(amt);
					}
					
				}
				tableModel.addRow(v);
			}
		con.close();
		//JOptionPane.showMessageDialog(null,"Fines Updated");
		}
		catch(SQLException exp){
			JOptionPane.showMessageDialog(null, "Error:" + exp.getMessage());
		}
	}

@Override
public void actionPerformed(ActionEvent e) {
	try{
		
		//System.out.println("Database connected");
		if(e.getSource() == refresh){
				show_fines("display");
				
			}
		else if(e.getSource()==displayoncard){
			dialogPanel = new JPanel();
			JLabel labeldialog = new JLabel("");
			number = new JLabel("Enter Card No");
			dialogPanel.setLayout(new FlowLayout());
			dialogField = new JTextField(4);
			dialogPanel.add(number);
			dialogPanel.add(dialogField);
			dialogPanel.add(labeldialog);
			int selection = JOptionPane.showConfirmDialog(null, dialogPanel, "Card No Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		    if (selection == JOptionPane.OK_OPTION)
		    {
		        if(dialogField.getText().isEmpty()){
		        	//do nothing
		        }
		        else{
		        	String number = dialogField.getText();
		        	show_fines(number);
		        }
		    	
		        
		    }
		    else if (selection == JOptionPane.CANCEL_OPTION)
		    {
		        // Do nothing
		    }
			
			
		}
		else if(e.getSource() == showpaid){
			db = new DbConnection();
			con = db.getConnection();
			clearTable();
			tableModel.setColumnIdentifiers(new Object[] {"Borrower Name","Card No", "Fine Amount($)" ,"Fine Paid"});
			//System.out.println("Database connected");
			String query = "select book_loans.card_no as card,fname,lname,SUM(fine_amt) as sum,book_loans.card_no from borrower,book_loans,fines where fines.loan_id= book_loans.loan_id AND book_loans.card_no = borrower.card_no AND paid=1 GROUP BY card_no;";
			PreparedStatement pstat = con.prepareStatement(query);
			rs = pstat.executeQuery();
			if(!rs.isBeforeFirst()){
				JOptionPane.showMessageDialog(null, "No Results Found");
				return;
			}
			while (rs.next()) {
					Vector<String> v = new Vector<String>();
					v.add(rs.getString("fname") + " " + rs.getString("lname"));
					v.add(rs.getString("card"));
					v.add(rs.getString("sum"));
					v.add("YES");
					tableModel.addRow(v);
				}
			con.close();
		}
		else if(e.getSource() == showunpaid){
			db = new DbConnection();
			con = db.getConnection();
			clearTable();
			tableModel.setColumnIdentifiers(new Object[] {"Borrower Name","Card No", "Fine Amount($)" ,"Fine Paid"});
			//System.out.println("Database connected");
			String query = "select book_loans.card_no as card,fname,lname,SUM(fine_amt) as sum,book_loans.card_no from borrower,book_loans,fines where fines.loan_id= book_loans.loan_id AND book_loans.card_no = borrower.card_no AND paid=0 GROUP BY card_no;";
			PreparedStatement pstat = con.prepareStatement(query);
			rs = pstat.executeQuery();
			if(!rs.isBeforeFirst()){
				JOptionPane.showMessageDialog(null, "No Results Found");
				return;
			}
			while (rs.next()) {
					Vector<String> v = new Vector<String>();
					v.add(rs.getString("fname") + " " + rs.getString("lname"));
					v.add(rs.getString("card"));
					v.add(rs.getString("sum"));
					v.add("NO");
					tableModel.addRow(v);
				}
			con.close();
		}
		else{
			
		}
		
	}
		catch(SQLException exp){
			JOptionPane.showMessageDialog(null, "Error:" + exp.getMessage());
		}
		
	}
public void clearTable(){
	tableModel.setRowCount(0);
	
}

}
