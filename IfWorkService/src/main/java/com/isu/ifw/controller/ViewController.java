package com.isu.ifw.controller;

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
import com.isu.auth.config.AuthConfigProvider;
import com.isu.auth.config.data.AuthConfig;
import com.isu.auth.dao.TenantDao;
import com.isu.ifw.StringUtil;

@RestController
//@RequestMapping(value="/resource")
public class ViewController {
	
	private StringUtil stringUtil;
	
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
	
	@GetMapping(value = "/{tsId}")
	public ModelAndView login(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return main(tsId, request);
	}
	
	@GetMapping(value="/{tsId}/main")
	public ModelAndView main(@PathVariable String tsId, HttpServletRequest request) throws JsonProcessingException {
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("tsId", tsId);
		return mv;
	}
	
	@RequestMapping(value = "/{tsId}/{viewPage}", method = RequestMethod.GET)
	public ModelAndView views(@PathVariable String tsId, @PathVariable String viewPage) throws Exception {
		ModelAndView mv = new ModelAndView(viewPage);
		mv.addObject("tsId", tsId);
		return mv;
	}
	
}
