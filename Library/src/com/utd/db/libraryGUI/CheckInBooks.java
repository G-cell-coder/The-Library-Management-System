package com.utd.db.libraryGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.utd.db.LibraryDB.DbConnection;

@SuppressWarnings("serial")
public class CheckInBooks extends JPanel implements ActionListener{

	private JPanel bottom_panel,one,two,three,center,parent_center;
	private JTable table;
	private Connection conn;
	private JButton button,search,reset;
	private JTextField textField,bookid,cardno;
	private JLabel label1,label2,label3;
	private DefaultTableModel tableModel;
	private JTextField fname;
	private ResultSet rs;
	PreparedStatement stat;
	String book_id,card_no,fName,querybuffer="";
	private JSpinner s;

	public CheckInBooks() {
		Date today = new Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
		String dtoday = formatDate.format(today);
		try {
			today = formatDate.parse(dtoday);
		} catch (ParseException e) {
		}
		s = new JSpinner(new SpinnerDateModel(today, null, null,Calendar.DATE){
			 public void setCalendarField(int field){}  
	    }); 
		JSpinner.DateEditor de = new JSpinner.DateEditor(s, "MM/dd/yyyy");
		s.setEditor(de);

		
		setLayout(new BorderLayout());
		JLabel heading = new JLabel("Check In Book",SwingConstants.CENTER);
		heading.setFont(new Font("Sans Serif", Font.BOLD, 18));
		
		one = new JPanel();
		two = new JPanel();
		three = new JPanel();
		center = new JPanel();
		parent_center = new JPanel();
		
		one.setLayout(new FlowLayout());
		two.setLayout(new FlowLayout());
		three.setLayout(new FlowLayout());
		
		label1 = new JLabel("Book Id             ");
		label2 = new JLabel("Card No             ");
		label3 = new JLabel("Borrower Name");
		
		bookid = new JTextField(20);
		fname = new JTextField(20);
		cardno = new JTextField(20);
		
		one.add(label1);
		one.add(bookid);
		
		two.add(label2);
		two.add(cardno);
		
		three.add(label3);
		three.add(fname);
		
		center.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 1;
		center.add(heading,c);
		
		c.gridx = 0;
		c.gridy = 2;
		center.add(one,c);
		
		c.gridx = 0;
		c.gridy = 3;
		center.add(two,c);
		
		c.gridx = 0;
		c.gridy = 4;
		center.add(three,c);
		
		search = new JButton("Search");
		c.gridx = 0;
		c.gridy = 5;
		center.add(search,c);
		search.addActionListener(this);

		bottom_panel = new JPanel();
		textField = new JTextField(10);
		button = new JButton("Check In Books");
		reset = new JButton("Reset");
		bottom_panel.add(new JLabel("Enter Check In Book Date(MM/DD/YYYY)*"));
		bottom_panel.add(s);
		bottom_panel.add(button);
		bottom_panel.add(reset);
		button.addActionListener(this);
		reset.addActionListener(this);
		add(bottom_panel,BorderLayout.SOUTH);
		
		tableModel = new DefaultTableModel(new Object[] {"Loan Id","Book Id","Card Number","Borrower Name", "Check out Date" ,"Due Date"}, 0);
		table = new JTable(tableModel);
		JScrollPane myPanel = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		table.setPreferredScrollableViewportSize(new Dimension(500,300));
		
		parent_center.setLayout(new BorderLayout());
		parent_center.add(center,BorderLayout.NORTH);
		parent_center.add(myPanel,BorderLayout.CENTER);
		
		add(parent_center, BorderLayout.CENTER);
		
		}
	
	public void clearTable(){
		tableModel.setRowCount(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		conn = new DbConnection().getConnection();
		if(e.getSource() == search){
			book_id = bookid.getText().trim();
			card_no = cardno.getText().trim();
			fName = fname.getText().trim();
			if(book_id.isEmpty() && card_no.isEmpty() && fName.isEmpty()){
				JOptionPane.showMessageDialog(null,"Enter atleast one of the search fields");
				return;
			}
			try{
				//check if value entered is all integer type i.e valid data
				if(card_no.length()>0 ){
				double c = Double.parseDouble(card_no);
				}
			}
			catch(NumberFormatException exp){
				JOptionPane.showMessageDialog(null, "Please enter valid card no");
				return;
			}
			querybuffer = "";
			if(!(book_id.isEmpty())){
				querybuffer=querybuffer + " and book_loans.book_id = '" + book_id + "' ";
			}
			if(!(card_no.isEmpty())){
				querybuffer=querybuffer + " and book_loans.card_no = " + card_no;
			}
			if(!(fName.isEmpty())){
				querybuffer=querybuffer + " and (LOWER(borrower.fname) like '%" + fName.toLowerCase() + "%' or LOWER(borrower.lname) like '%" + fName.toLowerCase() +"%') ";
			}
			updateTable(1);
			
		}
		else if(e.getSource()==button){
			Date d = null,currentDate;
			currentDate = new Date();
			Boolean valid = false;
			int row_selection = table.getSelectedRow();
			
			
			if(row_selection > -1){
				if(false){}
				else{
					String date;
					Object o = s.getValue();
					SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
					formatDate.setLenient(false);
					try{
						date = formatDate.format(o);
						d = formatDate.parse(date);
						valid = true;
					}
					catch(ParseException exception){
						textField.setText("");
						JOptionPane.showMessageDialog(null, "Please enter a valid date");
						return;
					}
					String date_out = table.getValueAt(row_selection, 4).toString();
					String date_due = table.getValueAt(row_selection, 5).toString();
					Date dateout = null,datedue = null;
					
					try {
						
						dateout = formatDate.parse(date_out);
						datedue = formatDate.parse(date_due);
					
						}
						catch (ParseException e1) {
						JOptionPane.showMessageDialog(null, "Error:" + e1.getMessage());
					}
					if(valid){
						if(d.compareTo(dateout) >= 0){//checkfor check in date > checkout date
							if(d.compareTo(new Date()) <= 0){//check for check in not beyond current date
							String loanid = table.getValueAt(row_selection, 0).toString();	
							
							String query = "update book_loans set date_in =? where loan_id = "+ Integer.parseInt(loanid) + ";";
							try {
								stat = conn.prepareStatement(query);
								stat.setString(1, date);
								stat.executeUpdate();
								UpdateFines.updateFineTable();
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(null, "Error:" + e1.getMessage());
							}
							JOptionPane.showMessageDialog(null, "Book Checked In ");
							updateTable(0);
							}
							else{
								JOptionPane.showMessageDialog(null,"Cannot check in for a future date");
							}
					}
						else{
							JOptionPane.showMessageDialog(null, "Cannot Check In :Check in date is previous to check out date");
						}
				}
				
			}
			}
			else{
				JOptionPane.showMessageDialog(null, "Please Select a Book to Check In");
			}
		}
		else if(e.getSource()==reset){
			resetFields();
		}
		
	}

	private void updateTable(int validator) {
		// TODO Auto-generated method stub
		try{
			clearTable();
			String query = "select loan_id,book_loans.book_id as bookid, book_loans.card_no as card,CONCAT(borrower.fname ,' ', borrower.lname) as name,date_out,due_date from book_loans,borrower where book_loans.card_no = borrower.card_no " + querybuffer + " and book_loans.date_in IS NULL";
			stat = conn.prepareStatement(query);
			rs = stat.executeQuery();
			if(!(rs.isBeforeFirst()))
            {
				if(validator==1){
                JOptionPane.showMessageDialog(this, "No result(s) found");
                return;
				}
				else{
					return;
				}
            }
			clearTable();
			while(rs.next()){
				Vector<String> v = new Vector<String>();
				v.add(String.valueOf(rs.getInt("loan_id")));
				v.add(rs.getString("bookid"));
				v.add(String.valueOf(rs.getInt("card")));
				
				v.add(rs.getString("name"));
				v.add(rs.getString("date_out"));
				v.add(rs.getString("due_date"));
				tableModel.addRow(v);
			}
		}
		catch(SQLException exp){
			JOptionPane.showMessageDialog(null, exp.getMessage());
		}
		
	}

	public void resetFields() {
		// TODO Auto-generated method stub
		bookid.setText("");
		cardno.setText("");
		textField.setText("");
		fname.setText("");
		clearTable();
	}

	}

