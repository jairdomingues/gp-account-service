package org.springframework.samples.petclinic.webhook;

public class Codebeautify {
	
	private String date;
	private String env;
	private String event;
	Resource ResourceObject;

	// Getter Methods

	public String getDate() {
		return date;
	}

	public String getEnv() {
		return env;
	}

	public String getEvent() {
		return event;
	}

	public Resource getResource() {
		return ResourceObject;
	}

	// Setter Methods

	public void setDate(String date) {
		this.date = date;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public void setResource(Resource resourceObject) {
		this.ResourceObject = resourceObject;
	}
}

