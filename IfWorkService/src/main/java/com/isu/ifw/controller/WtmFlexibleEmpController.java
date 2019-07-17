package com.isu.ifw.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.ifw.vo.WtmFlexibleEmpVO;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/flexibleEmp")
public class WtmFlexibleEmpController {
	
	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;
	
	/**
	 * 선택한 기간의 근무제 정보 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getFlexibleEmp(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		WtmFlexibleEmpVO flexibleInfo = null;
		
		try {
			flexibleInfo = flexibleEmpService.getFlexibleEmp(tenantId, enterCd, empNo, paramMap);
			rp.put("flexibleInfo", flexibleInfo);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}

	/**
	 * 이전에 시행한 근무제 기간 조회
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/prev",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ReturnParam getPrevFlexibleEmp(@RequestParam Map<String, Object> paramMap
													    , HttpServletRequest request) throws Exception {
		Long tenantId = Long.valueOf(request.getAttribute("tenantId").toString());
		Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
		String enterCd = sessionData.get("enterCd").toString();
		String empNo = sessionData.get("empNo").toString();

		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> prevFlexible = null;
		
		try {
			prevFlexible = flexibleEmpService.getPrevFlexible(tenantId, enterCd, empNo);
			rp.put("prevFlexible", prevFlexible);
		} catch(Exception e) {
			e.printStackTrace();
			rp.setFail("조회 시 오류가 발생했습니다.");
			return rp;
		}
		
		return rp;
	}
	
}
