package com.ilayangudi_news_posting.message_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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
	
	public void sendEmailFromRegisterVerify(String toEmail, String otp) {
			
			SimpleMailMessage messageOnMail = new SimpleMailMessage();
			
			messageOnMail.setTo(toEmail);
			messageOnMail.setSubject("இளையான்குடி நியூஸ் - மின்னஞ்சல் சரிபார்ப்பு");
			messageOnMail.setText(
			    "அன்புள்ள பயனர்,\r\n" +
			    "\r\n" +
			    "இளையான்குடி நியூஸ்-ல் உங்கள் புதிய பயனர் பதிவு தொடங்கப்பட்டிருக்கிறது.\r\n" +
			    "உங்கள் மின்னஞ்சல் சரிபார்க்க கீழே உள்ள OTP ஐ பயன்படுத்தவும்:\r\n" +
			    "\r\n" +
			    "OTP: " + otp + "\r\n" +
			    "\r\n" +
			    "இந்த OTP 5 நிமிடங்கள் மட்டுமே செல்லுபடியாகும்.\r\n" +
			    "\r\n" +
			    "உங்கள் மின்னஞ்சல் சரிபார்க்கப்பட்ட பிறகு, நீங்கள் உங்கள் கணக்கிற்கு முழு அணுகலைப் பெறுவீர்கள்.\r\n" +
			    "\r\n" +
			    "பாதுகாப்புக்காக, இந்த OTP-ஐ யாருடனும் பகிர்ந்துகொள்ள வேண்டாம்.\r\n" +
			    "\r\n" +
			    "உங்களுடைய ஆதரவை எங்களுக்கு தொடர்ந்து தாருங்கள்.\r\n" +
			    "\r\n" +
			    "நன்றி!\r\n" +
			    "\r\n" +
			    "அன்புடன்,\r\n" +
			    "இளையான்குடி நியூஸ் குழு\r\n" +
			    "Spicy Coding – Software Development, சென்னை"
			);
			mailSender.send(messageOnMail);
			
		}
	
	@Async
	public void sendEmailFromReportOurPost(String toEmail, String userName, Long newsNo, String newsTitle, Long reportTokenNo, String reportReason) {
		
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(toEmail);
		message.setSubject("இளையான்குடி நியூஸ் - செய்தி புகார் அறிவிப்பு");

		message.setText(
		    "அன்புள்ள "+userName+",\n\n" +
		    "நீங்கள் வெளியிட்ட செய்தி எண்: " + newsNo + " - '" + newsTitle + "' மீது புதிய புகார் சேர்க்கப்பட்டது.\n\n" +
		    "புகாரின் காரணம்: " + reportReason + "\n" +
		    "புகார் குறிப்பு எண்: " + reportTokenNo + "\n\n" +
		    "இது குறித்து இளையான்குடி நியூஸ் குழுவும் பரிசீலனை செய்யும். \n\n"+
		    "நீங்களும் தயவு செய்து அந்த செய்தியை சரிபார்த்து தேவையான நடவடிக்கைகளை எடுக்கவும்.\n\n" +
		    "மேலும் எதேனும் சந்தேகங்கள் இருக்கும் பட்ச்சத்தில் எங்களை தொடர்பு கொள்ளவும். \n\n"+
		    "நன்றி!\n\n" +
		    "அன்புடன்,\n" +
		    "இளையான்குடி நியூஸ் குழு\n" +
		    "Spicy Coding – Software Development, சென்னை"
		);

		mailSender.send(message);
		
	}
	

}
