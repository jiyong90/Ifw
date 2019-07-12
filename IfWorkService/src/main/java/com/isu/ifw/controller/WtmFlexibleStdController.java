package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.WtmFlexibleStdService;
import com.isu.ifw.vo.WtmFlexibleStdVO;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleStd")
public class WtmFlexibleStdController {
	
	@Autowired
	private WtmFlexibleStdService WtmFlexibleStdService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam flexibleStd(HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper mapper = new ObjectMapper();
		String userKey = sessionData.get("userKey").toString();
		String enterCd = null;
		String bisinessPlaceCd = null;
		if(sessionData.get("enterCd")!=null)
			enterCd = sessionData.get("enterCd").toString();
		if(sessionData.get("bisinessPlaceCd")!=null)
			bisinessPlaceCd = sessionData.get("bisinessPlaceCd").toString();
		
		/*List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<>();
		m.put("flexibleStdMgrId", "1");
		m.put("flexibleNm", "이수연구직");
		m.put("workTypeCd", "10");
		m.put("workTypeNm", "탄근제");
		//Map<String, Object> core = new HashMap<>();
		//core.put("sTime", "1000");
		//core.put("eTime", "1500");
		m.put("coreTime", null);
		Map<String, Object> limit = new HashMap<>();
		limit.put("sTime", "0800");
		limit.put("eTime", "2200");
		m.put("limitTime", limit);
		List<Map<String, Object>> workDateRangeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> workDateRange = new HashMap<>();
		workDateRange.put("lable", "2주");
		workDateRange.put("value", "1_week");
		workDateRangeList.add(workDateRange);
		workDateRange = new HashMap<>();
		workDateRange.put("lable", "1개월");
		workDateRange.put("value", "1_month");
		workDateRangeList.add(workDateRange);
		workDateRange = new HashMap<>();
		workDateRange.put("lable", "2개월");
		workDateRange.put("value", "2_month");
		workDateRangeList.add(workDateRange);
		workDateRange = new HashMap<>();
		workDateRange.put("lable", "3개월");
		workDateRange.put("value", "3_month");
		workDateRangeList.add(workDateRange);
		m.put("workDateRange", workDateRangeList);
		l.add(m);
		
		Map<String, Object> m1 = new HashMap<>();
		m1.put("flexibleStdMgrId", "2");
		m1.put("flexibleNm", "이수선근제기본");
		m1.put("workTypeCd", "20");
		m1.put("workTypeNm", "선근제[부분]");
		Map<String, Object> core1 = new HashMap<>();
		core1.put("sTime", "1000");
		core1.put("eTime", "1500");
		m1.put("coreTime", core1);
		Map<String, Object> limit1 = new HashMap<>();
		limit1.put("sTime", "0800");
		limit1.put("eTime", "2200");
		m1.put("limitTime", limit1);
		Map<String, Object> workingDays1 = new HashMap<>();
		workingDays1.put("월", "true");
		workingDays1.put("화", "true");
		workingDays1.put("수", "true");
		workingDays1.put("목", "true");
		workingDays1.put("금", "true");
		workingDays1.put("토", "false");
		workingDays1.put("일", "false");
		m1.put("workingDays", workingDays1);
		List<Map<String, Object>> workDateRangeList1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> workDateRange1 = new HashMap<>();
		workDateRange1.put("lable", "1주");
		workDateRange1.put("value", "1_week");
		workDateRangeList1.add(workDateRange1);
		workDateRange1 = new HashMap<>();
		workDateRange1.put("lable", "2주");
		workDateRange1.put("value", "1_week");
		workDateRangeList1.add(workDateRange1);
		workDateRange1 = new HashMap<>();
		workDateRange1.put("lable", "1개월");
		workDateRange1.put("value", "1_month");
		workDateRangeList1.add(workDateRange1);
		m1.put("workDateRange", workDateRangeList1);
		l.add(m1);*/
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		List<WtmFlexibleStdVO> wtmFlexibleStd = null;
		
		try {
			wtmFlexibleStd = WtmFlexibleStdService.getFlexibleStd(tenantId, enterCd, userKey);
			rp.put("wtmFlexibleStd", wtmFlexibleStd);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	/*@PostMapping(value = "/flexibleStd") 
	public ReturnParam flexibleStd(@RequestBody WtmFlexibleStdVO flexibleStdVO
								, HttpServletRequest request) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(flexibleStdVO));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReturnParam rp = new ReturnParam();
		return rp;
			
	}*/
}
