package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
public interface WtmInoutService {
	
	public Map<String, Object> getMenuContext(Long tenantId, String enterCd, String sabun);
	public int checkInoutHis(Long tenantId, String enterCd, String sabun, String inoutType, String ymd) throws Exception;
	public int updateTimeStamp(Map<String, Object> paramMap);
	public List<Map<String, Object>> getMyInoutList(Long tenantId, String enterCd, String sabun, String ymd) throws Exception;
	public Map<String, Object> getMyInoutDetail(Long tenantId, String enterCd, String sabun, String ymd) throws Exception;
}
