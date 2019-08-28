package com.isu.ifw.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author 
 *
 */
public interface WtmInoutService {
	
	public Map<String, Object> getMenuContext(String enterCd, String sabun);
	public int checkInoutHis(String enterCd, String sabun, String inoutType, String ymd) throws Exception;
	
}
