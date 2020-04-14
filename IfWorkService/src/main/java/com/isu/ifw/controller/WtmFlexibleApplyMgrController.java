package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmPropertie;
import com.isu.ifw.entity.WtmRule;
import com.isu.ifw.mapper.WtmFlexibleApplyMgrMapper;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.repository.WtmRuleRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleApplyMgrService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
@RequestMapping(value="/flexibleApply")
public class WtmFlexibleApplyMgrController {
	
	@Autowired
	WtmFlexibleApplyMgrService flexibleApplyService;
	
	
	@Autowired
	WtmFlexibleApplyMgrMapper wtmFlexibleApplyMgrMapper;
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService wtmApplService;
	
	@Autowired
	WtmPropertieRepository propertieRepo;
	
	@Autowired
	WtmRuleRepository ruleRepo;
	
	@Autowired
	@Qualifier("flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	private final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sYmd = paramMap.get("sYmd").toString();
		
		logger.debug("[getApplyList] " + tenantId + enterCd + sYmd);
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyList(tenantId, enterCd, sYmd);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("[getApplyList rp] " + rp.toString());

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
		
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		logger.debug("[setApplyList] " + convertMap.toString());

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
		logger.debug("[setApplyList rp] ", rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/getEymd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEymd(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		logger.debug("[getEymd] " + tenantId +enterCd);

		rp.setSuccess("");
		
		Map<String, Object> codeMap = null;
		try {
			codeMap = flexibleApplyService.getEymd(paramMap);
			
			rp.put("DATA", codeMap);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("[getEymd] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/workType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getworkType(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		Long flexibleStdMgrId = Long.valueOf(paramMap.get("flexibleStdMgrId").toString());
		logger.debug("[getworkType] " + tenantId +enterCd+flexibleStdMgrId);

		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getworkTypeList(flexibleStdMgrId);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("[getworkType rp] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/apply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApply(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("확정에 성공하였습니다.");
		try {
			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			String empNo = sessionData.get("empNo").toString();
			String userId = sessionData.get("userId").toString();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("userId", userId);
			
			logger.debug("[setApply] ", paramMap.toString());
			
			String workTypeCd = paramMap.get("workTypeCd").toString();
			Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());
			
			// 확정대상자 조회
			List<Map<String, Object>> searchList =  wtmFlexibleApplyMgrMapper.getApplyConfirmList(paramMap);
			if(searchList==null || searchList.size()==0) {
				rp.setFail("확정 대상자가 없습니다.");
				return rp;
			}
			
			logger.debug("[setApply] 확정대상자 " + searchList.size());
			
			//반복기준조회
			List<Map<String, Object>> ymdList = flexibleApplyService.getApplyYmdList(paramMap);
			
			//오류체크까지 하고 리턴
			int empCnt = 0;
			for(int i=0; i < searchList.size(); i++) {
				Map<String, Object> validateMap = new HashMap<>();
				validateMap = searchList.get(i);
				String sabun = validateMap.get("sabun").toString();
				
				WtmPropertie propertie = propertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_FLEXIBLE_EMP_EXCEPT_TARGET");
				
				String ruleValue = null;
				String ruleType = null;
				if(propertie!=null && propertie.getInfoValue()!=null && !"".equals(propertie.getInfoValue())) {
					WtmRule rule = ruleRepo.findByTenantIdAndEnterCdAndRuleNm(tenantId, enterCd, propertie.getInfoValue());
					if(rule!=null && rule.getRuleValue()!=null && !"".equals(rule.getRuleValue())) {
						ruleType = rule.getRuleType();
						ruleValue = rule.getRuleValue();
					}
				}
				
				boolean isTarget = false;
				if(ruleValue!=null) 
					isTarget = flexibleEmpService.isRuleTarget(tenantId, enterCd, sabun, ruleType, ruleValue);
				
				if(!isTarget) {
					for(int j=0; j < ymdList.size(); j++) {
						// 반복 구간별 밸리데이션 체크 및 유연근무기간 입력
						paramMap.put("sYmd", ymdList.get(j).get("symd"));
						paramMap.put("eYmd", ymdList.get(j).get("eymd"));
						
						//탄근제 validation 체크를 위한 param
						paramMap.put("adminYn", "Y");
						paramMap.put("flexibleApplyId", flexibleApplyId);
						
						rp = wtmApplService.validate(tenantId, enterCd, sabun, workTypeCd, paramMap);
						if(rp.getStatus().equals("FAIL")) {
							return rp;
						}
						
						validateMap.put("flexibleApplyId", flexibleApplyId);
						validateMap.put("workTypeCd", workTypeCd);
					}
					empCnt++;
				}
			}
			
			//비동기로 확정처리
			//flexibleApplyService.setApplyAsync(searchList, ymdList);
			//동기로 확정처리
			int cnt = flexibleApplyService.setApply(searchList, ymdList);
			rp.setMessage("총 " + empCnt + "명의 확정 대상자 중 " + cnt + "명의 확정에 성공하였습니다.");
		} catch(Exception e) {
			rp.setFail("확정 시 오류가 발생했습니다.");
		} 

		logger.debug("[setApply rp] " + rp.toString());
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
		logger.debug("[setApplyGrpList] " + paramMap.toString());

		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

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
		logger.debug("[setApplyGrpList rp] " + rp.toString());

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
		logger.debug("[setApplyEmpList] " + paramMap.toString());

		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

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
		logger.debug("[setApplyEmpList rp] " + rp.toString());

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
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
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
	
	@RequestMapping(value="/elasDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam createElasPlan(@RequestBody Map<String, Object> paramMap
		    									, HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleApplyService.createElasPlan(tenantId, enterCd, Long.valueOf(paramMap.get("flexibleApplyId").toString()), userId);
	}
}
