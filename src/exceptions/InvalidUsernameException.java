package exceptions;

/**
 * Exception for when a user attempts to login with an invalid username
 * this may be invalid characters (non alphabet letters), over 10 characters,
 * or if the username is already in use by another user.
 *
 */
public class InvalidUsernameException extends RuntimeException{

    private static final long serialVersionUID = 1L;

}
