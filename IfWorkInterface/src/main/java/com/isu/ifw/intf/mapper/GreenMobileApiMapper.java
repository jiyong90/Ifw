package com.isu.ifw.intf.mapper;

import java.util.List;
import java.util.Map;

public interface GreenMobileApiMapper {
	 
	/**
	 * 세션 정보 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getMobileSession(Map<String, Object> paramMap);
	public Map<String, Object> getCheckSession(Map<String, Object> paramMap);
	
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
	
	/**
	 * 직원 사진
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getEmpPhotoOut(Map<String, Object> paramMap);
}
