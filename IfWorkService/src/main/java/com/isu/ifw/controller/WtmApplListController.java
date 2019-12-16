package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmApplListService;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/applList")
public class WtmApplListController {
	
	@Autowired
	WtmApplListService applListService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/otList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getOtList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> workList = null;
		try {		
			workList = applListService.getOtList(tenantId, enterCd, sabun, paramMap);
			
			rp.put("DATA", workList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
}
