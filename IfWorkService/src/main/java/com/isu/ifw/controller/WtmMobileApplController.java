package com.isu.ifw.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmWorkCalendar;
import com.isu.ifw.mapper.WtmApplMapper;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmWorkCalendarRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmMobileService;
import com.isu.ifw.util.MobileUtil;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmApplLineVO;


@RestController
public class WtmMobileApplController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@Autowired
	WtmMobileService mobileService;
	
	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService otApplService;

	@Resource
	WtmApplCodeRepository applCodeRepository;
	
	@Autowired
	WtmApplMapper applMapper;

	@Resource
	WtmEmpHisRepository empRepository;

	@Autowired
	WtmWorkCalendarRepository workCalendarRepo;

	@Autowired
	WtmFlexibleEmpMapper flexEmpMapper;
	
	@Autowired
	TenantConfigManagerService tcms;

	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	/**
	 * 연장/휴일 신청서 
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/apply/init", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getOtLoad(@PathVariable Long tenantId, 
			@RequestParam(value = "tenantKey", required = true) String tenantKey,
			@RequestParam(value="locale", required = true) String locale, 
			@RequestParam(value="empKey", required = false) String empKey, 
			@RequestParam(value="applCd", required = true) String applCd, 
			HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		try {
			String userToken = request.getParameter("userToken");
		
			//Aes256 aes = new Aes256(userToken);
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");

			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			logger.debug("/mobile/"+ tenantId+"/otload s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			Map<String, Object> paramMap = new HashMap();
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			paramMap.put("applCd", applCd);
			
			paramMap.put("d", WtmUtil.parseDateStr(new Date(), null));

			Map<String, Object> result = new HashMap();
			//결재선 생성
			WtmApplCode applCode = applCodeRepository.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
			
			List<WtmApplLineVO> applLineVOs = applMapper.getWtmApplLine(paramMap);

			List<Map<String, Object>> apprLines = MobileUtil.makeApprLines(applLineVOs, applCode);
			//디폴트 데이터 가져오기
			if(applCd.equals("OT")) {
				Map<String,Map<String,Object>> itemPropertiesMap = new HashMap();
				itemPropertiesMap.put("gubun", mobileService.getCodeList(tenantId, enterCd, "REASON_CD"));
				result.put("itemAttributesMap", itemPropertiesMap);
			}
			
			result.put("apprLines", apprLines);
			
			//신청서 정보 조회할때 일부 필요한 데이터는 data에 넣어서 validation체크할때 쓰자....
			Map<String, Object> dataMap = new HashMap();
			dataMap.put("applCd", applCd);
			dataMap.put("applNm", applCode.getApplNm());
			dataMap.put("applLevelCd", applCode.getApplLevelCd());

			result.put("data", dataMap);
			rp.put("result", result);
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("조회 중 오류가 발생하였습니다.");
		}
		
		logger.debug("/mobile/"+ tenantId+"/team/teamdetail s " + rp.toString());
		return rp;
	}
	
	/**
	 * 연장/휴일 신청서 validation 체크
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/apply/val", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> applyValidate(@PathVariable Long tenantId, 
														@RequestBody Map<String, Object> paramMap
														,HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> resultMap = new HashMap();
		Map<String, Object> dataMap = new HashMap();
		Map<String,Object> itemPropertiesMap = new HashMap();
		Map<String,Object> propertiesMap = new HashMap<String,Object>();
		try {
			String userToken = paramMap.get("userToken").toString();
			String empKey = paramMap.get("empKey").toString();
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}

			logger.debug("/mobile/"+ tenantId+"/apply/val s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);
	
			dataMap.put("tenantId", tenantId);
			dataMap.put("enterCd", enterCd);
			dataMap.put("applSabun", sabun);

			dataMap = (Map<String, Object>)paramMap.get("data");
			setData(dataMap);

			if(paramMap.get("eventSource").equals("ymd")) {
				//신청 가능한지 확인
				ReturnParam temp = otApplService.preCheck(tenantId, enterCd, sabun, dataMap.get("applCd").toString(), dataMap);
				logger.debug("otApplService.preCheck " + temp.toString());
				if(temp.getStatus().equals("FAIL")) {
					dataMap.put("ymd", "");
					throw new Exception(temp.get("message").toString());
				}
				//휴일인지 확인
				WtmWorkCalendar calendars =  workCalendarRepo.findByTenantIdAndEnterCdAndYmdAndSabun(tenantId, enterCd, dataMap.get("ymd").toString(), sabun);
				dataMap.put("holidayYn", calendars.getHolidayYn());
				dataMap.put("timeCdMgrId", calendars.getTimeCdMgrId());
				
				Map<String, Object> workHourMap = flexibleEmpService.calcMinuteExceptBreaktime(calendars.getTimeCdMgrId(), dataMap, emp.getEmpHisId().toString());
				if(workHourMap == null) {
					throw new Exception();
				}
				logger.debug("workHourMap " + workHourMap.toString());
				
				//신청 가능한 날인지 먼저 체크하고 아니면 리턴
				
				//대체 휴가 사용여부
				dataMap.put("subsYn", temp.get("subsYn"));
				//수당지급대상자인지
				dataMap.put("payTargetYn", temp.get("payTargetYn"));
				
				if(dataMap.get("subsYn").equals("Y") && (dataMap.get("holidayYn").equals("Y") || dataMap.get("payTargetYn").equals("Y"))) {
					propertiesMap.put("disabled", "false");
					
					List<Map<String,Object>> itemCollection = new ArrayList<Map<String,Object>>();
					Map<String,Object> item = new HashMap<String,Object>();
					item = new HashMap<String,Object>();
					item.put("text", "위로금/시급지급");
					item.put("value", "N");
					itemCollection.add(item);

					item = new HashMap<String,Object>();
					item.put("text", "휴일대체");
					item.put("value", "Y");
					itemCollection.add(item);
					propertiesMap.put("collection", itemCollection);

					itemPropertiesMap.put("subYn", propertiesMap);
				} else {
					propertiesMap.put("disabled", "true");
					itemPropertiesMap.put("subYn", propertiesMap);
					itemPropertiesMap.put("subsSymd", propertiesMap);
					itemPropertiesMap.put("subsShm", propertiesMap);
					itemPropertiesMap.put("subsEhm", propertiesMap);
					
					dataMap.put("subYn", "");
					dataMap.put("subsSymd", "");
					dataMap.put("subsShm", "");
					dataMap.put("subsEhm", "");
				}
			} else if(paramMap.get("eventSource").equals("subYn")) {
				if(dataMap.get("subYn").equals("Y")) {
					propertiesMap.put("disabled", "false");
					itemPropertiesMap.put("subsSymd", propertiesMap);
					itemPropertiesMap.put("subsShm", propertiesMap);
					itemPropertiesMap.put("subsEhm", propertiesMap);
					
				} else {
					propertiesMap.put("disabled", "true");
					itemPropertiesMap.put("subsSymd", propertiesMap);
					itemPropertiesMap.put("subsShm", propertiesMap);
					itemPropertiesMap.put("subsEhm", propertiesMap);

					dataMap.put("subsSymd", "");
					dataMap.put("subsShm", "");
					dataMap.put("subsEhm", "");
				}
			} else if (paramMap.get("eventSource").equals("subsSymd")) {
				//휴일인지 확인
				WtmWorkCalendar calendars =  workCalendarRepo.findByTenantIdAndEnterCdAndYmdAndSabun(tenantId, enterCd, dataMap.get("subsSymd").toString(), sabun);
				if(calendars != null && calendars.getHolidayYn().equals("Y")) {
					dataMap.put("subsSymd", "");
//					rp.setFail("해당일은 휴일입니다.");
//					return rp;
					throw new Exception("해당일은 휴일입니다.");
				}
			}
			
			Map<String, Object> otWorkTime = null;
			if(!dataMap.get("shm").equals("") && !dataMap.get("ehm").equals("")) {
				otWorkTime = flexibleEmpService.calcMinuteExceptBreaktime(tenantId, enterCd, sabun, dataMap, emp.getEmpHisId().toString());
				
				System.out.println("otWorkTime" + otWorkTime.toString()); //{breakMinuteNoPay=30, calcMinute=-30, breakMinutePaid=0, breakMinute=30}
				
				if(otWorkTime != null && !otWorkTime.get("breakMinute").equals("")) {
					dataMap.put("desc", "근로시간 : "+otWorkTime.get("calcMinute").toString() + "분 휴게시간 : " + otWorkTime.get("breakMinute").toString() + "분");
				}
			}

			dataMap.put("calcMinute", otWorkTime != null ? otWorkTime.get("calcMinute").toString(): "0");
			
			Map<String, Object> val = applMapper.getApplValidation(dataMap);
			logger.debug("applValidationCheck : " + dataMap.toString() + " , " + val.toString());
			System.out.println("applValidationCheck : " + dataMap.toString() + " , " + val.toString());
			if(val == null) {
				throw new Exception("validation check에 실패하였습니다.");
			}

			if(val.get("valDate").equals("N")) {
				dataMap.put("ymd", "");
				throw new Exception("신청 가능한 기간은 " + val.get("pDate").toString() + " 입니다.");
				//rp.setFail("신청 가능한 기간은 " + val.get("pDate").toString() + " 입니다.");
			} else if(val.get("valTime").equals("N")) {
				//dataMap.put("shm", "");
				//dataMap.put("ehm", "");
				throw new Exception("신청 가능한 시간은 " + val.get("pTime").toString() + " 입니다.");
				//rp.setFail("신청 가능한 시간은 " + val.get("pTime").toString() + " 입니다.");
			} else if(val.get("valUnit").equals("N")) {
				//dataMap.put("shm", "");
				//dataMap.put("ehm", "");
				throw new Exception("신청 가능한 시간단위는 " + val.get("pUnit").toString() + "분 입니다.");
				//rp.setFail("신청 가능한 시간단위는 " + val.get("pUnit").toString() + "분 입니다.");
			} else if(val.get("valHunit").equals("N")) {
				//dataMap.put("shm", "");
				//dataMap.put("ehm", "");
				throw new Exception("신청 가능한 최대 시간은 " + val.get("pHunit").toString() + "분 입니다.");
				//rp.setFail("신청 가능한 최대 시간은 " + val.get("pHunit").toString() + "분 입니다.");
			} else if(val.get("valSdate").equals("N")) {
				//dataMap.put("subsSymd", "");
				throw new Exception("대체 휴일 신청 가능 기간은 " + val.get("pSdate").toString() + " 입니다.");
				//rp.setFail("대체 휴일 신청 가능 기간은 " + val.get("pSdate").toString() + " 입니다.");
			}
			//여기까진 신청서에 대한 validation

			if(!dataMap.get("subsSymd").equals("") && !dataMap.get("subsShm").equals("") && !dataMap.get("subsEhm").equals("")) {
				Map<String, Object> temp = new HashMap();
				temp.put("ymd", dataMap.get("subsSymd"));
				temp.put("shm", dataMap.get("subsShm"));
				temp.put("ehm", dataMap.get("subsEhm"));
				
				Map<String, Object> subWork = flexibleEmpService.calcMinuteExceptBreaktime(tenantId, enterCd, sabun, temp, emp.getEmpHisId().toString());
				if(subWork != null && !subWork.get("calcMinute").toString().equals(dataMap.get("calcMinute").toString())) {
					throw new Exception("대체 휴일 휴게시간은 " + subWork.get("breakMinute") + "분입니다. 신청해야 하는 휴게 시간은 " + dataMap.get("calcMinute") + "분입니다.");
					//rp.setFail("대체 휴일 휴게시간은 " + subWork.get("breakMinute") + "분입니다. 신청해야 하는 휴게 시간은 " + dataMap.get("calcMinute") + "분입니다.");
					//return rp;
				}
				
				System.out.println("otWorkTime" + otWorkTime.toString()); //{breakMinuteNoPay=30, calcMinute=-30, breakMinutePaid=0, breakMinute=30}
				
				if(otWorkTime != null && !otWorkTime.get("breakMinute").equals("")) {
					dataMap.put("desc", "근로시간 : "+otWorkTime.get("calcMinute").toString() + "분 휴게시간 : " + otWorkTime.get("breakMinute").toString() + "분");
				}

			}
			//데이터 검증
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail(e.getMessage());
		}
		setResult(dataMap);
		
		resultMap.put("itemAttributesMap", itemPropertiesMap);
		resultMap.put("data", dataMap);
		rp.put("result", resultMap);
		logger.debug("/mobile/"+ tenantId+"/apply/val e " + rp.toString());
		return rp;
	}
	
	private void setData(Map<String, Object> dataMap) {
		dataMap.put("ymd", dataMap.get("ymd").toString().replace(".", ""));
		//입력 항목으로 validation check
		if(!dataMap.containsKey("ehm")) {
			dataMap.put("ehm", "");
		} else {
			dataMap.put("ehm", dataMap.get("ehm").toString().replace(":", ""));
		}
		if(!dataMap.containsKey("shm")) {
			dataMap.put("shm", "");
		} else {
			dataMap.put("shm", dataMap.get("shm").toString().replace(":", ""));
		}
		if(!dataMap.containsKey("subsSymd")) {
			dataMap.put("subsSymd", "");
		} else {
			dataMap.put("subsSymd", dataMap.get("subsSymd").toString().replace(".", ""));
		}
		if(!dataMap.containsKey("subsShm")) {
			dataMap.put("subsShm", "");
		} else {
			dataMap.put("subsShm", dataMap.get("subsShm").toString().replace(":", ""));
		}
		if(!dataMap.containsKey("subsEhm")) {
			dataMap.put("subsEhm", "");
		} else {
			dataMap.put("subsEhm", dataMap.get("subsEhm").toString().replace(":", ""));
		}
	}
	
	private void setResult(Map<String, Object> dataMap) {
		if(!dataMap.get("ymd").equals("")) {
			dataMap.put("ymd", dataMap.get("ymd").toString().substring(0, 4)+"."+dataMap.get("ymd").toString().substring(4, 6) +"."+dataMap.get("ymd").toString().substring(6, 8));
		}
		if(!dataMap.get("shm").equals("")) {
			dataMap.put("shm", dataMap.get("shm").toString().substring(0, 2)+":"+dataMap.get("shm").toString().substring(2, 4));
		}
		if(!dataMap.get("ehm").equals("")) {
			dataMap.put("ehm", dataMap.get("ehm").toString().substring(0, 2)+":"+dataMap.get("ehm").toString().substring(2, 4));
		}
		if(!dataMap.get("subsSymd").equals("")) {
			dataMap.put("subsSymd", dataMap.get("subsSymd").toString().substring(0, 4)+"."+dataMap.get("subsSymd").toString().substring(4, 6) +"."+dataMap.get("subsSymd").toString().substring(6, 8));
		}
		if(!dataMap.get("subsShm").equals("")) {
			dataMap.put("subsShm", dataMap.get("subsShm").toString().substring(0, 2)+":"+dataMap.get("subsShm").toString().substring(2, 4));
		}
		if(!dataMap.get("subsEhm").equals("")) {
			dataMap.put("subsEhm", dataMap.get("subsEhm").toString().substring(0, 2)+":"+dataMap.get("subsEhm").toString().substring(2, 4));
		}
	}

	/**
	 * 연장/휴일 신청서 저장
	 * @param tenantKey
	 * @param locale
	 * @param empKey
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mobile/{tenantId}/apply/req", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public @ResponseBody Map<String, Object> applyRequest(@PathVariable Long tenantId, 
														@RequestBody Map<String, Object> paramMap
														,HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("신청되었습니다.");
		
		try {
			String userToken = paramMap.get("userToken").toString();
			String empKey = paramMap.get("empKey").toString();
			String enterCd = MobileUtil.parseEmpKey(userToken, empKey, "enterCd");
			String sabun = MobileUtil.parseEmpKey(userToken, empKey, "sabun");
			WtmEmpHis emp = empRepository.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun,  WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp == null) {
				rp.setFail("사용자 정보 조회 중 오류가 발생하였습니다.");
				return rp;
			}
			
			logger.debug("/mobile/"+ tenantId+"/apply/val s " + WtmUtil.paramToString(request) + ", "+enterCd + ", " + sabun);

			Map<String, Object> dataMap = (Map)paramMap.get("data");
			setData(dataMap);
			dataMap.put("otSdate", dataMap.get("ymd").toString() + dataMap.get("shm").toString());
			dataMap.put("otEdate", dataMap.get("ymd").toString() + dataMap.get("ehm").toString());
			rp =  otApplService.validate(tenantId, enterCd, sabun, dataMap.get("applCd").toString(), dataMap);
			if(rp!=null && rp.getStatus()!=null && "FAIL".equals(rp.getStatus())) {
				return rp;
			}
			
			rp = otApplService.requestSync(tenantId, enterCd, dataMap, sabun, emp.getEmpHisId().toString());
		} catch(Exception e) {
			logger.debug(e.getMessage());
			rp.setFail("신청 중 오류가 발생하였습니다.");
		}
		logger.debug("/mobile/"+ tenantId+"/apply/val e " + rp.toString());
		return rp;
	}

}