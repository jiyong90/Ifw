<div id="dayCalendar" class="calendar-wrap" v-cloak>
    <div id='calendar-container'>
		<!-- <full-calendar ref="fullCalendar" :header="header" :events="events" :eventsources="eventSources" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback" ></full-calendar> -->
		<full-calendar ref="fullCalendar" :custombuttons="customButtons" :header="header" @update="renderCallback" @datesrender="datesRenderCallback" @dayrender="dayRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback"></full-calendar>
    </div>
</div>
<script type="text/javascript">
   	var dayCalendarVue = new Vue({
   		el: "#dayCalendar",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
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
  		    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
  		    	data: [],
  		    	flexibleEmp: {},
  		    	selectedWorkday: {},
  		    	empHolidays: [],
  		    	dayResult: {}, //저장할 근무계획
  		    	dayWorks: {},
  		    	eventSource: {}
  		    },
  		    watch: {
  		    	selectedWorkday : function(val, oldVal){
  		    		if(val.start == val.end)
  		    			$("#selectedRange").text(val.start);
  		    		else
  		    			$("#selectedRange").text(val.start + "~" + val.end);
  		    	},
  		    	flexibleEmp : function(val, oldVal) {
  		    		if(val.sYmd!=null && val.sYmd!=undefined && val.sYmd!='') {
  		    		
	  		    		this.selectedWorkday  = {
		    				start: moment(val.sYmd).format('YYYY-MM-DD'),
		    				end: moment(val.sYmd).format('YYYY-MM-DD')
		    			};
	  		    		
	  	  				//근무시간, 코어시간 그래프 표기
	  	  				if(val.workShm!=null && val.workShm!=undefined && val.workShm!=''
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
  		    		}
  	  			}
  		    },
  		    mounted: function(){
  		    	var $this = this;
	    		
	    		<#if flexibleEmp?? && flexibleEmp!='' && flexibleEmp?exists >
	    			$this.flexibleEmp = JSON.parse("${flexibleEmp?js_string}"); 
	    			
	    			if($this.flexibleEmp.hasOwnProperty("holidays"))
	    				$this.empHolidays = $this.flexibleEmp.holidays;
	    			
	    			//탄력근무제는 조출/잔업 show
	    			if($this.flexibleEmp.hasOwnProperty('workTypeCd') && $this.flexibleEmp.workTypeCd!=null && $this.flexibleEmp.workTypeCd=='ELAS') {
	    				$("#elasOtTime").show();
	    			}
	  		    		
		    	</#if>
		    	
  		    	//근무 계획 작성 화면 전환
         		$("#flexibleDayPlan").show();
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		calendarLeftVue.calendar = calendar;
  		    		
  		    		if(this.flexibleEmp!=null && Object.keys(this.flexibleEmp).length>0) {
  		    			var $this = this;
	    				var unitMinute = this.flexibleEmp.unitMinute;
	    				
		    		    if(unitMinute!=null && unitMinute!='' && unitMinute!=undefined) {
	    					//$('#startTime').datetimepicker('stepping',Number(unitMinute));
	    					//$('#endTime').datetimepicker('stepping',Number(unitMinute));
	    					
	    					//출퇴근시간 unitMinute 세팅
	    					$('#startTime').datetimepicker({
	    			            //format: 'LT',
	    			            format: 'HH:mm',
	    			            use24hours: true,
	    			            language: 'ko',
	    			            widgetPositioning: {
	    			                horizontal: 'left',
	    			                vertical: 'bottom'
	    			            },
	    			 		    useCurrent: false,
	    			 		   	stepping: Number(unitMinute)
	    			        });
	    					
	    					$('#endTime').datetimepicker({
	    			            //format: 'LT',
	    			            format: 'HH:mm',
	    			            use24hours: true,
	    			            language: 'ko',
	    			            widgetPositioning: {
	    			                horizontal: 'right',
	    			                vertical: 'bottom'
	    			            },
	    			 		    useCurrent: false,
	    			 		    stepping: Number(unitMinute)
	    			        });
	    					
	    				} else {
	    					//출퇴근시간 unitMinute 세팅
	    					$('#startTime').datetimepicker({
	    			            //format: 'LT',
	    			            format: 'HH:mm',
	    			            use24hours: true,
	    			            language: 'ko',
	    			            widgetPositioning: {
	    			                horizontal: 'left',
	    			                vertical: 'bottom'
	    			            },
	    			 		    useCurrent: false
	    			        });
	    					
	    					$('#endTime').datetimepicker({
	    			            //format: 'LT',
	    			            format: 'HH:mm',
	    			            use24hours: true,
	    			            language: 'ko',
	    			            widgetPositioning: {
	    			                horizontal: 'right',
	    			                vertical: 'bottom'
	    			            },
	    			 		    useCurrent: false
	    			        });
	    				}
		    			
		    		    //console.log(this.flexibleEmp);
		    		    
		    		    
		    			//상세 계획 입력 화면 전환
		         		$this.viewWorkPlan(this.flexibleEmp);
		    			
						//일근무결과 달력에 표기
						$this.addDayWorks();
						
		    		    //유연근무제 신청 기간 이외의 날짜는 선택하지 못하게 함
  		    			$this.selectAllow(this.flexibleEmp);
	    			}
  		    		
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

  		    		if(info.view.type == 'dayGridMonth' && calendar.getOption('selectAllow')!=undefined) { //month change
  		    			$this.markAdditionalInfo();
  		    			//getWorkDayResult();
	  		    	}
  		    		
  		    	},
  		    	dayRenderCallback : function(info){ //day render
  		    		var date = info.date;
  	         		$('td').find(".fc-day-top[data-date='"+moment(date).format('YYYY-MM-DD')+"'] .fc-day-number").text(moment(date).format('D'));
  		    	},
  		    	selectCallback : function(info){ //day select
					var $this = this;
  		    	
					var selSymd = info.startStr;
  		    		var selEymd = new Date(info.endStr);
  		    		selEymd.setDate(selEymd.getDate()-1);
  		    		selEymd = moment(selEymd).format('YYYY-MM-DD');
  		    		
	    			var dayNum = moment(info.endStr).diff(info.startStr, 'days');
	    			
	    			//선택한 날짜가 1일인 경우 저장된 데이터가 있으면 출/퇴근 시간 표기
  		    		//선택한 날짜가 기간인 경우 clear
  	         		if(dayNum==1) {
  		    			if(Object.keys($this.dayResult).length>0 && $this.dayResult.hasOwnProperty(moment(info.startStr).format('YYYYMMDD'))) {
  		    				//작성중인 근무 계획(저장할 데이터)
  		    				
  		    				if($this.flexibleEmp!=null && Object.keys($this.flexibleEmp).length>0) {
		       					var dayWorks = $this.flexibleEmp.dayWorks;
			  	         		dayWorks.map(function(dayWork){
		  	         				if(selSymd==moment(dayWork.day).format('YYYY-MM-DD')) {
		  	         					$("#timeNm").text(dayWork.timeNm);
		  	         				}
		  	         					
		  	         			});
			  	         	}
  		    				
  		    				$("#startTime").val("");
			    			$("#endTime").val("");
			    			$("#otbMinute").val("");
			    			$("#otaMinute").val("");
  		    				
  		    				var day = moment(info.startStr).format('YYYYMMDD');
  		    				var valueMap = $this.dayResult[day];
  		    				
  		    				if(valueMap!=null && valueMap.hasOwnProperty("shm") && valueMap.shm!='') {
  		    					var shm = moment(day+' '+valueMap.shm).format('HH:mm');
		    					$("#startTime").val(shm);
  		    				}
  		    				if(valueMap!=null && valueMap.hasOwnProperty("ehm") && valueMap.ehm!='') { 
  		    					var ehm = moment(day+' '+valueMap.ehm).format('HH:mm');
		    					$("#endTime").val(ehm);
  		    				} 
  		    				if(valueMap!=null && valueMap.hasOwnProperty("otbMinute") && valueMap.otbMinute!='') {
		    					$("#otbMinute").val(valueMap.otbMinute);
  		    				} 
  		    				if(valueMap!=null && valueMap.hasOwnProperty("otaMinute") && valueMap.otaMinute!='') {
		    					$("#otaMinute").val(valueMap.otaMinute);
  		    				} 
  		    				
  		    			} else {
  		    				//작성한 근무 계획 조회
  		    				$this.getWorkPlan(info.startStr);
  		    			} 
  	         		} else {
  	         			$("#timeNm").text("");
  	         			$("#startTime").val("");
		    			$("#endTime").val("");
		    			$("#otbMinute").val("");
		    			$("#otaMinute").val("");
  	         		}
  		    		
  		    	},
  		    	eventRenderCallback : function(info){
  		    		
  		    		$(info.el).find('.fc-title').html(info.event.title);
  		    		
  		    		//결재 완료된 근무제 기간 표시
  		    		if(info.event.id.indexOf('workRange.')==0) {
  		    			var workTypeCd = info.event.extendedProps.workTypeCd;
  		    			
  		    			var borderDiv = '';
  		    			if(info.isStart) {
  		    				borderDiv = '<div class="fc-border start '+workTypeCd+'"></div>';
  		    			} else if(info.isEnd) {
  		    				borderDiv = '<div class="fc-border end '+workTypeCd+'"></div>';
  		    			} else {
  		    				borderDiv = '<div class="fc-border '+workTypeCd+'"></div>';
  		    			}
  		    			//$(info.el).css('background-color','#FFFFFF');
  		    			$(info.el).prepend(borderDiv);
  		    		}
  		    	},
  		    	/* eventClickCallback : function(info) {
					var $this = this;
  		    		var calendar = $this.$refs.fullCalendar.cal;
  		    		calendar.select(moment(info.event.start).format('YYYY-MM-DD'));
  		    	}, */
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
  	         	addEventSource : function(id, events){
  	         		var $this = this;
  	         		
  	         		if(events.length>0) {
  	         			var calendar = this.$refs.fullCalendar.cal;
  	         			
	  	         		var eventSource = calendar.getEventSourceById(id);
	  	         		if(eventSource!=null) {
	  	         			eventSource.remove();
	  	         		} 
	  	         		
	  	         		//이벤트 새로 생성
	  	         		var eMap = {
	  	         			id : id,
	  	         			events : events,
	  	         			editable : false
	  	         		}
	  	         		$this.eventSource = eMap;
	  	         		//$this.eventSource['id'] = id;
	  	         		//$this.eventSource['events'] = events;
 	         			//$this.eventSource['editable'] = false;
 	         			
         				calendar.batchRendering(function() {
         					calendar.addEventSource(eMap);
         				});
  	         		}
  	         	}, 
  	         	selectAllow : function(d){
  		    		var $this = this;
  		    		var calendar = $this.$refs.fullCalendar.cal;
  		    		
       				//선소진여부
       				//Y인 경우엔 평일 휴일 상관없이 출퇴근시간 입력 가능
       				//N인 경우엔 정해진 휴일엔 입력 못하게끔
  		    		//유연근무 신청 기간이 아닌 날짜와 휴일, 반차는 선택하지 못하게 함.
  	         		calendar.setOption('selectAllow', function(i){
  	         			
  	         			var selSymd = i.startStr;
  	  		    		var selEymd = new Date(i.endStr);
  	  		    		
  	  		    		selEymd.setDate(selEymd.getDate()-1);
  	  		    		selEymd = moment(selEymd).format('YYYY-MM-DD');

	  		    		//달력에 일자 선택하여 출/퇴근, 조출, 잔업시간 입력후 다른 날짜를 클릭하면 focus out시켜서 changeWorktime 호출될 수 있게끔 함
	  		    		/* if($this.selectedWorkday.start!=selSymd) {
	  		    			if($('#startTime:focus').length>0)
	  		    				$('#startTime').blur();
	  		    			else if($('#endTime:focus').length>0)
	  		    				$('#endTime').blur();
	  		    			else if($('#otbMinute:focus').length>0)
	  		    				$('#otbMinute').blur();
	  		    			else if($('#otaMinute:focus').length>0)
	  		    				$('#otaMinute').blur();
	  		    		} */
	  		    		
	  		    		//선택한 날짜 
	         			//유연근무제에 해당되지 않은 날짜여도 신청일자에 표시하기 위함.
  	  					$this.selectedWorkday  = {
  	  	    				start: selSymd,
  	  	    				end: selEymd
  	  	    			};
	  		    		
	  		    		//var selYmd = new Date(i.start);
	  		    		//휴일
	  	         		/* 
	  	         		var workDaysOpt = {};
	  	         		if(d.hasOwnProperty("workDaysOpt") && d.workDaysOpt!=null && d.workDaysOpt!=undefined && d.workDaysOpt!=''){
	  	         			workDaysOpt = JSON.parse(d.workDaysOpt);
	  	         		} */
  	  		    		
	  	         		return $this.setEditYn(i.startStr, i.endStr, d);
  	         			
  	         		});
  		    	},
  		    	setEditYn : function(startStr, endStr, obj) { //출/퇴근 시간 수정여부 판단
  		    		//선택한 시작일자, 선택한 종료일자, 유연근무제 종료일자, 유연근무제
  		    		var $this = this;
  		    		var editYn = false;
  		    		
  		    		var sYmd = moment(obj.sYmd).format('YYYY-MM-DD');
  		    		var eYmd = new Date(moment(obj.eYmd).format('YYYY-MM-DD'));
  		    		eYmd.setDate(eYmd.getDate()+1);
  		    		eYmd = moment(eYmd).format('YYYY-MM-DD');
  		    		
  		    		//당일 출퇴근 시간 수정여부
  		    		var todayPlanEditYn = 'N';
  		    		if(obj.hasOwnProperty('todayPlanEditYn') && obj.todayPlanEditYn!=null && obj.todayPlanEditYn!=undefined)
  		    			todayPlanEditYn = obj.todayPlanEditYn;
  		    		
  		    		if( moment(sYmd).diff(startStr)<=0 && moment(startStr).diff(eYmd)<=0
						&& moment(sYmd).diff(endStr)<=0 && moment(endStr).diff(eYmd)<=0
   						&& (todayPlanEditYn=='Y'&&moment($this.today).diff(startStr)<=0 || todayPlanEditYn=='N'&&moment($this.today).diff(startStr)<0) && moment($this.today).diff(endStr)<0
   						&& $this.empHolidays.indexOf(moment(startStr).format('YYYYMMDD'))==-1
   						&& (obj.workTypeCd!='ELAS' || (obj.workTypeCd=='ELAS' && obj.applStatusCd=='11')) //탄근제 임시저장했을 때만 수정 가능하도록
   						//&& (Object.keys(workDaysOpt).length==0 || Object.keys(workDaysOpt).length>0 && workDaysOpt[selYmd.getDay()+1])
   					) {
  		    			editYn = true;
  		    		} else {
  		    			if(moment(startStr).diff(sYmd)<0 || moment(startStr).diff(eYmd)>=0) {
  		    				$("#timeNm").text("");
  		    				$("#startTime").val("");
  		    				$("#endTime").val("");
  		    				$("#otbMinute").val("");
  		    				$("#otaMinute").val("");
  		    			} 
  		    		}
         			
         			if(editYn) {
         				$("#startTime").prop("disabled", false);
	    				$("#endTime").prop("disabled", false);
	    				$("#otbMinute").prop("disabled", false);
	    				$("#otaMinute").prop("disabled", false);
	    				$("#reason").prop("disabled", false);
	    				$("#timeSaveBtn").show();
	    				$("#timeApprBtn").show();
         			} else {
	    				//selectAllow 불가한 경우 select callback이 호출되지 않기 때문에 작성한 근무 계획 조회
         				$this.getWorkPlan(startStr);
		  	         		
         				$("#startTime").prop("disabled", true);
  		    			$("#endTime").prop("disabled", true);
  		    			$("#otbMinute").prop("disabled", true);
    					$("#otaMinute").prop("disabled", true);
    					$("#reason").prop("disabled", true);
  		    			$("#timeSaveBtn").hide();
  		    			$("#timeApprBtn").hide();
         			}
         			
         			return editYn;
  		    	},
  		    	getWorkPlan: function(selYmd) { //이전에 작성한 근무 계획 조회
  		    		var $this = this;
	    			if($this.flexibleEmp!=null && Object.keys($this.flexibleEmp).length>0) {
       					var dayWorks = $this.flexibleEmp.dayWorks;
	  	         		dayWorks.map(function(dayWork){
	  	         			if(selYmd==moment(dayWork.day).format('YYYY-MM-DD')) {
	  	         				calendarLeftVue.holidayYn = dayWork.holidayYn;
	  	         				$("#timeNm").text(dayWork.timeNm);
	  	         				$("#startTime").val("");
		  	  	    			$("#endTime").val("");
		  	  	    			$("#otbMinute").val("");
		  	  	    			$("#otaMinute").val("");
		  	  	    			
	  	         				dayWork.plans.map(function(p){
	  	         					var valueMap = p.valueMap;
	  	         					
	  	         					if(valueMap.timeTypeCd=='BASE') {
		  		    					if(valueMap!=null && valueMap.hasOwnProperty("shm")) {
		  		    						$("#startTime").val(valueMap.shm);
		  		    					} 
		  		    					if(valueMap!=null && valueMap.hasOwnProperty("ehm")) {
		  		    						$("#endTime").val(valueMap.ehm);
		  		    					} 
	  	         					} else if(valueMap.timeTypeCd=='OTB') {
	  	         						if(valueMap!=null && valueMap.hasOwnProperty("m")) {
		  		    						$("#otbMinute").val(valueMap.m);
		  		    					} 
	  	         					} else if(valueMap.timeTypeCd=='OTA') {
	  	         						if(valueMap!=null && valueMap.hasOwnProperty("m")) {
	  	         							var otaMin = valueMap.m;
		  		    						$("#otaMinute").val(otaMin);
		  		    					} 
	  	         					}
	  	         				});
  	         					
	  	         			}
						});
	    			} 
  		    	},
  		    	markAdditionalInfo : function() { //회사 휴일과 근태 정보 달력에 표기
  		    		var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;

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
									//if(cal.hasOwnProperty("holidayYmd") && cal.holidayYmd!='') {
										//$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"'] span.fc-holiday").remove();
										if($(".fc-day-top[data-date='"+cal.sunYmd+"'] span[name='companyHoliday']").text().indexOf(cal.holidayNm)==-1)
											$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").prepend("<span class='fc-holiday'>"+cal.holidayNm+"</span>");
										$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").css({"color":"#FF0000"});
									//}
								});
							}
						},
						error: function() {
							
						}
					});
   		    		
   		    		//근태 정보 달력에 표기
					if(Object.keys($this.dayWorks).length>0) {
  	         			$.each($this.dayWorks, function(k, v){
		  	         		v.map(function(dayWork){
		  	         			//근무일
		  	         			if(dayWork.hasOwnProperty("holidayYn") && dayWork.holidayYn!='Y') {
									dayWork.plans.map(function(plan){
										var day = moment(plan.key).format('YYYY-MM-DD');
										
			  		    				if(plan.valueMap.hasOwnProperty("taaCd") && plan.valueMap.taaCd!='') {
			  		    					if($(".fc-day-top[data-date='"+day+"'] span.fc-holiday").length==0) {
			 		    						$('td').find(".fc-day-top[data-date='"+day+"']").prepend("<span class='fc-holiday'>"+plan.label+"</span>");
			  		    					}else {
			  		    						if($(".fc-day-top[data-date='"+day+"'] span.fc-holiday").text().indexOf(plan.label)==-1)
				  		    						$('td').find(".fc-day-top[data-date='"+day+"'] span.fc-holiday").append(' '+plan.label);
			  		    					}
				  		    				$('td').find(".fc-day-top[data-date='"+day+"'] span.fc-holiday").css({"color":"#4d84fe"});
			  		    				}
									});
		  	         			}
		  	         		});
  	         			});
					} 
  		    		
  		    	},
	         	viewWorkPlan : function(d){
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		//console.log(d);
  	         		calendarLeftVue.flexibleAppl = d;
  	         		if(d.hasOwnProperty('reason') && d.reason!=null && d.reason!=undefined)
  	         			calendarLeftVue.applInfo.reason = d.reason;
  	         		
  	         		//결재라인
  	         		if(d.workTypeCd == 'ELAS')
	             		calendarLeftVue.applLine = calendarLeftVue.getApplLine(d.workTypeCd);
  	         		
         			var eDate = new Date(moment(d.eYmd).format('YYYY-MM-DD'));
         			eDate.setDate(eDate.getDate()+1);
         			
         			//유연근무제 표기
         			var newEvent = {
						id: 'workRange.'+d.sYmd,
						start: moment(d.sYmd).format('YYYY-MM-DD'),
	  		        	end: moment(eDate).format('YYYY-MM-DD'),
	  		        	rendering: 'background',
	  		        	extendedProps: {
	  		        		workTypeCd: d.workTypeCd
	  		        	}
					};
         			
         			$this.addEvent(newEvent);
         			
         			//selectAllow에서 해당 일 edit여부가 설정된 후 사용자가 일자를 클릭하면 edit여부에 따라 변경하는데
         			//화면 처음 뜨자마자는 달력 클릭 이벤트가 발생하지 않기 때문에 호출
         			$this.setEditYn(moment(d.sYmd).format('YYYY-MM-DD'), moment(eDate).format('YYYY-MM-DD'), d);
         			
         			calendar.gotoDate(moment(d.sYmd).format('YYYY-MM-DD'));
		         	calendar.select(moment(d.sYmd).format('YYYY-MM-DD'));
         			
  	         	},
  	         	addDayWorks : function(){ //근무시간 생성
  	         		var $this = this;
  	         		
  	         		//휴일, 반차가 아닌 근태(연차, 교육, 출장 등)
  	         		var events = [];
  	         		
  	         		if($this.flexibleEmp!=null && Object.keys($this.flexibleEmp).length>0) {
  	         			var dayWorks = $this.flexibleEmp.dayWorks;
 	         			dayWorks.map(function(dayWork){
	  	         			//휴일 데이터
	  	         			//탄근제는 휴일이어도 근무시간 작성할 수 있다.
	  	         			if($this.flexibleEmp.workTypeCd=='ELAS' || ($this.flexibleEmp.workTypeCd!='ELAS' && dayWork.hasOwnProperty("holidayYn") && dayWork.holidayYn!='Y')) {
								dayWork.plans.map(function(plan){
									var day = moment(plan.key).format('YYYY-MM-DD');
									
		  		    				if(plan.valueMap.hasOwnProperty("taaCd") && plan.valueMap.taaCd!='') {
		  		    					//개인 근태
			  		    				/* var taaEvent = {
											id: day,
				    						title: "<div class='dot work-type'><span>" + plan.label + "</span></div>",
				    						start: day,
				    						end: day
				    					};
			  		    				events.push(taaEvent); */
			  		    				
			  		    				//반차가 아닌 근태(연차, 교육, 출장 등)
			  		    				if(plan.valueMap.taaCd!='102' && plan.valueMap.taaCd!='103') {
			  		    					$this.empHolidays.push(moment(dayWork.day).format('YYYYMMDD'));
			  		    				}
			  		    				
		  		    				} else if(plan.valueMap.hasOwnProperty("shm") && plan.valueMap.shm!='' || plan.valueMap.hasOwnProperty("ehm") && plan.valueMap.ehm!='') {
		  		    					//클래스명 알파벳 순서대로 이벤트가 정렬됨 ㅠㅠ
		  		    					if(plan.valueMap.hasOwnProperty("timeTypeCd") && (plan.valueMap.timeTypeCd=='FIXOT' || plan.valueMap.timeTypeCd=='OT' || plan.valueMap.timeTypeCd=='NIGHT' || plan.valueMap.timeTypeCd=='OTA')) //고정ot, 조출, 야간은 색 다르게 표기
		  		    						timeTypeClass = ' later';
		  		    					else if(plan.valueMap.hasOwnProperty("timeTypeCd") && plan.valueMap.timeTypeCd=='OTB') //고정ot, 조출, 야간은 색 다르게 표기
	  		    							timeTypeClass = ' early';
		  		    					else
		  		    						timeTypeClass = ' general';
		  		    					
		  		    					//계획 or 실적시간
		  		    					var timeEvent = {
											id: day,
				    						title: "<div class='dot time "+timeTypeClass+"'><span>"+ plan.label + "</span></div>",
				    						start: day,
				    						end: day
				    					};
			  		    				events.push(timeEvent);
		  		    				}
		  		    			
								});
	  	         			}
						});
 	         			
 	         			if(events.length>0)
 	         				$this.addEventSource('dayWorks',events);
  	         		}
  	         		
  	         		//console.log($this.empHolidays);
  	         	},
  	         	changeDayWorks : function(sDate, eDate, dayResult){ //근무시간 변경
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		
  	         		var events = [];
  	         		if($this.eventSource.hasOwnProperty("events"))
  	         			events = $this.eventSource.events; //등록되어 있는 근무시간
  	         		
  	         		var prevEvent = []; //출.퇴근 시간이 수정된 날짜를 제외한 이벤트
        			if(events!=null && events.length>0) {
 	         			events.map(function(e){
 	         				if(moment(e.start).diff(sDate)<0 || moment(eDate).diff(e.start)<0) {
 	         					prevEvent.push(e);
 	         				}
 	         			});
        			}
  	         		//console.log(prevEvent);
  	         		
        			var updatedEvent = []; //출.퇴근시간이 수정된 이벤트
        			if(dayResult!=null && Object.keys(dayResult).length>0) {
        				$.each(dayResult, function(k, v){
        					var day = moment(k).format('YYYY-MM-DD');
        					
        					//선택된 날짜에 한해서만 출.퇴근시간 수정
        					if(moment($this.selectedWorkday.start).diff(day)<=0 && moment(day).diff($this.selectedWorkday.end)<=0){
        						var e = {
     	    						title: "<div class='dot time general'><span>"+ moment(k+' '+v.shm).format('HH:mm')+'~'+moment(k+' '+v.ehm).format('HH:mm') + "</span></div>",
     	    						start: day,
     	    						end: day
     	    					};
         						updatedEvent.push(e);
        					}
        					
		    			});
        			}
        			//console.log(updatedEvent);
        			
        			var result = [];
        			//변경한 근무시간 
        			if(updatedEvent!=null && updatedEvent.length>0)
        				result = prevEvent.concat(updatedEvent);
        			else
        				result = prevEvent;

        			$this.addEventSource('dayWorks',result);
  	         	},
  	         	changeWorkTime : function(){ //상세 근무계획 등록
					var $this = this;
  	         		var selday = $this.selectedWorkday;
  	         		var flexibleAppl = $this.flexibleEmp;
					//var workDaysOpt = calendarTopVue.flexibleStd.workDaysOpt; //근무요일
		    		//var applTermOpt = calendarTopVue.flexibleStd.applTermOpt; //신청기간
		    		
		    		//YYYY-MM-DD
	    			var sDate = selday.start;
	    		    var eDate = selday.end;
		    		
  		    		if(selday.start!='undefined'&& selday.end!='undefined' 
  		    				&& $("#startTime").val()!=='' && $("#endTime").val()!=='') {
  		    		    
  		    		    //YYYY-MM-DD HH:mm
  		    		    //근무시간(근무일 기준)
  		    		    var workStime = '';
		    		    if(flexibleAppl.hasOwnProperty('workShm') && flexibleAppl.workShm!='')
		    		    	workStime = moment(moment(sDate).format('YYYYMMDD')+' '+flexibleAppl.workShm).format('YYYY-MM-DD HH:mm');
		    		  	var workEtime = '';
		    		  	if(flexibleAppl.hasOwnProperty('workEhm') && flexibleAppl.workEhm!='')
		    		  		workEtime = moment(moment(sDate).format('YYYYMMDD')+' '+flexibleAppl.workEhm).format('YYYY-MM-DD HH:mm');
		    		  	
  		    		    //코어시간(근무일 기준)
  		    		    var coreChkYn = 'N';
  		    		    var coreStime = '';
  		    		  	if(flexibleAppl.hasOwnProperty('coreChkYn') && flexibleAppl.coreChkYn!='')
  		    		  		coreChkYn = flexibleAppl.coreChkYn;
  		    		    if(flexibleAppl.hasOwnProperty('coreShm') && flexibleAppl.coreShm!='')
  		    		    	coreStime = moment(moment(sDate).format('YYYYMMDD')+' '+flexibleAppl.coreShm).format('YYYY-MM-DD HH:mm');
  		    		  	var coreEtime = '';
  		    		  	if(flexibleAppl.hasOwnProperty('coreShm') && flexibleAppl.coreShm!='')
  		    		  		coreEtime = moment(moment(sDate).format('YYYYMMDD')+' '+flexibleAppl.coreEhm).format('YYYY-MM-DD HH:mm');
  		    		 	
  		    		  	//계획시간
		    		    var sTime = moment(sDate+' '+$("#startTime").val()).format('YYYY-MM-DD HH:mm');
  		    		    var eTime = moment(sDate+' '+$("#endTime").val()).format('YYYY-MM-DD HH:mm');
  		    		    
  		    		    //종료시간이 다음날일 때
  		    		  	if(moment(sTime).diff(eTime)>0){
	    					var date = new Date(sDate);
	    					date.setDate(date.getDate()+1);
	    					
	    					eTime = moment(moment(date).format('YYYY-MM-DD')+' '+$("#endTime").val()).format('YYYY-MM-DD HH:mm');
	    				}
  		    		    
	    		    	//근무일 기준 근무시간과 코어시간 포함하도록 체크
  		    			if(	(workStime==''&&workEtime=='' || moment(workStime).diff(sTime)<=0 && moment(eTime).diff(workEtime)<=0 )
  		    					&& (coreChkYn=='N' || coreChkYn=='Y' && (coreStime==''&&coreEtime=='' || moment(sTime).diff(coreStime)<=0 && moment(coreStime).diff(eTime)<=0 && moment(sTime).diff(coreEtime)<=0 && moment(coreEtime).diff(eTime)<=0))) {
  		    		    
	  		    			var d = new Date(sDate);
	  		    			
	  		    			while(moment(d).diff(eDate, 'days')<=0) {
	  		    				if( //workDaysOpt[d.getDay()+1] && 
	  		    						$this.empHolidays.indexOf(moment(d).format("YYYYMMDD"))==-1) { //근무요일이고, 신청기간이면
		  		    				if($this.dayResult.hasOwnProperty(moment(d).format("YYYYMMDD"))) {
		  		    					$this.dayResult[moment(d).format("YYYYMMDD")].shm = moment(sDate+' '+$("#startTime").val()).format('HHmm');
		  		    					$this.dayResult[moment(d).format("YYYYMMDD")].ehm = moment(sDate+' '+$("#endTime").val()).format('HHmm');
		  		    				} else {
		  		    					$this.dayResult[moment(d).format('YYYYMMDD')] = {
				  		    				shm: moment(sDate+' '+$("#startTime").val()).format('HHmm'),
		 			    	  		    	ehm: moment(sDate+' '+$("#endTime").val()).format('HHmm')	
				  		    			};
		  		    				}
	  		    						
	  		    					$this.dayResult[moment(d).format('YYYYMMDD')].otbMinute = $("#otbMinute").val();
	  		    					$this.dayResult[moment(d).format('YYYYMMDD')].otaMinute = $("#otaMinute").val();
	  		    				}
		  		    				
			  		    		//날짜 증가
			  		    		d.setDate(d.getDate()+1);
	  		    			}
	
	  		    			//console.log($this.dayResult);
	  		    			$this.changeDayWorks(sDate, eDate, $this.dayResult);
	  		    			
  		    			} else {
  		    				
  		    				$("#alertText").html("근무시간/코어시간이 포함되어야 합니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         			$("#startTime").val('');
	  	  	         			$("#endTime").val('');
		  	  	         		$("#otbMinute").val('');
	  	  	         			$("#otaMinute").val('');
	  	  	         		
	  	  	         			if(Object.keys($this.dayWorks).length>0 && $this.dayWorks[calendarLeftVue.flexibleAppl.sYmd].length>0) {
		  	  	         			var dayWorks = $this.dayWorks[calendarLeftVue.flexibleAppl.sYmd];
		  	  	         			dayWorks = dayWorks.filter(function(k){
		  	  	         				return (k.day != moment(sDate).format('YYYYMMDD'));
		  	  	         			});
		  	  	         			
		  	  	         			$this.dayWorks[calendarLeftVue.flexibleAppl.sYmd] = dayWorks;
	  	  	         			}
	  	  	         		
		  	  	         		var d = new Date(sDate);
		  		    			while(moment(d).diff(eDate, 'days')<=0) {
		  		    				//delete $this.dayResult[moment(d).format('YYYYMMDD')];
		  		    				
		  		    				$this.dayResult[moment(d).format('YYYYMMDD')] = {
		  		    					shm: '',
		  		    					ehm: '',
		  		    					otbMinute: '',
		  		    					otaMinute: ''
		  		    				};
		  		    				//날짜 증가
				  		    		d.setDate(d.getDate()+1);
		  		    			}
		  	  	         		
	  	  	         			//이벤트 재생성
	  	  	         			$this.changeDayWorks(sDate, eDate, null);
	  	  	         			
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
  		    			}
  		    		    
  		    		} else {
  		    			var d = moment(selday.start).format("YYYYMMDD");
  		    			
  		    			//탄근제 휴일인 경우 계획시간을 지울 수도 있다.
   						$this.dayResult[d] = {
  		    				shm: "",
		    	  		    ehm: ""
  		    			};
  		    			
  		    			if($("#otbMinute").length>0 ||$("#otbMinute").is(":visible")
  		    					||$("#otaMinute").length>0 ||$("#otaMinute").is(":visible")) {
  		    				$this.dayResult[d].otbMinute = '';
  		    				$this.dayResult[d].otaMinute = '';
  		    				$("#otbMinute").val('');
	  	         			$("#otaMinute").val('');
  		    			}
   							
   						//이벤트 재생성
 	  	         		$this.changeDayWorks(sDate, eDate, null);
  		    			
  		    		}
  		    		
  	         	},
  	         	saveWorkDayResult : function(lastYn){ //일근무결과 저장
	         		var $this = this;
  	         	
  	         		$("#loading").show();
	  	         	
         			var param = {
         				dayResult : $this.dayResult
	   		    	};
         			
         			var url = "${rc.getContextPath()}/flexibleEmp/save";
         			if($this.flexibleEmp.workTypeCd!='ELAS') {
         				param['flexibleEmpId'] = $this.flexibleEmp.flexibleEmpId;
         			} else {
         				url += "/elas";
         				param['flexibleApplId'] = $this.flexibleEmp.flexibleApplId;
         				param['reason'] = calendarLeftVue.applInfo.reason;
         			}
         			
         			//dayResult는 이런 형식으로 저장 {ymd : {shm: 0900, ehm: 1800, otbMinute: 1, otaMinute:2 } }
         			//console.log($this.dayResult);
   		    		Util.ajax({
						url: url,
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							//console.log(data);
							$("#loading").hide();
							if(data!=null && data.status=='OK') {
								//$this.dayResult = {};
								if(data.dayWorks!=null && data.dayWorks.length>0) {
									$this.flexibleEmp.dayWorks = data.dayWorks;
									$this.addDayWorks();
								}
								
								if(data.avgHour!=null) {
									calendarLeftVue.flexibleAppl.avgHour = data.avgHour;
								}
								
								//확인요청
								if(lastYn=='Y') {
									$this.validateElasFlexitimeAppl();
								} else {
									$("#alertText").html("저장되었습니다.");
									$("#alertModal").on('hidden.bs.modal',function(){
										if($this.flexibleEmp.workTypeCd!='ELAS')
											$this.getFlexibleWorktimeInfo();
										$("#alertModal").off('hidden.bs.modal');
										$("#flexibleDayPlan").find("form").removeClass('was-validated');
									});
			  	  	         		$("#alertModal").modal("show"); 
								}
								
							} else {
								$("#alertText").html(data.message);
								$("#alertModal").on('hidden.bs.modal',function(){
									$("#alertModal").off('hidden.bs.modal');
									$("#flexibleDayPlan").find("form").removeClass('was-validated');
								});
		  	  	         		$("#alertModal").modal("show"); 
							}
							
						},
						error: function(e) {
							$("#loading").hide();
							console.log(e);
							$("#alertText").html("저장 시 오류가 발생했습니다.");
	  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	  	         			$("#alertModal").off('hidden.bs.modal');
	  	  	         		});
	  	  	         		$("#alertModal").modal("show"); 
						}
					});

	         	},
	         	validateElasFlexitimeAppl : function() { //탄근제 확인요청 시 체크
	           		var $this = this;
	         		var validYn = true;
	         		var msg = "";
	         		
	           		//근무제 신청 기간 내의 시간 다 입력했는지 체크
	           		if($this.flexibleEmp!=null && $this.flexibleEmp.hasOwnProperty('dayWorks') && $this.flexibleEmp.dayWorks.length>0) {
	           			$this.flexibleEmp.dayWorks.map(function(d){
	           				d.plans.map(function(p){
	           					if(p.valueMap.timeTypeCd=='BASE' && d.holidayYn=='N' && (p.valueMap.shm=='' || p.valueMap.ehm==''))  {
	           						validYn = false;
	           						msg = moment(d.day).format('YYYY-MM-DD') + "의 출/퇴근 시간이 누락되었습니다.";
	           					}
			           				
	           				});
		           			
		           		});
	           		} else {
	           			validYn = false;
	           			msg = "출/퇴근 시간이 누락되었습니다.";
	           		}
	           		
	           		//사유 입력 했는지 체크
	           		if(calendarLeftVue.applInfo.reason=='') {
	           			validYn = false;
	           			msg = "사유를 입력해 주세요.";
	           		}
	           		
	           		if(validYn) {
	           			calendarLeftVue.flexitimeAppl($this.flexibleEmp.workTypeCd);
	           		} else {
	           			$("#alertText").html(msg);
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show");
	           		}
	           		
	           	},
		        getFlexibleWorktimeInfo : function(){ //해당 근무제의 약정 근로시간, 계획 시간 조회
		        	var $this = this;
		        
		        	var symd = moment($this.selectedWorkday.start).format('YYYYMMDD');
		        	var eymd = moment($this.selectedWorkday.end).format('YYYYMMDD');
		        
		        	$(".loading-spinner").show();
		        	
					var param = {
						flexibleEmpId: $this.flexibleEmp.flexibleEmpId,
	   		    		ymd : moment(symd).format('YYYYMMDD'),
	   		    		symd : symd,
	   		    		eymd : eymd
	   		    	};
			    		
			    	Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp/workTermTime",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						success: function(data) {
							$(".loading-spinner").hide();
							if(data!=null && data.hasOwnProperty('workTermTime')) {
								calendarLeftVue.flexibleAppl.totalWorkMinute = data.workTermTime.totalWorkMinute;
								calendarLeftVue.flexibleAppl.planWorkMinute = data.workTermTime.planWorkMinute;
							}
						},
						error: function(e) {
							$(".loading-spinner").hide();
							calendarLeftVue.flexibleAppl.totalWorkMinute = '';
							calendarLeftVue.flexibleAppl.planWorkMinute = '';
						}
					});
				}
  		    }
   	});
</script>

