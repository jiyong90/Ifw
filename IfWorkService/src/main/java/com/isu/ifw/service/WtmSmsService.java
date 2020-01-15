package com.isu.ifw.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isu.ifw.common.entity.CommManagementInfomation;
import com.isu.ifw.common.repository.CommManagementInfomationRepository;
import com.isu.ifw.util.WtmUtil;
import com.pb.msg.gabia.service.SmsService;
import com.pb.msg.vo.ReturnParam;

@Service
public class WtmSmsService {
	
	@Autowired
	private CommManagementInfomationRepository commManagementInfomationRepository;
	
	@Autowired
	private SmsService smsService;
	
	
	public ReturnParam sendSMS(Long tenantId, List<String> smsTarget, String from, String title, String smsContent) throws Exception {
		String[] toNumbers = null;
		if(smsTarget!=null)
			toNumbers = smsTarget.toArray(new String[smsTarget.size()]);
		
		String smsType = WtmUtil.byteCheck(smsContent, 60)? "sms" : "lms";
		Map<String, Object> etcMap = getSmsEtcMap(tenantId);
		etcMap.put("smsType", smsType);
		
		ReturnParam rp = smsService.sendMultiSms(toNumbers, from, title, smsContent, null, etcMap);
		
		return rp;
	}

	protected Map<String, Object> getSmsEtcMap(Long tenantId){
		List<CommManagementInfomation> infos = commManagementInfomationRepository.findByTenantIdAndInfoKeyLike(tenantId, "MSG.SMS.GABIA.%");

		Map<String, Object> etcMap = new HashMap<String, Object>();
		 
		if(infos != null && infos.size() > 0){
			for(CommManagementInfomation info : infos){
				if(info.getInfoKey().equals("MSG.SMS.GABIA.API_ID")){
					etcMap.put("apiId", info.getInfoData());
				}else if(info.getInfoKey().equals("MSG.SMS.GABIA.API_KEY")){
					etcMap.put("apiKey", info.getInfoData());
				}
			}
		}
		 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		etcMap.put("uniqueKey", sdf.format(new Date()));
		return etcMap;
	}
	
}
