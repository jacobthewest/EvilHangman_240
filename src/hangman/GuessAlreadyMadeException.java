package hangman;

public class GuessAlreadyMadeException extends Exception {
    public GuessAlreadyMadeException() {
        System.out.println("You already used that letter");
    }
}
