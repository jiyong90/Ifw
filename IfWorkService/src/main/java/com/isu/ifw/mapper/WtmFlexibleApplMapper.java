package com.isu.ifw.mapper;

import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleApplVO;

public interface WtmFlexibleApplMapper {
	
	public WtmFlexibleApplVO getWtmFlexibleAppl(Map<String, Object> paramMap);
	
	public Map<String, Object> getLastAppl(Map<String, Object> paramMap);
	
}
