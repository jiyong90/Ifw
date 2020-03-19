package com.isu.ifw.controller;


import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmAsyncService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmApplLineVO;


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
	
	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService wtmOtApplService;

	@Autowired
	@Qualifier("wtmEntryApplService")
	WtmApplService wtmEntryApplService;
	
	@Autowired
	WtmAsyncService wtmAsyncService;

	@Autowired
	@Qualifier("wtmApplService")
	WtmApplService applService;
	
	@Autowired
	WtmInoutService inoutService;

	
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
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
		
			logger.debug("/mobile/"+ tenantId+"/edocument/list s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			// 직원의 신청서 내역 조회
			List<Map<String, Object>> list = mobileService.getApplList(tenantId, enterCd, sabun, typeCd, startPage, pageCount);
			
			if (list.size() > 0) {
//				list = MobileUtil.parseApprList(list);
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
	@RequestMapping(value = "/mobile/{tenantId}/edocument/detail", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
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
			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

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
	
	/**
	 * 신청서 상태 변경(결재, 반려)
	 */
	@RequestMapping(value = "/mobile/{tenantId}/edocument/status", method = RequestMethod.POST, produces = "application/json; charset=UTF-8", consumes = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam statusChange(HttpServletRequest request, 
			@PathVariable Long tenantId, @RequestBody Map<String, Object> body)
			throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("결재가 완료되었습니다.");
		logger.debug("/mobile/"+ tenantId+"/edocument/status e " + body.toString());

		Map<String, Object> result = new HashMap();
		try {
			String empKey = body.get("empKey").toString();
			String applKey = body.get("applKey").toString();
			String apprStatCd = body.get("apprStatCd").toString();
			String userToken = body.get("userToken").toString();

			String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
			
			String applCd = applKey.split("@")[1];
			String applId = applKey.split("@")[2];

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				logger.debug("사용자 정보 조회 중 오류가 발생하였습니다." + emp.toString());
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
		
			String applSabun = "";
			List<WtmApplLineVO> lines = applMapper.getWtmApplLineByApplId(Long.parseLong(applId));
			if(lines == null || lines.size() <= 0) {
				logger.debug("신청서 조회에 실패했습니다." + lines.toString());
				rp.setFail("신청서 조회에 실패했습니다.");
				return rp;
			}
			int apprSeq = 1;
			for(WtmApplLineVO line : lines) {
				if(line.getApprSeq() == 1) {
					applSabun = line.getSabun();
					continue;
				}
				if(line.getSabun().equals(sabun) && line.getApprStatusCd().equals("10")) {
					apprSeq = line.getApprSeq();
					break;
				}
			}
			
			if(apprSeq == 0) {
				logger.debug("결재 상태 확인 중 오류가 발생했습니다." + lines.toString());
				rp.setFail("결재 상태 확인 중 오류가 발생했습니다.");
				return rp;
			}
			Map<String, Object> paramMap = (Map<String, Object>) body.get("data");
			paramMap.put("apprOpinion", body.get("returnReason"));

			paramMap.put("applSabun", applSabun);
			
			//String ymd = paramMap.get("ymd").toString();
			//String otSdate = paramMap.get("otSdate").toString();
			//String otEdate = paramMap.get("otEdate").toString();

			ObjectMapper mapper = new ObjectMapper();
			try {
				if(applCd!=null && !"".equals(applCd)) {
					if("OT".equals(applCd)) {
						paramMap.put("otSdate", paramMap.get("otSdate").toString().replace(".","").replace(":", "").replace(" ", ""));
						paramMap.put("otEdate", paramMap.get("otEdate").toString().replace(".","").replace(":", "").replace(" ", ""));
						paramMap.put("ymd", paramMap.get("otEdate").toString().substring(0,8));
						
						logger.debug(":::::::: enterCd " + enterCd);
						logger.debug(":::::::: applId " + applId);
						logger.debug(":::::::: apprSeq " + apprSeq);
						logger.debug(":::::::: applSabun " + applSabun);
						logger.debug(":::::::: sabun " + sabun);
						logger.debug(":::::::: paramMap " + paramMap.toString());

						if(body.get("apprStatCd").toString().equals("02")) {
							wtmOtApplService.reject(tenantId, enterCd, Long.parseLong(applId), apprSeq, paramMap, sabun, emp.getEmpHisId().toString());
							
						} else if(body.get("apprStatCd").toString().equals("01")) {
							List<String> sabuns = new ArrayList<String>();
							sabuns.add(applSabun);
							paramMap.put("applSabuns", mapper.writeValueAsString(sabuns));

							logger.debug(":::::::: sabuns " + sabuns.toString());
							rp = wtmOtApplService.apply(tenantId, enterCd, Long.parseLong(applId), apprSeq, paramMap, sabun, emp.getEmpHisId().toString());
						} else {
							rp.setFail("신청서 상태에 오류가 발생했습니다.");
							return rp;
						}
					} else {
						logger.debug(":::::::: enterCd " + enterCd);
						logger.debug(":::::::: applId " + applId);
						logger.debug(":::::::: apprSeq " + apprSeq);
						logger.debug(":::::::: applSabun " + applSabun);
						logger.debug(":::::::: sabun " + sabun);
						logger.debug(":::::::: paramMap " + paramMap.toString());

						
						if(body.get("apprStatCd").toString().equals("02")) {
							applService.reject(tenantId, enterCd, Long.parseLong(applId), apprSeq, paramMap, sabun, sabun);
							//wtmEntryApplService.reject(tenantId, enterCd, Long.parseLong(applId), apprSeq, paramMap, sabun, emp.getEmpHisId().toString());
						} else if(body.get("apprStatCd").toString().equals("01")) {
							rp = wtmEntryApplService.apply(tenantId, enterCd, Long.parseLong(applId), apprSeq, paramMap, sabun, emp.getEmpHisId().toString());
						
							if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) && rp.containsKey("stdYmd")) {
								Map<String, Object> pMap = new HashMap<String, Object>();
								pMap.put("tenantId", tenantId);
								pMap.put("enterCd", enterCd);
								pMap.put("stdYmd", rp.get("stdYmd")+"");
								pMap.put("sabun", rp.get("sabun")+"");
								inoutService.inoutPostProcess(pMap, rp.get("unplannedYn").toString());
							}

						} else {
							rp.setFail("신청서 상태에 오류가 발생했습니다.");
							return rp;
						}
					}
				}
				if(rp.containsKey("sabun") && rp.containsKey("symd") && rp.containsKey("eymd")) {
					//wtmAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, rp.get("sabun")+"", rp.get("symd")+"", rp.get("eymd")+"", emp.getEmpHisId().toString());
					wtmAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, rp.get("sabun")+"", rp.get("symd")+"", rp.get("eymd")+"", emp.getEmpHisId().toString(), false);
				}
			} catch (Exception e) {
				logger.debug(e.getMessage());
				rp.setFail(e.getMessage());
			}
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/edocument/status e " + rp.toString());
		return rp;
	}

	/**
	 * 신청서 갯수(기안 문서 중 완료 되지 않은 건, 결재해야 할 건)
	 */
	@RequestMapping(value = "/mobile/{tenantId}/edocument/requestCount", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam requestCountHr(HttpServletRequest request,
			@PathVariable Long tenantId, 
			@RequestParam(value = "locale", required = true) String locale,
			@RequestParam(value = "empKey", required = true) String empKey) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> result = new HashMap();
		
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(empKey, "sabun");

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			result = applMapper.getEdocCountForMobile(paramMap);
		
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/edocument/requestCount e " + rp.toString());
		return rp;
	}
	
	
	/**
	 * 신청서 갯수(기안 문서 중 완료 되지 않은 건, 결재해야 할 건)
	 */
	@RequestMapping(value = "/mobile/{tenantId}/edocument/menuBadges/{uId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam menuBadges(HttpServletRequest request,
			@PathVariable Long tenantId, @PathVariable String uId, 
			@RequestParam(value = "locale", required = true) String locale,
			@RequestParam(value = "empKey", required = true) String empKey) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> result = new HashMap();
		Map<String, Object> menuBadges = new HashMap();
		
		try {
			String userToken = request.getParameter("userToken");
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			
			Map<String, Object> cnt = applMapper.getEdocCountForMobile(paramMap);
			menuBadges.put("count", cnt.get("totRequestCnt"));
			menuBadges.put("key", uId);
			
			result.put("menuBadges", menuBadges);
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/edocument/menuBadges e " + rp.toString());
		return rp;
	}
	
	@RequestMapping(value = "/mobile/process", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public void menuBadges(HttpServletRequest request,
			@RequestParam(value = "tenantId", required = true) Long tenantId,
			@RequestParam(value = "enterCd", required = true) String enterCd,
			@RequestParam(value = "stdYmd", required = true) String stdYmd,
			@RequestParam(value = "sabun", required = true) String sabun,
			@RequestParam(value = "unplannedYn", required = true) String unplannedYn) throws Exception {

		Map<String, Object> map = new HashMap();
		map.put("tenantId", tenantId);
		map.put("enterCd", enterCd);
		map.put("stdYmd", stdYmd);
		map.put("sabun", sabun);
		
		inoutService.inoutPostProcess(map, unplannedYn);
	}
}