package com.isu.ifw.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.auth.config.AuthConfigProvider;
import com.isu.auth.config.data.AuthConfig;
import com.isu.option.service.TenantConfigManagerService;

/**
 * 공통 로직을 구현한 인증 정보 제공자 클래스
 */
@Service("authConfigProvider")
public class CusAuthConfigProvider implements AuthConfigProvider{

	@Autowired
	private TenantConfigManagerService tcms;
	

	@Override
	public AuthConfig initConfig(Long tenantId, Object... initParam) {
		//logger.debug("init config called");
		String tenantKey = (String)initParam[0];
		CusAuthConfig config = new CusAuthConfig();
		// 설정 정보를 받아온다.
		String shaKey = tcms.getConfigValue(tenantId, "SECURITY.SHA.KEY", true, null);
		String shaRepeat = tcms.getConfigValue(tenantId, "SECURITY.SHA.REPEAT", true, null);
		
		if(shaKey != null)
			config.setShaKey(shaKey);
		if(shaRepeat != null ){
			try{
				config.setRepeatCount(Integer.parseInt(shaRepeat));
			}catch(Exception e){
				config.setRepeatCount(1);
			}
		}
		
		config.setTsId(tenantKey);
		return config;
	}

}
