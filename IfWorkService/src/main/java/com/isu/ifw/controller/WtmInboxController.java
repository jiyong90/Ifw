package com.isu.ifw.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.service.WtmInboxService;
import com.isu.ifw.util.WtmUtil;
import com.isu.option.vo.ReturnParam;

@Controller
@RequestMapping(value="/noti")
public class WtmInboxController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");

	@Autowired
	WtmInboxService inboxService;
	
	@Autowired
	WtmFlexibleEmpRepository flexibleEmpRepo;
	
	@RequestMapping(value="/inbox/count", method=RequestMethod.GET)
	public @ResponseBody ReturnParam getNotiCount(HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", request.getAttribute("logId"));
		MDC.put("type", "C");
		logger.info("noti controller start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		
		rp = inboxService.getInboxCount(tenantId, enterCd, sabun);
		
		if(rp!=null && "OK".equals(rp.getStatus())) {
		
			try {
				WtmFlexibleEmp flexibleEmp = flexibleEmpRepo.findByTenantIdAndEnterCdAndSabunAndDate(tenantId, enterCd, sabun, WtmUtil.parseDateStr(new Date(), null));
				
				//유연근무대상자
				rp.put("flexibleEmp", flexibleEmp);
			}catch(Exception e) {
				e.printStackTrace();
				rp.setFail("조회 시 오류가 발생했습니다.");
			}
		}
		
		return rp; 
	}
	
	@RequestMapping(value="/inbox/list", method=RequestMethod.GET)
	public @ResponseBody ReturnParam getNotiList(HttpServletRequest request) {
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", request.getAttribute("logId"));
		MDC.put("type", "C");
		logger.info("noti controller start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		
		return inboxService.getInboxList(tenantId, enterCd, sabun);
	}
}

