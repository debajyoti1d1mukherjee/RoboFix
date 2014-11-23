package uk.co.bbc.fabric.interfaces.exception;

public class LockException extends Exception {
	String message;

	public String getMessage() {
		return message;
	}

	public LockException(String message) {
		super();
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
