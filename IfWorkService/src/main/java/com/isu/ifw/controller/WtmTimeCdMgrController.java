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

import com.isu.ifw.service.WtmTimeCdMgrService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/timeCdMgr")
public class WtmTimeCdMgrController {
	
	@Autowired
	WtmTimeCdMgrService timeCdMgrService;
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getTimeCdList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getTimeCdList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		rp.setSuccess("");
		
		List<Map<String, Object>> timeCdMgtList = null;
		try {		
			timeCdMgtList = timeCdMgrService.getTimeCdMgrList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", timeCdMgtList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setTimeCodeMgrList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
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
		logger.debug("setTimeCodeMgrList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = timeCdMgrService.setTimeCodeMgrList(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/breakList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getTimeBreakMgrList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("breakList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		rp.setSuccess("");
		
		List<Map<String, Object>> timeBreakMgtList = null;
		try {		
			timeBreakMgtList = timeCdMgrService.getTimeBreakMgrList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", timeBreakMgtList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/breakSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setTimeBreakMgrList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
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
		logger.debug("setTimeBreakMgtList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {
			cnt = timeCdMgrService.setTimeBreakMgrList(userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/breakTimeList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getTimeBreakTimeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("breakList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		rp.setSuccess("");
		
		List<Map<String, Object>> timeBreakMgtList = null;
		try {		
			timeBreakMgtList = timeCdMgrService.getTimeBreakTimeList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", timeBreakMgtList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/breakTimeSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setTimeBreakTimeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
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
		logger.debug("setTimeBreakMgtList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {
			cnt = timeCdMgrService.setTimeBreakTimeList(userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/timeCodeList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getTimeCodeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		String holYn = paramMap.get("holYn").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getTimeCdList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		rp.setSuccess("");
		
		List<Map<String, Object>> timeCdMgtList = null;
		try {		
			timeCdMgtList = timeCdMgrService.getTimeCodeList(tenantId, enterCd, holYn);
			
			rp.put("DATA", timeCdMgtList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}

}
