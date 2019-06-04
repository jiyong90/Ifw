package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.CodeSearchMapper;

/**
 * 직원조회 service
 * @author 
 *
 */
@Service("CodeSearchService")
public class CodeSearchService {
	
	@Autowired
	CodeSearchMapper codeSearchMapper;
	
	/**
	 * 자격증 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLicenseList(Map<String, Object> paramMap) throws Exception {
		return codeSearchMapper.getLicenseList(paramMap);
	}
	
	/**
	 * 어학 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLangList(Map<String, Object> paramMap) throws Exception {
		return codeSearchMapper.getLangList(paramMap);
	}
	
	/**
	 * 학교 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSchoolList(Map<String, Object> paramMap) throws Exception {
		return codeSearchMapper.getSchoolList(paramMap);
	}
	
}
