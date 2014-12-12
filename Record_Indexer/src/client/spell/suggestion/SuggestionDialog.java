package client.spell.suggestion;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.batch.state.BatchState;
import client.batch.state.Cell;

@SuppressWarnings("serial")
public class SuggestionDialog extends JDialog {

	JList<String> suggestionList = new JList<String>();
	Cell currentCell;
	String word;
	
	public SuggestionDialog(Cell cell, String value, Frame owner) {
		
		super(owner, true);
		
		currentCell = cell;
		word = value;
				
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(250, 200));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		setupGUI();
	}
	
	private void setupGUI() {
		//Set up suggestion List
		ArrayList<String> suggestions = (ArrayList<String>)BatchState.checkValue(word, this.currentCell);
		
		String[] suggestionArray = new String[suggestions.size()];
		
		for (int i = 0; i < suggestions.size(); i++) {
			suggestionArray[i] = suggestions.get(i).substring(0, 1).toUpperCase() + suggestions.get(i).substring(1);
		}
		
		Arrays.sort(suggestionArray);
		
		suggestionList.setListData(suggestionArray);
		suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(suggestionList);
		this.add(scrollPane);
		
		//Setup Buttons
		JPanel buttonPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		final JButton useButton = new JButton("Use Suggestion");
		useButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				BatchState.setValue(currentCell.getColumn(), currentCell.getRow(), suggestionList.getSelectedValue());
				setVisible(false);
				
			}
		});
		
		suggestionList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				useButton.setEnabled(true);
				
			}
		});
		
		buttonPanel.add(cancelButton);
		buttonPanel.add(useButton);
		
		this.add(buttonPanel);
		
		useButton.setEnabled(false);
		
	}
}
