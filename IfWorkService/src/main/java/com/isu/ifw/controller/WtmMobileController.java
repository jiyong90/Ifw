package com.isu.ifw.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.vo.ReturnParam;


@RestController
public class WtmMobileController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	/**
	 * dashboard
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/dashboard", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyDashboard(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, HttpServletRequest request) throws Exception {

		logger.debug("/mobile/{tenantId}/dashboard ");

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
//		String enterCd =  empKey.split("@")[0];
//		String sabun =  empKey.split("@")[1];

        Map statusMap = new HashMap();
        statusMap.put("param1", "선근제");
        statusMap.put("param2", "72:00");
        statusMap.put("param3", "160");
        statusMap.put("param4", "48:00");
        statusMap.put("param5", "33:00");
        statusMap.put("param6", "14");
        statusMap.put("param7", "10");
        statusMap.put("param8", "4");
        
		Map dashboards = new HashMap();
		Map status = new HashMap();
		Map data = new HashMap();


		data.put("param1", statusMap.get("param1") == null? "-" : statusMap.get("param1").toString());
		data.put("param2", statusMap.get("param2") == null? "-" : statusMap.get("param2").toString());
		status.put("part1", data);

		data = new HashMap();
		data.put("param3", statusMap.get("param3") == null? "-" : statusMap.get("param3").toString());
		data.put("param4", statusMap.get("param4") == null? "-" : statusMap.get("param4").toString());
		data.put("param5", statusMap.get("param5") == null? "-" : statusMap.get("param5").toString());
		status.put("part2", data);

		data = new HashMap();
		data.put("param6", statusMap.get("param6") == null? "-" : statusMap.get("param6").toString());
		data.put("param7", statusMap.get("param7") == null? "-" : statusMap.get("param7").toString());
		data.put("param8", statusMap.get("param8") == null? "-" : statusMap.get("param8").toString());
		status.put("part3", data);
		
		dashboards.put("DASHBOARD1", status);

		rp.put("result", dashboards);
		return rp;
	}
}