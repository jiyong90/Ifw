package com.isu.ifw.mapper;

import java.util.List;
import java.util.Map;

import com.isu.ifw.vo.WtmFlexibleStdVO;

public interface WtmInterfaceMapper {
	
	/**
	 * 공통코드 마지막 데이터 조회
	 **/
	public Map<String, Object> getCodeLastDate(String lastDataTime);
	
	/**
	 * 공통코드 ID조회
	 **/
	public List<Map<String, Object>> getCode(String lastDataTime, String ifLastDateTime);
	
}
