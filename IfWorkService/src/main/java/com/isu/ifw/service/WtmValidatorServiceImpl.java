package com.isu.ifw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.option.vo.ReturnParam;

public class WtmValidatorServiceImpl implements WtmValidatorService  {

	@Autowired
	WtmFlexibleStdMapper flexStdMapper;
	
	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Override
	public ReturnParam checkDuplicateFlexibleWork(Long tenantId, String enterCd, String sabun, String symd, String eymd, Long applId) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("symd", symd);
		paramMap.put("eymd", eymd);
		paramMap.put("applId", applId);
		
		Map<String, Object> m = flexStdMapper.checkRequestDate(paramMap);
		int cnt = Integer.parseInt(m.get("CNT").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
	}

	@Override
	public ReturnParam checkDuplicateWorktime(Long tenantId, String enterCd, String sabun, Date sdate, Date edate, Long applId){
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("sdate", sdate);
		paramMap.put("edate", edate);
		paramMap.put("applId", applId);

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Map<String, Object> m = flexEmpMapper.checkDuplicateWorktime(paramMap);
		int cnt = Integer.parseInt(m.get("workCnt").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
		 
	}
	
}
