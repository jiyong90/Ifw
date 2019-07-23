package com.isu.ifw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmInbox;
import com.isu.ifw.mapper.LoginMapper;
import com.isu.ifw.vo.WtmInboxVO;
import com.isu.ifw.vo.Login;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("WtmInboxService")
public interface WtmInboxService{
	
	public void setInbox(WtmInboxVO inbox);
	
	public int getInboxCount(Long tenantId, String enterCd, String sabun);

	public List<Map<String, Object>> getInboxList(Long tenantId, String enterCd, String sabun);
}