package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.WtmCalendarService;

@RestController
@RequestMapping(value="/calendar")
public class WtmCalendarController {

	@Autowired
	WtmCalendarService wtmCalendarService;
	
	/**
	 * 달력 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getCalendar(@RequestParam Map<String, Object> paramMap
													   		 , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		String enterCd = null;
		String bisinessPlaceCd = null;
		if(sessionData.get("enterCd")!=null)
			enterCd = sessionData.get("enterCd").toString();
		if(sessionData.get("bisinessPlaceCd")!=null)
			bisinessPlaceCd = sessionData.get("bisinessPlaceCd").toString();
		
		Map<String, Object> resultMap = wtmCalendarService.getCalendar(tenantId, enterCd, bisinessPlaceCd, paramMap);
		
		//ObjectMapper mapper = new ObjectMapper();
		//System.out.println("result : " + mapper.writeValueAsString(resultMap));

		return resultMap;
	}
	
	/**
	 * 근태 달력 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/worktime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getWorkTimeCalendar(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		paramMap.put("tenantId", tenantId);
		
		List<Map<String, Object>> resultMap = wtmCalendarService.getWorkTimeCalendar(paramMap);
		
		//ObjectMapper mapper = new ObjectMapper();
		//System.out.println("result : " + mapper.writeValueAsString(resultMap));

		return resultMap;
	}

}
