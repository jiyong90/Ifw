<div class="bg-light-blue">
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
	<#include "/applLineComponent.ftl">
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
		                    <ul class="nav approval-wrap nav-pills" role="tablist">
		                    	<li class="nav-item">
					                <a href="#" class="nav-link" :class="{active: flexStatus=='02'?true:false}" data-toggle="pill" role="tab" @click="changeFlexStatus('02')"
					                    aria-controls="pills-profile" :aria-selected="flexStatus=='02'?true:false">임시저장한 근무제</a>
					            </li>
					            <li class="nav-item">
					                <a href="#" class="nav-link" :class="{active: flexStatus=='01'?true:false}" data-toggle="pill" role="tab" @click="changeFlexStatus('01')"
					                    aria-controls="pills-home" :aria-selected="flexStatus=='01'?true:false">신규 근무제</a>
					            </li>
					        </ul>
		                    <div class="mt-3">
			                	<!-- <button class="btn btn-outline btn-flat btn-block text-left" type="button" data-toggle="collapse" data-target="#collapWork" aria-expanded="false" aria-controls="collapseExample">근무제</button> -->
		                        <!-- <div class="collapse" id="collapWork"> -->
		                       	<div v-if="flexStatus=='01'">
		                            <ul class="list-group select-work-list">
		                                <li class="list-group-item new-flex" v-for="(f, fIdx) in flexitimeList" @click="selectFlexitime(fIdx)">
		                                    <span class="tag">
			                                    <span :class="f.workTypeCd">
			                                    	{{f.workTypeNm}}
			                                    </span>
		                                    </span>
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
		                        <div v-if="flexStatus=='02'">
		                             <ul class="list-group select-work-list" v-if="imsiFlexitimeList.length>0">
		                                <li class="list-group-item imsi-flex" v-for="(i, iIdx) in imsiFlexitimeList" @click="selectImsiFlexitime(iIdx)">
		                                    <span class="tag">
			                                    <span :class="i.workTypeCd">
			                                    {{i.workTypeNm}}
			                                    </span>
			                                    <span class="tmp-save">임시저장</span>
		                                    </span>
		                                    <div class="title">{{i.flexibleNm}}</div>
		                                    <div class="desc" v-if="i.sYmd && i.eYmd">
		                                    	근무기간: {{moment(i.sYmd).format('YYYY-MM-DD')}} ~ {{moment(i.eYmd).format('YYYY-MM-DD')}}
		                                    </div>
		                                    <button type="button" class="delete" @click="calendarLeftVue.cancelFlexitime(i, 'imsi')">
                                                <span>삭제</span>
                                            </button>
		                                </li>
		                            </ul>
                                	<div class="desc" v-else>
                                    	임시저장한 근무제가 없습니다.
                                    </div>
		                        </div>
			                </div>
		                </div>
		                <div class="btn-wrap text-center">
		                    <button type="button" class="btn btn-secondary  rounded-0" data-dismiss="modal">취소</button>
		                    <button type="button" id="applyFlexBtn" class="btn btn-default rounded-0" style="display:none;" @click="applyFlexitime">적용하기</button>
		                    <button type="button" id="rewriteBtn" class="btn btn-default rounded-0" style="display:none;" @click="rewriteFlexitime">재작성하기</button>
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
	                <div v-if="'${calendar}'=='workMonthCalendar'" class="col-12 col-sm-4 col-md-4 col-lg-4 col-xl-4">
	                    <div class="btn-wrap text-right">
							<div v-if="'${tsId}'== 'dyc'" class="col-12">
								<button type="button" id="annualApplBtn" class="btn btn-request mr-2">휴가 신청</button>
								<button type="button" id="taaApplBtn" class="btn btn-request mr-2">출장/비상근무</button>
							</div>
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
                            <div class="sub-title">근로시간 산정 구간 평균 근무시간</div>
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
                                	<span class="sub-title"><i class="fas fa-clock"></i>간주근로</span>
                                    <span class="sub-desc">
                                    	<template v-if="Object.keys(workDayInfo).length>0 && workDayInfo.regaHour">
		                            	{{workDayInfo.regaHour}}
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
                                <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.taa && Object.keys(workDayInfo.taa).length>0">
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
                                        <li v-if="Object.keys(workDayInfo).length>0 && workDayInfo.paidHour">
                                            <span class="sub-title">유급</span>
                                            <span class="sub-desc">
				                            	{{workDayInfo.paidHour}}
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
                                <div class="form-group col-12" >
                                    <label for="reason">사유</label>
                                    <textarea class="form-control" id="reason" rows="3" placeholder="팀장 확인 시에 필요합니다." v-model="applInfo.reason" required></textarea>
                                </div>
                                <appl-line :bind-data="applLine"></appl-line>
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
	                                <!--<input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="startTime" value="" data-toggle="datetimepicker" data-target="#startTime" autocomplete="off" @focusout="changeWorkTime" :required="flexibleAppl.workTypeCd!='ELAS'?true:false">-->
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="startTime" value="" data-toggle="datetimepicker" data-target="#startTime" autocomplete="off" @focusout="changeWorkTime">
	                            </div>
	                            <div class="form-group col-6 pl-1">
	                                <label for="endTime" data-target-input="nearest">퇴근시간</label>
	                                <!--<input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="endTime" value="" data-toggle="datetimepicker" data-target="#endTime" autocomplete="off" @focusout="changeWorkTime" :required="flexibleAppl.workTypeCd!='ELAS'?true:false">-->
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="endTime" value="" data-toggle="datetimepicker" data-target="#endTime" autocomplete="off" @focusout="changeWorkTime">
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
		                                <label for="opinion">사유</label>
		                                <textarea class="form-control" id="opinion" rows="3" placeholder="팀장 확인 시에 필요합니다." v-model="applInfo.reason"></textarea>
		                            </div>
	                            </div>
	                            <appl-line :bind-data="applLine"></appl-line>
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
			            	<div class="sub-wrap position-relative">
			            		<div class="loading-spinner" style="display:none;"></div>
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
	                            <div class="main-title">간주 근무시간</div>
	                            <div class="main-desc">
	                            	<template v-if="workTimeInfo.regaMinute">
	                            	{{minuteToHHMM(workTimeInfo.regaMinute, 'detail')}}
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
	<div id="modalVue">
		<!-- 휴가신청 요청 modal start -->
		<div class="modal fade show" id="annualCreateApplModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog col-4" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">휴가 신청</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form class="needs-validation" id="annualUsedFrm" novalidate>
							<div class="modal-app-wrap">
								<div class="form-row no-gutters">
									<div class="form-row col-12 mt-2">
										<div class="form-group col-3 ">
											<label for="reasonCd">휴가구분</label>
										</div>
										<div class="form-group col-9">
											<select id="annualTaCd" name="annualTaCd" class="form-control" required>
												<option value="" disabled selected hidden>휴가를 선택해주세요.</option>
											</select>
										</div>
									</div>
									<div class="form-row col-12 mt-2">
										<div class="form-group col-3 ">
											<label for="reasonCd">휴가구분상세</label>
										</div>
										<div class="form-group col-9">
											<select id="annualTaDetailCd" name="annualTaDetailCd" class="form-control" required>
												<option value="" disabled selected hidden>휴가상세를 선택해주세요.</option>
											</select>
											<input id="annualRequestCd" name="annualRequestCd" type="hidden"/>
										</div>
									</div>

									<section id="annualDepList" class="form-row col-12 mt-2 mb-2">
										<div class="form-row col-12 mt-2">
											<div class="form-group col-3 mt-2">
												<label for="reasonCd">신청기간</label>
											</div>
											<div class="form-group col-9" id="annualRowGroup">
												<div class="col-12 pl-0 pr-0 mb-1">
													<input type="text" class="form-control datetimepicker-input annual-dt form-control-sm col-4" id="annualInputSDate" data-target="#annualInputSDate"
														   onchange="syncAnnualDate();"
														   data-toggle="datetimepicker" placeholder="연도-월-일" autocomplete="off" required="" style="display:inline-flex;">
													<span class="d-sm-block d-md-block d-lg-inline-block annual-dt text-center pl-2 pr-2 mt-1 " required="" style="display: inline-flex;">~</span>
													<input type="text" class="form-control datetimepicker-input form-control-sm col-4" id="annualInputEDate" data-target="#annualInputEDate"
														   data-toggle="datetimepicker" placeholder="연도-월-일" autocomplete="off" required="" style="display:inline-flex;">
													<button type="button" id="addRowAnnualBtn" class="btn btn-default btn-flat btn-sm" style="margin-bottom: 3px;">추가</button>
												</div>
											</div>
											<div class="form-row col-12 mt-2">
												<div class="form-group col-3 mt-2">

												</div>
												<div class="form-group col-9" id="annDtList">

												</div>
											</div>
											<div class="form-row col-12 mt-2">
												<div class="form-group col-3 mt-2">
													<label for="reasonCd">총일수</label>
												</div>
												<div class="form-group col-3">
													<input type="text" value="0" class="form-control form-control-sm mr-2" id="annualTotalCnt" autocomplete="off" readonly="readonly">
												</div>
												<div class="form-group col-3 pl-3">
													<label for="reasonCd">사용일수</label>
												</div>
												<div class="form-group col-3">
													<input type="text" value="0" class="form-control form-control-sm mr-2" id="annualUsedCnt" autocomplete="off" readonly="readonly">
												</div>
											</div>
											<div class="form-row col-12 mt-2">
												<div class="form-group col-3 mt-2">
													<label for="reasonCd">발생일수</label>
												</div>
												<div class="form-group col-3">
													<input type="text" value="0" class="form-control form-control-sm mr-2" id="annualCreateCnt" readonly="readonly">
												</div>
												<div class="form-group col-3 pl-3">
													<label for="reasonCd">잔여일수</label>
												</div>
												<div class="form-group col-3">
													<input type="text" value="0" class="form-control form-control-sm mr-2" id="annualNotUsedCnt" readonly="readonly">
												</div>
											</div>
											<div class="form-row col-12 mt-2">
												<div class="form-group col-3 mt-2">
													<label for="otReason">설명</label>
												</div>
												<div class="form-group col-9">
									<textarea class="form-control" id="annualUsedNote" name="annualUsedNote" rows="3"
											  placeholder="" required></textarea>
												</div>
											</div>
									</section>
								</div>
							</div>
							<div class="btn-wrap text-center">
								<button type="button" class="btn btn-secondary rounded-0"
										data-dismiss="modal">취소</button>
								<button type="button" class="btn btn-default rounded-0" @click="saveAnnualCreate">신청</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 휴가신청  정정요청 modal end -->

		<#-- 출장/비상근무 신청 modal start -->
		<div class="modal fade show" id="taaApplModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog col-6" role="document">
				<div class="modal-content rounded-0">
					<div class="modal-header">
						<h5 class="modal-title">출장/비상근무</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form class="needs-validation" id="taaFrm" novalidate>
							<div class="modal-app-wrap">
								<div class="form-row no-gutters">
									<div class="form-row col-12 mt-2">
										<div class="form-group col-3 ">
											<label for="reasonCd">근무구분</label>
										</div>
										<div class="form-group col-9">
											<select id="taaTypeCd" name="taaTypeCd" class="form-control" required>
												<option value="" disabled selected hidden>근무구분을 선택해주세요.</option>
											</select>
										</div>
									</div>


									<div class="form-row col-12 mt-2">
										<div class="form-group col-3 mt-2">
											<label for="reasonCd">신청기간</label>
										</div>
										<div class="form-group col-9" id="taaRowGroup">

										</div>
									</div>
									<div class="form-row col-12 mt-2">
										<div class="form-group col-3 mt-2">
											<label for="reasonCd"></label>
										</div>
										<div class="form-group col-9">
											<button type="button" id="addRowTaaBtn" class="btn btn-default btn-flat btn-sm mt-1">추가</button>
										</div>
									</div>
									<div class="form-row col-12 mt-2">
										<div class="form-group col-3 mt-2">
											<label for="otReason">사유</label>
										</div>
										<div class="form-group col-9">
									<textarea class="form-control" id="taaNote" name="taaNote" rows="3"
											  placeholder="" required></textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="btn-wrap text-center">
								<button type="button" class="btn btn-secondary rounded-0"
										data-dismiss="modal">취소</button>
								<button type="button" class="btn btn-default rounded-0" @click="saveTaa">신청</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<#-- 출장/비상근무 신청 modal end -->
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

		// 휴가 신청 버튼 이벤트
		$('#annualApplBtn').on('click', function(){
			calendarLeftVue.viewAnnuaCreatelAppl();
		});

		//	출장/비상근무 신청 버튼 이벤트
		$('#taaApplBtn').on('click', function(){
			calendarLeftVue.viewTaaAppl();
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
	    	flexStatus: '',
	    	flexitimeList: [], //사용할 유연근무제 리스트
	    	flexibleStd: {}, //현재 근무제
	    	legendList: [],
	    	imsiFlexitimeList: [], //재작성할 근무제 리스트
	    	selectedFlexibleStd: {},
	    	selectedImsiFlexibleStd: {}
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
	    	$this.getImsiFlexitimeList();
	    },
	    watch: {
	    	flexitimeList : function(val, oldVal) {
	    		if(val!=null && val.length>0 && $(".fc-legend-button").length>0) {
	    			//신청 가능한 유연근무제 리스트
	    			this.legendList = [];
    				this.drawLegend(val, true);
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
		     getImsiFlexitimeList : function(){ //임시저장된 유연근무제 신청서 리스트
	         	var $this = this;
		     
	         	$this.flexStatus = '02';
		     
	        	var param = {
	        		applStatusCd : '11'
	        	};
         	
		    	Util.ajax({
					url: "${rc.getContextPath()}/flexibleAppl/list",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$this.imsiFlexitimeList = [];
						if(data!=null && data!=undefined && data.length>0) {
							$this.imsiFlexitimeList = data;
						} else {
							$this.flexStatus = '01';
						}
					},
					error: function(e) {
						console.log(e);
						$this.imsiFlexitimeList = [];
					}
				});
		     },
	         selectFlexitime : function(idx){ //사용할 근무제 선택
         		var $this = this;
         		
         		$("#flexitimeModal .list-group-item.new-flex").not(idx).removeClass("active");
         		$("#flexitimeModal .list-group-item.new-flex").eq(idx).addClass("active");
         		 
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
         	},
         	drawLegend: function(val, isGuide){
         		var $this = this;
         		
         		var keys = [];
         		$this.legendList.map(function(l){
         			keys.push(l.workTypeCd);
         		});
         		
         		val.map(function(v){
         			var workTypeCd = v.workTypeCd;
					if(v.workTypeCd==null || v.workTypeCd=='' || !v.hasOwnProperty('workTypeCd'))
						workTypeCd = v.applCd;
         			
    				var o = {
    					workTypeCd : workTypeCd,
    					//flexibleNm : v.flexibleNm
    					workTypeNm : v.workTypeNm
    				};
    				
    				if(keys.indexOf(o.workTypeCd)==-1 && (!v.hasOwnProperty('baseWorkYn') || (v.hasOwnProperty('baseWorkYn')&&v.baseWorkYn!='Y'))) {
    					$this.legendList.push(o);
    					keys.push(o.workTypeCd);
    				}
    			});
         		
         		if($this.legendList.length>0) {
    				var legend = '<div class="sub-info-wrap clearfix">         '
		    			+'	<div class="form-inline work-check-wrap"> '
		    			+'		<span class="title">근무제 표시</span>    '
		    			+'		<ul class="legend-list-wrap">         '
		    			;
		    		
		    		$this.legendList.map(function(v){
	    				legend += '			<li class="'+v.workTypeCd+'">'+v.workTypeNm+'</li>        ';
	    			});
		    			
		    		legend += '		</ul>                                 '
		    			+'	</div>                                    '
		    			+' <div>                                       '
		    			;
		    			
		    		document.querySelector(".fc-legend-button").innerHTML= legend;
		    		
	                $(".bxslider").append('<li><img src="${rc.getContextPath()}/soldev/img/guide/p07.png" title="직원이 직접 유연근무제적용을 선택할 수 있습니다."></li>');
	                $(".bxslider").append('<li><img src="${rc.getContextPath()}/soldev/img/guide/p08.png" title="근무계획작성 버튼을 클릭해 특정기간의 근무계획을 작성할 수 있습니다."></li>');
	                $(".bxslider").append('<li><img src="${rc.getContextPath()}/soldev/img/guide/p09.png" title="여러 날을 동시에 선택해 근무계획을 입력할 수 있습니다."></li>');
    			} 
         		
         		if(isGuide)
         			this.loadSlider();
         	},
         	loadSlider: function() {
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
         	},
         	changeFlexStatus: function(flexStatus){
         		this.flexStatus=flexStatus; 
         		
         		if(flexStatus=='01') {
         			$("#rewriteBtn").hide();
         		} else if(flexStatus=='02') {
         			$("#applyFlexBtn").hide();
         		}
         		
         	},
         	selectImsiFlexitime: function(idx) {
				var $this = this;
         		
         		$("#flexitimeModal .list-group-item.imsi-flex").not(idx).removeClass("active");
         		$("#flexitimeModal .list-group-item.imsi-flex").eq(idx).addClass("active");
         		 
         		//선택한 근무제 적용
         		$this.selectedFlexibleStd = $this.imsiFlexitimeList[idx];
         		$("#rewriteBtn").show();
         	},
         	rewriteFlexitime: function() {
         		var $this = this;
             	
         		$('#flexitimeModal').on('hidden.bs.modal',function(){
         			$('#flexitimeModal').off('hidden.bs.modal');
         			$(".list-group-item").removeClass("active");

	         		calendarLeftVue.useYn='Y';
         			
         			//신청화면 전환
         			calendarLeftVue.viewFlexitimeAppl($this.selectedFlexibleStd);
         		});
         		$('#flexitimeModal .close').click();
         	}
	    }
   	});
   	
   	var calendarLeftVue = new Vue({
   		el: "#calendar_left",
   		components : {
			'appl-line': applLine
	    },
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
	    	otApplBtnYn: false,
	    	applLine: []
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

         		/* if(obj!=null) {
         			
         			if(obj.applStatusCd!='11') { //결재요청
         				$("#apprBtn").hide();
         				$("#flexibleAppl").find("input,select,textarea").prop("disabled", true);
         			}
         			
         			$this.calendar.gotoDate($this.applInfo.useSymd);
         		} */
         		
         		if(obj!=null) {
         			monthCalendarVue.startDaySelect(moment(obj.sYmd).format('YYYY-MM-DD'));
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
							$("#alertText").html(data.message);
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
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
	        cancelFlexitime: function(obj, gubn){ //근무제 적용 취소
	        	
	        	var $this = this;
	        
	        	$("#loading").show();
				
				var param;
				if(gubn!=null && gubn == 'imsi') {
					param = obj;
				} else {
					param = $this.rangeInfo;
		        	param.applCd = $this.rangeInfo.workTypeCd;
				}
	        
         		Util.ajax({
					url: "${rc.getContextPath()}/appl/delete",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
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
						$("#loading").hide();
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
         		timeCalendarVue.applLine = this.getApplLine('ENTRY_CHG');
         		$("#inOutChangeModal").modal("show"); 
         	},
         	planElasWorkDay: function(flexibleApplId){
         		location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Day&flexibleApplId='+flexibleApplId;
         	},
			viewAnnuaCreatelAppl: function(){	//	휴가신청 모달 호출

				modalCalendarVue.resetAnnualCreateModal();
				$('#annualTaCd').val('');
				$('#annualTaDetailCd').val('');

				//	초기화
				$("#annualCreateApplModal").modal("show");
			},
			viewTaaAppl : function(){	//	출장/비상근무 신청
				console.log("viewTaaAppl >>> call");
				$('#taaRowGroup').html(null);
				$('#taaTypeCd').val('');
				$('#taaNote').val('');
				modalCalendarVue.addTaaRow();
				$("#taaApplModal").modal("show");
			},

         	getApplLine: function(applCd) {
         		var $this = this;
         		var applLine = [];
         		var param = {
					applCd: applCd
				};
				
				//결재라인
				Util.ajax({
					url: "${rc.getContextPath()}/appl/line",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					async: false,
					success: function(data) {
						if(data!=null && data.status=='OK') {
							applLine = data.applLine;
						}
						
					},
					error: function(e) {
						console.log(e);
					}
				});
				
				return applLine;
			
         	}
	    }
   	});


	var modalCalendarVue = new Vue({
		el: "#modalVue",
		data : {
			t: true,
			vueNm : 'modalCalendarVue > ',
			result: {}, //일근무시간
			workday: '${today}', //근무일
			reasons: [], //연장/휴일 근로 사유
			subYmds: [], //대체휴일
			overtime: {}, //연장/휴일 근로시간, 휴게시간
			otTime: 0, //잔여소정근로시간 외 연장근로시간
			overtimeAppl: {},
			applCode: {}, //신청서 정보
			subsYn: false, //대체휴가 사용 여부
			payTargetYn: false, //수당 지급 대상자
			inOutChangeAppl: {},
			applLine: [],
			targets: {}, //대상자 잔여 연장근로시간
			//prevOtSubs: [] //이전에 신청한 휴일
			chgSubsAppl: {}, //대체휴일 정정 데이터
			isOtUse:true, //대체휴일 Div section사용 여부 기본근무에서 잔여 소정근로시간이 남았을 경우 잔여소정근로시간 선 소진 후 나머지 시간이 있을 경우 사용할 수 있
			workCloseYn: 'N'
		},
		computed: {
			subsRequired: function(val, oldVal) {
				return this.result.holidayYn=='Y'&&(this.subsYn=='Y'||this.payTargetYn)&&this.isOtUse?true:false;
			}
		},
		mounted: function(){
			<#if workday?? && workday!='' && workday?exists >
			this.workday = moment('${workday}').format('YYYY-MM-DD');
			<#else>
			this.workday = '${today}';
			</#if>

			<#if workCloseYn?? && workCloseYn!='' && workCloseYn?exists >
			this.workCloseYn = '${workCloseYn}';
			</#if>

			<#if reasons?? && reasons!='' && reasons?exists >
			this.reasons = JSON.parse('${reasons?js_string}');
			</#if>

		},
		methods : {
			addTaaRow : function(){

				var rowCnt = $('input[name="taaDate"]').length;
				var _rowHtml = "<div class=\"col-12 pl-0 pr-0 mb-1\" >\n" +
						"<input type=\"text\" class=\"form-control datetimepicker-input form-control-sm col-4\" name=\"taaDate\"\n" +
						"   id=\"taaDate" + rowCnt + "\"\n" +
						"   data-target=\"#taaDate" + rowCnt + "\"\n" +
						"   data-toggle=\"datetimepicker\"\n" +
						"   placeholder=\"연도-월-일\" autocomplete=\"off\" required style=\"display:inline-flex;\">\n" +
						"<span class=\"d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1 \" required style=\"display: inline-flex;\">~</span>\n" +
						"<input type=\"text\" class=\"form-control datetimepicker-input form-control-sm col-2\" name=\"taaSTime\"\n" +
						"   data-toggle=\"datetimepicker\"\n" +
						"   id=\"taaSTime" + rowCnt + "\"\n" +
						"   data-target=\"#taaSTime" + rowCnt + "\"\n" +
						"   placeholder=\"00:00\" autocomplete=\"off\" required required style=\"display: inline-flex;\">\n" +
						"<span class=\"d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1 \" required style=\"display: inline-flex;\">~</span>\n" +
						"<input type=\"text\" class=\"form-control datetimepicker-input form-control-sm col-2\" name=\"taaETime\"\n" +
						"   data-toggle=\"datetimepicker\"\n" +
						"   id=\"taaETime" + rowCnt + "\"\n" +
						"   data-target=\"#taaETime" + rowCnt + "\"\n" +
						"   placeholder=\"00:00\" autocomplete=\"off\" required required style=\"display: inline-flex;\">";

				if(rowCnt > 0){
					_rowHtml += "<button type=\"button\" class=\"btn btn-cancel delRowTaaBtn btn-flat btn-sm ml-1\" style='vertical-align: bottom;'>삭제</button>";
				}

				_rowHtml += "</div>";
				$('#taaRowGroup').append(_rowHtml);


				$('.delRowTaaBtn').on('click', function(){
					$(this).closest('div').remove();
				});

				$('input[name="taaDate"]').datetimepicker({
					format: 'YYYY-MM-DD',
					language: 'ko'
				});


				$('input[name="taaSTime"], input[name="taaETime"]').datetimepicker({
					format: 'HH:mm',
					use24hours: true,
					language: 'ko',
					widgetPositioning: {
						horizontal: 'left',
						vertical: 'bottom'
					}
				});
			},
			addAnnualRow : function(){	//	휴가신청기간 추가

				if($('input[name="annualCreateSDate"]').length >= 2){
					isuAlert("신청기간은 2개까지 추가가능합니다");
					return;
				}

				var _sVal = $('#annualInputSDate').val();
				var _eVal = $('#annualInputEDate').val();

				if(!_sVal || !_eVal){
					isuAlert("기간입력후 추가해주세요");
					return;
				}

				var _annualTaDetailCd = $('#annualTaDetailCd').val();
				var _requestTypeCd = "";

				if(_dList) {
					_dList.forEach(function (obj) {
						if (obj.code == _annualTaDetailCd) {
							switch (obj.requestTypeCd) {
								case "P":
								case "A":
								case "D":
									_requestTypeCd = obj.requestTypeCd;
									break;

							}
						}
					});
				}

				var s_inputNm = "annualCreateSDate";
				var e_inputNm = "annualCreateEDate";

				var _rowHtml =
						"<div class=\"col-12 pl-0 pr-0 mb-1\" >\n" +
						"<label style='display: block'>" + $('#annualTaDetailCd option:selected').text() + "</label>" +
						"<input type='hidden' name='requestTypeCd' value=\'" + _requestTypeCd + "\'>" +
						"<input type='hidden' name='annualTaaDetailTypeCd' value=\'" + _annualTaDetailCd + "\'>" +
						"<input type=\"text\" class=\"form-control annual-dt form-control-sm col-4\" " +
						"	name=\""+ s_inputNm + "\"\n" +
						"	value=\""+ _sVal + "\"\n" +
						"   placeholder=\"연도-월-일\" autocomplete=\"off\" readonly=readonly style=\"display:inline-flex;\">\n" +
						"<span class=\"d-sm-block d-md-block d-lg-inline-block annual-dt text-center pl-2 pr-2 mt-1 \" required style=\"display: inline-flex;\">~</span>\n" +
						"<input type=\"text\" class=\"form-control form-control-sm col-4\" " +
						"	name=\""+ e_inputNm + "\"\n" +
						"	value=\""+ _eVal + "\"\n" +
						"   placeholder=\"연도-월-일\" autocomplete=\"off\" readonly=readonly style=\"display:inline-flex;\">\n" +
						"<button type=\"button\" class=\"btn btn-cancel delRowAnnualBtn btn-flat btn-sm ml-1\" style='vertical-align: bottom;'>삭제</button>";


				_rowHtml += "</div>";
				$('#annDtList').prepend(_rowHtml);

				$('#annualInputSDate').val('');
				$('#annualInputEDate').val('');

				$('.delRowAnnualBtn').on('click', function(){
					$(this).closest('div').remove();
					getMyAnnualInfo();
				});

				getMyAnnualInfo();

			},
			resetAnnualCreateModal : function(){	//	휴가정보 초기화
				$('#annualDepList').hide();

				$('#annDtList').html('');
				$('#annualUsedNote').val('');

				//	사용일수
				$('#annualUsedCnt').val(0);
				//	총일수
				$('#annualTotalCnt').val(0);
				//	잔여일수
				$('#annualNotUsedCnt').val(_annualNotUsedCnt);


			},
			saveTaa : function(){	//	근태신청
				var param = $('#taaFrm').serialize();

				var _taaTypeCd = $('#taaTypeCd').val();

				if(_taaTypeCd == ''){
					isuAlert("근무구분을 선택해주세요");
					return;
				}

				var isTaaDate = true;
				var isTaaSTime = true;
				var isTaaETime = true;

				$('input[name="taaDate"]').each(function(idx, obj){
					if(obj.value == ''){
						isuAlert("신청일자를 입력해주세요");
						$(obj).focus();
						isTaaDate = false;
					}
				});

				$('input[name="taaSTime"]').each(function(idx, obj){
					if(obj.value == ''){
						isuAlert("시작시간을 입력해주세요");
						$(obj).focus();
						isTaaSTime = false;
					}
				});

				$('input[name="taaETime"]').each(function(idx, obj){
					if(obj.value == ''){
						isuAlert("종료시간을 입력해주세요");
						$(obj).focus();
						isTaaETime = false;
					}
				});

				if(!isTaaDate || !isTaaSTime || !isTaaETime){
					return;
				}

				var _taaDateArr = $('input[name="taaDate"]').vals();
				var _taaSTimeArr = $('input[name="taaSTime"]').vals();
				var _taaETimeArr = $('input[name="taaETime"]').vals();
				var _taaNote = $('#taaNote').val();

				//	요청
				var param = {
					taaTypeCd : _taaTypeCd,
					taaDate : _taaDateArr,
					taaSTime : _taaSTimeArr,
					taaETime : _taaETimeArr,
					note : _taaNote
				};

				Util.ajax({
					url: "${rc.getContextPath()}/wtmTaa/save",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						if(data!=null && data.status=='OK') {
							console.log(data.toString());

							$("#taaApplModal").modal("hide");

							groupwareOpen(data.groupwareUrl);

						}else{
							isuAlert(data.message);
						}
					},
					error: function(e) {
						isuAlert("출장/비상근무 신청 오류가 발생했습니다..");
					}
				});
			},
			saveAnnualCreate : function(){

				//	validation
				var _annualTaCd            = $('#annualTaCd').val();
				var _annualTaaDetailTypeCd = $('input[name="annualTaaDetailTypeCd"]').vals();
				var _sDate                 = $('input[name="annualCreateSDate"]').vals();
				var _eDate                 = $('input[name="annualCreateEDate"]').vals();
				var _requestTypeCd         = $('input[name="requestTypeCd"]').vals();
				var _usedNot               = $('#annualUsedNote').val();

				var _annualTotalCnt = $("#annualTotalCnt").val();
				var _annualUsedCnt = $("#annualUsedCnt").val();
				var _annualCreateCnt = $("#annualCreateCnt").val();
				var _annualNotUsedCnt = $("#annualNotUsedCnt").val();

				if(_sDate.length == 0){
					isuAlert('휴가신청 항목을 확인해주세요.');
					return;
				}

				// if(_dList) {
				// 	_dList.forEach(function (obj) {
				// 		if (obj.code == _annualTaDetailCd) {
				// 			_requestTypeCd = obj.requestTypeCd;
				// 		}
				// 	});
				// }

				//	요청
				var param = {
					annualTaaDetailTypeCd : _annualTaaDetailTypeCd,
					requestTypeCd : _requestTypeCd,
					annualTaCd : _annualTaCd,
					symd : _sDate,
					eymd : _eDate,
					note : _usedNot,
					annualTotalCnt : _annualTotalCnt,
					annualUsedCnt : _annualUsedCnt,
					annualCreateCnt : _annualCreateCnt,
					annualNotUsedCnt : _annualNotUsedCnt
				};

				Util.ajax({
					url: "${rc.getContextPath()}/wtmAnnualUsed/save",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						if(data!=null && data.status=='OK') {
							console.log(data.toString());

							$("#annualCreateApplModal").modal("hide");

							// isuAlert("휴가신청 완료.");
							groupwareOpen(data.groupwareUrl);

						}else{
							isuAlert(data.message);
						}
					},
					error: function(e) {
						isuAlert("휴가신청시 오류가 발생했습니다..");
					}
				});


			},
			renderCallback: function(){
				var calendar = this.$refs.fullCalendar.cal;
				calendarLeftVue.calendar = calendar;
			},
			datesRenderCallback: function(info){
				var $this = this;
				var calendar = this.$refs.fullCalendar.cal;

				if(info.view.type == 'timeGridDay') { //month change
					var ymd = moment(calendar.getDate()).format('YYYYMMDD');
					$this.workday = moment(calendar.getDate()).format('YYYY-MM-DD');

					//신청서 정보
					$this.getApplCode();
					/*
                    calendarLeftVue.otApplBtnYn = false;
                    if(moment('${today}').diff($this.workday)<=0)
  		  	    			calendarLeftVue.otApplBtnYn = true;

  		  		    	//근태사유서 신청 기간에 따라 버튼 보여줌
  		  		    	calendarLeftVue.inOutChgBtnYn = false;
  		  		    	<#if inoutChgLimit?? && inoutChgLimit!='' && inoutChgLimit?exists >
  		  		    		var inoutChgLimit = JSON.parse('${inoutChgLimit?js_string}');
  		  		    		var inoutChgDate = moment().subtract(inoutChgLimit, 'd');

  		  		    		if(moment(inoutChgDate).diff($this.workday)<=0 && moment($this.workday).diff('${today}')<=0)
  		  		    			calendarLeftVue.inOutChgBtnYn = true;
  		  		    	</#if> */

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
				console.log(JSON.stringify(info.event.extendedProps));
				//상세보기
				if(info.event.extendedProps.timeTypeCd=='OT' || info.event.extendedProps.timeTypeCd=='OT_CAN' || info.event.extendedProps.timeTypeCd=='NIGHT' ) {
					this.viewOvertimeApplDetail(info.event.extendedProps.timeTypeCd, info.event.extendedProps.applId, true);
				} else if(info.event.extendedProps.timeTypeCd=='SUBS') {
					this.viewChgSubsAppl(info);
				} else if(info.event.extendedProps.timeTypeCd=='SUBS_CHG') {
					this.viewChgSubsApplDetail(info.event.extendedProps.applId, info);
				}
			},
			dateClickCallback : function(info){
				//if(!info.allDay && moment('${today}').diff(this.workday)<=0)
				//	this.preCheck(info, false);

				if(!info.allDay) {
					var applCode = this.applCode['OT'];

					//연장근무 신청 기간에 따라 버튼 보여줌
					var isOtAppl = true;
					if(applCode.otApplSday!=null && applCode.otApplSday!=undefined && (applCode.otApplSday!=''||applCode.otApplSday==0)){
						var otApplSday = moment(moment().subtract(applCode.otApplSday, 'd')).format('YYYYMMDD');
						if(moment(this.workday).diff(otApplSday)<=0) {
							isOtAppl = false;
						}
					}

					if(applCode.otApplEday!=null && applCode.otApplEday!=undefined && (applCode.otApplEday!=''||applCode.otApplEday==0)){
						var otApplEday = moment(moment().add(applCode.otApplEday, 'd')).format('YYYYMMDD');
						if(moment(otApplEday).diff(this.workday)<=0) {
							isOtAppl = false;
						}
					}

					if(isOtAppl)
						this.preCheck(info, false);
				}

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
					url: "${rc.getContextPath()}/applCode/list",
					type: "POST",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$this.applCode = {};
						if(data!=null && data.status=='OK') {

							console.log('calendarLeftVue.inOutChgBtnYn >>> 2');
							data.DATA.map(function(d){
								$this.applCode[d.applCd] = d;

								if(d.applCd=='OT') {
									//연장근무 신청 기간에 따라 버튼 보여줌
									var isOtAppl = true;
									if(d.otApplSday!=null && d.otApplSday!=undefined && (d.otApplSday!=''||d.otApplSday==0)){
										var otApplSday = moment(moment().subtract(d.otApplSday, 'd')).format('YYYYMMDD');
										if(moment($this.workday).diff(otApplSday)<=0) {
											isOtAppl = false;
										}
									}
									if(d.otApplEday!=null && d.otApplEday!=undefined && (d.otApplEday!=''||d.otApplEday==0)){
										var otApplEday = moment(moment().add(d.otApplEday, 'd')).format('YYYYMMDD');
										if(moment(otApplEday).diff($this.workday)<0) {
											isOtAppl = false;
										}
									}

									calendarLeftVue.otApplBtnYn = isOtAppl;

									/* if($this.result.holidayYn=='Y') {
                                      //휴일연장 시간단위
                                        if(d.holApplTypeCd!=null && d.holApplTypeCd!=undefined && d.holApplTypeCd!=''){
                                              var holApplTypeCd = Number(d.holApplTypeCd);
                                            $('#sTime').datetimepicker('stepping', holApplTypeCd);
                                            $('#eTime').datetimepicker('stepping', holApplTypeCd);
                                          }
                                    } else {
                                    //연장 시간단위
                                        if(d.timeUnit!=null && d.timeUnit!=undefined && d.timeUnit!=''){
                                              var timeUnit = Number(d.timeUnit);
                                            $('#sTime').datetimepicker('stepping', timeUnit);
                                            $('#eTime').datetimepicker('stepping', timeUnit);
                                          }
                                    } */

									/*
                                    //시간 달력 선택 시 단위 세팅하면 1분 단위 신청 못해서 주석처리
                                    if($this.result.holidayYn!='Y') {
                                        if(d.timeUnit!=null && d.timeUnit!=undefined && d.timeUnit!=''){
                                              var timeUnit = Number(d.timeUnit);
                                            $('#sTime').datetimepicker('stepping', timeUnit);
                                            $('#eTime').datetimepicker('stepping', timeUnit);
                                          }
                                    } */
								} else if(d.applCd=='ENTRY_CHG') {
									//근태사유서 신청 기간에 따라 버튼 보여줌
									if(d.entryLimit!=null && d.entryLimit!=undefined && d.entryLimit!=''){
										var isInoutAppl = false;
										var inoutChgDate = moment().subtract(d.entryLimit, 'd');
										if(moment(inoutChgDate).diff($this.workday)<=0 && moment($this.workday).diff('${today}')<=0)
											isInoutAppl = true;

										calendarLeftVue.inOutChgBtnYn = isInoutAppl;
									}
								}
							});
						}
					},
					error: function(e) {
						$this.applCode = {};
					}
				});
			},
			getFlexibleDayInfo: function(ymd){ //근무일 정보
				console.log("getFlexibleDayInfo >>> call");
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
						modalCalendarVue.inOutChangeAppl = {};
						$this.inOutChangeAppl = {};
						if(data!=null) {
							calendarLeftVue.workTimeInfo = data;
							modalCalendarVue.inOutChangeAppl = data;

							//근태 사유서
							if(data.hasOwnProperty('planSdate') && data.planSdate!=null && data.planSdate!='')
								$this.inOutChangeAppl['planSdate']=data.planSdate;
							else
								$this.inOutChangeAppl['planSdate']='';

							if(data.hasOwnProperty('planEdate') && data.planEdate!=null && data.planEdate!='')
								$this.inOutChangeAppl['planEdate']=data.planEdate;
							else
								$this.inOutChangeAppl['planEdate']='';

							if(data.hasOwnProperty('entrySdate') && data.entrySdate!=null && data.entrySdate!='')
								$this.inOutChangeAppl['inoutSdate']=data.entrySdate;
							else
								$this.inOutChangeAppl['inoutSdate']='';

							if(data.hasOwnProperty('entryEdate') && data.entryEdate!=null && data.entryEdate!='')
								$this.inOutChangeAppl['inoutEdate']=data.entryEdate;
							else
								$this.inOutChangeAppl['inoutEdate']='';
						}
					},
					error: function(e) {
						calendarLeftVue.workTimeInfo = {};
						$this.inOutChangeAppl = {};
					}
				});
			},
			getDayResults: function(ymd){ //근무시간
				console.log(this.vueNm + 'getDayResults');

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
								//console.log(result);
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
								var workShm;
								var workEhm;

								if(data.planSdate!=null && data.planSdate!=undefined && data.planSdate!='')
									workShm = moment(data.planSdate).format('HH:mm');
								if(data.planEdate!=null && data.planEdate!=undefined && data.planEdate!='')
									workEhm = moment(data.planEdate).format('HH:mm');

								if(id.indexOf('subsSymd')!=-1 || id.indexOf('chgSubsYmd')!=-1) {
									//$("#"+id).closest(".form-row").children("div.guide.pl-1").children(":eq(0)").text("*해당일 근무시간은 " + workShm+ "~" + workEhm + " 입니다.");

									var key = id.split('_');
									var idx;
									if(key!=null && key!='undefined' && key.length>0)
										idx = key[1];

									if(idx!=null && idx!='' && idx!=undefined) {
										$this.subYmds[idx]['unplannedYn'] = data.unplannedYn;
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
			viewOvertimeAppl: function(date, btnYn){
				console.log('viewOvertimeAppl 2');
				var $this = this;

				//1시간 값 세팅
				var sYmd = new Date(date);
				var eYmd = new Date(date);
				var baseEdate = null;

				//기본근무 시간 이후로 자동으로 세팅
				if(btnYn && $this.result!=null && $this.result.dayResults!=null && $this.result.dayResults!=undefined && $this.result.dayResults!='') {
					var dayResults = JSON.parse($this.result.dayResults);

					dayResults.map(function(dayResult){
						if(dayResult.timeTypeCd == 'BASE' || dayResult.timeTypeCd == 'FIXOT' ||  dayResult.timeTypeCd == 'OT') {
							if(baseEdate==null || baseEdate < dayResult.eDate) {
								baseEdate = dayResult.eDate;
							}
						}
					});
				}

				if(baseEdate!=null) {
					sYmd = moment(baseEdate).format('YYYY-MM-DD HH:mm:ss');
					eYmd = moment(baseEdate).format('YYYY-MM-DD HH:mm:ss');
				}

				var applCode = $this.applCode['OT'];

				if(applCode!=null && applCode.timeUnit!=null && applCode.timeUnit!=undefined && applCode.timeUnit!='') {
					var timeUnit = Number(applCode.timeUnit);
					//eYmd.setMinutes(sYmd.getMinutes()+timeUnit);
					eYmd = moment(eYmd).add(timeUnit, 'minutes');
				} else {
					//eYmd.setHours(sYmd.getHours()+1);
					eYmd = moment(eYmd).add(1, 'hours');
				}

				$("#sDate").val(moment(sYmd).format('YYYY-MM-DD'));
				$("#eDate").val(moment(eYmd).format('YYYY-MM-DD'));
				$("#sTime").val(moment(sYmd).format('HH:mm'));
				$("#eTime").val(moment(eYmd).format('HH:mm'));

				$this.overtime = $this.calcMinute(moment($this.workday).format('YYYYMMDD'), moment(sYmd).format('HHmm'), moment(eYmd).format('HHmm'));
				$this.otTime = $this.overtime.calcMinute;	// 20200707 정열과장님 마스터 수정건 동기화
				//휴일근로신청의 경우 이전에 신청한 휴일 가져옴
				/* if(Object.keys($this.result).length>0 && $this.result.hasOwnProperty('holidayYn')
                        && $this.result.holidayYn!=null && $this.result.holidayYn=='Y') {
                    $this.getOtSubs(moment(sYmd).format('YYYYMMDD'));
                } */

				$this.getRestOtMinute();

				// this.applLine = calendarLeftVue.getApplLine('OT');

				$("#overtimeAppl").modal("show");

				console.info('subsYn : ' , this.subsYn);
				console.info('payTargetYn : ' , this.payTargetYn);
				console.info('isOtUse : ' , this.isOtUse);
				console.info('result.holidayYn : ' , this.result.holidayYn);

			},
			viewOvertimeApplDetail: function(timeTypeCd, applId, popYn){
				var $this = this;

				var url = "/otAppl";

				if(timeTypeCd=='OT_CAN')
					url = "/otCanAppl";

				var param = {
					applId: applId
				};

				Util.ajax({
					url: "${rc.getContextPath()}"+url,
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					async: false,
					success: function(data) {
						if(data!=null) {
							$this.overtimeAppl = data;

							if($this.overtimeAppl.hasOwnProperty('targetList')) {
								var emp = $this.overtimeAppl.targetList['${empNo}'];
								$("#overtimeApplDetail").find(".time.point").text(minuteToHHMM(emp.restOtMinute, 'detail'));
							}

							if(data.recoveryYn) {
								$("#recoveryBtn").show();
							} else {
								$("#recoveryBtn").hide();
							}

							if(popYn)
								$("#overtimeApplDetail").modal("show");
						}
					},
					error: function(e) {
						console.log(e);
						$this.overtimeAppl = {};
					}
				});

			},
			viewChgSubsAppl : function(info){
				var $this = this;

				var applId = info.event.extendedProps.applId;

				$this.viewOvertimeApplDetail('OT', applId, false);

				var otAppl = $this.overtimeAppl;

				$this.chgSubsAppl = {
					applId : applId,
					sDate : info.event.start,
					eDate : info.event.end
				};

				if(otAppl!=null && otAppl.hasOwnProperty("subs")) {
					otAppl.subs.map(function(s){
						if(moment(info.event.start).diff(s.subsSdate,'minutes')==0 && moment(info.event.end).diff(s.subsEdate,'minutes')==0) {
							$this.chgSubsAppl['calcMinute'] = s.subsMinute;
						}
					});
				}

				//변경 대체휴일
				if($this.subYmds.length==0) {
					var newSubYmd = {
						chgSubsYmd: '',
						chgSubsShm: '',
						chgSubsEhm: '',
					};

					$this.subYmds.push(newSubYmd);
				}

				// $this.applLine = calendarLeftVue.getApplLine('SUBS_CHG');

				$("#chgSubsModal").modal("show");
			},
			viewChgSubsApplDetail: function(applId, info){
				var $this = this;

				$this.chgSubsAppl = {
					applId : applId,
					sDate : info.event.start,
					eDate : info.event.end
				};

				var param = {
					applId: applId
				};

				Util.ajax({
					url: "${rc.getContextPath()}/otSubsChgAppl",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					async: false,
					success: function(data) {
						if(data!=null) {
							$this.overtimeAppl = data;

							if(data.recoveryYn) {
								$("#recoveryBtn").show();
							} else {
								$("#recoveryBtn").hide();
							}

							$("#chgSubsDetail").modal("show");
						}
					},
					error: function(e) {
						console.log(e);
						$this.overtimeAppl = {};
					}
				});

			},
			viewDayResults: function(ymd){

				console.log(this.vueNm + " viewDayResults");

				var $this = this;
				var classNames = [];

				if($this.result!=null && Object.keys($this.result).length>0) {
					//출퇴근 타각 표시
					if($this.result.hasOwnProperty('entry')) {
						var entry = $this.result.entry;

						if(entry.hasOwnProperty('entrySdate')) {
							//var sEdate = new Date(entry.entrySdate);
							//sEdate.setMinutes(sEdate.getMinutes()+3);
							var sEdate = moment(entry.entrySdate).add(3, 'minutes');

						}

						if(entry.hasOwnProperty('entryEdate')) {
							//var eEdate = new Date(entry.entryEdate);
							//eEdate.setMinutes(eEdate.getMinutes()+3);
							var eEdate = moment(entry.entryEdate).add(3, 'minutes');
						}

						classNames = [];
						classNames.push('ENTRY');


						/*var sEntry = {
							id: 'entrySdate',
							title: '출근 ' + moment(entry.entrySdate).format('YYYY-MM-DD HH:mm:ss'),
							start: entry.entrySdate,
							end: moment(sEdate).format('YYYY-MM-DD HH:mm:ss'),
							editable: false,
							classNames: classNames
						};
						$this.addEvent(sEntry);

						var eEntry = {
							id: 'entryEdate',
							title: '퇴근 ' + moment(entry.entryEdate).format('YYYY-MM-DD HH:mm:ss'),
							start: entry.entryEdate,
							end: moment(eEdate).format('YYYY-MM-DD HH:mm:ss'),
							editable: false,
							classNames: classNames
						};
						$this.addEvent(eEntry);*/
					}

					//근태 및 근무시간
					if($this.result.hasOwnProperty('dayResults') && $this.result.dayResults!=null && $this.result.dayResults!='') {
						var dayResults = JSON.parse($this.result.dayResults);
						//console.log(dayResults);
						dayResults.map(function(vMap){

							if(vMap.hasOwnProperty('taaCd') && vMap.taaCd!='' && vMap.taaCd != 'SUBS') {
								//근태
								classNames = [];
								classNames.push('TAA');

								if(vMap.taaCd.indexOf('BREAK')==-1) {
									var result;
									if((vMap.sDate==''||vMap.sDate==undefined) && (vMap.eDate==''||vMap.eDate==undefined)) {
										result = {
											id: 'TAA.'+vMap.taaCd,
											title: vMap.taaNm,
											start: moment(vMap.ymd).format('YYYY-MM-DD'),
											allDay: true,
											color: '#ffdbb2'
										};
									} else {
										result = {
											id: 'TAA.'+vMap.taaCd,
											title: vMap.taaNm,
											start: vMap.sDate,
											end: vMap.eDate,
											editable: false,
											classNames: classNames
										};
									}

									// $this.addEvent(result);
								}

							} else {
								//근무
								classNames = [];
								if(vMap.timeTypeCd == 'SUBS_CHG')
									classNames.push('SUBS');
								else
									classNames.push(vMap.timeTypeCd);

								var title = vMap.timeTypeNm;

								if(vMap.timeTypeCd!=null && vMap.timeTypeCd!=undefined && vMap.timeTypeCd=='OT_CAN')
									title += ' 취소';
								else if(vMap.timeTypeCd!=null && vMap.timeTypeCd!=undefined && vMap.timeTypeCd=='SUBS_CHG')
									title += ' 정정';

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

								// $this.addEvent(result);
							}
						});
					}
				}
			},
			preCheck : function(info, btnYn){ //소정근로 선 소진 여부, 연장근무 가능한지 체크
				var $this = this;

				console.log("otAppl/preCheck > 2");
				$("#loading").show();
				$this.workday = info.date;
				var param = {
					ymd: moment($this.workday).format('YYYYMMDD'),
					workTypeCd: 'OT'
				};

				//연장근무
				Util.ajax({
					url: "${rc.getContextPath()}/otAppl/preCheck",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null && data.status=='OK') {

							console.log('otAppl/preCheck >>> result :', data);
							$this.viewOvertimeAppl(info.date, btnYn);

							//대체 휴가 사용여부
							if(data.hasOwnProperty('subsYn'))
								$this.subsYn = data.subsYn;
							//수당지급대상자인지
							if(data.hasOwnProperty('payTargetYn'))
								$this.payTargetYn = data.payTargetYn;
						} else {
							$("#alertText").html(data.message);
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
							$("#alertModal").modal("show");
						}
					},
					error: function(e) {
						$("#loading").hide();
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

				var otReqMins = modalCalendarVue.overtime.calcMinute;
				var maxOtMins = modalCalendarVue.targets['${empNo}'].restOtMinute;

				console.log("otReqMins :" + otReqMins);
				console.log("maxOtMins :" + maxOtMins);

				if(otReqMins > maxOtMins){
					isuAlert("휴일근로시간은 잔여 연장근로시간을 초과할 수 없습니다.");
					return;
				}

				if(isValid) {
					var msg = '';

					$("#loading").show();

					var holidayYn = $this.result.holidayYn;

					//신청하려는 ot시간이 소정근무시간에 해당되지 않는지 체크
					var sDate = $("#sDate").val().replace(/-/gi,"");
					var eDate = $("#eDate").val().replace(/-/gi,"");
					var sTime = $("#sTime").val().replace(/:/gi,"");
					var eTime = $("#eTime").val().replace(/:/gi,"");

					var otSdate = moment(sDate+' '+sTime).format('YYYYMMDD HHmm');
					var otEdate = moment(eDate+' '+eTime).format('YYYYMMDD HHmm');

					var applCode = $this.applCode['OT'];
					//console.log(applCode);

					//신청 가능 시간 체크
					var inShm=null;
					var inEhm=null;
					if(holidayYn=='Y') {
						if(applCode.holInShm!=null && applCode.holInShm!=undefined && applCode.holInShm!=''
								&&applCode.holInEhm!=null && applCode.holInEhm!=undefined && applCode.holInEhm!='') {
							inShm = moment(sDate+' '+applCode.holInShm).format('YYYYMMDD HHmm');
							inEhm = moment(eDate+' '+applCode.holInEhm).format('YYYYMMDD HHmm');
						}
					} else {
						if(applCode.inShm!=null && applCode.inShm!=undefined && applCode.inShm!=''
								&&applCode.inEhm!=null && applCode.inEhm!=undefined && applCode.inEhm!='') {
							inShm = moment(sDate+' '+applCode.inShm).format('YYYYMMDD HHmm');
							inEhm = moment(eDate+' '+applCode.inEhm).format('YYYYMMDD HHmm');
						}
					}

					//시작 시간이 크면
					if(inShm!=null && inEhm!=null && moment(inShm).diff(inEhm)>0) {
						inEhm = moment(inEhm).add(1, 'days');
					}

					if(inShm!=null && inEhm!=null && (moment(otSdate).diff(inShm)<0 || moment(otEdate).diff(inEhm)>0)) {
						isValid = false;
						var shm =  moment(inShm).format('HH:mm');
						var ehm =  moment(inEhm).format('HH:mm');
						msg = '근무 가능 시간은 '+shm+'~'+ehm+' 입니다.';
						$("#sTime").val('');
						$("#eTime").val('');
					}

					//var time = Number(moment(otEdate).diff(otSdate,'minutes'));
					//var time = $this.overtime.calcMinute;
					var time = $this.otTime; // 20200429 jyp 수정
					if(time == null || time == undefined || time <= 0){
						msg="신청가능한 연장근무 시간이 없습니다.";
						isValid = false;
					}
					//var time = $this.otTime; // 20200429 jyp 수정
					// 신청 시간 단위
					if(applCode.timeUnit!=null && applCode.timeUnit!=undefined && applCode.timeUnit!='') {
						var timeUnit = Number(applCode.timeUnit);

						if($this.overtime.calcMinute % timeUnit != 0) {
							isValid = false;
							msg = '근무시간은 '+timeUnit+'분 단위로 신청 가능합니다.';
							$("#sTime").val('');
							$("#eTime").val('');
						}
					}

					if(moment(otEdate).diff(otSdate)==0) {
						isValid = false;
						msg = "근무시간을 지정하세요.";
						$("#sTime").val('');
						$("#eTime").val('');
					}
					if(moment(otEdate).diff(otSdate)<0) {
						isValid = false;
						msg = "종료일이 시작일보다 작습니다.";
						$("#sTime").val('');
						$("#eTime").val('');
					}


					//휴일근무 신청 시간 단위 체크
					//휴게시간 차감 후 근무시간을 휴일근무 신청시간으로 딱 나눠떨어져야함(분 단위)
					if(holidayYn=='Y' && $this.isOtUse ) {
						if(applCode.holApplTypeCd!=null && applCode.holApplTypeCd!=undefined && applCode.holApplTypeCd!='') {
							var holApplTypeCd = Number(applCode.holApplTypeCd);

							//휴게시간 차감
							/* if($this.overtime.breakMinute!=null && $this.overtime.breakMinute!=undefined && $this.overtime.breakMinute!='')
                                time = time - Number($this.overtime.breakMinute); */

							if($this.overtime.calcMinute % holApplTypeCd != 0) {
								isValid = false;
								msg = '휴일 근무시간은 '+minuteToHHMM(holApplTypeCd,'detail')+' 단위로 신청 가능합니다.';
								$("#sTime").val('');
								$("#eTime").val('');
							}

							//최대 신청 시간을 넘지 않는지 체크
							if(applCode.holMaxMinute!=null && applCode.holMaxMinute!=undefined && applCode.holMaxMinute!='') {
								var holMaxMinute = Number(applCode.holMaxMinute);

								if($this.overtime.calcMinute > holMaxMinute) {
									isValid = false;
									msg = '근무시간은 최대 '+minuteToHHMM(holMaxMinute,'detail')+' 까지 신청 가능합니다.';
									$("#sTime").val('');
									$("#eTime").val('');
								}
							}
						}
					}

					if(isValid) {
						if($this.result.hasOwnProperty('dayResults') && $this.result.dayResults!=null && $this.result.dayResults!='') {
							var dayResults = JSON.parse($this.result.dayResults);
							if(dayResults.length>0) {
								dayResults.map(function(dayResult){
									if((!dayResult.hasOwnProperty("applStatusCd") || (dayResult.hasOwnProperty("applStatusCd") && dayResult.applStatusCd!='22' && dayResult.applStatusCd!='32') )
											&& dayResult.timeTypeCd != 'GOBACK'){
										var workSdate = moment(dayResult.sDate).format('YYYY-MM-DD HH:mm');
										var workEdate = moment(dayResult.eDate).format('YYYY-MM-DD HH:mm');
										if(moment(workSdate).diff(otSdate)<=0 && moment(otSdate).diff(workEdate)<0
												|| moment(workSdate).diff(otEdate)<0 && moment(otEdate).diff(workEdate)<0 ) {
											isValid = false;
											msg = '이미 근무정보(신청중인 근무 포함)가 존재합니다.';
											$("#sTime").val('');
											$("#eTime").val('');
										}

									}
								});
							}

						}
					}

					if(isValid && $("input[name='subYn']:checked").val()=='Y') {
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
										if(sub.unplannedYn!='Y' && sub.hasOwnProperty("workShm") && sub.hasOwnProperty("workEhm")) {

											if(sub.workShm==undefined || sub.workEhm==undefined) {
												isValid = false;
												msg = sub.subsSymd+' 의 대체 근무 가능 시간이 없습니다.';

											} else {
												var workSdate = moment(sub.subsSymd+" "+sub.workShm).format('YYYY-MM-DD HH:mm');
												var workEdate = moment(sub.subsSymd+" "+sub.workEhm).format('YYYY-MM-DD HH:mm');
												var subSdate = moment(sub.subsSymd+" "+sub.subsShm).format('YYYY-MM-DD HH:mm');
												var subEdate = moment(sub.subsSymd+" "+sub.subsEhm).format('YYYY-MM-DD HH:mm');

												//시작 시간이 크면
												if(moment(workSdate).diff(workEdate)>0) {
													workEdate = moment(workEdate).add(1, 'days');
												}

												if(!(moment(workSdate).diff(subSdate)<=0 && moment(subSdate).diff(workEdate)<=0
														&& moment(workSdate).diff(subEdate)<=0 && moment(subEdate).diff(workEdate)<=0)) {
													isValid = false;
													msg = sub.subsSymd+' 의 대체 근무 가능 시간은 '+sub.workShm+'~'+sub.workEhm+' 입니다.';
												}
											}

										}
									}
								});

								//if(isValid && $this.overtime.calcMinute!=null && $this.overtime.calcMinute!='' && $this.overtime.calcMinute!=subsMin) {
								if(isValid && time !=null && time!='' && time!=subsMin) {
									isValid = false;
									msg = minuteToHHMM(time, 'detail')+'의 대체 휴일을 지정하세요.';
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
						$("#loading").hide();
						$("#alertText").html(msg);
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show");
					}
				}

			},
			validateChgSubsAppl : function(){
				var $this = this;
				var isValid = true;
				var forms = document.getElementById('chgSubsModal').getElementsByClassName('needs-validation');
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

					$("#loading").show();

					//신청하려는 휴일근로시간 = 대체일시 합산 시간
					if($this.subYmds!=null && $this.subYmds.length>0) {
						var subsMin = 0;

						$this.subYmds.map(function(sub){
							//대체일시 근무시간 합산
							if(sub.hasOwnProperty("subsMinute")) {
								var min = sub.subsMinute;
								subsMin += min;
							}

							//대체 시간이 근무시간에 포함되는지 확인
							if(sub.unplannedYn!='Y' && sub.hasOwnProperty("workShm") && sub.hasOwnProperty("workEhm")) {

								if(sub.workShm==undefined || sub.workEhm==undefined) {
									isValid = false;
									msg = sub.chgSubsYmd+' 의 대체 근무 가능 시간이 없습니다.';

								} else {
									var workSdate = moment(sub.chgSubsYmd+" "+sub.workShm).format('YYYY-MM-DD HH:mm');
									var workEdate = moment(sub.chgSubsYmd+" "+sub.workEhm).format('YYYY-MM-DD HH:mm');
									var subSdate = moment(sub.chgSubsYmd+" "+sub.chgSubsShm).format('YYYY-MM-DD HH:mm');
									var subEdate = moment(sub.chgSubsYmd+" "+sub.chgSubsEhm).format('YYYY-MM-DD HH:mm');

									//시작 시간이 크면
									if(moment(workSdate).diff(workEdate)>0) {
										workEdate = moment(workEdate).add(1, 'days');
									}

									if(!(moment(workSdate).diff(subSdate)<=0 && moment(subSdate).diff(workEdate)<=0
											&& moment(workSdate).diff(subEdate)<=0 && moment(subEdate).diff(workEdate)<=0)) {
										isValid = false;
										msg = sub.chgSubsYmd+' 의 대체 근무 가능 시간은 '+sub.workShm+'~'+sub.workEhm+' 입니다.';
									}
								}

							}
						});

						if(isValid && $this.chgSubsAppl.calcMinute!=null && $this.chgSubsAppl.calcMinute!='' && $this.chgSubsAppl.calcMinute!=subsMin) {
							isValid = false;
							msg = minuteToHHMM($this.chgSubsAppl.calcMinute, 'detail')+'의 대체 휴일을 지정하세요.';
						}

					} else {
						isValid = false;
						msg = '변경할 대체휴일을 입력하세요.';
					}

					if(isValid) {
						$this.subsChgAppl();
					} else {
						$("#loading").hide();
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
					//ymd: moment($this.workday).format('YYYYMMDD'),
					ymd: moment(calendarLeftVue.selectedDate).format('YYYYMMDD'),
					//ymd: moment($("#sDate").val()).format('YYYYMMDD'),
					otSdate : moment(otSdate).format('YYYYMMDDHHmm'),
					otEdate : moment(otEdate).format('YYYYMMDDHHmm'),
					reasonCd : $("#reasonCd").val(),
					reason: $("#otReason").val(),
					holidayYn: holidayYn,
					subYn: subYn
				};

				//휴일근로신청
				if(holidayYn=='Y') {
					param['subYn'] =  $('input[name="subYn"]:checked').val();

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
						$("#loading").hide();
						if(data!=null && data.status=='OK') {

							groupwareOpen(data.groupwareUrl);

							location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Time&date='+moment(calendarLeftVue.selectedDate).format('YYYYMMDD');

						} else {
							$("#alertText").html(data.message);
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
						}

						$("#alertModal").modal("show");
					},
					error: function(e) {
						$("#loading").hide();
						console.log(e);
						$("#alertText").html("연장근무 신청 시 오류가 발생했습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){});
						$("#alertModal").modal("show");
					}
				});
			},
			otCancelAppl: function(){ //연장근무취소신청

				var $this = this;

				$("#loading").show();

				var param = {
					workDayResultId: $this.overtimeAppl.workDayResultId,
					//applId: $this.overtimeAppl.otApplId,
					otApplId: $this.overtimeAppl.otApplId,
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
						$("#loading").hide();
						if(data!=null && data.status=='OK') {

							groupwareOpen(data.groupwareUrl);

							location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Time&date='+moment($this.workday).format('YYYYMMDD');
						} else {
							isuAlert(data.message);

						}

					},
					error: function(e) {
						$("#loading").hide();
						console.log(e);

						isuAlert("연장근무 취소 시 오류가 발생했습니다.");
					}
				});

			},
			recoveryAppl: function(){
				var $this = this;

				$("#loading").show();

				var param = {
					applCd : $this.overtimeAppl.applCd,
					applId : $this.overtimeAppl.applId
				};

				Util.ajax({
					url: "${rc.getContextPath()}/appl/delete",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null && data.status=='OK') {
							$("#alertText").html("회수되었습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								$("#confirmModal").modal("hide");

								if($this.overtimeAppl.applCd == 'SUBS_CHG')
									$("#chgSubsDetail").modal("hide");
								else
									$("#overtimeApplDetail").modal("hide");

								location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Time&date='+moment($this.workday).format('YYYYMMDD');
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
						$("#loading").hide();
						console.log(e);
						$("#alertText").html("회수 시 오류가 발생했습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show");
					}
				});

			},
			subsChgAppl: function(){
				var $this = this;

				var param = JSON.parse(JSON.stringify($this.overtimeAppl));
				param['workTypeCd'] = 'SUBS_CHG';

				var subs = [];

				//대체휴일 정정
				if(param!=null && param.hasOwnProperty("subs")) {
					param.subs.map(function(o){
						if(moment($this.chgSubsAppl.sDate).diff(o.subsSdate,'minutes')==0 && moment($this.chgSubsAppl.eDate).diff(o.subsEdate,'minutes')==0) {
							if($this.subYmds!=null && $this.subYmds.length>0) {
								$this.subYmds.map(function(s){
									var subsSdate = moment(s.chgSubsYmd+' '+s.chgSubsShm).format('YYYY-MM-DD HH:mm');
									var subsEdate = moment(s.chgSubsYmd+' '+s.chgSubsEhm).format('YYYY-MM-DD HH:mm');

									var sub = {
										subYmd: moment(s.chgSubsYmd).format('YYYYMMDD'),
										subsSdate: moment(subsSdate).format('YYYYMMDDHHmm'),
										subsEdate: moment(subsEdate).format('YYYYMMDDHHmm'),
										oldSubsApplId : o.otSubsApplId
									};
									subs.push(sub);
								});
							} else {
								subs.push(sub);
							}
						}
					});

				}

				param['subs'] = subs;

				Util.ajax({
					url: "${rc.getContextPath()}/otSubsChgAppl/request",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null && data.status=='OK') {
							$("#alertText").html("신청 되었습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								//location.reload();
								location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Time&date='+moment($this.workday).format('YYYYMMDD');
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
						$("#loading").hide();
						console.log(e);
						$("#alertText").html("대체휴일 정정요청 시 오류가 발생했습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){});
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

				//console.log(val);
			},
			calcSubsTime: function(id) { //휴일 대체 근로 시간 계산
				var $this = this;
				var key = id.split('_');
				var idx;
				if(key!=null && key!='undefined' && key.length>0)
					idx = key[1];

				if(idx!=null && idx!='' && idx!=undefined) {
					var subsInfo = $this.subYmds[idx];
					//console.log(subsInfo);

					if(subsInfo.subsSymd!=null && subsInfo.subsSymd!=undefined && subsInfo.subsSymd!=''
							&& subsInfo.subsShm!=null && subsInfo.subsShm!=undefined && subsInfo.subsShm!=''
							&& subsInfo.subsEhm!=null && subsInfo.subsEhm!=undefined && subsInfo.subsEhm!='') {

						var sDate = moment(subsInfo.subsSymd+" "+subsInfo.subsShm).format('YYYYMMDD HHmm');
						var eDate = moment(subsInfo.subsSymd+" "+subsInfo.subsEhm).format('YYYYMMDD HHmm');

						var isValid = true;
						$this.subYmds.map(function(s, i){
							var sd = moment(s.subsSymd+" "+s.subsShm).format('YYYYMMDD HHmm');
							var ed = moment(s.subsSymd+" "+s.subsEhm).format('YYYYMMDD HHmm');
							if(idx!=i && (moment(sd).diff(sDate)<=0 && moment(sDate).diff(ed)<=0 || moment(sd).diff(eDate)<=0 && moment(eDate).diff(ed)<=0)) {
								isValid = false;
							}

						})

						if(!isValid) {
							$("#alertText").html("이미 근무정보(신청중인 근무 포함)가 존재합니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								subsInfo.subsShm = '';
								subsInfo.subsEhm = '';
							});
							$("#alertModal").modal("show");
						} else if(sDate > eDate) {
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
			calcChgSubsTime: function(id) { //휴일 대체 근로 시간 계산
				var $this = this;
				var key = id.split('_');
				var idx;
				if(key!=null && key!='undefined' && key.length>0)
					idx = key[1];

				if(idx!=null && idx!='' && idx!=undefined) {
					var subsInfo = $this.subYmds[idx];
					//console.log(subsInfo);

					if(subsInfo.chgSubsYmd!=null && subsInfo.chgSubsYmd!=undefined && subsInfo.chgSubsYmd!=''
							&& subsInfo.chgSubsShm!=null && subsInfo.chgSubsShm!=undefined && subsInfo.chgSubsShm!=''
							&& subsInfo.chgSubsEhm!=null && subsInfo.chgSubsEhm!=undefined && subsInfo.chgSubsEhm!='') {

						var sDate = moment(subsInfo.chgSubsYmd+" "+subsInfo.chgSubsShm).format('YYYYMMDD HHmm');
						var eDate = moment(subsInfo.chgSubsYmd+" "+subsInfo.chgSubsEhm).format('YYYYMMDD HHmm');

						var isValid = true;
						$this.subYmds.map(function(s, i){
							var sd = moment(s.chgSubsYmd+" "+s.chgSubsShm).format('YYYYMMDD HHmm');
							var ed = moment(s.chgSubsYmd+" "+s.chgSubsEhm).format('YYYYMMDD HHmm');
							if(idx!=i && (moment(sd).diff(sDate)<=0 && moment(sDate).diff(ed)<=0 || moment(sd).diff(eDate)<=0 && moment(eDate).diff(ed)<=0)) {
								isValid = false;
							}

						})

						if(!isValid) {
							$("#alertText").html("이미 근무정보(신청중인 근무 포함)가 존재합니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								subsInfo.chgSubsShm = '';
								subsInfo.chgSubsEhm = '';
							});
							$("#alertModal").modal("show");
						} else if(sDate > eDate) {
							$("#alertText").html("종료시간이 시작시간보다 작습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								subsInfo.chgSubsShm = '';
								subsInfo.chgSubsEhm = '';
							});
							$("#alertModal").modal("show");
						} else {
							var shm = subsInfo.chgSubsShm.replace(/:/gi,"");
							var ehm = subsInfo.chgSubsEhm.replace(/:/gi,"");

							var result = $this.calcMinute(moment(subsInfo.chgSubsYmd).format('YYYYMMDD'), shm, ehm);
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
			},
			inOutChange: function() {
				var $this = this;
				var applYn = true;
				var msg = '';
				var forms = document.getElementById('inOutChangeModal').getElementsByClassName('needs-validation');
				var validation = Array.prototype.filter.call(forms, function(form) {
					if (form.checkValidity() === false) {
						applYn = false;
						msg = "사유를 입력해 주세요.";
						event.preventDefault();
						event.stopPropagation();
					}
					form.classList.add('was-validated');
				});

				if( moment('${today}').diff($this.inOutChangeAppl.planEdate)<=0 && $("#chgEdate").val()!='' && $("#chgEdate").val()!='00:00') {
					applYn = false;
					msg = '퇴근 시간을 변경할 수 없습니다.';
				}

				if( moment('${today}').diff($this.inOutChangeAppl.planEdate)>0) {
					if( ($this.inOutChangeAppl.inoutSdate=='' || $this.inOutChangeAppl.inoutSdate=='00:00') && ($("#chgSdate").val()=='' || $("#chgSdate").val()=='00:00') ) {
						applYn = false;
						msg = '변경 할 출근 시간을 입력해 주세요.';
					} else if( ($this.inOutChangeAppl.inoutEdate=='' || $this.inOutChangeAppl.inoutEdate=='00:00') && ($("#chgEdate").val()=='' || $("#chgEdate").val()=='00:00') ) {
						applYn = false;
						msg = '변경 할 퇴근 시간을 입력해 주세요.';
					}
				}

				if( ($("#chgSdate").val()=='' && $("#chgEdate").val()=='') || ($("#chgSdate").val()=='00:00' && $("#chgEdate").val()=='00:00')) {
					applYn = false;
					msg = '변경 할 출근/퇴근 시간을 입력해 주세요.';
				}

				if(applYn) {
					var inOutChgAppl = $this.inOutChangeAppl;
					var param = {};
					var ymd = moment(calendarLeftVue.selectedDate).format('YYYYMMDD');
					param['workTypeCd'] = 'ENTRY_CHG';
					param['ymd'] = ymd;

					if(inOutChgAppl.planSdate!=null && inOutChgAppl.planSdate!=undefined && inOutChgAppl.planSdate!='')
						param['planSdate'] = moment(inOutChgAppl.planSdate).format('YYYYMMDDHHmm');
					if(inOutChgAppl.planEdate!=null && inOutChgAppl.planEdate!=undefined && inOutChgAppl.planEdate!='')
						param['planEdate'] = moment(inOutChgAppl.planEdate).format('YYYYMMDDHHmm');
					if(inOutChgAppl.inoutSdate!=null && inOutChgAppl.inoutSdate!=undefined && inOutChgAppl.inoutSdate!='')
						param['entrySdate'] = moment(inOutChgAppl.inoutSdate).format('YYYYMMDDHHmm');
					if(inOutChgAppl.inoutEdate!=null && inOutChgAppl.inoutEdate!=undefined && inOutChgAppl.inoutEdate!='')
						param['entryEdate'] = moment(inOutChgAppl.inoutEdate).format('YYYYMMDDHHmm');

					if($("#chgSdate").val()!=undefined && $("#chgSdate").val()!='')
						param['chgSdate'] = ymd+($("#chgSdate").val().replace(":","")); // moment(moment(ymd+($("#chgSdate").val().replace(":","")) ).format('YYYYMMDDHHmm')).format('YYYYMMDDHHmm');
					if($("#chgEdate").val()!=undefined && $("#chgEdate").val()!='')
						param['chgEdate'] = ymd+($("#chgEdate").val().replace(":","")); // moment(moment(ymd+($("#chgEdate").val().replace(":","")) ).format('YYYYMMDDHHmm')).format('YYYYMMDDHHmm');

					param['reason'] = $("#chgReason").val();

					Util.ajax({
						url: "${rc.getContextPath()}/inOutChangeAppl/request",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							if(data!=null && data.status=='OK') {
								/*$("#alertText").html("신청 되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$("#inOutChangeModal").modal("hide");
								});*/
								$("#inOutChangeModal").modal("hide");

								groupwareOpen(data.groupwareUrl);
							} else {

								isuAlert(data.message);
							}

						},
						error: function(e) {
							console.log(e);
							isuAlert("저장 시 오류가 발생했습니다.");
						}
					});
				} else {
					$("#alertText").html(msg);
					$("#alertModal").on('hidden.bs.modal',function(){
						$("#alertModal").off('hidden.bs.modal');
						$("#inOutChangeModal").find(".needs-validation").removeClass('was-validated');
					});
					$("#alertModal").modal("show");
				}

			},
			getRestOtMinute : function() { //잔여 연장근로시간 조회
				var $this = this;
				$(".loading-spinner").show();

				var param = {
					ymd: moment(this.workday).format('YYYYMMDD')
				};

				Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/otMinute",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					async: false,
					success: function(data) {
						$(".loading-spinner").hide();

						if(data!=null) {
							// console.log(data.targetList);
							//잔여연장근로시간
							if(data.hasOwnProperty('targetList'))
								$this.targets = data.targetList;
						}
					},
					error: function(e) {
						$(".loading-spinner").hide();
						$this.targets = {};
					}
				});
			}
		}
	});

   	$('#flexitimeModal').on('hidden.bs.modal',function(){
   		$(".list-group-item").removeClass("active");
   		monthCalendarVue.prevEdate = '';
   		$("#applyFlexBtn").hide();
   		$("#rewriteBtn").hide();
   		
   		if(calendarTopVue.imsiFlexitimeList!=null && calendarTopVue.imsiFlexitimeList.length>0)
   			calendarTopVue.flexStatus='02';
   		else
   			calendarTopVue.flexStatus='01';
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
</div>