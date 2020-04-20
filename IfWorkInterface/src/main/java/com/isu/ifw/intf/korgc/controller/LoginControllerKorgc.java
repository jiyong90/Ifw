package com.isu.ifw.intf.korgc.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.intf.service.ExchangeService;
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
		String loginUrl = "http://localhost/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?";
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
//		Map<String, Object> secMap = new HashMap<String, Object>();
//		secMap.put("certKey", "DA949252-7918-46C1-9C9F-D2B60E524A19");
//		secMap.put("userPwd", "");
//		secMap.put("methodId", "LoginPwdCheck_kpxerp");
//		secMap.put("certId", "PWD_CHECK");
//		secMap.put("dsnOper", "kpxerp_oper");
//		secMap.put("userId", paramMap.get("loginUserId"));
//		secMap.put("languageSeq", "1");
//		secMap.put("isDebug", "0");
//		secMap.put("workingTag", "");
//		secMap.put("hostIPAddress", "");
//		secMap.put("securityType", "0");
//		secMap.put("companySeq", "1");
//		secMap.put("serviceId", "IsStoredProcedure");
//		secMap.put("hostComputername", "");
//		secMap.put("dsn", "kpxerp_oper");
		String securityStr = "certKey:DA949252-7918-46C1-9C9F-D2B60E524A19,"
				+ "userPwd:,"
				+ "methodId:LoginPwdCheck_kpxerp,"
				+ "certId:PWD_CHECK,"
				+ "dsnOper:kpxerp_oper,"
				+ "userId:"+paramMap.get("loginUserId")+","
				+ "languageSeq:1,"
				+ "isDebug:0,"
				+ "workingTag:,"
				+ "hostIPAddress:,"
				+ "securityType:0,"
				+ "companySeq:1,"
				+ "serviceId:IsStoredProcedure,"
				+ "hostComputername:,"
				+ "dsn:kpxerp_oper" 
				;
		//System.out.println("secMap : " + secMap.toString());
		//System.out.println("secMap : " + mapper.writeValueAsString(secMap));
		//String secStr = mapper.writeValueAsString(secMap);
		//secStr = secStr.replaceAll("\"", "");
		//System.out.println(secStr);
		loginUrl += "securityJson=" + securityStr; //URLEncoder.encode(secStr); //mapper.writeValueAsString(secMap);
		/*
		 * {"ROOT": {"DataBlock1": [{"UserID":"Master","UserPwd":"1234"}]}
		 * &dataJson={
		 * "ROOT":
		 * {"DataBlock1" : 
		 * [{
		 * "UserPwd":"@ini2093","UserID":"Master"}]}}&encryptionType=0&timeOut=30
		 */
//		Map<String, Object> rMap = new HashMap<String, Object>();
//		Map<String, Object> dbMap = new HashMap<String, Object>();
//		List<Map<String, Object>> dbs = new ArrayList<Map<String,Object>>();
//		Map<String, Object> uMap = new HashMap<String, Object>();
//		uMap.put("UserID" , paramMap.get("loginUserId"));
//		uMap.put("UserPwd" , paramMap.get("loginPassword"));
//		dbs.add(uMap);
//		dbMap.put("DataBlock1", dbs);
//		rMap.put("ROOT", dbMap);
//		
//		String rStr = mapper.writeValueAsString(rMap);
//		//rStr = rStr.replaceAll("\"", "");
//		System.out.println(rStr);
		
		
		// 공백 있으면 안된다.. url encode 도 쓰면 안된다..
		String dStr = "{\"ROOT\":{\"DataBlock1\":[{\"UserPwd\":\""+paramMap.get("loginPassword")+"\",\"UserID\":\""+paramMap.get("loginUserId")+"\"}]}}";
		loginUrl += "&dataJson=" + dStr; //URLEncoder.encode(rStr); 
		
		loginUrl += "&encryptionType=0";
		loginUrl += "&timeOut=30";
		
		System.out.println("loginUrl : " + loginUrl);
		
		//키	값
		/*
		 * GET /Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson
		 * ?securityJson=certKey:DA949252-7918-46C1-9C9F-D2B60E524A19,userPwd:,methodId:LoginPwdCheck_kpxerp,certId:PWD_CHECK,dsnOper:kpxerp_oper,userId:Master,languageSeq:1,isDebug:0,workingTag:,hostIPAddress:,securityType:0,companySeq:1,serviceId:IsStoredProcedure,hostComputername:,dsn:kpxerp_oper&dataJson={"ROOT":{"DataBlock1":[{"UserPwd":"@ini2093","UserID":"Master"}]}}&encryptionType=0&timeOut=30 
		 */
		/**
		 * { Tables : [
		 * 	{ TableName :
		 * 	  Columns : []
		 * 	  Rows : [{ IsApproval : 1, UserId : master }]
		 *  } 
		 * ]} 
		 */
		
		/*
http://localhost/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?securityJson=certKey:DA949252-7918-46C1-9C9F-D2B60E524A19,userPwd:,methodId:LoginPwdCheck_kpxerp,certId:PWD_CHECK,dsnOper:kpxerp_oper,userId:Master,languageSeq:1,isDebug:0,workingTag:,hostIPAddress:,securityType:0,companySeq:1,serviceId:IsStoredProcedure,hostComputername:,dsn:kpxerp_oper&dataJson={"ROOT": {"DataBlock1": [{"UserPwd":"@ini2093","UserID":"Master"}]}}&encryptionType=0&timeOut=30
http://localhost/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?securityJson=certKey:DA949252-7918-46C1-9C9F-D2B60E524A19,userPwd:,methodId:LoginPwdCheck_kpxerp,certId:PWD_CHECK,dsnOper:kpxerp_oper,userId:Master,languageSeq:1,isDebug:0,workingTag:,hostIPAddress:,securityType:0,companySeq:1,serviceId:IsStoredProcedure,hostComputername:,dsn:kpxerp_oper&dataJson={"ROOT":{"DataBlock1":[{"UserPwd":"@ini2093","UserID":"Master"}]}}&encryptionType=0&timeOut=30 
		*/
		//System.out.println(loginUrl);
		//System.out.println("http://localhost/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?securityJson=certKey:DA949252-7918-46C1-9C9F-D2B60E524A19,userPwd:,methodId:LoginPwdCheck_kpxerp,certId:PWD_CHECK,dsnOper:kpxerp_oper,userId:Master,languageSeq:1,isDebug:0,workingTag:,hostIPAddress:,securityType:0,companySeq:1,serviceId:IsStoredProcedure,hostComputername:,dsn:kpxerp_oper&dataJson={\"ROOT\":{\"DataBlock1\":[{\"UserPwd\":\"@ini2093\",\"UserID\":\"Master\"}]}}&encryptionType=0&timeOut=30");

		URL obj = new URL(loginUrl);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("====================================");
        System.out.println(response.toString()); //결과, json결과를 parser하여 처리
        System.out.println("====================================");
         		
		String r = mapper.readValue(response.toString(), String.class);
		rp.setSuccess("");
		Map<String, Object> resMap = mapper.readValue(r, new HashMap().getClass());
		System.out.println(resMap);
		
		//{Tables=[{TableName=DataBlock1, Columns=[{ColumnName=IsApproval, ColumnType=string}, {ColumnName=UserID, ColumnType=string}], Rows=[{IsApproval=1, UserID=master}]}]}`
		rp.putAll(resMap);
		return rp;

    }
	 
}
