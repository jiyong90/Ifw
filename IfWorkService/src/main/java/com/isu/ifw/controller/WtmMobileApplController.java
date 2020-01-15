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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmMobileApplService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmApplLineVO;


@RestController
public class WtmMobileApplController {
	
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

	@Autowired
	WtmMobileApplService mobileApplService;
	
	/**
	 * 연장/휴일 신청서 
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/apply/init", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getOtLoad(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, 
			@RequestParam(value="applCd", required = true) String applCd, 
			@RequestParam(value="id", required = false) String ymd,
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> dataMap = new HashMap();

		try {
			String userToken = request.getParameter("userToken");

			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+ tenantId+"/otload s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("applCd", applCd);
			paramMap.put("ymd", ymd);
			
			paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));

			Map<String, Object> result = new HashMap();
			//결재선 생성
			WtmApplCode applCode = applCodeRepository.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
			
			List<WtmApplLineVO> applLineVOs = applMapper.getWtmApplLine(paramMap);

			List<Map<String, Object>> apprLines = MobileUtil.makeApprLines(applLineVOs, applCode);
			//디폴트 데이터 가져오기
			if(applCd.equals("OT")) {
				Map<String,Map<String,Object>> itemPropertiesMap = new HashMap();
				itemPropertiesMap.put("gubun", mobileService.getCodeList(tenantId, enterCd, "REASON_CD"));
				result.put("itemAttributesMap", itemPropertiesMap);
			} else if(applCd.equals("ENTRY_CHG")) {
				if(!ymd.equals("")) {
					dataMap = mobileApplService.init(tenantId, enterCd, sabun, paramMap);
				}
			}
			
			result.put("apprLines", apprLines);
			
			//신청서 정보 조회할때 일부 필요한 데이터는 data에 넣어서 validation체크할때 쓰자....
			dataMap.put("applCd", applCd);
			dataMap.put("applNm", applCode.getApplNm());
			dataMap.put("applLevelCd", applCode.getApplLevelCd());

			result.put("data", dataMap);
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + rp.toString());
		return rp;
	}
	
	/**
	 * 연장/휴일 신청서 validation 체크
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/apply/val", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> applyValidate(@PathVariable Long tenantId, 
					@RequestBody Map<String, Object> paramMap
					,HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> resultMap = new HashMap();
		Map<String, Object> dataMap = new HashMap();

		try {
			String userToken = paramMap.get("userToken").toString();
			String empKey = paramMap.get("empKey").toString();
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}

			logger.debug("/mobile/"+ tenantId+"/apply/val s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			dataMap = (Map<String, Object>)paramMap.get("data");
			if("OT".equals(dataMap.get("applCd").toString())) {
				rp = mobileApplService.validateOtAppl(paramMap.get("eventSource").toString(), tenantId, enterCd, sabun, dataMap);
			} else if("ENTRY_CHG".equals(dataMap.get("applCd").toString())) {
				rp  = mobileApplService.validateEntryChgAppl(tenantId, enterCd, sabun, dataMap);
			}
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail(e.getMessage());
		} 
		logger.debug("/mobile/"+ tenantId+"/apply/val e " + rp.toString());
		return rp;
	}
	
	/**
	 * 연장/휴일 신청서 저장
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/apply/req", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> applyRequest(@PathVariable Long tenantId, 
														@RequestBody Map<String, Object> paramMap
														,HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("신청되었습니다.");
		
		try {
			String userToken = paramMap.get("userToken").toString();
			String empKey = paramMap.get("empKey").toString();
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			
			logger.debug("/mobile/"+ tenantId+"/apply/val s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

			Map<String, Object> dataMap = (Map)paramMap.get("data");
			if("OT".equals(dataMap.get("applCd").toString())) {
				rp = mobileApplService.requestOtAppl(tenantId, enterCd, sabun, dataMap);
			} else if("ENTRY_CHG".equals(dataMap.get("applCd").toString())) {
				rp = mobileApplService.requestEntryChgAppl(tenantId, enterCd, sabun, dataMap);
			}
			
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("신청 중 오류가 발생하였습니다.");
		}
		logger.debug("/mobile/"+ tenantId+"/apply/val e " + rp.toString());
		return rp;
	}

}