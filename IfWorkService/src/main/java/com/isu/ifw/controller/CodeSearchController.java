package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.CodeSearchService;
import com.isu.ifw.service.StdManagementService;
import com.isu.ifw.vo.StdManagement;

@RestController
@RequestMapping(value="/codeSearch")
public class CodeSearchController {

	@Autowired
	StdManagementService stdManagementService;
	
	@Autowired
	CodeSearchService codeSearchService;
	
	/**
	 * 자격증 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/license", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Map<String, Object> licenseSearch(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(paramMap.containsKey("userKey") && paramMap.get("userKey")!=null) {
			String[] userKeySpl = paramMap.get("userKey").toString().split("@");

			String enterCd = userKeySpl[0]; //enterCd
			String sabun = userKeySpl[1]; //sabun
			
			paramMap.put("loginEnterCd", enterCd);
		} else {
			paramMap.put("loginEnterCd", "TY");
		}
		
		StdManagement keyCheck = stdManagementService.getKeyCheckResult(paramMap);
		if ("SUCCESS".equals(keyCheck.getApikeyResult()) && "SUCCESS".equals(keyCheck.getSecretResult())) {
			List<Map<String, Object>> resultList  = codeSearchService.getLicenseList(paramMap);
	
			resultMap.put("data", resultList);
		}

		return resultMap;
	}

	/**
	 * 어학 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/language", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Map<String, Object> langSearch(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(paramMap.containsKey("userKey") && paramMap.get("userKey")!=null) {
			String[] userKeySpl = paramMap.get("userKey").toString().split("@");

			String enterCd = userKeySpl[0]; //enterCd
			String sabun = userKeySpl[1]; //sabun
			
			paramMap.put("loginEnterCd", enterCd);
		} else {
			paramMap.put("loginEnterCd", "TY");
		}
		
		StdManagement keyCheck = stdManagementService.getKeyCheckResult(paramMap);
		if ("SUCCESS".equals(keyCheck.getApikeyResult()) && "SUCCESS".equals(keyCheck.getSecretResult())) {
			List<Map<String, Object>> resultList  = codeSearchService.getLangList(paramMap);
	
			resultMap.put("data", resultList);
		}

		return resultMap;
	}

	/**
	 * 학교 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/school", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Map<String, Object> schoolSearch(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(paramMap.containsKey("userKey") && paramMap.get("userKey")!=null) {
			String[] userKeySpl = paramMap.get("userKey").toString().split("@");

			String enterCd = userKeySpl[0]; //enterCd
			String sabun = userKeySpl[1]; //sabun
			
			paramMap.put("loginEnterCd", enterCd);
		} else {
			paramMap.put("loginEnterCd", "TY");
		}
		
		StdManagement keyCheck = stdManagementService.getKeyCheckResult(paramMap);
		if ("SUCCESS".equals(keyCheck.getApikeyResult()) && "SUCCESS".equals(keyCheck.getSecretResult())) {
			List<Map<String, Object>> resultList  = codeSearchService.getSchoolList(paramMap);
	
			resultMap.put("data", resultList);
		}

		return resultMap;
	}
		
}
