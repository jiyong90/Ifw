package com.isu.ifw.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.repository.WtmWorkDayResultRepository;
import com.isu.ifw.vo.WtmWorkTermTimeVO;

@Service("flexibleEmpService")
public class WtmFlexibleEmpServiceImpl implements WtmFlexibleEmpService {

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	WtmFlexibleEmpRepository flexEmpRepo;
	
	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;
	
	@Autowired
	WtmWorkDayResultRepository workDayResultRepo;
	
	@Override
	public List<Map<String, Object>> getFlexibleEmpList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("empNo", empNo);
		
		return flexEmpMapper.getFlexibleEmpList(paramMap);
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
				WtmWorkDayResult result =  workDayResultRepo.findByTimeTypeCdAndYmdAndTenantIdAndEnterCdAndSabun("BASE", k, emp.getTenantId(), emp.getEnterCd(), emp.getSabun());
				if(result == null) {
					result = new WtmWorkDayResult();
					//부모키 가져오기
					WtmWorkCalendar c = workCalendarRepo.findByTenantIdAndEnterCdAndSabunAndYmd( emp.getTenantId(), emp.getEnterCd(), emp.getSabun(), k);
					result.setWorkCalendarId(c.getWorkCalendarId());
				}
				try {
					Map<String, String> dayResult = (Map<String, String>) dateMap.get(k);
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
					 
					result.setTimeTypeCd("BASE");
					result.setUpdateId(userId);
					workDayResultRepo.save(result);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
	}
	
}
