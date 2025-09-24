package com.ilayangudi_news_posting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthService authService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getRequestURI();
		if (path.equals("/auth/new-user") 
		        || path.equals("/auth/send-otp") 
		        || path.equals("/auth/verify-otp") 
		        || path.equals("/auth/user-login") 
		        || path.equals("/auth/refresh") 
		        || path.startsWith("/auth/forget-password") 
		        || path.equals("/news/home") 
		        || path.startsWith("/images/")
		        || path.startsWith("/actuator/")) {
		    filterChain.doFilter(request, response); // skip JWT check
		    return;
		}
		
		final String authHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				jwt = authHeader.substring(7);
				username = jwtUtil.extractUsername(jwt); // ⚠️ can throw ExpiredJwtException
			} else {
				throw new RuntimeException("Token இல்லை / தவறான Token");
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = authService.loadUserByUsername(username);

				if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			// ⚡ Handle JWT expiration here
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(
					"{\"message\":\"நீங்கள் யார் என்று என்னால் அறிய முடியவில்லை, தயவுசெய்து மீண்டும் உள்நுழையவும்/Please Login\"}");
		} catch (RuntimeException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"message\":\"Token இல்லை அல்லது தவறான Token ஆதலால் உள்நுழைவு பக்கம்(Login page) வழியாக மீண்டும் உள்நுழையவும்\"}");
		}
	}

}
