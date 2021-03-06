package hangman;

import java.util.ArrayList;


public class HangmanKeyPattern implements Comparable<HangmanKeyPattern> {
	private int wordLength = 0;
	private int numberOfWords = 0;
	private char character;
	private String key = " ";
	private ArrayList<Integer> characterOcurrences = new ArrayList<Integer>();	
	
	public HangmanKeyPattern(String word, char character, int numberOfWords) {
		this.key = HangmanKeyPattern.getKeyFromWord(word, character);
		this.character = character;
		this.wordLength = word.length();
		this.numberOfWords = numberOfWords;
		this.characterOcurrences = HangmanKeyPattern.getOccurencesFromString(word, character);
	}
	
	public HangmanKeyPattern(String key, int numberOfWords) {
		this.key = key;
		this.wordLength = key.length();
		this.numberOfWords = numberOfWords;
		this.character = getCharFromKey(key);
		//Check is the key has or not ocurrences
		if (this.character != ' ') {
			this.characterOcurrences = HangmanKeyPattern.getOccurencesFromString(key, this.character);
		}
	}
	
	public static String getKeyFromWord(String word, char character) {
		StringBuilder patternBuilder = new StringBuilder();
		
		for (int i = 0; i < word.length(); i++) {
			char curChar = Character.toLowerCase(word.charAt(i));
			character = Character.toLowerCase(character);
			
			if (curChar == character) {
				patternBuilder.append(character);
			}
			else {
				patternBuilder.append('-');
			}
		}
		
		return patternBuilder.toString();
	}
	
	private char getCharFromKey(String key) {
		for (int i = 0; i < key.length(); i++) {
			if (key.charAt(i) != '-') {
				return Character.toLowerCase(key.charAt(i));
			}
		}
		
		return ' ';
	}
	
	public static ArrayList<Integer> getOccurencesFromString(String word, char character) {
		ArrayList<Integer> ocurrences = new ArrayList<Integer>();
		
		for (int i = 0; i < word.length(); i++) {
			char curChar = word.charAt(i);
			if (curChar == character) {
				ocurrences.add(new Integer(i));
			}
		}
		
		return ocurrences;
	}
	
	//Compare Method
	
	@Override
	public int compareTo(HangmanKeyPattern key) {
		if (this == key) {
			return 0;
		}
		//First compare them by the amount of words
		if (this.numberOfWords > key.numberOfWords) {
			return 1;
		}else if (this.numberOfWords < key.numberOfWords) {
			return -1;
		}else { // If not, compare them by amount of ocurrences
			if (this.characterOcurrences.size() == 0 && key.characterOcurrences.size() > 0) {
				return 1;
			}else if (key.characterOcurrences.size() == 0 && this.characterOcurrences.size() > 0) {
				return - 1;
			} else if (this.characterOcurrences.size() < key.characterOcurrences.size()) {
				// compare them to see the less occurrences of the characte
				return 1;
			} else if (this.characterOcurrences.size() > key.characterOcurrences.size()) {
				return -1;
			} else { // Then go trough all the ocurrences and see which one has the rightmost occurrence
				for (int i = this.characterOcurrences.size() - 1; i >= 0; i--) {
					if (this.characterOcurrences.get(i).intValue() > key.characterOcurrences.get(i).intValue()) {
						return 1;
					}
					else if(this.characterOcurrences.get(i).intValue() < key.characterOcurrences.get(i).intValue()){
						return -1;
					}
				}
			}
		}
		
		return 0;
	} 
	
	//Getters
	
	public String key() {
		return this.key;
	}
	
	public char getCharacter() {
		return this.character;
	}
	
	public int length() {
		return this.wordLength;
	}
	
	public ArrayList<Integer> getOcurrences() {
		return this.characterOcurrences;
	}
	
	public int numberOfWords() {
		return this.numberOfWords;
	}
	
//	// Main Method
//	public static void main(String[] args) {
//		System.out.println(HangmanKeyPattern.getKeyFromWord("Cinthia", 'c'));
//		HangmanKeyPattern keyPattern1 = new HangmanKeyPattern("rrur", 'a', 4);
//		HangmanKeyPattern keyPattern2 = new HangmanKeyPattern("Saaa", 'a', 4);
//		
//		System.out.println(keyPattern1.compareTo(keyPattern2));
//		
//	}
}
