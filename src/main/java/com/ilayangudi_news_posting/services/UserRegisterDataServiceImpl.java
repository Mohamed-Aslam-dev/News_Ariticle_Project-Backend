package com.ilayangudi_news_posting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
}
