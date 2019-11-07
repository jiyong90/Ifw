package com.isu.ifw.auth;

import com.isu.auth.config.data.AuthConfig;
import com.isu.auth.config.data.Endpoint;
import com.isu.auth.config.data.RestEndpoint;
import com.isu.auth.config.data.SessionManagementConfig;

/**
 * 보안 관련한 설정 항목 정의부분
 * @author tykim
 *
 */
public class CusAuthConfig implements AuthConfig {

	
	String tsId;
	String shaKey;
	int repeatCount;
	
	public void setShaKey(String shaKey){
		this.shaKey = shaKey;
	}
	
	public void setRepeatCount(int repeatCount){
		this.repeatCount = repeatCount;
	}
	
	public void setTsId(String tsId){
		this.tsId = tsId;
	}

	@Override
	public Endpoint getLoginFormSubmitEndpoint(){
		RestEndpoint ep = new RestEndpoint("/login/certificate/"+tsId);
		ep.setMethod(RestEndpoint.METHOD_POST);
		return ep;
	}
	
	@Override
	public Endpoint getCertificationEndpoint() {
//		RestEndpoint ep = new RestEndpoint("/api/login/certificate/"+tsId+"/user");
//		ep.setMethod(RestEndpoint.METHOD_POST);
//		return ep;
		return null;
	}


	//클라우드 버전에서는 사용안한다. DB에서 테넌트별로 관리되어야 한다..... 2018.11.26 jyp
	@Override
	public String getCertificateMethod() {
		//return AuthConfig.CERTIFICATE_TYPE_REST;
		return AuthConfig.CERTIFICATE_TYPE_SQL;
	}

	@Override
	public String getCertificateQuery() {
		//return "select a.*, a.user_id as \"userKey\" from comm_user a where a.login_id = :loginId and a.tenant_id = :tenantId";
		return " select   a.login_id as \"loginId\"                                           "
				+ "      , a.user_id as \"userId\"                           "
				+ "      , F_WTM_AES_DECRYPT(a.login_id,  :enterCd) as \"empNo\" "
				+ "      , F_WTM_AES_DECRYPT(a.enter_cd, :enterCd) as \"enterCd\" "
				+ "      , a.* "
				+ "   from comm_user a                                     "
				+ "   join comm_management_infomation i                    "
				+ "     on a.tenant_id = i.tenant_id                       "
				+ "  where a.tenant_id = :tenantId                                 "
				+ "    and info_key= 'SECURITY.AES.KEY'                     "
				+ "    and a.enter_cd = F_WTM_AES_ENCRYPT(:enterCd, :enterCd)    "
				+ " 	and a.login_id = F_WTM_AES_ENCRYPT(:loginId, :enterCd)   "
			 ;
	}

	@Override
	public String getEncryptKey() {
		return this.shaKey;
	}

	@Override
	public int getHashIterationCount() {
		return this.repeatCount;
	}

	@Override
	public Endpoint getSessionInformationEndpoint() {
		return null;
	}

	@Override
	public Endpoint getSessionValidationEndpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Endpoint getLogoutEndpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Endpoint getLoginPageEndpoint() {
		RestEndpoint ep = new RestEndpoint("/login/"+tsId);
		ep.setMethod(RestEndpoint.METHOD_GET);
		return ep;
	}

	@Override
	public Endpoint getSessionValidationFailRedirectionPageEndpoint() {
		RestEndpoint ep = new RestEndpoint("/login/"+tsId);
		ep.setMethod(RestEndpoint.METHOD_GET);
		return ep;
	}

	@Override
	public Endpoint getMainPageEndpoint() {
		//String url = "/console/"+this.tsId+"/";
		String url = "/console/"+this.tsId+"/";
		Endpoint ep = new Endpoint(url);
		return ep;
	}

	@Override
	public SessionManagementConfig getSessionManagementConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLoginIdParameterName() {
		return "loginId";
	}

	@Override
	public String getPasswordParameterName() {
		return "password";
	}

	@Override
	public Endpoint getAddUserEndpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthCodeQuery() { 
		return "select a.comm_oauth_session_id as oauthSessionId, a.user_token as userToken, a.emp_key as empKey, a.status, a.create_datetime as createDatetime, a.invaliddate_datetime as invalidate_datetime from comm_oauth_session a where a.comm_oauth_session_id = :oauthSessionId";
	}
	
	@Override
	public boolean useCookieTime() {
		return true;
	}
	
	@Override
	public int getSetCookieTime() {
		return 60*30; //30분
	}

}
