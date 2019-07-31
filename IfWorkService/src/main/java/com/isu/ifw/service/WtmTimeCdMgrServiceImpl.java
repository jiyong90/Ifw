package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmTimeCdMgr;
import com.isu.ifw.repository.WtmTimeCdMgrRepository;

@Transactional
@Service
public class WtmTimeCdMgrServiceImpl implements WtmTimeCdMgrService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	
	@Resource
	WtmTimeCdMgrRepository timeCdRepository;

	@Override
	public List<Map<String, Object>> getTimeCdList(Long tenantId, String enterCd,  Map<String, Object> paramMap) {
		List<Map<String, Object>> timeList = new ArrayList();	
		List<WtmTimeCdMgr> list = timeCdRepository.findByTenantIdAndEnterCd(tenantId, enterCd);
		
		for(WtmTimeCdMgr l : list) {
			Map<String, Object> time = new HashMap();
			time.put("timeCdMgrId", l.getTimeCdMgrId());
			time.put("code", l.getTimeCdMgrId());
			time.put("codeNm", l.getTimeNm());
			time.put("timeNm", l.getTimeNm());
			time.put("timeCd", l.getTimeCd());
			time.put("symd", l.getSymd());
			time.put("eymd", l.getEymd());
			time.put("workShm", l.getWorkShm());
			time.put("workEhm", l.getWorkEhm());
			time.put("holYn", l.getHolYn() != null && l.getHolYn().equals("Y")?"1":"0");
			time.put("holTimeCd", l.getHolTimeCd());
			time.put("lateChkYn", l.getLateChkYn() != null && l.getLateChkYn().equals("Y")?"1":"0");
			time.put("leaveChkYn", l.getLeaveChkYn() != null && l.getLeaveChkYn().equals("Y")?"1":"0");
			time.put("absenceChkYn", l.getAbsenceChkYn() != null && l.getAbsenceChkYn().equals("Y")?"1":"0");
			time.put("note", l.getNote());
			timeList.add(time);
		}
		return timeList;
	}

}