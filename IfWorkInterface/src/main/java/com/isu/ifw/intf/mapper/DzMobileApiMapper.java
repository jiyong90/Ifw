package com.isu.ifw.intf.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.intf.vo.WtmFlexibleStdVO;

public interface DzMobileApiMapper {
	 
	
	/**
	 * 사원 정보 조회
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getEmpInfo(Map<String, Object> paramMap);
	
	/**
	 * 유저 정보 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getEmpUserInfo(Map<String, Object> paramMap);
	
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
	public int invalidateMobileSession(Map<String, Object> paramMap);
	

}
