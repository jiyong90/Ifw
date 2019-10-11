package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

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
	public List<Map<String, Object>> getCodeIfResult(String lastDataTime) throws Exception;
	
	/**
	 * 공휴일정보 이관
	 * @param lastDataTime - 최종 이관데이터 시간
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getHolidayIfResult(String lastDataTime) throws Exception;
	
	/**
	 * 근태코드정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getGntCodeIfResult(String lastDataTime) throws Exception;
	
	/**
	 * 조직코드정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgCodeIfResult(String lastDataTime) throws Exception;
	
	/**
	 * 조직도 기준정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgChartMgrIfResult(String lastDataTime) throws Exception;
	
	/**
	 * 조직도 상세정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgChartDetIfResult(String lastDataTime, String enterCd, String sdate) throws Exception;
	
	/**
	 * 사업장, 근무조 정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getOrgMapCodeIfResult(String lastDataTime) throws Exception;
	
	/**
	 * 임직원정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getEmpHisIfResult(String lastDataTime) throws Exception;
}
