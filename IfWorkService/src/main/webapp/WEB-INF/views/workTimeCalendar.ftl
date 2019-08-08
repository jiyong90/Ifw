<div id="timeCalendar" class="calendar-wrap" v-cloak>
	<!-- modal start -->
        <div class="modal fade show" id="overtimeApplModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content rounded-0">
                    <div class="modal-header">
                        <h5 class="modal-title">연장근로신청</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form class="needs-validation" novalidate>
                            <div class="modal-app-wrap">
                                <div class="inner-wrap">
                                    <div class="desc  clearfix" >
                                        <div class="float-sm-left float-md-left float-lg-left">
                                            <div class="title">연장근로시간</div>
                                            <span class="time-wrap">
                                                <i class="fas fa-clock"></i>
                                                <span class="time">1시간</span>
                                            </span>
                                        </div>
                                        <div class="form-group mt-sm-1 mt-md-3 mt-lg-3 xl-mt-3 float-sm-right float-md-right float-lg-right clearfix">
                                            <div class="form-row float-md-none float-lg-left">
                                                <div class="col-7">
                                                    <input type="text" class="form-control form-control-sm mr-2" id="sDate" value="" placeholder="" required>
                                                	<span class="input-group-addon">
								                        <span class="glyphicon glyphicon-time"></span>
								                    </span>
                                                </div>
                                                <div class="col-5">
                                                    <input type="text" class="form-control form-control-sm mr-2" id="sTime" value="" placeholder="" required>
                                                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-time"></span>
								                    </span>
                                                </div>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-1 pr-2 mt-1">~</span>
                                            <div class="form-row float-md-none float-lg-right">
                                                <div class="col-7">
                                                    <input type="text" class="form-control form-control-sm mr-2" id="eDate" value="" placeholder="" required>
                                                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-time"></span>
								                    </span>
                                                </div>
                                                <div class="col-5">
                                                    <input type="text" class="form-control form-control-sm mr-2" id="eTime" value="" placeholder="" required>
                                                    <span class="input-group-addon">
								                        <span class="glyphicon glyphicon-time"></span>
								                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row no-gutters">
                                    <div class="form-group col-12">
                                        <label for="reasonCd">사유구분</label>
                                        <select id="reasonCd" class="form-control" value="" required>
                                            <option value="" disabled selected hidden>사유를 선택해주세요.</option>
                                            <option value="">일반작업</option>
                                            <option>...</option>
                                        </select>
                                    </div>
                                    <div class="form-group col-12">
                                        <label for="reason">설명</label>
                                        <textarea class="form-control" id="reason" rows="3"
                                            placeholder="팀장 확인 시에 필요합니다." required></textarea>
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
        <!-- modal end -->
    <div id='calendar-container'>
		<full-calendar ref="fullCalendar" :header="header" :defaultview="view" :defaultdate="workday" :nowindicator="t" @update="renderCallback" @datesrender="datesRenderCallback" @dateclick="dateClickCallback" @select="selectCallback" @eventclick="eventClickCallback"></full-calendar>
    </div>
</div>
<script type="text/javascript">
	$(function () {
	    $('#sDate').datetimepicker({});
	    $('#eDate').datetimepicker({});
	    $('#sTime').datetimepicker({});
	    $('#eTime').datetimepicker({});
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
  		    	workday: ''
  		    },
  		    mounted: function(){
  		    	<#if workday?? && workday!='' && workday?exists >
  		    		this.workday = moment('${workday}').format('YYYY-MM-DD');
  		    	<#else>
  		    		this.workday = '${today}';
  		    	</#if>
  		    	
  		    	this.getFlexibleDayInfo(this.workday);
  		    	
  		    	//근무일 화면 전환
         		$("#workRangeInfo").show();
         		$("#flexibleDayInfo").show();
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'timeGridDay') { //month change
  		    			var ymd = moment(calendar.getDate()).format('YYYYMMDD');
  		    			
  		    			$this.getFlexibleDayInfo(ymd);
  		    			$this.getDayResults(ymd);
	   		    		
	  		    	}
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
					
  		    		
  		    	},
  		    	eventClickCallback : function(info){
  		    	},
  		    	dateClickCallback : function(info){
  		    		this.getOtAppl(info);
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
							$this.viewWorkDayResult(ymd, data);
						},
						error: function(e) {
							console.log(e);
						}
					});
  	         	},
  	         	viewWorkDayResult: function(ymd, data){
  	         		var $this = this;
  	         		var classNames = [];
  	         		
  	         		if(data!=null) {
  	         			//출퇴근 타각 표시
  	         			if(data.hasOwnProperty('entry')) {
  	         				var entry = data.entry;
  	         				
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
  	         			if(data.hasOwnProperty('workResult')) {
  	         				data.workResult.map(function(w){
	  	         				if(w.taaCd!='') {
	  	         					//근태
	  	         					classNames = [];
									classNames.push('TAA');
	  	         					
	  	         					var result = {
  	  	   	         					id: 'TAA.'+w.taaCd,
  	  	   	         					title: w.taaNm,
  	  	  								start: w.sDate,
  	  	  	  		  		        	end: w.eDate,
  	  	  	  		  		        	editable: false,
  	  	  		  		        		classNames: classNames
  	  	    	         			};
	  	  	         					
	  	  	    	         		$this.addEvent(result); 
	  	         				} else {
	  	         					//근무
	  	         					classNames = [];
									classNames.push(w.timeTypeCd);
	  	         					
  	  	         					var result = {
  	  	   	         					id: 'TIME.'+w.timeTypeCd,
  	  	   	         					title: w.timeTypeNm,
  	  	  								start: w.sDate,
  	  	  	  		  		        	end: w.eDate,
  	  	  	  		  		        	editable: false,
  	  	  		  		        		classNames: classNames
  	  	    	         			};
  	  	         					
  	  	    	         			$this.addEvent(result); 
	  	         				}
  	         				});
  	         			}
  	         		}
  	         	},
  	         	getOtAppl : function(info){
  	         		var $this = this;
  	         		var calendar = this.$refs.fullCalendar.cal;
  	         		
  	         		var event = calendar.getEventById('TIME.OT');
	         		if(event!=null) {
	         			//신청서 있으면 view
	         			$("#alertText").html("기신청된 연장근무신청서가 존재합니다.");
	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	         			$("#alertModal").off('hidden.bs.modal');
	  	         		});
	         		} else{
	         			//없으면
	  	         		$this.preCheck(info);
	         		}
  	         		
  	         	},
  	         	preCheck : function(info){ //소정근로 선 소진 여부, 연장근무 가능한지 체크
  	         		var $this = this;
  	         		
  	         		var param = {
  	         			ymd: moment($this.workday).format('YYYYMMDD'),
  	         			workTypeCd: calendarTopVue.flexibleStd.workTypeCd 
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
								//사용할 근무제 팝업 띄우기
								var eYmd = new Date(info.date);
								eYmd.setHours(eYmd.getHours()+1);
								
								
								$("#overtimeApplModal").modal("show"); 
							} else {
								$("#alertText").html(data.message);
			  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	         			$("#alertModal").off('hidden.bs.modal');
			  	         		});
							}
						},
						error: function(e) {
							console.log(e);
						}
					});
  	         	},
  	         	validateOtAppl : function(){
  	         		var applYn = true;
  	         		var forms = document.getElementById('overtimeApplModal').getElementsByClassName('needs-validation');
  	         		var validation = Array.prototype.filter.call(forms, function(form) {
  	         			if (form.checkValidity() === false) {
  	         				applYn = false;
  	         				event.preventDefault();
  	         		        event.stopPropagation();
  	         			}
  	         			form.classList.add('was-validated');
  	         		});
  	         		
  	         		if(applYn) {
  	         			//소정근무시간에 해당되지 않는지 체크
  	         			
  	         			
  	         			this.otAppl();
  	         		}
  	         		
  	         	},
  	         	otAppl : function(){ //연장근무신청
  	         		var sYmd = moment($("#sDate").val()+''+$("#sTime").val()).format('YYYYMMDDHHmm');
  	         		var eYmd = moment($("#eDate").val()+''+$("#eTime").val()).format('YYYYMMDDHHmm');
  	         		
  	         		var param = {
        				flexibleStdMgrId : calendarTopVue.flexibleStd.flexibleStdMgrId,
        				workTypeCd : calendarTopVue.flexibleStd.workTypeCd,
	   		    		sYmd : sYmd,
	   		    		eYmd : eYmd,
	   		    		reasonCd : $("#reasonCd").val(),
	   		    		reason: $("#reason").val()
	   		    	};
  	         		
  	         		Util.ajax({
						url: "${rc.getContextPath()}/otAppl/request",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							if(data!=null && data.status=='OK') {
								$("#alertText").html("확인요청 되었습니다.");
							} else {
								$("#alertText").html("저장 시 오류가 발생했습니다.");
							}
							$("#alertModal").on('hidden.bs.modal',function(){
								location.reload();
							});
	  	  	         		$("#alertModal").modal("show"); 
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("저장 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					}); 
  	         	}
  		    }
   	});

	$('[data-dismiss=modal]').on('click', function (e) {
		var $t = $(this),
	        target = $t[0].href || $t.data("target") || $t.parents('.modal') || [];

	  	$(target).find("input,select,textarea").val('').end();
	  	$(target).find(".needs-validation").removeClass('was-validated');
	  	
	});
</script>

