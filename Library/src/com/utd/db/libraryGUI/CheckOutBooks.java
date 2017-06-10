package com.utd.db.libraryGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.utd.db.LibraryDB.DbConnection;

@SuppressWarnings("serial")
public class CheckOutBooks extends JPanel implements ActionListener {

	private JTextField card_no, branch_id, book_id;
	private JLabel label1, label2, label3;
	private JButton button,reset_button;
	private JPanel parent_home;
	private Connection con;
	private DbConnection db;
	
	public CheckOutBooks() {
	
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		panel1.setLayout(new FlowLayout());
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new FlowLayout());

		label1 = new JLabel("Book ID           ");
		book_id = new JTextField(20);
		panel1.add(label1);
		panel1.add(book_id);
		
		label2 = new JLabel("Branch ID       ");
		branch_id = new JTextField(20);
		panel2.add(label2);
		panel2.add(branch_id);

		label3 = new JLabel("Card Number");
		card_no = new JTextField(20);
		panel3.add(label3);
		panel3.add(card_no);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		button = new JButton("Check Out");
		button.addActionListener(this);
		reset_button = new JButton("Reset");
		reset_button.addActionListener(this);
		
		c.gridx = 0;
		c.gridy = 1;
		panel4.add(panel1,c);
		c.gridx = 0;
		c.gridy = 2;
		panel4.add(panel2,c);
		c.gridx = 0;
		c.gridy = 3;
		panel4.add(panel3,c);
		c.gridx = 0;
		c.gridy = 4;
		panel4.add(button,c);
		
		JPanel bpanel = new JPanel();
		bpanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 1;
		bpanel.add(button,c);
		
		c.gridx = 0;
		c.gridy = 2;
		bpanel.add(reset_button,c);
		c.gridx = 0;
		c.gridy = 5;
		panel4.add(bpanel, c);
		add(panel4,BorderLayout.CENTER);
		JLabel heading = new JLabel("Check Out Book",SwingConstants.CENTER);
		heading.setFont(new Font("Sans Serif", Font.BOLD, 18));
		add(heading,BorderLayout.NORTH);

	}
	public void setFields(String book,String branch){
		branch_id.setText(branch);
		book_id.setText(book);
		card_no.setText("");
	}
	public void resetFields(){
		branch_id.setText("");
		book_id.setText("");
		card_no.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String branch,book,card,date_out,due_date;
		ResultSet rs;
		Calendar cal;
		int loan_count=0,no_copies=0,loan_copies=0,fines=0;
		double a=0,b=0,c=0;
		
		if(e.getSource()==button){
			branch = branch_id.getText().trim();
			book = book_id.getText().trim();
			card = card_no.getText().trim();
			
			if(branch.isEmpty() || book.isEmpty() || card.isEmpty()){
				JOptionPane.showMessageDialog(null, "Please enter all the Details");
			}
			else{
				try{
					//check if value entered is all integer type i.e valid data
					a = Double.parseDouble(book);
					b = Double.parseDouble(branch);
					c = Double.parseDouble(card);
				}
				catch(NumberFormatException exp){
					JOptionPane.showMessageDialog(null, "Please enter valid data" + exp.getMessage());
					return;
				}
				try{
					db = new DbConnection();
					con = db.getConnection();					
					//check if card no , book_id and branch id exists
					String query="SELECT *  from book WHERE book_id = ?";
					PreparedStatement pstat = con.prepareStatement(query);
					pstat.setString(1, book);
					rs=pstat.executeQuery();
					while (!rs.isBeforeFirst()) {

						JOptionPane.showMessageDialog(null, "Book Id does not exist");
						return;

					}
					
					query="SELECT * from borrower WHERE card_no = ?";
					pstat = con.prepareStatement(query);
					pstat.setString(1, card);
					rs=pstat.executeQuery();
					while (!rs.isBeforeFirst()) {
						JOptionPane.showMessageDialog(null, "Borrower Not Registered");
						return;
					}
					
					query="SELECT * from library_branch WHERE branch_id = ?";
					pstat = con.prepareStatement(query);
					pstat.setString(1, branch);
					rs=pstat.executeQuery();
					while (!rs.isBeforeFirst()) {

						JOptionPane.showMessageDialog(null, "Branch does not exist");
						return;

					}
					
					//get number of loans for a particular borrower
					query="SELECT COUNT(*) as count from book_loans WHERE card_no = ? and date_in IS NULL";
					pstat = con.prepareStatement(query);
					pstat.setString(1, card);
					rs=pstat.executeQuery();
					while (rs.next()) {

						loan_count = rs.getInt("count");

					}
					
					//get available copies  at a particular branch
					query = "select no_of_copies from book_copies where branch_id =? and book_id =?;";
					pstat = con.prepareStatement(query);
					pstat.setInt(1, Integer.parseInt(branch));
					pstat.setString(2,book);
					rs = pstat.executeQuery();
					while(rs.next()){
						no_copies = rs.getInt("no_of_copies");
					}
					
					query = "select count(*) as count from book_loans where branch_id =? and book_id =? and date_in IS NULL;";
					pstat = con.prepareStatement(query);
					pstat.setInt(1, Integer.parseInt(branch));
					pstat.setString(2,book);
					rs = pstat.executeQuery();
					while(rs.next()){
						loan_copies = rs.getInt("count");
					}
					
					query = "select count(*) as count from book_loans, fines where book_loans.loan_id = fines.loan_id AND card_no = ? AND date_in IS NOT NULL AND paid = 0;";
					pstat = con.prepareStatement(query);
					pstat.setInt(1, Integer.parseInt(card));
					rs = pstat.executeQuery();
					while(rs.next()){
						fines = rs.getInt("count");
					}
					
					if(loan_count<3 && (loan_copies < no_copies) && fines==0){
					
							query = "INSERT INTO book_loans(book_id,branch_id,card_no,date_out,due_date) VALUES(?,?,?,?,?)";
							pstat = con.prepareStatement(query);
							pstat.setString(1, book);
							pstat.setString(2, branch);
							pstat.setString(3, card);
							
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date = new Date();
							date_out = dateFormat.format(date);
							pstat.setString(4, date_out);
							
							cal = Calendar.getInstance();
							cal.setTime(date);
							cal.add(Calendar.DATE, 14);
							due_date = dateFormat.format(cal.getTime());
							pstat.setString(5, due_date);
							
							pstat.executeUpdate();
							JOptionPane.showMessageDialog(null, "Book Checked out");
						}
					else{
						if(loan_count >= 3){
							JOptionPane.showMessageDialog(null, "Borrower cannot check out more than 3 books");
						}
						else if(loan_copies >= no_copies){
							JOptionPane.showMessageDialog(null, "No Copies available at this branch");
						}
						else if(fines>0){
							JOptionPane.showMessageDialog(null, "Cannot check out a book.You have oustanding fines to settle");
						}
					}
					
					con.close();
				}
				catch(SQLException r){
					JOptionPane.showMessageDialog(null, r.getMessage());
				}
			}
		}
		else if(e.getSource()==reset_button){
			resetFields();
		}

	}

}
