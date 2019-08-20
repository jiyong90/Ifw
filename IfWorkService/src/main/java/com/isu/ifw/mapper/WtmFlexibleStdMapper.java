package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

public interface WtmFlexibleStdMapper {
	/*
	 * 신청가능한 근무제 조회
	 */
	public List<WtmFlexibleStdVO> getWtmFlexibleStd(Map<String, Object> paramMap);
	
	/**
	 * 기본근무제를 제외한 유연근무 신청 중 또는 이미 등록된 정보가 있는지 확인한다
	 * @param applId
	 * @return
	 */
	public Map<String, Object> checkRequestDate(Long applId);
	
	//근무제 전체 조회
	public List<Map<String, Object>> getWtmFlexibleStdList(Map<String, Object> paramMap);
	
	//근무제 work_type_cd별 조회
	public List<Map<String, Object>> getWtmFlexibleStdWorkTypeList(Map<String, Object> paramMap);
}
