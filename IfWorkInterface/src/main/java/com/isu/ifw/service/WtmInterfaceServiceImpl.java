package com.isu.ifw.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.mapper.WtmInterfaceMapper;

@Service
public class WtmInterfaceServiceImpl implements WtmInterfaceService {

	@Autowired
	WtmInterfaceMapper wtmInterfaceMapper;

	@Override
	public void getCodeIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getCodeIfResult");
        try {
        	// if 기간 크용 마지막 자료조회
        	Map<String, Object> ifLastDate = wtmInterfaceMapper.getCodeLastDate(lastDataTime);
        	if(ifLastDate != null && ifLastDate.size() > 0) {
	        	String ifLastDateTime = ifLastDate.get("END_DATE").toString();
	        	System.out.println("ifLastDateTime : " + ifLastDateTime);
	        	List<Map<String, Object>> ifCodeList = wtmInterfaceMapper.getCode(lastDataTime, ifLastDateTime);
	        	
	        	if (ifCodeList != null && ifCodeList.size() > 0) {
	        		for(int i=0; i< ifCodeList.size(); i++) {
		        		Map<String, Object> ifCodeMap = new HashMap<>();
		        		ifCodeMap = ifCodeList.get(i);
		        		System.out.println(i + ", grcode : " + ifCodeMap.get("GRCODE_CD").toString());
		        		System.out.println(i + ", code : " + ifCodeMap.get("CODE").toString());
	        		}
	        	}
        	} else {
        		System.out.println("no data found " + lastDataTime);
        	}
        } catch(Exception e){
            e.printStackTrace();
        }
		return;
	}
}
