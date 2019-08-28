<#include "/calendar.ftl">
<div>
	<div id="calendar_top" v-cloak>
	    <div class="modal fade" id="flexitimeModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
	        <div class="modal-dialog modal-lg" role="document">
	            <div class="modal-content rounded-0">
	                <div class="modal-header">
	                    <h5 class="modal-title">근무제 적용하기</h5>
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                </div>
	                <div class="modal-body">
	                	<div class="modal-app-wrap">
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
		                                    		근무구간: {{moment(f.useSymd+' '+f.workShm).format('HH:mm')}} ~ {{moment(f.useSymd+' '+f.workEhm).format('HH:mm')}}
		                                    	</template>
		                                    	<template v-else>
		                                    		근무구간: 없음
		                                    	</template>
		                                    	<span class="bar"></span>
		                                    	<template v-if="f.coreShm && f.coreEhm">
		                                    		코어구간: {{moment(f.useSymd+' '+f.coreShm).format('HH:mm')}} ~ {{moment(f.useSymd+' '+f.coreEhm).format('HH:mm')}}
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
		                <div class="btn-wrap text-center">
		                    <button type="button" class="btn btn-secondary  rounded-0" data-dismiss="modal">취소</button>
		                    <button type="button" id="applyFlexBtn" class="btn btn-default rounded-0" style="display:none;" @click="applyFlexitime">적용하기</button>
		                </div>
	                </div>
	            </div>
	        </div>
	    </div>
	    <div id="sub-nav" class="container-fluid">
	        <form id="calendar-top-wrap" action="">
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
	            <div class="sub-info-wrap clearfix">
	           		<div class="form-inline work-check-wrap">
	           			<span class="title">근무제 표시</span>
			            <ul class="legend-list-wrap">
			                <li class="ELAS">탄력근무</li>
			                <li class="SELE_F">완전선택근무</li>
			                <li class="SELE_C">부분선택근무</li>
			                <li class="DIFF">시차출퇴근</li>
			                <li class="AUTO">자율출퇴근</li>
			            </ul>
	           		</div>
	            	<!--  
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
		            <div class="legend-wrap">
			            <span class="title">근무제 표시</span>
			            <ul class="legend-list-wrap">
			                <li class="ELAS">탄력근무</li>
			                <li class="SELE_F">완전선택근무</li>
			                <li class="SELE_C">부분선택근무</li>
			                <li class="DIFF">시차출퇴근</li>
			                <li class="AUTO">자율출퇴근</li>
			            </ul>
			        </div>
			        -->
	           	</div>
	        </form>
	        <div id="summary-wrap" style="display:none;">
			    <ul class="summary-list">
			        <li><span class="label-title">현재 근무계획</span><span class="desc">기본근무제</span></li>
			        <li><span class="label-title">잔여소정근로</span><span class="desc">8시간 42분</span></li>
			        <li><span class="label-title">잔여연장근로</span><span class="desc">4시간</span></li>
			    </ul>
			</div>
			<div class="btn-collapse-wrap">
			    <button class="btn-collapse btn-inline"></button>
			</div>
		</div>
    </div>
    <div class="container-fluid">
        <div class="row no-gutters">
            <div id="calendar_left" class="col-12 col-md-3 pr-md-3" v-cloak>
                <div id="workRangeInfo" class="work-info-wrap mb-3" style="display:none;">
                    <div class="main-title">
                    	<template v-if="Object.keys(rangeInfo).length>0 && rangeInfo.sYmd && rangeInfo.eYmd">
                    	{{moment(rangeInfo.sYmd).format("YYYY년 M월 D일")}} ~ {{moment(rangeInfo.eYmd).format("YYYY년 M월 D일")}}
                    	</template>
                    </div>
                    <div class="main-desc">
                    	<template v-if="Object.keys(rangeInfo).length>0 && rangeInfo.flexibleNm">
                    	{{rangeInfo.flexibleNm}}
                    	</template>
                    </div>
                    <ul class="sub-wrap">
                        <li>
                            <div class="sub-title">총 계획 근무시간</div>
                            <div class="time-graph plan">
                                 <span class="work-time"></span>
                                 <span class="over-time"></span>
                             </div>
                             <ul class="legend-wrap">
                                 <li class="work-time">소정 <strong>40:00</strong></li>
                                 <li class="over-time">연장 <strong>12:00</strong></li>
                             </ul>
                        </li>
                        <li>
                            <div class="sub-title">잔여 근무시간</div>
                            <div class="time-graph rest">
                                 <span class="work-time"></span>
                                 <span class="over-time"></span>
                             </div>
                             <ul class="legend-wrap">
                                 <li class="work-time">소정 <strong>10:00</strong></li>
                                 <li class="over-time">연장 <strong>11:00</strong></li>
                             </ul>
                        </li>
                        <li>
                            <div class="sub-title">근로시간 산정 구간 평균 주간 근무시간</div>
                            <div class="sub-desc">
                            	<template v-if="Object.keys(rangeInfo).length>0">
                            	48시간
                            	</template>
                            </div>
                        </li>
                    </ul>
                    <div class="btn-wrap">
                        <button type="submit" id="workPlanBtn" class="btn btn-apply btn-block btn-lg" @click="viewWorkDayCalendar" style="display:none;">근무계획작성</button>
                    </div>
                </div>
                <div id="workDayInfo" class="white-box-wrap mb-3" style="display:none;">
               		<a href="#" class="btn-info-wrap">
	                    <span style="display:none;" id="btn-lbottom-wrap"><span>&#43;&nbsp;</span>자세히 보기</span>
	                    <span style="display:block;" id="btn-lbottom-simple-wrap"><span>&#8722;&nbsp;</span>간략하게 보기</span>
	                </a>
                    <div id="lbottom-wrap" class="work-plan-wrap">
                        <ul class="main-wrap">
                            <li>
                                <div class="main-title">해당일의 근무계획 구분</div>
                                <div class="main-desc">
                                	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.holidayYn">
                                	{{workDayInfo.holidayYn=='Y'?'휴무일':'근무일'}}
                                	</template>
                                </div>
                            </li>
                            <li>
                                <div class="main-title">계획 근무시간</div>
                                <div class="main-desc">
                                	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.planSdate && workDayInfo.planEdate">
                                	{{moment(workDayInfo.planSdate).format('HH:mm')}} ~ {{moment(workDayInfo.planEdate).format('HH:mm')}}({{minuteToHHMM(workDayInfo.planMinute)}})
                                	</template>
                                </div>
                            </li>
                            <li>
                                <div class="main-title">실적 근무시간</div>
                                <div class="main-desc">
                                	<template v-if="Object.keys(workDayInfo).length>0 && (workDayInfo.apprSdate || workDayInfo.apprEdate)">
	                                	<template v-if="workDayInfo.apprEdate && workDayInfo.apprEdate!='' && workDayInfo.apprEdate!=null && workDayInfo.apprEdate!=undefined">
	                                	{{moment(workDayInfo.apprSdate).format('HH:mm')}} ~ {{moment(workDayInfo.apprEdate).format('HH:mm')}}({{minuteToHHMM(workDayInfo.apprMinute)}})
	                                	</template>
	                                	<template v-else>
	                                	{{moment(workDayInfo.apprSdate).format('HH:mm')}} ~ 
	                                	</template>
                                	</template>
                                </div>
                            </li>
                            <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.taaNames">
                                <div class="main-title">해당일 근태</div>
                                <div class="main-desc">
                                	{{workDayInfo.taaNames}}
                                </div>
                            </li>
                            <li>
	                            <div class="main-title">근무시간표</div>
	                            <div class="main-desc">
	                            	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.timeNm">
	                            	{{workDayInfo.timeNm}}
	                            	</template>
	                            </div>
	                        </li>
                        </ul>
                        <div class="sub-wrap">
                            <div class="sub-big-title">근무시간 요약 <span style="font-size:10px;">(근무시간 분류별 합산)</span></div>
                            <ul class="sub-list">
                                <li>
                                	<span class="sub-title"><i class="fas fa-clock"></i>소정근로</span>
                                    <span class="sub-desc">
                                    	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.workHour">
		                            	{{workDayInfo.workHour}}
		                            	</template>
                                    </span>
                                </li>
                                <li>
                                	<span class="sub-title"><i class="fas fa-moon"></i>연장근로</span>
                                    <span class="sub-desc">
                                    	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.otHour">
		                            	{{workDayInfo.otHour}}
		                            	</template>
                                    </span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">일반연장</span>
                                            <span class="sub-desc">
                                            	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.otBasicHour">
				                            	{{workDayInfo.otBasicHour}}
				                            	</template>
                                            </span>
                                        </li>
                                        <li>
                                            <span class="sub-title">야간근무</span>
                                            <span class="sub-desc">
                                            	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.otNightHour">
				                            	{{workDayInfo.otNightHour}}
				                            	</template>
                                            </span>
                                        </li>
                                    </ul>
                                </li>
                                <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.taaNames">
                                    <span class="sub-title"><i class="fas fa-file-alt"></i>근태현황</span>
                                    <span class="sub-desc"></span>
                                    <ul class="sub-desc-list">
                                        <li v-for="(taa, idx) in workDayInfo.taaNames.split(',')">
                                            <span class="sub-title">{{taa}}</span>
                                            <span class="sub-desc">{{workDayInfo.taaHour.split(',')[idx]}}</span>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                	<span class="sub-title"><i class="fas fa-couch"></i>휴식/휴게 현황</span>
                                	<span class="sub-desc"></span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">무급</span>
                                            <span class="sub-desc">
                                            	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.breakHour">
				                            	{{workDayInfo.breakHour}}
				                            	</template>
                                            </span>
                                        </li>
                                        <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.paidhour">
                                            <span class="sub-title">유급</span>
                                            <span class="sub-desc">
				                            	{{workDayInfo.paidhour}}
                                            </span>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div id="lbottom-simple-wrap" class="work-plan-wrap" style="display:none;">
	                    <ul class="main-wrap">
	                    	<li v-if="Object.keys(workDayInfo).length>0 && (workDayInfo.apprSdate || workDayInfo.apprEdate)">
	                            <div class="main-title">실적 근무 시간</div>
	                            <div class="main-desc">
	                            	<template v-if="workDayInfo.apprEdate && workDayInfo.apprEdate!='' && workDayInfo.apprEdate!=null && workDayInfo.apprEdate!=undefined">
	                                {{moment(workDayInfo.apprSdate).format('HH:mm')}} ~ {{moment(workDayInfo.apprEdate).format('HH:mm')}}({{minuteToHHMM(workDayInfo.apprMinute)}})
                                	</template>
                                	<template v-else>
                                	{{moment(workDayInfo.apprSdate).format('HH:mm')}} ~ 
                                	</template>
	                            </div>
	                        </li>
	                        <li v-else>
	                            <div class="main-title">계획 근무 시간</div>
	                            <div class="main-desc">
	                            	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.planSdate && workDayInfo.planEdate">
	                                {{moment(workDayInfo.planSdate).format('HH:mm')}} ~ {{moment(workDayInfo.planEdate).format('HH:mm')}}({{minuteToHHMM(workDayInfo.planMinute)}})
                                	</template>
	                            </div>
	                        </li>
	                        <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.taaNames">
	                            <div class="main-title">해당일 근태</div>
	                            <div class="main-desc">
                                	{{workDayInfo.taaNames}}
	                            </div>
	                        </li>
	                    </ul>
	                    <div class="sub-wrap">
	                        <div class="sub-big-title">근무시간 요약 <span style="font-size:10px;">(근무시간 분류별 합산)</span></div>
	
	                        <ul class="sub-list reset">
	                            <li>
	                                <span class="sub-title"><i class="fas fa-clock"></i>소정근로 합산</span>
	                                <span class="sub-desc">8:00</span>
	                            </li>
	                            <li>
	                                <span class="sub-title"><i class="fas fa-moon"></i>연장근로 합산</span>
	                                <span class="sub-desc">2:00</span>
	                            </li>
	                        </ul>
	                    </div>
	                </div>
                </div>
                <div id="flexibleAppl" class="white-box-wrap full-height mb-3" style="display:none;">
                    <div class="work-plan-wrap">
                        <div class="main-wrap" v-if="calendarTopVue.flexibleStd">
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
                        <form action="" class="time-input-form needs-validation" novalidate>
                            <div class="form-row no-gutters">
                                <div class="form-group col-6 pr-1">
                                    <label for="useSymd-dt">시작일자</label>
                                    <div data-target-input="nearest">
                                    	<input type="text" id="useSymd" class="form-control datetimepicker-input" data-toggle="datetimepicker" data-target="#useSymd" placeholder="연도-월-일"
                                            autocomplete="off" v-model="applInfo.useSymd" :value="applInfo.useSymd" required>
                                	</div>
                                </div>
                                <div class="form-group col-6 pl-1">
                                    <label for="useEymd">종료일자</label>
                                    <div>
	                                    <input type="text" id="useEymd" class="form-control" v-model="applInfo.useEymd" :value="applInfo.useEymd" disabled required>
	                                </div>
                                </div>
                                <div class="form-group col-12">
                                    <label for="workTime">근무기간</label>
                                    <select id="workTime" v-if="calendarTopVue.flexibleStd" class="form-control" v-model="applInfo.workRange" :value="applInfo.workRange" @change="changeWorkRange" required>
                                        <option v-for="term in calendarTopVue.flexibleStd.usedTermOpt" :value="term.value">{{term.lable}}</option>
                                    </select>
                                </div>
                                <div class="form-group col-12">
                                    <label for="reson">사유</label>
                                    <textarea class="form-control" id="reson" rows="3" placeholder="팀장 확인 시에 필요합니다." v-model="applInfo.reason" required></textarea>
                                </div>
                            </div>
                            <div class="btn-wrap mt-5">
                                <button id="apprBtn" type="button" class="btn btn-apply btn-block btn-lg" @click="validateFlexitimeAppl">확인요청</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div id="flexibleDayPlan" class="white-box-wrap full-height mb-3" style="display:none;">
                	<form class="needs-validation" novalidate>
		                <div class="work-plan-wrap">
		                    <div class="big-title">
		                    	<template v-if="flexibleAppl.sYmd && flexibleAppl.eYmd">
		                    	{{moment(flexibleAppl.sYmd).format("YYYY년 M월 D일")}} ~ {{moment(flexibleAppl.eYmd).format("YYYY년 M월 D일")}}({{moment(flexibleAppl.eYmd).diff(flexibleAppl.sYmd, 'days')+1}}일)
		                    	</template>
		                    </div>
		                    <div class="inner-wrap">
		                    	<ul class="main-wrap">
	                                <li>
	                                    <div class="main-title">근무시간표</div>
	                                    <div class="main-desc">기본근무시간표</div>
	                                </li>
	                                <li>
	                                    <div class="main-title">근무시간</div>
	                                    <div class="main-desc">
	                                    	<template v-if="flexibleAppl.workShm && flexibleAppl.workEhm">
				                        	{{moment(flexibleAppl.sYmd+' '+flexibleAppl.workShm).format('HH:mm')}} ~ {{moment(flexibleAppl.sYmd+' '+flexibleAppl.workEhm).format('HH:mm')}}
				                        	</template>
	                                    </div>
	                                </li>
	                                <li>
	                                    <div class="main-title">코어시간</div>
	                                    <div class="main-desc">
	                                    	<template v-if="flexibleAppl.coreShm && flexibleAppl.coreEhm">
	                                    	{{moment(flexibleAppl.sYmd+' '+flexibleAppl.coreShm).format('HH:mm')}} ~ {{moment(flexibleAppl.sYmd+' '+flexibleAppl.coreEhm).format('HH:mm')}}
	                                    	</template>
	                                    </div>
	                                </li>
	                            </ul>
		                    </div>
		                    <div class="inner-wrap graph-wrap">
                                <div class="time-graph">
                                    <span class="core-time"></span>
                                </div>
                                <ul class="legend-wrap">
                                    <li class="work-time">근무시간</li>
                                    <li class="core-time">코어시간</li>
                                </ul>
                            </div>
		                    <hr class="bar">
		                    <ul class="main-wrap">
	                            <li>
	                                <div class="main-title">신청일자</div>
	                                <div id="selectedRange" class="main-desc"></div>
	                            </li>
	                        </ul>
	                        <div class="time-input-form form-row no-gutters">
	                            <div class="form-group col-6 pr-1">
	                                <label for="startDay" data-target-input="nearest">출근시간</label>
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="startTime" value="" data-toggle="datetimepicker" data-target="#startTime" autocomplete="off" @focusout="changeWorkTime" required>
	                            </div>
	                            <div class="form-group col-6 pl-1">
	                                <label for="endDay" data-target-input="nearest">퇴근시간</label>
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="endTime" value="" data-toggle="datetimepicker" data-target="#endTime" autocomplete="off" @focusout="changeWorkTime" required>
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
		                <div class="btn-wrap">
		                    <button type="button" id="timeSaveBtn" class="btn btn-apply btn-block btn-lg" @click="validateWorkDayResult">저장</button>
		                </div>
	                </form>
	            </div>
	            <div id="flexibleDayInfo" class="white-box-wrap mb-3" style="display:none;">
	                <div class="work-plan-wrap">
	                    <ul class="main-wrap">
	                        <li>
	                            <div class="main-title">근태</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.taaNames">
	                            	{{workTimeInfo.taaNames}}
	                            	</template>
	                            </div>
	                        </li>
	                        <li>
	                            <div class="main-title">계획 근무 시간</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.planSdate&&workTimeInfo.planEdate">
	                            	{{moment(workTimeInfo.planSdate).format('HH:mm')}}~{{moment(workTimeInfo.planEdate).format('HH:mm')}}({{minuteToHHMM(workTimeInfo.planMinute)}})
	                            	</template>
	                            </div>
	                        </li>
	                        <li>
	                            <div class="main-title">출/퇴근 시각</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.entrySdate||workTimeInfo.entryEdate">
	                             	<template v-if="workTimeInfo.entrySdate">
	                             		{{moment(workTimeInfo.entrySdate).format('HH:mm')}}
	                             	</template>
	                             	<template v-else>
	                             		미타각
	                             	</template>
	                             	~
	                             	<template v-if="workTimeInfo.entryEdate">
	                             		{{moment(workTimeInfo.entryEdate).format('HH:mm')}}
	                             	</template>
	                             	<template v-else>
	                             		미타각
	                             	</template>
	                            	</template>
	                            </div>
	                        </li>
	                        <li>
	                            <div class="main-title">근무 인정시간</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.apprSdate||workTimeInfo.apprEdate">
	                             	<template v-if="workTimeInfo.apprSdate">
	                             		{{moment(workTimeInfo.apprSdate).format('HH:mm')}}
	                             	</template>
	                             	<template v-else>
	                             		미타각
	                             	</template>
	                             	~
	                             	<template v-if="workTimeInfo.apprEdate">
	                             		{{moment(workTimeInfo.apprEdate).format('HH:mm')}}
	                             	</template>
	                             	<template v-else>
	                             		미타각
	                             	</template>
	                            	</template>
	                            </div>
	                        </li>
	                        <li>
	                            <div class="main-title">소정 근무시간</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.apprBaseMinute">
	                            	{{minuteToHHMM(workTimeInfo.apprBaseMinute, 'detail')}}
	                            	</template>
	                            </div>
	                        </li>
	                        <li>
	                            <div class="main-title">연장 근무시간</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.apprOtMinute">
	                            	{{minuteToHHMM(workTimeInfo.apprOtMinute, 'detail')}}
	                            	</template>
	                            </div>
	                        </li>
	             		</ul>
	                    <div class="btn-wrap">
                           	<button type="button" class="btn btn-apply btn-block btn-lg" @click="viewOvertimeAppl">연장근로신청</button>
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
	$(function () {
		
		var fireRefreshEventOnWindow = function () {
			var evt = document.createEvent("HTMLEvents");
		    evt.initEvent('resize', true, false);
		    window.dispatchEvent(evt);
		};
		
		$('#sub-nav #summary-wrap').hide();
        $( "#sub-nav .btn-collapse" ).on('click',function() {
        	//calendar resize를 위해 window resize 호출
        	//window.dispatchEvent는 IE에서 안됨
        	//window.dispatchEvent(new Event('resize'));
        	fireRefreshEventOnWindow();
        	
            $('#sub-nav #calendar-top-wrap').toggle();
            $('#sub-nav #summary-wrap').toggle();
            
            $('.calendar-wrap').toggleClass('collapse-height');
            $('.white-box-wrap').toggleClass('collapse-height');
            
        });
        
        $( ".btn-info-wrap" ).on('click',function() {
            $('#btn-lbottom-wrap').toggle();
            $('#btn-lbottom-simple-wrap').toggle();
            $('#lbottom-wrap').toggle();
            $('#lbottom-simple-wrap').toggle();
        });
		
		//유연근무제 신청일자
		$('#useSymd').datetimepicker({
	        format: 'YYYY-MM-DD',
	        language: 'ko'
	    });
		
		//출퇴근시간
		$('#startTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
		
		$('#endTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        });
	});
	
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
	    		
	    		if(flexibleAppl.applStatusCd!=null && flexibleAppl.applStatusCd!='' && flexibleAppl.applStatusCd!='99') {
					//신청화면 전환
					$("#applyBtn").bind('click', function(){
						 calendarLeftVue.viewFlexitimeAppl(flexibleAppl);
					});
	    		} else {
	    			$("#applyBtn").bind('click', function(){
	    				$this.getFlexitimeList();
					});
	    		}
       		
       		<#else>
	    		//사용할 근무제 리스트 조회
				$("#applyBtn").bind('click', function(){
					 $this.getFlexitimeList();
				});
	    	</#if>
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
	    	rangeInfo: {}, //선택한 기간의 근무제 정보
	    	workDayInfo: {}, //선택한 날의 근무 정보
	    	workTimeInfo: {}, //선택한 날의 근무시간 정보
	    	useYn: 'N', //근무제 적용 여부
	    	applInfo: { //신청 데이터
	    		flexibleApplId:'',
	    		applId:'',
	    		useSymd:'',
	    		useEymd:'',
	    		workRange:'',
	    		reason:''
	    	},
	    	flexibleAppl: {},
	    	selectedDate: '${today}',
	    	workPlanYn: false
  		},
  		watch: {
  			rangeInfo : function(val, oldVal) {
  				//총 계획 근무시간,잔여 근무시간 그래프 표기
  				if(val.baseWorkYn!=null && val.baseWorkYn!=undefined) {
  					if(val.baseWorkYn=='Y') {
		  				$(".work-info-wrap .time-graph.plan .work-time").css({ 'width': 'calc(40/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.plan .over-time").css({ 'left': 'calc((40 - 1)/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.plan .over-time").css({ 'width': 'calc((12 + 1)/52 * 100%)' });
		  				
		  				$(".work-info-wrap .time-graph.rest .work-time").css({ 'width': 'calc((40 - 30)/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.rest .over-time").css({ 'left': 'calc((40 - 30 - 1)/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.rest .over-time").css({ 'width': 'calc((40 - 30 - 1 + 1)/52 * 100%)' });
  					} else {
  						$(".work-info-wrap .time-graph.plan .work-time").css({ 'width': 'calc(40/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.plan .over-time").css({ 'left': 'calc((40 - 1)/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.plan .over-time").css({ 'width': 'calc((12 + 1)/52 * 100%)' });
		  				
		  				$(".work-info-wrap .time-graph.rest .work-time").css({ 'width': 'calc((40 - 30)/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.rest .over-time").css({ 'left': 'calc((40 - 30 - 1)/52 * 100%)' });
		  				$(".work-info-wrap .time-graph.rest .over-time").css({ 'width': 'calc((40 - 30 - 1 + 1)/52 * 100%)' });
  					}
  				} else {
  					
  				}
  			},
  			flexibleAppl : function(val, oldVal) {
  				//근무시간, 코어시간 그래프 표기
  				if(val.sYmd!=null && val.sYmd!=undefined && val.sYmd!=''
  						&& val.workShm!=null && val.workShm!=undefined && val.workShm!=''
  						&& val.coreEhm!=null && val.coreEhm!=undefined && val.coreEhm!='') {
	  				
	  				var workSh = moment(val.sYmd+' '+val.workShm).format('HH');
	  				var coreSh = moment(val.sYmd+' '+val.coreShm).format('HH');
	  				var coreEh = moment(val.sYmd+' '+val.coreEhm).format('HH');
	  				
	  				$(".graph-wrap .time-graph .core-time").css({ 'left': 'calc(('+coreSh+' - '+workSh+')/12*100%)' });
	  				$(".graph-wrap .time-graph .core-time").css({ 'width': 'calc(('+coreEh+' - '+coreSh+')/12*100%)' });
  				} else {
  					$(".graph-wrap .time-graph .core-time").css({ 'left': 0 });
	  				$(".graph-wrap .time-graph .core-time").css({ 'width': 0 });
  				}
  			}
  		},
	    mounted: function(){
	    	this.getFlexibleRangeInfo(this.today);
	    	this.getFlexibleDayInfo(this.today);
	    	//calendarLeftVue.getWorkDayInfo(this.today);
	    	
	    	<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
    			this.flexibleAppl = JSON.parse("${flexibleAppl?js_string}"); 
    			
    			if('${calendar}' == 'workDayCalendar') {
    				dayCalendarVue.getWorkDayResult();
    	    	}
    		</#if>
	    },
	    methods : {
	    	minuteToHHMM : function (min, type) {
	    		if(min!=null && min!=undefined && min!='') {
		    		if(type==null || type=='')
			   	    	type='short';
		    		
			   	    var min = Number(min);
			   	    var hours   = Math.floor(min / 60);
			   	    var minutes = Math.floor(min - (hours * 60));
		
			   	 	if(type=='detail') {
			   	 		var h = hours==0?'':hours+'시간';
			   	 		var m = minutes==0?'':minutes+'분';
			   	 		
			   	 		var s = h;
			   	 		if(h!=''&&m!='') s+=' ';
			   	 		s+=m;
			   	 		
			   	    	return s;
			   	 	}
			   	    	
			   	    if (hours   < 10) {hours   = "0"+hours;}
			   	    if (minutes < 10) {minutes = "0"+minutes;}
			   	    
			   	    if(type=='short')
			   	   		return hours+':'+minutes;
	    		} else {
	    			return '';
	    		}
		   	},
	    	getFlexibleRangeInfo : function(ymd){ //오늘 또는 선택한 기간의 근무제 정보(남색 박스)
				var $this = this;
		    	
	    		//근무계획작성 버튼 숨기기
				//$("#workPlanBtn").hide();
	    	
				var param = {
   		    		ymd : moment(ymd).format('YYYYMMDD')
   		    	};
				
		    	Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/range",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$this.rangeInfo = {};
						
						if(data!=null) {
							$this.rangeInfo = data;
							
							//근무계획작성
							if(data.hasOwnProperty('baseWorkYn') && data.baseWorkYn!=null && data.baseWorkYn!=undefined && data.baseWorkYn!=''
									&& data.baseWorkYn!='Y') {
								$("#workPlanBtn").show();
							} else {
								$("#workPlanBtn").hide();
							}
						}
					},
					error: function(e) {
						$("#workPlanBtn").hide();
						$this.rangeInfo = {};
					}
				});
	        },
	        getFlexibleDayInfo : function(ymd){ //오늘 또는 선택한 날의 근무일 정보(흰색 박스)
	        	var $this = this;
	    		
				var param = {
   		    		ymd : moment(ymd).format('YYYYMMDD')
   		    	};
		    		
		    	Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/day",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$this.workDayInfo = {};
						if(data!=null) {
							$this.workDayInfo = data;
						}
					},
					error: function(e) {
						$this.workDayInfo = {};
					}
				});
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
								dayCalendarVue.getWorkDayResult();
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
         	validateFlexitimeAppl : function(){
         		var applYn = true;
         		var forms = document.getElementById('flexibleAppl').getElementsByClassName('needs-validation');
         		var validation = Array.prototype.filter.call(forms, function(form) {
         			if (form.checkValidity() === false) {
         				applYn = false;
         				event.preventDefault();
         		        event.stopPropagation();
         			}
         			form.classList.add('was-validated');
         		});
         		
         		if(applYn) {
         			this.flexitimeAppl();
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
	         	
	         	/* 
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
         		}*/
					
         		if(flexibleStd.workTypeCd.indexOf('SELE')==0) {
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
								$("#alertText").html(data.message);
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
					
	         	} else if(flexibleStd.workTypeCd.indexOf('ELAS')==0){
	         		$("#flexibleAppl").hide();
	         	}
	        },
	        validateWorkDayResult : function(){
         		var applYn = true;
         		var forms = document.getElementById('flexibleDayPlan').getElementsByClassName('needs-validation');
         		var validation = Array.prototype.filter.call(forms, function(form) {
         			if (form.checkValidity() === false) {
         				applYn = false;
         				event.preventDefault();
         		        event.stopPropagation();
         			}
         			form.classList.add('was-validated');
         		});
         		
         		if(applYn) {
         			this.saveWorkDayResult();
         		}
         		
         	},
         	saveWorkDayResult: function(){
         		dayCalendarVue.saveWorkDayResult();
         	},
         	viewWorkDayCalendar: function(){
         		location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Day&date='+moment(this.selectedDate).format('YYYYMMDD');
         	},
         	viewOvertimeAppl: function(){
         		var $this = this;
         		timeCalendarVue.viewOvertimeAppl($this.calendar.getDate());
         	}
	    }
   	});
   	
   	$('#flexitimeModal').on('hidden.bs.modal',function(){
   		$(".list-group-item").removeClass("active");
   		monthCalendarVue.prevEdate = '';
   		$("#applyFlexBtn").hide();
   	});
   	
   	$('#useSymd').on("change.datetimepicker", function(e){
   		if(e.date!=null && e.date!='undefined' && e.date!='') {
	   		if(e.oldDate!=null && e.oldDate!='undefined' && e.date!=e.oldDate) {
	   			calendarLeftVue.applInfo.useSymd = moment(e.date).format('YYYY-MM-DD');
	   	    	calendarLeftVue.changeUseSymd();
	   		}
   		}
    }); 
   	
</script>

