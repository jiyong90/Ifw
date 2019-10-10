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

import com.isu.ifw.entity.WtmFlexibleApplyMgr;
import com.isu.ifw.mapper.WtmFlexibleApplyMgrMapper;
import com.isu.ifw.repository.WtmFlexibleApplyMgrRepository;

@Service("flexibleApplyMgrService")
public class WtmFlexibleApplyMgrServiceImpl implements WtmFlexibleApplyMgrService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Resource
	WtmFlexibleApplyMgrRepository flexibleApplyRepository;
//	@Resource
//	WtmFlexibleApplyGrpRepository flexibleApplyGrpRepository;
//	@Resource
//	WtmFlexibleApplyEmpRepository flexibleApplyEmpRepository;
	
	@Autowired
	WtmFlexibleApplyMgrMapper wtmFlexibleApplyMgrMapper;

	@Override
	public List<Map<String, Object>> getApplyList(Long tenantId, String enterCd, String sYmd) {
		List<Map<String, Object>> searchList = new ArrayList();	
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sYmd", sYmd);
			searchList =  wtmFlexibleApplyMgrMapper.getApplyList(paramMap);
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("getApplCodeList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		
		return searchList;
	}
	
	@Override
	public int setApplyList(Long tenantId, String enterCd, String userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<WtmFlexibleApplyMgr> codes = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmFlexibleApplyMgr code = new WtmFlexibleApplyMgr();
						code.setFlexibleApplyId(l.get("flexibleApplyId").toString().equals("") ? null : Long.parseLong(l.get("flexibleApplyId").toString()));
						code.setFlexibleStdMgrId(Long.parseLong(l.get("flexibleStdMgrId").toString()));
						code.setTenantId(tenantId);
						code.setEnterCd(enterCd);
						code.setApplyNm(l.get("applyNm").toString());
						code.setUseSymd(l.get("useSymd").toString());
						code.setUseEymd(l.get("useEymd").toString());
						code.setRepeatYn(l.get("repeatYn").toString());
						code.setWorkMinute(l.get("workMinute").toString().equals("") ? null : Integer.parseInt(l.get("workMinute").toString()));
						code.setOtMinute(l.get("otMinute").toString().equals("") ? null : Integer.parseInt(l.get("otMinute").toString()));
						code.setApplyYn(l.get("applyYn").toString());
						code.setNote(l.get("note").toString());
						code.setUpdateId(userId);
						codes.add(code);
					}
					codes = flexibleApplyRepository.saveAll(codes);
					cnt += codes.size();
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
		
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				List<WtmFlexibleApplyMgr> codes = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmFlexibleApplyMgr code = new WtmFlexibleApplyMgr();
						code.setFlexibleApplyId(l.get("flexibleApplyId").toString().equals("") ? null : Long.parseLong(l.get("flexibleApplyId").toString()));
						codes.add(code);
					}
					flexibleApplyRepository.deleteAll(codes);
				}
				
				MDC.put("delete cnt", "" + iList.size());
				cnt += iList.size();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setApplCodeList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}
	
	@Override
	public List<Map<String, Object>> getApplyGrpList(Map<String, Object> paramMap) {
		List<Map<String, Object>> searchList = null;
		try {
			searchList =  wtmFlexibleApplyMgrMapper.getApplyGrpList(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("getApplyGrpList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return searchList;
	}
	
	@Override
	public int setApplyGrpList(String userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<Map<String, Object>> saveList = new ArrayList();	// 추가용
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						Map<String, Object> saveMap = new HashMap();
						saveMap.put("flexibleApplyGroupId", l.get("flexibleApplyGroupId").toString().equals("") ? null : Long.parseLong(l.get("flexibleApplyGroupId").toString()));
						saveMap.put("flexibleApplyId", Long.parseLong(l.get("flexibleApplyId").toString()));
						saveMap.put("orgCd", l.get("orgCd").toString());
						saveMap.put("jobCd", l.get("jobCd").toString());
						saveMap.put("dutyCd", l.get("dutyCd").toString());
						saveMap.put("posCd", l.get("posCd").toString());
						saveMap.put("classCd", l.get("classCd").toString());
						saveMap.put("workteamCd", l.get("workteamCd").toString());
						saveMap.put("note", l.get("note").toString());
						saveMap.put("userId", userId);
						saveList.add(saveMap);
					}
				}
				
				if(saveList != null && saveList.size() > 0) {
					cnt += wtmFlexibleApplyMgrMapper.insertGrp(saveList);
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				if(iList != null && iList.size() > 0) {
					cnt += wtmFlexibleApplyMgrMapper.deleteGrp(iList);
				}
				MDC.put("delete cnt", "" + iList.size());
				cnt += iList.size();
			}
			// 프로시저 호출해서 데이터 재생성하자.
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setApplyGrpList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}
	
	@Override
	public List<Map<String, Object>> getApplyEmpList(Map<String, Object> paramMap) {
		List<Map<String, Object>> searchList = null;
		try {
			searchList =  wtmFlexibleApplyMgrMapper.getApplyEmpList(paramMap);
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("getApplyGrpList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return searchList;
	}
	
	@Override
	public int setApplyEmpList(String userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<Map<String, Object>> saveList = new ArrayList();	// 추가용
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						Map<String, Object> saveMap = new HashMap();
						saveMap.put("flexibleApplyEmpId", l.get("flexibleApplyEmpId").toString().equals("") ? null : Long.parseLong(l.get("flexibleApplyEmpId").toString()));
						saveMap.put("flexibleApplyId", Long.parseLong(l.get("flexibleApplyId").toString()));
						saveMap.put("sabun", l.get("sabun").toString());
						saveMap.put("note", l.get("note").toString());
						saveMap.put("userId", userId);
						saveList.add(saveMap);
					}
				}
				
				if(saveList != null && saveList.size() > 0) {
					cnt += wtmFlexibleApplyMgrMapper.insertGrp(saveList);
				}
				MDC.put("insert cnt", "" + cnt);
			}
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				if(iList != null && iList.size() > 0) {
					cnt += wtmFlexibleApplyMgrMapper.deleteEmp(iList);
				}
				MDC.put("delete cnt", "" + iList.size());
				cnt += iList.size();
			}
			// 프로시저 호출해서 데이터 재생성하자.
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setApplyGrpList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}
}