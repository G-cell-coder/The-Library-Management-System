package com.utd.db.libraryGUI;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Tiles {
		JPanel panel;
		JLabel label;
		
		public JPanel getTile(){
			panel = new JPanel();
			
			panel.setLayout(new BorderLayout());
			
			label = new JLabel("LIBRARY MANAGEMENT APPLICATION SYSTEM",SwingConstants.CENTER);
			label.setFont(new Font("ARIEL", Font.BOLD, 32));
			panel.add(label,BorderLayout.CENTER);
			return panel;
		}

}
