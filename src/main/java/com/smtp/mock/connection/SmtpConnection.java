package com.smtp.mock.connection;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportListener;
import javax.mail.internet.MimeMessage;

import com.smtp.mock.pool.SmtpConnectionPool;

/**
 * Smtp connection 
 * 
 * @author irlu
 *
 */
public class SmtpConnection {

  private final Transport transport;
  private SmtpConnectionPool objectPool;

  private final List<TransportListener> transportListeners = new ArrayList<>();

  public SmtpConnection(Transport delegate) {
    this.transport = delegate;
  }

  public void sendMessage(MimeMessage msg) throws MessagingException {
    doSend(msg, msg.getAllRecipients());
  }

  public void addTransportListener(TransportListener l) {
    transportListeners.add(l);
    transport.addTransportListener(l);
  }

  public void removeTransportListener(TransportListener l) {
    transportListeners.remove(l);
    transport.removeTransportListener(l);
  }


  public void clearListeners() {
    for (TransportListener transportListener : transportListeners) {
      transport.removeTransportListener(transportListener);
    }
    transportListeners.clear();
  }

  public boolean isConnected() {
    return transport.isConnected();
  }


  public void close() throws Exception {
    objectPool.returnObject(this);
  }

  public void setObjectPool(SmtpConnectionPool objectPool) {
    this.objectPool = objectPool;
  }

  public Transport getTransport() {
    return transport;
  }


  public Session getSession() {
    return objectPool.getSession();
  }


  private void doSend(MimeMessage mimeMessage, Address[] recipients) throws MessagingException {
    transport.sendMessage(mimeMessage, recipients);
  }


}
