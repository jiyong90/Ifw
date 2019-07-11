<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
<div id="workSystemCalendar">
	<div class="modal fade" id="workSystemModal" tabindex="-1" role="dialog">
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
	                	<button class="btn btn-outline btn-flat btn-block text-left" type="button" data-toggle="collapse" data-target="#collapWork" aria-expanded="false" aria-controls="collapseExample">근무제</button>
                        <div class="collapse" id="collapWork">
                            <ul class="list-group select-work-list">
                                <li class="list-group-item active">
                                    <span class="tag part">부분</span>
                                    <div class="title">이수 선근제 기본</div>
                                    <div class="desc">근무구간: 08:00 ~ 22:00<span class="bar"></span>코어구간: 10:00 ~ 15:00</div>
                                </li>
                                <li class="list-group-item">
                                    <span class="tag full">부분</span>
                                    <div class="title">이수연구직</div>
                                    <div class="desc">근무구간: 08:00 ~ 22:00<span class="bar"></span>코어구간: 10:00 ~ 15:00</div>
                                </li>
                            </ul>
                        </div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	                <button type="button" class="btn btn-default" disabled>적용하기</button>
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
	                    <button type="button" class="btn btn-block btn-apply" data-toggle="modal" data-target="#workSystemModal">근무제 적용하기</button>
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
                    <div class="col-12 col-md-3" style="padding-right:14px;">
	            <div class="work-info-wrap">
	                <div class="main-title">2019년 6월 10일 ~ 2019년 6월 16일</div>
	                <div class="main-desc">일반근무제</div>
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
                                <div class="sub-big-title">근무시간 요약 (근무시간 분류별 합산)</div>
                                <li>
                                    <span class="sub-title">소정근로</span>
                                    <span class="sub-desc">8:00</span>
                                </li>
                                <li>
                                    <span class="sub-title">연장근로</span>
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
                                    <span class="sub-title">근태현황</span>
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
	                </div>
	            </div>
	        </div>
	        <div class="col-12 col-md-9">
	            <div class="calendar-wrap">
	                <div id='calendar-container'>
	                    <div id='workCalendar'></div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</div>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/locales-all.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/moment/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/moment-timezone/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/bootstrap/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/interaction/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/luxon/main.min.js'></script>
<script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/rrule/main.min.js'></script>
<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('workCalendar');

        var calendar = new FullCalendar.Calendar(calendarEl, {
            plugins: ['interaction', 'dayGrid', 'timeGrid', 'list', 'bootstrap', 'rrule', 'luxon'],
            themeSystem: 'bootstrap',
            height: 'parent',
            locale: 'ko',
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            defaultView: 'dayGridMonth',
            defaultDate: '2019-06-19',
            navLinks: true, // can click day/week names to navigate views
            selectable: true,
            selectMirror: true,
            select: function (arg) {
                var title = prompt('시작일 지정');
                if (title) {
                    calendar.addEvent({
                        title: title,
                        start: arg.start,
                        end: arg.end,
                        allDay: arg.allDay,

                    })
                }
                calendar.unselect();
            },
            // select: function(start, end) {
            //     // Display the modal.
            //     // You could fill in the start and end fields based on the parameters
            //     $('.modal').modal('show');
            // },
            eventClick: function (event, element) {
                // Display the modal and set the values to the event values.
                $('.modal').modal('show');
                $('.modal').find('#startDate').val(event.start);
            },
            editable: true,
            eventLimit: true, // allow "more" link when too many events
            events: [{
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
                    end: '2019-06-13T16:00:00',
                    rendering: 'background',
                    color: 'red',

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
                    title: 'test',
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

                // red areas where no events can be dropped
                {
                    title: '출근',
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

        });

        calendar.render();
    });
</script>