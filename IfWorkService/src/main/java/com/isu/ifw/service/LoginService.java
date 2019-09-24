package com.isu.ifw.service;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmToken;
import com.isu.ifw.mapper.LoginMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmTokenRepository;
import com.isu.ifw.vo.Login;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("LoginService")
public class LoginService{
	
	@Autowired
	LoginMapper loginMapper;
	
	@Value("${path.hr.token}")
	private String pathHr;

	static String PARAM_NAME_USER_TOKEN = "accessToken";
	

	@Resource
	WtmTokenRepository tokenRepository;
	
	@Resource
	WtmEmpHisRepository empHisRepository;
	
	public Map<String, String> loginTryCnt(String loginEnterCd, String loginUserId, String loginPassword) throws Exception{
		return loginMapper.loginTryCnt(loginEnterCd, loginUserId, loginPassword);
	}
	
	public Login loginUser(String ssnLocaleCd, String localeCd, String baseLang,String loginEnterCd, String loginUserId, String loginPassword, String ssnSso) throws Exception{
		return loginMapper.loginUser(ssnLocaleCd, localeCd, baseLang, loginEnterCd, loginUserId, loginPassword, ssnSso);
	}
	
	public WtmToken getAccessToken(String accessToken) {
		List<WtmToken> token = tokenRepository.findByAccessToken(accessToken);
		
		if(token != null && token.size() > 0) {
			return token.get(0);
		}
		
		return null;
	}
	
	public void removeTokenCookie(ServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		((HttpServletResponse)response).addCookie(cookie);
	}
	
	public WtmToken refreshAccessToken(ServletResponse response, WtmToken token) {
		//WtmToken newToken = null;
		
		try {
			Map<String, Object> responseMap = new HashMap();
			ObjectMapper mapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();

			Map<String, Object> map = new HashMap<String, Object>();
			ResponseEntity<String> responseEntity = null;
			
			
			URI uri = UriComponentsBuilder.fromUriString(pathHr)
			        .queryParam("cmd", "tokenRefresh")
			        .queryParam("accessToken", token.getAccessToken())
			        .queryParam("refreshToken", token.getRefreshToken())
			        .build().toUri();
	        
			responseEntity = restTemplate.postForEntity(uri, "", String.class);
			if(responseEntity.getBody() == null) {
				System.out.println("body is null " + uri + " " + responseEntity.getStatusCodeValue());
				return null;
			}
			responseMap = mapper.readValue(responseEntity.getBody(), new HashMap<>().getClass());
		
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				Map<String, Object> responseData = null;
				String responseBody = responseEntity.getBody();

				responseData = mapper.readValue(responseBody, new HashMap().getClass());
				if(responseData == null || responseData.equals("")) {
					System.out.println("xxxx responseData is null");
					return null;
				} else if(!responseData.get("message").equals("")) {
					System.out.println("xxxx " + responseData.get("message"));
					return null;
				}
				//newToken = new WtmToken();
				//newToken.setId(token.getId());
				token.setAccessToken(responseData.get("accessToken").toString());
			
				String expiresAt = responseData.get("expiresAt").toString();
				Calendar cal = Calendar.getInstance();
			    cal.add( Calendar.SECOND, Integer.valueOf(expiresAt)); 
	 	        Date date = cal.getTime(); 
				
	 	       token.setExpiresAt(date);
//				newToken.setRefreshToken(responseData.get("refreshToken").toString());
	 	       token.setUpdateId(Long.valueOf(token.getUserId()));
				
	 	      token = tokenRepository.save(token);
			} else if (responseEntity.getStatusCode() != HttpStatus.UNAUTHORIZED) {
				System.out.println("xxxx hr session 만료");
				removeTokenCookie(response, "ACCESS_TOKEN");
			} else {
				System.out.println("xxxx" + responseEntity.getStatusCode() + " : " + responseEntity.getBody());
				removeTokenCookie(response, "ACCESS_TOKEN");
			} 

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return token;
	}
	
	public void creatAccessToken(HttpServletRequest request, HttpServletResponse response, WtmToken token) {
		tokenRepository.deleteByTenantIdAndEnterCdAndSabun(token.getTenantId(), token.getEnterCd(), token.getSabun());

		WtmEmpHis emp = empHisRepository.findByTenantIdAndEnterCdAndSabun(token.getTenantId(), token.getEnterCd(), token.getSabun());
		if(emp != null) {
			token.setUserId(emp.getEmpHisId());
			token.setUpdateId(emp.getEmpHisId());
			//기존 토큰 다 삭제하고 새로 등록(기존에 다른 곳에서 로그인한 상황이면 그쪽은 튕김)
			tokenRepository.save(token);
//
//			Cookie cookie = null;
//			cookie = new Cookie(PARAM_NAME_USER_TOKEN, token.getAccessToken());
//			cookie.setPath("/");
//			response.addCookie(cookie);
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx	xxxxxxxxx emp his에 없는 사원정보");
			removeTokenCookie(response, PARAM_NAME_USER_TOKEN);
			((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			//hr을 로그아웃시킬 필요는 없겠지
		}
		//emp his에 업으면 안됨...
	}
	
	public void deleteAccessToken(ServletResponse response, WtmToken token) {
		tokenRepository.deleteByTenantIdAndEnterCdAndSabun(token.getTenantId(), token.getEnterCd(), token.getSabun());
		removeTokenCookie(response, PARAM_NAME_USER_TOKEN);
	}
}