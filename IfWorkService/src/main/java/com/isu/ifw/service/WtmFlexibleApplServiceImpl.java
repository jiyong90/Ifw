package com.isu.ifw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmAppl;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmApplLine;
import com.isu.ifw.entity.WtmFlexibleAppl;
import com.isu.ifw.entity.WtmFlexibleDayPlan;
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmPropertie;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleApplMapper;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleApplRepository;
import com.isu.ifw.repository.WtmFlexibleDayPlanRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.ifw.vo.WtmFlexibleApplVO;
import com.isu.option.vo.ReturnParam;

@Service("wtmFlexibleApplService")
public class WtmFlexibleApplServiceImpl implements WtmApplService {


	@Autowired
	WtmApplMapper applMapper;
	
	@Autowired
	WtmFlexibleApplMapper flexApplMapper;
	
	@Autowired
	WtmFlexibleStdMapper flexStdMapper;

	@Autowired
	WtmApplRepository wtmApplRepo;
	
	@Autowired
	WtmFlexibleApplRepository wtmFlexibleApplRepo;
	
	@Autowired
	WtmFlexibleDayPlanRepository wtmFlexibleDayPlanRepo;
	
	/**
	 * 속성값 조회
	 */
	@Autowired
	WtmPropertieRepository wtmPropertieRepo;
	
	WtmFlexibleEmpRepository wtmFlexibleEmpRepo;
	
	@Autowired
	WtmApplLineRepository wtmApplLineRepo;
	
	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;
	

	@Override
	public WtmFlexibleApplVO getFlexibleAppl(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexApplMapper.getWtmFlexibleAppl(paramMap);
	}

	@Override
	public Map<String, Object> getFlexibleApplImsi(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexApplMapper.getWtmFlexibleApplImsi(paramMap);
	}
	
	@Transactional
	@Override
	public WtmAppl imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap, String sabun) {
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, this.APPL_STATUS_IMSI, sabun);
		
		applId = appl.getApplId();
		
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		
		//근무제 신청서 테이블 조회
		saveWtmFlexibleAppl(tenantId, enterCd, applId, flexibleStdMgrId, sYmd, eYmd, "", sabun);
		
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun);
		
		return appl;
		
		
	}
	
	@Transactional
	@Override
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap, String sabun) throws Exception {
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		
		
		Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
		String reason = paramMap.get("reason").toString();
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, this.APPL_STATUS_APPLY_ING, sabun);
		
		applId = appl.getApplId();
		
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		
		//근무제 신청서 테이블 조회
		saveWtmFlexibleAppl(tenantId, enterCd, applId, flexibleStdMgrId, sYmd, eYmd, reason, sabun);
		
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun);
		

		ReturnParam rp = validate(tenantId, enterCd, applId, workTypeCd, null);

		if(rp.getStatus().equals("FAIL")) {
			throw new RuntimeException(rp.get("message").toString());
		}
		
		 
	}

	protected WtmAppl saveWtmAppl(Long tenantId, String enterCd, Long applId, String workTypeCd, String applStatusCd, String sabun) {
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
		appl.setUpdateId(sabun); 
		//appl.
		
		return wtmApplRepo.save(appl);
	}
	
	
	
	/**
	 * paramMap - apprOpinion 결재 시 의견(optional)
	 */
	@Transactional
	@Override
	public void apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap, String sabun) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp = checkRequestDate(applId);
		if(rp.getStatus().equals("FAIL")) {
			throw new Exception("신청중인 또는 이미 적용된 근무정보가 있습니다.");
		}
		//신청서 메인 상태값 업데이트
		WtmAppl appl = wtmApplRepo.findById(applId).get();
		appl.setApplStatusCd(APPL_STATUS_APPR);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(sabun);
		
		appl = wtmApplRepo.save(appl);
		
		//결재라인 상태값 업데이트
		WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
		line.setApprStatusCd(APPR_STATUS_APPLY);
		line.setApprDate(WtmUtil.parseDateStr(new Date(), null));
		//결재의견
		if(paramMap != null && paramMap.containsKey("apprOpinion")) {
			line.setApprOpinion(paramMap.get("apprOpinion").toString());
		}
		line = wtmApplLineRepo.save(line);
		
		//대상자의 실제 근무 정보를 반영한다.
		WtmFlexibleApplVO flexibleApplVO = getFlexibleAppl(tenantId, enterCd, sabun, paramMap);
		WtmFlexibleEmp emp = new WtmFlexibleEmp();
		emp.setEnterCd(enterCd);
		emp.setTenantId(tenantId);
		emp.setFlexibleStdMgrId(flexibleApplVO.getFlexibleStdMgrId());
		emp.setSymd(flexibleApplVO.getsYmd());
		emp.setEymd(flexibleApplVO.geteYmd());
		emp.setUpdateId(sabun);
		emp.setWorkTypeCd(appl.getApplCd());
		
		emp = wtmFlexibleEmpRepo.save(emp);
		
		
	}
	
	
	@Override
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap, String sabun)  throws Exception {
		ReturnParam rp = new ReturnParam();
		rp = checkRequestDate(applId);
		if(rp.getStatus().equals("FAIL")) {
			throw new Exception("신청중인 또는 이미 적용된 근무정보가 있습니다.");
		}
		//신청서 메인 상태값 업데이트
		WtmAppl appl = wtmApplRepo.findById(applId).get();
		appl.setApplStatusCd(APPL_STATUS_APPR_REJECT);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(sabun);
		
		appl = wtmApplRepo.save(appl);
		
		//결재라인 상태값 업데이트
		WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
		line.setApprStatusCd(APPR_STATUS_REJECT);
		line.setApprDate(WtmUtil.parseDateStr(new Date(), null));
		//결재의견
		if(paramMap != null && paramMap.containsKey("apprOpinion")) {
			line.setApprOpinion(paramMap.get("apprOpinion").toString());
		}
		line = wtmApplLineRepo.save(line);
		
	}

	
	@Override
	public ReturnParam validate(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		//신청 시 날짜 중복되지 않도록 체크 한다.
		rp = checkRequestDate(applId);
		if(rp.getStatus().equals("FAIL")) {
			return rp;
		}
		
		Long flexibleApplId = Long.parseLong(paramMap.get("flexibleApplId").toString());
		List<WtmFlexibleDayPlan> days = wtmFlexibleDayPlanRepo.findByFlexibleApplId(flexibleApplId);
		//근무 상세에 대한 소정근로시간 체크 (탄근제)
		//근무제로 판단하지 않고 신청 시 신청에 딸린 계획데이터가 있을경우 체크하즈아.
		if(days != null && days.size() > 0) {
			WtmPropertie propertie = null;
			String defultWorktime = "8";
			String max2weekWithin = "0";
			String max2weekMorethen = "0";
			String maxAdd = "0";
			propertie = wtmPropertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_DEFAULT_WORKTIME");
			if(propertie != null)
				defultWorktime = propertie.getInfoValue();
			
			propertie = wtmPropertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_MAX_WORKTIME_2WEEK_WITHIN");
			if(propertie != null)
				max2weekWithin = propertie.getInfoValue();
			
			propertie = wtmPropertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_MAX_WORKTIME_2WEEK_MORETHEN");
			if(propertie != null)
				max2weekMorethen = propertie.getInfoValue();
			
			propertie = wtmPropertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_MAX_WORKTIME_ADD");
			if(propertie != null)
				maxAdd = propertie.getInfoValue();
			
			if(workTypeCd.startsWith("SELE")) {
				//선근제
				
			}else if(workTypeCd.equals("ELAS")) {
				//탄근제
				
			}else if(workTypeCd.equals("DIFF")) {
				//시차
				
			}else {
				rp.setFail("");
			}
			//소정근로시간 체크
			//2주이내 48체크	
			//2주이상 52시간 체크
			
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

	protected WtmFlexibleAppl saveWtmFlexibleAppl(Long tenantId, String enterCd, Long applId, Long flexibleStdMgrId, String sYmd, String eYmd, String reason, String sabun) {
		 
		WtmFlexibleAppl flexibleAppl = wtmFlexibleApplRepo.findByApplId(applId);
		if(flexibleAppl == null) {
			flexibleAppl = new WtmFlexibleAppl();
		}
		flexibleAppl.setFlexibleStdMgrId(flexibleStdMgrId);
		flexibleAppl.setApplId(applId);
		String ym = sYmd.substring(0, 4);
		flexibleAppl.setYm(ym);
		flexibleAppl.setSymd(sYmd);
		flexibleAppl.setEymd(eYmd);
		flexibleAppl.setReason(reason);
		flexibleAppl.setUpdateId(sabun);
		//flexibleAppl.setSabun(sabun);
		flexibleAppl.setWorkDay("0");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sYmd", sYmd);
		paramMap.put("eYmd", eYmd);
		if(sYmd != null && !sYmd.equals("") && eYmd != null && !eYmd.equals("")) {
			Map<String, Object> m = applMapper.calcWorkDay(paramMap);
			if(m != null) {
				flexibleAppl.setWorkDay(m.get("WORK_CNT").toString());
			}
		}
		
		return wtmFlexibleApplRepo.save(flexibleAppl);
	}
	
	protected ReturnParam checkRequestDate(Long applId) {
		ReturnParam rp = new ReturnParam();
		Map<String, Object> m = flexStdMapper.checkRequestDate(applId);
		int cnt = Integer.parseInt(m.get("CNT").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
	}
	
	protected void saveWtmApplLine(Long tenantId, String enterCd, int apprLvl, Long applId, String sabun) {
		
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
						applLine.setApprSeq(applLineVO.getApprSeq() + "");
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setApprTypeCd(APPL_LINE_S);
						applLine.setUpdateId(sabun);
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
						applLine.setApprSeq(applLineVO.getApprSeq()+"");
						applLine.setApprSabun(applLineVO.getSabun());
						applLine.setUpdateId(sabun);
						wtmApplLineRepo.save(applLine);
					}
					lineCnt++;
				}
			}
		}
		//결재라인 저장 끝
	}

	@Override
	public List<Map<String, Object>> getFlexibleApprList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
		return applMapper.getApprList(paramMap);
	}

}
