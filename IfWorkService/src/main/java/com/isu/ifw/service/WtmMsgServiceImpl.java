package com.isu.ifw.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isu.ifw.common.service.TenantConfigManagerService;
import com.isu.ifw.entity.WtmEmpAddr;
import com.isu.ifw.entity.WtmEmpHis;
import com.isu.ifw.entity.WtmOtp;
import com.isu.ifw.entity.WtmPushMgr;
import com.isu.ifw.entity.WtmPushSendHis;
import com.isu.ifw.mapper.WtmEmpAddrMapper;
import com.isu.ifw.repository.WtmEmpAddrRepository;
import com.isu.ifw.repository.WtmEmpHisRepository;
import com.isu.ifw.repository.WtmOtpRepository;
import com.isu.ifw.repository.WtmPushMgrRepository;
import com.isu.ifw.repository.WtmPushSendHisRepository;
import com.isu.ifw.util.WtmUtil;
import com.isu.ifw.vo.ReturnParam;
import com.isu.ifw.vo.WtmMessageVO;

@Service
public class WtmMsgServiceImpl implements WtmMsgService {

	private final Logger logger = LoggerFactory.getLogger("ifwDBLog");
	
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
	
	@Autowired
	WtmEmpHisRepository empHisRepo;
	
	//@Autowired
	//WtmHansungMailService hansungMailService;
	
	@Autowired
	WtmEmpAddrRepository empAddrRepository;
	
	@Override
	public ReturnParam sendMsg(Long tenantId, String enterCd, WtmMessageVO msgVO) {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		
		String from = msgVO.getFrom();
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
					if(from==null || "".equals(from))
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
					if(from==null || "".equals(from))
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
			String sabun = null;
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
				
				sabun = empAddr.get("sabun").toString();
				
				targets.add(empMap);
			} else {
				WtmEmpAddr empAddr = empAddrRepo.findByTenantIdAndEnterCdAndEmail(tenantId, enterCd, userInfo);
				
				empAddrId = empAddr.getEmpAddrId();
				
				Map<String, Object> empMap = new HashMap<String, Object>();
				empMap.put("sabun", empAddr.getSabun());
				empMap.put("email", empAddr.getEmail());
				
				sabun = empAddr.getSabun();
				
				targets.add(empMap);
			}
			
			WtmEmpHis emp = empHisRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, sabun, WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
			if(emp==null) {
				rp.setFail("사업장 정보가 없습니다.");
				return rp;
			}
			
			WtmPushMgr pushMgr = pushMgrRepo.findByTenantIdAndEnterCdAndBusinessPlaceCdAndYmdBetweenAndPushObjAndStdType(tenantId, enterCd, emp.getBusinessPlaceCd(), WtmUtil.parseDateStr(new Date(), "yyyyMMdd"), "COMM", "PW");
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
	
	@Transactional
	@Override
	public ReturnParam sendMailForAppl(Long tenantId, String enterCd, String fromSabun, List<String> toSabuns, String applCode, String type) {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> sendMailForAppl start");
		logger.debug(">>>>>>>>>>>>>>>>>>>> sendMailForAppl start");
		
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("메일을 전송하였습니다.");
		
		WtmEmpHis emp = empHisRepo.findByTenantIdAndEnterCdAndSabunAndYmd(tenantId, enterCd, fromSabun, WtmUtil.parseDateStr(new Date(), "yyyyMMdd"));
		if(emp==null) {
			rp.setMessage("사업장 정보가 없습니다.");
			return rp;
		}
		
		System.out.println("businessPlaceCd : " + emp.getBusinessPlaceCd());
		logger.debug("businessPlaceCd : " + emp.getBusinessPlaceCd());
	
		WtmPushMgr pushMgr = pushMgrRepo.findByTenantIdAndEnterCdAndBusinessPlaceCdAndYmdBetweenAndPushObjAndStdType(tenantId, enterCd, emp.getBusinessPlaceCd(), WtmUtil.parseDateStr(new Date(), "yyyyMMdd"), "COMM", applCode+"_"+type);
		if(pushMgr==null) {
			rp.setMessage("메일 전송 기준이 없습니다.");
			return rp;
		}
		
		System.out.println("emailYn : " + pushMgr.getEmailYn());
		logger.debug("emailYn : " + pushMgr.getEmailYn());
		
		if(pushMgr.getEmailYn()!=null && "Y".equals(pushMgr.getEmailYn())) {
			//회사마다 메일 전송 방식 다름
			//hansungMailService.sendMail(tenantId, enterCd, fromSabun, toSabuns, pushMgr.getTitle(), pushMgr.getPushMsg(), applCode, "APP");
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> sendMailForAppl end");
		logger.debug(">>>>>>>>>>>>>>>>>>>> sendMailForAppl end");
			
		return rp;
	}
	
	
}
