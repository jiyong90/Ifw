package com.isu.ifw.oauth;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.common.service.TenantConfigManagerService;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	@Autowired
	@Qualifier("WtmTenantConfigManagerService")
	TenantConfigManagerService tcms;
	

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
			throws IOException, ServletException {

		 String orgUri = request.getRequestURI();
	      
		 System.out.println("=== " + orgUri + " :: " + ex.getMessage());
		 
	      String clientId = getClientId(orgUri);
	      if(clientId.equals("")) {
	    	  System.out.println("잘못된 주소입니다.");
//	         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 주소입니다."); 
	         response.sendRedirect(request.getContextPath()+"/info/160");
	         return;
	      }

	      CommTenantModule tm = null;
	      tm = tenantModuleRepo.findByTenantKey(clientId);

	      
	      if(tm == null) {
	    	  System.out.println("클라이언트가 존재하지 않습니다.");
//	    	  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "클라이언트가 존재하지 않습니다."); 
		      response.sendRedirect(request.getContextPath()+"/info/170");
		      return;
	      }
	      
	      Long tenantId = tm.getTenantId();
	      String authorizeUri = tcms.getConfigValue(tenantId, "IFO.AUTHORIZE.URI", true, "");
	      if(authorizeUri.equals("")) {
	    	  System.out.println("인증 URL이 존재하지 않습니다.");
//	    	  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 URL이 존재하지 않습니다."); 
		      response.sendRedirect(request.getContextPath()+"/info/180");
		      return;
	      }
		  request.getSession().invalidate();
  		  SecurityContextHolder.clearContext();
	      
	      Enumeration headerNames = request.getHeaderNames();
	      while(headerNames.hasMoreElements()){
	    	  String name = (String)headerNames.nextElement();
	          String value = request.getHeader(name);
	          System.out.println("faileheader " + name + " : " + value);
	      }
  		  Cookie[] cookies = request.getCookies();
  		  if(cookies != null && cookies.length > 0) {
  			  for(Cookie cookie:cookies) {
  				  System.out.println("filecookie " + cookie.getName());
  				  System.out.println("filecookie " + cookie.getValue());
  				  cookie.setValue("");
				  response.addCookie(cookie);
  			  }
  		  }

  		response.setHeader("Pragma","no-cache");
  	    response.addHeader("Cache-control","no-store");
	    response.setDateHeader ( "Expires", 0 );

	      //	      String url = authorizeUri + "?client_id=" + clientId 
//	    		  + "&redirect_uri=http://"+request.getLocalAddr() + request.getContextPath() + "/login/" + clientId + "/authorize" 
//	    		  + "&response_type=code&scope=read%20write";
//	      
	    Enumeration<String> s = request.getParameterNames();
    	while(s.hasMoreElements()) {
    		String k = s.nextElement();
    		System.out.println(s + " : " + request.getParameter(k));
    	}
    	
    	System.out.println("================================authorizeUri===============================");
	    
	    authorizeUri = authorizeUri + "&aaaaa=aaaaaaa";
	    
	    System.out.println("authorizeUri : " + authorizeUri);
	    
	      response.sendRedirect(authorizeUri);
 	}
 
	private String getClientId(String uri) {
	      String clientId = "";
	      if(uri.contains("/console/")) {
	         clientId = uri.substring(uri.indexOf("console")+8, uri.length());
	         clientId.substring(0, clientId.contains("/")?clientId.indexOf("/")-1:clientId.length());
	         System.out.println(clientId);
	      } else if(uri.contains("/hr/")) {
	         clientId = uri.substring(uri.indexOf("hr")+3, uri.length());
	         clientId.substring(0, clientId.contains("/")?clientId.indexOf("/")-1:clientId.length());
	         System.out.println(clientId);
	      } else if(uri.contains("/logout/")) {
	    	  clientId = uri.substring(uri.indexOf("logout")+7, uri.length());
		      clientId.substring(0, clientId.contains("/")?clientId.indexOf("/")-1:clientId.length());
		      System.out.println(clientId);
	      }
	      return clientId;
	   }

}
