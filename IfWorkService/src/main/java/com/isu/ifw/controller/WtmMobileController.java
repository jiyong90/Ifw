package com.isu.ifw.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
public class WtmMobileController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmMobileService mobileService;
	
	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService otApplService;


	/**
	 * dashboard
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/dashboard", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyDashboard(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, HttpServletRequest request) throws Exception {

		logger.debug("/mobile/{tenantId}/dashboard ");

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
        Map statusMap = new HashMap();
        statusMap.put("param1", "선근제");
        statusMap.put("param2", "72:00");
        statusMap.put("param3", "160");
        statusMap.put("param4", "48:00");
        statusMap.put("param5", "33:00");
        statusMap.put("param6", "14");
        statusMap.put("param7", "10");
        statusMap.put("param8", "4");
        
		Map dashboards = new HashMap();
		Map status = new HashMap();
		Map data = new HashMap();


		data.put("param1", statusMap.get("param1") == null? "-" : statusMap.get("param1").toString());
		data.put("param2", statusMap.get("param2") == null? "-" : statusMap.get("param2").toString());
		status.put("part1", data);

		data = new HashMap();
		data.put("param3", statusMap.get("param3") == null? "-" : statusMap.get("param3").toString());
		data.put("param4", statusMap.get("param4") == null? "-" : statusMap.get("param4").toString());
		data.put("param5", statusMap.get("param5") == null? "-" : statusMap.get("param5").toString());
		status.put("part2", data);

		data = new HashMap();
		data.put("param6", statusMap.get("param6") == null? "-" : statusMap.get("param6").toString());
		data.put("param7", statusMap.get("param7") == null? "-" : statusMap.get("param7").toString());
		data.put("param8", statusMap.get("param8") == null? "-" : statusMap.get("param8").toString());
		status.put("part3", data);
		
		dashboards.put("DASHBOARD1", status);

		rp.put("result", dashboards);
		return rp;
	}
	
	/**
	 * 부서원 근태현황 (기간 리스트)
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/termlist", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getTermList(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="id", required = true) String month,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			logger.debug("/mobile/"+ tenantId+"/team/termlist s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("month", month);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			List<Map<String, Object>> l = mobileService.getTermList(paramMap);
			l = MobileUtil.parseMobileList(l);
			
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/termlist s " + rp.toString());
		return rp;
	}
	
	/**
	 * 부서원 근태현황 (직원 리스트)
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/teamlist", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getTeamList(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="id", required = true) String id,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try { 
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			logger.debug("/mobile/"+ tenantId+"/team/teamlist s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			String sdate = id.split("@")[0];
			String orgCd = id.split("@")[1];
			String workTypeCd = id.split("@")[2];
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("sdate", sdate);
			paramMap.put("orgCd", orgCd);
			paramMap.put("workTypeCd", workTypeCd);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			List<Map<String, Object>> l = mobileService.getTeamList(paramMap);
			l = MobileUtil.parseMobileList(l);
	
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}

		logger.debug("/mobile/"+ tenantId+"/team/teamlist s " + rp.toString());
		return rp;
	}
	
	/**
	 * 부서원 근태현황 (직원 리스트)
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/team/teamdetail", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getTeamDetail(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="id", required = true) String id,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			String userToken = request.getParameter("userToken");
		
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");

			logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			String targetSabun = id.split("@")[0];
			String sdate = id.split("@")[1];
			String edate = id.split("@")[2];
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", targetSabun);
			paramMap.put("sdate", sdate);
			paramMap.put("edate", edate);
			
			List<Map<String, Object>> l = mobileService.getTeamDetail(paramMap);
			l = MobileUtil.parseMobileList(l);
			
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + rp.toString());
		return rp;
	}
	
	/**
	 * 연장/휴일 신청서 
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/init", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getOtLoad(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="applCd", required = true) String applCd, 
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			String userToken = request.getParameter("userToken");
		
			//Aes256 aes = new Aes256(userToken);
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");

			logger.debug("/mobile/"+ tenantId+"/otload s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));

			//List<Map<String, Object> apprLines = MobileUtil.getApprLines(paramMap);
			Map<String, Object> result = new HashMap();
			
			//result.put("apprLines", apprLines);
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + rp.toString());
		return rp;
	}
}