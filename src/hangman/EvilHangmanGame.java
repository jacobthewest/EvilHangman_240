package hangman;

import com.sun.source.util.Trees;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    public SortedSet<Character> guessedLetters = new TreeSet<Character>();
    public Set<String> wordsInFocus = new TreeSet<String>();
    public Map<String, Set<String>> wordMap = new TreeMap<String, Set<String>>();
    int wordLength = 0;
    String wordPattern = "";

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        this.wordMap.clear();
        this.wordsInFocus.clear();
        Scanner scanner = new Scanner(dictionary);
        this.wordLength = wordLength;
        this.wordPattern = getFirstPattern();

        if(wordLength == 0) {
            throw new EmptyDictionaryException();
        }

        boolean stillProcessingWords = true;
        while(stillProcessingWords) {
            try {
                String tempWord = scanner.nextLine();
                if ((tempWord.length() == wordLength) && (tempWord != "")) {
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
        for (int i = 0; i < wordLength; i++) {
            returnMe += "-";
        }
        return returnMe;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        String stringAsChar = Character.toString(guess);
        stringAsChar = stringAsChar.toLowerCase();
        guess = stringAsChar.charAt(0);
        if(this.guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }

        this.guessedLetters.add(guess);
        mapItUpDude(guess);
        String key = chooseKey(guess);
        this.wordsInFocus = this.wordMap.get(key);
        return this.wordsInFocus;

    }

    public void mapItUpDude(char guess) {
        this.wordMap.clear();
        for (String word : this.wordsInFocus) {
            String tempPattern = createPattern(word, guess);
            if (this.wordMap.containsKey(tempPattern)) {
                this.wordMap.get(tempPattern).add(word);
            } else {
                Set<String> tempStringSet = new TreeSet<String>();
                tempStringSet.add(word);
                this.wordMap.put(tempPattern, tempStringSet);
            }
        }
    }

    public String createPattern(String word, char guess) {
        String newPattern = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                newPattern += guess;
            } else if (this.wordPattern.charAt(i) != '-') {
                newPattern += this.wordPattern.charAt(i);
            } else {
                newPattern += '-';
            }
        }

        return newPattern;
    }

    public String chooseKey(char guess) {
        String returnKey = "";
        filterMapByLength();
        if (this.wordMap.size() > 1) {
            filterMapByFrequency(guess);
            if (this.wordMap.size() > 1) {
                filterMapByRightMost(guess);
                for(String key : this.wordMap.keySet()) {
                    returnKey = key; // This will be only one.
                }
            } else {
                for(String key : this.wordMap.keySet()) {
                    returnKey = key; // This will be only one.
                }
            }
        } else {
            for(String key : this.wordMap.keySet()) {
                returnKey = key; // This will be only one.
            }
        }

        this.wordPattern = returnKey;
        return returnKey;
    }

    public void filterMapByLength() {
        int maxLength = 0;
        for(Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            int setLength = set.getValue().size();
            if (setLength > maxLength) {
                maxLength = setLength;
            }
        }

        Set<String> keysToRemove = new TreeSet<String>();
        for(Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            int setLength = set.getValue().size();
            if (setLength != maxLength) {
                keysToRemove.add(set.getKey());
            }
        }

        this.wordMap.keySet().removeAll(keysToRemove);
    }

    public void filterMapByFrequency(char guess) {
        int lowestFrequency = 200;

        for(Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            String setKeyValue = set.getKey();
            int setFrequency = 0;
            for (int i = 0; i < setKeyValue.length(); i++) {
                if (setKeyValue.charAt(i) == guess) {
                    setFrequency++;
                }
            }

            if (setFrequency < lowestFrequency) {
                lowestFrequency = setFrequency;
            }
        }

        Set<String> keysToRemove = new TreeSet<String>();
        for(Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            String setKeyValue = set.getKey();
            int setFrequency = 0;

            for (int i = 0; i < setKeyValue.length(); i++) {
                if (setKeyValue.charAt(i) == guess) {
                    setFrequency++;
                }
            }

            if (setFrequency != lowestFrequency) {
                keysToRemove.add(set.getKey());
            }
        }

        this.wordMap.keySet().removeAll(keysToRemove);
    }

    public void filterMapByRightMost(char guess) {
        String rightMost = "~";

        for(Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            String setKeyValue = set.getKey();

            if (setKeyValue.compareTo(rightMost) < 0) {
                rightMost = setKeyValue;
            }
        }

        Set<String> keysToRemove = new TreeSet<String>();
        for(Map.Entry<String, Set<String>> set : this.wordMap.entrySet()) {
            String setKeyValue = set.getKey();
            if (setKeyValue != rightMost) {
                keysToRemove.add(setKeyValue);
            }
        }

        this.wordMap.keySet().removeAll(keysToRemove);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessedLetters;
    }
}
