package com.ilayangudi_news_posting.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ilayangudi_news_posting.dto.ForgetPasswordDto;
import com.ilayangudi_news_posting.dto.LoginRequestDTO;
import com.ilayangudi_news_posting.dto.UserRegisterDTO;
import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import com.ilayangudi_news_posting.servicerepo.UserRegisterDataServiceRepository;

@Service
public class UserRegisterDataServiceImpl implements UserRegisterDataServiceRepository {

	@Autowired
	private UserRegisterDataRepository userDataRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void addNewUser(UserRegisterDTO userDataDto) {

		// Check duplicate by email
		if (userDataRepo.existsByEmailId(userDataDto.getEmailId())) {
			throw new RuntimeException("இந்த மின்னஞ்சல் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");
		}

		// Check duplicate by mobile
		if (userDataRepo.existsByUserMobileNumber(userDataDto.getUserMobileNumber())) {
			throw new RuntimeException("இந்த மொபைல் எண் ஏற்கனவே பதிவு செய்யப்பட்டுள்ளது");
		}

		UserRegisterData userRegisterdata = new UserRegisterData();

		userRegisterdata.setUserName(userDataDto.getUserName());
		userRegisterdata.setEmailId(userDataDto.getEmailId());
		userRegisterdata.setUserMobileNumber(userDataDto.getUserMobileNumber());
		userRegisterdata.setPassword(passwordEncoder.encode(userDataDto.getPassword()));
		userRegisterdata.setRole("USER");

		userDataRepo.save(userRegisterdata);

	}

	@Override
	public boolean forgetPassword(ForgetPasswordDto forgetPasswordDto) {
		Optional<UserRegisterData> optUser = userDataRepo
				.findByEmailIdOrUserMobileNumber(forgetPasswordDto.getEmailOrMobileNo());
		if (optUser.isPresent()) {
			UserRegisterData user = optUser.get();
			user.setPassword(passwordEncoder.encode(forgetPasswordDto.getNewPassword()));
			userDataRepo.save(user); // ✅ Save updated password
			return true;
		}
		return false;
	}

}
