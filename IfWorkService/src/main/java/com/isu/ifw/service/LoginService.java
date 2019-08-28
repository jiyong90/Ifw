package com.isu.ifw.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	private String pathHrToken;
	
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
		WtmToken newToken = null;
		
		try {
			Map<String, Object> responseMap = new HashMap();
			ObjectMapper mapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();
			// 유효성 검사 결과
			Map<String, Object> validationResult = null;
			Map<String, Object> map = new HashMap<String, Object>();
			ResponseEntity<String> responseEntity = null;
	
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("accessToken", token.getAccessToken());
			requestMap.put("refreshToken", token.getRefreshToken());
		
			responseEntity = restTemplate.postForEntity(pathHrToken, requestMap, String.class);
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
				newToken = new WtmToken();
				newToken.setId(token.getId());
				newToken.setAccessToken(responseData.get("accessToken").toString());
				
				String expiresAt = responseData.get("expiresAt").toString();
				Calendar cal = Calendar.getInstance();
			    cal.add( Calendar.SECOND, Integer.valueOf(expiresAt)); 
	 	        Date date = cal.getTime(); 
				
				newToken.setExpiresAt(date);
				newToken.setRefreshToken(responseData.get("refreshToken").toString());
				newToken.setUpdateId(Long.valueOf(token.getUserId()));
				
				newToken = tokenRepository.save(newToken);
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
		return newToken;
	}
	
	public void creatAccessToken(ServletResponse response, WtmToken token) {
		tokenRepository.deleteByTenantIdAndEnterCdAndSabun(token.getTenantId(), token.getEnterCd(), token.getSabun());

		List<WtmEmpHis> emp = empHisRepository.findByTenantIdAndEnterCdAndSabun(token.getTenantId(), token.getEnterCd(), token.getSabun());
		if(emp != null || emp.size() > 0) {
			token.setUserId(emp.get(0).getEmpHisId());
			token.setUpdateId(emp.get(0).getEmpHisId());
			//기존 토큰 다 삭제하고 새로 등록(기존에 다른 곳에서 로그인한 상황이면 그쪽은 튕김)
			tokenRepository.save(token);
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx emp his에 없는 사원정보");
			removeTokenCookie(response, "ACCESS_TOKEN");
			//hr을 로그아웃시킬 필요는 없겠지
		}
		//emp his에 업으면 안됨...
	}
	
	public void deleteAccessToken(ServletResponse response, WtmToken token) {
		tokenRepository.deleteByTenantIdAndEnterCdAndSabun(token.getTenantId(), token.getEnterCd(), token.getSabun());
		removeTokenCookie(response, "ACCESS_TOKEN");
	}
}