package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
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

import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/otCanAppl")
public class WtmOtCanApplController {
	
	@Autowired
	@Qualifier("wtmOtCanApplService")
	WtmApplService otCanApplService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getOtAppl(@RequestParam Long applId
												, HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return otCanApplService.getAppl(tenantId, enterCd, sabun, applId, userId);
	}
	
	@RequestMapping(value="/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam requestOtCanAppl(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) {
		
		validateParamMap(paramMap, "workDayResultId", "status", "workTypeCd", "reason");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = null;
		if(paramMap.get("applId")!=null && !"".equals(paramMap.get("applId")))
			applId = Long.valueOf(paramMap.get("applId").toString());
		
		String status = null;
		if(paramMap.get("status")!=null && !"".equals(paramMap.get("status")))
			status = paramMap.get("status").toString();
		
		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();
				
		try {
			//rp = otCanApplService.validate(tenantId, enterCd, sabun, workTypeCd, paramMap);
			//if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
				otCanApplService.request(tenantId, enterCd, applId, workTypeCd, paramMap, sabun, userId);
			//}
			
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
