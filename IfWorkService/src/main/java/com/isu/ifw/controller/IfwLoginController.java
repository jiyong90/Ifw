package com.isu.ifw.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
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
import com.isu.ifw.service.EncryptionService;
import com.isu.ifw.service.LoginService;
import com.isu.ifw.vo.Login;
import com.isu.option.util.Sha256;

@RestController
public class IfwLoginController {

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
	
	@RequestMapping(value = "/login/certificate/{tsId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void loginForRecruitManagement(@PathVariable String tsId, HttpServletRequest request,
										  HttpServletResponse response, RedirectAttributes redirectAttr) {
	
		System.out.println("call /login/certificate/" + tsId);
		
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

			tenantId = tm.getTenantId();
			tenantModuleId = tm.getTenantModuleId();
			
			// 관용적으로 넣기
			request.setAttribute("tenantId", tenantId);
			request.setAttribute("tenantModuleId", tenantModuleId);
			request.setAttribute("tsId", tsId);
			
			// 기타 정보 제공자를 찾는다.
			authConfig = authConfigProvider.initConfig(tenantId, tsId);

			String passwordParamName = authConfig.getPasswordParameterName();
			if(passwordParamName == null) passwordParamName = "password";
			
			// 해당 테넌트에대한 권한 설정 값 객체를 request scope에 넣는다.
			request.setAttribute("AUTH_CONFIG", authConfig);

			// 입력된 로그인 아이디를 받아온다.
			String loginId = "";
			String requestedPassword = request.getParameter(passwordParamName);

			// 사용자 인증 후, 서버에 세션 값으로 채울 데이터를 담을 저장소
			Map<String, Object> userData = null;
			
			// 사용자 정보를 조회하는 데 사용할 맵
			Map<String, Object> paramMap = null;
			
			String query = authConfig.getCertificateQuery();
			Enumeration<String> paramKeys = request.getParameterNames();
	
			while (paramKeys.hasMoreElements()) {
				String paramKey = paramKeys.nextElement();
	
				if (paramMap == null)
					paramMap = new HashMap<String, Object>();
				
				//id는 DB로 암호화
//				if((paramKey.toUpperCase()).endsWith("ID")) {
//					String encParam = request.getParameter(paramKey);
//					encParam = DBEncryptDecryptModule.encrypt(encParam);
//					paramMap.put(paramKey, encParam);
//					loginId = encParam;
//				}else {
					paramMap.put(paramKey, request.getParameter(paramKey));
//				}
	
			}
			
			System.out.println("paramMap : " + mapper.writeValueAsString(paramMap));
			 
			try {
				userData = tenantDao.getUserInfo(query, tenantId, paramMap);
			} catch (Exception e) {
				request.setAttribute("certificateError", "등록된 이메일이 없습니다.");
				request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
				return;
			}
			
			// 사용자 비밀번호를 체크한다.
			if (userData != null) {
				boolean validYn = true;
				String msg = "";
				String password = (String) userData.get("password");
	
//				String encKey = authConfig.getEncryptKey();
//				int repeatCount = authConfig.getHashIterationCount();
//	
//				requestedPassword = Sha256.getHash(requestedPassword, encKey, repeatCount);
				
				paramMap.put("encryptStr", requestedPassword);
				Map<String, Object> rMap = (Map<String, Object>)encryptionService.getShaEncrypt(paramMap);
				if(rMap!=null && rMap.get("encryptStr")!=null) {
					requestedPassword = rMap.get("encryptStr").toString();
					System.out.println("requestedPassword : " + requestedPassword);
				}
				
				if(userData.containsKey("account_lockout_yn") && "Y".equals(userData.get("account_lockout_yn"))) {
					validYn = false;
					msg = "계정잠김 : 비밀번호를 변경하세요.";
				}
				
//				if(userData.containsKey("valid_yn") && "N".equals(userData.get("valid_yn"))) {
//					validYn = false;
//					msg = "휴면계정 : 비밀번호를 변경하세요.";
//				}
				
				if(password == null || !password.equals(requestedPassword)){
					//throw new CertificateException("사용자가 없거나, 인증에 실패했습니다.");
					validYn = false;
					msg = "사용자가 없거나, 인증에 실패했습니다.";
					
//					userMgrService.loginFailureCountUp(loginId, tenantId);
//					int wrongCount = userMgrService.getLoginFailureCount(loginId, tenantId);
//					if(wrongCount > 4) {
//						userMgrService.accountLockOut(loginId, tenantId);
//						msg = "계정잠김 : 비밀번호를 변경하세요.";
//					} else {
//						msg =  wrongCount + "회 로그인 실패! " +  "ID 또는 비밀번호를 다시 확인하세요.";
//					}
				}
				
				if(!validYn) {
					request.setAttribute("certificateError", msg);
					request.getRequestDispatcher(authConfig.getLoginPageEndpoint().getUrl()).forward(request, response);
					return;
				} else {
//					userMgrService.loginFailureCountInit(loginId, tenantId);
//					userMgrService.renewLastLoginTime(loginId, tenantId);
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
			// 만일 위의 과정에서 로그인 id가 (의도한)조작에 의해 바뀔 수 있으면 바꾼다.
			if (userData != null && userData.containsKey("loginId")) {
				loginId = (String) userData.get("loginId");
			}
			
			// Map 데이터를 세션 DB에 넣는다.
			userData.put("clientIp", stringUtil.getClientIP(request));
			
			String userToken = oAuthService.createNewOAuthSession(tsId, userData, null);
			//logger.debug("+++userToken+++ : " + userToken);
			// 세션 아이디를 담는 쿠키 생성
			Cookie cookie = null;
			cookie = new Cookie("userToken", userToken);
			cookie.setMaxAge(60 * 60);
			cookie.setPath("/");
			response.addCookie(cookie);

			session.setAttribute("loginIp", stringUtil.getClientIP(request));
			
			String endPointUrl = (String) request.getParameter("o");
			//2018.03.16
			//로그인 성공 후 무조건 메인페이지로 간다. 
			endPointUrl = authConfig.getMainPageEndpoint().getUrl();// "/console/" + tsId;
			endPointUrl = stringUtil.appendUri(request, endPointUrl, request.getQueryString()).toString();
			
			String url = request.getRequestURL().toString();
			if(url.startsWith("http://") && url.indexOf("localhost") == -1) {
				endPointUrl = endPointUrl.replace("http://", "https://");
			}
			
			response.sendRedirect(endPointUrl);
			return;
			
			
		} catch (Exception e) {
			e.printStackTrace();
			String endPointUrl = authConfig.getMainPageEndpoint().getUrl(); //"/login/" + tsId;
			endPointUrl = stringUtil.appendUri(request, endPointUrl, request.getQueryString()).toString();
			
			String url = request.getRequestURL().toString();
			if(url.startsWith("http://") && url.indexOf("localhost") == -1) {
				endPointUrl = endPointUrl.replace("http://", "https://");
			}
			
			try {
				response.sendRedirect(endPointUrl);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
		
	}
		
	//채용사이트에서 로그인
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public Map<String, Object> recruitLogin(@RequestBody Map<String, Object> paramMap) throws Exception {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");

		String companyCd = paramMap.get("companyCd") + "";
		String loginId = paramMap.get("loginId") + "";
		String password = paramMap.get("password") + "";
		
		paramMap.put("loginEnterCd", companyCd);
		paramMap.put("loginUserId", loginId);
		paramMap.put("loginPassword", password);

		//Map<String, String> loginTryCntMap = (Map<String, String>) loginService.loginTryCnt(companyCd, loginId, password);
		
		ObjectMapper mapper = new ObjectMapper();
		//System.out.println(mapper.writeValueAsString(loginTryCntMap));
		
		Map<String, Object> resultMap = new HashMap<>();
		/**
		 * 해당 ID가 존재 하지 않음
		 */
		/*
		if (loginTryCntMap.get("ID_EXST").equals("N")) {
			resultMap.put("isUser", "notExist");
			resultMap.put("message", "ID가 없습니다.");
			return resultMap;

		}
		*/
		
		/**
		 * ID가 잠김
		 */
		/*
		if (loginTryCntMap.get("ROCKING_YN").equals("Y")) {
			resultMap.put("isUser", "rocking");
			resultMap.put("message", "ID가 잠김상태 입니다.");
			return resultMap;
		}
		*/

		/**
		 * 로그인 실패 횟수 Over
		 */
		/*
		if (loginTryCntMap.get("LOGIN_FAIL_CNT_YN").equals("Y")) {
			resultMap.put("isUser", "cntOver");
			resultMap.put("message", "로그인 실패 횟수 초과");
			return resultMap;
		}
		*/

		/**
		 * 비밀번호가 틀림
		 */
		/*
			if (loginTryCntMap.get("PSWD_CLCT").equals("Y")) {

				int loginFailCnt = Integer.parseInt(loginTryCntMap.get("LOGIN_FAIL_CNT") == null ? "" : String.valueOf(loginTryCntMap.get("LOGIN_FAIL_CNT"))); // 로그인 실패 횟수

				paramMap.put("loginFailCnt", loginFailCnt);
//				loginService.saveLoingFailCnt(paramMap);
				resultMap.put("isUser", "notMatch");
				resultMap.put("loginFailCntStd", loginTryCntMap.get("LOGIN_FAIL_CNT_STD"));
				resultMap.put("loginFailCnt", loginFailCnt + 1);
				resultMap.put("message", "비밀번호가 틀립니다.");
				return resultMap;
			}
			*/
		//String ssnLocaleCd, String localeCd, String baseLang, String loginUserId, String loginPassword, String ssnSso
		//String ssnLocaleCd = paramMap.get("ssnLocaleCd") + "";
		
		Map<?, ?> mobilcCheckd = mobilcCheck(paramMap);

		if(mobilcCheckd != null) {
			if(mobilcCheckd.get("success").toString().equals("false")) { 
	  			resultMap.put("isUser", "LoginFail");
	  			resultMap.put("message", mobilcCheckd.get("msg"));
	  			return resultMap;
			}
		}
		
		String localeCd = paramMap.get("localeCd") + "";
		String baseLang = paramMap.get("baseLang") + "";
		String loginUserId = paramMap.get("loginId") + "";
		String loginPassword  = paramMap.get("password") + "";
		String ssnSso  = paramMap.get("ssnSso") + "";

		Login loginUserMap = loginService.loginUser(localeCd, localeCd, baseLang, companyCd, loginUserId, loginPassword, ssnSso);
		//System.out.println("loginUserMap : " + mapper.writeValueAsString(loginUserMap));
  		/**
  		 * 사용자 정보가 없음
  		 */
  		if (loginUserMap == null) {
  			resultMap.put("isUser", "noLogin");
  			resultMap.put("message", "사용자 정보가 없습니다.");
  			return resultMap;
  		}

  		resultMap.put("isUser", "exist");
  		resultMap.put("empKey", paramMap.get("companyCd")+"@"+paramMap.get("loginId"));
  		resultMap.put("userData", loginUserMap);
  		resultMap.put("message", "");

			return resultMap;
		}
 
		
		public Map<?, ?> mobilcCheck(@RequestParam Map<String, Object> paramMap) throws Exception {

			String uId = paramMap.get("loginUserId").toString();
		String uPwd = paramMap.get("loginPassword").toString();
		String addr = "http://m.taeyoung.com/m/servlet/mobile?cmd=mkm_login&user_id=" + uId + "&password=" + uPwd;
		StringBuffer jsonHtml = new StringBuffer();
		Map<String, String> map = new HashMap<>();

		try {
			URL u = new URL(addr);
			InputStream uis = u.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(uis, "UTF-8"));

			String line = null;

			while ((line = br.readLine()) != null) {
				jsonHtml.append(line + "\n");
			}

			br.close();
			uis.close();

			String data = jsonHtml.toString();
			String [] array;
			array = data.split("\"");
			int k = 0;

			for(int i = 0; i < array.length; i++) {
				if(array[i].equals("key")) {
					map.put(array[i], array[i + 2]);
				}

				if(array[i].equals("msg")) {
					map.put(array[i], array[i + 2]);
				}

				if(array[i].equals("url")) {
					map.put(array[i], array[i + 2]);
				}

				if(array[i].equals("success")) {
					map.put(array[i], array[i + 1].substring(1).replace("}", "").trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}
}
