package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 근태 이상자
 *
 * @author 
 *
 */
@Service
public interface WtmWorktimeService{
	
	public List<Map<String, Object>> getWorktimeCheckList(Long tenantId, String enterCd, Map<String, Object> paramMap);
	
	public List<Map<String, Object>> getWorktimeDetail(Long tenantId, String enterCd, Map<String, Object> paramMap);
	
}