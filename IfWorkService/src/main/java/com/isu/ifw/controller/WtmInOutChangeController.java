package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmInOutChangeService;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/inOutChange")
public class WtmInOutChangeController {
	
	@Autowired
	WtmInOutChangeService inOutChangeService;
	
	@Autowired
	WtmInoutService inoutService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setInOutChangeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		logger.debug("setInOutChangeList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		// 20200118 효정 list 처리를 단건루프로 변경 (타각갱신을 건별처리해야함)
		if(convertMap.containsKey("insertRows") && ((List)convertMap.get("insertRows")).size() > 0) {
			List<Map<String, Object>> iList = (List<Map<String, Object>>) convertMap.get("insertRows");
			if(iList != null && iList.size() > 0) {
				for(Map<String, Object> l : iList) {
					try {
						Map<String, Object> saveMap = new HashMap();
						Map<String, Object> retMap = new HashMap();
						saveMap.put("tenantId", tenantId);
						saveMap.put("enterCd", enterCd);
						saveMap.put("sabun", l.get("sabun").toString());
						saveMap.put("ymd", l.get("ymd").toString());
						saveMap.put("entrySdate", l.get("entrySdate").toString());
						saveMap.put("entryStypeCd", l.get("entryStypeCd").toString());
						saveMap.put("entryEdate", l.get("entryEdate").toString());
						saveMap.put("entryEtypeCd", l.get("entryEtypeCd").toString());
						saveMap.put("chgSdate", l.get("chgSdate").toString());
						saveMap.put("chgEdate", l.get("chgEdate").toString());
						saveMap.put("reason", l.get("reason").toString());
						saveMap.put("typeCd", "ADM");
						saveMap.put("stdYmd", l.get("ymd").toString());
						saveMap.put("userId", userId);
						// 저장하자
						retMap = inOutChangeService.setInOutChange(saveMap);
						// 이력저장, 캘린더 수정했으니깐 계산을 다시하자 출근/퇴근이 전부 있어야 호출할수있음.
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						Date today = new Date();
						String ymd = sdf.format(today);
						if(Integer.parseInt(ymd) > Integer.parseInt(l.get("ymd").toString()) 	// 소급이거나
						   || (ymd.equals(l.get("ymd").toString()) && (!"".equals(l.get("chgSdate").toString()) || !"".equals(l.get("entrySdate").toString())) && (!"".equals(l.get("chgEdate").toString()) || !"".equals(l.get("entryEdate").toString())))	// 오늘인데 타각이 모두 있을경우 
						) {
							logger.debug("/mobile/"+ tenantId+"/inout/out s2 " + paramMap.toString());
							System.out.println("unplannedyn : " + retMap.get("unplannedYn").toString());
							inoutService.inoutPostProcess(saveMap, retMap.get("unplannedYn").toString());
						}

					} catch(Exception e) {
						logger.debug("outexception : " + e.getMessage() + paramMap.toString());
						rp.setFail(e.getMessage());
						return rp;
					}
				}
			}
		}
		/*
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		MDC.put("convertMap", convertMap);
		
		int cnt = 0;
		cnt = inOutChangeService.setInOutChangeList(tenantId, enterCd, userId, convertMap);
		
		if(cnt > 0) {
			rp.setSuccess("저장이 성공하였습니다.");
			return rp;
		}
		 */
		rp.setSuccess("저장이 성공하였습니다.");
		return rp;
	}
	
	/**
	 * 관리자_타각 변경 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEmpWorkCalendar(@RequestParam Map<String, Object> paramMap
													   				 , HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String sYmd = paramMap.get("sYmd").toString();
		String eYmd = paramMap.get("eYmd").toString();
		String searchKeyword = paramMap.get("searchKeyword").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("sYmd", sYmd);
		paramMap.put("eYmd", eYmd);
		paramMap.put("searchKeyword", searchKeyword);
		
		rp.setSuccess("");
	
		List<Map<String, Object>> inoutList = null;
		try {		
			inoutList =  inOutChangeService.getInpoutChangeHis(tenantId, enterCd, empNo, paramMap);
			
			rp.put("DATA", inoutList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}

}
