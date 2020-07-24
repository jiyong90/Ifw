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

import com.isu.ifw.service.WtmCompApplServiceImpl;
import com.isu.ifw.service.WtmCompMgrService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/compAppl")
public class WtmCompApplController {
	
	@Autowired
	WtmCompApplServiceImpl compService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	

	@RequestMapping(value="/info", method = RequestMethod.GET)
	public @ResponseBody ReturnParam getCompInfo(HttpServletRequest request
								, @RequestParam String ymd) throws Exception {
		
		System.out.println("/info :::::::::::::::::::::::::;");		
		ReturnParam rp = new ReturnParam();
		
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		Long tenantId = Long.parseLong(sessionData.get("tenantId").toString());
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		String empNo = sessionData.get("empNo").toString();
		rp.setSuccess("");
		
		Map<String, Object> infoMap = compService.getInitInfo(tenantId, enterCd, empNo, ymd);
		rp.put("result", infoMap);
		return rp;
	} 
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCompList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> compList = null;
		try {		
			compList = compService.getApprList(tenantId, enterCd, null, paramMap, userId);
			rp.put("DATA", compList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	} 
}
