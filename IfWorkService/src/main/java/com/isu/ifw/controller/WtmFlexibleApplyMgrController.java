package com.isu.ifw.controller;

import java.util.HashMap;
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

import com.isu.ifw.service.WtmFlexibleApplyMgrService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
@RequestMapping(value="/flexibleApply")
public class WtmFlexibleApplyMgrController {
	
	@Autowired
	WtmFlexibleApplyMgrService flexibleApplyService;
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sYmd = paramMap.get("sYmd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyList(tenantId, enterCd, sYmd);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApplyList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
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
		logger.debug("setApplyList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = flexibleApplyService.setApplyList(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/getEymd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEymd(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		Map<String, Object> codeMap = null;
		try {
			codeMap = flexibleApplyService.getEymd(paramMap);
			
			rp.put("DATA", codeMap);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/workType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getworkType(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		Long flexibleStdMgrId = Long.valueOf(paramMap.get("flexibleStdMgrId").toString());
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getworkTypeList(flexibleStdMgrId);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	
	
	
	@RequestMapping(value="/apply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApply(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("확정 시 오류가 발생했습니다.");
		try {
			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			String empNo = sessionData.get("empNo").toString();
			String userId = sessionData.get("userId").toString();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("userId", userId);
			
			MDC.put("sessionId", request.getSession().getId());
			MDC.put("logId", UUID.randomUUID().toString());
			MDC.put("type", "C");
			MDC.put("param", paramMap.toString());
			logger.debug("setApply Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			
			Map<String, Object> convertMap = new HashMap();
			convertMap.put("enterCd", enterCd);
			convertMap.put("tenantId", tenantId);
			convertMap.put("userId", userId);
			
			rp = flexibleApplyService.setApply(paramMap);
	
			MDC.put("convertMap", convertMap);
		} catch(Exception e) {
			rp.setStatus("FAIL");
			rp.setFail("확정 시 오류가 발생했습니다.");
			return rp;
		}
		
		//rp.setSuccess("");
		return rp;
	}
	
	@RequestMapping(value="/grpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyGrpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyGrpList(paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/grpSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApplyGrpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		logger.debug("setApplyList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = flexibleApplyService.setApplyGrpList(userId, flexibleApplyId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/empList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyEmpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyEmpList(paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/empSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApplyEmpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		logger.debug("setApplyList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = flexibleApplyService.setApplyEmpList(userId, flexibleApplyId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/empPopuplist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyEmpPopList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> searchList = null;
		try {
			searchList = flexibleApplyService.getApplyEmpPopList(paramMap);
			
			rp.put("DATA", searchList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/elasDetail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getElasDetail(@RequestParam Map<String, Object> paramMap
		    									, HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleApplyService.getElasDetail(tenantId, enterCd, paramMap, userId);
	}
	
}
