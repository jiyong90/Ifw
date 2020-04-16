package com.isu.ifw.intf.korgc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.intf.service.ExchangeService;
import com.isu.ifw.intf.util.Aes256;
import com.isu.ifw.intf.util.WtmUtil;
import com.isu.ifw.intf.vo.ReturnParam;

@RestController
public class LoginControllerKorgc {
	
	

	@Value("${ifw.status-get}")
	private String statusUrl;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired private RestTemplate restTemplate;
	
	@RequestMapping(value = "/certificate/korgc" , method = RequestMethod.POST)
	public ReturnParam korgcLogin(@RequestBody Map<String, Object> paramMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		ReturnParam rp = new ReturnParam();
		String loginUrl = "http://211.34.1.10/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?";
		ObjectMapper mapper = new ObjectMapper();
		/*
		 * {"password":"1","grant_type":"password","loginPassword":"1","loginUserId":"master","redirect_uri":"https://cloudhr.pearbranch.com/ifw/login/korgc/authorize","loginEnterCd":"KORGC","username":"korgc@KORGC@master"}
		 */
		System.out.println(mapper.writeValueAsString(paramMap));
		/*
		 * http://211.34.1.10/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson
		 * ?securityjson={
		 * certKey : DA949252-7918-46C1-9C9F-D2B60E524A19
		 * ,userPwd:
		 * ,methodId : LoginPwdCheck_kpxerp
		 * ,certId : PWD_CHECK
		 * ,dsnOper : kpxerp_oper
		 * ,userId : Master
		 * ,languageSeq : 1
		 * ,isDebug : 0
		 * ,workingTag :
		 * ,hostIPAddress :
		 * ,securityType : 0
		 * ,companySeq : 1
		 * ,serviceId : IsStoredProcedure
		 * ,hostComputername :
		 * ,dsn : kpxerp_oper
		 * }
		 */
		Map<String, Object> secMap = new HashMap<String, Object>();
		secMap.put("certKey", "DA949252-7918-46C1-9C9F-D2B60E524A19");
		secMap.put("userPwd", "");
		secMap.put("methodId", "LoginPwdCheck_kpxerp");
		secMap.put("certId", "PWD_CHECK");
		secMap.put("dsnOper", "kpxerp_oper");
		secMap.put("userId", paramMap.get("loginUserId"));
		secMap.put("languageSeq", "1");
		secMap.put("isDebug", "0");
		secMap.put("workingTag", "");
		secMap.put("hostIPAddress", "");
		secMap.put("securityType", "0");
		secMap.put("companySeq", "1");
		secMap.put("serviceId", "IsStoredProcedure");
		secMap.put("hostComputername", "");
		secMap.put("dsn", "kpxerp_oper");
		
		System.out.println("secMap : " + secMap.toString());
		System.out.println("secMap : " + mapper.writeValueAsString(secMap));
		String secStr = mapper.writeValueAsString(secMap);
		secStr = secStr.replaceAll("\"", "");
		System.out.println(secStr);
		loginUrl += "securityjson=" + secStr; //mapper.writeValueAsString(secMap);
		/*
		 * {"ROOT": {"DataBlock1": [{"UserID":"Master","UserPwd":"1234"}]}
		 * &dataJson={
		 * "ROOT":
		 * {"DataBlock1" : 
		 * [{
		 * "UserPwd":"@ini2093","UserID":"Master"}]}}&encryptionType=0&timeOut=30
		 */
		Map<String, Object> rMap = new HashMap<String, Object>();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		List<Map<String, Object>> dbs = new ArrayList<Map<String,Object>>();
		Map<String, Object> uMap = new HashMap<String, Object>();
		uMap.put("UserID" , paramMap.get("loginUserId"));
		uMap.put("UserPwd" , paramMap.get("loginPassword"));
		dbs.add(uMap);
		dbMap.put("DataBlock1", dbs);
		rMap.put("ROOT", dbMap);
		
		loginUrl += "&dataJson=" + mapper.writeValueAsString(rMap);
		
		loginUrl += "&encryptionType=0";
		loginUrl += "&timeOut=30";
		
		System.out.println("loginUrl : " + loginUrl);
		
		
		/**
		 * { Tables : [
		 * 	{ TableName :
		 * 	  Columns : []
		 * 	  Rows : [{ IsApproval : 1, UserId : master }]
		 *  } 
		 * ]} 
		 */
		Map<String, Object> responseMap = restTemplate.getForEntity(loginUrl, Map.class).getBody();
		
		System.out.println("responseMap : " + mapper.writeValueAsString(responseMap));
		rp.setSuccess("");
		rp.putAll(responseMap);
		return rp;

    }
	 
}
