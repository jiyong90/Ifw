package com.isu.ifw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.vo.WtmFlexibleStdVO;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/api")
public class WtmFlexibleStdController {

	@GetMapping(value="/flexibleStd")
	public ReturnParam flexibleStd(HttpServletRequest request) {
		/*
		Long tenantId = (Long)request.getAttribute("tenantId");
		Map sessionData = (Map)request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd") + "";
		String userId = sessionData.get("userId") + "";
		*/
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<>();
		m.put("flexibleStdMgrId", "1");
		m.put("flexibleNm", "이수연구직");
		m.put("workTypeCd", "10");
		m.put("workTypeNm", "탄근제");
		//Map<String, Object> core = new HashMap<>();
		//core.put("sTime", "1000");
		//core.put("eTime", "1500");
		m.put("coreTime", null);
		Map<String, Object> limit = new HashMap<>();
		limit.put("sTime", "0800");
		limit.put("eTime", "2200");
		m.put("limitTime", limit);
		
		Map<String, Object> m1 = new HashMap<>();
		m1.put("flexibleStdMgrId", "2");
		m1.put("flexibleNm", "이수선근제기본");
		m1.put("workTypeCd", "20");
		m1.put("workTypeNm", "선근제[부분]");
		Map<String, Object> core1 = new HashMap<>();
		core1.put("sTime", "1000");
		core1.put("eTime", "1500");
		m.put("coreTime", core1);
		Map<String, Object> limit1 = new HashMap<>();
		limit1.put("sTime", "0800");
		limit1.put("eTime", "2200");
		m.put("limitTime", limit1);
		l.add(m);
		
		return null;
	}
	@PostMapping(value = "/flexibleStd") 
	public ReturnParam flexibleStd(@RequestBody WtmFlexibleStdVO flexibleStdVO
								, HttpServletRequest request) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(flexibleStdVO));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReturnParam rp = new ReturnParam();
		return rp;
			
	}
}
