package com.isu.ifw.controller;

import com.isu.ifw.repository.WtmTaaCodeRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmTaaService;
import com.isu.ifw.vo.ReturnParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 출장/비상근무 신청
 */
@RequestMapping("/wtmRega")
@RestController
public class WtmRegaController {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	WtmTaaCodeRepository wtmTaaCodeRepo;

	@Qualifier("WtmRegaApplService")
	@Autowired
	WtmApplService regaApplService;

//	@Autowired
//	BithumbCommonService bithumbCommon;
	
	@Autowired
	WtmTaaService taaService;

	@Qualifier("WtmTaaApplService")
	@Autowired
	WtmApplService taaApplService;

	@Qualifier("wtmRegaCanService")
	@Autowired
	WtmApplService wtmRegaCanService;
	
	@Qualifier("WtmTaaCanApplService") @Autowired WtmApplService taaCanApplService;

	String groupwareLineUrl;

	/**
	 * 비상근무 코드 리스트 조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTaaCodelist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getCodeList(HttpServletRequest request) throws Exception {

		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();


		rp.setSuccess("");

		List<Map<String, Object>> codeList = null;
		try {
			codeList = taaService.getWtmTaaCodelist(tenantId, enterCd);

			rp.put("codeList", codeList);
		} catch (Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
		}

		return rp;
	}

	/**
	 * 비상근무 신청 , 신청후 그룹웨어 호출
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam save(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		Long applId = null;
		if(paramMap.get("applId")!=null && !"".equals(paramMap.get("applId"))) {
			applId = Long.valueOf(paramMap.get("applId").toString());
		}

		logger.debug("wtmRega save param : " + paramMap.toString());

		try {

			String workTypeCd = paramMap.get("taaTypeCd").toString();
			String note       = paramMap.get("note").toString();

			List<String> taaSdateArr = (List<String>) paramMap.get("taaSdate");
			List<String> taaEdateArr = (List<String>) paramMap.get("taaEdate");


			Map<String, Object> valiMap = new HashMap<String, Object>();
			valiMap.put("taaSdateArr", taaSdateArr);
			valiMap.put("taaEdateArr", taaEdateArr);
			valiMap.put("workTimeCode", workTypeCd);
			valiMap.put("note", note);
			valiMap.put("sabun", sabun);

			//  출장/긴급근무 저장
			rp = regaApplService.validate(tenantId, enterCd, sabun, WtmApplService.TIME_TYPE_REGA, valiMap);

			if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {

				rp = regaApplService.request(tenantId, enterCd, applId, WtmApplService.TIME_TYPE_REGA, valiMap, sabun, userId);

				if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {

					//  Save 인터페이스
//					bithumbCommon.saveWtmIfAppl(tenantId, enterCd, Long.parseLong(rp.get("applId")+""), WtmApplService.TIME_TYPE_REGA, WtmApplService.APPL_STATUS_IMSI, sabun, userId, Long.parseLong(rp.get("applId")+""));
					rp.setSuccess("저장이 성공하였습니다.");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rp;
	}

	/**
	 * 비상근무 코드 리스트 조회
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getTaaInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getTaaInfo(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws Exception {

		ReturnParam rp          = new ReturnParam();
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");

		Long   tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String enterCd  = sessionData.get("enterCd").toString();
		String sabun    = sessionData.get("empNo").toString();

		rp.setSuccess("");

		Long applId = Long.valueOf(paramMap.get("applId").toString());

		try {
			Map<String, Object> taaInfo =  taaApplService.getAppl(tenantId, enterCd, sabun, applId, sabun);

			rp.put("taaInfo", taaInfo);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
		}

		return rp;
	}

	/**
	 * 출장신청 취소
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancle",
	                method = RequestMethod.POST,
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam cancle(HttpServletRequest request,
	                          @RequestBody Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");

		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");

		Long   tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		String enterCd  = sessionData.get("enterCd").toString();
		String sabun    = sessionData.get("empNo").toString();
		String userId   = sessionData.get("userId").toString();

		logger.debug("WtmTaaController save param : " + paramMap.toString());


		try {

			Map<String, Object> valiMap = new HashMap<String, Object>();

			//Long   applId = Long.valueOf(paramMap.get("applId").toString());
			valiMap.put("note", "");
			valiMap.put("applCd",WtmApplService.TIME_TYPE_REGA_CAN);
			valiMap.put("applId", paramMap.get("applId"));

			//  imsi
			rp = taaCanApplService.imsi(tenantId, enterCd, null, null, valiMap,WtmApplService.APPL_STATUS_IMSI, sabun, userId);

			if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
				//  Save 인터페이스
//				bithumbCommon.saveWtmIfAppl(tenantId, enterCd, Long.parseLong(rp.get("applId")+""), WtmApplService.TIME_TYPE_REGA_CAN, WtmApplService.APPL_STATUS_IMSI, sabun, userId, Long.parseLong(paramMap.get("applId")+""));
				rp.setSuccess("저장이 성공하였습니다.");
				rp.put("groupwareUrl", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rp;
	}


	@RequestMapping(value="/canRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	ReturnParam regaCanApplRequest(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) {

		ReturnParam rp = new ReturnParam();
		rp.setFail("");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();

		Long taaApplId = null;
		if(paramMap.get("taaApplId")!=null && !"".equals(paramMap.get("taaApplId")))
			taaApplId = Long.valueOf(paramMap.get("taaApplId").toString());

		String status = null;
		if(paramMap.get("status")!=null && !"".equals(paramMap.get("status")))
			status = paramMap.get("status").toString();

		String workTypeCd = null;
		if(paramMap.get("workTypeCd")!=null && !"".equals(paramMap.get("workTypeCd")))
			workTypeCd = paramMap.get("workTypeCd").toString();

		try {
			//rp = otCanApplService.validate(tenantId, enterCd, sabun, workTypeCd, paramMap);
			//if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
			rp = wtmRegaCanService.request(tenantId, enterCd, taaApplId, workTypeCd, paramMap, sabun, userId);
			//}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}
		return rp;
	}
}
