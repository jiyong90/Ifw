package com.isu.ifw.vo;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

public class WtmGeneralVO extends HashMap {

	@Override
	public Object put(Object key, Object value) {
		
		String k = (String)key;
		
		try {
			// _ to camelCase
			if(k.contains("_") || k.equals(k.toUpperCase()))
				k = JdbcUtils.convertUnderscoreNameToPropertyName(k);
			
		}catch(Exception e) {
			e.printStackTrace();
			k = (String)key;
		}
		
		return super.put(k, value); 
	}
}
