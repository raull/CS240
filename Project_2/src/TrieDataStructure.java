
public class TrieDataStructure implements Trie{
	
	private TrieNode rootNode;
	private int nodeCount = 0;
	private int wordCount = 0;
	
	//Constructors
	public TrieDataStructure() {
		rootNode = new TrieNode();
	}
	
	public TrieDataStructure(String[] initialStrings) {
		rootNode = new TrieNode();
		for (String string : initialStrings) {
			this.add(string);
		}
	}
	
	//*******Methods***************
	
	//Adding
	public void add(String word) {
		this.addString(word, this.rootNode, new StringBuilder(""));
	}
	
	private void addString(String word, TrieNode curNode, StringBuilder curWord) {
		
		char newChar = word.charAt(0);
		int charPosition = newChar - 'a';
		
		word = word.substring(1, word.length());
		
		//Check to see if we are at the end of the world and return
		if (word.length() == 0) {
			//Add the last letter
			curWord.append(newChar);
			curNode.word = curWord.toString();
			curNode.count++;
			this.wordCount++;
			return;
		}
		
		//Otherwise keep going down the tree
		TrieNode childNode = curNode.nodes[charPosition];
		
		if (childNode == null) {
			childNode = new TrieNode();
			curNode.nodes[charPosition] = childNode;
			this.nodeCount++;
		}
		

		curWord.append(newChar);
		childNode.word = curWord.toString();
		childNode.character = newChar;
		
		addString(word, childNode, curWord);

	}
	
	//Finding
	public TrieNode find(String word) {
		return null;
	}
	
	//Traversing
	
	private String traverseTrie() {
		
		StringBuilder finalString = new StringBuilder();
		
		traverseTrie(this.rootNode, finalString);
		
		return finalString.toString();
	}
	
	private void traverseTrie(TrieNode curNode, StringBuilder stringBuilder) {
		//If the count is non-zero that means that words exists
		if (curNode.count > 0) {
			stringBuilder.append("<" + curNode.word + "> <" + curNode.count + ">\n");
			return;
		}
		
		for (TrieNode node : curNode.nodes) {
			if (node != null) {
				traverseTrie(node, stringBuilder);
			}
		}
		
	}
	
	//*******Getters and Setters***************
	
	public int getNodeCount() {
		return nodeCount;
	}

	public int getWordCount() {
		return wordCount;
	}
	
	//**********Override Methods****************
	
	@Override
	public String toString() {
		
		traverseTrie();
		
		return "";
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
	}
	
	
	
	
	/*******************
	 * TrieNode Inner Class
	 *******************/ 
	
	private class TrieNode implements Node{
		private TrieNode[] nodes;
		private int count;
		private char character;
		private String word = "";
		
		//*******Constructors******* 
		
		public TrieNode() {
			nodes = new TrieNode[26];
		}
		
		//*******Getters***********
		
		public int getValue() {
			return count;
		}
	}
	
	
	
	public static void main(String[] args) {
		TrieDataStructure trieDB = new TrieDataStructure();
		
		trieDB.add("ra");
		trieDB.add("ci");
		
		System.out.println(trieDB);
	}
}
