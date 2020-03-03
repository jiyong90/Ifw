package com.isu.ifw.controller;

import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.TenantSecuredControl;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmInoutHisMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.service.WtmValidatorService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/api")
public class WtmApiController extends TenantSecuredControl {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Resource
	WtmEmpHisRepository empRepository;
	
	@Autowired
	WtmInoutService inoutService;

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	WtmInoutHisMapper inoutHisMapper;

	@Autowired
	private WtmInterfaceService wtmInterfaceService;
	
	@Autowired
	WtmValidatorService validatorService;
	
	@RequestMapping(value = "/{tsId}/d/{gubun}",method = RequestMethod.POST)
	public void postCode(@PathVariable String tsId,@PathVariable String gubun, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		CommTenantModule tm = null;
	    tm = tenantModuleRepo.findByTenantKey(tsId);
        Long tenantId = tm.getTenantId();
        
		// 공통코드
		System.out.println("postCode start");
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramMap.get("data");
		if(gubun.equalsIgnoreCase("CODE")) {
			wtmInterfaceService.saveCodeIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("EMP")) {
			wtmInterfaceService.saveEmpIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("EMPADDR")) {
			wtmInterfaceService.saveEmpAddrIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("GNT")) {
			wtmInterfaceService.saveGntIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("HOLIDAY")) {
			wtmInterfaceService.saveHolidayIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("ORG")) {
			wtmInterfaceService.saveOrgIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("ORGCONC")) {
			wtmInterfaceService.saveOrgConcIntf(tenantId, dataList);
		}else if (gubun.equalsIgnoreCase("TAAAPPL")) {
			wtmInterfaceService.saveTaaApplIntf(tenantId, dataList);
		}
		System.out.println("postCode end");
		
		return;
	}
	
	
	//출퇴근 상태 정보
	@RequestMapping(value = "/{tsId}/worktime/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkStatus(
			@PathVariable String tsId, 
			@RequestParam(value="enterCd", required = true) String enterCd, 
			@RequestParam(value="sabun", required = true) String sabun, 
			HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map <String,Object> resultMap = new HashMap<String,Object>();

		try {
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			Long tenantId = tm.getTenantId();
			
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
		}catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		rp.put("result", resultMap);
		logger.debug("/api/workstatus e " + rp.toString());
		return rp;
	}
	
	//출근
	@RequestMapping (value="/{tsId}/worktime/in", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestIn(@PathVariable String tsId, 
			@RequestBody Map<String,Object> params, HttpServletRequest request)throws Exception{
	
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("출근 체크 되었습니다.");
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;

		try {
			logger.debug(tsId + "/api/in s " + params.toString());

			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
	
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			Map<String, Object> paramMap = new HashMap();
		
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "IN");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug(tsId + "/api/in s2 " + paramMap.toString());
		
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if(yn != null && yn.get("unplannedYn") != null && "Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}
			
			logger.debug("in : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());
		} catch(Exception e) {
			logger.debug("inexception : " + e.getMessage());
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}

		logger.debug(tsId + "/api/in e " + rp.toString());
		return rp;
	}
	
	//퇴근
	@RequestMapping (value="/{tsId}/worktime/out", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestOut(@PathVariable String tsId, 
			@RequestBody Map<String,Object> params, HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 체크 되었습니다.");
		
		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;
		try {
			logger.debug(tsId + "/api/out s " + params.toString());

			
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);

			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "OUT");
			paramMap.put("ymd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug(tsId + "/api/out s2 " + paramMap.toString());
			
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			if(yn != null && yn.get("unplannedYn") != null && "Y".equals(yn.get("unplannedYn").toString())) {
				inoutService.updateTimecardUnplanned(paramMap);
			} else {
				inoutService.updateTimecard(paramMap);
			}
			//퇴근일때만 인정시간 계산
			inoutService.inoutPostProcess(paramMap, yn.get("unplannedYn").toString());

		} catch(Exception e) {
			logger.debug("outexception : " + e.getMessage() + paramMap.toString());
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}
		logger.debug(tsId + "/api/out e " + rp.toString());
		return rp;
	}
	
	//퇴근취소
	@RequestMapping(value = "/{tsId}/worktime/cancel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam cancelOutRequest(@PathVariable String tsId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 정보가 취소되었습니다.");

		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;
		
		try {
			logger.debug(tsId + "/api/cancel s " + params.toString());
 
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "OUTC");
			paramMap.put("ymd", ymd);
			paramMap.put("stdYmd", ymd);
			paramMap.put("inoutDate", null);
			paramMap.put("entryType", "API");
			
			logger.debug(tsId + "/api/cancel s2 " + paramMap.toString());
			inoutService.updateTimecardCancelWeb(paramMap);
			
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}
		
		logger.debug(tsId + "/api/cancel e " + rp.toString());

		return rp;
	}
	
	//외출복귀
	@RequestMapping(value = "/{tsId}/worktime/except", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam exceptRequest(@PathVariable String tsId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("체크에 성공하였습니다.");

		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		String enterCd=null;
		String sabun=null;
		
		try {
			logger.debug(tsId + "/api/cancel s " + params.toString());
 
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String ymd = (String)params.get("ymd");
			enterCd = (String)params.get("enterCd");
			sabun = (String)params.get("sabun");
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
			Date now = new Date();
			String today = format1.format(now);
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("inoutType", "EXCEPT");
			paramMap.put("ymd", ymd);
			paramMap.put("stdYmd", ymd);
			paramMap.put("inoutDate", today);
			paramMap.put("entryType", "API");
			
			logger.debug(tsId + "/api/except s2 " + paramMap.toString());
			Map<String, Object> yn = inoutHisMapper.getMyUnplannedYn(paramMap);
			inoutService.updateTimecardExcept(paramMap, yn.get("unplannedYn").toString());
			logger.debug("EXCEPT : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		} finally {
			Map<String, Object> resultMap = inoutService.getMenuContextWeb(tenantId, enterCd, sabun); 
			rp.put("result", resultMap);
		}
		
		logger.debug(tsId + "/api/cancel e " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/worktime/appl/valid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam worktimeApplValid(@RequestBody Map<String, Object> paramMap, HttpServletRequest request ){
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			if(!paramMap.containsKey("apiKey") || paramMap.get("apiKey")==null || "".equals(paramMap.get("apiKey")) ) {
				rp.setFail("apiKey가 없습니다.");
				return rp;
			}
			if(!paramMap.containsKey("secret") || paramMap.get("secret")==null || "".equals(paramMap.get("secret")) ) {
				rp.setFail("secret가 없습니다.");
				return rp;
			}
			
			this.certificate(paramMap.get("apiKey").toString(), paramMap.get("secret").toString(), request.getRemoteHost());
		} catch (CertificateException e1) {
			rp.setFail(e1.getMessage());
			return rp;
		}
		
		Long tenantId = this.getTenantId(paramMap.get("apiKey").toString());
		if(!paramMap.containsKey("enterCd") || paramMap.get("enterCd")==null || "".equals(paramMap.get("enterCd")) ) {
			rp.setFail("회사 코드가 없습니다.");
			return rp;
		}
		
		Long applId = null;
		if(paramMap.containsKey("applId") && paramMap.get("applId")!=null && !"".equals(paramMap.get("applId")) ) {
			applId = Long.parseLong(paramMap.get("applId").toString());
		}
		
		if(!paramMap.containsKey("appl") || paramMap.get("appl")==null || "".equals(paramMap.get("appl")) ) {
			rp.setFail("근태 정보가 없습니다.");
			return rp;
		}
			
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> workList = (List<Map<String, Object>>)paramMap.get("appl");
		
		rp = validatorService.worktimeValid(tenantId, paramMap.get("enterCd").toString(), workList, applId);
		
		return rp;
	}
}
