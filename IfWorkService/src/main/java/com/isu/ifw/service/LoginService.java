package com.isu.ifw.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.LoginMapper;
import com.isu.ifw.vo.Login;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("LoginService")
public class LoginService{
	
	@Autowired
	LoginMapper loginMapper;
	
	public Map<String, String> loginTryCnt(String loginEnterCd, String loginUserId, String loginPassword) throws Exception{
		return loginMapper.loginTryCnt(loginEnterCd, loginUserId, loginPassword);
	}
	
	public Login loginUser(String ssnLocaleCd, String localeCd, String baseLang,String loginEnterCd, String loginUserId, String loginPassword, String ssnSso) throws Exception{
		return loginMapper.loginUser(ssnLocaleCd, localeCd, baseLang, loginEnterCd, loginUserId, loginPassword, ssnSso);
	}
	
}