package com.isu.ifw.oauth;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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