package com.isu.ifw.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.mapper.WtmDayCloseMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmWorkCalendarRepository;

@Service
public class WtmAsyncService {
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Autowired
	WtmDayCloseMapper wtmDayCloseMapper;
	
	@Autowired
	WtmWorkCalendarRepository wtmWorkCalendarRepo;
	
	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	WtmFlexibleEmpService flexEmpService;
		
		@Async("threadPoolTaskExecutor")
		public void createWorkTermtimeByEmployee(Long tenantId, String enterCd, String sabun, String symd, String eymd, String userId) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("symd", symd);
			paramMap.put("eymd", eymd);
			paramMap.put("pId", userId);
			wtmFlexibleEmpMapper.createWorkTermBySabunAndSymdAndEymd(paramMap);
		}
		
		/**
		 * 일 마감
		 * @param tenantId
		 * @param enterCd
		 * @param userId
		 */
		@Async("threadPoolTaskExecutor")
		public void workdayClose(Long tenantId, String enterCd, String userId) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date today = new Date();
			String ymd = sdf.format(today);
			
			flexEmpService.calcApprDayInfo(tenantId, enterCd, ymd, ymd, "");
			
			//Calendar cal = new GregorianCalendar();
			//cal.add(Calendar.DATE, -1);
			//Date yesterday = cal.getTime();
			
			//flexEmpService.calcApprDayInfo(tenantId, enterCd, sdf.format(yesterday), sdf.format(yesterday), "");
		}
}
