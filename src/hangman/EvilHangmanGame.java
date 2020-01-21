package hangman;

import com.sun.source.util.Trees;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    public SortedSet<Character> guessedLetters = new TreeSet<Character>();
    public Set<String> words = new TreeSet<String>();
    public Map<String, Set<String>> wordMap = new TreeMap<String, Set<String>>();
    int wordLength = 0;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        this.wordLength = wordLength;

        boolean stillProcessingWords = true;
        while(stillProcessingWords) {
            try {
                String tempWord = scanner.nextLine();
                if (tempWord.length() == wordLength) {
                    words.add(tempWord);
                }
            } catch(Exception ex) {
                if (words.size() < 1) { // An empty dictionary
                    throw new EmptyDictionaryException();
                } else {
                    stillProcessingWords = false;
                }
            }
        }
    }

    public String getFirstPattern() {
        String returnMe = "";
        for (int i = 0; i < wordLength; i++) {
            returnMe += "-";
        }
        return returnMe;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessedLetters;
    }
}
