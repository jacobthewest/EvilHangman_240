package hangman;

public class EmptyDictionaryException extends Exception {
	//Thrown when dictionary file is empty or no words in dictionary match the length asked for
    public EmptyDictionaryException() {
        System.out.println("The dictionary that you provided contains no words. Please provide a dictionary with words.");
    }
}
