<#include "/calendar.ftl">
<div id="workTimeCalendar" class="wrapper">
<!-- <div class="navbar navbar-expand-lg navbar-light bg-light ">
        <div class="container-fluid">
            <form>
                <div class="form-row align-items-center">
                    <div class="col-auto my-1">
                        <label class="mr-sm-2 sr-only" for="inlineFormCustomSelect">Preference</label>
                        <select class="custom-select mr-sm-2" id="inlineFormCustomSelect">
                            <option selected>Choose...</option>
                            <option value="1">One</option>
                            <option value="2">Two</option>
                            <option value="3">Three</option>
                        </select>
                    </div>
                    <div class="col-auto my-1">
                        <div class="input-group ">
                            <label for="selectDate" class="col-form-label mr-sm-2">이름</label>
                            <input type="text" class="form-control" id="selectDate" placeholder="">
                        </div>
                    </div>
                    <div class="col-auto my-1">
                        <div class="custom-control custom-checkbox mr-sm-2">
                            <input type="checkbox" class="custom-control-input" id="customControlAutosizing">
                            <label class="custom-control-label" for="customControlAutosizing">Remember my preference</label>
                        </div>
                    </div>
                    <div class="col-auto my-1">
                        <button type="submit" class="btn btn-primary">조회</button>
                        <button type="submit" class="btn btn-secondary"><span class="ico-wrap"><i class="fas fa-redo"></i></span></button>
                    </div>
                </div>
            </form>
        </div>
    </div> -->
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
                        <full-calendar ref="fullCalendar" :events="events" ></full-calendar>
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
  		    	today: '${today?date("yyyy-MM-dd")?string("yyyyMMdd")}',
  		    	useYn: 'N', //근무제 적용 여부
  		    	flexitimeList: [], //사용할 근무제 리스트
  		    	selectedFlexitime: {}, //선택한 근무제
  		    	useSymd: '', //시작일
  		    	events: [
  		    		{
  		                title: 'Business Lunch',
  		                start: '2019-06-03T13:00:00',
  		                constraint: 'businessHours'
  		            },
  		             
  		            {
  		                title: 'Conference',
  		                start: '2019-06-18',
  		                end: '2019-06-20'
  		            },
  		            {
  		                title: 'Party',
  		                start: '2019-06-29T20:00:00'
  		            },

  		            // areas where "Meeting" must be dropped
  		            {
  		                groupId: 'availableForMeeting',
  		                start: '2019-06-11T10:00:00',
  		                end: '2019-06-11T16:00:00',
  		                rendering: 'background'
  		            },
  		            {
  		                groupId: 'availableForMeeting',
  		                start: '2019-06-13T10:00:00',
  		                end: '2019-06-13T16:00:00',
  		                rendering: 'background'
  		            },

  		            // red areas where no events can be dropped
  		            {
  		                start: '2019-06-24',
  		                end: '2019-06-28',
  		                overlap: true,
  		                rendering: 'background',
  		                color: '#ff9f89'
  		            },
  		            {
  		                start: '2019-06-06',
  		                end: '2019-06-08',
  		                overlap: false,
  		                rendering: 'background',
  		                color: '#ff9f89'
  		            },
  		            {
  		                title: 'Business Lunch',
  		                start: '2019-06-03T13:00:00',
  		                constraint: 'businessHours'
  		            },
  		            
  		            {
  		                title: 'Conference',
  		                start: '2019-06-18',
  		                end: '2019-06-20'
  		            },
  		            {
  		                title: 'Party',
  		                start: '2019-06-29T20:00:00'
  		            },

  		            // areas where "Meeting" must be dropped
  		            {
  		                groupId: 'availableForMeeting',
  		                start: '2019-06-11T10:00:00',
  		                end: '2019-06-11T16:00:00',
  		                rendering: 'background'
  		            },
  		            {
  		                title: '출근',
  		                start: '2019-06-13T10:00:00',
  		                end: '2019-06-13T10:20:00',
  		                overlap: false,
  		                rendering : 'background',
  		                color : 'red'
  		            },
  		            {
  		                title: '출근',
  		                start: '2019-06-13T10:00:00',
  		                end: '2019-06-13T10:05:00',
  		                overlap: true,
  		                color : 'blue'
  		            },

  		            // red areas where no events can be dropped
  		            {
  		                start: '2019-06-24',
  		                end: '2019-06-28',
  		                overlap: true,
  		                rendering: 'background',
  		                color: '#ff9f89'
  		            },
  		            {
  		                start: '2019-06-06',
  		                end: '2019-06-08',
  		                overlap: true,
  		                rendering: 'background',
  		                color: '#ff9f89'
  		            }
  		    	]
  		    },
  		    mounted: function(){
  		    	
  		    },
  		    methods : {
  		    	
  		    }
   	});
   	
</script>

