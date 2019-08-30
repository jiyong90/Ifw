package com.isu.ifw;

import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmToken;
import com.isu.ifw.repository.WtmTokenRepository;
import com.isu.ifw.service.LoginService;

//@Component("accessTokenFilter") 
public class AccessTokenFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	static String PARAM_NAME_USER_TOKEN = "ACCESS_TOKEN";
	String freePassPath = null;
	
	@Value("${path.hr}")
	private String pathHr;
	
	@Autowired
	LoginService loginService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		freePassPath = filterConfig.getInitParameter("freePassPath");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		System.out.println("AccessTokenFilter");
		
		try {
			if(freePassPath != null && freePassPath.trim().length() != 0){
				String requestUri = ((HttpServletRequest)request).getRequestURI().substring(((HttpServletRequest)request).getContextPath().length());
				System.out.println("requestUri : " + requestUri);
				if(requestUri != null){
					String[] freePassPaths = freePassPath.split(",");
					if(freePassPaths.length > 0) {
						for(int i=0; i<freePassPaths.length; i++) {
							int pathIndex = requestUri.indexOf(freePassPaths[i]);
							if(pathIndex>=0){
								chain.doFilter(request,response);
								return;
							}
						}
					}
					
				}
			}
			
			String token = null;
			Cookie[] cookies = ((HttpServletRequest)request).getCookies();
			
			if(cookies != null){
				for(int i=0 ; i< cookies.length ; i++){
					String name = cookies[i].getName();
					String value = cookies[i].getValue();
					if(PARAM_NAME_USER_TOKEN.equalsIgnoreCase(name)){
						System.out.println("session validate filter access token : "+value);
						token = value;
						break;
					}
				}
			}
			
			if(token == null) {
				((HttpServletResponse) response).sendRedirect(pathHr);
				return;
			} else {
				WtmToken wtmToken = loginService.getAccessToken(token);
				if(wtmToken == null) {
					logger.debug("DB에 토큰 없음" + pathHr + "로 redirect", token);
					((HttpServletResponse) response).sendRedirect(pathHr);
					loginService.removeTokenCookie(response, PARAM_NAME_USER_TOKEN);
					return;
				} else {
					Date expiresAt = wtmToken.getExpiresAt();
					
					Calendar cal = Calendar.getInstance();
		 	        Date date = cal.getTime(); 
		 	         
		 	        int compare = date.compareTo(expiresAt);
					if(compare >= 0) {
						System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx 토큰만료");
						wtmToken = loginService.refreshAccessToken(response, wtmToken);
						if(wtmToken == null) {
							//hr세선 만료일 경우 토큰 갱신 불가
							((HttpServletResponse) response).sendRedirect(pathHr);
							return;
						}
					} 
					request.setAttribute("tenantId", wtmToken.getTenantId());
					Map<String, Object> sessionData = new HashMap();
					sessionData.put("enterCd", wtmToken.getEnterCd());
					sessionData.put("empNo", wtmToken.getSabun());
					sessionData.put("userId", wtmToken.getUserId());
					
					request.setAttribute("sessionData", sessionData);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}