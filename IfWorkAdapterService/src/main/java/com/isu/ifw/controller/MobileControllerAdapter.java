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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.ExchangeService;

@RestController
public class MobileControllerAdapter {

	@Value("${ifw.dashboard-get}")
	private String dashboardUrl;
	
	@Value("${ifw.edoc-url}")
	private String edocUrl;
	
	@Value("${ifw.team-url}")
	private String teamUrl;
	
	@Value("${ifw.apply-url}")
	private String applyUrl;
	
	@Autowired
	private ExchangeService certService;
	
	@RequestMapping(value = "/m/{tsId}/dashboard", method = RequestMethod.GET)
	public Map<String, Object> getDashboard(@PathVariable String tsId,
								HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = dashboardUrl.replace("{{tsId}}", tsId);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		Enumeration<String> er = request.getParameterNames();
		while(er.hasMoreElements()) {
			String k = er.nextElement();
			paramMap.put(k, request.getParameter(k));
		}
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
	

	@RequestMapping(value = "/m/{tsId}/edocument/{functionId}", method = RequestMethod.GET)
	public Map<String, Object> getEdoc(@PathVariable String tsId,@PathVariable String functionId,
								HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = edocUrl.replace("{{tsId}}", tsId).replace("{{functionId}}", functionId);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		Enumeration<String> er = request.getParameterNames();
		while(er.hasMoreElements()) {
			String k = er.nextElement();
			paramMap.put(k, request.getParameter(k));
		}
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
	@RequestMapping(value = "/m/{tsId}/team/{functionId}", method = RequestMethod.GET)
	public Map<String, Object> getTeam(@PathVariable String tsId, @PathVariable String functionId,
								HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = teamUrl.replace("{{tsId}}", tsId).replace("{{functionId}}", functionId);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		Enumeration<String> er = request.getParameterNames();
		while(er.hasMoreElements()) {
			String k = er.nextElement();
			paramMap.put(k, request.getParameter(k));
		}
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
	@RequestMapping(value = "/m/{tsId}/apply/{functionId}", method = RequestMethod.GET)
	public Map<String, Object> getApply(@PathVariable String tsId,@PathVariable String functionId,
								HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = applyUrl.replace("{{tsId}}", tsId).replace("{{functionId}}", functionId);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		Enumeration<String> er = request.getParameterNames();
		while(er.hasMoreElements()) {
			String k = er.nextElement();
			paramMap.put(k, request.getParameter(k));
		}
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
	
	@RequestMapping(value = "/m/{tsId}/edocument/{functionId}", method = RequestMethod.POST)
	public Map<String, Object> postEdoc(@PathVariable String tsId,@PathVariable String functionId,
								Map<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = edocUrl.replace("{{tsId}}", tsId).replace("{{functionId}}", functionId);
		
		resMap = certService.exchange(url, HttpMethod.POST, null, paramMap);
		return resMap;
    }
	@RequestMapping(value = "/m/{tsId}/team/{functionId}", method = RequestMethod.POST)
	public Map<String, Object> postTeam(@PathVariable String tsId, @PathVariable String functionId,
			Map<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = teamUrl.replace("{{tsId}}", tsId).replace("{{functionId}}", functionId);
		
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
	@RequestMapping(value = "/m/{tsId}/apply/{functionId}", method = RequestMethod.POST)
	public Map<String, Object> postApply(@PathVariable String tsId,@PathVariable String functionId,
			Map<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> resMap = new HashMap<String, Object>();
		
		String url = applyUrl.replace("{{tsId}}", tsId).replace("{{functionId}}", functionId);
		
		resMap = certService.exchange(url, HttpMethod.GET, null, paramMap);
		return resMap;
    }
}
