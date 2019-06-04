package com.isu.ifw.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.EmpSearchService;
import com.isu.ifw.service.StdManagementService;
import com.isu.ifw.vo.StdManagement;

@RestController
public class EmpSearchController {
	
	@Autowired
	StdManagementService stdManagementService;

	@Autowired
	EmpSearchService empSearchService;
	
	/**
	 * 직원조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/empSearch", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public Map<String, Object> recruitEmpSearch(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();
		
		String[] userKeySpl = paramMap.get("userKey").toString().split("@");

		String enterCd = userKeySpl[0]; //enterCd
		String sabun = userKeySpl[1]; //sabun
		paramMap.put("ssnLocaleCd", "ko_KR");
		paramMap.put("loginEnterCd", enterCd);
		paramMap.put("loginUserId", sabun);
		
		ObjectMapper mapper = new ObjectMapper();
		
		StdManagement keyCheck = stdManagementService.getKeyCheckResult(paramMap);
		if ("SUCCESS".equals(keyCheck.getApikeyResult()) && "SUCCESS".equals(keyCheck.getSecretResult())) {
			//ObjectMapper mapper = new ObjectMapper();
			List<String> targetUserKeyList = null;
			List<?> resultList  = new ArrayList<Object>();
			
			if (null != paramMap.get("targetUserKey") && !"".equals(paramMap.get("targetUserKey"))) {
				targetUserKeyList = mapper.readValue(paramMap.get("targetUserKey").toString(), new ArrayList<>().getClass());
				List<Serializable> searchRow = new ArrayList<Serializable>();
				HashMap<String, String> mapElement = null;
				String[] targetUserKeySpl = null;

				for (int i=0; i<targetUserKeyList.size(); i++) {
					mapElement = new HashMap<String, String>();
					targetUserKeySpl = targetUserKeyList.get(i).split("@");
					
					try {
						mapElement.put("searchSabun", targetUserKeySpl[1]);
						searchRow.add(mapElement);
					} catch(ArrayIndexOutOfBoundsException e) {
						continue;
					}
				}
				paramMap.put("searchRows", searchRow);
			}
			
			//System.out.println("paramMap : " + mapper.writeValueAsString(paramMap));
			resultList = empSearchService.getSearchEmpList(paramMap);
			System.out.println("resultList : " + mapper.writeValueAsString(resultList));
			resultMap.put("result", resultList);
			resultMap.put("message", "");
		} else {
			resultMap.put("result", "");
			resultMap.put("message", "APIKEY OR SECRET ERROR");
		}

		return resultMap;
	}
		
}
