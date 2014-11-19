package client.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import client.main.menu.FileMenuBar;
import client.main.menu.FileMenuBarListener;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
		
	public MainFrame() {
		super();
		setupGUI();
	}
	
	public void setupGUI() {
		
		this.setSize(new Dimension(800, 700));
		this.setLocationRelativeTo(null);
		
		//Set JMenu
		setFileMenuBar();
		
		//Set JSpliPanes
		setSpliPanes();
		
		//Window closing
		this.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent windowEvent) {
		        System.exit(0);
		    }
		});
	}
	
	private void setSpliPanes() {
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(100, 100, 100));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(100, 100, 100));
		
		JPanel righPanel = new JPanel();
		righPanel.setBackground(new Color(100, 100, 100));
		
		JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, righPanel);
		hSplitPane.setDividerLocation(this.getSize().width/2);
		JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topPanel, hSplitPane);
		vSplitPane.setDividerLocation(this.getSize().height/2);
		this.getContentPane().add(vSplitPane);
	}
	
	private void setFileMenuBar() {
		FileMenuBar menuBar = new FileMenuBar();
		menuBar.addFileMenuBarListener(new FileMenuBarListener() {
			
			@Override
			public void logoutItemPressed() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void exitItemPressed() {
				System.exit(0);
				
			}
			
			@Override
			public void downloadBatchItemPressed() {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.setJMenuBar(menuBar);
	}
	
	//Getters and Setters
	
	
	//Listener
}
