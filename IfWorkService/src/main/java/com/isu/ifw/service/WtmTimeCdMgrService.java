package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.isu.option.vo.ReturnParam;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("WtmTimeCdMgrService")
public interface WtmTimeCdMgrService{
	
	public List<Map<String, Object>> getTimeCdMgrList(Long tenantId, String enterCd, Map<String, Object> paramMap);
	
	public int setTimeCodeMgrList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap);
	
	public List<Map<String, Object>> getTimeBreakMgrList(Long tenantId, String enterCd, Map<String, Object> paramMap);
	
	public int setTimeBreakMgrList(Long userId, Map<String, Object> convertMap);
	
}