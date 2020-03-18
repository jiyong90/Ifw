package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmCalendarService;
import com.isu.ifw.vo.ReturnParam;


@RestController
@RequestMapping(value="/calendar")
public class WtmCalendarController {

	@Autowired
	WtmCalendarService wtmCalendarService;
	
	/**
	 * 달력 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getCalendar(@RequestParam Map<String, Object> paramMap
													   		 , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		String enterCd = null;
		String bisinessPlaceCd = null;
		if(sessionData.get("enterCd")!=null)
			enterCd = sessionData.get("enterCd").toString();
		if(sessionData.get("bisinessPlaceCd")!=null)
			bisinessPlaceCd = sessionData.get("bisinessPlaceCd").toString();
		
		Map<String, Object> resultMap = wtmCalendarService.getCalendar(tenantId, enterCd, bisinessPlaceCd, paramMap);
		
		//ObjectMapper mapper = new ObjectMapper();
		//System.out.println("result : " + mapper.writeValueAsString(resultMap));

		return resultMap;
	}
	
	/**
	 * 근태 달력 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/worktime", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getWorkTimeCalendar(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		
		paramMap.put("tenantId", tenantId);
		
		List<Map<String, Object>> resultMap = wtmCalendarService.getWorkTimeCalendar(paramMap);
		
		//ObjectMapper mapper = new ObjectMapper();
		//System.out.println("result : " + mapper.writeValueAsString(resultMap));

		return resultMap;
	}
	
	/**
	 * 관리자_근태 달력 전체 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEmpWorkCalendar(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		String searchKeyword = paramMap.get("searchKeyword").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", empNo);
		paramMap.put("sYmd", sYmd);
		paramMap.put("eYmd", eYmd);
		paramMap.put("searchKeyword", searchKeyword);
		
		rp.setSuccess("");
	
		List<Map<String, Object>> workteamList = null;
		try {		
			workteamList =  wtmCalendarService.getEmpWorkCalendar(paramMap);
			
			rp.put("DATA", workteamList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}
	
	/**
	 * 조직장_조직원 근태 달력 전체 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/org", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getOrgEmpWorkCalendar(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		
		rp.setSuccess("");
	
		List<Map<String, Object>> workteamList = null;
		try {		
			workteamList =  wtmCalendarService.getOrgEmpWorkCalendar(tenantId, enterCd, empNo, paramMap);
			
			rp.put("DATA", workteamList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}

	/**
	 * 관리자_근태 달력 하루 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dayInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEmpWorkCalendarDayInfo(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();
		String ymd = paramMap.get("ymd").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("ymd", ymd);
		
		rp.setSuccess("");
	
		Map<String, Object> dayInfo = null;
		try {		
			dayInfo =  wtmCalendarService.getEmpWorkCalendarDayInfo(paramMap);
			
			rp.put("DATA", dayInfo);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}
	
	/**
	 * 관리자_근태 달력 하루 조회(근무타각갱신체크용)
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dayInfoEntry", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEmpWorkCalendarDayInfoEntry(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();
		String ymd = paramMap.get("ymd").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("ymd", ymd);
		
		rp.setSuccess("");
	
		Map<String, Object> dayInfo = null;
		try {		
			dayInfo =  wtmCalendarService.getEmpWorkCalendarDayInfoEntry(paramMap);
			
			rp.put("DATA", dayInfo);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}
	
	/**
	 * 해당 일의 휴일 여부 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/holidayYn",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getHolidayYn(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			rp = wtmCalendarService.getHolidayYn(tenantId, enterCd, sabun, paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("휴일 조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}

}
