package com.isu.ifw.service;

import static org.mockito.Mockito.lenient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.isu.ifw.entity.WtmFlexibleEmp;
import com.isu.ifw.entity.WtmFlexibleStdMgr;
import com.isu.ifw.entity.WtmTaaCode;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.mapper.WtmFlexibleStdMapper;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmFlexibleStdMgrRepository;
import com.isu.ifw.repository.WtmTaaCodeRepository;
import com.isu.option.vo.ReturnParam;

public class WtmValidatorServiceImpl implements WtmValidatorService  {

	@Autowired
	WtmFlexibleStdMapper flexStdMapper;
	
	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	WtmFlexibleEmpRepository flexEmpRepo;
	
	@Autowired
	WtmFlexibleStdMgrRepository flexStdMgrRepo;

	@Autowired
	WtmTaaCodeRepository taaCodeRepo;
	
	public ReturnParam validTaa(Long tenantId, String enterCd, String sabun,
			String timeTypeCd, String taaCd,
			String symd, String shm, String eymd, String ehm, Long applId, String locale) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		WtmTaaCode taaCode = taaCodeRepo.findByTenantIdAndEnterCdAndTaaCd(tenantId, enterCd, taaCd);
		
		if(taaCode.getRequestTypeCd().equals(WtmTaaCode.REQUEST_TYPE_H)) {
			//근태가 시간단위 일때 시분 정보가 누락되지 않았는지 체크한다.
			if(shm.equals("") || ehm.equals("")) {
				rp.setFail("시간단위 근태의 경우 시간 정보가 필요합니다.");
				return rp;
			}
		}
		taaCode.getRequestTypeCd();
		List<WtmFlexibleEmp> empList = flexEmpRepo.findByTenantIdAndEnterCdAndSabunAndBetweenSymdAndEymd(tenantId, enterCd, sabun, symd, eymd);
		
		if(empList != null && empList.size() > 0) {
			for(WtmFlexibleEmp emp : empList) {
				//일별 근무제 옵션을 가지고 오자
				WtmFlexibleStdMgr flexStdMgr = flexStdMgrRepo.findById(emp.getFlexibleStdMgrId()).get();
				
				String workYn = flexStdMgr.getTaaWorkYn();
				
				 
				if(shm.equals("") || ehm.equals("")) {
					//없을 경우 근태에 해당하는 것만 체크 한다.
					
				}else {
					//근태 정보에 시간이 있으면 겹치는근태 시간이 있는지 확인하자
					
				}
				 
				
				//flexStdMgr.getTaaTimeYn() //가산여부 Y면 인정근무시간에 합산 / N이면 노합
				//flexStdMgr.getTaaWorkYn() //4시간 + 8시간을 할수 있다.
				
				//N이면4시간의 근무계획이 있을 경우 연차가 들어왔을 때는 안됨. 근무계획 변경 후 연차신청해야함.
			}
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("symd", symd);
		paramMap.put("eymd", eymd);
		paramMap.put("applId", applId);
		
		Map<String, Object> m = flexStdMapper.checkRequestDate(paramMap);
		int cnt = Integer.parseInt(m.get("CNT").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
	}	
	
	
	@Override
	public ReturnParam checkDuplicateFlexibleWork(Long tenantId, String enterCd, String sabun, String symd, String eymd, Long applId) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("symd", symd);
		paramMap.put("eymd", eymd);
		paramMap.put("applId", applId);
		
		Map<String, Object> m = flexStdMapper.checkRequestDate(paramMap);
		int cnt = Integer.parseInt(m.get("CNT").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
	}

	@Override
	public ReturnParam checkDuplicateWorktime(Long tenantId, String enterCd, String sabun, Date sdate, Date edate, Long applId){
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sabun", sabun);
		paramMap.put("sdate", sdate);
		paramMap.put("edate", edate);
		paramMap.put("applId", applId);

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		Map<String, Object> m = flexEmpMapper.checkDuplicateWorktime(paramMap);
		int cnt = Integer.parseInt(m.get("workCnt").toString());
		if(cnt > 0) {
			rp.setFail("신청중인 또는 이미 적용된 근무정보가 있습니다.");
			return rp;
		}
		return rp;
		 
	}
	
}
