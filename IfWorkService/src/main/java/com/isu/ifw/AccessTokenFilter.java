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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmToken;
import com.isu.ifw.repository.WtmTokenRepository;
import com.isu.ifw.service.LoginService;

@Component("accessTokenFilter") 
public class AccessTokenFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	LoginService loginService;

	String freePassPath = null;

//	@Value("${tenants}")
//	private List<String> tenants;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		freePassPath = filterConfig.getInitParameter("freePassPath");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		Cookie[] cookies = ((HttpServletRequest)request).getCookies();
		Map<String, Object> cookie = new HashMap();
		
		String token = null;
		String tenant = null;
		
		if(cookies != null){
			for(int i=0 ; i< cookies.length ; i++){
				String name = cookies[i].getName();
				String value = cookies[i].getValue();
				cookie.put(name,  value);
			}
		}
		
		if(cookie.containsKey("tenant")) tenant = cookie.get("tenant").toString();
		//param, cookie에서 테넌트 id 가져오기
		if(request.getParameter("tenant") != null) tenant = request.getParameter("tenant");
		
		logger.debug("111111111111111111111111111111111111111111111111111111111111111 : " + tenant);
	
		if(tenant == null || loginService.getHrInfoUrl(Long.parseLong(tenant)).equals("")) {
			chain.doFilter(request, response);
			return;
		}
		logger.debug("111111111111111111111111111111111111111111111111111111111111111 : AccessTokenFilter");

		System.out.println("AccessTokenFilter");

		String tokenUrl = loginService.getHrTokenUrl(Long.parseLong(tenant));
		String infoUrl = loginService.getHrInfoUrl(Long.parseLong(tenant));
		String tokenName = loginService.getHrTokenName(Long.parseLong(tenant));

		System.out.println("tokenUrl : " + tokenUrl +" , "+ "tokenName : " + tokenName);
		
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

			token = request.getParameter(tokenName);
			logger.debug("111111111111111111111111111111111111111111111111111111111111111 : token");
			
			if(token == null) {
				logger.debug("111111111111111111111111111111111111111111111111111111111111111 : param token...null");
				logger.debug("111111111111111111111111111111111111111111111111111111111111111 : cookie value" + cookie.toString());
				
				if(cookie.containsKey(tokenName)) {
					token = cookie.get(tokenName).toString();
				} else {
					logger.debug("111111111111111111111111111111111111111111111111111111111111111 : token이 아무데도 없음!!!!!!!");

					((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					((HttpServletResponse) response).sendRedirect(infoUrl);
					return;
				}
			} else {
				logger.debug("111111111111111111111111111111111111111111111111111111111111111 : add cookie");
				Cookie c = new Cookie(tokenName, token);
				c.setPath("/");
				Cookie c2 = new Cookie("tenant", tenant);
				c2.setPath("/");
				((HttpServletResponse)response).addCookie(c);
				((HttpServletResponse)response).addCookie(c2);
			}

			WtmToken wtmToken = loginService.getAccessToken(token);
			if(wtmToken == null) {
				logger.debug("111111111111111111111111111111111111111111111111111111111111111 : DB에 토큰 없음");
				((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				((HttpServletResponse) response).sendRedirect(infoUrl);
				return;
			} else {
				Date expiresAt = wtmToken.getExpiresAt();
				
				Calendar cal = Calendar.getInstance();
	 	        Date date = cal.getTime(); 
	 	         
	 	        int compare = date.compareTo(expiresAt);
				if(compare >= 0) {
					logger.debug("111111111111111111111111111111111111111111111111111111111111111 : 토큰만료");
					wtmToken = loginService.refreshAccessToken(request, response, wtmToken, tokenUrl, tokenName);
					if(wtmToken == null) {
						//hr세선 만료일 경우 토큰 갱신 불가
						((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						((HttpServletResponse) response).sendRedirect(infoUrl);
						return;
					}
					Cookie c = new Cookie(tokenName, wtmToken.getAccessToken());
					c.setPath("/");
					Cookie c2 = new Cookie("tenant", tenant);
					c2.setPath("/");					
					((HttpServletResponse)response).addCookie(c);
					((HttpServletResponse)response).addCookie(c2);
				} 
				request.setAttribute("tenantId", wtmToken.getTenantId());
				Map<String, Object> sessionData = new HashMap();
				sessionData.put("enterCd", wtmToken.getEnterCd());
				sessionData.put("empNo", wtmToken.getSabun());
				sessionData.put("userId", wtmToken.getUserId());
				
				request.setAttribute("sessionData", sessionData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		request.getRequestDispatcher(((HttpServletRequest) request).getServletPath()).forward(request, response);
		//chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
