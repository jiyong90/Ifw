package com.isu.ifw.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.isu.ifw.util.WtmUtil;

@RestController
@RequestMapping(value="/interface")
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService wtmInterfaceService;
	
	@Autowired
	WtmInoutService inoutService;
	
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	@RequestMapping(value = "/ifAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void getIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 인터페이스 시작
		System.out.println("getIf start");
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString()); 
		
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
		
		System.out.println("getEmpAddrIfResult call");
		wtmInterfaceService.getEmpAddrIfResult(tenantId); //사원 주소
		
		System.out.println("getIf end");
		
		return;
	}
	
	
	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void getcodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		System.out.println("getcodeIf start");
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString()); 
		wtmInterfaceService.getCodeIfResult(tenantId);
		System.out.println("getcodeIf end");
		
		return;
	}
	
	@RequestMapping(value = "/{tsId}/code",method = RequestMethod.POST)
	public void postCode(@PathVariable String tsId, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
        
		// 공통코드
		System.out.println("postCode start");
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramMap.get("data");
		wtmInterfaceService.saveCodeIntf(tenantId, dataList); 
		System.out.println("postCode end");
		
		return;
	}
	
	@RequestMapping(value = "/holiday",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void holidayIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공휴일정보
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		wtmInterfaceService.getHolidayIfResult(tenantId); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/taaCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void taaCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 근태코드정보
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		wtmInterfaceService.getTaaCodeIfResult(tenantId); //Servie 호출
		return;
	}
	
	@RequestMapping(value = "/orgCode",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void orgCodeIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 조직코드정보
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		wtmInterfaceService.getOrgCodeIfResult(tenantId); //Servie 호출
		wtmInterfaceService.getOrgChartIfResult(tenantId); //Servie 호출
		return;
	}
		
	@RequestMapping(value = "/empHis",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		wtmInterfaceService.getEmpHisIfResult(tenantId); //사원 변경정보 저장 Servie 호출
		wtmInterfaceService.getOrgConcIfResult(tenantId); //조직장 변경정보 저장 Service 호출
		return ;
	}
	
	
	@RequestMapping(value = "/empHisEtc",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empHisEtcIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		//Long tenantId = (long) 38;
		wtmInterfaceService.getEmpHisEtcIfResult(tenantId); //사원 변경정보 저장 Servie 호출
		return ;
	}
	
	@RequestMapping(value = "/empAddr",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void empAddrIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원메일정보
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		//Long tenantId = (long) 38;
		wtmInterfaceService.getEmpAddrIfResult(tenantId); //사원 변경정보 저장 Servie 호출
		return ;
	}
	
	// 근태API연동용
	@RequestMapping(value = "/workTimeIf",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setTaaApplIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
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
		
		wtmInterfaceService.setTaaApplIf(reqMap); //근태정보 인터페이스
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
	
	// 5분간격 배치용
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
	
	// 
	@RequestMapping(value = "/workTimeCloseIf",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setWorkTimeCloseIf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 사원정보
		HashMap<String, Object> reqMap = new HashMap<>();
		reqMap.put("tenantId", Long.parseLong(request.getParameter("tenantId").toString()));
		reqMap.put("enterCd", request.getParameter("enterCd").toString());
		reqMap.put("ym", request.getParameter("ym").toString());
		reqMap.put("sYmd", request.getParameter("startYmd").toString());
		reqMap.put("eYmd", request.getParameter("endYmd").toString());
		reqMap.put("sabun", request.getParameter("sabun").toString());
		
		wtmInterfaceService.setWorkTimeCloseIf(reqMap); //근태정보 인터페이스
		return ;
	}
	
	@RequestMapping(value = "/calcDay",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCalcDay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 
		System.out.println("setCalcDay start");
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		//Long tenantId = (long) 38;
		wtmInterfaceService.setCalcDay(tenantId);
		System.out.println("setCalcDay end");
		
		return;
	}
	
	
	@RequestMapping(value = "/colseDay",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCloseDay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 공통코드
		System.out.println("setCloseDay start");
		Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
		//Long tenantId = (long) 38;
		wtmInterfaceService.setCloseDay(tenantId);
		System.out.println("setCloseDay end");
		
		return;
	}
}
