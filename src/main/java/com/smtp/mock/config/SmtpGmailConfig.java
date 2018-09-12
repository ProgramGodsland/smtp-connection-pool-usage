package com.smtp.mock.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SmtpGmailConfig {
	private final String smtpHost = "smtp.gmail.com";
	private final String smtpPort = "587";
	private final String userName = "test3001.rbbn@gmail.com";
	private final String password = "Temp1234@";

	public String getSmtpHost() {
		return smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
	
}
