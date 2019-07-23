package com.isu.ifw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.isu.ifw.service.WtmInboxService;
import com.isu.ifw.vo.WtmInboxVO;
import com.isu.option.vo.ReturnParam;

@Controller
@RequestMapping(value="/noti")
public class WtmInboxController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");

	@Autowired
	WtmInboxService inboxService;
	
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
		
		Map<String, String> result = new HashMap();		
		int nCount = 0;
		try {
			nCount = inboxService.getInboxCount(tenantId, enterCd, sabun);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		rp.put("count", nCount);
		return rp; 
	}
	
	@RequestMapping(value="/inbox/list", method=RequestMethod.GET)
	public @ResponseBody ReturnParam getNotiList(HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", request.getAttribute("logId"));
		MDC.put("type", "C");
		logger.info("noti controller start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		Long tenantId = Long.parseLong(request.getAttribute("tenantId").toString());
		String enterCd = request.getAttribute("enterCd").toString();
		String sabun = request.getAttribute("sabun").toString();

		List<Map<String, Object>> inboxList = new ArrayList();
		try {
			inboxList = inboxService.getInboxList(tenantId, enterCd, sabun);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		rp.put("data", inboxList);
		return rp; 
	}
}

