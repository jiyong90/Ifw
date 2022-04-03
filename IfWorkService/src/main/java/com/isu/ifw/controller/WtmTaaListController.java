package com.isu.ifw.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmTimeCdMgr;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmTimeCdMgrRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpResetService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.service.WtmTaaListService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import java.text.SimpleDateFormat;


@RestController
@RequestMapping(value="/taaApplList")
public class WtmTaaListController {
	
	@Autowired
	WtmTaaListService taaListService;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Autowired
	WtmWorkDayResultRepository wtmWorkDayResultRepo;
	
	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;
	
	@Autowired
	@Qualifier("wtmInterfaceService")
	private WtmInterfaceService wtmInterfaceService;
	
	@Autowired
	private WtmFlexibleStdMgrRepository flexStdMgrRepo;
	
	@Autowired 
	private WtmFlexibleEmpRepository flexEmpRepo;
	
	@Autowired 
	private WtmFlexibleEmpResetService flexibleEmpResetSerevice;
	
	@Autowired
	private WtmTimeCdMgrRepository timeCdMgrRepo;
	
	@Autowired 
	private WtmWorkDayResultRepository workDayResultRepo;
	
	@Autowired 
	private WtmInterfaceService interfaceService;
	
	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	/**
	 * 근태신청내역(관리자) 결재상태 리스트 조회
	 * @param request
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value="/taaList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getTaaApplDetList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		rp.setSuccess("");

		List<Map<String, Object>> workList = null;
		try {		
			workList = taaListService.getTaaApplDetList(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", workList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	/**
	 * 근태신청내역(관리자) 결재상태 변경
	 * @param request
	 * @param paramMap
	 * @return
	 */
	
	@RequestMapping(value="/saveTaaSts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam saveTaaSts(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		String applSabun = "";
		String ifApplNo = null;
		String status = null;
		
		List<Map<String, Object>> updateList = null;
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		logger.debug(" convertMap ydh : " + convertMap);
		SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
		Date today = format1.parse(format1.format(new Date()));

		try {
			
			rp = taaListService.saveWtmTaaSts(tenantId, enterCd, sabun, userId, convertMap);
			
			if(rp!=null && rp.getStatus()!=null && "FAIL".equals(rp.getStatus())) {
				return rp;
			} else {
				
				if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
					List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
					if(iList != null && iList.size() > 0) {
						for(Map<String, Object> map : iList) {
							
							map.put("tenantId", tenantId);
							map.put("enterCd", enterCd);
							map.put("sabun", map.get("updtSabun").toString());
							map.put("paramSdate", map.get("taaSdate").toString());
							map.put("paramEdate", map.get("taaEdate").toString());
							map.put("symd", map.get("taaSdate").toString());
							map.put("eymd", map.get("taaEdate").toString());
							map.put("userId","ADMIN");
							
							List<String> timeTypeCds = new ArrayList<>();
							timeTypeCds.add(WtmApplService.TIME_TYPE_TAA);
							timeTypeCds.add(WtmApplService.TIME_TYPE_BASE);
							timeTypeCds.add(WtmApplService.TIME_TYPE_REGA);
							
							List<WtmWorkDayResult> taaResults = workDayResultRepo.findByTenantIdAndEnterCdAndSabunAndTimeTypeCdInAndYmdBetweenOrderByPlanSdateAsc(tenantId, enterCd, map.get("sabun").toString(), timeTypeCds, map.get("taaSdate").toString(), map.get("taaEdate").toString());
							workDayResultRepo.deleteAll(taaResults);
							
							List<WtmWorkCalendar> works = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmdBetweenOrderByYmdAsc(tenantId, enterCd, map.get("sabun").toString(), map.get("taaSdate").toString(), map.get("taaEdate").toString());
							for(WtmWorkCalendar calendar : works) {
								try {
									WtmTimeCdMgr timeCdMgr = timeCdMgrRepo.findById(calendar.getTimeCdMgrId()).get();
									WtmFlexibleEmp emp = flexEmpRepo.findByTenantIdAndEnterCdAndSabunAndYmdBetween(calendar.getTenantId(), calendar.getEnterCd(), calendar.getSabun(), calendar.getYmd());
									WtmFlexibleStdMgr flexStdMgr = flexStdMgrRepo.findById(emp.getFlexibleStdMgrId()).get();

									flexibleEmpResetSerevice.P_WTM_WORK_DAY_RESULT_RESET(calendar, flexStdMgr, timeCdMgr, "ADMIN");
									interfaceService.resetTaaResult(tenantId, enterCd, map.get("sabun").toString(), calendar.getYmd());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							// 오늘 이전인 경우만 일마감 돌려주기
							if( ymd.parse(map.get("taaSdate").toString()).compareTo(today) < 0 ) {
								flexibleEmpService.finishDay((Map<String, Object>)map, tenantId, enterCd, map.get("sabun").toString(), map.get("sabun").toString());
							}
						}
					}
				}
				rp.setSuccess("저장이 성공하였습니다.");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		return rp;
	}

}
