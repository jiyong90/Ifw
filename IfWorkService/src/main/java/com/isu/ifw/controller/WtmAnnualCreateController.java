package com.isu.ifw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.service.WtmAnnualCreateService;
import com.isu.ifw.service.WtmAnnualMgrService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmAnnualCreateVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 연차내역관리 컨트롤러
 */
@RequestMapping("/wtmAnnualCreate")
@RestController
public class WtmAnnualCreateController {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	WtmAnnualMgrService wtmAnnualMgrService;

	@Autowired
	WtmAnnualCreateService wtmAnnualCreateService;

	@RequestMapping(value = "/list",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getBaseWorkList(HttpServletRequest request, @RequestParam ( name = "sYmd") String sYmd
																, @RequestParam(name = "eYmd", required = false) String eYmd
																, @RequestParam(name = "searchType", required = false) String searchType
																, @RequestParam(name = "searchKeyword", required = false) String searchKeyword) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId")
		                                    .toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd")
		                            .toString();

		rp.setSuccess("");


		try {
			List<WtmAnnualCreateVo> list = wtmAnnualCreateService.getList(tenantId, enterCd, sYmd.replaceAll("[-.]", ""), searchKeyword, searchType);

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
	public ReturnParam saveAnnualCreate(HttpServletRequest request,
	                                    @RequestParam(name = "sabun") String sabun,
	                                    @RequestParam(name = "yy") String yy,
	                                    @RequestParam(name = "taaTypeCd") String taaTypeCd,
	                                    @RequestParam(name = "symd") String symd,
	                                    @RequestParam(name = "eymd") String eymd,
	                                    @RequestParam(name = "createCnt") Integer createCnt,
	                                    @RequestParam(name = "note", required = false) String note,
	                                    @RequestParam Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId")
		                                    .toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd")
		                            .toString();
		String empNo = sessionData.get("empNo")
		                          .toString();
		String userId = sessionData.get("userId")
		                           .toString();

		logger.debug("saveAnnualCreate Controller Start " + paramMap.toString());


		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request, paramMap.get("s_SAVENAME")
		                                                                                  .toString(), "");

		logger.debug("saveAnnualCreate Controller convertMap " + convertMap.toString());

		try {

			wtmAnnualCreateService.save(tenantId, enterCd, userId, convertMap);
			rp.setSuccess("저장이 성공하였습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rp;
	}



	@RequestMapping(value="/getTaaCodelist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getCodeList(HttpServletRequest request ) throws Exception {

		ReturnParam rp          = new ReturnParam();
		Long                          tenantId    = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object>           sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String                        enterCd     = sessionData.get("enterCd").toString();


		rp.setSuccess("");

		List<Map<String, Object>> codeList = null;
		try {
			codeList = wtmAnnualMgrService.getCodeList(tenantId, enterCd);

			rp.put("codeList", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
		}

		return rp;
	}

	@RequestMapping(value="/getTaaCodelist2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getCodeList2(HttpServletRequest request ) throws Exception {

		ReturnParam rp          = new ReturnParam();
		Long                          tenantId    = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object>           sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String                        enterCd     = sessionData.get("enterCd").toString();


		rp.setSuccess("");

		List<Map<String, Object>> codeList = null;
		try {
			codeList = wtmAnnualMgrService.getCodeList2(tenantId, enterCd);

			rp.put("codeList", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
		}

		return rp;
	}

	@RequestMapping(value = "/taaTypeCd",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getTaaTypeCd(HttpServletRequest request, @RequestParam(value = "taaTypeCd", required = false)String taaTypeCd) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId")
		                                    .toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		ObjectMapper        mapper      = new ObjectMapper();
		String userId = sessionData.get("userId")
		                           .toString();
		String enterCd = sessionData.get("enterCd")
		                            .toString();

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");


		try {
			List<Map<String, Object>> cdList = wtmAnnualMgrService.getTaaTypeCd(tenantId, enterCd, taaTypeCd);
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
