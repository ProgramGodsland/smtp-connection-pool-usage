package com.smtp.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smtp.mock.entities.EmailMessage;
import com.smtp.mock.service.SmtpPoolService;
import com.smtp.mock.service.SmtpService;

@RestController
@RequestMapping("/cpaas/email/v1")
public class SmtpController {
	@Autowired
	private SmtpPoolService poolService;

	@Autowired
	private SmtpService service;

	@RequestMapping(value = "/{userId}/messages", method = RequestMethod.POST)
	public ResponseEntity<EmailMessage> postNotify(@RequestBody EmailMessage request,
			@PathVariable("userId") final String userId) {
		// EmailMessage msg = poolService.send(userId, request);
		EmailMessage msg = service.send(userId, request);
		return new ResponseEntity<EmailMessage>(msg, HttpStatus.OK);
	}
}