package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.service.WtmInterfaceService;

@RestController
@RequestMapping(value="/interface")
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService WtmInterfaceService;
	
	private RestTemplate restTemplate;
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void getcodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getCodeIfResult(tenantId);
		
		return;
	}
	
	@RequestMapping(value = "/holiday",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void holidayIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공휴일정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getHolidayIfResult(tenantId); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/taaCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void taaCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 근태코드정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getTaaCodeIfResult(tenantId); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/orgCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void orgCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 조직코드정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getOrgCodeIfResult(tenantId); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/orgMapCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void orgMapCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사업장, 근무조 정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getOrgMapCodeIfResult(tenantId); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/empHis",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getEmpHisIfResult(tenantId); //사원 변경정보 저장 Servie 호출
		return ;
	}
}
