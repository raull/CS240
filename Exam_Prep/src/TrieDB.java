
public class TrieDB implements Trie{
	private int nodeCount = 1;
	private int wordCount = 0;
	private TrieNode root = new TrieNode();
	
	@Override
	public void add(String word) {
		TrieNode curNode = this.root;
		for (int i = 0; i < word.length(); i++) {
			char curChar = Character.toLowerCase(word.charAt(i));
			int charPosition = curChar - 'a';
			
			if (curNode.nodes[charPosition] == null) {
				curNode.nodes[charPosition] = new TrieNode();
				this.nodeCount++;
			}
			
			curNode = curNode.nodes[charPosition];
			curNode.word = word.substring(0, i+1).toLowerCase();
			
			if (i == word.length() - 1) {
				curNode.count++;
				wordCount++;
			}
			
		}
	}
	
	@Override
	public Node find(String word) {
		TrieNode curNode = this.root;
		
		for (int i = 0; i < word.length(); i++) {
			
			char curChar = Character.toLowerCase(word.charAt(i));
			int charPosition = curChar - 'a';
			
			curNode = curNode.nodes[charPosition];
			
			if (i == word.length() - 1 && curNode.count > 0) {
				return curNode;
			}
			
		}
		
		return null;
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	/**
	 * Returns the number of nodes in the trie
	 * 
	 * @return The number of nodes in the trie
	 */
	public int getNodeCount() {
		return nodeCount;
	}
	
	/**
	 * The toString specification is as follows:
	 * For each word, in alphabetical order:
	 * <word> <count>\n
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		printTreeFromNode(root, stringBuilder);
		
		return stringBuilder.toString();
	}
	
	private void printTreeFromNode(TrieNode root, StringBuilder currentWord) {
		
		if (root != null) {
			if (root.count > 0) {
				currentWord.append(root.word + " " + root.count + "\n");
			}
		}
		else {
			return;
		}
		
		for (int i = 0; i < root.nodes.length; i++) {
			TrieNode curNode = root.nodes[i];
			
			printTreeFromNode(curNode, currentWord);
		}
		
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (obj.getClass() != TrieDB.class) {
			return false;
		}
		
		TrieDB trie2 = (TrieDB)obj;
		
		if (trie2.nodeCount != this.nodeCount || trie2.wordCount != this.wordCount) {
			return false;
		}
		
		if (!compareTries(this.root, trie2.root)) {
			return false;
		}
		
		
		return true;
	}
	
	private Boolean compareTries(TrieNode root1, TrieNode root2) {
		if(root1.count != root2.count) {
			return false;
		}
		
		for (int i = 0; i < root1.nodes.length; i++) {
			TrieNode curNode1 = root1.nodes[i];
			TrieNode curNode2 = root2.nodes[i];
			
			if ((curNode1 == null && curNode2 != null) || (curNode1 != null && curNode2 == null)) {
				return false;
			}
			
			if (curNode1 != null && curNode2 != null) {
				if (!compareTries(curNode1, curNode2)) {
					return false;
				}
			}
			
		}
		
		return true;
	}

	/**
	 * Your trie node class should implement the Trie.Node interface
	 */
	public class TrieNode implements Node {
		private int count = 0;
		private TrieNode[] nodes = new TrieNode[26];
		private String word;
		
		/**
		 * Returns the frequency count for the word represented by the node
		 * 
		 * @return The frequency count for the word represented by the node
		 */
		public int getValue() {
			return count;
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println('b'-'a');
		
		TrieDB db = new TrieDB();
		TrieDB db2 = new TrieDB();
		
		db.add("Raul");
		db.add("cinthia");
		db.add("ra");
		db.add("cin");
		db.add("Raul");
		
		db2.add("Raul");
		db2.add("cinthia");
		db2.add("ra");
		db2.add("cin");
		db2.add("Raul");
		
		System.out.println(db.toString());
		
		Node foundNode = db.find("Raul");
		
		if (foundNode != null) {
			System.out.println("Found the word :)");
		}
		else{
			System.out.println("Didn't found the word");
		}
		
		if (db.equals(db2)) {
			System.out.println("The same");
		}
		else {
			System.out.println("Not the same");
		}
		
	}
}
