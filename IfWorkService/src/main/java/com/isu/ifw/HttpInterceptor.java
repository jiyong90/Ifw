package com.isu.ifw;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HttpInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response,
							 Object handler) {
		
		System.out.println("Interceptor preHandle");
		
		String requestUri = request.getRequestURI().substring(request.getContextPath().length());
		
//		String userKey = "";
//		String userToken = "";
		
		//userKey
//		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
//		if(sessionData!=null && sessionData.size()>0)
//			userKey = sessionData.get("userKey").toString();
		
		//userToken
//		Cookie[] cookies = request.getCookies();
//		if(cookies!=null && cookies.length>0) {
//			for(Cookie ck : cookies) {
//				if(ck.getName()!=null && "USERTOKEN".equalsIgnoreCase(ck.getName())) {
//					userToken = ck.getValue();
//				}
//			}
//		}
		
		//params
		ObjectMapper mapper = new ObjectMapper();
		String params = "";
		if(request.getMethod().equals("GET") || (request.getMethod().equals("POST") && "application/x-www-form-urlencoded".equalsIgnoreCase(request.getContentType())) ) {
			Map<String, Object> requestParam = new HashMap<String, Object>();
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()){
		        String key = (String)paramNames.nextElement();
		        requestParam.put(key, request.getParameter(key));
		    }
			
			try {
				params = mapper.writeValueAsString(requestParam);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			/*try {
				ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper(request);
				params = wrapper.getRequestBody();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("post parameter:"+e.getMessage());
				e.printStackTrace();
			}
			*/
			if(request.getAttribute("requestBody")!=null)
				params = request.getAttribute("requestBody").toString();
			
		}
		
		String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = request.getRemoteAddr();
		
		logger.debug(request.getMethod()+"::"+requestUri+ " {} {}",ip,params);
		
		return true;
	}
	
	@Override
	public void postHandle( HttpServletRequest request,
							HttpServletResponse response,
							Object handler,
							ModelAndView modelAndView) {
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response, 
								Object handler, 
								Exception ex) {
	}
}
