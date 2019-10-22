package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmOrgCodeMapper;

@Service("WtmOrgCodeService")
public class WtmOrgCodeServiceImpl implements WtmOrgCodeService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmOrgCodeMapper wtmOrgCodeMapper;

	@Override
	public List<Map<String, Object>> getOrgCodeList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> searchList = new ArrayList();	
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			if(!paramMap.containsKey("sYmd")) {
				paramMap.put("sYmd", "");
			}
			searchList =  wtmOrgCodeMapper.getOrgCodeList(paramMap);
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("getApplCodeList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		
		return searchList;
	}
}