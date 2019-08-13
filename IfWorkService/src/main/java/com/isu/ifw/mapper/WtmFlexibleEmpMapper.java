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
	
	/**
	 * 휴게시간을 제외한 시간 계산
	 * @param paramMap { timeCdMgrId : 휴게시간아이디,shm : '2200', ehm : '0200'}
	 * @return { calcMinute : 22 } 
	 */
	public Map<String, Object> calcMinuteExceptBreaktime(Map<String, Object> paramMap);
	
	/**
	 * 근무제 기간에 대한 정보
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getFlexibleRangeInfo(Map<String, Object> paramMap);
	
	/**
	 * 선택한 날의 근무시간에 대한 정보
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getFlexibleWorkTimeInfo(Map<String, Object> paramMap);
	
	/**
	 * 이미 신청중이거나 등록된 근무정보가 있는지 확인하자
	 * @param paramMap { tenantId, sabun, enterCd, sYmd, eYmd, otSdate(DATE), otEdate(DATE)
	 * @return { workCnt : 2 }
	 */
	public Map<String, Object> checkDuplicateWorktime(Map<String, Object> paramMap);
	
	/**
	 * 근무조에 등록된 대상자를 WTM_FLEXIBLE_EMP 테이블에 생성한다.
	 * 단 근무조 기간에 중복된 데이터가 있는 대상자는 제외하고 등록한다.
	 * 근무조, 유연근무제 모두 중복된 데이터로 한다. 근무조는 근무조 관리에서 시작종료를 관리하기 때문에 선 수정 작업 
	 * 기본근무제는 체크하지 않고 중복데이터 삽입 - 이후에 FLEXIBLE_EMP의 시작종료일 정리하는 업데이트문이 필요하다
	 * @param paramMap
	 */
	public void createWorkteamOfWtmFlexibleEmp(Map<String, Object> paramMap);
	
	/**
	 * 중복된 데이터들 중에 같은 근무제의 중복은 기간 업데이트를 하자. 
	 * 예를 들어 근무조의 종료일만 변경했을 경우 - 종료일 변경 시 사전에 중복되는 지 여부를판단해야한다
	 * @param paramMap
	 */
	public void updateWorkteamOfWtmFlexibleEmp(Map<String, Object> paramMap);
	
	
}
