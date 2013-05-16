package exceptions;

/**
 * Is thrown when User.startConvo is called with a convoID that already exists.
 *
 */
public class DuplicateConvoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

}
