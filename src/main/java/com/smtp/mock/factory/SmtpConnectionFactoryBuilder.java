package com.smtp.mock.factory;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.event.TransportListener;

/**
 * Build a Smtp connection for the use of smtp connection pool
 * 
 * @author irlu
 *
 */
public class SmtpConnectionFactoryBuilder {

  protected Session session = null;

  protected List<TransportListener> defaultTransportListeners = Collections.emptyList();

  private SmtpConnectionFactoryBuilder() {}

  public static SmtpConnectionFactoryBuilder newSmtpBuilder() {
    return new SmtpConnectionFactoryBuilder();
  }

  public SmtpConnectionFactoryBuilder session(Properties properties) {
    this.session = Session.getInstance(properties);
    return this;
  }

  public SmtpConnectionFactoryBuilder session(Properties properties, Authenticator authenticator) {
    this.session = Session.getInstance(properties, authenticator);
    return this;
  }

  public SmtpConnectionFactoryBuilder session(Session session) {
    this.session = requireNonNull(session);
    return this;
  }

  /**
   * Build the {@link SmtpConnectionFactory}
   *
   * @return
   * @throws Exception 
   */
  public SmtpConnectionFactory build() throws Exception {
    if (session == null) {
      System.err.println("session is newly created");
      session = Session.getInstance(new Properties());
    }

    return new SmtpConnectionFactory(session, defaultTransportListeners);
  }
}
