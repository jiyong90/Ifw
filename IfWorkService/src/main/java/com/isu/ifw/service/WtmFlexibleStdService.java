package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

/**
 * 근태담당자가 회사에 적용할 근무제도 기준을 설정한다
 * 유연근무제 기준을 등록하는 서비스
 * @author claire
 *
 */
public interface WtmFlexibleStdService {

	/**
	 * 미리정의된 근무제 조회
	 * @param tenantId - 테넌트 아이디
	 * @param enterCd - 회사코드
	 * @param userId - 대상자의 신청가능한 근무제를 가져온다
	 * @return 
	 */
	public List<WtmFlexibleStdVO> getFlexibleStd(Long tenantId, String enterCd, Long userId);
	
	public void saveFlexibleStd(Long tenantId, String enterCd, Map<String, Object> optionMap);
	

	/**
	 * 전체 근무제 목록 조회
	 * @param tenantId - 테넌트 아이디
	 * @param enterCd - 회사코드
	 * @return 
	 */
	public List<Map<String, Object>> getFlexibleStd(Long tenantId, String enterCd);
	
	/**
	 * 근무유형별 근무제 목록 조회
	 * @param tenantId - 테넌트 아이디
	 * @param enterCd - 회사코드
	 * @param workTypeCd - 근무유형
	 * @return 
	 */
	public List<Map<String, Object>> getFlexibleStdWorkType(Long tenantId, String enterCd, String workTypeCd);

	//public void saveFlexibleStdOption();
	//public void saveFlexibleStdBreaktime();
	
}
