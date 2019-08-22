package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

import com.isu.option.vo.ReturnParam;

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
	

	//결재처리 코드
	final static String APPR_STATUS_REQUEST = "10";	//결재요청
	final static String APPR_STATUS_APPLY = "20";	//결재완료
	final static String APPR_STATUS_REJECT = "30";	//반려
	
	final static String APPL_LINE_I = "1"; //기안
	final static String APPL_LINE_S = "2"; //발신결재
	final static String APPL_LINE_R = "3"; //수신결재
	
	

	//소정근무시간
	final static String TIME_TYPE_BASE = "BASE";
	//연장근무
	final static String TIME_TYPE_OT = "OT";
	
	public Map<String, Object> getAppl(Long applId);
	
	public List<Map<String, Object>> getPrevApplList(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId);
	
	public Map<String, Object> getLastAppl(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap, Long userId);
	/**
	 * 승인/반려 신청서 리스트(결재함)
	 * @param tenantId
	 * @param enterCd
	 * @param empNo
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getApprList(Long tenantId, String enterCd, String empNo, Map<String, Object> paramMap, Long userId);
	
	public void request(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap, String sabun, Long userId) throws Exception;
	public void apply(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap, String sabun, Long userId) throws Exception;
	public void reject(Long tenantId, String enterCd, Long applId, int apprSeq, Map<String, Object> paramMap, String sabun, Long userId) throws Exception;
	
	/**
	 * 
	 * @param tenantId
	 * @param enterCd
	 * @param applId - 
	 * @param targetApplId -WTM_APPL이 부모 신청서다 하위에 근테 근무 등의 신청테이블이 붙는다.
	 * @param workTypeCd - WTM_APPL_CODE의 신청서 코드이다
	 * @param paramMap - 신청서별 필요한 추가 파라메터들을 담는다.
	 * @param sabun
	 */
	public ReturnParam imsi(Long tenantId, String enterCd, Long applId, String workTypeCd, Map<String, Object> paramMap, String status, String sabun, Long userId) throws Exception;
	
	/**
	 * 사전점검
	 * @param tenantId
	 * @param enterCd
	 * @param applId
	 * @param workTypeCd
	 * @param paramMap
	 * @return
	 */
	public ReturnParam preCheck(Long tenantId, String enterCd, String sabun, String workTypeCd, Map<String, Object> paramMap);
	public ReturnParam validate(Long tenantId, String enterCd, String sabun, String workTypeCd, Map<String, Object> paramMap);
	
	public void sendPush();

	
	
}
