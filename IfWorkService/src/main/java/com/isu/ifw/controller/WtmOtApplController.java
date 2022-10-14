package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmMsgService;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.ifw.util.WtmUtil;


@RestController
@RequestMapping(value="/otAppl")
public class WtmOtApplController {
	
	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService otApplService;
	
	@Autowired
	WtmMsgService msgService;
	
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
		
		return otApplService.getAppl(tenantId, enterCd, sabun, applId, userId);
	}
	
	/*@RequestMapping(value="/subs/prev", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getPrevOtSubsAppl(@RequestParam Map<String, Object> paramMap
												, HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		return otApplService.getPrevApplList(tenantId, enterCd, sabun, paramMap, userId);
	}*/
	
	@RequestMapping(value="/preCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam preCheckOtAppl(@RequestParam Map<String, Object> paramMap
												, HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();
		
		return otApplService.preCheck(tenantId, enterCd, sabun, workTypeCd, paramMap);
	}
	
	@RequestMapping(value="/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam requestOtAppl(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) {
		
		//validateParamMap(paramMap, "workTypeCd", "flexibleStdMgrId", "otSdate", "otEdate", "reasonCd", "reason");
		validateParamMap(paramMap, "workTypeCd", "otSdate", "otEdate", "reasonCd", "reason");
		
		ReturnParam rp = new ReturnParam();
		ReturnParam rp2 = new ReturnParam();
		rp.setSuccess("");
		
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = null;
		if(paramMap.get("applId")!=null && !"".equals(paramMap.get("applId")))
			applId = Long.valueOf(paramMap.get("applId").toString());
		
		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();
				
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			Map<String, Object> applSabuns = null;
			if(paramMap.get("applSabuns")!=null && !"".equals(paramMap.get("applSabuns"))) {
				applSabuns = mapper.readValue(paramMap.get("applSabuns").toString(), new HashMap<String, Object>().getClass());
				
				if(applSabuns!=null && applSabuns.keySet().size()>0) {
					for(String applSabun : applSabuns.keySet()) {
						rp = otApplService.validate(tenantId, enterCd, applSabun, workTypeCd, paramMap);
						System.out.println("ydh 1 : ");
						if(rp!=null && rp.getStatus()!=null && "FAIL".equals(rp.getStatus())) {
							return rp;
						}
					}
					System.out.println("ydh 8 : " +rp2);
					rp2 = otApplService.request(tenantId, enterCd, applId, workTypeCd, paramMap, sabun, userId);
					System.out.println("ydh 2 : " +rp2);
				} else {
					rp.setFail("대상자를 선택해 주세요.");
					return rp;
				}
			} else {
				rp = otApplService.validate(tenantId, enterCd, sabun, workTypeCd, paramMap);
				if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
					rp2 = otApplService.request(tenantId, enterCd, applId, workTypeCd, paramMap, sabun, userId);
					System.out.println("ydh 3 : " + rp2);
				}
				
			}
			
			System.out.println("ydh 4 : ");
			System.out.println("ydh 5 : " + rp2.get("to"));
			//메일 전송
			if(rp2.getStatus()!=null && "OK".equals(rp2.getStatus()) 
					&& rp2.containsKey("from") && rp2.get("from")!=null && !"".equals(rp2.get("from")) 
					&& rp2.containsKey("to") && rp2.get("to")!=null && !"".equals(rp2.get("to"))) {
				List<String> toSabuns = (List<String>)rp2.get("to");
				
				System.out.println("toSabuns : " + mapper.writeValueAsString(toSabuns));
				
				msgService.sendMailForAppl(tenantId, enterCd, rp2.get("from").toString(), toSabuns, "FLEX", "APPR");
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
	
	/**
	 * 연장근무관리(관리자) 결재상태 변경
	 * @param request
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value="/saveApplSts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam saveApplSts(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		int cnt = 0;
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		

		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");

		try {
			rp = otApplService.saveWtmApplSts(tenantId, enterCd, sabun, userId, convertMap);
			
			if(rp!=null && rp.getStatus()!=null && "FAIL".equals(rp.getStatus())) {
				return rp;
			}
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		
		return rp;
	}
}
