package com.isu.ifw.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class PreFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("PreFilter ::::::::");
		System.out.println("PreFilter HEADER::::::::");
		Collection<String> names = ((HttpServletResponse)response).getHeaderNames();
		for(String name : names) {
			System.out.println(name + " : " + ((HttpServletResponse)response).getHeader(name));
		}
		System.out.println("PreFilter HEADER END::::::::");
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		Enumeration<String> er = req.getAttributeNames();
		System.out.println("PreFilter getAttributeNames::::::::");
		while(er.hasMoreElements()) {
			String key = er.nextElement();
			System.out.println(key + " : " + req.getAttribute(key));
		}
		System.out.println("PreFilter getAttributeNames::::::::");
		
		Enumeration<String> pr = req.getParameterNames();
		System.out.println("PreFilter getParameter::::::::");
		while(pr.hasMoreElements()) {
			String key = pr.nextElement();
			System.out.println(key + " : " + req.getParameter(key));
		}
		System.out.println("PreFilter getParameter::::::::");
		
		Enumeration<String> sess = 
		req.getSession().getAttributeNames();
		while(sess.hasMoreElements()) {
			String ses = sess.nextElement();
			System.out.println(ses + " : " + req.getSession().getAttribute(ses)); 
		}
		
		String redisSession = ((HttpServletResponse)response).getHeader("SESSION");
		
		System.out.println("redisSession ::: "+ redisSession); 
		
		chain.doFilter(request, response);
	}

}
