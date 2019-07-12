package com.isu.ifw.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;

@Service
public class WtmFlexibleEmpServiceImpl implements WtmFlexibleEmpService {

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Override
	public Map<String, Object> getPrevFlexible(Long tenantId, String enterCd, String userKey) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("userKey", userKey);
		//paramMap.put("today", WtmUtil.parseDateStr(new Date(), null));
		ObjectMapper mapper=  new ObjectMapper();
		try {
			System.out.println("paramMap : " + mapper.writeValueAsString(paramMap));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flexEmpMapper.getPrevFlexible(paramMap);
	}
	
}
