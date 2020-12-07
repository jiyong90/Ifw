package com.isu.ifw.controller;

import com.isu.ifw.service.*;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmAnnualCreateVo;
import org.apache.camel.json.simple.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 연차사용 컨트롤러
 */
@RequestMapping("/wtmAnnualUsed")
@RestController
public class WtmAnnualUsedController {

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	WtmAnnualMgrService wtmAnnualMgrService;

	@Autowired
	WtmAnnualCreateService annualCreateService;

	@Autowired
	WtmAnnualUsedService annualUsedService;

	@Autowired
	WtmCalcServiceImpl calcService;

	@Qualifier("WtmTaaApplService")
	@Autowired
	WtmApplService taaApplService;

	//  출장/연차 신청 취소
	@Autowired
	@Qualifier("WtmTaaCanApplService")
	WtmTaaCanServiceImpl wtmTaaCanApplService;

	private String groupwareLineUrl;


	/**
	 * 휴가신청
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam save(HttpServletRequest request, @RequestBody JsonObject paramMap) throws Exception {
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

		logger.debug("wtmAnnualUsed save param : " + paramMap.toString());

		try {
			List<String> symdArr          = (ArrayList<String>) paramMap.get("symd");
			List<String> eymdArr          = (ArrayList<String>) paramMap.get("eymd");
			List<String> requestTypeCdArr = (List<String>) paramMap.get("requestTypeCd");
			List<String> taaTypeCdArr     = (List<String>) paramMap.get("annualTaaDetailTypeCd");


			String annualTaCd = paramMap.get("annualTaCd").toString();
			String note       = paramMap.get("note").toString();

			String annualTotalCnt = "";
			String annualUsedCnt = "";
			String annualCreateCnt = "";
			String annualNotUsedCnt = "";
			if( paramMap.containsKey("annualTotalCnt") && paramMap.get("annualTotalCnt") != null )
				annualTotalCnt = paramMap.get("annualTotalCnt").toString();

			if( paramMap.containsKey("annualUsedCnt") && paramMap.get("annualUsedCnt") != null )
				annualUsedCnt = paramMap.get("annualUsedCnt").toString();

			if( paramMap.containsKey("annualCreateCnt") && paramMap.get("annualCreateCnt") != null )
				annualCreateCnt = paramMap.get("annualCreateCnt").toString();

			if( paramMap.containsKey("annualNotUsedCnt") && paramMap.get("annualNotUsedCnt") != null )
				annualNotUsedCnt = paramMap.get("annualNotUsedCnt").toString();

			Map<String, Object> etcMap = new HashMap<String, Object>();
			etcMap.put("annualTotalCnt", annualTotalCnt);
			etcMap.put("annualUsedCnt", annualUsedCnt);
			etcMap.put("annualCreateCnt", annualCreateCnt);
			etcMap.put("annualNotUsedCnt", annualNotUsedCnt);
			etcMap.put("annualTaCd", annualTaCd);

			Map<String, Object> valiMap = new HashMap<String, Object>();
			valiMap.put("startYmdArr", symdArr);
			valiMap.put("endYmdArr", eymdArr);
			valiMap.put("requestTypeCdArr", requestTypeCdArr);
			valiMap.put("taaTypeCdArr", taaTypeCdArr);
			valiMap.put("startHm", ""); //  validation 기본 키값
			valiMap.put("endHm", "");   //  validation 기본 키값
			valiMap.put("note", note);

			rp = taaApplService.validate(tenantId, enterCd, sabun, WtmApplService.TIME_TYPE_ANNUAL, valiMap);

			if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {
				rp = taaApplService.request(tenantId, enterCd, applId, WtmApplService.TIME_TYPE_TAA, valiMap, sabun, userId);

			}

		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		}

		return rp;
	}

	/**
	 * 휴가기간 계산
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/myAnnualInfo",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam getMyAnnualInfo(HttpServletRequest request
			,@RequestBody Map<String, Object> paramMap
	) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId")
				.toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sabun   = sessionData.get("empNo").toString();
		String userId  = sessionData.get("userId").toString();

		String annualTaCd = paramMap.get("annualTaCd").toString();


		List<String> symdArr = (List<String>) paramMap.get("annualCreateSDate");
		List<String> eymdArr = (List<String>) paramMap.get("annualCreateEDate");
		List<String> requestTypeCdArr = (List<String>) paramMap.get("requestTypeCd");



		try {

			Double rpTotalCnt       = 0.0d;
			Double rpCreateCnt      = 0.0d;
			Double rpUsedCnt        = 0.0d;
			Double rpNotUsedCnt     = 0.0d;
			Double rpAlreadyUsedCnt     = 0.0d;


			for (int i = 0; i < symdArr.size(); i++) {

				String symd = symdArr.get(i);
				String eymd = eymdArr.get(i);
				String requestCd = requestTypeCdArr.get(i);

				String yy = symd.split("-")[0];

				symd = symd.replaceAll("-", "");
				eymd = eymd.replaceAll("-", "");

				//  계산 해야함. totDays, holDays
				Map<String, Integer> calMap = calcService.calcDayCnt(tenantId, enterCd, symd, eymd);
				logger.debug("calMap.toString() : " + calMap.toString());

				//  발생일수, 사용일수
				WtmAnnualCreateVo annualInfo = annualUsedService.getMyAnnualInfo(tenantId, enterCd, userId, sabun, yy, symd, eymd, annualTaCd);
				rpCreateCnt = annualInfo.getCreateCnt().doubleValue();

				Double totalCnt         = calMap.get("totDays").doubleValue();
				Double usedCnt          = totalCnt - calMap.get("holDays").doubleValue();
				rpAlreadyUsedCnt = annualInfo.getUsedCnt().doubleValue();

				switch (requestCd) {
					case "A":
					case "P":

						totalCnt = 0.5;

						if (usedCnt != 0) {
							usedCnt   = 0.5;

						}

				}

				System.out.println("totalCnt    :" + totalCnt);
				System.out.println("usedCnt     :" + usedCnt + "\n");

				if (!annualTaCd.equals("10") && !annualTaCd.equals("70")){
					usedCnt = 0.0d;
				}
				rpTotalCnt += totalCnt;
				rpUsedCnt += usedCnt;

				System.out.println("rpTotalCnt  :" + rpTotalCnt);
				System.out.println("rpUsedCnt   :" + rpUsedCnt);
				System.out.println("rpCreateCnt :" + rpCreateCnt);
				System.out.println("rpNotUsedCnt:" + rpNotUsedCnt);

			}


			rpNotUsedCnt = rpCreateCnt - rpAlreadyUsedCnt - rpUsedCnt;

			rp.put("totalCnt", rpTotalCnt);
			rp.put("usedCnt", rpUsedCnt);
			rp.put("createCnt", rpCreateCnt);
			rp.put("notUsedCnt", rpNotUsedCnt);
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
		}

		return rp;
	}


	/**
	 * 휴가신청 취소
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

		logger.debug("wtmAnnualUsed save param : " + paramMap.toString());


		try {

			Map<String, Object> valiMap = new HashMap<String, Object>();

			Long   applId = Long.valueOf(paramMap.get("applId").toString());
//			String note = paramMap.get("note").toString();
			valiMap.put("note", "");
			valiMap.put("applCd",WtmApplService.TIME_TYPE_TAA_CAN);
			valiMap.put("applId",applId);



			//  imsi
			rp = wtmTaaCanApplService.imsi(tenantId, enterCd, (long) 0, null, valiMap,WtmApplService.APPL_STATUS_IMSI, sabun, userId);

			if(rp!=null && rp.getStatus()!=null && "OK".equals(rp.getStatus())) {

				//  Save 인터페이스
//				Common.saveWtmIfAppl(tenantId, enterCd, Long.parseLong(rp.get("applId")+""), WtmApplService.TIME_TYPE_TAA_CAN, WtmApplService.APPL_STATUS_IMSI, sabun, userId, Long.parseLong(rp.get("applId")+""));

				rp.setSuccess("저장이 성공하였습니다.");

//				WtmEmpHis empHis = Common.getEmpHis(tenantId, enterCd, sabun);

				//  부서id:사용자id:yyyymmddhhMMss:연동키:폼아이디
				String groupwareEncData = "";
//						empHis.getOrgCd() + ":"
//								+ sabun + ":"
//								+ WtmUtil.parseDateStr(new Date(), "yyyyMMddHHmmss") + ":"
//								+ rp.get("applId").toString() + ":"
//								+ GroupwareInterface.FORM_ID_ANNUAL_CANCLE
//						;

//				rp.put("groupwareUrl", groupwareLineUrl + GroupwareAES.encryptAES(groupwareEncData));
				rp.put("groupwareUrl", "");


			}



		} catch (Exception e) {
			e.printStackTrace();
		}

		return rp;
	}


	/**
	 * 휴가발생일수 조회
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/myAnnualCreateCnt",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam myAnnualCreateCnt(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws Exception {

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");

		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());

		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String              enterCd     = sessionData.get("enterCd").toString();
		String              sabun       = sessionData.get("empNo").toString();
		String              userId      = sessionData.get("userId").toString();

		String annualTaCd = paramMap.get("taCd").toString();


		try {

			String yy = WtmUtil.parseDateStr(new Date(), "yyyy");
			//  발생일수
			WtmAnnualCreateVo annualInfo = annualUsedService.getMyCreatCnt(tenantId, enterCd, sabun, yy, annualTaCd);
			Double totalCnt = 0.0d;
			Double usedCnt = 0.0d;

			if (annualInfo != null) {
				totalCnt = annualInfo.getCreateCnt().doubleValue();
			}

			WtmAnnualCreateVo usedInfo = annualCreateService.getByUserId(tenantId, enterCd, yy, sabun, annualTaCd);

			if (usedInfo != null) {
				usedCnt = usedInfo.getUsedCnt();
			}

			rp.put("totalCnt", totalCnt);
			rp.put("usedCnt", usedCnt);
			rp.put("noUsedCnt", totalCnt - usedCnt);
		} catch (Exception e) {
			e.printStackTrace();
			rp.setFail("휴가 발생일수가 존재하지 않습니다");
		}

		return rp;
	}
}
