package com.ilayangudi_news_posting.servicerepo;

import org.springframework.web.multipart.MultipartFile;

import com.ilayangudi_news_posting.dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.dto.ForgetPasswordRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;

public interface UserRegisterDataServiceRepository {

	public void addNewUser(UserRegisterDTO userDataDto, MultipartFile profilePic);

	public boolean generateResetToken(ForgetPasswordRequestDTO forgetPasswordRequest);

	public boolean resetPasswordWithToken(ForgetPasswordDto forgetPasswordDto);

}
