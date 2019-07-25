package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmInbox;
import com.isu.ifw.repository.WtmInboxRepository;
import com.isu.ifw.vo.WtmInboxVO;

@Transactional
@Service
public class WtmInboxServiceImpl implements WtmInboxService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Resource
	WtmInboxRepository inboxRepository;
	
	@Async("threadPoolTaskExecutor")
	@Override
	public void setInbox(Long tenantId, String enterCd, String sabun, String title) {
		WtmInbox data = new WtmInbox();
		data.setEnterCd(enterCd);
		data.setSabun(sabun);
		data.setTenantId(tenantId);
		data.setTitle(title);
		
		try {
			MDC.put("inbox", data.toString());
			logger.info("setInbox", MDC.get("sessionId"), MDC.get("logId"), "S");
			
			data = inboxRepository.save(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MDC.remove("inbox");
			if (data != null && data.getId() != null) {
				String url = "/api/"+tenantId+"/"+enterCd+"/"+sabun+"/noti";
				System.out.println(url);
				this.template.convertAndSend(url, data);
			}
		}
	}
	
	@Override
	public List<Map<String, Object>> getInboxList(Long tenantId, String enterCd, String sabun) {
		List<Map<String, Object>> inboxList = new ArrayList();
		
		List<WtmInbox> list = inboxRepository.findByTenantIdAndEnterCdAndSabunAndCheckYn(tenantId, enterCd, sabun, "N");
		for(WtmInbox l : list) {
			Map<String, Object> inbox = new HashMap();
			inbox.put("title", l.getTitle());
			inboxList.add(inbox);
		}
		return inboxList;
	
	}

	@Override
	public int getInboxCount(Long tenantId, String enterCd, String sabun) {
		return inboxRepository.countByTenantIdAndEnterCdAndSabunAndCheckYn(tenantId, enterCd, sabun, "N");
	}

}