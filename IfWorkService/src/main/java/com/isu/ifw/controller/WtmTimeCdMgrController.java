package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmTimeCdMgrService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/timecd")
public class WtmTimeCdMgrController {
	
	@Autowired
	WtmTimeCdMgrService timeCdService;
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getTimeCdList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		rp.setSuccess("");
		
		List<Map<String, Object>> timeCdList = null;
		try {		
			timeCdList = timeCdService.getTimeCdList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", timeCdList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
}
