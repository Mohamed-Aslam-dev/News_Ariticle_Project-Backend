package com.ilayangudi_news_posting.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.file_service.NewsImageAndVideoFile;
import com.ilayangudi_news_posting.message_services.EmailSenderService;
import com.ilayangudi_news_posting.message_services.OtpGenerateService;
import com.ilayangudi_news_posting.message_services.SmsService;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.request_dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.request_dto.ForgetPasswordRequestDTO;
import com.ilayangudi_news_posting.request_dto.UserRegisterDTO;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;

@Service
public class UserRegisterDataServiceImpl implements UserRegisterDataServiceRepository {

//    private final AppConfiguration appConfiguration;

	@Autowired
	private UserRegisterDataRepository userDataRepo;
	
	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private NewsImageAndVideoFile newsFileStore;

	@Autowired
	private SmsService smsService;

	@Autowired
	private OtpGenerateService otpGenerateService;

//    UserRegisterDataServiceImpl(AppConfiguration appConfiguration) {
//        this.appConfiguration = appConfiguration;
//    }

	@Override
	public void addNewUser(UserRegisterDTO userDataDto, MultipartFile profilePic) {

		// Check duplicate by email
		if (userDataRepo.existsByEmailId(userDataDto.getEmailId())) {
			throw new RuntimeException("இந்த மின்னஞ்சல் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");
		}

		// Check duplicate by mobile
		if (userDataRepo.existsByUserMobileNumber(userDataDto.getUserMobileNumber())) {
			throw new RuntimeException("இந்த மொபைல் எண் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");
		}
		try {
			UserRegisterData userRegisterdata = new UserRegisterData();
			if (profilePic != null && !profilePic.isEmpty()) {
				String uploadDir = "D:/Users/userProfilePics/";
				String imagePath = newsFileStore.getNewsImageAndVideoFilepath(profilePic, uploadDir);
				userRegisterdata.setProfilePicUrl(imagePath);
			} else {
				userRegisterdata.setProfilePicUrl(null);
			}

			userRegisterdata.setUserName(userDataDto.getUserName());
			userRegisterdata.setEmailId(userDataDto.getEmailId());
			userRegisterdata.setUserMobileNumber(userDataDto.getUserMobileNumber());
			userRegisterdata.setPassword(passwordEncoder.encode(userDataDto.getPassword()));
			userRegisterdata.setRole("USER");

			emailSenderService.sendEmailFromRegisteration(userRegisterdata.getEmailId(), userRegisterdata.getUserName());
			
			userDataRepo.save(userRegisterdata);	
			
			
		} catch (IOException e) {
			// log & rethrow as runtime so global handler can catch
			throw new RuntimeException("Error while saving profile pic", e);
		}

	}

	@Override
	public boolean generateResetToken(ForgetPasswordRequestDTO forgetPasswordRequest) {
		Optional<UserRegisterData> optUser = userDataRepo
				.findByEmailIdOrUserMobileNumber(forgetPasswordRequest.getEmailOrPhone());

		if (optUser.isPresent()) {
			UserRegisterData user = optUser.get();

			String token = otpGenerateService.generateToken();
			LocalDateTime expiry = LocalDateTime.now().plusMinutes(10); // 10 mins expiry
			//Mobile SMS
//			String otpSmsMessage = "[SpicyCoding] Hi "+user.getUserName()+", your OTP is: " + token
//					+ " (valid for 10 mins). Please do not share.";
//			System.out.println(user.getUserMobileNumber());
//			smsService.sendOtp(otpSmsMessage, user.getUserMobileNumber());

			user.setResetToken(token);
			user.setResetTokenExpiry(expiry);
			userDataRepo.save(user);
			
			emailSenderService.sendOTPToEmail(user.getEmailId(), user.getUserName(), token);

			// TODO: send token via Email/SMS (later you can integrate)

			System.out.println("Reset Token: " + token);

			return true;
		}
		return false;
	}

	@Override
	public boolean resetPasswordWithToken(ForgetPasswordDto forgetPasswordDto) {
		Optional<UserRegisterData> optUser = userDataRepo.findByResetToken(forgetPasswordDto.getResetToken());

		if (optUser.isPresent()) {
			UserRegisterData user = optUser.get();

			if (user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
				user.setPassword(passwordEncoder.encode(forgetPasswordDto.getNewPassword()));
				user.setResetToken(null); // clear
				user.setResetTokenExpiry(null);
				userDataRepo.save(user);
				return true;
			}
		}
		return false;
	}

}
