package com.isu.ifw.auth;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.isu.auth.config.data.SessionManagementConfig;
import com.isu.auth.service.TableBaseSessionValidator;

/**
 * 공통 세션 유효성 검사 클래스
 * @author tykim
 *
 */
@Component
public class CusSessionValidator extends TableBaseSessionValidator {

	@Override
	protected boolean checkSessionRule(Map sessionData, Date loginDate, String loginStatus,	SessionManagementConfig config) {
		// TODO Auto-generated method stub
		return true;
	}



}
