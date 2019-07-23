package com.isu.ifw.service;

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
	public void setInbox(WtmInboxVO inbox) {
		WtmInbox data = new WtmInbox();
		data.setEnterCd(inbox.getEnterCd());
		data.setSabun(inbox.getSabun());
		data.setTenantId(inbox.getTenantId());
		data.setTitle(inbox.getTitle());
		
		try {
			MDC.put("inbox", inbox.toString());
			logger.info("setInbox", MDC.get("sessionId"), MDC.get("logId"), "S");
			
			data = inboxRepository.save(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MDC.remove("inbox");
			if (data != null && data.getId() != null) {
				String url = "/api/"+inbox.getTenantId()+"/"+inbox.getEnterCd()+"/"+inbox.getSabun()+"/noti";
				System.out.println(url);
				this.template.convertAndSend(url, inbox);
			}
		}
	}
	
	@Override
	public List<Map<String, Object>> getInboxList(Long tenantId, String enterCd, String sabun) {

		return null;
	}

	@Override
	public int getInboxCount(Long tenantId, String enterCd, String sabun) {
		return inboxRepository.countByTenantIdAndEnterCdAndSabunAndCheckYn(tenantId, enterCd, sabun, "N");
	}

}