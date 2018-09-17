package com.smtp.mock;

import java.util.ArrayList;
import java.util.List;

import com.smtp.mock.entities.EmailMessage;

public class EmailMessageFactory {
  public static EmailMessage createMessage() {
    EmailMessage emailMessage = new EmailMessage();
    List<String> addresses = new ArrayList<>();
    addresses.add("test3001.rbbn@gmail.com");
    emailMessage.setAddress(addresses);
    emailMessage.setMessage("This is a test email");
    emailMessage.setSubject("Hello!");
    emailMessage.setMessageFormat("plainText");
    return emailMessage;
  }
}
