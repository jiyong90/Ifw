package com.isu.ifw.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WtmUtil {
	
	public static String parseDateStr(Date d, String format)  { 
		if(format == null || format.equals(""))
			format = "yyyyMMdd";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(d);
	}

}
