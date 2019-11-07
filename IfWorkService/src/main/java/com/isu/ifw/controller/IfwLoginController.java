package com.isu.ifw.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.auth.config.AuthConfigProvider;
import com.isu.auth.config.data.AuthConfig;
import com.isu.auth.config.data.RestEndpoint;
import com.isu.auth.config.service.AuthService;
import com.isu.auth.dao.TenantDao;
import com.isu.auth.entity.CommTenantModule;
import com.isu.auth.repository.CommTenantModuleRepository;
import com.isu.auth.service.OAuthService;
import com.isu.ifw.StringUtil;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmToken;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmTokenRepository;
import com.isu.ifw.service.EncryptionService;
import com.isu.ifw.service.LoginService;
import com.isu.ifw.util.WtmUtil;
import com.isu.option.service.TenantConfigManagerService;
import com.isu.option.util.Aes256;
import com.isu.option.util.Sha256;
import com.isu.option.vo.ReturnParam;

@RestController
public class IfwLoginController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");

	private StringUtil stringUtil;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	OAuthService oAuthService;
	
	@Autowired
	EncryptionService encryptionService;
	
	@Resource
	TenantDao tenantDao;
	
	@Resource
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	AuthConfigProvider authConfigProvider;
	
	@Autowired
	TenantConfigManagerService tcms;
	
	@Resource
	WtmTokenRepository tokenRepository;

	@Resource
	WtmEmpHisRepository empHisRepository;
	
	@RequestMapping(value = "/login/certificate/{tsId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void loginForRecruitManagement(@PathVariable String tsId, HttpServletRequest request,
										  HttpServletResponse response, RedirectAttributes redirectAttr) {

		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("loginController Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		AuthConfig authConfig = null;
		try {
			// 테넌트 id를 찾는다.
			Long tenantId = null;
			Long tenantModuleId = null;

			HttpSession session = ((HttpServletRequest) request).getSession();
			ObjectMapper mapper = new ObjectMapper();
			CommTenantModule tm = null;
			
			if (session.getAttribute("moduleId") != null) {
				String moduleId = session.getAttribute("moduleId").toString();
				tm = tenantModuleRepo.findByModuleIdAndtenantKey(Long.valueOf(moduleId), tsId);
			} else {
				tm = tenantModuleRepo.findByTenantKey(tsId);
			}
			
			if(tm == null) {
				response.sendRedirect(request.getContextPath()+"/info/"+tsId+"?status=100");
			}

			tenantId = tm.getTenantId();
			tenantModuleId = tm.getTenantModuleId();
			
			// 관용적으로 넣기
			request.setAttribute("tenantId", tenantId);
			request.setAttribute("tenantModuleId", tenantModuleId);
			request.setAttribute("tsId", tsId);
		
			// 기타 정보 제공자를 찾는다.
			authConfig = authConfigProvider.initConfig(tenantId, tsId);
			MDC.put("tenantId", String.valueOf(tenantId));
	    	MDC.put("enterCd", request.getParameter("companyCd"));
	    	MDC.put("userId", request.getParameter(authConfig.getLoginIdParameterName()));
	    	    	
	    	
			String passwordParamName = authConfig.getPasswordParameterName();
			if(passwordParamName == null) passwordParamName = "password";
			
			// 해당 테넌트에대한 권한 설정 값 객체를 request scope에 넣는다.
			request.setAttribute("AUTH_CONFIG", authConfig);

			// 입력된 로그인 아이디를 받아온다.
			String loginId = "";
			String requestedPassword = request.getParameter(passwordParamName);
			// 인증 방법을 가져온다.
			//String certificateMethod = authConfig.getCertificateMethod();
			String certificateMethod = tcms.getConfigValue(tenantId, "WTMS.LOGIN.CERTIFICATE_METHOD", true, "");			
			// 사용자 인증 후, 서버에 세션 값으로 채울 데이터를 담을 저장소
			Map<String, Object> userData = null;

			// 사용자 정보를 조회하는 데 사용할 맵
			Map<String, Object> paramMap = null;

			// 인증방법에 따라 적합한 방식을 사용한다.
			if (AuthConfig.CERTIFICATE_TYPE_REST.equalsIgnoreCase(certificateMethod)) {
				MDC.put("loginType", AuthConfig.CERTIFICATE_TYPE_REST);
				RestEndpoint rep = (RestEndpoint) authConfig.getCertificationEndpoint();
				//널이면 데이터베이스에서 URL를 가지고 오자
				int method;
				String endPointUrl = "";
				if(rep == null) {
					endPointUrl = tcms.getConfigValue(tenantId, "WTMS.LOGIN.CERTIFICATE_ENDPOINT", true, "");
					String m = tcms.getConfigValue(tenantId, "WTMS.LOGIN.CERTIFICATE_ENDPOINT_METHOD", true, "");;
					if(m.equalsIgnoreCase("POST")) {
						method = RestEndpoint.METHOD_POST;
					}else {
						method = RestEndpoint.METHOD_GET;
					}
				} else {
					endPointUrl = rep.getUrl();
					method = rep.getMethod();
				}
				
				// REST URL을 호출하게 될 엔드포인트
				RestTemplate restTemplate = new RestTemplate();
				// 유효성 검사 결과
				Map<String, Object> validationResult = null;
				Map<String, Object> map = new HashMap<String, Object>();
				ResponseEntity<String> responseEntity = null;

				// GET 방식 호출인 경우 URL을 get 방식으로 구성하여 반환한다.
				if (method == RestEndpoint.METHOD_GET) {
					Enumeration<String> paramNames = request.getParameterNames();
					String params = "";
					
					while (paramNames != null && paramNames.hasMoreElements()) {
						String paramName = paramNames.nextElement();

						if (params.length() > 0)
							params = params + "&";
						String value = request.getParameter(paramName);

						params = params + URLEncoder.encode(value, request.getCharacterEncoding());
					}
					responseEntity = restTemplate.getForEntity(endPointUrl + params, String.class, map);
					
				} else if (method == RestEndpoint.METHOD_POST) {
					// POST 방식 호출인 경우, URL을 POST 방식으로 구성하여 반환한다 
					// http:// 나 https:// 등의 url 선행 문자열이 없으면, 기본으로 요청한 request 정보로 앞에 붙여줌
					if (endPointUrl != null && endPointUrl.indexOf("://") < 0) {
						String urlPrefix = request.getRequestURL().toString();

						urlPrefix = urlPrefix.substring(0,
								urlPrefix.indexOf(request.getContextPath()) + request.getContextPath().length());

						endPointUrl = urlPrefix + endPointUrl;
					}
					
					Map<String, String> requestMap = new HashMap<String, String>();
					Enumeration<String> itor = request.getParameterNames();
					while(itor.hasMoreElements()) {
						String k = itor.nextElement();
						requestMap.put(k, request.getParameter(k));
					}
					
					MDC.put("requestMap", requestMap.toString());					
			
					try {
						responseEntity = restTemplate.postForEntity(endPointUrl, requestMap, String.class);
						Map<String, Object> responseMap = mapper.readValue(responseEntity.getBody(), new HashMap<>().getClass());
						MDC.put("responseMap", responseMap.toString());
					} catch (Exception e) {
						logger.warn("certificateError", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
						request.setAttribute("certificateError", "서버 접속 시 에러가 발생했습니다.");
						request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
						return;
					}
				}
				// 정상 처리가 안된 모든 경우에 대해 오류 처리함
				if (responseEntity.getStatusCode() != HttpStatus.OK) {
					logger.warn("certificateError responseEntity " + responseEntity.getStatusCode(),  MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));					
					request.setAttribute("certificateError", "로그인에 실패했습니다");
					request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
				}

				// 결과를 Map형태로 반환
				Map<String, Object> responseData = null;
				String responseBody = responseEntity.getBody();

				try {
					responseData = mapper.readValue(responseBody, new HashMap().getClass());
					MDC.put("responseData", responseData.toString());
					if(responseData.containsKey("message")) {
						if(responseData.get("message") != null && !"".equals(responseData.get("message"))) {
							request.setAttribute("certificateError", responseData.get("message"));
							request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
							return;
						}
					}
					
					userData = (Map<String, Object>) responseData.get("userData");
					//System.out.println("userData: " + mapper.writeValueAsString(userData));
					userData.put("userKey", responseData.get("empKey"));
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("사용자가 없거나, 인증에 실패했습니다.", e);
					throw new CertificateException("사용자가 없거나, 인증에 실패했습니다.");
				}
			} else if (AuthConfig.CERTIFICATE_TYPE_SQL.equalsIgnoreCase(certificateMethod)) {
				String query = authConfig.getCertificateQuery();
				MDC.put("query", query);
				Enumeration<String> paramKeys = request.getParameterNames();
		
				while (paramKeys.hasMoreElements()) {
					String paramKey = paramKeys.nextElement();
		
					if (paramMap == null)
						paramMap = new HashMap<String, Object>();
					
					paramMap.put(paramKey, request.getParameter(paramKey));
				}
				MDC.put("paramMap", mapper.writeValueAsString(paramMap));
				
				System.out.println("paramMap : " + mapper.writeValueAsString(paramMap));
				 
				try {
					userData = tenantDao.getUserInfo(query, tenantId, paramMap);
					MDC.put("userData", userData.toString());
				} catch (Exception e) {
					logger.warn("등록된 사용자가 없습니다.",  MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
					request.setAttribute("certificateError", "등록된 사용자가 없습니다.");
					request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
					return;
				}
				
				// 사용자 비밀번호를 체크한다.
				if (userData != null) {
					boolean validYn = true;
					String msg = "";
					String password = (String) userData.get("password");
					
					paramMap.put("encryptStr", requestedPassword);
					/*Map<String, Object> rMap = (Map<String, Object>)encryptionService.getShaEncrypt(paramMap);
					if(rMap!=null && rMap.get("encryptStr")!=null) {
						requestedPassword = rMap.get("encryptStr").toString();
						System.out.println("requestedPassword : " + requestedPassword);
					}*/
					String encKey = authConfig.getEncryptKey();
					int repeatCount = authConfig.getHashIterationCount();

					requestedPassword = Sha256.getHash(requestedPassword, encKey, repeatCount);
					System.out.println("requestedPassword : " + requestedPassword);
					
					
					if(userData.containsKey("account_lockout_yn") && "Y".equals(userData.get("account_lockout_yn"))) {
						validYn = false;
						msg = "계정잠김 : 비밀번호를 변경하세요.";
						logger.warn("계정잠김",  MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
					}
					
					if(password == null || !password.equals(requestedPassword)){
						validYn = false;
						msg = "사용자가 없거나, 인증에 실패했습니다.";
					}
					
					if(!validYn) {
						logger.warn(msg, MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
						request.setAttribute("certificateError", msg);
						request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
						return;
					} else {
					}
				}
				
				// 추가로 (외부 서비스에서)세션 데이터로 집어 넣어야 할 데이터를 요청받아 넣는다.

				RestEndpoint rp = (RestEndpoint) authConfig.getSessionInformationEndpoint();
				//logger.debug("pass2:");
		
				if (rp != null && rp.getUrl() != null) {
					Map<String, Object> extendedSessionData = authService.getMapFromEndpoint(rp, paramMap);
					
					// 이미 구성된 세션 정보에 추가로 값을 더 넣는다.
					if (extendedSessionData != null) {
						userData.putAll(extendedSessionData);
					}
				}
			}
			
			// 만일 위의 과정에서 로그인 id가 (의도한)조작에 의해 바뀔 수 있으면 바꾼다.
			if (userData != null && userData.containsKey("loginId")) {
				loginId = (String) userData.get("loginId");
				userData.put("loginId", loginId);
			}
			
			String userToken = oAuthService.createNewOAuthSession(tsId, userData, null);
			MDC.put("userToken", userToken);
			// 세션 아이디를 담는 쿠키 생성
			Cookie cookie = null;
			cookie = new Cookie("userToken", userToken);
			cookie.setMaxAge(60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);

			session.setAttribute("loginIp", stringUtil.getClientIP(request));
			//session.setAttribute("enterCd", request.getParameter("enterCd"));
			//session.setAttribute("loginId", loginId);
			
			String endPointUrl = (String) request.getParameter("o");
			//2018.03.16
			//로그인 성공 후 무조건 메인페이지로 간다. 
			endPointUrl = authConfig.getMainPageEndpoint().getUrl();// "/console/" + tsId;
			endPointUrl = stringUtil.appendUri(request, endPointUrl, request.getQueryString()).toString();
			
//			String url = request.getRequestURL().toString();
//			if(url.startsWith("http://") && url.indexOf("localhost") == -1) {
//				endPointUrl = endPointUrl.replace("http://", "https://");
//			}
			
			
			System.out.println("endPointUrl :::: " + endPointUrl);
			
			//cookie에 테넌트 추가
			Cookie c = null;
			c = new Cookie("tenant", String.valueOf(tenantId));
			c.setPath("/");
			((HttpServletResponse)response).addCookie(c);
			response.sendRedirect(endPointUrl);
			return;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
			
			String endPointUrl = authConfig.getMainPageEndpoint().getUrl(); //"/login/" + tsId;
			endPointUrl = stringUtil.appendUri(request, endPointUrl, request.getQueryString()).toString();
			
			String url = request.getRequestURL().toString();
			if(url.startsWith("http://") && url.indexOf("localhost") == -1) {
				endPointUrl = endPointUrl.replace("http://", "https://");
			}
			
			try {
				response.sendRedirect(endPointUrl);
			} catch (IOException e1) {
				logger.warn(e1.toString(), e);
				e1.printStackTrace();
			}
		} finally {
			//logger.info("loginController End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}	
		
	}
		
	@RequestMapping(value = "/certificate/token", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public void loginForToken(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = true) Map<String, String> body) {
	
		ReturnParam rp =  new ReturnParam();
		rp.setSuccess("");
		try {
			WtmToken token = new WtmToken();
			token.setAccessToken(body.get("accessToken"));
			token.setEnterCd(body.get("enterCd"));
			token.setRefreshToken(body.get("refreshToken"));
			token.setSabun(body.get("sabun"));
			token.setTenantId(Long.valueOf(body.get("tenantId")));
			
			String expiresAt = body.get("expiresAt");
			Calendar cal = Calendar.getInstance();
		    cal.add( Calendar.SECOND, Integer.valueOf(expiresAt)); 
 	        Date date = cal.getTime(); 

 	        token.setExpiresAt(date);
 	       
 	        loginService.creatAccessToken(request, response, token);
 	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/certificate/deltoken", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public void logoutForToken(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = true) Map<String, String> body) {

		try {
			WtmToken token = new WtmToken();
			token.setEnterCd(body.get("enterCd"));
			token.setSabun(body.get("sabun"));
			token.setTenantId(Long.valueOf(body.get("tenantId")));
			
 	        loginService.deleteAccessToken(request, response, token);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/certificate/mobile/request", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam loginForMobile(HttpServletRequest request, @RequestBody(required = true) Map<String, String> body) {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String loginEnterCd = body.get("loginEnterCd");
			String loginUserId = body.get("loginUserId").toUpperCase();
			String loginPassword = body.get("loginPassword");
			String localeCd = body.get("locale");
			String userToken = body.get("userToken");
		
			String empKey = loginEnterCd + "@"	+ loginUserId;
			
			Aes256 aes = new Aes256(userToken);
			empKey = aes.encrypt(empKey);
			//비밀번호 검증을 위한 hr연동필요
						
			HashMap<String, Object> rtnMap = new HashMap<String, Object>();
			rtnMap.put("empKey", empKey);

			//우리쪽 empHis조회 추가해야함
			HashMap<String, Object> sessionMap = new HashMap<String, Object>();
			sessionMap.put("empNm", "테스트");
			sessionMap.put("orgNm", "테스트");
			sessionMap.put("id", loginEnterCd + loginUserId);
			sessionMap.put("accessToken", "123456789");
			
			rtnMap.put("sessionData", sessionMap);
			rp.put("result", rtnMap);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return rp;
	}
	
	@RequestMapping(value = "/certificate/mobile/valid", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam validEmp(HttpServletRequest request,
			@RequestParam(value = "locale", required = true) String locale,
			@RequestParam(value = "empKey", required = true) String empKey,
			@RequestParam(value = "accessToken", required = true) String accessToken) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		HashMap<String, Object> rtnMap = new HashMap<String, Object>();
		
		String enterCd = empKey.split("@")[0];
		String sabun = empKey.split("@")[1];
		
		rtnMap.put("empKey", enterCd + "@"	+ sabun);

		HashMap<String, Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put("empNm", "테스트");
		sessionMap.put("orgNm", "테스트");
		sessionMap.put("id", enterCd + sabun);
		sessionMap.put("accessToken", "123456789");

		rtnMap.put("sessionData", sessionMap);
		rp.put("result", rtnMap);
		return rp;
	}
	
	@RequestMapping(value="/logout/{tsId}", method=RequestMethod.GET)
	public void logout(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response){
		

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
		
		HttpSession session = request.getSession();
		if(session != null)
			session.invalidate();
		
		
		String endPointUrl;
		try {
			Long tenantId = tenantDao.findTenantId(tsId);
			AuthConfig authConfig = authConfigProvider.initConfig(tenantId, tsId);
			
			//endPointUrl = stringUtil.appendUri(request,authConfig.getMainPageEndpoint().getUrl(), null).toString();
			endPointUrl = stringUtil.appendUri(request,authConfig.getLoginPageEndpoint().getUrl(), null).toString();

//			String url = request.getRequestURL().toString();
//			if(url.startsWith("http://") && url.indexOf("localhost") == -1) {
//				endPointUrl = endPointUrl.replace("http://", "https://");
//			}
			System.out.println("endPointUrl : " + endPointUrl);
			response.sendRedirect(endPointUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/login/token/{tsId}", method = RequestMethod.GET)
	public void loginForToken(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = ((HttpServletRequest) request).getSession();
		session.setAttribute("token", "111111");
	}
	
	private Map<String, Object> makeUserData(HttpServletRequest request) {
		Map<String, Object> userData = new HashMap();
		if(	request.getParameter("userId") == null ||
				request.getParameter("empNo") == null ||
				request.getParameter("enterCd") == null	) {
			return null;
		}
			
		userData.put("userId", request.getParameter("userId"));
		userData.put("loginId", request.getParameter("userId"));
		userData.put("empNo", request.getParameter("empNo"));
		userData.put("enterCd", request.getParameter("enterCd"));
		userData.put("authCd", request.getParameter("authCd") == null ?"U":request.getParameter("authCd"));	
		userData.put("isEmbedded",false);
		userData.put("type","console");
		
		return userData;
	}
	
	@RequestMapping(value = "/sso/{tsId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void loginForSso(@PathVariable String tsId, HttpServletRequest request,
										  HttpServletResponse response) {
		
		Map<String, Object> userData = makeUserData(request);
		
		if(userData == null) {
			try {
				response.sendRedirect(request.getContextPath()+"/info/"+tsId+"?status=120");
				return;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		AuthConfig authConfig = null;
		try {
			// 테넌트 id를 찾는다.
			Long tenantId = null;
			Long tenantModuleId = null;

			HttpSession session = ((HttpServletRequest) request).getSession();
			ObjectMapper mapper = new ObjectMapper();
			CommTenantModule tm = null;
			
			tm = tenantModuleRepo.findByTenantKey(tsId);

			if(tm == null) {
				response.sendRedirect(request.getContextPath()+"/info/"+tsId+"?status=100");
				return;
			}

			tenantId = tm.getTenantId();
			tenantModuleId = tm.getTenantModuleId();
			
			WtmEmpHis emp = empHisRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, userData.get("enterCd").toString(), userData.get("empNo").toString(), WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				response.sendRedirect(request.getContextPath()+"/info/"+tsId+"?status=130");
				return;
			}
			
			authConfig = authConfigProvider.initConfig(tenantId, tsId);
			
			String userToken = oAuthService.createNewOAuthSession(tsId, userData, null);
			logger.debug("userToken : " + userToken);
			// 세션 아이디를 담는 쿠키 생성
			Cookie cookie = null;
			cookie = new Cookie("userToken", userToken);
			cookie.setMaxAge(60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);

			session.setAttribute("loginIp", stringUtil.getClientIP(request));
			
			String endPointUrl = (String) request.getParameter("o");
			endPointUrl = authConfig.getMainPageEndpoint().getUrl();// "/console/" + tsId;
			logger.debug("endPointUrl 1 : " + endPointUrl);
			endPointUrl = stringUtil.appendUri(request, endPointUrl, request.getQueryString()).toString();
			logger.debug("endPointUrl 2 : " + endPointUrl);

			Cookie c = null;
			c = new Cookie("tenant", String.valueOf(tenantId));
			c.setPath("/");
			((HttpServletResponse)response).addCookie(c);
			response.sendRedirect(endPointUrl);
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendRedirect(request.getContextPath()+"/info/"+tsId);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
		}	
		
	}
}
