package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import com.isu.option.vo.ReturnParam;

public class WtmOtApplServiceImpl implements WtmApplService {

	@Override
	public Map<String, Object> getAppl(Long tenantId, String enterCd, Long applId, String sabun,
			Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getApprList(Long tenantId, String enterCd, String empNo,
			Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnParam validate(Long tenantId, String enterCd, Long applId, String workTypeCd,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendPush() {
		// TODO Auto-generated method stub

	}

}
