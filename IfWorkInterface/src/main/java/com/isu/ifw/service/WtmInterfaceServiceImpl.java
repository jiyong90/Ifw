package com.isu.ifw.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
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
	public void getEmpHisIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("tenantId", tenantId);
		// interfaseMapper.getWtmEmpHis(paramMap);
		String rtn = "";
        try {
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
        	empSb.append("FROM V_IF_WTM_EMPHIS ");
        	rs = stmt.executeQuery(empSb.toString());
        	
        	while (rs.next()) { 
        		String enter_cd = rs.getString("enter_cd"); 
        		//System.out.println(enter_cd); 
        		
        		// 2. TENANT_ID, ENTER_CD, SABUN 으로 데이터 유무 체크
            	// 2.1 없으면 WTM_IF_EMP_MSG, WTM_EMP_HIS무조건생성
            	// 2.2 있으면 데이터 비교용 자료조회
            	// 3. 이름, 일자, 재직상태, 조직코드(조직경로 포함), 사업장코드, 직책, 직위, 직급, 직군, 직무, 급여유형, 조직장여부 항목 체크
            	// 3.1 데이터 변경 없으면 PASS
            	// 3.2 데이터 변경이 있으면 WTM_IF_EMP_MSG 변경정보생성
            	// 3.3 데이터 변경이 1개의 항목이라도 있으면 WTM_EMP_HIS 이력 생성 및 시작일, 종료일 정리요함
            	
            	// 사진은 어케하나요??? ㅠㅠ
        	}

    		rtn = "ok";
        }catch(Exception e){
            e.printStackTrace();
        }
		return;
	}
	
	@Override
	public void getCodeIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		
        try {
        	Class.forName(DRIVER);
        	con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	stmt = con.createStatement();
        	
            // 1. 공통코드 데이터 조회
        	
        	/* 공통코드 매핑기준은 어따다 정할까???? 코드별 루프를 돌리는게 좋겠음.
        	 *
        	'H20010', -- 직급 	'H20020', -- 직책	  'H20030', -- 직위	'H10050', -- 직군	   'H10110' -- 급여유형
        	 */
        	
        	// if 가져올 인사db 그룹코드
        	String[] hrGrpCode = {"H10110", "H20020"};
//        	String[] hrGrpCode = {"H20010", "H20020", "H20030", "H10050", "H10110"};
        	// if 저장할 wtmdb 그룹코드
        	String[] wtmGrpCode = {"PAY_TYPE_CD", "DUTY_CD"};
//        	String[] wtmGrpCode = {"CLASS_CD", "DUTY_CD", "POS_CD", "JOB_CD", "PAY_TYPE_CD"};
        	
        	for(int i=1; i<=hrGrpCode.length; i++) {
        		//hrGrpCode[i-1];
        		rs = null;
        		
        		StringBuffer codeSb = new StringBuffer();
        		codeSb.append("SELECT ENTER_CD");
        		codeSb.append("     , GRCODE_CD");
        		codeSb.append("     , CODE");
        		codeSb.append("     , CODE_NM");
        		codeSb.append("     , SYMD");
        		codeSb.append("     , NOTE");
        		codeSb.append("  FROM V_IF_WTM_CODE ");
        		codeSb.append(" WHERE GRCODE_CD = '" + hrGrpCode[i-1].toString() + "'");
            	rs = stmt.executeQuery(codeSb.toString());
            	
            	while (rs.next()) { 
            		String enterCd = rs.getString("ENTER_CD");
            		String grpCodeCd = wtmGrpCode[i-1].toString(); 
            		String codeCd = rs.getString("CODE"); 
            		String codeNm = rs.getString("CODE_NM"); 
            		String symd = rs.getString("SYMD");
            		String note = rs.getString("NOTE");
            		
            		Map<String, Object> codeMap = new HashMap<>();
            		codeMap.put("tenantId", tenantId);
            		codeMap.put("enterCd", enterCd);
            		codeMap.put("grpCodeCd", grpCodeCd);
            		codeMap.put("codeCd", codeCd);
            		codeMap.put("codeNm", codeNm);
            		codeMap.put("symd", symd);
            		codeMap.put("note", note);
            		
            		// 2. 건별 data 저장(MARGE INTO가 없으니깐 ㅠㅠ)
            		try {
            			// DATA KEY기준으로 SELECT 
            			Map<String, Object> result = wtmInterfaceMapper.getWtmCodeId(codeMap);
            			
            			if(result != null) {
            				try {
		            			String codeId = result.get("CODE_ID").toString();
		            			System.out.println("update : " + codeId);
		            			if(codeId != null && codeId.equals("")) {
		            				codeMap.put("codeId", codeId);
		            				int resultCnt = wtmInterfaceMapper.updateWtmCode(codeMap);
		            				System.out.println("update : " + codeId);
		            			}
            				} catch(Exception e){
            		            e.printStackTrace();
            		        }
            			} else {
            				try {
            					// 이력기준 insert 하자
            					int resultCnt = 0;
            					// 이전일자로 저장된건이 있으면....시작일 -1일로 업데이트가 필요함.
            					resultCnt = wtmInterfaceMapper.updateWtmCodeEymd(codeMap);
            					// 이후일자 데이터가 있으면, 시작일을 알아야함.시작일 -1일로 저장해야함.
            					// EYMD = wtmInterfaceMapper.insertWtmCode(codeMap);
            					Map<String, Object> result2 = wtmInterfaceMapper.getWtmCodeEymd(codeMap);
            					if(result2 != null) {
            						String eymd = result2.get("EYMD").toString();
            						codeMap.put("eymd", eymd);
            					} else {
            						codeMap.put("eymd", "29991231");
            					}
            					// 이력갱신했으니깐 INSERT 하자.
	            				resultCnt = wtmInterfaceMapper.insertWtmCode(codeMap);
	            				System.out.println("insert : " + codeCd);
	            				// 그럼 이력정리를 해야하는지 체크하자
	            				
            				} catch(Exception e){
            		            e.printStackTrace();
            		        }
            			}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
            	}
            	rs.close();
        	}
        	stmt.close();
        	con.close();    		
        } catch(Exception e){
            e.printStackTrace();
        }
		return;
	}

}
