package com.ilayangudi_news_posting.servicerepo;

import com.ilayangudi_news_posting.dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;

public interface UserRegisterDataServiceRepository {
	
	public void addNewUser(UserRegisterDTO userDataDto);

	public boolean forgetPassword(ForgetPasswordDto forgetPasswordDto);
	
}
