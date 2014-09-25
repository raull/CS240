package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman implements EvilHangmanGame {
	private int wordLength;
	private int numberOfGuesses;
	private ArrayList<String> wordList;
	private ArrayList<Character> charsGuessed;
	private StringBuilder userWord;
	
	public EvilHangman() {
		wordLength = 0;
		numberOfGuesses = 0;
		wordList = new ArrayList<String>();
		charsGuessed = new ArrayList<Character>();
		userWord = new StringBuilder();
	}
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		this.wordLength = wordLength;
		try {
			Scanner dictionaryScanner = new Scanner(dictionary);
			while(dictionaryScanner.hasNext()) {
				String newWord = dictionaryScanner.next();
				if (newWord.length() == this.wordLength) {
					this.wordList.add(newWord);
				}
			}
			
			dictionaryScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		//Set the initial state of the user word
		
		for (int i = 0; i < wordLength; i++) {
			this.userWord.append('-');
		}
		
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {		
		System.out.print("You have " + this.numberOfGuesses + " guesses left\nUsed letters: ");
		
		//Print Used letters
		StringBuilder charUsedStringBuilder = new StringBuilder();
		for (Character curCharacter : this.charsGuessed) {
			charUsedStringBuilder.append(curCharacter.charValue() + " ");
		}
		System.out.println(charUsedStringBuilder.toString());
		
		//Print Current word
		System.out.println("Word: " + this.userWord.toString());
		
		//Print User input
		System.out.println("Enter guess: " + guess);
		Character newCharacter = new Character(guess);
		//Check if it is a letter
		if (!Character.isLetter(guess)) {
			System.out.println("Invalid input\n");
			return null;
		}
		
		//Check if it has already been used
		
		for (int i = 0; i < this.charsGuessed.size(); i++) {
			char char1 = this.charsGuessed.get(i).charValue();
			char char2 = Character.toLowerCase(guess);
			if (char1 == char2) {
				System.out.println("You already used that letter\n");
				return null;
			}
		}
		System.out.println();
		this.numberOfGuesses--;
		this.charsGuessed.add(newCharacter);
		
		return null;
	}
	
	//Getters and Setters
	
	public void setNumberOfGuesses(int numberOfGuesses) {
		this.numberOfGuesses = numberOfGuesses;
	}
	
	//Main Method
	public static void main(String[] args) {
		if (args.length == 3) {
			File dictionaryFile = new File(args[0]);
			EvilHangman game = new EvilHangman();
			int wordLength = Integer.parseInt(args[1]);
			int numberOfGuesses = Integer.parseInt(args[2]);
			game.setNumberOfGuesses(numberOfGuesses);
			game.startGame(dictionaryFile, wordLength);
			try {
				game.makeGuess('a');
				game.makeGuess('b');
				game.makeGuess('-');
				game.makeGuess('b');
				game.makeGuess('c');
				game.makeGuess('a');
			} catch (Exception e) {
				
			}
		}
	}

}
