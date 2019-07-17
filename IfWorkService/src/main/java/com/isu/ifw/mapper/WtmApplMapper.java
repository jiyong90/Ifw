package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmApplLineVO;

public interface WtmApplMapper {
	public List<WtmApplLineVO> getWtmApplLine(Map<String, Object> paramMap);
	
	public Map<String, Object> calcWorkDay(Map<String, Object> paramMap);
}
