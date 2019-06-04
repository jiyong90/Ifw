package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface EmpSearchMapper {
	/**
	 * 직원 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSearchEmpList(Map<String, Object> paramMap) throws Exception; 
	
}
