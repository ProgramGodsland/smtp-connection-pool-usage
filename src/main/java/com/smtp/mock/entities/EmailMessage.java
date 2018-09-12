package com.smtp.mock.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("emailMessage")
public class EmailMessage extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5061313332931770962L;
	private List<String> address;
	private String message;
	private String messageFormat;
	private String subject;
	private String resourceURL;

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

}
