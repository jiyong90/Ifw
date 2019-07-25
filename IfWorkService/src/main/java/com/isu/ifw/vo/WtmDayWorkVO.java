package com.isu.ifw.vo;

import java.util.List;

public class WtmDayWorkVO {
	private String day;
	private List<WtmDayPlanVO> plans;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public List<WtmDayPlanVO> getPlans() {
		return plans;
	}
	public void setPlans(List<WtmDayPlanVO> plans) {
		this.plans = plans;
	}
	
	
}

