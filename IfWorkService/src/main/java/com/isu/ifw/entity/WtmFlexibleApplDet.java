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
@Table(name="WTM_FLEXIBLE_APPL_DET")
public class WtmFlexibleApplDet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FLEXIBLE_APPL_DET_ID")
	private Long flexibleApplDetId;
	@Column(name="FLEXIBLE_APPL_ID")
	private Long flexibleApplId;
	@Column(name="YMD")
	private String ymd;
	@Column(name="PLAN_SDATE", columnDefinition="DATETIME") 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planSdate;
	@Column(name="PLAN_EDATE", columnDefinition="DATETIME") 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date planEdate;
	@Column(name="PLAN_MINUTE")
	private Integer planMinute; 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_DATE", columnDefinition="DATETIME") 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	@Column(name="UPDATE_ID")
	private Long updateId;
	
	public Long getFlexibleApplDetId() {
		return flexibleApplDetId;
	}
	public void setFlexibleApplDetId(Long flexibleApplDetId) {
		this.flexibleApplDetId = flexibleApplDetId;
	}
	public Long getFlexibleApplId() {
		return flexibleApplId;
	}
	public void setFlexibleApplId(Long flexibleApplId) {
		this.flexibleApplId = flexibleApplId;
	}
	public String getYmd() {
		return ymd;
	}
	public void setYmd(String ymd) {
		this.ymd = ymd;
	}
	public Date getPlanSdate() {
		return planSdate;
	}
	public void setPlanSdate(Date planSdate) {
		this.planSdate = planSdate;
	}
	public Date getPlanEdate() {
		return planEdate;
	}
	public void setPlanEdate(Date planEdate) {
		this.planEdate = planEdate;
	}
	public Integer getPlanMinute() {
		return planMinute;
	}
	public void setPlanMinute(Integer planMinute) {
		this.planMinute = planMinute;
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
