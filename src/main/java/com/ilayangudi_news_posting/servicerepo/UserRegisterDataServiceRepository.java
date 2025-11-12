package com.ilayangudi_news_posting.servicerepo;

import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.request_dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.request_dto.ForgetPasswordRequestDTO;
import com.ilayangudi_news_posting.request_dto.NotificationDevicetokenDTO;
import com.ilayangudi_news_posting.request_dto.UserRegisterDTO;

public interface UserRegisterDataServiceRepository {

	public void newUserEmailVerified(String email, String mobileNumber);
	
	public void addNewUser(UserRegisterDTO userDataDto, MultipartFile profilePic);

	public boolean generateResetToken(ForgetPasswordRequestDTO forgetPasswordRequest);

	public boolean resetPasswordWithToken(ForgetPasswordDto forgetPasswordDto);
	
	public String checkUserAccountStatus(String userEmailOrMobile);
	
	public void updateDeviceToken(NotificationDevicetokenDTO updateDeeviceToken, String userName);

}
