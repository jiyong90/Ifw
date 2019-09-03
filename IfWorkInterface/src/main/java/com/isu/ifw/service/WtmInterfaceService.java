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
	public void getCodeIfResult(String lastDataTime) throws Exception;
	
	
}
