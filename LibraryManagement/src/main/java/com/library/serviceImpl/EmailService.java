package com.library.serviceImpl;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.library.model.custom.displayDto.RetrivingDto;

@Service
public class EmailService 
{
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(RetrivingDto member) throws MessagingException 
	{
		SimpleMailMessage message = new SimpleMailMessage();

		String subject = "Overdue Book Reminder";
		
		List<String> titles = Arrays.asList(member.getTitle().split(","));		
		List<String> authors = Arrays.asList(member.getAuthor().split(","));
		List<String> languages = Arrays.asList(member.getLanguage().split(","));
		
		
		String text =  "Dear " + member.getFirstName() + " " + member.getLastName() + ",\n\n"
					+ "This is a reminder that you have crossed overdue date for the below book.\n\n";
				
		for(int i = 0; i<titles.size(); i++)
		{
			text += "\n\nTitle    : " + titles.get(i)
			+ "\nAuthor   : " + authors.get(i)
			+ "\nLanguage : " + languages.get(i);
		}
				
		text += "\n\nPlease return them as soon as possible.\n"+		
				 "\nThanks and Regards,\nLibrary Management";	

		message.setFrom("bhuvanesh@humworld.in");
		message.setTo(member.getEmail());
		message.setSubject(subject);
		message.setText(text);

		javaMailSender.send(message);
	}
}
