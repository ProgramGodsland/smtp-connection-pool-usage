package com.smtp.mock.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smtp.mock.entities.EmailMessage;

@Service
public class SmtpService {

	@Autowired
	private SmtpEmailSender smtpSender;

	private void send(List<String> to, String subject, String text) throws MessagingException {
		Session session = smtpSender.setEmailSession();

		MimeMessage mimeMessage = new MimeMessage(session);
		if (to.size() == 1) {
			mimeMessage.setRecipients(RecipientType.TO, to.get(0));
		} else {
			String dest = to.stream().collect(Collectors.joining(","));
			mimeMessage.setRecipients(RecipientType.TO, dest);
		}
		mimeMessage.setSubject(subject);
		mimeMessage.setText(text);
		Transport.send(mimeMessage);
	}

	public EmailMessage send(String userId, EmailMessage emailMessage) {
		if (emailMessage == null) {
			System.err.println("No requested message!!");
			return null;
		}

		try {
			send(emailMessage.getAddress(), emailMessage.getSubject(), emailMessage.getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String messageId = UUID.randomUUID().toString();
		String resourceURL = String.format("/cpaas/email/%s/messages/%s", userId, messageId);
		emailMessage.setResourceURL(resourceURL);
		return emailMessage;
	}
}
