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
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmOtAppl;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmOtApplMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmOtApplRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmApplLineVO;
import com.isu.option.vo.ReturnParam;

@Service("wtmOtApplService")
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
	WtmWorkCalendarRepository wtmWorkCalendarRepository;
	@Autowired
	WtmFlexibleStdMgrRepository wtmFlexibleStdMgrRepo;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Autowired
	WtmOtApplMapper wtmOtApplMapper;

	
	@Override
	public Map<String, Object> getAppl(Long applId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, Object> getLastAppl(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap,
			Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getApprList(Long tenantId, String enterCd, String empNo,
			Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception { 
		
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, this.APPL_STATUS_APPLY_ING, sabun, userId);
		
		applId = appl.getApplId();

		String ymd = paramMap.get("ymd")+"";
		String otSdate = paramMap.get("otSdate")+"";
		String otEdate = paramMap.get("otEdate")+"";
		String reasonCd = paramMap.get("reasonCd").toString();
		String reason = paramMap.get("reason").toString();

		WtmWorkCalendar calendar = wtmWorkCalendarRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		
		//근무제 신청서 테이블 조회
		WtmOtAppl otAppl = saveWtmOtAppl(tenantId, enterCd, applId, applId, otSdate, otEdate, calendar.getHolidayYn(), reasonCd, reason, sabun, userId);
		
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
	
		paramMap.put("applId", applId);
		wtmOtApplMapper.calcOtMinute(paramMap);
		//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		
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
			
			String ymd = paramMap.get("ymd").toString();
			String otSdate = paramMap.get("otSdate").toString();
			String otEdate = paramMap.get("otEdate").toString();
			String reasonCd = paramMap.get("reasonCd").toString();
			String reason = paramMap.get("reason").toString();
			
			WtmWorkCalendar calendar = wtmWorkCalendarRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
			
			//근무제 신청서 테이블 조회
			WtmOtAppl otAppl = saveWtmOtAppl(tenantId, enterCd, applId, applId, otSdate, otEdate, calendar.getHolidayYn(), reasonCd, reason, sabun, userId);
			
			saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		
			rp.put("applId", appl.getApplId());
			//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		
		} catch(Exception e) {
			throw new Exception("저장 시 오류가 발생했습니다.");
		}
		
		return rp;
	}

	@Override
	public ReturnParam validate(Long tenantId, String enterCd, String sabun, String workTypeCd,
			Map<String, Object> paramMap) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		// TODO Auto-generated method stub
		// 중복 신청은 화면에서 제어 하겠지?
		String ymd = paramMap.get("ymd").toString();
		String otSdate = paramMap.get("otSdate").toString();
		String otEdate = paramMap.get("otEdate").toString();
		
		Date sd = WtmUtil.toDate(otSdate, "yyyyMMddHHmm");
		Date ed = WtmUtil.toDate(otEdate, "yyyyMMddHHmm");
		
		Date chkD = WtmUtil.addDate(sd, 1);

		//연장근무 신청 기간이 1일 이상이어서도 안된다! 미쳐가지고..
		int compare = chkD.compareTo(ed);
		//시작일보다 하루 더한 날과 비교하여 크면 안됨
		if(compare < 0) {
			rp.setFail("연장근무 신청을 하루 이상 신청할 수 없습니다.");
			return rp;
		}
		

		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("tenantId", tenantId);
		
		//연장근무 신청 기간 내에 소정근로 외 다른 근무계획이 있는지 체크 한다.
		paramMap.put("sdate", sd);
		paramMap.put("edate", ed);
		Map<String, Object> resultMap = wtmFlexibleEmpMapper.checkDuplicateWorktime(paramMap);
		//Long timeCdMgrId = Long.parseLong(paramMap.get("timeCdMgrId").toString());
		
		int workCnt = Integer.parseInt(resultMap.get("workCnt").toString());
		if(workCnt > 0) {
			rp.setFail("이미 근무정보(신청중인 근무 포함)가 존재합니다.");
			return rp;
		}
		
		//현재 신청할 연장근무 시간 계산
		resultMap.putAll(wtmFlexibleEmpMapper.calcMinuteExceptBreaktime(paramMap));
		
		Integer calcMinute = Integer.parseInt(resultMap.get("calcMinute").toString());
		resultMap.putAll(wtmFlexibleEmpMapper.getSumOtMinute(paramMap));
		Integer sumOtMinute = Integer.parseInt(resultMap.get("otMinute").toString());
		
		//연장근무 가능 시간을 가지고 오자
		WtmFlexibleEmp emp = wtmFlexibleEmpRepo.findByTenantIdAndEnterCdAndSabunAndYmdBetween(tenantId, enterCd, sabun, ymd);
		
		Integer otMinute = emp.getOtMinute();
		//otMinute 연장근무 가능 시간이 0이면 체크 하지말자 우선!!
		if(otMinute != null && otMinute > 0) {
			int t = (((sumOtMinute!=null)?sumOtMinute:0) + ((calcMinute!=null)?calcMinute:0));
			//연장 가능 시간보다 이미 신청중이거나 연장근무시간과 신청 시간의 합이 크면 안되유.
			if(otMinute < t ) {
				rp.setFail("연장근무 가능시간은 " + otMinute + " 시간 입니다. 신청 가능 시간은 " + (otMinute-((sumOtMinute!=null)?sumOtMinute:0)) + " 시간 입니다. 추가 신청이 필요한 경우 담당자에게 문의하세요" );
			}
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
	
	protected WtmOtAppl saveWtmOtAppl(Long tenantId, String enterCd, Long applId, Long oldOtApplId, String otSdate, String otEdate, String holidayYn, String reasonCd, String reason, String sabun, Long userId) {
		 
		WtmOtAppl otAppl = wtmOtApplRepo.findByApplId(applId);
		if(otAppl == null) {
			otAppl = new WtmOtAppl();
		}
		Date sDate = WtmUtil.toDate(otSdate, "yyyyMMddHHmm");
		otAppl.setApplId(applId);
		otAppl.setYmd(WtmUtil.parseDateStr(sDate, null));
		otAppl.setOtSdate(sDate);
		otAppl.setOtEdate(WtmUtil.toDate(otEdate, "yyyyMMddHHmm"));
		otAppl.setHolidayYn(holidayYn);
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
		

		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
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
		Integer otMinute = emp.getOtMinute();
		if(otMinute == null) {
			otMinute = 0;
		}
		
		//우선 오티 시간이 없으면 체크하지 말자
		if(otMinute > 0) {
			Map<String, Object> rMap = wtmFlexibleEmpMapper.getSumOtMinute(paramMap);
			Integer sumOtMinute = Integer.parseInt(rMap.get("otMinute").toString());
			if(otMinute <= sumOtMinute) {
				rp.setFail("금주 사용가능한 연장근무 시간이 없습니다. 담당자에게 문의하세요.");
				return rp;
			}
		}
		
		
		return rp;
	}

	
}
