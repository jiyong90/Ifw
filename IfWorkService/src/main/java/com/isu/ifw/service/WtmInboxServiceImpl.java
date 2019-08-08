package com.isu.ifw.service;

import java.util.Date;
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
import com.isu.ifw.mapper.WtmInboxMapper;
import com.isu.ifw.repository.WtmInboxRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.option.vo.ReturnParam;

@Transactional
@Service
public class WtmInboxServiceImpl implements WtmInboxService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Resource
	WtmInboxRepository inboxRepository;
	
	@Resource
	WtmInboxMapper inboxMapper;
	
	@Async("threadPoolTaskExecutor")
	@Override
	public void setInbox(Long tenantId, String enterCd, String sabun, String type, String title) {
		WtmInbox data = new WtmInbox();
		data.setEnterCd(enterCd);
		data.setSabun(sabun);
		data.setTenantId(tenantId);
		data.setType(type);
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
	public ReturnParam getInboxList(Long tenantId, String enterCd, String sabun) {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<WtmInbox> inboxList = null;
		try {
			inboxList = inboxRepository.findByTenantIdAndEnterCdAndSabunAndCheckYn(tenantId, enterCd, sabun, "N");
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		//알림 리스트
		rp.put("inboxList", inboxList);
		
		return rp;
	
	}

	@Override
	public ReturnParam getInboxCount(Long tenantId, String enterCd, String sabun) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> toDoPlanDays = null;
		int inboxCount = 0;
		try {
			//유연근무제 근무 계획 작성 여부
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("ymd", WtmUtil.parseDateStr(new Date(), null));
			toDoPlanDays = inboxMapper.getToDoPlanDays(paramMap);
			
			//알림 카운트
			inboxCount = inboxRepository.countByTenantIdAndEnterCdAndSabunAndCheckYn(tenantId, enterCd, sabun, "N");
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		//근무 계획 작성 알림
		rp.put("workPlan", toDoPlanDays);
		
		//알림 카운트
		rp.put("inboxCount", inboxCount);
		
		return rp;
	}

}