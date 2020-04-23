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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.WtmWorkteamMgrService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/workteamMgr")
public class WtmWorkteamMgrController {
	
	@Autowired
	WtmWorkteamMgrService workteamMgrService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getBaseWorkList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> workList = null;
		try {		
			workList = workteamMgrService.getWorkteamMgrList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", workList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("getBaseWorkList rp " + rp.toString());
		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setBaseWorkList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		logger.debug("setBaseWorkList Controller Start " + paramMap.toString());
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		logger.debug("setBaseWorkList Controller convertMap " + convertMap.toString());
		
//		int cnt = 0;
		try {		
			rp = workteamMgrService.setWorkteamMgrList(tenantId, enterCd, userId, convertMap);
//			if(cnt > 0) {
//				rp.setSuccess("저장이 성공하였습니다.");
//				return rp;
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/workteamCd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkteamCdList(HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper mapper = new ObjectMapper();
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> wtmWorkteamCd = null;
		
		try {
			wtmWorkteamCd = workteamMgrService.getWorkteamCdList(tenantId, enterCd);
			rp.put("DATA", wtmWorkteamCd);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("getWorkteamCdList rp " + rp.toString());

		return rp;
	}
}
