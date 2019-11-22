<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/websocket.ftl">
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
</head>
<body>
	<!-- loading image start -->
    <!-- loading image end -->
	<#include "/navTop.ftl">
    <div class="wrapper">
        <#include "/navLeft.ftl">
        <#include "/inbox.ftl">
        <div id="content">
        	<div id="loading" class="dim" style="display:none;" >
                <i class="fa fa-spinner fa-pulse fa-5x fa-fw"></i>
                <span class="sr-only">Loading...</span>
            </div>
            <!-- alert modal start -->
			<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" style="z-index:1600;">
		        <div class="modal-dialog " role="document">
		            <div class="modal-content">
		                <!-- <div class="modal-header">
		    	                <h5 class="modal-title"></h5>
		    	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		    	                    <span aria-hidden="true">&times;</span>
		    	                </button>
		    	            </div> -->
		                <div class="modal-body">
		                    <p id="alertText" class="text-center"></p>
		                </div>
		                <div class="modal-body text-center">
		                    <button type="button" class="btn btn-default" data-dismiss="modal">확인</button>
		                </div>
		            </div>
		        </div>
		    </div>
		    <!-- alert modal end -->
		    
		    <!-- 알림에서 근무 계획 작성 시 어느 화면에서나 호출하기 위함  -->
		    <!-- 근무 계획 작성 리스트 start -->
		    <div class="modal fade" id="planWorkDayModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
		        <div class="modal-dialog modal-lg" role="document">
		            <div class="modal-content rounded-0">
		                <div class="modal-header">
		                    <h5 class="modal-title">근무제 선택</h5>
		                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		                        <span aria-hidden="true">&times;</span>
		                    </button>
		                </div>
		                <div class="modal-body">
		                	<div class="modal-app-wrap">
			                    <p>근무 계획을 작성할 근무제를 선택하세요.</p>
			                    <div class="mt-3">
			                       	<div>
			                            <ul class="list-group select-work-list">
			                                <li class="list-group-item" v-for="(p, pIdx) in planFlexitimeList" @click="selectedPlanFlexitime(pIdx)">
			                                    <span :class="['tag ' + p.workTypeCd]">{{p.workTypeNm}}</span>
			                                    <div class="title">{{p.flexibleNm}}</div>
			                                    <div class="desc" v-if="p.sYmd && p.eYmd">
			                                    	근무기간: {{moment(p.sYmd).format('YYYY-MM-DD')}} ~ {{moment(p.eYmd).format('YYYY-MM-DD')}}
			                                    </div>
			                                </li>
			                            </ul>
			                        </div>
				                </div>
			                </div>
			                <div class="btn-wrap text-center">
			                    <button type="button" class="btn btn-secondary  rounded-0" data-dismiss="modal">취소</button>
			                    <button type="button" id="planWorkDayBtn" class="btn btn-default rounded-0" style="display:none;" @click="planWorkDay(selectedPlanFlexitime.flexibleEmpId)">작성하기</button>
			                </div>
		                </div>
		            </div>
		        </div>
		    </div>
		    <!-- 근무 계획 작성 리스트 modal end -->
		    
            <#include "/${pageName}.ftl">
            <!-- <#include "/footer.ftl"> -->
        </div>
    </div>
    <script type="text/javascript">
	    $(function () {
	        $('#sidebarCollapse').on('click', function () {
	            $('#sidebar').toggleClass('active');
	        });
	
	    });
	    
		var modalVue = new Vue({
			el: "#planWorkDayModal",
		    data : {
		    	flexibleEmpId: '',
		    	selectedFlexibleStd: {},
		    	planFlexitimeList: [] //근무계획을 작성할 유연근무제 리스트
		    },
		    mounted: function(){
		    	<#if pageName=='workCalendar' && calendar?? && calendar!='' && calendar?exists>
		    		if('${calendar}'=='workMonthCalendar') {
			    		this.getPlanFlexitimeList('Y');
			    	}
	    		</#if>
		    		
		    },
		    methods: {
	         	getPlanFlexitimeList : function(isCalendarPage){ //근무계획을 작성할 근무제 리스트
		         	var $this = this;
	         		var today = '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}';
	         	
			    	Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/plan/list?ymd="+today,
						type: "GET",
						contentType: 'application/json',
						//data: param,
						dataType: "json",
						success: function(data) {
							$this.planFlexitimeList = [];
							if(data.status=='OK' && data.flexibleList!=null && data.flexibleList.length>0) {
								$this.planFlexitimeList = data.flexibleList;
								
				    			//근무 계획을 작성할 근무제 리스트가 1개 이면 바로 근무 계획 작성화면으로 이동
								//여러 개이면 리스트 팝업 보여줌.
								if($this.planFlexitimeList.length==1) {
									var flexibleEmpId;
									$this.planFlexitimeList.map(function(f){
										flexibleEmpId = f.flexibleEmpId;
									});
									
									if(isCalendarPage=='Y') {
										$("#planBtn").bind('click', function(){
											$this.planWorkDay(flexibleEmpId);
										});
									} else {
										$this.planWorkDay(flexibleEmpId);
									}
									
								} else {
									
									if(isCalendarPage=='Y') {
										$("#planBtn").bind('click', function(){
											$("#planWorkDayModal").modal("show"); 
										});
									} else {
										$("#planWorkDayModal").modal("show"); 
									}
									
								}
				    			
								if(isCalendarPage=='Y')
									$("#planBtn").show();
								
							}
						},
						error: function(e) {
							console.log(e);
							$this.planFlexitimeList = [];
						}
					});
		        },
	         	selectedPlanFlexitime: function(idx){
					var $this = this;
	         		
	         		$("#planWorkDayModal .list-group-item").not(idx).removeClass("active");
	         		$("#planWorkDayModal .list-group-item").eq(idx).addClass("active");
	         		
	         		//근무 계획을 작성할 근무제 선택
	         		$this.selectedPlanFlexitime = $this.planFlexitimeList[idx];
	         		$("#planWorkDayBtn").show();
	         	},
	         	planWorkDay: function(flexibleEmpId){
	         		location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Day&flexibleEmpId='+flexibleEmpId;
	         	}
		    }
		});
    </script>
</body>

</html>
