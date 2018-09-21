package com.smtp.mock.pool;

import javax.mail.Session;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.smtp.mock.config.SmtpPoolConfig;
import com.smtp.mock.connection.SmtpConnection;
import com.smtp.mock.factory.SmtpConnectionFactory;

/**
 * Preparing a smtp connection thread pool
 * 
 * @author irlu
 *
 */
public class SmtpConnectionPool extends GenericObjectPool<SmtpConnection> {

  private SmtpPoolConfig config;

  public SmtpConnectionPool(SmtpConnectionFactory factory) {
    super(factory);
  }

  public SmtpConnectionPool(SmtpConnectionFactory factory, SmtpPoolConfig config) {
    super(factory, config);
    this.config = config;
  }

  public SmtpConnectionPool(SmtpConnectionFactory factory, SmtpPoolConfig config,
      AbandonedConfig abandonedConfig) {
    super(factory, config, abandonedConfig);
  }

  public void setConfig(SmtpPoolConfig config) {
    this.config = config;
  }

  public void init() throws Exception {
    if (config == null) {
      System.err.println("Add one object!");
      addObject();
    }
    for (int i = 0; i < config.getMaxTotal(); i++) {
      System.err.println("Add object! ");
      addObject();
    }
  }

  @Override
  public SmtpConnection borrowObject() throws Exception {
    SmtpConnection object = super.borrowObject();
    System.err.println("Required object: " + object);
//    if (object == null) {
//      for(int i = 0; i < 10; i++) {
//        addObject();
//      }
//      object = super.borrowObject();
//      System.err.println("Is Connection still open: " + object.isConnected() + " active number: "
//          + getNumActive() + " idled number: " + getNumIdle());
//    }
    if (!object.isConnected()) {
      System.err.println("Is Connection still open: " + object.isConnected());
      object.getTransport().connect();

      System.err.println("Reconnected: " + object.isConnected());
    }
    object.setObjectPool(this);
    return object;
  }

  @Override
  public SmtpConnection borrowObject(long borrowMaxWaitMillis) throws Exception {
    SmtpConnection object = super.borrowObject(borrowMaxWaitMillis);
    System.err.println("Required object: " + object);
    object.setObjectPool(this);
    return object;
  }

  @Override
  public void returnObject(SmtpConnection connection) {
    int activeNum = getNumActive();
    
    super.returnObject(connection);
    System.err.println("Active number: " + activeNum + " idled number: " + getNumIdle()
    + " total number: " + getNumWaiters());
  }

  public Session getSession() {
    return ((SmtpConnectionFactory) getFactory()).getSession();
  }
}
