package com.utd.db.libraryGUI;

import java.awt.MediaTracker;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.Label;

@SuppressWarnings("serial")
public class Home extends JFrame implements ActionListener,MenuListener{

	private JPanel one,two,four,five,panel,six;
	public static JPanel three;
	private JMenuBar menuBar;
	private JMenu menu1,menu2,menu3,menu4,menu5,home;
	private JMenuItem mntmCheckFines,update,receive;
	public static Home frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Home();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
				    //
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public Home() throws IOException {
		
		initialize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 120, 500, 350);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
		setSize(800, 600);
		setResizable(false);
		Tiles tile = new Tiles();
		panel = tile.getTile();
		setContentPane(panel);
		
		menuBar = new JMenuBar();
		
		home = new JMenu("Home Page");
		menu1 = new JMenu("Search Available Books");
		menu2 = new JMenu("Add New Borrower");
		menu3 = new JMenu("Check Out Available Books");
		menu4 = new JMenu("Check In Books");
		menu5 = new JMenu("Fines");
		
		mntmCheckFines = new JMenuItem("View/Update Fines");
        menu5.add(mntmCheckFines);
        update= new JMenuItem("Update Fines");
        update.addActionListener(this);
        mntmCheckFines.addActionListener(this);
        receive = new JMenuItem("Pay Fine");
        receive.addActionListener(this);
        
       // menu5.add(update);
        menu5.add(mntmCheckFines);
        menu5.add(receive);
        
        menu1.addMenuListener(this);
        menu2.addMenuListener(this);
        menu3.addMenuListener(this);
        menu4.addMenuListener(this);
        home.addMenuListener(this);
        
        menuBar.add(home);
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu4);
        menuBar.add(menu5);
        
               
        setJMenuBar(menuBar);
		
	}
	 
	private void initialize() {
		// TODO Auto-generated method stub
		five =new Fine();
		one = new SearchBooks();
		two = new BorrowerManagementPage();
		three = new CheckOutBooks();
		four = new CheckInBooks();
		six = new FineReceivable();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("View/Update Fines")){
			//System.out.print("here");
			setContentPane(five);
			setVisible(true);
			
		}
//		else if (command.equals("Update Fines")){
//			UpdateFines.updateFineTable();
//			JOptionPane.showMessageDialog(null,"Fines Updated");
//			((FineReceivable)six).resetFields();
//			
//		}
		else if (command.equals("Pay Fine")){
			setContentPane(six);
			((FineReceivable)six).resetFields();
			setVisible(true);
		}
		
	}

	public void setPanelforCheckout(String book , String branch){
		setContentPane(three);
		((CheckOutBooks)three).setFields(book,branch);
		setVisible(true);
	}

	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==menu1){
			setContentPane(one);
			((SearchBooks)one).resetFields();
			setVisible(true);
		}
		else if(e.getSource()==home){
			setContentPane(panel);
			setVisible(true);
		}
		else if(e.getSource()==menu2){
			setContentPane(two);
			((BorrowerManagementPage)two).resetFields();
			setVisible(true);
		}
		else if(e.getSource()==menu4){
			setContentPane(four);
			((CheckInBooks)four).resetFields();
			setVisible(true);
		}
		else if(e.getSource()==menu3){
			setContentPane(three);
			((CheckOutBooks)three).resetFields();
			setVisible(true);
		}
		else{
			
		}
		
		
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

}
