package com.smtp.mock.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SmtpServerConfig {
  private final String smtpHost = "172.28.249.60";
  private final String smtpPort = "25";
  private final String userName = "root";
  private final String password = "Aser5ase";

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
