package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmWorkteamMgr;
import com.isu.ifw.mapper.WtmWorkteamMgrMapper;
import com.isu.ifw.repository.WtmWorkteamMgrRepository;

@Service("workteamMgrService")
public class WtmWorkteamMgrServiceImpl implements WtmWorkteamMgrService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Resource
	WtmWorkteamMgrRepository workteamMgrRepository;
	
	@Autowired
	WtmWorkteamMgrMapper workteamMgrMapper;
	
	
	@Override
	public List<Map<String, Object>> getWorkteamMgrList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> searchList = new ArrayList();	
		List<WtmWorkteamMgr> list = workteamMgrRepository.findByTenantIdAndEnterCd(tenantId, enterCd, paramMap.get("sYmd").toString());
		
		for(WtmWorkteamMgr l : list) {
			Map<String, Object> workteam = new HashMap();
			workteam.put("workteamMgrId", l.getWorkteamMgrId());
			workteam.put("workteamNm", l.getWorkteamNm());
			workteam.put("flexibleStdMgrId", l.getFlexibleStdMgrId());
			workteam.put("symd", l.getSymd());
			workteam.put("eymd", l.getEymd());
			workteam.put("note", l.getNote());
			searchList.add(workteam);
		}
		return searchList;
	}
	
	@Override
	public int setWorkteamMgrList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<WtmWorkteamMgr> saveList = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmWorkteamMgr workteam = new WtmWorkteamMgr();
						workteam.setEnterCd(enterCd);
						workteam.setTenantId(tenantId);
						workteam.setUpdateId(userId);
						workteam.setWorkteamMgrId(l.get("workteamMgrId").toString().equals("") ? null : Long.parseLong(l.get("workteamMgrId").toString()));
						workteam.setWorkteamNm(l.get("workteamNm").toString());
						workteam.setEymd(l.get("eymd").toString());
						workteam.setNote(l.get("note").toString());
						workteam.setSymd(l.get("symd").toString());
						workteam.setFlexibleStdMgrId(Long.parseLong(l.get("flexibleStdMgrId").toString()));
						saveList.add(workteam);
					}
					saveList = workteamMgrRepository.saveAll(saveList);
					cnt += saveList.size();
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
		
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				List<WtmWorkteamMgr> deleteList = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmWorkteamMgr workteam = new WtmWorkteamMgr();
						workteam.setWorkteamMgrId(Long.parseLong(l.get("workteamMgrId").toString()));
						deleteList.add(workteam);
					}
					workteamMgrRepository.deleteAll(deleteList);
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
	
	@Override
	public List<Map<String, Object>> getWorkteamCdList(Long tenantId, String enterCd) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		
		return workteamMgrMapper.getWorkteamCdList(paramMap);
	}
}