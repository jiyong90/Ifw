package com.isu.ifw.intf.honda.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.intf.service.ExchangeService;
import com.isu.ifw.intf.util.Aes256;
import com.isu.ifw.intf.util.TokenUtil;
import com.isu.ifw.intf.util.WtmUtil;

@RestController
public class LoginControllerHonda {



	@Value("${dzGateway.sign-key}")
	private String signKey;
	
	@Value("${dzGateway.expired-time:60000l}")
	private Long expiredTime;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@RequestMapping(value = "/wtms/hondasso", method = RequestMethod.GET)
	public void hondaSsoLogin(@RequestParam(required = true) String p, HttpServletRequest req, HttpServletResponse res) throws Exception {
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
		//mv.addObject("tsId", "honda");
		//Aes256 aes256 = new Aes256(signKey);
		Map<String, Object> payloads = new HashMap<String, Object>();
		payloads.put("CD_COMPANY", "1000");
		payloads.put("NO_EMP", "인사");
		
		String jwtString = TokenUtil.createJWT(signKey, "id", "issure", "subject", payloads, expiredTime);
		//System.out.println(aes256.encrypt(jwtString));
		System.out.println(jwtString);
		//System.out.println(aes256.decrypt(jwtString));
		//String decJwt = aes256.decrypt(p);
		//System.out.println("decJwt : " + decJwt);
		Map<String, Object> pMap = TokenUtil.decodeJWT(signKey, p);
		
		System.out.println(pMap.toString());
		
		
		
		try { 
			String enterCd = pMap.get("CD_COMPANY") + "";
			String empId = pMap.get("NO_EMP")+"";
			
			String t = exchangeService.getAccessToken();
			Map<String, Object> tokenMap = WtmUtil.parseJwtToken(t);
			String jwtId = tokenMap.get("jti").toString();
			
			String prefix = "___";
			String paramStr = "username||" + "honda@"+enterCd+"@"+empId + prefix+ "password||ssoCertificate" + prefix+ "loginUserId||" + empId + prefix+ "loginEnterCd||" + enterCd; 
			
			Aes256 aes = new Aes256(jwtId);
			String encParam = aes.encrypt(paramStr);
			//https://cloudhr.pearbranch.com/ifw/login/honda/sso?accessToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTgwOTAwOTM2LCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiJlZjI5MTFlNS03OGI2LTRjNjUtOGQzOS03N2JmMzAxMmQyMzciLCJjbGllbnRfaWQiOiJoZG5ndiJ9.9PN95JEorGaxrOE0ZFGJdVBVqrLexjjB-2mJx26Dr_I&p=3w65MARMUvbQDhiX7fBCtyhGz1x0Jr9CuCDVAJJYFhIygdvHvXwNmTqj1yE4+7JODK4JBBhAoJ0SeJWEc5R7bmCRyYhrcwv1wDAkMZ4m0SaCZCHKk32QQE3i3TARHTBfG05qzsuBalwGtEi+JinXxA==
			System.out.println(jwtId+ " :: encParam : " + encParam);
			//http://cloudhr.pearbranch.com/ifa/oauth/authorize?client_id=honda&redirect_uri=http://cloudhr.pearbranch.com/ifw/login/honda/authorize&response_type=code&scope=read%20write
			res.sendRedirect("https://cloudhr.pearbranch.com/ifa/oauth/authorize?client_id=hondasso&redirect_uri=https://cloudhr.pearbranch.com/ifw/login/honda/authorize&response_type=code&scope=read%20write&accessToken="+t+"&p="+encParam);
		}catch(Exception e) {
			res.sendRedirect("/err?mgs="+ e.getMessage());
		}

    }
	
}
