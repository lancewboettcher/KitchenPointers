package exception;

public class NoRecipesFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoRecipesFoundException(String message) {
		super(message);
	}
}
