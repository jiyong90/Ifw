<div id="approval" v-cloak>
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
                            <!--  
                            <template v-if="appl.applCd=='ELAS'">
                            <div class="accordion-wrap inner-wrap">
                                <ul id="accordion" class="accordion">
                                    <li>
                                        <div class="link">2019.06.18 ~ 2019.06.18<i class="ico arrow-down"></i></div>
                                        <div class="submenu">
                                            <ul class="all-time-wrap">
                                                <li>
                                                    <span class="title">근무시간</span>
                                                    <span class="time bold">40</span>
                                                </li>
                                                <li>
                                                    <div class="total">
                                                        <span class="title">연장합산</span>
                                                        <span class="time bold">40</span>
                                                    </div>
                                                    <ul class="time-list">
                                                        <li>
                                                            <span class="title">조출시간</span>
                                                            <span class="time">8</span>
                                                        </li>
                                                        <li>
                                                            <span class="title">잔업시간</span>
                                                            <span class="time">20</span>
                                                        </li>
                                                        <li>
                                                            <span class="title">휴일시간</span>
                                                            <span class="time">12</span>
                                                        </li>
                                                    </ul>
                                                </li>
                                            </ul>
                                            <p class="title time-desc-title">출,퇴근시간</p>
                                            <ul class="time-desc-wrap">
                                                <li>
                                                    <div class="date">2019.06.18(금) 09:00~18:00</div>
                                                    <ul class="time-desc">
                                                        <li><span class="title">근무시간</span>8시간</li>
                                                        <li><span class="title">조출시간</span>1시간</li>
                                                        <li><span class="title">잔업시간</span>1시간</li>
                                                        <li><span class="title">휴일시간</span>1시간</li>
                                                    </ul>
                                                </li>
                                            </ul>
                                        </div>
                                    </li>
                        		</ul>
                        	</div>
                            </template>
                            -->
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
                    <template v-if="appl.otCanApplId!=null&&appl.otCanApplId!=undefined&&appl.otCanApplId!=''">
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
                            <div class="inner-wrap" v-show="appl.holidayYn=='Y' && (appl.subYn=='Y'||appl.payTargetYn=='Y')">
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
                                <div class="sub-desc">*해당일 근무시간은 {{moment(sub.workSDate).format('HH:mm')}}~{{moment(sub.workEDate).format('HH:mm')}} 입니다.</div>
                                </template>
                            </div>
                            <div class="inner-wrap" v-if="appl.cancelReason">
                                <div class="title">취소사유</div>
                                <div class="desc">
                                	{{appl.cancelReason}}
                                </div>
                            </div>
                            <hr class="bar">
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
    <!-- 연장근무신청 상세보기 modal end -->
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
                                        	{{moment(appl.planSdate).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.planEdate">
                                        	{{moment(appl.planEdate).format('YYYY-MM-DD HH:mm')}}
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
                                        	{{moment(appl.entrySdate).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.entryEdate">
                                        	{{moment(appl.entryEdate).format('YYYY-MM-DD HH:mm')}}
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
                                        	{{moment(appl.chgSdate).format('YYYY-MM-DD HH:mm')}}
                                        	</template>
                                        </span>
                                        <span class="ml-1 mr-1">~</span>
                                        <span class="end-date">
                                        	<template v-if="appl.chgEdate">
                                        	{{moment(appl.chgEdate).format('YYYY-MM-DD HH:mm')}}
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
	<div class="container-fluid">
		<p class="page-title">결재 알림</p>
		<ul class="nav approval-wrap nav-pills" role="tablist">
            <li class="nav-item">
                <a href="#" class="nav-link active" id="appl_type_request" data-toggle="pill" @click="getApprovalList('01')" role="tab"
                    aria-controls="pills-home" aria-selected="true">신청서상태</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link" id="appl_type_pending" data-toggle="pill" @click="getApprovalList('02')" role="tab"
                    aria-controls="pills-profile" aria-selected="false">미결함</a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link" id="appl_type_complete" data-toggle="pill" @click="getApprovalList('03')" role="tab"
                    aria-controls="pills-contact" aria-selected="false">기결함</a>
            </li>
        </ul>
		<template v-if="apprList.length>0">
		<div class="row no-gutters notice-card" v-for="a in apprList">
			<div class="col-12 col-md-6 col-lg-9" @click="viewAppl(a)">
				<div :class="['rounded-circle notice-mark '] + a.applCd">{{a.applNm.substr(0,1)}}</div>
				<div class="inner-wrap">
					<div class="title">{{a.applNm}}</div>
					<div class="desc" v-if="a.appl">
						<span class="sub-title">사용기한</span> 
						<span v-if="a.applCd=='OT'||a.applCd=='OT_CAN'||a.applCd=='SUBS_CHG'">
							{{moment(a.appl.otSdate).format('YYYY.MM.DD')}}~{{moment(a.appl.otEdate).format('YYYY.MM.DD')}}
						</span>
						<span v-else-if="a.applCd=='ENTRY_CHG'">
							{{moment(a.appl.ymd).format('YYYY.MM.DD')}}~{{moment(a.appl.ymd).format('YYYY.MM.DD')}}
						</span>
						<span v-else>
							{{moment(a.appl.symd).format('YYYY.MM.DD')}}~{{moment(a.appl.eymd).format('YYYY.MM.DD')}}
						</span>
						<span class="sub-desc" v-if="a.appl.reason">{{a.appl.reason}}</span>
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
					class="btn btn-block btn-outline btn-approval cancel" @click="approval(a,'reject')">반송</button>
			</div>
			<div class="col-6 col-md-2 col-lg-1 pl-1">
				<button type="button"
					class="btn btn-block btn-outline btn-approval sign" @click="approval(a,'apply')">승인</button>
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
   	var approvalVue = new Vue({
   		el: "#approval",
   		data : {
   			apprList: [],
   			apprType: '', // 승인 or 반려
   			applType: '${applType}', // 신청 내역 조회('01') or 미결('02') or 기결('03')
   			apprOpinion: '',
   			appl: {}, //신청서view 
   			appr: {} //승인할 신청서
   		},
	    mounted: function(){
	    	this.getApprovalList(this.applType); //신청내역 조회
	    },
	    methods : {
	    	getApprovalList: function(applType){
	    		var $this = this;
	    		$this.apprList = [];
	    		$this.applType = applType;
	    		
	    		var param = {
	    			applType: applType
	    		};
	    		
	    		Util.ajax({
					url: "${rc.getContextPath()}/appl",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						if(data.status=='OK' && data!=null && data.apprList!=null) {
							$this.apprList = data.apprList;
						}
					},
					error: function(e) {
						$this.apprList = [];
					}
				});
	    	},
	    	viewAppl: function(appr){
	    		/* if(appr.applCd=='OT') {
	    			//연장근무신청서
	    			this.getOTAppl(appr.applId);
	    		} else if(appr.applCd=='SELE_F' || appr.applCd=='SELE_C') {
	    			//선근제 신청서
	    			this.getFlexibleSeleAppl(appr.applId);
	    		} */
	    		this.appl = appr.appl;
	    		this.appl['applCd'] = appr.applCd;
	    		console.log(this.appl);
	    		
	    		if(appr.applCd=='OT' || appr.applCd=='OT_CAN') {
	    			//연장근무신청서
	    			$("#otAppl").modal("show"); 
	    		} else if(appr.applCd=='SELE_F' || appr.applCd=='SELE_C' || appr.applCd=='ELAS') {
	    			//선근제 신청서
	    			$("#flexibleAppl").modal("show");
	    		} else if(appr.applCd=='ENTRY_CHG') {
	    			//근태 사유서
	    			$("#inOutChangeAppl").modal("show");
	    		}
	    		
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
    			if($this.apprOpinion=='') {
    				saveYn = false;
    				$("#alertText").html("의견을 입력해 주세요.");
    			}
    			
    			if(saveYn) {
    				//$('#apprOpinionModal').modal("hide"); 
    				$("#loading").show();
    				var appr = $this.appr;
	    			var param = appr;
	    			
	    			param['apprOpinion'] = $this.apprOpinion;
	    			
	    			if(appr.applCd=='OT') {
	    				param['ymd'] = moment(appr.appl.ymd).format('YYYYMMDD');
	    				param['otSdate'] = moment(appr.appl.otSdate).format('YYYYMMDDHHmm');
	    				param['otEdate'] = moment(appr.appl.otEdate).format('YYYYMMDDHHmm');
	    			}else if(appr.applCd=='ENTRY_CHG') {
	    				param['ymd'] = moment(appr.appl.ymd).format('YYYYMMDD');
	    				
	    				if(appr.appl.planSdate!=null && appr.appl.planSdate!=undefined && appr.appl.planSdate!='')
	    					param['planSdate'] = moment(appr.appl.planSdate).format('YYYYMMDDHHmm');
	    				if(appr.appl.planEdate!=null && appr.appl.planEdate!=undefined && appr.appl.planEdate!='')
	    					param['planEdate'] = moment(appr.appl.planEdate).format('YYYYMMDDHHmm');
	    				if(appr.appl.entrySdate!=null && appr.appl.entrySdate!=undefined && appr.appl.entrySdate!='')
	    					param['entrySdate'] = moment(appr.appl.entrySdate).format('YYYYMMDDHHmm');
	    				if(appr.appl.entryEdate!=null && appr.appl.entryEdate!=undefined && appr.appl.entryEdate!='')
	    					param['entryEdate'] = moment(appr.appl.entryEdate).format('YYYYMMDDHHmm');
	    				if(appr.appl.chgSdate!=null && appr.appl.chgSdate!=undefined && appr.appl.chgSdate!='')
	    					param['chgSdate'] = moment(appr.appl.chgSdate).format('YYYYMMDDHHmm');
	    				if(appr.appl.chgEdate!=null && appr.appl.chgEdate!=undefined && appr.appl.chgEdate!='')
	    					param['chgEdate'] = moment(appr.appl.chgEdate).format('YYYYMMDDHHmm');
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
							} else {
								$("#alertText").html(data.message);
							}
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								$this.getApprovalList();
							});
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
	    	}
	    }
   	});
   	
   	$('[data-dismiss=modal]').on('click', function (e) {
		var $t = $(this),
	        target = $t[0].href || $t.data("target") || $t.parents('.modal') || [];

		$(target).find("textarea").val('').end();
	})
</script>