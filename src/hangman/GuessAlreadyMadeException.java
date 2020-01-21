package hangman;

public class GuessAlreadyMadeException extends Exception {
    public GuessAlreadyMadeException() {
        System.out.println("You have already guessed that letter. Please guess again.");
    }
}
