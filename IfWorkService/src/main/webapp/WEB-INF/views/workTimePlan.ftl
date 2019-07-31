<#include "/calendar.ftl">
<div id="workTimePlan" v-cloak>
    <div id="sub-nav" class="container-fluid">
        <form action="">
            <div class="row no-gutters work-time-wrap">
                <div class="col-12 col-sm-2 col-xl-1">
                    <div class="title">현재 근무계획</div>
                    <div class="desc">기본근무제</div>
                </div>
                <div class="col-12 col-sm-2 col-xl-1">
                    <div class="title">잔여소정근로</div>
                    <div class="desc">8시간 42분</div>
                </div>
                <div class="col-12 col-sm-2 col-xl-1">
                    <div class="title">잔여연장근로</div>
                    <div class="desc">4시간</div>
                </div>
                <div class="col">
                </div>
                <div class="col-12 col-sm-4 col-md-3 col-lg-2 col-xl-2">
                </div>
            </div>
            <div class="form-inline work-check-wrap">
                <span class="title">캘린더 표시</span>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" id="customCheck1">
                    <label class="custom-control-label" for="customCheck1">회사캘린더</label>
                </div>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" id="customCheck2">
                    <label class="custom-control-label" for="customCheck2">근무계획</label>
                </div>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" id="customCheck3">
                    <label class="custom-control-label" for="customCheck3">근무실적</label>
                </div>
                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" id="customCheck4">
                    <label class="custom-control-label" for="customCheck4">요약정보로 보기</label>
                </div>
            </div>
        </form>
    </div>
    <div class="container-fluid">
        <div class="row no-gutters">
            <div class="col-12 col-md-3 pr-md-3">
            	<div id="workRangeInfo" class="work-info-wrap mb-3">
                    <div class="main-title" v-if="Object.keys(workTermTime).length>0 && workTermTime.flexubleSdate!='' && workTermTime.flexubleEdate!=''">
                    	{{moment(workTermTime.flexubleSdate).format("YYYY년 M월 D일")}} ~ {{moment(workTermTime.flexubleEdate).format("YYYY년 M월 D일")}}
                    </div>
                    <div class="main-desc" v-if="Object.keys(workTermTime).length>0 && workTermTime.flexibleNm!=''">
                    	{{workTermTime.flexibleNm}}
                    </div>
                    <ul class="sub-wrap">
                        <li>
                            <div class="sub-title">총 계획 근무시간(소정/연장/휴게)</div>
                            <div class="sub-desc" v-if="Object.keys(workTermTime).length>0">
                            	<template v-if="workTermTime.planWorkMinute!=''">
                            	{{workTermTime.planWorkMinute}}
                            	</template>
                            	<template v-else-if="workTermTime.planOtMinute!=''">
                            	 / {{workTermTime.planOtMinute}}
                            	</template>
                            	 / 0:30
                            </div>
                        </li>
                        <li>
                            <div class="sub-title">총 실적 근무시간(소정/연장/휴게)</div>
                            <div class="sub-desc" v-if="Object.keys(workTermTime).length>0">
                            	<template v-if="workTermTime.apprWorkMinute!=''">
                            	{{workTermTime.apprWorkMinute}}
                            	</template>
                            	<template v-else-if="workTermTime.apprOtMinute!=''">
                            	 / {{workTermTime.apprOtMinute}}
                            	</template>
                            	 / 0:30
                            </div>
                        </li>
                        <li>
                            <div class="sub-title">근로시간 산정 구간 평균 주간 근무시간</div>
                            <div class="sub-desc" v-if="Object.keys(workTermTime).length>0 && workTermTime.avlMinute!=''">{{workTermTime.avlMinute}}시간</div>
                        </li>
                        <li>
                            <div class="sub-title">근무시간표</div>
                            <div class="sub-desc">표준 근무 시간표</div>
                        </li>
                    </ul>
                </div>
                <div id="flexibleDayPlan" class="white-box-wrap mb-3">
	                <div class="work-plan-wrap">
	                	<div class="main-wrap">
                            <div class="main-desc">이수 선근제 기본</div>
                            <ul class="time-list">
                                <li>
                                    <span class="title">근태</span>
                                    <span class="desc"></span>
                                </li>
                                <li>
                                    <span class="title">계획 근무 시간</span>
                                    <span class="desc"></span>
                                </li>
                                <li>
                                    <span class="title">근무시간표</span>
                                    <span class="desc"></span>
                                </li>
                                <li>
                                    <span class="title">출/퇴근 시각</span>
                                    <span class="desc"></span>
                                </li>
                                <li>
                                    <span class="title">근무 인정시간</span>
                                    <span class="desc"></span>
                                </li>
                                <li>
                                    <span class="title">소정 근무시간</span>
                                    <span class="desc"></span>
                                </li>
                                <li>
                                    <span class="title">연장 근무시간</span>
                                    <span class="desc"></span>
                                </li>
                            </ul>
	                    </div>
	                </div>
	            </div>
            </div>
            <div class="col-12 col-md-9">
                <div class="calendar-wrap">
                    <div id='calendar-container'>
                		<!-- <full-calendar ref="fullCalendar" :header="header" :events="events" :eventsources="eventSources" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback" ></full-calendar> -->
                		<full-calendar ref="fullCalendar" :header="header" :defaultview="view" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback"></full-calendar>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
   	var workTimePlanVue = new Vue({
   		el: "#workTimePlan",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
  		    	header: {
  		    		left: 'prev,next',
			        center: 'title',
			        right: ''
  		    	},
  		    	view: 'timeGridDay',
  		    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
  		    	workTermTime: {} //선택한 기간의 근무제 정보
  		    },
  		    mounted: function(){
  		    	
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'timeGridDay') { //month change
	   		    		var param = {
		   		    		ym : moment(calendar.getDate()).format('YYYYMM')
		   		    	};
	   		    		
	   		    		Util.ajax({
							url: "${rc.getContextPath()}/calendar",
							type: "GET",
							contentType: 'application/json',
							data: param,
							dataType: "json",
							success: function(data) {
								//회사 캘린더(휴무일 포함)
								if(data.companyCalendar!=null) {
									data.companyCalendar.map(function(cal){
										if(cal.hasOwnProperty("holidayYmd") && cal.holidayYmd!='') {
											$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").css({"color":"#FF0000"});
											$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").prepend(cal.holidayNm);
										}
									});
								}
					  	         	
								//상세 계획 입력 화면 전환
				         		//$this.viewWorkPlan();
							},
							error: function() {
								
							}
						});
	   		    		
	  		    	}
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
					
  		    		
  		    	},
  		    	eventRenderCallback : function(info){
  		    		
  		    		
  		    	},
  	         	addEvent : function(Obj){
  	         		if(Obj!=null) {
  	         			var calendar = this.$refs.fullCalendar.cal;
  	         			
  	         			var event = calendar.getEventById(Obj.id);
  	         			
	         			if(event!=null) {
	         				event.remove();
	         			}
	         			//이벤트 새로 생성
         				calendar.batchRendering(function() {
  	  	         			calendar.addEvent(Obj);
  	  	         		});
  	         		}
  	         	}
  		    }
   	});

   	
</script>

