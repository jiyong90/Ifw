package com.isu.ifw.controller;

import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmCompApplListService;
import com.isu.ifw.service.WtmValidatorService;
import com.isu.ifw.vo.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/compAppl")
public class WtmCompApplController {
	
	@Autowired
	WtmCompApplListService wtmCompApplListService;
	
	@Autowired
	@Qualifier("wtmCompApplService")
	WtmApplService wtmCompApplService;
	
	@Autowired
	WtmValidatorService wtmValidatorService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	/**
	 * 보상 휴가 신청 내역 조회
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCompList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		String sabun = sessionData.get("empNo").toString();
		
		if(paramMap.get("ymd")!=null) {
			paramMap.put("ymd", paramMap.get("ymd").toString().replaceAll("[-.]", ""));
		}
		
		rp.setSuccess("");
		
		List<Map<String, Object>> compList = null;
		try {		
			compList = wtmCompApplListService.getApprList(tenantId, enterCd, null, paramMap, userId, sabun);
			rp.put("DATA", compList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	
	/**
	 * 보상 휴가 신청
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam requestCompAppl(@RequestBody Map<String, Object> paramMap , HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		String sabun = sessionData.get("empNo").toString();
		
		Long applId = null;
		if(paramMap.get("applId")!=null && !"".equals(paramMap.get("applId"))) {
			applId = Long.valueOf(paramMap.get("applId").toString());
		}
		
		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd"))) {
			workTypeCd = paramMap.get("workTypeCd").toString();
		}
		
		// 유효성 체크 시작
		List<Map<String, Object>> worksDet = new ArrayList<>();
		Map<String, Object> workMap = new HashMap<String, Object>();
		workMap.put("workTimeCode", paramMap.get("taaCd"));
		workMap.put("startYmd", paramMap.get("sDate"));
		workMap.put("startHm", "");
		workMap.put("endYmd", paramMap.get("eDate"));
		workMap.put("endHm", "");
		worksDet.add(workMap);
		
		List<Map<String, Object>> works = new ArrayList<>();
		Map<String, Object> worksValidMap = new HashMap<String, Object>();
		worksValidMap.put("sabun", sabun);
		worksValidMap.put("worksDet", worksDet);
		works.add(worksValidMap);
		// 유효성 체크 종료
		
		try {
			rp = wtmValidatorService.worktimeValid(tenantId, enterCd, "", works, sabun);

			if(rp!=null && rp.getStatus()!=null && "FAIL".equals(rp.getStatus())) {
				return rp;
			}
			wtmCompApplService.request(tenantId, enterCd, applId, workTypeCd, paramMap, sabun, userId);
			
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		return rp;
	}
	
	
	
	/**
	 * 보상 휴가 신청 가능 시간 조회(현재 날짜 기준으로 신청 가능 시간을 조회 한다.)
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getPossibleUseTime", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getPossibleUseTime(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		String sabun = sessionData.get("empNo").toString();

		rp.setSuccess("");
		
		Map<String, Object> compList = null;
		try {		
			compList = wtmCompApplListService.getPossibleUseTime(tenantId, enterCd, null, paramMap, userId, sabun);
			if(compList.size() > 0) {
				rp.put("comptime", compList);
			} else {
				rp.setFail("보상휴가 신청 가능시간이 없습니다.");
			}
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	
	/**
	 * 보상 휴가 신청 가능 시간 조회(현재 날짜 기준으로 신청 가능 시간을 조회 한다.)
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getWorkDay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkDay(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		String sabun = sessionData.get("empNo").toString();

		rp.setSuccess("");
		
		Map<String, Object> workDayMap = null;
		try {		
			workDayMap = wtmCompApplListService.getWorkDay(tenantId, enterCd, null, paramMap, userId, sabun);
			rp.put("workDayMap", workDayMap);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
}
