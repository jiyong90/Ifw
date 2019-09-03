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

	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	// 일단 63번서버 KABNAG에 연결하기
	String DB_URL = "jdbc:oracle:thin:@203.231.40.63:1521:UDSHRD";
	String DB_USER = "EHR_LW44";
	String DB_PASSWORD= "EHR_LW44";
	
	Connection con = null; // 데이터 베이스와 연결을 위한 객체 
	Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체 
	ResultSet rs = null; // SQL 질의에 의해 생성된 테이블을 저장하는 객체
	
	/*
	 * saas 개발서버 -> 정책상 현재 연결불가
	 * String DB_URL = "jdbc:oracle:thin:@92.168.150.5:1521:pes";
	 * String DB_USER = "EHR_HR44";
	 * String DB_PASSWORD= "EHR_HR44";
	 */

	@Autowired
	WtmInterfaceMapper wtmInterfaceMapper;
	
	@Override
	public void getCodeIfResult(Long tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getCodeIfResult");
        try {
        	String retMsg = "";
        	int resultCnt = 0;
        	
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", "V_IF_WTM_CODE");
        	ifHisMap.put("ifEndDate", lastDataTime);
        	
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	
            // 1. 공통코드 데이터 조회
        	
        	// if 가져올 인사db 그룹코드
        	String[] hrGrpCode = {"H20010", "H20020", "H20030", "H10050", "H10110", "T10003"};
        	//                    직급            직책             직위             직군            급여유형        근태타입
        	// if 저장할 wtmdb 그룹코드
        	String[] wtmGrpCode = {"CLASS_CD", "DUTY_CD", "POS_CD", "JOB_CD", "PAY_TYPE_CD", "TAA_TYPE_CD"};
        	
    		//hrGrpCode[i-1];
    		rs = null;
    		int i = 0;
    		
    		StringBuffer codeSb = new StringBuffer();
    		codeSb.append("SELECT ENTER_CD");
    		codeSb.append("     , GRCODE_CD");
    		codeSb.append("     , CODE");
    		codeSb.append("     , CODE_NM");
    		codeSb.append("     , SYMD");
    		codeSb.append("     , NOTE");
    		codeSb.append("  FROM V_IF_WTM_CODE ");
    		codeSb.append(" WHERE ENTER_CD = 'KABANG' ");
    		codeSb.append("   AND CHKDATE > TO_DATE('" + lastDataTime + "', 'YYYYMMDDHH24MISS')");
    		codeSb.append(" ORDER BY ENTER_CD, GRCODE_CD ");
    		
        	rs = stmt.executeQuery(codeSb.toString());
        	List<Map<String, Object>> ifList = new ArrayList();
        	List<Map<String, Object>> ifUpdateList = new ArrayList();
        	
        	while (rs.next()) { 
        		i = Arrays.asList(hrGrpCode).indexOf(rs.getString("GRCODE_CD"));
        		Map<String, Object> ifMap = new HashMap<>();
        		ifMap.put("tenantId", tenantId);
        		ifMap.put("enterCd", rs.getString("ENTER_CD"));
        		ifMap.put("grpCodeCd", wtmGrpCode[i].toString());
        		ifMap.put("codeCd", rs.getString("CODE"));
        		ifMap.put("codeNm", rs.getString("CODE_NM"));
        		ifMap.put("symd", rs.getString("SYMD"));
        		ifMap.put("eymd", "29991231");
        		ifMap.put("note", rs.getString("NOTE"));
        		
//            		String[] akList = {"TENANT_ID","ENTER_CD","GRP_CODE_CD","CODE_CD"};
//            		Map<String, Object> akMap = new HashMap<>();
//            		ifMap.put("tableName", "WTM_CODE");
//            		ifMap.put("akList", akList);
//            		ifMap.put("TENANT_ID", tenantId);
//            		ifMap.put("ENTER_CD", enterCd);
//            		ifMap.put("GRP_CODE_CD", grpCodeCd);
//            		ifMap.put("CODE_CD", codeCd);
//            		ifMap.put("symd", symd);
//            		ifMap.put("eymd", eymd);
        		
        		// 2. 건별 data 저장
        		try {
        			// DATA KEY기준으로 SELECT 
        			Map<String, Object> result = wtmInterfaceMapper.getWtmCodeId(ifMap);
        			
        			if(result != null) {
        				try {
	            			String codeId = result.get("CODE_ID").toString();
	            			System.out.println(codeId);
	            			if(codeId != null && codeId.equals("")) {
	            				ifMap.put("codeId", codeId);
	            				ifUpdateList.add(ifMap);
	            			}
        				} catch(Exception e){
        					retMsg = e.getMessage();
        		            e.printStackTrace();
        		            // 에러걸리면 그냥 아웃시키기
        		            break;
        		        }
        			} else {
        				ifList.add(ifMap);
        			}
    			} catch (Exception e) {
    				retMsg = e.getMessage();
		            e.printStackTrace();
		            // 에러걸리면 그냥 아웃시키기
		            break;
    			}
        	}
        	if("".equals(retMsg)) {
        		try {
	        		//수정건이 있으면....
	        		if (ifUpdateList.size() > 0) {
	        			System.out.println("update size : " + ifUpdateList.size());
	        			resultCnt += wtmInterfaceMapper.updateWtmCode(ifUpdateList);
	        		}
	        		// 추가건이 있으면
	        		if (ifList.size() > 0) {
	        			System.out.println("insert size : " + ifList.size());
	        			resultCnt += wtmInterfaceMapper.insertWtmCode(ifList);
	        		}
	        		if(resultCnt > 0) {
	        			retMsg = resultCnt + "건 반영완료";
	        		} else {
	        			retMsg = "갱신자료없음";
	        		}
	        		ifHisMap.put("ifStatus", "OK");
	        		
	        		// 이력데이터 수정은 건별로
	        		for(i=0; i< ifList.size(); i++) {
	        			Map<String, Object> ifCodeHisMap = new HashMap<>();
	        			ifCodeHisMap = ifList.get(i);
	        			try {
		    				int resultCnt2 = 0 ;
		    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisEymd(ifCodeHisMap);
		    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisSymd(ifCodeHisMap);
	        			} catch (Exception e) {
	            			// 이력수정의 오류는 어쩌나?
	            			retMsg = e.getMessage();
	    		            e.printStackTrace();
	        			}
	        		}
        		} catch (Exception e) {
        			ifHisMap.put("ifStatus", "ERR");
        			retMsg = e.getMessage();
		            e.printStackTrace();
    			}
        	} else {
        		ifHisMap.put("ifStatus", "ERR");
        	}
        	
        	// 3. 처리결과 저장
    		try {
    			// WTM_IF_HIS 테이블에 결과저장
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        	rs.close();
        	stmt.close();
        	con.close();  
        }
		return;
	}
	
	@Override
	public void getHolidayIfResult(Long tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		
		String retMsg = "";
        try {
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", "V_FTM_HOLIDAY");
        	ifHisMap.put("ifEndDate", lastDataTime);
        	
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	
            // 1. 공휴일 데이터 조회
    		rs = null;
    		StringBuffer codeSb = new StringBuffer();
    		codeSb.append("SELECT ENTER_CD");
    		codeSb.append("     , LOCATION_CD");
    		codeSb.append("     , YY||MM||DD AS YMD");
    		codeSb.append("     , HOLIDAY_NM");
    		codeSb.append("     , FESTIVE_YN");
    		codeSb.append("     , PAY_YN");
    		codeSb.append("  FROM V_FTM_HOLIDAY ");
    		codeSb.append(" WHERE ENTER_CD = 'KABANG' ");
    		codeSb.append("   AND CHKDATE > TO_DATE('" + lastDataTime + "', 'YYYYMMDDHH24MISS')");
    		
        	rs = stmt.executeQuery(codeSb.toString());
        	List<Map<String, Object>> ifList = new ArrayList();
        	
        	while (rs.next()) { 
        		Map<String, Object> ifMap = new HashMap<>();
        		ifMap.put("tenantId", tenantId);
        		ifMap.put("enterCd", rs.getString("ENTER_CD"));
        		ifMap.put("locationCd", rs.getString("LOCATION_CD"));
        		ifMap.put("holidayYmd", rs.getString("YMD"));
        		ifMap.put("holidayNm", rs.getString("HOLIDAY_NM"));
        		ifMap.put("sunYn", "Y");
        		ifMap.put("festiveYn", rs.getString("FESTIVE_YN"));
        		ifMap.put("payYn", rs.getString("PAY_YN"));
        		ifList.add(ifMap);
        	}
        	if (ifList.size() > 0) {
	        	// 2. data 저장
	    		try {
	    			// key값을 기준으로 mergeinto하자
					int resultCnt = wtmInterfaceMapper.insertWtmHoliday(ifList);
					retMsg = resultCnt + "건 반영완료";
					ifHisMap.put("ifStatus", "OK");
				} catch (Exception e) {
					ifHisMap.put("ifStatus", "ERR");
					retMsg = e.getMessage();
					e.printStackTrace();
				}
        	} else {
        		retMsg = "갱신자료없음";
				ifHisMap.put("ifStatus", "OK");
        	}
    		// 3. 처리결과 저장
    		try {
    			// key값을 기준으로 mergeinto하자
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        	rs.close();
        	stmt.close();
        	con.close();
        }
		return;
	}
	
	@Override
	public void getTaaCodeIfResult(Long tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getTaaCodeIfResult");
        try {
        	String retMsg = "";
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", "TTIM014");
        	ifHisMap.put("ifEndDate", lastDataTime);
        	
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	
            // 1. 공휴일 데이터 조회
    		rs = null;
    		StringBuffer codeSb = new StringBuffer();
    		codeSb.append("SELECT ENTER_CD");
    		codeSb.append("     , GNT_CD");
    		codeSb.append("     , GNT_NM");
    		codeSb.append("     , GNT_GUBUN_CD");
    		codeSb.append("     , HOL_INCL_YN");
    		codeSb.append("     , REQUEST_USE_TYPE");
    		codeSb.append("     , WORK_YN");
    		codeSb.append("  FROM TTIM014 ");
    		codeSb.append(" WHERE ENTER_CD = 'KABANG' ");
    		codeSb.append("   AND CHKDATE > TO_DATE('" + lastDataTime + "', 'YYYYMMDDHH24MISS')");
        	rs = stmt.executeQuery(codeSb.toString());
        	
        	List<Map<String, Object>> ifList = new ArrayList();
        	List<Map<String, Object>> ifUpdateList = new ArrayList();
        	int resultCnt = 0;
        	while (rs.next()) { 
        		
        		Map<String, Object> ifMap = new HashMap<>();
        		ifMap.put("tenantId", tenantId);
        		ifMap.put("enterCd", rs.getString("ENTER_CD"));
        		ifMap.put("taaCd", rs.getString("GNT_CD"));
        		ifMap.put("taaNm", rs.getString("GNT_NM"));
        		ifMap.put("taaTypeCd", rs.getString("GNT_GUBUN_CD"));
        		ifMap.put("holInclYn", rs.getString("HOL_INCL_YN"));
        		ifMap.put("requestTypeCd", rs.getString("REQUEST_USE_TYPE"));
        		ifMap.put("workYn", rs.getString("WORK_YN"));
        		
        		// 2. 건별 data 저장
        		try {
        			// 중복여부 확인해서 insert/update 구분해야함.
        			// DATA KEY기준으로 SELECT 
        			Map<String, Object> result = wtmInterfaceMapper.getWtmTaaCodeId(ifMap);
        			if(result != null) {
        				try {
        					String taaCodeId = result.get("TAA_CODE_ID").toString();
        					if(taaCodeId != null && taaCodeId.equals("")) {
	            				ifMap.put("taaCodeId", taaCodeId);
	            				ifUpdateList.add(ifMap);
	            			}
        				} catch(Exception e){
        					retMsg = e.getMessage();
        		            e.printStackTrace();
        		            // 에러걸리면 그냥 아웃시키기
        		            break;
        		        }
        			} else {
        				ifList.add(ifMap);
        			}
    			} catch (Exception e) {
    				retMsg = e.getMessage();
    				e.printStackTrace();
    				// 에러걸리면 그냥 아웃시키기
		            break;
    			}
        	}
        	
        	if("".equals(retMsg)) {
        		try {
	        		//수정건이 있으면....
	        		if (ifUpdateList.size() > 0) {
	        			System.out.println("update size : " + ifUpdateList.size());
	        			resultCnt += wtmInterfaceMapper.updateTaaCode(ifUpdateList);
	        		}
	        		// 추가건이 있으면
	        		if (ifList.size() > 0) {
	        			System.out.println("insert size : " + ifList.size());
	        			resultCnt += wtmInterfaceMapper.insertTaaCode(ifList);
	        		}
	        		if(resultCnt > 0) {
	        			retMsg = resultCnt + "건 반영완료";
	        		} else {
	        			retMsg = "갱신자료없음";
	        		}
	        		ifHisMap.put("ifStatus", "OK");
        		} catch (Exception e) {
        			ifHisMap.put("ifStatus", "ERR");
        			retMsg = e.getMessage();
		            e.printStackTrace();
    			}
        	} else {
        		ifHisMap.put("ifStatus", "ERR");
        	}
        	
        	// 3. 처리결과 저장
    		try {
    			// WTM_IF_HIS 테이블에 결과저장
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        	rs.close();
        	stmt.close();
        	con.close();    
        }
        System.out.println("WtmInterfaceServiceImpl getTaaCodeIfResult end");
		return;
	}
	
	@Override
	public void getOrgCodeIfResult(Long tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgCodeIfResult");
        try {
        	String retMsg = "";
        	int resultCnt = 0;
        	
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", "V_IF_ORG_CODE");
        	ifHisMap.put("ifEndDate", lastDataTime);
        	
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	
            // 1. 조직코드 데이터 조회
    		rs = null;
    		int i = 0;
    		
    		StringBuffer codeSb = new StringBuffer();
    		codeSb.append("SELECT ENTER_CD");
    		codeSb.append("     , ORG_CD");
    		codeSb.append("     , ORG_NM");
    		codeSb.append("     , SDATE");
    		codeSb.append("     , EDATE");
    		codeSb.append("     , MEMO");
    		codeSb.append("  FROM V_IF_ORG_CODE ");
    		codeSb.append(" WHERE ENTER_CD = 'KABANG' ");
    		codeSb.append("   AND CHKDATE > TO_DATE('" + lastDataTime + "', 'YYYYMMDDHH24MISS')");
    		codeSb.append(" ORDER BY ENTER_CD, CHKDATE ");
    		
        	rs = stmt.executeQuery(codeSb.toString());
        	List<Map<String, Object>> ifList = new ArrayList();
        	List<Map<String, Object>> ifUpdateList = new ArrayList();
        	
        	while (rs.next()) {
        		Map<String, Object> ifMap = new HashMap<>();
        		ifMap.put("tenantId", tenantId);
        		ifMap.put("enterCd", rs.getString("ENTER_CD"));
        		ifMap.put("grpCodeCd", "ORG_CD");
        		ifMap.put("codeCd", rs.getString("ORG_CD"));
        		ifMap.put("codeNm", rs.getString("ORG_NM"));
        		ifMap.put("symd", rs.getString("SDATE"));
        		ifMap.put("eymd", rs.getString("EDATE"));
        		ifMap.put("note", rs.getString("MEMO"));
        		
        		// 2. 건별 data 저장용 만들기
        		try {
        			// DATA KEY기준으로 SELECT 
        			Map<String, Object> result = wtmInterfaceMapper.getWtmCodeId(ifMap);
        			
        			if(result != null) {
        				try {
	            			String codeId = result.get("CODE_ID").toString();
	            			System.out.println(codeId);
	            			if(codeId != null && codeId.equals("")) {
	            				ifMap.put("codeId", codeId);
	            				ifUpdateList.add(ifMap);
	            			}
        				} catch(Exception e){
        					retMsg = e.getMessage();
        		            e.printStackTrace();
        		            // 에러걸리면 그냥 아웃시키기
        		            break;
        		        }
        			} else {
        				ifList.add(ifMap);
        			}
    			} catch (Exception e) {
    				retMsg = e.getMessage();
		            e.printStackTrace();
		            // 에러걸리면 그냥 아웃시키기
		            break;
    			}
        	}
        	if("".equals(retMsg)) {
        		try {
	        		//수정건이 있으면....
	        		if (ifUpdateList.size() > 0) {
	        			System.out.println("update size : " + ifUpdateList.size());
	        			resultCnt += wtmInterfaceMapper.updateWtmCode(ifUpdateList);
	        		}
	        		// 추가건이 있으면
	        		if (ifList.size() > 0) {
	        			System.out.println("insert size : " + ifList.size());
	        			resultCnt += wtmInterfaceMapper.insertWtmCode(ifList);
	        		}
	        		if(resultCnt > 0) {
	        			retMsg = resultCnt + "건 반영완료";
	        		} else {
	        			retMsg = "갱신자료없음";
	        		}
	        		ifHisMap.put("ifStatus", "OK");
	        		
	        		// 이력데이터 수정은 건별로
	        		for(i=0; i< ifList.size(); i++) {
	        			Map<String, Object> ifCodeHisMap = new HashMap<>();
	        			ifCodeHisMap = ifList.get(i);
	        			try {
		    				int resultCnt2 = 0 ;
		    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisEymd(ifCodeHisMap);
		    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisSymd(ifCodeHisMap);
	        			} catch (Exception e) {
	            			// 이력수정의 오류는 어쩌나?
	            			retMsg = e.getMessage();
	    		            e.printStackTrace();
	        			}
	        		}
        		} catch (Exception e) {
        			ifHisMap.put("ifStatus", "ERR");
        			retMsg = e.getMessage();
		            e.printStackTrace();
    			}
        	} else {
        		ifHisMap.put("ifStatus", "ERR");
        	}
        	
        	// 3. 처리결과 저장
    		try {
    			// WTM_IF_HIS 테이블에 결과저장
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        	rs.close();
        	stmt.close();
        	con.close();  
        }
		return;
	}
	
	@Override
	public void getOrgMapCodeIfResult(Long tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgMapCodeIfResult");
        try {
        	String retMsg = "";
        	int resultCnt = 0;
        	
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", "V_IF_ORG_MAP_CODE");
        	ifHisMap.put("ifEndDate", lastDataTime);
        	
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	
            // 1. 조직코드 데이터 조회
        	// if 가져올 인사db 그룹코드
        	String[] hrGrpCode = {"100", "500"};
        	//                    사업장     근무조
        	// if 저장할 wtmdb 그룹코드
        	String[] wtmGrpCode = {"BUSINESS_PLACE_CD", "WORKTYPE_CD"};
    		rs = null;
    		int i = 0;
    		
    		StringBuffer codeSb = new StringBuffer();
    		codeSb.append("SELECT ENTER_CD");
    		codeSb.append("     , MAP_TYPE_CD");
    		codeSb.append("     , MAP_CD");
    		codeSb.append("     , MAP_NM");
    		codeSb.append("     , SDATE");
    		codeSb.append("     , EDATE");
    		codeSb.append("     , NOTE");
    		codeSb.append("  FROM V_IF_ORG_MAP_CODE ");
    		codeSb.append(" WHERE ENTER_CD = 'KABANG' ");
    		codeSb.append("   AND CHKDATE > TO_DATE('" + lastDataTime + "', 'YYYYMMDDHH24MISS')");
    		codeSb.append(" ORDER BY ENTER_CD, MAP_TYPE_CD, CHKDATE ");
    		
        	rs = stmt.executeQuery(codeSb.toString());
        	List<Map<String, Object>> ifList = new ArrayList();
        	List<Map<String, Object>> ifUpdateList = new ArrayList();
        	
        	while (rs.next()) {
        		i = Arrays.asList(hrGrpCode).indexOf(rs.getString("MAP_TYPE_CD"));
        		Map<String, Object> ifMap = new HashMap<>();
        		
        		ifMap.put("tenantId", tenantId);
        		ifMap.put("enterCd", rs.getString("ENTER_CD"));
        		ifMap.put("grpCodeCd", wtmGrpCode[i].toString());
        		ifMap.put("codeCd", rs.getString("MAP_CD"));
        		ifMap.put("codeNm", rs.getString("MAP_NM"));
        		ifMap.put("symd", rs.getString("SDATE"));
        		ifMap.put("eymd", rs.getString("EDATE"));
        		ifMap.put("note", rs.getString("NOTE"));
        		
        		// 2. 건별 data 저장용 만들기
        		try {
        			// DATA KEY기준으로 SELECT 
        			Map<String, Object> result = wtmInterfaceMapper.getWtmCodeId(ifMap);
        			
        			if(result != null) {
        				try {
	            			String codeId = result.get("CODE_ID").toString();
	            			System.out.println(codeId);
	            			if(codeId != null && codeId.equals("")) {
	            				ifMap.put("codeId", codeId);
	            				ifUpdateList.add(ifMap);
	            			}
        				} catch(Exception e){
        					retMsg = e.getMessage();
        		            e.printStackTrace();
        		            // 에러걸리면 그냥 아웃시키기
        		            break;
        		        }
        			} else {
        				ifList.add(ifMap);
        			}
    			} catch (Exception e) {
    				retMsg = e.getMessage();
		            e.printStackTrace();
		            // 에러걸리면 그냥 아웃시키기
		            break;
    			}
        	}
        	if("".equals(retMsg)) {
        		try {
	        		//수정건이 있으면....
	        		if (ifUpdateList.size() > 0) {
	        			System.out.println("update size : " + ifUpdateList.size());
	        			resultCnt += wtmInterfaceMapper.updateWtmCode(ifUpdateList);
	        		}
	        		// 추가건이 있으면
	        		if (ifList.size() > 0) {
	        			System.out.println("insert size : " + ifList.size());
	        			resultCnt += wtmInterfaceMapper.insertWtmCode(ifList);
	        		}
	        		if(resultCnt > 0) {
	        			retMsg = resultCnt + "건 반영완료";
	        		} else {
	        			retMsg = "갱신자료없음";
	        		}
	        		ifHisMap.put("ifStatus", "OK");
	        		
	        		// 이력데이터 수정은 건별로
	        		for(i=0; i< ifList.size(); i++) {
	        			Map<String, Object> ifCodeHisMap = new HashMap<>();
	        			ifCodeHisMap = ifList.get(i);
	        			try {
		    				int resultCnt2 = 0 ;
		    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisEymd(ifCodeHisMap);
		    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisSymd(ifCodeHisMap);
	        			} catch (Exception e) {
	            			// 이력수정의 오류는 어쩌나?
	            			retMsg = e.getMessage();
	    		            e.printStackTrace();
	        			}
	        		}
        		} catch (Exception e) {
        			ifHisMap.put("ifStatus", "ERR");
        			retMsg = e.getMessage();
		            e.printStackTrace();
    			}
        	} else {
        		ifHisMap.put("ifStatus", "ERR");
        	}
        	
        	// 3. 처리결과 저장
    		try {
    			// WTM_IF_HIS 테이블에 결과저장
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        	rs.close();
        	stmt.close();
        	con.close();  
        }
		return;
	}
	
	@Override
	public void getEmpHisIfResult(Long tenantId, String lastDataTime) throws Exception {
		// TODO Auto-generated method stub
		String rtn = "";
        try {
        	String retMsg = "";
        	int resultCnt = 0;
        	
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", "V_IF_WTM_EMPHIS");
        	ifHisMap.put("ifEndDate", lastDataTime);
        	
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	rs = null;
            // 1. 인사 데이터 조회하기
        	StringBuffer empSb = new StringBuffer();
        	empSb.append("SELECT ENTER_CD");
        	empSb.append("     , SABUN");
        	empSb.append("     , EMP_NM");
        	empSb.append("     , EMP_ENG_NM");
        	empSb.append("     , SYMD");
        	empSb.append("     , STATUS_CD");
        	empSb.append("     , ORG_CD");
        	empSb.append("     , BUSINESS_PLACE_CD");
        	empSb.append("     , DUTY_CD");
        	empSb.append("     , POS_CD");
        	empSb.append("     , CLASS_CD");
        	empSb.append("     , JOB_GROUP_CD");
        	empSb.append("     , JOB_CD");
        	empSb.append("     , PAY_TYPE_CD");
        	empSb.append("     , ORG_PATH");
        	empSb.append("     , LEADER_YN");
        	empSb.append("  FROM V_IF_WTM_EMPHIS ");
        	empSb.append(" WHERE ENTER_CD = 'KABANG' ");
        	empSb.append("   AND CHKDATE > TO_DATE('" + lastDataTime + "', 'YYYYMMDDHH24MISS')");
        	empSb.append(" ORDER BY ENTER_CD, CHKDATE ");
        	
        	rs = stmt.executeQuery(empSb.toString());
        	
        	List<Map<String, Object>> ifList = new ArrayList();
        	while (rs.next()) { 
        		// 사원이력을 임시테이블로 이관 후 프로시저에서 이력을 정리한다.
        		Map<String, Object> ifMap = new HashMap<>();
        		ifMap.put("tenantId", tenantId);
        		ifMap.put("enterCd", rs.getString("ENTER_CD"));
        		ifMap.put("sabun", rs.getString("SABUN"));
        		ifMap.put("empNm", rs.getString("EMP_NM"));
        		ifMap.put("empEngNm", rs.getString("EMP_ENG_NM"));
        		ifMap.put("symd", rs.getString("SYMD"));
        		ifMap.put("eymd", "29991231");
        		ifMap.put("statusCd", rs.getString("STATUS_CD"));
        		ifMap.put("orgCd", rs.getString("ORG_CD"));
        		ifMap.put("businessPlaceCd", rs.getString("BUSINESS_PLACE_CD"));
        		ifMap.put("dutyCd", rs.getString("DUTY_CD"));
        		ifMap.put("posCd", rs.getString("POS_CD"));
        		ifMap.put("classCd", rs.getString("CLASS_CD"));
        		ifMap.put("jobGroupCd", rs.getString("JOB_GROUP_CD"));
        		ifMap.put("jobCd", rs.getString("JOB_CD"));
        		ifMap.put("payTypeCd", rs.getString("PAY_TYPE_CD"));
        		ifMap.put("orgPath", rs.getString("ORG_PATH"));
        		ifMap.put("leaderYn", rs.getString("LEADER_YN"));
        		ifList.add(ifMap);
        	}
        	
        	try {
        	// 추가건이 있으면
    		if (ifList.size() > 0) {
    			System.out.println("insert size : " + ifList.size());
    			resultCnt += wtmInterfaceMapper.insertEmpHisTemp(ifList);
    			if(resultCnt > 0) {
        			retMsg = resultCnt + "건 반영완료";
        		} else {
        			retMsg = "갱신자료없음";
        		}
        		ifHisMap.put("ifStatus", "OK");
    		}
        	}catch(Exception e) {
        		ifHisMap.put("ifStatus", "ERR");
    			retMsg = e.getMessage();
	            e.printStackTrace();
        	}
        	
        	// 이력정리용 프로시저 호출하기
        	// wtmInterfaceMapper.setEmpHis(ifMap);
        	
        	// 3. 처리결과 저장
    		try {
    			// WTM_IF_HIS 테이블에 결과저장
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }catch(Exception e){
            e.printStackTrace();
        }
		return;
	}

}
