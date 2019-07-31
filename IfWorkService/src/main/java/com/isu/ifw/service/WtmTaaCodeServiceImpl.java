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

import com.isu.ifw.entity.WtmTaaCode;
import com.isu.ifw.mapper.WtmTaaCodeMapper;
import com.isu.ifw.repository.WtmTaaCodeRepository;

@Service("taaCodeService")
public class WtmTaaCodeServiceImpl implements WtmTaaCodeService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Resource
	WtmTaaCodeRepository taaCodeRepository;

	@Resource
	WtmTaaCodeMapper taaCodeMapper;

	@Override
	public List<Map<String, Object>> getTaaCodeList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		List<Map<String, Object>> codeList = new ArrayList();	
		List<WtmTaaCode> list = taaCodeRepository.findByTenantIdAndEnterCd(tenantId, enterCd, paramMap.get("sData").toString());
		
		for(WtmTaaCode l : list) {
			Map<String, Object> code = new HashMap();
			code.put("taaCd", l.getTaaCd());
			code.put("taaNm", l.getTaaNm());
			code.put("holInclYn", l.getHolInclYn().equals("Y")?"1":"0");
			code.put("workYn", l.getWorkYn().equals("Y")?"1":"0");
			code.put("workApprHour", l.getWorkApprHour());
			code.put("note", l.getTaaNm());
			code.put("taaTypeCd", l.getTaaTypeCd());
			codeList.add(code);
		}
		return codeList;
	}
	
	@Transactional
	@Override
	public int setTaaCodeList(Long tenantId, String enterCd, Map<String, Object> convertMap) {
		logger.debug("setTaaCodeList start");		
		int cnt = 0;
//		List<Map<String, Object>> list = (List<Map<String, Object>>) convertMap.get("mergeRows");
//		
//		if(list != null && list.size() > 0) {
//			for(int i = 0; i < list.size(); i++) {
//				Map<String,Object> mp = (Map<String,Object>)list.get(i);
//				mp.put("enterCd", convertMap.get("enterCd").toString());
//				mp.put("tenantId", convertMap.get("tenantId").toString());
//				mp.put("sabun", convertMap.get("sabun").toString());
//			}
//		}
//		cnt = taaCodeMapper.saveWtmTaaCode(convertMap);
		
		System.out.println("====================================" + cnt);
		return cnt;
	}
}