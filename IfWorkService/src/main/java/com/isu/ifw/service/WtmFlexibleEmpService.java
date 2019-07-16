package com.isu.ifw.service;

import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleEmpVO;

/**
 * 
 * @author 
 *
 */
public interface WtmFlexibleEmpService {

	/**
	 * 선택한 기간의 근무제 정보 조회
	 * @param tenantId
	 * @param enterCd
	 * @param empNo
	 * @param paramMap
	 * @return
	 */
	public WtmFlexibleEmpVO getFlexibleEmp(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap);
	
	/**
	 * 기존에 신청한 근무제 적용일 가져오기
	 * @param tenantId - 테넌트 아이디
	 * @param enterCd - 회사코드
	 * @param userKey - 대상자의 기존에 신청한 근무제 적용일을 가져온다.
	 * @return 
	 */
	public Map<String, Object> getPrevFlexible(Long tenantId, String enterCd, String userKey);
	
}
