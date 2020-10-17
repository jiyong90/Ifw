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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.mapper.WtmFlexibleApplMapper;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmMsgService;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmFlexibleStdVO;


@RestController
@RequestMapping(value="/flexibleAppl")
public class WtmFlexibleApplController {
	
	final static String APPL_FLEXIBLE_SELE = "FLEXIBLE_SELE";	//선택근무제신청
	final static String APPL_FLEXIBLE_ELAS = "FLEXIBLE_ELAS";	//탄력근무제신청
	final static String APPL_FLEXIBLE_DIFF = "FLEXIBLE_DIFF"; 	//시차근무제신청
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService flexibleApplService;
	
	@Autowired
	WtmFlexibleApplMapper flexibleApplMaper;
	
	@Autowired
	WtmFlexibleStdMgrRepository flexibleStdMgrRepo;
	
	@Autowired
	WtmMsgService msgService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> flexibleAppl(@RequestParam Long applId
		    									, HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleApplService.getAppl(tenantId, enterCd, empNo, applId, userId);
	}
	
	@RequestMapping(value="/imsi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam imsiFlexitime(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) {
		
		validateParamMap(paramMap, "flexibleStdMgrId","workTypeCd", "sYmd", "eYmd");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = null;
		String workTypeCd = null;
		if(paramMap.get("applId")!=null && !"".equals(paramMap.get("applId")))
			applId = Long.valueOf(paramMap.get("applId").toString());
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();
			
		try {
			rp = flexibleApplService.imsi(tenantId, enterCd, applId, workTypeCd, paramMap, WtmApplService.APPL_STATUS_IMSI, empNo, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		return rp;
	}
	
	@RequestMapping(value="/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam requestFlexitime(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) {
		
		validateParamMap(paramMap, "applId", "flexibleStdMgrId","workTypeCd", "sYmd", "eYmd", "reason");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();
				
		try {
			rp = flexibleApplService.request(tenantId, enterCd, applId, workTypeCd, paramMap, empNo, userId);
			
			ObjectMapper mapper = new ObjectMapper();
			
			System.out.println("rp : " + mapper.writeValueAsString(rp));
			
			//메일 전송
			if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) 
					&& rp.containsKey("from") && rp.get("from")!=null && !"".equals(rp.get("from")) 
					&& rp.containsKey("to") && rp.get("to")!=null && !"".equals(rp.get("to"))) { 
				List<String> toSabuns = (List<String>)rp.get("to");
				
				System.out.println("toSabuns : " + mapper.writeValueAsString(toSabuns));
				
				msgService.sendMailForAppl(tenantId, enterCd, rp.get("from").toString(), toSabuns, "FLEX", "APPR");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public @ResponseBody List<WtmFlexibleStdVO> getImsiFlexibleAppl(@RequestParam String applStatusCd
																		,HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", empNo);
		paramMap.put("applStatusCd", applStatusCd);

		return flexibleApplMaper.getImsiFlexAppl(paramMap);
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
