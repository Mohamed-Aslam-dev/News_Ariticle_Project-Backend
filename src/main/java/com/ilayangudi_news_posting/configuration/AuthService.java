package com.ilayangudi_news_posting.configuration;

import com.ilayangudi_news_posting.entity.UserRegisterData;
import com.ilayangudi_news_posting.repository.UserRegisterDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRegisterDataRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrPhone) throws UsernameNotFoundException {
        UserRegisterData user = userRepository.findByEmailIdOrUserMobileNumber(usernameOrEmailOrPhone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Use emailId (or mobile) as the username field in Spring Security
        String loginKey = (user.getEmailId() != null) ? user.getEmailId() : user.getUserMobileNumber();

        return User.builder()
                .username(loginKey)          // ✅ matches login input
                .password(user.getPassword()) // should be BCrypt encoded
                .roles(user.getRole())        // e.g., USER
                .build();
    }

}

