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
public class FineReceivable extends JPanel implements ActionListener{
	private JButton fine,pay,reset;
	private JLabel card_no,heading;
	private JTextField card;
	private JPanel one , two , three,four,five,six;
	String cardno;
	private JTable table;
	private ResultSet rs;
	private DbConnection db ;
	private Connection conn;
	private DefaultTableModel tableModel;
	/**
	 * Create the panel.
	 */
	public FineReceivable() {
		
		setLayout(new BorderLayout());
		
		fine = new JButton("Show Fines");
		pay = new JButton("Receive Fine Amount");
		reset = new JButton("Reset");
		fine.addActionListener(this);
		pay.addActionListener(this);
		reset.addActionListener(this);
		
		card_no = new JLabel("Card Number");
		card = new JTextField(20);
		
		heading = new JLabel("Pay Fines for Checked In Books",SwingConstants.CENTER);
		heading.setFont(new Font("Sans Serif", Font.BOLD, 18));
		heading = new JLabel("Pay Fines");
		one = new JPanel();
		one.setLayout(new BorderLayout());
		one.add(heading,BorderLayout.NORTH);
		
		two = new JPanel();
		two.setLayout(new FlowLayout());
		two.add(card_no);
		two.add(card);
		
		three = new JPanel();
		three.setLayout(new FlowLayout());
		three.add(fine);
		three.add(reset);
		
		four = new JPanel();
		four.setLayout(new BorderLayout());
		four.add(Box.createVerticalStrut(25),BorderLayout.NORTH);
		four.add(two,BorderLayout.CENTER);
		four.add(three,BorderLayout.SOUTH);
		
		five = new JPanel();
		five.setLayout(new BorderLayout());
		
		six = new JPanel();
		six.setLayout(new BorderLayout());
		six.add(four,BorderLayout.NORTH);
		
				
		tableModel = new DefaultTableModel(new Object[] {"Card Number","Borrower Name","Fine","Transaction Id"}, 0);
		table = new JTable(tableModel);
		JScrollPane myPanel = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		table.setPreferredScrollableViewportSize(new Dimension(200,250));
		
		five.add(myPanel,BorderLayout.NORTH);
		five.add(pay, BorderLayout.CENTER);
		five.add(Box.createHorizontalStrut(200),BorderLayout.EAST);
		five.add(Box.createHorizontalStrut(200),BorderLayout.WEST);
		five.add(Box.createVerticalStrut(30),BorderLayout.SOUTH);
		six.add(five,BorderLayout.CENTER);
		one.add(six,BorderLayout.CENTER);
		add(one);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		db = new DbConnection();
		conn = db.getConnection();
		if(e.getSource()==fine){
			cardno = card.getText().trim();
			if(cardno.isEmpty()){
				JOptionPane.showMessageDialog(null,"Card No required");
			}
			else{
				updateTable(1);
				
			}
			
		}
		else if(e.getSource()==pay){
			int[] row = table.getSelectedRows();
			if(row.length==0){
				JOptionPane.showMessageDialog(null, "Select records(s) to pay fine");
			}
			else{
				try{
					for(int i=0 ;i<row.length;i++){
						String query = "update fines set paid=1 where loan_id=?";
						PreparedStatement stat = conn.prepareStatement(query);
						stat.setInt(1,Integer.parseInt(((table.getValueAt(row[i], 3)).toString())));
						stat.executeUpdate();
					}
					JOptionPane.showMessageDialog(null, "Fine Received and Paid.");
					updateTable(0);
				}
				catch(SQLException exp){
					JOptionPane.showMessageDialog(null, exp.getMessage());
				}
			}
		}
		else if(e.getSource()==reset){
			resetFields();
			clearTable();
		}
		else{
			
		}
		try {
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	public void clearTable(){
		tableModel.setRowCount(0);
		
	}
	public void resetFields(){
		card.setText("");
	}

	private void updateTable(int validator) {
		try{
			clearTable();
			//System.out.print("Before query");
			String query = "select l.card_no as cardno,CONCAT(fname,' ',lname) as name,fine_amt as fine,l.loan_id from book_loans as l,borrower b,fines f where date_in IS NOT NULL AND l.card_no = b.card_no AND f.loan_id = l.loan_id AND paid=0 AND l.card_no=?;";
			PreparedStatement stat  = conn.prepareStatement(query);
			stat.setInt(1,Integer.parseInt(cardno) );
			rs = stat.executeQuery();
			if(!rs.isBeforeFirst()){
				if(validator==1){
					JOptionPane.showMessageDialog(null, "No Fines Found");
				}
			}
			else{
				//System.out.print("After query");
				while(rs.next()){
					Vector<String> v = new Vector<String>();
					v.add(String.valueOf(rs.getInt("cardno")));
					v.add(rs.getString("name"));
					v.add(String.valueOf(rs.getFloat("fine")));
					v.add(rs.getString("l.loan_id"));
					tableModel.addRow(v);
					
				}
			}
			
		}
		catch(SQLException exp){
			JOptionPane.showMessageDialog(null, exp.getMessage());
		}
		catch(NumberFormatException exp){
			JOptionPane.showMessageDialog(null, "Card No not Valid");
		}
		
	}

}
