<#include "/calendar.ftl">
<div id="workDayPlan" v-cloak>
    <div id="sub-nav" class="container-fluid">
        <form action="">
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
                </div>
            </div>
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
        </form>
    </div>
    <div class="container-fluid">
        <div class="row no-gutters">
            <div class="col-12 col-md-3 pr-md-3">
                <div id="flexibleDayPlan" class="white-box-wrap full-height mb-3">
	                <div class="work-plan-wrap">
	                    <div class="big-title" v-if="flexibleEmp">
	                    	{{moment(flexibleEmp.sYmd).format("YYYY년 M월 D일")}} ~ {{moment(flexibleEmp.eYmd).format("YYYY년 M월 D일")}}({{moment(flexibleEmp.eYmd).diff(flexibleEmp.sYmd, 'days')+1}}일)
	                    </div>
	                    <div class="inner-wrap">
	                        <div class="main-title">근무시간표</div>
	                        <div class="main-desc">기본근무시간표</div>
	                    </div>
	                    <div class="time-input-form">
	                        <div class="form-row no-gutters">
	                            <div class="form-group col-5">
	                                <label for="startTime">출근시간</label>
	                                <input type="time" class="form-control" id="startTime" placeholder="" @change="changeWorkTime">
	                            </div>
	                            <div class="form-group col-2 text-center">
	                                <lable></lable>
	                                <span>~</span>
	                            </div>
	                            <div class="form-group col-5">
	                                <label for="endTime">퇴근시간</label>
	                                <input type="time" class="form-control" id="endTime" placeholder="" @change="changeWorkTime">
	                            </div>
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
	                <div class="btn-wrap mt-5">
	                    <button type="button" class="btn btn-apply btn-block btn-lg" @click="saveWorkDayResult">저장</button>
	                </div>
	            </div>
            </div>
            <div class="col-12 col-md-9">
                <div class="calendar-wrap">
                    <div id='calendar-container'>
                		<!-- <full-calendar ref="fullCalendar" :header="header" :events="events" :eventsources="eventSources" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback" ></full-calendar> -->
                		<full-calendar ref="fullCalendar" :header="header" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback" ></full-calendar>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
   	var workDayPlanVue = new Vue({
   		el: "#workDayPlan",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
  		    	header: {
  		    		left: 'prev,next today',
			        center: 'title',
			        right: ''
  		    	},
  		    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
  		    	workTermTime: {}, //선택한 기간의 근무제 정보
  		    	selectedFlexitime: {}, //적용한 근무제
  		    	flexibleEmp: {},
  		    	selectedWorkday: {},
  		    	dayResult: {}, //상세 근무계획
  		    	dayWorks: [], //저장된 상세 근무계획
  		    	//events: [],
  		    	eventSource: {},
  		    	eventSources: [
  		    		/* {
  		    	      events: [  
  		    	        {
  		    	          start     : '2019-07-19',
  		    	          rendering: 'background'
  		    	        }
  		    	      ],
  		    	      backgroundColor: 'green',
  		    	      borderColor: 'red',
  		    	      textColor: 'yellow'
  		    	    } */
  		    	]
  		    },
  		    mounted: function(){
  		    	//신청서 조회
				<#if flexibleAppl?? && flexibleAppl!='' && flexibleAppl?exists >
  		    		this.flexibleEmp = JSON.parse("${flexibleAppl?js_string}");
  		    		
  		    		//근무시간 조회
  		    		this.getWorkDayResult();
  		    	</#if>
  		    	
  		    	//this.getWorkRangeInfo(this.today);
  		    	this.getWorkDayInfo(this.today);
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'dayGridMonth') { //month change
	   		    		var param = {
		   		    		ym : moment(calendar.getDate()).format('YYYYMM')
		   		    	};
	   		    		
	   		    		Util.ajax({
							url: "${rc.getContextPath()}/calendar",
							type: "GET",
							contentType: 'application/json',
							data: param,
							dataType: "json",
							success: function(data) {
								//회사 캘린더(휴무일 포함)
								if(data.companyCalendar!=null) {
									data.companyCalendar.map(function(cal){
										if(cal.hasOwnProperty("holidayYmd") && cal.holidayYmd!='') {
											$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").css({"color":"#FF0000"});
											$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").prepend(cal.holidayNm);
										}
									});
								}
					  	         	
								//상세 계획 입력 화면 전환
				         		$this.viewWorkPlan();
							},
							error: function() {
								
							}
						});
	   		    		
	  		    	}
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
  		    		var $this = this;
  		    	
  		    		var isWorkDay = false;
  		    		
  		    		var selSymd = info.startStr;
  		    		var selEymd = new Date(info.endStr);
  		    		selEymd.setDate(selEymd.getDate()-1);
  		    		selEymd = moment(selEymd).format('YYYY-MM-DD');
  		    		
  		    		//유연근무제 신청기간 내 
  		    		//신청 기간 체크, 현재는 오늘 날짜 이후만 수정할 수 있도록 함
  		    		if(moment($this.flexibleEmp.sYmd).diff(selSymd)<=0 && moment(selSymd).diff($this.flexibleEmp.eYmd)<=0
  		    				&& moment($this.flexibleEmp.sYmd).diff(selEymd)<=0 && moment(selEymd).diff($this.flexibleEmp.eYmd)<=0
  		    				&& moment($this.today).diff(selSymd)<0 && moment($this.today).diff(selEymd)<0) {
	  		    		$this.selectedWorkday = {
		    				start: selSymd,
		    				end: selEymd
		    			};
	  		    		$this.selectDay(selSymd);
	  		    		
	  		    		//근무요일 아닌 경우 출/퇴근 시간 입력 못하도록 함
	  		    		var workDaysOpt = $this.selectedFlexitime.workDaysOpt;
	  		    		
	  		    		var d = new Date(selSymd);
	 		    		while(moment(d).diff(selEymd, 'days')<=0) {
	  		    			if(workDaysOpt[d.getDay()+1])
	  		    				isWorkDay = true;
	  		    			d.setDate(d.getDate()+1);
	  		    		}
 		    			
  		    		} 
  		    		
  		    		if(isWorkDay) 
  		    			$("#flexibleDayPlan").find(".time-input-form").show();
  		    		else
  		    			$("#flexibleDayPlan").find(".time-input-form").hide();
  		    	},
  		    	eventRenderCallback : function(info){
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
  	         	addEventSource : function(Obj){
  	         		if(Obj!=null) {
	  	         		var calendar = this.$refs.fullCalendar.cal;
	  	         		
	  	         		var eventSource = calendar.getEventSourceById(Obj.id);
	  	         		
	  	         		if(eventSource!=null) {
	  	         			eventSource.remove();
	  	         		}
	  	         		//이벤트 새로 생성
         				calendar.batchRendering(function() {
         					calendar.addEventSource(Obj);
         				});
  	         		}
  	         	},
  	         	addDayWorks : function(){ //근무시간 생성
  	         		var $this = this;
  	         	
  	         		var selStart = null;
  	         		if($this.selectedWorkday.hasOwnProperty('start')) {
  	         			selStart = $this.selectedWorkday.start;
  	         		}

  	         		var events = [];
  	         		var i = 0;
  	         		$this.dayWorks.map(function(dayWork){
						dayWork.plans.map(function(plan){
							var day = moment(plan.key).format('YYYY-MM-DD');
							var newEvent = {
	    						title: plan.label,
	    						start: day,
	    						end: day
	    					};
  		    				 
  		    				events.push(newEvent);
  		    				
  		    				i++;
						});
					});
					
					this.viewDayWorks(events);
  	         	},
  	         	changeDayWorks : function(sDate, eDate, event){ //근무시간 변경
  	         		var $this = this;
  	         		var events = $this.eventSource.events; //등록되어 있는 근무시간
  	         		
        			var newEvents = [];
        			if(events!=null && events.length>0) {
 	         			events.map(function(e){
 	         				if(moment(e.start).diff(sDate)<0 || moment(eDate).diff(e.start)<0) {
 	         					newEvents.push(e);
 	         				}
 	         			});
        			}
        			
        			//변경한 근무시간 
        			if(event!=null)
        				newEvents.push(event);
        			
	         		this.viewDayWorks(newEvents);
  	         	},
  	         	viewDayWorks : function(events) { //근무시간 달력에 보여줌
  	         		var $this = this;
  	         		if(events.length>0) {
	    				$this.eventSource['id'] = 'workTime';
	    				$this.eventSource['events'] = events;
	    				$this.eventSource['editable'] = false;
	    				$this.eventSource['backgroundColor'] = '#90e91b';
	    				$this.eventSource['borderColor'] = '#90e91b';
  		    			$this.addEventSource($this.eventSource);
		    		}
  	         	},
  	         	selectDay : function(day){ //input box에 근무시간 보여줌
  	         		var $this = this;
  	         		
  	         		if(day!=null) {
  	         			$("#startTime").val("");
  		    			$("#endTime").val("");
  	         			
	  	         		$this.dayWorks.map(function(dayWork){
	  	         			if(day==moment(dayWork.day).format('YYYY-MM-DD')) {
  		    					var valueMap = dayWork.plans[0].valueMap;
  		    					
  		    					if(valueMap!=null && valueMap.hasOwnProperty("shm"))
  		    						$("#startTime").val(valueMap.shm);
  		    					if(valueMap!=null && valueMap.hasOwnProperty("ehm"))
  		    						$("#endTime").val(valueMap.ehm);
	  	         			}
						});
  	         		}
  	         	},
				getWorkRangeInfo : function(ymd){ //오늘 또는 선택한 기간의 근무제 정보
					var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		
					var param = {
	   		    		ymd : moment(ymd).format('YYYYMMDD')
	   		    	};
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/term",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							console.log(data);
							$this.workTermTime = {};
							/* if(data.status=='OK' && data.workTermTime!=null) {
								$this.workTermTime = data.workTermTime;
								
							} */
						},
						error: function(e) {
							$this.workTermTime = {};
						}
					});
  	         	},
				getWorkDayInfo : function(ymd){ //해당일의 근무 정보
					
				},
				viewWorkPlan : function(){
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		
         			var eDate = new Date(moment($this.flexibleEmp.eYmd).format('YYYY-MM-DD'));
         			eDate.setDate(eDate.getDate()+1);
         			
         			var classNames = [];
					classNames.push($this.flexibleEmp.applCd);
					
					//결재완료된 유연근무제 표기
         			$this.addEvent({
						id: 'workRange',
						start: moment($this.flexibleEmp.sYmd).format('YYYY-MM-DD'),
	  		        	end: moment(eDate).format('YYYY-MM-DD'),
	  		        	rendering: 'background',
	  		        	classNames: classNames
					});
         			
         			<#if flexibleStdMgr?? && flexibleStdMgr!='' && flexibleStdMgr?exists >
         				var flexitime = JSON.parse("${flexibleStdMgr?js_string}");
         				
         				if(flexitime.usedTermOpt!=null && flexitime.usedTermOpt!='undefined' && flexitime.usedTermOpt!='')
         					flexitime.usedTermOpt = JSON.parse(flexitime.usedTermOpt);
         				if(flexitime.workDaysOpt!=null && flexitime.workDaysOpt!='undefined' && flexitime.workDaysOpt!='')
         					flexitime.workDaysOpt = JSON.parse(flexitime.workDaysOpt);
         				if(flexitime.applTermOpt!=null && flexitime.applTermOpt!='undefined' && flexitime.applTermOpt!='')
         					flexitime.applTermOpt = JSON.parse(flexitime.applTermOpt);
         			
         				$this.selectedFlexitime = flexitime;	
         			</#if>
         			
         			var ym = moment(calendar.getDate()).format('YYYYMM');
         			if(ym == moment($this.flexibleEmp.sYmd).format('YYYYMM')) {
         				calendar.gotoDate(moment($this.flexibleEmp.sYmd).format('YYYY-MM-DD'));
         				calendar.select(moment($this.flexibleEmp.sYmd).format('YYYY-MM-DD'));
         			}
         			
         			if($this.flexibleEmp.applStatusCd!='99') { //결재요청
         				
         			}
  	         	},
  	         	changeWorkTime : function(){ //상세 근무계획 등록
					var $this = this;
  	         		var selday = $this.selectedWorkday;
  	         		var flexibleEmp = $this.flexibleEmp;
					var workDaysOpt = $this.selectedFlexitime.workDaysOpt; //근무요일
		    		//var applTermOpt = $this.selectedFlexitime.applTermOpt; //신청기간
		    		
  		    		if(selday.start!='undefined'&& selday.end!='undefined' 
  		    				&& $("#startTime").val()!=='' && $("#endTime").val()!=='') {
  		    			//YYYY-MM-DD
  		    			var sDate = selday.start;
  		    		    var eDate = selday.end;
  		    		    
  		    		    //console.log('sDate: ' + sDate);
  		    		    //console.log('flexibleEmp.coreShm: ' + flexibleEmp.coreShm);
  		    		    //console.log('flexibleEmp.coreEhm: ' + flexibleEmp.coreEhm);
  		    		    
  		    		    //YYYY-MM-DD HH:mm
  		    		 	var coreStime = moment(moment(sDate).format('YYYYMMDD')+' '+flexibleEmp.coreShm).format('YYYY-MM-DD HH:mm');
		    		    var coreEtime = moment(moment(sDate).format('YYYYMMDD')+' '+flexibleEmp.coreEhm).format('YYYY-MM-DD HH:mm');
		    		    var sTime = moment(sDate+' '+$("#startTime").val()).format('YYYY-MM-DD HH:mm');
  		    		    var eTime = moment(sDate+' '+$("#endTime").val()).format('YYYY-MM-DD HH:mm');
  		    		  	if(moment(sTime).diff(eTime)>0){
	    					var date = new Date(sDate);
	    					date.setDate(date.getDate()+1);
	    					eTime = moment(moment(date).format('YYYY-MM-DD')+' '+$("#endTime").val()).format('YYYY-MM-DD HH:mm');
	    				}
  		    		  	
  		    		  	//console.log('coreStime'+coreStime);
  		    		  	//console.log('coreEtime'+coreEtime);
  		    		  	//console.log('sTime'+sTime);
  		    		  	//console.log('eTime'+eTime);
  		    		    
  		    			//부분 선근제의 경우 코어시간 포함하도록 체크
  		    			if(flexibleEmp.applCd!='SELE_C' || (moment(sTime).diff(coreStime)<=0 && moment(coreStime).diff(eTime)<=0
  		    					&& moment(sTime).diff(coreEtime)<=0 && moment(coreEtime).diff(eTime)<=0)) {
  		    		    
	  		    			var d = new Date(sDate);
	  		    			while(moment(d).diff(eDate, 'days')<=0) {
	  		    				if(workDaysOpt[d.getDay()+1]) { //근무요일이고, 신청기간이면
		  		    				if($this.dayResult.hasOwnProperty(moment(d).format("YYYYMMDD"))) {
		  		    					$this.dayResult[moment(d).format("YYYYMMDD")].shm = moment(sDate+' '+$("#startTime").val()).format('HHmm');
		  		    					$this.dayResult[moment(d).format("YYYYMMDD")].ehm = moment(sDate+' '+$("#endTime").val()).format('HHmm');
		  		    				} else {
		  		    					$this.dayResult[moment(d).format('YYYYMMDD')] = {
				  		    				shm: moment(sDate+' '+$("#startTime").val()).format('HHmm'),
		 			    	  		    	ehm: moment(sDate+' '+$("#endTime").val()).format('HHmm')	
				  		    			};
		  		    				}
	  		    				}
		  		    				
			  		    		//날짜 증가
			  		    		d.setDate(d.getDate()+1);
	  		    			}
	
	  		    			console.log($this.dayResult);
	  		    			
	  		    			var events = [];
	  		    			$.each($this.dayResult, function(k, v){
	  		    				var day = moment(k).format('YYYY-MM-DD');
	  		    				var newEvent = {
		    						title: moment(k+' '+v.shm).format('HH:mm')+'~'+moment(k+' '+v.ehm).format('HH:mm'),
		    						start: day,
		    						end: day
		    					};
	  		    				 
	  		    				$this.changeDayWorks(day, day, newEvent);
	  		    			}); 
	  		    			
  		    			} else {
  		    				$("#alertText").html("코어시간이 포함되어야 합니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#startTime").val('');
	  	  	         			$("#endTime").val('');
	  	  	         			
	  	  	         			//이벤트 재생성
	  	  	         			$this.changeDayWorks(sDate, eDate, null);
	  	  	         			
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
  		    			}
  		    		} 
  		    		
  	         	},
  	         	getWorkDayResult : function(){ //일근무결과 조회
	         		var $this = this;
	  	         	
         			var param = {
         				flexibleEmpId : $this.flexibleEmp.flexibleEmpId
	   		    	};
 	         			
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/dayWorks",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							console.log(data);
							if(data!=null) {
								$this.dayWorks = data;
								$this.addDayWorks();
							} 
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("근무시간 조회 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					}); 

	         	},
  	         	saveWorkDayResult : function(){ //일근무결과 저장
	         		var $this = this;
	  	         	
         			var param = {
         				flexibleEmpId : $this.flexibleEmp.flexibleEmpId,
         				dayResult : $this.dayResult
	   		    	};
 	         			
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/save",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							//console.log(data);
							if(data!=null && data.status=='OK') {
								$("#alertText").html("저장되었습니다.");
								
								if(data.dayWorks!=null && data.dayWorks.length>0) {
									$this.dayWorks = data.dayWorks;
									$this.addDayWorks();
								}
								
							} else {
								$("#alertText").html(data.message);
							}
							$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						},
						error: function(e) {
							console.log(e);
							$("#alertText").html("확인요청 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
	  	  	         		$("#alertModal").modal("show"); 
						}
					}); 

	         	}
  		    }
   	});
   	
</script>

