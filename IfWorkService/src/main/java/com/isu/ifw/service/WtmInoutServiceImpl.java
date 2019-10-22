package com.isu.ifw.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmCode;
import com.isu.ifw.entity.WtmCodeGrp;
import com.isu.ifw.entity.WtmTimeBreakMgr;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmCalendarMapper;
import com.isu.ifw.mapper.WtmInoutHisMapper;
import com.isu.ifw.repository.WtmCodeGrpRepository;
import com.isu.ifw.repository.WtmCodeRepository;

@Service("inoutService")
public class WtmInoutServiceImpl implements WtmInoutService{
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@Autowired
	WtmInoutHisMapper inoutHisMapper;
	
	@Autowired
	WtmCalendarMapper wtmCalendarMapper;
	
	@Autowired
	WtmFlexibleEmpService empService;
	
	@Override
	public Map<String, Object> getMenuContext(Long tenantId, String enterCd, String sabun) {

		Map <String,Object> paramMap = new HashMap<String, Object>();

		Map <String,Object> menuIn = new HashMap();
		Map <String,Object> menuOut = new HashMap();
		Map <String,Object> menuGoback = new HashMap();
		
		Map <String,Object> returnMap = new HashMap();
		returnMap.put("D01", menuIn);
		returnMap.put("D02", menuOut);
		returnMap.put("D03", menuGoback);
		
		//tenant 어디서 가져올지
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
			
//		String ymd = null; //기준일 
//		String md = null; // 기준일에서 월/일만 뺀 값  
//		String inoutType = "NONE";
//		String label = "근무계획없음";
//		String description = "출근체크 필요시 인사팀에 문의 바랍니다";
		
		try {
			//근무계획으로 출퇴근 활성화
			/*
			List<Map<String, Object>> list = inoutHisMapper.getInoutStatus(paramMap);
			System.out.println("inoutStatus : " + list.toString());
			
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
			Date now = new Date();
			String today = format1.format(now);
			
			for(Map<String, Object> time : list) {
				if(time.get("pSymd").equals(today) && time.get("entrySdate") == null) {
					ymd = time.get("ymd").toString();
					md = time.get("ymd").toString().substring(4, 6) + "/" +time.get("ymd").toString().substring(6, 8);
					inoutType = "IN";
					label =  md +" 출근하기";
					description = "출입 비콘 근처에서 버튼이 활성화됩니다";
				} else if(time.get("pEymd").equals(today) && time.get("entryEdate") == null) {
					ymd = time.get("ymd").toString();
					md = time.get("ymd").toString().substring(4, 6) + "/" +time.get("ymd").toString().substring(6, 8);
					inoutType = "OUT";
					label =  md +" 퇴근하기";
					description = "출입 비콘 근처에서 버튼이 활성화됩니다";
				}
			}*/
			
			String dIn = "-";
			String dOut = "-";
			String dGoback = "-";
			String type = "GO";
			List<Map<String, Object>> list = inoutHisMapper.getGobackStatus(paramMap);
			for(Map<String, Object> data : list) {
				if(data.get("inoutTypeCd").equals("IN")) {
					dIn = data.get("inout_date").toString();
				} else if(data.get("inoutTypeCd").equals("OUT")){
					dOut = data.get("inout_date").toString();
				} else if(data.get("inoutTypeCd").equals("GO") || data.get("inoutTypeCd").equals("BACK")) {
					if(data.get("inoutTypeCd").equals("GO")) {
						type = "BACK";
						dGoback = "외출 " + data.get("inout_date").toString();
					}
					else {
						type = "GO";
						dGoback = "복귀 " + data.get("inout_date").toString();
					} 
				}
			}
			
			menuIn.put("descriptionIn", dIn);
			menuIn.put("inoutType", "IN");
			
			menuOut.put("descriptionIn", dOut);
			menuOut.put("inoutType", "OUT");

			menuGoback.put("descriptionIn", dGoback);
			menuGoback.put("actionType", "ACTIVE");
			menuGoback.put("label", type.equals("GO")?"외출하기":"복귀하기");
			menuGoback.put("backgroundColor", type.equals("GO")?"#93DaFF":"#FFF56E");

		}catch(Exception e) {
			e.printStackTrace();
		} 
		
		return returnMap;
	}

	@Override
	public int checkInoutHis(Long tenantId, String enterCd, String sabun, String inoutType, String ymd) throws Exception {
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String today = format1.format(now);
		int cnt = 0;
		
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("tenantId", "1");
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("inoutTypeCd", inoutType);
		paramMap.put("ymd", ymd);
		paramMap.put("now", today);
		paramMap.put("entryTypeCd", "MO");
		
		cnt = inoutHisMapper.saveWtmInoutHis(paramMap);
		if(cnt <= 0) {
			return cnt;
		}
	
		cnt += updateTimeStamp(paramMap);
		return cnt;
	}
	
	@Override
	public int updateTimeStamp(Map<String, Object> paramMap) {
		int cnt = 0;
		//근무캘린더에 시간만 업데이트
		try {
			cnt =  wtmCalendarMapper.updateEntryDate(paramMap);
			
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		empService.calcApprDayInfo(Long.parseLong(paramMap.get("tenantId").toString()), 
				paramMap.get("enterCd").toString(), paramMap.get("ymd").toString(),
				paramMap.get("ymd").toString(), paramMap.get("sabun").toString());
		
		return cnt;
	}

	@Override
	public List<Map<String, Object>> getMyInoutList(Long tenantId, String enterCd, String sabun, String month) throws Exception {
		
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("month", month);
		
		return inoutHisMapper.getMyInoutList(paramMap);
	}
	
	@Override
	public Map<String, Object> getMyInoutDetail(Long tenantId, String enterCd, String sabun, String month) throws Exception {
		
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("month", month);
		
		return inoutHisMapper.getMyInoutDetail(paramMap);
	}
}