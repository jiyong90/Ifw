package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.isu.ifw.entity.WtmCode;
import com.isu.ifw.mapper.WtmCalendarMapper;
import com.isu.ifw.mapper.WtmInOutChangeMapper;

@Service("inOutChangeService")
public class WtmInOutChangeServiceImpl implements WtmInOutChangeService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmInOutChangeMapper inOutChangeMapper;
	
	@Autowired
	WtmCalendarMapper calendarMapper;

	@Autowired
	WtmFlexibleEmpService flexibleEmpService;
	
	@Autowired
	WtmFlexibleEmpService empService;
		
	@Override
	public int setInOutChangeList(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) {
		if(convertMap.containsKey("insertRows") && ((List)convertMap.get("insertRows")).size() > 0) {
			List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("insertRows");
			List<Map<String, Object>> saveList = new ArrayList();
				
			try {
				if(iList != null && iList.size() > 0) {
					saveInout(tenantId, enterCd, userId, iList);
				}
			} catch(Exception e) {
				e.printStackTrace();
				return 0;
			}
			
			for(Map<String, Object> data : iList) {
				empService.calcApprDayInfo(tenantId, enterCd, data.get("ymd").toString(), data.get("ymd").toString(), data.get("sabun").toString());
			}
			
		}
			
		logger.debug("setInOutChangeList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		MDC.clear();
		
		return 1;
	}
	
	@Transactional
	public void saveInout(Long tenantId, String enterCd, Long userId, List<Map<String, Object>> iList) {
		int cnt = 0;
		for(Map<String, Object> data : iList) {
			data.put("tenantId", tenantId);
			data.put("enterCd", enterCd);
			data.put("userId", userId);
			data.put("typeCd", "ADM");
			int n = inOutChangeMapper.setInOutChangeList(data);
			System.out.println("inOutChangeMapper.setInOutChangeList " + n);
			n = calendarMapper.updateEntryDateByAdm(data);
			System.out.println("calendarMapper.updateEntryDateByAdm " + n);

			cnt++;
		}
	}
	
	@Override
	public List<Map<String, Object>> getInpoutChangeHis(Map<String, Object> paramMap) {
		List<Map<String, Object>> list = new ArrayList();
		try {
			list = inOutChangeMapper.getInOutChangeList(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("getInpoutChangeHis Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return list;
	}
}