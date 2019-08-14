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
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmDayWorkVO;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleEmp")
public class WtmFlexibleEmpController {
	
	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	/**
	 * 해당 월의 근무제 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getFlexibleEmpList(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> flexibleList = null;
		
		try {
			flexibleList = flexibleEmpService.getFlexibleEmpList(tenantId, enterCd, sabun, paramMap, userId);
			rp.put("flexibleList", flexibleList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	/**
	 * 선택한 기간의 근무제 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/range", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getFlexibleRangeInfo(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();

		return flexibleEmpService.getFlexibleRangeInfo(tenantId, enterCd, sabun, paramMap);
	}
	
	/**
	 * 선택한 날의 근무시간 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/worktime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getFlexibleDayInfo(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();

		return flexibleEmpService.getFlexibleWorkTimeInfo(tenantId, enterCd, sabun, paramMap);
	}

	/**
	 * 이전에 시행한 근무제 기간 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/prev",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getPrevFlexibleEmp(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> prevFlexible = null;
		
		try {
			prevFlexible = flexibleEmpService.getPrevFlexible(tenantId, enterCd, empNo);
			rp.put("prevFlexible", prevFlexible);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value = "/dayResults",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getDayResults(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		
		validateParamMap(paramMap, "ymd");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());

		return flexibleEmpService.getWorkDayResult(tenantId, enterCd, empNo, paramMap.get("ymd").toString(), userId);
	}
	
	@RequestMapping(value = "/dayWorks",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getDayWorks(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		return flexibleEmpService.getFlexibleEmpListForPlan(tenantId, enterCd, sabun, paramMap, userId);
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam saveWorkDayResult(@RequestBody Map<String, Object> paramMap
														, HttpServletRequest request) {
		
		validateParamMap(paramMap, "flexibleEmpId");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Long flexibleEmpId = Long.valueOf(paramMap.get("flexibleEmpId").toString());
		Map<String, Object> dayResult = new HashMap<String, Object>();
		if(paramMap.containsKey("dayResult") && paramMap.get("dayResult")!=null && !"".equals(paramMap.get("dayResult"))) 
			dayResult = (Map<String, Object>)paramMap.get("dayResult");
				
		try {
			
			System.out.println("flexibleEmpId : " + flexibleEmpId);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("dayResult : " + mapper.writeValueAsString(dayResult));
			System.out.println("userId : " + userId);
			flexibleEmpService.save(flexibleEmpId, dayResult, userId);
			List<WtmDayWorkVO> dayWorks = flexibleEmpService.getDayWorks(flexibleEmpId, userId);
			rp.put("dayWorks", dayWorks);
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
	 * calendarId로 일근무표 조회(관리자용)
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/caldays", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getDayList(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> flexibleList = null;
		
		try {
			flexibleList = flexibleEmpService.getEmpDayResults(tenantId, enterCd, Long.valueOf(paramMap.get("workCalendarId").toString()));
			rp.put("DATA", flexibleList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	/**
	 * 일근무표  다건 수정(관리자용)
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save/caldays", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam saveDayList(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		rp.setSuccess("");
		
		try {
			flexibleEmpService.saveEmpDayResults(tenantId, enterCd, userId, convertMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
}
