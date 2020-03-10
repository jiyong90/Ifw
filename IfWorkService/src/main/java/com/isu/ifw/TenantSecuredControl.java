package com.isu.ifw;

import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.util.Sha256;

/**
 * 테넌트 API 접근 정보로부터 권한 인증 체크를 해야하는 Control의 상위 클래스 
 * @author admin
 *
 */
public abstract class TenantSecuredControl extends HttpErrorHandler{

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	/**
	 * 테넌트 id를 api 키로부터 찾는다.
	 * @param apiKey
	 * @return
	 */
	protected Long getTenantId(String apiKey){
		CommTenantModule commTenantModule = tenantModuleRepo.findByApiKey(apiKey);
		if(commTenantModule.getTenantId() != null)
			return commTenantModule.getTenantId();
		return null;
	}
	
	protected CommTenantModule getTenantInfo(String apiKey){
		return tenantModuleRepo.findByApiKey(apiKey);
	}
	/**
	 * 주어진 apiKey와 secret으로 유효성을 판단한다.
	 * 
	 * @param apiKey
	 * @param secret
	 * @param ip 요청자의 ip
	 * @return
	 * @throws 인증에 실패한 경우 CertificateException을 반환한다.
	 */
	protected boolean certificate(String apiKey, String secret, String ip) throws CertificateException{
		CommTenantModule tenantInfo = getTenantInfo(apiKey);
		
		// 태넌트 찾기 실패
		if(tenantInfo == null)
			throw new CertificateException("no tenants were found by given apikey.");

		String secretCode = tenantInfo.getSecret();
		
		// 허용 ip들 조회. ',' 으로 구분되어 있음
		String apiAllowedIps = tenantInfo.getApiAllowedIps();
				
		
		// ip 비교
		if(apiAllowedIps == null)
			throw new CertificateException("target ip is not allowed.");
		
		String [] ipRules = apiAllowedIps.split(",");
		
		if(ipRules == null)
			throw new CertificateException("target ip is not allowed.");
		
		// 허용 가능한 ip 인지 체크함
		for(int i=0; i<ipRules.length; i++){
			String rule = ipRules[i];
			try {
				if(!checkAllowedIp(rule, ip)){
					throw new CertificateException("target ip is not allowed.");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				throw new CertificateException("invalid ip format.");
			}
		}
			
		// 저장된 시크릿 코드가 틀림
		if(secretCode == null)
			throw new CertificateException("invalid saved secret cocde.");
		
		// 넘어온 시크릿 코드가 널이면 안됨
		if(secret == null)
			throw new CertificateException("invalid secret code. null value is not allowed.");
		
		// 테넌트 키로 암호화하여 비교한다.
		String encryptCode= tenantInfo.getTenantKey();
		
		
		// 암호화 키가 없으면 안됨
		if(encryptCode == null)
			throw new CertificateException("fail to encrypt secret.");
		
		try {
			
			if(encryptCode.length() < 12) {
				encryptCode = String.format("%12s", encryptCode).replaceAll(" ", "o");
			}
			// 단방향 암호화 하기...
			secret = Sha256.getHash(secret, encryptCode, 10);
			System.out.println("secret : " + secret);
		} catch (Exception e) {
			// 암호화 중 오류나면 안됨
			throw new CertificateException("fail to compare secret.");
		}

		// DB에 저장된 암호화 넘어온 암호를 비교
		if(secretCode.equals(secret))
			return true;
		else
			throw new CertificateException("invalid secret.");
		
	}
	
	/**
	 * 파라미터 맵의 유효성을 검사한다.
	 * 파라미터가 다음의 두 조건을 만족하지 않으면, InvalidParameterException를 발생한다.
	 * 1. map 자체가 null 인 경우
	 * 2. paramName 배열에 기술된 값이 map에 (하나라도) 키로 존재하지 않는 경우
	 * 
	 * @param paramMap
	 * @param parameterNames
	 * @throws InvalidParameterException
	 */
	protected void validateParamMap(Map<String,Object> paramMap, String...parameterNames )throws InvalidParameterException{
		
		// 파라미터가 아무것도 없는 경우에, 아무것도 할 수 없다. 무조건 예외 발생
		if(paramMap == null)
			throw new InvalidParameterException("param map is null.");
		
		if(parameterNames == null)
			return; // 파라미터가 없으면 그냥 리턴..
		
		// 넘겨 받은 이름 배열이 map의 부분 집합인지를 따짐.
		Set<String> paramKeySet = paramMap.keySet();
		Collection<String> params = Arrays.asList(parameterNames);
		
		if(!paramKeySet.containsAll(params))
			throw new InvalidParameterException("required parameter is not found.");
		
	}
	
	 /**
	 * 주어진 ip 가 지정된 ip 규칙이 허용하는 값인지 확인한다.
	 * 
	 * 지정된 ip 규칙은 '.'로 구분된 4자리수로 표시하고,
	 * '*' 이 들어있는 부분은 모든 번호를 허용한다.
	 * 
	 * 즉 규칙은 
	 * 
	 * 203.231.11.11 과 같이 표시하거나
	 * 203.231.11.*
	 * 203.231.*.*
	 * 203.*.*.*
	 * *.*.*.*,
	 * 
	 * 과 같이 표현할 수 있고,
	 * 다음과 같은 표현도 허용한다.
	 * 
	 * *.231.*.11
	 *
	 * @param rule 숫자, ., * 로 표시된 문자열 
	 * @param targetIp 모든 값이 . 으로 구분된 숫자인 문자열
	 * @return 규칙에 맞는 문자열이 들어오지 않는 경우 {@link ParseException}을 발생하고, 정상적인 경우 규칙 적용 여부에 따라 true/false 값을 반환한다. 
	 */
	public static boolean checkAllowedIp(String rule, String targetIp) throws ParseException {
		
		if(rule == null || targetIp == null)
			throw new ParseException("rule and targetIp cannot be null.",0);
		
		if("*.*.*.*".equals(rule))
			return true;
		
		String[] ruleTerms = null;
		String[] targetTerms = null;
		
		try{
			ruleTerms = rule.split("[.]");
			
			targetTerms = targetIp.split("[.]");
			
		}catch(Exception e){
			throw new ParseException("faile to split ip texts",0);
		}
		
		if(ruleTerms.length != 4 || targetTerms.length != 4)
			throw new ParseException("unsupported format. the length of each text shuld be 4.",0);
		
		
		for( int i=4 ; i< 4 ; i++){
			String r = ruleTerms[i];
			String t = targetTerms[i];
			
			if("*".equals(r))
				continue;
			
			// 하나라도 맞지 않으면 바로 끝
			if(!r.equals(t))
				return false;
		}
		
		return true;
		
	}
	
}
