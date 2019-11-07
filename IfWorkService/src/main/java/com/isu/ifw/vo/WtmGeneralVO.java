package com.isu.ifw.vo;

import java.util.HashMap;

public class WtmGeneralVO extends HashMap {

	@Override
	public Object put(Object key, Object value) {
		
		String k = (String)key;
		
		System.out.println("k ::::::::::: " + k);
		
		try {
			// _ to camelCase
			if(k !=null && !"".equals(k)) {
				k = k.toLowerCase();
				
				while( k.lastIndexOf("_")!=k.length()-1 && k.contains("_")) {
		            k = k.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(k.charAt(k.indexOf("_") + 1))));
		        }
			}
		}catch(Exception e) {
			e.printStackTrace();
			k = (String)key;
		}
		
		System.out.println("change k ::::::::::: " + k);
		
		return super.put(k, value);
	}
}
