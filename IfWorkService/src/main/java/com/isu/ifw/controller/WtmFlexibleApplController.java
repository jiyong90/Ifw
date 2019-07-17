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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.vo.WtmFlexibleApplVO;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleAppl")
public class WtmFlexibleApplController {
	
	final static String APPL_FLEXIBLE_SELE = "FLEXIBLE_SELE";	//선택근무제신청
	final static String APPL_FLEXIBLE_ELAS = "FLEXIBLE_ELAS";	//탄력근무제신청
	final static String APPL_FLEXIBLE_DIFF = "FLEXIBLE_DIFF"; 	//시차근무제신청
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService flexibleApplService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getFlexitime(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		
		validateParamMap(paramMap, "sYmd");
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		
		rp.setSuccess("");
		
		WtmFlexibleApplVO flexibleAppl = null;
		try {		
			flexibleAppl = flexibleApplService.getFlexibleAppl(tenantId, enterCd, empNo, paramMap);
			rp.put("flexibleAppl", flexibleAppl);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam imsiFlexitime(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		
		validateParamMap(paramMap, "flexibleStdMgrId","workTypeCd", "sYmd", "eYmd");
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		
		Long applId = null;
		Long flexibleStdMgrId = null;
		String workTypeCd = null;
		if(paramMap.get("flexibleStdMgrId")!=null && !"".equals(paramMap.get("flexibleStdMgrId")))
			flexibleStdMgrId = Long.valueOf(paramMap.get("flexibleStdMgrId").toString());
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();
				
		flexibleApplService.imsi(tenantId, enterCd, applId, flexibleStdMgrId, workTypeCd, paramMap, empNo);
		rp.setSuccess("");
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
