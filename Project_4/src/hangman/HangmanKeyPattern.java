package hangman;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;

public class HangmanKeyPattern {
	private int wordLength = 0;
	private String word = "";
	private char character;
	private String key = "";
	private int rightmostPosition = -1;	
	
	public HangmanKeyPattern(String word, char character) {
		this.key = HangmanKeyPattern.getKeyFromWord(word, character);
		this.word = word;
		this.character = character;
		this.wordLength = word.length();
		this.rightmostPosition = HangmanKeyPattern.getRightmostOccurenceFromKey(word, character);
	}
	
	public static String getKeyFromWord(String word, char character) {
		StringBuilder patternBuilder = new StringBuilder();
		
		for (int i = 0; i < word.length(); i++) {
			char curChar = word.charAt(i);
			
			if (curChar == character) {
				patternBuilder.append(character);
			}
			else {
				patternBuilder.append('-');
			}
		}
		
		return patternBuilder.toString();
	}
	
	public static int getRightmostOccurenceFromKey(String key, char character) {
		int rightmostPosition = -1;
		
		for (int i = 0; i < key.length(); i++) {
			char curChar = key.charAt(i);
			if (curChar == character) {
				rightmostPosition = i;
			}
		}
		
		return rightmostPosition;
	}
	
	//Getters and Setters
	
	public String key() {
		return this.key;
	}
	
	public char getCharacter() {
		return this.character;
	}
	
	public int length() {
		return this.wordLength;
	}
	
	public int rightMostPosition() {
		return this.rightmostPosition;
	}
	
	// Main Method
	public static void main(String[] args) {
		System.out.println(HangmanKeyPattern.getKeyFromWord("Cinthia", 'i'));
	}
}