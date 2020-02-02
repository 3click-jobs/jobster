package com.iktpreobuka.jobster.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.UserEntity;

@Service
public class EmailDaoImpl implements EmailDao {
	
	@Autowired
	public JavaMailSender emailSender;
	
	@Override
	public void sendCommentAddedEmail(UserEntity u) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(u.getEmail());
		helper.setSubject("You received a new comment");
		String text = "Please check the application to see the comment";
		helper.setText(text, true);
		emailSender.send(mail);

	}
	
	@Override
	public void sendCommentEditedEmail(UserEntity u) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(u.getEmail());
		helper.setSubject("One of the comments you recevied was edited by the comment creator");
		String text = "Please check the application to see the comment";
		helper.setText(text, true);
		emailSender.send(mail);

	}
	
	@Override
	public void sendCommentDeletedEmail(UserEntity u) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(u.getEmail());
		helper.setSubject("One of the comments you recevied has been deleted");
		String text = "Please check the application";
		helper.setText(text, true);
		emailSender.send(mail);

	}
	
	@Override
	public void sendCommentUndeleteEmail(UserEntity u) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(u.getEmail());
		helper.setSubject("Comment restored!");
		String text = "Please check the application to see the changes";
		helper.setText(text, true);
		emailSender.send(mail);

	}
	

	public void testEmailSending() throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo("jobster.testing@gmail.com");
		helper.setSubject("Email Sending Test!");
		String text = "<h1>NICE!!!!</h1>";
		helper.setText(text, true);
		emailSender.send(mail);
	}
	
	

}
