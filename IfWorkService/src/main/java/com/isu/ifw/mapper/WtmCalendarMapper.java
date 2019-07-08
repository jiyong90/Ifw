package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface WtmCalendarMapper {
	public List<Map<String, Object>> getWorkTimeCalendar(Map<String, Object> paramMap) throws Exception;
	
}
