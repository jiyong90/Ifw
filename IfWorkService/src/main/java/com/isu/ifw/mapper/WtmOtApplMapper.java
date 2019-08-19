package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface WtmOtApplMapper {
	
	/**
	 * 연장근무 신청 시간 업데이트
	 * @param paramMap
	 * @return
	 */
	public void calcOtMinute(Map<String, Object> paramMap);
	
	/**
	 * 연장근무 신청서 조회
	 * @param applId
	 * @return
	 */
	public Map<String, Object> otApplfindByApplId(Long applId);
	 	
	/**
	 * 이전에 신청한 휴일신청서 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getPrevOtSubsApplList(Map<String, Object> paramMap);
}
