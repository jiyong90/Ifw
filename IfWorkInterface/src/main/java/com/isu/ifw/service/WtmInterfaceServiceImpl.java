package com.isu.ifw.service;

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
	
	@Override
	public List<Map<String, Object>> getHolidayIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getHolidayIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getHoliday(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getGntCodeIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getTaaCodeIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getGnt(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgCodeIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgCodeIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getOrg(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgChartMgrIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgChartMgrIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getOrgChart(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgChartDetIfResult(String lastDataTime, String enterCd, String sdate) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgChartDetIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("sdate", sdate);
        			
        	ifList = wtmInterfaceMapper.getOrgChartDtl(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgMapCodeIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgMapCodeIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getOrgMap(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getEmpHisIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getEmpHisIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getEmp(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getEmpAddrIfResult(String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getEmpAddrIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	ifList = wtmInterfaceMapper.getEmpAddr(lastDataTime);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
}
