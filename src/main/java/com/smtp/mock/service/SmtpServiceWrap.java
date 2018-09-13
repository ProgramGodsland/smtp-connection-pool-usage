package com.smtp.mock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smtp.mock.entities.EmailMessage;

@Service
public class SmtpServiceWrap {
  @Autowired
  private SmtpPoolService smtpSvc;

  @Autowired
  private SmtpService service;

  public EmailMessage sendThroughPool(String userId, EmailMessage request) {
    if (request == null) {
      System.err.println("No requested message!!");
      return null;
    }
    return smtpSvc.send(userId, request);
  }

  public EmailMessage send(String userId, EmailMessage request) {
    if (request == null) {
      System.err.println("No requested message!!");
      return null;
    }
    return service.send(userId, request);
  }
}
