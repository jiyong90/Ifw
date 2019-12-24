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
	
	public String enterCd = "";
	
	@Override
	public String getEnterCd(String tenantId) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("getEnterCd tenantId : " + tenantId);
		Map<String, Object> retMap = new HashMap<>();

		try {
			retMap = wtmInterfaceMapper.getEnterCd(tenantId);
			if(retMap != null && retMap.size() > 0) {
				enterCd = retMap.get("ENTER_CD").toString();
			} else {
				// 이관이력이 없으면 그냥 과거부터 쭉쭉 옮기자
				enterCd = "";
			}
		} catch(Exception e){
            e.printStackTrace();
        }
		return enterCd;
	}

	@Override
	public List<Map<String, Object>> getCodeIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getCodeIfResult");
		List<Map<String, Object>> ifCodeList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifCodeList = wtmInterfaceMapper.getCode(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifCodeList;
	}
	
	@Override
	public List<Map<String, Object>> getHolidayIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getHolidayIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getHoliday(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getGntCodeIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getTaaCodeIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getGnt(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgCodeIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgCodeIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getOrg(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgChartMgrIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgChartMgrIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getOrgChart(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgChartDetIfResult(String lastDataTime, String enterCd, String symd) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgChartDetIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("symd", symd);
        			
        	ifList = wtmInterfaceMapper.getOrgChartDtl(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgMapCodeIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgMapCodeIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getOrgMap(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getEmpHisIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getEmpHisIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getEmp(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getOrgConcIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgConcIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getOrgConc(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getEmpAddrIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getEmpAddrIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getEmpAddr(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
	@Override
	public List<Map<String, Object>> getTaaApplIfResult(String tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getTaaApplIfResult");
		List<Map<String, Object>> ifList = null;
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// if 데이터 조회
        	Map<String, Object> paramMap = new HashMap<>();
        	paramMap.put("enterCd", enterCd);
        	paramMap.put("lastDataTime", lastDataTime);
        	ifList = wtmInterfaceMapper.getTaaAppl(paramMap);
        } catch(Exception e){
            e.printStackTrace();
        }
		return ifList;
	}
	
}
