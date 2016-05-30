package fi.bioklaani.klaanonbot;

/** An exception class for all exceptions thrown inside bot code.
* Saves the original exception (optional) and a detail message.*/
public class BotException extends RuntimeException {

	private final String msg;
	private final Throwable throwable;

	public BotException(Throwable throwable, String msg) {
		this.throwable = throwable;
		this.msg = msg;
	}
	
	public BotException(String msg) {
		throwable = null;
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return toString();
	}
	
	@Override
	public String toString() {
		return (throwable == null ? "Exception"
			: throwable.getClass().getSimpleName()) + ": " + msg;
	}
}
