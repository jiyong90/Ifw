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

import com.isu.ifw.service.WtmWorkteamEmpService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
@RequestMapping(value="/workteam")
public class WtmWorkteamEmpController {
	
	private final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmWorkteamEmpService workteamService;
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkteamList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		logger.debug("getWorkteamList paramMap" + paramMap.toString());
	
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
	public @ResponseBody ReturnParam setWorkteamList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		logger.debug("setWorkteamList paramMap : " + paramMap.toString());
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		logger.debug("setWorkteamList convertMap : " + convertMap.toString());

		try {		
			rp = workteamService.setWorkteamList(tenantId, enterCd, userId, convertMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
}
