package com.isu.ifw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.repository.WtmPushMgrRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmScheduleService;

@RestController
@RequestMapping(value="/schedule")
public class WtmScheduleController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	private WtmScheduleService wtmScheduleService;
	
	@Autowired
	WtmInoutService inoutService;
	
	private RestTemplate restTemplate;
	
	@Resource
	WtmPushMgrRepository pushMgrRepository;

	
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
			wtmScheduleService.setCloseDay(tenantId);
			System.out.println("colseDay end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	//하루에 몇번 보내야 하는지 기준이 없넹...
	//일단 타각알림의 경우 분단위로 스케줄러가 돌아야 하니까...
	@RequestMapping(value="/push/min", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void sendPushMin(HttpServletRequest request, 
			@RequestParam(value = "tenantId", required = false) Long tenantId,
			@RequestParam(value="enterCd", required = false) String enterCd) {
		
		logger.debug("================push/send s " + tenantId + ", " + enterCd);
		wtmScheduleService.sendPushMessageMin(tenantId, enterCd);
		logger.debug("================push/send e");
	}
	
	//근무시간 관련 알림은 하루에 몇번 돌지 정해야 하지만 일단 한번
	@RequestMapping(value="/push/day", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void sendPushDay(HttpServletRequest request, 
			@RequestParam(value = "tenantId", required = false) Long tenantId,
			@RequestParam(value="enterCd", required = false) String enterCd) {
		
		logger.debug("================push/send s " + tenantId + ", " + enterCd);
		wtmScheduleService.sendPushMessageDay(tenantId, enterCd);
		logger.debug("================push/send e");
	}
}
