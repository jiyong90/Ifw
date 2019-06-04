package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface CodeSearchMapper {
	/**
	 * 자격증 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLicenseList(Map<String, Object> paramMap) throws Exception; 
	
	/**
	 * 어학 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLangList(Map<String, Object> paramMap) throws Exception; 
	
	/**
	 * 학교 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSchoolList(Map<String, Object> paramMap) throws Exception; 
}
