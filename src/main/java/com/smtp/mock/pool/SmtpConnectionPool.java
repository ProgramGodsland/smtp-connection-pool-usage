package com.smtp.mock.pool;

import javax.mail.Session;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.smtp.mock.connection.ClosableSmtpConnection;
import com.smtp.mock.factory.SmtpConnectionFactory;

/**
 * Created by nlabrot on 30/04/15.
 */
public class SmtpConnectionPool extends GenericObjectPool<ClosableSmtpConnection> {

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
  public ClosableSmtpConnection borrowObject() throws Exception {
    ClosableSmtpConnection object = super.borrowObject();
    if (object instanceof ObjectPoolAware) {
      ((ObjectPoolAware) object).setObjectPool(this);
    }
    return object;
  }

  @Override
  public ClosableSmtpConnection borrowObject(long borrowMaxWaitMillis) throws Exception {
    ClosableSmtpConnection object = super.borrowObject(borrowMaxWaitMillis);
    if (object instanceof ObjectPoolAware) {
      ((ObjectPoolAware) object).setObjectPool(this);
    }
    return object;
  }

  public Session getSession() {
    return ((SmtpConnectionFactory) getFactory()).getSession();
  }


}
