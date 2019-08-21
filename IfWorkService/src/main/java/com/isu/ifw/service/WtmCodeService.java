package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
public interface WtmCodeService {
	
	public List<Map<String, Object>> getCodeList(Long tenantId, String enterCd, String grpCodeCd);
	
	public int setCodeList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap);
	
	public List<Map<String, Object>> getCodeGrpList(Long tenantId, String enterCd);
	
	public int setCodeGrpList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap);
}
