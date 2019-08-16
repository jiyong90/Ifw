package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
public interface WtmWorkteamMgrService {
	
	public List<Map<String, Object>> getWorkteamMgrList(Long tenantId, String enterCd, Map<String, Object> paramMap);

	public int setWorkteamMgrList(Long tenantId, String enterCd, Long userId, Map<String, Object> paramMap);
}
