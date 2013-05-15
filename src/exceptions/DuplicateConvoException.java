package exceptions;

/**
 * Exception for when a user attempts to make duplicate conversations
 * only one active conversation is allowed between any given group of users
 *
 */
public class DuplicateConvoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

}
