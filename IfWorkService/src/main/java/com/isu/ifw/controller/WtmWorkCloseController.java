package com.isu.ifw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.common.entity.CommTenantModule;
import com.isu.ifw.common.repository.CommTenantModuleRepository;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.service.WtmAsyncService;

@RestController
@RequestMapping(value="/close")
public class WtmWorkCloseController {
	
	@Autowired
	private TenantConfigManagerService tcms;
	
	@Autowired
	WtmAsyncService wymAsyncService;
	
	@Autowired
	CommTenantModuleRepository commTenantModulRepo;

	/**
	 * 일 마감(정오)
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping (value="/day/{tenantKey}", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public void dayCloseNoon( @PathVariable String tenantKey
			                , @RequestBody Map<String,Object> paramMap
							, HttpServletRequest request)throws Exception{
		
		try {
			CommTenantModule commTenantModule = commTenantModulRepo.findByModuleIdAndtenantKey(1L, tenantKey);
			if(commTenantModule!=null && commTenantModule.getTenantId()!=null) {
				Long tenantId = commTenantModule.getTenantId();
				
				String infoData = tcms.getConfigValue(tenantId, "WTMS.LOGIN.COMPANY_LIST", true, "");
				if(!"".equals(infoData)) {
					ObjectMapper mapper = new ObjectMapper();
					
					List<Map<String, Object>> companyList = mapper.readValue(infoData, new ArrayList<Map<String, Object>>().getClass());
					if(companyList!=null && companyList.size()>0) {
						for(Map<String, Object> company : companyList) {
							for(String enterCd : company.keySet()) {
								if(enterCd.equals("ISU"))
									wymAsyncService.workdayClose(tenantId, enterCd, "99999");
							}
						}
					}
					
				}
				
			}
		}catch(Exception e) {
			System.out.println(">>> CALL DAY CLOSE ERROR");
			e.printStackTrace();
		}
		
	}
	
}
