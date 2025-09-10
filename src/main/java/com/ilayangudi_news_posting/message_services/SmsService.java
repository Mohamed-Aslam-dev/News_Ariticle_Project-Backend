package com.ilayangudi_news_posting.message_services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Service
public class SmsService {
    
//    private static final String accountSid=""; 
//    private static final String authToken=""; 
//    private static final String twilioPhoneNumber="";
//
//
//    public void sendOtp(String message, String number) {
//        Twilio.init(accountSid, authToken);
//        
//        String phoneNum ="+91"+number;
//        
//        Message.creator(new PhoneNumber(phoneNum), new PhoneNumber(twilioPhoneNumber), message).create();
//    }

}
