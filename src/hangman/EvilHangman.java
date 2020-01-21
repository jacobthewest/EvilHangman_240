package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.SortedSet;

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
        System.out.println("Used letters: ");
        //System.out.println("guessedLetters.size()  = " + guessedLetters.size());
        if(guessedLetters.size() > 0) {
            for(Character c : guessedLetters) {
                System.out.print(c + " ");
            }
        }
        System.out.println("Word: " + wordPattern);
    }

    public static void runProgram(EvilHangmanGame game, int guesses) {
        String stringPattern = game.getFirstPattern();
        SortedSet<Character> guessedLetters = game.getGuessedLetters();
        printRound(stringPattern, guesses, guessedLetters);

        while(guesses > 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter guess: ");
            String guessAsCharString = scanner.nextLine().toLowerCase();
            char guessAsChar = 0;

            if (guessAsCharString.length() > 1) {
                System.out.println("Guess must be one letter. Try again");
                continue;
            }
            guessAsChar = guessAsCharString.charAt(0);
            if (!Character.isLetter(guessAsChar)) {
                System.out.println("Guess must be a letter. Try again");
                continue;
            }

        }
    }

}
