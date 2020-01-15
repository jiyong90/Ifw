package com.isu.ifw.intf.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.intf.vo.WtmFlexibleStdVO;

public interface WtmInterfaceMapper {
	
	
	/**
	 * 공통코드 조회
	 **/
	public Map<String, Object> getEnterCd(String tenantId);
	
	
	/**
	 * 공통코드 조회
	 **/
	public List<Map<String, Object>> findMaCodedtlAll(Map<String, Object> paramMap);
	public List<Map<String, Object>> getCode(Map<String, Object> paramMap);
	
	/**
	 * 공휴일 조회
	 **/
	public List<Map<String, Object>> getHoliday(Map<String, Object> paramMap);
	
	/**
	 * 근태코드 조회
	 **/
	public List<Map<String, Object>> getGnt(Map<String, Object> paramMap);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getOrg(Map<String, Object> paramMap);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getOrgChart(Map<String, Object> paramMap);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getOrgChartDtl(Map<String, Object> paramMap);
	
	/**
	 * 조직코드설정(근무조,사업장) 조회
	 **/
	public List<Map<String, Object>> getOrgMap(Map<String, Object> paramMap);
	
	/**
	 * 발령이력 조회
	 **/
	public List<Map<String, Object>> getEmp(Map<String, Object> paramMap);
	
	/**
	 * 조직장 조회
	 **/
	public List<Map<String, Object>> getOrgConc(Map<String, Object> paramMap);
	
	/**
	 * 임직원정보 메일연락처  조회
	 **/
	public List<Map<String, Object>> getEmpAddr(Map<String, Object> paramMap);
	
	/**
	 * 근태정보 조회
	 **/
	public List<Map<String, Object>> getTaaAppl(Map<String, Object> paramMap);
}
