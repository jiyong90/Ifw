package com.isu.ifw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmInterfaceService;

@RestController
@RequestMapping(value="/interface")
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService WtmInterfaceService;

	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void codeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String lastDataTime = "20080101023000";	// 최종 data 전달data시간
		WtmInterfaceService.getCodeIfResult(tenantId, lastDataTime); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/holiday",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void holidayIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공휴일정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String lastDataTime = "20190817023000";	// 최종 data 전달data시간
		WtmInterfaceService.getHolidayIfResult(tenantId, lastDataTime); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/taaCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void taaCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 근태코드정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String lastDataTime = "20080101023000";	// 최종 data 전달data시간
		WtmInterfaceService.getTaaCodeIfResult(tenantId, lastDataTime); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/orgCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void orgCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 조직코드정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String lastDataTime = "20080101023000";	// 최종 data 전달data시간
		WtmInterfaceService.getOrgCodeIfResult(tenantId, lastDataTime); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/orgMapCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void orgMapCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사업장, 근무조 정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String lastDataTime = "20080101023000";	// 최종 data 전달data시간
		WtmInterfaceService.getOrgMapCodeIfResult(tenantId, lastDataTime); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/empHis",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String lastDataTime = "20170701023000";	// 최종 data 전달data시간
		WtmInterfaceService.getEmpHisIfResult(tenantId, lastDataTime); //Servie 호출;
		return ;
	}
}
