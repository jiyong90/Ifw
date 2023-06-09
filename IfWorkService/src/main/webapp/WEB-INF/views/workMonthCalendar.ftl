<div id="monthCalendar" class="calendar-wrap" v-cloak>
	<div id="calendar-popover" class="popover-inner-wrap" style="display:none;">
		<div class="msg">시작일로 지정</div>
		<span id="startDaySelect" class="btn btn-default btn-flat btn-sm mt-1">
			<input id="startDay" type="hidden" value="">확인
		</span>
	</div>
    <div id='calendar-container'>
		<full-calendar ref="fullCalendar" :custombuttons="customButtons" :header="header" :navlinks="t" :events="events" @update="renderCallback" @navlinkdayclick="navLinkDayClickCallback" @datesrender="datesRenderCallback" @dateclick="dateClickCallback" @dayrender="dayRenderCallback" @select="selectCallback" @eventrender="eventRenderCallback" @eventclick="eventClickCallback"></full-calendar>
    </div>
</div>

<script type="text/javascript">
	var _dList;
	var _annualCreateCnt = 0;
	var _annualNotUsedCnt = 0;

	$(function () {
		//	휴가신청 Select 정보 조회
		var taaTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/wtmAnnualCreate/getTaaCodelist2", ""), "선택");
		$("#annualTaCd").html(taaTypeCdList[2]);

		var modalTaaTypeList = stfConvCode(codeList("${rc.getContextPath()}/wtmRega/getTaaCodelist", ""), "선택");
		$("#taaTypeCd").html(modalTaaTypeList[2]);

		//	휴가신청 변경 이벤트
		$('#annualTaCd').on('change', function(){
			modalCalendarVue.resetAnnualCreateModal();

			var masterCd = $(this).val();
			var taaTypeCdDetailList = stfConvCode(ajaxCall("${rc.getContextPath()}/wtmAnnualCreate/taaTypeCd", "taaTypeCd=" + masterCd,false).DATA, "전체");
			_dList = ajaxCall("${rc.getContextPath()}/wtmAnnualCreate/taaTypeCd", "taaTypeCd=" + masterCd,false).DATA;
			$("#annualTaDetailCd").html(taaTypeCdDetailList[2]);
			$('#annualTotalCnt').val(0);

		});

		//	휴가신청 상세 변경 이벤트
		$('#annualTaDetailCd').on('change', function(){

			console.log('휴가신청 상세 변경 이벤트');
			var _annualTaCd       = $('#annualTaCd').val();
			var _annualTaDetailCd = $('#annualTaDetailCd').val();

			//	연차 총일수 조회
			var param = {
				taCd : _annualTaCd,
				taDetailCd : _annualTaDetailCd
			}


			Util.ajax({
				url: "${rc.getContextPath()}/wtmAnnualUsed/myAnnualCreateCnt",
				type: "POST",
				contentType: 'application/json',
				data: JSON.stringify(param),
				dataType: "json",
				success: function(data) {
					if(data!=null && data.status=='OK') {

						_annualCreateCnt = data.totalCnt;
						_annualNotUsedCnt = data.noUsedCnt;

						//	발생일수
						$('#annualCreateCnt').val(_annualCreateCnt);
						$('#annualNotUsedCnt').val(_annualNotUsedCnt);

					}
				},
				complete : function(data){
					var eq_sDt = '#annualInputSDate';
					var eq_eDt = '#annualInputEDate';
					var _length = $(eq_sDt).length;

					if(_annualTaDetailCd == ''){
						$('#annualDepList').hide();
					}else{
						$('#annualDepList').show();

						if(_dList){
							_dList.forEach(function(obj){
								if(obj.code == _annualTaDetailCd){
									switch (obj.requestTypeCd){
										case "P":
										case "A":
											$(eq_sDt).attr('readonly', false);
											$(eq_eDt).attr('readonly', true);
											syncAnnualDate();
											break;
										case "D":
											$(eq_sDt).attr('readonly', false);
											$(eq_eDt).attr('readonly', false);
											break;
									}
								}
							});
						}

					}
				},
				error: function(e) {
					isuAlert("휴가사용 일자가 없습니다");
					return;
				}
			});




		});


		//	출장/비상근무 추가  button event
		$('#addRowTaaBtn').on('click', function(){
			modalCalendarVue.addTaaRow();
		});

		//	휴가신청 추가 button event
		$('#addRowAnnualBtn').on('click', function(){
			modalCalendarVue.addAnnualRow();
		});

		$('#sDate, #eDate').datetimepicker({
			format: 'YYYY-MM-DD',
			language: 'ko'
		});

		$('#annualInputSDate, #annualInputEDate').datetimepicker({
			format: 'YYYY-MM-DD',
			language: 'ko'
		});
	});


   	var monthCalendarVue = new Vue({
   		el: "#monthCalendar",
		components : {
			FullCalendar : fullCalendarComponent
	    },
	    data : {
	    	t: true,
	    	customButtons: {
	        	legend: {}
	       	},
	    	header: {
	    		left: 'prev,next',
	        	center: 'title',
	        	right: 'legend'
	    	},
	    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
	    	prevEdate: '', //이전 근무제 종료일
	    	monthFlexitimeList: [], //해당 월의 근무제 리스트
	    	events: [],
	    	dayWorks: {}
	    },
	    watch: {
	    	monthFlexitimeList : function(val, oldVal) {
	    		if(val!=null && val.length>0 && $(".fc-legend-button").length>0) {
	    			//적용 중인 유연근무제 리스트
    				calendarTopVue.drawLegend(val, false);
	    		} 
	    	}
	    },
	    mounted: function(){
	    	//근무 계획 작성 화면 전환
	    	$("#workRangeInfo").show();
     		$("#workDayInfo").show();
	    },
	    methods : {
	    	renderCallback: function(){
	    		//기존에 시행한 유연근무 기간의 경우 선택하지 못하게 함
	    		this.selectAllow();
	    		
	    		//화면에 보이는 달력의 시작일, 종료일을 파라미터로 넘김
	    		var calendar = this.$refs.fullCalendar.cal;
	    		calendarLeftVue.calendar = calendar;
	    		document.querySelector(".fc-legend-button").innerHTML='';
	    	},
	    	navLinkDayClickCallback: function(info){
	    		if(calendarLeftVue.useYn!='Y')
         			location.href='${rc.getContextPath()}/${type}/${tsId}/views/workCalendar?calendarType=Time&date='+moment(info).format('YYYYMMDD');
	    	},
	    	datesRenderCallback: function(info){
	    		var $this = this;
	    		var calendar = this.$refs.fullCalendar.cal;
	    		
	    		//document.querySelector(".fc-legend-button").innerHTML='';
	    		if(info.view.type == 'dayGridMonth' && calendar.getOption('selectAllow')!=undefined) { //month change
	    			var d = moment(calendar.getDate()).format('YYYY-MM-DD');
	    			calendar.select(d);
	    			
	    			//월이 바뀌었을 때 달력에 남아있는 event 지우기
	    			if(calendarTopVue.selectedFlexibleStd!=null && Object.keys(calendarTopVue.selectedFlexibleStd).length>0
	    					&& (moment(d).diff(calendar.view.currentStart)<0 || moment(calendar.view.currentEnd).diff(d)<0)) {
	    				var event = calendar.getEventById('workRange.'+calendarTopVue.selectedFlexibleStd.workTypeCd+'.new');
	    				if(event!=null) {
		       				event.remove();
		       			}
	    			}
	    		
	    			$this.markAdditionalInfo();
	 		    	this.getFlexibleEmpList(calendar.view.activeStart, calendar.view.activeEnd);
	 		    	//console.log(calendar.getEvents());
		    	}
	    	},
	    	dateClickCallback : function(info){
	    		var $this = this;
  		    	
	    		//시작일 지정 팝업
	    		if(calendarLeftVue.useYn=='Y') {
	    			$('.popover').remove();
	    			
	    			if($this.prevEdate==''||moment(info.dateStr).diff($this.prevEdate)>0){
		    			$(info.dayEl).popover({
	  		    			html: true,
					    	placement: 'bottom',
						    //animation:true,
				            //delay: 100,
				            content: function(){
				            	$("#startDay").val(info.dateStr);
				            	return $("#calendar-popover").html();
				            },
				            container:'#calendar'
					    });
		    			$(info.dayEl).popover('show');
	    			}
	    		} else {
	    			calendarLeftVue.selectedDate = info.dateStr;
	    		}
	    	},
	    	selectCallback : function(info){
	    		var date = moment(info.start).format('YYYYMMDD');
	    		calendarLeftVue.selectedDate = date;
	    		
	    		if($("#workRangeInfo").is(':visible'))
	    			calendarLeftVue.getFlexibleRangeInfo(date);
	    		
	    		if($("#workDayInfo").is(':visible'))
	    			calendarLeftVue.getFlexibleDayInfo(date);
	    	},
	    	eventRenderCallback : function(info){
	    		/* if(info.event.id.indexOf('workRange.')==0 && info.isStart) {
	    			//$(info.el).prev().css('border-right', '1px solid red');
	    			//$(info.el).siblings("td").css('border-right', '1px solid red');
	    			console.log(info.event.id);
	    			var key = info.event.id.split(".");
	    			var workTypeCd = key[1];
	    			
	    			//$('td').find(".fc-day[data-date='"+moment(yesterday).format('YYYY-MM-DD')+"']").css('border-right', '1px solid red');
	    			$('td').find(".fc-day[data-date='"+moment(info.event.start).format('YYYY-MM-DD')+"']").prepend('<div class="fc-triangle start '+workTypeCd+'"></div>');
	    		}  */
	    		
	    		$(info.el).find('.fc-title').html(info.event.title);
	    		
	    		//기존에 신청한 근무제
	    		if(info.event.id.indexOf('workRange.')==0) {
	    			var key = info.event.id.split(".");
	    			var workTypeCd = key[1];
	    			
	    			var borderDiv = '';
	    			
	    			if(info.isStart) {
	    				borderDiv = '<div class="fc-border start '+workTypeCd+'"></div>';
	    			} else if(info.isEnd) {
	    				borderDiv = '<div class="fc-border end '+workTypeCd+'"></div>';
	    			} else {
	    				borderDiv = '<div class="fc-border '+workTypeCd+'"></div>';
	    			}
	    			$(info.el).empty();
	    			$(info.el).prepend(borderDiv);
	    		}
	    	},
	    	eventClickCallback : function(info){
	    	},
	    	dayRenderCallback : function(info){ //day render
	    		var date = info.date;
         		$('td').find(".fc-day-top[data-date='"+moment(date).format('YYYY-MM-DD')+"'] .fc-day-number").text(moment(date).format('D'));
         	},
         	addEvent : function(Obj){
         		if(Obj!=null) {
         			var calendar = this.$refs.fullCalendar.cal;
         			var event = calendar.getEventById(Obj.id);
         			
	       			/* if(event==null) {
	       				//이벤트 새로 생성
	       				calendar.batchRendering(function() {
		  	         			calendar.addEvent(Obj);
		  	         		});
	       			} else {
	       				//이벤트 날짜 수정
	       				calendar.batchRendering(function() {
	        				event.setStart(Obj.start);
	        				event.setEnd(Obj.end);
	        				event.setProp("startRecur",Obj.startRecur);
	        				event.setProp("endRecur",Obj.endRecur);
	       				});
	       			} */
	       			
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
        			
      				calendar.batchRendering(function() {
      					calendar.addEventSource(eMap);
      				});
         		}
         	},
 	        getFlexibleEmpList : function(sYmd, eYmd){ //해당월의 근무제 정보
 	         	var $this = this;
 		    	var calendar = this.$refs.fullCalendar.cal;

				var param = {
   		    		sYmd : moment(sYmd).format('YYYYMMDD'),
   		    		eYmd : moment(eYmd).format('YYYYMMDD')
   		    	};
  		    		
				$this.monthFlexitimeList = [];
  		    	Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/list",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						if(data.status=='OK' && data.flexibleList!=null) {
							$this.monthFlexitimeList = data.flexibleList;
							$this.monthFlexitimeList.map(function(f){
								var sYmd = moment(f.sYmd).format('YYYY-MM-DD');
								var eYmd = new Date(moment(f.eYmd).format('YYYY-MM-DD'));
								eYmd.setDate(eYmd.getDate()+1);
								eYmd = moment(eYmd).format('YYYY-MM-DD');
								
								var classNames = [];
								//classNames.push(f.applCd);
								classNames.push(f.workTypeCd);
											
								var workTypeCd = f.workTypeCd;
								if(f.workTypeCd==null || f.workTypeCd=='' || !f.hasOwnProperty('workTypeCd'))
									workTypeCd = f.applCd;
								
								//console.log('-----------------------------------'+sYmd);
								//console.log(calendarLeftVue.applInfo.useSymd!=sYmd);
								//console.log(!f.hasOwnProperty('applStatusCd'));
								//console.log(f.applStatusCd!='11');
								
								//현재 선택된 임시저장건만 빼고 그리기
								if(calendarLeftVue.applInfo.useSymd!=sYmd 
										|| !f.hasOwnProperty('applStatusCd') || f.applStatusCd!='11') {
									$this.addEvent({
										//id: 'workRange.'+f.applCd+'.'+sYmd,
										id: 'workRange.'+workTypeCd+'.'+sYmd,
										start: sYmd,
			  		  		        	end: eYmd,
			  		  		        	rendering: 'background'
			  		  		        	//classNames: classNames
									});
								}
								
								//근무계획
								//if(f.hasOwnProperty('flexibleEmp') && ($this.dayWorks[f.sYmd]==null || $this.dayWorks[f.sYmd]==undefined)) {
								if(f.hasOwnProperty('flexibleEmp')) {
									$this.dayWorks[f.sYmd] = f.flexibleEmp;
									$this.addDayWorks(f.sYmd, f.flexibleEmp);
								} 
							});
							
							if(Object.keys($this.dayWorks).length>0) {
				         		$.each($this.dayWorks, function(k, v){
						         	v.map(function(dayWork){
						         		//근무일
						         		//if(dayWork.hasOwnProperty("holidayYn") && dayWork.holidayYn!='Y') {
											dayWork.plans.map(function(plan){
												var day = moment(plan.key).format('YYYY-MM-DD');
												
					 		    				if(plan.valueMap.hasOwnProperty("taaCd") && plan.valueMap.taaCd!='' && plan.valueMap.hasOwnProperty("timeTypeCd") && plan.valueMap.timeTypeCd!='EXCEPT') {
					 		    					//개인 근태
					  		    					/* var taaEvent = {
														id: day,
							    						title: "<div class='dot work-type'><span>" + plan.label + "</span></div>",
							    						start: day,
							    						end: day
							    					};
						  		    				events.push(taaEvent); */
						  		    				
						  		    				if($(".fc-day-top[data-date='"+day+"'] span.fc-holiday").length==0) {
					 		    						$('td').find(".fc-day-top[data-date='"+day+"']").prepend("<span class='fc-holiday'>"+plan.label+"</span>");
						  		    				}else {
						  		    					if($(".fc-day-top[data-date='"+day+"'] span.fc-holiday").text().indexOf(plan.label)==-1)
						  		    						$('td').find(".fc-day-top[data-date='"+day+"'] span.fc-holiday").append(' '+plan.label);
						  		    				}
						  		    				$('td').find(".fc-day-top[data-date='"+day+"'] span.fc-holiday").css({"color":"#4d84fe"});
					  		    				
					 		    				} 
											});
						         		//} 
									});
				         		});
			        		}
							
						}
					},
					error: function(e) {
						$this.monthFlexitimeList = [];
					}
				});
  		    	
 	        },
 	        markAdditionalInfo : function(){ //회사 휴일 달력에 표기
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
										$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").prepend("<span name='companyHoliday' class='fc-holiday'>"+cal.holidayNm+"</span>");
									$('td').find(".fc-day-top[data-date='"+cal.sunYmd+"']").css({"color":"#FF0000"});
								//}
							});
						}
						
					},
					error: function() {
						
					}
				});
 		    	
 	        },
	        addDayWorks : function(symd, v){ //근무시간 생성
	         	var $this = this;
	        	//if(Object.keys($this.dayWorks).length>0) {
		         	var events = [];
		         	//$.each($this.dayWorks, function(k, v){
			         	v.map(function(dayWork){
			         		//근무일
			         		//if(dayWork.hasOwnProperty("holidayYn") && dayWork.holidayYn!='Y') {
			         			var ymd = '';
			         			var isBase = false;
								dayWork.plans.map(function(plan){
									var day = moment(plan.key).format('YYYY-MM-DD');
								
		 		    				//if(plan.valueMap.hasOwnProperty("taaCd") && plan.valueMap.taaCd!='') {
		 		    					//개인 근태
		  		    					/* var taaEvent = {
											id: day,
				    						title: "<div class='dot work-type'><span>" + plan.label + "</span></div>",
				    						start: day,
				    						end: day
				    					};
			  		    				events.push(taaEvent); */
			  		    				
			  		    			//	if($(".fc-day-top[data-date='"+day+"'] span.fc-holiday").length==0)
		 		    				//		$('td').find(".fc-day-top[data-date='"+day+"']").prepend("<span class='fc-holiday'>"+plan.label+"</span>");
			  		    			//	else
			  		    			//		$('td').find(".fc-day-top[data-date='"+day+"'] span.fc-holiday").append(' '+plan.label);
			  		    			//	$('td').find(".fc-day-top[data-date='"+day+"'] span.fc-holiday").css({"color":"#4d84fe"});
		  		    				
		 		    				//} else if(plan.valueMap.hasOwnProperty("shm") && plan.valueMap.shm!='' || plan.valueMap.hasOwnProperty("ehm") && plan.valueMap.ehm!='') {
		 		    				if((plan.valueMap.taaCd==undefined || plan.valueMap.taaCd=='') 
		 		    						&& (plan.valueMap.hasOwnProperty("shm") && plan.valueMap.shm!='' || plan.valueMap.hasOwnProperty("ehm") && plan.valueMap.ehm!='')) {
		 		    					//타이틀 알파벳 순서대로 이벤트가 정렬됨 ㅠㅠ
		  		    					if(plan.valueMap.hasOwnProperty("timeTypeCd") && (plan.valueMap.timeTypeCd=='FIXOT' || plan.valueMap.timeTypeCd=='NIGHT' || plan.valueMap.timeTypeCd=='OT')) { //고정ot, 조출, 야간은 색 다르게 표기
		  		    						timeTypeClass = ' later';
		  		    					} else {
		  		    						timeTypeClass = ' general';
		  		    					}
		 		    					
		 		    					
		 		    					//계획시간
										var timeEvent = {
											id: day,
				    						title: "<span style='display:none;'>"+moment(day+' '+plan.valueMap.shm).format('YYYY-MM-DD HH:mm')+"</span>"+"<div class='dot time "+timeTypeClass+"'><span>"+ plan.label + "</span></div>",
				    						start: day,
				    						end: day
				    					};
			  		    				events.push(timeEvent);
		 		    				}
								});
			         		//} 
						});
		         	//});
		         	$this.addEventSource('dayWorks'+symd, events);
	        	//}
	        },
			selectAllow : function(){
	    		var $this = this;
	    		var calendar = $this.$refs.fullCalendar.cal;
	    		
	    		//기존에 시행한 유연근무 기간의 경우 선택하지 못하게끔 함
         		calendar.setOption('selectAllow', function(i){
  		    		if($this.prevEdate!=null && $this.prevEdate!='')
  		    			return moment(i.startStr).diff($this.prevEdate)>0;
  		    		else
  		    			return true;
         		});
	    	},
			/* getWorkRangeInfo : function(ymd){ //오늘 또는 선택한 기간의 근무제 정보
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
						// if(data.status=='OK' && data.workTermTime!=null) {
						//	$this.workTermTime = data.workTermTime;
							
						//} 
					},
					error: function(e) {
						$this.workTermTime = {};
					}
				});
 	        }, */
         	getPrevFlexitime : function(){ //이전에 시행한 근무제 기간 조회
         		var $this = this;
         		Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/prev",
					type: "GET",
					contentType: 'application/json',
					//data: param,
					dataType: "json",
					success: function(data) {
						$this.prevEdate = '';
						if(data.status=='OK' && data.prevFlexible!=null) {
							$this.prevEdate = data.prevFlexible.eYmd;
						}
					},
					error: function(e) {
						console.log(e);
						$this.prevEdate = '';
					}
				});
         	},
         	changeWorkRange : function(){ //근무기간 변경에 따라 background 변경
         		var $this = this;
         		var calendar = $this.$refs.fullCalendar.cal;
         		
         		if(calendarLeftVue.applInfo.useSymd!=null && calendarLeftVue.applInfo.useSymd!='' && calendarLeftVue.applInfo.workRange!=null && calendarLeftVue.applInfo.workRange!='') {
         			var workDateRange = calendarLeftVue.applInfo.workRange.split('_');
	       			var eYmd = new Date(calendarLeftVue.applInfo.useSymd);
	       			if(workDateRange[1]=='week') {
	       				eYmd.setDate(eYmd.getDate()+ (workDateRange[0]*7));
	       			} else if(workDateRange[1]=='month') {
	         			eYmd.setMonth(eYmd.getMonth()+ (Number(workDateRange[0])));
	       			}
	       			
	       			var classNames = [];
					classNames.push(calendarTopVue.selectedFlexibleStd.workTypeCd);
					
					//근무 요일이 아닌 경우 제외하고 event 생성
					/* var workDaysOpt = [];
					$.each(calendarTopVue.flexibleStd.workDaysOpt, function(k, v){
						if(v==true) {
							workDaysOpt.push(k-1);
						}
					}); */
					
					//임시저장된 건이 있으면 이벤트 삭제하고 재생성
					/* if(calendarLeftVue.flexibleAppl.hasOwnProperty("applStatusCd") && calendarLeftVue.flexibleAppl.applStatusCd=='11') {
						var eventId = 'workRange.'+ calendarLeftVue.flexibleAppl.applCd + '.' + moment(calendarLeftVue.flexibleAppl.sYmd).format('YYYY-MM-DD');
						var event = calendar.getEventById(eventId);
						//console.log(eventId);
						//console.log(event);
						if(event!=null)
							event.remove();
					} */
					//console.log('new event:' + 'workRange.'+calendarTopVue.selectedFlexibleStd.workTypeCd+'.'+calendarLeftVue.applInfo.useSymd);
	       			$this.addEvent({
	       				id: 'workRange.'+calendarTopVue.selectedFlexibleStd.workTypeCd+'.new',
	 		    		start: calendarLeftVue.applInfo.useSymd,
	 		        	end: moment(eYmd).format('YYYY-MM-DD'),
	 		        	rendering: 'background'
	 		        	//daysOfWeek: workDaysOpt,
	 		        	//startRecur: calendarLeftVue.applInfo.useSymd,
	 		        	//endRecur: moment(eYmd).format('YYYY-MM-DD'),
	 		        	//classNames: classNames
		  		    });
	       			
	       			eYmd.setDate(eYmd.getDate()-1);
	       			calendarLeftVue.applInfo.useEymd = moment(eYmd).format('YYYY-MM-DD');
	       			
	       			calendar.gotoDate(calendarLeftVue.applInfo.useSymd);
	       			
	       			//탄근제 제외하고 임시저장
	       			if(calendarTopVue.selectedFlexibleStd.workTypeCd!='ELAS')
	       				calendarLeftVue.flexitimeApplImsi();
         		}
         	},
         	startDaySelect: function(selectedDay){
         		var $this = this;
         		
         		var flexStd = calendarTopVue.selectedFlexibleStd;
         	
         		calendarLeftVue.applInfo.useSymd = selectedDay;
    			
    			//결재라인
    			calendarLeftVue.applLine = calendarLeftVue.getApplLine(flexStd.workTypeCd);
    			
    			$("#applyBtn").hide();
    			$("#flexibleAppl").find(".sub-wrap").show();
    			
    			//신청 화면 전환
    			//calendarLeftVue.setUsedTermOpt();
    			
    	   		if(flexStd.hasOwnProperty("usedTermOpt") && flexStd.usedTermOpt!=null) {
    	   			if(!flexStd.hasOwnProperty("applId") || flexStd.applId==null || flexStd.applId=='') {
    	   				//적용기간은 첫번째 항목으로 기본 세팅
    	   				var workDateRangeItem = flexStd.usedTermOpt; 
        	   			
        	   			if(workDateRangeItem.hasOwnProperty("value")&&workDateRangeItem.value!=null) {
        	   				calendarLeftVue.applInfo.workRange = workDateRangeItem.value;
        	   				monthCalendarVue.changeWorkRange();
        	   			}
    	   			} else {
    	   				//임시저장된 근무제 보여주기
    	   				calendarLeftVue.applInfo.applId = flexStd.applId;
    	   			
    	   				//종료일자 세팅	
    	   				if(flexStd.hasOwnProperty("eYmd")&&flexStd.eYmd!=null&&flexStd.eYmd!='') {
    	   					calendarLeftVue.applInfo.useEymd = moment(flexStd.eYmd).format('YYYY-MM-DD');
    	   					
    	   					var usedTermOpt = flexStd.usedTermOpt;
    	   					flexStd.usedTermOpt = usedTermOpt;
        	   				
    	   					usedTermOpt.map(function(u){
    	   						var eYmd = new Date(selectedDay);
    	   						var value = u.value.split('_');
    	   						
            	       			if(value[1]=='week') {
            	       				eYmd.setDate(eYmd.getDate()+ (value[0]*7));
            	       			} else if(value[1]=='month') {
            	         			eYmd.setMonth(eYmd.getMonth()+ (Number(value[0])));
            	       			}
            	       			
            	       			eYmd.setDate(eYmd.getDate()-1);
            	       			
            	       			if(calendarLeftVue.applInfo.useEymd == moment(eYmd).format('YYYY-MM-DD')) {
            	       				calendarLeftVue.applInfo.workRange = u.value;
            	       				monthCalendarVue.changeWorkRange();
            	       			}
            	       				
    	   					});
    	   				}
    	   			}
    	   			
    	   		}
         	
         	}
    	}
   	});

	$(document).on("click", "#startDaySelect", function() {
		$('.popover').remove();
		
		var selectedDay = $("#startDay").val();
		
		//기존에 신청중이거나 적용된 근무정보 있는지 
		var isExist = false;
		monthCalendarVue.monthFlexitimeList.map(function(f){
			if(f.baseWorkYn=='N' && (moment(f.sYmd).diff(selectedDay)<=0 && moment(selectedDay).diff(f.eYmd)<=0)) {
				isExist = true;
			}
		});
		
		if(!isExist) {
			monthCalendarVue.startDaySelect(selectedDay);
		} else {
			$("#alertText").html("신청중인 또는 이미 적용된 근무정보가 있습니다.<br>시작일을 다시 지정하세요.");
       		$("#alertModal").on('hidden.bs.modal',function(){
       			$("#alertModal").off('hidden.bs.modal');
       		});
       		$("#alertModal").modal("show"); 
		}
		
   	});

	// 날짜 입력 변경 event
	function syncAnnualDate(){
		var _annualTaDetailCd = $('#annualTaDetailCd').val();
		var _requestTypeCd = "";

		if(_dList) {
			_dList.forEach(function (obj) {
				if (obj.code == _annualTaDetailCd) {
					switch (obj.requestTypeCd) {
						case "P":
						case "A":
							$('#annualInputEDate').val($('#annualInputSDate').val());
							break;
					}
				}
			});
		}
	}

	//	휴가기간 정보 조회
	function getMyAnnualInfo() {
		console.log('getMyAnnualInfo');
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

		//	validation
		var _sDateLen = $('input[name="annualCreateSDate"]').length;

		if(_sDateLen == 0){

			//	총일수
			$('#annualTotalCnt').val(0);

			//	사용일수
			$('#annualUsedCnt').val(0);

			//	잔여일수
			$('#annualNotUsedCnt').val(_annualNotUsedCnt);

			return;
		}

		//	요청
		var $this = this;


		var param = {
			annualCreateSDate : $('input[name="annualCreateSDate"]').vals(),
			annualCreateEDate : $('input[name="annualCreateEDate"]').vals(),
			requestTypeCd : $('input[name="requestTypeCd"]').vals(),
			annualTaCd : $('#annualTaCd').val()
		}


		Util.ajax({
			url: "${rc.getContextPath()}/wtmAnnualUsed/myAnnualInfo",
			type: "POST",
			contentType: 'application/json',
			data: JSON.stringify(param),
			dataType: "json",
			success: function(data) {
				if(data!=null && data.status=='OK') {

					//	총일수
					$('#annualTotalCnt').val(data.totalCnt);

					//	사용일수
					$('#annualUsedCnt').val(data.usedCnt);

					//	잔여일수
					$('#annualNotUsedCnt').val(data.notUsedCnt);

				}
			},
			error: function(e) {
			}
		});
	}

</script>

