package com.ilayangudi_news_posting.servicerepo;

import com.ilayangudi_news_posting.dto.LoginRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;

public interface UserRegisterDataServiceRepository {
	
	public void addNewUser(UserRegisterDTO userDataDto);
	
	public Boolean loginUser(LoginRequestDTO loginRequest);

}
