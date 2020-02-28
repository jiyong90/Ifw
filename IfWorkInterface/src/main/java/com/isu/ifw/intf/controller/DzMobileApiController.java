package com.isu.ifw.intf.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.isu.ifw.intf.service.DzMobileApiService;
import com.isu.ifw.intf.vo.ReturnParam;

@Controller
public class DzMobileApiController {
	
	@Autowired
	private DzMobileApiService service;
	
	/**
	 * 개인 로그인(웹)
	 *
	 * @param request
	 * @param body
	 *            전달받은 데이터(회사별로 전달받는 데이터가 다름)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/certificate/request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam loginForWeb(HttpServletRequest request,
			@RequestBody(required = true) Map<String, String> body) throws Exception {
		
		System.out.println("#########################/certificate/request");
		System.out.println(body);
		
		//웹에서는 전송하지 않는 값들에 대해 임의의 파라미터를 셋팅한다.
		
		body.put("loginGroupCd", "3000"); //사이트마다 달라짐
		body.put("deviceId", "WEB");
		body.put("deviceModel", "WEB");
		body.put("osType", "ios");
		body.put("osVersion", "0.0.0");
		body.put("userToken", null);
		
		return service.login(request, body);
	}
	
	@RequestMapping(value = "/certificate/request/sso", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam loginForWeb_sso(HttpServletRequest request,
			@RequestBody(required = true) Map<String, Object> body) throws Exception {
		
		System.out.println("#########################/certificate/request/sso");
		System.out.println(body);
		
		return null;
	}
	
	/**
	 * 개인 로그인(모바일)
	 *
	 * @param request
	 * @param body
	 *            전달받은 데이터(회사별로 전달받는 데이터가 다름)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/m/certificate/request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam loginForMobile(HttpServletRequest request,
			@RequestBody(required = true) Map<String, String> body) throws Exception {
		
		System.out.println("#########################/m/certificate/request");
		System.out.println("loginUserId?" + (String)body.get("loginUserId").toUpperCase());
		System.out.println(body);
		
		return service.login(request, body);
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
	@RequestMapping(value = "/m/certificate/valid", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam validEmp(HttpServletRequest request,
			@RequestParam(value = "locale", required = false) String locale,
			@RequestParam(value = "empKey", required = false) String empKey,
			@RequestParam(value = "userToken", required = false) String accessToken) throws Exception {
		
		System.out.println("#########################/m/certificate/valid");
		Set<String> set = request.getParameterMap().keySet();
		Iterator<String> iterator = set.iterator();
		
		while(iterator.hasNext()) {
			String key = iterator.next();
			System.out.println(key+":"+request.getParameter(key));
		}
		
		return service.validEmp(request, locale, empKey, accessToken);
	}

}
