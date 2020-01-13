package com.isu.ifw.controller;

import java.net.URLDecoder;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmInoutHisMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
public class WtmInoutController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmInoutService inoutService;

	@Resource
	WtmEmpHisRepository empRepository;

	@Autowired
	WtmInoutHisMapper inoutHisMapper;
	
	/**
	 * 출/퇴근 버튼 각각
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkStatus(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = request.getParameter("userToken");
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

//		String enterCd =  empKey.split("@")[0];
//		String sabun =  empKey.split("@")[1];
		
		logger.debug("/mobile/"+ tenantId+"/inout/status s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

		
		Map <String,Object> resultMap = new HashMap<String,Object>();
		Map <String,Object> returnMap = new HashMap<String,Object>();

		Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
		
		resultMap.put("menus", menus);
		rp.put("result", resultMap);
		
		logger.debug("/mobile/"+ tenantId+"/inout/status e " + rp.toString());
		return rp;
	}	
	
	
	/**
	 * 근무계획으로 출/퇴근 타각 현황 
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/worktime/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkCheckStatus(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

//		String userToken = request.getParameter("userToken");
//		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
//		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");
		String enterCd = MobileUtil.parseEmpKey(empKey, "enterCd");
		String sabun = MobileUtil.parseEmpKey(empKey, "sabun");
//		logger.debug("/mobile/"+ tenantId+"inout/checkstatus s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

		Map <String,Object> resultMap = new HashMap<String,Object>();
		Map <String,Object> returnMap = new HashMap<String,Object>();
		//Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
//		Map<String,Object> menuIn = new HashMap();
//		menuIn.put("inoutType", "IN");
	
//		Map<String,Object> menuOut = new HashMap();
//		menuOut.put("inoutType", "OUT");
		
		
		//Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 

		Map<String,Object> menus = inoutService.getMenuContext2(tenantId, enterCd, sabun); 
		
//		returnMap.put("D01", menuIn);
//		returnMap.put("D02", menuOut);
//		returnMap.put("D02", menus);
		
		resultMap.put("menus", menus);
		rp.put("result", resultMap);
		
		logger.debug("/mobile/"+ tenantId+"/inout/checkstatus e " + rp.toString());
		return rp;
	}
	
	/**
	 * 출근 요청
	 * 처리 후, 어플에서는 퇴근 가능으로 메시지를 바꾼다.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/mobile/{tenantId}/inout/in", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestIn(@PathVariable Long tenantId, 
			@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
	
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("출근 체크 되었습니다.");
		
		String userToken = (String)params.get("userToken");
		String empKey = (String)params.get("empKey");
		String ymd = (String)params.get("ymd");

		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
		Date now = new Date();
		String today = format1.format(now);
		
		logger.debug("/mobile/"+ tenantId+"/inout/in s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
		
		Map<String, Object> paramMap = new HashMap();
		
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "IN");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "MO");
			
			logger.debug("/mobile/"+ tenantId+"/inout/in s2 " + paramMap.toString());
		
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if(yn != null && yn.get("unplannedYn") != null && "Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}
			
			logger.debug("in : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

		} catch(Exception e) {
			logger.debug("inexception : " + e.getMessage() + paramMap.toString());
			rp.setFail(e.getMessage());
		}
		logger.debug("/mobile/"+ tenantId+"/inout/in e " + rp.toString());
		return rp;
	}
	
	/**
	 * 퇴근 요청
	 * 처리 후, 어플에서는 퇴근 가능으로 메시지를 바꾼다.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/mobile/{tenantId}/inout/out", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestOut(@PathVariable Long tenantId, 
			@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 체크 되었습니다.");
		
		String userToken = (String)params.get("userToken");
		String empKey = (String)params.get("empKey");
		String ymd = (String)params.get("ymd");
		
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
		Date now = new Date();
		String today = format1.format(now);

		logger.debug("/mobile/"+ tenantId+"/inout/out s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

		Map<String, Object> paramMap = new HashMap();
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "OUT");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "MO");
			
			logger.debug("/mobile/"+ tenantId+"/inout/out s2 " + paramMap.toString());
			
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if("Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}
			
			logger.debug("out : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

		} catch(Exception e) {
			logger.debug("outexception : " + e.getMessage() + paramMap.toString());
			rp.setFail(e.getMessage());
		}
		logger.debug("/mobile/"+ tenantId+"/inout/out e " + rp.toString());
		return rp;
	}
	
	/**
	 * 출퇴근 데이터 현황
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/list", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyInoutList(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey,
			@RequestParam(value="id", required = true) String month,
			HttpServletRequest request) throws Exception {		

		ReturnParam rp = new ReturnParam();
		rp.setFail("출퇴근 이력 조회 중 오류가 발생했습니다.");

		String userToken = request.getParameter("userToken");
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
		if(emp == null) {
			rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
			return rp;
		}

		logger.debug("/mobile/"+ tenantId+"/inout/list s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("month", month);
			
			List<Map <String,Object>> l = inoutService.getMyInoutList(paramMap);
			if(l == null || l.size() <= 0) {
				rp.setFail("조회결과가 없습니다.");
				return rp;
			}
			
			l = MobileUtil.parseMobileList(l);
			
			rp.setSuccess("");
			rp.put("result", l);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/inout/list e " + rp.toString());
		return rp;
	}
	
	/**
	 * 일별 타각 현황
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/history", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyInoutHistory(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey,
			@RequestParam(value="id", required = true) String ymd,
			HttpServletRequest request) throws Exception {		

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = request.getParameter("userToken");
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		logger.debug("/mobile/"+ tenantId+"/inout/history s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
		WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
		if(emp == null) {
			rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
			return rp;
		}

		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("ymd", ymd);
			
			List<Map <String,Object>> l = inoutService.getMyInoutHistory(paramMap);
			if(l == null || l.size() <= 0) {
				rp.setFail("조회결과가 없습니다.");
				return rp;
			}
			l = MobileUtil.parseMobileList(l);
			rp.put("result", l);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/inout/history e " + rp.toString());

		return rp;
	}
	
	/**
	 * 출퇴근 데이터 상세
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/detail", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyInoutDetail(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey,
			@RequestParam(value="id", required = true) String id,
			HttpServletRequest request) throws Exception {		

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = request.getParameter("userToken");
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		logger.debug("/mobile/"+ tenantId+"/inout/detail s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
		WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
		if(emp == null) {
			rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
			return rp;
		}

		try {
			String inoutDate = id.split("@")[0];
			String inoutTypeCd = id.split("@")[1];
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutTypeCd", inoutTypeCd);
			paramMap.put("inoutDate", inoutDate);

			
			Map <String,Object> data = inoutService.getMyInoutDetail(paramMap);
			Map <String,Object> resultMap = new HashMap();
			
			resultMap.put("data", data);
			rp.put("result", resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("/mobile/"+ tenantId+"/inout/detail e " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value = "/mobile/{tenantId}/inout/cancel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam cancelOutRequest(@PathVariable Long tenantId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		String empKey = params.get("empKey").toString();
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 정보가 취소되었습니다.");

		String userToken = params.get("userToken").toString();
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		Map<String, Object> data = (Map<String, Object>) params.get("data");
		
		logger.debug("/mobile/"+ tenantId+"/inout/cancel s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

		try {
			
			inoutService.updateTimecard(data);
//			rp = inoutService.cancel(data);
//			rp = inoutService.updateTimecard(tenantId, enterCd, sabun, ymd, "OUTC", "MO");

//			if(cnt <= 0) {
//				rp.setFail("퇴근 취소가 실패하였습니다.");
//			}
			
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		logger.debug("/mobile/"+ tenantId+"/inout/cancel e " + rp.toString());

		return rp;
	}
	
	/**
	 * 외출 복귀
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/mobile/{tenantId}/inout/goback", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestGoback(@PathVariable Long tenantId, 
			@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("체크에 성공하였습니다.");
		
		
		String userToken = (String)params.get("userToken");
		String empKey = (String)params.get("empKey");

		String ymd = (String)params.get("ymd");
		String enterCd = MobileUtil.parseDEmpKey(userToken, empKey, "enterCd");
		String sabun = MobileUtil.parseDEmpKey(userToken, empKey, "sabun");

		logger.debug("/mobile/"+ tenantId+"/inout/goback s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
		Date now = new Date();
		String today = format1.format(now);

		Map<String, Object> paramMap = new HashMap();
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "EXCEPT");
			paramMap.put("ymd", ymd); //ymd 넘어오면 사용하기
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "MO");
			
			logger.debug("/mobile/"+ tenantId+"/inout/goback s2 " + paramMap.toString());
			
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if("Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}
			logger.debug("EXCEPT : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

		} catch(Exception e) {
			logger.debug("outexception : " + e.getMessage() + paramMap.toString());
			rp.setFail(e.getMessage());
		}
		
		logger.debug("/mobile/"+ tenantId+"/inout/goback s " + rp.toString());
		return rp;
	}
	
}

//출근, 퇴근 정보 전부 있으면 > calcApprDayInfo
//퇴근 취소 시 타각 데이터 지우고 인정시간도 지워주기