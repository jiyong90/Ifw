package com.isu.ifw.oauth;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.isu.ifw.util.WtmUtil;

class CustomExtractor implements TokenExtractor {
    private static final String TOKEN_KEY_JWT = "Authorization";
    private static final String TOKEN_KEY_PARAM = "access_token";

//    @Autowired
//    StringRedisTemplate redisTemplate;
        
    @Override
    public Authentication extract(HttpServletRequest request)  {
    	
    	System.out.println("extract ::::::::");
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		Enumeration<String> er = req.getAttributeNames();
		System.out.println("extract getAttributeNames::::::::");
		while(er.hasMoreElements()) {
			String key = er.nextElement();
			System.out.println(key + " : " + req.getAttribute(key));
		}
		System.out.println("extract getAttributeNames::::::::");
		
		Enumeration<String> pr = req.getParameterNames();
		System.out.println("extract getParameter::::::::");
		while(pr.hasMoreElements()) {
			String key = pr.nextElement();
			System.out.println(key + " : " + req.getParameter(key));
		}
		System.out.println("extract getParameter::::::::");
		

		System.out.println("extract getSession::::::::");
		Enumeration<String> sess = 
		req.getSession().getAttributeNames();
		while(sess.hasMoreElements()) {
			String ses = sess.nextElement();
			System.out.println(ses + " : " + req.getSession().getAttribute(ses)); 
		}
		System.out.println("extract getSession::::::::");
		
    	String token = "";
   	
    	if(request.getHeader(TOKEN_KEY_JWT) != null) {
    		token = request.getHeader(TOKEN_KEY_JWT);
    	} else if(request.getParameter(TOKEN_KEY_PARAM) != null && !request.getParameter(TOKEN_KEY_PARAM).equals("")) {
    		token = request.getParameter(TOKEN_KEY_JWT);
    	} else { 
//    		Cookie[] cookies = request.getCookies();
//    		if(cookies != null && cookies.length > 0) {
//	    		for(Cookie cookie:cookies) {
//	    			System.out.println("0000000000000 " + cookie.getName());
//	    			System.out.println("0000000000000 " + cookie.getValue());
//	    		}
//    		}
    		
    		
    		token = getTokenFromRequest(request);
    	}

//		System.out.println("111111111111111111111111 session id 2 " + request.getSession().getId());
//		System.out.println("111111111111111111111111 session id 2 " + request.getSession().getAttribute(TOKEN_KEY_JWT));
//		token = request.getSession().getAttribute(TOKEN_KEY_JWT) != null ? request.getSession().getAttribute(TOKEN_KEY_JWT).toString():null ;
    	
    	if(token == null || token.equals("")) {
    	    return null;
    	}
    	
    	Map<String, Object> t = WtmUtil.parseJwtToken(request, token);
    	System.out.println("parseJwtToken " + t.toString());
//    	if(t.containsKey("user_name")) {
//    		try {
//   				redisTemplate.opsForValue().set("11", "22");
//    			String refreshToken = redisTemplate.opsForValue().get(t.get("user_name").toString()).toString();
//    			System.out.println("111111111111111111111100 " + refreshToken);
//    		} catch(Exception e) {
//    			e.printStackTrace();
//    		}
//    	}
    	
    	return new PreAuthenticatedAuthenticationToken(token, "");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                     .filter(cookie -> cookie.getName().equals(TOKEN_KEY_JWT))
                     .findFirst()
                     .map(Cookie::getValue).orElse(null);
    }
}