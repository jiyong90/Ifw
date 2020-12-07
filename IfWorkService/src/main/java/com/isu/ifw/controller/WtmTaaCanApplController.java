package com.isu.ifw.controller;

import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.vo.ReturnParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="/taaCanAppl")
public class WtmTaaCanApplController {
	
	@Autowired
	@Qualifier("WtmTaaApplService")
	WtmApplService taaApplService;

	@Autowired
	@Qualifier("wtmTaaCanService")
	WtmApplService taaCanApplService;


	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Object> getTaaAppl(@RequestParam Long applId, HttpServletRequest request) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		rtnMap = taaApplService.getAppl(tenantId, enterCd, sabun, applId, userId);
		return rtnMap;
	}

	@RequestMapping(value="/request", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam taaCanApplRequest(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) {

		ReturnParam rp = new ReturnParam();
		rp.setFail("");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		Long taaApplId = null;
		if(paramMap.get("taaApplId")!=null && !"".equals(paramMap.get("taaApplId")))
			taaApplId = Long.valueOf(paramMap.get("taaApplId").toString());

		String status = null;
		if(paramMap.get("status")!=null && !"".equals(paramMap.get("status")))
			status = paramMap.get("status").toString();

		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();

		try {
			//rp = otCanApplService.validate(tenantId, enterCd, sabun, workTypeCd, paramMap);
			//if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
			rp = taaCanApplService.request(tenantId, enterCd, taaApplId, workTypeCd, paramMap, sabun, userId);
			//}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}

}
