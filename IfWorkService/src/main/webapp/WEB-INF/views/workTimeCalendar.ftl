



<#include "/calendar.ftl">
<div id="workTimeCalendar" v-cloak>
    <div class="modal fade" id="flexitimeModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">근무제 적용하기</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>사용할 근무제를 선택하세요.</p>
                    <div class="mt-2">
	                	<!-- <button class="btn btn-outline btn-flat btn-block text-left" type="button" data-toggle="collapse" data-target="#collapWork" aria-expanded="false" aria-controls="collapseExample">근무제</button> -->
                        <!-- <div class="collapse" id="collapWork"> -->
                       	<div>
                            <ul class="list-group select-work-list">
                                <li class="list-group-item" v-for="(f, fIdx) in flexitimeList" @click="selectFlexitime(fIdx)">
                                    <span :class="['tag ' + f.workTypeCd]">{{f.workTypeNm}}</span>
                                    <div class="title">{{f.flexibleNm}}</div>
                                    <div class="desc">근무구간: {{f.workShm}} ~ {{f.workEhm}}<span class="bar"></span>코어구간: {{f.coreShm}} ~ {{f.coreEhm}}</div>
                                </li>
                            </ul>
                        </div>
	                </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" class="btn btn-default" v-if="Object.keys(selectedFlexitime).length>0" @click="applyFlexitime">적용하기</button>
                </div>
            </div>
        </div>
    </div>
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
                    <div class="btn-wrap text-right">
                        <button type="button" class="btn btn-apply" data-toggle="modal" @click="getFlexitimeList">근무제 적용하기</button>
                    </div>
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
                <div id="workRangeInfo" class="work-info-wrap mb-3">
                    <div class="main-title">{{moment(flexibleInfo.sYmd).format("YYYY년 M월 D일")}} ~ {{moment(flexibleInfo.eYmd).format("YYYY년 M월 D일")}}</div>
                    <div class="main-desc">{{flexibleInfo.flexibleNm}}</div>
                    <ul class="sub-wrap">
                        <li>
                            <div class="sub-title">총 계획 근무시간(소정/연장/휴게)</div>
                            <div class="sub-desc">60:40 / 12:00 / 0:30</div>
                        </li>
                        <li>
                            <div class="sub-title">총 실적 근무시간(소정/연장/휴게)</div>
                            <div class="sub-desc">60:40 / 12:00 / 0:30</div>
                        </li>
                        <li>
                            <div class="sub-title">근로시간 산정 구간 평균 주간 근무시간</div>
                            <div class="sub-desc">48시간</div>
                        </li>
                        <li>
                            <div class="sub-title">근무시간표</div>
                            <div class="sub-desc">표준 근무 시간표</div>
                        </li>
                    </ul>
                </div>
                <div id="workDayInfo" class="white-box-wrap mb-3">
                    <div class="work-plan-wrap">
                        <ul class="main-wrap">
                            <li>
                                <div class="main-title">해당일의 근무계획 구분</div>
                                <div class="main-desc">근무일</div>
                            </li>
                            <li>
                                <div class="main-title">계획 근무시간</div>
                                <div class="main-desc">09:00 ~ 21:00 (10:00)</div>
                            </li>
                            <li>
                                <div class="main-title">실적 근무시간</div>
                                <div class="main-desc">09:00 ~ 21:00 (10:00)</div>
                            </li>
                            <li>
                                <div class="main-title">해당일 근태</div>
                                <div class="main-desc">연차, 반차</div>
                            </li>
                        </ul>
                        <div class="sub-wrap">
                            <div class="sub-big-title">근무시간 요약 <span style="font-size:10px;">(근무시간 분류별 합산)</span></div>
                            <ul class="sub-list">
                                <li>
                                    <span class="sub-title"><i class="fas fa-clock"></i>소정근로</span>
                                    <span class="sub-desc">8:00</span>
                                </li>
                                <li>
                                    <span class="sub-title"><i class="fas fa-moon"></i>연장근로</span>
                                    <span class="sub-desc">2:00</span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">일반연장</span>
                                            <span class="sub-desc">1:00</span>
                                        </li>
                                        <li>
                                            <span class="sub-title">야간근무</span>
                                            <span class="sub-desc">1:00</span>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <span class="sub-title"><i class="fas fa-file-alt"></i>근태현황</span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">오후반차</span>
                                            <span class="sub-desc">4:00</span>
                                        </li>
                                        <li>
                                            <span class="sub-title">외출</span>
                                            <span class="sub-desc">2:00</span>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <span class="sub-title"><i class="fas fa-couch"></i>휴식/휴게 현황</span>
                                    <ul class="sub-desc-list">
                                        <li>
                                            <span class="sub-title">무급</span>
                                            <span class="sub-desc">00:30</span>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                    
                        </div>
                    </div>
                </div>
                <div id="applForm" class="white-box-wrap full-height mb-3" style="display:none;">
                    <div class="work-plan-wrap">
                        <div class="main-wrap">
                            <div class="main-title">해당일의 근무계획 구분</div>
                            <div class="main-desc">{{selectedFlexitime.flexibleNm}}</div>
                            <ul class="time-list">
                                <li>
                                    <span class="title">근무가능시간</span>
                                    <span class="desc">{{selectedFlexitime.workShm}} ~ {{selectedFlexitime.workEhm}}</span>
                                </li>
                                <li>
                                    <span class="title">필수근무시간</span>
                                    <span class="desc">{{selectedFlexitime.coreShm}} ~ {{selectedFlexitime.coreEhm}}</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="sub-wrap">
                        <form action="">
                            <div class="form-row no-gutters time-input-form">
                                <div class="form-group col-5">
                                    <label for="useSymd">시작일자</label>
                                    <input type="text" class="form-control" id="useSymd" pattern="\d{1,2}/\d{1,2}/\d{4}" placeholder="YYYY-MM-DD" v-model="useSymd" @change="changeUseSymd">
                                </div>
                                <div class="form-group col-2 text-center">
                                    <lable></lable>
                                    <span>~</span>
                                </div>
                                <div class="form-group col-5">
                                    <label for="useEymd">종료일자</label>
                                    <input type="text" class="form-control" id="useEymd" pattern="\d{1,2}/\d{1,2}/\d{4}" placeholder="YYYY-MM-DD" v-model="useEymd" disabled>
                                </div>
                                <div class="form-group col-12">
                                    <label for="workTime">근무기간</label>
                                    <select id="workTime" class="form-control" v-model="workRange" @change="changeWorkRange">
                                        <option v-for="term in selectedFlexitime.usedTermOpt" :value="term.value">{{term.lable}}</option>
                                    </select>
                                </div>
                                <div class="form-group col-12">
                                    <label for="reson">사유</label>
                                    <textarea class="form-control" id="reson" rows="3" placeholder="팀장 확인 시에 필요합니다."></textarea>
                                </div>
                            </div>
                            <div class="btn-wrap mt-5">
                                <button type="button" class="btn btn-apply btn-block btn-lg">확인요청</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-9">
                <div class="calendar-wrap">
                    <div id='calendar-container'>
                		<full-calendar ref="fullCalendar" :events="events" @update="renderCallback" @datesrender="datesRenderCallback" @select="selectCallback" @eventclick="eventClickCallback"></full-calendar>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
   	var workTimeCalendarVue = new Vue({
   		el: "#workTimeCalendar",
  			components : {
  				FullCalendar : fullCalendarComponent
  		    },
  		    data : {
  		    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
  		    	useYn: 'N', //근무제 적용 여부
  		    	prevEdate: '', //이전 근무제 종료일
  		    	flexibleInfo: {}, //선택한 기간의 근무제 정보
  		    	flexitimeList: [], //사용할 근무제 리스트
  		    	selectedFlexitime: {}, //적용할 근무제
  		    	useSymd: '', //시작일
  		    	useEymd: '',
  		    	workRange: '', //근무기간
  		    	workDaysOpt: [],
  		    	events: [
  		    		{
  		                title: '출근',
  		                start: '2019-07-11T08:23:00',
  		                end: '2019-07-11T08:25:00',
  		                color: '#4d84fe'
  		            },
  		          	{
  		                title: '퇴근',
  		                start: '2019-07-11T21:05:00',
  		                end: '2019-07-11T21:07:00',
  		                color: '#4d84fe'
  		            },
  		    		{
  		            	title: '무급휴게',
  		                start: '2019-07-11T11:30:00',
  		                end: '2019-07-11T13:00:00',
  		                rendering: 'background',
  		                color: '#fc8262'
  		            },
		          	{
		                title: '기본근무',
		                start: '2019-07-11T09:00:00',
		                end: '2019-07-11T18:00:00',
		                color: '#1fc486'
		            },
		            {
  		            	title: '무급휴게',
  		                start: '2019-07-11T18:00:00',
  		                end: '2019-07-11T19:00:00',
  		                rendering: 'background',
  		                color: '#fc8262'
  		            },
  		          	{
  		                title: '연장근무',
  		                start: '2019-07-11T19:00:00',
  		                end: '2019-07-11T21:05:00',
  		                color: '#f75353'
  		            }
  		    	]
  		    },
  		    mounted: function(){
  		    	this.getWorkDayInfo(this.today);
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		//기존에 시행한 유연근무 기간의 경우 선택하지 못하게끔 함
  		    		this.selectAllow();
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
							},
							error: function() {
								
							}
						});
	  		    	}
  		    		
  		    		$this.getWorkRangeInfo(calendar.getDate());
  		    		
  		    	},
  		    	selectCallback : function(info){ //day select
  		    		var $this = this;
  		    	
  		    		//시작일 지정 팝업
  		    		if(this.useYn=='Y' && (this.useSymd==null || this.useSymd=='')) {
  		    			$("#alertText").html(info.startStr + " 시작일 지정");
  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	  	         			$("#alertModal").off('hidden.bs.modal');
  	  	         			$this.useSymd = info.startStr;
  	  	         			
  	  	         			//신청 화면 전환
  		    				$this.viewFlexitimeAppl();
  	  	         		});
  	  	         		$("#alertModal").modal("show"); 
  		    		}
  		    	},
  		    	eventClickCallback : function(info){
  		    		console.log('eventClick');
  		    	},
  		    	dayRenderCallback : function(dayRenderInfo){ //day render
  		    		var date = dayRenderInfo.date;
  	         		$('td').find(".fc-day-top[data-date='"+moment(date).format('YYYY-MM-DD')+"'] .fc-day-number").text(moment(date).format('D'));
  	         	},
  	         	addEvent : function(Obj){
  	         		var calendar = this.$refs.fullCalendar.cal;
  	         		var event = calendar.getEventById(Obj.id);
         			if(event==null) {
         				//이벤트 새로 생성
         				calendar.batchRendering(function() {
  	  	         			calendar.addEvent(Obj);
  	  	         		});
         			} else {
         				//이벤트 날짜 수정
         				event.setStart(Obj.start);
         				event.setEnd(Obj.end);
         			}
  	         	},
				getWorkRangeInfo : function(ymd){ //선택한 근무제 정보
					var $this = this;
  		    		var calendar = this.$refs.fullCalendar.cal;
  		    		
					var param = {
	   		    		ymd : moment(ymd).format('YYYYMMDD')
	   		    	};
   		    		
   		    		Util.ajax({
						url: "${rc.getContextPath()}/flexibleEmp",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							//console.log(data);
							if(data.status=='OK' && data.flexibleInfo!=null) {
								$this.flexibleInfo = data.flexibleInfo;
								
								var sYmd = moment(data.flexibleInfo.sYmd).format('YYYY-MM-DD');
								var eYmd = new Date(moment(data.flexibleInfo.eYmd).format('YYYY-MM-DD'));
								eYmd.setDate(eYmd.getDate()+1);
								eYmd = moment(eYmd).format('YYYY-MM-DD');
								
								var classNames = [];
								classNames.push(data.flexibleInfo.workTypeCd);
								
								$this.addEvent({
									id: 'flexitimeInfo',
									//title: data.flexibleInfo.flexibleNm,
									start: sYmd,
		  		  		        	end: eYmd,
		  		  		        	rendering: 'background',
		  		  		        	classNames: classNames
		  		  		        	//color: '#4d84fe'
								});
							}
						},
						error: function() {
							
						}
					});
  	         	},
				getWorkDayInfo : function(ymd){ //해당일의 근무 정보
					
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
  	         	getFlexitimeList : function(){ //사용할 근무제 리스트
  	         		var $this = this;
  	         		/* var param = {
	   		    		useSymd : this.today
	   		    	}; */
   		    		
   		    		Util.ajax({
						//url: "${rc.getContextPath()}/flexitime/list",
						url: "${rc.getContextPath()}/flexibleStd",
						type: "GET",
						contentType: 'application/json',
						//data: param,
						dataType: "json",
						success: function(data) {
							if(data.status=='OK' && data.wtmFlexibleStd!=null) {
								//console.log(data.wtmFlexibleStd);
								$this.flexitimeList = data.wtmFlexibleStd;
								
								//사용할 근무제 팝업 띄우기
								$("#flexitimeModal").modal("show"); 
							}
						},
						error: function() {
							
						}
					});
  	         		
  	         	},
  	         	selectFlexitime : function(idx){ //사용할 근무제 선택
  	         		var $this = this;
  	         		
  	         		$(".list-group-item").not(idx).removeClass("active");
  	         		$(".list-group-item").eq(idx).addClass("active");
  	         		
  	         		//선택한 근무제 적용
  	         		$this.selectedFlexitime = $this.flexitimeList[idx];
  	         		console.log($this.selectedFlexitime);
  	         	},
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
						error: function() {
							$this.prevEdate = '';
						}
					});
  	         	},
  	         	clearFlexitimeAppl : function(){
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		
  	         		var workRangeEvent = calendar.getEventById('workRange');
  	         		if(workRangeEvent!=null)
  	         			workRangeEvent.remove();
  	         		
  	         		$this.useSymd = '';
         			$this.useEymd = '';
         			
  	         	},
  	         	applyFlexitime : function(){ //근무제 적용
  	         		var $this = this;
  	         	
  	         		$this.clearFlexitimeAppl();
  		    		$this.workRange = '';
  	         	
  	         		$('#flexitimeModal').on('hidden.bs.modal',function(){
  	         			$('#flexitimeModal').off('hidden.bs.modal');
  	         			$(".list-group-item").removeClass("active");
  	         			
  	         			//신청화면 전환
  	  	         		$("#workRangeInfo").hide();
  	  	         		$("#workDayInfo").hide();
  	         			
  	         			$("#alertText").html("달력에서 근무제 시작일을 선택해주세요.");
  	  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	  	         			$("#alertModal").off('hidden.bs.modal');
  	  	         			$this.useSymd='';
	  	         			$this.useYn='Y';
	  	         			
	  	         			//선택할 수 있는 근무기간 체크
	  	         			$this.getPrevFlexitime();
  	  	         		});
  	  	         		$("#alertModal").modal("show"); 
  	         		});
  	         		$('#flexitimeModal .close').click();
  	         		
  	         	},
  	         	viewFlexitimeAppl : function(){ //신청서 setting
  	         		var $this = this;
  	         	
  	         		$("#applForm").show();
  	         	
  	         		//적용기간은 첫번째 항목으로 기본 세팅
  	         		if($this.selectedFlexitime.hasOwnProperty("usedTermOpt") && $this.selectedFlexitime.usedTermOpt!=null) {
  	         			var workDateRangeItem = $this.selectedFlexitime.usedTermOpt[0]; 
  	         			
  	         			if(workDateRangeItem.hasOwnProperty("value")&&workDateRangeItem.value!=null)
  	         				//$this.changeWorkRange(workDateRangeItem.value);
  	         				$this.workRange = workDateRangeItem.value;
  	         				$this.changeWorkRange();
  	         		}
  	         		
  	         	},
  	         	changeUseSymd : function(e){
  	         		var $this = this;
  	         		
  	         		if(moment($this.useSymd).diff($this.prevEdate)<=0) {
  	         			$this.clearFlexitimeAppl();
  	         			return false;
  	         		}
  	         		$this.changeWorkRange();
  	         	},
  	         	changeWorkRange : function(){ //근무기간 변경에 따라 background 변경
  	         		var $this = this;
  	         		var calendar = $this.$refs.fullCalendar.cal;
  	         		if($this.workRange!=null && $this.workRange!='') {
	  	         		var workDateRange = $this.workRange.split('_');
	         		
	         			var eYmd = new Date($this.useSymd);
	         			if(workDateRange[1]=='week') {
	         				eYmd.setDate(eYmd.getDate()+ (workDateRange[0]*7));
	         			} else if(workDateRange[1]=='month') {
	 	         			eYmd.setMonth(eYmd.getMonth()+ (Number(workDateRange[0])));
	         			}
	         			
	         			var classNames = [];
						classNames.push($this.selectedFlexitime.workTypeCd);
						
						//근무 요일이 아닌 경우 제외하고 event 생성
						var daysOfWeek = ['일','월','화','수','목','금','토'];
						$.each($this.selectedFlexitime.workDaysOpt, function(k, v){
							if(v==true) {
								if($.inArray(v, $this.selectedFlexitime.workDaysOpt) === -1) {
									$this.workDaysOpt.push(daysOfWeek.indexOf(k));
								}
							}
						});
						
	         			$this.addEvent({
  	         				id: 'workRange',
  		  		    		start: $this.useSymd,
  		  		        	end: moment(eYmd).format('YYYY-MM-DD'),
  		  		        	rendering: 'background',
  		  		        	daysOfWeek: $this.workDaysOpt,
  		  		        	startRecur: $this.useSymd,
  		  		        	endRecur: moment(eYmd).format('YYYY-MM-DD'),
  		  		        	classNames: classNames
  	  	  		      	});
	         			
	         			eYmd.setDate(eYmd.getDate()-1);
	         			$this.useEymd = moment(eYmd).format('YYYY-MM-DD');
	         			
	         			calendar.gotoDate($this.useSymd);
  	         		}
  	         	},
  	         	flexitimeAppl : function(){ //확인요청
  	         		
  	         	}
  		    }
   	});
   	
   	$('#flexitimeModal').on('hidden.bs.modal',function(){
   		$(".list-group-item").removeClass("active");
   		workTimeCalendarVue.prevEdate = '';
   	});
   	
</script>

