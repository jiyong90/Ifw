package com.isu.ifw.ext.hdngv.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiControllerHdngv {

	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/api/in", method = RequestMethod.POST)
	public Map<String, Object> in(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("clientId : " + clientId);
		//System.out.println("secret : " + secret);
		
		
		return null;
	}
	
}
