package com.isu.ifw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmInOutChangeMapper;

@Service("inOutChangeService")
public class WtmInOutChangeServiceImpl implements WtmInOutChangeService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmInOutChangeMapper inOutChangeMapper;
	
	@Autowired
	WtmFlexibleEmpService flexibleEmpService;
	
	@Override
	public int setInOutChangeList(Long tenantId, String enterCd, Long userId) {
		int cnt = 0;
		try {
			// 대상조회
			Map <String,Object> paramMap = new HashMap<String, Object>();
			//tenant 어디서 가져올지
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			List<Map<String, Object>> list = inOutChangeMapper.getInOutChangeList(paramMap);
			
			// 루프
			for(int i=0; i<list.size(); i++) {
				
				String ymd = (String) list.get(i).get("YMD");
				String sabun = (String) list.get(i).get("SABUN");

				flexibleEmpService.calcApprDayInfo(tenantId, enterCd, ymd, ymd, sabun);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setInOutChangeList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}
}