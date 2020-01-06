package com.isu.ifw.controller;


import java.net.URLDecoder;
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
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
public class WtmMobileEdocController {
	
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
	 * 신청서 목록 
	 */
	@RequestMapping(value = "/mobile/{tenantId}/edocument/list", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getOtLoad(@PathVariable Long tenantId, 
			@RequestParam(value = "locale", required = true) String locale,
			@RequestParam(value = "empKey", required = true) String empKey,
			@RequestParam(value = "typeCd", required = true) String typeCd,
			@RequestParam(value = "applCd", required = false) String applCd,
			@RequestParam(value = "startPage", required = false, defaultValue = "0") int startPage,
			@RequestParam(value = "pageCount", required = false, defaultValue = "15") int pageCount,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			String userToken = request.getParameter("userToken");
			logger.debug("111111111111111111111111111111111111111 " + empKey);
			empKey = URLDecoder.decode(empKey);
			empKey = empKey.replace(" ", "+");
			logger.debug("111111111111111111111111111111111111112 " + empKey);

			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
		
			logger.debug("/mobile/"+ tenantId+"/edocument/list s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			// 직원의 신청서 내역 조회
			List<?> list = mobileService.getApplList(tenantId, enterCd, sabun, typeCd, startPage, pageCount);

			if (list.size() > 0) {
				rp.setSuccess("");
				rp.put("result", list);
			} else {
				rp.setSuccess("검색된 신청서가 존재하지 않습니다.");
			}
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/edocument/list e " + rp.toString());
		return rp;
	}
	
	/**
	 * 신청서 상세 
	 */
	@RequestMapping(value = "/mobile/{tenantId}/edocument/init", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam detail(HttpServletRequest request,
			@PathVariable Long tenantId,
			@RequestParam(value = "locale", required = true) String locale,
			@RequestParam(value = "empKey", required = true) String empKey,
			@RequestParam(value = "applKey", required = true) String applKey) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Map<String, Object> result = new HashMap();
		try {
			String userToken = request.getParameter("userToken");
			
			logger.debug("111111111111111111111111111111111111111 " + empKey);
			empKey = URLDecoder.decode(empKey);
			empKey = empKey.replace(" ", "+");
			logger.debug("111111111111111111111111111111111111112 " + empKey);

			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
		
			result = mobileService.getApplDetail(tenantId, enterCd, sabun, applKey);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/edocument/list e " + rp.toString());
		rp.put("result", result);
		return rp;
	}
}