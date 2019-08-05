package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmBaseWorkMgr;
import com.isu.ifw.entity.WtmTaaCode;
import com.isu.ifw.mapper.WtmTaaCodeMapper;
import com.isu.ifw.repository.WtmBaseWorkMgrRepository;
import com.isu.ifw.repository.WtmCodeRepository;
import com.isu.ifw.repository.WtmTaaCodeRepository;

@Service("baseWorkMgrService")
public class WtmBaseWorkMgrServiceImpl implements WtmBaseWorkMgrService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Resource
	WtmBaseWorkMgrRepository baseWorkRepository;
	
	@Override
	public List<Map<String, Object>> getBaseWorkList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> baseList = new ArrayList();	
		List<WtmBaseWorkMgr> list = baseWorkRepository.findByTenantIdAndEnterCd(tenantId, enterCd, paramMap.get("sYmd").toString());
		
		for(WtmBaseWorkMgr l : list) {
			Map<String, Object> time = new HashMap();
			time.put("baseWorkMgrId", l.getBaseWorkMgrId());
			time.put("businessPlaceCd", l.getBusinessPlaceCd());
			time.put("timeCdMgrId", l.getTimeCdMgrId());
			time.put("symd", l.getSymd());
			time.put("eymd", l.getEymd());
			time.put("note", l.getNote());
			baseList.add(time);
		}
		return baseList;
	}
	
	@Override
	public int setBaseWorkList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<WtmBaseWorkMgr> works = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmBaseWorkMgr work = new WtmBaseWorkMgr();
						work.setEnterCd(enterCd);
						work.setTenantId(tenantId);
						work.setUpdateId(userId);
						work.setBaseWorkMgrId(l.get("baseWorkMgrId").toString().equals("") ? null : Long.parseLong(l.get("baseWorkMgrId").toString()));
						work.setBusinessPlaceCd(l.get("businessPlaceCd").toString());
						work.setEymd(l.get("eymd").toString());
						work.setNote(l.get("note").toString());
						work.setSymd(l.get("symd").toString());
						work.setTimeCdMgrId(Long.parseLong(l.get("timeCdMgrId").toString()));
						works.add(work);
					}
					works = baseWorkRepository.saveAll(works);
					cnt += works.size();
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
		
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				List<WtmBaseWorkMgr> works = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmBaseWorkMgr work = new WtmBaseWorkMgr();
						work.setBaseWorkMgrId(Long.parseLong(l.get("baseWorkMgrId").toString()));
						works.add(work);
					}
					baseWorkRepository.deleteAll(works);
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