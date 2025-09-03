package com.ilayangudi_news_posting.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ilayangudi_news_posting.dto.LoginRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;

@Service
public class UserRegisterDataServiceImpl implements UserRegisterDataServiceRepository {
	
	@Autowired
	private UserRegisterDataRepository userDataRepo;

	@Override
	public void addNewUser(UserRegisterDTO userDataDto) {
		
		UserRegisterData userRegisterdata = new UserRegisterData();
		
		userRegisterdata.setUserName(userDataDto.getUserName());
		userRegisterdata.setEmailId(userDataDto.getEmailId());
		userRegisterdata.setUserMobileNumber(userDataDto.getUserMobileNumber());
		userRegisterdata.setPassword(userDataDto.getPassword());
		userRegisterdata.setRole("USER");
		
		userDataRepo.save(userRegisterdata);
		
	}

	@Override
	public Boolean loginUser(LoginRequestDTO loginRequest) {
		
		String userMailOrPassword = loginRequest.getEmailOrPhone();
		String userPassword = loginRequest.getPassword();
		
		Optional<UserRegisterData> userLoginData = userDataRepo.findByEmailIdOrUserMobileNumber(userMailOrPassword);
		
		if(userLoginData.isPresent()) {
			UserRegisterData perUserData = userLoginData.get();
			if(perUserData.getPassword().equals(userPassword)) {
				return true;
			}
		}	
		
		return false;
	}
	
	
	
}
