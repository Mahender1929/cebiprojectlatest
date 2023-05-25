package com.cebi.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterchain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpServletRequest=(HttpServletRequest)request;
		HttpServletResponse httpservletresponse=(HttpServletResponse)response;
		//filterchain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
		/*if(!httpServletRequest.getMethod().equals("OPTION"))
		{
				filterchain.doFilter(request, response);
		}
		else
		{
			httpservletresponse.sendRedirect("/cebireports1/");
		}*/
		filterchain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
