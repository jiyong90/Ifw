package com.isu.ifw.vo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WtmFlexibleStdVO {

	private Long flexibleStdMgrId;
	private Long tenantId;
	private String enterCd;
	private String workTypeCd;
	private String workTypeNm;
	private String flexibleNm;
	private String useSymd;
	private String useEymd;
	private String workShm;
	private String workEhm;
	private String coreShm;
	private String coreEhm;
	private String defaultWorkMinute;
	private String unitMinute;
	private String exhaystionYn;
	private String holExceptYn;
	private Map<String, Object> workDaysOpt;
	private String applShowYn;
	public Long getFlexibleStdMgrId() {
		return flexibleStdMgrId;
	}
	public void setFlexibleStdMgrId(Long flexibleStdMgrId) {
		this.flexibleStdMgrId = flexibleStdMgrId;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getEnterCd() {
		return enterCd;
	}
	public void setEnterCd(String enterCd) {
		this.enterCd = enterCd;
	}
	public String getWorkTypeCd() {
		return workTypeCd;
	}
	public void setWorkTypeCd(String workTypeCd) {
		this.workTypeCd = workTypeCd;
	}
	public String getWorkTypeNm() {
		return workTypeNm;
	}
	public void setWorkTypeNm(String workTypeNm) {
		this.workTypeNm = workTypeNm;
	}
	public String getFlexibleNm() {
		return flexibleNm;
	}
	public void setFlexibleNm(String flexibleNm) {
		this.flexibleNm = flexibleNm;
	}
	public String getUseSymd() {
		return useSymd;
	}
	public void setUseSymd(String useSymd) {
		this.useSymd = useSymd;
	}
	public String getUseEymd() {
		return useEymd;
	}
	public void setUseEymd(String useEymd) {
		this.useEymd = useEymd;
	}
	public String getWorkShm() {
		return workShm;
	}
	public void setWorkShm(String workShm) {
		this.workShm = workShm;
	}
	public String getWorkEhm() {
		return workEhm;
	}
	public void setWorkEhm(String workEhm) {
		this.workEhm = workEhm;
	}
	public String getCoreShm() {
		return coreShm;
	}
	public void setCoreShm(String coreShm) {
		this.coreShm = coreShm;
	}
	public String getCoreEhm() {
		return coreEhm;
	}
	public void setCoreEhm(String coreEhm) {
		this.coreEhm = coreEhm;
	}
	public String getDefaultWorkMinute() {
		return defaultWorkMinute;
	}
	public void setDefaultWorkMinute(String defaultWorkMinute) {
		this.defaultWorkMinute = defaultWorkMinute;
	}
	public String getUnitMinute() {
		return unitMinute;
	}
	public void setUnitMinute(String unitMinute) {
		this.unitMinute = unitMinute;
	}
	public String getExhaystionYn() {
		return exhaystionYn;
	}
	public void setExhaystionYn(String exhaystionYn) {
		this.exhaystionYn = exhaystionYn;
	}
	public String getHolExceptYn() {
		return holExceptYn;
	}
	public void setHolExceptYn(String holExceptYn) {
		this.holExceptYn = holExceptYn;
	}
	public Map<String, Object> getWorkDaysOpt() {
		return workDaysOpt;
	}
	public void setWorkDaysOpt(String workDaysOpt) {
		
		if(workDaysOpt.equals("") || workDaysOpt == null) {
			this.workDaysOpt = null;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				this.workDaysOpt = mapper.readValue(workDaysOpt, new HashMap<String, Object>().getClass());
			} catch (Exception e) {
				e.printStackTrace();
				this.workDaysOpt = null;
			}
		}
	}
	public String getApplShowYn() {
		return applShowYn;
	}
	public void setApplShowYn(String applShowYn) {
		this.applShowYn = applShowYn;
	}
	
	
	
	
}
