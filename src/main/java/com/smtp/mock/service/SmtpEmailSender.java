package com.smtp.mock.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smtp.mock.config.SmtpGmailConfig;

/**
 * 
 * @author irlu
 *
 */
@Component
public class SmtpEmailSender {
  @Autowired
  private SmtpGmailConfig configUtils;
  
  public Session setEmailSession() {
    Properties props = new Properties();
    props.put("mail.smtp.host", configUtils.getSmtpHost());
    props.put("mail.smtp.port", configUtils.getSmtpPort());
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    Authenticator authenticator = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(configUtils.getUserName(), configUtils.getPassword());
      }
    };
    
    Session session = Session.getInstance(props, authenticator);
    return session;
    
  }
}
