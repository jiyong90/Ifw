<div id="timeCalendar" class="calendar-wrap" v-cloak>
	<!-- 연장근무신청 modal start -->
    <div class="modal fade show" id="overtimeAppl" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title" v-if="result.holidayYn!='Y'">연장근로신청</h5>
                    <h5 class="modal-title" v-else>휴일근로신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="desc row">
                                    <div class="col-sm-12 col-md-12 col-lg-2">
                                        <div class="title" id="overtime" v-if="result.holidayYn!='Y'">연장근로시간</div>
                                        <div class="title" id="overtime" v-else>휴일근로시간</div>
                                        <span class="time-wrap">
                                            <i class="fas fa-clock"></i>
                                            <span class="time" v-if="overtime.calcHour">{{overtime.calcHour}}</span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control  datetimepicker-input form-control-sm mr-2" id="sDate" data-toggle="datetimepicker" data-target="#sDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sTime" data-toggle="datetimepicker" data-target="#sTime" autocomplete="off" required>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control  datetimepicker-input form-control-sm mr-2" id="eDate" data-toggle="datetimepicker" data-target="#eDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eTime" data-toggle="datetimepicker" data-target="#eTime" autocomplete="off" required>
                                            </div>
                                        </div>
                                        <div class="guide" v-if="overtime.breakHour">*해당일 총 휴게시간은 {{overtime.breakHour}} 입니다.</div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="reasonCd">사유구분</label>
                                    <select id="reasonCd" class="form-control" required>
                                        <option value="" disabled selected hidden>사유를 선택해주세요.</option>
                                        <option :value="reason.codeCd" v-for="reason in reasons">{{reason.codeNm}}</option>
                                    </select>
                                </div>
                                <div class="form-group col-12">
                                    <label for="reason">설명</label>
                                    <textarea class="form-control" id="reason" rows="3"
                                        placeholder="팀장 확인 시에 필요합니다." required></textarea>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="result.holidayYn=='Y'">
                                <div class="title mb-2">휴일대체방법</div>
                                <div class="desc">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="subYnY" name="subYn" class="custom-control-input" value="Y" @change="changeSubYn($event.target.value)" :required="subsRequired">
                                        <label class="custom-control-label" for="subYnY">휴일대체</label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="subYnN" name="subYn" class="custom-control-input" value="N" @change="changeSubYn($event.target.value)" :required="subsRequired">
                                        <label class="custom-control-label" for="subYnN">위로금/시급지급</label>
                                    </div>
                                </div>
                            </div>
                            <div class="radio-toggle-wrap" style="display:none;">
                                <hr>
                                <!--  
                                <div class="big-title" id="subTitle" v-if="overtime">{{calendarLeftVue.minuteToHHMM(overtime, 'detail')}}의 대체 휴일을 지정하세요.</div>
                               	-->
                                <!--  
                                <div class="info-box clearfix mt-2" v-if="prevOtSubs.length>0">
                                    <div class="title">이전에 신청한 휴일</div>
                                    <template v-for="s in prevOtSubs">
                                    <div class="desc">{{moment(s.sDate).format('YYYY-MM-DD HH:mm')}} ~ {{moment(s.eDate).format('YYYY-MM-DD HH:mm')}}({{calendarLeftVue.minuteToHHMM(s.otMinute,'detail')}})
                                    	<span class="guide d-sm-block">
                                    		해당일 근무시간 {{moment(moment(s.ymd).format('YYYYMMDD')+' '+s.workSdateShm).format('HH:mm')}}~{{moment(moment(s.ymd).format('YYYYMMDD')+' '+s.workSdateEhm).format('HH:mm')}}
                                    	</span>
                                    </div>
                                    </template>
                                </div>
                                -->
                                <div class="inner-wrap">
                                    <div class="title">대체일시</div>
                                    <div class="desc row" v-for="(s, idx) in subYmds">
                                    	<div class="col-12 col-lg-12 mb-sm-2">
                                    		<div class="form-row">
			                                    <div class="col-md-12 col-lg-2">
		                                            <span class="time-wrap">
		                                                <i class="fas fa-clock"></i>
		                                                <span class="time" v-if="s.subsMinute">{{calendarLeftVue.minuteToHHMM(s.subsMinute, 'detail')}}</span>
			                                            <span class="time" v-else>0시간</span>
		                                            </span>
		                                        </div>
			                                    <div class="form-row col-md-12 col-lg-10 mb-sm-2">
			                                    	<!--  
	                                             	<div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsSymd_'+idx" v-model="s.subsSymd" data-toggle="datetimepicker" :data-target="'#subsSymd_'+idx" placeholder="연도-월-일" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <div class="col-2 col-sm-2 col-md-2 col-lg-2" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsShm_'+idx" v-model="s.subsShm" data-toggle="datetimepicker" :data-target="'#subsShm_'+idx" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <span class="col-1 col-sm-1 col-md-1 col-lg-1 text-center pl-2 pr-2 mt-1">~</span>
	                                                <div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsEymd_'+idx" v-model="s.subsEymd" data-toggle="datetimepicker" :data-target="'#subsEymd_'+idx" placeholder="연도-월-일" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <div class="col-2 col-sm-2 col-md-2 col-lg-2" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsEhm_'+idx" v-model="s.subsEhm" data-toggle="datetimepicker" :data-target="'#subsEhm_'+idx" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <div class="col-1 col-sm-1 col-md-1 col-lg-1 col-md-text-right text-center"><a href="#" class="align-middle" @click="delSubYmd(idx)">삭제</a></div>
		                                            <div class="guide pl-1">
		                                            	<span></span>
		                                            	<span v-if="s.subsBreakMinute"> 총 휴게시간은 {{calendarLeftVue.minuteToHHMM(s.subsBreakMinute, 'detail')}} 입니다.</span>
		                                            </div>
		                                            -->
		                                            <div class="col-4 col-sm-4 col-md-4 col-lg-4" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsSymd_'+idx" v-model="s.subsSymd" data-toggle="datetimepicker" :data-target="'#subsSymd_'+idx" placeholder="연도-월-일" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsShm_'+idx" v-model="s.subsShm" data-toggle="datetimepicker" :data-target="'#subsShm_'+idx" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <span class="col-1 col-sm-1 col-md-1 col-lg-1 text-center pl-2 pr-2 mt-1">~</span>
	                                                <!--  
	                                                <div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsEymd_'+idx" v-model="s.subsEymd" data-toggle="datetimepicker" :data-target="'#subsEymd_'+idx" placeholder="연도-월-일" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                -->
	                                                <div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
	                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsEhm_'+idx" v-model="s.subsEhm" data-toggle="datetimepicker" :data-target="'#subsEhm_'+idx" autocomplete="off" :required="subsRequired">
	                                                </div>
	                                                <div class="col-1 col-sm-1 col-md-1 col-lg-1 col-md-text-right text-center"><a href="#" class="align-middle" @click="delSubYmd(idx)">삭제</a></div>
		                                            <div class="guide pl-1">
		                                            	<span v-if="s.workShm && s.workEhm">*해당일 근무시간은 {{s.workShm}}~{{s.workEhm}} 입니다.</span>
		                                            	<span v-if="s.subsBreakMinute"> 총 휴게시간은 {{calendarLeftVue.minuteToHHMM(s.subsBreakMinute, 'detail')}} 입니다.</span>
		                                            </div>
		                                        </div>
	                                        </div>
	                                	</div>
                                	</div>
                                </div>
                                <div class="btn-wrap text-center">
                                    <button type="button" class="btn btn-inline btn-plus" @click="addSubYmd"><i class="fas fa-plus"></i>대체일시 추가</button>
                                </div>
                            </div>
                            <!-- 회사별 옵션에 따라 대체 휴일 지정하는 방법 다르게 -->
                            <!-- 
                            <div class="radio-toggle-wrap" style="display:none;">
                                <hr>
                                <div class="big-title">8시간의 대체 휴일을 지정하세요.</div>
                                <div class="info-box clearfix mt-2">
                                    <div class="title">이전에 신청한 휴일</div>
                                    <div class="desc">2019.06.18(금) 13:00~17:00(4시간) <span class="guide d-sm-block">해당일 근무시간 09:00~12:00</span></div>
                                    <div class="desc">2019.06.18(금) 13:00~17:00(4시간) <span class="guide d-sm-block">해당일 근무시간 09:00~12:00</span></div>
                                </div>
                                <div class="inner-wrap">
                                    <div class="title">대체일자</div>
                                    <div class="desc">
                                        <div class="form-group clearfix">
                                            <div class="form-row ">
                                                <div class="col-md-6 col-lg" data-target-input="nearest">
                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" data-toggle="datetimepicker"  placeholder="연도-월-일" autocomplete="off" required>
                                                </div>
                                                <div class="col-md-6 col-lg mt-xs-1 mt-sm-0 mt-md-1 mt-lg-0 float-right">
                                                    <div class="custom-control custom-radio custom-control-inline mt-sm-1 mt-md-0 mt-lg-1">
                                                        <input type="radio" id="halfAM" name="halfType"
                                                            class="custom-control-input">
                                                        <label class="custom-control-label" for="amWork">오전반차</label>
                                                    </div>
                                                    <div class="custom-control custom-radio custom-control-inline mt-sm-1 mt-md-0 mt-lg-1">
                                                        <input type="radio" id="halfPM" name="halfType"
                                                            class="custom-control-input">
                                                        <label class="custom-control-label" for="halfPM">오후반차</label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="guide">*해당일 근무시간은 09:00~18:00 입니다.</div>
                                    </div>
                                </div>
                            </div>
                            -->
                        </div>
                        <div class="btn-wrap text-center">
                            <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-default rounded-0" @click="validateOtAppl">확인요청</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>    
    <!-- 연장근무신청 modal end -->
    <!-- 연장근무취소사유 modal start -->
    <div class="modal fade show" id="cancelOpinionModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">취소 사유</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="reason">취소 사유</label>
                                    <textarea class="form-control" id="cancelOpinion" rows="3" placeholder="취소 사유를 작성해주시기 바랍니다."
                                        required=""></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="btn-wrap text-center">
                            <button type="button" class="btn btn-secondary rounded-0"
                                data-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-default rounded-0" @click="otCancelAppl">확인</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 연장근무취소사유 modal end -->
    <!-- 연장근무신청 상세보기 modal start -->
    <div class="modal fade show" id="overtimeApplDetail" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                	<template v-if="overtimeAppl.otCanApplId!=null&&overtimeAppl.otCanApplId!=undefined&&overtimeAppl.otCanApplId!=''">
                		<h5 class="modal-title">연장근로 취소신청</h5>
                	</template>
                	<template v-else="">
                		<h5 class="modal-title" v-if="result.holidayYn!='Y'">연장근로신청</h5>
                    	<h5 class="modal-title" v-else>휴일근로신청</h5>
                	</template>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                        	<div class="inner-wrap">
                        		<div class="row">
	                        		<div class="col-4 col-sm-4 col-xl-4" v-for="l in overtimeAppl.applLine">
					                    <div class="title">{{l.apprTypeNm}}</div>
					                    <div class="desc">{{l.empNm}}({{l.sabun}})</div>
					                </div>
				                </div>
                        	</div>
                        	<hr class="bar">
                            <div class="inner-wrap">
                                <div class="title" v-if="result.holidayYn!='Y'">연장근로시간</div>
                                <div class="title" v-else>휴일근로시간</div>
                                <div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="overtimeAppl.otMinute">
                                        		{{calendarLeftVue.minuteToHHMM(overtimeAppl.otMinute, 'detail')}}
                                        	</template>
                                        </span>
                                    </span>
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="overtimeAppl.otSdate">
                                        	{{moment(overtimeAppl.otSdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="start-time">
                                        	<template v-if="overtimeAppl.otSdate">
                                        	{{moment(overtimeAppl.otSdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="overtimeAppl.otEdate">
                                        	{{moment(overtimeAppl.otEdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="end-time">
                                        	<template v-if="overtimeAppl.otEdate">
                                        	{{moment(overtimeAppl.otEdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유구분</div>
                                <div class="desc">
                                	<template v-if="overtimeAppl.reasonNm">
                                	{{overtimeAppl.reasonNm}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">설명</div>
                                <div class="desc">
                                	<template v-if="overtimeAppl.reason">
                                	{{overtimeAppl.reason}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="result.holidayYn=='Y'">
                                <div class="title">휴일대체방법</div>
                                <div class="desc">
                                	<template v-if="overtimeAppl.subYn">
                                	{{overtimeAppl.subYn=='Y'?'휴일대체':'수당지급'}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="result.holidayYn=='Y'">
                                <div class="title">대체일시</div>
                                <template v-if="overtimeAppl.subs" v-for="sub in overtimeAppl.subs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{calendarLeftVue.minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc">*해당일 근무시간은 {{moment(sub.sDate).format('HH:mm')}}~{{moment(sub.eDate).format('HH:mm')}} 입니다.</div>
                                </template>
                            </div>
                            <div class="inner-wrap" v-if="overtimeAppl.cancelReason">
                                <div class="title">취소사유</div>
                                <div class="desc">
                                	{{overtimeAppl.cancelReason}}
                                </div>
                            </div>
                            <hr class="bar">
                        </div>
                        <div class="btn-wrap text-center" v-if="overtimeAppl.otCanApplId==null||overtimeAppl.otCanApplId==undefined||overtimeAppl.otCanApplId==''">
                            <button type="button" class="btn btn-default rounded-0" v-if="result.holidayYn!='Y'" data-toggle="modal" data-target="#cancelOpinionModal">연장근로신청 취소하기</button>
                            <button type="button" class="btn btn-default rounded-0" v-else data-toggle="modal" data-target="#cancelOpinionModal">휴일근로신청 취소하기</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 연장근무신청 상세보기 modal end -->
    <div id='calendar-container'>
		<full-calendar ref="fullCalendar" :header="header" :defaultview="view" :defaultdate="workday" :nowindicator="t" :scrolltime="moment(new Date()).format('HH:mm:ss')" @update="renderCallback" @datesrender="datesRenderCallback" @dateclick="dateClickCallback" @select="selectCallback" @eventclick="eventClickCallback"></full-calendar>
    </div>
</div>
<script type="text/javascript">
	$(function () {
		$('#sDate, #eDate').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		
        $('#sTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#eTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        
		/* if(calendarLeftVue.rangeInfo!=null && Object.keys(calendarLeftVue.rangeInfo).length>0) {
			var unitMinute = calendarLeftVue.rangeInfo.unitMinute;
			if(unitMinute!=null && unitMinute!=undefined && unitMinute!='') {
				$('#sTime').datetimepicker('stepping',Number(unitMinute));
				$('#eTime').datetimepicker('stepping',Number(unitMinute));
			}	
		} */
        
	});

   	var timeCalendarVue = new Vue({
   		el: "#timeCalendar",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
  		    	t: true,
  		    	header: {
  		    		left: 'prev,next',
			        center: 'title',
			        right: ''
  		    	},
  		    	view: 'timeGridDay',
  		    	result: {}, //일근무시간
  		    	workday: '${today}', //근무일
  		    	reasons: [], //연장/휴일 근로 사유
  		    	subYmds: [], //대체휴일
  		    	overtime: {}, //연장/휴일 근로시간, 휴게시간
  		    	overtimeAppl: {},
  		    	applCode: {} //신청서 정보
  		    	//prevOtSubs: [] //이전에 신청한 휴일
  		    },
  		    computed: {
  		    	subsRequired: function(val, oldVal) {
  		    		return this.result.holidayYn=='Y'?true:false;
  		    	}
  		    },
  		    mounted: function(){
  		    	<#if workday?? && workday!='' && workday?exists >
  		    		this.workday = moment('${workday}').format('YYYY-MM-DD');
  		    	<#else>
  		    		this.workday = '${today}';
  		    	</#if>
  		    	
  		    	<#if reasons?? && reasons!='' && reasons?exists >
		    		this.reasons = JSON.parse('${reasons?js_string}');
		    	</#if>
  		    	
  		    	//this.getFlexibleDayInfo(this.workday);
  		    	
  		    	//근무일 화면 전환
         		$("#workRangeInfo").show();
         		$("#flexibleDayInfo").show();
         		
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		calendarLeftVue.calendar = calendar;
  		    		
  		    		//신청서 정보
  		    		this.getApplCode();
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'timeGridDay') { //month change
  		    			var ymd = moment(calendar.getDate()).format('YYYYMMDD');
  		    			
  		    			//선택한 기간의 근무제 정보(남색 박스)
  		    			calendarLeftVue.getFlexibleRangeInfo(ymd);
  		    			
  		    			//선택한 날의 근무일 정보(흰색 박스)
  		    			$this.getFlexibleDayInfo(ymd);
  		    			
  		    			//선택한 날의 근무시간 정보
  		    			$this.getDayResults(ymd);
	   		    		
	  		    	}
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
					
  		    		
  		    	},
  		    	eventClickCallback : function(info){
  		    		//상세보기
  		    		if(info.event.extendedProps.timeTypeCd=='OT') {
  		    			this.viewOvertimeApplDetail(info.event.extendedProps.applId);
  		    		}
  		    	},
  		    	dateClickCallback : function(info){
  		    		if(!info.allDay)
  		    			this.preCheck(info);
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
  	         	},
  	         	getApplCode: function(){ //신청서 정보
					var $this = this;
		    		
  					var param = {
  	   		    		applCd : 'OT'
  	   		    	};
  			    		
  			    	Util.ajax({
  						url: "${rc.getContextPath()}/appl/code",
  						type: "GET",
  						contentType: 'application/json',
  						data: param,
  						dataType: "json",
  						success: function(data) {
  							$this.applCode = {};
  							if(data!=null) {
  								$this.applCode = data;
  								
  								if(data.useMinutes!=null && data.useMinutes!=undefined && data.useMinutes!=''){
  									var useMinutes = Number(data.useMinutes);
									$('#sTime').datetimepicker('stepping', useMinutes);
									$('#eTime').datetimepicker('stepping', useMinutes);
  								}
  								
  							}
  						},
  						error: function(e) {
  							$this.applCode = {};
  						}
  					});
  	         	},
  	         	getFlexibleDayInfo: function(ymd){ //근무일 정보
  	         		var $this = this;
		    		
  					var param = {
  	   		    		ymd : moment(ymd).format('YYYYMMDD')
  	   		    	};
  			    		
  			    	Util.ajax({
  						url: "${rc.getContextPath()}/flexibleEmp/worktime",
  						type: "GET",
  						contentType: 'application/json',
  						data: param,
  						dataType: "json",
  						success: function(data) {
  							calendarLeftVue.workTimeInfo = {};
  							if(data!=null) {
  								calendarLeftVue.workTimeInfo = data;
  							}
  						},
  						error: function(e) {
  							calendarLeftVue.workTimeInfo = {};
  						}
  					});
  	         	},
  	         	getDayResults: function(ymd){ //근무시간
  	         		var $this = this;
  	         		var param = {
	   		    		ymd : ymd
	   		    	};
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/dayResults",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							//console.log(data);
							if(data!=null) {
								$this.result = data;
								calendarLeftVue.holidayYn = $this.result.holidayYn;
								
								$this.viewDayResults(ymd);
							}
						},
						error: function(e) {
							console.log(e);
							$this.result = {};
						}
					});
  	         	},
  	         	/* getOtSubs: function(ymd){
  	         		var $this = this;
  	         		var param = {
	   		    		ymd : ymd
	   		    	};
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/otAppl/subs/prev",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							if(data!=null) {
								$this.prevOtSubs = data;
								console.log(data);
							}
						},
						error: function(e) {
							console.log(e);
							$this.prevOtSubs = [];
						}
					});
  	         	}, */
  	         	calcMinute: function(ymd, shm, ehm){
  	         		var $this = this;
  	         		var result = {};
  	         		if(ymd!=null && ymd!=undefined && ymd!=''
  	         				&& shm!=null && shm!=undefined && shm!='' && ehm!=null && ehm!=undefined && ehm!='') {
	  	         		
	  	         		var param = {
	  	         			ymd: ymd,
		   		    		shm : shm,
		   		    		ehm : ehm
		   		    	};
	   		    		
	   		    		Util.ajax({
							url: "${rc.getContextPath()}/flexibleEmp/workHm",
							type: "GET",
							contentType: 'application/json',
							data: param,
							dataType: "json",
							async: false,
							success: function(data) {
								if(data!=null) {
									result = data;
									
									if(data.hasOwnProperty('calcMinute')) {
										result['calcHour'] = calendarLeftVue.minuteToHHMM(data.calcMinute, 'detail');
									}
									if(data.hasOwnProperty('breakMinute')) {
										result['breakHour'] = calendarLeftVue.minuteToHHMM(data.breakMinute, 'detail');
									}
								} 
							},
							error: function(e) {
								result = {};
							}
						});
	   		    		
  	         		}
  	         		return result;
  	         	},		
  	         	getWorkHour: function(id, ymd){
  	         		var $this = this;
  	         		var param = {
	   		    		ymd : ymd
	   		    	};
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/worktime",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							if(data!=null && Object.keys(data).length>0) {
								
								//if(id.indexOf('subsSymd')!=-1) 
								//	$("#"+id).closest(".form-row").children("div.guide.pl-1").children(":eq(0)").text("");
								
								if(data.holidayYn && data.holidayYn=='Y') {
									$("#alertText").html("휴일을 대체일시로 선택할 수 없습니다.");
			  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	  	         			$("#alertModal").off('hidden.bs.modal');
			  	  	         			$("#"+id).val("");
			  	  	         			$this.updateValue(id, '');
			  	  	         		});
			  	  	         		$("#alertModal").modal("show"); 
									
								} else {
									$this.updateValue(id, moment(ymd).format('YYYY-MM-DD'));
									
									//근무 시간 세팅
									//var workShm = moment(data.sDate).format('HH:mm');
									//var workEhm = moment(data.eDate).format('HH:mm');
									var workShm = moment(data.planSdate).format('HH:mm');
									var workEhm = moment(data.planEdate).format('HH:mm');
									if(id.indexOf('subsSymd')!=-1) {
										//$("#"+id).closest(".form-row").children("div.guide.pl-1").children(":eq(0)").text("*해당일 근무시간은 " + workShm+ "~" + workEhm + " 입니다.");
										
										var key = id.split('_');
					  	 				var idx;
					  	 				if(key!=null && key!='undefined' && key.length>0) 
					  	 					idx = key[1];
					  	 				
					  	 				if(idx!=null && idx!='' && idx!=undefined) {
					  	 					$this.subYmds[idx]['workShm'] = workShm;
					  	 					$this.subYmds[idx]['workEhm'] = workEhm;
					  	 				}
									}
								}
							} else {
								$this.updateValue(id, moment(ymd).format('YYYY-MM-DD'));
							}
						},
						error: function(e) {
							$this.updateValue(id, moment(ymd).format('YYYY-MM-DD'));
							//if(id.indexOf('subsSymd')!=-1) 
							//	$("#"+id).closest(".form-row").children("div.guide.pl-1").children(":eq(0)").text("");
						}
					});
  	         	},
  	         	viewOvertimeAppl: function(date){
  	         		var $this = this;
  	         		
  	         		//1시간 값 세팅
					var sYmd = new Date(date);
					var eYmd = new Date(date);
					var baseEdate = null;
					
  	         		if($this.result!=null && $this.result.dayResults!=null && $this.result.dayResults!=undefined && $this.result.dayResults!='') {
  	         			var dayResults = JSON.parse($this.result.dayResults);
  	         		
  	         			dayResults.map(function(dayResult){
  	         				if(dayResult.timeTypeCd == 'BASE' || dayResult.timeTypeCd == 'OT') {
  	         					if(baseEdate==null || baseEdate < dayResult.eDate) {
  	         						baseEdate = dayResult.eDate;
  	         					} 
  	         				}
  	         			});
  	         		} 
  	         		
  	         		if(baseEdate!=null) {
  	         			sYmd = new Date(baseEdate);
  	         			eYmd = new Date(baseEdate);
  	         		}
  	         		
  	         		if($this.applCode!=null && $this.applCode.timeUnit!=null && $this.applCode.timeUnit!=undefined && $this.applCode.timeUnit!='') {
  	         			var timeUnit = Number($this.applCode.timeUnit);
  	         			eYmd.setMinutes(sYmd.getMinutes()+timeUnit);
  	         		} else {
  	         			eYmd.setHours(sYmd.getHours()+1);
  	         		}
					
					$("#sDate").val(moment(sYmd).format('YYYY-MM-DD'));
					$("#eDate").val(moment(eYmd).format('YYYY-MM-DD'));
					$("#sTime").val(moment(sYmd).format('HH:mm'));
					$("#eTime").val(moment(eYmd).format('HH:mm'));
					
					$this.overtime = $this.calcMinute(moment($this.workday).format('YYYYMMDD'), moment(sYmd).format('HHmm'), moment(eYmd).format('HHmm'));
					
					//휴일근로신청의 경우 이전에 신청한 휴일 가져옴
					/* if(Object.keys($this.result).length>0 && $this.result.hasOwnProperty('holidayYn')
							&& $this.result.holidayYn!=null && $this.result.holidayYn=='Y') {
						$this.getOtSubs(moment(sYmd).format('YYYYMMDD'));
					} */
					
					var param = {
						d : moment($this.workday).format('YYYYMMDD')
					};
					
					//결재라인
					Util.ajax({
						url: "${rc.getContextPath()}/otAppl/line",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							$("#overtimeAppl").modal("show"); 
						},
						error: function(e) {
							console.log(e);
						}
					});
					
  	         	},
  	         	viewOvertimeApplDetail: function(applId){
  	         		var $this = this;
  	         		
  	         		var param = {
  	         			applId: applId	
  	         		};
  	         		
  	         		Util.ajax({
						url: "${rc.getContextPath()}/otAppl",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							if(data!=null) {
								$this.overtimeAppl = data;
								$("#overtimeApplDetail").modal("show"); 
							}
						},
						error: function(e) {
							console.log(e);
							$this.overtimeAppl = {};
						}
					});
					
  	         	},
  	         	viewDayResults: function(ymd){
  	         		var $this = this;
  	         		var classNames = [];
  	         		
  	         		if($this.result!=null && Object.keys($this.result).length>0) {
  	         			//출퇴근 타각 표시
  	         			if($this.result.hasOwnProperty('entry')) {
  	         				var entry = $this.result.entry;
  	         				
  	         				var sEdate = new Date(entry.entrySdate);
  	         				sEdate.setMinutes(sEdate.getMinutes()+3);
  	         				
  	         				var eEdate = new Date(entry.entryEdate);
  	         				eEdate.setMinutes(eEdate.getMinutes()+3);
  	         				
  	         				classNames = [];
							classNames.push('ENTRY');
  	         				
  	         				var sEntry = {
 	         					id: 'entrySdate',
 	         					title: '출근 ' + entry.entrySdate,
								start: entry.entrySdate,
	  		  		        	end: moment(sEdate).format('YYYY-MM-DDTHH:mm:ss'),
	  		  		        	editable: false,
	  		  		        	classNames: classNames
  	         				};
  	         				$this.addEvent(sEntry);
  	         				
  	         				var eEntry = {
 	         					id: 'entryEdate',
 	         					title: '퇴근 ' + entry.entryEdate,
								start: entry.entryEdate,
	  		  		        	end: moment(eEdate).format('YYYY-MM-DDTHH:mm:ss'),
	  		  		        	editable: false,
	  		  		        	classNames: classNames
  	         				};
  	         				$this.addEvent(eEntry); 
  	         			}
  	         			
  	         			//근태 및 근무시간
  	         			if($this.result.hasOwnProperty('dayResults') && $this.result.dayResults!=null && $this.result.dayResults!='') {
  	         				var dayResults = JSON.parse($this.result.dayResults);
  	         				//console.log(dayResults);
         					dayResults.map(function(vMap){
         						if(vMap.hasOwnProperty('taaCd') && vMap.taaCd!='') {
	  	         					//근태
	  	         					classNames = [];
									classNames.push('TAA');
	  	         					
	  	         					var result = {
  	  	   	         					id: 'TAA.'+vMap.taaCd,
  	  	   	         					title: vMap.taaNm,
  	  	  								start: vMap.sDate,
  	  	  	  		  		        	end: vMap.eDate,
  	  	  	  		  		        	editable: false,
  	  	  		  		        		classNames: classNames
  	  	    	         			};
	  	  	         					
	  	  	    	         		$this.addEvent(result); 
	  	         				} else {
	  	         					//근무
	  	         					classNames = [];
	  	         					
	  	         					if(vMap.timeTypeCd == 'SUBS')
	  	         						classNames.push('TAA');
	  	         					else
										classNames.push(vMap.timeTypeCd);
									
									var title = vMap.timeTypeNm;
									if(vMap.otCanApplId!=null && vMap.otCanApplId!=undefined && vMap.otCanApplId!='')
										title += ' 취소 (결재처리중)';
									
									if(vMap.applStatusCd!=null && vMap.applStatusCd!=undefined && vMap.applStatusCd!='' && vMap.applStatusCd!='99')
										title += ' (' + vMap.applStatusNm + ')';
										
  	  	         					var result = {
  	  	   	         					id: 'TIME.'+vMap.timeTypeCd+'.'+moment(vMap.sDate).format('HH:mm:ss'),
  	  	   	         					title: title,
  	  	  								start: vMap.sDate,
  	  	  	  		  		        	end: vMap.eDate,
  	  	  	  		  		        	editable: false,
  	  	  		  		        		classNames: classNames,
			  	  	  		  		    extendedProps: {
			  	  	  		  		    	applId: vMap.applId,
			  	  	  		  		    	timeTypeCd: vMap.timeTypeCd
			  	  	  		  		  	}
  	  	    	         			};
  	  	         					
  	  	    	         			$this.addEvent(result); 
	  	         				}
         					});
  	         			}
  	         		}
  	         	},
  	         	preCheck : function(info){ //소정근로 선 소진 여부, 연장근무 가능한지 체크
  	         		var $this = this;
  	         		
  	         		var param = {
  	         			ymd: moment($this.workday).format('YYYYMMDD'),
  	         			workTypeCd: ''
  	         		};
  	         		
  	         		//연장근무
  	         		Util.ajax({
						url: "${rc.getContextPath()}/otAppl/preCheck",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							if(data!=null && data.status=='OK') {
								$this.viewOvertimeAppl(info.date);
							} else {
								$("#alertText").html(data.message);
			  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	         			$("#alertModal").off('hidden.bs.modal');
			  	         		});
			  	         		$("#alertModal").modal("show"); 
							}
						},
						error: function(e) {
							console.log(e);
						}
					});
  	         	},
  	         	validateOtAppl : function(){
  	         		var $this = this;
  	         		var isValid = true;
  	         		var forms = document.getElementById('overtimeAppl').getElementsByClassName('needs-validation');
  	         		var validation = Array.prototype.filter.call(forms, function(form) {
  	         			if (form.checkValidity() === false) {
  	         				isValid = false;
  	         				event.preventDefault();
  	         		        event.stopPropagation();
  	         			}
  	         			form.classList.add('was-validated');
  	         		});
  	         		
  	         		if(isValid) {
  	         			var msg = '';
  	         			
  	         			//신청하려는 ot시간이 소정근무시간에 해당되지 않는지 체크
  	         			var sDate = $("#sDate").val().replace(/-/gi,"");
			   			var eDate = $("#eDate").val().replace(/-/gi,"");
			   			var sTime = $("#sTime").val().replace(/:/gi,"");
			   			var eTime = $("#eTime").val().replace(/:/gi,"");
			   			
			   			var otSdate = moment(sDate+' '+sTime).format('YYYYMMDD HHmm');
			         	var otEdate = moment(eDate+' '+eTime).format('YYYYMMDD HHmm');
			         	
			         	var applCode = $this.applCode;
			       		
		       			//신청 가능 시간
		     			var inShm = moment(sDate+' '+applCode.inShm).format('YYYYMMDD HHmm');
		     			var inEhm = moment(eDate+' '+applCode.inEhm).format('YYYYMMDD HHmm');
		       			
		     			if(moment(otSdate).diff(inShm)<0 || moment(otEdate).diff(inEhm)>0) {
		     				isValid = false;
		       				var shm =  moment(inShm).format('HH:mm');
		       				var ehm =  moment(inEhm).format('HH:mm');
		       				msg = '근무 가능 시간은 '+shm+'~'+ehm+' 입니다.';
		       				$("#sTime").val('');
  	  	         			$("#eTime").val('');
		   				}
		     				
		       			//신청 시간 단위
		       			if(applCode.timeUnit!=null && applCode.timeUnit!=undefined && applCode.timeUnit!='')
		       				var timeUnit = Number(applCode.timeUnit);
		       			
		       			var time = Number(moment(otEdate).diff(otSdate,'minutes'));
		       			if(time % timeUnit != 0) {
		       				isValid = false;
		       				msg = '근무시간은 '+timeUnit+'분 단위로 신청 가능합니다.';
		       				$("#sTime").val('');
  	  	         			$("#eTime").val('');
		       			} 
			         	
			         	if(moment(otEdate).diff(otSdate)<0) {
			         		isValid = false;
			         		msg = "종료일이 시작일보다 작습니다.";
			         		$("#sTime").val('');
  	  	         			$("#eTime").val('');
			         	}
  	  	         		
			         	if(isValid) {
	  	  	         		if($this.result.hasOwnProperty('dayResults') && $this.result.dayResults!=null && $this.result.dayResults!='') {
		         				var dayResults = JSON.parse($this.result.dayResults);
	     						dayResults.map(function(dayResult){
		  	  	         			//if(dayResult.timeTypeCd == 'BASE'){
			  	  	         			var workSdate = moment(dayResult.sDate).format('YYYY-MM-DD HH:mm');
				  	         			var workEdate = moment(dayResult.eDate).format('YYYY-MM-DD HH:mm');
				  	         			if(moment(workSdate).diff(otSdate)<=0 && moment(otSdate).diff(workEdate)<0 
				  	         					|| moment(workSdate).diff(otEdate)<0 && moment(otEdate).diff(workEdate)<0 ) {
				  	         				isValid = false;
				  	         				msg = '이미 근무정보(신청중인 근무 포함)가 존재합니다.';
				  	         				$("#sTime").val('');
				  	  	         			$("#eTime").val('');
				  	         			}
				  	         				
		  	  	         			//}
		  	  	         		});
	  	  	         		}
			         	}
  	  	         		
  	  	         		if(isValid) {
	  	  	         		//신청하려는 휴일근로시간 = 대체일시 합산 시간
	  	  	         		if($this.result.hasOwnProperty('holidayYn') && $this.result.holidayYn!=null && $this.result.holidayYn=='Y') {
	  	  	         			
	  	  	         			if($this.subYmds!=null && $this.subYmds.length>0) {
	  	  	         				var subsMin = 0;
	  	  	         				
		  	  	         			$this.subYmds.map(function(sub){
		  	  	         				//대체일시 근무시간 합산
		  	  	         				if(sub.hasOwnProperty("subsMinute")) {
		  	  	         					var min = sub.subsMinute;
		  	  	         					subsMin += min;
		  	  	         				}
		  	  	         				
		  	  	         				if(isValid) {
			  	  	         				//대체 시간이 근무시간에 포함되는지 확인
			  	  	         				if(sub.hasOwnProperty("workShm") && sub.hasOwnProperty("workEhm")) {
			  	  	         					var workSdate = moment(sub.subsSymd+" "+sub.workShm).format('YYYY-MM-DD HH:mm');
			  	  	         					var workEdate = moment(sub.subsSymd+" "+sub.workEhm).format('YYYY-MM-DD HH:mm');
				  	  	         				var subSdate = moment(sub.subsSymd+" "+sub.subsShm).format('YYYY-MM-DD HH:mm');
			  	  	         					var subEdate = moment(sub.subsSymd+" "+sub.subsEhm).format('YYYY-MM-DD HH:mm');
			  	  	         					
			  	  	         					if(!(moment(workSdate).diff(subSdate)<=0 && moment(subSdate).diff(workEdate)<=0 
			  	  	         							&& moment(workSdate).diff(subEdate)<=0 && moment(subEdate).diff(workEdate)<=0)) {
			  	  	         						isValid = false;
			  	  	         						msg = sub.subsSymd+' 의 대체 근무 가능 시간은 '+sub.workShm+'~'+sub.workEhm+' 입니다.';
			  	  	         					}
			  	  	         				}
		  	  	         				}
		  	  	         			});
		  	  	         			
		  	  	         			if(isValid && $this.overtime.calcMinute!=null && $this.overtime.calcMinute!='' && $this.overtime.calcMinute!=subsMin) {
		  	  	         				isValid = false;
		  	  	         				msg = calendarLeftVue.minuteToHHMM($this.overtime.calcMinute, 'detail')+'의 대체 휴일을 지정하세요.';
		  	  	         			}
		  	  	         			
	  	  	         			} else {
		  	  	         			isValid = false;
		  	         				msg = '휴일대체 선택 시 대체일시를 입력해야 합니다.';
	  	  	         			}
	  	  	         		}
  	  	         		}
  	  	         		
  	  	         		if(isValid) {
  	         				$this.otAppl(otSdate, otEdate);
  	  	         		} else {
  	  	         			$("#alertText").html(msg);
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
  	  	         		}
  	         		}
  	         		
  	         	},
  	         	otAppl : function(otSdate, otEdate){ //연장근무신청
  	         		var $this = this;
  	         	
  	         		var holidayYn = $this.result.holidayYn;
  	         		var subYn = '';
  	         	
  	         		var param = {
        				flexibleStdMgrId : calendarTopVue.flexibleStd.flexibleStdMgrId,
        				workTypeCd : 'OT',
        				ymd: moment($this.workday).format('YYYYMMDD'),
        				otSdate : moment(otSdate).format('YYYYMMDDHHmm'),
        				otEdate : moment(otEdate).format('YYYYMMDDHHmm'),
	   		    		reasonCd : $("#reasonCd").val(),
	   		    		reason: $("#reason").val(),
	   		    		holidayYn: holidayYn,
	   		    		subYn: subYn
	   		    	};
  	         		
  	         		//휴일근로신청
					if(holidayYn=='Y') {
						param['subYn'] =  $('input[name="subYn"]').val();
						
						var subs = [];
						if($this.subYmds!=null && $this.subYmds.length>0) {
							$this.subYmds.map(function(s){
								var subsSdate = moment(s.subsSymd+' '+s.subsShm).format('YYYY-MM-DD HH:mm');
								var subsEdate = moment(s.subsSymd+' '+s.subsEhm).format('YYYY-MM-DD HH:mm');
								
								var sub = {
									subYmd: moment(s.subsSymd).format('YYYYMMDD'),
									subsSdate: moment(subsSdate).format('YYYYMMDDHHmm'),
									subsEdate: moment(subsEdate).format('YYYYMMDDHHmm')
								};
								subs.push(sub);
							});
						}
						param['subs'] = subs;
						
					}
  	         		
  	         		Util.ajax({
						url: "${rc.getContextPath()}/otAppl/request",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							if(data!=null && data.status=='OK') {
								$("#alertText").html("확인요청 되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									location.reload();
								});
							} else {
								$("#alertText").html(data.message);
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
								});
							}
							
	  	  	         		$("#alertModal").modal("show"); 
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("연장근무 확인요청 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					});
  	         	},
  	         	otCancelAppl: function(){ //연장근무취소신청
  	         		var $this = this;
  	         		
  	         		var param = {
  	         			workDayResultId: $this.overtimeAppl.workDayResultId,
  	         			status: $this.overtimeAppl.applStatusCd,
        				workTypeCd : 'OT_CAN',
	   		    		reason: $("#cancelOpinion").val()
	   		    	};
  	         		
  	         		Util.ajax({
						url: "${rc.getContextPath()}/otCanAppl/request",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							if(data!=null && data.status=='OK') {
								$("#alertText").html("취소요청 되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$("#cancelOpinionModal").modal("hide");
									$("#overtimeApplDetail").modal("hide");
									location.reload();
								});
							} else {
								$("#alertText").html(data.message);
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
								});
							}
							
	  	  	         		$("#alertModal").modal("show"); 
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("연장근무 취소 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
						}
					});
  	         		
  	         	},
  	         	changeSubYn: function(val){
  	         		var $this = this;
  	         		
  	         		if($this.subYmds.length==0)
	         			$this.addSubYmd();
  	         		
  	         		if (val == "Y") { //휴일대체
  	                    $(".radio-toggle-wrap").show(500);
  	                }
  	                else if(val == "N") { //수당지급
  	                	$this.subYmds = [];
  	                    $(".radio-toggle-wrap").hide(500);
  	                }
  	                else {
  	                    $(".radio-toggle-wrap").hide(500);
  	                }
  	         	},
  	         	calcSubsTime: function(id) { //휴일 대체 근로 시간 계산
  	         		var $this = this;
  	         		var key = id.split('_');
  	 				var idx;
  	 				if(key!=null && key!='undefined' && key.length>0) 
  	 					idx = key[1];
  	 				
  	 				if(idx!=null && idx!='' && idx!=undefined) {
  	 					var subsInfo = this.subYmds[idx];
  	 					
  	 					if(subsInfo.subsSymd!=null && subsInfo.subsSymd!=undefined && subsInfo.subsSymd!=''
  	 							&& subsInfo.subsShm!=null && subsInfo.subsShm!=undefined && subsInfo.subsShm!=''
  	 							&& subsInfo.subsEhm!=null && subsInfo.subsEhm!=undefined && subsInfo.subsEhm!='') {
  	 						
  	 						var sDate = moment(subsInfo.subsSymd+" "+subsInfo.subsShm).format('YYYY-MM-DD HH:mm');
  	 						var eDate = moment(subsInfo.subsSymd+" "+subsInfo.subsEhm).format('YYYY-MM-DD HH:mm');
  	 						
  	  	           			if(sDate > eDate) {
	  	  	           			$("#alertText").html("종료시간이 시작시간보다 작습니다.");
		  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
		  	  	         			$("#alertModal").off('hidden.bs.modal');
		  	  	         			subsInfo.subsShm = '';
		  	  	         			subsInfo.subsEhm = '';
		  	  	         		});
		  	  	         		$("#alertModal").modal("show"); 
  	  	           			} else {
	  	  	           			var shm = subsInfo.subsShm.replace(/:/gi,"");
	  	  	           			var ehm = subsInfo.subsEhm.replace(/:/gi,"");
	  	  	           			var result = $this.calcMinute(moment(subsInfo.subsSymd).format('YYYYMMDD'), shm, ehm);
	  	 						subsInfo['subsMinute'] = result.calcMinute;
	  	 						subsInfo['subsBreakMinute'] = result.breakMinute;
  	  	           			}
  	  	           			
  	 					}
  	 					
  	 				}
  	         	},
  	         	updateValue: function(id, val){
  	         		var $this = this;
  	         		var key = id.split('_');
  	         		
  	         		if(key!=null && key!='undefined' && key.length>0) {
  	         			$this.subYmds[key[1]][key[0]] = val; 
  	         			//console.log('$this.subYmds['+key[1]+']['+key[0]+'] : ' + val);
  	         		}
  	         	},
  	         	addSubYmd: function(){
  	         		var newSubYmd = {
  	         			subsSymd: '',
  	         			subsShm: '',
  	         			subsEymd: '',
  	         			subsEhm: '',
  	         		};
  	         		
  	         		this.subYmds.push(newSubYmd);
  	         	},
  	         	delSubYmd: function(idx){
  	         		this.subYmds.splice(idx,1);
  	         	}
  		    }
   	});
   	
  	//동적으로 추가하는 요소에 datetimepicker를 그리기 위함
  	/* $('body').on('focus',"input[id^='subYmd']", function(){
   		var $this = this;
 		$(this).datetimepicker({
 			format: 'YYYY-MM-DD',
 		    language: 'ko'
 		});
 		
 		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				timeCalendarVue.updateValue($($this).attr('id'), moment(e.date).format('YYYY-MM-DD'));
 				timeCalendarVue.getWorkHour($($this).attr('id'), moment(e.date).format('YYYYMMDD'));
 			}
 		});
   	}); */
  	
  	$('body').on('focus',"input[id^='subsSymd'], input[id^='subsEymd']", function(){
   		var $this = this;
 		$(this).datetimepicker({
 			format: 'YYYY-MM-DD',
 		    language: 'ko',
 		    useCurrent: false
 		});
 		
 		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var subDate = moment(e.date).format('YYYYMMDD');
 				var id = $($this).attr('id');
 				var msg ='';
 				if($("#sDate").val()!='' && $("#eDate").val()!='') {
	 				//대체휴가 사용 가능일인지 판단
	         		if(timeCalendarVue.applCode!=null) {
	         			var sDate = $("#sDate").val().replace(/-/gi,"");
	           			var eDate = $("#eDate").val().replace(/-/gi,"");
	         			var otSdate = moment(sDate).format('YYYYMMDD');
	                 	var otEdate = moment(eDate).format('YYYYMMDD');
	                 	
	         			var applCode = timeCalendarVue.applCode;
	         			var dayDiff = Number(moment(subDate).diff(sDate,'days'));
	         			if(dayDiff<0) {
         					var subsSday = Number(applCode.subsSday);
	         				if(Math.abs(dayDiff)>subsSday) {
	         					msg = '대체휴가는 근무일 '+subsSday+'일 전까지 사용 가능합니다.';
	         				} else if(subsSday == 0) {
	         					msg = '대체휴가는 근무일 이후에만 사용 가능합니다.';
	         				}
	         			} else {
         					var subsEday = Number(applCode.subsEday);
         					if(dayDiff>subsEday) {
	         					msg = '대체휴가는 근무일 '+subsEday+'일 후까지 사용 가능합니다.';
	         				} else if(subsEday == 0) {
	         					msg = '대체휴가는 근무일 이전에만 사용 가능합니다.';
	         				}
	         			}
	         			
	         		} 
	 				
 				} else {
 					msg = '근로시간을 입력하세요.';
 				}
 				
				if(msg!='') {
	         		$("#alertText").html(msg);
	         		$("#alertModal").on('hidden.bs.modal',function(){
	         			$("#alertModal").off('hidden.bs.modal');
	         			$("#"+id).val('');
	         		});
	         		$("#alertModal").modal("show"); 
	         	} else {
	         		//해당일 근무시간 세팅
	 				timeCalendarVue.getWorkHour(id, subDate);
	 				//대체시간 계산
	 				timeCalendarVue.calcSubsTime(id);
	         	}
 				
 			}
 		});
   	});
  	
   	$('body').on('focus',"input[id^='subsShm']", function(){
   		var $this = this;
		$(this).datetimepicker({
			format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'top'
            },
            useCurrent: false
		});
		
		if(calendarLeftVue.rangeInfo!=null && Object.keys(calendarLeftVue.rangeInfo).length>0) {
			var unitMinute = calendarLeftVue.rangeInfo.unitMinute;
			if(unitMinute!=null && unitMinute!=undefined && unitMinute!='')
				$(this).datetimepicker('stepping',Number(unitMinute));
		}
		
		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var id = $($this).attr('id');
 				timeCalendarVue.updateValue(id, moment(e.date).format('HH:mm'));
 				timeCalendarVue.calcSubsTime(id);
 			}
 		});
   	});
   	
   	$('body').on('focus',"input[id^='subsEhm']", function(){
   		var $this = this;
		$(this).datetimepicker({
			format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'top'
            },
            useCurrent: false
		});
		
		if(calendarLeftVue.rangeInfo!=null && Object.keys(calendarLeftVue.rangeInfo).length>0) {
			var unitMinute = calendarLeftVue.rangeInfo.unitMinute;
			if(unitMinute!=null && unitMinute!=undefined && unitMinute!='')
				$(this).datetimepicker('stepping',Number(unitMinute));
		}
		
		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var id = $($this).attr('id');
 				timeCalendarVue.updateValue(id, moment(e.date).format('HH:mm'));
 				timeCalendarVue.calcSubsTime(id);
 			}
 		});
   	})

   	//날짜,시간 변경 시 근로시간 계산
   	$('#sDate, #eDate, #sTime, #eTime').on("change.datetimepicker", function(e){
   		
   		if($("#sDate").val()!='' && $("#eDate").val()!='' && $("#sTime").val()!='' && $("#eTime").val()!='') {
   			var sTime = $("#sTime").val().replace(/:/gi,"");
   			var eTime = $("#eTime").val().replace(/:/gi,"");
       		
       		timeCalendarVue.overtime = timeCalendarVue.calcMinute(moment(timeCalendarVue.workday).format('YYYYMMDD'), sTime, eTime);
   		}
    }); 
   	
	$('#timeCalendar [data-dismiss=modal]').on('click', function (e) {
		var $t = $(this),
	        target = $t[0].href || $t.data("target") || $t.parents('.modal') || [];

		$(target).find("input,select,textarea").not('input[name="subYn"]').val('').end();
	  	$(target).find("input[name='subYn']:checked").prop("checked", "").end();
	  	$(".radio-toggle-wrap").hide();
	  	$(target).find(".needs-validation").removeClass('was-validated');
	  	
	});
	
</script>

