package com.isu.ifw.intf.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.intf.mapper.GreenMobileApiMapper;
import com.isu.ifw.intf.mapper.WtmIntfMapper;
import com.isu.ifw.intf.vo.ReturnParam;

@Service
public class GreenMobileApiService {
	
	@Autowired
	private GreenMobileApiMapper mobile;
	
	@Autowired
	private WtmIntfMapper intfMapper;
	
	public String createAccessToken(String enterCd, String sabun){
		String accessToken = UUID.randomUUID().toString();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("enterCd", sabun);
		paramMap.put("sabun", sabun);
		paramMap.put("accessToken", accessToken);
		paramMap.put("note", "");
		
		int res = mobile.saveMobileSession(paramMap);
		if(res > 0) {
			return accessToken;
		}
		return null;
	}
	
	public String updateAccessToken(String enterCd, String sabun){
		String accessToken = UUID.randomUUID().toString();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("enterCd", sabun);
		paramMap.put("sabun", sabun);
		paramMap.put("accessToken", accessToken);
		
		int res = mobile.updateAccessToken(paramMap);
		if(res > 0) {
			return accessToken;
		}
		return null;
	}
	
	public ReturnParam greenValidEmp(String locale, String empKey,  String accessToken){
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		ReturnParam rp = new ReturnParam();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("enterCd", sabun);
		paramMap.put("sabun", sabun);
		paramMap.put("accessToken", accessToken);
		
		Map<String, Object> resMap = mobile.getMobileSession(paramMap);
		if(resMap != null) {
			//올바른 사용자다 토큰을 갱신하자
			String newAccessToken = updateAccessToken(enterCd, sabun);
			
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("EmpID", sabun);		
			
			Map<String, Object> sessionData = intfMapper.getWtmEmpByEmpID(pMap);
			sessionData.put("accessToken", newAccessToken);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			resultMap.put("sessionData", sessionData);
			rp.setSuccess("");
			rp.put("result", resultMap);
		}else {
			rp.setFail("인증 실패");
		}
		return rp;
	}
}
