package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface WtmCalendarMapper {
	
	/**
	 * 회사 달력 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCalendar(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 근태 달력 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getWorkCalendar(Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getWorkTimeCalendar(Map<String, Object> paramMap) throws Exception;
	
	
	/**
	 * 관리자요_근태 달력 전체 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getEmpWorkCalendar(Map<String, Object> paramMap) throws Exception;
}
