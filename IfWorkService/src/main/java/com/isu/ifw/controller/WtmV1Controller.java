package com.isu.ifw.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.mapper.WtmEmpHisMapper;
import com.isu.ifw.mapper.WtmInoutHisMapper;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.service.WtmInoutService;
import com.isu.ifw.service.WtmInterfaceService;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;

@RestController
@RequestMapping(value="/v1")
public class WtmV1Controller{

	private static final Logger logger = LoggerFactory.getLogger("ifwFileLog");

	@Autowired
	@Qualifier("WtmTenantModuleRepository")
	CommTenantModuleRepository tenantModuleRepo;
	
	@Autowired
	WtmEmpHisMapper empHisMapper;
	
	//조직장
	@RequestMapping(value = "/{tsId}/isLeader", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody ReturnParam getLeaderYn(@PathVariable String tsId, HttpServletRequest request) throws Exception {		
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		Map<String, Object> paramMap = new HashMap();
		Long tenantId=null;
		
		try {
 
			CommTenantModule tm = null;
			tm = tenantModuleRepo.findByTenantKey(tsId);
			
			if(tm == null) {
				rp.setFail("잘못된 호출 url입니다.");
				return rp;
			}
			tenantId = tm.getTenantId();
			
			String enterCd = request.getParameter("enterCd").toString();
			String sabun = request.getParameter("sabun").toString();
			
			paramMap.put("tenantId", tenantId);
			paramMap.put("enterCd", enterCd);
			paramMap.put("sabun", sabun);
			Map<String, Object> data = empHisMapper.getLeaderYn(paramMap);
			rp.put("result", data);
		}catch(Exception e) {
			e.printStackTrace();
			rp.setFail(e.getMessage());
		} 
		return rp;
	}
}
