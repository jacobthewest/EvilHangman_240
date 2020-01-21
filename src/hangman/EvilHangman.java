package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) {
        String dictionary;
        int wordLength;
        int guesses;

        if (args.length == 3) {
            dictionary = args[0];
            wordLength = Integer.parseInt(args[1]);
            guesses = Integer.parseInt(args[2]);

            EvilHangmanGame game = new EvilHangmanGame();

            try {
                game.startGame(new File(dictionary), wordLength); // throws IOException, EmptyDictionaryException
            } catch(IOException ex) {
                System.out.println(ex);
            } catch(EmptyDictionaryException ex) {
                System.out.println(ex);
            }

            runProgram(game, guesses);
        }
        else {
            System.out.println("Usage: java [your main class name] dictionary wordLength guesses");
        }
    }

    public static void printRound(String wordPattern, int guesses, SortedSet<Character> guessedLetters) {
        System.out.println("You have " + guesses + " guesses left");
        System.out.print("Used letters: ");
        //System.out.println("guessedLetters.size()  = " + guessedLetters.size());
        if(guessedLetters.size() > 0) {
            for(Character c : guessedLetters) {
                System.out.print(c + " ");
            }
            System.out.print("\n");
        }
        System.out.println("Word: " + wordPattern);
    }

    public static void printEndOfGame(boolean theyWon, Set<String> resultsOfFinalGuess, String wordPatten) {
        if (theyWon) {
            System.out.println("You Win!");
            System.out.println("The word was: " + wordPatten);
        } else {
            System.out.println("You Lose!");
            System.out.println("The word was: " + resultsOfFinalGuess.iterator().next());
        }
    }

    public static void printSecondPartOfRound(boolean guessedCorrectly, char guessAsChar, String wordPattern) {
        if (guessedCorrectly) {
            int numCharsFound = 0;
            for (int i = 0; i < wordPattern.length(); i++) {
                if (wordPattern.charAt(i) == guessAsChar) {
                    numCharsFound++;
                }
            }

            if (numCharsFound > 1) {
                System.out.print("Yes, there are " + numCharsFound + "'s\n\n");
            } else {
                System.out.print("Yes, there is " + numCharsFound + "\n\n");
            }

        } else {
            System.out.print("Sorry, there are no " + guessAsChar + "'s\n\n");
        }
    }

    public static void runProgram(EvilHangmanGame game, int guesses) {
        String stringPattern = game.wordPattern;
        SortedSet<Character> guessedLetters = game.getGuessedLetters();
        printRound(stringPattern, guesses, guessedLetters);
        Set<String> results;

        while(guesses > 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter guess: ");
            String guessAsCharString = scanner.nextLine().toLowerCase();
            char guessAsChar = 0;

            if (guessAsCharString.length() > 1) {
                System.out.println("“Invalid input”");
                continue;
            }
            guessAsChar = guessAsCharString.charAt(0);
            if (!Character.isLetter(guessAsChar)) {
                System.out.println("“Invalid input”");
                continue;
            }

            try {
                results = game.makeGuess(guessAsChar);
                guesses--;
                stringPattern = game.wordPattern;
                if (stringPattern.contains(guessAsCharString)) {
                    // They guessed a char correctly
                    printSecondPartOfRound(true, guessAsChar, game.wordPattern);
                }
                else {
                    // They guessed a wrong char
                    printSecondPartOfRound(false, guessAsChar, game.wordPattern);
                }

                int correctCharsTheyHave = 0;
                for (int i = 0; i < stringPattern.length(); i++) {
                    if(stringPattern.charAt(i) != '-') {
                        correctCharsTheyHave++;
                    }
                }

                if (correctCharsTheyHave == stringPattern.length()) {
                    // They won
                    printEndOfGame(true, results, game.wordPattern);
                } else if (guesses == 0) {
                    // They lost
                    printEndOfGame(false, results, game.wordPattern);
                } else {
                    // They are still playing
                    printRound(stringPattern, guesses, game.getGuessedLetters());
                }

            } catch(GuessAlreadyMadeException ex) {
                System.out.println(ex);
                continue;
            }
        }
    }

}
