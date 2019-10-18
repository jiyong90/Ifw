package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmWorktimeService;
import com.isu.option.vo.ReturnParam;

@RestController
public class WtmWorktimeController {
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmWorktimeService worktimeService;
	
	@RequestMapping(value="/worktime/check/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeCheckList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getWorkTimeCheckList Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		MDC.put("paramMap", paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> workTimeCheckList = null;
		try {		
			workTimeCheckList = worktimeService.getWorktimeCheckList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", workTimeCheckList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/worktime/detail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeDetail(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getWorkTimeDetail Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		MDC.put("paramMap", paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> getWorkTimeDetail = null;
		try {		
			getWorkTimeDetail = worktimeService.getWorktimeDetail(tenantId, enterCd, paramMap);
			
			rp.put("DATA", getWorkTimeDetail);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/entry/check/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEntryCheckList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getEntryCheckList Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		MDC.put("paramMap", paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> entryCheckList = null;
		try {		
			entryCheckList = worktimeService.getEntryCheckList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", entryCheckList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
