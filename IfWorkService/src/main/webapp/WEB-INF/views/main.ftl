<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="shortcut icon" href="${rc.getContextPath()}/soldev/img/favicon.ico" />
    <title>근태관리 시스템</title>
    <!-- Bootstrap CSS -->
    <!-- <link rel="stylesheet" href="bootstrap-4.3.1-dist/css/bootstrap-reboot.min.css"> -->
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-4.3.1-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-4.3.1-dist/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/NotosansKR.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/layout.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/common.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/fontawesome-free-5.8.2-web/css/all.min.css">
    <!-- S : calendar -->
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/core/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/daygrid/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/timegrid/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
    <link rel="stylesheet" href="${rc.getContextPath()}/fullcalendar-4.2.0/packages/list/main.css"/>
    <!-- E : calendar -->

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="wrapper">
        <nav id="sidebar">
            <div class="sidebar-header">
                <a href="#">
                    <h3 class="full-logo CTAs"><img src="${rc.getContextPath()}/soldev/img/logo_ko_60px.png" alt=""></h3>
                    <strong class="logo text-center"><img src="${rc.getContextPath()}/soldev/img/gnb_logo.png" alt=""></strong>
                </a>
            </div>

            <ul class="list-unstyled components">
                <li>
                    <a href="#">
                        <i class="fas fa-user-cog"></i>
                        나의 정보
                    </a>
                </li>
                <li class="active">
                    <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                        <i class="fas fa-home"></i>
                        Home
                    </a>
                    <ul class="collapse list-unstyled" id="homeSubmenu">
                        <li>
                            <a href="#">Home 1</a>
                        </li>
                        <li>
                            <a href="#">Home 2</a>
                        </li>
                        <li>
                            <a href="#">Home 3</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="#">
                        <i class="fas fa-briefcase"></i>
                        About
                    </a>
                    <a href="#pageSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                        <i class="fas fa-copy"></i>
                        Pages
                    </a>
                    <ul class="collapse list-unstyled" id="pageSubmenu">
                        <li>
                            <a href="#">Page 1</a>
                        </li>
                        <li>
                            <a href="#">Page 2</a>
                        </li>
                        <li>
                            <a href="#">Page 3</a>
                        </li>
                    </ul>
                </li>
                
                <li>
                    <a href="#">
                        <i class="fas fa-question"></i>
                        FAQ
                    </a>
                </li>
                <li>
                    <a href="#">
                        <i class="fas fa-paper-plane"></i>
                        Contact
                    </a>
                </li>
            </ul>

            <!-- <ul class="list-unstyled CTAs">
                <li>
                    <a href="https://bootstrapious.com/tutorial/files/sidebar.zip" class="download">Download source</a>
                </li>
                <li>
                    <a href="https://bootstrapious.com/p/bootstrap-sidebar" class="article">Back to article</a>
                </li>
            </ul> -->
        </nav>
        <div id="content">
            <nav class="navbar navbar-expand-lg navbar-light ">
                <div class="container-fluid">
                    <button type="button" id="sidebarCollapse" class="btn btn-info">
                        <i class="fas fa-bars"></i>
                        <!-- <span>Toggle Sidebar</span> -->
                    </button>
                    <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                        aria-label="Toggle navigation">
                        <i class="fas fa-align-justify"></i>
                    </button>
            
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="nav navbar-nav ml-auto">
                            <li class="nav-item active">
                                <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"> <i class="fas fa-exchange-alt"></i></span>권한모드 전환</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"><i class="far fa-plus-square"></i></span>세션 늘리기</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"><i class="fas fa-cog"></i></span>설정</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#"><span class="icon-wrap mr-sm-2"><i class="fas fa-power-off"></i></span>로그인/로그아웃</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

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
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="${rc.getContextPath()}/jQuery-3.4.1/jquery-3.4.1.min.js"></script>
    <script src="${rc.getContextPath()}/popper-1.15.0/popper.min.js"></script>
    <script src="${rc.getContextPath()}/bootstrap-4.3.1-dist/js/bootstrap.min.js"></script>
    <script src="${rc.getContextPath()}/moment/moment.js"></script>
    <script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script src="${rc.getContextPath()}/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.ko.js"></script>
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
