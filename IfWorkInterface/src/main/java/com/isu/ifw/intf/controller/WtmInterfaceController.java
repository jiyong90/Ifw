package com.isu.ifw.intf.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
	private WtmInterfaceService wtmInterfaceService;
	
   @RequestMapping(value="/data/{type}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
   public void sendMaCodedtl(@PathVariable String type, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
      try {
         //ObjectMapper mapper = new ObjectMapper();
		  wtmInterfaceService.sendData(type, paramMap);
         //return mapper.writeValueAsString(resMap);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         //return e.getMessage();
      }
       
   }
	
   @RequestMapping(value="/intf/data/{type}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
   public void receiveMaCodedtl(@PathVariable String type, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
      try {
    	  System.out.println("############ /intf/data/"+type + " : " + paramMap.toString());
         //ObjectMapper mapper = new ObjectMapper();
		  wtmInterfaceService.receiveData(type, paramMap);
    	 //return mapper.writeValueAsString(resMap);
      } catch (Exception e) {
    	  e.printStackTrace();
      }
   }
	
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
			ifCodeList = wtmInterfaceService.getCodeIfResult(tenantId, lastDataTime); //Servie 호출
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
			ifList = wtmInterfaceService.getHolidayIfResult(tenantId, lastDataTime); //Servie 호출
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
			ifList = wtmInterfaceService.getGntCodeIfResult(tenantId, lastDataTime); //Servie 호출
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
			ifList = wtmInterfaceService.getOrgCodeIfResult(tenantId, lastDataTime); //Servie 호출
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
			ifList = wtmInterfaceService.getOrgChartMgrIfResult(tenantId, lastDataTime); //Servie 호출
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
			ifList = wtmInterfaceService.getOrgChartDetIfResult(lastDataTime, enterCd, symd); //Servie 호출
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
			ifList = wtmInterfaceService.getEmpHisIfResult(tenantId, lastDataTime); //Servie 호출
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
			ifList = wtmInterfaceService.getOrgConcIfResult(tenantId, lastDataTime); //Service 호출
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
			ifList = wtmInterfaceService.getEmpAddrIfResult(tenantId, lastDataTime); //Service 호출
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
			ifList = wtmInterfaceService.getTaaApplIfResult(tenantId, lastDataTime); //Service 호출
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
			int cnt = wtmInterfaceService.setWorkTimeClose(tenantId, saveList); //Service 호출
			rp.setStatus("OK");
			rp.setMessage("보상휴가 생성이 완료되었습니다.");
		} catch(Exception e) {
			rp.setFail("보상휴가저장시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
		
	}
	
	@RequestMapping(value="/hr/data/{type}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendDataToHR(@PathVariable String type, @RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
	    try {
	       //ObjectMapper mapper = new ObjectMapper();
	       wtmInterfaceService.sendDataToHR(type, paramMap);
	       //return mapper.writeValueAsString(resMap);
	    } catch (Exception e) {
	       // TODO Auto-generated catch block
	       e.printStackTrace();
	       //return e.getMessage();
	    }
	       
	}


	@RequestMapping(value="/infWorktimeClose", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void worktimeClose(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
		try {
			wtmInterfaceService.worktimeClose(paramMap);
			//return mapper.writeValueAsString(resMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return e.getMessage();
		}

	}
}
