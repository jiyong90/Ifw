package com.isu.ifw.mapper;

import java.util.Map;

public interface WtmFlexibleEmpMapper {
	/*
	 * 기존에 신청한 근무제 적용일 가져오기
	 */
	public Map<String, Object> getPrevFlexible(Map<String, Object> paramMap);
	

}
