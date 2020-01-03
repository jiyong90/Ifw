package com.isu.ifw.ext.hdngv.controller;

import java.util.HashMap;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.service.ExchangeService;

@RestController
public class ApiControllerHdngv {


	
	@Value("${ifw.in-post}")
	private String inUrl;
	
	@Value("${ifw.out-post}")
	private String outUrl;
	
	@Value("${ifw.cancel-post}")
	private String cancelUrl;

	@Autowired
	private ExchangeService certService;
	
	@RequestMapping(value = "/api/{{functionId}}", method = RequestMethod.POST)
	public Map<String, Object> postInout(@PathVariable String functionId, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("clientId : " + clientId);
		//System.out.println("secret : " + secret);
		String url = "";
		if(functionId.equalsIgnoreCase("IN")) {
			url = inUrl;
		}else if(functionId.equalsIgnoreCase("OUT")) {
			url = outUrl;
		}else if(functionId.equalsIgnoreCase("CANCEL")) {
			url = cancelUrl;
		}
		Map<String, Object> resMap = new HashMap<>();
		if(url != "") {
			resMap = certService.exchange(url, HttpMethod.POST, null, paramMap);
			return resMap;
		}else {
			resMap.put("status", "fail");
			resMap.put("message", "/api/in out cancel    url is null");
			return resMap;
		}
	}
	
}
