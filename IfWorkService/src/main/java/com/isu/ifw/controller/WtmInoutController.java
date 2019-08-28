package com.isu.ifw.controller;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
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
@RequestMapping(value="/mobile/inout")
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
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getMyWorkStatus(@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = true) String empKey, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		
		//tenantId정보는 어떻게???
		Map <String,Object> resultMap = new HashMap<String,Object>();
		Map <String,Object> returnMap = new HashMap<String,Object>();
		Map<String,Object> menus = inoutService.getMenuContext(enterCd, sabun); 
		
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
	@RequestMapping (value="/in", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestIn(@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
		
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
			Map<String,Object> menus = inoutService.getMenuContext(enterCd, sabun); 
			if(menus == null || menus.get("ymd") == null) {
				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");
			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("IN")) {
				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
			} 
			int cnt = inoutService.checkInoutHis(enterCd, sabun, "IN", ymd);
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
	@RequestMapping (value="/out", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> requestOut(@RequestBody Map<String,Object> params,HttpServletRequest request)throws Exception{
		
		System.out.println("#########################/inout/out" + params.toString());
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("퇴근 체크 되었습니다.");
		
		String empKey = (String)params.get("empKey");
		String ymd = (String)params.get("ymd");
		String enterCd = null;
		String sabun = null;
		
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
			Map<String,Object> menus = inoutService.getMenuContext(enterCd, sabun); 
			if(menus == null || menus.get("ymd") == null) {
				rp.setFail("현재 출퇴근 가능한 상태가 없습니다.");
			} else if (!menus.get("ymd").equals(ymd) || !menus.get("inoutType").equals("OUT")) {
				rp.setFail("근태 정보가 일치하지 않습니다. 앱을 재실행 해주세요.");
			} 
			int cnt = inoutService.checkInoutHis(enterCd, sabun, "OUT", ymd);
			if(cnt == 2) {
				rp.setSuccess("퇴근 체크 하였습니다.");
			}
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("출퇴근 정보 기록 중 오류가 발생했습니다.");
		}
		return rp;
	}
}
