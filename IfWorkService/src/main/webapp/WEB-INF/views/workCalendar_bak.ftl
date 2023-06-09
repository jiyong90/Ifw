<#include "/calendar.ftl">
<div id="workTimeCalendar" v-cloak>
    <div class="modal fade" id="flexitimeModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">근무제 적용하기</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>사용할 근무제를 선택하세요.</p>
                    <div class="mt-3">
	                	<!-- <button class="btn btn-outline btn-flat btn-block text-left" type="button" data-toggle="collapse" data-target="#collapWork" aria-expanded="false" aria-controls="collapseExample">근무제</button> -->
                        <!-- <div class="collapse" id="collapWork"> -->
                       	<div>
                            <ul class="list-group select-work-list">
                                <li class="list-group-item" v-for="(f, fIdx) in flexitimeList" @click="selectFlexitime(fIdx)">
                                    <span :class="['tag ' + f.workTypeCd]">{{f.workTypeNm}}</span>
                                    <div class="title">{{f.flexibleNm}}</div>
                                    <div class="desc">
                                    	<template v-if="f.workShm && f.workEhm">
                                    		근무구간: {{f.workShm}} ~ {{f.workEhm}}
                                    	</template>
                                    	<template v-else>
                                    		근무구간: 없음
                                    	</template>
                                    	<span class="bar"></span>
                                    	<template v-if="f.coreShm && f.coreEhm">
                                    		코어구간: {{f.coreShm}} ~ {{f.coreEhm}}
                                    	</template>
                                    	<template v-else>
                                    		코어구간: 없음
                                    	</template>
                                    </div>
                                </li>
                            </ul>
                        </div>
	                </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" class="btn btn-default" v-if="Object.keys(selectedFlexitime).length>0" @click="applyFlexitime">적용하기</button>
                </div>
            </div>
        </div>
    </div>
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
                    <div class="btn-wrap text-right">
                        <button type="button" id="applyBtn" class="btn btn-apply" data-toggle="modal" @click="getFlexitimeList">근무제 적용하기</button>
                    </div>
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
                <div id="workDayInfo" class="white-box-wrap mb-3">
                    <div class="work-plan-wrap">
                        <ul class="main-wrap">
                            <li>
                                <div class="main-title">해당일의 근무계획 구분</div>
                                <div class="main-desc">근무일</div>
                            </li>
                            <li>
                                <div class="main-title">계획 근무시간</div>
                                <div class="main-desc">09:00 ~ 21:00 (10:00)</div>
                            </li>
                            <li>
                                <div class="main-title">실적 근무시간</div>
                                <div class="main-desc">09:00 ~ 21:00 (10:00)</div>
                            </li>
                            <li>
                                <div class="main-title">해당일 근태</div>
                                <div class="main-desc">연차, 반차</div>
                            </li>
                        </ul>
                        <div class="sub-wrap">
                            <div class="sub-big-title">근무시간 요약 <span style="font-size:10px;">(근무시간 분류별 합산)</span></div>
                            <ul class="sub-list">
                                <li>
                                	<span class="sub-title"><i class="fas fa-clock"></i>소정근로</span>
                                    <span class="sub-desc">8:00</span>
                                </li>
                                <li>
                                	<span class="sub-title"><i class="fas fa-moon"></i>연장근로</span>
                                    <span class="sub-desc">2:00</span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">일반연장</span>
                                            <span class="sub-desc">1:00</span>
                                        </li>
                                        <li>
                                            <span class="sub-title">야간근무</span>
                                            <span class="sub-desc">1:00</span>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <span class="sub-title"><i class="fas fa-file-alt"></i>근태현황</span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">오후반차</span>
                                            <span class="sub-desc">4:00</span>
                                        </li>
                                        <li>
                                            <span class="sub-title">외출</span>
                                            <span class="sub-desc">2:00</span>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                	<span class="sub-title"><i class="fas fa-couch"></i>휴식/휴게 현황</span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">무급</span>
                                            <span class="sub-desc">00:30</span>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div id="flexibleAppl" class="white-box-wrap full-height mb-3" style="display:none;">
                    <div class="work-plan-wrap">
                        <div class="main-wrap">
                            <div class="main-title">해당일의 근무계획 구분</div>
                            <div class="main-desc">{{selectedFlexitime.flexibleNm}}</div>
                            <ul class="time-list">
                                <li>
                                    <span class="title">근무가능시간</span>
                                    <span class="desc">
                                    	<template v-if="selectedFlexitime.workShm && selectedFlexitime.workEhm">
                                    	<!-- {{selectedFlexitime.workShm}} ~ {{selectedFlexitime.workEhm}} -->
                                    	{{moment(selectedFlexitime.useSymd+' '+selectedFlexitime.workShm).format('HH:mm')}} ~ {{moment(selectedFlexitime.useSymd+' '+selectedFlexitime.workEhm).format('HH:mm')}}
                                    	</template>
                                    	<template v-else>
                                    	없음
                                    	</template>
                                    </span>
                                </li>
                                <li>
                                    <span class="title">필수근무시간</span>
                                    <span class="desc">
                                    	<template v-if="selectedFlexitime.coreShm && selectedFlexitime.coreEhm">
                                    	<!-- {{selectedFlexitime.coreShm}} ~ {{selectedFlexitime.coreEhm}} -->
                                    	{{moment(selectedFlexitime.useSymd+' '+selectedFlexitime.coreShm).format('HH:mm')}} ~ {{moment(selectedFlexitime.useSymd+' '+selectedFlexitime.coreEhm).format('HH:mm')}}
                                    	</template>
                                    	<template v-else>
                                    	없음
                                    	</template>
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="sub-wrap" v-show="applInfo.useSymd">
                        <form action="">
                            <div class="form-row no-gutters time-input-form">
                                <div class="form-group col-5">
                                    <label for="useSymd">시작일자</label>
                                    <input type="text" class="form-control" id="useSymd" pattern="\d{1,2}/\d{1,2}/\d{4}" placeholder="YYYY-MM-DD" v-model="applInfo.useSymd" @change="changeUseSymd">
                                </div>
                                <div class="form-group col-2 text-center">
                                    <lable></lable>
                                    <span>~</span>
                                </div>
                                <div class="form-group col-5">
                                    <label for="useEymd">종료일자</label>
                                    <input type="text" class="form-control" id="useEymd" pattern="\d{1,2}/\d{1,2}/\d{4}" placeholder="YYYY-MM-DD" v-model="applInfo.useEymd" disabled>
                                </div>
                                <div class="form-group col-12">
                                    <label for="workTime">근무기간</label>
                                    <select id="workTime" class="form-control" v-model="applInfo.workRange" @change="changeWorkRange">
                                        <option v-for="term in selectedFlexitime.usedTermOpt" :value="term.value">{{term.lable}}</option>
                                    </select>
                                </div>
                                <div class="form-group col-12">
                                    <label for="reson">사유</label>
                                    <textarea class="form-control" id="reson" rows="3" placeholder="팀장 확인 시에 필요합니다." v-model="applInfo.reason"></textarea>
                                </div>
                            </div>
                            <div class="btn-wrap mt-5">
                                <button id="apprBtn" type="button" class="btn btn-apply btn-block btn-lg" @click="flexitimeAppl">확인요청</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div id="flexibleDayPlan" class="white-box-wrap full-height mb-3" style="display:none;">
	                <div class="work-plan-wrap">
	                    <div class="big-title" v-if="Object.keys(applInfo).length>0 && applInfo.useSymd!='' && applInfo.useEymd!=''">
	                    	{{moment(applInfo.useSymd).format("YYYY년 M월 D일")}} ~ {{moment(applInfo.useEymd).format("YYYY년 M월 D일")}}({{moment(applInfo.useEymd).diff(applInfo.useSymd, 'days')+1}}일)
	                    </div>
	                    <div class="inner-wrap">
	                        <div class="main-title">근무시간표</div>
	                        <div class="main-desc">기본근무시간표</div>
	                    </div>
	                    <div class="time-input-form">
	                        <div class="form-row no-gutters">
	                            <div class="form-group col-5">
	                                <label for="startTime">출근시간</label>
	                                <input type="time" class="form-control" id="startTime" placeholder="" @change="changeWorkTime">
	                            </div>
	                            <div class="form-group col-2 text-center">
	                                <lable></lable>
	                                <span>~</span>
	                            </div>
	                            <div class="form-group col-5">
	                                <label for="endTime">퇴근시간</label>
	                                <input type="time" class="form-control" id="endTime" placeholder="" @change="changeWorkTime">
	                            </div>
	                        </div>
	                    </div>
	                </div>
	                <div class="sub-wrap">
	                    <ul class="time-block-list">
	                        <li>
	                            <div class="title">총 소정 근로 시간</div>
	                            <div class="desc">120시간</div>
	                        </li>
	                        <li>
	                            <div class="title">계획 시간</div>
	                            <div class="desc">42시간</div>
	                        </li>
	                    </ul>
	                </div>
	                <div class="sub-desc">*연차는 표준근무시간 8시간 인정</div>
	                <div class="btn-wrap mt-5">
	                    <button type="button" class="btn btn-apply btn-block btn-lg" @click="saveWorkDayResult">저장</button>
	                </div>
	            </div>
            </div>
            <div class="col-12 col-md-9">
                <div class="calendar-wrap">
               		<div id="calendar-popover" class="popover-inner-wrap" style="display:none;">
                		<div class="msg">시작일로 지정</div>
               			<span id="startDaySelect" class="btn btn-default btn-flat">
               				<input id="startDay" type="hidden" value="">확인
               			</span>
               		</div>
                    <div id='calendar-container'>
                		<full-calendar ref="fullCalendar" :events="events" :eventsources="eventSources" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @dateclick="dateClickCallback" @eventrender="eventRenderCallback" @eventclick="eventClickCallback"></full-calendar>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
   	var workTimeCalendarVue = new Vue({
   		el: "#workTimeCalendar",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
  		    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
  		    	useYn: 'N', //근무제 적용 여부
  		    	prevEdate: '', //이전 근무제 종료일
  		    	workTermTime: {}, //선택한 기간의 근무제 정보
  		    	monthFlexitimeList: [], //해당 월의 근무제 리스트
  		    	flexitimeList: [], //사용할 근무제 리스트
  		    	selectedFlexitime: {}, //적용할 근무제
  		    	applInfo: { //신청 데이터
  		    		flexibleApplId:'',
  		    		flexibleEmpId:'',
  		    		applId:'',
  		    		useSymd:'',
  		    		useEymd:'',
  		    		workRange:'',
  		    		reason:''
  		    	}, 
  		    	selectedWorkday: {},
  		    	dayResult: {}, //상세 근무계획
  		    	//useSymd: '', //시작일
  		    	//useEymd: '',
  		    	//workRange: '', //근무기간
  		    	events: [
  		    		/* {
  		                title: '연차',
  		              	description: '개인 휴가',
  		                start: '2019-07-03',
  		                end: '2019-07-03',
  		                color: '#4d84fe'
  		            },
  		    		{
  		                title: '출근',
  		                start: '2019-07-11T08:23:00',
  		                end: '2019-07-11T08:25:00',
  		                color: '#4d84fe'
  		            },
  		          	{
  		                title: '퇴근',
  		                start: '2019-07-11T21:05:00',
  		                end: '2019-07-11T21:07:00',
  		                color: '#4d84fe'
  		            },
  		    		{
  		            	title: '무급휴게',
  		                start: '2019-07-11T11:30:00',
  		                end: '2019-07-11T13:00:00',
  		                rendering: 'background',
  		                color: '#fc8262'
  		            },
		          	{
		                title: '기본근무',
		                start: '2019-07-11T09:00:00',
		                end: '2019-07-11T18:00:00',
		                color: '#1fc486'
		            },
		            {
  		            	title: '무급휴게',
  		                start: '2019-07-11T18:00:00',
  		                end: '2019-07-11T19:00:00',
  		                rendering: 'background',
  		                color: '#fc8262'
  		            },
  		          	{
  		                title: '연장근무',
  		                start: '2019-07-11T19:00:00',
  		                end: '2019-07-11T21:05:00',
  		                color: '#f75353'
  		            } */
  		    	],
  		    	eventSources: [
  		    		/* {
  		    	      events: [  
  		    	        {
  		    	          start     : '2019-07-19',
  		    	          rendering: 'background'
  		    	        }
  		    	      ],
  		    	      backgroundColor: 'green',
  		    	      borderColor: 'red',
  		    	      textColor: 'yellow'
  		    	    } */
  		    	]
  		    },
  		    mounted: function(){
  		    	<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
  		    		var flexibleAppl = JSON.parse("${flexibleAppl?js_string}"); //임시저장된 신청서
  		    		
  		    		$("#applyBtn").hide();
					$("#workRangeInfo").hide();
	  	         	$("#workDayInfo").hide();
	  	         	
					//신청화면 전환
	         		this.viewFlexitimeAppl(flexibleAppl);
  		    	</#if>
  		    	
  		    	//this.getWorkRangeInfo(this.today);
  		    	this.getWorkDayInfo(this.today);
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		//기존에 시행한 유연근무 기간의 경우 선택하지 못하게끔 함
  		    		this.selectAllow();
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'dayGridMonth') { //month change
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
							},
							error: function() {
								
							}
						});
	   		    		
	   		    		//화면에 보이는 달력의 시작일, 종료일을 파라미터로 넘김
	   		    		$this.getFlexibleEmpList(calendar.view.activeStart, calendar.view.activeEnd);
	  		    	}
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
  		    		var $this = this;
  		    	
  		    		if($("#flexibleDayPlan").is(":visible")) {
  		    			
	  		    		if($this.selectedWorkday!=null && Object.keys($this.selectedWorkday).length>0) {
	  		    			$("#startTime").val("");
	  		    			$("#endTime").val("");
	  		    		}
	  		    		
	  		    		var endDate = new Date(info.endStr);
	  		    		endDate.setDate(endDate.getDate()-1);
	  		    		endDate = moment(endDate).format('YYYY-MM-DD');
	  		    		
	  		    		var dayNum = moment(endDate).diff(info.startStr, 'days')+1; //선택한 일 수
	  		    		$this.selectedWorkday = {
		    				start: info.startStr,
		    				end: endDate,
		    				dayNum: dayNum
		    			};
	  		    		
	  		    		//근무요일 아닌 경우 출/퇴근 시간 입력 못하도록 함
	  		    		var isWorkDay = false;
	  		    		var workDaysOpt = $this.selectedFlexitime.workDaysOpt;
	  		    		
	  		    		var d = new Date(info.startStr);
  		    			while(moment(d).diff(endDate, 'days')<=0) {
	  		    			if(workDaysOpt[d.getDay()+1])
	  		    				isWorkDay = true;
	  		    			d.setDate(d.getDate()+1);
	  		    		}
	  		    		
	  		    		if(isWorkDay) 
	  		    			$("#flexibleDayPlan").find(".time-input-form").show();
	  		    		else
	  		    			$("#flexibleDayPlan").find(".time-input-form").hide();
  		    		}
  		    	},
  		    	dateClickCallback : function(info){
  		    		
  		    		var $this = this;
  	  		    	
  		    		//시작일 지정 팝업
  		    		if(this.useYn=='Y' /* && (this.useSymd==null || this.useSymd=='') */) {
  		    			$('.popover').remove();
  		    			
  		    			if($this.prevEdate==''||moment(info.dateStr).diff($this.prevEdate)>0){
	  		    			$(info.dayEl).popover({
	  	  		    			html: true,
	  					    	placement: 'bottom',
	  						    //animation:true,
	  				            //delay: 100,
	  				            content: function(){
	  				            	$("#startDay").val(info.dateStr);
	  				            	return $("#calendar-popover").html();
	  				            },
	  				            container:'#calendar'
	  					    });
	  		    			$(info.dayEl).popover('show');
  		    			}
  		    			
  		    			/* $("#alertText").html(info.startStr + " 시작일 지정");
  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	  	         			$("#alertModal").off('hidden.bs.modal');
  	  	         			
  	  	         			$this.useSymd = info.startStr;
  	  					
  	  	    				//신청 화면 전환
  	  	    				$this.viewFlexitimeAppl();
  	  	         		});
  	  	         		$("#alertModal").modal("show");  */
  		    		}
  		    		
  		    	},
  		    	eventRenderCallback : function(info){
  		    		/* if(info.event.id.indexOf('workRange.')==0 && info.isStart) {
  		    			//$(info.el).prev().css('border-right', '1px solid red');
  		    			//$(info.el).siblings("td").css('border-right', '1px solid red');
  		    			console.log(info.event.id);
  		    			var key = info.event.id.split(".");
  		    			var workTypeCd = key[1];
  		    			
  		    			//$('td').find(".fc-day[data-date='"+moment(yesterday).format('YYYY-MM-DD')+"']").css('border-right', '1px solid red');
  		    			$('td').find(".fc-day[data-date='"+moment(info.event.start).format('YYYY-MM-DD')+"']").prepend('<div class="fc-triangle start '+workTypeCd+'"></div>');
  		    		}  */
  		    		
  		    		//기존에 신청한 근무제
  		    		if(info.event.id.indexOf('workRange.')==0) {
  		    			if(info.isStart) {
  		    				
  		    			} else if(info.isEnd) {
  		    				
  		    			} else {
  		    				//$(info.el).css('border-top', '1px solid red');
  		    			}
  		    		}
  		    	},
  		    	eventClickCallback : function(info){
  		    	},
  		    	dayRenderCallback : function(dayRenderInfo){ //day render
  		    		var date = dayRenderInfo.date;
  	         		$('td').find(".fc-day-top[data-date='"+moment(date).format('YYYY-MM-DD')+"'] .fc-day-number").text(moment(date).format('D'));
  	         	},
  	         	addEvent : function(Obj){
  	         		if(Obj!=null) {
  	         			var calendar = this.$refs.fullCalendar.cal;
  	         			
  	         			var event = calendar.getEventById(Obj.id);
  	         			
	         			/* if(event==null) {
	         				//이벤트 새로 생성
	         				calendar.batchRendering(function() {
	  	  	         			calendar.addEvent(Obj);
	  	  	         		});
	         			} else {
	         				//이벤트 날짜 수정
	         				calendar.batchRendering(function() {
		         				event.setStart(Obj.start);
		         				event.setEnd(Obj.end);
		         				event.setProp("startRecur",Obj.startRecur);
		         				event.setProp("endRecur",Obj.endRecur);
	         				});
	         			} */
	         			
	         			if(event!=null) {
	         				event.remove();
	         			}
	         			//이벤트 새로 생성
         				calendar.batchRendering(function() {
  	  	         			calendar.addEvent(Obj);
  	  	         		});
	         			
  	         		}
  	         	},
  	         	addEventSource : function(Obj){
  	         		if(Obj!=null) {
	  	         		var calendar = this.$refs.fullCalendar.cal;
	  	         		
	  	         		var eventSource = calendar.getEventSourceById(Obj.id);
	  	         		
	  	         		if(eventSource!=null) {
	  	         			eventSource.remove();
	  	         		}
	  	         		//이벤트 새로 생성
         				calendar.batchRendering(function() {
         					calendar.addEventSource(Obj);
         				});
  	         		}
  	         	},
  	         	getFlexibleEmpList : function(sYmd, eYmd){ //해당월의 근무제 정보
  	         		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		
					var param = {
	   		    		sYmd : moment(sYmd).format('YYYYMM'),
	   		    		eYmd : moment(eYmd).format('YYYYMM')
	   		    	};
   		    		
					$this.monthFlexitimeList = [];
					
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/list",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							//console.log(data);
							if(data.status=='OK' && data.flexibleList!=null) {
								$this.monthFlexitimeList = data.flexibleList;
								
								$this.monthFlexitimeList.map(function(f){
									var sYmd = moment(f.sYmd).format('YYYY-MM-DD');
									var eYmd = new Date(moment(f.eYmd).format('YYYY-MM-DD'));
									eYmd.setDate(eYmd.getDate()+1);
									eYmd = moment(eYmd).format('YYYY-MM-DD');
									
									var classNames = [];
									classNames.push(f.workTypeCd);
									
									$this.addEvent({
										id: 'workRange.'+f.workTypeCd+'.'+f.applId,
										start: sYmd,
			  		  		        	end: eYmd,
			  		  		        	rendering: 'background',
			  		  		        	classNames: classNames
									});
								});
							}
						},
						error: function(e) {
							$this.monthFlexitimeList = [];
						}
					});
  	         	},
				selectAllow : function(){
  		    		var $this = this;
  		    		var calendar = $this.$refs.fullCalendar.cal;
  		    		
  		    		//기존에 시행한 유연근무 기간의 경우 선택하지 못하게끔 함
  	         		calendar.setOption('selectAllow', function(i){
  	  		    		if($this.prevEdate!=null && $this.prevEdate!='')
  	  		    			return moment(i.startStr).diff($this.prevEdate)>0;
  	  		    		else
  	  		    			return true;
  	         		});
  		    	},
				getWorkRangeInfo : function(ymd){ //오늘 또는 선택한 기간의 근무제 정보
					var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		
					var param = {
	   		    		ymd : moment(ymd).format('YYYYMMDD')
	   		    	};
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/term",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							console.log(data);
							$this.workTermTime = {};
							/* if(data.status=='OK' && data.workTermTime!=null) {
								$this.workTermTime = data.workTermTime;
								
							} */
						},
						error: function(e) {
							$this.workTermTime = {};
						}
					});
  	         	},
				getWorkDayInfo : function(ymd){ //해당일의 근무 정보
					
				},
  	         	getFlexitimeList : function(){ //사용할 근무제 리스트
  	         		var $this = this;
  	         		/* var param = {
	   		    		useSymd : this.today
	   		    	}; */
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleStd",
						type: "GET",
						contentType: 'application/json',
						//data: param,
						dataType: "json",
						success: function(data) {
							$this.flexitimeList = [];
							if(data.status=='OK' && data.wtmFlexibleStd!=null) {
								//console.log(data.wtmFlexibleStd);
								$this.flexitimeList = data.wtmFlexibleStd;
								
								//사용할 근무제 팝업 띄우기
								$("#flexitimeModal").modal("show"); 
							}
						},
						error: function(e) {
							console.log(e);
							$this.flexitimeList = [];
						}
					});
  	         		
  	         	},
  	         	selectFlexitime : function(idx){ //사용할 근무제 선택
  	         		var $this = this;
  	         		
  	         		$(".list-group-item").not(idx).removeClass("active");
  	         		$(".list-group-item").eq(idx).addClass("active");
  	         		
  	         		//선택한 근무제 적용
  	         		$this.selectedFlexitime = $this.flexitimeList[idx];
  	         	},
  	         	getPrevFlexitime : function(){ //이전에 시행한 근무제 기간 조회
  	         		var $this = this;
  	         		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/prev",
						type: "GET",
						contentType: 'application/json',
						//data: param,
						dataType: "json",
						success: function(data) {
							$this.prevEdate = '';
							if(data.status=='OK' && data.prevFlexible!=null) {
								$this.prevEdate = data.prevFlexible.eYmd;
							}
						},
						error: function(e) {
							console.log(e);
							$this.prevEdate = '';
						}
					});
  	         	},
  	         	clearFlexitimeAppl : function(){
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		
  	         		var workRangeEvent = calendar.getEventById('workRange');
  	         		if(workRangeEvent!=null)
  	         			workRangeEvent.remove();
  	         		
  	         		$this.applInfo.useSymd = '';
         			$this.applInfo.useEymd = '';
         			
  	         	},
  	         	applyFlexitime : function(){ //근무제 적용
  	         		var $this = this;
  	         	
  	         		$this.clearFlexitimeAppl();
  		    		$this.applInfo.workRange = '';
  	         	
  	         		$('#flexitimeModal').on('hidden.bs.modal',function(){
  	         			$('#flexitimeModal').off('hidden.bs.modal');
  	         			$(".list-group-item").removeClass("active");
  	         			
  	         			//신청화면 전환
  	  	         		$("#workRangeInfo").hide();
  	  	         		$("#workDayInfo").hide();
  	         			
  	         			$("#alertText").html("달력에서 근무제 시작일을 선택해주세요.");
  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	  	         			$("#alertModal").off('hidden.bs.modal');
  	  	         			$this.applInfo.useSymd='';
	  	         			$this.useYn='Y';
	  	         			
	  	         			//신청화면 전환
	  	         			$this.viewFlexitimeAppl(null);
	  	         			
	  	         			//선택할 수 있는 근무기간 체크
	  	         			$this.getPrevFlexitime();
  	  	         		});
  	  	         		$("#alertModal").modal("show"); 
  	         		});
  	         		$('#flexitimeModal .close').click();
  	         		
  	         	},
  	         	viewFlexitimeAppl : function(obj){
  	         		var $this = this;
  	         		
  	         		if(obj!=null) {
  	         			$this.applInfo.flexibleApplId = obj.flexibleApplId;
  	         			$this.applInfo.applId = obj.applId;
  	         			$this.applInfo.useSymd = moment(obj.sYmd).format('YYYY-MM-DD');
  	         			$this.applInfo.useEymd = moment(obj.eYmd).format('YYYY-MM-DD');
  	         			
  	         			<#if flexibleStdMgr?? && flexibleStdMgr!='' && flexibleStdMgr?exists >
  	         				$this.selectedFlexitime = JSON.parse("${flexibleStdMgr?js_string}");
  	         				
  	         				if($this.selectedFlexitime.usedTermOpt!=null && $this.selectedFlexitime.usedTermOpt!='undefined' && $this.selectedFlexitime.usedTermOpt!='')
  	         					$this.selectedFlexitime.usedTermOpt = JSON.parse($this.selectedFlexitime.usedTermOpt);
  	         				if($this.selectedFlexitime.workDaysOpt!=null && $this.selectedFlexitime.workDaysOpt!='undefined' && $this.selectedFlexitime.workDaysOpt!='')
  	         					$this.selectedFlexitime.workDaysOpt = JSON.parse($this.selectedFlexitime.workDaysOpt);
  	         				if($this.selectedFlexitime.applTermOpt!=null && $this.selectedFlexitime.applTermOpt!='undefined' && $this.selectedFlexitime.applTermOpt!='')
  	         					$this.selectedFlexitime.applTermOpt = JSON.parse($this.selectedFlexitime.applTermOpt);
  	         			</#if>
  	         			
  	         			if(obj.applStatusCd=='99') { //결재완료
  	  	         			$this.applInfo.flexibleEmpId = obj.flexibleEmpId;
  	  	         			$("#flexibleDayPlan").show();
  	  	         		}else { 
  	  	         			if(obj.applStatusCd!='11') { //결재요청
  	  	         				$("#apprBtn").hide();
  	  	         				$("#flexibleAppl").find("input,select,textarea").prop("disabled", true);
  	  	         			}
  	  	         			$("#flexibleAppl").show();
  	  	         		}
  	         			
  	         		} else {
  	         			$("#flexibleAppl").show();
  	         		}
  	         	},
  	         	setUsedTermOpt : function(){ //신청서 setting
  	         		var $this = this;
  	         	
  	         		//적용기간은 첫번째 항목으로 기본 세팅
  	         		if($this.selectedFlexitime.hasOwnProperty("usedTermOpt") && $this.selectedFlexitime.usedTermOpt!=null) {
  	         			var workDateRangeItem = $this.selectedFlexitime.usedTermOpt[0]; 
  	         			
  	         			if(workDateRangeItem.hasOwnProperty("value")&&workDateRangeItem.value!=null)
  	         				$this.applInfo.workRange = workDateRangeItem.value;
  	         				$this.changeWorkRange();
  	         		}
  	         		
  	         	},
  	         	changeUseSymd : function(){
  	         		var $this = this;
  	         		
  	         		if(moment($this.applInfo.useSymd).diff($this.prevEdate)<=0) {
  	         			$this.clearFlexitimeAppl();
  	         			return false;
  	         		}
  	         		$this.changeWorkRange();
  	         	},
  	         	changeWorkRange : function(){ //근무기간 변경에 따라 background 변경
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		
  	         		if($this.applInfo.useSymd!=null && $this.applInfo.useSymd!='' && $this.applInfo.workRange!=null && $this.applInfo.workRange!='') {
	  	         		var workDateRange = $this.applInfo.workRange.split('_');
	         			var eYmd = new Date($this.applInfo.useSymd);
	         			if(workDateRange[1]=='week') {
	         				eYmd.setDate(eYmd.getDate()+ (workDateRange[0]*7));
	         			} else if(workDateRange[1]=='month') {
	 	         			eYmd.setMonth(eYmd.getMonth()+ (Number(workDateRange[0])));
	         			}
	         			
	         			var classNames = [];
						classNames.push($this.selectedFlexitime.workTypeCd);
						
						//근무 요일이 아닌 경우 제외하고 event 생성
						var workDaysOpt = [];
						$.each($this.selectedFlexitime.workDaysOpt, function(k, v){
							if(v==true) {
								workDaysOpt.push(k-1);
							}
						});
						
	         			$this.addEvent({
  	         				id: 'workRange',
  		  		    		start: $this.applInfo.useSymd,
  		  		        	end: moment(eYmd).format('YYYY-MM-DD'),
  		  		        	rendering: 'background',
  		  		        	daysOfWeek: workDaysOpt,
  		  		        	startRecur: $this.applInfo.useSymd,
  		  		        	endRecur: moment(eYmd).format('YYYY-MM-DD'),
  		  		        	classNames: classNames
  	  	  		      	});
	         			
	         			eYmd.setDate(eYmd.getDate()-1);
	         			$this.applInfo.useEymd = moment(eYmd).format('YYYY-MM-DD');
	         			
	         			calendar.gotoDate($this.applInfo.useSymd);
  	         		}
  	         	},
  	         	changeWorkTime : function(){ //상세 근무계획 등록
					var $this = this;
					var workDaysOpt = $this.selectedFlexitime.workDaysOpt; //근무요일
		    		//var applTermOpt = $this.selectedFlexitime.applTermOpt; //신청기간
  		    		
  		    		if($this.selectedWorkday.start!='undefined'&& $this.selectedWorkday.end!='undefined' 
  		    				&& $("#startTime").val()!=='' && $("#endTime").val()!=='') {
  		    			
  		    			//부분 선근제의 경우 코어시간 포함하도록 체크
  		    			
  		    			
  		    			var sDate = $this.selectedWorkday.start;
  		    		    var eDate = $this.selectedWorkday.end;

  		    		    var shm = moment($this.selectedWorkday.start+' '+$("#startTime").val()).format('HHmm');
  		    		    var ehm = moment($this.selectedWorkday.start+' '+$("#endTime").val()).format('HHmm');
  		    		    
  		    			var d = new Date(sDate);
  		    			while(moment(d).diff(eDate, 'days')<=0) {
  		    				if(workDaysOpt[d.getDay()+1]) { //근무요일이고, 신청기간이면
	  		    				if($this.dayResult.hasOwnProperty(moment(d).format("YYYYMMDD"))) {
	  		    					$this.dayResult[moment(d).format("YYYYMMDD")].shm = shm;
	  		    					$this.dayResult[moment(d).format("YYYYMMDD")].ehm = ehm;
	  		    				} else {
	  		    					$this.dayResult[moment(d).format('YYYYMMDD')] = {
			  		    				shm: shm,
	 			    	  		    	ehm: ehm	
			  		    			};
	  		    				}
  		    				}
	  		    				
		  		    		//날짜 증가
		  		    		d.setDate(d.getDate()+1);
  		    			}

  		    			console.log($this.dayResult);
  		    			
  		    			var eventSource = {};
  		    			var events = [];
  		    			$.each($this.dayResult, function(k, v){
  		    				//출근시간>퇴근시간 인 경우 다음날로
  		    				/* var sDatetime = moment(k+' '+v.shm).format('YYYY-MM-DD HH:mm');
  		    				var eDatetime = moment(k+' '+v.ehm).format('YYYY-MM-DD HH:mm');
  		    				
  		    				if(moment(sDatetime).diff(eDatetime)>0){
  		    					var date = new Date(moment(k).format('YYYY-MM-DD'));
  		    					date.setDate(date.getDate()+1);
  		    					eDatetime = moment(moment(date).format('YYYYMMDD')+' '+v.ehm).format('YYYY-MM-DD HH:mm');
  		    				}
  		    				
  		    				var newEvent = {
	    						title: moment(sDatetime).format('HH:mm')+'~'+moment(eDatetime).format('HH:mm'),
	    						start: sDatetime,
	    						end: eDatetime
	    					};
  		    				*/
  		    				var newEvent = {
	    						title: moment(k+' '+v.shm).format('HH:mm')+'~'+moment(k+' '+v.ehm).format('HH:mm'),
	    						start: moment(k).format('YYYY-MM-DD'),
	    						end: moment(k).format('YYYY-MM-DD')
	    					};
  		    				 
	    					events.push(newEvent);
  		    			}); 
  		    			
  		    			//이벤트 추가
  		    			if(events.length>0) {
  		    				eventSource['id'] = 'workTime';
  		    				eventSource['events'] = events;
  		    				eventSource['editable'] = false;
  		    				eventSource['backgroundColor'] = '#90e91b';
  		    				eventSource['borderColor'] = '#90e91b';
	  		    			$this.addEventSource(eventSource);
  		    			}
  		    		} 
  		    		
  	         	},
  	         	flexitimeApplImsi : function(){ //임시저장
  	         		var $this = this;
  	  	         	
  	         		//선택한 근무제
  	         		var flexitime = $this.selectedFlexitime;
  	         		
  	         		if(flexitime.workTypeCd.indexOf('SELE')==0) {
	         			var param = {
	         				flexibleStdMgrId : flexitime.flexibleStdMgrId,
	         				workTypeCd : flexitime.workTypeCd,
	         				//empNo : "${empNo}",
	         				applId : $this.applInfo.applId,
		   		    		sYmd : moment($this.applInfo.useSymd).format('YYYYMMDD'),
		   		    		eYmd : moment($this.applInfo.useEymd).format('YYYYMMDD')
		   		    	};
	 	         			
	   		    		Util.ajax({
							url: "${rc.getContextPath()}/flexibleAppl/imsi",
							type: "POST",
							contentType: 'application/json',
							data: JSON.stringify(param),
							dataType: "json",
							success: function(data) {
								console.log(data);
							},
							error: function(e) {
								console.log(e);
								$("#alertText").html("확인요청 시 오류가 발생했습니다.");
		  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
		  	  	         		$("#alertModal").modal("show"); 
							}
						}); 
  	         		} else if(flexitime.workTypeCd.indexOf('ELAS')==0){
  	         			
  	         		}

  	         	},
  	         	flexitimeAppl : function(){ //확인요청
  	         		var $this = this;
  	  	         	
  	         		//선택한 근무제
  	         		var flexitime = $this.selectedFlexitime;
  	         		//신청서 정보
  	         		var applInfo = $this.applInfo;
  	         		
         			var saveYn = true;
         			if(applInfo.useSymd=='') {
         				saveYn = false;
         				$("#alertText").html("시작일을 입력해 주세요.");
         			}else if(applInfo.useEymd=='') {
         				saveYn = false;
						$("#alertText").html("종료일을 입력해 주세요.");
	         		}else if(applInfo.workRange=='') {
	         			saveYn = false;
						$("#alertText").html("근무기간을 입력해 주세요.");
	         		}else if(applInfo.reason=='') {
	         			saveYn = false;
						$("#alertText").html("사유를 입력해 주세요.");
	         		}
						
	         		if(flexitime.workTypeCd.indexOf('SELE')==0) {
						if(saveYn) {
							var param = {
								flexibleApplId : $this.applInfo.flexibleApplId,	
								applId : $this.applInfo.applId,
	  	         				flexibleStdMgrId : flexitime.flexibleStdMgrId,
	  	         				workTypeCd : flexitime.workTypeCd,
	  	         				//empNo : "${empNo}",
			   		    		sYmd : moment($this.applInfo.useSymd).format('YYYYMMDD'),
			   		    		eYmd : moment($this.applInfo.useEymd).format('YYYYMMDD'),
			   		    		reason: $this.applInfo.reason
			   		    	};
	  	         			
		   		    		Util.ajax({
								url: "${rc.getContextPath()}/flexibleAppl/request",
								type: "POST",
								contentType: 'application/json',
								data: JSON.stringify(param),
								dataType: "json",
								success: function(data) {
									if(data!=null && data.status=='OK') {
										$("#alertText").html("확인요청 되었습니다.");
									} else {
										$("#alertText").html(data.message);
									}
									$("#alertModal").on('hidden.bs.modal',function(){});
			  	  	         		$("#alertModal").modal("show"); 
								},
								error: function(e) {
									console.log(e);
									$("#alertText").html("확인요청 시 오류가 발생했습니다.");
			  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
			  	  	         		$("#alertModal").modal("show"); 
								}
							});
						} else {
							$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
  	         		} else if(flexitime.workTypeCd.indexOf('ELAS')==0){
  	         			
  	         			$("#flexibleAppl").hide();
  	         			$("#flexibleDayPlan").show();
  	         		}
  	         	},
  	         	saveWorkDayResult : function(){ //일근무결과 저장
	         		var $this = this;
	  	         	
         			var param = {
         				flexibleEmpId : $this.applInfo.flexibleEmpId,
         				dayResult : $this.dayResult
	   		    	};
 	         			
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/save",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							console.log(data);
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("확인요청 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					}); 

	         	}
  		    }
   	});
   	
   	$('#flexitimeModal').on('hidden.bs.modal',function(){
   		$(".list-group-item").removeClass("active");
   		workTimeCalendarVue.prevEdate = '';
   	});
   	
	$(document).on("click", "#startDaySelect", function() {
		$('.popover').remove();
		workTimeCalendarVue.applInfo.useSymd = $("#startDay").val();
		//신청 화면 전환
		workTimeCalendarVue.setUsedTermOpt();
   	});
   	
</script>

