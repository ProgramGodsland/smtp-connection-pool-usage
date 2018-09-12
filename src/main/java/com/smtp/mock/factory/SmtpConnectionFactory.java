package com.smtp.mock.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportListener;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smtp.mock.connection.SmtpConnection;

/**
 * A part of the code of this class is taken from the Spring <a href=
 * "http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSenderImpl.html">JavaMailSenderImpl
 * class</a>.
 */
public class SmtpConnectionFactory implements PooledObjectFactory<SmtpConnection> {

  private static final Logger LOG = LoggerFactory.getLogger(SmtpConnectionFactory.class);

  protected final Session session;

  protected Collection<TransportListener> defaultTransportListeners;

  public SmtpConnectionFactory(Session session,
      Collection<TransportListener> defaultTransportListeners) {
    this.session = session;
    this.defaultTransportListeners = new ArrayList<>(defaultTransportListeners);
  }
  

  public SmtpConnectionFactory(Session session) {
    this(session, Collections.<TransportListener>emptyList());
  }


  @Override
  public PooledObject<SmtpConnection> makeObject() throws Exception {
    LOG.debug("makeObject");

    Transport transport = session.getTransport();
    transport.connect();

    SmtpConnection closableSmtpTransport =
        new SmtpConnection(transport);
    initDefaultListeners(closableSmtpTransport);

    return new DefaultPooledObject(closableSmtpTransport);
  }

  @Override
  public void destroyObject(PooledObject<SmtpConnection> pooledObject) throws Exception {
    try {
      if (LOG.isDebugEnabled()) {
        LOG.debug("destroyObject [{}]", pooledObject.getObject().isConnected());
      }
      pooledObject.getObject().clearListeners();
      pooledObject.getObject().getTransport().close();
    } catch (Exception e) {
      LOG.warn(e.getMessage(), e);
    }
  }

  @Override
  public boolean validateObject(PooledObject<SmtpConnection> pooledObject) {
    boolean connected = pooledObject.getObject().isConnected();
    LOG.debug("Is connected [{}]", connected);
    return connected;
  }


  @Override
  public void activateObject(PooledObject<SmtpConnection> pooledObject) throws Exception {
    initDefaultListeners(pooledObject.getObject());
  }

  @Override
  public void passivateObject(PooledObject<SmtpConnection> pooledObject) throws Exception {
    if (LOG.isDebugEnabled()) {
      LOG.debug("passivateObject [{}]", pooledObject.getObject().isConnected());
    }
    pooledObject.getObject().clearListeners();
  }


  public void setDefaultListeners(Collection<TransportListener> listeners) {
    defaultTransportListeners = new CopyOnWriteArrayList<>(Objects.requireNonNull(listeners));
  }

  public List<TransportListener> getDefaultListeners() {
    return new ArrayList<>(defaultTransportListeners);
  }

  public Session getSession() {
    return session;
  }

  private void initDefaultListeners(SmtpConnection smtpTransport) {
    for (TransportListener transportListener : defaultTransportListeners) {
      System.err.println("Smtp connection listener: " + transportListener);
      smtpTransport.addTransportListener(transportListener);
    }
  }
}
