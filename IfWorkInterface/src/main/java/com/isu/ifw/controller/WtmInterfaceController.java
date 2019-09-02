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
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService WtmInterfaceService;

	@RequestMapping(value = "/interface/empHis",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getEmpHisIfResult(tenantId); //Servie 호출;
		return ;
	}
	
	@RequestMapping(value = "/interface/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void codeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		WtmInterfaceService.getCodeIfResult(tenantId); //Servie 호출
		return;
	}
}
