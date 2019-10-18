package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmWorktimeMapper;

@Service
public class WtmWorkTimeServiceImpl implements WtmWorktimeService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");

	@Autowired
	WtmWorktimeMapper worktimeMapper;
	
	@Override
	public List<Map<String, Object>> getWorktimeCheckList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> WorktimeCheckList = null;
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			WorktimeCheckList = worktimeMapper.getWorktimeCheckList(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			logger.debug(e.toString(), e);
		} finally {
			MDC.clear();
			logger.debug("getWorktimeCheckList End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));

		}
		
		return WorktimeCheckList;
	}
	
	@Override
	public List<Map<String, Object>> getWorktimeDetail(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> WorktimeCheckList = null;
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			WorktimeCheckList = worktimeMapper.getWorktimeDetail(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			logger.debug(e.toString(), e);
		} finally {
			MDC.clear();
			logger.debug("getWorktimeDetail End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));

		}
		
		return WorktimeCheckList;
	}


}