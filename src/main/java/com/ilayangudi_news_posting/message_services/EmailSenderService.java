package com.ilayangudi_news_posting.message_services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendOTPToEmail(String toEmail, String name, String otp) {

		SimpleMailMessage messageOnMail = new SimpleMailMessage();

		messageOnMail.setTo(toEmail);
		messageOnMail.setSubject("இளையான்குடி நியூஸ் - உங்ளுடைய OTP உறுதிபடுத்துதல்");
		messageOnMail.setText(
				"அன்புள்ள " + name + ",\r\n" + "\r\n" + "இளையான்குடி நியூஸ்-ஐ தேர்ந்தெடுத்ததற்கு நன்றி.\r\n" + "\r\n"
						+ "மீறிய கடவுச்சொல் (Forget Password) உறுதிப்படுத்தலுக்கான உங்கள் ஒரே முறை கடவுச்சொல் (OTP): "
						+ otp + "\r\n" + "(இந்த OTP அடுத்த 10 நிமிடங்களுக்குள் மட்டுமே செல்லுபடியாகும்)\r\n" + "\r\n"
						+ "பாதுகாப்புக்காக, இந்த OTP-ஐ யாருடனும் பகிர்ந்துகொள்ள வேண்டாம்.\r\n" + "\r\n"
						+ "அன்புடன்,\r\n" + "Spicy Coding \r\n" + "Software Development \r\n" + "சென்னை");
		mailSender.send(messageOnMail);

	}

	public void sendEmailFromRegisteration(String toEmail, String name) {

		SimpleMailMessage messageOnMail = new SimpleMailMessage();

		messageOnMail.setTo(toEmail);
		messageOnMail.setSubject("இளையான்குடி நியூஸ் - வரவேற்கிறோம்!");
		messageOnMail
				.setText("அன்புள்ள " + name + ",\r\n" + "\r\n" + "இளையான்குடி நியூஸ்-ஐ தேர்ந்தெடுத்ததற்கு நன்றி.\r\n"
						+ "உங்கள் புதிய பயனர் பதிவு வெற்றிகரமாக முடிந்துள்ளது.\r\n" + "\r\n"
						+ "இளையான்குடி நியூஸ்-ஐப் பயன்படுத்தி நீங்கள் சமீபத்திய செய்திகளை நேரடியாக பெறலாம்.\r\n"
						+ "உங்களுடைய ஆதரவை எங்களுக்கு தொடர்ந்து தாருங்கள். \r\n" + "நன்றி! \r\n" + "\r\n"
						+ "அன்புடன்,\r\n" + "Spicy Coding \r\n" + "Software Development \r\n" + "சென்னை");
		mailSender.send(messageOnMail);

	}

	public void sendEmailFromRegisterVerify(String toEmail, String otp) {

		SimpleMailMessage messageOnMail = new SimpleMailMessage();

		messageOnMail.setTo(toEmail);
		messageOnMail.setSubject("இளையான்குடி நியூஸ் - மின்னஞ்சல் சரிபார்ப்பு");
		messageOnMail.setText("அன்புள்ள பயனர்,\r\n" + "\r\n"
				+ "இளையான்குடி நியூஸ்-ல் உங்கள் புதிய பயனர் பதிவு தொடங்கப்பட்டிருக்கிறது.\r\n"
				+ "உங்கள் மின்னஞ்சல் சரிபார்க்க கீழே உள்ள OTP ஐ பயன்படுத்தவும்:\r\n" + "\r\n" + "OTP: " + otp + "\r\n"
				+ "\r\n" + "இந்த OTP 5 நிமிடங்கள் மட்டுமே செல்லுபடியாகும்.\r\n" + "\r\n"
				+ "உங்கள் மின்னஞ்சல் சரிபார்க்கப்பட்ட பிறகு, நீங்கள் உங்கள் கணக்கிற்கு முழு அணுகலைப் பெறுவீர்கள்.\r\n"
				+ "\r\n" + "பாதுகாப்புக்காக, இந்த OTP-ஐ யாருடனும் பகிர்ந்துகொள்ள வேண்டாம்.\r\n" + "\r\n"
				+ "உங்களுடைய ஆதரவை எங்களுக்கு தொடர்ந்து தாருங்கள்.\r\n" + "\r\n" + "நன்றி!\r\n" + "\r\n"
				+ "அன்புடன்,\r\n" + "இளையான்குடி நியூஸ் குழு\r\n" + "Spicy Coding – Software Development, சென்னை");
		mailSender.send(messageOnMail);

	}

	@Async
	public CompletableFuture<Boolean> sendEmailPostReportReminderFromNewStatus(String toEmail, String userName,
			Long newsNo, String newsTitle, Long reportTokenNo, String reportReason) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(toEmail);
			message.setSubject("இளையான்குடி நியூஸ் - செய்தி புகார் அறிவிப்பு");

			message.setText("அன்புள்ள " + userName + ",\n\n" + "நீங்கள் வெளியிட்ட செய்தி எண்: " + newsNo + " - '"
					+ newsTitle + "' மீது புதிய புகார் சேர்க்கப்பட்டது.\n\n" + "புகாரின் காரணம்: " + reportReason + "\n"
					+ "புகார் குறிப்பு எண்: " + reportTokenNo + "\n\n"
					+ "இது குறித்து இளையான்குடி நியூஸ் குழுவும் பரிசீலனை செய்யும். \n\n"
					+ "நீங்களும் தயவு செய்து அந்த செய்தியை சரிபார்த்து தேவையான நடவடிக்கைகளை எடுக்கவும்.\n\n"
					+ "மேலும் எதேனும் சந்தேகங்கள் இருக்கும் பட்ச்சத்தில் எங்களை தொடர்பு கொள்ளவும். \n\n" + "நன்றி!\n\n"
					+ "அன்புடன்,\n" + "இளையான்குடி நியூஸ் குழு\n" + "Spicy Coding – Software Development, சென்னை");

			mailSender.send(message);
			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			log.error("Mail failed for {}: {}", toEmail, e.getMessage(), e);
			return CompletableFuture.completedFuture(false);
		}

	}

	@Async
	public CompletableFuture<Boolean> sendEmailPostReportReminderFromReviewedStatus(String toEmail, String userName,
			Long newsNo, String newsTitle, Long reportTokenNo, String reportReason) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("இளையான்குடி நியூஸ் - செய்தி புகார் பரிசீலனை முடிவு");

			message.setText("அன்புள்ள " + userName + ",\n\n" + "நீங்கள் வெளியிட்ட செய்தி எண்: " + newsNo + " - '"
					+ newsTitle + "' மீது பதிவு செய்யப்பட்ட புகார் எங்கள் குழுவால் பரிசீலிக்கப்பட்டது.\n\n"
					+ "புகாரின் காரணம்: " + reportReason + "\n" + "புகார் குறிப்பு எண்: " + reportTokenNo + "\n\n"
					+ "இந்த புகார் சரியானதாக இருப்பது உறுதி செய்யப்பட்டுள்ளது.\n\n"
					+ "👉 எனவே, அடுத்த 12 மணி நேரத்திற்குள் தயவு செய்து அந்த செய்தியை நீக்கவும்.\n"
					+ "அப்படிச் செய்யாவிட்டால், எங்கள் குழுவே நேரடியாக அந்த செய்தியை நீக்கி, உங்கள் கணக்கை 1 வாரத்திற்கு முடக்கும் நடவடிக்கை எடுக்கப்படும்.\n\n"
					+ "⚠️ கவனிக்கவும்: இந்த செயல்பாடு எங்கள் விதிமுறைகள் மற்றும் நிபந்தனைகளுக்கு புறம்பானது.\n"
					+ "மேலும் மீறல்கள் கண்டறியப்பட்டால், உங்கள் கணக்கை நிரந்தரமாக இடைநீக்கம் செய்யப்படும் வாய்ப்பு உள்ளது.\n\n"
					+ "தயவுசெய்து இத்தகவலை மிகுந்த முக்கியத்துடன் கருதுங்கள்.\n\n" + "நன்றி!\n\n" + "அன்புடன்,\n"
					+ "இளையான்குடி நியூஸ் குழு\n" + "Spicy Coding – Software Development, சென்னை");

			mailSender.send(message);
			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			log.error("Mail failed for {}: {}", toEmail, e.getMessage(), e);
			return CompletableFuture.completedFuture(false);
		}
	}

	@Async
	public CompletableFuture<Boolean> sendEmailPostReportReminderFromRejectedStatus(String toEmail, String userName,
			Long newsNo, String newsTitle, Long reportTokenNo, String reportReason) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("இளையான்குடி நியூஸ் - செய்தி புகார் பரிசீலனை முடிவு");

			message.setText("அன்புள்ள " + userName + ",\n\n" + "நீங்கள் வெளியிட்ட செய்தி எண்: " + newsNo + " - '"
					+ newsTitle + "' மீது பதிவு செய்யப்பட்ட புகாரை எங்கள் குழு பரிசீலித்தது.\n\n" + "புகாரின் காரணம்: "
					+ reportReason + "\n" + "புகார் குறிப்பு எண்: " + reportTokenNo + "\n\n"
					+ "🔎 எங்கள் பரிசீலனைக்குப் பிறகு, இந்த புகார் தவறானது என்று உறுதி செய்யப்பட்டுள்ளது.\n"
					+ "அதனால், உங்கள் செய்தி எந்த விதத்திலும் நீக்கப்படாமல் தொடர்ந்தும் காணப்படும்.\n\n"
					+ "🤝 தயவுசெய்து கவலைப்பட வேண்டாம்.\n"
					+ "இளையான்குடி நியூஸ் எப்போதும் உண்மைச் செய்திகளைப் பாதுகாக்கும்.\n\n"
					+ "⚖️ எங்கள் விதிமுறைகள் மற்றும் நிபந்தனைகள் படி, உண்மைச் செய்திகளை தொடர்ந்து வெளியிடுவதை ஊக்குவிக்கிறோம்.\n"
					+ "உங்களின் செய்தியாளர் முயற்சிகள் எங்கள் இளையான்குடி நியூஸ் குழுவுக்கு மிக முக்கியமானவை.\n\n"
					+ "எப்போதும் போல, மேலும் செய்திகள் வெளியிட்டு எங்களை ஆதரிக்கவும்.\n"
					+ "உங்களின் பங்களிப்புக்கு எங்கள் நன்றிகள்!\n\n" + "அன்புடன்,\n" + "இளையான்குடி நியூஸ் குழு\n"
					+ "Spicy Coding – Software Development, சென்னை");

			mailSender.send(message);
			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			log.error("Mail failed for {}: {}", toEmail, e.getMessage(), e);
			return CompletableFuture.completedFuture(false);
		}
	}

	@Async
	public CompletableFuture<Boolean> sendEmailPostReporterReminderFromRejectedStatus(String toEmail, String userName,
			Long newsNo, String newsTitle, Long reportTokenNo, String reportReason) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("இளையான்குடி நியூஸ் - நீங்கள் புகார் செய்த செய்தி பரிசீலனை முடிவு");
			message.setText("அன்புள்ள " + userName + ",\n\n" + "நீங்கள் புகார் செய்த செய்தி எண்: " + newsNo + " - '"
					+ newsTitle + "' மீது பதிவு செய்யப்பட்ட புகாரை எங்கள் குழு பரிசீலித்தது.\n\n" + "புகாரின் காரணம்: "
					+ reportReason + "\n" + "புகார் குறிப்பு எண்: " + reportTokenNo + "\n\n"
					+ "🔎 எங்கள் பரிசீலனைக்குப் பிறகு, இந்த புகார் தவறானதாகும் என்று உறுதி செய்யப்பட்டுள்ளது.\n"
					+ "அதனால், இந்த புகாரின் அடிப்படையில் எங்களின் எந்த நடவடிக்கையும் தேவையில்லை.\n\n"
					+ "🤝 தயவுசெய்து கவலைப்பட வேண்டாம். உங்கள் பங்களிப்பு மதிப்பிடத்தக்கது.\n\n"
					+ "⚖️ எங்கள் விதிமுறைகள் மற்றும் நிபந்தனைகள் படி, உண்மைச் செய்திகள் மட்டுமே புகார் செய்ய வேண்டும்.\n"
					+ "எதிர்காலத்தில் தவறான புகார்களைத் தவிர்க்கவும்.\n\n"
					+ "நீங்கள் எப்போதும் போல உண்மைச் செய்திகள் மற்றும் சமூகப் பொறுப்புடன் செயல்படுவீர்கள் என்று நம்புகிறோம்.\n\n"
					+ "நன்றி!\n\n" + "அன்புடன்,\n" + "இளையான்குடி நியூஸ் குழு\n"
					+ "Spicy Coding – Software Development, சென்னை");

			mailSender.send(message);
			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			log.error("Mail failed for {}: {}", toEmail, e.getMessage(), e);
			return CompletableFuture.completedFuture(false);
		}
	}

	@Async
	public CompletableFuture<Boolean> sendUserSuspensionMail(String toEmail, String userName) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("இளையான்குடி நியூஸ் - உங்கள் கணக்கு தற்காலிகமாக முடக்கப்பட்டுள்ளது");
			message.setText("அன்புள்ள " + userName + ",\n\n"
					+ "உங்கள் சில செய்திகளில் விதிமுறைகள் மீறல் இருப்பதால், உங்கள் கணக்கு தற்காலிகமாக முடக்கப்பட்டுள்ளது.\n\n"
					+ "⏳ முடக்கப்பட்ட காலம்: 5 நாட்கள்\n"
					+ "அந்த காலம் முடிந்தவுடன் உங்கள் கணக்கு தானாகவே மீண்டும் செயல்படுத்தப்படும்.\n\n"
					+ "தயவுசெய்து எதிர்காலத்தில் நியூஸ் வெளியிடும்போது எங்கள் சமூக விதிமுறைகள் மற்றும் நம்பகத்தன்மை வழிகாட்டுதல்களை பின்பற்றவும்.\n\n"
					+ "இது ஒரு தற்காலிக நடவடிக்கை மட்டுமே, எதிர்காலத்தில் நம்பகமான பங்களிப்பை எதிர்நோக்குகிறோம்.\n\n"
					+ "அன்புடன்,\n" + "இளையான்குடி நியூஸ் குழு\n" + "Spicy Coding – Software Development, சென்னை");

			mailSender.send(message);
			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			log.error("📧 Suspension mail failed for {}: {}", toEmail, e.getMessage(), e);
			return CompletableFuture.completedFuture(false);
		}
	}

	@Async
	public CompletableFuture<Boolean> sendUserReactivationMail(String toEmail, String userName) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toEmail);
			message.setSubject("இளையான்குடி நியூஸ் - உங்கள் கணக்கு மீண்டும் செயல்படுத்தப்பட்டது");

			message.setText("அன்புள்ள " + userName + ",\n\n"
					+ "உங்கள் கணக்கின் தற்காலிக முடக்கம் 5 நாட்கள் நிறைவடைந்ததால், "
					+ "உங்கள் கணக்கு மீண்டும் செயல்படுத்தப்பட்டுள்ளது.\n\n"
					+ "✅ இனி நீங்கள் மீண்டும் செய்திகளை பதிவிடலாம்.\n"
					+ "தயவுசெய்து எதிர்காலத்தில் விதிமுறைகள் மற்றும் நம்பகத்தன்மை வழிகாட்டுதல்களை பின்பற்றவும்.\n\n"
					+ "உங்கள் ஒத்துழைப்புக்கு நன்றி.\n\n" + "அன்புடன்,\n" + "இளையான்குடி நியூஸ் குழு\n"
					+ "Spicy Coding – Software Development, சென்னை");

			mailSender.send(message);
			return CompletableFuture.completedFuture(true);
		} catch (Exception e) {
			log.error("📧 Reactivation mail failed for {}: {}", toEmail, e.getMessage(), e);
			return CompletableFuture.completedFuture(false);
		}
	}

}
