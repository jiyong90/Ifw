package com.isu.ifw.controller;

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

import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.service.WtmInOutChangeService;
import com.isu.ifw.util.WtmUtil;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/inOutChange")
public class WtmInOutChangeController {
	
	@Autowired
	WtmInOutChangeService inOutChangeService;

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setInOutChangeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		Long userId = Long.valueOf(sessionData.get("userId").toString());
		
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		MDC.put("param", paramMap.toString());
		logger.debug("setInOutChangeList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
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
			inoutList =  inOutChangeService.getInpoutChangeHis(paramMap);
			
			rp.put("DATA", inoutList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}

		return rp;
	}

}
