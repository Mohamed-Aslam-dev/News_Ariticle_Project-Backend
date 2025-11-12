package com.ilayangudi_news_posting.message_services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class NotificationService {

	@Autowired
	private UserRegisterDataRepository userRegisterDataRepo;

	@PostConstruct
	public void init() throws IOException {
		// ✅ Initialize Firebase Admin SDK (only once)
		if (FirebaseApp.getApps().isEmpty()) {
			FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-service-account.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).setProjectId("article-project-7be0f")
					.build();
			FirebaseApp.initializeApp(options);
		}
	}

	public void sendWelcomeNotification(String deviceToken, String userName) {
		try {
			Message message = Message.builder().setToken(deviceToken)
					.setNotification(Notification.builder().setTitle(userName + ", Welcome 🎉")
							.setBody("Welcome to ILY Connect. Stay updated with latest info!").build())
					.build();

			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("✅ Notification sent: " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendCustomNotification(String deviceToken, String title, String body, String userEmail) {
		try {
			Message message = Message.builder().setToken(deviceToken)
					.setNotification(Notification.builder().setTitle(title).setBody(body).build()).build();

			System.out.println("📱 Sending notification to token: " + deviceToken);
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("✅ Notification sent: " + response);

		} catch (FirebaseMessagingException e) {
			if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
				// 🧹 Remove invalid token from DB
				userRegisterDataRepo.clearDeviceToken(userEmail);
				System.out.println("⚠️ Removed invalid FCM token for user: " + userEmail);
			} else {
				e.printStackTrace();
			}
		}
	}

}
