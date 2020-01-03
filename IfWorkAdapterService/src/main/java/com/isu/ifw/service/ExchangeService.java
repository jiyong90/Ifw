package com.isu.ifw.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.util.AjaxUtils;

@Service
public class ExchangeService {

	@Value("${ifw.token-url}")
	private String tokenUri;
	
	@Value("${ifw.client-id}")
	private String clientId;
	
	@Value("${ifw.secret}")
	private String secret;

	@Autowired
	private RestTemplate restTemplate;

	public Map<String, Object> exchange(String url, HttpMethod method, String contentType, Map<String, Object> paramMap) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", ""+ this.getAccessToken());
		if(contentType == null)
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		else
			headers.set("Content-Type", contentType);
		
		//MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		//param.setAll(paramMap);
		//param.add("code", request.getParameter("code"));
		//param.add("grant_type", "client_credentials");
		//param.add("client_id", clientId);
		//param.add("client_secret", secret);
		
		Map<String, Object> result = new HashMap<String, Object>();
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paramMap, headers);

		try {
			ResponseEntity<Map> res = null;
			
			switch (method) {
				case GET:
					url = AjaxUtils.buildUrl(url, paramMap);
					System.out.println("buildUrl : " + url);
					res = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

					if(res.getStatusCode().value() == HttpServletResponse.SC_OK) {
						result = res.getBody();
		 				System.out.println("result " +result.toString());
					}
					break;
				case POST:
					res = restTemplate.postForEntity(url, entity, Map.class);
					System.out.println("getStatusCode" +res.getStatusCode().value());
					if(res.getStatusCode().value() == HttpServletResponse.SC_OK) {
						result = res.getBody();
		 				System.out.println("result " +result.toString());
					}
					break;
	
				default:
					break;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
		//return accessToken;
	
	}
	
	public String getAccessToken() {
		String url = tokenUri;

        String clientCredentials = clientId+":"+secret;
        String base64ClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));
        String accessToken = "";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic "+base64ClientCredentials);
		//headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		//param.add("code", request.getParameter("code"));
		param.add("grant_type", "client_credentials");
		//param.add("client_id", clientId);
		//param.add("client_secret", secret);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(param, headers);

		try {
			ResponseEntity<Map> res = restTemplate.postForEntity(url, entity, Map.class);
			System.out.println("getStatusCode" +res.getStatusCode().value());

			if(res.getStatusCode().value() == HttpServletResponse.SC_OK) {
				 Map<String, Object> tokenMap = res.getBody();
 	             accessToken = tokenMap.get("access_token").toString();
 				System.out.println("tokenMap " +tokenMap.toString());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return accessToken;
	}
}
