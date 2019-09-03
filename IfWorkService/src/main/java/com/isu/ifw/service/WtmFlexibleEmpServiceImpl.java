package com.isu.ifw.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmTaaCode;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.entity.WtmWorkteamEmp;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
import com.isu.ifw.repository.WtmWorkteamEmpRepository;
import com.isu.ifw.repository.WtmWorkteamMgrRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.WtmDayPlanVO;
import com.isu.ifw.vo.WtmDayWorkVO;

@Service("flexibleEmpService")
public class WtmFlexibleEmpServiceImpl implements WtmFlexibleEmpService {

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	WtmFlexibleEmpRepository flexEmpRepo;
	
	@Autowired
	WtmFlexibleStdMgrRepository flexStdMgrRepo;
	
	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;
	
	@Autowired
	WtmWorkDayResultRepository workDayResultRepo;
	
	@Override
	public List<Map<String, Object>> getFlexibleEmpList(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		List<Map<String, Object>> flexibleList = flexEmpMapper.getFlexibleEmpList(paramMap);
		if(flexibleList!=null && flexibleList.size()>0) {
			for(Map<String, Object> flex : flexibleList) {
				if(flex.containsKey("flexibleEmpId") && flex.get("flexibleEmpId")!=null && !"".equals(flex.get("flexibleEmpId")))
					flex.put("flexibleEmp", getDayWorks(Long.valueOf(flex.get("flexibleEmpId").toString()), userId));
			}
		}
		
		return flexibleList;
	}
	
	@Override
	public List<Map<String, Object>> getFlexibleEmpListForPlan(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("ymd", WtmUtil.parseDateStr(new Date(), null));
		
		List<Map<String, Object>> flexibleEmpList = flexEmpMapper.getFlexibleEmpListForPlan(paramMap);
		if(flexibleEmpList!=null && flexibleEmpList.size()>0) {
			for(Map<String, Object> flexibleEmp : flexibleEmpList) {
				if(flexibleEmp.get("flexibleEmpId")!=null && !"".equals(flexibleEmp.get("flexibleEmpId"))) {
					List<WtmDayWorkVO> dayWorks = getDayWorks(Long.valueOf(flexibleEmp.get("flexibleEmpId").toString()), userId);
					flexibleEmp.put("dayWorks", dayWorks);
				}
			}
		}
		
		return flexibleEmpList;
	}
	
	@Override
	public Map<String, Object> getWorkDayResult(Long tenantId, String enterCd, String sabun, String ymd, Long userId) {
		
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
		result.put("holidayYn", workCalendar.getHolidayYn());
		result.put("entry", workCalendar);
		
		//근태, 근무 정보
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("ymd", ymd);
		
		List<Map<String, Object>> workDayResult = flexEmpMapper.getWorkDayResult(paramMap);
		ObjectMapper mapper = new ObjectMapper();
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
	
	
	@Transactional
	@Override
	public void save(Long flexibleEmpId, Map<String, Object> dateMap, Long userId) throws Exception{
		WtmFlexibleEmp emp =  flexEmpRepo.findById(flexibleEmpId).get();
		flexEmpMapper.createWorkCalendarOfSeleC(flexibleEmpId, userId);
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
		
		
	}

	@Override
	public List<WtmDayWorkVO> getDayWorks(Long flexibleEmpId, Long userId) {
		List<Map<String, Object>> plans = flexEmpMapper.getWorktimePlan(flexibleEmpId);
		
		Map<String, Object> imsiMap = new HashMap<>();
		
		if(plans != null && plans.size() > 0) {
			WtmDayWorkVO work = new WtmDayWorkVO();
			for(Map<String, Object> plan : plans) {
				String ymd = plan.get("ymd").toString();
				String holidayYn = "";
				if(plan.containsKey("holidayYn") && plan.get("holidayYn") != null) {
					holidayYn = plan.get("holidayYn").toString();
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
			workVO.setPlans((List<WtmDayPlanVO>)dtMap.get("plan"));
			works.add(workVO);
		}
		return works;
	}

	@Override
	public void updEntrySdate(Long tenantId, String enterCd, String sabun, String ymd, String entryTypeCd, Date sdate, Long userId) {
		WtmWorkCalendar calendar = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		calendar.setEntryStypeCd(entryTypeCd);
		calendar.setEntrySdate(sdate); 
		calendar.setUpdateId(userId);
		workCalendarRepo.save(calendar);
	}

	@Override
	public void updEntryEdate(Long tenantId, String enterCd, String sabun, String ymd, String entryTypeCd, Date edate, Long userId) {
		
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
	protected void calcApprDayInfo(Long tenantId, String enterCd, String ymd, String sabun, String timeTypeCd) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("timeTypeCd", timeTypeCd);
		//소정근로시간의 경우 출퇴근 타각기록으로만 판단
		flexEmpMapper.updateApprDatetimeByYmdAndSabun(paramMap);
		flexEmpMapper.updateApprMinuteByYmdAndSabun(paramMap);
		//연장근로의 경우 히스토리 타각기록을 통해 계산이 필요함
	}

	@Override
	public void workClosed(Long tenantId, String enterCd, String sabun, String ymd, Long userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelWorkClosed(Long tenantId, String enterCd, String sabun, String ymd, Long userId) {
		// TODO Auto-generated method stub
		
	}
	
	@Autowired
	WtmWorkteamMgrRepository wtmWorkteamMgrRepo;
	
	@Autowired
	WtmWorkteamEmpRepository wtmWorkteamEmpRepo;
	
	
	@Transactional
	@Override
	public void createWorkteamEmpData(Long tenantId, String enterCd, Long workteamMgrId, Long userId) {
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
	public void saveEmpDayResults(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) throws Exception {
		try {
			if(convertMap.containsKey("mergeRows") && ((List)convertMap.get("mergeRows")).size() > 0) {
				List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("mergeRows");
				List<Map<String, Object>> day = new ArrayList();
				if(iList != null && iList.size() > 0) {
					for(Map<String, Object> l : iList) {
						if(l.get("timeTypeCd").equals("BASE")) {
							Map<String, Object> temp = new HashMap();
							Map<String, Object> param = new HashMap();
							param.put("dayResult", temp);
							temp.put("shm", l.get("planSdate"));
							temp.put("ehm", l.get("planEdate"));
								
							this.save(Long.valueOf(l.get("flexibleEmpId").toString()), param, userId);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
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
	
}
