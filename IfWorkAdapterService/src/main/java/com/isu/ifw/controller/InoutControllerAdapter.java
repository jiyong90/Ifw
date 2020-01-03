package com.isu.ifw.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InoutControllerAdapter {
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/api/in", method = RequestMethod.POST)
	public Map<String, Object> in(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("clientId : " + clientId);
		//System.out.println("secret : " + secret);
		
		
		return null;
	}
	
}
