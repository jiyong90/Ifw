package com.isu.ifw.intf.service;

import java.util.List;
import java.util.Map;

import com.isu.ifw.intf.vo.WtmFlexibleStdVO;

/**
 * 인사자료 이관용 인터페이스
 * @author lhj
 *
 */
public interface WtmInterfaceService {

	/**
	 * 공통코드 이관
	 * @param lastDataTime - 최종 이관데이터 시간
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getCodeIfResult(String tenantId, String lastDataTime) throws Exception;
	public void sendCode(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 공휴일정보 이관
	 * @param lastDataTime - 최종 이관데이터 시간
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getHolidayIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 근태코드정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getGntCodeIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 조직코드정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgCodeIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 조직도 기준정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgChartMgrIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 조직도 상세정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgChartDetIfResult(String lastDataTime, String enterCd, String symd) throws Exception;
	
	/**
	 * 사업장, 근무조 정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgMapCodeIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 임직원정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getEmpHisIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 조직장(겸직)정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgConcIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 임직원정보 메일연락처 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getEmpAddrIfResult(String tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 근태정보 이관용
	 * @param lastDataTime - 최종갱신일
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getTaaApplIfResult(String tenantId, String lastDataTime) throws Exception;

	String getEnterCd(String tenantId) throws Exception;
	
	
}
