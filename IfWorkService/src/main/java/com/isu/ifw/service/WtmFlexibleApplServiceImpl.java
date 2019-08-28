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
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmPropertie;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleApplRepository;
import com.isu.ifw.repository.WtmFlexibleDayPlanRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
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
	WtmFlexibleStdMgrRepository flexStdMgrRepo;
	
	@Autowired
	WtmFlexibleDayPlanRepository wtmFlexibleDayPlanRepo;
	
	/**
	 * 속성값 조회
	 */
	@Autowired
	WtmPropertieRepository wtmPropertieRepo;
	
	@Autowired
	WtmFlexibleEmpRepository wtmFlexibleEmpRepo;
	
	@Autowired
	WtmApplLineRepository wtmApplLineRepo;
	
	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Override
	public Map<String, Object> getAppl(Long applId) {
		return flexApplMapper.findByApplId(applId);
	}
	
	@Override
	public Map<String, Object> getLastAppl(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId) {
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexApplMapper.getLastAppl(paramMap);
	}
	
	@Transactional
	@Override
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap, String status, String sabun, Long userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
			Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
			//신청서 최상위 테이블이다. 
			WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, status, sabun, userId);
			
			applId = appl.getApplId();
			
			String sYmd = paramMap.get("sYmd").toString();
			String eYmd = paramMap.get("eYmd").toString();
			
			//근무제 신청서 테이블 조회
			WtmFlexibleAppl flexibleAppl = saveWtmFlexibleAppl(tenantId, enterCd, applId, flexibleStdMgrId, sYmd, eYmd, "", sabun, userId);
			
			saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		
			rp.put("applId", appl.getApplId());
			rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		
		} catch(Exception e) {
			throw new Exception("저장 시 오류가 발생했습니다.");
		}
		
		return rp;
		
	}
	
	@Transactional
	@Override
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap, String sabun, Long userId) throws Exception {
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		
		
		Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
		String reason = paramMap.get("reason").toString();
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, this.APPL_STATUS_APPLY_ING, sabun, userId);
		
		applId = appl.getApplId();
		
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		
		//근무제 신청서 테이블 조회
		saveWtmFlexibleAppl(tenantId, enterCd, applId, flexibleStdMgrId, sYmd, eYmd, reason, sabun, userId);
		
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		
		paramMap.put("applId", applId);
		ReturnParam rp = validate(tenantId, enterCd, sabun, workTypeCd, paramMap);

		if(rp.getStatus().equals("FAIL")) {
			throw new RuntimeException(rp.get("message").toString());
		}
		
		//결재라인 상태값 업데이트
		//WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
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
	
	
	
	/**
	 * paramMap - apprOpinion 결재 시 의견(optional)
	 */
	@Transactional
	@Override
	public void apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap, String sabun, Long userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp = checkRequestDate(applId);
		if(rp.getStatus().equals("FAIL")) {
			throw new Exception("신청중인 또는 이미 적용된 근무정보가 있습니다.");
		}
		
		//결재라인 상태값 업데이트
		//WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
		List<WtmApplLine> lines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);

		//마지막 결재자인지 확인하자
		boolean lastAppr = false;
		if(lines != null && lines.size() > 0) {
			for(WtmApplLine line : lines) {
				if(line.getApprSeq() == apprSeq && line.getApprSabun().equals(sabun)) {
					line.setApprStatusCd(APPR_STATUS_APPLY);
					line.setApprDate(WtmUtil.parseDateStr(new Date(), null));
					//결재의견
					if(paramMap != null && paramMap.containsKey("apprOpinion")) {
						line.setApprOpinion(paramMap.get("apprOpinion").toString());
						line.setUpdateId(userId);
					}
					line = wtmApplLineRepo.save(line);
					lastAppr = true;
				}else {
					if(lastAppr) {
						line.setApprStatusCd(APPR_STATUS_REQUEST);
						line = wtmApplLineRepo.save(line);
					}
					lastAppr = false;
				}
			}
		}
		
		
		//신청서 메인 상태값 업데이트
		WtmAppl appl = wtmApplRepo.findById(applId).get();
		appl.setApplStatusCd((lastAppr)?APPL_STATUS_APPR:APPL_STATUS_APPLY_ING);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId);
		
		appl = wtmApplRepo.save(appl);
		
		if(lastAppr) {
			//대상자의 실제 근무 정보를 반영한다.
			WtmFlexibleAppl flexibleAppl = wtmFlexibleApplRepo.findByApplId(applId);
			WtmFlexibleEmp emp = new WtmFlexibleEmp();
			
			//사전 체크에서 유연근무끼리의 중복은 이미 막힌다.
			//중복은 기본근무와의 중복이 있다.
			//유연근무제 기간과 중복되는 데이터를 찾아 시작일과 종료일을 갱신해주자 
			/*List<WtmFlexibleEmp> empList = wtmFlexibleEmpRepo.findByTenantIdAndEnterCdAndSabunAndBetweenSymdAndEymd(tenantId, enterCd, appl.getApplSabun(), flexibleAppl.getSymd(), flexibleAppl.getEymd());
			if(empList != null) {
				for(WtmFlexibleEmp e : empList) {
					//신청기간내에 시작 종료가 포함되어있을 경우
					if(Integer.parseInt(flexibleAppl.getSymd()) <= Integer.parseInt(e.getSymd()) && Integer.parseInt(flexibleAppl.getEymd()) >= Integer.parseInt(e.getEymd())) {
						wtmFlexibleEmpRepo.delete(e);
					//시작일만 포함되어있을 경우 
					}else if(Integer.parseInt(flexibleAppl.getSymd()) >= Integer.parseInt(e.getSymd()) && Integer.parseInt(flexibleAppl.getEymd()) < Integer.parseInt(e.getEymd())) {
						//시작일을 신청종료일 다음날로 업데이트 해주자
						e.setSymd(WtmUtil.parseDateStr(WtmUtil.addDate(WtmUtil.toDate(flexibleAppl.getEymd(), ""), 1),null));
						wtmFlexibleEmpRepo.save(e);
					//종료일만 포함되어있을 경우
					}else if(Integer.parseInt(flexibleAppl.getSymd()) > Integer.parseInt(e.getSymd()) && Integer.parseInt(flexibleAppl.getEymd()) <= Integer.parseInt(e.getEymd())) {
						//종료일을 신청시작일 전날로 업데이트 해주자
						e.setEymd(WtmUtil.parseDateStr(WtmUtil.addDate(WtmUtil.toDate(flexibleAppl.getSymd(), ""), -1),null));
						wtmFlexibleEmpRepo.save(e);
						
					//신청 시작일과 종료일이 기존 근무정보 내에 있을 경우 
					}else if(Integer.parseInt(flexibleAppl.getSymd()) > Integer.parseInt(e.getSymd()) && Integer.parseInt(flexibleAppl.getEymd()) < Integer.parseInt(e.getEymd())) {
						String eymd = e.getEymd();
						
						e.setEymd(WtmUtil.parseDateStr(WtmUtil.addDate(WtmUtil.toDate(flexibleAppl.getSymd(), ""), -1),null));
						wtmFlexibleEmpRepo.save(e);
						WtmFlexibleEmp newEmp = new WtmFlexibleEmp();
						newEmp.setFlexibleStdMgrId(e.getFlexibleStdMgrId());
						newEmp.setTenantId(e.getTenantId());
						newEmp.setEnterCd(e.getEnterCd());
						newEmp.setSymd(WtmUtil.parseDateStr(WtmUtil.addDate(WtmUtil.toDate(flexibleAppl.getEymd(), ""), 1),null));
						newEmp.setEymd(eymd);
						newEmp.setUpdateId(userId);
						wtmFlexibleEmpRepo.save(newEmp);
					}
						
				}
			}*/
			emp.setEnterCd(enterCd);
			emp.setTenantId(tenantId);
			emp.setFlexibleStdMgrId(flexibleAppl.getFlexibleStdMgrId());
			emp.setSabun(appl.getApplSabun());
			emp.setSymd(flexibleAppl.getSymd());
			emp.setEymd(flexibleAppl.getEymd());
			emp.setUpdateId(userId);
			emp.setWorkTypeCd(appl.getApplCd());
			
			emp = wtmFlexibleEmpRepo.save(emp);

			paramMap.put("userId", userId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("tenantId", tenantId);
			paramMap.put("sabun", appl.getApplSabun());
			
			wtmFlexibleEmpMapper.initWtmFlexibleEmpOfWtmWorkDayResult(paramMap);
			
			WtmFlexibleStdMgr stdMgr = flexStdMgrRepo.findById(flexibleAppl.getFlexibleStdMgrId()).get();
			paramMap.putAll(stdMgr.getWorkDaysOpt());
			paramMap.put("flexibleEmpId", emp.getFlexibleEmpId());
			//근무제 기간의 총 소정근로 시간을 업데이트 한다.
			flexApplMapper.updateWorkMinuteOfWtmFlexibleEmp(paramMap);
		}
		
		
	}
	
	 
	
	@Transactional
	@Override
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap, String sabun, Long userId)  throws Exception {
		if(paramMap == null || !paramMap.containsKey("apprOpinion") && paramMap.get("apprOpinion").equals("")) {
			throw new Exception("사유를 입력하세요.");
		}
		String apprOpinion = paramMap.get("apprOpinion").toString();
		
		List<WtmApplLine> lines = wtmApplLineRepo.findByApplIdOrderByApprSeqAsc(applId);
		if(lines != null && lines.size() > 0) {
			for(WtmApplLine line : lines) {
				if(line.getApprSeq() <= apprSeq) {
					line.setApprStatusCd(APPR_STATUS_REJECT);
					line.setApprDate("");
					if(line.getApprSeq() == apprSeq) {
						line.setApprOpinion(apprOpinion);
					}
				}else {
					line.setApprStatusCd("");
				}
				line.setUpdateId(userId);
				wtmApplLineRepo.save(line);
			}
		}
		
		WtmAppl appl = wtmApplRepo.findById(applId).get();
		appl.setApplStatusCd(APPL_STATUS_APPLY_REJECT);
		appl.setApplYmd(WtmUtil.parseDateStr(new Date(), null));
		appl.setUpdateId(userId);	
		wtmApplRepo.save(appl);
	}

	
	@Override
	public ReturnParam validate(Long tenantId, String enterCd, String sabun, String workTypeCd, Map<String, Object> paramMap) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Long applId = Long.parseLong(paramMap.get("applId").toString());
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

	protected WtmFlexibleAppl saveWtmFlexibleAppl(Long tenantId, String enterCd, Long applId, Long flexibleStdMgrId, String sYmd, String eYmd, String reason, String sabun, Long userId) {
		 
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
		flexibleAppl.setUpdateId(userId);
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
		rp.setSuccess("");
		Map<String, Object> m = flexStdMapper.checkRequestDate(applId);
		int cnt = Integer.parseInt(m.get("CNT").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
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

	@Override
	public List<Map<String, Object>> getApprList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
		return applMapper.getApprList(paramMap);
	}

	@Override
	public ReturnParam preCheck(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getPrevApplList(Long tenantId, String enterCd, String sabun,
			Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
 

}
