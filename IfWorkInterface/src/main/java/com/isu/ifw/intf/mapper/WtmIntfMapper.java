package com.isu.ifw.intf.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.intf.vo.WtmFlexibleStdVO;

public interface WtmIntfMapper {
	 
	/**
	 * 공통코드 조회
	 **/
	public List<Map<String, Object>> getWtmCode(Map<String, Object> paramMap);
	
	/**
	 * 공휴일 조회
	 **/
	public List<Map<String, Object>> getWtmHoliday(Map<String, Object> paramMap);
	
	/**
	 * 근태코드 조회
	 **/
	public List<Map<String, Object>> getWtmGnt(Map<String, Object> paramMap);
	
	/**
	 * 조직코드 조회
	 **/
	public List<Map<String, Object>> getWtmOrg(Map<String, Object> paramMap);
	
	/**
	 * 발령이력 조회
	 **/
	public List<Map<String, Object>> getWtmEmp(Map<String, Object> paramMap);
	
	/**
	 * 겸직 조회
	 **/
	public List<Map<String, Object>> getWtmEmpMulti(Map<String, Object> paramMap);

	/**
	 * 조직장 조회
	 **/
	public List<Map<String, Object>> getWtmOrgConc(Map<String, Object> paramMap);
	
	/**
	 * 임직원정보 메일연락처  조회
	 **/
	public List<Map<String, Object>> getWtmEmpAddr(Map<String, Object> paramMap);
	
	/**
	 * 근태정보 조회
	 **/
	public List<Map<String, Object>> getWtmTaaAppl(Map<String, Object> paramMap);
}
