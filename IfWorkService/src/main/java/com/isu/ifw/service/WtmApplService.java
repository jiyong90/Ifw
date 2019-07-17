package com.isu.ifw.service;

import java.util.Map;

/**
 * 
 * @author 
 *
 */
public interface WtmApplService {
	//임시저장
	final static String APPL_STATUS_IMSI = "11";
	//결재처리중
	final static String APPL_STATUS_APPLY_ING = "21";
	//결재반려
	final static String APPL_STATUS_APPLY_REJECT = "22";
	//승인처리중
	final static String APPL_STATUS_APPR_ING = "31";
	//승인반려
	final static String APPL_STATUS_APPR_REJECT = "32";
	//취소처리완료
	final static String APPL_STATUS_CANCEL = "44";
	//처리완료
	final static String APPL_STATUS_APPR = "99";
	
	public void apply();
	/**
	 * 
	 * @param tenantId
	 * @param enterCd
	 * @param applId
	 * @param targetApplId -WTM_APPL이 부모 신청서다 하위에 근테 근무 등의 신청테이블이 붙는다.
	 * @param workTypeCd - WTM_APPL_CODE의 신청서 코드이다
	 * @param paramMap
	 * @param userId
	 */
	public void imsi(Long tenantId, String enterCd, Long applId, Long targetApplId, String workTypeCd, Map<String, Object> paramMap, String userId);
	
	
}
