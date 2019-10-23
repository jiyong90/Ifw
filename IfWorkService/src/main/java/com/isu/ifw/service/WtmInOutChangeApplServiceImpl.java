package com.isu.ifw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmAppl;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmApplLine;
import com.isu.ifw.entity.WtmEntryAppl;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmEntryApplMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmEntryApplRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.option.vo.ReturnParam;

@Service("wtmInOutChangeApplService")
public class WtmInOutChangeApplServiceImpl implements WtmApplService {
	
	@Autowired
	WtmApplMapper applMapper;
	
	@Autowired
	WtmEntryApplMapper entryApplMapper;
	
	@Autowired
	WtmApplRepository wtmApplRepo;
	
	@Autowired
	WtmApplLineRepository wtmApplLineRepo;
	
	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;

	@Autowired
	WtmEntryApplRepository wtmEntryApplRepo;
	
	@Override
	public Map<String, Object> getAppl(Long applId) {
		return null;
	}

	@Override
	public List<WtmApplLineVO> getApplLine(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getPrevApplList(Long tenantId, String enterCd, String sabun,
			Map<String, Object> paramMap, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getLastAppl(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap,
			String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getApprList(Long tenantId, String enterCd, String empNo,
			Map<String, Object> paramMap, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String sabun, String userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		
		rp = imsi(tenantId, enterCd, applId, workTypeCd, paramMap, this.APPL_STATUS_APPLY_ING, sabun, userId);
		
		if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
			applId = Long.valueOf(rp.get("applId").toString());
			List<WtmApplLine> lines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
			 
			if(lines != null && lines.size() > 0) {
				for(WtmApplLine line : lines) {
					//첫번째 결재자의 상태만 변경 후 스탑
					line.setApprStatusCd(APPR_STATUS_REQUEST);
					line = wtmApplLineRepo.save(line);
					break;
					 
				}
			}
		}
	}

	@Override
	public ReturnParam apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, String userId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long applId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String status, String sabun, String userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, this.APPL_STATUS_APPLY_ING, sabun, userId);
		
		applId = appl.getApplId();
		
		String ymd = paramMap.get("ymd").toString();
		String planSdate = null;
		if(paramMap.get("planSdate")!=null && !"".equals(paramMap.get("planSdate")))
			planSdate = paramMap.get("planSdate").toString();
		String planEdate = null;
		if(paramMap.get("planEdate")!=null && !"".equals(paramMap.get("planEdate")))
			planEdate = paramMap.get("planEdate").toString();
		String entrySdate = null;
		if(paramMap.get("entrySdate")!=null && !"".equals(paramMap.get("entrySdate")))
			entrySdate = paramMap.get("entrySdate").toString();
		String entryEdate = null;
		if(paramMap.get("entryEdate")!=null && !"".equals(paramMap.get("entryEdate")))
			entryEdate = paramMap.get("entryEdate").toString();
		String chgSdate = null;
		if(paramMap.get("chgSdate")!=null && !"".equals(paramMap.get("chgSdate")))
			chgSdate = paramMap.get("chgSdate").toString();
		String chgEdate = null;
		if(paramMap.get("chgEdate")!=null && !"".equals(paramMap.get("chgEdate")))
			chgEdate = paramMap.get("chgEdate").toString();
		String reason = paramMap.get("reason").toString();
		
		//근태사유서 신청서 저장
		saveInOutChangeAppl(tenantId, enterCd, applId, ymd, planSdate, planEdate, entrySdate, entryEdate, chgSdate, chgEdate, reason, sabun, userId);
		
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		
		paramMap.put("applId", applId);
		

		if(rp.getStatus().equals("FAIL")) {
			throw new RuntimeException(rp.get("message").toString());
		}
		
		return rp;
	}

	@Override
	public ReturnParam preCheck(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnParam validate(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		paramMap.put("sabun", sabun);
		
		Map<String, Object> resultMap = entryApplMapper.checkDuplicateEntryAppl(paramMap);
		
		int chgCnt = Integer.parseInt(resultMap.get("chgCnt").toString());
		if(chgCnt > 0) {
			rp.setFail("이미 신청중인 근태사유서가 존재합니다.");
			return rp;
		}
		
		return rp;
	}

	@Override
	public void sendPush() {
		// TODO Auto-generated method stub
		
	}
	
	protected WtmApplCode getApplInfo(Long tenantId,String enterCd,String applCd) {
		return wtmApplCodeRepo.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
	}
	
	protected WtmEntryAppl saveInOutChangeAppl(Long tenantId, String enterCd, Long applId, String ymd, String planSdate, String planEdate, String entrySdate, String entryEdate, String chgSdate, String chgEdate, String reason, String sabun, String userId) {
		WtmEntryAppl entryAppl = wtmEntryApplRepo.findByApplId(applId);
		if(entryAppl == null) {
			entryAppl = new WtmEntryAppl();
		}
		entryAppl.setApplId(applId);
		entryAppl.setYmd(ymd);
		
		if(planSdate!=null && !"".equals(planSdate))
			entryAppl.setPlanSdate(WtmUtil.toDate(planSdate, "yyyyMMddHHmm"));
		if(planEdate!=null && !"".equals(planEdate))
			entryAppl.setPlanEdate(WtmUtil.toDate(planEdate, "yyyyMMddHHmm"));
		if(entrySdate!=null && !"".equals(entrySdate))
			entryAppl.setEntrySdate(WtmUtil.toDate(entrySdate, "yyyyMMddHHmm"));
		if(entryEdate!=null && !"".equals(entryEdate))
			entryAppl.setEntryEdate(WtmUtil.toDate(entryEdate, "yyyyMMddHHmm"));
		if(chgSdate!=null && !"".equals(chgSdate))
			entryAppl.setChgSdate(WtmUtil.toDate(chgSdate, "yyyyMMddHHmm"));
		if(chgEdate!=null && !"".equals(chgEdate))
			entryAppl.setChgEdate(WtmUtil.toDate(chgEdate, "yyyyMMddHHmm"));
		
		entryAppl.setUpdateId(userId);
		
		return wtmEntryApplRepo.save(entryAppl);
	}

	protected WtmAppl saveWtmAppl(Long tenantId, String enterCd, Long applId, String workTypeCd, String applStatusCd, String sabun, String userId) {
		WtmAppl appl = null;
		if(applId != null && !applId.equals("")) {
			appl = wtmApplRepo.findById(applId).get();
		}else {
			appl = new WtmAppl();
		}
		appl.setApplStatusCd(applStatusCd);
		appl.setTenantId(tenantId);
		appl.setEnterCd(enterCd);
		appl.setApplInSabun(sabun);
		appl.setApplSabun(sabun);
		appl.setApplCd(workTypeCd);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId); 
		//appl.
		
		return wtmApplRepo.save(appl);
	}
	
	protected void saveWtmApplLine(Long tenantId, String enterCd, int apprLvl, Long applId, String sabun, String userId) {
		
		//결재라인 저장
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<WtmApplLine> applLines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("tenantId", tenantId);
		paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));
		//결재라인 조회 기본으로 3단계까지 가져와서 뽑아  쓰자
		List<WtmApplLineVO> applLineVOs = applMapper.getWtmApplLine(paramMap);
		//기본 결재라인이 없으면 저장도 안됨.
		if(applLineVOs != null && applLineVOs.size() > 0){

			//결재라인 코드는 1,2,3으로 되어있다 이렇게 써야한다!!!! 1:1단계, 2:2단계, 3:3단계
			int applCnt = apprLvl;
			
			//기 저장된 결재라인과 비교
			if(applLines != null && applLines.size() > 0) {
				int whileLoop = 0;
				
				for(WtmApplLine applLine : applLines) {
					
					WtmApplLineVO applLineVO = applLineVOs.get(whileLoop);
					if(whileLoop < applCnt) {
						applLine.setApplId(applId);
						applLine.setApprSeq(applLineVO.getApprSeq());
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setApprTypeCd(APPL_LINE_S);
						applLine.setUpdateId(userId);
						wtmApplLineRepo.save(applLine);
					}else {
						//기존 결재라인이 더 많으면 지운다. 임시저장이니.. 바뀔수도 있을 것 같아서..
						wtmApplLineRepo.delete(applLine);
					}
					whileLoop++;
				} 
			}else {
				//신규생성
				int lineCnt = 0; 
				for(WtmApplLineVO applLineVO : applLineVOs) {
					if(lineCnt < applCnt) {
						WtmApplLine applLine = new WtmApplLine();
						applLine.setApplId(applId);
						applLine.setApprSeq(applLineVO.getApprSeq());
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setApprTypeCd(APPL_LINE_S);
						applLine.setUpdateId(userId);
						wtmApplLineRepo.save(applLine);
					}
					lineCnt++;
				}
			}
		}
		//결재라인 저장 끝
	}
	
	
}
