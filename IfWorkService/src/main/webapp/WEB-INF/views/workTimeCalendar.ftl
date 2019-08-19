<div id="timeCalendar" class="calendar-wrap" v-cloak>
	<!-- 연장근무신청 modal start -->
    <div class="modal fade show" id="overtimeAppl" tabindex="-1" role="dialog">
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
                                            <span id="overtime" class="time"></span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control  datetimepicker-input form-control-sm mr-2" id="sDate" data-toggle="datetimepicker" data-target="#sDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sTime" data-toggle="datetimepicker" data-target="#sTime" autocomplete="off" required>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control  datetimepicker-input form-control-sm mr-2" id="eDate" data-toggle="datetimepicker" data-target="#eDate" placeholder="연도-월-일" autocomplete="off" required>
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
                                        <option :value="reason.codeCd" v-for="reason in reasons">{{reason.codeNm}}</option>
                                    </select>
                                </div>
                                <div class="form-group col-12">
                                    <label for="reason">설명</label>
                                    <textarea class="form-control" id="reason" rows="3"
                                        placeholder="팀장 확인 시에 필요합니다." required></textarea>
                                </div>
                            </div>
                            <div class="inner-wrap" v-show="result.holidayYn=='Y'">
                                <div class="title mb-2">휴일대체방법</div>
                                <div class="desc">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="subYnY" name="subYn" class="custom-control-input" value="Y" @change="changeSubYn($event.target.value)" required>
                                        <label class="custom-control-label" for="subYnY">휴일대체</label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="subYnN" name="subYn" class="custom-control-input" value="N" @change="changeSubYn($event.target.value)" required>
                                        <label class="custom-control-label" for="subYnN">위로금/시급지급</label>
                                    </div>
                                </div>
                            </div>
                            <div class="radio-toggle-wrap" style="display:none;">
                                <hr>
                                <div class="big-title">8시간의 대체 휴일을 지정하세요.</div>
                                <div class="info-box clearfix mt-2">
                                    <div class="title">이전에 신청한 휴일</div>
                                    <div class="desc">2019.06.18(금) 13:00~17:00(4시간) <span class="guide d-sm-block">해당일 근무시간 09:00~12:00</span></div>
                                    <div class="desc">2019.06.18(금) 13:00~17:00(4시간) <span class="guide d-sm-block">해당일 근무시간 09:00~12:00</span></div>
                                </div>
                                <div class="inner-wrap">
                                    <div class="title">대체일시</div>
                                    <div class="desc" v-for="(s, idx) in subYmds">
                                        <div class="form-group clearfix">
                                            <div class="form-row">
                                                <div class="col-11 col-sm-11 col-md-11 col-lg-6" data-target-input="nearest">
                                                    <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subYmd_'+idx" v-model="s.subYmd" data-toggle="datetimepicker" :data-target="'#subYmd_'+idx" placeholder="연도-월-일" autocomplete="off" required>
                                                </div>
                                                <div class="col-11 col-sm-11 col-md-11 col-lg-5 mt-xs-1 mt-sm-1 mt-lg-0 float-right ">
                                                    <div class="form-row">
                                                        <div class="col float-left" data-target-input="nearest">
                                                            <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsSdate_'+idx" v-model="s.subsSdate" data-toggle="datetimepicker" :data-target="'#subsSdate_'+idx" autocomplete="off" required>
                                                        </div>
                                                        <span class="d-inline-block text-center pl-1 pr-2 mt-1">~</span>
                                                        <div class="col float-right" data-target-input="nearest">
                                                            <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" :id="'subsEdate_'+idx" v-model="s.subsEdate" data-toggle="datetimepicker" :data-target="'#subsEdate_'+idx" autocomplete="off" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-1 col-sm-1 col-md-1 col-lg-1 col-md-text-right text-center"><a href="#" class="align-middle" @click="delSubYmd(idx)">삭제</a></div>
                                            </div>
                                        </div>
                                        <div class="guide">*해당일 근무시간은 09:00~18:00 입니다.</div>
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
    <!-- 연장근무신청 상세보기 modal start -->
    <div class="modal fade show" id="overtimeApplDetail" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">연장근로신청</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="title">연장근로시간</div>
                                <div class="desc">
                                    <span class="time-wrap">
                                        <i class="fas fa-clock"></i>
                                        <span class="time">
                                        	<template v-if="overtimeAppl.otMinute">
                                        		{{calendarLeftVue.minuteToHHMM(overtimeAppl.otMinute, 'detail')}}
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
                            <hr class="bar">
                        </div>
                        <div class="btn-wrap text-center" v-if="overtimeAppl.applStatusCd=='99'">
                            <button type="button" class="btn btn-default rounded-0">연장근로신청 취소하기</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 연장근무신청 상세보기 modal end -->
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
		
        $('#sTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#eTime').datetimepicker({
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
  		    	result: {}, //일근무시간
  		    	workday: '', //근무일
  		    	reasons: [], //연장/휴일 근로 사유
  		    	subYmds: [], //대체휴일
  		    	overtimeAppl: {}
  		    },
  		    mounted: function(){
  		    	<#if workday?? && workday!='' && workday?exists >
  		    		this.workday = moment('${workday}').format('YYYY-MM-DD');
  		    	<#else>
  		    		this.workday = '${today}';
  		    	</#if>
  		    	
  		    	<#if reasons?? && reasons!='' && reasons?exists >
		    		this.reasons = JSON.parse('${reasons?js_string}');
		    	</#if>
  		    	
  		    	this.getFlexibleDayInfo(this.workday);
  		    	
  		    	//근무일 화면 전환
         		$("#workRangeInfo").show();
         		$("#flexibleDayInfo").show();
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		//화면에 보이는 달력의 시작일, 종료일을 파라미터로 넘김
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		calendarLeftVue.calendar = calendar;
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
  		    		//상세보기
  		    		if(info.event.extendedProps.timeTypeCd=='OT') {
  		    			this.viewOvertimeApplDetail(info.event.extendedProps.applId);
  		    		}
  		    	},
  		    	dateClickCallback : function(info){
  		    		if(!info.allDay)
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
							//console.log(data);
							if(data!=null) {
								$this.result = data;
								
								$this.viewDayResults(ymd);
							}
						},
						error: function(e) {
							console.log(e);
							$this.result = {};
						}
					});
  	         	},
  	         	viewOvertimeAppl: function(date){
  	         		var $this = this;
  	         		//1시간 값 세팅
					var sYmd = new Date(date);
					var eYmd = new Date(date);
					eYmd.setHours(eYmd.getHours()+1);
					$("#sDate").val(moment(sYmd).format('YYYY-MM-DD'));
					$("#eDate").val(moment(eYmd).format('YYYY-MM-DD'));
					$("#sTime").val(moment(sYmd).format('HH:mm'));
					$("#eTime").val(moment(eYmd).format('HH:mm'));
					
					$("#overtime").text("1시간");
					
					$("#overtimeAppl").modal("show"); 
  	         	},
  	         	viewOvertimeApplDetail: function(applId){
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
							console.log(data);
							if(data!=null) {
								$this.overtimeAppl = data;
								$("#overtimeApplDetail").modal("show"); 
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
  	         			if($this.result.hasOwnProperty('dayResults') && $this.result.dayResults!=null && $this.result.dayResults!='') {
  	         				var dayResults = JSON.parse($this.result.dayResults);
  	         				//console.log(dayResults);
         					dayResults.map(function(vMap){
         						if(vMap.hasOwnProperty('taaCd') && vMap.taaCd!='') {
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
									
									var title = vMap.timeTypeNm;
									if(vMap.applStatusCd!=null && vMap.applStatusCd!='')
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
								$this.viewOvertimeAppl(info.date);
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
  	         		var forms = document.getElementById('overtimeAppl').getElementsByClassName('needs-validation');
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
  	  	         		
  	  	         		if($this.result.hasOwnProperty('dayResults') && $this.result.dayResults!=null && $this.result.dayResults!='') {
	         				var dayResults = JSON.parse($this.result.dayResults);
     						dayResults.map(function(dayResult){
	  	  	         			if(dayResult.timeTypeCd == 'BASE'){
		  	  	         			var baseSdate = moment(baseWork.sDate).format('YYYY-MM-DD HH:mm');
			  	         			var baseEdate = moment(baseWork.eDate).format('YYYY-MM-DD HH:mm');
			  	         			if(moment(baseSdate).diff(otSdate)<=0 && moment(otSdate).diff(baseEdate)<=0 
			  	         					|| moment(baseSdate).diff(otEdate)<=0 && moment(otEdate).diff(baseEdate)<=0 )
			  	         				isBase = true;
	  	  	         			}
	  	  	         		});
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
        				workTypeCd : 'OT',
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
  	         	},
  	         	changeSubYn: function(val){
  	         		var $this = this;
  	         		
  	         		if($this.subYmds.length==0)
	         			$this.addSubYmd();
  	         		
  	         		if (val == "Y") { //휴일대체
  	                    $(".radio-toggle-wrap").show(500);
  	                }
  	                else if(val == "N") { //수당지급
  	                    $(".radio-toggle-wrap").hide(500);
  	                }
  	                else {
  	                    $(".radio-toggle-wrap").hide(500);
  	                }
  	         	},
  	         	updateValue: function(id, val){
  	         		var $this = this;
  	         		var key = id.split('_');
  	         		if(key!=null && key!='undefined' && key.length>0) {
  	         			$this.subYmds[key[1]][key[0]] = val; 
  	         		}
  	         	},
  	         	addSubYmd: function(){
  	         		var newSubYmd = {
  	         			subYmd: '',
  	         			subsSdate: '',
  	         			subsEdate: ''
  	         		};
  	         		
  	         		this.subYmds.push(newSubYmd);
  	         	},
  	         	delSubYmd: function(idx){
  	         		this.subYmds.splice(idx,1);
  	         	}
  		    }
   	});
   	
  	//동적으로 추가하는 요소에 datetimepicker를 그리기 위함
  	$('body').on('focus',"input[id^='subYmd']", function(){
   		var $this = this;
 		$(this).datetimepicker({
 			format: 'YYYY-MM-DD',
 		    language: 'ko'
 		});
 		
 		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				timeCalendarVue.updateValue($($this).attr('id'), moment(e.date).format('YYYY-MM-DD'));
 			}
 		});
   	});
  	
   	$('body').on('focus',"input[id^='subsSdate']", function(){
		$(this).datetimepicker({
			format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'top'
            }
		});
		
		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				timeCalendarVue.updateValue($($this).attr('id'), moment(e.date).format('HH:mm'));
 			}
 		});
   	});
   	
   	$('body').on('focus',"input[id^='subsEdate']", function(){
		$(this).datetimepicker({
			format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'top'
            }
		});
		
		$(this).on("change.datetimepicker", function(e){
 			if(e.date!=null && e.date!='undefined' && e.date!='') {
 				timeCalendarVue.updateValue($($this).attr('id'), moment(e.date).format('HH:mm'));
 			}
 		});
   	})

   	//날짜,시간 변경 시 연장근로시간 계산
   	$('#sDate, #eDate, #sTime, #eTime').on("change.datetimepicker", function(e){
   		if($("#sDate").val()!='' && $("#eDate").val()!='' && $("#sTime").val()!='' && $("#eTime").val()!='') {
   			var otSdate = moment($("#sDate").val()+' '+$("#sTime").val()).format('YYYY-MM-DD HH:mm');
         	var otEdate = moment($("#eDate").val()+' '+$("#eTime").val()).format('YYYY-MM-DD HH:mm');
         	
         	if(moment(otEdate).diff(otSdate)<0) {
         		$("#alertText").html("종료일이 시작일보다 작습니다.");
         		$("#alertModal").on('hidden.bs.modal',function(){
         			$("#alertModal").off('hidden.bs.modal');
         			$("#sDate").val('');
         			$("#eDate").val('');
         			$("#sTime").val('');
         			$("#eTime").val('');
         		});
         		$("#alertModal").modal("show"); 
         	} else {
         		var minutes = moment(otEdate).diff(otSdate, 'minutes');
             	$("#overtime").text(calendarLeftVue.minuteToHHMM(minutes, 'detail'));
         	}
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

