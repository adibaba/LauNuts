package org.dice_research.launuts.exceptions;

public class CsvRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CsvRuntimeException() {
		super();
	}

	public CsvRuntimeException(String message) {
		super(message);
	}

	public CsvRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CsvRuntimeException(Throwable cause) {
		super(cause);
	}

	protected CsvRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}