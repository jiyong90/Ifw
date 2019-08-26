package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.service.WtmFlexibleStdService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmFlexibleStdVO;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleStd")
public class WtmFlexibleStdController {
	
	@Autowired
	private WtmFlexibleStdService WtmFlexibleStdService;
	
	@Autowired
	private WtmFlexibleStdMgrRepository flexibleStdMgrRepo;

	@RequestMapping(value="/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam flexibleStdList(HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper mapper = new ObjectMapper();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		String enterCd = null;
		String bisinessPlaceCd = null;
		if(sessionData.get("enterCd")!=null)
			enterCd = sessionData.get("enterCd").toString();
		if(sessionData.get("bisinessPlaceCd")!=null)
			bisinessPlaceCd = sessionData.get("bisinessPlaceCd").toString();
		
		/*List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<>();
		m.put("flexibleStdMgrId", "1");
		m.put("flexibleNm", "이수연구직");
		m.put("workTypeCd", "10");
		m.put("workTypeNm", "탄근제");
		//Map<String, Object> core = new HashMap<>();
		//core.put("sTime", "1000");
		//core.put("eTime", "1500");
		m.put("coreTime", null);
		Map<String, Object> limit = new HashMap<>();
		limit.put("sTime", "0800");
		limit.put("eTime", "2200");
		m.put("limitTime", limit);
		List<Map<String, Object>> workDateRangeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> workDateRange = new HashMap<>();
		workDateRange.put("lable", "2주");
		workDateRange.put("value", "1_week");
		workDateRangeList.add(workDateRange);
		workDateRange = new HashMap<>();
		workDateRange.put("lable", "1개월");
		workDateRange.put("value", "1_month");
		workDateRangeList.add(workDateRange);
		workDateRange = new HashMap<>();
		workDateRange.put("lable", "2개월");
		workDateRange.put("value", "2_month");
		workDateRangeList.add(workDateRange);
		workDateRange = new HashMap<>();
		workDateRange.put("lable", "3개월");
		workDateRange.put("value", "3_month");
		workDateRangeList.add(workDateRange);
		m.put("workDateRange", workDateRangeList);
		l.add(m);
		
		Map<String, Object> m1 = new HashMap<>();
		m1.put("flexibleStdMgrId", "2");
		m1.put("flexibleNm", "이수선근제기본");
		m1.put("workTypeCd", "20");
		m1.put("workTypeNm", "선근제[부분]");
		Map<String, Object> core1 = new HashMap<>();
		core1.put("sTime", "1000");
		core1.put("eTime", "1500");
		m1.put("coreTime", core1);
		Map<String, Object> limit1 = new HashMap<>();
		limit1.put("sTime", "0800");
		limit1.put("eTime", "2200");
		m1.put("limitTime", limit1);
		Map<String, Object> workingDays1 = new HashMap<>();
		workingDays1.put("월", "true");
		workingDays1.put("화", "true");
		workingDays1.put("수", "true");
		workingDays1.put("목", "true");
		workingDays1.put("금", "true");
		workingDays1.put("토", "false");
		workingDays1.put("일", "false");
		m1.put("workingDays", workingDays1);
		List<Map<String, Object>> workDateRangeList1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> workDateRange1 = new HashMap<>();
		workDateRange1.put("lable", "1주");
		workDateRange1.put("value", "1_week");
		workDateRangeList1.add(workDateRange1);
		workDateRange1 = new HashMap<>();
		workDateRange1.put("lable", "2주");
		workDateRange1.put("value", "1_week");
		workDateRangeList1.add(workDateRange1);
		workDateRange1 = new HashMap<>();
		workDateRange1.put("lable", "1개월");
		workDateRange1.put("value", "1_month");
		workDateRangeList1.add(workDateRange1);
		m1.put("workDateRange", workDateRangeList1);
		l.add(m1);*/
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<WtmFlexibleStdVO> wtmFlexibleStd = null;
		
		try {
			wtmFlexibleStd = WtmFlexibleStdService.getFlexibleStd(tenantId, enterCd, userId);
			rp.put("wtmFlexibleStd", wtmFlexibleStd);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody WtmFlexibleStdMgr flexibleStd(@RequestParam Long flexibleStdMgrId
			                                   , HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper mapper = new ObjectMapper();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		return flexibleStdMgrRepo.findById(flexibleStdMgrId).get();
	}
	/*@PostMapping(value = "/flexibleStd") 
	public ReturnParam flexibleStd(@RequestBody WtmFlexibleStdVO flexibleStdVO
								, HttpServletRequest request) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(flexibleStdVO));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReturnParam rp = new ReturnParam();
		return rp;
			
	}*/
	
	@RequestMapping(value="/all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam stdList(HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper mapper = new ObjectMapper();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		String enterCd = sessionData.get("enterCd").toString();
		String bisinessPlaceCd = null;
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> wtmFlexibleStd = null;
		
		try {
			wtmFlexibleStd = WtmFlexibleStdService.getFlexibleStd(tenantId, enterCd);
			rp.put("DATA", wtmFlexibleStd);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/worktype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam stdWorkTypeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper mapper = new ObjectMapper();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		String enterCd = sessionData.get("enterCd").toString();
		String workTypeCd = paramMap.get("workTypeCd").toString();
		String bisinessPlaceCd = null;
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<Map<String, Object>> wtmFlexibleStd = null;
		
		try {
			wtmFlexibleStd = WtmFlexibleStdService.getFlexibleStdWorkType(tenantId, enterCd, workTypeCd);
			rp.put("DATA", wtmFlexibleStd);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	
	@RequestMapping(value="/listWeb", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam stdListWeb(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String ymd = paramMap.get("sYmd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> stdList = null;
		try {		
			stdList = WtmFlexibleStdService.getStdListWeb(tenantId, enterCd, ymd);
			
			rp.put("DATA", stdList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/saveWeb", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setStdListWeb(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {
			cnt = WtmFlexibleStdService.setStdListWeb(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/listPatt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkPattList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long flexibleStdMgrId = Long.valueOf(paramMap.get("flexibleStdMgrId").toString());
		
		rp.setSuccess("");
		
		List<Map<String, Object>> stdList = null;
		try {		
			stdList = WtmFlexibleStdService.getWorkPattList(flexibleStdMgrId);
			
			rp.put("DATA", stdList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	
	@RequestMapping(value="/savePatt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setWorkPattList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);

		
		rp.setSuccess("");
		int cnt = 0;
		try {
			cnt = WtmFlexibleStdService.setWorkPattList(userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	
}
