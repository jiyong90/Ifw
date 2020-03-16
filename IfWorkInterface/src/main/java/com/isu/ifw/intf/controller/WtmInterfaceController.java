package com.isu.ifw.intf.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.intf.service.WtmInterfaceService;
import com.isu.ifw.intf.vo.ReturnParam;

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
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifCodeList = null;
		rp.setSuccess("OK");
		try {
			ifCodeList = WtmInterfaceService.getCodeIfResult(tenantId, lastDataTime); //Servie 호출
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
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getHolidayIfResult(tenantId, lastDataTime); //Servie 호출
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
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getGntCodeIfResult(tenantId, lastDataTime); //Servie 호출
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
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgCodeIfResult(tenantId, lastDataTime); //Servie 호출
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
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgChartMgrIfResult(tenantId, lastDataTime); //Servie 호출
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
		System.out.println("lastDataTime : " + lastDataTime);
		String enterCd = paramMap.get("enterCd").toString(); // 회사
		System.out.println("enterCd : " + enterCd);
		String symd = paramMap.get("symd").toString(); // 기준일
		System.out.println("symd : " + symd);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgChartDetIfResult(lastDataTime, enterCd, symd); //Servie 호출
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
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getEmpHisIfResult(tenantId, lastDataTime); //Servie 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	
	@RequestMapping(value = "/orgConc", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam orgConcIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController orgConcIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getOrgConcIfResult(tenantId, lastDataTime); //Service 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	@RequestMapping(value = "/empAddr", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam empAddrIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController empAddrIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getEmpAddrIfResult(tenantId, lastDataTime); //Service 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/taaAppl", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam taaApplIf(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController taaApplIf start");
		ReturnParam rp = new ReturnParam();
		
		// 공통코드
		String lastDataTime = paramMap.get("lastDataTime").toString(); // 최종 data 전달data시간
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		System.out.println("tenantId : " + tenantId + "lastDataTime : " + lastDataTime);
		List<Map<String, Object>> ifList = null;
		rp.setSuccess("OK");
		try {
			ifList = WtmInterfaceService.getTaaApplIfResult(tenantId, lastDataTime); //Service 호출
			rp.put("ifData", ifList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		return rp;
	}
	
	@RequestMapping(value = "/workTimeClose",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam setWorkTimeClose(@RequestBody Map<String,Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("WtmInterfaceController taaApplIf start");
		ReturnParam rp = new ReturnParam();
		
		//보상휴가를 생성해야함.
		System.out.println("paramMap : " + paramMap.toString());
		System.out.println("request : " + request.toString());
		String tenantId = paramMap.get("tenantId").toString(); // 회사코드
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) paramMap.get("compList");
		try {
			int cnt = WtmInterfaceService.setWorkTimeClose(tenantId, saveList); //Service 호출
			rp.setStatus("OK");
			rp.setMessage("보상휴가 생성이 완료되었습니다.");
		} catch(Exception e) {
			rp.setFail("보상휴가저장시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
		
	}
	
}
