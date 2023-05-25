package com.cebi.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class AddHeaderFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.setHeader("X-XSS-Protection", "1; mode=block");
		response.setHeader("X-Content-Security-Policy", "script-src 'self'");
		response.setHeader("X-Frame-Options", "sameorigin");
		response.setHeader("X-Frame-Options", "deny");
		response.setHeader("Strict-Transport-Security","max-age=3153600; includeSubDomains");
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setHeader("Referrer-Policy", "no-referrer");
		response.setHeader("Referrer-Policy", "no-referrer-when-downgrade");
		response.setHeader("Referrer-Policy", "origin");
		response.setHeader("Referrer-Policy", "origin-when-cross-origin");
		response.setHeader("Referrer-Policy", "same-origin");
		response.setHeader("Referrer-Policy", "strict-origin");
		response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
		response.setHeader("Referrer-Policy", "unsafe-url");
		response.setHeader("Cache-Control","private, no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setDateHeader("Expires", -1);
		response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
		filterChain.doFilter(request, response);

		
	}

}
