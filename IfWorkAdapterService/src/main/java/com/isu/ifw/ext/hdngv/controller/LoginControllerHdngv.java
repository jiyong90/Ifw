package com.isu.ifw.ext.hdngv.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.tempuri.SitemapWSSoap;

import com.fasterxml.jackson.core.TSFBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.ExchangeService;

@RestController
public class LoginControllerHdngv {
	
	

	@Value("${ifw.status-get}")
	private String statusUrl;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@RequestMapping(value = "/certificate/hdngv")//, method = RequestMethod.POST)
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
				
				HashMap<String, String> hashMap = new HashMap<String, String>();
				
				String decrptStr = SitemapWSSoap.getPlainText(strEncID, strCompanyCode, strEncText);
				System.out.println("decrptStr=   "+decrptStr);
				
				String decrptVal1[] = decrptStr.split("___");
				
				// Email||1111111@dev-hyundai-ngv.com___User_ID||H133001111111___IP||10.206.34.254___Emp_ID||1111111___ssoTIME||20191127191810
				System.out.println("decrptVal1 :: " + decrptVal1);
				for(int i=0; i<decrptVal1.length; i++)
				{
					String tmp[]=decrptVal1[i].split("\\|\\|");
					hashMap.put(tmp[0], tmp[1]);
				}
				
				
				System.out.println("resultMap : " + mapper.writeValueAsString(hashMap));
				String empId = hashMap.get("Emp_ID")+"";
				Map<String, Object> paramMap = new HashMap<>();
				//paramMap.put("empKey", strCompanyCode + "@" + empId);
				paramMap.put("enterCd", strCompanyCode);
				paramMap.put("sabun", empId);
				mv.addObject("enterCd", strCompanyCode);
				mv.addObject("sabun", empId);
				
				Map<String, Object> resMap = exchangeService.exchange(url, HttpMethod.GET, null, paramMap);
				
				mv.addAllObjects((Map<String, ?>) resMap.get("result"));
				
				System.out.println(mapper.writeValueAsString(resMap));
						
			} else {
				mv.addObject("message", "Encode is empty.");
				//mv.setViewName("error");
				 
			}
			

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("enterCd", "HDNGV");
			paramMap.put("sabun", "2008856");
			Map<String, Object> resMap = exchangeService.exchange(url, HttpMethod.GET, null, paramMap);

			mv.addObject("enterCd", "HDNGV");
			mv.addObject("sabun", "2008856");
			
			Map<String, Object> rMap = ((Map<String, Object>) resMap.get("result"));
			if(!rMap.containsKey("ymd") || !rMap.containsKey("entrySdate") || !rMap.containsKey("entryEdate") || !rMap.containsKey("label") || !rMap.containsKey("desc") || !rMap.containsKey("inoutType")) {
				
			}else {
				
			}
			
			for(String kk : rMap.keySet()) {
				System.out.println(kk + " : " + rMap.get(kk));
				mv.addObject(kk, (rMap.get(kk)==null)?"":rMap.get(kk));
			}
			System.out.println(mapper.writeValueAsString(rMap));
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
}
