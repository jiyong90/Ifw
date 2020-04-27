package com.isu.ifw.intf.mapper;

import java.util.Map;

public interface GreenMobileApiMapper {
	 
	/**
	 * 세션 정보 조회
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getMobileSession(Map<String, Object> paramMap);
	
	/**
	 * 세션 정보 저장
	 * @param paramMap
	 * @return
	 */
	public int saveMobileSession(Map<String, Object> paramMap);
	
	/**
	 * 세션 무효화
	 * @param paramMap
	 * @return
	 */
	public int updateAccessToken(Map<String, Object> paramMap);
	

}
