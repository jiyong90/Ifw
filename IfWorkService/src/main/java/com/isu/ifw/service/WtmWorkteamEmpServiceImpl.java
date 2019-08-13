package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmTimeCdMgr;
import com.isu.ifw.entity.WtmWorkteamEmp;
import com.isu.ifw.repository.WtmTimeCdMgrRepository;
import com.isu.ifw.repository.WtmWorkteamEmpRepository;

@Service
public class WtmWorkteamEmpServiceImpl implements WtmWorkteamEmpService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	
	@Resource
	WtmWorkteamEmpRepository workteamRepository;

	@Override
	public List<Map<String, Object>> getWorkteamList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> timeList = new ArrayList();	
		try {
			List<Map<String, Object>> list = workteamRepository.findByTenantIdAndEnterCd(tenantId, enterCd, paramMap.get("sYmd").toString(), paramMap.get("searchKeyword").toString());
			
			for(Map<String, Object> l : list) {
				Map<String, Object> time = new HashMap();
				time.put("workteamEmpId", l.get("workteamEmpId"));
				time.put("workteamCd", l.get("workteamCd"));
				time.put("sabun", l.get("sabun"));
				time.put("symd", l.get("symd"));
				time.put("eymd", l.get("eymd"));
				time.put("note", l.get("note"));
				time.put("orgCd", l.get("orgCd"));
				time.put("classCd", l.get("classCd"));
				time.put("empNm", l.get("empNm"));
				timeList.add(time);
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.debug(e.toString(), e);
		} finally {
			MDC.clear();
			logger.debug("getWorkteamList Controller End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));

		}
		
		return timeList;
	}

}