package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmIfEmpMsg;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmIfEmpMsgRepository;


@Service("empMgrService")
public class WtmEmpMgrServiceImpl implements WtmEmpMgrService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Resource
	WtmEmpHisRepository empHisRepository;
	
	@Resource
	WtmIfEmpMsgRepository empMsgRepository;
	
	@Override
	public List<Map<String, Object>> getEmpHisList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> empList = new ArrayList();	
		List<WtmEmpHis> list = empHisRepository.findByTenantIdAndEnterCd(tenantId, enterCd, paramMap.containsKey("sYmd")?paramMap.get("sYmd").toString():"", paramMap.get("searchKeyword").toString());
		
		for(WtmEmpHis l : list) {
			Map<String, Object> emp = new HashMap();
			emp.put("empHisId", l.getEmpHisId());
			emp.put("sabun", l.getSabun());
			emp.put("empNm", l.getEmpNm());
			emp.put("empEngNm", l.getEmpEngNm());
			emp.put("symd", l.getSymd());
			emp.put("eymd", l.getEymd());
			emp.put("statusCd", l.getStatusCd());
			emp.put("orgCd", l.getOrgCd());
			emp.put("businessPlaceCd", l.getBusinessPlaceCd());
			emp.put("dutyCd", l.getDutyCd());
			emp.put("posCd", l.getPosCd());
			emp.put("classCd", l.getClassCd());
			emp.put("jobGroupCd", l.getJobGroupCd());
			emp.put("jobCd", l.getJobCd());
			emp.put("payTypeCd", l.getPayTypeCd());
			emp.put("leaderYn", l.getLeaderYn());
			emp.put("note", l.getNote());
			empList.add(emp);
		}
		return empList;
	}

	@Override
	public Map<String, Object> getEmpHis(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		WtmEmpHis l = empHisRepository.findByEmpHisId(Long.parseLong(paramMap.get("empHisId").toString()));
		
		Map<String, Object> emp = new HashMap();
		emp.put("sabun", l.getSabun());
		emp.put("empNm", l.getEmpNm());
		emp.put("empEngNm", l.getEmpEngNm());
		emp.put("symd", l.getSymd());
		emp.put("eymd", l.getEymd());
		emp.put("statusCd", l.getStatusCd());
		emp.put("orgCd", l.getOrgCd());
		emp.put("businessPlaceCd", l.getBusinessPlaceCd());
		emp.put("dutyCd", l.getDutyCd());
		emp.put("posCd", l.getPosCd());
		emp.put("classCd", l.getClassCd());
		emp.put("jobGroupCd", l.getJobGroupCd());
		emp.put("jobCd", l.getJobCd());
		emp.put("payTypeCd", l.getPayTypeCd());
		emp.put("leaderYn", l.getLeaderYn());
		emp.put("note", l.getNote());
		return emp;
	}
	
	@Override
	public List<Map<String, Object>> getEmpIfMsgList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> empList = new ArrayList();	
		List<WtmIfEmpMsg> list = empMsgRepository.findByTenantIdAndEnterCd(tenantId, enterCd, paramMap.get("sYmd").toString(), paramMap.get("searchKeyword").toString());
		
		for(WtmIfEmpMsg l : list) {
			Map<String, Object> emp = new HashMap();
			emp.put("sabun", l.getSabun());
			emp.put("chgYmd", l.getChgYmd());
			emp.put("chgTypeCd", l.getChgTypeCd());
			emp.put("oldValue", l.getOldValue());
			emp.put("newValue", l.getNewValue());
			emp.put("note", l.getNote());
			empList.add(emp);
		}
		return empList;
	}
}