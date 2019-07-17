package com.isu.ifw.controller;

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
import com.isu.ifw.service.NotiService;
import com.isu.option.vo.ReturnParam;

@Controller
@RequestMapping(value="/noti")
public class NotiController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");

	@Autowired
	NotiService notiService;
	
	@RequestMapping(value="/inbox/count", method=RequestMethod.GET)
	public @ResponseBody ReturnParam getNotiCount(HttpServletRequest request) {
		ReturnParam rp = new ReturnParam();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", request.getAttribute("logId"));
		MDC.put("type", "C");
		logger.info("noti controller start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	
		rp.put("count", 0);
		return rp; 
	}
	
}

