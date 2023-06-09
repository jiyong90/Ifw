package com.isu.ifw.controller;

import com.isu.ifw.service.WtmWorktimeService;
import com.isu.ifw.vo.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.*;

@RestController
public class WtmWorktimeController {
	
	private final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmWorktimeService worktimeService;
	
	@RequestMapping(value="/worktime/check/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeCheckList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		
		logger.debug("getWorkTimeCheckList Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> workTimeCheckList = null;
		try {		
			workTimeCheckList = worktimeService.getWorktimeCheckList(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", workTimeCheckList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/worktime/detail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeDetail(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		logger.debug("getWorkTimeDetail Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> getWorkTimeDetail = null;
		try {		
			getWorkTimeDetail = worktimeService.getWorktimeDetail(tenantId, enterCd, paramMap);
			
			rp.put("DATA", getWorkTimeDetail);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/entry/check/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEntryCheckList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		
		logger.debug("getEntryCheckList Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> entryCheckList = null;
		try {		
			entryCheckList = worktimeService.getEntryCheckList(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", entryCheckList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/entry/diff/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEntryDiffList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		
		logger.debug("getEntryDiffList Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> entryDiffList = null;
		try {		
			entryDiffList = worktimeService.getEntryDiffList(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", entryDiffList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/worktime/change/target", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeChangeTarget(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		
		logger.debug("getWorkTimeChangeTarget Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> getWorkTimeChangeTarget = null;
		try {		
			getWorkTimeChangeTarget = worktimeService.getWorkTimeChangeTarget(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", getWorkTimeChangeTarget);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/worktime/change/target/workplan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkPlan(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		logger.debug("getWorkPlan Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> workplan = null;
		try {		
			workplan = worktimeService.getWorkPlan(tenantId, enterCd, paramMap);
			
			rp.put("DATA", workplan);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/worktime/change", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam changeWorktime(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {
		
		validateParamMap(paramMap, "ymd", "timeCdMgrId");
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		
		logger.debug("workTimeChangeTarget Start " + paramMap.toString());
		rp.setSuccess("");
		
		List<Map<String, Object>> getWorkTimeChangeTarget = null;
		try {		
			worktimeService.changeWorktime(tenantId, enterCd, paramMap, userId);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("근무시간 변경 시 오류가 발생했습니다.");
			return rp;
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


	@RequestMapping(value="/worktime/checkAll/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeCheckAllList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();

		logger.debug("getWorkTimeCheckAllList Start " + paramMap.toString());
		rp.setSuccess("");

		List<Map<String, Object>> workTimeCheckAllList = null;
		try {
			workTimeCheckAllList = worktimeService.getWorktimeCheckAllList(tenantId, enterCd, sabun, paramMap);

			rp.put("DATA", workTimeCheckAllList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}

	@RequestMapping(value="/worktime/workCalendarList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkCalendarList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();

		paramMap.put("searchKeyword", sabun);

		logger.debug("workCalendarList Start " + paramMap.toString());
		rp.setSuccess("");

		List<Map<String, Object>> workTimeCheckAllList = null;
		try {
			if(sabun != null && !"".equals(sabun)) {
				workTimeCheckAllList = worktimeService.getWorktimeCheckAllList(tenantId, enterCd, sabun, paramMap);
				rp.put("DATA", workTimeCheckAllList);
			}

		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
}
