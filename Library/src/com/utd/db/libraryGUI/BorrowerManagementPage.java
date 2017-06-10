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
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.utd.db.LibraryDB.DbConnection;

@SuppressWarnings("serial")
public class BorrowerManagementPage extends JPanel implements ActionListener {

	private JPanel home, panel1, panel2, panel3, panel4, panel5,panel6, parent_home;
	private JLabel start,end,dash, label2, label3, label4, label5,label1,label12;
	private JTextField field11, field2, field3, field12, field13,part1,part2,part3;
	private JButton button,reset;

	public BorrowerManagementPage() {
		
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		panel6 = new JPanel();
		parent_home = new JPanel();
		home = new JPanel();

		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new FlowLayout());
		panel4.setLayout(new FlowLayout());
		panel5.setLayout(new FlowLayout());
		panel6.setLayout(new FlowLayout());
		home.setLayout(new GridBagLayout());
		panel1.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		label1 = new JLabel("This (*) indicates all fields required");
		label4 = new JLabel("First Name *");
		label5 = new JLabel("Last Name *");
		label12 = new JLabel("SSN");
		label2 = new JLabel("Address *   ");
		label3 = new JLabel("Phone No");
		field11 = new JTextField(30);
		field12 = new JTextField(30);
		field2 = new JTextField(30);
		field13 = new JTextField(30);
		
		//phone number parts
		part1 = new JTextField(3);
		part2 = new JTextField(3);
		part3 = new JTextField(4);
		start = new JLabel("(");
		end = new JLabel(")");
		dash = new JLabel("-");
		
		panel4.add(label4);
		panel4.add(field11);
		panel5.add(label5);
		panel5.add(field12);

		panel3.add(label3);
		panel3.add(start);
		panel3.add(part1);
		panel3.add(end);
		panel3.add(part2);
		panel3.add(dash);
		panel3.add(part3);
		
		part1.setDocument(new JTextFieldLimit(3));
		part2.setDocument(new JTextFieldLimit(3));
		part3.setDocument(new JTextFieldLimit(4));

		c.gridx = 0;
		c.gridy = 1;
		panel1.add(panel4, c);

		c.gridx = 0;
		c.gridy = 2;
		panel1.add(panel5, c);
		
		c.gridx =0;
		c.gridy =3;
		panel6.add(label12);
		panel6.add(field13);

		panel2.add(label2);
		panel2.add(field2);

		button = new JButton("Add New Borrower");
		reset = new JButton("Reset");
		
		c.gridx = 0;
		c.gridy = 1;
		home.add(panel1, c);

		c.gridx = 0;
		c.gridy = 2;
		home.add(panel2, c);

		c.gridx = 0;
		c.gridy = 3;
		home.add(panel3, c);

		c.gridx = 0;
		c.gridy = 5;

		home.add(label1, c);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(button);
		buttonPanel.add(reset);
		
		c.gridx = 0;
		c.gridy = 4;
		
		home.add(buttonPanel, c);

		button.addActionListener(this);
		reset.addActionListener(this);
		setLayout(new BorderLayout());
		add(home, BorderLayout.CENTER);
		
		JLabel heading = new JLabel("Add New Borrower",SwingConstants.CENTER);
		heading.setFont(new Font("Times New Roman", Font.BOLD , 18));
		add(heading,BorderLayout.NORTH);

	}
	
	public boolean isAlpha(String name) {
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}
	
	public void resetFields(){
		field11.setText("");
		field12.setText("");
		field2.setText("");
		field13.setText("");
		part1.setText("");
		part2.setText("");
		part3.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ssn, fname, lname, ph, add,part1s,part2s,part3s;
		Connection con;
		DbConnection db;
		int db_card_no = 0, new_card_no = 0,count=0,ph_no;
		ResultSet rs;

		if (e.getSource() == button) {
			fname = field11.getText().trim().toLowerCase();
			lname = field12.getText().trim().toLowerCase();
			add = field2.getText().trim().toLowerCase();
			ssn = field3.getText().trim().toLowerCase();
			part1s = part1.getText();
			part2s = part2.getText();
			part3s = part3.getText();
			ph = "(" + part1s + ") " + part2s + "-" + part3s;
			
			if (fname.isEmpty() || lname.isEmpty() || add.isEmpty()|| ssn.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"Please enter all the required Details");
			} else {
				// check if ph is all integers and names are alphabets
				if(!(isAlpha(lname)) ) {
					JOptionPane.showMessageDialog(null,
							"Please enter valid last name");
					return;
				}
				if(!(isAlpha(fname)) ) {
					JOptionPane.showMessageDialog(null,
							"Please enter valid first name");
					return;
				}
				if(!(isAlpha(ssn))){
					JOptionPane.showMessageDialog(null,
							"Enter a valid SSN");
				}
				try{
					if(part1s.isEmpty()||part2s.isEmpty()||part3s.isEmpty()){
						if(part1s.isEmpty()&&part2s.isEmpty()&&part3s.isEmpty()){
							ph="";
						}
						else{	
						
							JOptionPane.showMessageDialog(null, "Please Enter Valid Phone Number");
							return;
						}
					}
					else{
						ph_no = Integer.parseInt(part1.getText());
						ph_no = Integer.parseInt(part2.getText());
						ph_no = Integer.parseInt(part3.getText());
					}
				}
				catch(NumberFormatException exp){
					System.out.println("Number format exception" + exp.getMessage());
					JOptionPane.showMessageDialog(null, "Please Enter Valid Phone Number");
					return;
				}

				try {
					db = new DbConnection();
					con = db.getConnection();

					String query = "select COUNT(*) as count from borrower where LOWER(fname)=? AND LOWER(lname)=? AND LOWER(address)=?;";
					PreparedStatement pstat = con.prepareStatement(query);
					pstat.setString(1, fname);
					pstat.setString(2, lname);
					pstat.setString(3, add);
					rs = pstat.executeQuery();
					while (rs.next()) {

						count = rs.getInt("count");

					}
					System.out.println("Count : " + count);
						if(!(count>0)){

							query = "select card_no from borrower order by card_no DESC;";
							Statement stmts = con.createStatement();
							rs = stmts.executeQuery(query);
							while (rs.next()) {
		
								db_card_no = rs.getInt("card_no");
		
							}
							System.out.println("New Db card Number is " + db_card_no);
							new_card_no = db_card_no + 1;
							query = "INSERT INTO borrower(card_no,fname,lname,address,phone,ssn)"
									+ "VALUES(?,?,?,?,?,?)";
							pstat = con.prepareStatement(query);
							pstat.setString(1, String.valueOf(new_card_no));
							pstat.setString(2, fname);
							pstat.setString(3, lname);						
							pstat.setString(4, add);
							pstat.setString(5, ph);
							pstat.setString(6,ssn);
							pstat.executeUpdate();
		
							con.close();
							JOptionPane.showMessageDialog(null,
									"New Borrower created\n Card no : " + new_card_no);
							con.close();

				
				
					}
					else{
						JOptionPane.showMessageDialog(null,
								"Borrower already exists,Try again");
						con.close();
					}
				}
				catch (SQLException exp) {
					System.out.println(exp.getMessage());
				}
				
			}
		}
		else if(e.getSource()==reset){
			resetFields();
		}

	}


}


