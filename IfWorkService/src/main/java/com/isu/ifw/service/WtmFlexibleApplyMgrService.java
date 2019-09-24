package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 
 * @author 
 *
 */
public interface WtmFlexibleApplyMgrService {
	
	public List<Map<String, Object>> getApplyList(Long tenantId, String enterCd, String sYmd);

	public int setApplyList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap);
	
	public List<Map<String, Object>> getApplyGrpList(Map<String, Object> paramMap);

	public int setApplyGrpList(Long userId, Map<String, Object> convertMap);
	
	public List<Map<String, Object>> getApplyEmpList(Map<String, Object> paramMap);

	public int setApplyEmpList(Long userId, Map<String, Object> convertMap);
}
