package com.isu.ifw.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.type.TimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmFlexibleApplDet;
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmOtCanAppl;
import com.isu.ifw.entity.WtmOtSubsAppl;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.repository.WtmFlexibleApplDetRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmOtCanApplRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
import com.isu.ifw.repository.WtmWorkteamEmpRepository;
import com.isu.ifw.repository.WtmWorkteamMgrRepository;
import com.isu.ifw.vo.WtmDayPlanVO;
import com.isu.ifw.vo.WtmDayWorkVO;
import com.isu.option.vo.ReturnParam;

@Service("flexibleEmpService")
public class WtmFlexibleEmpServiceImpl implements WtmFlexibleEmpService {

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	WtmFlexibleStdMapper flexStdMapper;
	
	@Autowired
	WtmFlexibleEmpRepository flexEmpRepo;
	
	@Autowired
	WtmFlexibleStdMgrRepository flexStdMgrRepo;
	
	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;
	
	@Autowired
	WtmWorkDayResultRepository workDayResultRepo;
	
	@Autowired
	WtmOtCanApplRepository otCanApplRepo;
	
	@Autowired
	WtmFlexibleApplDetRepository flexApplDetRepo;
	
	@Override
	public List<Map<String, Object>> getFlexibleEmpList(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, String userId) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		List<Map<String, Object>> flexibleList = flexEmpMapper.getFlexibleEmpList(paramMap);
		if(flexibleList!=null && flexibleList.size()>0) {
			for(Map<String, Object> flex : flexibleList) {
				if(flex.containsKey("flexibleEmpId") && flex.get("flexibleEmpId")!=null && !"".equals(flex.get("flexibleEmpId"))) {
					paramMap.put("flexibleEmpId", Long.valueOf(flex.get("flexibleEmpId").toString()));
					List<Map<String, Object>> plans = flexEmpMapper.getWorktimePlanByYmdBetween(paramMap);
					flex.put("flexibleEmp", getDayWorks(plans, userId));
				}
			}
		}
		
		return flexibleList;
	}
	
	public Map<String, Object> getFlexibleEmp(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, String userId) {
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexEmpMapper.getFlexibleEmp(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getFlexibleEmpListForPlan(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, String userId) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		//paramMap.put("ymd", WtmUtil.parseDateStr(new Date(), null));
		paramMap.put("ymd", paramMap.get("ymd").toString());
		
		List<Map<String, Object>> flexibleEmpList = flexEmpMapper.getFlexibleEmpListForPlan(paramMap);
		if(flexibleEmpList!=null && flexibleEmpList.size()>0) {
			for(Map<String, Object> flexibleEmp : flexibleEmpList) {
				if(flexibleEmp.get("flexibleEmpId")!=null && !"".equals(flexibleEmp.get("flexibleEmpId"))) {
					List<Map<String, Object>> plans = flexEmpMapper.getWorktimePlan(Long.valueOf(flexibleEmp.get("flexibleEmpId").toString()));
					List<WtmDayWorkVO> dayWorks = getDayWorks(plans, userId);
					flexibleEmp.put("dayWorks", dayWorks);
				}
			}
		}
		
		return flexibleEmpList;
	}
	
	@Override
	public Map<String, Object> getWorkDayResult(Long tenantId, String enterCd, String sabun, String ymd, String userId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		//출퇴근 타각 정보
		WtmWorkCalendar workCalendar = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		/*Map<String, Object> entry = new HashMap<String, Object>();
		if(workCalendar!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if(workCalendar.getEntrySdate()!=null && !"".equals(workCalendar.getEntrySdate()))
				entry.put("entrySdate", sdf.format(workCalendar.getEntrySdate()));
			if(workCalendar.getEntryStypeCd()!=null && !"".equals(workCalendar.getEntryStypeCd()))
				entry.put("entryStypeCd", workCalendar.getEntryStypeCd());
			if(workCalendar.getEntryEdate()!=null && !"".equals(workCalendar.getEntryEdate()))
				entry.put("entryEdate", sdf.format(workCalendar.getEntryEdate()));
			if(workCalendar.getEntryStypeCd()!=null && !"".equals(workCalendar.getEntryStypeCd()))
				entry.put("entryStypeCd", workCalendar.getEntryStypeCd());
			if(workCalendar.getEntryEtypeCd()!=null && !"".equals(workCalendar.getEntryEtypeCd()))
				entry.put("entryEtypeCd", workCalendar.getEntryEtypeCd());
			
		}
		result.put("entry", entry);*/
		
		String holidayYn = "";
		if(workCalendar!=null && workCalendar.getHolidayYn()!=null)
			holidayYn = workCalendar.getHolidayYn();
		
		result.put("holidayYn", holidayYn);
		result.put("entry", workCalendar);
		
		//근태, 근무 정보
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("ymd", ymd);
		
		List<Map<String, Object>> workDayResult = flexEmpMapper.getWorkDayResult(paramMap);
		
		//취소 신청서 있는지 조회
		if(workDayResult!=null && workDayResult.size()>0) {
			for(Map<String, Object> r : workDayResult) {
				if(r.get("applId")!=null && !"".equals(r.get("applId"))) {
					WtmOtCanAppl otCanAppl = otCanApplRepo.findByOtApplId(Long.valueOf(r.get("applId").toString()));
					if(otCanAppl!=null && otCanAppl.getOtCanApplId()!=null) {
						r.put("otCanApplId", otCanAppl.getOtCanApplId());
					}
				}
			}
		}
		/*Map<String, Object> dayResults = new HashMap<String, Object>();
		if(result!=null && result.size()>0) {
			for(Map<String, Object> r : workDayResult) {
				List<Map<String, Object>> dayResult = null;
				String date = r.get("ymd").toString();
				if(dayResults.containsKey(date)) {
					dayResult = (List<Map<String, Object>>)dayResults.get(date);
				} else {
					dayResult = new ArrayList<Map<String, Object>>();
				}
				
				dayResult.add(r);
				dayResults.put(ymd, dayResult);
			}
		}*/
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			result.put("dayResults", mapper.writeValueAsString(workDayResult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> getFlexibleRangeInfo(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) {
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexEmpMapper.getFlexibleRangeInfo(paramMap);
	}
	
	@Override
	public Map<String, Object> getFlexibleDayInfo(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) {
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("taaTimeYn", "");
		paramMap.put("taaWorkYn", "");
		
		Map<String, Object> flexEmp = flexEmpMapper.getFlexibleEmp(paramMap);
		if(flexEmp!=null) {
			if(flexEmp.get("taaTimeYn")!=null) //근태시간포함여부
				paramMap.put("taaTimeYn", flexEmp.get("taaTimeYn").toString());
			if(flexEmp.get("taaWorkYn")!=null) //근태일 근무여부
				paramMap.put("taaWorkYn", flexEmp.get("taaWorkYn").toString());
		}
		
		return flexEmpMapper.getFlexibleDayInfo(paramMap);
	}
	
	@Override
	public Map<String, Object> getFlexibleWorkTimeInfo(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) {
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexEmpMapper.getFlexibleWorkTimeInfo(paramMap);
	}
	
	@Override
	public Map<String, Object> getPrevFlexible(Long tenantId, String enterCd, String empNo) {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		//paramMap.put("today", WtmUtil.parseDateStr(new Date(), null));
		
		return flexEmpMapper.getPrevFlexible(paramMap);
	}
	
	public void imsi(Long tenantId, Long enterCd, String sabun, Long flexibleApplId, Map<String, Object> dateMap, String userId) throws Exception{
		
		flexEmpMapper.createFlexibleApplDet(flexibleApplId, userId);

		if(dateMap != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			for(String k : dateMap.keySet()) {
				WtmFlexibleApplDet result =  flexApplDetRepo.findByFlexibleApplIdAndYmd(flexibleApplId, k);
				try {
					Map<String, String> dayResult = (Map<String, String>) dateMap.get(k);
					if(dayResult.get("shm") != null && !dayResult.get("shm").equals("")) {
						String shm = dayResult.get("shm");
						String ehm = dayResult.get("ehm");
						Date s = sdf.parse(k+shm);
						Date e = sdf.parse(k+ehm);
						
						if(s.compareTo(e) > 0) {
							// 날짜 더하기
					        Calendar cal = Calendar.getInstance();
					        cal.setTime(e);
					        cal.add(Calendar.DATE, 1);
					        e = cal.getTime();
						}
						result.setPlanSdate(s);
						result.setPlanEdate(e);
						Map<String, Object> paramMap = new HashMap<>();
						paramMap.put("shm", shm);
						paramMap.put("ehm", ehm);
						paramMap.put("tenantId", tenantId);
						paramMap.put("enterCd", enterCd);
						paramMap.put("sabun", sabun);
						paramMap.put("ymd", result.getYmd());
						
						Map<String, Object> planMinuteMap = flexEmpMapper.calcMinuteExceptBreaktime(paramMap);
						result.setPlanMinute(Integer.parseInt(planMinuteMap.get("calcMinute")+""));
					}else {
						result.setPlanSdate(null);
						result.setPlanEdate(null);
						result.setPlanMinute(0);
					}
					flexApplDetRepo.save(result);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//특정한 주의 근로시간은 52시간을, 특정일의 근로시간은 12시간을 초과할 수 없음(연장 . 휴일근로시간 제외)
		}
		
	}
	
	@Transactional
	@Override
	public ReturnParam save(Long flexibleEmpId, Map<String, Object> dateMap, String userId) throws Exception{
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		WtmFlexibleEmp emp =  flexEmpRepo.findById(flexibleEmpId).get();
		flexEmpMapper.createWorkCalendarOfSeleC(flexibleEmpId, userId);

		rp.put("sabun", emp.getSabun());
		
		if(dateMap != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			for(String k : dateMap.keySet()) {
				WtmWorkDayResult result =  workDayResultRepo.findByTimeTypeCdAndTenantIdAndEnterCdAndSabunAndYmd(WtmApplService.TIME_TYPE_BASE, emp.getTenantId(), emp.getEnterCd(), emp.getSabun(), k);
				if(result == null) {
					result = new WtmWorkDayResult();
					//부모키 가져오기
					WtmWorkCalendar c = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd( emp.getTenantId(), emp.getEnterCd(), emp.getSabun(), k);
					result.setTenantId(c.getTenantId());
					result.setEnterCd(c.getEnterCd());
					result.setYmd(c.getYmd());
					result.setSabun(c.getSabun());
				}
				try {
					Map<String, String> dayResult = (Map<String, String>) dateMap.get(k);
					if(dayResult.get("shm") != null && !dayResult.get("shm").equals("")) {
						String shm = dayResult.get("shm");
						String ehm = dayResult.get("ehm");
						Date s = sdf.parse(k+shm);
						Date e = sdf.parse(k+ehm);
						
						if(s.compareTo(e) > 0) {
							// 날짜 더하기
					        Calendar cal = Calendar.getInstance();
					        cal.setTime(e);
					        cal.add(Calendar.DATE, 1);
					        e = cal.getTime();
						}
						result.setPlanSdate(s);
						result.setPlanEdate(e);
						Map<String, Object> paramMap = new HashMap<>();
						paramMap.put("shm", shm);
						paramMap.put("ehm", ehm);
						paramMap.put("tenantId", emp.getTenantId());
						paramMap.put("enterCd", emp.getEnterCd());
						paramMap.put("sabun", emp.getSabun());
						paramMap.put("ymd", result.getYmd());
						
						Map<String, Object> planMinuteMap = flexEmpMapper.calcMinuteExceptBreaktime(paramMap);
						result.setPlanMinute(Integer.parseInt(planMinuteMap.get("calcMinute")+""));
					}else {
						result.setPlanSdate(null);
						result.setPlanEdate(null);
						result.setPlanMinute(0);
					}
					result.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
					result.setUpdateId(userId);
					workDayResultRepo.save(result);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			WtmFlexibleStdMgr stdMgr = flexStdMgrRepo.findById(emp.getFlexibleStdMgrId()).get();
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.putAll(stdMgr.getWorkDaysOpt());
			paramMap.put("flexibleEmpId", flexibleEmpId);
			paramMap.put("userId", userId);
			
			ObjectMapper mapper  = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(paramMap));
			//holidayYn 갱신
			//flexEmpMapper.updateHolidayYnOFWorkCalendar(paramMap);
			//planMinute갱신
			//flexEmpMapper.updatePlanMinute(flexibleEmpId);
			
			Map<String, Object> result = flexEmpMapper.checkBaseWorktime(flexibleEmpId);
			if(result!=null && result.get("isValid")!=null && result.get("isValid").equals("0")) {
				throw new RuntimeException(result.get("totalWorktime").toString() + "시간의 소정근로시간을 넘을 수 없습니다.");
			}
		}
		
		//저장된 데이터를 기반으로 유효성 검사 진행
		//select CEIL( F_WTM_TO_DAYS(E.SYMD, E.EYMD) * 40 / 7) from WTM_FLEXIBLE_EMP E;
		
		return rp;
	}

	@Override
	//public List<WtmDayWorkVO> getDayWorks(Long flexibleEmpId, Long userId) {
	public List<WtmDayWorkVO> getDayWorks(List<Map<String, Object>> plans, String userId) {
		//List<Map<String, Object>> plans = flexEmpMapper.getWorktimePlan(flexibleEmpId);
		
		Map<String, Object> imsiMap = new HashMap<>();
		
		if(plans != null && plans.size() > 0) {
			WtmDayWorkVO work = new WtmDayWorkVO();
			for(Map<String, Object> plan : plans) {
				String ymd = plan.get("ymd").toString();
				String holidayYn = "";
				if(plan.containsKey("holidayYn") && plan.get("holidayYn") != null) {
					holidayYn = plan.get("holidayYn").toString();
				}
				String timeNm = "";
				if(plan.containsKey("timeNm") && plan.get("timeNm") != null) {
					timeNm = plan.get("timeNm").toString();
				}
				
				String shm = "";
				if(plan.containsKey("shm") && plan.get("shm") != null) {
					shm = plan.get("shm").toString();
				}
				String ehm = "";
				if(plan.containsKey("ehm") && plan.get("ehm") != null) {
					ehm = plan.get("ehm").toString();
				}
				String m = "";
				Float H = 0f;
				Float i = 0f;
				if(plan.containsKey("minute") && plan.get("minute") != null) {
					m = plan.get("minute").toString();
					H = Float.parseFloat(m)/60;
					i = (H - H.intValue()) * 60;
				}
				String taaCd =  "";
				if(plan.containsKey("taaCd") && plan.get("taaCd") != null) {
					 taaCd = plan.get("taaCd").toString();
				}
				String taaNm =  "";
				if(plan.containsKey("taaNm") && plan.get("taaNm") != null) {
					taaNm = plan.get("taaNm").toString();
				}
				
				List<WtmDayPlanVO> planVOs = new ArrayList<>();
				Map<String, Object> dtMap = new HashMap<>();
				if(imsiMap.containsKey(ymd)) {
					dtMap = (Map<String, Object>) imsiMap.get(ymd);
					planVOs = (List<WtmDayPlanVO>) dtMap.get("plan");

				}
				WtmDayPlanVO planVO = new WtmDayPlanVO();
				planVO.setKey(ymd);
				if(taaNm != null && !taaNm.equals("")) {
					planVO.setLabel(taaNm);
				}else{
					planVO.setLabel(shm + "~" + ehm + "("+H.intValue()+"시간"+((i.intValue()>0)?i.intValue()+"분":"")+")");
				}
				
				Map<String, Object> valueMap = new HashMap<>();
				valueMap.put("shm", shm);
				valueMap.put("ehm", ehm);
				valueMap.put("m", m);
				valueMap.put("taaNm", taaNm);
				valueMap.put("taaCd", taaCd);
				planVO.setValueMap(valueMap);
				planVOs.add(planVO);

				dtMap.put("holidayYn", holidayYn);
				dtMap.put("timeNm", timeNm);
				dtMap.put("plan", planVOs);
				
				imsiMap.put(ymd, dtMap);
			}
		}
		List<WtmDayWorkVO> works = new ArrayList<WtmDayWorkVO>();
		for(String k : imsiMap.keySet()) {
			WtmDayWorkVO workVO = new WtmDayWorkVO();
			workVO.setDay(k);
			Map<String, Object> dtMap = (Map<String, Object>) imsiMap.get(k);
			workVO.setHolidayYn(dtMap.get("holidayYn").toString());
			workVO.setTimeNm(dtMap.get("timeNm").toString());
			workVO.setPlans((List<WtmDayPlanVO>)dtMap.get("plan"));
			works.add(workVO);
		}
		return works;
	}

	@Override
	public void updEntrySdate(Long tenantId, String enterCd, String sabun, String ymd, String entryTypeCd, Date sdate, String userId) {
		WtmWorkCalendar calendar = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		calendar.setEntryStypeCd(entryTypeCd);
		calendar.setEntrySdate(sdate); 
		calendar.setUpdateId(userId);
		workCalendarRepo.save(calendar);
	}

	@Override
	public void updEntryEdate(Long tenantId, String enterCd, String sabun, String ymd, String entryTypeCd, Date edate, String userId) {
		
		WtmWorkCalendar calendar = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		calendar.setEntryEtypeCd(entryTypeCd);
		calendar.setEntryEdate(edate); 
		calendar.setUpdateId(userId);
		workCalendarRepo.save(calendar);
		//트랜젝션을 묶지 않는다.
		//타각은 타각대로 저장이 저장적으로 이루어져야 한다.
		
	}
	
	/**
	 * 타각시간 기준으로 인정시간 계산
	 */
	@Transactional
	@Override
	public void calcApprDayInfo(Long tenantId, String enterCd, String sYmd, String eYmd, String sabun) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("sYmd", sYmd);
		paramMap.put("eYmd", eYmd);
//		paramMap.put("timeTypeCd", timeTypeCd);
		//소정근로시간의 경우 출퇴근 타각기록으로만 판단
		flexEmpMapper.updateApprDatetimeByYmdAndSabun(paramMap);
		flexEmpMapper.updateApprMinuteByYmdAndSabun(paramMap);
		//연장근로의 경우 히스토리 타각기록을 통해 계산이 필요함
	}

	@Override
	public void workClosed(Long tenantId, String enterCd, String sabun, String ymd, String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelWorkClosed(Long tenantId, String enterCd, String sabun, String ymd, String userId) {
		// TODO Auto-generated method stub
		
	}
	
	@Autowired
	WtmWorkteamMgrRepository wtmWorkteamMgrRepo;
	
	@Autowired
	WtmWorkteamEmpRepository wtmWorkteamEmpRepo;
	
	
	@Transactional
	@Override
	public void createWorkteamEmpData(Long tenantId, String enterCd, Long workteamMgrId, String userId) {
		//ID 채번 때문에 프로시저로 못했다..
		/*
		 * 중복된 근무 체크
		 * 기본근무제는 제외한 나머지는 다 중복되면 안된다. 근무조 및 유연근무제 기간과 중복되면 중복되는 데이터를 선 조정후 가능하다.
		 * 
		 */
		
		/*
		 * 근무조의 시작일 종료일이 바꼈을 경우
		 * WTM_WORK_FLEXIBLE_EMP 테이블의 근무일의 시작/종료일이 다를 경우 데이터를 다시 만들어야한다.
		 * 예를 들어 근무조의 종료일이 줄었을 경우 그 갭을 기본근무정보로 업데이트 하자
		 * 근무정보 업데이트는 반드시 지우면 안된다 실적데이터가 같이 있기때문에 BASE PLAN 정보를 갱신한다
		 * WTM_WORK_FLEXIBLE_EMP 의 시작 종료일을 겹치지 않게 재생성 한다.
			 
		*/
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("workteamMgrId", workteamMgrId);
		paramMap.put("updateId", userId);
		paramMap.put("pId", userId);
		paramMap.put("result", "");
		
		//1. FLEXIBLE_EMP에 없는 사람 부터 넣자
		/*
		 * 근무조에 등록된 대상자를 WTM_FLEXIBLE_EMP 테이블에 생성한다.
		 * 단 근무조 기간에 중복된 데이터가 있는 대상자는 제외하고 등록한다.
		 * 근무조, 유연근무제 모두 중복된 데이터로 한다. 근무조는 근무조 관리에서 시작종료를 관리하기 때문에 선 수정 작업 
		 * 기본근무제는 체크하지 않고 중복데이터 삽입 - 이후에 FLEXIBLE_EMP의 시작종료일 정리하는 업데이트문이 필요하다
		 */
		flexEmpMapper.createWorkteamOfWtmFlexibleEmp(paramMap);
		
		//case 단순히 근무조의 종료일만 증가했을 경우 1번의 케이스에서 빠졌을 것이다. 업데이트 해줘야한다. 기준아이디가 같으면 업데이트 하자
		//기간의 중복은 입력 자체에서 막아야한다!!!!!!!!!!!!!!!
		flexEmpMapper.updateWorkteamOfWtmFlexibleEmp(paramMap);
		
		flexEmpMapper.createWtmWorkteamOfWtmWorkDayResult(paramMap);
		 
		// 근무조 대상자 가져왓!
		//List<WtmWorkteamEmp> workteamEmpList = wtmWorkteamEmpRepo.findByWorkteamMgrId(workteamMgrId);
		// 패턴 가져왓!
		// 이 모든걸 프로시져로..
		
		// loop를 돌며 대상자 별로 calendar와 dayResult를 추가 갱신 하자
		
		//1-1. FLEXIBLE_EMP 중복된 근무 기간에서 FLEXIBLE_STD_MGR 에서 BASE_WORK_YN 이 N일 경우 데이터를 삽입하고 시작일 종료일 정리는 일괄적으로 한다.
		
		//2. FLEXIBLE_EMP에 있는데 근무조 기간 내에 정보가 중복되는 사람을 업데이트
		
		//WtmWorkteamMgr workteamMgr = wtmWorkteamMgrRepo.findById(workteamMgrId).get();
		
		
		
		//END FLEXIBLE_EMP의 시작일 종료일 정리
	}

	/**
	 * calendar id로 일근무표 조회(관리자용)
	 * @param tenantId
	 * @param enterCd
	 * @param workCalendarId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getEmpDayResults(Long tenantId, String enterCd, String sabun, String ymd) {
		List<Map<String, Object>> workDayResult = null;
		try {
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("ymd", ymd);
			
			workDayResult = flexEmpMapper.getWorkDayResultByCalendarId(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workDayResult;

	}
	
	/**
	 * 일근무 다건 저장(관리자용)
	 * @param tenantId
	 * @param enterCd
	 * @param workCalendarId
	 * @return
	 */
	@Override
	public void saveEmpDayResults(Long tenantId, String enterCd, String userId, Map<String, Object> convertMap) throws Exception {
		if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
			List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
			List<Map<String, Object>> day = new ArrayList();
			if(iList != null && iList.size() > 0) {
				for(Map<String, Object> l : iList) {
					
					l.put("shm", l.get("planSdate").toString().substring(8,12));
					l.put("ehm", l.get("planEdate").toString().substring(8,12));

					Map<String, Object> planMinuteMap = flexEmpMapper.calcMinuteExceptBreaktime(l);
					l.put("planMinute", (Integer.parseInt(planMinuteMap.get("calcMinute")+"")));
					l.put("updateId", userId);
					l.put("tenantId", tenantId);
					l.put("enterCd", enterCd);
					
					WtmWorkDayResult result = new WtmWorkDayResult();
					if(l.get("workDayResultId") != "") {
						result = workDayResultRepo.findByWorkDayResultId(Long.parseLong(l.get("workDayResultId").toString()));
					} else {
						result.setEnterCd(enterCd);
						result.setSabun(l.get("sabun").toString());
						result.setTenantId(tenantId);
						result.setTimeTypeCd("BASE");
						result.setYmd(l.get("ymd").toString());
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
					
					result.setPlanSdate(sdf.parse(l.get("planSdate").toString()));
					result.setPlanEdate(sdf.parse(l.get("planEdate").toString()));
					result.setUpdateId(userId);	
				
					
					workDayResultRepo.save(result);
					
					Map<String, Object> result2 = flexEmpMapper.checkBaseWorktime(Long.parseLong(l.get("flexibleEmpId").toString()));
					if(result2!=null && result2.get("isValid")!=null && result2.get("isValid").equals("0")) {
						throw new RuntimeException(result2.get("totalWorktime").toString() + "시간의 소정근로시간을 넘을 수 없습니다.");
					}
				}
			}
		}
	}	
	
	@Override
	public Map<String, Object> calcMinuteExceptBreaktime(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) {
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		return flexEmpMapper.calcMinuteExceptBreaktime(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> getFlexibleEmpWebList(Long tenantId, String enterCd, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		
		List<Map<String, Object>> flexibleList = flexEmpMapper.getFlexibleEmpWebList(paramMap);
		
		return flexibleList;
	}

	@Transactional
	@Override
	public void addWtmDayResultInBaseTimeType(Long tenantId, String enterCd, String ymd, String sabun, String addTimeTypeCd, String addTaaCd,
			Date addSdate, Date addEdate, Long applId, String userId) {
	 
		List<WtmWorkDayResult> base = workDayResultRepo.findByTenantIdAndEnterCdAndSabunAndTimeTypeCdAndYmdBetween(tenantId, enterCd, sabun, WtmApplService.TIME_TYPE_BASE, ymd, ymd);
		
		//List<WtmWorkDayResult> days = workDayResultRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);

		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		 
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("tenantId", tenantId);
		pMap.put("enterCd", enterCd);
		pMap.put("sabun", sabun);
		pMap.put("ymd", ymd);
		
		for(WtmWorkDayResult r : base) {
			//근무 계획 시작시간과 종료시간의 범위를 절대 벗어날수 없다. 그렇다 한다. ㅋ
			boolean isDelete = false;
			//시종시간이 동일하면 기본근무 계획시간을 지운다.
			if(r.getPlanSdate().compareTo(addSdate) == 0 && r.getPlanEdate().compareTo(addEdate) == 0) {
				isDelete = true;
			//시작시간은 같지만 계획 종료 시간 보다 대체휴일종료 시간이 작을 경우
			}else if(r.getPlanSdate().compareTo(addSdate) == 0 && r.getPlanEdate().compareTo(addEdate) > 0) {
				r.setPlanSdate(addEdate); // 계획의 시작일은 휴일대체 종료로 변경한다
				 
			//종료시간은 같지만 계획 시작시간 보다 대체휴일시작시간이 클경우
			}else if(r.getPlanSdate().compareTo(addSdate) < 0 && r.getPlanEdate().compareTo(addEdate) == 0) {
				r.setPlanEdate(addSdate); // 계획의 종료일을 휴일대체 시작일로 변경한다
				
			//계회의 시종 시간 중간에!! 대체휴일 시종시간이 있을 경우! 거지같넹.. 앞에데이터는 수정하고 뒤에 데이터는 만들어줘야한다.. 
			}else if(r.getPlanSdate().compareTo(addSdate) < 0 && r.getPlanEdate().compareTo(addEdate) > 0) {
				Date oriEdate = r.getPlanEdate();
				r.setPlanEdate(addSdate);
				
				WtmWorkDayResult addR = new WtmWorkDayResult();
				addR.setApplId(r.getApplId());
				addR.setTenantId(r.getTenantId());
				addR.setEnterCd(enterCd);
				addR.setYmd(r.getYmd());
				addR.setSabun(r.getSabun());
				addR.setPlanSdate(addEdate);
				addR.setPlanEdate(oriEdate);

				Map<String, Object> addMap = new HashMap<>();
				addMap.putAll(pMap);
				
				String shm = sdf.format(addEdate);
				String ehm = sdf.format(oriEdate); 
				addMap.put("shm", shm);
				addMap.put("ehm", ehm);
				Map<String, Object> addPlanMinuteMap = flexEmpMapper.calcMinuteExceptBreaktime(addMap);
				addR.setPlanMinute(Integer.parseInt(addPlanMinuteMap.get("calcMinute")+""));
				addR.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
				addR.setUpdateId(userId);
				
				workDayResultRepo.save(addR);
				
			}
			if(!isDelete) {
				String shm = sdf.format(r.getPlanSdate());
				String ehm = sdf.format(r.getPlanEdate()); 
				pMap.put("shm", shm);
				pMap.put("ehm", ehm);
				Map<String, Object> planMinuteMap = flexEmpMapper.calcMinuteExceptBreaktime(pMap);
				r.setPlanMinute(Integer.parseInt(planMinuteMap.get("calcMinute")+"")); 
				workDayResultRepo.save(r);
			}else {
				workDayResultRepo.delete(r);
			}
		}
		

		WtmWorkDayResult addDayResult = new WtmWorkDayResult();
		addDayResult.setApplId(applId);
		addDayResult.setTenantId(tenantId);
		addDayResult.setEnterCd(enterCd);
		addDayResult.setYmd(ymd);
		addDayResult.setSabun(sabun);
		addDayResult.setPlanSdate(addSdate);
		addDayResult.setPlanEdate(addEdate);
		Map<String, Object> addMap = new HashMap<>();
		addMap.putAll(pMap);
		
		String shm = sdf.format(addEdate);
		String ehm = sdf.format(addEdate); 
		addMap.put("shm", shm);
		addMap.put("ehm", ehm);
		Map<String, Object> addPlanMinuteMap = flexEmpMapper.calcMinuteExceptBreaktime(addMap);
		addDayResult.setPlanMinute(Integer.parseInt(addPlanMinuteMap.get("calcMinute")+""));
		addDayResult.setTimeTypeCd(addTimeTypeCd);
		addDayResult.setTaaCd(addTaaCd);
		addDayResult.setUpdateId(userId);
		
		workDayResultRepo.save(addDayResult); 
	}

	@Override
	public void removeWtmDayResultInBaseTimeType(Long tenantId, String enterCd, String ymd, String sabun,
			String removeTimeTypeCd, String removeTaaCd, Date removeSdate, Date removeEdate, Long applId, String userId) {
		
		//if(otSubsAppls != null && otSubsAppls.size() > 0) {
			String currYmd = null;
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			Map<String, Map<String, Date>> resetBaseTime = new HashMap<String, Map<String, Date>>();
			//for(WtmOtSubsAppl otSubsAppl : otSubsAppls) {
				List<String> timeTypeCd = new ArrayList<>();
				timeTypeCd.add(WtmApplService.TIME_TYPE_BASE);
				timeTypeCd.add(WtmApplService.TIME_TYPE_SUBS); 
				timeTypeCd.add(WtmApplService.TIME_TYPE_TAA); 
				
				List<WtmWorkDayResult> workDayResults = workDayResultRepo.findByTenantIdAndEnterCdAndSabunAndTimeTypeCdInAndYmdBetweenOrderByPlanSdateAsc(tenantId, enterCd, sabun, timeTypeCd, ymd, ymd);
				 
				//Date sdate = otSubsAppl.getSubsSdate();
				//Date edate = otSubsAppl.getSubsEdate();
				 
				int cnt = 0;
				Boolean isPrev = null;
				for(WtmWorkDayResult res : workDayResults) {
					 
					if(( res.getTimeTypeCd().equals(WtmApplService.TIME_TYPE_TAA) || res.getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS) ) && res.getPlanSdate().compareTo(removeSdate) == 0 && res.getPlanEdate().compareTo(removeEdate) == 0) {
						if(cnt == 0) {
							//시작시간이 대체휴일이면 다음 데이터 여부를 판단하고 다음데이터가 SUBS BASE로 변경하자
							if(workDayResults.size() == (cnt+1) || workDayResults.get(cnt+1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS) || workDayResults.get(cnt+1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_TAA) ) {
								//뒤에 데이터가 없으면
								res.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
								res.setApplId(applId);
								workDayResultRepo.save(res);
								break;
							}else { 
								WtmWorkDayResult modiResult = workDayResults.get(cnt+1);
								modiResult.setPlanSdate(removeSdate);
								modiResult.setApplId(applId);
								
								workDayResultRepo.deleteById(res.getWorkDayResultId());
								workDayResultRepo.save(modiResult);
								break;
							}
						}else {
							// 삭제하려는 데이터면 이전 데이터가 SUBS 인지를 체크 한다.
							if(workDayResults.get(cnt-1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS) || workDayResults.get(cnt-1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_TAA)) {
								isPrev = false;
							}else {
								isPrev = true;
							}
							// 삭제하려는 데이터가 마지막인지 확인하자
							if(workDayResults.size() == (cnt+1)) {
								if(isPrev) {
									//이전 데이터로 지우려는 데이터의 종료일로 바꿔주면 땡
									WtmWorkDayResult modiResult = workDayResults.get(cnt-1);
									modiResult.setPlanEdate(removeEdate);
									
									workDayResultRepo.deleteById(res.getWorkDayResultId());
									workDayResultRepo.save(modiResult);
									break;
								}else {
									// SUBS or TAA
									// SUBS(지우려는 데이터) -> BASE 로 변
									res.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
									res.setApplId(applId);

									workDayResultRepo.save(res);
									break;
								}
							}else {
								//마지막 데이터가 아니면 다음 데이터의 timeTypeCd를 확인하자
								if(workDayResults.get(cnt+1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_SUBS) || workDayResults.get(cnt+1).getTimeTypeCd().equals(WtmApplService.TIME_TYPE_TAA)) {
									if(isPrev) { 
										//이전 데이터로 지우려는 데이터의 종료일로 바꿔주면 땡
										WtmWorkDayResult modiResult = workDayResults.get(cnt-1);
										modiResult.setPlanEdate(removeEdate);
										
										workDayResultRepo.deleteById(res.getWorkDayResultId());
										workDayResultRepo.save(modiResult);
										break;
									}else { 
										//SUBS or TAA
										// SUBS(지우려는 데이터) -> BASE 로 변
										//SUBS or TAA
										res.setTimeTypeCd(WtmApplService.TIME_TYPE_BASE);
										res.setApplId(applId); 
										workDayResultRepo.save(res);
										break;
									}
								}else { 
									if(isPrev) { 
										//1. BASE
										//2. SUBS TAA
										//3. BASE 인 상황  1,2번을 보내드리고 3번으로 통합하자
										workDayResultRepo.deleteById(workDayResults.get(cnt-1).getWorkDayResultId());
										workDayResultRepo.deleteById(res.getWorkDayResultId());

										WtmWorkDayResult modiResult = workDayResults.get(cnt+1); 
										modiResult.setPlanSdate(workDayResults.get(cnt-1).getPlanSdate());  
										workDayResultRepo.save(modiResult);
										break;
									}else {
										//이후 데이터로 지우려는 데이터의 시작일로 바꿔주면 땡
										WtmWorkDayResult modiResult = workDayResults.get(cnt+1);
										modiResult.setPlanSdate(removeSdate); 
										workDayResultRepo.deleteById(res.getWorkDayResultId());
										workDayResultRepo.save(modiResult);
										break;
									}
									
								} 
							}
							
						}
						
						
					}
					cnt++;
				}
				 
			//}  		
				
	}
	
	
}
