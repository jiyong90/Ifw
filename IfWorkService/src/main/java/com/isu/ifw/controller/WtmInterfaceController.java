package com.isu.ifw.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.service.WtmIuerpInterfaceService;
import com.isu.ifw.util.WtmUtil;

@RestController
@RequestMapping(value="/interface")
public class WtmInterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwDbLog");
	
	@Autowired
	@Qualifier("wtmInterfaceService")
	private WtmInterfaceService wtmInterfaceService;
	
	@Autowired
	@Qualifier("wtmIuerpInterfaceService")
	private WtmIuerpInterfaceService wtmIuerpInterfaceService;
	
	
//	@Autowired
//	AuthConfigProvider authConfigProvider;
		
	@Autowired
	WtmInoutService inoutService;
	
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@RequestMapping(value = "/{tsId}/iuerp/{type}",method = RequestMethod.POST)
	public void applyIntf(@PathVariable String tsId, @PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
        
        logger.debug("["+tsId+"] iuERP INTERFACE START >>>>>");
        
        try {
	        wtmIuerpInterfaceService.applyIntf(tenantId, type);
        } catch(Exception e) {
        	logger.debug("iuERP INTERFACE ERROR");
        	e.printStackTrace();
        }
        
        logger.debug("iuERP INTERFACE END >>>>>");
		
	}
	
	@RequestMapping(value = "/ifAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void getIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 인터페이스 시작
		try {
			System.out.println("getIf start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString()); 
			//Long tenantId = (long) 38;
			System.out.println("getCodeIfResult call");
			wtmInterfaceService.getCodeIfResult(tenantId);
						
			System.out.println("getHolidayIfResult call");
			wtmInterfaceService.getHolidayIfResult(tenantId);	// 공휴일
			
			System.out.println("getTaaCodeIfResult call");
			wtmInterfaceService.getTaaCodeIfResult(tenantId);	// 근태코드
			
			System.out.println("getOrgCodeIfResult call");
			wtmInterfaceService.getOrgCodeIfResult(tenantId); // 조직코드
			
			System.out.println("getOrgChartIfResult call");
			wtmInterfaceService.getOrgChartIfResult(tenantId); // 조직도
			
			System.out.println("getEmpHisIfResult call");
			wtmInterfaceService.getEmpHisIfResult(tenantId); //사원이력
			
			System.out.println("getOrgConcIfResult call");
			wtmInterfaceService.getOrgConcIfResult(tenantId); //조직장 변경정보 저장 Service 호출
			
			System.out.println("getEmpAddrIfResult call");
			wtmInterfaceService.getEmpAddrIfResult(tenantId); //사원 주소
			
			System.out.println("getIf end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	
	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void getcodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		try {
			System.out.println("getcodeIf start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString()); 
			//Long tenantId = (long) 38;
			wtmInterfaceService.getCodeIfResult(tenantId);
			System.out.println("getcodeIf end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/holiday",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void holidayIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공휴일정보
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.getHolidayIfResult(tenantId); //Service 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/taaCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void taaCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 근태코드정보
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.getTaaCodeIfResult(tenantId); //Service 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/orgCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void orgCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 조직코드정보
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.getOrgCodeIfResult(tenantId); //Service 호출
			wtmInterfaceService.getOrgChartIfResult(tenantId); //Service 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/empHis",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.getEmpHisIfResult(tenantId); //사원 변경정보 저장 Service 호출
			wtmInterfaceService.getOrgConcIfResult(tenantId); //조직장 변경정보 저장 Service 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	
	@RequestMapping(value = "/empHisEtc",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisEtcIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보 수기 근무시간생성
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.getEmpHisEtcIfResult(tenantId); //사원 변경정보 저장 Service 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	@RequestMapping(value = "/empAddr",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empAddrIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원메일정보
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.getEmpAddrIfResult(tenantId); //사원 변경정보 저장 Service 호출
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	@RequestMapping(value = "/workTimeIf",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setTaaApplIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		try {
			HashMap<String, Object> reqMap = new HashMap<>();
			reqMap.put("tenantId", Long.parseLong(request.getParameter("tenantId").toString()));
			reqMap.put("enterCd", request.getParameter("enterCd").toString());
			reqMap.put("sabun", request.getParameter("sabun").toString());
			reqMap.put("taaCd", request.getParameter("workTimeCode").toString());
			reqMap.put("sYmd", request.getParameter("startYmd").toString());
			reqMap.put("eYmd", request.getParameter("endYmd").toString());
			reqMap.put("sHm", request.getParameter("startHm").toString());
			reqMap.put("eHm", request.getParameter("endHm").toString());
			reqMap.put("ifApplNo", Long.parseLong(request.getParameter("applNo").toString()));
			reqMap.put("status", request.getParameter("status").toString());
			System.out.println("+++++++++++++ reqMap : " + reqMap.toString());
			wtmInterfaceService.setTaaApplIf(reqMap); //근태정보 인터페이스
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	@RequestMapping(value = "/workTimeParam",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setTaaApplParma(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		try {
			HashMap<String, Object> reqMap = new HashMap<>();
			reqMap.put("tenantId", Long.parseLong(request.getParameter("tenantId").toString()));
			reqMap.put("enterCd", request.getParameter("enterCd").toString());
			System.out.println("+++++++++++++ reqMap : " + reqMap.toString());
			wtmInterfaceService.setTaaApplParam(reqMap); //근태정보 인터페이스
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	@RequestMapping(value = "/workTimeArrIf",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> setTaaApplArrIf(@RequestBody Map<String,Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		Map<String,Object> retMap = new HashMap();
		Map<String, Object> ifHisMap = new HashMap<>();
		System.out.println("workTimeArrIf : start");
		
		// 테넌트정보 조회
		String apiKey = null;
		apiKey = paramMap.get("apiKey").toString();
		CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByApiKey(apiKey);
	    Long tenantId = tm.getTenantId();
	    
		try {
	        // 근태저장
	        retMap = wtmInterfaceService.setTaaApplArrIf(paramMap); //근태정보(출장등 배열용) 인터페이스	        
	        // 결과값 저장 tenantId 있을때만
	        // WTM_IF_HIS 테이블에 결과저장
 	    	ifHisMap.put("tenantId", tenantId);
 	    	ifHisMap.put("ifItem", "WORK_TIME");
 			ifHisMap.put("ifStatus", retMap.get("status").toString());
 			ifHisMap.put("updateDate", WtmUtil.parseDateStr(new Date(), "yyyyMMddHHmmss"));
    		ifHisMap.put("ifEndDate", WtmUtil.parseDateStr(new Date(), "yyyyMMddHHmmss"));
 			ifHisMap.put("ifMsg", retMap.get("retMsg").toString());
 			wtmInterfaceService.setIfHis(ifHisMap);
	        
		} catch(Exception e) {
			
			e.printStackTrace();
			String errMsg = e.getMessage();
			ifHisMap.put("tenantId", tenantId);
 	    	ifHisMap.put("ifItem", "WORK_TIME");
 			ifHisMap.put("ifStatus", "ERR");
 			ifHisMap.put("updateDate", WtmUtil.parseDateStr(new Date(), "yyyyMMddHHmmss"));
    		ifHisMap.put("ifEndDate", WtmUtil.parseDateStr(new Date(), "yyyyMMddHHmmss"));
 			ifHisMap.put("ifMsg", errMsg);
 			wtmInterfaceService.setIfHis(ifHisMap);
 			
 			retMap.put("status", "ERR");
 	    	retMap.put("retMsg", errMsg);
		}
		System.out.println("workTimeArrIf : end");
		return retMap;
	}
	
	
	
	@RequestMapping(value = "/workTimeBatch",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setTaaApplPPIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		try {
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			
			wtmInterfaceService.setTaaApplBatchIf(tenantId); //5분간격 근태정보 인터페이스
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	@RequestMapping(value = "/workTimeCloseIf",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setWorkTimeCloseIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		try {
			HashMap<String, Object> reqMap = new HashMap<>();
			reqMap.put("tenantId", Long.parseLong(request.getParameter("tenantId").toString()));
			reqMap.put("enterCd", request.getParameter("enterCd").toString());
			reqMap.put("worktimeCloseId", Long.parseLong(request.getParameter("worktimeCloseId").toString()));
			reqMap.put("sYmd", request.getParameter("startYmd").toString());
			reqMap.put("eYmd", request.getParameter("endYmd").toString());
			reqMap.put("sabun", request.getParameter("sabun").toString());
			
			//WtmInterfaceService.setWorkTimeCloseIf(reqMap); //근무시간 마감생성
			wtmInterfaceService.setCloseWorkIf(reqMap); //근무시간 마감생성(루프돌려야함)
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
	
	@RequestMapping(value = "/calcDay",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCalcDay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 
		try {
			System.out.println("setCalcDay start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.setCalcDay(tenantId);
			System.out.println("setCalcDay end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/calcDayParam",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCalcDayParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 
		try {
			System.out.println("setCalcDay start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.setCalcDayParam(tenantId, request.getParameter("enterCd").toString(), request.getParameter("sabun").toString(), request.getParameter("sYmd").toString(), request.getParameter("eYmd").toString());
			System.out.println("setCalcDay end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/calcDayLoop",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCalcDayLoop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 
		try {
			System.out.println("setCalcDayloop start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			List<Map<String, Object>> searchList = null;
			try {
				searchList = wtmInterfaceService.getCalcDayLoopEmp(tenantId);
			} catch(Exception e) {
				e.getStackTrace();
			}
			
			if(searchList != null && searchList.size() > 0) {
				for(Map<String, Object> l : searchList) {
					wtmInterfaceService.setCalcDayLoop(tenantId, l.get("enterCd").toString(), l.get("sabun").toString(), l.get("sYmd").toString(), l.get("eYmd").toString());
				}
			}
			System.out.println("setCalcDayloop end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/colseDayBrs",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCloseDay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		try {
			System.out.println("setCloseDay start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			//Long tenantId = (long) 38;
			wtmInterfaceService.setCloseDay(tenantId);
			System.out.println("setCloseDay end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/sendCompCnt",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void sendCompCnt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		try {
			System.out.println("sendCompCnt start");
			HashMap<String, Object> reqMap = new HashMap<>();
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
			Long worktimeCloseId = Long.parseLong(request.getParameter("worktimeCloseId").toString());
			reqMap.put("tenantId", tenantId);
			reqMap.put("worktimeCloseId", worktimeCloseId);
			//Long tenantId = (long) 38;
			wtmInterfaceService.sendCompCnt(reqMap);
			System.out.println("sendCompCnt end");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/tempHj",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setTempHj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		try {
			HashMap<String, Object> reqMap = new HashMap<>();
			/*
			Long tenantId =  Long.parseLong("21");
			AuthConfig authConfig = null;
			authConfig = authConfigProvider.initConfig(tenantId, "samhwacrown");
			String encKey = authConfig.getEncryptKey();
			int repeatCount = authConfig.getHashIterationCount();
			String requestedPassword = Sha256.getHash("202002001", encKey, repeatCount);
			System.out.println("202002001 requestedPassword : " + requestedPassword);
			requestedPassword = Sha256.getHash("202002002", encKey, repeatCount);
			System.out.println("202002002 requestedPassword : " + requestedPassword);
			requestedPassword = Sha256.getHash("202002003", encKey, repeatCount);
			System.out.println("202002003 requestedPassword : " + requestedPassword);
			requestedPassword = Sha256.getHash("202002004", encKey, repeatCount);
			System.out.println("202002004 requestedPassword : " + requestedPassword);
			
			// System.out.println("apiKey : " + UUID.randomUUID().toString());
			
			*/
			reqMap.put("tenantId", Long.parseLong(request.getParameter("tenantId").toString()));
			
			wtmInterfaceService.setTempHj(reqMap); //근태정보 인터페이스
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ;
	}
}