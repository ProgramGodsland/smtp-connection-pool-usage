package com.smtp.mock.pool;

import javax.mail.Session;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.smtp.mock.connection.SmtpConnection;
import com.smtp.mock.factory.SmtpConnectionFactory;

/**
 * Preparing a smtp connection thread pool
 * 
 * @author irlu
 *
 */
public class SmtpConnectionPool extends GenericObjectPool<SmtpConnection> {

  public SmtpConnectionPool(SmtpConnectionFactory factory) {
    super(factory);
  }

  public SmtpConnectionPool(SmtpConnectionFactory factory, GenericObjectPoolConfig config) {
    super(factory, config);
  }

  public SmtpConnectionPool(SmtpConnectionFactory factory, GenericObjectPoolConfig config,
      AbandonedConfig abandonedConfig) {
    super(factory, config, abandonedConfig);
  }

  /**
   * Open 10 thread initially
   * 
   */
  public void init() {
    try {
      for (int i = 0; i < 10; i++) {
        super.addObject();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public SmtpConnection borrowObject() throws Exception {
    SmtpConnection object = super.borrowObject();
    object.setObjectPool(this);
    return object;
  }

  @Override
  public SmtpConnection borrowObject(long borrowMaxWaitMillis) throws Exception {
    SmtpConnection object = super.borrowObject(borrowMaxWaitMillis);
    object.setObjectPool(this);
    return object;
  }

  public Session getSession() {
    return ((SmtpConnectionFactory) getFactory()).getSession();
  }
}
