package com.isu.ifw;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.util.WtmUtil;

@Component("customOauthFilter") 
public class CustomOauthFilter implements Filter {
	
	String freePassPath = null;

	//@Autowired
	//SaasLoginService loginService;	

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	StringRedisTemplate redisTemplate;
	 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		System.out.println("== customOauthFilter " + ((HttpServletRequest)request).getRequestURI());
		
		if(freePassPath != null && freePassPath.trim().length() != 0){
			String requestUri = ((HttpServletRequest)request).getRequestURI().substring(((HttpServletRequest)request).getContextPath().length());
			System.out.println("requestUri : " + requestUri);
			if(requestUri != null){
				String[] freePassPaths = freePassPath.split(",");
				if(freePassPaths.length > 0) {
					for(int i=0; i<freePassPaths.length; i++) {
						int pathIndex = requestUri.indexOf(freePassPaths[i]);
						if(pathIndex>=0){
							System.out.println("requestUri OK");
							chain.doFilter(request,response);
							return;
						}
					}
				}
				
			}
		}
		
		String bearer = ((HttpServletRequest)request).getHeader("Authorization");
		if(bearer == null || bearer.equals("")) {
			 bearer = ((HttpServletRequest)request).getParameter("access_token");
			
			 if(bearer == null || bearer.equals("")) {
				 Cookie[] cookies = ((HttpServletRequest)request).getCookies();
				 if (cookies != null) {
				      for (Cookie cookie : cookies) {
				    	  if(cookie.getName().equals("Authorization")) {
				    		  bearer = cookie.getValue();
				    		  break;
				    	  }
				      }
				 }
			 }
		}
		
		Cookie c = new Cookie("Authorization", bearer);
		c.setPath("/");
		((HttpServletResponse)response).addCookie(c);
		
		String jwtToken = bearer;
		
        Map<String, Object> sessionData = WtmUtil.parseJwtToken((HttpServletRequest)request, jwtToken);
        try {
	        if(sessionData.containsKey("user_name")) {
	        	if(redisTemplate.hasKey(sessionData.get("user_name").toString()) ) {
	    			String refreshToken = redisTemplate.opsForValue().get(sessionData.get("user_name").toString()).toString();
	    			System.out.println("getRefreshToken " + refreshToken);
	        	} 
	        } else {
	        	throw new Exception();
	        }
        } catch(Exception e) {
        	System.out.println("refreshToken 없음");
        	Cookie cookie = new Cookie("Authorization",  "");
        	cookie.setPath("/");
        	cookie.setMaxAge(0);
			((HttpServletResponse) response).addCookie(cookie);
			((HttpServletResponse) response).sendRedirect(((HttpServletRequest)request).getContextPath() +"/info/140");
        }
        //hr에서 들어올 때 임시로 쿠키에 넣어서 찾아쓰자
        {
            Cookie c1 = new Cookie("enterCd",  sessionData.get("enterCd").toString());
			c1.setPath("/");
			((HttpServletResponse) response).addCookie(c1);
			
            Cookie c2 = new Cookie("sabun",  sessionData.get("loginId").toString());
			c2.setPath("/");
			((HttpServletResponse) response).addCookie(c2);
        }
        

	      CommTenantModule tm = null;
	      tm = tenantModuleRepo.findByTenantKey(sessionData.get("tsId").toString());

	      
	      if(tm == null) {
	    	  System.out.println("클라이언트가 존재하지 않습니다.");
//	    	  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "클라이언트가 존재하지 않습니다."); 

	    	  Cookie cookie = new Cookie("Authorization",  "");
	    	  cookie.setPath("/");
	    	  cookie.setMaxAge(0);
	    	  ((HttpServletResponse) response).addCookie(cookie);
	    	  ((HttpServletResponse) response).sendRedirect(((HttpServletRequest)request).getContextPath() +"/info/140");
	      }
	      
	    Long tenantId = tm.getTenantId();
        //Long tenantId = loginService.getTenantId(sessionData.get("tsId").toString());
        
        request.setAttribute("sessionData", sessionData);
		request.setAttribute("tenantId", tenantId);
		request.setAttribute("access_token", bearer);
		
		((HttpServletResponse)response).setHeader("X-Frame-Options", "ALLOW-FROM http://hr.isu.co.kr/");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		freePassPath = filterConfig.getInitParameter("freePassPath");
	}
	
	@Override
	public void destroy() {
	}
}
