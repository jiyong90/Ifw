package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmCalendarMapper;

/**
 * 근태 달력 관리 service
 * @author 
 *
 */
@Service("WtmCalendarService")
public class WtmCalendarService {
	
	@Autowired
	WtmCalendarMapper wtmCalendarMapper;
	
	
	/**
	 * 근태 달력 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getWorkTimeCalendar(Map<String, Object> paramMap) throws Exception {
		return wtmCalendarMapper.getWorkTimeCalendar(paramMap);
	}
	
}
