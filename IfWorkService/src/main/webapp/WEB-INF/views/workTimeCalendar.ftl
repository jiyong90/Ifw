<#include "/applLineComponent.ftl">
<div id="timeCalendar" class="calendar-wrap" v-cloak>
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
                        	<div class="inner-wrap position-relative row">
			                	<div class="loading-spinner" style="display:none;"></div>
			                	<div class="col-4 col-sm-3 col-md-3 col-lg-2" v-if="targets != null && targets['${empNo}'] != null && targets['${empNo}'].restWorkMinute != null">
				                	<p class="page-sub-title mb-0">잔여 소정근로시간</p>
				                    <span class="time-wrap">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(targets['${empNo}'].restWorkMinute, 'detail')}}</span> 
				                    </span>
			                    </div>
			                    <div class="col-4 col-sm-3 col-md-3 col-lg-2" v-if="targets != null && targets['${empNo}'] != null && targets['${empNo}'].restOtMinute != null">
				                	<p class="page-sub-title mb-0">잔여 연장근로시간</p>
				                    <span class="time-wrap">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(targets['${empNo}'].restOtMinute, 'detail')}}</span>
				                    </span>
			                    </div>
			                    <hr class="separate-bar">
		                    </div>
                            <div class="inner-wrap">
                                <div class="desc row">
                                    <div class="col-sm-12 col-md-12 col-lg-2 pr-lg-0">
                                        <div class="title" id="overtime" v-if="result.holidayYn!='Y'">연장근로시간</div>
                                        <div class="title" id="overtime" v-else>휴일근로시간</div>
                                        <span class="time-wrap">
                                            <i class="fas fa-clock"></i>
                                            <span class="time" v-if="overtime.calcMinute">{{minuteToHHMM(overtime.calcMinute,'detail')}}</span>
                                            <span class="time" v-else>{{minuteToHHMM(0,'detail')}}</span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sDate" data-toggle="datetimepicker" data-target="#sDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sTime" data-toggle="datetimepicker" data-target="#sTime" autocomplete="off" required>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eDate" data-toggle="datetimepicker" data-target="#eDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eTime" data-toggle="datetimepicker" data-target="#eTime" autocomplete="off" required>
                                            </div>
                                        </div>
                                        <div class="guide" v-if="overtime.breakMinute && overtime.breakMinute!=0">*해당일 총 휴게시간은 {{minuteToHHMM(overtime.breakMinute,'detail')}} 입니다.</div>
                                        <div class="guide" v-if="targets != null && targets['${empNo}'] != null && targets['${empNo}'].guideMessage != null">{{targets['${empNo}'].guideMessage}}</div>
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
                                    <label for="otReason">설명</label>
                                    <textarea class="form-control" id="otReason" rows="3"
                                        placeholder="팀장 확인 시에 필요합니다." required></textarea>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="isOtUse && result.holidayYn=='Y' && (subsYn=='Y' || payTargetYn)">
                                <div class="title mb-2">휴일대체방법</div>
                                <div class="desc">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="subYnY" name="subYn" class="custom-control-input" value="Y" @change="changeSubYn($event.target.value)" :required="subsRequired">
                                        <label class="custom-control-label" for="subYnY">휴일대체</label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline" v-show="payTargetYn">
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
			                                    <div class="col-md-12 col-lg-2 pr-lg-0">
		                                            <span class="time-wrap">
		                                                <i class="fas fa-clock"></i>
		                                                <span class="time" v-if="s.subsMinute">{{minuteToHHMM(s.subsMinute, 'detail')}}</span>
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
		                                            	<span v-if="s.subsBreakMinute"> 총 휴게시간은 {{minuteToHHMM(s.subsBreakMinute, 'detail')}} 입니다.</span>
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
                            <appl-line :bind-data="applLine"></appl-line>
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
                                    <label for="cancelOpinion">취소 사유</label>
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
    <!-- 근무시간 정정요청 modal start -->
    <div class="modal fade show" id="inOutChangeModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">근무시간 정정요청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                        	<div class="form-row no-gutters">
		                        <div class="form-group col-12">
	                                <label for="planDate">계획 근무시간</label>
	                                <div id="planDate" v-if="inOutChangeAppl.planSdate||inOutChangeAppl.planEdate">
		                             	<template v-if="inOutChangeAppl.planSdate">{{moment(inOutChangeAppl.planSdate).format('YYYY-MM-DD HH:mm')}}</template>
		                             	<template v-if="inOutChangeAppl.planEdate">~ {{moment(inOutChangeAppl.planEdate).format('YYYY-MM-DD HH:mm')}}</template>
	                            	</div>
	                            </div>
	                        </div>
	                        <div class="form-row no-gutters">
	                            <div class="form-group col-12">
	                                <label for="inOutDate">출/퇴근 시각</label>
	                                <div>
		                             	<template v-if="inOutChangeAppl.inoutSdate">
		                             		{{moment(inOutChangeAppl.inoutSdate).format('YYYY-MM-DD HH:mm')}}
		                             	</template>
		                             	<template v-else>
		                             		미타각
		                             	</template>
		                             	~
		                             	<template v-if="inOutChangeAppl.inoutEdate">
		                             		{{moment(inOutChangeAppl.inoutEdate).format('YYYY-MM-DD HH:mm')}}
		                             	</template>
		                             	<template v-else>
		                             		미타각
		                             	</template>
	                            	</div>
	                            </div>
	                        </div>
                           	<div class="time-input-form form-row no-gutters">
	                            <div class="form-group col-6 pr-1">
	                                <label for="chgSdate" data-target-input="nearest">변경 출근시간</label>
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="chgSdate" value="" data-toggle="datetimepicker" data-target="#chgSdate" autocomplete="off">
	                            </div>
	                            <div class="form-group col-6 pl-1">
	                                <label for="chgEdate" data-target-input="nearest">변경 퇴근시간</label>
	                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="chgEdate" value="" data-toggle="datetimepicker" data-target="#chgEdate" autocomplete="off">
	                            </div>
	                        </div>
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="chgReason">사유</label>
                                    <textarea class="form-control" id="chgReason" rows="3" placeholder="사유를 반드시 입력해 주시기 바랍니다."
                                        required=""></textarea>
                                </div>
                            </div>
                            <appl-line :bind-data="applLine"></appl-line>
                        </div>
                        <div class="btn-wrap text-center">
                            <button type="button" class="btn btn-secondary rounded-0"
                                data-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-default rounded-0" @click="inOutChange">확인요청</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 근무시간 정정요청 modal end -->
    <!-- 연장근무신청 상세보기 modal start -->
    <div class="modal fade show" id="overtimeApplDetail" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                	<template v-if="overtimeAppl.cancelYn!=null&&overtimeAppl.cancelYn!=undefined&&overtimeAppl.cancelYn=='Y'">
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
                        	<div class="inner-wrap position-relative">
			                	<div class="loading-spinner" style="display:none;"></div>
			                	<p class="page-sub-title mb-0">잔여 연장근로시간</p>
			                    <span class="time-wrap">
			                        <i class="fas fa-clock"></i><span class="time point"></span>
			                    </span>
			                    <hr class="separate-bar">
		                    </div>
                            <div class="inner-wrap">
                                <div class="title" v-if="result.holidayYn!='Y'">연장근로시간</div>
                                <div class="title" v-else>휴일근로시간</div>
                                <div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="overtimeAppl.otMinute">
                                        		{{minuteToHHMM(overtimeAppl.otMinute, 'detail')}}
                                        	</template>
                                        	<template v-else>
                                        		{{minuteToHHMM(0, 'detail')}}
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
                            <div class="inner-wrap" v-show="result.holidayYn=='Y' && overtimeAppl.subYn=='Y'">
                                <div class="title">대체일시</div>
                                <template v-if="overtimeAppl.subs" v-for="sub in overtimeAppl.subs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(sub.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(sub.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(sub.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc" v-if="(sub.workSDate && sub.workEDate) || overtimeAppl.oldSubs">
                                <template v-if="sub.workSDate && sub.workEDate">
                                *해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.
                                </template>
                                <template v-if="overtimeAppl.oldSubs">
	                                <template v-for="oldSub in overtimeAppl.oldSubs" v-if="oldSub.otSubsApplId == sub.oldSubsApplId">
	                                <br>*이전 대체휴일은  {{moment(oldSub.subsSdate).format('YYYY-MM-DD HH:mm')}}~{{moment(oldSub.subsEdate).format('YYYY-MM-DD HH:mm')}} 입니다.
	                                </template>
                                </template>
                                </div>
                                </template>
                            </div>
                            <div class="inner-wrap" v-if="overtimeAppl.cancelReason">
                                <div class="title">취소사유</div>
                                <div class="desc">
                                	{{overtimeAppl.cancelReason}}
                                </div>
                            </div>
                            <appl-line :bind-data="overtimeAppl.applLine"></appl-line>
                            <hr class="bar">
                        </div>
                        <div class="btn-wrap text-center" v-if="overtimeAppl.applSabun==overtimeAppl.sabun">
                        	<template v-if="(overtimeAppl.cancelYn==null||overtimeAppl.cancelYn==undefined||overtimeAppl.cancelYn!='Y') && overtimeAppl.applStatusCd=='99'">
                            	<button type="button" class="btn btn-default rounded-0" v-if="workCloseYn == 'N' && result.holidayYn!='Y'" data-toggle="modal" data-target="#cancelOpinionModal">연장근로신청 취소하기</button>
                            	<button type="button" class="btn btn-default rounded-0" v-if="workCloseYn == 'N' && result.holidayYn =='Y'" data-toggle="modal" data-target="#cancelOpinionModal">휴일근로신청 취소하기</button>
                        	</template>
                        	<template v-else>
                            	<button type="button" id="recoveryBtn" class="btn btn-default rounded-0" style="display:none;" data-toggle="modal" data-target="#confirmModal">회수하기</button>
                        	</template>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 연장근무신청 상세보기 modal end -->
    <!-- 대체휴일 정정요청 modal start -->
    <div class="modal fade show" id="chgSubsModal" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
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
                        <div class="modal-app-wrap" v-if="overtimeAppl">
	                        <div class="inner-wrap">
                                <div class="title">휴일근로시간</div>
                                <div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="overtimeAppl.otMinute">
                                        		{{minuteToHHMM(overtimeAppl.otMinute, 'detail')}}
                                        	</template>
                                        	<template v-else>
                                        		{{minuteToHHMM(0, 'detail')}}
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
                                <div class="title">대체휴일</div>
                                <template v-if="overtimeAppl.subs" v-for="s in overtimeAppl.subs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(s.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(s.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(s.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc" v-if="s.workSDate&&s.workEDate">*해당일 근무시간은 {{moment(s.workSDate).format('HH:mm')}}~{{moment(s.workEDate).format('HH:mm')}} 입니다.</div>
                                </template>
                            </div>
                            <hr class="separate-bar">
                           	<div class="inner-wrap">
                            	<div class="big-title">{{moment(chgSubsAppl.sDate).format('YYYY-MM-DD HH:mm')}} ~ {{moment(chgSubsAppl.eDate).format('YYYY-MM-DD HH:mm')}} 의 변경 대체휴일</div>
                                <div class="desc row" v-for="(s, idx) in subYmds">
                                   	<div class="col-12 col-lg-12 mb-sm-2">
                                   		<div class="form-row">
		                                    <div class="col-md-12 col-lg-2 pr-lg-0">
	                                            <span class="time-wrap">
	                                                <i class="fas fa-clock"></i>
	                                                <span class="time" v-if="s.subsMinute">{{minuteToHHMM(s.subsMinute, 'detail')}}</span>
		                                            <span class="time" v-else>0시간</span>
	                                            </span>
	                                        </div>
		                                    <div class="form-row col-md-12 col-lg-10 mb-sm-2">
	                                            <div class="col-4 col-sm-4 col-md-4 col-lg-4" data-target-input="nearest">
                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'chgSubsYmd_'+idx" v-model="s.chgSubsYmd" data-toggle="datetimepicker" :data-target="'#chgSubsYmd_'+idx" placeholder="연도-월-일" autocomplete="off" required>
                                                </div>
                                                <div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'chgSubsShm_'+idx" v-model="s.chgSubsShm" data-toggle="datetimepicker" :data-target="'#chgSubsShm_'+idx" autocomplete="off" required>
                                                </div>
                                                <span class="col-1 col-sm-1 col-md-1 col-lg-1 text-center pl-2 pr-2 mt-1">~</span>
                                                <div class="col-3 col-sm-3 col-md-3 col-lg-3" data-target-input="nearest">
                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'chgSubsEhm_'+idx" v-model="s.chgSubsEhm" data-toggle="datetimepicker" :data-target="'#chgSubsEhm_'+idx" autocomplete="off" required>
                                                </div>
	                                            <div class="guide pl-1">
	                                            	<span v-if="s.workShm && s.workEhm">*해당일 근무시간은 {{s.workShm}}~{{s.workEhm}} 입니다.</span>
	                                            	<span v-if="s.subsBreakMinute"> 총 휴게시간은 {{minuteToHHMM(s.subsBreakMinute, 'detail')}} 입니다.</span>
	                                            </div>
	                                        </div>
                                        </div>
                                	</div>
                               	</div>
                            </div>
                            <appl-line :bind-data="applLine"></appl-line>
                        </div>
                        <div class="btn-wrap text-center">
                            <button type="button" class="btn btn-secondary rounded-0"
                                data-dismiss="modal">취소</button>
                            <button type="button" class="btn btn-default rounded-0" @click="validateChgSubsAppl">정정요청</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 대체휴일 정정요청 modal end -->
    <!-- 대체휴일 정정신청 상세보기 modal start -->
    <div class="modal fade show" id="chgSubsDetail" tabindex="-1" role="dialog"  data-backdrop="static" data-keyboard="false">
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
                                        	<template v-if="overtimeAppl.otMinute">
                                        		{{minuteToHHMM(overtimeAppl.otMinute, 'detail')}}
                                        	</template>
                                        	<template v-else>
                                        		{{minuteToHHMM(0, 'detail')}}
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
                                <div class="title">대체휴일</div>
                                <template v-if="overtimeAppl.oldSubs" v-for="s in overtimeAppl.oldSubs">
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">{{moment(s.subsSdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="day-end-time">{{moment(s.subsEdate).format('YYYY-MM-DD HH:mm')}}</span>
                                        <span class="sub-time">{{minuteToHHMM(s.subsMinute,'detail')}}</span>
                                    </span>
                                </div>
                                <div class="sub-desc" v-if="s.workSDate&&s.workEDate">*해당일 근무시간은 {{moment(s.workSDate).format('HH:mm')}}~{{moment(s.workEDate).format('HH:mm')}} 입니다.</div>
                                </template>
                            </div>
                            <hr class="separate-bar">
                            <div class="inner-wrap">
                                <div class="big-title">{{moment(chgSubsAppl.sDate).format('YYYY-MM-DD HH:mm')}} ~ {{moment(chgSubsAppl.eDate).format('YYYY-MM-DD HH:mm')}} 의 변경 대체휴일</div>
                                <template v-if="overtimeAppl.subs" v-for="sub in overtimeAppl.subs">
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
                            <appl-line :bind-data="overtimeAppl.applLine"></appl-line>
                        </div>
                        <div class="btn-wrap text-center">
                            <button type="button" class="btn btn-default rounded-0" v-if="overtimeAppl.recoveryYn" data-toggle="modal" data-target="#confirmModal">회수하기</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 대체휴일 정정신청 상세보기 modal end -->

    <!-- 휴가신청 상세보기 modal start -->
    <div class="modal fade show" id="annualApplDetail" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title" >휴가 신청내역</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap position-relative">
                                <div class="loading-spinner" style="display:none;"></div>
                                <p class="page-sub-title mb-0">휴가 구분</p>
                                <div class="desc">
                                     {{annualAppl.taaNm}}
			                    </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">휴가기간</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="annualAppl.sYmd">
                                        	{{moment(annualAppl.sYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="annualAppl.eYmd">
                                        	{{moment(annualAppl.eYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">설명</div>
                                <div class="desc">
                                    <template v-if="annualAppl.reason">
                                        {{annualAppl.reason}}
                                    </template>
                                </div>
                            </div>
                            <appl-line :bind-data="applLine"></appl-line>
                            <hr class="bar">
                        </div>
                        <div class="btn-wrap text-center" v-if="annualAppl.applSabun==annualAppl.sabun">
                            <template v-if="annualAppl.applStatusCd=='99'">
                                <button type="button" class="btn btn-default rounded-0" v-if="workCloseYn == 'N'" data-toggle="modal" data-dismiss="modal"  @click="annualCanShow">휴가 취소하기</button>
                            </template>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 휴가신청 상세보기 modal end -->

    <!-- 휴가취소 상세보기 modal start -->
    <div class="modal fade show" id="cancelAnnualModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">휴가 신청취소</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap position-relative">
                                <div class="loading-spinner" style="display:none;"></div>
                                <p class="page-sub-title mb-0">휴가 구분</p>
                                <div class="desc">
                                    {{annualAppl.taaNm}}
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">휴가기간</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="annualAppl.sYmd">
                                        	{{moment(annualAppl.sYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="annualAppl.eYmd">
                                        	{{moment(annualAppl.eYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>

                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
									<textarea class="form-control" id="annualCanNote" name="annualCanNote" rows="3" placeholder="" required></textarea>
                                </div>
                            </div>
                            <appl-line :bind-data="applLine"></appl-line>
                        </div>
                        <div class="btn-wrap text-center" v-if="annualAppl.applSabun==annualAppl.sabun">
                            <template v-if="annualAppl.applStatusCd=='99'">
                                <button type="button" class="btn btn-secondary rounded-0" data-toggle="modal" data-dismiss="modal">취소</button>
                                <button type="button" class="btn btn-default rounded-0" v-if="workCloseYn == 'N'" data-toggle="modal" @click="annualCanApplRequest">취소 신청하기</button>
                            </template>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 휴가취소 신청 modal end -->

    <!-- 출장신청 상세보기 modal start -->
    <div class="modal fade show" id="regaDetail" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title" >출장 신청내역</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap position-relative">
                                <div class="loading-spinner" style="display:none;"></div>
                                <p class="page-sub-title mb-0">간주근무 구분</p>
                                <div class="desc">
                                    {{annualAppl.taaNm}}
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">기간</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="annualAppl.sYmd">
                                        	{{moment(annualAppl.sYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="annualAppl.eYmd">
                                        	{{moment(annualAppl.eYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">설명</div>
                                <div class="desc">
                                    <template v-if="annualAppl.reason">
                                        {{annualAppl.reason}}
                                    </template>
                                </div>
                            </div>
                            <appl-line :bind-data="applLine"></appl-line>
                            <hr class="bar">
                        </div>
                        <div class="btn-wrap text-center" v-if="annualAppl.applSabun==annualAppl.sabun">
                            <template v-if="annualAppl.applStatusCd=='99'">
                                <button type="button" class="btn btn-default rounded-0" v-if="workCloseYn == 'N'" data-toggle="modal" data-dismiss="modal"  @click="regaCanShow">취소 신청하기</button>
                            </template>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 출장신청 상세보기 modal end -->

    <!-- 휴가취소 상세보기 modal start -->
    <div class="modal fade show" id="cancelRegaModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">출장 신청취소</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap position-relative">
                                <div class="loading-spinner" style="display:none;"></div>
                                <p class="page-sub-title mb-0">간주근무 구분</p>
                                <div class="desc">
                                    {{annualAppl.taaNm}}
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="title">기간</div>
                                <div class="desc">
                                    <span class="date-wrap">
                                        <span class="start-date">
                                        	<template v-if="annualAppl.sYmd">
                                        	{{moment(annualAppl.sYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="annualAppl.eYmd">
                                        	{{moment(annualAppl.eYmd).format('YYYY-MM-DD')}}
                                        	</template>
                                        </span>
                                    </span>
                                </div>
                            </div>

                            <div class="inner-wrap">
                                <div class="title">사유</div>
                                <div class="desc">
                                    <textarea class="form-control" id="annualCanNote" name="annualCanNote" rows="3" placeholder="" required></textarea>
                                </div>
                            </div>
                            <appl-line :bind-data="applLine"></appl-line>
                        </div>
                        <div class="btn-wrap text-center" v-if="annualAppl.applSabun==annualAppl.sabun">
                            <template v-if="annualAppl.applStatusCd=='99'">
                                <button type="button" class="btn btn-secondary rounded-0" data-toggle="modal" data-dismiss="modal">취소</button>
                                <button type="button" class="btn btn-default rounded-0" v-if="workCloseYn == 'N'" data-toggle="modal" @click="regaCanApplRequest">간주근무취소 신청</button>
                            </template>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 휴가취소 신청 modal end -->

    <div id='calendar-container'>
		<full-calendar ref="fullCalendar" :unselectauto="t" :custombuttons="customButtons" :header="header" :defaultview="view" :nowindicator="t" :scrolltime="moment(new Date()).format('HH:mm:ss')" @update="renderCallback" @datesrender="datesRenderCallback" @dateclick="dateClickCallback" @select="selectCallback" @eventclick="eventClickCallback"></full-calendar>
    </div>
</div>
<script type="text/javascript">
	$(function () {
		$('#sDate, #eDate').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		
        $('#sTime, #chgSdate').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#eTime, #chgEdate').datetimepicker({
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
  				FullCalendar : fullCalendarComponent,
  				'appl-line': applLine
  		    },
  		    data : {
  		    	t: true,
  		    	customButtons: {
  		        	month_calendar: {
  		            	text: '근태달력보기',
  		              	click: function() {
  		              		location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Month';
  		              	}	
  		          	}
  		       	},
  		    	header: {
  		    		left: 'prev,next',
			        center: 'title',
			        right: 'month_calendar'
  		    	},
  		    	view: 'timeGridDay',
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
  		    	annualAppl: {}, //휴가정정 데이터
  		    	regaAppl: {}, //간주근무 데이터
  		    	isOtUse:false, //대체휴일 Div section사용 여부 기본근무에서 잔여 소정근로시간이 남았을 경우 잔여소정근로시간 선 소진 후 나머지 시간이 있을 경우 사용할 수 있
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
		    	
  		    	//this.getFlexibleDayInfo(this.workday);
  		    	
  		    	//근무일 화면 전환
         		$("#workRangeInfo").show();
         		$("#flexibleDayInfo").show();
         		
  		    },
  		    methods : {
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
  		    		//상세보기
  		    		if(info.event.extendedProps.timeTypeCd=='OT' || info.event.extendedProps.timeTypeCd=='OT_CAN' || info.event.extendedProps.timeTypeCd=='NIGHT' ) {
  		    			this.viewOvertimeApplDetail(info.event.extendedProps.timeTypeCd, info.event.extendedProps.applId, true);
  		    		} else if(info.event.extendedProps.timeTypeCd=='SUBS') {
  		    			this.viewChgSubsAppl(info);
  		    		} else if(info.event.extendedProps.timeTypeCd=='SUBS_CHG') {
  		    			this.viewChgSubsApplDetail(info.event.extendedProps.applId, info);
  		    		} else if(info.event.extendedProps.timeTypeCd=='TAA') {
                        this.viewAnnualApplDetail(info.event.extendedProps.applId, info);
                    } else if(info.event.extendedProps.timeTypeCd=='REGA') {
                        this.viewRegaApplDetail(info.event.extendedProps.applId, info);
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
  							$this.inOutChangeAppl = {};
  							if(data!=null) {
  								calendarLeftVue.workTimeInfo = data;
  								
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
	     			$this.otTime = $this.overtime.calcMinute;
					//휴일근로신청의 경우 이전에 신청한 휴일 가져옴
					/* if(Object.keys($this.result).length>0 && $this.result.hasOwnProperty('holidayYn')
							&& $this.result.holidayYn!=null && $this.result.holidayYn=='Y') {
						$this.getOtSubs(moment(sYmd).format('YYYYMMDD'));
					} */
					
					$this.getRestOtMinute();
					
					this.applLine = calendarLeftVue.getApplLine('OT');
					
					$("#overtimeAppl").modal("show"); 
					
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
  	         		
  	         		$this.applLine = calendarLeftVue.getApplLine('SUBS_CHG');
  	         		
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
                viewAnnualApplDetail: function(applId, info){

  	         	   if('${tsId}' !='soldev' && '${tsId}' != 'dyc') {
  	         	       return;
                   }
                    var $this = this;

                    $this.annualAppl = {
                        applId : applId,
                        sDate : info.event.start,
                        eDate : info.event.end
                    };

                    var param = {
                        applId: applId
                    };

                    //휴가신청내역 조회
                    Util.ajax({
                        url: "${rc.getContextPath()}/taaCanAppl",
                        type: "GET",
                        contentType: 'application/json',
                        data: param,
                        dataType: "json",
                        async: false,
                        success: function(data) {
                            if(data!=null) {
                                $this.annualAppl = data.taaApplList[0];
                                $this.applLine = data.applLine;
                                $("#annualApplDetail").modal("show");
                            }
                        },
                        error: function(e) {
                            console.log(e);
                            $this.overtimeAppl = {};
                        }
                    });

                },
                viewRegaApplDetail: function(applId, info){

                    if('${tsId}' !='soldev' && '${tsId}' != 'dyc') {
                        return;
                    }

                    var $this = this;

                    $this.regaAppl = {
                        applId : applId,
                        sDate : info.event.start,
                        eDate : info.event.end
                    };

                    var param = {
                        applId: applId
                    };

                    Util.ajax({
                        url: "${rc.getContextPath()}/taaCanAppl",
                        type: "GET",
                        contentType: 'application/json',
                        data: param,
                        dataType: "json",
                        async: false,
                        success: function(data) {
                            if(data!=null) {
                                $this.annualAppl = data.taaApplList[0];
                                $this.applLine = data.applLine;

                                if(data.recoveryYn) {
                                    $("#recoveryBtn").show();
                                } else {
                                    $("#recoveryBtn").hide();
                                }

                                $("#regaDetail").modal("show");
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
							
  	         				
  	         				var sEntry = {
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
  	         				$this.addEvent(eEntry); 
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
		  	  	  		  		        		classNames: classNames,
                                                extendedProps: {
                                                    applId: vMap.applId,
                                                    timeTypeCd: vMap.timeTypeCd
                                                }
		  	  	    	         			};
										}
		  	  	         					
		  	  	    	         		$this.addEvent(result); 
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
  	  	         					
  	  	    	         			$this.addEvent(result); 
	  	         				}
         					});
  	         			}
  	         		}
  	         	},
  	         	preCheck : function(info, btnYn){ //소정근로 선 소진 여부, 연장근무 가능한지 체크
  	         		var $this = this;
  	         	
  	         		$("#loading").show();
  	         	
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
			         	//
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
        				ymd: moment($this.workday).format('YYYYMMDD'),
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
								$("#alertText").html("확인요청 되었습니다.");
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
							$("#alertText").html("연장근무 확인요청 시 오류가 발생했습니다.");
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
								$("#alertText").html("취소요청 되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$("#cancelOpinionModal").modal("hide");
									$("#overtimeApplDetail").modal("hide");
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
							$("#alertText").html("연장근무 취소 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
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
								$("#alertText").html("확인요청 되었습니다.");
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
  	         			var ymd = moment($this.workday).format('YYYYMMDD');
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
         					param['chgSdate'] = moment(moment($this.workday+' '+$("#chgSdate").val()).format('YYYY-MM-DD HH:mm')).format('YYYYMMDDHHmm');
  	         			if($("#chgEdate").val()!=undefined && $("#chgEdate").val()!='')
  	         				param['chgEdate'] = moment(moment($this.workday+' '+$("#chgEdate").val()).format('YYYY-MM-DD HH:mm')).format('YYYYMMDDHHmm');
  	         			param['reason'] = $("#chgReason").val();
  	         			
  	         			Util.ajax({
	  	  					url: "${rc.getContextPath()}/inOutChangeAppl/request",
	  	  					type: "POST",
	  	  					contentType: 'application/json',
	  	  					data: JSON.stringify(param),
	  	  					dataType: "json",
	  	  					success: function(data) {
	  	  						if(data!=null && data.status=='OK') {
		  	  						$("#alertText").html("확인요청 되었습니다.");
			  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	  	         			$("#alertModal").off('hidden.bs.modal');
			  	  	         			$("#inOutChangeModal").modal("hide"); 
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
	  	  						$("#alertText").html("저장 시 오류가 발생했습니다.");
	  	    	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	    	  	         		$("#alertModal").modal("show"); 
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
								console.log(data.targetList);
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
  	         	},
                annualCanShow : function () {  //휴가 신청

                    this.applLine = calendarLeftVue.getApplLine('ANNUAL');
                    $("#cancelAnnualModal").modal("show");

                },
                annualCanApplRequest : function() {
                    var $this = this;
                    $("#loading").show();
                    var param = {
                        taaApplId :  this.annualAppl.applId,
                        reason : $('#annualCanNote').val(),
                        status : "21",
                        workTypeCd : "ANNUAL_CAN"
                    };

                    $("#cancelAnnualModal").modal("hide");

                    Util.ajax({
                        url: "${rc.getContextPath()}/taaCanAppl/request",
                        type: "POST",
                        contentType: 'application/json',
                        data: JSON.stringify(param),
                        dataType: "json",
                        success: function(data) {
                            $("#loading").hide();
                            if(data!=null && data.status=='OK') {
                                $("#alertText").html("취소신청 되었습니다.");

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
                            $("#alertText").html("신청시 오류가 발생했습니다.");
                            $("#alertModal").on('hidden.bs.modal',function(){
                                $("#alertModal").off('hidden.bs.modal');
                            });
                            $("#alertModal").modal("show");
                        }
                    });
                },
                regaCanShow : function () {  //출장취소 신청

                    this.applLine = calendarLeftVue.getApplLine('REGA_CAN');
                    $("#cancelRegaModal").modal("show");

                },
                regaCanApplRequest : function() {
                    var $this = this;
                    $("#loading").show();
                    var param = {
                        taaApplId :  this.annualAppl.applId,
                        reason : $('#annualCanNote').val(),
                        status : "21",
                        workTypeCd : "REGA_CAN"
                    };

                    $("#cancelRegaModal").modal("hide");

                    Util.ajax({
                        url: "${rc.getContextPath()}/wtmRega/canRequest",
                        type: "POST",
                        contentType: 'application/json',
                        data: JSON.stringify(param),
                        dataType: "json",
                        success: function(data) {
                            $("#loading").hide();
                            if(data!=null && data.status=='OK') {
                                $("#alertText").html("취소신청 되었습니다.");

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
                            $("#alertText").html("신청시 오류가 발생했습니다.");
                            $("#alertModal").on('hidden.bs.modal',function(){
                                $("#alertModal").off('hidden.bs.modal');
                            });
                            $("#alertModal").modal("show");
                        }
                    });
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
   	
   	//대체휴가 정정
   	$('body').on('focus',"input[id^='chgSubsYmd']", function(){
   		var $this = this;
 		$(this).datetimepicker({
 			format: 'YYYY-MM-DD',
 		    language: 'ko',
 		    useCurrent: false
 		});
 		
 		$(this).off("change.datetimepicker").on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var subDate = moment(e.date).format('YYYYMMDD');
 				var id = $($this).attr('id');
 				var msg ='';
 				if(timeCalendarVue.chgSubsAppl.hasOwnProperty('sDate') && timeCalendarVue.chgSubsAppl.sDate!='' && timeCalendarVue.chgSubsAppl.hasOwnProperty('eDate') && timeCalendarVue.chgSubsAppl.eDate!='') {
	 				//대체휴가 사용 가능일인지 판단
	         		if(timeCalendarVue.applCode!=null && timeCalendarVue.hasOwnProperty('OT')) {
	         			var sDate = moment(timeCalendarVue.chgSubsAppl.sDate).format('YYYYMMDD');
	           			var eDate = moment(timeCalendarVue.chgSubsAppl.eDate).format('YYYYMMDD');
	         			var otSdate = moment(sDate).format('YYYYMMDD');
	                 	var otEdate = moment(eDate).format('YYYYMMDD');
	                 	
	         			var applCode = timeCalendarVue.applCode['SUBS_CHG'];
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
   	
   	$('body').on('focus',"input[id^='chgSubsShm']", function(){
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
		
		$(this).off("change.datetimepicker").on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var id = $($this).attr('id');
 				timeCalendarVue.updateValue(id, moment(e.date).format('HH:mm'));
 				timeCalendarVue.calcChgSubsTime(id);
 			}
 		});
   	});
   	
   	$('body').on('focus',"input[id^='chgSubsEhm']", function(){
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
		
		$(this).off("change.datetimepicker").on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var id = $($this).attr('id');
 				timeCalendarVue.updateValue(id, moment(e.date).format('HH:mm'));
 				timeCalendarVue.calcChgSubsTime(id);
 			}
 		});
   	})

   	//연장근로 신청
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
	         		if(timeCalendarVue.applCode!=null && timeCalendarVue.hasOwnProperty('OT')) {
	         			var sDate = $("#sDate").val().replace(/-/gi,"");
	           			var eDate = $("#eDate").val().replace(/-/gi,"");
	         			var otSdate = moment(sDate).format('YYYYMMDD');
	                 	var otEdate = moment(eDate).format('YYYYMMDD');
	                 	
	         			var applCode = timeCalendarVue.applCode['OT'];
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
		
		//시간 달력 선택 시 단위 세팅하면 1분 단위 신청 못해서 주석처리
		/* if(calendarLeftVue.rangeInfo!=null && Object.keys(calendarLeftVue.rangeInfo).length>0) {
			var unitMinute = calendarLeftVue.rangeInfo.unitMinute;
			if(unitMinute!=null && unitMinute!=undefined && unitMinute!='')
				$(this).datetimepicker('stepping',Number(unitMinute));
		} */
		
		$(this).off("change.datetimepicker").on("change.datetimepicker", function(e){
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
		
		//시간 달력 선택 시 단위 세팅하면 1분 단위 신청 못해서 주석처리
		/* if(calendarLeftVue.rangeInfo!=null && Object.keys(calendarLeftVue.rangeInfo).length>0) {
			var unitMinute = calendarLeftVue.rangeInfo.unitMinute;
			if(unitMinute!=null && unitMinute!=undefined && unitMinute!='')
				$(this).datetimepicker('stepping',Number(unitMinute));
		} */
		
		$(this).off("change.datetimepicker").on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				var id = $($this).attr('id');
 				timeCalendarVue.updateValue(id, moment(e.date).format('HH:mm'));
 				timeCalendarVue.calcSubsTime(id);
 			}
 		});
   	})
   	
   	//날짜,시간 변경 시 근로시간 계산
    $('#sDate, #eDate, #sTime, #eTime').off("change.datetimepicker").on("change.datetimepicker", function(e){
    	
    	//시작일자 변경될 때만 휴일여부 조회
		if($(this).get(0) === $("#sDate").get(0)) {
			timeCalendarVue.getRestOtMinute();
		}
    	
   		if($("#sDate").val()!='' && $("#eDate").val()!='' && $("#sTime").val()!='' && $("#eTime").val()!='') {
   			var sTime = $("#sTime").val().replace(/:/gi,"");
   			var eTime = $("#eTime").val().replace(/:/gi,"");
   			//restWorkMinute
       		timeCalendarVue.overtime = timeCalendarVue.calcMinute(moment(timeCalendarVue.workday).format('YYYYMMDD'), sTime, eTime);
   			if(timeCalendarVue.targets['${empNo}'] != null && timeCalendarVue.targets['${empNo}'].restWorkMinute != null){
   		
   				console.log(timeCalendarVue.targets['${empNo}']);
   				console.log(timeCalendarVue.overtime);
   				timeCalendarVue.otTime = timeCalendarVue.overtime.calcMinute;
	       		if( timeCalendarVue.targets['${empNo}'].restWorkMinute >= timeCalendarVue.overtime.calcMinute){
	       			timeCalendarVue.isOtUse = false;
	       		}else{
	       			//잔여소정근로시간보다 근무시간이 더 클 경우 연장근로시간에 대해 대체휴가 탭을 활성화 한다. 
	       			timeCalendarVue.otTime = timeCalendarVue.overtime.calcMinute - timeCalendarVue.targets['${empNo}'].restWorkMinute;
	       			timeCalendarVue.isOtUse = true;
	       		}
   			}else{
   				timeCalendarVue.otTime = timeCalendarVue.overtime.calcMinute;
       			timeCalendarVue.isOtUse = true;
   			}
       		
   		}
    });
   	
	$('#inOutChangeModal').on('hidden.bs.modal',function(){
		$(this).find("input,select,textarea").val('').end();
		$(this).find(".needs-validation").removeClass('was-validated');
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

