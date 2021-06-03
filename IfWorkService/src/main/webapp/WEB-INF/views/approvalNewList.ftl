<#include "/applLineComponent.ftl">
<div id="approval" v-cloak class="bg-light-blue">
	<!-- 유연근무제신청 상세보기 modal start -->
    <div class="modal fade show" id="flexibleAppl" tabindex="-1" role="dialog" v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">유연근무제신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                    	<div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="title">유연근무제 신청일자</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.sYmd">
                                        	{{moment(appl.sYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.eYmd">
                                        	{{moment(appl.eYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">근무기간</div>
                                <div class="desc">
                                	<template v-if="appl.workDay">
                                	{{appl.workDay}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                	<template v-if="appl.reason">
                                	{{appl.reason}}
                                	</template>
                                </div>
                            </div>
                            <div class="accordion-wrap inner-wrap">
                                <ul id="accordion" class="accordion" v-if="appl.hasOwnProperty('elasDetails')">
                                    <li v-for="e in appl.elasDetails">
                                        <div class="link" @click="accordionDropdown($event.target)">{{moment(e.startYmd).format('YYYY-MM-DD')}} ~ {{moment(e.endYmd).format('YYYY-MM-DD')}}<i class="ico arrow-down"></i></div>
                                        <div class="submenu">
                                            <ul class="all-time-wrap">
                                                <li>
                                                    <span class="title">근무시간</span>
                                                    <span class="time bold">{{minuteToHHMM(e.workMinute, 'detail')}}</span>
                                                </li>
                                                <li>
                                                    <div class="total">
                                                        <span class="title">연장합산</span>
                                                        <span class="time bold">{{minuteToHHMM(e.otMinute, 'detail')}}</span>
                                                    </div>
                                                    <ul class="time-list">
                                                        <li>
                                                            <span class="title">조출시간</span>
                                                            <span class="time">{{minuteToHHMM(e.otbMinute, 'detail')}}</span>
                                                        </li>
                                                        <li>
                                                            <span class="title">잔업시간</span>
                                                            <span class="time">{{minuteToHHMM(e.otaMinute, 'detail')}}</span>
                                                        </li>
                                                        <li>
                                                            <span class="title">휴일시간</span>
                                                            <span class="time">{{minuteToHHMM(e.holidayMinute, 'detail')}}</span>
                                                        </li>
                                                    </ul>
                                                </li>
                                            </ul>
                                            <template v-if="e.hasOwnProperty('details')">
                                            <p class="title time-desc-title">출,퇴근시간</p>
                                            <ul class="time-desc-wrap">
                                                <li v-for="d in e.details">
                                                    <div class="date">{{moment(d.ymd).format('YYYY-MM-DD')}}({{d.weekday}}) 
                                                    	<template v-if="d.planSdate && d.planEdate">{{moment(d.ymd+' '+d.planSdate).format('HH:mm')}}~{{moment(d.ymd+' '+d.planEdate).format('HH:mm')}}</template>
                                                    </div>
                                                    <ul class="time-desc">
                                                        <li><span class="title">근무시간</span><span class="time">{{minuteToHHMM(d.workMinute, 'detail')}}</span></li>
                                                        <li><span class="title">조출시간</span><span class="time">{{minuteToHHMM(d.otbMinute, 'detail')}}</span></li>
                                                        <li><span class="title">잔업시간</span><span class="time">{{minuteToHHMM(d.otaMinute, 'detail')}}</span></li>
                                                        <li><span class="title">휴일시간</span><span class="time">{{minuteToHHMM(d.holidayMinute, 'detail')}}</span></li>
                                                    </ul>
                                                </li>
                                            </ul>
                                            </template>
                                        </div>
                                    </li>
                        		</ul>
                        	</div>
                        	<appl-line :bind-data="appl.applLine"></appl-line>
                        </div>
                        <!--  
                        <div class="btn-wrap text-center">
                        </div>
                        -->
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 유연근무제신청 상세보기 modal end -->
	<!-- 연장근무신청 상세보기 modal start -->
    <div class="modal fade show" id="otAppl" tabindex="-1" role="dialog"  v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <template v-if="appl.applCd=='OT_CAN'">
                		<h5 class="modal-title">연장근로 취소신청</h5>
                	</template>
                	<template v-else="">
                		<h5 class="modal-title" v-if="appl.holidayYn!='Y'">연장근로신청</h5>
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
                            	<template v-if="appl.targetList && appl.targetList!=null && (Object.keys(appl.targetList).length>1 || (Object.keys(appl.targetList).length==1 && !appl.targetList.hasOwnProperty(appl.applSabun)))">
				                	<p class="page-sub-title mb-1" v-if="appl.holidayYn!='Y'">연장근로 대상자</p>
				                	<p class="page-sub-title mb-1" v-else>휴일근로 대상자</p>
						   			<div class="select-list-wrap position-relative">
						   				<div class="loading-spinner" style="display:none;"></div>
						   				<span class="targetor" v-for="(v, k) in appl.targetList">
						   					<span class="name">{{v.empNm}}</span>
						   					<span class="time">잔여 {{minuteToHHMM(v.restOtMinute, 'short')}}</span>
						                </span>
						            </div>
						            <hr class="separate-bar">
					            </template>
					            <template  v-if="appl.targetList && appl.targetList!=null && (Object.keys(appl.targetList).length==1 && appl.targetList.hasOwnProperty(appl.applSabun))">
				                	<p class="page-sub-title mb-0">잔여 연장근로시간</p>
				                    <span class="time-wrap"  v-for="(v, k) in appl.targetList">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(v.restOtMinute, 'detail')}}</span>
				                    </span>
				                    <hr class="separate-bar">
					            </template>
                                <div class="title" v-if="appl.holidayYn!='Y'">연장근로시간</div>
                                <div class="title" v-else>휴일근로시간</div>
                                <div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="appl.otMinute">
                                        		{{minuteToHHMM(appl.otMinute, 'detail')}}
                                        	</template>
                                        </span>
                                    </span>
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="start-time">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="end-time">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유구분</div>
                                <div class="desc">
                                	<template v-if="appl.reasonNm">
                                	{{appl.reasonNm}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">설명</div>
                                <div class="desc">
                                	<template v-if="appl.reason">
                                	{{appl.reason}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="appl.holidayYn=='Y'">
                                <div class="title">휴일대체방법</div>
                                <div class="desc">
                                	<template v-if="appl.subYn">
                                	{{appl.subYn=='Y'?'휴일대체':'수당지급'}}
                                	</template>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="appl.holidayYn=='Y' && appl.subYn=='Y'">
                                <div class="title">대체일시</div>
                                <template v-if="appl.subs" v-for="sub in appl.subs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc" v-if="(sub.workSDate && sub.workEDate) || appl.oldSubs">
                                <template v-if="sub.workSDate && sub.workEDate">
                                *해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.
                                </template>
                                <template v-if="appl.oldSubs">
	                                <template v-for="oldSub in appl.oldSubs" v-if="oldSub.otSubsApplId == sub.oldSubsApplId">
	                                <br>*이전 대체휴일은  {{moment(oldSub.subsSdate).format('YYYY-MM-DD HH:mm')}}~{{moment(oldSub.subsEdate).format('YYYY-MM-DD HH:mm')}} 입니다.
	                                </template>
                                </template>
                                </div>
                                </template>
                            </div>
                            <div class="inner-wrap" v-if="appl.applCd=='OT_CAN' && appl.cancelReason">
                                <div class="title">취소사유</div>
                                <div class="desc">
                                	{{appl.cancelReason}}
                                </div>
                            </div>
                            <appl-line :bind-data="appl.applLine"></appl-line>
                            <hr class="bar">
                        </div>
                        <div class="btn-wrap text-center" v-if="applType=='01' && appl.applSabun == appl.sabun">
                        	<!-- <template v-if="(appl.cancelYn==null||appl.cancelYn==undefined||appl.cancelYn!='Y') && appl.applStatusCd=='99'">
                            	<button type="button" class="btn btn-default rounded-0" v-if="appl.holidayYn!='Y'" data-toggle="modal" data-target="#cancelOpinionModal">연장근로신청 취소하기</button>
                            	<button type="button" class="btn btn-default rounded-0" v-else data-toggle="modal" data-target="#cancelOpinionModal">휴일근로신청 취소하기</button>
                        	</template>
                        	<template v-else> -->
                            	<button type="button" class="btn btn-default rounded-0" v-if="appl.recoveryYn" data-toggle="modal" data-target="#confirmModal">회수하기</button>
                        	<!-- </template> -->
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 연장근무신청 상세보기 modal end -->
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
    <!-- 회수하기 modal start -->
    <div class="modal fade show" id="confirmModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <!-- <h5 class="modal-title">모달제목이 들어갑니다.</h5> -->
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                        	회수 시 신청서 데이터가 모두 삭제됩니다.<br>회수하시겠습니까?
                        </div>
                        <div class="btn-wrap text-center">
                            <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-default rounded-0" @click="recoveryAppl">확인</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 회수하기 modal end -->
    <!-- 근태사유서신청 상세보기 modal start -->
    <div class="modal fade show" id="inOutChangeAppl" tabindex="-1" role="dialog" v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">근태사유서신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                    	<div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="title">계획 근무시간</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.planSdate">
                                        	{{moment(moment(appl.planSdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.planEdate">
                                        	{{moment(moment(appl.planEdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">출/퇴근 시각</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.entrySdate">
                                        	{{moment(moment(appl.entrySdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.entryEdate">
                                        	{{moment(moment(appl.entryEdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">변경 출/퇴근 시각</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.chgSdate">
                                        	{{moment(moment(appl.chgSdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.chgEdate">
                                        	{{moment(moment(appl.chgEdate, 'YYYYMMDDHHmmss').toDate()).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                	<template v-if="appl.reason">
                                	{{appl.reason}}
                                	</template>
                                </div>
                            </div>
                            <appl-line :bind-data="appl.applLine"></appl-line>
                        </div>
                        <!--  
                        <div class="btn-wrap text-center">
                        </div>
                        -->
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 근태사유서신청 상세보기 modal end -->
    <!-- 대체휴일 정정신청 상세보기 modal start -->
    <div class="modal fade show" id="chgSubsModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false" v-if="appl">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">대체휴일 정정요청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
	                        <div class="inner-wrap">
                                <div class="title">휴일근로시간</div>
                                <div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="appl.otMinute">
                                        		{{minuteToHHMM(appl.otMinute, 'detail')}}
                                        	</template>
                                        </span>
                                    </span>
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="start-time">
                                        	<template v-if="appl.otSdate">
                                        	{{moment(appl.otSdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="end-time">
                                        	<template v-if="appl.otEdate">
                                        	{{moment(appl.otEdate).format('HH:mm')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">대체휴일</div>
                                <template v-if="appl.oldSubs" v-for="sub in appl.oldSubs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc" v-if="sub.workSDate&&sub.workEDate">*해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.</div>
                                </template>
                            </div>
                            <hr class="separate-bar">
                            <div class="inner-wrap">
                                <div class="big-title">{{moment(chgSubsTarget.sDate).format('YYYY-MM-DD HH:mm')}} ~ {{moment(chgSubsTarget.eDate).format('YYYY-MM-DD HH:mm')}} 의 변경 대체휴일</div>
                                <template v-if="appl.subs" v-for="sub in appl.subs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc" v-if="sub.workSDate&&sub.workEDate">*해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.</div>
                                </template>
                            </div>
                            
                            <appl-line :bind-data="appl.applLine"></appl-line>
                        </div>
                        <div class="btn-wrap text-center" v-if="applType=='01' && appl.applSabun == appl.sabun">
                            <button type="button" class="btn btn-default rounded-0" v-if="appl.recoveryYn" data-toggle="modal" data-target="#confirmModal">회수하기</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 대체휴일 정정신청 상세보기 modal end -->
	<!-- 결재의견 modal start -->
	<div class="modal fade" id="apprOpinionModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" v-if="apprType=='apply'">결재 의견</h5>
                    <h5 class="modal-title" v-else>반려 의견</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group col-12">
                        <textarea class="form-control" id="apprOpinion" rows="3" :placeholder="apprType=='apply'?'결재 의견 작성':'반려 의견 작성'" v-model="apprOpinion"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" id="apprBtn" class="btn btn-default" v-if="apprType=='apply'" @click="apply">결재하기</button>
                    <button type="button" id="apprBtn" class="btn btn-default" v-else  @click="apply">반려하기</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 결재의견 modal end -->
    
    <!-- 보상휴가신청 상세보기 modal start -->
    <div class="modal fade show" id="compAppl" tabindex="-1" role="dialog"  v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <template v-if="appl.applCd=='COMP_CAN'">
                		<h5 class="modal-title">보상휴가 취소신청</h5>
                	</template>
                	<template v-else="">
                		<h5 class="modal-title">보상휴가신청</h5>
                	</template>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
					            <template v-if="appl.applCd=='COMP'">
				                	<p class="page-sub-title mb-0">잔여 보상휴가시간</p>
				                    <span class="time-wrap">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(appl.restMinute, 'detail')}}</span>
				                    </span>
				                    <hr class="separate-bar">
					            </template>
					            <div class="inner-wrap">
	                                <div class="title">근태</div>
	                                <div class="desc">
	                                	<template v-if="appl.taaNm">
	                                	{{appl.taaNm}}
	                                	</template>
	                                </div>
	                            </div>
	                            <div class="inner-wrap">
	                                <div class="title">보상휴가시간</div>
	                                <div class="desc">
	                                    <span class="time-wrap">
	                                        <i class="fas fa-clock"></i>
	                                        <span class="time">
	                                        	<template v-if="appl.compMinute">
	                                        		{{minuteToHHMM(appl.compMinute, 'detail')}}
	                                        	</template>
	                                        </span>
	                                    </span>
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="appl.symd">
	                                        	{{moment(appl.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="appl.eymd">
	                                        	{{moment(appl.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
	                                </div>
	                            </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                	<template v-if="appl.reason">
                                	{{appl.reason}}
                                	</template>
                                </div>
                            </div>
                            <appl-line :bind-data="appl.applLine"></appl-line>
                            <hr class="bar">
                        </div>
<!--                         <div class="btn-wrap text-center" v-if="applType=='01' && appl.applSabun == appl.sabun"> -->
<!--                            	<button type="button" class="btn btn-default rounded-0" v-if="appl.recoveryYn" data-toggle="modal" data-target="#confirmModal">회수하기</button> -->
<!--                         </div> -->
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 보상휴가신청 상세보기 modal end -->

    <!-- 휴가신청 상세보기 modal start -->
    <div class="modal fade show" id="taaAppl" tabindex="-1" role="dialog"  v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">휴가신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="inner-wrap">
                                    <div class="title">휴가구분</div>
                                    <div class="desc">
                                        <template v-if="appl.taaNm">
                                            {{appl.taaNm}}
                                        </template>
                                    </div>
                                </div>
                                <div class="inner-wrap">
                                    <div class="title">휴가기간</div>
                                    <template v-if = "taaApplDetList.length > 0">
                                    <div class="desc" v-for = "det in taaApplDetList">
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="det.symd">
	                                        	{{moment(det.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="det.eymd">
	                                        	{{moment(det.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
                                    </div>
                                    </template>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                    <template v-if="taaApplDetList[0]">
                                        {{taaApplDetList[0].note}}
                                    </template>
                                </div>
                            </div>
                            <appl-line :bind-data="appl.applLine"></appl-line>
                            <hr class="bar">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 휴가신청 상세보기 modal end -->

    <!-- 휴가취소신청 상세보기 modal start -->
    <div class="modal fade show" id="taaCanAppl" tabindex="-1" role="dialog"  v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">휴가취소신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="inner-wrap">
                                    <div class="title">휴가구분</div>
                                    <div class="desc">
                                        <template v-if="appl.taaNm">
                                            {{appl.taaNm}}
                                        </template>
                                    </div>
                                </div>
                                <div class="inner-wrap">
                                    <div class="title">휴가기간</div>
                                    <template v-if = "taaApplDetList.length > 0">
                                    <div class="desc" v-for = "det in taaApplDetList">
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="det.symd">
	                                        	{{moment(det.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="det.eymd">
	                                        	{{moment(det.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
                                    </div>
                                    </template>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                    <template v-if="wtmTaaCanAppl.note">
                                        {{wtmTaaCanAppl.note}}
                                    </template>
                                </div>
                            </div>
                            <appl-line :bind-data="appl.applLine"></appl-line>
                            <hr class="bar">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 휴가취소신청 상세보기 modal end -->

    <!-- 출장신청 상세보기 modal start -->
    <div class="modal fade show" id="regaAppl" tabindex="-1" role="dialog"  v-if="appl">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">출장신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="inner-wrap">
                                    <div class="title">간주시간 구분</div>
                                    <div class="desc">
                                        <template v-if="appl.taaNm">
                                            {{appl.taaNm}}
                                        </template>
                                    </div>
                                </div>
                                <div class="inner-wrap">
                                    <div class="title">기간</div>
                                    <template v-if = "taaApplDetList.length > 0">
                                        <div class="desc" v-for = "det in taaApplDetList">
	                                    <span class="date-wrap">
	                                        <span class="start-date">
	                                        	<template v-if="det.symd">
	                                        	{{moment(det.symd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                        <span class="ml-1 mr-1">~</span>
	                                        <span class="end-date">
	                                        	<template v-if="det.eymd">
	                                        	{{moment(det.eymd).format('YYYY-MM-DD')}}
	                                        	</template>
	                                        </span>
	                                    </span>
                                        </div>
                                    </template>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                    <template v-if="taaApplDetList[0]">
                                        {{taaApplDetList[0].note}}
                                    </template>
                                </div>
                            </div>
                            <appl-line :bind-data="appl.applLine"></appl-line>
                            <hr class="bar">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 출장신청 상세보기 modal end -->

	<div class="container-fluid mgr-wrap mgr-wrap-height">
        <form id="sheetForm" name="sheetForm">
            <div class="sheet_search outer">
                <div>
                    <table>
                        <input type="hidden" id="applType" name="applType" value="01" />
                        <tr>
                            <td>
                                <span class="magnifier"><i class="fas fa-search"></i></span>
                                <span class="search-title">Search</span>
                            </td>

                            <td>
                                <span class="label">상신일 </span>
                                <input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
                                ~
                                <input type="text" id="eYmd" name="eYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
                            </td>
                            <td>
                                <span class="label">사번/성명 </span>
                                <input type="text" id="searchKeyword" name="searchKeyword" />
                            </td>
                            <td>
                                <a href="#" @click="getApprovalList()" class="button">조회</a>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
		<p class="page-title">결재 알림</p>
		<ul class="nav approval-wrap nav-pills" role="tablist">
            <li class="nav-item">
                <a href="#" class="nav-link" :class="{active: applType=='01'?true:false}" id="appl_type_request" data-toggle="pill" @click="getApprovalList('01')" role="tab"
                    aria-controls="pills-home" :aria-selected="applType=='01'?true:false">신청서상태</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link" :class="{active: applType=='02'?true:false}" id="appl_type_pending" data-toggle="pill" @click="getApprovalList('02')" role="tab"
                    aria-controls="pills-profile" :aria-selected="applType=='02'?true:false">미결함</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link" :class="{active: applType=='03'?true:false}" id="appl_type_complete" data-toggle="pill" @click="getApprovalList('03')" role="tab"
                    aria-controls="pills-contact" :aria-selected="applType=='03'?true:false">기결함</a>
            </li>
        </ul>
		<template v-if="apprList.length>0">
		<div class="row no-gutters notice-card" v-for="a in apprList">
			<div class="col-12 col-md-6 col-lg-9" @click="viewDetail(a, 'detail')">
				<div :class="['rounded-circle notice-mark '] + a.applCd">{{a.applNm.substr(0,1)}}</div>
				<div class="inner-wrap">
					<div class="title">{{a.applNm}}</div>
					<div class="desc" v-if="a">
						<span class="sub-title" >사용기한</span>
                        <span>
							{{moment(a.symd).format('YYYY.MM.DD')}}~{{moment(a.eymd).format('YYYY.MM.DD')}}
                        </span>
                        <span class="sub-desc" v-if="a.reason">{{a.reason}}</span>
					</div>
				</div>
			</div>
			<div class="col-12 col-md-2 col-lg-1">
				<span class="name">{{a.empNm}}</span>
			</div>
			<template v-if="applType=='01'">
			<div class="col-12 col-md-4 col-lg-2">
                <button type="button" class="btn btn-block btn-outline btn-approval">{{a.applStatusNm}}</button>
            </div>
            </template>
            <template v-if="applType=='02'">
			<div class="col-6 col-md-2 col-lg-1 pr-1">
				<button type="button"
					class="btn btn-block btn-outline btn-approval cancel" @click="viewDetail(a,'reject')">반송</button>
			</div>
			<div class="col-6 col-md-2 col-lg-1 pl-1">
				<button type="button"
					class="btn btn-block btn-outline btn-approval sign" @click="viewDetail(a,'apply')">승인</button>
			</div>
			</template>
		</div>
		</template>
		<template v-else>
		<div class="row no-gutters notice-card none">
			<div class="col-12">
				<div class="rounded-circle notice-mark AUTO"><i class="far fa-bell-slash"></i></div>
                <p class="name msg">결재 알림을 모두 확인했습니다.</p>
			</div>
		</div>
		</template>
	</div>
</div>

<script type="text/javascript">

    $(function (){
        $('#sYmd, #eYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
    });

    var approvalVue = new Vue({
   		el: "#approval",
   		components: {
			'appl-line': applLine
		},
   		data : {
   			apprList: [],
   			apprType: '', // 승인 or 반려
   			applType: '${applType}', // 신청 내역 조회('01') or 미결('02') or 기결('03')
   			apprOpinion: '',
   			appl: {}, //신청서view 
   			appr: {}, //승인할 신청서
   			chgSubsTarget: {}, //대체휴일 정정 데이터
            taaAppl:{},
            taaApplDetList:[],
            wtmTaaCanAppl:{},
            wtmTaaCode:{}

   		},
	    mounted: function(){
	    	this.getApprovalList(this.applType); //신청내역 조회
	    },
	    methods : {
	    	getApprovalList: function(applType){
	    		var $this = this;
	    		
	    		$("#loading").show();
	    		
	    		$this.apprList = [];
	    		if(applType != undefined && applType != null) {
	    		    $this.applType = applType;
                }

	    		var param = {
	    			"applType": $this.applType,
                    "sYmd":$("#sYmd").val(),
                    "eYmd":$("#eYmd").val(),
                    "searchKeyword":$("#searchKeyword").val()
	    		};
	    		
	    		Util.ajax({
					url: "${rc.getContextPath()}/appl/approvalApplList",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data.status=='OK' && data!=null) {
							$this.apprList = data.DATA;
							//console.log($this.apprList);
						}
					},
					error: function(e) {
						$("#loading").hide();
						$this.apprList = [];
					}
				});
	    	},
            viewDetail: function(appl, gubun) {
                $("#loading").show();
                var applId = appl.applId;
                var applCd = appl.applCd;
                var applSabun = appl.applSabun;
                var apprSeq = appl.apprSeq;

                var param = {
                    "applId" : applId,
                    "applCd" : applCd,
                    "applSabun" : applSabun
                }

                Util.ajax({
                    url: "${rc.getContextPath()}/appl/getApplDetail",
                    type: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify(param),
                    dataType: "json",
                    success: function(data) {
                        $("#loading").hide();
                        if(data.status=='OK' && data!=null) {
                            if(gubun == 'detail') {
                                approvalVue.viewAppl(data);
                            } else if(gubun == 'apply' || gubun == 'reject'){
                                data.applId = applId;
                                data.applCd = applCd;
                                data.applSabun = applSabun;
                                data.apprSeq = apprSeq;
                                approvalVue.approval(data, gubun);
                            }
                        }
                    },
                    error: function(e) {
                        $("#loading").hide();
                        approvalVue.apprList = [];
                    }
                });
            },
	    	viewAppl: function(appr){
	    		var $this = this;
	    		/* if(appr.applCd=='OT') {
	    			//연장근무신청서
	    			this.getOTAppl(appr.applId);
	    		} else if(appr.applCd=='SELE_F' || appr.applCd=='SELE_C') {
	    			//선근제 신청서
	    			this.getFlexibleSeleAppl(appr.applId);
	    		} */
	    		$this.appl = appr.appl;
	    		$this.appl['applCd'] = appr.applCd;
	    		
	    		if(appr.applCd=='OT' || appr.applCd=='OT_CAN') {
	    			//연장근무신청서
	    			$("#otAppl").modal("show"); 
	    		} else if(appr.applCd=='SELE_F' || appr.applCd=='SELE_C' || appr.applCd=='ELAS' || appr.applCd=='DIFF') {
	    			//선근제 신청서
	    			$("#flexibleAppl").modal("show");
	    		} else if(appr.applCd=='ENTRY_CHG') {
	    			//근태 사유서
	    			$("#inOutChangeAppl").modal("show");
	    		} else if(appr.applCd=='SUBS_CHG') {
	    			
	    			if($this.appl.hasOwnProperty("subs") && $this.appl.hasOwnProperty("oldSubs")
	    					&& $this.appl.subs!=null && $this.appl.oldSubs!=null
	    					&& $this.appl.subs!=undefined && $this.appl.oldSubs!=undefined
	    					&& $this.appl.subs!="" && $this.appl.oldSubs!='' ) {
	    				
	    				$this.appl.oldSubs.map(function(o){
	    					$this.appl.subs.map(function(s){
	    						if(s.hasOwnProperty('oldSubsApplId') && s.oldSubsApplId!=null && s.oldSubsApplId==o.otSubsApplId) {
    								$this.chgSubsTarget = {
    									sDate : o.subsSdate,
    									eDate : o.subsEdate
    								};
	    						} 
	    					});
	    				});
	    			}
	    			
	    			
	    			//대체휴가 정정
	    			$("#chgSubsModal").modal("show");
	    		} else if(appr.applCd=='COMP' || appr.applCd=='COMP_CAN') {
	    			//근태 사유서
	    			$("#compAppl").modal("show");

	    		//휴가신청
	    		} else if(appr.applCd=='ANNUAL' || appr.applCd=='REGA' ) {
                    $this.taaAppl = appr.appl.taaAppl;
                    $this.taaApplDetList = appr.appl.taaApplDetList;
                    $this.wtmTaaCode = appr.appl.wtmTaaCode;

                    if(appr.applCd == 'ANNUAL') {
                        $("#taaAppl").modal("show");
                    } else if(appr.applCd == 'REGA') {
                        $("#regaAppl").modal("show");
                    }

                } else if(appr.applCd=='ANNUAL_CAN') {
                    //휴가취소신청
                    $this.taaAppl = appr.appl.taaAppl;
                    $this.taaApplDetList = appr.appl.taaApplDetList;
                    $this.wtmTaaCanAppl = appr.appl.wtmTaaCanAppl;
                    $("#taaCanAppl").modal("show");

                } else if(appr.applCd=='REGA_CAN') {
                    //출장취소신청
                    $this.taaAppl = appr.appl.taaAppl;
                    $this.taaApplDetList = appr.appl.taaApplDetList;
                    $this.wtmTaaCanAppl = appr.appl.wtmTaaCanAppl;
                    $("#taaCanAppl").modal("show");
                }
                $("#loading").hide();
	    		
	    	},
	    	getOTAppl: function(applId){
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
						$this.appl = {};
						if(data!=null) {
							$this.appl = data;
							console.log(data);
							$("#otAppl").modal("show"); 
						}
					},
					error: function(e) {
						console.log(e);
						$this.appl = {};
					}
				});
					
	    	},
	    	getFlexibleSeleAppl: function(applId){
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
						$this.appl = {};
						if(data!=null) {
							$this.appl = data;
							$("#flexibleAppl").modal("show");
						}
						
					},
					error: function(e) {
						$this.flexibleAppl = {};
					}
				});
	    	},
	    	approval: function(appr, apprType){ //결재
	    		this.appr = appr;
	    		this.apprType = apprType;
	    		
	    		$('#apprOpinionModal').modal("show"); 
	    	},
	    	apply: function(){
	    		var $this = this;
	    		
    			var saveYn = true;
    			
    			//반려일 때만 결재의견 체크
    			if($this.apprType=='reject' && $this.apprOpinion=='') {
    				saveYn = false;
    				$("#alertText").html("의견을 입력해 주세요.");
    			}
    			
    			if(saveYn) {
    				//$('#apprOpinionModal').modal("hide"); 
    				$("#loading").show();
    				var appr = $this.appr;
	    			var param = appr;
	    			
	    			if($this.apprOpinion!='')
	    				param['apprOpinion'] = $this.apprOpinion;
	    			
	    			//연장근무신청 대상자
    				if(appr.appl.hasOwnProperty('targetList') && appr.appl.targetList!=null && appr.appl.targetList!=undefined) {
    					var applSabuns = [];
    					/* appr.appl.targetList.map(function(t){
    						applSabuns.push(t.sabun);
    					}); */
    					
    					$.each(appr.appl.targetList, function(k,v){
    						applSabuns.push(k);
    					});
    					
    					param['applSabuns'] = JSON.stringify(applSabuns);
    				}
	    			
	    			if(appr.applCd=='OT') {
	    				param['ymd'] = moment(appr.appl.ymd).format('YYYYMMDD');
	    				param['otSdate'] = moment(appr.appl.otSdate).format('YYYYMMDDHHmm');
	    				param['otEdate'] = moment(appr.appl.otEdate).format('YYYYMMDDHHmm');
	    			}else if(appr.applCd=='ENTRY_CHG') {
	    				param['ymd'] = moment(appr.appl.ymd).format('YYYYMMDD');
	    				
	    				if(appr.appl.planSdate!=null && appr.appl.planSdate!=undefined && appr.appl.planSdate!='')
	    					param['planSdate'] = appr.appl.planSdate;
	    				if(appr.appl.planEdate!=null && appr.appl.planEdate!=undefined && appr.appl.planEdate!='')
	    					param['planEdate'] = appr.appl.planEdate;
	    				if(appr.appl.entrySdate!=null && appr.appl.entrySdate!=undefined && appr.appl.entrySdate!='')
	    					param['entrySdate'] = appr.appl.entrySdate;
	    				if(appr.appl.entryEdate!=null && appr.appl.entryEdate!=undefined && appr.appl.entryEdate!='')
	    					param['entryEdate'] = appr.appl.entryEdate;
	    				if(appr.appl.chgSdate!=null && appr.appl.chgSdate!=undefined && appr.appl.chgSdate!='')
	    					param['chgSdate'] = appr.appl.chgSdate;
	    				if(appr.appl.chgEdate!=null && appr.appl.chgEdate!=undefined && appr.appl.chgEdate!='')
	    					param['chgEdate'] = appr.appl.chgEdate;

	    			} else if(appr.applCd=='ANNUAL') { // 휴가신청
                        param['symd'] = moment(appr.appl.symd).format('YYYYMMDD');
                        param['eymd'] = moment(appr.appl.eymd).format('YYYYMMDD');
                        param['sabun'] = appr.appl.sabun;
                    }
	    			
    	    		Util.ajax({
    					url: "${rc.getContextPath()}/appl/"+$this.apprType,
    					type: "POST",
    					contentType: 'application/json',
    					data: JSON.stringify(param),
    					dataType: "json",
    					success: function(data) {
    						$("#loading").hide();
    						$('#apprOpinionModal .close').click();
    						
    						if(data!=null && data.status=='OK') {
								$("#alertText").html("결재되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$this.getApprovalList($this.applType);
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
    						$("#alertText").html("확인요청 시 오류가 발생했습니다.");
      	  	         		$("#alertModal").on('hidden.bs.modal',function(){
      	  	         			$("#alertModal").off('hidden.bs.modal');
      	  	         		});
      	  	         		$("#alertModal").modal("show"); 
    					}
    				});
    			} else {
    				$("#alertModal").on('hidden.bs.modal',function(){
    					$("#apprOpinion").focus();
    				});
 	  	         		$("#alertModal").modal("show"); 
    			}
	    	},
	    	otCancelAppl: function(){ //연장근무취소신청
         		var $this = this;
         	
         		$("#loading").show();
         		
         		var appl = $this.appl;
         		
         		var param = {
         			workDayResultId: appl.workDayResultId,
         			otApplId: appl.otApplId,
         			applId: appl.applId,
         			status: appl.applStatusCd,
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
							$("#alertText").html("취소요청 되었습니다.");
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								$("#cancelOpinionModal").modal("hide");
								$("#otAppl").modal("hide");
								$this.getApprovalList($this.applType);
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
						$("#alertText").html("연장근무 취소 시 오류가 발생했습니다.");
  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	  	         			$("#alertModal").off('hidden.bs.modal');
  	  	         		});
  	  	         		$("#alertModal").modal("show"); 
					}
				}); 
	         		
	         },
	         recoveryAppl: function(){ //연장근무 회수
         		var $this = this;
	         
         		var appl = $this.appl;
				
         		$("#loading").show();
         		
				var param = {
         			applCd : appl.applCd,
         			applId : appl.applId
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
								
								if(appl.applCd == 'SUBS_CHG') {
									$("#chgSubsModal").modal("hide");
								} else {
									$("#otAppl").modal("hide");
								}
								$this.getApprovalList($this.applType);
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
         	accordionDropdown: function(target) {
         		var $el = $('#accordion');
         		$this = $(target),
                $next = $this.next();

                $next.slideToggle();
                $this.parent().toggleClass('open');
                
                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');

         	}
	    }
   	});
   	
   	$('[data-dismiss=modal]').on('click', function (e) {
		var $t = $(this),
	        target = $t[0].href || $t.data("target") || $t.parents('.modal') || [];

		$(target).find("textarea").val('').end();
	})


</script>