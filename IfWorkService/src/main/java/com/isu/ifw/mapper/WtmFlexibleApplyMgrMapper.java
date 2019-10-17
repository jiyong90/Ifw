package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

public interface WtmFlexibleApplyMgrMapper {
	
	public List<Map<String, Object>> getApplyList(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> getApplyGrpList(Map<String, Object> paramMap);
	
	public int insertGrp(List<Map<String, Object>> list);
	
	public int deleteGrp(List<Map<String, Object>> paramList);
	
	public List<Map<String, Object>> getApplyEmpList(Map<String, Object> paramMap);
	
	public int insertEmp(List<Map<String, Object>> list, String userId);
	
	public int deleteEmp(List<Map<String, Object>> paramList);
}
