package com.isu.ifw.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.Aes256;
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

	@Resource
	WtmApplCodeRepository applCodeRepository;
	
	@Autowired
	WtmApplMapper applMapper;

	@Resource
	WtmEmpHisRepository empRepository;

	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	TenantConfigManagerService tcms;

	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;

	
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


		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/{tenantId}/dashboard " + empKey);
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
			Date now = new Date();
			String today = format1.format(now);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("ymd", today);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			Map<String, Object> statusMap = flexEmpMapper.getFlexibleRangeInfoMobile(paramMap);
	
			Map dashboards = new HashMap();
			Map status = new HashMap();
			Map data = new HashMap();
	
	
			data.put("param1", statusMap.get("param1") == null? "-" : statusMap.get("param1").toString());
//			String date = "-";
//			if(statusMap.get("param2") != null && statusMap.get("param3") != null) {
//				date += statusMap.get("param2").toString() + "\n" + statusMap.get("param3").toString();
//			}
			data.put("param2", statusMap.get("param2") == null? "-" : statusMap.get("param2").toString());
			data.put("param3", statusMap.get("param3") == null? "-" : statusMap.get("param3").toString());
			status.put("part1", data);
	
			data = new HashMap();
			data.put("param4", statusMap.get("param4") == null? "-" : statusMap.get("param4").toString());
			data.put("param5", statusMap.get("param5") == null? "-" : statusMap.get("param5").toString());
			data.put("param6", statusMap.get("param6") == null? "-" : statusMap.get("param6").toString());
			status.put("part3", data);
	
			String adapter = tcms.getConfigValue(tenantId, "HR.ADAPTER_URL", true, "");
			Map restCnt = mobileService.getDataMap(adapter+"/GetDataMap.do", "getMyRestCnt", enterCd, sabun);
			data = new HashMap();
			data.put("param7", restCnt == null || restCnt.get("useCnt") == null? "-" : restCnt.get("useCnt").toString());
			data.put("param8", restCnt == null || restCnt.get("usedCnt") == null? "-" : restCnt.get("usedCnt").toString());
			data.put("param9", restCnt == null || restCnt.get("restCnt") == null? "-" : restCnt.get("restCnt").toString());
			status.put("part2", data);
			
			dashboards.put("DASHBOARD1", status);
	
			rp.put("result", dashboards);
		} catch(Exception e) {
			rp.put("result", null);
			logger.debug(e.getMessage());
		}
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
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
				
			
//			sabun = "317290";//"317268";
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
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+ tenantId+"/team/teamlist s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			String sdate = id.split("@")[0];
			String orgCd = id.split("@")[1];
			String workTypeCd = id.split("@")[2];
			
			String[] orgList = orgCd.split(",");
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("sdate", sdate);
			paramMap.put("orgList", orgList);
			paramMap.put("workTypeCd", workTypeCd);
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			List<Map<String, Object>> l = mobileService.getTeamList(paramMap);
			l = MobileUtil.parseMobileList(l);
	
			rp.put("result", l);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail(e.getMessage());
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
		
			Aes256 aes = new Aes256(userToken);
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
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
}