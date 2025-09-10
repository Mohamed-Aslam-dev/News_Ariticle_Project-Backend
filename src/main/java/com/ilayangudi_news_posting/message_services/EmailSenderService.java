package com.ilayangudi_news_posting.message_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;

	public void sendOTPToEmail(String toEmail, String name, String otp) {

		SimpleMailMessage messageOnMail = new SimpleMailMessage();

		messageOnMail.setTo(toEmail);
		messageOnMail.setSubject("இளையான்குடி நியூஸ் - உங்ளுடைய OTP உறுதிபடுத்துதல்");
		messageOnMail.setText("அன்புள்ள "+name+",\r\n"
				+ "\r\n"
				+ "இளையான்குடி நியூஸ்-ஐ தேர்ந்தெடுத்ததற்கு நன்றி.\r\n"
				+ "\r\n"
				+ "மீறிய கடவுச்சொல் (Forget Password) உறுதிப்படுத்தலுக்கான உங்கள் ஒரே முறை கடவுச்சொல் (OTP): "+otp+"\r\n"
				+ "(இந்த OTP அடுத்த 10 நிமிடங்களுக்குள் மட்டுமே செல்லுபடியாகும்)\r\n"
				+ "\r\n"
				+ "பாதுகாப்புக்காக, இந்த OTP-ஐ யாருடனும் பகிர்ந்துகொள்ள வேண்டாம்.\r\n"
				+ "\r\n"
				+ "அன்புடன்,\r\n"
				+ "Spicy Coding \r\n"
				+ "Software Development \r\n"
				+ "சென்னை");
		mailSender.send(messageOnMail);

	}
	
	public void sendEmailFromRegisteration(String toEmail, String name) {
		
		SimpleMailMessage messageOnMail = new SimpleMailMessage();
		
		messageOnMail.setTo(toEmail);
		messageOnMail.setSubject("இளையான்குடி நியூஸ் - வரவேற்கிறோம்!");
	    messageOnMail.setText(
	        "அன்புள்ள " + name + ",\r\n" +
	        "\r\n" +
	        "இளையான்குடி நியூஸ்-ஐ தேர்ந்தெடுத்ததற்கு நன்றி.\r\n" +
	        "உங்கள் புதிய பயனர் பதிவு வெற்றிகரமாக முடிந்துள்ளது.\r\n" +
	        "\r\n" +
	        "இளையான்குடி நியூஸ்-ஐப் பயன்படுத்தி நீங்கள் சமீபத்திய செய்திகளை நேரடியாக பெறலாம்.\r\n"
	        +"உங்களுடைய ஆதரவை எங்களுக்கு தொடர்ந்து தாருங்கள். \r\n"
	        +"நன்றி! \r\n" +
	        "\r\n" +
	        "அன்புடன்,\r\n" +
	        "Spicy Coding \r\n" +
	        "Software Development \r\n" +
	        "சென்னை"
	    );
		mailSender.send(messageOnMail);
		
	}
	

}
