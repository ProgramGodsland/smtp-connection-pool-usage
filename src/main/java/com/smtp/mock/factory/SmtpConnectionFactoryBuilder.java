package com.smtp.mock.factory;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.event.TransportListener;

/**
 * 
 * @author irlu
 *
 */
public class SmtpConnectionFactoryBuilder {

  protected Session session = null;
  protected String protocol = null;
  protected String host = null;
  protected int port = -1;
  protected String username;
  protected String password;

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
   */
  public SmtpConnectionFactory build() {
    if (session == null) {
      session = Session.getInstance(new Properties());
    }

    return new SmtpConnectionFactory(session, defaultTransportListeners);
  }
}
