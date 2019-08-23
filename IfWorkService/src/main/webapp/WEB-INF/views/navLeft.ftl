<nav id="sidebar" class="active">
    <ul class="list-unstyled components">
        <!-- <li class="active">
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
        </li> -->
        <li id="cal">
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Month';">
                <i class="fas fa-calendar-alt"></i>
                <span class="title">나의 정보 </span>
            </a>
        </li>
        <li id="appl">
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/approvalList';">
                <i class="fas fa-clipboard-check"></i>
                <span class="title">결재</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/codeMgr';">
                <span class="title">코드관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/taaCodeMgr';">
                <span class="title">근태코드관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/empHisMgr';">
                <span class="title">사원정보</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/ifEmpMsg';">
                <span class="title">사원이력</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/baseWorkMgr';">
                <span class="title">기본근무시간관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/workteamMgr';">
                <span class="title">근무조관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/workteamEmp';">
                <span class="title">근무조대상자관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/empCalendarMgr';">
                <span class="title">개인별근무시간관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/timeCdMgr';">
                <span class="title">근무유형관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleStdMgr';">
                <span class="title">근무제도관리</span>
            </a>
        </li>
        <li>
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleEmp';">
                <span class="title">개인별 근무제도 조회</span>
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

<script type="text/javascript">
$(document).ready(function () {
	if("${pageName}" == "workTimeCalendar") {
		$("#cal").attr('class', 'active');
		$("#appl").removeAttr('class', 'active');
	} else if("${pageName}" == "applList") {
		$("#appl").attr('class', 'active');
		$("#cal").removeAttr('class', 'active');
	}
});
</script>