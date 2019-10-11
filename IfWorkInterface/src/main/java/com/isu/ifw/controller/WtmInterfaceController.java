package com.isu.ifw.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmInterfaceService;
import com.isu.option.vo.ReturnParam;

@RestController
public class WtmInterfaceController {
	
	@Autowired
	private WtmInterfaceService WtmInterfaceService;
	
	@RequestMapping(value = "/code",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam codeIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController codeIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifCodeList = null;
		rp.setSuccess("OK");
		try {
			ifCodeList = WtmInterfaceService.getCodeIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifCodeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/holiday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam holidayIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController holiday start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getHolidayIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/gntCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam gntCodeIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController gntCodeIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getGntCodeIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/orgCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam orgCodeIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController orgCodeIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgCodeIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/orgChartMgr", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam orgChartMgrIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController orgChartMgrIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgChartMgrIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/orgChartDet", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam orgChartDetIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController orgChartDetIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		String enterCd = paramMap.get("enterCd").toString(); // 회사
		String sdate = paramMap.get("synd").toString(); // 기준일
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgChartDetIfResult(lastDataTime, enterCd, sdate); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/orgMapCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam orgMapCodeIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController orgMapCodeIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgMapCodeIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/empHis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam empIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController empIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		System.out.println("lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getEmpHisIfResult(lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
}
