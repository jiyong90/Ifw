package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.WtmHolidayMgrService;
import com.isu.ifw.service.WtmWorktimeService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/holiday")
public class WtmHolidayMgrController {
	
	@Autowired
	WtmHolidayMgrService holidayMgrService;
	
	@Autowired
	WtmWorktimeService worktimeService;
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getHolidayMgrList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> holidayList = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			holidayList = holidayMgrService.getHolidayList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", holidayList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setHolidayMgrList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		logger.debug("setHolidayMgrList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		int cnt = 0;
		try {		
			cnt = holidayMgrService.setHolidayList(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/change", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam changeWorktime(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
	
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		
		logger.debug("workTimeChangeTarget Start " + paramMap.toString());
		rp.setSuccess("");
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);
		
		logger.debug("convertMap ydh Start " + convertMap.toString());
		
		List<Map<String, Object>> getWorkTimeChangeTarget = null;
		try {
			
			if(convertMap.containsKey("updateRows") && ((List)convertMap.get("updateRows")).size() > 0) {
				holidayMgrService.changeWorktime(tenantId, enterCd, convertMap, userId);
				rp.setSuccess("저장에 성공하였습니다.");
			} else {
				rp.setSuccess("저장에 실패하였습니다.");	
			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("근무시간 변경 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
