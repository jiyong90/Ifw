package com.isu.ifw.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WtmUtil {
	
	public static String parseDateStr(Date d, String format)  { 
		if(format == null || format.equals(""))
			format = "yyyyMMdd";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(d);
	}
	
	public static Date toDate(String dateStr, String format) {
		if(format == null || format.equals("")) {
			format = "yyyyMMdd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(format);
	    try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static long dayCnt(String begin, String end) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    Date beginDate = formatter.parse(begin);
	    Date endDate = formatter.parse(end);
	 
	    long diff = endDate.getTime() - beginDate.getTime();
	    long diffDays = diff / (24 * 60 * 60 * 1000) + 1;


	    return diffDays;
	}

}
