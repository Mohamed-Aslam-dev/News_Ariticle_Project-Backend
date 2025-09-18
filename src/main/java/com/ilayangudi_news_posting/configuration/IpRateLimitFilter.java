package com.ilayangudi_news_posting.configuration;

import java.io.IOException;
import jakarta.servlet.Filter; 
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IpRateLimitFilter implements Filter{

	private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

	private Bucket createNewBucket() {
	    Bandwidth limit = Bandwidth.builder()
	        .capacity(5)  // 5 login attempts
	        .refillIntervally(5, Duration.ofMinutes(1)) // every 1 min reset
	        .build();
	    return Bucket.builder().addLimit(limit).build();
	}

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String ip = ((HttpServletRequest) request).getRemoteAddr();
        Bucket bucket = resolveBucket(ip);
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(429);
            response.getWriter().write("Too many requests from this IP");
        }
    }
	
}
