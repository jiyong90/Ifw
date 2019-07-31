package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
			time.put("businessPlaceCd", l.getBusinessPlaceCd());
			time.put("timeCdMgrId", l.getTimeCdMgrId());
			time.put("symd", l.getSymd());
			time.put("eymd", l.getEymd());
			time.put("note", l.getNote());
			baseList.add(time);
		}
		return baseList;
	}
}