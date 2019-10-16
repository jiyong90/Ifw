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

	@Value("${tenants}")
	private List<String> tenants;

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
		
		//param, cookie에서 테넌트 id 가져오기
		if(request.getParameter("tenant") != null) tenant = request.getParameter("tenant");
		if(cookie.containsKey("tenant")) tenant = cookie.get("tenant").toString();

		System.out.println("111111111111111111111111111111111111111111111111111111111111111 : " + tenant);
	
		if(tenant == null || !tenants.contains("|"+tenant+"|")) {
			chain.doFilter(request, response);
			return;
		}
		
		System.out.println("AccessTokenFilter");

		String tenantId = request.getParameter("tenantId");
		String tokenUrl = loginService.getHrTokenUrl(Long.parseLong(tenantId));
		String infoUrl = loginService.getHrInfoUrl(Long.parseLong(tenantId));
		String tokenName = loginService.getHrTokenName(Long.parseLong(tenantId));

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
			System.out.println("xxxxxxxxxxxxx token : " + token);
			
			if(token == null) {
				System.out.println("xxxxxxxxxxxxx param token...null ");
				if(!cookie.containsKey(tokenName)) {
					token = cookie.get(tokenName).toString();
				} else {
					System.out.println("xxxxxxxxxxxxx token이 아무데도 없음!!!!!!!");
					((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					((HttpServletResponse) response).sendRedirect(infoUrl);
					return;
				}
			} else {
				System.out.println("xxxxxxxxxxxxx add cookie : ");
				Cookie c = null;
				c = new Cookie(tokenName, token);
				c.setPath("/");
				((HttpServletResponse)response).addCookie(c);
			}

			WtmToken wtmToken = loginService.getAccessToken(token);
			if(wtmToken == null) {
				logger.debug("DB에 토큰 없음 : " +  token);
				((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				((HttpServletResponse) response).sendRedirect(infoUrl);
				return;
			} else {
				Date expiresAt = wtmToken.getExpiresAt();
				
				Calendar cal = Calendar.getInstance();
	 	        Date date = cal.getTime(); 
	 	         
	 	        int compare = date.compareTo(expiresAt);
				if(compare >= 0) {
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx 토큰만료");
					wtmToken = loginService.refreshAccessToken(response, wtmToken, tokenUrl, tokenName);
					if(wtmToken == null) {
						//hr세선 만료일 경우 토큰 갱신 불가
						((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						((HttpServletResponse) response).sendRedirect(infoUrl);
						return;
					}
					Cookie c = null;
					c = new Cookie(tokenName, wtmToken.getAccessToken());
					c.setPath("/");
					c.setMaxAge(60*60*24);   
					((HttpServletResponse)response).addCookie(c);
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
