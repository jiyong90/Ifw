package com.isu.ifw.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
import com.isu.ifw.vo.WtmDayPlanVO;
import com.isu.ifw.vo.WtmDayWorkVO;
import com.isu.ifw.vo.WtmWorkTermTimeVO;
import com.sun.istack.FinalArrayList;

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
	public List<Map<String, Object>> getFlexibleEmpList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap, Long userId) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
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
		result.put("entry", workCalendar);
		
		//근태, 근무 정보
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("ymd", ymd);
		
		List<Map<String, Object>> workResult = flexEmpMapper.getWorkDayResult(paramMap);
		result.put("workResult", workResult);
		
		return result;
	}
	
	@Override
	public WtmWorkTermTimeVO getWorkTermTime(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
		return flexEmpMapper.getWorkTermTime(paramMap);
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
		flexEmpMapper.createWorkCalendar(flexibleEmpId, userId);
		if(dateMap != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			for(String k : dateMap.keySet()) {
				WtmWorkDayResult result =  workDayResultRepo.findByTimeTypeCdAndTenantIdAndEnterCdAndSabunAndYmd("BASE", emp.getTenantId(), emp.getEnterCd(), emp.getSabun(), k);
				if(result == null) {
					result = new WtmWorkDayResult();
					//부모키 가져오기
					WtmWorkCalendar c = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd( emp.getTenantId(), emp.getEnterCd(), emp.getSabun(), k);
					result.setWorkCalendarId(c.getWorkCalendarId());
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
					}else {
						result.setPlanSdate(null);
						result.setPlanEdate(null);
						result.setPlanMinute(0);
					}
					result.setTimeTypeCd("BASE");
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
			flexEmpMapper.updateHolidayYnOFWorkCalendar(paramMap);
			//planMinute갱신
			flexEmpMapper.updatePlanMinute(flexibleEmpId);
			
			Map<String, Object> result = flexEmpMapper.checkBaseWorktime(flexibleEmpId);
			if(result.get("isValid").equals("0")) {
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
	
}
