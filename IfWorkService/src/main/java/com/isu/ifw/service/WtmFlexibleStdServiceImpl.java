package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmWorkPattDet;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmWorkPattDetRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmFlexibleStdVO;

@Transactional
@Service
public class WtmFlexibleStdServiceImpl implements WtmFlexibleStdService {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmFlexibleStdMapper flexStdMapper;
	
	@Resource
	WtmFlexibleStdMgrRepository flexibleStdRepository; 
	
	@Resource
	WtmWorkPattDetRepository workPattDetRepository;
	
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
	
	@Override
	public List<Map<String, Object>> getFlexibleStdWorkType(Long tenantId, String enterCd, String workTypeCd) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("workTypeCd", workTypeCd);
		
		return flexStdMapper.getWtmFlexibleStdWorkTypeList(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getStdListWeb(Long tenantId, String enterCd, String ymd) {
		
		List<Map<String, Object>> stdList = new ArrayList();
		List<Map<String, Object>> list = flexibleStdRepository.findByTenantIdAndEnterCdAndYmd(tenantId, enterCd, ymd);
		
		for(Map<String, Object> l : list) {
			Map<String, Object> stdMgr = new HashMap();
			stdMgr.put("flexibleStdMgrId", l.get("FLEXIBLE_STD_MGR_ID"));
			stdMgr.put("tenantId", l.get("TENANT_ID"));
			stdMgr.put("enterCd", l.get("ENTER_CD"));
			stdMgr.put("workTypeCd", l.get("WORK_TYPE_CD"));
			stdMgr.put("flexibleNm", l.get("FLEXIBLE_NM"));
			stdMgr.put("useSymd", l.get("USE_SYMD"));
			stdMgr.put("useEymd", l.get("USE_EYMD"));
			stdMgr.put("note", l.get("NOTE"));
			stdList.add(stdMgr);
		}
		return stdList;
	}
	
	@Override
	public int setStdListWeb(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("insertRows") && ((List)convertMap.get("insertRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("insertRows");
				List<Map<String, Object>> insertList = new ArrayList();	// 추가용
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						Map<String, Object> saveMap = new HashMap();
						saveMap.put("flexibleStdMgrId", l.get("flexibleStdMgrId").toString().equals("") ? null : Long.parseLong(l.get("flexibleStdMgrId").toString()));
						saveMap.put("tenantId", tenantId);
						saveMap.put("enterCd", enterCd);
						String workTypeCd = l.get("workTypeCd").toString();
						saveMap.put("workTypeCd", workTypeCd);
						saveMap.put("flexibleNm", l.get("flexibleNm").toString());
						saveMap.put("useSymd", l.get("useSymd").toString());
						saveMap.put("useEymd", l.get("useEymd").toString());
						if("BASE".equals(workTypeCd) || "WORKTEAM".equals(workTypeCd)) {
							saveMap.put("baseWorkYn", "Y");
						} else {
							saveMap.put("baseWorkYn", "N");
						}
						saveMap.put("note", l.get("note").toString());
						saveMap.put("userId", userId);
						insertList.add(saveMap);
					}
				}
				
				if(insertList != null && insertList.size() > 0) {
					System.out.println("insertList : " + insertList.size());
					cnt = flexStdMapper.insertFlexibleStd(insertList);
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
			
			if(convertMap.containsKey("updateRows") && ((List)convertMap.get("updateRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("updateRows");
				List<Map<String, Object>> updateList = new ArrayList(); // 수정용
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						Map<String, Object> saveMap = new HashMap();
						saveMap.put("flexibleStdMgrId", Long.parseLong(l.get("flexibleStdMgrId").toString()));
						saveMap.put("useEymd", l.get("useEymd").toString());
						saveMap.put("note", l.get("note").toString());
						saveMap.put("userId", userId);
						updateList.add(saveMap);
					}
				}
				
				if(updateList != null && updateList.size() > 0) {
					System.out.println("updateList : " + updateList.size());
					cnt = flexStdMapper.updateFlexibleStd(updateList);
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setWorkPattList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}
	
	@Override
	public List<Map<String, Object>> getWorkPattList(Long flexibleStdMgrId) {
		List<Map<String, Object>> workPattList = new ArrayList();	
		List<WtmWorkPattDet> list = workPattDetRepository.findByFlexibleStdMgrId(flexibleStdMgrId);
		
		for(WtmWorkPattDet l : list) {
			Map<String, Object> workPatt = new HashMap();
			workPatt.put("workPattDetId", l.getWorkPattDetId());
			workPatt.put("flexibleStdMgrId", l.getFlexibleStdMgrId());
			workPatt.put("seq", l.getSeq());
			workPatt.put("timeCdMgrId", l.getTimeCdMgrId());
			workPatt.put("note", l.getNote());
			workPattList.add(workPatt);
		}
		return workPattList;
	}
	
	@Override
	public int setWorkPattList(Long userId, Map<String, Object> convertMap) {
		int cnt = 0;
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<WtmWorkPattDet> saveList = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmWorkPattDet code = new WtmWorkPattDet();
						code.setWorkPattDetId(l.get("workPattDetId").toString().equals("") ? null : Long.parseLong(l.get("workPattDetId").toString()));
						code.setFlexibleStdMgrId(Long.parseLong(l.get("flexibleStdMgrId").toString()));
						code.setSeq(Integer.parseInt(l.get("seq").toString()));
						code.setTimeCdMgrId(Long.parseLong(l.get("timeCdMgrId").toString()));
						code.setNote(l.get("note").toString());
						code.setUpdateId(userId);
						saveList.add(code);
					}
					saveList = workPattDetRepository.saveAll(saveList);
					cnt += saveList.size();
				}
				
				MDC.put("insert cnt", "" + cnt);
			}
		
			if(convertMap.containsKey("deleteRows") && ((List)convertMap.get("deleteRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("deleteRows");
				List<WtmWorkPattDet> delList = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						WtmWorkPattDet code = new WtmWorkPattDet();
						code.setWorkPattDetId(Long.parseLong(l.get("workPattDetId").toString()));
						delList.add(code);
					}
					workPattDetRepository.deleteAll(delList);
				}
				
				MDC.put("delete cnt", "" + iList.size());
				cnt += iList.size();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.warn(e.toString(), e);
		} finally {
			logger.debug("setWorkPattList Service End", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
			MDC.clear();
		}
		return cnt;
	}
	
}
