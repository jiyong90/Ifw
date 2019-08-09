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
                                <div class="desc row">
                                    <div class="col-sm-12 col-md-12 col-lg-2">
                                        <div class="title">연장근로시간</div>
                                        <span class="time-wrap">
                                            <i class="fas fa-clock"></i>
                                            <span class="time">1시간</span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control  datetimepicker-input form-control-sm mr-2" id="sDate" data-toggle="datetimepicker" data-target="#sDate" placeholder="연도/월/일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sTime" data-toggle="datetimepicker" data-target="#sTime" autocomplete="off" required>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control  datetimepicker-input form-control-sm mr-2" id="eDate" data-toggle="datetimepicker" data-target="#eDate" placeholder="연도/월/일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eTime" data-toggle="datetimepicker" data-target="#eTime" autocomplete="off" required>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="reasonCd">사유구분</label>
                                    <select id="reasonCd" class="form-control" required>
                                        <option value="" disabled selected hidden>사유를 선택해주세요.</option>
                                        <option value="1">일반작업</option>
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
		$('#sDate, #eDate').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
       
        $('#sTime, #eTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko'
        });
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
  		    	workday: '',
  		    	dayResults: {}
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
							if(data!=null) {
								if(data.hasOwnProperty('dayResults'))
									$this.dayResults = data.dayResults;
								console.log($this.dayResults);
								$this.viewDayResults(ymd, data);
							}
						},
						error: function(e) {
							console.log(e);
						}
					});
  	         	},
  	         	viewDayResults: function(ymd, data){
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
  	         			if(data.hasOwnProperty('dayResults')) {
  	         				$.each(data.dayResults, function(k, v){
  	         					v.map(function(vMap){
  	         						if(vMap.taaCd!='') {
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
  										classNames.push(vMap.timeTypeCd);
  		  	         					
  	  	  	         					var result = {
  	  	  	   	         					id: 'TIME.'+vMap.timeTypeCd,
  	  	  	   	         					title: vMap.timeTypeNm,
  	  	  	  								start: vMap.sDate,
  	  	  	  	  		  		        	end: vMap.eDate,
  	  	  	  	  		  		        	editable: false,
  	  	  	  		  		        		classNames: classNames
  	  	  	    	         			};
  	  	  	         					
  	  	  	    	         			$this.addEvent(result); 
  		  	         				}
  	         					});
  	         				});
  	         			}
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
								//1시간 값 세팅
								var sYmd = new Date(info.date);
								var eYmd = new Date(info.date);
								eYmd.setHours(eYmd.getHours()+1);
								$("#sDate").val(moment(sYmd).format('YYYY-MM-DD'));
								$("#eDate").val(moment(eYmd).format('YYYY-MM-DD'));
								$("#sTime").val(moment(sYmd).format('HH:mm'));
								$("#eTime").val(moment(eYmd).format('HH:mm'));
								
								//사용할 근무제 팝업 띄우기
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
  	         		var $this = this;
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
  	         			//신청하려는 ot시간이 소정근무시간에 해당되지 않는지 체크
  	         			var otSdate = moment($("#sDate").val()+' '+$("#sTime").val()).format('YYYY-MM-DD HH:mm');
  	  	         		var otEdate = moment($("#eDate").val()+' '+$("#eTime").val()).format('YYYY-MM-DD HH:mm');
  	         			
  	  	         		var isBase = false;
  	  	         		var baseWork = {};
  	  	         		var dayResults = $this.dayResults[moment($this.workday).format('YYYYMMDD')];
  	  	         		dayResults.map(function(dayResult){
  	  	         			if(dayResult.timeTypeCd == 'BASE'){
  	  	         				baseWork = dayResult;
  	  	         			}
  	  	         		});
  	         			
  	  	         		if(baseWork!=null && Object.keys(baseWork).length>0) {
	  	         			var baseSdate = moment(baseWork.sDate).format('YYYY-MM-DD HH:mm');
	  	         			var baseEdate = moment(baseWork.eDate).format('YYYY-MM-DD HH:mm');
	  	         			if(moment(baseSdate).diff(otSdate)<=0 && moment(otSdate).diff(baseEdate)<=0 
	  	         					|| moment(baseSdate).diff(otEdate)<=0 && moment(otEdate).diff(baseEdate)<=0 )
	  	         				isBase = true;
  	  	         		}
  	         			
  	  	         		if(!isBase) {
  	         				$this.otAppl(otSdate, otEdate);
  	  	         		} else {
  	  	         			$("#alertText").html("소정근로 시간을 포함하여 신청할 수 없습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         			$("#sTime").val('');
	  	  	         			$("#eTime").val('');
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
  	  	         		}
  	         		}
  	         		
  	         	},
  	         	otAppl : function(otSdate, otEdate){ //연장근무신청
  	         		var $this = this;
  	         		var param = {
        				flexibleStdMgrId : calendarTopVue.flexibleStd.flexibleStdMgrId,
        				workTypeCd : calendarTopVue.flexibleStd.workTypeCd,
        				ymd: moment($this.workday).format('YYYYMMDD'),
        				otSdate : moment(otSdate).format('YYYYMMDDHHmm'),
        				otEdate : moment(otEdate).format('YYYYMMDDHHmm'),
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
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									location.reload();
								});
							} else {
								$("#alertText").html("확인요청 시 오류가 발생했습니다.");
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

