package com.isu.ifw.intf.util;

import java.util.ResourceBundle;

public class AdapterProp {

	private static ResourceBundle rb = ResourceBundle.getBundle("adapter");
	
	/**
	 * API adapter 시스템에서 사용하는 프로퍼티의 값을 반환한다.
	 * @param key
	 * @return
	 */
	public static String getPropValue(String key) {
		if(rb != null) {
			return rb.getString(key);
		}
		
		return null;
	}
	
	/**
	 * 암호화 대상키 배열을 반환한다
	 * @return
	 */
	public static String[] getEncCols() {
		String encCols = getPropValue("encrypt.columnKey");
		System.out.println("encCols : " + encCols);
		
		if(encCols != null && !"".equals(encCols)) {
			return encCols.split(",");
		}
		
		return null;
	}
	
	
	
}
