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

import com.isu.ifw.entity.WtmCode;
import com.isu.ifw.entity.WtmTaaCode;
import com.isu.ifw.repository.WtmCodeRepository;
import com.isu.ifw.repository.WtmTaaCodeRepository;

@Service("codeService")
public class WtmCodeServiceImpl implements WtmCodeService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Resource
	WtmCodeRepository codeRepository;

	@Override
	public List<Map<String, Object>> getCodeList(Long tenantId, String enterCd, String grpCodeCd) {
		List<Map<String, Object>> codeList = new ArrayList();	
		List<WtmCode> list = codeRepository.findByTenantIdAndEnterCd(tenantId, enterCd, grpCodeCd);
		
		for(WtmCode l : list) {
			Map<String, Object> code = new HashMap();
			code.put("codeId", l.getCodeId());
			code.put("grpCodeCd", l.getGrpCodeCd());
			code.put("code", l.getCodeCd());
			code.put("codeNm", l.getCodeNm());
			code.put("symd", l.getSymd());
			code.put("eymd", l.getEymd());
			code.put("note", l.getNote());
			code.put("tenantId", l.getTenantId());
			code.put("enterCd", l.getEnterCd());
			codeList.add(code);
		}
		return codeList;
	}
}