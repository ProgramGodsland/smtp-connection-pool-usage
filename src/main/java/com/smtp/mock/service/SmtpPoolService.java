package com.smtp.mock.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smtp.mock.config.SmtpPoolConfig;
import com.smtp.mock.connection.SmtpConnection;
import com.smtp.mock.entities.EmailMessage;
import com.smtp.mock.factory.SmtpConnectionFactory;
import com.smtp.mock.factory.SmtpConnectionFactoryBuilder;
import com.smtp.mock.pool.SmtpConnectionPool;

@Service
public class SmtpPoolService {

  @Autowired
  private SmtpEmailSender smtpEmailSender;

  @Autowired
  private SmtpPoolConfig poolConfig;

  private SmtpConnectionPool connectionPool;
  private SmtpConnectionFactory factory;

  @PostConstruct
  private void init() throws Exception {
    this.factory = SmtpConnectionFactoryBuilder.newSmtpBuilder()
        .session(smtpEmailSender.setEmailSession()).build();
    System.err.println("Session creation: " + factory.getSession().getTransport().isConnected());
    this.connectionPool = new SmtpConnectionPool(factory, poolConfig);
    System.err.println("Pool creation count: " + connectionPool.getCreatedCount());
    // connectionPool.init();
    connectionPool.setTestOnBorrow(true);
  }

  // private Session setEmailSession() {
  // Properties props = new Properties();
  // props.put("mail.smtp.host", smtpConfig.getSmtpHost());
  // props.put("mail.smtp.port", smtpConfig.getSmtpPort());
  // props.put("mail.transport.protocol", "smtp");
  // props.put("mail.smtp.auth", "true");
  // props.put("mail.smtp.starttls.enable", "true");
  // props.put("mail.debug", "true");
  //
  // Authenticator authenticator = new Authenticator() {
  // protected PasswordAuthentication getPasswordAuthentication() {
  // return new PasswordAuthentication(smtpConfig.getUserName(), smtpConfig.getPassword());
  // }
  // };
  //
  // Session session = Session.getInstance(props, authenticator);
  // return session;
  // }

  public EmailMessage send(String userId, EmailMessage emailMessage) {

    List<String> addresses = emailMessage.getAddress();

    try {
      SmtpConnection connection = connectionPool.borrowObject();
      System.err.println("Is Connection still open: " + connection.isConnected()
          + " borrowed connection: " + connectionPool.getBorrowedCount() + " active connection: "
          + connectionPool.getNumActive() + " idled connection: " + connectionPool.getNumIdle()
          + " total max active: " + (connectionPool.getMaxTotal() - connectionPool.getMaxIdle()));
      MimeMessage mimeMessage = new MimeMessage(connectionPool.getSession());
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
      connectionPool.returnObject(connection);

      System.err.println("Is Connection still open: " + connection.isConnected()
          + " returned connection: " + connectionPool.getReturnedCount() + " active connection: "
          + connectionPool.getNumActive() + " idled conncetion: " + connectionPool.getNumIdle()
          + " total max: " + connectionPool.getMaxTotal());

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
