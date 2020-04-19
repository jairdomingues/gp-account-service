package org.springframework.samples.petclinic.system;

public class CustomGenericInternalErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomGenericInternalErrorException() {
        super();
    }

    public CustomGenericInternalErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CustomGenericInternalErrorException(final String message) {
        super(message);
    }

    public CustomGenericInternalErrorException(final Throwable cause) {
        super(cause);
    }
}
