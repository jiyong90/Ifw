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

import com.isu.ifw.service.WtmCompCreateService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/compCreate")
public class WtmCompCreateController {
	
	@Autowired
	WtmCompCreateService compCreateService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCompCreateList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getTaaCodeList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));

		
		rp.setSuccess("");
		
		List<Map<String, Object>> compCreateList = null;
		try {		
			compCreateList = compCreateService.getCompCreateList(paramMap);
			
			rp.put("DATA", compCreateList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/listDet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCompCreateDetList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getTaaCodeList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));

		
		rp.setSuccess("");
		
		List<Map<String, Object>> compCreateDetList = null;
		try {		
			compCreateDetList = compCreateService.getCompCreateDetList(paramMap);
			
			rp.put("DATA", compCreateDetList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
