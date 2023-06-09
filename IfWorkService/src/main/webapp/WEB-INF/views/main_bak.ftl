<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
    <!-- S : calendar -->
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
    <!-- E : calendar -->
</head>
<body>
	<#include "/navTop.ftl">
    <div class="wrapper">
        <#include "/navLeft.ftl">
        <div id="content">
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
                                <div id='calendar'></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-3"></div>
                </div>
                
            </div>
            <footer class="text-center mt-auto pt-4">Copyright © 2019 ISUSYSTEM. All rights reserved</footer>
        </div>
    </div>
    <#include "/metaScript.ftl">
    <script type="text/javascript">
        $(function () {
            $('#sidebarCollapse').on('click', function () {
                $('#sidebar').toggleClass('active');
            });

        });
    </script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.js'></script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/interaction/main.js'></script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.js'></script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.js'></script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.js'></script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/bootstrap/main.js'></script>
    <script src='${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/locales-all.min.js'></script>
    
    <script type="text/javascript">
        document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');

        var calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: [ 'interaction', 'dayGrid', 'timeGrid', 'list', 'bootstrap'],
        themeSystem: 'bootstrap',
        height: 'parent',
        locale: 'ko',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
        },
        defaultView: 'dayGridMonth',
        defaultDate: '2019-06-19',
        navLinks: true, // can click day/week names to navigate views
        selectable: true,
        selectMirror: true,
        select: function(arg) {
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
        eventClick: function(event, element) {
            // Display the modal and set the values to the event values.
            $('.modal').modal('show');
            $('.modal').find('#startDate').val(event.start);
        },
        editable: true,
        eventLimit: true, // allow "more" link when too many events
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
        });

        calendar.render();
        });
    </script>
</body>

</html>
