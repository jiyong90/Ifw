package com.isu.ifw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.isu.ifw.entity.WtmInbox;
import com.isu.ifw.service.WtmInboxService;
import com.isu.ifw.vo.WtmInboxVO;

@Controller
public class TestController {
	
	@Autowired
	WtmInboxService inboxService;

	
	@RequestMapping("/test")
	public void test() throws Exception {
		WtmInboxVO inbox = new WtmInboxVO();
		inbox.setEnterCd("ISU");
		inbox.setSabun("test");
		inbox.setTenantId(1L);
		inbox.setTitle("테스트");
		inboxService.setInbox(inbox);
	}
}

