package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.option.util.Aes256;



@RestController
public class WtmInoutController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmInoutService inoutService;

	/**
	 * 현재 맥락정보 반환, 동적으로 변하는 내용에 대한 내용을 처리함
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkStatus(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, HttpServletRequest request) throws Exception {
		
		logger.debug("/mobile/{tenantId}/inout/status s " + WtmUtil.paramToString(request));
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		
		Map <String,Object> resultMap = new HashMap<String,Object>();
		Map <String,Object> returnMap = new HashMap<String,Object>();
		//Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
//		Map<String,Object> menuIn = new HashMap();
//		menuIn.put("inoutType", "IN");
	
//		Map<String,Object> menuOut = new HashMap();
//		menuOut.put("inoutType", "OUT");
		
		
		//Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 

		Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
		
//		returnMap.put("D01", menuIn);
//		returnMap.put("D02", menuOut);
//		returnMap.put("D02", menus);
		
		resultMap.put("menus", menus);
		rp.put("result", resultMap);
		
		logger.debug("/mobile/{tenantId}/inout/status e " + rp.toString());
		return rp;
	}
	
	/**
	 * 출근 요청
	 * 처리 후, 어플에서는 퇴근 가능으로 메시지를 바꾼다.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/mobile/{tenantId}/inout/in", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestIn(@PathVariable Long tenantId, 
			@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
	
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("출근 체크 되었습니다.");
		
		String userToken = (String)params.get("userToken");
		String empKey = (String)params.get("empKey");
		String ymd = (String)params.get("ymd");
		String enterCd = null;
		String sabun = null;
		
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		if(empKey != null && empKey.indexOf("@") >=0 ){
			String separator = "@";
			String[] arrEmpKey = empKey.split(separator);
			if(arrEmpKey.length == 2) {
				enterCd = arrEmpKey[0];
				sabun = arrEmpKey[1];
			}
		}
		try {
			//현재 시간으로 다시 데이터 가져와서 비교??
//			Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
//			if(menus == null || menus.get("ymd") == null) {
//				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");
//			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("IN")) {
//				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
//			} 
			rp = inoutService.updateTimecard(tenantId, enterCd, sabun, ymd, "IN", "MO");
			
			logger.debug("in : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

//			if(cnt == 2) {
//				rp.setSuccess("출근 체크 하였습니다.");
//			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("출퇴근 정보 기록 중 오류가 발생했습니다.");
		}
		logger.debug("/mobile/{tenantId}/inout/in e " + rp.toString());
		return rp;
	}
	
	/**
	 * 퇴근 요청
	 * 처리 후, 어플에서는 퇴근 가능으로 메시지를 바꾼다.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/mobile/{tenantId}/inout/out", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestOut(@PathVariable Long tenantId, 
			@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 체크 되었습니다.");
		
		String userToken = (String)params.get("userToken");
		String empKey = (String)params.get("empKey");
		String ymd = (String)params.get("ymd");
		String enterCd = null;
		String sabun = null;
		
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		if(empKey != null && empKey.indexOf("@") >=0 ){
			String separator = "@";
			String[] arrEmpKey = empKey.split(separator);
			if(arrEmpKey.length == 2) {
				enterCd = arrEmpKey[0];
				sabun = arrEmpKey[1];
			}
		}
		try {
			//현재 시간으로 다시 데이터 가져와서 비교??
//			Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
//			if(menus == null || menus.get("ymd") == null) {
//				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");
//			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("OUT")) {
//				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
//			} 
			rp = inoutService.updateTimecard(tenantId, enterCd, sabun, ymd, "OUT", "MO");
			
			logger.debug("out : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

//			if(cnt == 2) {
//				rp.setSuccess("퇴근 체크 하였습니다.");
//			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("출퇴근 정보 기록 중 오류가 발생했습니다.");
		}
		logger.debug("/mobile/{tenantId}/inout/out e " + rp.toString());
		return rp;
	}
	
	/**
	 * 출퇴근 데이터 현황
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/list", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyInoutList(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey,
			@RequestParam(value="id", required = true) String month,
			HttpServletRequest request) throws Exception {		

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = request.getParameter("userToken");
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		
		try {
			List<Map <String,Object>> resultMap = inoutService.getMyInoutList(tenantId, enterCd, sabun, month);
			for(Map<String,Object> temp : resultMap) {
				temp.put("key", temp.get("key2"));
			}
			rp.put("result", resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.debug("/mobile/{tenantId}/inout/list e " + rp.toString());
		return rp;
	}
	
	/**
	 * 일별 타각 현황
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/history", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyInoutHistory(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey,
			@RequestParam(value="id", required = true) String ymd,
			HttpServletRequest request) throws Exception {		

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = request.getParameter("userToken");
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		
		try {
			List<Map <String,Object>> resultMap = inoutService.getMyInoutHistory(tenantId, enterCd, sabun, ymd);
			for(Map<String,Object> temp : resultMap) {
				temp.put("key", temp.get("key2"));
			}
			rp.put("result", resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.debug("/mobile/{tenantId}/inout/history e " + rp.toString());

		return rp;
	}
	
	/**
	 * 출퇴근 데이터 상세
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/inout/detail", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyInoutDetail(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey,
			@RequestParam(value="id", required = true) String key,
			HttpServletRequest request) throws Exception {		

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = request.getParameter("userToken");
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		
		try {
			String inoutDate = key.split("@")[0];
			String inoutTypeCd = key.split("@")[1];
			
			Map <String,Object> data = inoutService.getMyInoutDetail(tenantId, enterCd, sabun, inoutTypeCd, inoutDate);
			Map <String,Object> resultMap = new HashMap();
			
			resultMap.put("data", data);
			rp.put("result", resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("/mobile/{tenantId}/inout/detail e " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value = "/mobile/{tenantId}/inout/cancel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam cancelOutRequest(@PathVariable Long tenantId,
			@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {		
		
		String empKey = params.get("empKey").toString();
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String userToken = params.get("userToken").toString();
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];

		Map<String, Object> data = (Map<String, Object>) params.get("data");
		
		try {
			rp = inoutService.cancel(data);
//			rp = inoutService.updateTimecard(tenantId, enterCd, sabun, ymd, "OUTC", "MO");

//			if(cnt <= 0) {
//				rp.setFail("퇴근 취소가 실패하였습니다.");
//			}
			
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		logger.debug("/mobile/{tenantId}/inout/cancel e " + rp.toString());

		return rp;
	}
	
	/**
	 * 외출 복귀
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/mobile/{tenantId}/inout/goback", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestGoback(@PathVariable Long tenantId, 
			@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("체크에 실패하였습니다.");
		
		String userToken = (String)params.get("userToken");
		String empKey = (String)params.get("empKey");
		String ymd = (String)params.get("ymd");
		String enterCd = null;
		String sabun = null;
		
		Aes256 aes = new Aes256(userToken);
		empKey = aes.decrypt(empKey);
		
		if(empKey != null && empKey.indexOf("@") >=0 ){
			String separator = "@";
			String[] arrEmpKey = empKey.split(separator);
			if(arrEmpKey.length == 2) {
				enterCd = arrEmpKey[0];
				sabun = arrEmpKey[1];
			}
		}
		try {
			//현재 시간으로 다시 데이터 가져와서 비교??
//			Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
//			if(menus == null || menus.get("ymd") == null) {
//				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");skeh 
//			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("OUT")) {
//				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
//			} 
			rp = inoutService.updateTimecard(tenantId, enterCd, sabun, ymd, "EXCEPT", "MO");
			logger.debug("EXCEPT : " + tenantId + "," + enterCd + "," + sabun + "," + rp.toString());

//			if(cnt > 0) {
//				rp.setSuccess("체크 하였습니다.");
//			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("외출/복귀 기록 중 오류가 발생했습니다.");
		}
		return rp;
	}
	
}

//출근, 퇴근 정보 전부 있으면 > calcApprDayInfo
//퇴근 취소 시 타각 데이터 지우고 인정시간도 지워주기