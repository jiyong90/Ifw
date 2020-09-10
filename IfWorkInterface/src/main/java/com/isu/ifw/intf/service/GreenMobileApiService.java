package com.isu.ifw.intf.service;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		System.out.println("######################### 1111 ");
		String accessToken = UUID.randomUUID().toString();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("accessToken", accessToken);
		paramMap.put("note", "");
		try {
			List<Map<String, Object>> resMap = mobile.getMobileSession(paramMap);
			if(resMap != null && resMap.size() > 0) {
				System.out.println("######################### 3333 ");
				int res = mobile.updateAccessToken(paramMap);
				System.out.println("#################### res " + res);
				if(res > 0) {
					return accessToken;
				}
				//this.updateAccessToken(enterCd, sabun);
			}else {
				System.out.println("######################### 2222 ");
				int res = mobile.saveMobileSession(paramMap);
				System.out.println("#################### res " + res);
				if(res > 0) {
					return accessToken;
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
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
	
	public String greenEmpPhotoOut(String locale, String empKey) {
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		
		System.out.println("#########################/employee/image " + paramMap.toString());
		Map<String, Object> emp = mobile.getEmpPhotoOut(paramMap);
		if(emp != null && emp.containsKey("Photo")) {
			return emp.get("Photo").toString();
		}
		return null;
	}
	
	public ReturnParam greenValidEmp(String locale, String empKey,  String accessToken) throws CertificateException{
		String enterCd =  empKey.split("@")[0];
		String sabun =  empKey.split("@")[1];
		ReturnParam rp = new ReturnParam();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("accessToken", accessToken);
		System.out.println("##################### 잉?");
		Map<String, Object> resMap = mobile.getCheckSession(paramMap);
		if(resMap != null) {
			//올바른 사용자다 토큰을 갱신하자, 하지마
			
//			String newAccessToken = updateAccessToken(enterCd, sabun);
			
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("EmpID", sabun);		
			
			Map<String, Object> sessionData = intfMapper.getWtmEmpByEmpID(pMap);
			System.out.println("##################### sessionData " + sessionData.toString());
			List<String> listdata = new ArrayList();
			if(sessionData.containsKey("LEADER_YN") && sessionData.get("LEADER_YN").equals("Y")) {
				listdata.add("leader");
			}
			
			if(sessionData.containsKey("EMP_NM")) {
				sessionData.put("empNm", sessionData.get("EMP_NM"));
			}
			
			sessionData.put("authCode", listdata);
			sessionData.put("accessToken", accessToken);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			resultMap.put("sessionData", sessionData);
			rp.setSuccess("");
			rp.put("result", resultMap);
			System.out.println("##################### rp " + rp.toString());

		}else {
			throw new CertificateException("인증 실패");
		}
		return rp;
	}
}
