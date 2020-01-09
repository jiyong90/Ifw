package com.isu.ifw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmScheduleService;

@RestController
@RequestMapping(value="/schedule")
public class WtmScheduleController {
	
	@Autowired
	private WtmScheduleService WtmScheduleService;
	
	@Autowired
	WtmInoutService inoutService;
	
	private RestTemplate restTemplate;
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	// 일마감 배치(캘린더기준 data 기준 대상자 일마감)
	@RequestMapping(value = "/colseDay",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCloseDay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		try {
			System.out.println("colseDay start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			WtmScheduleService.setCloseDay(tenantId);
			System.out.println("colseDay end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
