package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.EmpSearchMapper;

/**
 * 직원조회 service
 * @author 
 *
 */
@Service("EmpSearchService")
public class EmpSearchService {
	
	@Autowired
	EmpSearchMapper empSearchMapper;
	
	/**
	 * 직원 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSearchEmpList(Map<String, Object> paramMap) throws Exception {
		return empSearchMapper.getSearchEmpList(paramMap);
	}
	
}
