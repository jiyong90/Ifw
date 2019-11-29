package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmPropertie;
import com.isu.ifw.repository.WtmCodeRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.service.LoginService;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.util.WtmUtil;
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
	
	@Autowired
	LoginService loginService;
	
	@Resource
	WtmEmpHisRepository empHisRepo;
	
	@Resource
	WtmPropertieRepository propertieRepo;

	/**
	 * POST 방식은 로그인 실패시 포워드를 위한 엔드포인트 
	 * @param tsId
	 * @return
	 */
	@RequestMapping(value="/login/{tsId}", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewLogin(@PathVariable String tsId, HttpServletRequest request) throws Exception {
		Long tenantId = tenantDao.findTenantId(tsId);
		if(tenantId == null) {
			ModelAndView mv = new ModelAndView("info");
			mv.addObject("status", "100");
			return mv;
		}
		
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
        
        mv.addObject("loginBackgroundImg", tcms.getConfigValue(tenantId, "WTMS.LOGIN.BACKGROUND_IMG", true, ""));
        mv.addObject("loginLogoImg", tcms.getConfigValue(tenantId, "WTMS.LOGIN.LOGO_IMG", true, ""));
        mv.addObject("mainTitle", tcms.getConfigValue(tenantId, "WTMS.MAIN.TITLE", true, ""));
        mv.addObject("copyright", tcms.getConfigValue(tenantId, "WTMS.MAIN.COPYRIGHT", true, ""));
        
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
		//return views(tsId, "main", request);
		return views(tsId, "workCalendar", request);
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
		String userId = sessionData.get("userId").toString();
		String authCd = (sessionData.containsKey("authCd")?sessionData.get("authCd").toString():"U");

		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", loginId);
		mv.addObject("pageName", viewPage);
		mv.addObject("mainLogoImg", tcms.getConfigValue(tenantId, "WTMS.MAIN.LOGO_IMG", true, ""));
		mv.addObject("mainTitle", tcms.getConfigValue(tenantId, "WTMS.MAIN.TITLE", true, ""));
		mv.addObject("isEmbedded",false);
		mv.addObject("type","console");
		mv.addObject("authCd", authCd);
		mv.addObject("applType", request.getParameter("applType")!=null?request.getParameter("applType"):"01");
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		
		String ymd = WtmUtil.parseDateStr(new Date(), "yyyyMMdd");
		WtmEmpHis empHis = empHisRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, empNo, ymd);
		if(empHis!=null) {
			mv.addObject("leaderYn", empHis.getLeaderYn());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		if("workCalendar".equals(viewPage)){
			String workday = null;
			if(request.getParameter("date")!=null && !"".equals(request.getParameter("date"))) {
				workday = request.getParameter("date");
				mv.addObject("workday", workday); 
			}
			
			String calendarType = "Month"; //기본은 월달력
			
			WtmPropertie flexApplYn = propertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_FLEXIBLE_APPL_YN");
			
			if(flexApplYn!=null) {
				mv.addObject("flexApplYn", flexApplYn.getInfoValue());
			}
			
			if(request.getParameter("calendarType")!=null) {
				calendarType = request.getParameter("calendarType").toString();
				
				if("Time".equals(calendarType)) {
					//근태사유서 신청 기간
					WtmPropertie propertie = propertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_INOUT_CHG_LIMIT");
					if(propertie!=null) {
						mv.addObject("inoutChgLimit", propertie.getInfoValue());
					}
					
					//연장근무 또는 휴일근무 신청 시 사유
					List<WtmCode> reasons = codeRepo.findByTenantIdAndEnterCdAndYmdAndGrpCodeCd(tenantId, enterCd, ymd, "REASON_CD");
					mv.addObject("reasons", mapper.writeValueAsString(reasons));
				} else if("Day".equals(calendarType)) {
					//근무 계획을 작성해야 하는 근무제
					if(request.getParameter("flexibleEmpId")!=null && !"".equals(request.getParameter("flexibleEmpId"))) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("flexibleEmpId", Long.valueOf(request.getParameter("flexibleEmpId")));
						Map<String, Object> flexibleEmp = flexibleEmpService.getFlexibleEmpForPlan(tenantId, enterCd, empNo, paramMap, userId);
						if(flexibleEmp!=null) {
							try {
								//System.out.println("flexibleEmp::::::"+mapper.writeValueAsString(flexibleEmp));
								mv.addObject("flexibleEmp", mapper.writeValueAsString(flexibleEmp));
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								mv.addObject("flexibleEmp", null);
							}
						}
						
					}
					
					//탄근제 근무제패턴 조회
					//탄근제의 경우 신청 시 바로 근무계획을 작성
					if(request.getParameter("flexibleApplId")!=null && !"".equals(request.getParameter("flexibleApplId"))) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("flexibleApplId", Long.valueOf(request.getParameter("flexibleApplId")));
						Map<String, Object> flexibleEmp = flexibleEmpService.getFlexibleApplDetForPlan(tenantId, enterCd, empNo, paramMap, userId);
						if(flexibleEmp!=null) {
							try {
								mv.addObject("flexibleEmp", mapper.writeValueAsString(flexibleEmp));
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								mv.addObject("flexibleEmp", null);
							}
						}
						
					}
				}
			} 
			
			mv.addObject("calendar", "work"+ calendarType +"Calendar");
			
			return workCalendarPage(mv, tenantId, enterCd, empNo, userId, request);
		} else {
			return mv;
		}
		
	}
	
	@RequestMapping(value = "/info/{tsId}/{status}", method = RequestMethod.GET)
	public ModelAndView viewInfo(@PathVariable String tsId, @PathVariable String status, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("info");
		mv.addObject("status", status);
		mv.addObject("tsId", tsId);
		return mv;
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
		String userId = sessionData.get("userId").toString();
		String authCd = (sessionData.containsKey("authCd")?sessionData.get("authCd").toString():"U");

		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", loginId);
		mv.addObject("pageName", "mgr/"+viewPage);
		mv.addObject("mainLogoImg", tcms.getConfigValue(tenantId, "WTMS.MAIN.LOGO_IMG", true, ""));
		mv.addObject("mainTitle", tcms.getConfigValue(tenantId, "WTMS.MAIN.TITLE", true, ""));
		
		mv.addObject("type","console");
		mv.addObject("authCd", authCd);
		
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date.getTime());
		mv.addObject("today", today);
		
		WtmEmpHis empHis = empHisRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, empNo, WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
		if(empHis!=null) {
			mv.addObject("leaderYn", empHis.getLeaderYn());
		}
		
		mv.addObject("isEmbedded",false); // 단독으로 열린것임을 표시
		
		return mv;
	}
	
	protected ModelAndView workCalendarPage(ModelAndView mv, Long tenantId, String enterCd, String empNo, String userId, HttpServletRequest request) {
		Map<String, Object> flexibleAppl = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {		
			//근무제 마지막 신청정보
			flexibleAppl = flexibleApplService.getLastAppl(tenantId, enterCd, empNo, new HashMap<String, Object>(), userId);
			/*WtmFlexibleStdMgr flexibleStdMgr = null;
			
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
			}*/
			
			if(flexibleAppl!=null)
				mv.addObject("flexibleAppl", mapper.writeValueAsString(flexibleAppl));
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Calendar date = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			paramMap.put("ymd", sdf.format(date.getTime()));
			
			Map<String, Object> flexibleStdMgr = flexibleEmpService.getFlexibleEmp(tenantId, enterCd, empNo, paramMap, userId);
			
			mv.addObject("flexibleStdMgr", mapper.writeValueAsString(flexibleStdMgr));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}

	protected ModelAndView showView(String tsId, String viewPage, HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView("hrtemplate");
		
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		String authCd = (sessionData.containsKey("authCd")?sessionData.get("authCd").toString():"U");

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

		mv.addObject("tsId", tsId);
		mv.addObject("enterCd", enterCd);
		mv.addObject("empNo", empNo);
		mv.addObject("loginId", userId);
		mv.addObject("pageName", viewPage);
		mv.addObject("type","hr");
		mv.addObject("redirectUrl", loginService.getHrInfoUrl(tenantId));
		mv.addObject("authCd", authCd);
		mv.addObject("applType", request.getParameter("applType")!=null?request.getParameter("applType"):"01");

//		mv.addObject("tenant", tenantId);

		if("workCalendar".equals(viewPage)){
			ObjectMapper mapper = new ObjectMapper();
			String workday = null;
			if(request.getParameter("date")!=null && !"".equals(request.getParameter("date"))) {
				workday = request.getParameter("date");
				mv.addObject("workday", workday); 
			}
			String calendarType = "Month";
			if(request.getParameter("calendarType")!=null) {
				calendarType = request.getParameter("calendarType").toString();
			} 
			mv.addObject("calendar", "work"+ calendarType +"Calendar");
			
			WtmPropertie flexApplYn = propertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_FLEXIBLE_APPL_YN");
			if(flexApplYn!=null) {
				mv.addObject("flexApplYn", flexApplYn.getInfoValue());
			}
			
			if("Time".equals(calendarType)) {
				//근태사유서 신청 기간
				WtmPropertie propertie = propertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_INOUT_CHG_LIMIT");
				if(propertie!=null) {
					mv.addObject("inoutChgLimit", propertie.getInfoValue());
				}
				
				//연장근무 또는 휴일근무 신청 시 사유
				List<WtmCode> reasons = codeRepo.findByTenantIdAndEnterCdAndYmdAndGrpCodeCd(tenantId, enterCd, WtmUtil.parseDateStr(new Date(), "yyyyMMdd"), "REASON_CD");
				try {
					mv.addObject("reasons", mapper.writeValueAsString(reasons));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mv.addObject("reasons", null);
				}
			} else if("Day".equals(calendarType)) {
				//근무 계획을 작성해야 하는 근무제
				if(request.getParameter("flexibleEmpId")!=null && !"".equals(request.getParameter("flexibleEmpId"))) {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("flexibleEmpId", Long.valueOf(request.getParameter("flexibleEmpId")));
					Map<String, Object> flexibleEmp = flexibleEmpService.getFlexibleEmpForPlan(tenantId, enterCd, empNo, paramMap, userId);
					if(flexibleEmp!=null) {
						try {
							mv.addObject("flexibleEmp", mapper.writeValueAsString(flexibleEmp));
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							mv.addObject("flexibleEmp", null);
						}
					}
					
				}
					
			}
			mv.addObject("pageName", viewPage);
			return workCalendarPage(mv, tenantId, enterCd, empNo, userId, request);
		}
		else if(viewPage.equals("workDayCalendar")
				|| viewPage.equals("workMonthCalendar") || viewPage.equals("workTimeCalendar") || viewPage.equals("approvalList")) {
		
			mv.addObject("pageName", viewPage);
		}
		else
			mv.addObject("pageName", "mgr/"+viewPage);
		
		System.out.println("isEmbedded > true");
		
		mv.addObject("isEmbedded",true); // HR 에서 포함되어 열린것임을 표시 (단독으로 사용되지 않는 경우를 화면에서 식별하기 위해 이 속성을 사용함) 
		
		return mv;
		
		
	}
	
	@RequestMapping(value = "/hr/{tsId}/views/{viewPage}", method = RequestMethod.POST)
	public ModelAndView hrViews(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
	
		return showView(tsId, viewPage, request);
	}


	@RequestMapping(value = "/hr/{tsId}/views/{viewPage}", method = RequestMethod.GET)
	public ModelAndView infoViews(@PathVariable String tsId, @PathVariable String viewPage, HttpServletRequest request) throws Exception {
	
		return showView(tsId, viewPage, request);
	}
}
