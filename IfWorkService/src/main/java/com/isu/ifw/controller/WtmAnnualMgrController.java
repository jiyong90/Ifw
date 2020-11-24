package com.isu.ifw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.entity.WtmAnnualMgr;
import com.isu.ifw.service.WtmAnnualMgrService;
import com.isu.ifw.service.WtmCodeService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 연차기준관리 컨트롤러
 */
@RequestMapping("/wtmAnnualMgr")
@RestController
public class WtmAnnualMgrController {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	WtmAnnualMgrService wtmAnnualMgrService;

	@Autowired
	@Qualifier("codeService")
	WtmCodeService codeService;

	@RequestMapping(value = "/list",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getBaseWorkList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();

		rp.setSuccess("");


		try {
			List<WtmAnnualMgr> list = wtmAnnualMgrService.getList(tenantId, enterCd);

			rp.put("DATA", list);
		} catch (Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("wtmAnnualMgrService.getList rp " + rp.toString());
		return rp;
	}

	@RequestMapping(value = "/save",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam saveAnnualMgr(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		logger.debug("saveAnnualMgr Controller Start " + paramMap.toString());

		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request, paramMap.get("s_SAVENAME").toString(), "");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);
		convertMap.put("minusYn", paramMap.get("minusYn").toString());
		convertMap.put("note", paramMap.get("note").toString());
		convertMap.put("taaTypeCd", paramMap.get("taaTypeCd").toString());

		logger.debug("saveAnnualMgr Controller convertMap " + convertMap.toString());

		try {
			wtmAnnualMgrService.save(tenantId, enterCd, userId, convertMap);
			rp.setSuccess("저장이 성공하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rp;
	}

	@RequestMapping(value = "/taaTypeCd",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getTaaTypeCd(HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper        mapper      = new ObjectMapper();
		String userId = sessionData.get("userId").toString();
		String enterCd = sessionData.get("enterCd").toString();

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");


		try {
			List<Map<String, Object>> cdList = wtmAnnualMgrService.getCodeList(tenantId, enterCd);
//			List<Map<String, Object>> cdList = codeService.getTaaCodeList(tenantId, enterCd);
			rp.put("DATA", cdList);
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("getTaaTypeCd rp " + rp.toString());

		return rp;
	}

}
