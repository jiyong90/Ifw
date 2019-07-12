package com.isu.ifw.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleEmp")
public class WtmFlexibleEmpController {
	
	@Autowired
	private WtmFlexibleEmpService wtmFlexibleEmpService;

	@RequestMapping(value = "/prev",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getPrevFlexibleEmp(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> prevFlexible = null;
		
		try {
			prevFlexible = wtmFlexibleEmpService.getPrevFlexible(tenantId, enterCd, empNo);
			rp.put("prevFlexible", prevFlexible);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
