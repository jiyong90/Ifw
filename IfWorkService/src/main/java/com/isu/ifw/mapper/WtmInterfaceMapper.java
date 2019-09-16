package com.isu.ifw.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

public interface WtmInterfaceMapper {
		
	/**
	 * 인터페이스 기록시간 조회
	 * @param paramMap
	 * @return 
	 */
	public Map<String, Object> getIfLastDate(Map<String, Object> paramMap);
	
	/**
	 * 인터페이스 기록시간 조회
	 * @param paramMap
	 * @return 
	 */
	public Map<String, Object> getIfNowDate(Map<String, Object> paramMap);
	
	/**
	 * 인터페이스 이력저장
	 * @param paramMap
	 * @return
	 */
	public int insertIfHis(Map<String, Object> paramMap);
	
	/*
	 * 임직원정보
	 */
	public List<WtmFlexibleStdVO> getWtmEmpHis(Map<String, Object> paramMap);
	
	/**
	 * 공통코드 ID조회
	 **/
	public Map<String, Object> getWtmCodeId(Map<String, Object> paramMap);
	
	/**
	 * 공통코드 저장
	 * @param paramMap
	 * @return
	 */
	public int insertWtmCode(List<Map<String, Object>> paramList);
	/**
	 * 공통코드 저장
	 * @param paramMap
	 * @return
	 */
	public int updateWtmCode(List<Map<String, Object>> paramList);
	
	/**
	 * 공통코드 저장 종료일 갱신
	 * @param paramMap
	 * @return
	 */
	public int updateWtmCodeHisEymd(Map<String, Object> paramMap);
	
	/**
	 * 공통코드 저장 시작일 갱신
	 * @param paramMap
	 * @return
	 */
	public int updateWtmCodeHisSymd(Map<String, Object> paramMap);
	
	/**
	 * 공휴일 저장
	 * @param paramMap
	 * @return
	 */
	public int insertWtmHoliday(List<Map<String, Object>> paramList);
	
	
	/**
	 * 근태코드 ID조회
	 **/
	public Map<String, Object> getWtmTaaCodeId(Map<String, Object> paramMap);
	
	/**
	 * 근태코드 수정
	 * @param paramMap
	 * @return
	 */
	public int updateTaaCode(List<Map<String, Object>> paramList);
	/**
	 * 근태코드 입력
	 * @param paramMap
	 * @return
	 */
	public int insertTaaCode(List<Map<String, Object>> paramList);
	
	/**
	 * 사원이력 입력
	 * @param paramMap
	 * @return
	 */
	public int insertEmpHisTemp(List<Map<String, Object>> paramList);
	
	public void setEmpHis(Map<String, Object> paramMap);
	
	/*
	 * 사원 기본근무 생성
	 */
	public List<Map<String, Object>> getEmpBaseList(Map<String, Object> paramMap);
	
}
