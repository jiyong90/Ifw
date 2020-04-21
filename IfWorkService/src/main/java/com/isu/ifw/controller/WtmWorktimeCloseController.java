package com.isu.ifw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.entity.WtmPropertie;
import com.isu.ifw.repository.WtmPropertieRepository;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.service.WtmWorktimeCloseService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/worktimeClose")
public class WtmWorktimeCloseController {
	
	@Autowired
	WtmWorktimeCloseService worktimeCloseService;
	
	@Autowired
	WtmInterfaceService wtmInterfaceService;
	

	@Autowired
	WtmPropertieRepository propertieRepo;
	
	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
		
	@RequestMapping(value="/save/confirm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setWorkTimeCloseConfirm(HttpServletRequest request, @RequestBody Map<String, Object> paramMap ) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");		
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		
		paramMap.put("tenantId", tenantId);
		paramMap.put("enterCd", enterCd);
		paramMap.put("userId", userId);				
		
		rp.setSuccess("");
		int cnt = 0;
		try {			
			cnt = worktimeCloseService.setWorktimeCloseConfirm(tenantId, enterCd, userId, paramMap);
			if(cnt > 0) {
				// 마감사용이 HR전송방식이면 인터페이스 해야한다.
				// 단 오라클 db사용회사는 마감프로시져에서 insert 연동하니깐 제외됨
				if(!"HSML".equals(enterCd)) {
					WtmPropertie propertie = propertieRepo.findByTenantIdAndEnterCdAndInfoKey(tenantId, enterCd, "OPTION_COMP_USED");
					if(propertie!=null && "HR".equalsIgnoreCase(propertie.getInfoValue())) {
						wtmInterfaceService.sendCompCnt((HashMap) paramMap);
					}
				}
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
		
	@RequestMapping(value = "/workTimeCloseIf",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam setWorkTimeCloseIf(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) throws Exception {
		ReturnParam rp = new ReturnParam();		
		// 사원정보
		try {
			Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
			Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
			String enterCd = sessionData.get("enterCd").toString();
			
			System.out.println("tenantId >>> "+tenantId);
			System.out.println("enterCd >>> "+enterCd);			
			System.out.println("paramMap >>> "+paramMap.toString());
			// paramMap >>> {worktimeCloseId=3, sYmd=20191230, eYmd=20200126}
			
			Long worktimeCloseId = Long.parseLong(paramMap.get("worktimeCloseId").toString());
			
			HashMap<String, Object> reqMap = new HashMap<>();			
			reqMap.put("tenantId", tenantId );
			reqMap.put("enterCd", enterCd );	
			reqMap.put("worktimeCloseId", worktimeCloseId);
			reqMap.put("sYmd", (String)paramMap.get("sYmd") );
			reqMap.put("eYmd", (String)paramMap.get("eYmd") );			
			reqMap.put("sabun", (String)paramMap.get("sabun") );
									
			//wtmInterfaceService.setCloseWorkIf(reqMap); //근무시간 마감생성 자바루프용 호출
			rp = wtmInterfaceService.setCloseWorkIfN(reqMap); //근무시간 마감생성 자바루프용 호출
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rp;
	}
		
	@RequestMapping(value="/dayList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getDayList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = worktimeCloseService.getDayList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/monList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getMonList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = worktimeCloseService.getMonList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	//2020-03-11
	@RequestMapping(value="/closeList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCloseList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = worktimeCloseService.getCloseList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam setWorktimeCloseList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		rp.setFail("저장 시 오류가 발생했습니다.");
		
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String userId = sessionData.get("userId").toString();
		
		Map<String, Object> convertMap = WtmUtil.requestInParamsMultiDML(request,paramMap.get("s_SAVENAME").toString(),"");
		convertMap.put("userId", userId);
		convertMap.put("enterCd", enterCd);
		convertMap.put("tenantId", tenantId);

		// MDC.put("convertMap", convertMap);
		
		rp.setSuccess("");
		int cnt = 0;
		try {
			cnt = worktimeCloseService.setWorktimeCloseList(tenantId, enterCd, userId, convertMap);
			if(cnt > 0) {
				rp.setSuccess("저장이 성공하였습니다.");
				return rp;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
	}
	
	@RequestMapping(value="/closeEmpList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCloseEmpList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		List<Map<String, Object>> codeList = null;
		try {
			codeList = worktimeCloseService.getCloseEmpList(tenantId, enterCd, paramMap);
			
			rp.put("DATA", codeList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/WorktimeCloseCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getWorktimeCloseCode(HttpServletRequest request, @RequestParam Map<String, Object> paramMap ) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();
		String userId = sessionData.get("userId").toString();
		/*
		MDC.put("sessionId", request.getSession().getId());
		MDC.put("logId", UUID.randomUUID().toString());
		MDC.put("type", "C");
		logger.debug("getTimeCdList Controller Start", MDC.get("sessionId"), MDC.get("logId"), MDC.get("type"));
		*/
		
		rp.setSuccess("");
		
		List<Map<String, Object>> WorktimeCloseList = null;
		try {		
			WorktimeCloseList = worktimeCloseService.getWorktimeCloseCode(tenantId, enterCd);
			
			rp.put("DATA", WorktimeCloseList);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
	@RequestMapping(value="/closeYn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getCloseYn(@RequestParam Map<String, Object> paramMap, HttpServletRequest request) throws Exception {
		
		ReturnParam rp = new ReturnParam();
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		
		rp.setSuccess("");
		
		Map<String, Object> resultMap = null;
		try {
			resultMap = worktimeCloseService.getCloseYn(tenantId, enterCd, paramMap);
			
			rp.put("DATA", resultMap);
		} catch(Exception e) {
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
