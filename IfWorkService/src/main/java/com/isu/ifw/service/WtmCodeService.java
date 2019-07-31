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
}
