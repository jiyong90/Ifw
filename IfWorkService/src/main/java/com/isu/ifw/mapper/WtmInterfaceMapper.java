package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmCodeVO;
import com.isu.ifw.vo.WtmFlexibleStdVO;

public interface WtmInterfaceMapper {
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
	public int insertWtmCode(Map<String, Object> paramMap);
	/**
	 * 공통코드 저장
	 * @param paramMap
	 * @return
	 */
	public int updateWtmCode(Map<String, Object> paramMap);
	
	/**
	 * 공통코드 저장 종료일 갱신
	 * @param paramMap
	 * @return
	 */
	public int updateWtmCodeEymd(Map<String, Object> paramMap);
	/**
	 * 공통코드 eymd조회
	 **/
	public Map<String, Object> getWtmCodeEymd(Map<String, Object> paramMap);
}
