package com.isu.ifw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmFlexibleApply;
import com.isu.ifw.mapper.WtmFlexibleApplyMgrMapper;
import com.isu.ifw.repository.WtmFlexibleApplyRepository;
import com.isu.ifw.repository.WtmFlexibleEmpRepository;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.repository.WtmRuleRepository;
import com.isu.ifw.service.WtmApplService;
import com.isu.ifw.service.WtmAsyncService;
import com.isu.ifw.service.WtmFlexibleApplyMgrService;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;


@RestController
@RequestMapping(value="/flexibleApply")
public class WtmFlexibleApplyMgrController {
	
	@Autowired
	WtmFlexibleApplyMgrService flexibleApplyService;
	
	
	@Autowired
	WtmFlexibleApplyMgrMapper wtmFlexibleApplyMgrMapper;
	
	@Autowired
	@Qualifier("wtmFlexibleApplService")
	WtmApplService wtmApplService;
	
	@Autowired
	WtmPropertieRepository propertieRepo;
	
	@Autowired
	WtmRuleRepository ruleRepo;
	
	@Autowired
	WtmFlexibleApplyRepository flexibleApplyRepo;

	@Autowired
	WtmFlexibleEmpRepository flexibleEmpRepo;

	@Autowired
	WtmAsyncService wtmAsyncService;
	
	@Autowired
	@Qualifier("flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	private final Logger logger = LoggerFactory.getLogger("ifwFileLog");
	
	@RequestMapping(value="/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String sYmd = paramMap.get("sYmd").toString();
		
		logger.debug("[getApplyList] " + tenantId + enterCd + sYmd);
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyList(tenantId, enterCd, sYmd);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("[getApplyList rp] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApplyList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		logger.debug("[setApplyList] " + convertMap.toString());

		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = flexibleApplyService.setApplyList(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("[setApplyList rp] ", rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/getEymd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getEymd(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		logger.debug("[getEymd] " + tenantId +enterCd);

		rp.setSuccess("");
		
		Map<String, Object> codeMap = null;
		try {
			codeMap = flexibleApplyService.getEymd(paramMap);
			
			rp.put("DATA", codeMap);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("[getEymd] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/workType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getworkType(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		Long flexibleStdMgrId = Long.valueOf(paramMap.get("flexibleStdMgrId").toString());
		logger.debug("[getworkType] " + tenantId +enterCd+flexibleStdMgrId);

		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getworkTypeList(flexibleStdMgrId);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		logger.debug("[getworkType rp] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/apply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApply(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {
		logger.debug("[setApply] !!!!!!!!!!!!!!!!!!");
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("확정에 성공하였습니다.");
		try {
			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			String empNo = sessionData.get("empNo").toString();
			String userId = sessionData.get("userId").toString();
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("userId", userId);
			
			logger.debug("[setApply] " + paramMap.toString());
			
			String workTypeCd = paramMap.get("workTypeCd").toString();
			Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());

			WtmFlexibleApply wtmFlexibleApply = flexibleApplyRepo.findById(flexibleApplyId).get();


			if(wtmFlexibleApply.getApplyYn().equals(WtmApplService.WTM_FLEXIBLE_APPLY_I)){
				rp.setFail("확정 진행중입니다.");
				return rp;
			}

			if(wtmFlexibleApply.getApplyYn().equals(WtmApplService.WTM_FLEXIBLE_APPLY_C)){
				rp.setFail("취소 진행중입니다.");
				return rp;
			}


			// 확정대상자 조회
			List<Map<String, Object>> searchList =  wtmFlexibleApplyMgrMapper.getApplyConfirmList(paramMap);
			if(searchList==null || searchList.size()==0) {
				rp.setFail("확정 대상자가 없습니다.");
				return rp;
			}

			logger.debug("[setApply] 확정대상자 " + searchList.size());

			paramMap.put("symd", wtmFlexibleApply.getUseSymd());
			paramMap.put("eymd", wtmFlexibleApply.getUseEymd());
			paramMap.put("searchList", searchList);

			int existCnt = wtmFlexibleApplyMgrMapper.getExistCountBySymdAndEymd(paramMap);
			if(existCnt > 0) {
				rp.setFail("이미 등록된 확정건이 있습니다.");
				return rp;
			}

			//반복기준조회
			List<Map<String, Object>> ymdList = flexibleApplyService.getApplyYmdList(paramMap);

			wtmFlexibleApply.setApplyYn(WtmApplService.WTM_FLEXIBLE_APPLY_I);
			flexibleApplyRepo.save(wtmFlexibleApply);

			//동기로 확정처리
			//bithumbWtmflexibleApplyService.setApply(wtmFlexibleApply, searchList, ymdList);
			flexibleApplyService.setApply(wtmFlexibleApply, searchList, ymdList);
		} catch(Exception e) {
			rp.setFail("확정 시 오류가 발생했습니다.");
		}


		logger.debug("[setApply rp] " + rp.toString());
		return rp;
	}


	/**
	 * 확정취소
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cancle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setCancle(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {
		logger.debug("[setCancle] !!!!!!!!!!!!!!!!!!");
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("취소하였습니다.");
		try {
			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			String empNo = sessionData.get("empNo").toString();
			String userId = sessionData.get("userId").toString();

			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("userId", userId);

			logger.debug("[setCancle] " + paramMap.toString());

			Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());

			WtmFlexibleApply wtmFlexibleApply = flexibleApplyRepo.findById(flexibleApplyId).get();

			if(wtmFlexibleApply.getApplyYn().equals(WtmApplService.WTM_FLEXIBLE_APPLY_I)){
				rp.setFail("확정 진행중입니다.");
				return rp;
			}

			if(wtmFlexibleApply.getApplyYn().equals(WtmApplService.WTM_FLEXIBLE_APPLY_C)){
				rp.setFail("취소 진행중입니다.");
				return rp;
			}

			paramMap.put("symd", wtmFlexibleApply.getUseSymd());
			paramMap.put("eymd", wtmFlexibleApply.getUseEymd());

			// 확정취소 대상자 조회
			List<Map<String, Object>> searchList =  wtmFlexibleApplyMgrMapper.getApplyConfirmCancelList(paramMap);
			if(searchList==null || searchList.size()==0) {
				rp.setFail("확정취소 대상자가 없습니다.");
				return rp;
			}

			logger.debug("[setCancle] 확정취소 대상자 " + searchList.size());


			wtmFlexibleApply.setApplyYn(WtmApplService.WTM_FLEXIBLE_APPLY_C);
			flexibleApplyRepo.save(wtmFlexibleApply);


			List<Long> flexibleEmpIds = new ArrayList<Long>();

			searchList.stream().forEach(x -> flexibleEmpIds.add(Long.valueOf(x.get("flexibleEmpId").toString())));

			wtmAsyncService.cancelFlexibleEmpById(tenantId, enterCd, flexibleEmpIds, userId, wtmFlexibleApply);
		} catch(Exception e) {
			rp.setFail("취소 시 오류가 발생했습니다.");
		}


		logger.debug("[setCancle rp] " + rp.toString());
		return rp;
	}


	/**
	 * 확정취소(개인)
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cancle/personal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setCanclePersonal(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {
		logger.debug("[setCanclePersonal] !!!!!!!!!!!!!!!!!!");
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("취소하였습니다.");
		try {
			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			String empNo = sessionData.get("empNo").toString();
			String userId = sessionData.get("userId").toString();

			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("userId", userId);

			Long flexibleEmpId = Long.valueOf(paramMap.get("flexibleEmpId").toString());

			logger.debug("[setCanclePersonal] ", paramMap.toString());


			List<Long> flexibleEmpIds = new ArrayList<Long>();
			flexibleEmpIds.add(flexibleEmpId);

			wtmAsyncService.cacelFlexibleByEmpId(tenantId, enterCd, flexibleEmpIds, userId);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("취소 시 오류가 발생했습니다.");
		}


		logger.debug("[setCanclePersonal rp] " + rp.toString());
		return rp;
	}
	
	@RequestMapping(value="/grpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyGrpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyGrpList(paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/grpSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApplyGrpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());
		logger.debug("[setApplyGrpList] " + paramMap.toString());

		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = flexibleApplyService.setApplyGrpList(userId, flexibleApplyId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("[setApplyGrpList rp] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/empList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyEmpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = flexibleApplyService.getApplyEmpList(paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/empSave", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setApplyEmpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		Long flexibleApplyId = Long.valueOf(paramMap.get("flexibleApplyId").toString());
		logger.debug("[setApplyEmpList] " + paramMap.toString());

		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);
		convertMap.put("userId", userId);

		rp.setSuccess("");
		int cnt = 0;
		try {		
			cnt = flexibleApplyService.setApplyEmpList(userId, flexibleApplyId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		logger.debug("[setApplyEmpList rp] " + rp.toString());

		return rp;
	}
	
	@RequestMapping(value="/empPopuplist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getApplyEmpPopList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> searchList = null;
		try {
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			searchList = flexibleApplyService.getApplyEmpPopList(paramMap);
			
			rp.put("DATA", searchList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/elasDetail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Map<String, Object>> getElasDetail(@RequestParam Map<String, Object> paramMap
		    									, HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleApplyService.getElasDetail(tenantId, enterCd, paramMap, userId);
	}
	
	@RequestMapping(value="/elasDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam createElasPlan(@RequestBody Map<String, Object> paramMap
		    									, HttpServletRequest request) {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		
		return flexibleApplyService.createElasPlan(tenantId, enterCd, Long.valueOf(paramMap.get("flexibleApplyId").toString()), userId);
	}
}
