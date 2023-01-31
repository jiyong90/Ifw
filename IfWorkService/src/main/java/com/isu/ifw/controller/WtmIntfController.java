package com.isu.ifw.controller;


import com.isu.ifw.TenantSecuredControl;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.service.*;
import com.isu.ifw.util.Sha256;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class WtmIntfController extends TenantSecuredControl {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmMobileService mobileService;
	
	@Autowired
	WtmInoutService inoutService;
	
	@Resource
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	WtmValidatorService validatorService;
	
	@Autowired
	private WtmInterfaceService interfaceService;

	@Autowired
	WtmInterfaceService wtmInterfaceService;
	
	@RequestMapping(value = "/intf/abcd", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public void abcd(HttpServletRequest request) throws Exception {
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("tenantId", request.getParameter("tenantId"));
		paramMap.put("enterCd", request.getParameter("enterCd"));
		paramMap.put("sabun", request.getParameter("sabun"));
		paramMap.put("stdYmd", request.getParameter("stdYmd"));
		
		inoutService.inoutPostProcess(paramMap);
	}

	@RequestMapping (value="/intf/inoutCheck", method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> inoutCheck(HttpServletRequest request)throws Exception{
   
		System.out.println("/intf/inoutCheck : requestParam type");
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
      
		Map<String, Object> paramMap = new HashMap();
		
		logger.debug("/intf/inoutCheck s " + WtmUtil.paramToString(request));
		try {
			String apiKey = request.getParameter("apiKey");
			String secret = request.getParameter("secret");
			// 세콤 타각이용하는 고객사에서 인터페이스 되는 날짜 데이터가 뒤에 공백을 포함해서 넘어옴
			String time = request.getParameter("time").toString().trim();
			if(time.length() <= 12) {
				time = request.getParameter("time").toString()+"00";
			}
			CommTenantModule tm = tenantModuleRepo.findByApiKey(apiKey);
      
			if(tm == null) {
				rp.setFail("apiKey 불일치");
				return rp;
			}

			String encryptCode = tm.getTenantKey().toString();
			if(encryptCode.length() < 12) {
				encryptCode = String.format("%12s", encryptCode).replaceAll(" ", "o");
			}
			String s = Sha256.getHash(secret, encryptCode, 10);
			if(!s.equals(tm.getSecret()))
			{
				rp.setFail("secret 불일치");
				return rp;
			}
			
			paramMap.put("tenantId", tm.getTenantId());
			paramMap.put("enterCd", request.getParameter("enterCd"));
			paramMap.put("sabun", request.getParameter("emp"));
			paramMap.put("inoutDate", time);
			paramMap.put("inoutType", request.getParameter("type"));
			paramMap.put("entryNote", request.getParameter("deviceKey"));
			paramMap.put("entryType", "INTF");
         
			logger.debug("getParameter s2 " + request.getParameter("deviceKey") + " " + request.getParameter("emp") + " " + time + " " + request.getParameter("type"));
      
			if(tm.getTenantKey().equalsIgnoreCase("SAMHWACROWN")|| tm.getTenantKey().equalsIgnoreCase("SOLDEV")) {
				inoutService.updEntryDate(tm.getTenantId(), request.getParameter("enterCd"), request.getParameter("emp"), request.getParameter("type"), time, request.getParameter("deviceKey"), "INTF");
			}else {
				String stdYmd = inoutService.updateTimecard3(paramMap);

				if(stdYmd != null && "".equals(stdYmd)) {
					paramMap.put("stdYmd", stdYmd);
				}

				logger.debug("/intf/inoutCheck rp : " + rp.toString());
				if(request.getParameter("type").toString().equals("OUT")) {
					//퇴근일때만 인정시간 계산
					inoutService.inoutPostProcess(paramMap);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		logger.debug("getParameter e " + request.getParameter("emp") + "," + request.getParameter("time")+"," +request.getParameter("type")+","+request.getParameter("deviceKey")+","+ rp.toString());
		return rp;
	}
	
	@RequestMapping (value="/intf/inoutCheck", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> inoutCheck(HttpServletRequest request,
			@RequestBody Map<String,Object> params)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		logger.debug("/intf/inoutCheck s " + params.toString());
		try {
			String apiKey = params.get("apiKey").toString();
			String secret = params.get("secret").toString();
			// 세콤 타각이용하는 고객사에서 인터페이스 되는 날짜 데이터가 뒤에 공백을 포함해서 넘어옴
			String time = params.get("time").toString().trim();
			if(time.length() <= 12) {
				time = params.get("time").toString()+"00";
			}
			
			CommTenantModule tm = tenantModuleRepo.findByApiKey(apiKey);
			logger.debug("tm " + tm.toString());
			if(tm == null) {
				rp.setFail("apiKey 불일치");
				return rp;
			}

			String encryptCode = tm.getTenantKey().toString();
			if(encryptCode.length() < 12) {
				encryptCode = String.format("%12s", encryptCode).replaceAll(" ", "o");
			}
			String s = Sha256.getHash(secret, encryptCode, 10);
			/*
			if(!s.equals(tm.getSecret()))
			{
				rp.setFail("secret 불일치");
				return rp;
			}
			*/
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tm.getTenantId());
			paramMap.put("enterCd", params.get("enterCd").toString());
			paramMap.put("sabun", params.get("emp"));
			paramMap.put("inoutDate", time);
			paramMap.put("inoutType", params.get("type"));
			paramMap.put("entryNote", params.get("deviceKey"));
			paramMap.put("entryType", "INTF");
			
			logger.debug("getParameter s2 " + params.get("deviceKey") + " " + params.get("emp") + " " + time + " " + params.get("type"));
		
			//삼화왕관 만 타각정보를 다르게 지정한다. 
			//if(tm.getTenantId().equals(22)) {
			if(tm.getTenantKey().equalsIgnoreCase("SAMHWACROWN") || tm.getTenantKey().equalsIgnoreCase("SOLDEV")){
				inoutService.updEntryDate(tm.getTenantId(), params.get("enterCd")+"", params.get("emp")+"", params.get("type")+"", time+"", params.get("deviceKey")+"", "INTF");
			}else {
				String stdYmd = inoutService.updateTimecard3(paramMap);

				if(stdYmd != null && "".equals(stdYmd)) {
					paramMap.put("stdYmd", stdYmd);
				}

				logger.debug("/intf/inoutCheck rp : " + rp.toString());
				if(params.get("type").toString().equals("OUT")) {
					//퇴근일때만 인정시간 계산
					inoutService.inoutPostProcess(paramMap);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		logger.debug("getParameter e " + request.getParameter("emp") + "," + request.getParameter("time")+"," +request.getParameter("type")+","+request.getParameter("deviceKey")+","+ rp.toString());
		return rp;
	}
	
	@RequestMapping(value="/intf/appl/valid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam worktimeApplValid(@RequestBody Map<String, Object> paramMap, HttpServletRequest request ){
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			validateParamMap(paramMap, "apiKey", "secret", "enterCd", "applNo", "works");
			
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
			
			String applSabun = null;
			if(paramMap.containsKey("applSabun") && paramMap.get("applSabun")!=null && !"".equals(paramMap.get("applSabun")) ) {
				applSabun = paramMap.get("applSabun").toString();
			}
				
			List<Map<String, Object>> works = (List<Map<String, Object>>)paramMap.get("works");
			
			rp = validatorService.worktimeValid(tenantId, paramMap.get("enterCd").toString(), paramMap.get("applNo").toString(), works, applSabun);
		
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/intf/appl/request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam requestWorktimeAppl(@RequestBody Map<String, Object> paramMap, HttpServletRequest request ){
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			validateParamMap(paramMap, "apiKey", "secret", "enterCd", "applNo", "works", "applSabun", "status");
			
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
			
			String applSabun = null;
			if(paramMap.containsKey("applSabun") && paramMap.get("applSabun")!=null && !"".equals(paramMap.get("applSabun")) ) {
				applSabun = paramMap.get("applSabun").toString();
			}
				
			List<Map<String, Object>> works = (List<Map<String, Object>>)paramMap.get("works");
			String status = paramMap.get("status")+"";
			if(!status.equals(WtmApplService.APPL_STATUS_CANCEL)) {
	    		
	    		
	    		rp = validatorService.worktimeValid(tenantId, paramMap.get("enterCd").toString(), paramMap.get("applNo").toString(), works,paramMap.get("applSabun").toString());
	    		//ObjectMapper mm = new ObjectMapper();
	    		logger.debug(rp.getStatus() + " : " + rp.get("message"));
	    		if(rp!=null && rp.getStatus()!=null && !"OK".equals(rp.getStatus())) {
					return rp;
	    		}
	    	}
			//rp = validatorService.worktimeValid(tenantId, paramMap.get("enterCd").toString(), paramMap.get("applNo").toString(), works, applSabun);
			//if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
			interfaceService.setTaaApplArrIf(paramMap);
//			interfaceService.taaResult(tenantId, paramMap.get("enterCd")+"", paramMap.get("applSabun")+"", paramMap.get("applNo")+"", paramMap.get("status")+"", works);
			//}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
			return rp;
		}
		
		return rp;
	}
	
	/**
	 * 파라미터 맵의 유효성을 검사한다.
	 * 파라미터가 다음의 두 조건을 만족하지 않으면, InvalidParameterException를 발생한다.
	 * 1. map 자체가 null 인 경우
	 * 2. paramName 배열에 기술된 값이 map에 (하나라도) 키로 존재하지 않는 경우
	 * 
	 * @param paramMap
	 * @param parameterNames
	 * @throws InvalidParameterException
	 */
	protected void validateParamMap(Map<String,Object> paramMap, String...parameterNames )throws InvalidParameterException{
		
		// 파라미터가 아무것도 없는 경우에, 아무것도 할 수 없다. 무조건 예외 발생
		if(paramMap == null)
			throw new InvalidParameterException("param map is null.");
		
		if(parameterNames == null)
			return; // 파라미터가 없으면 그냥 리턴..
		
		// 넘겨 받은 이름 배열이 map의 부분 집합인지를 따짐.
		Set<String> paramKeySet = paramMap.keySet();
		Collection<String> params = Arrays.asList(parameterNames);
		
		if(!paramKeySet.containsAll(params))
			throw new InvalidParameterException("required parameter is not found.");
		
	}
	
	@RequestMapping (value="/intf/test/status", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> test0(HttpServletRequest request, @RequestParam Map<String,Object> params)throws Exception{
   
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
      
		try {
         	Map<String,Object> menus = inoutService.getMenuContext3(Long.parseLong(params.get("tenantId").toString())
         			, params.get("enterCd").toString(), params.get("emp").toString(), params.get("ymd").toString()); 
         	Map<String, Object> resultMap = new HashMap();
    		resultMap.put("menus", menus);
    		rp.put("result", resultMap);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		return rp;
	}
	
	@RequestMapping (value="/intf/test/in", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> test1(HttpServletRequest request, @RequestParam Map<String,Object> params)throws Exception{
   
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
      
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String today = format1.format(now);
      
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", request.getParameter("tenantId"));
			paramMap.put("enterCd", request.getParameter("enterCd"));
			paramMap.put("sabun", request.getParameter("emp"));
			paramMap.put("inoutDate", request.getParameter("time"));
			paramMap.put("inoutType", "IN");
			paramMap.put("entryNote", "TEST");
			paramMap.put("entryType", "INTF");
         	//inoutService.updateTimecard3(paramMap); 2022-03-17 혼다 퇴근타각 문제로 2번으로 변경
         	inoutService.updateTimecard2(paramMap);
			if(request.getParameter("enterCd").equals("ISU_ST")) {
				inoutService.sendErp(request.getParameter("enterCd"), request.getParameter("emp"), paramMap);
			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping (value="/intf/test/out", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> test2(HttpServletRequest request, @RequestParam Map<String,Object> params)throws Exception{
   
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
      
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String today = format1.format(now);
      
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", request.getParameter("tenantId"));
			paramMap.put("enterCd", request.getParameter("enterCd"));
			paramMap.put("sabun", request.getParameter("emp"));
			paramMap.put("inoutDate", request.getParameter("time"));
			paramMap.put("inoutType", "OUT");
			paramMap.put("entryNote", "TEST");
			paramMap.put("entryType", "INTF");
         	//inoutService.updateTimecard3(paramMap); 2022-03-17 혼다 퇴근타각 문제로 2번으로 변경
         	inoutService.updateTimecard2(paramMap);
         	inoutService.inoutPostProcess(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping (value="/intf/test/except", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> test3(HttpServletRequest request, @RequestParam Map<String,Object> params)throws Exception{
   
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
      
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String today = format1.format(now);
      
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", request.getParameter("tenantId"));
			paramMap.put("enterCd", request.getParameter("enterCd"));
			paramMap.put("sabun", request.getParameter("emp"));
			paramMap.put("inoutDate", request.getParameter("time"));
			paramMap.put("inoutType", "EXCEPT");
			paramMap.put("entryNote", "TEST");
			paramMap.put("entryType", "INTF");
			inoutService.updateTimecardExcept(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping (value="/intf/test/calcel", method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> test4(HttpServletRequest request, @RequestParam Map<String,Object> params)throws Exception{
   
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
      
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String today = format1.format(now);
      
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", request.getParameter("tenantId"));
			paramMap.put("enterCd", request.getParameter("enterCd"));
			paramMap.put("sabun", request.getParameter("emp"));
			paramMap.put("inoutDate", request.getParameter("time"));
			paramMap.put("inoutType", "OUTC");
			paramMap.put("entryNote", "TEST");
			paramMap.put("entryType", "INTF");
			paramMap.put("stdYmd", request.getParameter("ymd"));
			paramMap.put("ymd", request.getParameter("ymd"));
			paramMap.put("stdYmd", request.getParameter("ymd"));
			inoutService.updateTimecardCancel(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}


	@RequestMapping(value = "/intf/workTimeCloseIf",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam setWorkTimeCloseIf(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws Exception {
		ReturnParam rp = new ReturnParam();
		// 사원정보
		try {
//			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
//			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
//			String enterCd = sessionData.get("enterCd").toString();
			Long tenantId = Long.valueOf(paramMap.get("tenantId").toString());
			String enterCd = paramMap.get("enterCd").toString();

			System.out.println("tenantId >>> "+tenantId);
			System.out.println("enterCd >>> "+enterCd);
			System.out.println("paramMap >>> "+paramMap.toString());
			// paramMap >>> {worktimeCloseId=3, sYmd=20191230, eYmd=20200126}

			Long worktimeCloseId = Long.parseLong(paramMap.get("worktimeCloseId").toString());

			HashMap<String, Object> reqMap = new HashMap<>();
			reqMap.put("tenantId", tenantId );
			reqMap.put("enterCd", enterCd );
			reqMap.put("worktimeCloseId", worktimeCloseId);
			reqMap.put("sYmd", (String)paramMap.get("sYmd") );
			reqMap.put("eYmd", (String)paramMap.get("eYmd") );
			reqMap.put("sabun", (String)paramMap.get("sabun") );

			//wtmInterfaceService.setCloseWorkIf(reqMap); //근무시간 마감생성 자바루프용 호출
			//rp =
			rp = wtmInterfaceService.setCloseWorkIfN(reqMap); //근무시간 마감생성 자바루프용 호출

		} catch(Exception e) {
			e.printStackTrace();
		}
		return rp;
	}
	
	@RequestMapping (value="/intf/allWorkTimeCheck", method=RequestMethod.POST)
	public @ResponseBody ReturnParam allWorkTimeCheck(HttpServletRequest request)throws Exception{

		System.out.println("/intf/allWorkTimeCheck : requestParam type");
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		Map<String, Object> paramMap = new HashMap();
		List<Map<String, Object>> allWorkTimeList = new ArrayList();
		logger.debug("/intf/allWorkTimeCheck s " + WtmUtil.paramToString(request));
		try {
			
			String apiKey = request.getParameter("apiKey");
			String secret = request.getParameter("secret");

			CommTenantModule tm = tenantModuleRepo.findByApiKey(apiKey);
      
			if(tm == null) {
				rp.setFail("apiKey 불일치");
				return rp;
			}

			String encryptCode = tm.getTenantKey().toString();
			if(encryptCode.length() < 12) {
				encryptCode = String.format("%12s", encryptCode).replaceAll(" ", "o");
			}
			String s = Sha256.getHash(secret, encryptCode, 10);
			if(!s.equals(tm.getSecret()))
			{
				rp.setFail("secret 불일치");
				return rp;
			}
			
			Long tenantId = tm.getTenantId();
			String enterCd = request.getParameter("enterCd").toString();
			String sabun = "";
			if(request.getParameter("sabun") != null) {
				sabun = request.getParameter("sabun").toString();
			}
			String symd = request.getParameter("symd").toString();
			String eymd = request.getParameter("eymd").toString();
			
			paramMap.put("tenantId", tm.getTenantId());
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("symd", symd);
			paramMap.put("eymd", eymd);
			
			allWorkTimeList =  wtmInterfaceService.allWorkTimeCheck(tenantId, enterCd, sabun, symd, eymd);
			rp.put("DATA", allWorkTimeList);
			rp.put("message", "");
			
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}

}