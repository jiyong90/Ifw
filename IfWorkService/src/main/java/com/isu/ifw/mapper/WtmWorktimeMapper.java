package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface WtmWorktimeMapper {
	
	//근무 이상자 조회(근무시간 초과자)
	public List<Map<String, Object>> getWorktimeCheckList(Map<String, Object> paramMap);
}
