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
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.utd.db.LibraryDB.DbConnection;

@SuppressWarnings("serial")
public class SearchBooks extends JPanel implements ActionListener,ChangeListener{

	private JPanel home,up,down,south,five,buttonpanel;
	private JPanel one,two,three,four,six;
	private JTextField book,author,title;
	private JLabel label1,label4,label5;
	private JTable table;
	private JButton button,reset,check_out;
	private DbConnection db;
	private Connection con;
	private DefaultTableModel tableModel;
	private String book_id;
	private String name="";
	private String btitle="";
	private String query;
	private PreparedStatement pstat;
	private ResultSet rs;
	private String querybuffer;
	private String having;
	private GridBagConstraints c;


	
	public SearchBooks() {
		
		home = new JPanel();
		up = new JPanel();
		down = new JPanel();
		four = new JPanel();
		
		home.setLayout(new BorderLayout());
		
		one = new JPanel();
		two = new JPanel();
		three = new JPanel();
		five = new JPanel();
		six = new JPanel();
		buttonpanel = new JPanel();
		
		one.setLayout(new FlowLayout());
		two.setLayout(new FlowLayout());
		three.setLayout(new FlowLayout());
		four.setLayout(new FlowLayout());
		
		label1 = new JLabel("Book Id         ");
		label4 = new JLabel("Book Title     ");
		book = new JTextField(30);
		author = new JTextField(28);
		title = new JTextField(30);
		
		
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		one.add(label1);
		one.add(book);
		
		four.add(label4);
		four.add(title);
		
		six.add(new JLabel("Author Name"));
		six.add(author);
		
		up.setLayout(new GridBagLayout());
		
		JLabel heading = new JLabel("Search Book(s)",SwingConstants.CENTER);
		heading.setFont(new Font("Sans Serif", Font.BOLD, 18));
		
		c.gridx = 0;
		c.gridy = 0;
		up.add(heading,c);
		
		c.gridx = 0;
		c.gridy = 1;
		up.add(one,c);
		
		c.gridx = 0;
		c.gridy = 3;
		up.add(three,c);
		
		c.gridx = 0;
		c.gridy = 4;
		up.add(six,c);
		
		c.gridx = 0;
		c.gridy = 5;
		up.add(five,c);
		
		c.gridx = 0;
		c.gridy = 2;
		up.add(four,c);
		
		buttonpanel.setLayout(new FlowLayout());
		
		button = new JButton("Search");
		reset = new JButton("Reset");
		reset.addActionListener(this);
		button.addActionListener(this);
		buttonpanel.add(button);
		buttonpanel.add(reset);
		c.gridx = 0;
		c.gridy = 6;
		up.add(buttonpanel,c);
		
		tableModel = new DefaultTableModel(new Object[] {"Book Id","Title","Author(s)", "Branch Id" ,"No Of copies","Available Copies"}, 0) {

		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		table = new JTable(tableModel);
		JScrollPane myPanel = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(600,175));
		down.add(myPanel);
		
		check_out = new JButton("Check Out");
		check_out.addActionListener(this);
		south = new JPanel();
		south.setLayout(new FlowLayout());
		south.add(check_out);
		
		home.add(up,BorderLayout.NORTH);
		home.add(down,BorderLayout.CENTER);
		home.add(south,BorderLayout.SOUTH);
		setLayout(new BorderLayout());
		add(home, BorderLayout.CENTER);
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		int partialcase=0;
		if(e.getSource() == button){
			
			book_id = book.getText().trim();
			btitle = title.getText().trim();
			if(name.isEmpty() && book_id.isEmpty() && btitle.isEmpty()){
				JOptionPane.showMessageDialog(null, "Please enter atlease one of the details mentioned above");
			}
			
			else{	
					try{
						db = new DbConnection();
						con = db.getConnection();
						
						query = "SELECT b.book_id,b.title, GROUP_CONCAT(ba.author_name) name,bc.branch_id,bc.no_of_copies FROM book b JOIN book_authors ba ON b.book_id=ba.book_id JOIN book_copies bc ON b.book_id=bc.book_id GROUP BY b.book_id, bc.branch_id having book_id LIKE '%"+book_id+"%' AND title LIKE '%"+btitle+"%';";
						
						
						System.out.println(query);
						pstat = con.prepareStatement(query);
						rs = pstat.executeQuery();
						if(!rs.isBeforeFirst()){
							JOptionPane.showMessageDialog(null,"No Books found");
							return;
						}
						clearTable();
			
						System.out.println("");
						System.out.println(query);
						pstat = con.prepareStatement(query);
						rs = pstat.executeQuery();
						if(!rs.isBeforeFirst()){
							JOptionPane.showMessageDialog(null,"No Books found");
							return;
						}
						clearTable();
						while (rs.next()) {
								
								String bookid = rs.getString("book_id");
								String branch = rs.getString("branch_id");
								int branch_copies = rs.getInt("no_of_copies");
								Vector<String> v = new Vector<String>();
								v.add(bookid);
								v.add(rs.getString("title"));
								v.add(rs.getString("name"));
								v.add(branch);
								v.add(String.valueOf(branch_copies));
								query = "select COUNT(*) as count from book_loans where book_id ='"+bookid+"' and branch_id = "+Integer.parseInt(branch)+" and date_in IS NULL;";
								pstat = con.prepareStatement(query);
								ResultSet rs_inner = pstat.executeQuery();
								int loan_copies=0;
								while(rs_inner.next()){
									loan_copies = rs_inner.getInt("count");
								}
								v.add(String.valueOf(branch_copies-loan_copies));
								tableModel.addRow(v);
							}
						
						con.close();
					}
					catch (SQLException exp) {
						JOptionPane.showMessageDialog(null, "Error:" + exp.getMessage());
					}
				}
			}
		else if(e.getSource()==reset){
			resetFields();
		}
		else if (e.getSource()==check_out){
			int row = table.getSelectedRow();
			if(row==-1){
				JOptionPane.showMessageDialog(null, "No Book Selected");
			}
			else{
				String book_id = table.getValueAt(row, 0).toString();
				String branch_id =table.getValueAt(row, 3).toString();
				Home.frame.setContentPane(Home.three);
				Home.three.setVisible(true);
				((CheckOutBooks)Home.three).setFields(book_id, branch_id);
			}
		}
	}


	public void resetFields() {
		book.setText("");
		author.setText("");
		title.setText("");
		author.setEnabled(true);
		clearTable();
	}


	public void clearTable() {
		// TODO Auto-generated method stub
		tableModel.setRowCount(0);
		
	}


	@Override
	public void stateChanged(ChangeEvent arg0) {
	
		
	}
		
	}


