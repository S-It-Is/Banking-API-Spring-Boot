package com.project.bank.service;



import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Autowired
	JavaMailSender javaMail;
	
	public void sendMail(String toEmail, String body, String subject, ByteArrayOutputStream outputStream, String fileName) throws MessagingException {
	    MimeMessage mimeMessage = javaMail.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
	    mimeMessageHelper.setFrom("santhoshk65656@gmail.com");
	    mimeMessageHelper.setTo(toEmail);
	    mimeMessageHelper.setText(body);
	    mimeMessageHelper.setSubject(subject);

	    mimeMessageHelper.addAttachment(fileName, new ByteArrayResource(outputStream.toByteArray()));


	    javaMail.send(mimeMessage);

	    System.out.println("Mail Sent Successfully");
	}
	

}
