package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmWorkTermTimeVO;

/**
 * 
 * @author 
 *
 */
public interface WtmFlexibleEmpService {
	
	/**
	 * 해당 월의 근무제 정보 조회
	 * @param tenantId
	 * @param enterCd
	 * @param empNo
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getFlexibleEmpList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap);

	/**
	 * 선택한 기간의 근무제 정보 조회
	 * @param tenantId
	 * @param enterCd
	 * @param empNo
	 * @param paramMap
	 * @return
	 */
	public WtmWorkTermTimeVO getWorkTermTime(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap);
	
	/**
	 * 기존에 신청한 근무제 적용일 가져오기
	 * @param tenantId - 테넌트 아이디
	 * @param enterCd - 회사코드
	 * @param userKey - 대상자의 기존에 신청한 근무제 적용일을 가져온다.
	 * @return 
	 */
	public Map<String, Object> getPrevFlexible(Long tenantId, String enterCd, String userKey);
	
	/**
	 * 
	 * @param flexibleEmpId
	 * @param dateMap	{ dayResult : { "20190101" : {"shm" : "0800" , "ehm" : "0200"} } } -- ehm이더작을 경우 다음날로 인식한다
	 * @param userId
	 * @throws Exception
	 */
	public void save(Long flexibleEmpId, Map<String, Object> dateMap, Long userId) throws Exception;
	
}
