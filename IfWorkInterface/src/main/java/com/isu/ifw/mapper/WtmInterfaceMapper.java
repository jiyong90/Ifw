package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

public interface WtmInterfaceMapper {
	
	/**
	 * 공통코드 조회
	 **/
	public List<Map<String, Object>> getCode(String lastDataTime);
	
	/**
	 * 공휴일 조회
	 **/
	public List<Map<String, Object>> getHoliday(String lastDataTime);
	
	/**
	 * 근태코드 조회
	 **/
	public List<Map<String, Object>> getGnt(String lastDataTime);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getOrg(String lastDataTime);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getOrgChart(String lastDataTime);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getOrgChartDtl(String lastDataTime, String enterCd, String sdate);
	
	/**
	 * 조직코드설정(근무조,사업장) 조회
	 **/
	public List<Map<String, Object>> getOrgMap(String lastDataTime);
	
	/**
	 * 발령이력 조회
	 **/
	public List<Map<String, Object>> getEmp(String lastDataTime);
}
