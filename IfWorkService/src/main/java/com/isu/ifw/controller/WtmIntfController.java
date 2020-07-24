package com.isu.ifw.controller;


import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.isu.ifw.TenantSecuredControl;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.service.WtmValidatorService;
import com.isu.ifw.util.Sha256;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


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

			CommTenantModule tm = tenantModuleRepo.findByApiKey(apiKey);
      
			if(tm == null) {
				rp.setFail("secret 불일치");
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
			paramMap.put("inoutDate", request.getParameter("time"));
			paramMap.put("inoutType", request.getParameter("type"));
			paramMap.put("entryNote", request.getParameter("deviceKey"));
			paramMap.put("entryType", "INTF");
         
			logger.debug("getParameter s2 " + request.getParameter("deviceKey") + " " + request.getParameter("emp") + " " + request.getParameter("time") + " " + request.getParameter("type"));
      
			inoutService.updateTimecard2(paramMap);
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

			CommTenantModule tm = tenantModuleRepo.findByApiKey(apiKey);
			logger.debug("tm " + tm.toString());
			if(tm == null) {
				rp.setFail("secret 불일치");
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
			
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tm.getTenantId());
			paramMap.put("enterCd", params.get("enterCd").toString());
			paramMap.put("sabun", params.get("emp"));
			paramMap.put("inoutDate", params.get("time"));
			paramMap.put("inoutType", params.get("type"));
			paramMap.put("entryNote", params.get("deviceKey"));
			paramMap.put("entryType", "INTF");
			
			logger.debug("getParameter s2 " + params.get("deviceKey") + " " + params.get("emp") + " " + params.get("time") + " " + params.get("type"));
		
			inoutService.updateTimecard2(paramMap);
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
			
			rp = validatorService.worktimeValid(tenantId, paramMap.get("enterCd").toString(), paramMap.get("applNo").toString(), works, applSabun);
			if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
				interfaceService.setTaaApplArrIf(paramMap);
			}
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
}