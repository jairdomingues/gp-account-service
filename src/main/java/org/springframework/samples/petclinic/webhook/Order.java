package org.springframework.samples.petclinic.webhook;

public class Order {

	private String href;
	private String title;

	// Getter Methods

	public String getHref() {
		return href;
	}

	public String getTitle() {
		return title;
	}

	// Setter Methods

	public void setHref(String href) {
		this.href = href;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}