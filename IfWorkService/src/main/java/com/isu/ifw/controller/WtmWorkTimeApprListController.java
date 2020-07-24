package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmWorkTimeApprListService;
import com.isu.ifw.vo.ReturnParam;


@RestController
@RequestMapping(value="/workTimeApprList")
public class WtmWorkTimeApprListController {
	
	@Autowired
	WtmWorkTimeApprListService wtmWorkTimeApprListService;
	
	/**
	 * 인정근무조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorkTimeApprList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();
		//String empNo = sessionData.get("empNo").toString();
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		String searchKeyword = paramMap.get("searchKeyword").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		//paramMap.put("sabun", empNo);
		paramMap.put("sYmd", sYmd);
		paramMap.put("eYmd", eYmd);
		paramMap.put("searchKeyword", searchKeyword);
		System.out.println("tenantId : "+tenantId+"/ enterCd: "+enterCd);
		
		rp.setSuccess("");
		
		List<Map<String, Object>> workTimeApprList = null;  
		try {
			workTimeApprList = wtmWorkTimeApprListService.getWorkTimeApprList(paramMap);
			rp.put("DATA", workTimeApprList);
			
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}		
				
		System.out.println("workTimeApprList = "+workTimeApprList);
		return rp;
	}
	
}
