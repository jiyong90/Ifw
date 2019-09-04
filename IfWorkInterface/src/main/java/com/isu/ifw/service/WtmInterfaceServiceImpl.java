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
	public List<Map<String, Object>> getCodeIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getCodeIfResult");
		List<Map<String, Object>> ifCodeList = null;
        try {
        	// if 데이터 조회
        	ifCodeList = wtmInterfaceMapper.getCode(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifCodeList;
	}
}
