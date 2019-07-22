package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmFlexibleApprService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleAppr")
public class WtmFlexibleApprController {
	
	@Autowired
	@Qualifier("wtmFlexibleApprService")
	WtmFlexibleApprService flexibleApprService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getFlexibleApprList(/*@RequestBody Map<String, Object> paramMap,*/ HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> apprList = null;
		try {		
			apprList = flexibleApprService.getFlexibleApprList(tenantId, enterCd, empNo, new HashMap<String, Object>());
			
			rp.put("apprList", apprList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
