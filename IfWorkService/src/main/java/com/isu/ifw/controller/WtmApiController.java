package com.isu.ifw.controller;

import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isu.auth.control.TenantSecuredControl;
import com.isu.ifw.service.WtmFlexibleEmpService;
import com.isu.option.vo.ReturnParam;

@RestController
@RequestMapping(value="/api")
public class WtmApiController extends TenantSecuredControl {

	@Autowired
	@Qualifier(value="flexibleEmpService")
	private WtmFlexibleEmpService flexibleEmpService;

	@RequestMapping(value="/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReturnParam test(HttpServletRequest request ) throws Exception {
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		flexibleEmpService.createWorkteamEmpData(new Long("1"), "ISU", new Long("5"), "112313");	
		
		return rp;
	}
	
	@RequestMapping(value="/worktime/valid", method = RequestMethod.GET)
	public ReturnParam workValid(@RequestParam(required=true) String apiKey,
								@RequestParam(required=true) String secret,
								@RequestParam(required=true) String enterCd,
								@RequestParam(required=true) String sabun,
								@RequestParam(required=true) String sdate,
								@RequestParam(required=true) String edate,
								@RequestParam(required=false) Long applId,
			HttpServletRequest request ){
		ReturnParam rp = new ReturnParam();
		rp.setSuccess("");
		try {
			this.certificate(apiKey, secret, request.getRemoteHost());
		} catch (CertificateException e1) {
			rp.setFail(e1.getMessage());
			return rp;
		}
		
		String f = "yyyyMMddHHmm";
		if(sdate.length() != edate.length()) {
			rp.setFail("시작시분과 종료시분에 정보의 길이가 맞지 않습니다.");
			return rp;
		}
		
		if(sdate.length() == 12) {
			f = "yyyyMMddHHmm";
		}else if(sdate.length() == 14) {
			f = "yyyyMMddHHmmss";
		}else { 
			rp.setFail("일자 정보의 포맷은 yyyyMMddHHmm 또는 yyyyMMddHHmmss 입니다.");
			return rp;
		}
		Long tenantId = this.getTenantId(apiKey);
		SimpleDateFormat sdf = new SimpleDateFormat(f);
		Date sd, ed;
		try {
			sd = sdf.parse(sdate);
			ed = sdf.parse(edate);
		} catch (ParseException e) { 
			rp.setFail("일자 정보의 포맷은 yyyyMMddHHmm 또는 yyyyMMddHHmmss 입니다.");
			return rp;
		}
		
		if(sd.compareTo(ed) > 0) {
			rp.setFail("시작일자가 종료일자보다 큽니다.");
			return rp;
		}
		
		rp = flexibleEmpService.checkDuplicateWorktime(tenantId, enterCd, sabun, sd, ed, applId);
		
		return rp;
	}
	
	
}
