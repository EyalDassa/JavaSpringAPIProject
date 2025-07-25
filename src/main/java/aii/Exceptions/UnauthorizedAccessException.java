package aii.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends RuntimeException {
	private static final long serialVersionUID = 6015675587763476628L;

	public UnauthorizedAccessException() {
	}

	public UnauthorizedAccessException(String message) {
		super(message);
	}

	public UnauthorizedAccessException(Throwable cause) {
		super(cause);
	}

	public UnauthorizedAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
