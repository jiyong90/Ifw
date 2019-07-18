package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmFlexibleApprMapper;

@Service("wtmFlexibleApprService")
public class WtmFlexibleApprServiceImpl implements WtmFlexibleApprService {

	@Autowired
	WtmFlexibleApprMapper flexibleApprMapper;
	
	@Override
	public List<Map<String, Object>> getFlexibleApprList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
		return flexibleApprMapper.getApprList(paramMap);
	}
	
}
