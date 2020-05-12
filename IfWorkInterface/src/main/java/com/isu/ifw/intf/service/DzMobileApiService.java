package com.isu.ifw.intf.service;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.intf.mapper.DzMobileApiMapper;
import com.isu.ifw.intf.vo.ReturnParam;
import com.fasterxml.jackson.core.JsonParser;

@Service
public class DzMobileApiService {

	private static final Logger logger = LoggerFactory.getLogger(DzMobileApiService.class);

	@Value("${dzGateway.gateway-url}")
	private String url;
	@Value("${dzGateway.gateway-customkey}")
	private String customkey;
	@Value("${dzGateway.gateway-product}")
	private String product;
	@Value("${dzGateway.gateway-product-version}")
	private String productVersion;
	@Value("${dzGateway.gateway-language}")
	private String language;
	@Value("${ifw.username}")
	private String username;
	@Value("${ifw.url}")
	private String ifwUrl;
	
	@Autowired
	private DzMobileApiMapper dzMobileApiMapper;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ExchangeService exchangeService;


	/**
	 * 사용자가 명시적으로 로그인할 때, 더존 모바일 게이트웨이 인증을 통해 사용자의 유효성을 검증한다. 
	 * 
	 * 1. session key 인증
	 * 2. erp 인증
	 * 
	 * @param request
	 * @param body
	 * @return 인증이 끝난 후, 어플에서 기본 설정값으로 사용할 데이터를 맵으로 반환한다.
	 *         empKey, userToken, sessionData 가 반환 데이터에 포함되어야한다.
	 * @throws Exception
	 */
	public ReturnParam login(HttpServletRequest request, Map<String, String> body) throws Exception {

		String loginEnterCd = (String) body.get("loginEnterCd");
		String loginGroupCd = (String) body.get("loginGroupCd");
		String loginUserId = (String) body.get("loginUserId");
		String loginPassword = (String) body.get("loginPassword");
		String deviceId = (String) body.get("deviceId");
		String deviceModel = (String) body.get("deviceModel");
		String osType = "ios".equalsIgnoreCase((String) body.get("deviceType")) ? "01" : "02";
		String osVersion = (String) body.get("osVersion");
		String userToken = (String) body.get("userToken");

		ReturnParam rp=new ReturnParam();

		try {

			// 결과로 반환할 데이터
			Map<String, Object> resultMap = new HashMap<String, Object>();

			// 1. 더존 session key 인증
			String sessionKey = null;

			Map<String, Object> gatewayResult = requestSessionKey(loginEnterCd, loginGroupCd, loginUserId, loginPassword, osType, osVersion, deviceId, deviceModel);

			//세션키 발급에 실패한 경우 fail처리 한다.
			if(gatewayResult.containsKey("error")) {
				rp.setFail((String) gatewayResult.get("error"));

				System.out.println("==================================");
				System.out.println("SESSION KEY AUTHORIZATION FAIL");
				System.out.println("==================================");
				System.out.println("gateway 인증 실패 : " + rp);

				return rp;
			}

			sessionKey = (String) gatewayResult.get("result");

			// 2. 사번 조회
			String enterCd = null;
			String sabun = null;
			String empKey = null;

			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("enterCd", loginEnterCd);
			paramMap.put("userId", loginUserId);

			Map<String, Object> empMap = dzMobileApiMapper.getEmpInfo(paramMap);

			// 해당 ID가 ERP에 등록되지 않은 경우 fail처리 한다.
			if(empMap == null) {
				rp.setFail("ERP에 등록되지 않은 사원입니다.");

				System.out.println("================================");
				System.out.println("ERP AUTHORIZATION FAIL");
				System.out.println("================================");
				System.out.println("ERP 인증 실패 : " + rp);

				return rp;
			}

			System.out.println(":::::ERP조회결과");
			System.out.println(empMap);

			enterCd = (String) empMap.get("CD_COMPANY"); 
			sabun = (String) empMap.get("NO_EMP");
			empKey = enterCd + "@" + sabun;


			// 3. 발급된 session key와 디바이스 정보를 DB에 저장한다.

			//access token의 만료 시각을 구한다.
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss");

			String timeCreated = df.format(cal.getTime());
			//cal.add(Calendar.MINUTE, 144000);
			//String timeExpired = df.format(cal.getTime());

			paramMap = new HashMap<String,Object>();

			paramMap.put("sessionKey", sessionKey);
			paramMap.put("timeCreated", timeCreated);
			//paramMap.put("timeExpired", timeExpired);
			paramMap.put("timeExpired", null);
			paramMap.put("tokenJson", null);
			paramMap.put("userToken", userToken);
			paramMap.put("osType", osType);
			paramMap.put("osVersion", osVersion);
			paramMap.put("deviceId", deviceId);
			paramMap.put("deviceModel", deviceModel);


			//DB에 저장
			try {
				int cnt = dzMobileApiMapper.saveMobileSession(paramMap);
				if(cnt < 1) {
					rp.setFail("세션 저장에 실패했습니다.");
					System.out.println("세션 인증 실패 : " + rp);
					return rp;
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

			//앱에서 가지고 있을 세션 정보
			Map<String, Object> sessionData = new HashMap<String, Object>();
			//Map<String, Object> session = sessionService.getSessionData(enterCd, sabun); //사원 기본정보


			//권한코드
			List<String> authCode = new ArrayList<String>();
			
			//---------------------------------------------------------------
			//20200512 추가 : authcode API호출
			paramMap = new HashMap<String, Object>();
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			String enterName = "";
			if (username != null && username.indexOf("@") >= 0) {
				String separator = "@";
				String[] usernameArr = username.split(separator);
				if (usernameArr.length > 1) {
					enterName = usernameArr[0];
				}
			}
			String url = ifwUrl + "/ifw/v1/" + enterName + "/isLeader";
			Map<String, Object> chkAuthCodeMap = exchangeService.exchange(url, HttpMethod.GET, MediaType.APPLICATION_JSON_VALUE, paramMap);
			
			if(chkAuthCodeMap != null) {
				Map<String, Object> result = (Map) chkAuthCodeMap.get("result");
				if(result != null) {
					String isLeader = "" + result.get("isLeader");
					authCode.add("Y".equals(isLeader) ? "LEADER" : "");
				}
			}
			//---------------------------------------------------------------

			sessionData.put("authCode", authCode);
			sessionData.put("empNm", (String)empMap.get("NM_KOR")); //사원이름

			resultMap.put("empKey", empKey);
			resultMap.put("sessionData", sessionData); 

			/**
			 * 더존은 로그인을 사번으로 안할 수 있다. 우리는 사번으로 데이터를 조회하도록 되어있어서 username에 대해서 사번정보로 바꿔서 보내주자
			 */
			if(username != null && !username.equals("") && username.indexOf("##1##") != -1 && username.indexOf("##2##") != -1) {
				String replace_username = username;
				replace_username = replace_username.replace("##1##", enterCd);
				replace_username = replace_username.replace("##2##", sabun);
				rp.put("username", replace_username);
			}
			
			rp.setSuccess("");
			rp.put("result", resultMap);

		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail("로그인에 실패했습니다.");
		}

		System.out.println("#########################");
		System.out.println(rp);

		return rp;
	}

	/**
	 * 사용자가 앱을 재시작했을 때, 세션키의 유효성을 검증한다.
	 * 
	 * 1. user token이 유효한지 검증한다.
	 * 2. access token의 만료 여부를 검증한다.
	 * 	  2-1) 아직 유효하다면, 인증 완료
	 *    2-2) 만료됐을 경우, access token과 session key를 갱신한다.
	 *    
	 * @param request
	 * @param body
	 * @return 
	 * @throws Exception
	 */
	public ReturnParam validEmp(HttpServletRequest request, String locale, String empKey, String userToken)
			throws Exception {

		ReturnParam rp = new ReturnParam();

		try {

			// 결과로 반환할 데이터
			Map<String, Object> resultMap = new HashMap<String, Object>();

			String enterCd =  empKey.split("@")[0];
			String sabun =  empKey.split("@")[1];

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userToken", userToken);

			//1. user token이 유효한지 검증한다.
			Map<String, Object> sessionMap = dzMobileApiMapper.getMobileSession(paramMap);

			if(sessionMap == null || "Y".equals((String)sessionMap.get("invalidate"))) {
				rp.setFail("user token이 유효하지 않습니다.");

				System.out.println("==================================");
				System.out.println("USER TOKEN IS INVALIDATED!!!!!!!!!");
				System.out.println("==================================");
				System.out.println("user token 인증 실패 : " + rp);

				return rp;
			}

			//2. session key의 만료 여부를 검증한다.

			//임의로 게이트웨이를 호출하여 정상적으로 결과가 오는지 확인한다.
			//정상적으로 오지 않는다면 키 만료로 판단.

			Map<String, Object> header = new HashMap<String,Object>();
			header.put("os_type", sessionMap.get("OS_TYPE"));
			header.put("os_ver", sessionMap.get("OS_VERSION"));
			header.put("device", sessionMap.get("DEVICE_ID"));
			header.put("device_model", sessionMap.get("DEVICE_MODEL"));
			header.put("session", sessionMap.get("SESSIONKEY"));

			Map<String, Object> body = new HashMap<String,Object>();
			body.put("FunctionID", "UP_MI_WHR_MG_EMP_USER_BASE_S");
			body.put("BusinessID", "UP_MI_WHR_MG_EMP_USER_BASE_S");
			body.put("P_CD_COMPANY", enterCd);
			body.put("P_NO_EMP", sabun);

			List<Map<String, Object>> empInfo = null; //사원 기본정보
			try {
				empInfo = requestSingleService(header, body);				
			}catch(CertificateException e){
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss");
				String timeInvalidated = df.format(cal.getTime());

				paramMap.put("note", "sessionKey expired");
				paramMap.put("timeInvalidated", timeInvalidated);

				dzMobileApiMapper.invalidateMobileSession(paramMap); //세션 무효화 처리

				rp.setFail("세션이 만료되었습니다. 다시 로그인 하세요.");
				System.out.println("==================================");
				System.out.println("SESSION KEY IS INVALIDATED!!!!!!!!");
				System.out.println("==================================");
				System.out.println("session key 무효화 : " + rp);

				return rp;
			}

			System.out.println("==================================");
			System.out.println("SESSION KEY IS AVAILABLE!!!!!!!!!!");
			System.out.println("==================================");


			/*
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss");

			String now = df.format(cal.getTime()); //현재 시각
			String expire = (String)sessionMap.get("timeExpired");

			//2-1. 아직 유효할 때
			if(now.compareTo(expire) < 0) {
				System.out.println("==================================");
				System.out.println("SESSION KEY IS AVAILABLE!!!!!!!!!!");
				System.out.println("==================================");
			}

			//2-2. 만료됐을 때, 세션 상태 업데이트 후 로그아웃시킨다.
			else {
				System.out.println("==================================");
				System.out.println("SESSION KEY IS INVALIDATED!!!!!!!!");
				System.out.println("==================================");

				paramMap.put("note", "sessionKey expired");
				dao.update("invalidateSession", paramMap); //세션 무효화 처리

				rp.setFail("세션이 만료되었습니다. 다시 로그인 하세요.");

				System.out.println("RP:::::");
				System.out.println(rp);
				return rp;
			}

			 */


			//앱에서 가지고 있을 세션 정보
			Map<String, Object> sessionData = new HashMap<String, Object>();

			//권한코드
			List<String> authCode = new ArrayList<String>();
			
			//---------------------------------------------------------------
			//20200512 추가 : authcode API호출
			paramMap = new HashMap<String, Object>();
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			String enterName = "";
			if (username != null && username.indexOf("@") >= 0) {
				String separator = "@";
				String[] usernameArr = username.split(separator);
				if (usernameArr.length > 1) {
					enterName = usernameArr[0];
				}
			}
			String url = ifwUrl + "/ifw/v1/" + enterName + "/isLeader";
			Map<String, Object> chkAuthCodeMap = exchangeService.exchange(url, HttpMethod.GET, MediaType.APPLICATION_JSON_VALUE, paramMap);
			
			if(chkAuthCodeMap != null) {
				Map<String, Object> result = (Map) chkAuthCodeMap.get("result");
				if(result != null) {
					String isLeader = "" + result.get("isLeader");
					authCode.add("Y".equals(isLeader) ? "LEADER" : "");
				}
			}
			//---------------------------------------------------------------

			sessionData.put("authCode", authCode);
			sessionData.put("empNm", empInfo.get(0).get("NM_KOR")); //사원이름

			resultMap.put("empKey", empKey);
			resultMap.put("sessionData", sessionData);

			rp.setSuccess("");
			rp.put("result", resultMap);

		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail("로그인에 실패했습니다.");
		}

		System.out.println("#########################");
		System.out.println(rp);

		return rp;
	}


	/**
	 * 모바일 게이트웨이를 통해 사용자 인증 후 session key를 반환한다.
	 * 
	 * @param company
	 * @param user
	 * @param osType
	 * @param osVersion
	 * @param deviceToken
	 * @param deviceModel
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> requestSessionKey(String company, String group, String user, String pwd, String osType, String osVersion, String deviceId, String deviceModel) throws Exception {

		//결과로 반환할 데이터
		Map<String, Object> resultMap = new HashMap<>();

		//모바일 게이트웨이 호출 시 필요한 custom key
		//String devCustomkey = AdapterProp.getPropValue("gateway.dev.customkey");
		//String customkey = AdapterProp.getPropValue("gateway.customkey");

		String sessionKey=null;

		Map<String, Object> header = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();

		header.put("os_type", osType);
		header.put("os_ver", osVersion);
		header.put("device", deviceId);
		header.put("device_model", deviceModel);

		body.put("server_key", customkey);
		body.put("company", company);
		body.put("group", group);
		body.put("user", user);
		body.put("pwd", pwd);


		Map<String, Object> gatewayResult = callRestService("certificate", header, body, HttpMethod.POST);

		//세션키 발급 실패했을 경우 gateway의 에러 메시지를 반환한다.
		if(!"1000".equals(gatewayResult.get("resultCode"))) {
			resultMap.put("error", (String)gatewayResult.get("resultMessage"));
			return resultMap;
		}

		sessionKey = (String) gatewayResult.get("result");
		System.out.println("==================================");
		System.out.println("SESSION KEY AUTHORIZATION SUCCESS");
		System.out.println("==================================");
		System.out.println("=====> SESSION KEY : " + sessionKey);

		return gatewayResult;
	}

	/**
	 * 더존 모바일 게이트웨이를 통해 웹 서비스를 호출한다. 
	 * 
	 * @param urlCd
	 * @param header
	 * @param body
	 * @param httpMethod
	 * @return Map
	 * @throws Exception
	 */
	public Map<String, Object> callRestService(String urlCd, Map<String,Object> header, Object body, HttpMethod httpMethod) throws Exception {


		//연결할 url이 없으면 예외처리
		if(urlCd == null) 
			throw new Exception("urlCd is null");

		//urlCd는 certificate_sgid,requestNtx,RequestOut,RequestScalar,requestTx,requestTx2 6개 중 하나여야 한다.
		if(!"certificate".equals(urlCd) && !"requestNtx".equals(urlCd) && !"RequestOut".equals(urlCd) 
				&&!"RequestScalar".equals(urlCd)&& !"requestTx".equals(urlCd) && !"requestTx2".equals(urlCd)) 
			throw new Exception("urlCd is wrong");


		//공통 파라미터값 셋팅
		//구조는 다음과 같다.
		//프로시저 1개 호출 => { "header":{}, "body":{} }
		//프로시저 2개 이상 호출=> { "header":{}, "body":[{},{},...] }

		Map<String, Object> param = new HashMap<String,Object>();

		//session값이 없으면 공백으로 넘긴다.(사용자 인증해야 session값 발급됨)
		if(header.get("session") == null)
			header.put("session", "");

		//"header" 데이터 셋팅	
		header.put("customkey", customkey);
		header.put("product", product);
		header.put("product_ver", productVersion);
		header.put("language", language);
		header.put("push", ""); //공백

		param.put("header", header);

		//"body" 데이터 셋팅
		//해쉬맵이나 리스트로 변환하여 담는다.
		if(body.getClass() == HashMap.class)
			param.put("body", (HashMap)body);
		else if(body.getClass() == ArrayList.class)
			param.put("body", (List)body);
		else
			throw new Exception("body data is wrong");

		ResponseEntity<HashMap> response = null;
		ObjectMapper objectMapper = new ObjectMapper();

		//특수문자를 허용시켜준다.
		objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

		//전송되는 MediaType(text/html)을 허용시켜준다.
		MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();

		List<MediaType> supportedMediaTypes=new LinkedList<MediaType>(converter.getSupportedMediaTypes());
		MediaType mediaType=new MediaType(MediaType.TEXT_HTML.getType());
		supportedMediaTypes.add(mediaType);

		converter.setSupportedMediaTypes(supportedMediaTypes); 
		converter.setObjectMapper(objectMapper);

		HttpClient httpClient = HttpClientBuilder.create().build();
		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		//호출할 때 마다 restTemplate 객체를 새로 만든다.
		//그렇지 않으면, request header의 사이즈가 점점 커져 400에러를 낸다..
		restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().add(converter); 
		//restTemplate.getMessageConverters().add(htmlEscapingConveter()); 

		System.out.println("");
		System.out.println("**************MOBILE GATEWAY**************");
		System.out.println("URL :: " + url + urlCd + ".aspx");
		System.out.println("METHOD :: " + httpMethod);
		System.out.println("PARAM :: " + "{" + '"'+"header"+'"'+":" + objectMapper.writeValueAsString(param.get("header")) + "," + '"'+"body"+'"'+":" + objectMapper.writeValueAsString(param.get("body")) + "}");
		System.out.println("*******************************************");
		System.out.println("");

		//요청 방식(GET/POST)에 따라 분류해서 호출한다.
		try {
			if(HttpMethod.GET.equals(httpMethod)) 
				response = restTemplate.getForEntity(url + urlCd + ".aspx", HashMap.class);

			else if(HttpMethod.POST.equals(httpMethod))
				//response = restTemplate.postForEntity("http://10.249.92.147:85/ErpuMobileGate/_gateway/" + urlCd + ".aspx", param, HashMap.class);
				response = restTemplate.postForEntity(url + urlCd + ".aspx", param, HashMap.class);


		} catch (HttpClientErrorException e) {
			e.printStackTrace();

			//client오류 예외처리
			/*
			HttpStatus httpStatus=e.getStatusCode();
			if(HttpStatus.BAD_REQUEST.equals(httpStatus))
				throw new BadRequestException();
			if(HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus))
				throw new InternalServerErrorException();
			if(HttpStatus.SERVICE_UNAVAILABLE.equals(httpStatus))
				throw new ServiceUnavailableException();
			 */
		} catch(Exception e) {
			e.printStackTrace();
		}

		if(response==null) 
			throw new NullPointerException("response is null");


		return response.getBody();
	}

	/**
	 * 1개의 조회용 웹 서비스를 호출하여 받은 결과값을 List로 반환한다.
	 * 
	 * <pre>
	 * [
	 * 	{},
	 * 	{},...
	 * ]
	 * </pre>
	 * 
	 * @param header
	 * @param body
	 * @return List
	 * @throws Exception
	 */
	public List<Map<String, Object>> requestSingleService(Map<String,Object> header, Map<String,Object> body) throws Exception{

		String key = (String)body.get("BusinessID");

		Map<String,Object> map = callRestService("requestNtx", header, body, HttpMethod.POST);

		System.out.println("┌────────────────── Result Start────────────────────────");
		System.out.println("│  " + map.toString());
		System.out.println("└────────────────── Result End──────────────────────────");

		//세션 만료
		if("2000".equals((String)map.get("resultCode")))
			throw new CertificateException((String)map.get("resultCode") + " : " + (String)map.get("resultMessage"));

		//그 외 에러
		if(!"1000".equals((String)map.get("resultCode")) && !"2000".equals((String)map.get("resultCode")))
			throw new Exception((String)map.get("resultCode") + " : " + (String)map.get("resultMessage"));

		return (List<Map<String, Object>>) ((HashMap)((HashMap)map.get("result")).get("List")).get(key);
	}


}
