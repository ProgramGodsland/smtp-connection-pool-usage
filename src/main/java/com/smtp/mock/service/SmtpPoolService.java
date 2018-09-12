package com.smtp.mock.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smtp.mock.connection.SmtpConnection;
import com.smtp.mock.entities.EmailMessage;
import com.smtp.mock.factory.SmtpConnectionFactory;
import com.smtp.mock.factory.SmtpConnectionFactoryBuilder;
import com.smtp.mock.pool.SmtpConnectionPool;

@Service
public class SmtpPoolService {

  @Autowired
  private SmtpEmailSender smtpEmailSender;

  private SmtpConnectionPool connectionPool;
  private SmtpConnectionFactory factory;

  @PostConstruct
  private void init() {
    this.factory = SmtpConnectionFactoryBuilder.newSmtpBuilder().session(smtpEmailSender.setEmailSession()).build();
    this.connectionPool = new SmtpConnectionPool(factory);
    connectionPool.init();
  }

//  private Session setEmailSession() {
//    Properties props = new Properties();
//    props.put("mail.smtp.host", smtpConfig.getSmtpHost());
//    props.put("mail.smtp.port", smtpConfig.getSmtpPort());
//    props.put("mail.transport.protocol", "smtp");
//    props.put("mail.smtp.auth", "true");
//    props.put("mail.smtp.starttls.enable", "true");
//    props.put("mail.debug", "true");
//
//    Authenticator authenticator = new Authenticator() {
//      protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(smtpConfig.getUserName(), smtpConfig.getPassword());
//      }
//    };
//
//    Session session = Session.getInstance(props, authenticator);
//    return session;
//  }

  public EmailMessage send(String userId, EmailMessage emailMessage) {
    if (emailMessage == null) {
      System.err.println("No requested message!!");
      return null;
    }

    List<String> addresses = emailMessage.getAddress();

    try {
      SmtpConnection connection = connectionPool.borrowObject();
      MimeMessage mimeMessage = new MimeMessage(connection.getSession());
      if (addresses.size() == 1) {

        mimeMessage.setRecipients(RecipientType.TO, addresses.get(0));

      } else {
        String dest = addresses.stream().collect(Collectors.joining(","));
        mimeMessage.setRecipients(RecipientType.TO, dest);
      }
      mimeMessage.setSubject(emailMessage.getSubject());
      mimeMessage.setText(emailMessage.getMessage());

      System.err.println("MimeMessage prepared: " + mimeMessage.toString());

      connection.sendMessage(mimeMessage);
      System.err.println("Message sent!");
      connection.close();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      System.err.println("Connection is null");
      e.printStackTrace();
    }
    String messageId = UUID.randomUUID().toString();
    String resourceURL = String.format("/cpaas/email/%s/messages/%s", userId, messageId);
    emailMessage.setResourceURL(resourceURL);
    return emailMessage;
  }
}
