package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.auth.config.AuthConfigProvider;
import com.isu.auth.config.data.AuthConfig;
import com.isu.auth.dao.TenantDao;
import com.isu.ifw.StringUtil;
import com.isu.ifw.entity.WtmCode;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.repository.WtmCodeRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.option.service.TenantConfigManagerService;

@RestController
//@RequestMapping(value="/resource")
public class ViewController {
	
	private StringUtil stringUtil;
	
	@Autowired
	private TenantConfigManagerService tcms;
	
	@Autowired
	AuthConfigProvider authConfigProvider;
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService flexibleApplService;
	
	@Autowired
	@Qualifier("flexibleEmpService")
	WtmFlexibleEmpService flexibleEmpService;
	
	@Resource
	TenantDao tenantDao;
	
	@Resource
	WtmFlexibleStdMgrRepository flexibleStdMgrRepo;
	
	@Resource
	WtmCodeRepository codeRepo;
	
	@Resource
	WtmFlexibleEmpRepository flexibleEmpRepo;
	
	/**
	 * POST 방식은 로그인 실패시 포워드를 위한 엔드포인트 
	 * @param tsId
	 * @return
	 */
	@RequestMapping(value="/login/{tsId}", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewLogin(@PathVariable String tsId, HttpServletRequest request) throws Exception {
		Long tenantId = tenantDao.findTenantId(tsId);
		System.out.println("call for forward /login/"+tsId);
		ModelAndView mv = new ModelAndView("login");
		
		// 권한 설정 값을 받는다.
        AuthConfig authConfig = authConfigProvider.initConfig(tenantId, tsId);
        
		mv.addObject("AUTH_CONFIG", authConfig);
		
		String company = tcms.getConfigValue(tenantId, "WTMS.LOGIN.COMPANY_LIST", true, "");
        List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
        
        ObjectMapper mapper = new ObjectMapper();
        if(company != null && !"".equals(company)) 
        	companyList = mapper.readValue(company, new ArrayList<Map<String, Object>>().getClass());
        mv.addObject("companyList", companyList);
        
		return mv;
	}
	
	/*@GetMapping(value="/login/{tsId}")
	public ModelAndView login(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		Long tenantId = tenantDao.findTenantId(tsId);
		System.out.println("call /login/"+tsId);
		ModelAndView mv = new ModelAndView("login");
		
		// 권한 설정 값을 받는다.
        AuthConfig authConfig = authConfigProvider.initConfig(tenantId, tsId);
        
		mv.addObject("AUTH_CONFIG", authConfig);
		return mv;
	}*/
	
	/*@RequestMapping(value = "/{viewPage}", method = RequestMethod.GET)
	public ModelAndView views(@PathVariable String viewPage) throws Exception {
		ModelAndView mv = new ModelAndView(viewPage);
		mv.addObject("tsId", "isu");
		return mv;
		 
	}*/
	
	@GetMapping(value = "/console/{tsId}")
	public ModelAndView login(@PathVariable String tsId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//return main(tsId, request);
		return views(tsId, "main", request);
	}
	
	@GetMapping(value="/console/{tsId}/main")
	public ModelAndView main(@PathVariable String tsId, HttpServletRequest request) throws JsonProcessingException {
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("tsId", tsId);
		return mv;
	}
	
	@RequestMapping(value = "/console/{tsId}/{viewPage}", method = RequestMethod.GET)
	public ModelAndView viewPage(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView(viewPage);
		mv.addObject("tsId", tsId);
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		return mv;
	}
	
	@RequestMapping(value = "/console/{tsId}/views/{viewPage}", method = RequestMethod.GET)
	public ModelAndView views(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("template");
		
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String loginId = sessionData.get("loginId").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", loginId);
		mv.addObject("pageName", viewPage);
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		ObjectMapper mapper = new ObjectMapper();
		if("workCalendar".equals(viewPage)){
			if(request.getParameter("date")!=null && !"".equals(request.getParameter("date"))) {
				String workday = request.getParameter("date");
				mv.addObject("workday", workday); 
			}
			
			if(request.getParameter("calendarType")!=null) {
				String calendarType = request.getParameter("calendarType").toString();
				mv.addObject("calendar", "work"+ calendarType +"Calendar");
				
				if("Time".equals(calendarType)) {
					//연장근무 또는 휴일근무 신청 시 사유
					List<WtmCode> reasons = codeRepo.findByTenantIdAndEnterCd(tenantId, enterCd, "REASON_CD");
					mv.addObject("reasons", mapper.writeValueAsString(reasons));
				}
			}
			
			return workCalendarPage(mv, tenantId, enterCd, empNo, userId, request);
		} else {
			return mv;
		}
		
	}
	
	//관리자페이지는 주소 분리
	@RequestMapping(value = "/console/{tsId}/views/mgr/{viewPage}", method = RequestMethod.GET)
	public ModelAndView mgrviews(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("template");
		
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String loginId = sessionData.get("loginId").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", loginId);
		mv.addObject("pageName", "mgr/"+viewPage);
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		return mv;
	}
	
	protected ModelAndView workCalendarPage(ModelAndView mv, Long tenantId, String enterCd, String empNo, Long userId, HttpServletRequest request) {
		Map<String, Object> flexibleAppl = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {		
			WtmFlexibleStdMgr flexibleStdMgr = null;
			//근무제 마지막 신청정보
			flexibleAppl = flexibleApplService.getLastAppl(tenantId, enterCd, empNo, new HashMap<String, Object>(), userId);
			
			if(flexibleAppl!=null) {
				mv.addObject("flexibleAppl", mapper.writeValueAsString(flexibleAppl));
				
				if(flexibleAppl.get("flexibleStdMgrId")!=null && !"".equals(flexibleAppl.get("flexibleStdMgrId"))) {
					Long flexibleStdMgrId = Long.valueOf(flexibleAppl.get("flexibleStdMgrId").toString());
					flexibleStdMgr = flexibleStdMgrRepo.findById(flexibleStdMgrId).get();
				}
			} else {
				Calendar date = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				
				//기본근무
				flexibleStdMgr = flexibleStdMgrRepo.findByTenantIdAndEnterCdAndSabunAndYmdBetween(tenantId, enterCd, empNo, sdf.format(date.getTime()));
			}
			mv.addObject("flexibleStdMgr", mapper.writeValueAsString(flexibleStdMgr));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}

	@RequestMapping(value = "/hr/{tsId}/views/{viewPage}", method = RequestMethod.POST)
	public ModelAndView hrViews(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("hrtemplate");
		
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);

		
		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", userId);
		mv.addObject("pageName", viewPage);
		
		if("workCalendar".equals(viewPage)){
			ObjectMapper mapper = new ObjectMapper();
			if(request.getParameter("date")!=null && !"".equals(request.getParameter("date"))) {
				String workday = request.getParameter("date");
				mv.addObject("workday", workday); 
			}
			String calendarType = "Month";
			if(request.getParameter("calendarType")!=null) {
				calendarType = request.getParameter("calendarType").toString();
			} 
			mv.addObject("calendar", "work"+ calendarType +"Calendar");
			
			if("Time".equals(calendarType)) {
				//연장근무 또는 휴일근무 신청 시 사유
				List<WtmCode> reasons = codeRepo.findByTenantIdAndEnterCd(tenantId, enterCd, "REASON_CD");
				mv.addObject("reasons", mapper.writeValueAsString(reasons));
			}
			mv.addObject("pageName", viewPage);
			return workCalendarPage(mv, tenantId, enterCd, empNo, userId, request);
		}
		else if(viewPage.equals("workDayCalendar")
				|| viewPage.equals("workMonthCalendar") || viewPage.equals("workTimeCalendar")) {
		
			mv.addObject("pageName", viewPage);
		}
		else
			mv.addObject("pageName", "mgr/"+viewPage);
		
		return mv;
	}


	@RequestMapping(value = "/hr/{tsId}/views/{viewPage}", method = RequestMethod.GET)
	public ModelAndView infoViews(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("hrtemplate");
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);

		
		if(viewPage.equals("info")) {
			mv.addObject("tsId", tsId);
			mv.addObject("pageName", viewPage);
			mv.addObject("pageName", viewPage);
			return mv;
		}
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		

		
		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", userId);
		mv.addObject("pageName", viewPage);
		
		if("workCalendar".equals(viewPage)){
			ObjectMapper mapper = new ObjectMapper();
			if(request.getParameter("date")!=null && !"".equals(request.getParameter("date"))) {
				String workday = request.getParameter("date");
				mv.addObject("workday", workday); 
			}
			String calendarType = "Month";
			if(request.getParameter("calendarType")!=null) {
				calendarType = request.getParameter("calendarType").toString();
			} 
			mv.addObject("calendar", "work"+ calendarType +"Calendar");
			
			if("Time".equals(calendarType)) {
				//연장근무 또는 휴일근무 신청 시 사유
				List<WtmCode> reasons = codeRepo.findByTenantIdAndEnterCd(tenantId, enterCd, "REASON_CD");
				mv.addObject("reasons", mapper.writeValueAsString(reasons));
			}
			mv.addObject("pageName", viewPage);
			return workCalendarPage(mv, tenantId, enterCd, empNo, userId, request);
		}
		else if(viewPage.equals("workDayCalendar")
				|| viewPage.equals("workMonthCalendar") || viewPage.equals("workTimeCalendar")) {
		
			mv.addObject("pageName", viewPage);
		}
		else
			mv.addObject("pageName", "mgr/"+viewPage);
		
		return mv;
	}
}
