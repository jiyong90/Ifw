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
        <li :class="{active: curPageName==''||curPageName=='workCalendar'}">
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Month';">
                <i class="fas fa-calendar-alt"></i>
                <span class="title">근태 캘린더</span>
            </a>
        </li>
        <li :class="{active: curPageName=='approvalList'}">
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/approvalList';">
                <i class="fas fa-clipboard-check"></i>
                <span class="title">결재</span>
            </a>
        </li>
        <li :class="{active: curPageName!=''||curPageName!='workCalendar'||curPageName!='approvalList'}" @click="curMenu='flexibleMgr'">
        	<a href="#" id="lnb-mng">
                <i class="fas fa-laptop"></i>
                <span class="title">유연근무관리</span>
            </a>
            <ul id="lnb-sub">
                <li :class="{active: curSubMenu=='sub1'}">
                	<a href="#submenu-list" data-toggle="collapse" :aria-expanded="curSubMenu=='sub1'?true:false" class="dropdown-toggle">근태기본조회</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub1'}" id="submenu-list">
                        <li :class="{active: curPageName=='codeMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/codeMgr';">[연계]코드관리</a></li>
                        <li :class="{active: curPageName=='taaCodeMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/taaCodeMgr';">[연계]근태코드관리</a></li>
                        <li :class="{active: curPageName=='empHisMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/empHisMgr';">[연계]사원정보</a></li>
                        <li :class="{active: curPageName=='ifEmpMsg'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/ifEmpMsg';">[연계]사원이력</a></li>
                        <li :class="{active: curPageName=='workteamMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/workteamMgr';">[연계]근무조관리</a></li>
                        <li :class="{active: curPageName=='workteamEmp'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/workteamEmp';">[연계]근무조대상자관리</a></li>
                        <li :class="{active: curPageName=='applCode'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/applCode';">신청서관리</a></li>
                        <li :class="{active: curPageName=='pushMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/pushMgr';">알림관리</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub2'}">
                	<a href="#submenu-list1" data-toggle="collapse" :aria-expanded="curSubMenu=='sub2'?true:false" class="dropdown-toggle">근무제도관리</a>
                	<ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub2'}" id="submenu-list1">
                        <li :class="{active: curPageName=='timeCdMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/timeCdMgr';">근무시간표관리</a></li>
                        <li :class="{active: curPageName=='baseWorkMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/baseWorkMgr';">기본근무시간관리</a></li>
                        <li :class="{active: curPageName=='flexibleStdMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleStdMgr';">근무제도관리</a></li>                                
                        <li :class="{active: curPageName=='flexibleApplyMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleApplyMgr';">근무제도적용</a></li>                                
                        <li :class="{active: curPageName=='flexibleEmp'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleEmp';">개인별 근무제도 조회</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub3'}">
                	<a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/empCalendarMgr';">개인별근무시간관리</a>
                </li>
                <li :class="{active: curSubMenu=='sub4'}">
                	<a href="#submenu-list2" data-toggle="collapse" :aria-expanded="curSubMenu=='sub4'?true:false" class="dropdown-toggle">보상휴가관리</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub4'}" id="submenu-list2">
                        <li :class="{active: curPageName=='compMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/compMgr';">보상휴가기준관리</a></li>
                        <li :class="{active: curPageName=='compCreateList'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/compCreateList';">보상휴가시간조회</a></li>
                    </ul>
                </li>
            </ul>
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
/* $(document).ready(function () {
	if("${pageName}" == "workTimeCalendar") {
		$("#cal").attr('class', 'active');
		$("#appl").removeAttr('class', 'active');
	} else if("${pageName}" == "applList") {
		$("#appl").attr('class', 'active');
		$("#cal").removeAttr('class', 'active');
	}
}); */
	var navLeftVue = new Vue({
		el: "#sidebar",
		data : {
			curMenu: '',
			curSubMenu: '',
			curPageName: ''
		},
		mounted: function(){
			var path = $(location).attr('pathname');
			var pageName = path.substring(path.lastIndexOf('/') + 1);
			
			if(pageName=='codeMgr'||pageName=='taaCodeMgr'||pageName=='empHisMgr'||pageName=='ifEmpMsg'||pageName=='workteamMgr'||pageName=='workteamEmp'||pageName=='applCode'||pageName=='pushMgr')
				this.curSubMenu = 'sub1';
			else if(pageName=='timeCdMgr'||pageName=='baseWorkMgr'||pageName=='flexibleStdMgr'||pageName=='flexibleApplyMgr'||pageName=='flexibleEmp')
				this.curSubMenu = 'sub2';
			else if(pageName=='empCalendarMgr')
				this.curSubMenu = 'sub3';
			else if(pageName=='compMgr'||pageName=='compCreateList')
				this.curSubMenu = 'sub4';
					
			this.curPageName = pageName;
			
			console.log('this.curMenu : ' + this.curMenu);
			console.log('this.curPageName : ' + this.curPageName);
		}
	});
	
	$(function(){
		//lnb-sub-menu
	    $('#lnb-mng').on('click', function () {
	        $(this).siblings('#lnb-sub').toggleClass('active');
	    });
	});
</script>