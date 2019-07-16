package com.isu.ifw.mapper;

import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleEmpVO;

public interface WtmFlexibleEmpMapper {
	
	/**
	 * 선택한 기간의 근무제 정보 조회
	 * @param paramMap
	 * @return
	 */
	public WtmFlexibleEmpVO getFlexibleEmp(Map<String, Object> paramMap);
	
	/**
	 * 기존에 신청한 근무제 적용일 가져오기
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getPrevFlexible(Map<String, Object> paramMap);
	
}
