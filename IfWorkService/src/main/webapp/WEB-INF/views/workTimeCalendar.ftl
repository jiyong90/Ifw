<#include "/calendar.ftl">
<div id="workTimeCalendar" class="wrapper">
    <div class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Modal title</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>Modal body text goes here.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary">Save changes</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-9">
                <div class="card">
                    <div class="row px-2 py-4">
                        <div class="col-sm-4 col-md-2">
                            <div class="icon-wrap text-primary"><i class="fas fa-briefcase"></i></div>
                            <div class="text-secondary">근무제도</div>
                            <div>기본근무</div>
                        </div>
                        <div class="col-sm-4 col-md-2">
                            <div class="icon-wrap text-primary"><i class="fas fa-calendar-minus"></i></div>
                            <div class="text-secondary">근무제도 적용기간</div>
                            <div>2018.09.10~2019.09.15</div>
                        </div>
                        <div class="col-sm-4 col-md-2">
                            <div class="icon-wrap text-primary"><i class="fas fa-business-time"></i></div>
                            <div class="text-secondary">주 평균 근무시간</div>
                            <div>8시간</div>
                        </div>
                        <div class="col-sm-4 col-md-2">
                            <div class="icon-wrap text-primary"><i class="fas fa-stopwatch"></i></div>
                            <div class="text-secondary">잔여근무시간</div>
                            <div>32시간</div>
                        </div>
                        <div class="col-sm-4 col-md-2">
                            <div class="icon-wrap text-primary"><i class="fas fa-clock"></i></div>
                            <div class="text-secondary">연장근무시간</div>
                            <div>8시간</div>
                        </div>
                        <div class="col-sm-4 col-md-2">
                            <div class="icon-wrap text-primary"><i class="fas fa-user-clock"></i></div>
                            <div class="text-secondary">잔여연장근무시간</div>
                            <div>12시간</div>
                        </div>
                    </div>
                </div>
                <div class="content inner-wrap">
                    <div id='calendar-container'>
                        <!-- <full-calendar ref="fullCalendar" :events="events" @update="renderCallback" @datesrender="datesRenderCallback" @dayrender="dayRenderCallback"></full-calendar> -->
                        <full-calendar ref="fullCalendar" :events="events" @update="renderCallback" @datesrender="datesRenderCallback"></full-calendar>
                    </div>
                </div>
            </div>
            <div class="col-sm-3"></div>
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
  		    	events: [{
  	                title: 'Business Lunch',
  	                start: '2019-07-03T13:00:00',
  	                constraint: 'businessHours'
  	            }]
  		    },
  		    mounted: function(){
  		    	//var ym = '${today?date("yyyy-MM-dd")?string("yyyyMM")}';
  		    },
  		    methods : {
  		    	renderCallback: function(){
  		    		
  		    	},
  		    	datesRenderCallback: function(info){
  		    		var $this = this;
  		    		if(info.view.type == 'dayGridMonth') { //month change
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
							console.log(data);
							
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
  		    	},
  		    	dayRenderCallback : function(dayRenderInfo){ //day render
  		    		var date = dayRenderInfo.date;
  	         		$('td').find(".fc-day-top[data-date='"+moment(date).format('YYYY-MM-DD')+"'] .fc-day-number").text(moment(date).format('D'));
  	         	}
  		    }
   	});
   	
</script>

