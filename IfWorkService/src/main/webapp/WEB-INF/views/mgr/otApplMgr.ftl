<div id="content" class="bg-white">
	<div id="otApplMgr" v-cloak>
		<!-- 연장근무신청 modal start -->
	    <div class="container-fluid">
	    	<p class="page-title mb-3">연장근로신청</p>
	    	<div class="row">
	    		<div class="col-6">
	    			<form>
	    				<div class="input-group mb-2">
	                        <input id="searchKeyword" type="text" class="form-control rounded-0" placeholder="검색어를 입력해주세요" aria-label="검색어를 입력해주세요"
	                            aria-describedby="search" @keyup.enter="getTargetList">
	                        <div class="input-group-append">
	                            <button class="btn btn-primary rounded-0" type="button" @click="getTargetList"><i class="fas fa-search"></i></button>
	                        </div>
	                    </div>
	                    <div class="inner-wrap setting except200 over-height">
	                        <div class="function-list-wrap">
	                            <div class="title">연장근로 대상자 선택</div>
	                            <ul class="fun-list">
	                                <li v-for="t in targetList">
	                                    <input type="checkbox" :id="t.sabun" name="otApplTarget" value="" @click="checkTarget(t,$event)" :checked="isCheck(t.sabun)">
	                                    <label :for="t.sabun">
	                                        <span class="group">{{t.orgNm}}</span>
	                                        <span class="num">{{t.sabun}}</span>
	                                        <span class="name">{{t.empNm}}</span>
	                                    </label>
	                                </li>
	                            </ul>
	                        </div>
	                    </div>
	    			</form>
				</div>
	    		<div id="overtimeAppl" class="col-6" v-show="Object.keys(applSabuns).length>0">
	    			<div class="mt-3 mb-3 select-list-wrap">
	    				<template v-for="a in applSabuns">
	                    	<span class="btn btn-outline-secondary rounded-pill mr-1" >{{a.empNm}}</span>
	                    </template>
	                </div>
	    			<form class="needs-validation" novalidate>
		                 <div class="modal-app-wrap">
		                     <div class="inner-wrap">
		                         <div class="desc row">
		                             <div class="col-sm-12 col-md-12 col-lg-2 pr-lg-0">
		                                 <div class="title" id="overtime">연장근로시간</div>
		                                 <span class="time-wrap">
		                                     <i class="fas fa-clock"></i>
		                                     <span class="time" v-if="overtime.calcMinute">{{minuteToHHMM(overtime.calcMinute,'detail')}}</span>
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
		                     <div class="inner-wrap" v-if="holidayYn=='Y'&&payTargetYn">
		                         <div class="title mb-2">휴일대체방법</div>
		                         <div class="desc">
		                             <div class="custom-control custom-radio custom-control-inline">
		                                 <input type="radio" id="subYnN" name="subYn" class="custom-control-input" value="N" :required="holidayYn=='Y'?true:false">
		                                 <label class="custom-control-label" for="subYnN">위로금/시급지급</label>
		                             </div>
		                         </div>
		                     </div>
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
        
	});

   	var otApplMgrVue = new Vue({
   		el: "#otApplMgr",
  		    data : {
  		    	holidayYn: 'N',
  		    	workday: '${today}', //근무일
  		    	reasons: [], //연장/휴일 근로 사유
  		    	subYmds: [], //대체휴일
  		    	overtime: {}, //연장/휴일 근로시간, 휴게시간
  		    	overtimeAppl: {},
  		    	applCode: {}, //신청서 정보
  		    	payTargetYn: false, //수당 지급 대상자
  		    	targetList: [],
  		    	applSabuns: {}
  		    },
  		    watch: {
  		    	applSabuns : function(val, oldVal) {
  		    		var $this = this;
  		    		if(Object.keys(val).length>0) 
  		    			$this.viewOvertimeAppl($this.workday);
  		    	}
  		    },
  		    mounted: function(){
  		    	//팀원 조회
  		    	this.getTargetList();
  		    	
  		    	<#if reasons?? && reasons!='' && reasons?exists >
		    		this.reasons = JSON.parse('${reasons?js_string}');
		    	</#if>
  		    },
  		    methods : {
  		    	getHolidayYn: function(ymd){
  		    		var $this = this;
  		    		$this.holidayYn = 'N';
  		    		
  		    		var i = 0;
  		    		
  		    		var param = {
  		    			ymd : ymd,
  		    			applSabuns : JSON.stringify($this.applSabuns)
  		    		};
  		    		
  		    		Util.ajax({
  		    			url: "${rc.getContextPath()}/calendar/emps",
  						type: "GET",
  						contentType: 'application/json',
  						data: param,
  						dataType: "json",
						success: function(data) {
							//console.log(data);
							if(data!=null && data.status=='OK') {
								$this.holidayYn = data.holidayYn;
							} else {
								
							}
						},
						error: function(e) {
							console.log(e);
							$this.holidayYn = 'N';
						}
					});
  		    		
  		    	},
  		    	getTargetList: function(){ //팀원 조회
  		    		var $this = this;
  		    		
  		    		$("#loading").show();
  		    	
  		    		$this.targetList = [];
  		    		
  		    		var searchKeyword = $("#searchKeyword").val();
  		    		var param = {
  		    			searchKeyword : searchKeyword	
  		    		};
  		    		
  		    		Util.ajax({
						url: "${rc.getContextPath()}/emp/list",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							//console.log(data);
							$("#loading").hide();
							if(data!=null && data.status=='OK') {
								$this.targetList = data.DATA;
							} 
						},
						error: function(e) {
							$("#loading").hide();
							console.log(e);
							$this.targetList = [];
						}
					});
  		    	},
  		    	checkTarget: function(emp, e){
  		    		var $this = this;	 
  		    		
  		    		if(e.target.checked) {
  		    			var applicant = {
  		    				tenantId : emp.tenantId,
  		    				enterCd : emp.enterCd,
  		    				sabun: emp.sabun,
  		    				empNm : emp.empNm,
  		    				orgNm : emp.orgNm
  		    			};
  		    			
  		    			//소정근로 선 소진 여부, 연장근무 가능한지 체크
  		    			this.preCheck(applicant);
  		    			
  		    			//Vue.set($this.applSabuns, emp.sabun, applicant);
  		    		} else {
  		    			
  		    			if($this.applSabuns.hasOwnProperty(emp.sabun)) {
  		    				$this.uncheckTarget(emp.sabun);
  		    			}

  		    		}
  		    	},
  		    	uncheckTarget : function(sabun) {
  		    		Vue.delete(this.applSabuns, sabun);
  		    	},
  		    	isCheck: function(sabun){
  		    		if(this.applSabuns.hasOwnProperty(sabun)) 
  		    			return true;
  		    	},
  		    	viewOvertimeAppl: function(date){
  	         		var $this = this;
  	         		//신청서 정보
  			    	this.getApplCode();
  	         		
  	         		//1시간 값 세팅
					var sYmd = new Date(date);
					var eYmd = new Date(date);

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
					
					//결재라인
					/* 
					var param = {
						d : moment($this.workday).format('YYYYMMDD')
					};
					
					Util.ajax({
						url: "${rc.getContextPath()}/otAppl/line",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							
						},
						error: function(e) {
							console.log(e);
						}
					}); 
					*/
					
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
  								
  								if($this.holidayYn=='Y') {
  								//휴일연장 시간단위
  									if(data.holApplTypeCd!=null && data.holApplTypeCd!=undefined && data.holApplTypeCd!=''){
  	  									var holApplTypeCd = Number(data.holApplTypeCd);
  										$('#sTime').datetimepicker('stepping', holApplTypeCd);
  										$('#eTime').datetimepicker('stepping', holApplTypeCd);
  	  								}
  								} else {
  								//연장 시간단위
  									if(data.timeUnit!=null && data.timeUnit!=undefined && data.timeUnit!=''){
  	  									var timeUnit = Number(data.timeUnit);
  										$('#sTime').datetimepicker('stepping', timeUnit);
  										$('#eTime').datetimepicker('stepping', timeUnit);
  	  								}
  								}
  								
  							}
  						},
  						error: function(e) {
  							$this.applCode = {};
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
  	         	preCheck : function(emp){ //소정근로 선 소진 여부, 연장근무 가능한지 체크
  	         		var $this = this;
  	         	
  	         		$("#loading").show();
  	         		
  	         		var ymd = moment($this.workday).format('YYYYMMDD');
  	         	
  	         		var param = {
  	         			ymd: ymd,
  	         			sabun : emp.sabun,
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
								//연장근로신청 대상자에 추가.
								Vue.set($this.applSabuns, emp.sabun, emp);
								
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
  	         			
  	         			var holidayYn = $this.holidayYn;
  	         			
  	         			//신청하려는 ot시간이 소정근무시간에 해당되지 않는지 체크
  	         			var sDate = $("#sDate").val().replace(/-/gi,"");
			   			var eDate = $("#eDate").val().replace(/-/gi,"");
			   			var sTime = $("#sTime").val().replace(/:/gi,"");
			   			var eTime = $("#eTime").val().replace(/:/gi,"");
			   			
			   			var otSdate = moment(sDate+' '+sTime).format('YYYYMMDD HHmm');
			         	var otEdate = moment(eDate+' '+eTime).format('YYYYMMDD HHmm');
			         	
			         	var applCode = $this.applCode;
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
		       			
		     			if(inShm!=null && inEhm!=null && (moment(otSdate).diff(inShm)<0 || moment(otEdate).diff(inEhm)>0)) {
		     				isValid = false;
		       				var shm =  moment(inShm).format('HH:mm');
		       				var ehm =  moment(inEhm).format('HH:mm');
		       				msg = '근무 가능 시간은 '+shm+'~'+ehm+' 입니다.';
		       				$("#sTime").val('');
  	  	         			$("#eTime").val('');
		   				}
		     				
		     			//var time = Number(moment(otEdate).diff(otSdate,'minutes'));
		     			var time = $this.overtime.calcMinute;
		       			// 신청 시간 단위
		       			if(applCode.timeUnit!=null && applCode.timeUnit!=undefined && applCode.timeUnit!='') {
		       				var timeUnit = Number(applCode.timeUnit);
		       			
			       			if(time % timeUnit != 0) {
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
			         	if(holidayYn=='Y') {
			         		if(applCode.holApplTypeCd!=null && applCode.holApplTypeCd!=undefined && applCode.holApplTypeCd!='') {
			         			var holApplTypeCd = Number(applCode.holApplTypeCd);
			         			
			         			//휴게시간 차감
			         			/* if($this.overtime.breakMinute!=null && $this.overtime.breakMinute!=undefined && $this.overtime.breakMinute!='')
			         				time = time - Number($this.overtime.breakMinute); */
			         			
			         			if(time % holApplTypeCd != 0) {
			         				isValid = false;
				       				msg = '휴일 근무시간은 '+minuteToHHMM(holApplTypeCd,'detail')+' 단위로 신청 가능합니다.';
				       				$("#sTime").val('');
		  	  	         			$("#eTime").val('');
			         			}
			         			
			         			//최대 신청 시간을 넘지 않는지 체크
			         			if(applCode.holMaxMinute!=null && applCode.holMaxMinute!=undefined && applCode.holMaxMinute!='') {
				         			var holMaxMinute = Number(applCode.holMaxMinute);
				         			
				         			if(time > holMaxMinute) {
				         				isValid = false;
					       				msg = '근무시간은 최대 '+minuteToHHMM(holMaxMinute,'detail')+' 까지 신청 가능합니다.';
					       				$("#sTime").val('');
			  	  	         			$("#eTime").val('');
				         			}
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
  	         	otAppl : function(otSdate, otEdate){ //연장근무신청
  	         		var $this = this;
  	         	
  	         		var holidayYn = $this.holidayYn;
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
					
					if($this.overtimeAppl.hasOwnProperty('otCanAppl') && $this.overtimeAppl.otCanAppl!=null && $this.overtimeAppl.otCanAppl!=undefined) {
						var otCanAppl = $this.overtimeAppl.otCanAppl;
						param = {
	  	         			applCd : otCanAppl.applCd,
	  	         			applId : otCanAppl.applId
	  	         		};
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
								$("#alertText").html("회수되었습니다.");
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$("#confirmModal").modal("hide");
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
						
  	         	}
  	         }
   	});

   	//날짜,시간 변경 시 근로시간 계산
   	$('#sDate, #eDate, #sTime, #eTime').off("change.datetimepicker").on("change.datetimepicker", function(e){
   		if($("#sDate").val()!='' && $("#eDate").val()!='' && $("#sTime").val()!='' && $("#eTime").val()!='') {
   			var sTime = $("#sTime").val().replace(/:/gi,"");
   			var eTime = $("#eTime").val().replace(/:/gi,"");
   			
   			var date = moment($("#sDate").val()).format('YYYYMMDD');
   			
   			//시작일자 변경될 때만 휴일여부 조회
   			if($(this).get(0) === $("#sDate").get(0))
   				otApplMgrVue.getHolidayYn(date);
   			
   			otApplMgrVue.overtime = otApplMgrVue.calcMinute(date, sTime, eTime);
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

