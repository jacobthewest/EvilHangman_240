package hangman;

import com.sun.source.util.Trees;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    public SortedSet<Character> guessedLetters = new TreeSet<Character>();
    public Set<String> wordsInFocus = new TreeSet<String>();
    public Map<String, Set<String>> wordMap = new TreeMap<String, Set<String>>();
    public String wordPattern = "";
    int wordLength = 0;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        this.wordMap.clear();
        this.wordsInFocus.clear();
        Scanner scanner = new Scanner(dictionary);
        this.wordLength = wordLength;
        this.wordPattern = getFirstPattern();

        boolean stillProcessingWords = true;
        while(stillProcessingWords) {
            try {
                String tempWord = scanner.nextLine();
                if ((tempWord.length() == wordLength) && (!tempWord.equals(""))){
                    wordsInFocus.add(tempWord);
                }
            } catch(Exception ex) {
                if (wordsInFocus.size() < 1) { // An empty dictionary
                    throw new EmptyDictionaryException();
                } else {
                    stillProcessingWords = false;
                }
            }
        }
    }

    public String getFirstPattern() {
        String returnMe = "";
        for (int i = 0; i < this.wordLength; i++) {
            returnMe += "-";
        }
        return returnMe;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        this.wordMap.clear();
        String charAsString = Character.toString(guess);
        charAsString = charAsString.toLowerCase();
        guess = charAsString.charAt(0);
        if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }

        guessedLetters.add(guess);
        mapItUpDude(guess);
        String key = getTheKey(guess);
        this.wordsInFocus = wordMap.get(key);
        return this.wordsInFocus;
    }

    public String getTheKey(char guess) {
        String returnKey = "";
        filterWordMapByValueSize();
        if (this.wordMap.size() > 1) {
            filterByLeastFrequent(guess);
            if (this.wordMap.size() > 1) {
                filterByRightMostChar(guess);
                for (String wordKey: wordMap.keySet()) {
                    returnKey = wordKey; // There is only one wordKey at this point
                }
            } else {
                for (String wordKey: wordMap.keySet()) {
                    returnKey = wordKey; // There is only one wordKey at this point
                }
            }
        } else {
            for (String wordKey: wordMap.keySet()) {
                returnKey = wordKey; // There is only one wordKey at this point
            }
        }
        this.wordPattern = returnKey;
        return returnKey;
    }

    public void filterByRightMostChar(char guess) {
        String rightMost = "~";

        for (Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
           if (set.getKey().compareTo(rightMost) < 0) {
               rightMost = set.getKey();
           }
        }

        Set<String> keysToRemove = new TreeSet<String>();
        for (Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            if (set.getKey() != rightMost) {
                keysToRemove.add(set.getKey());
            }
        }

        this.wordMap.keySet().removeAll(keysToRemove);
    }

    public void filterWordMapByValueSize() {
        int largestWordSet = 0;
        for (Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            int setSize = set.getValue().size();
            if (setSize > largestWordSet) {
                largestWordSet = setSize;
            }
        }

        Set<String> keysToRemove = new TreeSet<String>();
        for (Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            if (set.getValue().size() != largestWordSet) {
                keysToRemove.add(set.getKey());
            }
        }

        this.wordMap.keySet().removeAll(keysToRemove);
    }

    public void filterByLeastFrequent(char guess) {
        int lowestFrequencyOfChar = 100;

        for (Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            int setFrequency = 0;
            for (int i = 0; i < set.getKey().length(); i++) {
                if (set.getKey().charAt(i) == guess) {
                    setFrequency++;
                }
            }

            if(setFrequency < lowestFrequencyOfChar) {
                lowestFrequencyOfChar = setFrequency;
            }
        }


        Set<String> keysToRemove = new TreeSet<String>();
        for (Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            int setFrequency = 0;
            for (int i = 0; i < set.getKey().length(); i++) {
                if (set.getKey().charAt(i) == guess) {
                    setFrequency++;
                }
            }
            if (setFrequency != lowestFrequencyOfChar) {
                keysToRemove.add(set.getKey());
            }
        }

        this.wordMap.keySet().removeAll(keysToRemove);
    }

    public void mapItUpDude(char guess) {
        this.wordMap.clear();
        for (String singleWord : wordsInFocus) {
            String tempPattern = createTempPattern(singleWord, guess);
            if (this.wordMap.containsKey(tempPattern)) {
                this.wordMap.get(tempPattern).add(singleWord);
            } else {
                Set<String> tempStringSet = new TreeSet<String>();
                tempStringSet.add(singleWord);
                this.wordMap.put(tempPattern, tempStringSet);
            }
        }
    }

    public String createTempPattern(String singleWord, char guess) {
        String returnMe = "";
        for (int i = 0; i < singleWord.length(); i++) {
            if (singleWord.charAt(i) == guess) {
                returnMe += guess;
            } else if (wordPattern.charAt(i) != '-') {
                returnMe += wordPattern.charAt(i);
            } else {
                returnMe += '-';
            }
        }
        return returnMe;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessedLetters;
    }
}
