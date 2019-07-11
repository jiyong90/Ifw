package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.isu.auth.service.OAuthService;
import com.isu.ifw.mapper.test;

@Controller
public class TestController {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@MessageMapping("/test")
	public void test() throws Exception {
		
		logger.info("test", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
	}
}

