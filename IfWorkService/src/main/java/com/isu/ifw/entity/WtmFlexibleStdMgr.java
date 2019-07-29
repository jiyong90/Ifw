package com.isu.ifw.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name="WTM_FLEXIBLE_STD_MGR")
public class WtmFlexibleStdMgr {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FLEXIBLE_STD_MGR_ID")
	private Long flexibleStdMgrId;
	@Column(name="TENANT_ID")
	private Long tenantId;
	@Column(name="ENTER_CD")
	private String enterCd;
	@Column(name="WORK_TYPE_CD")
	private String workTypeCd;
	@Column(name="FLEXIBLE_NM")
	private String flexibleNm;
	@Column(name="USE_SYMD")
	private String useSymd;
	@Column(name="TIME_CD_MGR_ID")
	private Long timeCdMgrId;
	@Column(name="USE_EYMD")
	private String useEymd;
	@Column(name="WORK_SHM")
	private String workShm;
	@Column(name="WORK_EHM")
	private String workEhm;
	@Column(name="CORE_SHM")
	private String coreShm;
	@Column(name="CORE_EHM")
	private String coreEhm;
	@Column(name="DEFAULT_WORK_MINUTE")
	private int defaultWorkMinute;
	@Column(name="UNIT_MINUTE")
	private int unitMinute;
	@Column(name="EXHAUSTION_YN")
	private String exhaustionYn;
	@Column(name="HOL_EXCEPT_YN")
	private String holExceptYn;
	
	@Column(name="WORK_DAYS_OPT")
	private String workDaysOpt;
	
	@Column(name="USED_TERM_OPT")
	private String usedTermOpt;
	@Column(name="APPL_TERM_OPT")
	private String applTermOpt;
	@Column(name="NOTE")
	private String note;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_DATE", columnDefinition="DATETIME") 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	@Column(name="UPDATE_ID")
	private Long updateId;
	
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

	public Long getTimeCdMgrId() {
		return timeCdMgrId;
	}

	public void setTimeCdMgrId(Long timeCdMgrId) {
		this.timeCdMgrId = timeCdMgrId;
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

	public int getDefaultWorkMinute() {
		return defaultWorkMinute;
	}

	public void setDefaultWorkMinute(int defaultWorkMinute) {
		this.defaultWorkMinute = defaultWorkMinute;
	}

	public int getUnitMinute() {
		return unitMinute;
	}

	public void setUnitMinute(int unitMinute) {
		this.unitMinute = unitMinute;
	}

	public String getExhaustionYn() {
		return exhaustionYn;
	}

	public void setExhaustionYn(String exhaustionYn) {
		this.exhaustionYn = exhaustionYn;
	}

	public String getHolExceptYn() {
		return holExceptYn;
	}

	public void setHolExceptYn(String holExceptYn) {
		this.holExceptYn = holExceptYn;
	}

	public Map<String, Boolean> getWorkDaysOpt() {
		Map<String, Boolean> resultMap = new HashMap<>();
		if(this.workDaysOpt != null && !this.workDaysOpt.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				resultMap = mapper.readValue(this.workDaysOpt, new HashMap<String, Boolean>().getClass());
			} catch (Exception e) {
				e.printStackTrace();
				resultMap = null;
			}
		}else {
			resultMap = null;
		}
		
		return resultMap;
	}

	public void setWorkDaysOpt(String workDaysOpt) {
		this.workDaysOpt = workDaysOpt;
	}

	public List<Map<String, Object>> getUsedTermOpt() {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if(this.usedTermOpt != null && !this.usedTermOpt.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				result = mapper.readValue(this.usedTermOpt, new ArrayList<Map<String, Boolean>>().getClass());
			} catch (Exception e) {
				e.printStackTrace();
				result = null;
			}
		}else {
			result = null;
		}
		
		return result;
		
	}

	public void setUsedTermOpt(String usedTermOpt) {
		this.usedTermOpt = usedTermOpt;
	}

	public String getApplTermOpt() {
		return applTermOpt;
	}

	public void setApplTermOpt(String applTermOpt) {
		this.applTermOpt = applTermOpt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	@PrePersist
    protected void onCreate() {
		this.updateDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
    	this.updateDate = new Date();
    }
	
	
	
}
