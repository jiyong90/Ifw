package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmInterfaceService;

@RestController
public class TestController {
	
	@Autowired
	WtmFlexibleEmpService empService;
	
	@Autowired
	WtmInterfaceService interfaceService;
	
	@Autowired
	WtmFlexibleStdMapper flexStdMapper;
	
	@Autowired
	WtmFlexibleStdMgrRepository stdMgrRepo;
	
	@Autowired
	WtmFlexibleEmpRepository flexEmpRepo;
	
	@RequestMapping(value = "/login/calcAppr", method = RequestMethod.GET)
	public Map<String, Object> testCalcAppr(@RequestParam String enterCd,
										@RequestParam Long tenantId,
										@RequestParam String sabun,
										@RequestParam String sYmd,
										@RequestParam String eYmd,
										HttpServletRequest request, 
										HttpServletResponse response){
		
		Map<String, Object> m = new HashMap<String, Object>();
		empService.calcApprDayInfo(tenantId, enterCd, sYmd, eYmd, sabun);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("pId", "JYP");
		paramMap.put("symd", sYmd);
		paramMap.put("eymd", eYmd);
		empService.createWorkTermtimeByEmployee(tenantId, enterCd, sabun, paramMap, "JSP");
		return m;
	}
	
	
	@RequestMapping(value = "/calcDayParam",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setCalcDayParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
      // 
		try {
			System.out.println("setCalcDay start");
			Long tenantId = Long.parseLong(request.getParameter("tenantId").toString());
         //Long tenantId = (long) 38;
			interfaceService.setCalcDayParam(tenantId, request.getParameter("enterCd").toString(), request.getParameter("sabun").toString(), request.getParameter("sYmd").toString(), request.getParameter("eYmd").toString());
			System.out.println("setCalcDay end");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/login/dayPatt",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public void createDayPatt(HttpServletRequest request, HttpServletResponse response) throws Exception {
      // 
		try {
			System.out.println("dayPatt start");
			
			List<WtmFlexibleStdMgr> mgrs = stdMgrRepo.findAll();
			for(WtmFlexibleStdMgr mgr : mgrs) {
				List<WtmFlexibleEmp> emps = flexEmpRepo.findByFlexibleStdMgrId(mgr.getFlexibleStdMgrId());
				for(WtmFlexibleEmp emp : emps) {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("tenantId", emp.getTenantId());
					paramMap.put("enterCd", emp.getEnterCd());
					paramMap.put("flexibleStdMgrId", emp.getFlexibleStdMgrId());
					paramMap.put("symd", emp.getSymd());
					paramMap.put("eymd", emp.getEymd());
					
					flexStdMapper.mergeWtmDayPattByFlexibleStdMgrId(paramMap);
				}
			}
			System.out.println("dayPatt end");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
