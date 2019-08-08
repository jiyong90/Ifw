package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.isu.ifw.vo.WtmWorkTermTimeVO;

public interface WtmFlexibleEmpMapper {
	
	/**
	 * 해당 월의 근무제 정보 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getFlexibleEmpList(Map<String, Object> paramMap);
	
	/**
	 * 해당 일의 근무 시간 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getWorkDayResult(Map<String, Object> paramMap);
	
	/**
	 * 선택한 기간의 근무제 정보 조회
	 * @param paramMap
	 * @return
	 */
	public WtmWorkTermTimeVO getWorkTermTime(Map<String, Object> paramMap);
	
	/**
	 * 기존에 신청한 근무제 적용일 가져오기
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getPrevFlexible(Map<String, Object> paramMap);
	
	public void createWorkCalendar(@Param("flexibleEmpId")Long flexibleEmpId, @Param("userId")Long userId);
	
	public void updateHolidayYnOFWorkCalendar(Map<String, Object> paramMap);
	
	public void updatePlanMinute(@Param("flexibleEmpId") Long flexibleEmpId);
	/**
	 * 인정시간 갱신
	 * @param paramMap
	 */
	public void updateApprDatetimeByYmdAndSabun(Map<String, Object> paramMap);
	/**
	 * 인정시간의 분 계산 - 휴게시간 제외
	 * @param paramMap
	 */
	public void updateApprMinuteByYmdAndSabun(Map<String, Object> paramMap);
	
	public Map<String, Object> checkBaseWorktime(@Param("flexibleEmpId") Long flexibleEmpId);
	
	public List<Map<String, Object>> getWorktimePlan(@Param("flexibleEmpId") Long flexibleEmpId);
	
	/**
	 * 근무제 기간에서 특정일 포함 이전 근무시간 합(분) - 인정 분 이 없을 경우 계획 분으로   
	 * @param paramMap - tenantId, enterCd, sabun, ymd
	 * @return { totalApprMinute : 22 }
	 */
	public Map<String, Object> getTotalApprMinute(Map<String, Object> paramMap);
	
	/**
	 * 근무제 기간에서 특정일 이후부터 근무제 적용 종료기간 까지의 코어시간의 합을 가지고 온다.
	 * 사용처 : 연장근무 신청 시 소정근로 선 소진사용여부에 따라 사용할 수 있다. 
	 * @param paramMap - tenantId, enterCd, sabun,  ymd
	 * @return { coreHm : 22 }
	 */
	public Map<String, Object> getTotalCoretime(Map<String, Object> paramMap);
	/**
	 * 근무제 기간에서 연장근무 시간을 가지고 오자
	 * @param paramMap
	 * @return { otMinute : 22 }
	 */
	public Map<String, Object> getSumOtMinute(Map<String, Object> paramMap);
	
}
