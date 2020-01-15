package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmEmpAddr;
import com.isu.ifw.entity.WtmOtp;
import com.isu.ifw.entity.WtmPushMgr;
import com.isu.ifw.entity.WtmPushSendHis;
import com.isu.ifw.mapper.WtmEmpAddrMapper;
import com.isu.ifw.repository.WtmEmpAddrRepository;
import com.isu.ifw.repository.WtmOtpRepository;
import com.isu.ifw.repository.WtmPushMgrRepository;
import com.isu.ifw.repository.WtmPushSendHisRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmMessageVO;

@Service
public class WtmMsgServiceImpl implements WtmMsgService {

	@Autowired
	@Qualifier("WtmTenantConfigManagerService")
	TenantConfigManagerService tcms;
	
	@Autowired
	WtmSmsService smsService;
	
	@Autowired	
	WtmPushSendHisRepository pushHisRepository;
	
	@Autowired
	WtmEmpAddrMapper empAddrMapper;
	
	@Autowired
	WtmEmpAddrRepository empAddrRepo;
	
	@Autowired
	WtmOtpRepository otpRepo;
	
	@Autowired
	WtmPushMgrRepository pushMgrRepo;
	
	@Override
	public ReturnParam sendMsg(Long tenantId, String enterCd, WtmMessageVO msgVO) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		String from = "";
		String title = msgVO.getTitle();
		String contents = msgVO.getContent();
		List<Map<String, Object>> targets = msgVO.getTargets();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("targets:: " + mapper.writeValueAsString(targets));
			
			if(targets!=null && targets.size()>0) {
				List<WtmPushSendHis> histories = new ArrayList<WtmPushSendHis>();
	
				if(msgVO.isMobileYn()) {
					
					
				}
				
				if(msgVO.isMailYn()) {
					from = tcms.getConfigValue(tenantId, "WTMS.API.MAIL_MANAGER", true, "");
					
					for(Map<String, Object> target : targets) {
						WtmPushSendHis pushSendHis = new WtmPushSendHis();
						pushSendHis.setEnterCd(enterCd);
						pushSendHis.setTenantId(tenantId);
						pushSendHis.setStdType(msgVO.getStdType());
						pushSendHis.setSendType("MAIL");
						pushSendHis.setReceiveSabun(target.get("sabun").toString());
						pushSendHis.setReceiveMail(target.get("email").toString());
						pushSendHis.setSendMsg(contents);
						pushSendHis.setUpdateId(target.get("sabun").toString());
						histories.add(pushSendHis);
					}
				}
				
				if(msgVO.isSmsYn()) {
					from = tcms.getConfigValue(tenantId, "WTMS.API.SMS_MANAGER", true, "");
					
					List<String> smsTarget = new ArrayList<String>();
					for(Map<String, Object> target : targets) {
						WtmPushSendHis pushSendHis = new WtmPushSendHis();
						pushSendHis.setEnterCd(enterCd);
						pushSendHis.setTenantId(tenantId);
						pushSendHis.setStdType(msgVO.getStdType());
						pushSendHis.setSendType("SMS");
						pushSendHis.setReceiveSabun(target.get("sabun").toString());
						pushSendHis.setReceiveMail(target.get("phone").toString());
						pushSendHis.setSendMsg(contents);
						pushSendHis.setUpdateId(target.get("sabun").toString());
						histories.add(pushSendHis);
						
						smsTarget.add(target.get("phone").toString());
					}
					
					com.pb.msg.vo.ReturnParam msgResult = smsService.sendSMS(tenantId, smsTarget, from, title, contents);
					
					System.out.println("status: " + msgResult.getStatus());
					System.out.println("message: " + msgResult.get("message").toString());
					
					rp.setStatus(msgResult.getStatus());
					if(msgResult.get("message")!=null)
						rp.setMessage(msgResult.get("message").toString());
					
					if(!"FAIL".equals(rp.getStatus())) {
						pushHisRepository.saveAll(histories);
					}
					
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rp;
		
	}
	
	@Transactional
	@Override
	public ReturnParam sendCertificateCodeForChangePw(Long tenantId, String enterCd, String userInfo) {
		//send email
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("인증코드를 전송하였습니다.");
		
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MINUTE, 3);

		try {
			Long empAddrId = null;
			
			List<Map<String, Object>> targets = new ArrayList<Map<String, Object>>();
			String passwordCertificate = tcms.getConfigValue(tenantId, "WTMS.LOGIN.PASSWORD_CERTIFICATE", true, "");
			if("PHONE".equalsIgnoreCase(passwordCertificate)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("tenantId", tenantId);
				paramMap.put("enterCd", enterCd);
				paramMap.put("handPhone", userInfo);
				Map<String, Object> empAddr = empAddrMapper.findByTenantIdAndEnterCdAndHandPhone(paramMap);
				
				empAddrId = Long.valueOf(empAddr.get("empAddrId").toString());
				
				Map<String, Object> empMap = new HashMap<String, Object>();
				empMap.put("sabun", empAddr.get("sabun").toString());
				empMap.put("phone", empAddr.get("handPhone").toString());
				
				targets.add(empMap);
			} else {
				WtmEmpAddr empAddr = empAddrRepo.findByTenantIdAndEnterCdAndEmail(tenantId, enterCd, userInfo);
				
				empAddrId = empAddr.getEmpAddrId();
				
				Map<String, Object> empMap = new HashMap<String, Object>();
				empMap.put("sabun", empAddr.getSabun());
				empMap.put("email", empAddr.getEmail());
				
				targets.add(empMap);
			}
			
			WtmPushMgr pushMgr = pushMgrRepo.findByTenantIdAndEnterCdAndYmdBetweenAndPushObjAndStdType(tenantId, enterCd, WtmUtil.parseDateStr(new Date(), "yyyyMMdd"), "COMM", "PW");
			//String otpCode = UUID.randomUUID().toString();
			
			StringBuffer otpCode = new StringBuffer();
			Random rnd = new Random();
			for (int i = 0; i <6 ; i++) {
				int rIndex = rnd.nextInt(2);
				
				switch (rIndex) {
			    case 0:
			    	otpCode.append((char) ((int) (rnd.nextInt(26)) + 97));
			    case 1:
			    	otpCode.append((char) ((int) (rnd.nextInt(26)) + 65));
				}
			}
			
			if(pushMgr!=null) {
				String pushMsg = pushMgr.getPushMsg();
				if(pushMsg.contains("[[OTP]]")) {
					pushMsg = pushMsg.replace("[[OTP]]", otpCode);
				}
				
				WtmMessageVO msgVO = new WtmMessageVO();
				msgVO.setTenantId(tenantId);
				msgVO.setEnterCd(enterCd);
				msgVO.setStdType(pushMgr.getStdType());
				msgVO.setTargets(targets);
				msgVO.setTitle(pushMgr.getTitle()!=null && !"".equals(pushMgr.getTitle())?pushMgr.getTitle():"인증번호전송");
				msgVO.setContent(pushMsg);
				msgVO.setMobileYn("Y".equals(pushMgr.getMobileYn())?true:false);
				msgVO.setSmsYn("Y".equals(pushMgr.getSmsYn())?true:false);
				msgVO.setMailYn("Y".equals(pushMgr.getEmailYn())?true:false);
				
				rp = sendMsg(tenantId, enterCd, msgVO);
				
				if(!"FAIL".equals(rp.getStatus())) {
					WtmOtp otp = new WtmOtp();
					otp.setOtp(otpCode.toString());
					otp.setExpireDate(date.getTime());
					otp.setResourceId(empAddrId);
					otpRepo.save(otp);
				} 
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rp.setFail("인증코드 전송에 실패하였습니다.");
		}
		
		return rp;
		
	}
}
