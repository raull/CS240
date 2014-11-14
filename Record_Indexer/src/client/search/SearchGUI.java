package client.search;

import java.awt.EventQueue;

public class SearchGUI {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
				public void run() {
					@SuppressWarnings("unused")
					SearchController controller = new SearchController();					
				}
			}
		);

	}
}
