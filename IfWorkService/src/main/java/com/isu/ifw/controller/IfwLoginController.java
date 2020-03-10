package com.isu.ifw.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.mapper.CommUserMapper;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.service.LoginService;
import com.isu.ifw.service.WtmEmpMgrService;
import com.isu.ifw.service.WtmMsgService;
import com.isu.ifw.util.Aes256;
import com.isu.ifw.util.CookieUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.StringUtil;

@RestController
public class IfwLoginController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	private static final String jwtTokenCookieName = "Authorization"; //"JWT-TOKEN";
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

    @Autowired
    LoginService loginService;

    @Autowired 
    DefaultTokenServices tokenService;
    
    @Autowired
	WtmEmpMgrService empMgrService;
	
	@Autowired
	WtmMsgService msgService;
	
	@Autowired
	WtmEmpHisRepository wtmEmpHisRepo;
   
    @RequestMapping(value = "/login/{tsId}/sso", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView ssoLogin(@PathVariable String tsId,
			@RequestParam(required = true) String p,
			@RequestParam(required = true) String accessToken,
			HttpServletRequest request, HttpServletResponse response) {
    	
    	OAuth2AccessToken token = tokenService.readAccessToken(accessToken);
    	
    	if (token == null) {
			throw new InvalidTokenException("Token was not recognised");
		}

		if (token.isExpired()) {
			throw new InvalidTokenException("Token has expired");
		}
		
    	//Token token = tokenService.verifyToken(accessToken);
    	//System.out.println("token.getKey() : token.getKey());
    	
	    CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
        //sso 사용여부를 판단
         
        //로그인 정보를 보낼 URL
	    String authorizeUri = tcms.getConfigValue(tenantId, "IFO.LOGIN.URI", true, "");
	     
	    Map<String, String> requestMap = new HashMap<String, String>(); 
	    System.out.println("token.getValue() : " + token.getValue());
		Map<String, Object> tokenMap = WtmUtil.parseJwtToken(token.getValue());
		
		Map<String, Object> hashMap = new HashMap<>();
		
		
		String jwtId = tokenMap.get("jti").toString();
		
		
		Aes256 aes;
		String decParam = null;
		try {
			aes = new Aes256(jwtId);
			decParam = aes.decrypt(p);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("======================= decParam: ::::: " + decParam);

		String decrptVal1[] = decParam.split("___");
		
		System.out.println("decrptVal1 :: " + decrptVal1);
		// Email||1111111@dev-hyundai-ngv.com___User_ID||H133001111111___IP||10.206.34.254___Emp_ID||1111111___ssoTIME||20191127191810
		for(int i=0; i<decrptVal1.length; i++)
		{
			String tmp[]=decrptVal1[i].split("\\|\\|");
			requestMap.put(tmp[0], tmp[1]);
		}
		
	    requestMap.put("client_id", tsId);
	    //requestMap.put("username", username);
	    //requestMap.put("password", password);
	    
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
	public void authorizeCallback(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect, Authentication authentication) {

		System.out.println("1111111111111111111111111111111111111 callback");
		//UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
		//System.out.println("userDetails.getUsername() : " + userDetails.getUsername());

			
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
 	           
 	            //mv.addObject("access_token", accessToken);
 	            //mv.addObject("Authorization", accessToken);
 	            //Cookie c = new Cookie("Authorization",  accessToken);
   				//c.setPath("/");
   				//response.addCookie(c);
 	       		
   				redirect.addFlashAttribute("Authorization", accessToken);
   				redirect.addFlashAttribute("x-auth-token", accessToken);
   				
   				
   				/*
   				String username = loginRequest.getUsername();
   			    String password = loginRequest.getPassword();
   			    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
   			    Authentication authentication = this.authenticationManager.authenticate(token);
   			    // vvv THIS vvv
   			    SecurityContextHolder
   			        .getContext()
   			        .setAuthentication(authentication);
   			    return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
   			    */
   				
   				Enumeration<String> k = request.getHeaderNames();
   				System.out.println("header :::::::");
   				while(k.hasMoreElements()) {
   					String key = k.nextElement();
   					System.out.println(key + " : " + request.getHeader(key));
   				}
   				System.out.println("header ::::::: END");
   				if(1==1) {
   					try {
   						redisTemplate.opsForValue().set(data.get("userName").toString(), tokenMap.get("refresh_token").toString());
   						
   				        //String token = JwtUtil.generateToken(signingKey, username);
   				        CookieUtil.create(response, jwtTokenCookieName, accessToken, false, -1, null);
   				        
   						response.sendRedirect(request.getContextPath()+"/console/"+tsId);
   						//return;
   					} catch (Exception e1) {
   						// TODO Auto-generated catch block
   						e1.printStackTrace();
   					}
   					//return "redirect:" + request.getContextPath()+"/console/"+tsId;
   				}
   				

   				//   				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//   				System.out.println("0000000" + auth.getPrincipal().toString());
//   				System.out.println("0000000" + auth.getCredentials().toString());
//   				
//   				Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities());
//   				
//   			
   				/*
   	  		  Cookie c2 = new Cookie("JSESSIONID",  "");
   		 	  c2.setPath("/ifw");
   			  c2.setMaxAge(0);
   			  response.addCookie(c2);
	*/
 	       	    //return mv;
   			  //return ""; 
 	       	            
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
 	            
 				//return mv;
			}
		} catch(Exception e) {
			e.printStackTrace();
			try {
				response.sendRedirect(request.getContextPath()+"/console/"+tsId);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//return null;
		}
		//return "";
    }
	    
	@RequestMapping(value="/logout/{tsId}", method=RequestMethod.GET)
	public void logout(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws ServletException{
		
		//JwtUtil.invalidateRelatedTokens(request);
        /*
		CookieUtil.clear(response, jwtTokenCookieName);

        HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
             session= request.getSession(false);
            if(session != null) {
                session.invalidate();
            }
            for(Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
            }
        */
		/*
        SecurityContextHolder.clearContext();
        HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
        session= request.getSession(false);
    	if(session != null) {
    		session.invalidate();
    	}
    	for(Cookie cookie : request.getCookies()) {
    		System.out.println("cookie.getName() :: " + cookie.getName()); 
    		cookie.setValue(null);
    		cookie.setMaxAge(0);
    		cookie.setPath("/");
    		response.addCookie(cookie);
    	}
*/
		loginService.logout(request, response, tsId);
//		SecurityContextHolder.clearContext();
////		
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//            cookie.setMaxAge(0);
//            //cookie.setValue(null);
//            //cookie.setPath("/ifw");
//            response.addCookie(cookie);
//        }
//		try {
//			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
//			String enterCd = sessionData.get("enterCd").toString();
//			String loginId = sessionData.get("loginId").toString();
//	
//			boolean delKey = redisTemplate.delete(tsId+"@"+enterCd+"@"+loginId);
////			redisTemplate.opsForValue().getOperations().delete(tsId+"@"+enterCd+"@"+loginId);
//			System.out.println("redisTemplate.delete " + delKey);
//			
//			HttpSession session = request.getSession();
//			if(session != null)
//				session.invalidate();
//		
//			response.setHeader("Cookie", "");
//			response.setHeader("Location", "");
////		
////			response.sendRedirect(request.getContextPath() +"/console/" + tsId);
////			request.getRequestDispatcher(request.getContextPath() +"/console/" + tsId).forward(request, response);
//			
////			response.sendRedirect(request.getContextPath() +"/console/" + tsId);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//        ModelAndView mv = new ModelAndView("loading2");
//    	mv.addObject("tsId", tsId);
//    	try {
//			response.sendRedirect("http://10.30.30.188:8180/ifa/logout");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	//return mv;
	}
	

	@RequestMapping(value = "/certificate/{tsId}/sso", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam ssoLoginForWtm(@PathVariable String tsId, @RequestBody Map<String,Object> params,
										  HttpServletRequest request,
										  HttpServletResponse response) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		System.out.println("=================== login sso");
		
		Map<String, Object> userData = null;
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
			String loginEnterCd = params.get("loginEnterCd").toString();
			

			String encKey = tcms.getConfigValue(tenantId, "SECURITY.SHA.KEY", true, "");
				
			
			//userData = loginService.findUserData(tenantId, loginEnterCd, loginUserId, encKey);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			WtmEmpHis empHis = wtmEmpHisRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, loginEnterCd, loginUserId, sdf.format(new Date()));
			if(empHis != null) {
				userData = new HashMap<>();
				userData.put("loginId", empHis.getSabun());
				userData.put("userId", empHis.getEmpHisId());
				userData.put("empNo", empHis.getSabun());
				userData.put("enterCd", empHis.getEnterCd());
				userData.put("name", empHis.getEmpNm());
			}
		
		} catch (Exception e) {
			logger.debug(e.getMessage());
			rp.setFail(e.getMessage());
			return rp;
		}
			
		rp.put("userData", userData);
		return rp;
	}
	@RequestMapping(value = "/certificate/{tsId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam loginForWtm(@PathVariable String tsId, @RequestBody Map<String,Object> params,
										  HttpServletRequest request,
										  HttpServletResponse response) {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		System.out.println("=================== login");
		
		Map<String, Object> userData = null;
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
//			String encKey = tcms.getConfigValue(tenantId, "SECURITY.SHA.KEY", true, "");

//				Map<String, Object> paramMap = new HashMap();
//				paramMap.put("tenantId", String.valueOf(tenantId));
//				paramMap.put("enterCd", loginEnterCd);
//				paramMap.put("loginId", loginUserId);
//				paramMap.put("encKey", encKey);
//				System.out.println("11111111111111111111111111 1 " + paramMap.toString());

			System.out.println("======================= tenantId : " + tenantId);
			System.out.println("======================= loginEnterCd : " + loginEnterCd);
			System.out.println("======================= loginUserId : " + loginUserId);
			System.out.println("======================= loginPassword : " + loginPassword);
			//userData = loginService.getUserData(tenantId, loginEnterCd, loginUserId, loginPassword);
		
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("======================= userData : " + mapper.writeValueAsString(userData));
			
		
		} catch (Exception e) {
			logger.debug(e.getMessage());
			rp.setFail(e.getMessage());
			return rp;
		}
			
		rp.put("userData", userData);
		return rp;
	}
	
	@RequestMapping(value = "/certificate/all", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void loginForWtm(HttpServletRequest request, HttpServletResponse response) {

	}
	
	@RequestMapping(value = "/login/{tsId}/check/user/info", method = RequestMethod.GET)
	public @ResponseBody boolean checkPasswordCertificate(@PathVariable String tsId
														,@RequestParam String enterCd
														,@RequestParam String userInfo
														,HttpServletRequest request) throws Exception {
		CommTenantModule tm = tenantModuleRepo.findByTenantKey(tsId);
	    Long tenantId = tm.getTenantId();
		
		return empMgrService.checkPasswordCertificate(tenantId, enterCd, userInfo);
	}
	
	@RequestMapping(value = "/login/{tsId}/sendCertificateCodeForChangePw", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam sendCertificateCodeForChangePw(@PathVariable String tsId, @RequestBody Map<String, Object> paramMap
			, HttpServletRequest request) throws Exception {
		//validateParamMap(paramMap, "tenantId", "locale", "recruitId", "recruitNm", "data");
		Map sessionData = (Map)request.getSession().getAttribute("sessionData");
		//Long applicantId = new Long(sessionData.get("applicant_id")+"");
		CommTenantModule tm = tenantModuleRepo.findByTenantKey(tsId);
	    Long tenantId = tm.getTenantId();
		String enterCd = paramMap.get("enterCd").toString();
		String userInfo = paramMap.get("userInfo").toString();
		
		ReturnParam rp = new ReturnParam();
		try{
			rp = msgService.sendCertificateCodeForChangePw(tenantId, enterCd, userInfo);
		}catch(Exception e){
			e.printStackTrace();
			rp.setMessage(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value = "/login/{tsId}/changePassword", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam changePassword(@PathVariable String tsId, @RequestBody Map<String, Object> paramMap
			, HttpServletRequest request) throws Exception {
		Map sessionData = (Map)request.getSession().getAttribute("sessionData");
		CommTenantModule tm = tenantModuleRepo.findByTenantKey(tsId);
	    Long tenantId = tm.getTenantId();
		String enterCd = paramMap.get("enterCd").toString();
		String userInfo = paramMap.get("userInfo").toString();
		String otp = paramMap.get("otp").toString();
		
		ReturnParam rp = new ReturnParam();
		try{
			rp = empMgrService.codeCheck(tenantId, enterCd, otp, userInfo);
			
			if(rp.containsKey("status") && rp.get("status")!=null && "OK".equals(rp.get("status"))) {
				
				String encKey = tcms.getConfigValue(tenantId, "SECURITY.SHA.KEY", true, "");
				int repeatCount = Integer.valueOf(tcms.getConfigValue(tenantId, "SECURITY.SHA.REPEAT", true, ""));
				
				paramMap.put("encKey", encKey);
				paramMap.put("repeatCount", repeatCount);
				
				empMgrService.changePw(tenantId, tsId, enterCd, paramMap);
				rp.setSuccess("비밀번호 재설정이 완료되었습니다.");
			} else {
				return rp;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			rp.setFail("비밀번호 설정 시 오류가 발생했습니다.");
		}
		return rp;
	}
}