package com.bouncingdata.plfdemo.service;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author techburg
 *
 */
public class DTMailSender {	
	private String content;
	private String title;
	Session session;
	String host = "smtp.gmail.com";
	String from = "bouncingdata";
	String pass = "T1234567";
	Transport transport;
	
	public DTMailSender(String title, String content) {				
		this.content = content;
		this.title = title;
	}
	
	public boolean init(){
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true"); // added this line
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		session = Session.getDefaultInstance(props, null);
		try {
			transport = session.getTransport("smtp");
			transport.connect(host, from, pass);

			return true;
			
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			
		}catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean close(){
		try {
			transport.close();
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean sendGroupMail(String[] targetAddrList){		
		
		String host = "smtp.gmail.com";
	    String from = "bouncingdata";
	    String pass = "T1234567";
	    Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.user", from);
	    props.put("mail.smtp.password", pass);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");

	   // String[] to = {target}; // added this line

	    Session session = Session.getDefaultInstance(props, null);
	    MimeMessage message = new MimeMessage(session);
	    try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[targetAddrList.length];

		    // To get the array of addresses
		    for( int i=0; i < targetAddrList.length; i++ ) { // changed from a while loop
		        toAddress[i] = new InternetAddress(targetAddrList[i]);
		       
		    }
		    System.out.println(Message.RecipientType.TO);

		    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
		        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		    }
		    message.setSubject(title);
		    message.setText(content);
		    Transport transport = session.getTransport("smtp");
		    transport.connect(host, from, pass);
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
		  
		} catch (AddressException e) {
			e.printStackTrace();
			//System.out.println("error." + e.getMessage());
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

	    return true;
	}
	
	public boolean sendEmail(String targetAddr){		
		
		String host = "smtp.gmail.com";
	    String from = "bouncingdata";
	    String pass = "T1234567";
	    Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.user", from);
	    props.put("mail.smtp.password", pass);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");

	    Session session = Session.getDefaultInstance(props, null);
	    MimeMessage message = new MimeMessage(session);
	    try {
			message.setFrom(new InternetAddress(from));
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(targetAddr));
		    message.setSubject(title);
		    message.setContent(content,"text/html");
		    Transport transport = session.getTransport("smtp");
		    transport.connect(host, from, pass);
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();		    
		} catch (AddressException e) {
			e.printStackTrace();
			
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			
			return false;
		}

	    return true;
	}
	
	
	public boolean sendEmailWithAttach(String to, String content, String fileAttachment){
		
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(title);
			
			// create the message part 
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//fill message
			messageBodyPart.setText(content);
			

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(fileAttachment);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileAttachment.split(File.separator)[fileAttachment.split(File.separator).length - 1]);
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);

			/*		// Send the message
				Transport.send( message );*/

			transport.sendMessage(message, message.getAllRecipients());
			
			return true;
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*public static void main(String[] args){
//		String target[] = {"vinhpq@techburgcorp.com","vinhpq177@gmail.com", "pham_quang_vinh1412@yahoo.com"};
		String target[] = {"vinhpq177@gmail.com"};
		String title = "Hello Chau. Your account is registered!";
		String content = "Click here to activate your account: www.vnexpress.net";
		
		
		
		*//** Build Register Email *//*
		DTMailSender sender = buildActivateMail();
		sender.sendEmail("vinhpq177@gmail.com");
		
		MailSender sender = new MailSender(title, content);
		sender.sendGroupMail(target);
		DTMailSender userReport = new DTMailSender("test from derivatrust", "test content");
		userReport.sendEmail("vinhpq@techburgcorp.com");
		
		String content = "<p>Dear ";
//		content += a.getDerivaName() + ",\n";
		content += "Duong" + ",</p>";
		content += "<p>Please find user usage report for last month on attached file.</p><p>Regards,</p>";
		
		DTMailSender userReport = new DTMailSender("DerivaTrust user monthly report", content);
		userReport.sendEmail("vinhpq@techburgcorp.com");
//		userReport.sendEmail(emailList.get(0).getEmailAddress());
		
	}*/
	
	private static DTMailSender buildActivateMail(){
		String title = "Please verify your bouncingdata account";
		String url = "www.vnexpress.net";
		String content = "'<p>'.T_(\"Click the following link or copy and paste it into your browser address field to activate your account\").'</p>"
     	+ "<p><a href=\"'" + url + ".'\">'.T_(\"Confirm account\").'</a></p><p>'" + url + "'</p>'";
		
		return new DTMailSender(title, content);
	}
	
}
