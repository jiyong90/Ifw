package com.isu.ifw.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.mapper.CommUserMapper;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.util.Sha256;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.StringUtil;

@RestController
public class IfwLoginController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");

	private StringUtil stringUtil;
	
	@Autowired
	@Qualifier("WtmTenantConfigManagerService")
	TenantConfigManagerService tcms;

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;

	@Autowired
	CommUserMapper commUserMapper;
	
    @Autowired
    StringRedisTemplate redisTemplate;
    
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/login/{tsId}/sso", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView ssoLogin(@PathVariable String tsId,
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password,
			HttpServletRequest request, HttpServletResponse response) {
    	
	    CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
        //sso 사용여부를 판단
        
        //로그인 정보를 보낼 URL
	    String authorizeUri = tcms.getConfigValue(tenantId, "IFO.LOGIN.URI", true, "");
	     
	    Map<String, String> requestMap = new HashMap<String, String>();
	    Enumeration<String> e = request.getParameterNames();
	    while(e.hasMoreElements()) {
	    	String key = e.nextElement();
	    	requestMap.put(key, request.getParameter(key));
	    }
	    
	    requestMap.put("client_id", tsId);
	    requestMap.put("username", username);
	    requestMap.put("password", password);
	    
	    ModelAndView mv = new ModelAndView("ssoLogin");
	    ObjectMapper mapper = new ObjectMapper();
    	try {
			mv.addObject("loginParam", mapper.writeValueAsString(requestMap));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        mv.addObject("userAuthorizationUri", authorizeUri);
         
		return mv;
    }
    @RequestMapping(value = "/login/{tsId}/authorize", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView authorizeCallback(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) {

		System.out.println("1111111111111111111111111111111111111 callback");
		CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
		
	    String tokenUri = tcms.getConfigValue(tenantId, "IFO.TOKEN.URI", true, "");
	    String redirectUri = tcms.getConfigValue(tenantId, "IFO.REDIRECT.URI", true, "");
		
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
		factory.setReadTimeout(3000); 
		factory.setConnectTimeout(3000); 
		CloseableHttpClient httpClient = HttpClientBuilder.create().build(); 
		factory.setHttpClient(httpClient); 
		RestTemplate restTemplate = new RestTemplate(factory);
		String url = tokenUri;

        String clientCredentials = tsId+":"+tm.getSecret();
        String base64ClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));
        
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic "+base64ClientCredentials);
		headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("code", request.getParameter("code"));
		param.add("grant_type", "authorization_code");
		param.add("redirect_uri", redirectUri);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(param, headers);
        ModelAndView mv = new ModelAndView("loading");
   		mv.addObject("tenantId", tenantId);
    	mv.addObject("tsId", tsId);
//   		mv.addObject("pageName", "loading");

		try {
			ResponseEntity<Map> res = restTemplate.postForEntity(url, entity, Map.class);
			System.out.println("1111111111111111111111111111111111111 getStatusCode" +res.getStatusCode().value());

			if(res.getStatusCode().value() == HttpServletResponse.SC_OK) {
				 Map<String, Object> tokenMap = res.getBody();
 	             String accessToken = tokenMap.get("access_token").toString();
 				System.out.println("1111111111111111111111111111111111111 tokenMap " +tokenMap.toString());
 	           
 	            Map<String, Object> data = WtmUtil.parseJwtToken(request, accessToken);
 	           
 	            mv.addObject("access_token", accessToken);
 	            mv.addObject("Authorization", accessToken);
 	            Cookie c = new Cookie("Authorization",  accessToken);
   				c.setPath("/");
   				response.addCookie(c);
 	       		
   				redisTemplate.opsForValue().set(data.get("userName").toString(), tokenMap.get("refresh_token").toString());

   				//   				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//   				System.out.println("0000000" + auth.getPrincipal().toString());
//   				System.out.println("0000000" + auth.getCredentials().toString());
//   				
//   				Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities());
//   				
//   				
   	  		  Cookie c2 = new Cookie("JSESSIONID",  "");
   		 	  c2.setPath("/ifw");
   			  c2.setMaxAge(0);
   			  response.addCookie(c2);
	
 	       	    return mv;
 	       	            
// 	            Cookie c = new Cookie("Authorization",  accessToken);
// 				c.setPath("/");
// 				c.setMaxAge(60*60*24);
// 				response.addCookie(c);
// 				response.setHeader("Set-Cookie", "HttpOnly;SameSite=Lax;");
// 				response.setHeader("Access-Control-Allow-Origin", "*");
// 				response.sendRedirect(request.getContextPath()+"/console/"+tsId);
// 				return null;
 	            
// 				request.getRequestDispatcher(request.getContextPath() +"/console/" + tsId).forward(request, response);

// 	            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
// 	            temp.put("Authorization", accessToken);
// 	       
 	            
// 				return mv;
			}
		} catch(Exception e) {
			e.printStackTrace();
			try {
				response.sendRedirect(request.getContextPath()+"/console/"+tsId);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
		return mv;
    }
	    
	@RequestMapping(value="/logout/{tsId}", method=RequestMethod.GET)
	public ModelAndView logout(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws ServletException{
		
		SecurityContextHolder.clearContext();
//		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/ifw");
            response.addCookie(cookie);
        }
		try {
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			String loginId = sessionData.get("loginId").toString();
	
			boolean delKey = redisTemplate.delete(tsId+"@"+enterCd+"@"+loginId);
//			redisTemplate.opsForValue().getOperations().delete(tsId+"@"+enterCd+"@"+loginId);
			System.out.println("redisTemplate.delete " + delKey);
			
			HttpSession session = request.getSession();
			if(session != null)
				session.invalidate();
		
			response.setHeader("Cookie", "");
			response.setHeader("Location", "");
//		
//			response.sendRedirect(request.getContextPath() +"/console/" + tsId);
//			request.getRequestDispatcher(request.getContextPath() +"/console/" + tsId).forward(request, response);
			
//			response.sendRedirect(request.getContextPath() +"/console/" + tsId);
		}catch(Exception e) {
			e.printStackTrace();
		}
        ModelAndView mv = new ModelAndView("loading2");
    	mv.addObject("tsId", tsId);

    	return mv;
	}
	
	@RequestMapping(value = "/certificate/{tsId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam loginForWtm(@PathVariable String tsId, @RequestBody Map<String,Object> params,
										  HttpServletRequest request,
										  HttpServletResponse response) {

		ReturnParam rp = new ReturnParam();
		System.out.println("=================== login");
		try {
			Long tenantId = null;
			Long tenantModuleId = null;

			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("테넌트 정보가 존재하지 않습니다.");
				return rp;
			}
			
			tenantId = tm.getTenantId();
			
			String loginUserId = params.get("loginUserId").toString();
			String loginPassword = params.get("loginPassword").toString();
			String loginEnterCd = params.get("loginEnterCd").toString();
			
			// 사용자 인증 후, 서버에 세션 값으로 채울 데이터를 담을 저장소
			Map<String, Object> userData = null;
			String encKey = tcms.getConfigValue(tenantId, "SECURITY.SHA.KEY", true, "");

			try {
				Map<String, Object> paramMap = new HashMap();
				paramMap.put("tenantId", String.valueOf(tenantId));
				paramMap.put("enterCd", loginEnterCd);
				paramMap.put("loginId", loginUserId);
				paramMap.put("encKey", encKey);
				System.out.println("11111111111111111111111111 1 " + paramMap.toString());
				
				userData = commUserMapper.getCommUser(paramMap);
				
				System.out.println("11111111111111111111111111 2 " + userData.toString());
			} catch (Exception e) {
				e.printStackTrace();
				rp.setFail("등록된 사용자가 없습니다.");
				return rp;
			}
				
			// 사용자 비밀번호를 체크한다.
			if (userData != null) {
				String password = (String) userData.get("password");
				System.out.println("11111111111111111111111111 3 " + password);

				int repeatCount = Integer.valueOf(tcms.getConfigValue(tenantId, "SECURITY.SHA.REPEAT", true, ""));
				
				// 사용자 비밀번호를 체크한다.
				String requestedPassword = Sha256.getHash(loginPassword, encKey, repeatCount);
				System.out.println("11111111111111111111111111 4 " + requestedPassword);
				if(!requestedPassword.contentEquals(password)) {
					System.out.println("11111111111111111111111111 5 ");
					rp.setFail("비밀번호가 일치하지 않습니다.");
					return rp;
				}
				rp.setSuccess("");
				rp.put("userData", userData);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rp;
	}
	
	@RequestMapping(value = "/certificate/all", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void loginForWtm(HttpServletRequest request, HttpServletResponse response) {

	}
}