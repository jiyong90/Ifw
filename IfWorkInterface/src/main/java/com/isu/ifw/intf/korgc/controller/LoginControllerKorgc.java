package com.isu.ifw.intf.korgc.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.intf.mapper.WtmIntfMapper;
import com.isu.ifw.intf.service.ExchangeService;
import com.isu.ifw.intf.service.GreenMobileApiService;
import com.isu.ifw.intf.vo.ReturnParam;

import oracle.net.aso.d;

@RestController
public class LoginControllerKorgc {
	
	
/*
	@Value("${ifw.status-get}")
	private String statusUrl;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired private RestTemplate restTemplate;
*/
	
	@Autowired
	private WtmIntfMapper intfMapper;
	
	@Autowired
	private GreenMobileApiService service;
	
	
	@RequestMapping(value = "/certificate/korgc" , method = RequestMethod.POST)
	public ReturnParam korgcLogin(@RequestBody Map<String, Object> paramMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		

		String userId = paramMap.get("loginUserId")+"";
		String userPw = paramMap.get("loginPassword")+"";
		String enterCd = paramMap.get("loginEnterCd")+"";
		ObjectMapper mapper = new ObjectMapper();
		/*
		 * {"password":"1","grant_type":"password","loginPassword":"1","loginUserId":"master","redirect_uri":"https://cloudhr.pearbranch.com/ifw/login/korgc/authorize","loginEnterCd":"KORGC","username":"korgc@KORGC@master"}
		 */
		System.out.println(mapper.writeValueAsString(paramMap));
		
		/*
		{Tables=[
			{TableName=DataBlock1, 
			Columns=
				[
					{ColumnName=IsApproval, ColumnType=string}, {ColumnName=UserID, ColumnType=string}], 
			Rows=
				[{IsApproval=1, UserID=master}]
			}
		]}`
		{Tables=[{TableName=DataBlock1
		, Columns=[{ColumnName=IsApproval, ColumnType=string}, {ColumnName=UserID, ColumnType=string}]
		, Rows=[{IsApproval=1, UserID=master}]}]}
		*/
		String encodePw = URLEncoder.encode(userPw,"UTF-8");
		Map<String, Object> resMap = ksystemLogin(userId, enterCd, encodePw);
		
		if(resMap.containsKey("Tables") && resMap.get("Tables") != null) {
			List<Map<String, Object>> tables = (List<Map<String, Object>>) resMap.get("Tables");
			if(tables.size() > 0) {
				Map<String, Object> table = tables.get(0);
				if(table != null && table.containsKey("Rows") && table.get("Rows") != null) {
					List<Map<String, Object>> rows = (List<Map<String, Object>>) table.get("Rows");
					if(rows.size() > 0) {
						Map<String, Object> row = rows.get(0);
						if(row.containsKey("UserID") && row.get("UserID") != null && (row.get("UserID")+"").equalsIgnoreCase(userId)
								&& row.containsKey("IsApproval") && row.get("IsApproval") != null && (row.get("IsApproval")+"").equalsIgnoreCase("1")
										&& row.containsKey("EmpID") && row.get("EmpID") != null
								) {
							//rp.putAll(resMap);
							//"username":"korgc@KORGC@master"
							String username = "korgc@"+enterCd+"@"+row.get("EmpID") ;
							rp.put("username",username);
							
							System.out.println(mapper.writeValueAsString(rp));
							return rp;
						}
					}
				}
			}
		}
		
		return null;

    }

	@RequestMapping(value = "/korgc/employee/image", method = RequestMethod.GET, produces = "text/plain")
	public @ResponseBody String getImage(HttpServletRequest request,
			@RequestParam(value = "locale", required = false) String locale,
			@RequestParam(value = "empKey", required = true) String empKey) throws Exception {
		return service.greenEmpPhotoOut(locale, empKey);
	}
	
	@RequestMapping(value = "/korgc/certificate/request" , method = RequestMethod.POST)
	public ReturnParam korgcLoginForMobile(@RequestBody Map<String, Object> paramMap, HttpServletRequest req, HttpServletResponse res) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setMessage("");
		

		String userId = paramMap.get("loginUserId")+"";
		String userPw = paramMap.get("loginPassword")+"";
		String enterCd = paramMap.get("loginEnterCd")+"";
		ObjectMapper mapper = new ObjectMapper();
		/*
		 * {"password":"1","grant_type":"password","loginPassword":"1","loginUserId":"master","redirect_uri":"https://cloudhr.pearbranch.com/ifw/login/korgc/authorize","loginEnterCd":"KORGC","username":"korgc@KORGC@master"}
		 */
		System.out.println("#########################/m/certificate/request s " + mapper.writeValueAsString(paramMap));
		
		/*
		{Tables=[
			{TableName=DataBlock1, 
			Columns=
				[
					{ColumnName=IsApproval, ColumnType=string}, {ColumnName=UserID, ColumnType=string}], 
			Rows=
				[{IsApproval=1, UserID=master}]
			}
		]}`
		{Tables=[{TableName=DataBlock1
		, Columns=[{ColumnName=IsApproval, ColumnType=string}, {ColumnName=UserID, ColumnType=string}]
		, Rows=[{IsApproval=1, UserID=master}]}]}
		*/
		String encodePw = URLEncoder.encode(userPw,"UTF-8");
		System.out.println("encodePw : " + encodePw);
		Map<String, Object> resMap = ksystemLogin(userId, enterCd, encodePw);
		
		if(resMap.containsKey("Tables") && resMap.get("Tables") != null) {
			List<Map<String, Object>> tables = (List<Map<String, Object>>) resMap.get("Tables");
			if(tables.size() > 0) {
				Map<String, Object> table = tables.get(0);
				if(table != null && table.containsKey("Rows") && table.get("Rows") != null) {
					List<Map<String, Object>> rows = (List<Map<String, Object>>) table.get("Rows");
					if(rows.size() > 0) {
						Map<String, Object> row = rows.get(0);
						if(row.containsKey("UserID") && row.get("UserID") != null && (row.get("UserID")+"").equalsIgnoreCase(userId)
								&& row.containsKey("IsApproval") && row.get("IsApproval") != null && (row.get("IsApproval")+"").equalsIgnoreCase("1")
										&& row.containsKey("EmpID") && row.get("EmpID") != null
								) {
							//rp.putAll(resMap);
							//"username":"korgc@KORGC@master"
							Map<String, Object> pMap = new HashMap<String, Object>();
							String sabun = row.get("EmpID")+"";
							String username = "korgc@"+enterCd+"@"+sabun ;
							pMap.put("empKey", enterCd+"@"+ sabun);
							
							String accessToken = service.createAccessToken(enterCd, sabun);
							pMap.put("EmpID", row.get("EmpID"));		
							
							Map<String, Object> sessionData = intfMapper.getWtmEmpByEmpID(pMap);
							sessionData.put("userId", userId);
							sessionData.put("accessToken", accessToken);
							pMap.put("sessionData", sessionData);
							rp.put("result", pMap);
							System.out.println("#########################/m/certificate/request " + rp.toString());
							return rp;
						}
					}
				}
			}
		}
		
		return null;

    }
	
	/**
	 * 개인 유효성 체크(시스템 로그인 가능 여부 판단)
	 *
	 * @param request
	 * @param locale
	 *            (필수) 어휘 코드 (ko_KR, en_US....)
	 * @param empKey
	 *            (필수) 직원 고유키
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/korgc/certificate/valid", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam greenValidEmp(HttpServletRequest request,
			@RequestParam(value = "locale", required = false) String locale,
			@RequestParam(value = "empKey", required = false) String empKey,
			@RequestParam(value = "accessToken", required = false) String accessToken) throws Exception {
		
		String data = "";
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()){
		    String name = (String)params.nextElement();
		    data += name+":"+request.getParameter(name)+",";
		}
		
		System.out.println("#########################/m/certificate/valid " + data);
		System.out.println("empKey : " + empKey);
		return service.greenValidEmp(locale, empKey, accessToken);
	}
	
	@RequestMapping(value = "/korgc/emp/rest", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam greenRestCnt(HttpServletRequest request,
			@RequestParam(value = "locale", required = false) String locale,
			@RequestParam(value = "sabun", required = false) String sabun,
			@RequestParam(value = "enterCd", required = false) String enterCd) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		rp.put("Message", "");
		//ObjectMapper mapper = new ObjectMapper();
		
		
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("EmpID", sabun);		
		pMap.put("creCnt", "-");
		pMap.put("usedCnt", "-");
		pMap.put("restCnt", "-");
		
		
		Map<String, Object> sessionDataMap = intfMapper.getWtmEmpByEmpID(pMap);
		 
		//ReturnParam rp = service.greenValidEmp(locale, empKey, accessToken);
		String userId = sessionDataMap.get("USER_ID").toString();
		 
		
		Map<String, Object> resMap = this.ksystemRest(userId, sabun);;

		if(resMap.containsKey("Tables") && resMap.get("Tables") != null) {
			List<Map<String, Object>> tables = (List<Map<String, Object>>) resMap.get("Tables");
			if(tables.size() > 0) {
				Map<String, Object> table = tables.get(0);
				if(table != null && table.containsKey("Rows") && table.get("Rows") != null) {
					List<Map<String, Object>> rows = (List<Map<String, Object>>) table.get("Rows");
					if(rows.size() > 0) {
						Map<String, Object> row = rows.get(0);
						if(row.containsKey("EmpID") && row.get("EmpID") != null) {
							//rp.putAll(resMap);
							//"username":"korgc@KORGC@master"
							Double useCnt = Double.parseDouble(row.get("UseCnt")+"");
							Double usedCnt = Double.parseDouble(row.get("UsedCnt")+"");
							Double restCnt = Double.parseDouble(row.get("RestCnt")+"");
							 
							pMap.put("creCnt", useCnt);
							pMap.put("usedCnt", usedCnt);
							pMap.put("restCnt", restCnt);
							
							System.out.println("#########################/rest " + rp.toString());
//							return rp;
						}
					}
				}
			}
		}
		rp.put("DATA", pMap);
//		rp.setFail("");
		return rp;
	}
	
	
	
	
	private Map<String, Object> ksystemRest(String userId, String empId){
		
		String url = "http://localhost/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?";
		ObjectMapper mapper = new ObjectMapper();
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
		String securityStr = "certKey:0F8555BE-34EC-4B6D-82E6-DAE94AE012DF,"
				+ "userPwd:,"
				+ "methodId:YYQuery,"
				+ "certId:YYQuery,"
				+ "dsnOper:kpxerp_oper,"
				+ "userId:"+userId+","
				+ "languageSeq:1,"
				+ "isDebug:0,"
				+ "workingTag:,"
				+ "hostIPAddress:,"
				+ "securityType:0,"
				+ "companySeq:1,"
				+ "serviceId:AngKor.KPXERP.PR.DatWkYyMmListAPI_kpxerp,"
				+ "hostComputername:,"
				+ "dsn:kpxerp_oper" 
				;
		//System.out.println("secMap : " + secMap.toString());
		//System.out.println("secMap : " + mapper.writeValueAsString(secMap));
		//String secStr = mapper.writeValueAsString(secMap);
		//secStr = secStr.replaceAll("\"", "");
		//System.out.println(secStr);
		url += "securityJson=" + securityStr; //URLEncoder.encode(secStr); //mapper.writeValueAsString(secMap);
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
		String dStr = "{\"ROOT\":{\"DataBlock1\":[{\"EmpID\":\""+empId+"\"}]}}";
		url += "&dataJson=" + dStr; //URLEncoder.encode(rStr); 
		
		url += "&encryptionType=0";
		url += "&timeOut=30";
		
		System.out.println("url : " + url);
		
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
		try {
			URL obj = new URL(url);
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
			return mapper.readValue(r, new HashMap().getClass());
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	private Map<String, Object>  ksystemLogin(String userId, String enterCd, String userPw){
		String loginUrl = "http://localhost/Angkor.Ylw.Common.HttpExecute/RestOutsideService.svc/GetServiceMethodJson?";
		ObjectMapper mapper = new ObjectMapper();
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
				+ "userId:"+userId+","
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
		String dStr = "{\"ROOT\":{\"DataBlock1\":[{\"UserPwd\":\""+userPw+"\",\"UserID\":\""+userId+"\"}]}}";
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
		try {
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
			return mapper.readValue(r, new HashMap().getClass());
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	 
}
