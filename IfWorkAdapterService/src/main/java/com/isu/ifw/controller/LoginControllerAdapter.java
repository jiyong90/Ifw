package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.tempuri.SitemapWSSoap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.ExchangeService;

@RestController
public class LoginControllerAdapter {
	

	@Value("${ifw.login-url}")
	private String loginUrl;
	
	@Autowired
	private ExchangeService certService;
	
	@RequestMapping(value = "/certificate/{tsId}", method = RequestMethod.POST)
	public Map<String, Object> certificate(@PathVariable String tsId,
					@RequestBody Map<String, String> paramMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = loginUrl.replace("{{tsId}}", tsId);
		if(!paramMap.containsKey("loginUserId") || !paramMap.containsKey("loginPassword") || !paramMap.containsKey("loginEnterCd")) {
			resMap.put("message", "loginUserId, loginPassword, loginEnterCd is required.");
			return resMap;
		}
		 
		resMap = certService.exchange(url, HttpMethod.POST, null, paramMap);
		
		return resMap;
		 

    }
}
