package com.isu.ifw.entity;

import java.util.Date;

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

@Entity
@Table(name="WTM_APPL_CODE")
public class WtmApplCode {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="APPL_CODE_ID")
	private Long applCodeId;
	@Column(name="TENANT_ID")
	private Long tenantId;
	@Column(name="ENTER_CD")
	private String enterCd;
	@Column(name="APPL_CD")
	private String applCd;
	@Column(name="APPL_NM")
	private String applNm;
	@Column(name="APPL_LEVEL_CD")
	private String applLevelCd;
	@Column(name="REC_LEVEL_CD")
	private String recLevelCd;
	@Column(name="NOTE")
	private String note;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_DATE", columnDefinition="DATETIME") 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	@Column(name="UPDATE_ID")
	private String updateId;
	public Long getApplCodeId() {
		return applCodeId;
	}
	public void setApplCodeId(Long applCodeId) {
		this.applCodeId = applCodeId;
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
	public String getApplCd() {
		return applCd;
	}
	public void setApplCd(String applCd) {
		this.applCd = applCd;
	}
	public String getApplNm() {
		return applNm;
	}
	public void setApplNm(String applNm) {
		this.applNm = applNm;
	}
	public String getApplLevelCd() {
		return applLevelCd;
	}
	public void setApplLevelCd(String applLevelCd) {
		this.applLevelCd = applLevelCd;
	}
	public String getRecLevelCd() {
		return recLevelCd;
	}
	public void setRecLevelCd(String recLevelCd) {
		this.recLevelCd = recLevelCd;
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
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
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
