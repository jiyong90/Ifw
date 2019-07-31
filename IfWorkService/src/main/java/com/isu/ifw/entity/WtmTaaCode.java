package com.isu.ifw.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="WTM_TAA_CODE")
public class WtmTaaCode {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TAA_CODE_ID")
	private Long taaCodeId;
	@Column(name="TENANT_ID")
	private Long tenantId;
	@Column(name="ENTER_CD")
	private String enterCd;
	@Column(name="TAA_CD")
	private String taaCd;
	@Column(name="TAA_NM")
	private String taaNm;
	@Column(name="TAA_TYPE_CD")
	private String taaTypeCd;
	@Column(name="HOL_INCL_YN")
	private String holInclYn;
	@Column(name="REQUEST_TYPE_CD")
	private String requestTypeCd;
	@Column(name="WORK_YN")
	private String workYn;
	@Column(name="WORK_APPR_HOUR")
	private String workApprHour;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_DATE", columnDefinition="DATETIME") 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	@Column(name="UPDATE_ID")
	private String updateId;

	
	public Long getTaaCodeId() {
		return taaCodeId;
	}


	public void setTaaCodeId(Long taaCodeId) {
		this.taaCodeId = taaCodeId;
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


	public String getTaaCd() {
		return taaCd;
	}


	public void setTaaCd(String taaCd) {
		this.taaCd = taaCd;
	}

	public String getTaaTypeCd() {
		return taaTypeCd;
	}


	public void setTaaTypeCd(String taaTypeCd) {
		this.taaTypeCd = taaTypeCd;
	}

	public String getTaaNm() {
		return taaNm;
	}


	public void setTaaNm(String taaNm) {
		this.taaNm = taaNm;
	}


	public String getHolInclYn() {
		return holInclYn;
	}


	public void setHolInclYn(String holInclYn) {
		this.holInclYn = holInclYn;
	}


	public String getRequestTypeCd() {
		return requestTypeCd;
	}


	public void setRequestTypeCd(String requestTypeCd) {
		this.requestTypeCd = requestTypeCd;
	}


	public String getWorkYn() {
		return workYn;
	}


	public void setWorkYn(String workYn) {
		this.workYn = workYn;
	}


	public String getWorkApprHour() {
		return workApprHour;
	}


	public void setWorkApprHour(String workApprHour) {
		this.workApprHour = workApprHour;
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
    protected void onUpdate() {
		this.updateDate = new Date();
    }
}
