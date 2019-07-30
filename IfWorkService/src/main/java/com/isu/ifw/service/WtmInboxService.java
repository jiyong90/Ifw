package com.isu.ifw.service;

import org.springframework.stereotype.Service;

import com.isu.option.vo.ReturnParam;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("WtmInboxService")
public interface WtmInboxService{
	
	public void setInbox(Long tenantId, String enterCd, String sabun, String title);
	
	public ReturnParam getInboxCount(Long tenantId, String enterCd, String sabun);

	public ReturnParam getInboxList(Long tenantId, String enterCd, String sabun);
}