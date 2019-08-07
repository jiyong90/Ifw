<#include "/calendar.ftl">
<div>
	<div id="calendar_top" v-cloak>
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
	                    <button type="button" id="applyFlexBtn" class="btn btn-default" style="display:none;" @click="applyFlexitime">적용하기</button>
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
	                        <button type="button" id="applyBtn" class="btn btn-apply" data-toggle="modal">근무제 적용하기</button>
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
    </div>
    <div class="container-fluid">
        <div class="row no-gutters">
            <div id="calendar_left" class="col-12 col-md-3 pr-md-3" v-cloak>
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
                            <li>
                                <button type="button" class="btn btn-apply btn-block btn-lg" @click="viewWorkTimeCalendar">연장근로신청</button>
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
                            <div class="main-desc">{{calendarTopVue.flexibleStd.flexibleNm}}</div>
                            <ul class="time-list">
                                <li>
                                    <span class="title">근무가능시간</span>
                                    <span class="desc">
                                    	<template v-if="calendarTopVue.flexibleStd.workShm && calendarTopVue.flexibleStd.workEhm">
                                    	{{moment(calendarTopVue.flexibleStd.useSymd+' '+calendarTopVue.flexibleStd.workShm).format('HH:mm')}} ~ {{moment(calendarTopVue.flexibleStd.useSymd+' '+calendarTopVue.flexibleStd.workEhm).format('HH:mm')}}
                                    	</template>
                                    	<template v-else>
                                    	없음
                                    	</template>
                                    </span>
                                </li>
                                <li>
                                    <span class="title">필수근무시간</span>
                                    <span class="desc">
                                    	<template v-if="calendarTopVue.flexibleStd.coreShm && calendarTopVue.flexibleStd.coreEhm">
                                    	{{moment(calendarTopVue.flexibleStd.useSymd+' '+calendarTopVue.flexibleStd.coreShm).format('HH:mm')}} ~ {{moment(calendarTopVue.flexibleStd.useSymd+' '+calendarTopVue.flexibleStd.coreEhm).format('HH:mm')}}
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
                                        <option v-for="term in calendarTopVue.flexibleStd.usedTermOpt" :value="term.value">{{term.lable}}</option>
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
	                <div class="work-plan-wrap" v-if="Object.keys(flexibleAppl).length>0">
	                    <div class="big-title">
	                    	{{moment(flexibleAppl.sYmd).format("YYYY년 M월 D일")}} ~ {{moment(flexibleAppl.eYmd).format("YYYY년 M월 D일")}}({{moment(flexibleAppl.eYmd).diff(flexibleAppl.sYmd, 'days')+1}}일)
	                    </div>
	                    <div class="inner-wrap">
	                        <div class="main-title">근무시간표</div>
	                        <div class="main-desc">기본근무시간표</div>
	                    </div>
	                    <div class="inner-wrap" v-if="flexibleAppl.workShm && flexibleAppl.workEhm">
	                        <div class="main-title">근무시간</div>
	                        <div class="main-desc">{{moment(flexibleAppl.sYmd+' '+flexibleAppl.workShm).format('HH:mm')}} ~ {{moment(flexibleAppl.sYmd+' '+flexibleAppl.workEhm).format('HH:mm')}}</div>
	                    </div>
	                    <div class="inner-wrap" v-if="flexibleAppl.coreShm && flexibleAppl.coreEhm">
	                        <div class="main-title">코어시간</div>
	                        <div class="main-desc">{{moment(flexibleAppl.sYmd+' '+flexibleAppl.coreShm).format('HH:mm')}} ~ {{moment(flexibleAppl.sYmd+' '+flexibleAppl.coreEhm).format('HH:mm')}}</div>
	                    </div>
	                    <div class="time-input-form">
	                    	<div class="form-row no-gutters">
	                    		<div class="form-group">
	                                <label id="selectedRange" for=""></label>
	                            </div>
	                    	</div>
	                        <div class="form-row no-gutters">
	                            <div class="form-group col-5">
	                                <label for="startTime">출근시간</label>
	                                <input type="time" class="form-control" id="startTime" placeholder="" @focusout="changeWorkTime">
	                            </div>
	                            <div class="form-group col-2 text-center">
	                                <lable></lable>
	                                <span>~</span>
	                            </div>
	                            <div class="form-group col-5">
	                                <label for="endTime">퇴근시간</label>
	                                <input type="time" class="form-control" id="endTime" placeholder="" @focusout="changeWorkTime">
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
	            <div id="flexibleDayInfo" class="white-box-wrap mb-3" style="display:none;">
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
            	<#if calendar?? && calendar!='' && calendar?exists >
               		<#include "/${calendar}.ftl">
                </#if>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
   	var calendarTopVue = new Vue({
   		el: "#calendar_top",
	    data : {
	    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
	    	flexitimeList: [], //사용할 유연근무제 리스트
	    	flexibleStd: {} //적용한 근무제
  		},
	    mounted: function(){
	    	var $this = this;
	    	
	    	<#if flexibleStdMgr?? && flexibleStdMgr!='' && flexibleStdMgr?exists >
				$this.flexibleStd = JSON.parse("${flexibleStdMgr?js_string}");
			</#if>
	    	
	    	<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
	    		var flexibleAppl = JSON.parse("${flexibleAppl?js_string}"); //임시저장된 신청서
	    		
	    		if(flexibleAppl.applStatusCd!='11') {
	    			$("#applyBtn").hide();
	    		} else {
					//신청화면 전환
					$("#applyBtn").bind('click', function(){
						 calendarLeftVue.viewFlexitimeAppl(flexibleAppl);
					});
	    		}
       		
       		<#else>
	    		//사용할 근무제 리스트 조회
				$("#applyBtn").bind('click', function(){
					 $this.getFlexitimeList();
				});
	    	</#if>
	    	
	    	//calendarLeftVue.getWorkRangeInfo(this.today);
	    	//calendarLeftVue.getWorkDayInfo(this.today);
	    },
	    methods : {
	    	getFlexitimeList : function(){ //사용할 근무제 리스트
	         	var $this = this;
		    		
		    	Util.ajax({
					url: "${rc.getContextPath()}/flexibleStd/list",
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
         		$this.flexibleStd = $this.flexitimeList[idx];
         		$("#applyFlexBtn").show();
         	},
         	applyFlexitime : function(){ //근무제 적용
         		var $this = this;
         	
         		calendarLeftVue.clearFlexitimeAppl();
         		calendarLeftVue.applInfo.workRange = '';
         	
         		$('#flexitimeModal').on('hidden.bs.modal',function(){
         			$('#flexitimeModal').off('hidden.bs.modal');
         			$(".list-group-item").removeClass("active");

         			//신청화면 전환
  	         		$("#workRangeInfo").hide();
  	         		$("#workDayInfo").hide();

         			$("#alertText").html("달력에서 근무제 시작일을 선택해주세요.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         			calendarLeftVue.applInfo.useSymd='';
  	         			calendarLeftVue.useYn='Y';
  	         			
 	         			//신청화면 전환
 	         			calendarLeftVue.viewFlexitimeAppl(null);
 	         			
 	         			//선택할 수 있는 근무기간 체크
 	         			monthCalendarVue.getPrevFlexitime();
  	         		});
  	         		$("#alertModal").modal("show"); 
         		});
         		$('#flexitimeModal .close').click();
         	},
         	getFlexibleAppl : function(flexibleAppl){
         		calendarLeftVue.flexibleAppl = flexibleAppl;
         	}
	    }
   	});
   	
   	var calendarLeftVue = new Vue({
   		el: "#calendar_left",
	    data : {
	    	calendar: {},
	    	workTermTime: {}, //선택한 기간의 근무제 정보
	    	useYn: 'N', //근무제 적용 여부
	    	applInfo: { //신청 데이터
	    		flexibleApplId:'',
	    		applId:'',
	    		useSymd:'',
	    		useEymd:'',
	    		workRange:'',
	    		reason:''
	    	},
	    	flexibleAppl: {}, //임시저장된 신청서
	    	selectedDate: '${today}'
  		},
	    mounted: function(){
	    	<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
    			this.flexibleAppl = JSON.parse("${flexibleAppl?js_string}"); //결재된 신청서
    			
    			if('${calendar}' == 'workDayCalendar') {
    				dayCalendarVue.getWorkDayResult(this.flexibleAppl.flexibleEmpId);
    	    	}
    		</#if>
	    },
	    methods : {
	    	getWorkRangeInfo : function(ymd){ //오늘 또는 선택한 기간의 근무제 정보
				var $this = this;
		    		
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
         	getFlexitimeAppl : function(applId){ //신청서 조회
         		var $this = this;
         	
         		var param = {
         			applId: applId
         		};
         		
         		Util.ajax({
					url: "${rc.getContextPath()}/flexibleAppl",
					type: "GET",
					contentType: 'application/json',
					dataType: "json",
					data: param,
					success: function(data) {
						$this.flexibleAppl = {};
						if(data!=null) {
							$this.flexibleAppl = data;
							
							if('${calendar}' == 'workDayCalendar') {
								dayCalendarVue.getWorkDayResult($this.flexibleAppl.flexibleEmpId);
					    	}
						}
						
					},
					error: function(e) {
						$this.flexibleAppl = {};
					}
				});
         	},
         	viewFlexitimeAppl : function(obj){
         		var $this = this;

				$("#workRangeInfo").hide();
	         	$("#workDayInfo").hide();

         		if(obj!=null) {
         			$this.applInfo = obj;
         			$this.applInfo.useSymd = moment(obj.sYmd).format('YYYY-MM-DD');
         			$this.applInfo.useEymd = moment(obj.eYmd).format('YYYY-MM-DD');
         			$this.flexibleAppl = obj;
         			
         			if(obj.applStatusCd!='11') { //결재요청
         				$("#apprBtn").hide();
         				$("#flexibleAppl").find("input,select,textarea").prop("disabled", true);
         			}
         			
         			$this.calendar.gotoDate($this.applInfo.useSymd);
         		} 
         		$("#flexibleAppl").show();
         	},
         	setUsedTermOpt : function(){ //신청서 setting
         		var $this = this;
         	
         		//적용기간은 첫번째 항목으로 기본 세팅
         		if(calendarTopVue.flexibleStd.hasOwnProperty("usedTermOpt") && calendarTopVue.flexibleStd.usedTermOpt!=null) {
         			var workDateRangeItem = calendarTopVue.flexibleStd.usedTermOpt[0]; 
         			
         			if(workDateRangeItem.hasOwnProperty("value")&&workDateRangeItem.value!=null)
         				$this.applInfo.workRange = workDateRangeItem.value;
         				monthCalendarVue.changeWorkRange();
         		}
         		
         	},
         	clearFlexitimeAppl : function(){
         		var $this = this;
         		
         		var workRangeEvent = $this.calendar.getEventById('workRange');
         		if(workRangeEvent!=null)
         			workRangeEvent.remove();
         		
         		$this.applInfo.useSymd = '';
     			$this.applInfo.useEymd = '';
	        },
	        changeUseSymd : function(){
         		var $this = this;
         		
         		if(moment($this.applInfo.useSymd).diff(monthCalendarVue.prevEdate)<=0) {
         			$this.clearFlexitimeAppl();
         			return false;
         		}
		
         		$this.changeWorkRange();
         	},
         	changeWorkRange : function(){
         		monthCalendarVue.changeWorkRange();
         	},
         	changeWorkTime : function(){
         		dayCalendarVue.changeWorkTime();
         	},
         	flexitimeApplImsi : function(){ //임시저장
         		var $this = this;
  	         	
         		//선택한 근무제
         		var flexibleStd = calendarTopVue.flexibleStd;
         		
         		if(flexibleStd.workTypeCd.indexOf('SELE')==0) {
        			var param = {
        				flexibleStdMgrId : flexibleStd.flexibleStdMgrId,
        				workTypeCd : flexibleStd.workTypeCd,
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
							if(data!=null && data.status=='OK') {
								$this.applInfo.applId = data.applId;
								$this.applInfo.flexibleApplId = data.flexibleApplId;
								
								//신청서 조회
								$this.getFlexitimeAppl(data.applId);
							}
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("저장 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					}); 
         		} else if(flexibleStd.workTypeCd.indexOf('ELAS')==0){
         			
         		}

         	},
         	flexitimeAppl : function(){ //확인요청
	         	var $this = this;
	  	         	
	         	//선택한 근무제
	         	var flexibleStd = calendarTopVue.flexibleStd;
	         	
	         	//임시저장된 신청서
	         	var flexibleAppl = $this.flexibleAppl;
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
					
         		if(flexibleStd.workTypeCd.indexOf('SELE')==0) {
					if(saveYn) {
						var param = {
							flexibleApplId : flexibleAppl.flexibleApplId,	
							applId : flexibleAppl.applId,
  	         				flexibleStdMgrId : flexibleAppl.flexibleStdMgrId,
  	         				workTypeCd : flexibleStd.workTypeCd,
  	         				//empNo : "${empNo}",
		   		    		sYmd : moment($this.applInfo.useSymd).format('YYYYMMDD'),
		   		    		eYmd : moment($this.applInfo.useEymd).format('YYYYMMDD'),
		   		    		reason: applInfo.reason
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
									$("#alertText").html("확인요청 시 오류가 발생했습니다.");
								}
								$("#alertModal").on('hidden.bs.modal',function(){
									location.reload();
								});
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
	         	} else if(flexibleStd.workTypeCd.indexOf('ELAS')==0){
	         		$("#flexibleAppl").hide();
	         	}
	        },
         	saveWorkDayResult: function(){
         		dayCalendarVue.saveWorkDayResult();
         	},
         	viewWorkTimeCalendar: function(){
         		if(this.selectedDate!='') {
         			location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Time&date='+moment(this.selectedDate).format('YYYYMMDD');
         		} else {
         			$("#alertText").html("달력에서 일자를 선택해 주세요.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         		});
  	         		$("#alertModal").modal("show"); 
         		}
         	}
	    }
   	});
   	
   	$('#flexitimeModal').on('hidden.bs.modal',function(){
   		$(".list-group-item").removeClass("active");
   		monthCalendarVue.prevEdate = '';
   	});
   	
</script>

