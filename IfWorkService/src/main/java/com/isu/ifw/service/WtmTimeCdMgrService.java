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
	
	public List<Map<String, Object>> getTimeCdList(Long tenantId, String enterCd, Map<String, Object> paramMap);
}