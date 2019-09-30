package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmAsyncService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/appl")
public class WtmApplController {
	

	@Autowired
	WtmAsyncService wymAsyncService;
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService flexibleApplService;
	
	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService wtmOtApplService;
	
	@Autowired
	@Qualifier("wtmOtCanApplService")
	WtmApplService wtmOtCanApplService;
	
	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApprList(/*@RequestBody Map<String, Object> paramMap,*/ HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		rp.setSuccess("");
		
		List<Map<String, Object>> apprList = null;
		try {		
			apprList = flexibleApplService.getApprList(tenantId, enterCd, empNo, new HashMap<String, Object>(), userId);
			
			rp.put("apprList", apprList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody WtmApplCode getApplCode(@RequestParam String applCd, HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		return wtmApplCodeRepo.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
	}
	
	@RequestMapping(value="/apply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam applyAppl(@RequestBody Map<String, Object> paramMap
												, HttpServletRequest request) {
		
		validateParamMap(paramMap, "applId", "apprSeq");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String applCd = paramMap.get("applCd").toString();
		int apprSeq = Integer.valueOf(paramMap.get("apprSeq").toString());
				
		try {
			if(applCd!=null && !"".equals(applCd)) {
				if("OT".equals(applCd)) {
					rp = wtmOtApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
				} else if("OT_CAN".equals(applCd)){
					rp = wtmOtCanApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
				} else {
					rp = flexibleApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
					if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) && rp.containsKey("sabun")) {
						paramMap.put("tenantId", tenantId);
						paramMap.put("enterCd", enterCd);
						paramMap.put("sabun",  rp.get("sabun")+"");
						paramMap.put("userId", userId);
						wtmFlexibleEmpMapper.initWtmFlexibleEmpOfWtmWorkDayResult(paramMap);
					}
				}
			}
			if(rp.containsKey("sabun") && rp.containsKey("symd") && rp.containsKey("eymd")) {
				wymAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, rp.get("sabun")+"", rp.get("symd")+"", rp.get("eymd")+"", userId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value="/reject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam rejectAppl(@RequestBody Map<String, Object> paramMap
												, HttpServletRequest request) {
		
		validateParamMap(paramMap, "applId", "apprSeq");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String applCd = paramMap.get("applCd").toString();
		int apprSeq = Integer.valueOf(paramMap.get("apprSeq").toString());
				
		try {
			if(applCd!=null && !"".equals(applCd)) {
				if("OT".equals(applCd)) {
					wtmOtApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else {
					flexibleApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam deleteAppl(@RequestBody Map<String, Object> paramMap
												, HttpServletRequest request) {
		
		validateParamMap(paramMap, "applCd", "applId");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String applCd = paramMap.get("applCd").toString();
				
		try {
			if(applCd!=null && !"".equals(applCd)) {
				if("OT".equals(applCd)) {
					wtmOtApplService.delete(applId);
				} else if("OT_CAN".equals(applCd)) {
					wtmOtCanApplService.delete(applId);
				} else {
					flexibleApplService.delete(applId);
					
					if(paramMap.containsKey("sabun") && paramMap.containsKey("sYmd") && paramMap.containsKey("eYmd")) {
						paramMap.put("tenantId", tenantId);
						paramMap.put("enterCd", enterCd);
						paramMap.put("userId", userId);
						wtmFlexibleEmpMapper.initWtmFlexibleEmpOfWtmWorkDayResult(paramMap);
						
						wymAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, paramMap.get("sabun")+"", paramMap.get("sYmd")+"", paramMap.get("eYmd")+"", userId);
					}
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	/**
	 * 파라미터 맵의 유효성을 검사한다.
	 * 파라미터가 다음의 두 조건을 만족하지 않으면, InvalidParameterException를 발생한다.
	 * 1. map 자체가 null 인 경우
	 * 2. paramName 배열에 기술된 값이 map에 (하나라도) 키로 존재하지 않는 경우
	 * 
	 * @param paramMap
	 * @param parameterNames
	 * @throws InvalidParameterException
	 */
	protected void validateParamMap(Map<String,Object> paramMap, String...parameterNames )throws InvalidParameterException{
		
		// 파라미터가 아무것도 없는 경우에, 아무것도 할 수 없다. 무조건 예외 발생
		if(paramMap == null)
			throw new InvalidParameterException("param map is null.");
		
		if(parameterNames == null)
			return; // 파라미터가 없으면 그냥 리턴..
		
		// 넘겨 받은 이름 배열이 map의 부분 집합인지를 따짐.
		Set<String> paramKeySet = paramMap.keySet();
		Collection<String> params = Arrays.asList(parameterNames);
		
		if(!paramKeySet.containsAll(params))
			throw new InvalidParameterException("required parameter is not found.");
		
	}
}
