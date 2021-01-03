package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmTimeCdMgr;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.mapper.WtmEmpHisMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmTimeCdMgrRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.service.WtmAsyncService;
import com.isu.ifw.service.WtmFlexibleEmpResetService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmDayWorkVO;


@RestController
@RequestMapping(value="/flexibleEmp")
public class WtmFlexibleEmpController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	
	@Autowired
	WtmAsyncService wymAsyncService;
	
	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	@Autowired
	private WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired private WtmFlexibleEmpResetService flexibleEmpResetService;
	@Autowired private WtmEmpHisRepository wtmEmpHisRepo;

	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;

	@Autowired
	WtmFlexibleStdMgrRepository flexibleStdMgrRepo;

	@Autowired
	WtmTimeCdMgrRepository timeCdMgrRepo;

	@Autowired
	WtmEmpHisMapper empHisMapper;
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
		String userId = sessionData.get("userId").toString();
		
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
	 * 선택한 날의 근무일 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/day", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getFlexibleDayInfo(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();

		return flexibleEmpService.getFlexibleDayInfo(tenantId, enterCd, sabun, paramMap);
	}
	
	/**
	 * 선택한 날의 근무시간(소정, 연장 등) 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/worktime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getFlexibleWorkTimeInfo(@RequestParam Map<String, Object> paramMap
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
		String userId = sessionData.get("userId").toString();

		return flexibleEmpService.getWorkDayResult(tenantId, enterCd, empNo, paramMap.get("ymd").toString(), userId);
	}
	
	@RequestMapping(value = "/dayWorks",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getDayWorks(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleEmpService.getFlexibleEmpListForPlan(tenantId, enterCd, sabun, paramMap, userId);
	}
	
	/**
	 * 해당 일의 근무시간(분) 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/workHm",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> calcMinuteExceptBreaktime(@RequestParam Map<String, Object> paramMap
													    			, HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleEmpService.getDayWorkHm(tenantId, enterCd, sabun, paramMap, userId);
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
		String userId = sessionData.get("userId").toString();
		
		Long flexibleEmpId = Long.valueOf(paramMap.get("flexibleEmpId").toString());
		/*Map<String, Object> dayResult = new HashMap<String, Object>();
		String symd = "";
		String eymd = "";
		
		if(paramMap.containsKey("dayResult") && paramMap.get("dayResult")!=null && !"".equals(paramMap.get("dayResult"))) {
			dayResult = (Map<String, Object>)paramMap.get("dayResult");
			
			if(dayResult.size()>0) {
				int i = 0;
				for(String k : dayResult.keySet()) {
					String date = k;
					
					if(i==0) {
						symd = date;
						eymd = date;
					} else {
						if(date.compareTo(symd) == -1)
							symd = date;
						if(date.compareTo(eymd) == 1)
							eymd = date;
					}
					i++;
				}
			}
		}*/
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			rp = flexibleEmpService.save(flexibleEmpId, (Map<String, Object>)paramMap.get("dayResult"), userId);
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", empNo);
			
			List<Map<String, Object>> plans = flexEmpMapper.getWorktimePlan(paramMap);
			List<WtmDayWorkVO> dayWorks = flexibleEmpService.getDayWorks(plans, userId);
			rp.put("dayWorks", dayWorks);
			
			/*
			//조회성 데이터는 비동기 호출하지 않음
			//term을 만들고 해당 근무제의 약정 근로 시간과 계획 시간을 조회해야 하기 때문에
			if(rp.containsKey("sabun") && !"".equals(symd) && !"".equals(eymd)) {
				wymAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, rp.get("sabun")+"", symd, eymd, userId);
			}
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value="/save/elas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam saveElasWorkDayResult(@RequestBody Map<String, Object> paramMap
														, HttpServletRequest request) {
		
		validateParamMap(paramMap, "flexibleApplId");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long flexibleApplId = Long.valueOf(paramMap.get("flexibleApplId").toString());
		
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", empNo);
			rp = flexibleEmpService.saveElasPlan(flexibleApplId, paramMap, userId);
			
			List<Map<String, Object>> plans = flexEmpMapper.getElasPlanByFlexibleApplId(paramMap);
			List<WtmDayWorkVO> dayWorks = flexibleEmpService.getDayWorks(plans, userId);
			
			rp.put("dayWorks", dayWorks);
			
			//평균 근무 시간 계산
			Map<String, Object> avgHourMap = flexEmpMapper.getElasAvgHour(paramMap);
			if(avgHourMap!=null) {
				rp.put("avgHour", Double.parseDouble(avgHourMap.get("avgHour")+""));
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
		String userId = sessionData.get("userId").toString();
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> flexibleList = null;
		
		try {
			flexibleList = flexibleEmpService.getEmpDayResults(tenantId, enterCd, paramMap.get("sabun").toString(), paramMap.get("ymd").toString(), Long.valueOf(paramMap.get("timeCdMgrId").toString()));
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
		//rp.setSuccess("저장 시 오류가 발생했습니다.");
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
		
		rp.setSuccess("");
		int cnt = 0;
		try {
			
			cnt = flexibleEmpService.saveEmpDayResults(tenantId, enterCd, userId, convertMap);
			//20200221 추가
			if(cnt > 0) {				
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("catch >>"+e);
			//rp.setFail("저장에 실패하였습니다.");
			rp.setFail(e.getMessage());
		}
		
		return rp;
	}
	
	/**
	 * 개인별 근무제도조회 관리자 화면
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/listWeb", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getFlexibleEmpWebList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		rp.setSuccess("");
		
		List<Map<String, Object>> flexibleList = null;
		try {		
			
			flexibleList = flexibleEmpService.getFlexibleEmpWebList(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", flexibleList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	} 
	
	/**
	 * 근무 계획을 작성할 근무제 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/plan/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getFlexibleListForPlan(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> flexibleList = null;
		
		try {
			flexibleList = flexibleEmpService.getFlexibleListForPlan(tenantId, enterCd, sabun, paramMap, userId);
			rp.put("flexibleList", flexibleList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp; 
	}
	
	/**
	 * 유연근무 변경/취소 확인
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/changeChk", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getChangeChk(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		System.out.println("changeChk controller start");
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("userId", userId);
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> retMap = null;
		
		try {
			retMap = flexibleEmpService.GetChangeChk(paramMap);
			rp.put("data", retMap);
			System.out.println("changeChk controller end service");
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		System.out.println("changeChk controller end");
		return rp; 
	}
	
	/**
	 * 유연근무 변경/취소 적용
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/changeFlexible", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setChangeFlexible(@RequestBody Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		System.out.println("setChangeFlexible controller start");
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("userId", userId);
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> retMap = null;
		
		try {
			retMap = flexibleEmpService.setChangeFlexible(paramMap);
			rp.put("data", retMap);
			System.out.println("changeChk controller end service");
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		System.out.println("changeChk controller end");
		return rp; 
	}
	
	/**
	 * 근무제의 주별 데이터 생성
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/workTermTime", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam createWorkTermtimeByEmployee(@RequestBody Map<String, Object> paramMap
														, HttpServletRequest request) {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		try {
			Map<String, Object> workTermTime = flexibleEmpService.createWorkTermtimeByEmployee(tenantId, enterCd, empNo, paramMap, userId);
			rp.put("workTermTime", workTermTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	/**
	 * 해당 일의 잔여 연장근무 시간 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/otMinute",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getOtMinute(@RequestParam Map<String, Object> paramMap
													    			, HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleEmpService.getOtMinute(tenantId, enterCd, sabun, paramMap, userId);
	}
	
	@RequestMapping(value="/retire", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam retireEmp(@RequestBody Map<String, Object> paramMap
														, HttpServletRequest request) {
		
		validateParamMap(paramMap, "flexibleEmpId");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long flexibleEmpId = Long.valueOf(paramMap.get("flexibleEmpId").toString());
		
		try {
			rp = flexibleEmpService.retireEmp(tenantId, enterCd, flexibleEmpId, userId);

			
			//조회성 데이터는 비동기 호출하지 않음
			//term을 만들고 해당 근무제의 약정 근로 시간과 계획 시간을 조회해야 하기 때문에
			if(rp.containsKey("sabun") && rp.containsKey("symd") && rp.containsKey("eymd") && !"".equals(rp.get("symd")) && !"".equals(rp.get("eymd"))) {
				wymAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, rp.get("sabun")+"", rp.get("symd").toString(), rp.get("eymd").toString(), userId, false);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	/**
	 * 일마감
	 * @param paramMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/finishDay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam saveWorkDayFinishResult(@RequestBody Map<String, Object> paramMap
														, HttpServletRequest request) {
		
		validateParamMap(paramMap, "ymd", "sabun");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		
		try {
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("empNo", empNo);
			
			rp = flexibleEmpService.finishDay((Map<String, Object>)paramMap, tenantId, enterCd, empNo, userId);
			
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}

	/*
		해당일 근무정보 조회
	    AS-IS : API Merge
			    1. getFlexibleWorkTimeInfo
			    2. getDayResults
			    3. getFlexibleRangeInfo
			    4. getFlexibleDayInfo
	 */
	@RequestMapping(value="/monthlyDayInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getMonthlyDayInfo(@RequestParam(required=true) String ymd, HttpServletRequest request) {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId")
		                                    .toString());
		String enterCd = sessionData.get("enterCd")
		                            .toString();
		String sabun = sessionData.get("empNo")
		                          .toString();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ymd", ymd);

		try{
			rp.put("workTimeInfo", flexibleEmpService.getFlexibleWorkTimeInfo(tenantId, enterCd, sabun, paramMap));
			rp.put("workDayResult", flexibleEmpService.getWorkDayResult(tenantId, enterCd, sabun, ymd, sabun));
			rp.put("rangeInfo", flexibleEmpService.getFlexibleRangeInfo(tenantId, enterCd, sabun, paramMap));
			rp.put("dayInfo", flexibleEmpService.getFlexibleDayInfo(tenantId, enterCd, sabun, paramMap));

		}catch (Exception e){
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}

		return rp;
	}


	/**
	 * 전체사용자 리셋
	 *
	 * @param ymd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/empResetAsync", method = RequestMethod.GET)
	public @ResponseBody ReturnParam empResetAsync(@RequestParam(required = true) String ymd, HttpServletRequest request) {

		logger.debug("### empResetAsync start");
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();


		try {

			//ymd = WtmUtil.parseDateStr(new Date(), "yyyyMMdd");
			ymd = ymd.replaceAll("-", "");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tenantId", tenantId);
			map.put("enterCd", enterCd);
			map.put("statusCd", "AA");
			map.put("ymd", ymd);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(map));
			//List<WtmEmpHis> empList = empHisMapper.getWtmFlexibleEmp(map);
			List<WtmEmpHis> empList = empHisMapper.getWtmEmpHis(map);
			System.out.println("empList.size() : " + empList.size());
			logger.debug("empList size : " + empList.size());
			
			wymAsyncService.asyncFlexibleEmpRest(tenantId, enterCd, ymd, empList);

		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}

		return rp;
	}

	
	/**
	 * 일마감
	 * @param paramMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkFinDay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> checkFinDay(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception{
		
		validateParamMap(paramMap, "sabun", "flexibleEmpId", "flexibleStdMgrId", "ymd");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		
		Map<String, Object> resultMap = flexibleEmpService.checkFinDay((Map<String, Object>)paramMap, tenantId, enterCd, empNo, userId);
		
		return resultMap;
	}
	

	/**
	 * 초기화
	 *
	 * @param paramMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/resetFinDay",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	ReturnParam resetFinDay(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("초기화 요청완료 되었습니다.");
		validateParamMap(paramMap, "sabun", "flexibleEmpId", "flexibleStdMgrId", "ymd");


		try {
			Long                tenantId    = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String              enterCd     = sessionData.get("enterCd").toString();
			String              empNo       = sessionData.get("empNo").toString();
			String              userId      = sessionData.get("userId").toString();

			String ymd              = paramMap.get("ymd").toString();
			String sabun            = paramMap.get("sabun").toString();
			Long   flexibleStdMgrId = Long.valueOf(paramMap.get("flexibleStdMgrId").toString());


			WtmWorkCalendar   workCalendar   = workCalendarRepo.findByTenantIdAndEnterCdAndYmdAndSabun(tenantId, enterCd, ymd, sabun);
			WtmFlexibleStdMgr flexibleStdMgr = flexibleStdMgrRepo.findByFlexibleStdMgrId(flexibleStdMgrId);

			WtmTimeCdMgr timeCdMgr = timeCdMgrRepo.findById(workCalendar.getTimeCdMgrId()).get();

			flexibleEmpResetService.P_WTM_WORK_DAY_RESULT_RESET(workCalendar, flexibleStdMgr, timeCdMgr, "ADM"); //  TODO : userId 는 추후 삭제예정 ADM 은 임시
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail("초기화 요청시 오류가 발생했습니다.");
		}

		return rp;
	}


	/**
	 * 초기화
	 *
	 * @param paramMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/newEmpReset",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	ReturnParam newEmpReset(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		//  TODO : 신규 입사자 초기화 처리
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("초기화 요청완료 되었습니다.");
		validateParamMap(paramMap, "tenantId", "enterCd", "sabun", "ymd");


		try {
			Long   tenantId = Long.valueOf(paramMap.get("tenantId").toString());
			String enterCd  = paramMap.get("enterCd").toString();
			String ymd      = paramMap.get("ymd").toString();
			String sabun    = paramMap.get("sabun").toString();


			flexibleEmpResetService.P_WTM_FLEXIBLE_EMP_RESET(tenantId, enterCd, sabun, ymd.substring(0, 4) + "0101", ymd.substring(0, 4) + "1231", sabun);
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail("초기화 요청시 오류가 발생했습니다.");
		}

		return rp;
	}

	
}
