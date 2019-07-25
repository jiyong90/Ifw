package com.isu.ifw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmInbox;
import com.isu.ifw.vo.WtmInboxVO;

/**
 * 로그인 서비스
 *
 * @author ParkMoohun
 *
 */
@Service("WtmInboxService")
public interface WtmInboxService{
	
	public void setInbox(Long tenantId, String enterCd, String sabun, String title);
	
	public int getInboxCount(Long tenantId, String enterCd, String sabun);

	public List<Map<String, Object>> getInboxList(Long tenantId, String enterCd, String sabun);
}