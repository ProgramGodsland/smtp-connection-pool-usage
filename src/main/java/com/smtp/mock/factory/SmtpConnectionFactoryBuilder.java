package com.smtp.mock.factory;

import static com.smtp.mock.strategy.ConnectionStrategyFactory.newConnectionStrategy;
import static com.smtp.mock.strategy.TransportStrategyFactory.newProtocolStrategy;
import static com.smtp.mock.strategy.TransportStrategyFactory.newSessiontStrategy;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.event.TransportListener;

import com.smtp.mock.strategy.ConnectionStrategy;
import com.smtp.mock.strategy.ConnectionStrategyFactory;
import com.smtp.mock.strategy.TransportStrategy;
import com.smtp.mock.strategy.TransportStrategyFactory;

/**
 * A part of the code of this class is taken from the Spring
 * <a href="http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html">JavaMailSenderImpl class</a>.
 * <br><br>
 * {@link SmtpConnectionFactory} builder<br><br>
 * <p>
 * If no {@link Session} is provided, a default one is created.<br>
 * If any of the host , port, username, password properties are provided the factory is initialized with the {@link ConnectionStrategyFactory#newConnectionStrategy(String, int, String, String)}
 * otherwise with the {@link ConnectionStrategyFactory#newConnectionStrategy()}<br>
 * If the protocol is provided the factory is initialized with the {@link TransportStrategyFactory#newProtocolStrategy}
 * otherwise with the {@link TransportStrategyFactory#newSessiontStrategy()} ()}<br>
 */
public class SmtpConnectionFactoryBuilder {

  protected Session session = null;
  protected String protocol = null;
  protected String host = null;
  protected int port = -1;
  protected String username;
  protected String password;

  protected List<TransportListener> defaultTransportListeners = Collections.emptyList();

  private SmtpConnectionFactoryBuilder() {
  }

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

  public SmtpConnectionFactoryBuilder protocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  public SmtpConnectionFactoryBuilder host(String host) {
    this.host = host;
    return this;
  }

  public SmtpConnectionFactoryBuilder port(int port) {
    this.port = port;
    return this;
  }

  public SmtpConnectionFactoryBuilder username(String username) {
    this.username = username;
    return this;
  }

  public SmtpConnectionFactoryBuilder password(String password) {
    this.password = password;
    return this;
  }

  public SmtpConnectionFactoryBuilder defaultTransportListeners(TransportListener... listeners) {
    defaultTransportListeners = Arrays.asList(requireNonNull(listeners));
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

    TransportStrategy transportStrategy = protocol == null ? newSessiontStrategy() : newProtocolStrategy(protocol);

    ConnectionStrategy connectionStrategy;
    if (host == null && port == -1 && username == null && password == null) {
      connectionStrategy = newConnectionStrategy();
    } else {
      connectionStrategy = newConnectionStrategy(host, port, username, password);
    }

    return new SmtpConnectionFactory(session, transportStrategy, connectionStrategy, defaultTransportListeners);
  }
}