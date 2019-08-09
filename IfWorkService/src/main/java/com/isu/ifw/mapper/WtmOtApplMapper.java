package com.isu.ifw.mapper;

import java.util.Map;

public interface WtmOtApplMapper {
	
	/**
	 * 연장근무 신청 시간 업데이트
	 * @param paramMap
	 * @return
	 */
	public void calcOtMinute(Map<String, Object> paramMap);
	 	
}
