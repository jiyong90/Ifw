package com.isu.ifw.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmAppl;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmApplLine;
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmOtAppl;
import com.isu.ifw.entity.WtmOtSubsAppl;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.mapper.WtmOtApplMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmApplLineRepository;
import com.isu.ifw.repository.WtmApplRepository;
import com.isu.ifw.repository.WtmFlexibleApplRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmOtApplRepository;
import com.isu.ifw.repository.WtmOtSubsApplRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
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
	WtmOtSubsApplRepository wtmOtSubsApplRepo;
	@Autowired
	WtmFlexibleEmpRepository wtmFlexibleEmpRepo;
	@Autowired
	WtmWorkCalendarRepository wtmWorkCalendarRepository;
	@Autowired
	WtmFlexibleStdMgrRepository wtmFlexibleStdMgrRepo;
	@Autowired
	WtmWorkDayResultRepository wtmWorkDayResultRepo;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Autowired
	WtmOtApplMapper wtmOtApplMapper;
	
	@Autowired
	WtmFlexibleApplRepository wtmFlexibleApplRepo;

	@Autowired
	WtmFlexibleStdMgrRepository  flexStdMgrRepo;
	
	@Autowired
	WtmFlexibleApplMapper flexApplMapper;
	
	@Autowired
	WtmFlexibleStdMapper wtmFlexibleStdMapper;

	
	@Override
	public Map<String, Object> getAppl(Long applId) {
		try {
			Map<String, Object> otAppl = wtmOtApplMapper.otApplfindByApplId(applId);
			
			if(otAppl!=null && otAppl.get("holidayYn")!=null && "Y".equals(otAppl.get("holidayYn"))) {
				//대체휴일
				if(otAppl.get("subYn")!=null && "Y".equals(otAppl.get("subYn"))) {
					List<Map<String, Object>> otSubsAppls = wtmOtApplMapper.otSubsApplfindByOtApplId(Long.valueOf(otAppl.get("otApplId").toString()));
					if(otSubsAppls!=null && otSubsAppls.size()>0)
						otAppl.put("subs", otSubsAppls);
				}
			}
			
			otAppl.put("applLine", applMapper.getWtmApplLineByApplId(applId));
			
			return otAppl;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<WtmApplLineVO> getApplLine(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId) {
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("tenantId", tenantId);
		return applMapper.getWtmApplLine(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getPrevApplList(Long tenantId, String enterCd, String sabun,
			Map<String, Object> paramMap, Long userId) {
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("tenantId", tenantId);
		return wtmOtApplMapper.getPrevOtSubsApplList(paramMap);
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
		/* 
		ReturnParam rp = new ReturnParam();
		paramMap.put("applId", applId);
		
		String applSabun = paramMap.get("applSabun").toString();
		
		rp = this.validate(tenantId, enterCd, applSabun, "", paramMap);
		
		if(rp.getStatus().equals("FAIL")) {
			throw new Exception(rp.get("message").toString());
		}
		*/
		ReturnParam rp = imsi(tenantId, enterCd, applId, workTypeCd, paramMap, this.APPL_STATUS_APPLY_ING, sabun, userId);
		
		//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		
		//결재라인 상태값 업데이트
		//WtmApplLine line = wtmApplLineRepo.findByApplIdAndApprSeq(applId, apprSeq);
		
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

	@Transactional
	@Override
	public void apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		paramMap.put("applId", applId);
		
		String applSabun = paramMap.get("applSabun").toString();
		
		rp = this.validate(tenantId, enterCd, applSabun, "", paramMap);
		//rp = validate(applId);
		if(rp.getStatus().equals("FAIL")) {
			throw new Exception(rp.get("message").toString());
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
			WtmOtAppl otAppl = wtmOtApplRepo.findByApplId(applId);
			WtmWorkDayResult dayResult = new WtmWorkDayResult();
			dayResult.setApplId(applId);
			dayResult.setTenantId(tenantId);
			dayResult.setEnterCd(enterCd);
			dayResult.setYmd(otAppl.getYmd());
			dayResult.setSabun(appl.getApplSabun());
			dayResult.setPlanSdate(otAppl.getOtSdate());
			dayResult.setPlanEdate(otAppl.getOtEdate());
			dayResult.setPlanMinute(Integer.parseInt(otAppl.getOtMinute()));
			dayResult.setTimeTypeCd(WtmApplService.TIME_TYPE_OT);
			dayResult.setUpdateId(userId);
			
			wtmWorkDayResultRepo.save(dayResult);
		}
		

	}

	@Transactional
	@Override
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap,
			String sabun, Long userId) throws Exception {
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

	@Transactional
	@Override
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap,
			String status, String sabun, Long userId) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		WtmApplCode applCode = getApplInfo(tenantId, enterCd, workTypeCd);
		Long flexibleStdMgrId = Long.parseLong(paramMap.get("flexibleStdMgrId").toString());
		//신청서 최상위 테이블이다. 
		WtmAppl appl = saveWtmAppl(tenantId, enterCd, applId, workTypeCd, status, sabun, userId);
		
		applId = appl.getApplId();
		
		String ymd = paramMap.get("ymd").toString();
		String otSdate = paramMap.get("otSdate").toString();
		String otEdate = paramMap.get("otEdate").toString();
		String reasonCd = paramMap.get("reasonCd").toString();
		String reason = paramMap.get("reason").toString();
		
		WtmWorkCalendar calendar = wtmWorkCalendarRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		
		String subYn = "";
		if(paramMap.containsKey("subYn")) {
			subYn = paramMap.get("subYn")+"";
		}
		//근무제 신청서 테이블 조회
		WtmOtAppl otAppl = saveWtmOtAppl(tenantId, enterCd, applId, applId, otSdate, otEdate, calendar.getHolidayYn(), subYn,  reasonCd, reason, sabun, userId);
		
		//휴일근무 신청 여부
		if(paramMap.containsKey("holidayYn") && paramMap.get("holidayYn").equals("Y")) {
			//대체휴일이면 subYn = Y
			if(paramMap.containsKey("subYn") && paramMap.get("subYn").equals("Y") && paramMap.containsKey("subs")) {
				List<WtmOtSubsAppl> otSubsAppl = wtmOtSubsApplRepo.findByApplId(applId);
				
				wtmOtSubsApplRepo.deleteAll(otSubsAppl);
				
				List<Map<String, Object>> subs = (List<Map<String, Object>>) paramMap.get("subs");
				if(subs != null && subs.size() > 0) {
					Map<String, Object> resultMap = new HashMap<>();
					
					for(Map<String, Object> sub : subs) {
						String subYmd = sub.get("subYmd").toString();
						String subsSdate = sub.get("subsSdate").toString();
						String subsEdate = sub.get("subsEdate").toString();
						
						Date sd = WtmUtil.toDate(subsSdate, "yyyyMMddHHmm");
						Date ed = WtmUtil.toDate(subsEdate, "yyyyMMddHHmm");
						
						WtmOtSubsAppl otSub = new WtmOtSubsAppl();
						otSub.setApplId(applId);
						otSub.setOtApplId(otAppl.getOtApplId());
						otSub.setSubYmd(subYmd);
						otSub.setSubsSdate(sd);
						otSub.setSubsEdate(ed);
						
						String sHm = WtmUtil.parseDateStr(sd, "HHmm");
						String eHm = WtmUtil.parseDateStr(ed, "HHmm");
						paramMap.put("shm", sHm);
						paramMap.put("ehm", eHm);
						paramMap.put("sabun", appl.getApplSabun());
						
						//현재 신청할 연장근무 시간 계산
						resultMap.putAll(wtmFlexibleEmpMapper.calcMinuteExceptBreaktime(paramMap));
						
						otSub.setSubsMinute(resultMap.get("calcMinute").toString());
						otSub.setUpdateId(userId);
						wtmOtSubsApplRepo.save(otSub);
					}
				}
			}
		}
		
		saveWtmApplLine(tenantId, enterCd, Integer.parseInt(applCode.getApplLevelCd()), applId, sabun, userId);
		paramMap.put("applId", appl.getApplId());
		//rp.put("flexibleApplId", flexibleAppl.getFlexibleApplId());
		wtmOtApplMapper.calcOtMinute(paramMap);
		rp.put("applId", appl.getApplId());
			
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
		
		String sHm = WtmUtil.parseDateStr(sd, "HHmm");
		String eHm = WtmUtil.parseDateStr(ed, "HHmm");
		paramMap.put("shm", sHm);
		paramMap.put("ehm", eHm);
		
		//현재 신청할 연장근무 시간 계산
		resultMap.putAll(wtmFlexibleEmpMapper.calcMinuteExceptBreaktime(paramMap));
		
		Integer calcMinute = Integer.parseInt(resultMap.get("calcMinute").toString());
		
		resultMap.putAll(wtmFlexibleEmpMapper.getSumOtMinute(paramMap));
		Integer sumOtMinute = Integer.parseInt(resultMap.get("otMinute").toString());
		
		//회사의 주 시작요일을 가지고 온다.
		paramMap.put("d", ymd);
		Map<String, Object> rMap = wtmFlexibleStdMapper.getRangeWeekDay(paramMap);
		if(rMap == null) {
			rp.setFail("기준 일자 정보가 없습니다. 관리자에게 문의하세요.");
			return rp;
		}
		String symd = rMap.get("symd").toString();
		String eymd = rMap.get("eymd").toString();
		
		Integer subCalcMinute = 0; 
		//대체휴일 정보 가져와서 휴일근무일 경우 같은 주의 대체휴일 만큼 빼줘야한다
		if(paramMap.containsKey("holidayYn") && paramMap.get("holidayYn").equals("Y")
				&& paramMap.containsKey("subYn") && paramMap.get("subYn").equals("Y")
				&& paramMap.containsKey("subs")) {
			
			List<Map<String, Object>> subs = (List<Map<String, Object>>) paramMap.get("subs");
			if(subs != null && subs.size() > 0) {

				for(Map<String, Object> sub : subs) {
					if(!sub.containsKey("subYmd") || !sub.containsKey("subsSdate") || !sub.containsKey("subsEdate")
							|| sub.get("subYmd").equals("") || sub.get("subsSdate").equals("") || sub.get("subsEdate").equals("")) {
						rp.setFail("대체휴일을 선택하셨을 경우 대체휴일의 정보를 모두 입력해야합니다.");
						return rp;
					}
					String subYmd = sub.get("subYmd").toString();
					//같은 주에 있는 대체휴일 시간정보만
					if(Integer.parseInt(subYmd) >= Integer.parseInt(symd) && Integer.parseInt(subYmd) <= Integer.parseInt(eymd)) {
						String subsSdate = paramMap.get("subsSdate").toString();
						String subsEdate = paramMap.get("subsEdate").toString();
						
						Date subSd = WtmUtil.toDate(subsSdate, "yyyyMMddHHmm");
						Date subEd = WtmUtil.toDate(subsEdate, "yyyyMMddHHmm");
						 
						
						String subSHm = WtmUtil.parseDateStr(subSd, "HHmm");
						String subEHm = WtmUtil.parseDateStr(subEd, "HHmm");
						paramMap.put("shm", subSHm);
						paramMap.put("ehm", subEHm);
						
						//현재 신청할 연장근무 시간 계산
						Map<String, Object> subMap = wtmFlexibleEmpMapper.calcMinuteExceptBreaktime(paramMap);
						if(subMap != null && !resultMap.get("calcMinute").equals("")) {
							subCalcMinute += Integer.parseInt(resultMap.get("calcMinute").toString());
						}
					}
					
				}
			}
		}
		calcMinute = calcMinute - subCalcMinute;
		
		boolean weekOtCheck = true;
		
		//연장근무 가능 시간을 가지고 오자
		WtmFlexibleEmp emp = wtmFlexibleEmpRepo.findByTenantIdAndEnterCdAndSabunAndYmdBetween(tenantId, enterCd, sabun, ymd);
		//선근제 이면
		if(emp.getWorkTypeCd().startsWith("SELE")) {
			//1주의 범위가 선근제 기간내에 있는지 체크
			if(Integer.parseInt(symd) >= Integer.parseInt(emp.getSymd() ) && Integer.parseInt(eymd) <= Integer.parseInt(emp.getEymd())) {
				//선근제는 주단위 연장근무 시간을 체크하지 않는다.
				weekOtCheck = false;
			}
		}
		
		
		if(weekOtCheck) {
			paramMap.putAll(rMap);

			//휴일근무의 경우 대체휴일 정보가 같은 주일 경우 퉁친다.	
 			//근데 4시간 일하고 2시간씩 이번주 차주로 나눠쓰면!!!!! 차주꺼는 연장근무 시간으로 본다 써글
			rMap = wtmOtApplMapper.getTotOtMinuteBySymdAndEymd(paramMap);
			int totOtMinute = 0;
			if(rMap != null && rMap.get("totOtMinute") != null && !rMap.get("totOtMinute").equals("")) {
				totOtMinute = Integer.parseInt(rMap.get("totOtMinute")+"");
			}
			Float f = (float) ((totOtMinute + sumOtMinute) / 60);
			if(f > 12) {
				Float ff = (f - f.intValue()) * 60;
				rp.setFail("연장근무 신청 가능 시간은 " + f.intValue() + "시간 " + ff.intValue() + "분 입니다.");
				return rp;
			}
		}
		
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
	
	protected WtmOtAppl saveWtmOtAppl(Long tenantId, String enterCd, Long applId, Long oldOtApplId, String otSdate, String otEdate, String holidayYn, String subYn, String reasonCd, String reason, String sabun, Long userId) {
		 
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
		otAppl.setSubYn(subYn);
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
		
		//회사의 주 시작요일을 가지고 온다.
		paramMap.put("d", ymd);
		Map<String, Object> rMap = wtmFlexibleStdMapper.getRangeWeekDay(paramMap);
		
		String symd = rMap.get("symd").toString();
		String eymd = rMap.get("eymd").toString();
		
		boolean weekOtCheck = true;
		
		//연장근무 가능 시간을 가지고 오자
		//선근제 이면
		if(emp.getWorkTypeCd().startsWith("SELE")) {
			//1주의 범위가 선근제 기간내에 있는지 체크
			if(Integer.parseInt(symd) >= Integer.parseInt(emp.getSymd() ) && Integer.parseInt(eymd) <= Integer.parseInt(emp.getEymd())) {
				//선근제는 주단위 연장근무 시간을 체크하지 않는다.
				weekOtCheck = false;
			}
		}
		
		if(weekOtCheck) {
			paramMap.putAll(rMap);
			
			rMap = wtmOtApplMapper.getTotOtMinuteBySymdAndEymd(paramMap);
			int totOtMinute = 0;
			if(rMap != null && rMap.get("totOtMinute") != null && !rMap.get("totOtMinute").equals("")) {
				totOtMinute = Integer.parseInt(rMap.get("totOtMinute")+"");
			}
			Float f = (float) (totOtMinute / 60);
			if(f > 12) {
				Float ff = (f - f.intValue()) * 60;
				rp.setFail("연장근무 신청 가능 시간은 " + f.intValue() + "시간 " + ff.intValue() + "분 입니다.");
				return rp;
			}
		}
		
		Integer otMinute = emp.getOtMinute();
		if(otMinute == null) {
			otMinute = 0;
		}
		
		//우선 오티 시간이 없으면 체크하지 말자
		if(otMinute > 0) {
			rMap = wtmFlexibleEmpMapper.getSumOtMinute(paramMap);
			Integer sumOtMinute = Integer.parseInt(rMap.get("otMinute").toString());
			if(otMinute <= sumOtMinute) {
				rp.setFail("금주 사용가능한 연장근무 시간이 없습니다. 담당자에게 문의하세요.");
				return rp;
			}
		}
		
		
		return rp;
	}

	
}
