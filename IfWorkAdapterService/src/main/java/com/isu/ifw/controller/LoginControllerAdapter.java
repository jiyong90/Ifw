package com.isu.ifw.controller;

import java.util.Enumeration;
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
	

	@Value("${ifw.login-post}")
	private String loginUrl;
	@Value("${ifw.valid-get}")
	private String validUrl;
	
	@Autowired
	private ExchangeService certService;
	
	@RequestMapping(value = "/certificate/{tsId}", method = RequestMethod.POST)
	public Map<String, Object> certificate(@PathVariable String tsId,
					@RequestBody Map<String, Object> paramMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = loginUrl.replace("{{tsId}}", tsId);
		if(!paramMap.containsKey("loginUserId") || !paramMap.containsKey("loginPassword") || !paramMap.containsKey("loginEnterCd")) {
			resMap.put("message", "loginUserId, loginPassword, loginEnterCd is required.");
			return resMap;
		}
		 
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(paramMap));
		resMap = certService.exchange(url, HttpMethod.POST, null, paramMap);
		
		return resMap;

    }
	
	@RequestMapping(value = "/validate/{tsId}", method = RequestMethod.GET)
	public Map<String, Object> getDashboard(@PathVariable String tsId,
								HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = validUrl.replace("{{tsId}}", tsId);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		Enumeration<String> er = request.getParameterNames();
		while(er.hasMoreElements()) {
			String k = er.nextElement();
			paramMap.put(k, request.getParameter(k));
		}
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
}
