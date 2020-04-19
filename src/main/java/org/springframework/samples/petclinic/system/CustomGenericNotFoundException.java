package org.springframework.samples.petclinic.system;

public class CustomGenericNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomGenericNotFoundException() {
        super();
    }

    public CustomGenericNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CustomGenericNotFoundException(final String message) {
        super(message);
    }

    public CustomGenericNotFoundException(final Throwable cause) {
        super(cause);
    }
}
