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
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public void getCodeIfResult(Long tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 공휴일정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public void getHolidayIfResult(Long tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 근태코드정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public void getTaaCodeIfResult(Long tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 조직코드정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public void getOrgCodeIfResult(Long tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 사업장, 근무조 정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public void getOrgMapCodeIfResult(Long tenantId, String lastDataTime) throws Exception;
	
	/**
	 * 임직원정보 이관
	 * @param tenantId - 테넌트 아이디
	 * @return 
	 * @throws Exception 
	 */
	public void getEmpHisIfResult(Long tenantId, String lastDataTime) throws Exception;
}
