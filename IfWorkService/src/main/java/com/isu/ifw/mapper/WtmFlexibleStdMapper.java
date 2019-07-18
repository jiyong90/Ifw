package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

public interface WtmFlexibleStdMapper {
	/*
	 * 신청가능한 근무제 조회
	 */
	public List<WtmFlexibleStdVO> getWtmFlexibleStd(Map<String, Object> paramMap);
	public Map<String, Object> checkRequestDate(Long applId);
	

}
