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

import com.isu.ifw.service.ExchangeService;

@RestController
public class InoutControllerAdapter {

	@Value("${ifw.in-post}")
	private String inUrl;
	
	@Value("${ifw.out-post}")
	private String outUrl;
	
	@Value("${ifw.cancel-post}")
	private String cancelUrl;
	
	@Value("${ifw.except-post}")
	private String exceptUrl;

	@Autowired
	private ExchangeService certService;
	
	@RequestMapping(value = "/api/{tsId}/{functionId}", method = RequestMethod.POST)
	public Map<String, Object> postInout(@PathVariable String tsId,@PathVariable String functionId, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("clientId : " + clientId);
		//System.out.println("secret : " + secret);
		String url = "";
		if(functionId.equalsIgnoreCase("IN")) {
			url = inUrl.replace("{{tsId}}", tsId);
		}else if(functionId.equalsIgnoreCase("OUT")) {
			url = outUrl.replace("{{tsId}}", tsId);
		}else if(functionId.equalsIgnoreCase("CANCEL")) {
			url = cancelUrl.replace("{{tsId}}", tsId);
		}else if(functionId.equalsIgnoreCase("EXCEPT")) {
			url = exceptUrl.replace("{{tsId}}", tsId);
		}
		Map<String, Object> resMap = new HashMap<>();
		if(url != "") {
			resMap = certService.exchange(url, HttpMethod.POST, null, paramMap);
			return resMap;
		}else {
			resMap.put("status", "FAIL");
			resMap.put("message", "/api/in out cancel    url is null");
			return resMap;
		}
	}
}
