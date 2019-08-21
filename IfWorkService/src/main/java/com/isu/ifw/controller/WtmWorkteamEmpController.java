package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmWorkteamEmpService;
import com.isu.ifw.util.WtmUtil;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/workteam")
public class WtmWorkteamEmpController {
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmWorkteamEmpService workteamService;
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkteamList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getWorkteamList Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		MDC.put("paramMap", paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> workteamList = null;
		try {		
			workteamList = workteamService.getWorkteamList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", workteamList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setWorkteamList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		logger.debug("setTaaCodeList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = workteamService.setWorkteamList(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//근무조 저장 후 프로시저 호출 
		//P_WTM_FLEXIBLE_EMP_RESET
		
		return rp;
	}
}
