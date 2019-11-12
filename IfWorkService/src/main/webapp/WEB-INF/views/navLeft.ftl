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
        <li :class="{active: curMenu!='flexibleMgr'&& (curPageName==''||curPageName=='workCalendar')}">
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Month';">
                <i class="fas fa-calendar-alt"></i>
                <span class="title">근태 캘린더</span>
            </a>
        </li>
        <li :class="{active: curMenu!='flexibleMgr' && curPageName=='approvalList'}">
            <a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/approvalList';">
                <i class="fas fa-clipboard-check"></i>
                <span class="title">결재</span>
            </a>
        </li>
        <li v-if="tsId=='isu' || authCd=='10'" :class="{active: curMenu=='flexibleMgr'}" @click="curMenu='flexibleMgr'">
        	<a href="#" id="lnb-mng">
                <i class="fas fa-laptop"></i>
                <span class="title">유연근무관리</span>
            </a>
            <ul id="lnb-sub">
                <li :class="{active: curSubMenu=='sub1'}">
                	<a href="#submenu-list1" data-toggle="collapse" :aria-expanded="curSubMenu=='sub1'?true:false" class="dropdown-toggle" @click="curSubMenu='sub1'">근태기본조회</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub1'}" id="submenu-list1">
                        <li :class="{active: curPageName=='codeMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/codeMgr';">[연계]코드관리</a></li>
                        <li :class="{active: curPageName=='taaCodeMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/taaCodeMgr';">[연계]근태코드관리</a></li>
                        <li :class="{active: curPageName=='orgCode'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/orgCode';">[연계]조직정보</a></li>
                        <li :class="{active: curPageName=='empHisMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/empHisMgr';">[연계]사원정보</a></li>
                        <li :class="{active: curPageName=='ifEmpMsg'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/ifEmpMsg';">[연계]사원이력</a></li>
                        <li :class="{active: curPageName=='applCode'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/applCode';">신청서관리</a></li>
                        <li :class="{active: curPageName=='pushMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/pushMgr';">알림관리</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub2'}">
                	<a href="#submenu-list2" data-toggle="collapse" :aria-expanded="curSubMenu=='sub2'?true:false" class="dropdown-toggle" @click="curSubMenu='sub2'">근무제도관리</a>
                	<ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub2'}" id="submenu-list2">
                        <li :class="{active: curPageName=='timeCdMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/timeCdMgr';">근무시간표관리</a></li>
                        <li :class="{active: curPageName=='baseWorkMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/baseWorkMgr';">기본근무시간관리</a></li>
                        <li :class="{active: curPageName=='workteamMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/workteamMgr';">근무조관리</a></li>
                        <li :class="{active: curPageName=='workteamEmp'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/workteamEmp';">근무조대상자관리</a></li>
                        <li :class="{active: curPageName=='flexibleStdMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleStdMgr';">근무제도관리</a></li>                                
                        <li :class="{active: curPageName=='flexibleApplyMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleApplyMgr';">근무제도적용</a></li>                                
                        <li :class="{active: curPageName=='flexibleEmp'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/flexibleEmp';">개인별 근무제도 조회</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub3'}">
                	<a href="#submenu-list3" data-toggle="collapse" :aria-expanded="curSubMenu=='sub3'?true:false" class="dropdown-toggle" @click="curSubMenu='sub3'">근태이상자조회</a>
                	<ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub3'}" id="submenu-list3">
                        <li :class="{active: curPageName=='worktimeCheckList'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/worktimeCheckList';">근무시간 초과자 조회</a></li>
                        <li :class="{active: curPageName=='entryCheckList'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/entryCheckList';">출/퇴근 미타각자 조회</a></li>
                        <li :class="{active: curPageName=='entryDiffList'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/entryDiffList';">출/퇴근 차이자 조회</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub4'}">
                	<a href="#submenu-list4" data-toggle="collapse" :aria-expanded="curSubMenu=='sub4'?true:false" class="dropdown-toggle"  @click="curSubMenu='sub4'">근무시간관리</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub4'}" id="submenu-list4">
                        <li :class="{active: curPageName=='empCalendarMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/empCalendarMgr';">근무시간관리</a></li>
                        <li :class="{active: curPageName=='inoutMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/inoutMgr';">근무시간수정</a></li>
                        <li :class="{active: curPageName=='orgEmpCalendarMgr'}" v-show="leaderYn=='Y'"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/orgEmpCalendarMgr';">팀원근무시간관리</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub5'}">
                	<a href="#submenu-list5" data-toggle="collapse" :aria-expanded="curSubMenu=='sub5'?true:false" class="dropdown-toggle"  @click="curSubMenu='sub5'">보상휴가관리</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub5'}" id="submenu-list5">
                        <li :class="{active: curPageName=='compMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/compMgr';">보상휴가기준관리</a></li>
                        <li :class="{active: curPageName=='compCreateList'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/compCreateList';">보상휴가시간조회</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub6'}">
                	<a href="#submenu-list6" data-toggle="collapse" :aria-expanded="curSubMenu=='sub6'?true:false" class="dropdown-toggle"  @click="curSubMenu='sub6'">근무마감조회</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub6'}" id="submenu-list6">
                        <li :class="{active: curPageName=='ruleMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/worktimeDayClose';">근무마감일별조회</a></li>
                        <li :class="{active: curPageName=='ruleMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/worktimeMonClose';">근무마감월별조회</a></li>
                    </ul>
                </li>
                <li :class="{active: curSubMenu=='sub7'}">
                	<a href="#submenu-list7" data-toggle="collapse" :aria-expanded="curSubMenu=='sub7'?true:false" class="dropdown-toggle"  @click="curSubMenu='sub7'">시스템관리</a>
                    <ul class="collapse list-unstyled" :class="{show: curSubMenu=='sub7'}" id="submenu-list7">
                        <li :class="{active: curPageName=='ruleMgr'}"><a href="#" onclick="location.href='${rc.getContextPath()}/console/${tsId}/views/mgr/ruleMgr';">규칙관리</a></li>
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
			curPageName: '',
			leaderYn: '',
			tsId: '',
			authCd: ''
		},
		mounted: function(){
			
			<#if leaderYn?? && leaderYn!='' && leaderYn?exists >
				this.leaderYn = "${leaderYn}";
			</#if>
			
			<#if tsId?? && tsId!='' && tsId?exists >
				this.tsId = "${tsId}";
			</#if>
			
			<#if authCd?? && authCd!='' && authCd?exists >
				this.authCd = "${authCd}";
			</#if>
			
			var path = $(location).attr('pathname');
			var pageName = path.substring(path.lastIndexOf('/') + 1);
			
			this.curSubMenu = '';
						
			if(pageName=='codeMgr'||pageName=='taaCodeMgr'||pageName=='empHisMgr'||pageName=='ifEmpMsg'||pageName=='ruleMgr'||pageName=='applCode'||pageName=='pushMgr')
				this.curSubMenu = 'sub1';
			else if(pageName=='timeCdMgr'||pageName=='baseWorkMgr'||pageName=='workteamMgr'||pageName=='workteamEmp'||pageName=='flexibleStdMgr'||pageName=='flexibleApplyMgr'||pageName=='flexibleEmp')
				this.curSubMenu = 'sub2';
			else if(pageName=='worktimeCheckList'||pageName=='entryCheckList'||pageName=='entryDiffList')
				this.curSubMenu = 'sub3';
			else if(pageName=='empCalendarMgr'||pageName=='orgEmpCalendarMgr')
				this.curSubMenu = 'sub4';
			else if(pageName=='compMgr'||pageName=='compCreateList')
				this.curSubMenu = 'sub5';
					
			this.curPageName = pageName;
			
			if(this.curSubMenu!='') 
				this.curMenu='flexibleMgr';
		}
	});
	
	$(function(){
		//lnb-sub-menu
	    $('#lnb-mng').on('click', function () {
	        $(this).siblings('#lnb-sub').toggleClass('active');
	    });
	});
</script>