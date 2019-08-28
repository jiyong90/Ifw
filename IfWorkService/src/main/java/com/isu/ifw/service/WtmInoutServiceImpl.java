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
	
	@Override
	public Map<String, Object> getMenuContext(String enterCd, String sabun) {

		Map <String,Object> menuAttributeMap = new HashMap<String,Object>();
		Map <String,Object> paramMap = new HashMap<String, Object>();
		//tenant 어디서 가져올지
		paramMap.put("tenantId", "1");
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
			
		String ymd = null; //기준일 
		String md = null; // 기준일에서 월/일만 뺀 값  
		String inoutType = "NONE";
		String label = "근무계획없음";
		String description = "출근체크 필요시 인사팀에 문의 바랍니다";
		
		try {
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
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			menuAttributeMap.put("ymd", ymd);
			menuAttributeMap.put("inoutType", inoutType);
			menuAttributeMap.put("label", label);
			menuAttributeMap.put("description", description);
		}
		System.out.println("getMenuContext " + enterCd +"@" +sabun+"@"+menuAttributeMap.toString());
		return menuAttributeMap;
	}

	@Override
	public int checkInoutHis(String enterCd, String sabun, String inoutType, String ymd) throws Exception {
		
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
	
		cnt += wtmCalendarMapper.updateEntryDate(paramMap);
		//근무캘린더에 시간만 업데이트
		return cnt;
	}
}