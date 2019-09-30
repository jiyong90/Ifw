package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmInterfaceMapper;

@Service
public class WtmInterfaceServiceImpl implements WtmInterfaceService {
	
	// 일단 63번서버 KABNAG에 연결하기
	String IF_URL = "http://smarthrd.servicezone.co.kr:8282/IfWorkInterface"; 
	
	@Autowired
	WtmInterfaceMapper wtmInterfaceMapper;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
		
	@Override
	public Map<String, Object> getIfLastDate(Long tenantId, String ifType) throws Exception {
		// TODO Auto-generated method stub
		String lastDataTime = null;
		String nowDataTime = null;
		Map<String, Object> retMap = new HashMap<>();
		// 2. 건별 data 저장
		try {
			// DATA KEY기준으로 SELECT 
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("tenantId", tenantId);
			paramMap.put("ifType", ifType);
			Map<String, Object> result = wtmInterfaceMapper.getIfLastDate(paramMap);
			
			if(result != null && result.size() > 0) {
				try {
        			lastDataTime = result.get("LAST_DATE").toString();
				} catch(Exception e){
		            e.printStackTrace();
		        }
			} else {
				// 이관이력이 없으면 그냥 과거부터 쭉쭉 옮기자
				lastDataTime = "19000101000000";
			}
			
			result = wtmInterfaceMapper.getIfNowDate(paramMap);
			if(result != null && result.size() > 0) {
				try {
					nowDataTime = result.get("IF_DATE").toString();
				} catch(Exception e){
		            e.printStackTrace();
		        }
			} else {
				nowDataTime = "19000101000000";
			}
			
			retMap.put("lastDate", lastDataTime);
			retMap.put("nowDate", nowDataTime);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return retMap;
	}
	
	@Override
	public HashMap getIfRt(String url) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> getIfMap = null;
		try {
        	RestTemplate restTemplate = new RestTemplate();
	   		getIfMap = restTemplate.getForObject(url, HashMap.class);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return getIfMap;
	}
	
	@Override
	public void getCodeIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getCodeIfResult");
        try {
        	// 인터페이스 결과 저장용
        	String retMsg = null;
        	int resultCnt = 0;
        	String ifType = "V_IF_WTM_CODE";
        	Map<String, Object> ifHisMap = new HashMap<>();
        	ifHisMap.put("tenantId", tenantId);
        	ifHisMap.put("ifItem", ifType);
        	
        	// 인터페이스용 변수
        	String lastDataTime = null;
        	String nowDataTime = null;
        	HashMap<String, Object> getDateMap = null;
        	HashMap<String, Object> getIfMap = null;
        	List<Map<String, Object>> getIfList = null;
        	
        	// 최종 자료 if 시간 조회
        	try {
        		getDateMap = (HashMap<String, Object>) getIfLastDate(tenantId, ifType);
        		lastDataTime = getDateMap.get("lastDate").toString();
        		nowDataTime = getDateMap.get("nowDate").toString();
	        	try {
		        	String ifUrl = IF_URL + "/code" + "?lastDataTime="+lastDataTime;
			   		getIfMap = getIfRt(ifUrl);
			   		
			   		if (getIfMap != null && getIfMap.size() > 0) {
			   			String ifMsg = getIfMap.get("message").toString();
			   			getIfList = (List<Map<String, Object>>) getIfMap.get("ifData");
			   		} else {
			   			retMsg = "Code get : If 데이터 없음";
			   		}
	        	} catch(Exception e) {
	        		retMsg = "Code get : 서버통신 오류";
	        	}
        	} catch(Exception e) {
        		retMsg = "Code get : 최종갱신일 조회오류";
        	}
        	
        	// 조회된 자료가 있으면...
   			if(retMsg == null && getIfList != null && getIfList.size() > 0) {
   	        	
   	        	String[] hrGrpCode = {"H20010", "H20020", "H20030", "H10050", "H10110", "T10003"};
   	        	String[] wtmGrpCode = {"CLASS_CD", "DUTY_CD", "POS_CD", "JOB_CD", "PAY_TYPE_CD", "TAA_TYPE_CD"};
   	        	List<Map<String, Object>> ifList = new ArrayList();
   	        	List<Map<String, Object>> ifUpdateList = new ArrayList();
   	        	for(int i=0; i<getIfList.size(); i++) {
   	        		Map<String, Object> ifMap = new HashMap<>();
   	        		int j = Arrays.asList(hrGrpCode).indexOf(getIfList.get(i).get("GRCODE_CD"));
   	        		
   	        		ifMap.put("tenantId", tenantId);
   	        		ifMap.put("enterCd", getIfList.get(i).get("ENTER_CD"));
   	        		ifMap.put("grpCodeCd", wtmGrpCode[j].toString());
   	        		ifMap.put("codeCd", getIfList.get(i).get("CODE"));
   	        		ifMap.put("codeNm", getIfList.get(i).get("CODE_NM"));
   	        		ifMap.put("symd", getIfList.get(i).get("SYMD"));
   	        		ifMap.put("eymd", "29991231");
   	        		ifMap.put("note", getIfList.get(i).get("NOTE"));
   	        		try {
   	        			// DATA KEY기준으로 SELECT 
   	        			Map<String, Object> result = wtmInterfaceMapper.getWtmCodeId(ifMap);
   	        			
   	        			if(result != null) {
   	        				try {
   		            			String codeId = result.get("CODE_ID").toString();
   		            			//System.out.println(codeId);
   		            			if(codeId != null && codeId.equals("")) {
   		            				ifMap.put("codeId", codeId);
   		            				ifUpdateList.add(ifMap);
   		            			}
   	        				} catch(Exception e){
   	        					retMsg = "code set : code id 조회시 오류";
   	        		            e.printStackTrace();
   	        		            // 에러걸리면 그냥 아웃시키기
   	        		            break;
   	        		        }
   	        			} else {
   	        				ifList.add(ifMap);
   	        			}
   	    			} catch (Exception e) {
   	    				retMsg = "code set : code 데이터 이관시 오류";
   			            e.printStackTrace();
   			            // 에러걸리면 그냥 아웃시키기
   			            break;
   	    			}
   	        	}
   	        	
   	        	if(retMsg == null || "".equals(retMsg) ) {
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
   		        		for(int i=0; i< ifList.size(); i++) {
   		        			Map<String, Object> ifCodeHisMap = new HashMap<>();
   		        			ifCodeHisMap = ifList.get(i);
   		        			try {
   			    				int resultCnt2 = 0 ;
   			    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisEymd(ifCodeHisMap);
   			    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisSymd(ifCodeHisMap);
   		        			} catch (Exception e) {
   		            			// 이력수정의 오류는 어쩌나?
   		        				retMsg = "code set : 이력수정 중 오류";
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
   			} else {
   				ifHisMap.put("ifStatus", "ERR");
   			}
   			// 3. 처리결과 저장
    		try {
    			ifHisMap.put("ifEndDate", lastDataTime);
    			ifHisMap.put("updateDate", nowDataTime);
    			// WTM_IF_HIS 테이블에 결과저장
    			ifHisMap.put("ifMsg", retMsg);
				wtmInterfaceMapper.insertIfHis(ifHisMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
        } catch(Exception e){
            e.printStackTrace();
        }
	}
	
	
	@Override
	public void getHolidayIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		
		// 인터페이스 결과 저장용
    	String retMsg = null;
    	int resultCnt = 0;
    	String ifType = "V_FTM_HOLIDAY";
    	Map<String, Object> ifHisMap = new HashMap<>();
    	ifHisMap.put("tenantId", tenantId);
    	ifHisMap.put("ifItem", ifType);
    	
    	// 인터페이스용 변수
    	String lastDataTime = null;
    	String nowDataTime = null;
    	HashMap<String, Object> getDateMap = null;
    	HashMap<String, Object> getIfMap = null;
    	List<Map<String, Object>> getIfList = null;
    	
    	// 최종 자료 if 시간 조회
    	try {
    		getDateMap = (HashMap<String, Object>) getIfLastDate(tenantId, ifType);
    		lastDataTime = getDateMap.get("lastDate").toString();
    		nowDataTime = getDateMap.get("nowDate").toString();
        	try {
	        	String ifUrl = IF_URL + "/holiday" + "?lastDataTime="+lastDataTime;
	        	getIfMap = getIfRt(ifUrl);
		   		
		   		if (getIfMap != null && getIfMap.size() > 0) {
		   			String ifMsg = getIfMap.get("message").toString();
		   			getIfList = (List<Map<String, Object>>) getIfMap.get("ifData");
		   		} else {
		   			retMsg = "Holiday get : If 데이터 없음";
		   		}
        	} catch(Exception e) {
        		retMsg = "Holiday get : If 서버통신 오류";
        	}
    	} catch(Exception e) {
    		retMsg = "Holiday get : 최종갱신일 조회오류";
    	}
    	// 조회된 자료가 있으면...
		if(retMsg == null && getIfList != null && getIfList.size() > 0) {
	        try {
	        	List<Map<String, Object>> ifList = new ArrayList();
   	        	List<Map<String, Object>> ifUpdateList = new ArrayList();
   	        	for(int i=0; i<getIfList.size(); i++) {
	        		Map<String, Object> ifMap = new HashMap<>();
	        		ifMap.put("tenantId", tenantId);
	        		ifMap.put("enterCd", getIfList.get(i).get("ENTER_CD"));
	        		ifMap.put("locationCd", getIfList.get(i).get("LOCATION_CD"));
	        		ifMap.put("holidayYmd", getIfList.get(i).get("YMD"));
	        		ifMap.put("holidayNm", getIfList.get(i).get("HOLIDAY_NM"));
	        		ifMap.put("sunYn", "Y");
	        		ifMap.put("festiveYn", getIfList.get(i).get("FESTIVE_YN"));
	        		ifMap.put("payYn", getIfList.get(i).get("PAY_YN"));
	        		ifList.add(ifMap);
	        	}
	        	if (ifList.size() > 0) {
		        	// 2. data 저장
		    		try {
		    			// key값을 기준으로 mergeinto하자
						resultCnt = wtmInterfaceMapper.insertWtmHoliday(ifList);
						retMsg = resultCnt + "건 반영완료";
						ifHisMap.put("ifStatus", "OK");
					} catch (Exception e) {
						ifHisMap.put("ifStatus", "ERR");
						retMsg = "Holiday set : 데이터 저장 오류";
						e.printStackTrace();
					}
	        	} else {
	        		retMsg = "갱신자료없음";
					ifHisMap.put("ifStatus", "OK");
	        	}
	        } catch(Exception e){
	            e.printStackTrace();
	        }
		} else {
			ifHisMap.put("ifStatus", "ERR");
		}
        // 3. 처리결과 저장
		try {
			// 최종갱신된 일시조회
			ifHisMap.put("updateDate", nowDataTime);
			ifHisMap.put("ifEndDate", lastDataTime);
			// WTM_IF_HIS 테이블에 결과저장
			ifHisMap.put("ifMsg", retMsg);
			wtmInterfaceMapper.insertIfHis(ifHisMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	@Override
	public void getTaaCodeIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getTaaCodeIfResult");
		// 인터페이스 결과 저장용
    	String retMsg = null;
    	int resultCnt = 0;
    	String ifType = "V_IF_TAA_CODE";
    	Map<String, Object> ifHisMap = new HashMap<>();
    	ifHisMap.put("tenantId", tenantId);
    	ifHisMap.put("ifItem", ifType);
    	
    	// 인터페이스용 변수
    	String lastDataTime = null;
    	String nowDataTime = null;
    	HashMap<String, Object> getDateMap = null;
    	HashMap<String, Object> getIfMap = null;
    	List<Map<String, Object>> getIfList = null;
    	
    	// 최종 자료 if 시간 조회
    	try {
    		getDateMap = (HashMap<String, Object>) getIfLastDate(tenantId, ifType);
    		lastDataTime = getDateMap.get("lastDate").toString();
    		nowDataTime = getDateMap.get("nowDate").toString();
        	try {
	        	String ifUrl = IF_URL + "/gntCode" + "?lastDataTime="+lastDataTime;
	        	getIfMap = getIfRt(ifUrl);
		   		
		   		if (getIfMap != null && getIfMap.size() > 0) {
		   			String ifMsg = getIfMap.get("message").toString();
		   			getIfList = (List<Map<String, Object>>) getIfMap.get("ifData");
		   		} else {
		   			retMsg = "TaaCode get : If 데이터 없음";
		   		}
        	} catch(Exception e) {
        		retMsg = "TaaCode get : 서버통신 오류";
        	}
    	} catch(Exception e) {
    		retMsg = "TaaCode get : 최종갱신일 조회오류";
    	}
    	// 조회된 자료가 있으면...
		if(retMsg == null && getIfList != null && getIfList.size() > 0) {
			try {
	        	List<Map<String, Object>> ifList = new ArrayList();
   	        	List<Map<String, Object>> ifUpdateList = new ArrayList();
   	        	for(int i=0; i<getIfList.size(); i++) {
	        		Map<String, Object> ifMap = new HashMap<>();
	        		ifMap.put("tenantId", tenantId);
	        		ifMap.put("enterCd", getIfList.get(i).get("ENTER_CD"));
	        		ifMap.put("taaCd", getIfList.get(i).get("GNT_CD"));
	        		ifMap.put("taaNm", getIfList.get(i).get("GNT_NM"));
	        		ifMap.put("taaTypeCd", getIfList.get(i).get("GNT_GUBUN_CD"));
	        		ifMap.put("holInclYn", getIfList.get(i).get("HOL_INCL_YN"));
	        		ifMap.put("requestTypeCd", getIfList.get(i).get("REQUEST_USE_TYPE"));
	        		ifMap.put("workYn", getIfList.get(i).get("WORK_YN"));
        		
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
	        					retMsg = "TaaCode set : taa_code_id 조회오류";
	        		            e.printStackTrace();
	        		            // 에러걸리면 그냥 아웃시키기
	        		            break;
	        		        }
	        			} else {
	        				ifList.add(ifMap);
	        			}
	    			} catch (Exception e) {
	    				retMsg = "TaaCode set : taa_code 검증 오류";
	    				e.printStackTrace();
	    				// 에러걸리면 그냥 아웃시키기
			            break;
	    			}
   	        	}
        	
	        	if(retMsg == null || "".equals(retMsg) ) {
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
	        			retMsg = "TaaCode set : taa_code 데이터 저장 오류";
			            e.printStackTrace();
	    			}
	        	} else {
	        		ifHisMap.put("ifStatus", "ERR");
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
			System.out.println("nowDataTime : " + nowDataTime );
			ifHisMap.put("updateDate", nowDataTime);
   			ifHisMap.put("ifEndDate", lastDataTime);
			ifHisMap.put("ifMsg", retMsg);
			wtmInterfaceMapper.insertIfHis(ifHisMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("WtmInterfaceServiceImpl getTaaCodeIfResult end");
		return;
	}
	
	@Override
	public void getOrgCodeIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgCodeIfResult");
		// 인터페이스 결과 저장용
    	String retMsg = null;
    	int resultCnt = 0;
    	String ifType = "V_IF_ORG_CODE";
    	Map<String, Object> ifHisMap = new HashMap<>();
    	ifHisMap.put("tenantId", tenantId);
    	ifHisMap.put("ifItem", ifType);
    	
    	// 인터페이스용 변수
    	String lastDataTime = null;
    	String nowDataTime = null;
    	HashMap<String, Object> getDateMap = null;
    	HashMap<String, Object> getIfMap = null;
    	List<Map<String, Object>> getIfList = null;
    	
    	// 최종 자료 if 시간 조회
    	try {
    		getDateMap = (HashMap<String, Object>) getIfLastDate(tenantId, ifType);
    		lastDataTime = getDateMap.get("lastDate").toString();
    		nowDataTime = getDateMap.get("nowDate").toString();
        	try {
	        	String ifUrl = IF_URL + "/orgCode" + "?lastDataTime="+lastDataTime;
	        	getIfMap = getIfRt(ifUrl);
		   		
		   		if (getIfMap != null && getIfMap.size() > 0) {
		   			String ifMsg = getIfMap.get("message").toString();
		   			System.out.println("ifMsg : " + ifMsg);
		   			System.out.println("getIfMap.size() : " + getIfMap.size());
		   			if(!"OK".equals(ifMsg)) {
		   				retMsg = "org get : " + ifMsg;
		   			} else {
		   				getIfList = (List<Map<String, Object>>) getIfMap.get("ifData");
		   				System.out.println("getIfList.size() : " + getIfList.size());
		   			}
		   		} else {
		   			retMsg = "org get : If 데이터 없음";
		   		}
        	} catch(Exception e) {
        		retMsg = "org get : 서버통신 오류";
        	}
    	} catch(Exception e) {
    		retMsg = "org get : 최종갱신일 조회오류";
    	}
    	// 조회된 자료가 있으면...
		if(retMsg == null && getIfList != null && getIfList.size() > 0) {
			System.out.println("data loop ");
	        try {
	        	List<Map<String, Object>> ifList = new ArrayList();
   	        	List<Map<String, Object>> ifUpdateList = new ArrayList();
   	        	for(int i=0; i<getIfList.size(); i++) {
	        		Map<String, Object> ifMap = new HashMap<>();
	        		ifMap.put("tenantId", tenantId);
	        		ifMap.put("enterCd", getIfList.get(i).get("ENTER_CD"));
	        		ifMap.put("grpCodeCd", "ORG_CD");
	        		ifMap.put("codeCd", getIfList.get(i).get("ORG_CD"));
	        		ifMap.put("codeNm", getIfList.get(i).get("ORG_NM"));
	        		ifMap.put("symd", getIfList.get(i).get("SDATE"));
	        		ifMap.put("eymd", getIfList.get(i).get("EDATE"));
	        		ifMap.put("note", getIfList.get(i).get("MEMO"));
	        		
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
	        					retMsg = "org set : code_id 조회오류";
	        		            e.printStackTrace();
	        		            // 에러걸리면 그냥 아웃시키기
	        		            break;
	        		        }
	        			} else {
	        				ifList.add(ifMap);
	        			}
	    			} catch (Exception e) {
	    				retMsg = "org set : org 검증 오류";
			            e.printStackTrace();
			            // 에러걸리면 그냥 아웃시키기
			            break;
	    			}
   	        	}
   	        	if(retMsg == null || "".equals(retMsg) ) {
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
		        		for(int i=0; i< ifList.size(); i++) {
		        			Map<String, Object> ifCodeHisMap = new HashMap<>();
		        			ifCodeHisMap = ifList.get(i);
		        			try {
			    				int resultCnt2 = 0 ;
			    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisEymd(ifCodeHisMap);
			    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisSymd(ifCodeHisMap);
		        			} catch (Exception e) {
		            			// 이력수정의 오류는 어쩌나?
		        				retMsg = "org set : 이력 갱신 오류";
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
	        } catch(Exception e){
	            e.printStackTrace();
	        }
		} else {
			ifHisMap.put("ifStatus", "ERR");
		}
        // 3. 처리결과 저장
		try {
			// 최종갱신된 일시조회
			ifHisMap.put("updateDate", nowDataTime);
   			ifHisMap.put("ifEndDate", lastDataTime);
			// WTM_IF_HIS 테이블에 결과저장
			ifHisMap.put("ifMsg", retMsg);
			wtmInterfaceMapper.insertIfHis(ifHisMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	public void getOrgMapCodeIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("WtmInterfaceServiceImpl getOrgMapCodeIfResult");
		// 인터페이스 결과 저장용
    	String retMsg = null;
    	int resultCnt = 0;
    	String ifType = "V_IF_ORG_MAP_CODE";
    	Map<String, Object> ifHisMap = new HashMap<>();
    	ifHisMap.put("tenantId", tenantId);
    	ifHisMap.put("ifItem", ifType);
    	
    	// 인터페이스용 변수
    	String lastDataTime = null;
    	String nowDataTime = null;
    	HashMap<String, Object> getDateMap = null;
    	HashMap<String, Object> getIfMap = null;
    	List<Map<String, Object>> getIfList = null;
    	
    	// 최종 자료 if 시간 조회
    	try {
    		getDateMap = (HashMap<String, Object>) getIfLastDate(tenantId, ifType);
    		lastDataTime = getDateMap.get("lastDate").toString();
    		nowDataTime = getDateMap.get("nowDate").toString();
    		System.out.println("lastDataTime : " + lastDataTime);
    		System.out.println("nowDataTime : " + nowDataTime);
        	try {
	        	String ifUrl = IF_URL + "/orgMapCode" + "?lastDataTime="+lastDataTime;
	        	getIfMap = getIfRt(ifUrl);
	        	System.out.println("getIfMap.size() : " + getIfMap.size());
		   		if (getIfMap != null && getIfMap.size() > 0) {
		   			String ifMsg = getIfMap.get("message").toString();
		   			getIfList = (List<Map<String, Object>>) getIfMap.get("ifData");
		   			System.out.println("getIfList.size() : " + getIfList.size());
		   		} else {
		   			retMsg = "orgMap get : If 데이터 없음";
		   		}
        	} catch(Exception e) {
        		retMsg = "orgMap get : If 서버통신 오류";
        	}
    	} catch(Exception e) {
    		retMsg = "orgMap get : 최종갱신일 조회오류";
    	}
    	
    	// 조회된 자료가 있으면...
    	if(retMsg == null && getIfList != null && getIfList.size() > 0) {
	        try {
	            // 1. 조직코드 데이터 조회
	        	// if 가져올 인사db 그룹코드
	        	String[] hrGrpCode = {"100", "500"};
	        	//                    사업장     근무조
	        	// if 저장할 wtmdb 그룹코드
	        	String[] wtmGrpCode = {"BUSINESS_PLACE_CD", "WORKTYPE_CD"};
	        	
	        	List<Map<String, Object>> ifList = new ArrayList();
   	        	List<Map<String, Object>> ifUpdateList = new ArrayList();
   	        	
   	        	for(int i=0; i<getIfList.size(); i++) {
	        		int j = Arrays.asList(hrGrpCode).indexOf(getIfList.get(i).get("MAP_TYPE_CD"));
	        		Map<String, Object> ifMap = new HashMap<>();
	        		
	        		ifMap.put("tenantId", tenantId);
	        		ifMap.put("enterCd", getIfList.get(i).get("ENTER_CD"));
	        		ifMap.put("grpCodeCd", wtmGrpCode[j].toString());
	        		ifMap.put("codeCd", getIfList.get(i).get("MAP_CD"));
	        		ifMap.put("codeNm", getIfList.get(i).get("MAP_NM"));
	        		ifMap.put("symd", getIfList.get(i).get("SDATE"));
	        		ifMap.put("eymd", getIfList.get(i).get("EDATE"));
	        		ifMap.put("note", getIfList.get(i).get("NOTE"));
	        		
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
	        					retMsg = "orgMap set : code id 조회오류";
	        		            e.printStackTrace();
	        		            // 에러걸리면 그냥 아웃시키기
	        		            break;
	        		        }
	        			} else {
	        				ifList.add(ifMap);
	        			}
	    			} catch (Exception e) {
	    				retMsg = "orgMap set : codemap 검증 오류";
			            e.printStackTrace();
			            // 에러걸리면 그냥 아웃시키기
			            break;
	    			}
	        	}
   	        	if(retMsg == null || "".equals(retMsg) ) {
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
		        		for(int i=0; i< ifList.size(); i++) {
		        			Map<String, Object> ifCodeHisMap = new HashMap<>();
		        			ifCodeHisMap = ifList.get(i);
		        			try {
			    				int resultCnt2 = 0 ;
			    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisEymd(ifCodeHisMap);
			    				resultCnt2 = wtmInterfaceMapper.updateWtmCodeHisSymd(ifCodeHisMap);
		        			} catch (Exception e) {
		        				retMsg = "orgMap set : code 이력반영 오류";
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
	        } catch(Exception e){
	            e.printStackTrace();
	        }
    	} else {
			ifHisMap.put("ifStatus", "ERR");
		}
    	// 3. 처리결과 저장
		try {
			// 최종갱신된 일시조회
			ifHisMap.put("updateDate", nowDataTime);
   			ifHisMap.put("ifEndDate", lastDataTime);
			// WTM_IF_HIS 테이블에 결과저장
			ifHisMap.put("ifMsg", retMsg);
			wtmInterfaceMapper.insertIfHis(ifHisMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	@Override
	public void getEmpHisIfResult(Long tenantId) throws Exception {
		// TODO Auto-generated method stub
		// 인터페이스 결과 저장용
    	String retMsg = null;
    	int resultCnt = 0;
    	String ifType = "V_IF_WTM_EMPHIS";
    	Map<String, Object> ifHisMap = new HashMap<>();
    	ifHisMap.put("tenantId", tenantId);
    	ifHisMap.put("ifItem", ifType);
    	
    	// 인터페이스용 변수
    	String lastDataTime = null;
    	String nowDataTime = null;
    	HashMap<String, Object> getDateMap = null;
    	HashMap<String, Object> getIfMap = null;
    	List<Map<String, Object>> getIfList = null;
    	
    	// 최종 자료 if 시간 조회
    	try {
    		getDateMap = (HashMap<String, Object>) getIfLastDate(tenantId, ifType);
    		lastDataTime = getDateMap.get("lastDate").toString();
    		nowDataTime = getDateMap.get("nowDate").toString();
        	try {
	        	String ifUrl = IF_URL + "/empHis" + "?lastDataTime="+lastDataTime;
	        	getIfMap = getIfRt(ifUrl);
		   		System.out.println("111111111111111111111" + getIfMap.toString());
		   		
		   		if (getIfMap != null && getIfMap.size() > 0) {
		   			String ifMsg = getIfMap.get("message").toString();
		   			getIfList = (List<Map<String, Object>>) getIfMap.get("ifData");
		   		} else {
		   			retMsg = "emp get : If 데이터 없음";
		   		}
        	} catch(Exception e) {
        		retMsg = "emp get : If 서버통신 오류";
        	}
    	} catch(Exception e) {
    		retMsg = "emp get : 최종갱신일 조회오류";
    	}
    	// 조회된 자료가 있으면...
    	if(retMsg == null && getIfList != null && getIfList.size() > 0) {
	        try {
	        	List<Map<String, Object>> ifList = new ArrayList();
   	        	List<Map<String, Object>> ifUpdateList = new ArrayList();
   	        	for(int i=0; i<getIfList.size(); i++) {
	        		// 사원이력을 임시테이블로 이관 후 프로시저에서 이력을 정리한다.
	        		Map<String, Object> ifMap = new HashMap<>();
	        		ifMap.put("tenantId", tenantId);
	        		ifMap.put("enterCd", getIfList.get(i).get("ENTER_CD"));
	        		ifMap.put("sabun", getIfList.get(i).get("SABUN"));
	        		ifMap.put("empNm", getIfList.get(i).get("EMP_NM"));
	        		ifMap.put("empEngNm", getIfList.get(i).get("EMP_ENG_NM"));
	        		ifMap.put("symd", getIfList.get(i).get("SYMD"));
	        		ifMap.put("eymd", "29991231");
	        		ifMap.put("statusCd", getIfList.get(i).get("STATUS_CD"));
	        		ifMap.put("orgCd", getIfList.get(i).get("ORG_CD"));
	        		ifMap.put("businessPlaceCd", getIfList.get(i).get("BUSINESS_PLACE_CD"));
	        		ifMap.put("dutyCd", getIfList.get(i).get("DUTY_CD"));
	        		ifMap.put("posCd", getIfList.get(i).get("POS_CD"));
	        		ifMap.put("classCd", getIfList.get(i).get("CLASS_CD"));
	        		ifMap.put("jobGroupCd", getIfList.get(i).get("JOB_GROUP_CD"));
	        		ifMap.put("jobCd", getIfList.get(i).get("JOB_CD"));
	        		ifMap.put("payTypeCd", getIfList.get(i).get("PAY_TYPE_CD"));
	        		ifMap.put("orgPath", getIfList.get(i).get("ORG_PATH"));
	        		ifMap.put("leaderYn", getIfList.get(i).get("LEADER_YN"));
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
	    			
	    			// temp 저장후 프로시저 호출
	    			HashMap<String, Object> setSpRetMap = new HashMap<>();
	    			setSpRetMap.put("tenantId", tenantId);
	    			setSpRetMap.put("nowDataTime", nowDataTime);
	    			setSpRetMap.put("retCode", "");
	    			wtmInterfaceMapper.setEmpHis(setSpRetMap);
	    			
	    			String retCode = setSpRetMap.get("retCode").toString();
	    			ifHisMap.put("ifStatus", retCode);
	    			if("ERR".equals(retCode)) {
	    				retMsg = "사원정보 이관갱신중 오류 오류로그 확인";
	    			}
	    		}
	        	}catch(Exception e) {
	        		ifHisMap.put("ifStatus", "ERR");
	        		retMsg = "emp set : temp 자료저장 오류";
		            e.printStackTrace();
	        	}
	        }catch(Exception e){
	            e.printStackTrace();
	        }
    	} else {
			ifHisMap.put("ifStatus", "ERR");
		}
    	// 3. 처리결과 저장
		try {
			// 최종갱신된 일시조회
			ifHisMap.put("updateDate", nowDataTime);
   			ifHisMap.put("ifEndDate", lastDataTime);
			// WTM_IF_HIS 테이블에 결과저장
			ifHisMap.put("ifMsg", retMsg);
			wtmInterfaceMapper.insertIfHis(ifHisMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 4. 기본근무 생성대상자 조회해서 근무를 생성해주자
		try {
			List<Map<String, Object>> getEmpBaseList = null;
			getEmpBaseList = wtmInterfaceMapper.getEmpBaseList(ifHisMap);
			if(getEmpBaseList != null && getEmpBaseList.size() > 0) {
				for(int i=0; i<getEmpBaseList.size(); i++) {
					Map<String, Object> setEmpMap = new HashMap<>();
					setEmpMap = getEmpBaseList.get(i);
					// 입사자만? 이력정리용 프로시저 호출하기
			    	wtmFlexibleEmpMapper.initWtmFlexibleEmpOfWtmWorkDayResult(setEmpMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
	    return;
	}

}
