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
import com.isu.ifw.entity.WtmWorkteamMgr;
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
				time.put("workteamMgrId", l.get("workteamMgrId"));
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
	
	@Override
	public int setWorkteamList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<WtmWorkteamEmp> saveList = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmWorkteamEmp workteam = new WtmWorkteamEmp();
						workteam.setEnterCd(enterCd);
						workteam.setTenantId(tenantId);
						workteam.setUpdateId(userId);
						workteam.setWorkteamEmpId(l.get("workteamEmpId").toString().equals("") ? null : Long.parseLong(l.get("workteamMgrId").toString()));
						workteam.setWorkteamMgrId(Long.parseLong(l.get("workteamMgrId").toString()));
						workteam.setEymd(l.get("eymd").toString());
						workteam.setNote(l.get("note").toString());
						workteam.setSymd(l.get("symd").toString());
						saveList.add(workteam);
					}
					saveList = workteamRepository.saveAll(saveList);
					cnt += saveList.size();
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
		
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				List<WtmWorkteamEmp> deleteList = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmWorkteamEmp workteam = new WtmWorkteamEmp();
						workteam.setWorkteamEmpId(Long.parseLong(l.get("workteamEmpId").toString()));
						deleteList.add(workteam);
					}
					workteamRepository.deleteAll(deleteList);
				}
				
				MDC.put("delete cnt", "" + iList.size());
				cnt += iList.size();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setTaaCodeList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}

}