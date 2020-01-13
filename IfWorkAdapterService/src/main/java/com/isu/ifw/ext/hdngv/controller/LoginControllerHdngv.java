package com.isu.ifw.ext.hdngv.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.tempuri.SitemapWSSoap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.ExchangeService;
import com.isu.ifw.util.Aes256;
import com.isu.ifw.util.CookieUtil;
import com.isu.ifw.util.WtmUtil;

@RestController
public class LoginControllerHdngv {
	
	

	@Value("${ifw.status-get}")
	private String statusUrl;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@RequestMapping(value = "/certificate/hdngv" , method = RequestMethod.POST)
	public ModelAndView autowayLogin(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("inout");

		/* @SuppressWarnings("unchecked")
		Enumeration<String> params = req.getParameterNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			if(req.getParameter(paramName) != null) {
				System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName).toString());
			} else {
				System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName));
			}
		} */
		mv.addObject("tsId", "hdngv"); 
		
		String strEncID = req.getParameter("HKMCENC_ID") == null ? "" : req.getParameter("HKMCENC_ID");
		String strCompanyCode = req.getParameter("CompanyCode") == null ? "" : req.getParameter("CompanyCode");
		String strEncText = req.getParameter("Encode") == null ? "" : req.getParameter("Encode");
		
		/*
		String strEncID = paramMap.get("HKMCENC_ID") == null ? "" : paramMap.get("HKMCENC_ID") ;
		String strCompanyCode = paramMap.get("CompanyCode") == null ? "" : paramMap.get("CompanyCode") ;
		String strEncText = paramMap.get("Encode") == null ? "" : paramMap.get("Encode") ;
		*/
		
		String resultMsg = "";
		System.out.println("HKMCENC_ID = " + strEncID);
		System.out.println("CompanyCode = " + strCompanyCode);
		System.out.println("Encode = " + strEncText);
		ObjectMapper mapper = new ObjectMapper();
		String url = statusUrl.replace("{{tsId}}", "hdngv");
		try
		{
			if(!"".equals(strEncText))
			{
				SitemapWSSoap SitemapWSSoap = (new org.tempuri.SitemapWSLocator()).getSitemapWSSoap();
				String decrptStr = SitemapWSSoap.getPlainText(strEncID, strCompanyCode, strEncText);
				//String decrptStr = "Emp_ID||2008856___email||askldjlks";
				HashMap<String, String> hashMap = new HashMap<String, String>();
				System.out.println("decrptStr=   "+decrptStr);
				
				
				String decrptVal1[] = decrptStr.split("___");
				
				// Email||1111111@dev-hyundai-ngv.com___User_ID||H133001111111___IP||10.206.34.254___Emp_ID||1111111___ssoTIME||20191127191810
				System.out.println("decrptVal1 :: " + decrptVal1);
				for(int i=0; i<decrptVal1.length; i++)
				{
					String tmp[]=decrptVal1[i].split("\\|\\|");
					hashMap.put(tmp[0], tmp[1]);
				}
				
				

				Cookie[] cookies = req.getCookies();
				if (cookies != null) {
					
					for (Cookie cookie : cookies) {
						CookieUtil.clear(res, cookie.getName());
					}
						 
				}
   			 
				Cookie c = new Cookie("sessiondata_____"+strCompanyCode,decrptStr);
				c.setPath("/");
				c.setMaxAge(60*60*24); //하루
				res.addCookie(c);
				
				System.out.println("resultMap : " + mapper.writeValueAsString(hashMap));
				String empId = hashMap.get("Emp_ID")+"";
				Map<String, Object> paramMap = new HashMap<>();
				//paramMap.put("empKey", strCompanyCode + "@" + empId);
				
				//empId = "2008856";
				
				paramMap.put("enterCd", strCompanyCode);
				paramMap.put("sabun", empId);
				
				
				mv.addObject("enterCd", strCompanyCode);
				mv.addObject("sabun", empId);
				
				Map<String, Object> resMap = exchangeService.exchange(url, HttpMethod.GET, null, paramMap);
				
				Map<String, Object> rMap = ((Map<String, Object>) resMap.get("result"));
//				if(!rMap.containsKey("ymd") || !rMap.containsKey("entrySdate") || !rMap.containsKey("entryEdate") || !rMap.containsKey("label") || !rMap.containsKey("desc") || !rMap.containsKey("inoutType")) {
//					
//				}else {
//					
//				}
//				
				for(String kk : rMap.keySet()) {
					System.out.println(kk + " : " + rMap.get(kk));
					mv.addObject(kk, (rMap.get(kk)==null)?"":rMap.get(kk));
				}
				System.out.println(mapper.writeValueAsString(rMap));
				
//				mv.addAllObjects((Map<String, ?>) resMap.get("result"));
				
				System.out.println(mapper.writeValueAsString(resMap));
						
			} else {
				mv.addObject("message", "Encode is empty.");
				mv.setViewName("error");
				 
			}
//			
//
//			Map<String, Object> paramMap = new HashMap<>();
//			paramMap.put("enterCd", "HDNGV");
//			paramMap.put("sabun", "2008856");
//			Map<String, Object> resMap = exchangeService.exchange(url, HttpMethod.GET, null, paramMap);
//
//			mv.addObject("enterCd", "HDNGV");
//			mv.addObject("sabun", "2008856");
//			
//			Map<String, Object> rMap = ((Map<String, Object>) resMap.get("result"));
//			if(!rMap.containsKey("ymd") || !rMap.containsKey("entrySdate") || !rMap.containsKey("entryEdate") || !rMap.containsKey("label") || !rMap.containsKey("desc") || !rMap.containsKey("inoutType")) {
//				
//			}else {
//				
//			}
//			
//			for(String kk : rMap.keySet()) {
//				System.out.println(kk + " : " + rMap.get(kk));
//				mv.addObject(kk, (rMap.get(kk)==null)?"":rMap.get(kk));
//			}
//			System.out.println(mapper.writeValueAsString(rMap));
//
//			mv.setViewName("inout");
		} 
		catch (Exception e)
		{
			System.out.println(e.toString());
			resultMsg = e.toString();
			mv.addObject("message", resultMsg);
			
			mv.setViewName("error");
			return mv;
			
		}

		mv.addObject("result", resultMsg);
		//res.sendRedirect(req.getContextPath() + "/i?msg=" + resultMsg);
		return mv;

    }
	
	@RequestMapping(value = "/certificate/hdngv" , method = RequestMethod.GET)
	public ModelAndView autowayLoginGet(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("inout");

		/* @SuppressWarnings("unchecked")
		Enumeration<String> params = req.getParameterNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			if(req.getParameter(paramName) != null) {
				System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName).toString());
			} else {
				System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName));
			}
		} */
		mv.addObject("tsId", "hdngv"); 
		
		String strEncID = req.getParameter("HKMCENC_ID") == null ? "" : req.getParameter("HKMCENC_ID");
		String strCompanyCode = req.getParameter("CompanyCode") == null ? "" : req.getParameter("CompanyCode");
		String strEncText = req.getParameter("Encode") == null ? "" : req.getParameter("Encode");
		
		/*
		String strEncID = paramMap.get("HKMCENC_ID") == null ? "" : paramMap.get("HKMCENC_ID") ;
		String strCompanyCode = paramMap.get("CompanyCode") == null ? "" : paramMap.get("CompanyCode") ;
		String strEncText = paramMap.get("Encode") == null ? "" : paramMap.get("Encode") ;
		*/
		
		String resultMsg = "";
		System.out.println("HKMCENC_ID = " + strEncID);
		System.out.println("CompanyCode = " + strCompanyCode);
		System.out.println("Encode = " + strEncText);
		ObjectMapper mapper = new ObjectMapper();
		String url = statusUrl.replace("{{tsId}}", "hdngv");
		try
		{
			if(!"".equals(strEncText))
			{
				//SitemapWSSoap SitemapWSSoap = (new org.tempuri.SitemapWSLocator()).getSitemapWSSoap();
				//String decrptStr = SitemapWSSoap.getPlainText(strEncID, strCompanyCode, strEncText);
				String decrptStr = "Emp_ID||2008856___email||askldjlks";
				HashMap<String, String> hashMap = new HashMap<String, String>();
				System.out.println("decrptStr=   "+decrptStr);
				
				
				String decrptVal1[] = decrptStr.split("___");
				
				// Email||1111111@dev-hyundai-ngv.com___User_ID||H133001111111___IP||10.206.34.254___Emp_ID||1111111___ssoTIME||20191127191810
				System.out.println("decrptVal1 :: " + decrptVal1);
				for(int i=0; i<decrptVal1.length; i++)
				{
					String tmp[]=decrptVal1[i].split("\\|\\|");
					hashMap.put(tmp[0], tmp[1]);
				}
				
				Cookie c = new Cookie("sessiondata_____"+strCompanyCode,decrptStr);
				c.setPath("/");
				c.setMaxAge(60*60*24); //하루
				res.addCookie(c);
				
				System.out.println("resultMap : " + mapper.writeValueAsString(hashMap));
				String empId = hashMap.get("Emp_ID")+"";
				Map<String, Object> paramMap = new HashMap<>();
				//paramMap.put("empKey", strCompanyCode + "@" + empId);
				
				//empId = "2008856";
				
				paramMap.put("enterCd", strCompanyCode);
				paramMap.put("sabun", empId);
				
				
				mv.addObject("enterCd", strCompanyCode);
				mv.addObject("sabun", empId);
				
				Map<String, Object> resMap = exchangeService.exchange(url, HttpMethod.GET, null, paramMap);
				
				Map<String, Object> rMap = ((Map<String, Object>) resMap.get("result"));
//				if(!rMap.containsKey("ymd") || !rMap.containsKey("entrySdate") || !rMap.containsKey("entryEdate") || !rMap.containsKey("label") || !rMap.containsKey("desc") || !rMap.containsKey("inoutType")) {
//					
//				}else {
//					
//				}
//				
				for(String kk : rMap.keySet()) {
					System.out.println(kk + " : " + rMap.get(kk));
					mv.addObject(kk, (rMap.get(kk)==null)?"":rMap.get(kk));
				}
				System.out.println(mapper.writeValueAsString(rMap));
				
//				mv.addAllObjects((Map<String, ?>) resMap.get("result"));
				
				System.out.println(mapper.writeValueAsString(resMap));
						
			} else {
				mv.addObject("message", "Encode is empty.");
				mv.setViewName("error");
				 
			}
//			
//
//			Map<String, Object> paramMap = new HashMap<>();
//			paramMap.put("enterCd", "HDNGV");
//			paramMap.put("sabun", "2008856");
//			Map<String, Object> resMap = exchangeService.exchange(url, HttpMethod.GET, null, paramMap);
//
//			mv.addObject("enterCd", "HDNGV");
//			mv.addObject("sabun", "2008856");
//			
//			Map<String, Object> rMap = ((Map<String, Object>) resMap.get("result"));
//			if(!rMap.containsKey("ymd") || !rMap.containsKey("entrySdate") || !rMap.containsKey("entryEdate") || !rMap.containsKey("label") || !rMap.containsKey("desc") || !rMap.containsKey("inoutType")) {
//				
//			}else {
//				
//			}
//			
//			for(String kk : rMap.keySet()) {
//				System.out.println(kk + " : " + rMap.get(kk));
//				mv.addObject(kk, (rMap.get(kk)==null)?"":rMap.get(kk));
//			}
//			System.out.println(mapper.writeValueAsString(rMap));
//
//			mv.setViewName("inout");
		} 
		catch (Exception e)
		{
			System.out.println(e.toString());
			resultMsg = e.toString();
			mv.addObject("message", resultMsg);
			
			mv.setViewName("error");
			return mv;
			
		}

		mv.addObject("result", resultMsg);
		//res.sendRedirect(req.getContextPath() + "/i?msg=" + resultMsg);
		return mv;

    }
	
	@RequestMapping(value = "/wtms/hdngvsso")//, method = RequestMethod.POST)
	public void autowaySsoLogin(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//ModelAndView mv = new ModelAndView("inout");

		/* @SuppressWarnings("unchecked")
		Enumeration<String> params = req.getParameterNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			if(req.getParameter(paramName) != null) {
				System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName).toString());
			} else {
				System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName));
			}
		} */
		//mv.addObject("tsId", "hdngv");  
		try {
			Cookie[] getCookie = req.getCookies();
			String decrptStr = "";
			String enterCd = "";
			if(getCookie != null){
				for(int i=0; i<getCookie.length; i++){
					Cookie c = getCookie[i];
					String name = c.getName(); // 쿠키 이름 가져오기
					String value = c.getValue(); // 쿠키 값 가져오기
					if(name.startsWith("sessiondata_____")) {
						decrptStr = value;
						enterCd = name.split("_____")[1];
					}
				}	
			}
			
			Map<String, Object> hashMap = new HashMap<>();
			
			String decrptVal1[] = decrptStr.split("___");
			
			System.out.println("decrptVal1 :: " + decrptVal1);
			for(int i=0; i<decrptVal1.length; i++)
			{
				String tmp[]=decrptVal1[i].split("\\|\\|");
				hashMap.put(tmp[0], tmp[1]);
			}
			 
			String empId = hashMap.get("Emp_ID")+"";
			
			String t = exchangeService.getAccessToken();
			Map<String, Object> tokenMap = WtmUtil.parseJwtToken(t);
			String jwtId = tokenMap.get("jti").toString();
			
			String prefix = "___";
			String paramStr = "username||" + "hdngv@"+enterCd+"@"+empId + prefix+ "password||ssoCertificate" + prefix+ "loginUserId||" + empId + prefix+ "loginEnterCd||" + enterCd; 
			
			Aes256 aes = new Aes256(jwtId);
			String encParam = aes.encrypt(paramStr);
			//https://cloudhr.pearbranch.com/ifw/login/hdngv/sso?accessToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTgwOTAwOTM2LCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiJlZjI5MTFlNS03OGI2LTRjNjUtOGQzOS03N2JmMzAxMmQyMzciLCJjbGllbnRfaWQiOiJoZG5ndiJ9.9PN95JEorGaxrOE0ZFGJdVBVqrLexjjB-2mJx26Dr_I&p=3w65MARMUvbQDhiX7fBCtyhGz1x0Jr9CuCDVAJJYFhIygdvHvXwNmTqj1yE4+7JODK4JBBhAoJ0SeJWEc5R7bmCRyYhrcwv1wDAkMZ4m0SaCZCHKk32QQE3i3TARHTBfG05qzsuBalwGtEi+JinXxA==
			System.out.println(jwtId+ " :: encParam : " + encParam);
			//http://cloudhr.pearbranch.com/ifa/oauth/authorize?client_id=hdngv&redirect_uri=http://cloudhr.pearbranch.com/ifw/login/hdngv/authorize&response_type=code&scope=read%20write
			res.sendRedirect("https://cloudhr.pearbranch.com/ifa/oauth/authorize?client_id=hdngvsso&redirect_uri=http://cloudhr.pearbranch.com/ifw/login/hdngv/authorize&response_type=code&scope=read%20write&accessToken="+t+"&p="+encParam);
		}catch(Exception e) {
			res.sendRedirect("/err?mgs="+ e.getMessage());
		}
		

    }
	
	@RequestMapping(value = "/err")//, method = RequestMethod.POST)
	public ModelAndView errorPage(@RequestParam String msg, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("message", msg);
		return mv;
    }
}
