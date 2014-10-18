import java.util.ArrayList;


public class StringTransformer {

	public static ArrayList<String> transformationDeleteFromWord(String word) {
		ArrayList<String> wordsArrayList = new ArrayList<String>();
		
		for (int i = 0; i < word.length(); i++) {
			StringBuilder builder = new StringBuilder(word);
			builder.deleteCharAt(i);
			wordsArrayList.add(builder.toString());
		}
		
		return wordsArrayList;
	}
	
	public static ArrayList<String> transformationTransposeFromWord(String word) {
		ArrayList<String> wordsArrayList = new ArrayList<String>();
		
		for (int i = 0; i < word.length(); i++) {
			if (i != word.length() - 1) {
				StringBuilder builder = new StringBuilder(word);
				char charToSwap = word.charAt(i);
				builder.deleteCharAt(i);
				builder.insert(i+1, charToSwap);
				wordsArrayList.add(builder.toString());
			}
		}
		
		return wordsArrayList;
	}
	
	public static ArrayList<String> transformationAddFromWord(String word) {
		ArrayList<String> wordArrayList = new ArrayList<String>();
		
		for (int i = 0; i <= word.length(); i++) {
			int charPos = (int)'a';
			for (int j = charPos; j < charPos+26; j++) {
				char charToAdd = (char)j;
				StringBuilder builder = new StringBuilder(word);
				
				builder.insert(i, charToAdd);
				wordArrayList.add(builder.toString());
			}
		}
		
		return wordArrayList;
	}
	
	public static ArrayList<String> transformationAlterationFromWord(String word) {
		ArrayList<String> wordArrayList = new ArrayList<String>();
		
		for (int i = 0; i < word.length(); i++) {
			int charPos = (int)'a';
			for (int j = charPos; j < charPos+26; j++) {
				char charToAdd = (char)j;
				StringBuilder builder = new StringBuilder(word);
				builder.deleteCharAt(i);
				builder.insert(i, charToAdd);
				wordArrayList.add(builder.toString());
			}
		}
		
		return wordArrayList;
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(StringTransformer.transformationDeleteFromWord("Raul"));
		System.out.println(StringTransformer.transformationTransposeFromWord("Raul"));
		
		System.out.println(StringTransformer.transformationAddFromWord("Raul"));
		System.out.println(StringTransformer.transformationAlterationFromWord("Raul"));

	}
}
