package com.isu.ifw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmApplCode;
import com.isu.ifw.entity.WtmFlexibleAppl;
import com.isu.ifw.entity.WtmOtAppl;
import com.isu.ifw.mapper.WtmFlexibleEmpMapper;
import com.isu.ifw.repository.WtmApplCodeRepository;
import com.isu.ifw.repository.WtmFlexibleApplRepository;
import com.isu.ifw.service.*;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmApplLineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping(value="/appl")
public class WtmApplController {
	
	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	WtmAsyncService wtmAsyncService;
	
	@Autowired
	@Qualifier("wtmApplService")
	WtmApplService applService;
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService flexibleApplService;
	
	@Autowired
	@Qualifier("wtmOtApplService")
	WtmApplService wtmOtApplService;
	
	@Autowired
	@Qualifier("wtmOtCanApplService")
	WtmApplService wtmOtCanApplService;
	
	@Autowired
	@Qualifier("wtmEntryApplService")
	WtmApplService wtmEntryApplService;
	
	@Autowired
	@Qualifier("wtmOtSubsChgApplService")
	WtmApplService wtmOtSubsChgApplService;
	
	@Autowired
	@Qualifier("wtmCompApplService")
	WtmApplService wtmCompApplService;
	
	@Autowired
	@Qualifier("wtmCompCanApplService")
	WtmApplService wtmCompCanApplService;

	@Autowired
	@Qualifier("WtmTaaApplService")
	WtmApplService wtmTaaApplService;

	@Autowired
	@Qualifier("WtmRegaApplService")
	WtmApplService wtmRegaApplService;

	@Autowired
	@Qualifier("wtmRegaCanService")
	WtmApplService wtmRegaCanService;

	@Autowired
	@Qualifier("wtmTaaCanService")
	WtmTaaCanApplServiceImpl wtmTaaCanApplService;

	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService wtmFlexibleApplService;

	@Autowired
	WtmApplCodeRepository wtmApplCodeRepo;
	
	@Autowired
	WtmFlexibleEmpMapper wtmFlexibleEmpMapper;
	
	@Autowired
	WtmInoutService inoutService;
	
	@Autowired
	@Qualifier(value="flexibleEmpService")
	WtmFlexibleEmpService wtmflexibleEmpService;
	
	@Autowired
	@Qualifier(value="applLineService")
	WtmApplLineService wtmApplLineService;
	
	@Autowired
	WtmMsgService msgService;

	@Autowired	private WtmFlexibleApplRepository flexApplRepository;

	@Autowired
	WtmFlexibleEmpService flexibleEmpService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApprList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		rp.setSuccess("");
		
		List<Map<String, Object>> apprList = null;
		try {		
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(">>>>>>>>>>>>>>>>["+empNo+"] approvalList applType: "+ paramMap.get("applType"));
			logger.debug(">>>>>>>>>>>>>>>>["+empNo+"] approvalList applType: "+ paramMap.get("applType"));
			
			apprList = applService.getApprList(tenantId, enterCd, empNo, paramMap, userId);
			
			System.out.println(" 22. apprList : " + mapper.writeValueAsString(apprList));
			rp.put("apprList", apprList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/line", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApprLine(@RequestParam String applCd, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		rp.setSuccess("");
		
		List<WtmApplLineVO> applLine = null;
		try {		
			applLine = wtmApplLineService.getApplLine(tenantId, enterCd, empNo, applCd, userId);
			
			rp.put("applLine", applLine);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody WtmApplCode getApplCode(@RequestParam String applCd, HttpServletRequest request) throws Exception {
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return wtmApplCodeRepo.findByTenantIdAndEnterCdAndApplCd(tenantId, enterCd, applCd);
	}
	
	@RequestMapping(value="/apply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam applyAppl(@RequestBody Map<String, Object> paramMap
												, HttpServletRequest request) {
		
		validateParamMap(paramMap, "applId", "apprSeq");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String applCd = paramMap.get("applCd").toString();
		int apprSeq = Integer.valueOf(paramMap.get("apprSeq").toString());
				
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(applCd!=null && !"".equals(applCd)) {
				boolean initResult = false;
				boolean isFlexAppl = false;
				
				if("OT".equals(applCd)) {
					rp = wtmOtApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
					if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) && rp.containsKey("otApplList")) { 
						
						if(rp.get("otApplList")!=null && !"".equals(rp.get("otApplList"))) {
							List<WtmOtAppl> otApplList = (List<WtmOtAppl>)rp.get("otApplList");
							
							if(otApplList!=null && otApplList.size()>0) {
								//소급의 경우 인정시간과 연장근로시간을 비교하여 다른 경우 대체휴일 정보를 생성하지 않는다.
								//미래의 연장근로시간의 경우 인정시간계산 서비스에서 대체휴일 정보를 생성한다.
								wtmflexibleEmpService.applyOtSubs(tenantId, enterCd, otApplList, true, userId);
							}
							
						}
					}
					
				} else if("OT_CAN".equals(applCd)){
					rp = wtmOtCanApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
				} else if("ENTRY_CHG".equals(applCd)){
					rp = wtmEntryApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
					if(rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
						// 20200827 이효정 결재갱신은 완료 과거이면 마감을 돌리자
						String stdYmd = rp.get("stdYmd").toString();
						Date today = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						String ymd = sdf.format(today);
						if(Integer.parseInt(ymd) > Integer.parseInt(stdYmd)){ 	// 소급이면 마감돌리기
							Map<String, Object> pMap = new HashMap<String, Object>();
							pMap.put("tenantId", tenantId);
							pMap.put("enterCd", enterCd);
							pMap.put("stdYmd", rp.get("stdYmd")+"");
							pMap.put("sabun", rp.get("sabun")+"");
							pMap.put("paramSdate", rp.get("stdYmd")+"");
							pMap.put("paramEdate", rp.get("stdYmd")+"");

							// 근태사유서 신청한 날의 마감을 다시 돌려 주도록 한다.
							// 주말근무시에는 마감을 다시 돌려줘야 보상휴가가 있을경우 다시 생성이 된다.
							flexibleEmpService.finishDay((Map<String, Object>)pMap, tenantId, enterCd, rp.get("sabun")+"", userId);

						}
					}
					
				} else if("SUBS_CHG".equals(applCd)){
					rp = wtmOtSubsChgApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
					if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) && rp.containsKey("otApplList")) { 
						
						if(rp.get("otApplList")!=null && !"".equals(rp.get("otApplList"))) {
							List<WtmOtAppl> otApplList = (List<WtmOtAppl>)rp.get("otApplList");
							
							if(otApplList!=null && otApplList.size()>0) {
								//소급의 경우 인정시간과 연장근로시간을 비교하여 다른 경우 대체휴일 정보를 생성하지 않는다.
								//미래의 연장근로시간의 경우 인정시간계산 서비스에서 대체휴일 정보를 생성한다.
								wtmflexibleEmpService.applyOtSubs(tenantId, enterCd, otApplList, false, userId);
							}
							
						}
					}
				} else if("COMP".equals(applCd)) {
					rp = wtmCompApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
				} else if("COMP_CAN".equals(applCd)) {
					rp = wtmCompCanApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
				} else if("ANNUAL".equals(applCd)) {
					rp = wtmTaaApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);

				} else if("REGA".equals(applCd)) {
					rp = wtmRegaApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);

				} else if("ANNUAL_CAN".equals(applCd)) {
					rp = wtmTaaCanApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);

				} else if("REGA_CAN".equals(applCd)) {
					rp = wtmRegaCanService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);

				}else {
					isFlexAppl = true;
					rp = flexibleApplService.apply(tenantId, enterCd, applId, apprSeq, paramMap, sabun, userId);
					
					if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) && rp.containsKey("sabun")) { 
						
						//유연근무제 기간 앞뒤로 +1 일
						/*Date sDate = WtmUtil.toDate(rp.get("symd")+"", "yyyyMMdd");
						Calendar sYmd = Calendar.getInstance();
						sYmd.setTime(sDate);
						sYmd.add(Calendar.DATE, -1);
						
						Date eDate = WtmUtil.toDate(rp.get("eymd")+"", "yyyyMMdd");
						Calendar eYmd = Calendar.getInstance();
						eYmd.setTime(eDate);
						eYmd.add(Calendar.DATE, 1);*/
						//wtmAsyncService.initWtmFlexibleEmpOfWtmWorkDayResult(tenantId, enterCd, rp.get("sabun")+"", rp.get("symd")+"", rp.get("eymd")+"", userId);
						
						initResult = true;
					}
				}
				
				if(rp.containsKey("sabun") && rp.containsKey("symd") && rp.containsKey("eymd")) {
					wtmAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, rp.get("sabun")+"", rp.get("symd")+"", rp.get("eymd")+"", userId, initResult);
				}
				
				//메일 전송
				if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) 
						&& rp.containsKey("from") && rp.get("from")!=null && !"".equals(rp.get("from")) 
						&& rp.containsKey("to") && rp.get("to")!=null && !"".equals(rp.get("to"))
						&& rp.containsKey("msgType") && rp.get("msgType")!=null && !"".equals(rp.get("msgType"))) { 
					List<String> toSabuns = (List<String>)rp.get("to");

					String applCode = isFlexAppl?"FLEX":applCd;
					
					msgService.sendMailForAppl(tenantId, enterCd, rp.get("from").toString(), toSabuns, applCode, rp.get("msgType").toString());
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value="/reject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam rejectAppl(@RequestBody Map<String, Object> paramMap
												, HttpServletRequest request) {
		
		validateParamMap(paramMap, "applId", "apprSeq");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String applCd = paramMap.get("applCd").toString();
		int apprSeq = Integer.valueOf(paramMap.get("apprSeq").toString());
				
		try {
			if(applCd!=null && !"".equals(applCd)) {
				if("OT".equals(applCd)) {
					wtmOtApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else if("COMP".equals(applCd)) {
					wtmCompApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else if("COMP_CAN".equals(applCd)) {
					wtmCompCanApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else if("ANNUAL".equals(applCd)) { //휴가신청
					wtmTaaApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else if("ANNUAL_CAN".equals(applCd)) { //휴가취소신청
					wtmTaaCanApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else if("REGA".equals(applCd)) { //출장신청
					wtmRegaApplService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else if("REGA_CAN".equals(applCd)) { //출장신청취소
					wtmRegaCanService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				} else {
					applService.reject(tenantId, enterCd, applId, apprSeq, paramMap, empNo, userId);
				}
				
				//메일 전송
				if(rp.getStatus()!=null && "OK".equals(rp.getStatus()) 
						&& rp.containsKey("from") && rp.get("from")!=null && !"".equals(rp.get("from")) 
						&& rp.containsKey("to") && rp.get("to")!=null && !"".equals(rp.get("to"))) { 
					List<String> toSabuns = (List<String>)rp.get("to");
					
					String applCode = null;
					
					if("DIFF".equals(applCd) || "ELAS".equals(applCd) || "SELE_C".equals(applCd) || "SELE_F".equals(applCd))
						applCode = "FLEX";
					else
						applCode = applCd;
					
					msgService.sendMailForAppl(tenantId, enterCd, rp.get("from").toString(), toSabuns, applCode, "REJECT");
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam deleteAppl(@RequestBody Map<String, Object> paramMap
												, HttpServletRequest request) {
		
		validateParamMap(paramMap, "applCd", "applId");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		Long applId = Long.valueOf(paramMap.get("applId").toString());
		String applCd = paramMap.get("applCd").toString();
		logger.debug("appl/delete " + tenantId + ", "+ enterCd +", "+ userId +", "+ applCd +", "+applId);
	
		try {
			if(applCd!=null && !"".equals(applCd)) {
				if("OT".equals(applCd)) {
					wtmOtApplService.delete(applId);
				} else if("OT_CAN".equals(applCd)) {
					wtmOtCanApplService.delete(applId);
				} else if("SUBS_CHG".equals(applCd)) {
					wtmOtSubsChgApplService.delete(applId);
				} else if("COMP".equals(applCd)) {
					wtmCompApplService.delete(applId);
				} else if("COMP_CAN".equals(applCd)) {
					wtmCompCanApplService.delete(applId);
				} else {
					WtmFlexibleAppl appl = flexApplRepository.findByApplId(applId);
					if(appl == null) {
						throw new Exception("삭제할 정보를 찾을 수 없습니다. 화면을 갱신하시고 다시 시도해주세요.");
					}
					
					flexibleApplService.delete(applId);
					
					String applStatusCd = null;
					if(paramMap.containsKey("applStatusCd") && paramMap.get("applStatusCd")!=null && !"".equals(paramMap.get("applStatusCd")))
						applStatusCd = paramMap.get("applStatusCd").toString();
						
					if((applStatusCd==null || !WtmApplService.APPL_STATUS_IMSI.equals(applStatusCd)) && paramMap.containsKey("sabun") && paramMap.containsKey("sYmd") && paramMap.containsKey("eYmd")) {
						//wtmAsyncService.initWtmFlexibleEmpOfWtmWorkDayResult(tenantId, enterCd, paramMap.get("sabun")+"", paramMap.get("sYmd")+"", paramMap.get("eYmd")+"", userId);
						//wtmAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, paramMap.get("sabun")+"", paramMap.get("sYmd")+"", paramMap.get("eYmd")+"", userId);
						wtmAsyncService.createWorkTermtimeByEmployee(tenantId, enterCd, paramMap.get("sabun")+"", paramMap.get("sYmd")+"", paramMap.get("eYmd")+"", userId, true);
					}
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
	
	/**
	 * 파라미터 맵의 유효성을 검사한다.
	 * 파라미터가 다음의 두 조건을 만족하지 않으면, InvalidParameterException를 발생한다.
	 * 1. map 자체가 null 인 경우
	 * 2. paramName 배열에 기술된 값이 map에 (하나라도) 키로 존재하지 않는 경우
	 * 
	 * @param paramMap
	 * @param parameterNames
	 * @throws InvalidParameterException
	 */
	protected void validateParamMap(Map<String,Object> paramMap, String...parameterNames )throws InvalidParameterException{
		
		// 파라미터가 아무것도 없는 경우에, 아무것도 할 수 없다. 무조건 예외 발생
		if(paramMap == null)
			throw new InvalidParameterException("param map is null.");
		
		if(parameterNames == null)
			return; // 파라미터가 없으면 그냥 리턴..
		
		// 넘겨 받은 이름 배열이 map의 부분 집합인지를 따짐.
		Set<String> paramKeySet = paramMap.keySet();
		Collection<String> params = Arrays.asList(parameterNames);
		
		if(!paramKeySet.containsAll(params))
			throw new InvalidParameterException("required parameter is not found.");
		
	}

	@RequestMapping(value="/approvalApplList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApprovalApplList(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		rp.setSuccess("");

		List<Map<String, Object>> approvalApplList = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(">>>>>>>>>>>>>>>>["+empNo+"] approvalApplList applType: "+ paramMap.get("applType"));
			logger.debug(">>>>>>>>>>>>>>>>["+empNo+"] approvalApplList applType: "+ paramMap.get("applType"));

			approvalApplList = applService.getApprovalApplList(tenantId, enterCd, empNo, paramMap, userId);

			System.out.println(" 22. apprList : " + mapper.writeValueAsString(approvalApplList));
			rp.put("DATA", approvalApplList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}

	@RequestMapping(value="/getApplDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplDetail(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		String applCd = paramMap.get("applCd").toString();
		String applSabun = paramMap.get("applSabun").toString();
		Long applId = Long.parseLong(paramMap.get("applId").toString());

		rp.setSuccess("");

		Map<String, Object> appl = null;
		try {
			ObjectMapper mapper = new ObjectMapper();

			if("OT".equals(applCd)
					|| WtmApplService.TIME_TYPE_EARLY_OT .equals(applCd)
					|| WtmApplService.TIME_TYPE_EARLY_NIGHT .equals(applCd)
					|| WtmApplService.TIME_TYPE_NIGHT .equals(applCd)
			) { //연장
				appl = wtmOtApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("OT_CAN".equals(applCd)) { //연장 취소
				appl = wtmOtCanApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("SUBS_CHG".equals(applCd)) { //대체휴가 정정
				appl = wtmOtSubsChgApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("ENTRY_CHG".equals(applCd)) { //근태사유서
				appl = wtmEntryApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("COMP".equals(applCd)) { //보상휴가
				appl = wtmCompApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("COMP_CAN".equals(applCd)) { //보상휴가취소
				appl = wtmCompCanApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("ANNUAL".equals(applCd)) { //휴가신청
				appl = wtmTaaApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("REGA".equals(applCd)) { //출장신청
				appl = wtmTaaApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("ANNUAL_CAN".equals(applCd)) { //휴가신청취소
				appl = wtmTaaCanApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else if("REGA_CAN".equals(applCd)) { //출장취소신청
				appl = wtmTaaCanApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			} else {
				//유연근무제
				appl = wtmFlexibleApplService.getAppl(tenantId, enterCd, applSabun, applId, userId);
			}

			System.out.println(" 22. apprList : " + mapper.writeValueAsString(appl));
			if(appl != null) {
				rp.put("applCd", applCd);
			}
			rp.put("appl", appl);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}


	@RequestMapping(value="/approvalApplGrList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam approvalApplGrList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		List<Map<String, Object>> approvalApplList = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(">>>>>>>>>>>>>>>>["+empNo+"] approvalApplList applType: "+ paramMap.get("applType"));
			logger.debug(">>>>>>>>>>>>>>>>["+empNo+"] approvalApplList applType: "+ paramMap.get("applType"));

			approvalApplList = applService.getApprovalApplList(tenantId, enterCd, empNo, paramMap, userId);

			System.out.println(" 22. apprList : " + mapper.writeValueAsString(approvalApplList));
			rp.put("DATA", approvalApplList);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}

}
