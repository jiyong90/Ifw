package com.isu.ifw.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmFlexibleEmpMapper;

@Service
public class WtmAsyncService {
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
		
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
}
