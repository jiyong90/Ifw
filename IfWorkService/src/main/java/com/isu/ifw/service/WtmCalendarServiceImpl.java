package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmCalendarMapper;
import com.isu.ifw.mapper.WtmOrgChartMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.util.WtmUtil;

/**
 * 근태 달력 관리 service
 * @author 
 *
 */
@Service("WtmCalendarService")
public class WtmCalendarServiceImpl implements WtmCalendarService{
	
	@Autowired
	WtmCalendarMapper wtmCalendarMapper;
	
	@Autowired
	WtmOrgChartMapper wtmOrgChartMapper;
	
	@Autowired
	WtmEmpHisRepository empHisRepo;
	
	/**
	 * 달력 조회
	 * @param tenantId
	 * @param enterCd
	 * @param bisinessPlaceCd
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCalendar(Long tenantId, String enterCd, String bisinessPlaceCd, Map<String, Object> paramMap) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("bisinessPlaceCd", bisinessPlaceCd);
		
		//회사 달력
		result.put("companyCalendar", wtmCalendarMapper.getCalendar(paramMap));
		
		//개인 근태 달력
		//result.put("workCalendar", wtmCalendarMapper.getWorkCalendar(paramMap));
		
		//지난 날은 실적, 아직 도래하지 않은 날은 계획을 보여줌
		//계획
		
		//실적
		
		//권한에 따라 팀원들 근태 보여줌
		
		
		return result;
		
	}
	
	/**
	 * 근태 달력 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getWorkTimeCalendar(Map<String, Object> paramMap) throws Exception {
		return wtmCalendarMapper.getWorkTimeCalendar(paramMap);
	}
	
	/**
	 * 근태 달력 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getEmpWorkCalendar(Map<String, Object> paramMap) throws Exception {
		return wtmCalendarMapper.getEmpWorkCalendar(paramMap);
	}
	
	/**
	 * 조직원 근태 달력 조회(조직장권한)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrgEmpWorkCalendar(Long tenantId, String enterCd, String sabun, Map<String, Object> paramMap) throws Exception {
		
		String ymd = WtmUtil.parseDateStr(new Date(), "yyyyMMdd");
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("ymd", ymd);
		
		if(paramMap.get("sYmd")!=null && paramMap.get("eYmd")!=null) {
			paramMap.put("sYmd", paramMap.get("sYmd").toString().replaceAll("-", ""));
			paramMap.put("eYmd", paramMap.get("eYmd").toString().replaceAll("-", ""));
		}
		
		WtmEmpHis emp = empHisRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, ymd);
		if(emp!=null && emp.getOrgCd()!=null && !"".equals(emp.getOrgCd()))
			paramMap.put("orgCd", emp.getOrgCd());
		
		//하위 조직 조회
		List<Map<String, Object>> lowLevelOrgList = wtmOrgChartMapper.getLowLevelOrg(paramMap); 
		List<String> orgList = new ArrayList<String>();
		
		if(lowLevelOrgList!=null && lowLevelOrgList.size()>0) {
			for(Map<String, Object> orgMap : lowLevelOrgList) {
				orgList.add(orgMap.get("orgCd").toString());
			}
			
			paramMap.put("orgList", orgList);
		}
		
		return wtmCalendarMapper.getOrgEmpWorkCalendar(paramMap);
	}
	
	/**
	 * 근태 달력 조회(특정일)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getEmpWorkCalendarDayInfo(Map<String, Object> paramMap) throws Exception {
		return wtmCalendarMapper.getEmpWorkCalendarDayInfo(paramMap);
	}
}
