package com.isu.ifw.service;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.LoginMapper;
import com.isu.ifw.vo.Login;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("NotiService")
public class NotiService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Async("threadPoolTaskExecutor")
	public void setNotiMessage(Map paramMap) {
		
		try {
			for(int i = 0; i < 10; i++) {
				Thread.sleep(1000);
				System.out.println(i);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Map<String, Object> resultMap = new HashMap();
		resultMap.put("count", 99999);
		this.template.convertAndSend("/api/isust/ISU/4B2940C268AC096DE3393F04126A7078/noti", resultMap);
	}
	
	public void test() {
		setNotiMessage(null);
		System.out.println("1111111111111111111111111111");
	}
	
}