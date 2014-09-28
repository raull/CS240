package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman implements EvilHangmanGame {
	private int wordLength;
	private HashSet<String> wordList;
	private ArrayList<Character> charsGuessed;
	private StringBuilder userWord;
	
	public EvilHangman() {
		wordLength = 0;
		wordList = new HashSet<String>();
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
		//Check if it has already been used
		
		for (int i = 0; i < this.charsGuessed.size(); i++) {
			char char1 = this.charsGuessed.get(i).charValue();
			char char2 = Character.toLowerCase(guess);
			if (char1 == char2) {
				throw new GuessAlreadyMadeException();
			}
		}
		
		//Get new Set of words
		this.wordList = getListFromGuess(guess);
		
		//Print the result of the user's guess
		Iterator<String> iterator = this.wordList.iterator();
		String resultKey = HangmanKeyPattern.getKeyFromWord(iterator.next(), guess);
		
		ArrayList<Integer> occurrences = HangmanKeyPattern.getOccurencesFromString(resultKey, guess);
		
		if (occurrences.size() == 0) {
			System.out.println("Sorry, there are no " + guess + "'s");
		} else {
			updateUserWord(guess, occurrences);
			System.out.println("Yes, there is " + occurrences.size() + " " + guess + "'s");
		}
		
		System.out.println();
		
		return this.wordList;
	}
	
	private HashSet<String> getListFromGuess(char guess) {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		/*
		 * Iterate trough the strings on the current set of words and split them 
		 * on sets depending on their character occurrences
		 */
		for (String curWord : this.wordList) {
			String key = HangmanKeyPattern.getKeyFromWord(curWord, guess);
			
			if (map.containsKey(key)) {
				map.get(key).add(curWord);
			}
			else {
				HashSet<String> newSet = new HashSet<String>();
				newSet.add(curWord);
				map.put(key, newSet);
			}
		}
		
		//System.out.println(map.toString());
		
		//Return the best set with the higher chance for winning
		return getTopSet(map);
	}
	
	private HashSet<String> getTopSet(HashMap<String, HashSet<String>> map) {
		//Get all the keys
		ArrayList<String> keys = new ArrayList<>(map.keySet());
		ArrayList<HangmanKeyPattern> keyPatterns = new ArrayList<HangmanKeyPattern>();
		
		//Convert them to KeyPatterns to sort them easier
		for (String key : keys) {
			int numberOfWords = map.get(key).size();
			HangmanKeyPattern newKeyPattern = new HangmanKeyPattern(key, numberOfWords);
			keyPatterns.add(newKeyPattern);
		}
		
		Collections.sort(keyPatterns);
		
		//Get the top
		return map.get(keyPatterns.get(keyPatterns.size() - 1).key());
	}
	
	private void updateUserWord(char guess, ArrayList<Integer> occurrences) {
		for (Integer curInt : occurrences) {
			this.userWord.deleteCharAt(curInt);
			this.userWord.insert(curInt.intValue(), guess);
		}
	}
	
	public boolean hasWon() {
		
		ArrayList<Integer> guessesLeftArrayList = HangmanKeyPattern.getOccurencesFromString(this.userWord.toString(), '-');
		
		if (guessesLeftArrayList.size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//Main Method
	public static void main(String[] args) {
		if (args.length == 3) {
			File dictionaryFile = new File(args[0]);
			EvilHangman game = new EvilHangman();
			int wordLength = Integer.parseInt(args[1]);
			int numberOfGuesses = Integer.parseInt(args[2]);
			game.startGame(dictionaryFile, wordLength);
			Scanner userInputScanner = new Scanner(System.in);
			Set<String> wordOptions = new HashSet<String>();
			
			//Start turns
			
			boolean hasWon = false;
			
			while(numberOfGuesses > 0 && hasWon == false) {
				System.out.print("You have " + numberOfGuesses + " guesses left\nUsed letters: ");
				
				//Print Used letters
				StringBuilder charUsedStringBuilder = new StringBuilder();
				for (Character curCharacter : game.charsGuessed) {
					charUsedStringBuilder.append(curCharacter.charValue() + " ");
				}
				System.out.println(charUsedStringBuilder.toString());
				
				//Print Current word
				System.out.println("Word: " + game.userWord.toString());
				
				//Print User input
				
				boolean isLetter = true;
				char guess;
				
				do {
					System.out.print("Enter guess: ");
					guess = userInputScanner.next().charAt(0);
					//Check if it is a letter
					if (!Character.isLetter(guess)) {
						System.out.println("Invalid input");
						isLetter = false;
					} else {
						isLetter = true;
					}
				} while(!isLetter);
				
				try {
					wordOptions = game.makeGuess(guess);
					Character newCharacter = new Character(guess);
					game.charsGuessed.add(newCharacter);
					numberOfGuesses--;
				} catch (GuessAlreadyMadeException e) {
					System.out.println("You already used that letter\n");
				}
				
				hasWon = game.hasWon();
				
				if (hasWon) {
					System.out.print("You won!\n The word was:" + game.userWord.toString() + "\n");
				}
				
			}
			
			if (!hasWon) {
				Iterator<String> iterator = wordOptions.iterator();
				System.out.print("You lose!\nThe word was: " + iterator.next() + "\n");
			}
			
			userInputScanner.close();
			
		}
	}

}
