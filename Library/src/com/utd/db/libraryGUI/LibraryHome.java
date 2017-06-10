package com.utd.db.libraryGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

@SuppressWarnings("serial")
public class LibraryHome extends JFrame implements ActionListener {

	private JButton button1, button2, button3, button4;
	private JPanel panel;
	static LibraryHome frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new LibraryHome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				try { 
//				    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//				} catch (Exception e) {
//				    e.printStackTrace();
//				}
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
				    // If Nimbus is not available, you can set the GUI to another look and feel.
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LibraryHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 450, 300);
		setSize(800, 550);
		setResizable(false);

		JPanel home_panel = new JPanel();
		home_panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		Tiles tile = new Tiles();
		panel = tile.getTile();
		setContentPane(panel);
		

		button1 = new JButton("Borrower Management>>");
		button2 = new JButton("Search Books>>");
		button3 = new JButton("Check IN books>>");
		button4 = new JButton("Check Out books>>");

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 40;
		c.ipady = 40;
		c.ipadx = 60;
		//c.insets = new Insets(20, 0, 0, 0);
		home_panel.add(button1, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 40;
		c.ipady = 40;
		c.ipadx = 60;
		//c.insets = new Insets(20, 0, 0, 0);
		home_panel.add(button2, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 40;
		c.ipady = 40;
		c.ipadx = 60;
		//c.insets = new Insets(20, 0, 0, 0);
		home_panel.add(button3, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 40;
		c.ipady = 40;
		c.ipadx = 60;
		//c.insets = new Insets(20, 0, 0, 0);
		home_panel.add(button4, c);
		// dummy.add(home_panel,BorderLayout.CENTER);

		panel.add(home_panel, BorderLayout.CENTER);

		button1.addActionListener((ActionListener) this);
		button2.addActionListener((ActionListener) this);
		button3.addActionListener((ActionListener) this);
		button4.addActionListener((ActionListener) this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == button1) {

			BorrowerManagementPage borrower_frame = new BorrowerManagementPage();
			borrower_frame.setVisible(true);
//			panel.add(new BorrowerManagementPage(),BorderLayout.CENTER);
			frame.setVisible(false);
		} else if (e.getSource() == button2) {

			SearchBooks book_frame = new SearchBooks();
			book_frame.setVisible(true);
//			panel.add(new SearchBooks(), BorderLayout.CENTER);
			frame.setVisible(false);
		} else if (e.getSource() == button3) {

			CheckInBooks checkIn = new CheckInBooks();
			checkIn.setVisible(true);
			//panel.add(new CheckInBooks(), BorderLayout.CENTER);
			frame.setVisible(false);

		} else if (e.getSource() == button4) {

			CheckOutBooks checkOut = new CheckOutBooks();
			checkOut.setVisible(true);
			//panel.add(new CheckOutBooks(), BorderLayout.CENTER);
			frame.setVisible(false);
			
			

		} else {

		}
	}

}
