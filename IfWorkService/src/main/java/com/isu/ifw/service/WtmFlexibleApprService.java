package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
public interface WtmFlexibleApprService {
	
	public List<Map<String, Object>> getFlexibleApprList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap);
	
}
