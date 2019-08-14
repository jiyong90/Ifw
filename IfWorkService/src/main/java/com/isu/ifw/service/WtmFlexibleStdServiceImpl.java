package com.isu.ifw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmFlexibleStdVO;

@Service
public class WtmFlexibleStdServiceImpl implements WtmFlexibleStdService {


	@Autowired
	WtmFlexibleStdMapper flexStdMapper;
	
	@Override
	public List<WtmFlexibleStdVO> getFlexibleStd(Long tenantId, String enterCd, Long userKey) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));
		
		return flexStdMapper.getWtmFlexibleStd(paramMap);
	}

	@Override
	public void saveFlexibleStd(Long tenantId, String enterCd, Map<String, Object> optionMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> getFlexibleStd(Long tenantId, String enterCd) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		
		return flexStdMapper.getWtmFlexibleStdList(paramMap);
	}
	
}
