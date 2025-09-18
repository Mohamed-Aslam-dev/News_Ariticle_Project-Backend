package com.ilayangudi_news_posting.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Service
public class LoginAttemptService {

	private final int CAPTCHA_THRESHOLD = 3;
	private final int MAX_ATTEMPT = 5;
	private final Cache<String, Integer> attemptsCache;

	public LoginAttemptService() {
		this.attemptsCache = Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES) // unblock after 15 mins
				.maximumSize(10000).build();
	}

	public void loginSucceeded(String key) {
		attemptsCache.invalidate(key);
	}

	public void loginFailed(String key) {
		Integer attempts = attemptsCache.getIfPresent(key);
		if (attempts == null)
			attempts = 0;
		attempts++;
		attemptsCache.put(key, attempts);
	}

	public boolean shouldShowCaptcha(String key) {
	    Integer attempts = attemptsCache.getIfPresent(key);
	    return attempts != null && attempts >= CAPTCHA_THRESHOLD && attempts < MAX_ATTEMPT;
	}
	
	public boolean isBlocked(String key) {
		Integer attempts = attemptsCache.getIfPresent(key);
		return attempts != null && attempts >= MAX_ATTEMPT;
	}

}
