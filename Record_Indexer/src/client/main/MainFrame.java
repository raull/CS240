package client.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import shared.modal.Batch;
import shared.modal.Project;
import client.batch.download.DownloadBatchDialog;
import client.batch.download.DownloadBatchDialogListener;
import client.batch.image.BatchImageComponent;
import client.batch.state.BatchState;
import client.main.menu.FileMenuBar;
import client.main.menu.FileMenuBarListener;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
		
	private DownloadBatchDialog downloadBatchDialog = new DownloadBatchDialog(this);
	
	private ArrayList<MainFrameListener> listeners = new ArrayList<MainFrameListener>();
	private BatchImageComponent imageComponent;
	
	public MainFrame() {
		super();
		setupGUI();
	}
	
	public void setupGUI() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(900, 700));
		this.setLocationRelativeTo(null);
		
		//Set JMenu
		setFileMenuBar();
		
		//Set Button Bar
		setButtonBar();
		
		//Set JSpliPanes
		setSpliPanes();
				
		//Set Download Batch Dialog
		downloadBatchDialog.addDownloadBatchDialogListener(new DownloadBatchDialogListener() {
			
			@Override
			public void newBatchDownloaded(Project project, Batch batch) {
				setNewBatch(project, batch);
			}
		});
		
		//Window closing
		this.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent windowEvent) {
		        System.exit(0);
		    }
		});
	}
	
	private void setSpliPanes() {
		//Split Panel
		JPanel splitPanel = new JPanel();
		splitPanel.setLayout(new BoxLayout(splitPanel, BoxLayout.Y_AXIS));
		splitPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Set top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setPreferredSize(this.getSize());

		try {

			URL url = new URL("http://localhost:8080/images/1890_image0.png");
			imageComponent = new BatchImageComponent(ImageIO.read(url));
			imageComponent.setPreferredSize(topPanel.getSize());
			
			topPanel.add(imageComponent);

		} catch (Exception e) {
			System.out.println("Error loading Image");
		}
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(100, 100, 100));
		
		JPanel righPanel = new JPanel();
		righPanel.setBackground(new Color(100, 100, 100));
		
		JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, righPanel);
		hSplitPane.setDividerLocation(this.getSize().width/2);
		JSplitPane vSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topPanel, hSplitPane);
		vSplitPane.setDividerLocation(this.getSize().height/3 * 2);
		splitPanel.add(vSplitPane);
		this.getContentPane().add(splitPanel);
		
	}
	
	private void setFileMenuBar() {
		FileMenuBar menuBar = new FileMenuBar();
		menuBar.addFileMenuBarListener(new FileMenuBarListener() {
			
			@Override
			public void logoutItemPressed() {
				for (MainFrameListener mainFrameListener : listeners) {
					mainFrameListener.logoutPressed();
				}
			}
			
			@Override
			public void exitItemPressed() {
				System.exit(0);
			}
			
			@Override
			public void downloadBatchItemPressed() {
				downloadBatchDialog.setVisible(true);
			}
		});
		
		this.setJMenuBar(menuBar);
	}
	
	private void setButtonBar() {
		JPanel buttonBarPanel= new JPanel();
		
		JButton zoomInButton = new JButton("Zoom In");
		zoomInButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				imageComponent.setScale(imageComponent.getScale() + 0.25);
			}
		});
		JButton zoomOutButton = new JButton("Zoom Out");
		zoomOutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				imageComponent.setScale(imageComponent.getScale() - 0.25);
			}
		});
		JButton invertImageButton = new JButton("Invert Image");
		JButton toggleHighlightButton = new JButton("Toggle Highlight");
		JButton saveButton = new JButton("Save");
		JButton submitButton = new JButton("Submit");
		
		buttonBarPanel.add(zoomInButton);
		buttonBarPanel.add(zoomOutButton);
		buttonBarPanel.add(invertImageButton);
		buttonBarPanel.add(toggleHighlightButton);
		buttonBarPanel.add(saveButton);
		buttonBarPanel.add(submitButton);
		
		this.getContentPane().add(buttonBarPanel);
	}
	
	private void setNewBatch(Project project, Batch batch) {
		System.out.println("New Batch Downloaded: " + batch + "\nFrom Project: " + project);
		
		BatchState.setBatchSize(project.getFields().size(), project.getRecordsPerBatch());
		BatchState.setProject(project);
		BatchState.setBatch(batch);
		
	}	
	
	//Listener
	
	public void addMainFrameListener(MainFrameListener listener) {
		this.listeners.add(listener);
	}
}
