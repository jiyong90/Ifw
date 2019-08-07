package com.isu.ifw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmAppl;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmApplLine;
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmOtAppl;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmOtApplRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.option.vo.ReturnParam;

public class WtmOtApplServiceImpl implements WtmApplService {

	@Autowired
	WtmApplMapper applMapper;
	
	@Autowired
	WtmApplRepository wtmApplRepo;
	/**
	 * 속성값 조회
	 */
	@Autowired
	WtmPropertieRepository wtmPropertieRepo;

	@Autowired
	WtmApplLineRepository wtmApplLineRepo;
	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;
	@Autowired
	WtmOtApplRepository wtmOtApplRepo;
	@Autowired
	WtmFlexibleEmpRepository wtmFlexibleEmpRepo;
	@Autowired
	WtmFlexibleStdMgrRepository wtmFlexibleStdMgrRepo;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;

	
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

	@Transactional
	@Override
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
			Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
			//신청서 최상위 테이블이다. 
			WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, this.APPL_STATUS_IMSI, sabun, userId);
			
			applId = appl.getApplId();
			
			String sYmd = paramMap.get("sYmd").toString();
			String eYmd = paramMap.get("eYmd").toString();
			
			//근무제 신청서 테이블 조회
			WtmOtAppl otAppl = saveWtmOtAppl(tenantId, enterCd, applId, applId, sYmd, eYmd, "", "", sabun, userId);
			
			saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		
			rp.put("applId", appl.getApplId());
			//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		
		} catch(Exception e) {
			throw new Exception("저장 시 오류가 발생했습니다.");
		}
		
		return rp;
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

	protected WtmApplCode getApplInfo(Long tenantId,String enterCd,String applCd) {
		return wtmApplCodeRepo.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
	}
	protected WtmAppl saveWtmAppl(Long tenantId, String enterCd, Long applId, String workTypeCd, String applStatusCd, String sabun, Long userId) {
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
	protected void saveWtmApplLine(Long tenantId, String enterCd, int apprLvl, Long applId, String sabun, Long userId) {
		
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
						applLine.setUpdateId(userId);
						wtmApplLineRepo.save(applLine);
					}
					lineCnt++;
				}
			}
		}
		//결재라인 저장 끝
	}
	
	protected WtmOtAppl saveWtmOtAppl(Long tenantId, String enterCd, Long applId, Long oldOtApplId, String otSdate, String otEdate,String reasonCd, String reason, String sabun, Long userId) {
		 
		WtmOtAppl otAppl = wtmOtApplRepo.findByApplId(applId);
		if(otAppl == null) {
			otAppl = new WtmOtAppl();
		}
		Date sDate = WtmUtil.toDate(otSdate, "yyyyMMddHHmm");
		otAppl.setApplId(applId);
		otAppl.setYmd(WtmUtil.parseDateStr(sDate, null));
		otAppl.setOtSdate(sDate);
		otAppl.setOtEdate(WtmUtil.toDate(otEdate, "yyyyMMddHHmm"));
		otAppl.setReasonCd(reasonCd);
		otAppl.setReason(reason);
		otAppl.setUpdateId(userId);
		
		return wtmOtApplRepo.save(otAppl);
	}

	@Override
	public ReturnParam preCheck(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		String ymd = paramMap.get("ymd").toString();
		
		WtmFlexibleEmp emp = wtmFlexibleEmpRepo.findByTenantIdAndEnterCdAndSabunAndYmdBetween(tenantId, enterCd, sabun, ymd);

		//1. 연장근무 신청 시 소정근로 선 소진 여부를 체크한다.
		WtmFlexibleStdMgr flexibleStdMgr = wtmFlexibleStdMgrRepo.findById(emp.getFlexibleStdMgrId()).get();
		//선 소진 여부
		String exhaustionYn = flexibleStdMgr.getExhaustionYn();
		if(exhaustionYn.equals("Y")) {
			//선소진시
			//코어타임을 제외한 잔여 소정근로시간을 알려준다
			//근무제 기간 내의 총 소정근로 시간
			int workMinute = emp.getWorkMinute();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			
			
			Map<String, Object> resultMap = wtmFlexibleEmpMapper.getTotalApprMinute(paramMap); //totalApprMinute
			resultMap.putAll(wtmFlexibleEmpMapper.getTotalCoretime(paramMap)); //coreHm
			int apprMinute = Integer.parseInt(resultMap.get("totalApprMinute").toString());
			int coreMinute = Integer.parseInt(resultMap.get("coreHm").toString());
			//근무제 기간 내 총 소정근로 시간 > 연장근무신청일 포함 이전일의 인정소정근로시간(인정소정근로시간이 없을 경우 계획소정근로 시간) + 연장근무신청일 이후의 코어타임 시간			
			if(workMinute > apprMinute + coreMinute) {
				int baseWorkMinute = workMinute - apprMinute - coreMinute;
				rp.setFail("필수 근무시간을 제외한 " + baseWorkMinute + "분의 소정근로시간을 선 소진 후 연장근무를 신청할 수 있습니다.");
				return rp;
			}
		}
		
		//2.연장근무 가능시간 초과 체크
		
		
		return rp;
	}

	
}
