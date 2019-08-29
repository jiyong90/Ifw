package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmInoutService;
import com.isu.option.util.Aes256;
import com.isu.option.vo.ReturnParam;

@RestController
public class WtmInoutController {

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
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		
		Map <String,Object> resultMap = new HashMap<String,Object>();
		Map <String,Object> returnMap = new HashMap<String,Object>();
		Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
		
		returnMap.put("D01", menus);
		resultMap.put("menus", returnMap);
		rp.put("result", resultMap);
		
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
		
		System.out.println("#########################/inout/in" + params.toString());
		
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
			Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
			if(menus == null || menus.get("ymd") == null) {
				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");
			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("IN")) {
				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
			} 
			int cnt = inoutService.checkInoutHis(tenantId, enterCd, sabun, "IN", ymd);
			if(cnt == 2) {
				rp.setSuccess("출근 체크 하였습니다.");
			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("출퇴근 정보 기록 중 오류가 발생했습니다.");
		}
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
		
		System.out.println("#########################/inout/out" + params.toString());
		
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
			Map<String,Object> menus = inoutService.getMenuContext(tenantId, enterCd, sabun); 
			if(menus == null || menus.get("ymd") == null) {
				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");
			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("OUT")) {
				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
			} 
			int cnt = inoutService.checkInoutHis(tenantId, enterCd, sabun, "OUT", ymd);
			if(cnt == 2) {
				rp.setSuccess("퇴근 체크 하였습니다.");
			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("출퇴근 정보 기록 중 오류가 발생했습니다.");
		}
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
			Map <String,Object> data = inoutService.getMyInoutDetail(tenantId, enterCd, sabun, month);
			Map <String,Object> resultMap = new HashMap();
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
			Date time = new Date();
			String now = format1.format(time);

			if(month.equals(now) 
					&& data.get("sdate") != null && !data.get("sdate").equals("") 
					&& data.get("edate") != null && !data.get("edate").equals("")) {

				Map<String, Object> preference = new HashMap<>();
				preference.put("extBtnLabel", "퇴근취소");
				preference.put("useExtBtn", "true");
				resultMap.put("preference", preference);
			}
			
			resultMap.put("data", data);
			rp.put("result", resultMap);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rp;
	}
	
	@RequestMapping(value = "/mobile/{tenantId}/inout/outcancel", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
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
		String ymd = data.get("ymd").toString().replaceAll("-", "");
		
		try {
			int cnt = inoutService.checkInoutHis(tenantId, enterCd, sabun, "OUTC", ymd);
			if(cnt <= 0) {
				rp.setFail("퇴근 취소가 실패하였습니다.");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		
		return rp;
	}
}
