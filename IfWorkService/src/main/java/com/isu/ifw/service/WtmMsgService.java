package com.isu.ifw.service;

import java.util.List;

import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmMessageVO;

public interface WtmMsgService {

	/**
	 * 메시지 전송
	 * @param tenantId
	 * @param enterCd
	 * @param msgVO
	 */
	public ReturnParam sendMsg(Long tenantId, String enterCd, WtmMessageVO msgVO);
	
	/**
	 * 인증코드 발송
	 * @param tenantId
	 * @param enterCd
	 * @param userInfo
	 * @return
	 */
	public ReturnParam sendCertificateCodeForChangePw(Long tenantId, String enterCd, String userInfo);
	
	/**
	 * 신청서 메일 발송
	 * @param tenantId
	 * @param enterCd
	 * @param fromSabun
	 * @param toSabuns
	 * @param applCode
	 * @param type
	 * APPR 결재요청
	 * APPLY 결재
     * REJECT 반려
	 * @return
	 */
	public ReturnParam sendMailForAppl(Long tenantId, String enterCd, String fromSabun, List<String> toSabuns, String applCode, String type);
}
