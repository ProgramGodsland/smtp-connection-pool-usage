package com.smtp.mock.factory;

import static com.smtp.mock.strategy.ConnectionStrategyFactory.newConnectionStrategy;
import static com.smtp.mock.strategy.TransportStrategyFactory.newSessiontStrategy;

import java.util.Properties;

import javax.mail.Session;

/**
 * {@link SmtpConnectionFactory} factory
 */
public final class SmtpConnectionFactories {

  private SmtpConnectionFactories() {
  }

  /**
   * Initialize the {@link SmtpConnectionFactory} with a
   * {@link Session} initialized to {@code Session.getInstance(new Properties())},
   * {@link com.smtp.mock.strategy.TransportStrategyFactory#newSessiontStrategy},
   * {@link com.smtp.mock.strategy.ConnectionStrategyFactory#newConnectionStrategy}
   *
   * @return
   */
  public static SmtpConnectionFactory newSmtpFactory() {
    return new SmtpConnectionFactory(Session.getInstance(new Properties()), newSessiontStrategy(), newConnectionStrategy());
  }

  /**
   * Initialize the {@link SmtpConnectionFactory} using the provided
   * {@link Session} and
   * {@link com.smtp.mock.strategy.TransportStrategyFactory#newSessiontStrategy},
   * {@link com.smtp.mock.strategy.ConnectionStrategyFactory#newConnectionStrategy}
   *
   * @param session
   * @return
   */
  public static SmtpConnectionFactory newSmtpFactory(Session session) {
    return new SmtpConnectionFactory(session, newSessiontStrategy(), newConnectionStrategy());
  }


}