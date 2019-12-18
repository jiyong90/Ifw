<!-- guide modal start -->
<div class="modal fade show" id="guide" data-backdrop="static"  tabindex="-1" role="dialog">
    <div class="modal-dialog modal-guide" role="document">
        <form>
        <div class="modal-content rounded-0">
            <ol class="bxslider">
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p01.png" title="달력의 날짜를 선택해서 해당일의 상세정보를 볼 수 있습니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p02.png" title="해당일 화면에서 시간을 선택하면 연장/휴일 근무를 신청할 수 있습니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p03.png" title="결재권한자는 팀원들의 연장/휴일/유연근무 신청서를 결재 메뉴에서 승인합니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p04.png" title="월 근무 캘린더 상단에는 현재 적용되는 근무제와 소정/잔여 근로시간이 표시됩니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p05.png" title="월 근무 캘린더 좌측에는 선택한 날이 포함된 기간의 근무요약정보가 표시됩니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p06.png" title="선택한 날의 근무상세정보는 화면 좌측 하단에 표시됩니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p07.png" title="직원이 직접 유연근무제적용을 선택할 수 있습니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p08.png" title="근무계획작성 버튼을 클릭해 특정기간의 근무계획을 작성할 수 있습니다."></li>
                <li><img src="${rc.getContextPath()}/soldev/img/guide/p09.png" title="여러 날을 동시에 선택해 근무계획을 입력할 수 있습니다."></li>
            </ol>
            <div class="modal-footer rounded-0">
                <div class="form-element float-left">
                    <input type="checkbox" id="today-close" name="today-close" value="" title="오늘 하루 이창 열지 않기">
                    <label for="today-close">오늘 하루 이창 열지 않기</label>
                </div>
                <div class="float-right">
                    <button type="button" class="close">
                        <span aria-hidden="true" data-dismiss="modal">닫기</span>
                    </button>
                </div>
            </div>
        </div>
    </form>
    </div>
</div>
<!-- guide modal end -->
<#include "/calendar.ftl">
<div>
	<div id="calendar_top" v-cloak>
		<!-- 근무제 적용 modal start -->
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
	    <!-- 근무제 적용 modal end -->
	    <div id="sub-nav" class="container-fluid">
	        <form id="calendar-top-wrap" action="">
	            <div class="row no-gutters work-time-wrap">
	                <div class="col-12 col-sm-3 col-xl-1">
	                    <div class="title">현재 근무계획</div>
	                    <div class="desc" v-if="flexibleStd">{{flexibleStd.flexibleNm}}</div>
	                </div>
	                <div class="col-12 col-sm-2 col-xl-1">
	                    <div class="title">잔여소정근로</div>
	                    <div class="desc" v-if="flexibleStd">{{minuteToHHMM(flexibleStd.restWorkMinute,'detail')}}</div>
	                </div>
	                <div class="col-12 col-sm-2 col-xl-1">
	                    <div class="title">잔여연장근로</div>
	                    <div class="desc" v-if="flexibleStd">{{minuteToHHMM(flexibleStd.restOtMinute,'detail')}}</div>
	                </div>
	                <div class="col">
	                </div>
	                <div v-if="'${calendar}'=='workMonthCalendar'" class="col-12 col-sm-4 col-md-3 col-lg-2 col-xl-2">
	                    <div class="btn-wrap text-right">
	                    	<button type="button" id="applyBtn" class="btn btn-apply" data-toggle="modal" data-target="#flexitimeModal" v-if="flexitimeList.length>0" >근무제 적용하기</button>
	                        <!--  <button type="button" id="applyBtn" class="btn btn-apply" data-toggle="modal" v-if="flexApplYn=='Y'" @click="getFlexitimeList">근무제 적용하기</button> -->
	                    	<button type="button" id="planBtn" class="btn btn-write" style="display:none;">근무계획작성</button>
	                    </div>
	                </div>
	            </div>
	            <!-- 
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
	           		 -->
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
			        
	           	</div>
	           	-->
	        </form>
	        <div id="summary-wrap" style="display:none;">
			    <ul class="summary-list">
			        <li><span class="label-title">현재 근무계획</span><span class="desc" v-if="flexibleStd">{{flexibleStd.flexibleNm}}</span></li>
			        <li><span class="label-title">잔여소정근로</span><span class="desc" v-if="flexibleStd">{{minuteToHHMM(flexibleStd.restWorkMinute,'detail')}}</span></li>
			        <li><span class="label-title">잔여연장근로</span><span class="desc" v-if="flexibleStd">{{minuteToHHMM(flexibleStd.restOtMinute,'detail')}}</span></li>
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
                    	<template v-else>
                    	{{moment(selectedDate).format("YYYY년 M월 D일")}}
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
                                 <li class="work-time">소정 <strong>{{minuteToHHMM(rangeInfo.totalWorkMinute)}}</strong></li>
                                 <li class="over-time">연장 <strong>{{minuteToHHMM(rangeInfo.totalOtMinute)}}</strong></li>
                             </ul>
                        </li>
                        <li>
                            <div class="sub-title">잔여 근무시간</div>
                            <div class="time-graph rest">
                                 <span class="work-time"></span>
                                 <span class="over-time"></span>
                             </div>
                             <ul class="legend-wrap">
                                 <li class="work-time" v-if="rangeInfo.restWorkMinute!=0">소정 <strong>{{minuteToHHMM(rangeInfo.restWorkMinute)}}</strong></li>
                                 <li class="work-time" v-else>소정 <strong>00:00</strong></li>
                                 <li class="over-time" v-if="rangeInfo.restOtMinute!=0">연장 <strong>{{minuteToHHMM(rangeInfo.restOtMinute)}}</strong></li>
                                 <li class="over-time" v-else>연장 <strong>00:00</strong></li>
                             </ul>
                        </li>
                        <li>
                            <div class="sub-title">근로시간 산정 구간 평균 주간 근무시간</div>
                            <div class="sub-desc">
                            	<template v-if="Object.keys(rangeInfo).length>0 && rangeInfo.avlMinute && rangeInfo.avlMinute!=0">
                            	{{minuteToHHMM(rangeInfo.avlMinute,'detail')}}
                            	</template>
                            </div>
                        </li>
                    </ul>
                    <div class="btn-wrap row" v-if="flexCancelBtnYn || workPlanBtnYn || inOutChgBtnYn || otApplBtnYn">
                    	<!--  
                    	<template v-if="flexCancelBtnYn && workPlanBtnYn">
                    		<div class="col-6 pr-1">
                    			<button type="button" class="btn btn-cancel btn-block" @click="calendarLeftVue.cancelFlexitime">근무제적용취소</button>
                       		</div>
                       		<div class="col-6 pl-1">
                       			<button type="button" class="btn btn-apply btn-block" @click="viewWorkDayCalendar(moment(selectedDate).format('YYYYMMDD'))">근무계획작성</button>
                    		</div>
                    	</template>
                    	<template v-else>
                    	-->
                    		<div class="col-12">
                    			<button type="button" class="btn btn-cancel btn-block" v-if="flexCancelBtnYn" @click="calendarLeftVue.cancelFlexitime">근무제적용취소</button>
                       			<button type="button" class="btn btn-apply btn-block" v-if="workPlanBtnYn" @click="viewWorkDayCalendar(moment(selectedDate).format('YYYYMMDD'))">근무계획작성</button>
                    		</div>
                    	<!--  
                    	</template>
                    	-->
                    	<template v-if="'${calendar}'=='workTimeCalendar'">
                    		<template v-if="inOutChgBtnYn && otApplBtnYn">
	                    		<div class="col-6 pr-1">
		                           	<button type="button" class="btn btn-cancel btn-block" @click="viewInOutChangeAppl">근태사유서신청</button>
		                        </div>
	                    		<div class="col-6 pl-1">
		                           	<button type="button" class="btn btn-apply btn-block" @click="viewOvertimeAppl">
		                           		<template v-if="holidayYn=='Y'">
		                           		휴일근로신청
		                           		</template>
		                           		<template v-else>
		                           		연장근로신청
		                           		</template>
		                           	</button>
	                           	</div>
                           	</template>
                           	<template v-else>
                           		<div class="col-12">
                           			<button type="button" class="btn btn-cancel btn-block" v-if="inOutChgBtnYn" @click="viewInOutChangeAppl">근태사유서신청</button>
                           			<button type="button" class="btn btn-apply btn-block" v-if="otApplBtnYn" @click="viewOvertimeAppl">
		                           		<template v-if="holidayYn=='Y'">
		                           		휴일근로신청
		                           		</template>
		                           		<template v-else>
		                           		연장근로신청
		                           		</template>
		                           	</button>
                           		</div>
                           	</template>
                    	</template>
                    </div>
                </div>
                <div id="workDayInfo" class="white-box-wrap mb-3" :class="btnNone" style="display:none;">
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
                                <!--  
                                <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.taaNames">
                                    <span class="sub-title"><i class="fas fa-file-alt"></i>근태현황</span>
                                    <span class="sub-desc"></span>
                                    <ul class="sub-desc-list">
                                        <li v-if="workDayInfo.taaNames.indexOf(',')!=-1" v-for="(taa, idx) in workDayInfo.taaNames.split(',')">
                                            <span class="sub-title">{{taa}}</span>
                                            <span class="sub-desc">{{workDayInfo.taaHour.split(',')[idx]}}</span>
                                        </li>
                                        <li v-else>
                                            <span class="sub-title">{{workDayInfo.taaNames}}</span>
                                            <span class="sub-desc">{{workDayInfo.taaHour}}</span>
                                        </li>
                                    </ul>
                                </li>
                                -->
                                <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.hasOwnProperty('taa') && Object.keys(workDayInfo.taa).length>0">
                                    <span class="sub-title"><i class="fas fa-file-alt"></i>근태현황</span>
                                    <span class="sub-desc"></span>
                                    <ul class="sub-desc-list">
                                        <li v-for="(v, k) in workDayInfo.taa">
                                            <span class="sub-title">{{k}}</span>
                                            <span class="sub-desc">{{v}}</span>
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
	                                <span class="sub-desc">
	                                	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.workHour">
	                                	{{workDayInfo.workHour}}
	                                	</template>
	                                </span>
	                            </li>
	                            <li>
	                                <span class="sub-title"><i class="fas fa-moon"></i>연장근로 합산</span>
	                                <span class="sub-desc">
	                                	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.otHour">
	                                	{{workDayInfo.otHour}}
	                                	</template>
	                                </span>
	                            </li>
	                        </ul>
	                    </div>
	                </div>
                </div>
                <div id="flexibleAppl" class="white-box-wrap full-height mb-3" style="display:none;">
                    <div class="work-plan-wrap" v-if="useYn=='Y'">
                        <div class="main-wrap">
                            <div class="main-title">해당일의 근무계획 구분</div>
                            <div class="main-desc">{{calendarTopVue.selectedFlexibleStd.flexibleNm}}</div>
                            <ul class="time-list">
                                <li>
                                    <span class="title">근무가능시간</span>
                                    <span class="desc">
                                    	<template v-if="calendarTopVue.selectedFlexibleStd.workShm && calendarTopVue.selectedFlexibleStd.workEhm">
                                    	{{moment(calendarTopVue.selectedFlexibleStd.useSymd+' '+calendarTopVue.selectedFlexibleStd.workShm).format('HH:mm')}} ~ {{moment(calendarTopVue.selectedFlexibleStd.useSymd+' '+calendarTopVue.selectedFlexibleStd.workEhm).format('HH:mm')}}
                                    	</template>
                                    	<template v-else>
                                    	없음
                                    	</template>
                                    </span>
                                </li>
                                <li>
                                    <span class="title">필수근무시간</span>
                                    <span class="desc">
                                    	<template v-if="calendarTopVue.selectedFlexibleStd.coreShm && calendarTopVue.selectedFlexibleStd.coreEhm">
                                    	{{moment(calendarTopVue.selectedFlexibleStd.useSymd+' '+calendarTopVue.selectedFlexibleStd.coreShm).format('HH:mm')}} ~ {{moment(calendarTopVue.selectedFlexibleStd.useSymd+' '+calendarTopVue.selectedFlexibleStd.coreEhm).format('HH:mm')}}
                                    	</template>
                                    	<template v-else>
                                    	없음
                                    	</template>
                                    </span>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <template v-if="calendarTopVue.selectedFlexibleStd">
                    <template v-if="calendarTopVue.selectedFlexibleStd.workTypeCd!='ELAS'">
                    <div class="sub-wrap" style="display:none;">
                        <form action="" class="time-input-form needs-validation" novalidate>
                            <div class="form-row no-gutters">
                                <div class="form-group col-6 pr-1">
                                    <label for="useSymd">시작일자</label>
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
                                    <select id="workTime" v-if="calendarTopVue.selectedFlexibleStd" class="form-control" v-model="applInfo.workRange" :value="applInfo.workRange" @change="changeWorkRange" required>
                                        <option v-for="term in calendarTopVue.selectedFlexibleStd.usedTermOpt" :value="term.value">{{term.lable}}</option>
                                    </select>
                                </div>
                                <div class="form-group col-12">
                                    <label for="reason">사유</label>
                                    <textarea class="form-control" id="reason" rows="3" placeholder="팀장 확인 시에 필요합니다." v-model="applInfo.reason" required></textarea>
                                </div>
                            </div>
                            <div class="btn-wrap mt-5">
	                            <button id="apprBtn" type="button" class="btn btn-apply btn-block btn-lg" @click="validateFlexitimeAppl(calendarTopVue.selectedFlexibleStd)">확인요청</button>
                            </div>
                        </form>
                    </div>
                    </template>
                    <template v-else>
                    <!-- 탄근제 신청서 -->
                    <div class="sub-wrap" style="display:none;">
                        <form action="" class="time-input-form needs-validation" novalidate>
                            <div class="form-row no-gutters">
                                <div class="form-group col-6 pr-1">
                                    <label for="useSymd">시작일자</label>
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
                                    <select id="workTime" v-if="calendarTopVue.selectedFlexibleStd" class="form-control" v-model="applInfo.workRange" :value="applInfo.workRange" @change="changeWorkRange" required>
                                        <option v-for="term in calendarTopVue.selectedFlexibleStd.usedTermOpt" :value="term.value">{{term.lable}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="btn-wrap mt-5">
	                            <button id="apprBtn" type="button" class="btn btn-apply btn-block btn-lg" @click="validateFlexitimeAppl(calendarTopVue.selectedFlexibleStd)">다음</button>
                            </div>
                        </form>
                    </div>
                    </template>
                    </template>
                </div>
                <div id="flexibleDayPlan" class="white-box-wrap full-height mb-3" style="display:none;">
                	<form class="needs-validation time-input-form" novalidate>
		                <div class="work-plan-wrap">
		                    <div class="big-title">
		                    	<template v-if="flexibleAppl.sYmd && flexibleAppl.eYmd">
		                    	{{moment(flexibleAppl.sYmd).format("YYYY년 M월 D일")}} ~ {{moment(flexibleAppl.eYmd).format("YYYY년 M월 D일")}}({{moment(flexibleAppl.eYmd).diff(flexibleAppl.sYmd, 'days')+1}}일)
		                    	</template>
		                    </div>
		                    <div class="inner-wrap" v-if="flexibleAppl.workShm && flexibleAppl.workEhm || flexibleAppl.coreShm && flexibleAppl.coreEhm">
		                    	<ul class="main-wrap">
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
		                    <div class="inner-wrap graph-wrap" v-if="flexibleAppl.workShm && flexibleAppl.workEhm || flexibleAppl.coreShm && flexibleAppl.coreEhm">
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
                                    <div class="main-title">근무시간표</div>
                                    <div id="timeNm" class="main-desc"></div>
                                </li>
	                            <li>
	                                <div class="main-title">선택일자</div>
	                                <div id="selectedRange" class="main-desc"></div>
	                            </li>
	                        </ul>
	                        <div class="time-input-form form-row no-gutters">
	                            <div class="form-group col-6 pr-1">
	                                <label for="startTime" data-target-input="nearest">출근시간</label>
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="startTime" value="" data-toggle="datetimepicker" data-target="#startTime" autocomplete="off" @focusout="changeWorkTime" :required="flexibleAppl.workTypeCd!='ELAS'?true:false">
	                            </div>
	                            <div class="form-group col-6 pl-1">
	                                <label for="endTime" data-target-input="nearest">퇴근시간</label>
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="endTime" value="" data-toggle="datetimepicker" data-target="#endTime" autocomplete="off" @focusout="changeWorkTime" :required="flexibleAppl.workTypeCd!='ELAS'?true:false">
	                            </div>
	                        </div>
	                        <div id="elasOtTime" class="time-input-form form-row no-gutters" style="display:none;">
	                            <div class="form-group col-6 pr-1">
	                                <label for="otbMinute" data-target-input="nearest">조출시간(분)</label>
	                                <input type="number" class="form-control form-control-sm mr-2" id="otbMinute" value="" @focusout="changeWorkTime">
	                            </div>
	                            <div class="form-group col-6 pl-1">
	                                <label for="otaMinute" data-target-input="nearest">잔업시간(분)</label>
	                                <input type="number" class="form-control form-control-sm mr-2" id="otaMinute" value="" @focusout="changeWorkTime">
	                            </div>
	                        </div>
		                </div>
		                <template v-if="flexibleAppl.workTypeCd=='ELAS'"> <!-- 탄력근무제 신청 -->
		                	<div class="sub-wrap">
			                    <ul class="time-block-list">
			                        <li>
			                            <div class="title">평균 근무 시간</div>
			                            <div class="desc">{{minuteToHHMM(flexibleAppl.avgHour,'detail')}}</div>
			                        </li>
			                    </ul>
			                    <div class="form-row no-gutters">
				                    <div class="form-group col-12">
		                                <label for="reason">사유</label>
		                                <textarea class="form-control" id="reason" rows="3" placeholder="팀장 확인 시에 필요합니다." v-model="applInfo.reason"></textarea>
		                            </div>
	                            </div>
			                </div>
		                	<div class="btn-wrap row no-gutters" v-if="flexibleAppl.applStatusCd=='11'">
			                	<div class="col-6 pr-1">
	                    			<button type="button" id="timeSaveBtn" class="btn btn-cancel btn-block" @click="validateWorkDayResult('N')">저장</button>
	                       		</div>
	                       		<div class="col-6 pl-1">
	                       			<button type="button" id="timeApprBtn" class="btn btn-apply btn-block" @click="validateWorkDayResult('Y')">확인요청</button>
	                    		</div>
	                    	</div>
			            </template>
			            <template v-else>
			            	<div class="sub-wrap">
			                    <ul class="time-block-list">
			                        <li>
			                            <div class="title">약정 근로 시간</div>
			                            <div class="desc">{{minuteToHHMM(flexibleAppl.totalWorkMinute,'detail')}}</div>
			                        </li>
			                        <li>
			                            <div class="title">계획 시간</div>
			                            <div class="desc">{{minuteToHHMM(flexibleAppl.planWorkMinute,'detail')}}</div>
			                        </li>
			                    </ul>
			                </div>
			                <div class="btn-wrap">
			                    <button type="button" id="timeSaveBtn" class="btn btn-apply btn-block btn-lg" @click="validateWorkDayResult('N')">저장</button>
			                </div>
		                </template>
	                </form>
	            </div>
	            <div id="flexibleDayInfo" class="white-box-wrap mb-3" :class="btnNone" style="display:none;">
	                <div class="work-plan-wrap">
	                    <ul class="main-wrap">
	                        <li>
	                            <div class="main-title">근태</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.holidayYn">{{workTimeInfo.holidayYn=='Y'?'휴무일':'근무일'}}</template><template v-if="workTimeInfo.taaNames">,{{workTimeInfo.taaNames}}</template>
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
		//guide slider
		var guide = $('.bxslider').bxSlider({
            mode: 'fade',
            captions: false,
            slideWidth: 900
        });
		
		var pathname = window.location.pathname;
		if(getCookie("notToday")!="Y" && pathname.indexOf("/workCalendar")==-1){
            $("#guide").modal('show');
            setTimeout(function(){
            //bxslider reload
            guide.reloadSlider();
            },200);
        }
		
		$('#guide .modal-footer .close').click(function(){
            // 링크의 페이지 이동 속성 강제 차단
            event.preventDefault();

            if ($('#today-close').is(":checked")){
                // 쿠키값을 "Y"로 하여 하루동안 저장시킴
                setCookie('notToday','Y', 1);
                // $("#guide").modal('hide');
            }

            // 팝업창 닫기
            $("#guide").modal('hide');
        });
		
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
        	
            $('#sub-nav #calendar-top-wrap').fadeToggle(150);
            $('#sub-nav #summary-wrap').fadeToggle(150);
            
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
		
	});
	
	function setCookie(name, value, expiredays) {
        var today = new Date();
        today.setDate(today.getDate() + expiredays);

        document.cookie = name + '=' + escape(value) + '; path=/; expires=' + today.toGMTString() + ';'
    }

    function getCookie(name){
        var cName = name + "="; 
        var x = 0; 
        while ( x <= document.cookie.length ) 
        { 
            var y = (x+cName.length); 
            if ( document.cookie.substring( x, y ) == cName ) 
            { 
                if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 ) 
                    endOfCookie = document.cookie.length;
                return unescape( document.cookie.substring( y, endOfCookie ) ); 
            } 
            x = document.cookie.indexOf( " ", x ) + 1; 
            if ( x == 0 ) 
                break; 
        } 
        return ""; 
    } 
	
   	var calendarTopVue = new Vue({
   		el: "#calendar_top",
	    data : {
	    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
	    	flexitimeList: [], //사용할 유연근무제 리스트
	    	flexibleStd: {} //현재 근무제
  		},
	    mounted: function(){
	    	var $this = this;
			
	    	<#if flexibleStdMgr?? && flexibleStdMgr!='' && flexibleStdMgr?exists >
				$this.flexibleStd = JSON.parse("${flexibleStdMgr?js_string}");
			</#if>
	    	
	    	<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
	    		var flexibleAppl = JSON.parse("${flexibleAppl?js_string}"); //임시저장된 신청서
	    		/* if(flexibleAppl.applStatusCd!=null && flexibleAppl.applStatusCd!='' && flexibleAppl.applStatusCd!='99') {
					//신청화면 전환
					$("#applyBtn").bind('click', function(){
						 calendarLeftVue.viewFlexitimeAppl(flexibleAppl);
					});
	    		} else {
	    			$("#applyBtn").bind('click', function(){
	    				$this.getFlexitimeList();
					});
	    		} */
	    	</#if>
	    	$this.getFlexitimeList();
	    },
	    watch: {
	    	flexitimeList : function(val, oldVal) {
	    		if(val!=null && val.length>0 && $(".fc-legend-button").length>0) {
	    			var legend = '<div class="sub-info-wrap clearfix">         '
		    			+'	<div class="form-inline work-check-wrap"> '
		    			+'		<span class="title">근무제 표시</span>    '
		    			+'		<ul class="legend-list-wrap">         '
		    			;
		    		
	    			val.map(function(v){
	    				legend += '			<li class="'+v.workTypeCd+'">'+v.flexibleNm+'</li>        ';
	    			});
		    			
		    		legend += '		</ul>                                 '
		    			+'	</div>                                    '
		    			+' <div>                                       '
		    			;
		    			
		    		document.querySelector(".fc-legend-button").innerHTML= legend;
	    		} 
	    	}
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
							//$("#flexitimeModal").modal("show"); 
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
         		
         		$("#flexitimeModal .list-group-item").not(idx).removeClass("active");
         		$("#flexitimeModal .list-group-item").eq(idx).addClass("active");
         		 
         		//선택한 근무제 적용
         		$this.selectedFlexibleStd = $this.flexitimeList[idx];
         		$("#applyFlexBtn").show();
         	},
         	applyFlexitime : function(){ //근무제 적용
         		var $this = this;
         	
         		//calendarLeftVue.clearFlexitimeAppl();
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
 	         			//monthCalendarVue.getPrevFlexitime();
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
	    	holidayYn: '',
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
	    	workPlanYn: false,
	    	flexCancelBtnYn: false,
	    	workPlanBtnYn: false,
	    	inOutChgBtnYn: false,
	    	otApplBtnYn: false
  		},
  		computed: {
	    	btnNone: function(val, oldVal) {
	    		return this.flexCancelBtnYn || this.workPlanBtnYn || this.inOutChgBtnYn || this.otApplBtnYn?"":"btn-none";
	    	}
	    },
  		watch: {
  			rangeInfo : function(val, oldVal) {
  				//총 계획 근무시간,잔여 근무시간 그래프 표기
  				
  				var tMin = Number(val.totalWorkMinute + val.totalOtMinute) / 60;
  				var tWorkMin = Number(val.totalWorkMinute) / 60;
  				var tOtMin = Number(val.totalOtMinute) / 60;
  				var rMin = Number(val.restWorkMinute + val.restOtMinute) / 60;
  				var rWorkMin = Number(val.restWorkMinute) / 60;
  				var rOtMin = Number(val.restOtMinute) / 60;
  				
  				$(".work-info-wrap .time-graph.plan .work-time").css({ 'width': 'calc('+tWorkMin+'/'+tMin+' * 100%)' });
  				$(".work-info-wrap .time-graph.plan .over-time").css({ 'left': 'calc(('+tWorkMin+' - 1)/'+tMin+' * 100%)' });
  				$(".work-info-wrap .time-graph.plan .over-time").css({ 'width': 'calc(('+tOtMin+' + 1)/'+tMin+'* 100%)' });
  				
  				$(".work-info-wrap .time-graph.rest .work-time").css({ 'width': 'calc('+rWorkMin+'/'+tMin+' * 100%)' });
  				
  				if(rWorkMin==0) {
  					$(".work-info-wrap .time-graph.rest .over-time").css({ 'left': 'calc(('+rWorkMin+')/'+tMin+' * 100%)' });
  	  				$(".work-info-wrap .time-graph.rest .over-time").css({ 'width': 'calc(('+rOtMin+')/'+tMin+'* 100%)' });
  				} else {
  					$(".work-info-wrap .time-graph.rest .over-time").css({ 'left': 'calc(('+rWorkMin+' - 1)/'+tMin+' * 100%)' });
  	  				$(".work-info-wrap .time-graph.rest .over-time").css({ 'width': 'calc(('+rOtMin+' + 1)/'+tMin+'* 100%)' });
  				}
  			}/* ,
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
  			} */
  		},
	    mounted: function(){
	    	//this.getFlexibleRangeInfo(this.today);
	    	//this.getFlexibleDayInfo(this.today);
	    	//calendarLeftVue.getWorkDayInfo(this.today);
	    	
	    	<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
    			this.flexibleAppl = JSON.parse("${flexibleAppl?js_string}"); 
    			
    			//if('${calendar}' == 'workDayCalendar') {
    			//	dayCalendarVue.getWorkDayResult();
    	    	//}
    		</#if>
	    },
	    methods : {
	    	getFlexibleRangeInfo : function(ymd){ //오늘 또는 선택한 기간의 근무제 정보(남색 박스)
				var $this = this;
		    	
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
						//console.log(data);
						if(data!=null) {
							$this.rangeInfo = data;
							
							var now = '';
							<#if today?? && today!='' && today?exists >
								now = '${today}';
							</#if>
							
							$this.flexCancelBtnYn = false;
							$this.workPlanBtnYn = false;
							if('${calendar}'=='workMonthCalendar' && data.baseWorkYn!=null && data.baseWorkYn!=undefined && data.baseWorkYn!='Y') {
								//근무제적용취소
								if(data.sYmd!=null && moment(now).diff(data.sYmd)<0){
									$this.flexCancelBtnYn = true;
								}
								
								//근무계획작성
								//if(now!='' && moment(now).diff(data.eYmd)<=0)
								//	$this.workPlanBtnYn = true;
							} 
						} else {
							//근무제적용취소,근무계획작성 버튼 숨기기
							$this.flexCancelBtnYn = false;
							$this.workPlanBtnYn = false;
						}
					},
					error: function(e) {
						$this.flexCancelBtnYn = false;
						$this.workPlanBtnYn = false;
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
						//console.log(data);
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
						$("#loading").hide();
						$this.flexibleAppl = {};
						if(data!=null) {
							$this.flexibleAppl = data;
							
							//if('${calendar}' == 'workDayCalendar') {
							//	dayCalendarVue.getWorkDayResult();
					    	//}
						} 
						
					},
					error: function(e) {
						$("#loading").hide();
						$this.flexibleAppl = {};
					}
				});
         	},
         	viewFlexitimeAppl : function(obj){ //유연근무제 신청서
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
         	viewElasFlexitimeAppl : function(){ //탄근제 신청서
         		var $this = this;
         		var flexibleStd = calendarTopVue.selectedFlexibleStd;
         		
         		var param = {
         			flexibleStdMgrId : flexibleStd.flexibleStdMgrId,
         			sYmd : moment($this.applInfo.useSymd).format('YYYYMMDD'),
   		    		eYmd : moment($this.applInfo.useEymd).format('YYYYMMDD')
         		};
         		
         		$this.flexitimeApplImsi();		
         		
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
         	
         		$("#loading").show();
  	         	
         		//선택한 근무제
         		var flexibleStd = calendarTopVue.selectedFlexibleStd;
         		
       			var param = {
       				flexibleStdMgrId : flexibleStd.flexibleStdMgrId,
       				workTypeCd : flexibleStd.workTypeCd,
       				//empNo : "${empNo}",
       				applId : $this.applInfo.applId,
   		    		sYmd : moment($this.applInfo.useSymd).format('YYYYMMDD'),
   		    		eYmd : moment($this.applInfo.useEymd).format('YYYYMMDD')
   		    	};
         			
       			//탄근제의 경우 근무패턴 조회하여 flexibleApplDet에 저장하고 출/퇴근시간, 조출/잔업 입력하는 근무 계획 작성 화면으로 넘어감
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
							
							//탄근제의 경우 근무 계획 작성 화면으로 전환
							if(flexibleStd.workTypeCd=='ELAS') 
								$this.planElasWorkDay(data.flexibleApplId);
						} else {
							$("#loading").hide();
						}
					},
					error: function(e) {
						console.log(e);
						$("#loading").hide();
						$("#alertText").html("저장 시 오류가 발생했습니다.");
  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
  	  	         		$("#alertModal").modal("show"); 
					}
				}); 

         	},
         	validateFlexitimeAppl : function(flexStd){
         		var $this = this;
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
         		
         		//console.log(this.applInfo);
         		
         		if(applYn) {
         			if(flexStd.workTypeCd == 'ELAS') { //탄근제
         				
         				var param = {
         					ymd: moment($this.applInfo.useSymd).format('YYYYMMDD')
         				};
         				
         				//선택한 근무제의 패턴 시작일과 신청 시작일의 요일이 같은지 확인
         				Util.ajax({
        					url: "${rc.getContextPath()}/flexibleStd/dayOfWeek",
        					type: "GET",
        					contentType: 'application/json',
        					data: param,
        					dataType: "json",
        					success: function(data) {
        						var viewYn = false;
        						if(data!=null && data.status=='OK' && data.hasOwnProperty('weekDay'))
        							if(data.weekDay == flexStd.weekDay)
        								viewYn = true;
        						
        						if(viewYn) {
        							$this.viewElasFlexitimeAppl();
        						}else {
        							$("#alertText").html("탄력근무제의 시작 요일은 "+flexStd.weekDay+" 입니다.<br>달력에서 근무제 시작일을 다시 지정해 주세요.");
        		  	         		$("#alertModal").on('hidden.bs.modal',function(){
        		  	         			$("#alertModal").off('hidden.bs.modal');
        		  	         			var eventId = 'workRange.'+flexStd.workTypeCd+'.new';
										var event = $this.calendar.getEventById(eventId);
										if(event!=null)
											event.remove();
										
										$this.applInfo.useSymd='';
        		  	         		});
        		  	         		$("#alertModal").modal("show"); 
        						}
        							
        					},
        					error: function(e) {
        						console.log(e);
        						$("#alertText").html("탄력근무제의 시작 요일은 "+flexStd.weekDay+" 입니다.<br>달력에서 근무제 시작일을 다시 지정해 주세요.");
    		  	         		$("#alertModal").on('hidden.bs.modal',function(){
    		  	         			$("#alertModal").off('hidden.bs.modal');
    		  	         			var eventId = 'workRange.'+flexStd.workTypeCd+'.'+$this.applInfo.useSymd;
									var event = $this.calendar.getEventById(eventId);
									if(event!=null)
										event.remove();
									
									$this.applInfo.useSymd='';
    		  	         		});
    		  	         		$("#alertModal").modal("show"); 
        					}
        				});
         				
         			} else {
         				this.flexitimeAppl(calendarTopVue.selectedFlexibleStd.workTypeCd);
         			}
         		}
         		
         	},
         	validateElasFlexitimeAppl : function() { //탄근제 확인요청 시 체크
         		dayCalendarVue.validateElasFlexitimeAppl();
         	},
         	flexitimeAppl : function(workTypeCd){ //확인요청
	         	var $this = this;
         	
	         	$("#loading").show();
	  	         	
	         	//선택한 근무제
	         	//var flexibleStd = calendarTopVue.selectedFlexibleStd;
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
         		
         		
         		//if(flexibleStd.workTypeCd.indexOf('SELE')==0 || flexibleStd.workTypeCd.indexOf('DIFF')==0) {
					var param = {
						flexibleApplId : flexibleAppl.flexibleApplId,	
						applId : flexibleAppl.applId,
 	         			flexibleStdMgrId : flexibleAppl.flexibleStdMgrId,
 	         			workTypeCd : workTypeCd,
 	         			//empNo : "${empNo}",
	   		    		reason: applInfo.reason
	   		    	};
					
					if(flexibleAppl.workTypeCd== 'ELAS') {
						param['sYmd'] = flexibleAppl.sYmd;
						param['eYmd'] = flexibleAppl.eYmd;
					} else {
						param['sYmd'] = moment($this.applInfo.useSymd).format('YYYYMMDD');
						param['eYmd'] = moment($this.applInfo.useEymd).format('YYYYMMDD');
					}
					
					
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleAppl/request",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							$("#loading").hide();
							
							if(data!=null) {
								if(data.status=='OK') {
									$("#alertText").html("확인요청 되었습니다.");
									$("#alertModal").on('hidden.bs.modal',function(){
										$("#alertModal").off('hidden.bs.modal');
										
										location.reload();
									});
								} else {
									$("#alertText").html(data.message);
									$("#alertModal").on('hidden.bs.modal',function(){
										$("#alertModal").off('hidden.bs.modal');
										
										var eventId = 'workRange.'+workTypeCd+'.'+$this.applInfo.useSymd;
										var event = $this.calendar.getEventById(eventId);
										if(event!=null)
											event.remove();
										
									});
								}
								
		  	  	         		$("#alertModal").modal("show"); 
							}
						},
						error: function(e) {
							$("#loading").hide();
							console.log(e);
							$("#alertText").html("확인요청 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					});
					
	         	//} else if(flexibleStd.workTypeCd.indexOf('ELAS')==0){
	         	//	$("#flexibleAppl").hide();
	         	//}
	        },
	        cancelFlexitime: function(){ //근무제 적용 취소
	        	
	        	var $this = this;
	        
	        	$("#loading").show();
				
	        	var param = $this.rangeInfo;
	        	param.applCd = $this.rangeInfo.workTypeCd;
	        
         		Util.ajax({
					url: "${rc.getContextPath()}/appl/delete",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("loading").hide();
						if(data!=null && data.status=='OK') {
							$("#alertText").html("취소되었습니다.");
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
						$("loading").hide();
						console.log(e);
						$("#alertText").html("취소 시 오류가 발생했습니다.");
	 	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	 	  	         			$("#alertModal").off('hidden.bs.modal');
	 	  	         		});
	 	  	         		$("#alertModal").modal("show"); 
					}
				});
	        	
			},
	        validateWorkDayResult : function(lastYn){
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
         			this.saveWorkDayResult(lastYn);
         		}
         		
         	},
         	saveWorkDayResult: function(lastYn){
         		dayCalendarVue.saveWorkDayResult(lastYn);
         	},
         	viewWorkDayCalendar: function(date){
         		location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Day&date='+date;
         	},
         	viewOvertimeAppl: function(){
         		var $this = this;
         		var info = {
         			date : $this.calendar.getDate()
         		};
         		timeCalendarVue.preCheck(info, true);
         	},
         	viewInOutChangeAppl: function(){
         		$("#inOutChangeModal").modal("show"); 
         	},
         	planElasWorkDay: function(flexibleApplId){
         		location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Day&flexibleApplId='+flexibleApplId;
         	}
	    }
   	});
   	
   	$('#flexitimeModal').on('hidden.bs.modal',function(){
   		$(".list-group-item").removeClass("active");
   		monthCalendarVue.prevEdate = '';
   		$("#applyFlexBtn").hide();
   	});
   	
    $("#useSymd").off("change.datetimepicker").on("change.datetimepicker", function(e){
   		if(e.date!=null && e.date!='undefined' && e.date!='') {
	   		if(e.oldDate!=null && e.oldDate!='undefined' && e.date!=e.oldDate) {
	   			calendarLeftVue.applInfo.useSymd = moment(e.date).format('YYYY-MM-DD');
	   	    	calendarLeftVue.changeWorkRange();
	   		}
   		}
    });
  	
</script>

