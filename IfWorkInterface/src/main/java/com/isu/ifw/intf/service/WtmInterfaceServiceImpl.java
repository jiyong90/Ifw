package com.isu.ifw.intf.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.intf.mapper.WtmInterfaceMapper;
import com.isu.ifw.intf.mapper.WtmIntfMapper;

@Service
public class WtmInterfaceServiceImpl implements WtmInterfaceService {


	@Value("${ifw.code-post}")
	private String codeUrl;
	@Value("${ifw.emp-post}")
	private String empUrl;
	@Value("${ifw.empaddr-post}")
	private String empaddrUrl;
	@Value("${ifw.gnt-post}")
	private String gntUrl;
	@Value("${ifw.holiday-post}")
	private String holidayUrl;
	@Value("${ifw.org-post}")
	private String orgUrl;
	@Value("${ifw.orgconc-post}")
	private String orgConcUrl;
	@Value("${ifw.orgchart-post}")
	private String orgChartUrl;
	@Value("${ifw.taaappl-post}")
	private String taaApplUrl;
	
	@Autowired
	WtmInterfaceMapper wtmInterfaceMapper;
	
	@Autowired
	WtmIntfMapper intfMapper;
	
	@Autowired
	ExchangeService exchangeService;
	
	public String enterCd = "";
	
	@Override
	public void sendData(String T, Map<String, Object> paramMap) throws Exception{
		System.out.println("WtmInterfaceServiceImpl getCode");
		List<Map<String, Object>> dataList = null;
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("================================");
		System.out.println(mapper.writeValueAsString(paramMap));
		
		System.out.println("================================");
		
		String url = "";
        try {
        	if(T.equalsIgnoreCase("CODE")) {
        		dataList = intfMapper.getWtmCode(paramMap);
        		url = codeUrl;
        	}else if(T.equalsIgnoreCase("EMP")) {
        		dataList = intfMapper.getWtmEmp(paramMap);
        		url = empUrl;
        	}else if(T.equalsIgnoreCase("EMPMULTI")) {
        		dataList = intfMapper.getWtmEmpMulti(paramMap);
        		url = empUrl;
        	}else if(T.equalsIgnoreCase("EMPADDR")) {
        		dataList = intfMapper.getWtmEmpAddr(paramMap);
        		url = empaddrUrl;
        	}else if(T.equalsIgnoreCase("GNT")) {
        		dataList = intfMapper.getWtmGnt(paramMap);
        		url = gntUrl;
        	}else if(T.equalsIgnoreCase("HOLIDAY")) {
        		dataList = intfMapper.getWtmHoliday(paramMap);
        		url = holidayUrl;
        	}else if(T.equalsIgnoreCase("ORG")) {
        		dataList = intfMapper.getWtmOrg(paramMap);
        		url = orgUrl;
        	}else if(T.equalsIgnoreCase("ORGCONC")) {
        		dataList = intfMapper.getWtmOrgConc(paramMap);
        		url = orgConcUrl;
        	}else if(T.equalsIgnoreCase("TAAAPPL")) {
        		paramMap.put("ifDate", "2020-01-01 00:00");
        		//디비 읽기전에 파일
        		try {
       	        	FileReader rw = new FileReader("./intfTime.txt");
                    BufferedReader br = new BufferedReader(rw);
                 	String readLine = null ;
    	            while( ( readLine =  br.readLine()) != null ){
    	            	System.out.println("========= a " + readLine);
    	            	paramMap.put("ifDate", readLine);
    	            }
        		} catch(Exception e) {System.out.println(e.getMessage());}    
        		
        		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm", Locale.KOREA );
         		String ifDate = format.format(new Date());
         		
         		//시간 미리 조회해놓고 db쿼리
        		dataList = intfMapper.getWtmTaaAppl(paramMap);
        		
        		FileWriter fw = new FileWriter("./intfTime.txt");
                BufferedWriter bw = new BufferedWriter( fw );
                bw.write(ifDate); 
                bw.flush();
	            System.out.println("========= b " + ifDate);
 
                url = taaApplUrl;
        	}else if(T.equalsIgnoreCase("ORGCHART")) {
        		dataList = intfMapper.getWtmOrgChart(paramMap);
        		url = orgChartUrl;
        	}else {
        		dataList = null;
        	}
        	if(dataList != null) {
	        	Map<String, Object> eParam = new HashMap<>();
	        	eParam.put("data", dataList);
	    		System.out.println("================================");
	    		System.out.println(mapper.writeValueAsString(dataList));
	    		System.out.println("================================");
	        	exchangeService.exchange(url, HttpMethod.POST, null, eParam);
        	}else {
        		System.out.println(T + " is no data. " + mapper.writeValueAsString(paramMap));
        	}

        } catch(Exception e){
            e.printStackTrace(); 
        }
        
	}
	
	@Override
	public void receiveData(String T, Map<String, Object> paramMap) throws Exception{
		System.out.println("############ receiveData");
		List<Map<String, Object>> dataList = null;
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("================================");
		System.out.println(mapper.writeValueAsString(paramMap));
		
		System.out.println("================================");
		
		String url = "";
        try {
        	int cnt = 0;
        	if(paramMap.containsKey("data")) {
        		List<Map<String, Object>> list = (List<Map<String, Object>>) paramMap.get("data");
            	
        		if(list != null && list.size() > 0) {
        			System.out.println("intf total : " + list.size() );

        			for(Map<String, Object> data : list) {
          				int temp = 0;
            			if(T.equalsIgnoreCase("OT")) {
          					temp = intfMapper.saveIntfOtData(data);
                    	}else if(T.equalsIgnoreCase("COMP")) {
                    		temp = intfMapper.saveIntfCompData(data);
                    	}
          				if(temp == 0) {
          					System.out.println("intf fail : " + data.toString());
          				} else {
          					cnt += temp ;
          					System.out.println("intf success : " + data.toString());
          				}
            		}
            	}
        	}
			System.out.println("intf totalsuccess : " + cnt);
				 	
        } catch(Exception e){
            e.printStackTrace(); 
        }
        
	}
	
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
	
	@Override
	public int setWorkTimeClose(String tenantId, List<Map<String, Object>> compList) throws Exception {
		// TODO Auto-generated method stub
		int cnt = 0;
		System.out.println("WtmInterfaceServiceImpl setWorkTimeClose");
        try {
        	//회사코드 조회
        	enterCd = getEnterCd(tenantId);
        	// 루프돌려서 저장하기
        	for(int i=0; i<compList.size(); i++) {
	        	Map<String, Object> paramMap = compList.get(i);
	        	paramMap.put("enterCd", enterCd);
	        	// paramMap.put("enterCd", "BRS");
	        	System.out.println("paramMap" + paramMap.toString());
	        	int rtn = wtmInterfaceMapper.insertCompBrs(paramMap);
        	}
        } catch(Exception e){
            e.printStackTrace();
        }
		return cnt;
	}
	
}
