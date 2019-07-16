package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.auth.config.AuthConfigProvider;
import com.isu.auth.config.data.AuthConfig;
import com.isu.auth.dao.TenantDao;
import com.isu.ifw.StringUtil;
import com.isu.option.service.TenantConfigManagerService;

@RestController
//@RequestMapping(value="/resource")
public class ViewController {
	
	private StringUtil stringUtil;
	
	@Autowired
	private TenantConfigManagerService tcms;
	
	@Autowired
	AuthConfigProvider authConfigProvider;
	
	@Resource
	TenantDao tenantDao;
	
	/**
	 * POST 방식은 로그인 실패시 포워드를 위한 엔드포인트 
	 * @param tsId
	 * @return
	 */
	@RequestMapping(value="/login/{tsId}", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewLogin(@PathVariable String tsId, HttpServletRequest request) throws Exception {
		Long tenantId = tenantDao.findTenantId(tsId);
		System.out.println("call for forward /login/"+tsId);
		ModelAndView mv = new ModelAndView("login");
		
		// 권한 설정 값을 받는다.
        AuthConfig authConfig = authConfigProvider.initConfig(tenantId, tsId);
        
		mv.addObject("AUTH_CONFIG", authConfig);
		
		String company = tcms.getConfigValue(tenantId, "WTMS.LOGIN.COMPANY_LIST", true, "");
        List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
        
        ObjectMapper mapper = new ObjectMapper();
        if(company != null && !"".equals(company)) 
        	companyList = mapper.readValue(company, new ArrayList<Map<String, Object>>().getClass());
        mv.addObject("companyList", companyList);
        
		return mv;
	}
	
	/*@GetMapping(value="/login/{tsId}")
	public ModelAndView login(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		Long tenantId = tenantDao.findTenantId(tsId);
		System.out.println("call /login/"+tsId);
		ModelAndView mv = new ModelAndView("login");
		
		// 권한 설정 값을 받는다.
        AuthConfig authConfig = authConfigProvider.initConfig(tenantId, tsId);
        
		mv.addObject("AUTH_CONFIG", authConfig);
		return mv;
	}*/
	
	/*@RequestMapping(value = "/{viewPage}", method = RequestMethod.GET)
	public ModelAndView views(@PathVariable String viewPage) throws Exception {
		ModelAndView mv = new ModelAndView(viewPage);
		mv.addObject("tsId", "isu");
		return mv;
		 
	}*/
	
	@GetMapping(value = "/console/{tsId}")
	public ModelAndView login(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//return main(tsId, request);
		return views(tsId, "main", request);
	}
	
	@GetMapping(value="/console/{tsId}/main")
	public ModelAndView main(@PathVariable String tsId, HttpServletRequest request) throws JsonProcessingException {
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("tsId", tsId);
		return mv;
	}
	
	@RequestMapping(value = "/console/{tsId}/{viewPage}", method = RequestMethod.GET)
	public ModelAndView viewPage(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(viewPage);
		mv.addObject("tsId", tsId);
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		return mv;
	}
	
	@RequestMapping(value = "/console/{tsId}/views/{viewPage}", method = RequestMethod.GET)
	public ModelAndView views(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("template");
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		mv.addObject("tsId", tsId);
		mv.addObject("pageName", viewPage);
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		return mv;
	}
	
}
