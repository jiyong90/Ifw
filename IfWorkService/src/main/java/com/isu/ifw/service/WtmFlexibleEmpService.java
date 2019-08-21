package com.isu.ifw.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.isu.ifw.entity.WtmWorkDayResult;
import com.isu.ifw.vo.WtmDayWorkVO;
import com.isu.ifw.vo.WtmWorkTermTimeVO;
import com.isu.option.vo.ReturnParam;

/**
 * 
 * @author 
 *
 */
public interface WtmFlexibleEmpService {
	/**
	 * 최종 출근 타각 정보
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param ymd - 출근기준일
	 * @param entryTypeCd
	 * @param sdate
	 * @param userId
	 */
	public void updEntrySdate(Long tenantId, String enterCd, String sabun, String ymd, String entryTypeCd, Date sdate, Long userId);
	/**
	 * 최종퇴근타각 정보
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param ymd - 퇴근기준일
	 * @param entryTypeCd
	 * @param sdate
	 * @param userId
	 */
	public void updEntryEdate(Long tenantId, String enterCd, String sabun, String ymd, String entryTypeCd, Date edate, Long userId);
	/**
	 * 근무 마감
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param ymd
	 * @param userId
	 */
	public void workClosed(Long tenantId, String enterCd, String sabun, String ymd, Long userId);
	/**
	 * 근무 마감 취소
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param ymd
	 * @param userId
	 */
	public void cancelWorkClosed(Long tenantId, String enterCd, String sabun, String ymd, Long userId);
	/**
	 * 해당 월의 근무제 정보 조회
	 * @param tenantId
	 * @param enterCd
	 * @param empNo
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getFlexibleEmpList(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId);

	/**
	 * 계획을 작성 해야 하는 유연근무제 리스트 조회
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param paramMap
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getFlexibleEmpListForPlan(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId);
	
	/**
	 * 해당 일의 근무 시간 조회
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param ymd
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getWorkDayResult(Long tenantId, String enterCd, String sabun, String ymd, Long userId);
	
	/**
	 * 근무제 기간에 대한 정보
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getFlexibleRangeInfo(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap);
	
	/**
	 * 근무일에 대한 정보
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getFlexibleDayInfo(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap);
	
	/**
	 * 선택한 날의 근무시간에 대한 정보
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getFlexibleWorkTimeInfo(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap);
	
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
	
	public List<WtmDayWorkVO> getDayWorks(Long flexibleEmpId, Long userId);
	public void createWorkteamEmpData(Long tenantId, String enterCd, Long workteamMgrId, Long userId); 
	
	/**
	 * calendar id로 일근무표 조회(관리자용)
	 * @param tenantId
	 * @param enterCd
	 * @param workCalendarId
	 * @return
	 */
	public List<Map<String, Object>> getEmpDayResults(Long tenantId, String enterCd, String sabun, String ymd);
	
	/**
	 * 일별상세 리스트 저장(관리자용)
	 * @param flexibleEmpId
	 * @param dateMap	
	 * @param userId
	 * @throws Exception
	 */
	public void saveEmpDayResults(Long tenantId, String enterCd, Long userId, Map<String, Object> convertMap) throws Exception;
	
	/**
	 * 근무시간 조회
	 * @param tenantId
	 * @param enterCd
	 * @param sabun
	 * @param paramMap
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getWorkHour(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId);
}
