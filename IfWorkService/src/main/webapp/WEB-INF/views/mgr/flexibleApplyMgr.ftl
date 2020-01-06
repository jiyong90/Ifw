<div id="timeCdMgr">
	<!-- 유연근무대상자 보기 modal start -->
	<div class="modal fade" id="ApplyEmpPopModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">유연근무 대상자</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
						<tr>
							<td>
								<div class="inner">
									<div class="sheet_title_wrap clearfix">
										<div id="popupTitle" class="float-left title"></div>
									</div>
								</div>
								<script type="text/javascript"> createIBSheet("sheet4", "100%", sheetH90, "kr"); </script>
							</td>
						</tr>
					</table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 유연근무대상자 보기 modal end -->
    <!-- 근무 상세보기 modal start -->
	<div class="modal fade" id="workPlanModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">근무 계획 조회</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                	<div class="modal-app-wrap">
                    <div class="accordion-wrap inner-wrap">
	                    <ul id="accordion" class="accordion" >
	                        <li v-for="e in elasDetail">
	                            <div class="link" @click="accordionDropdown($event.target)">{{moment(e.startYmd).format('YYYY-MM-DD')}} ~ {{moment(e.endYmd).format('YYYY-MM-DD')}}<i class="ico arrow-down"></i></div>
	                            <div class="submenu">
	                                <ul class="all-time-wrap">
	                                    <li>
	                                        <span class="title">근무시간</span>
	                                        <span class="time bold">{{minuteToHHMM(e.workMinute, 'detail')}}</span>
	                                    </li>
	                                    <li>
	                                        <div class="total">
	                                            <span class="title">연장합산</span>
	                                            <span class="time bold">{{minuteToHHMM(e.otMinute, 'detail')}}</span>
	                                        </div>
	                                        <ul class="time-list">
	                                            <li>
	                                                <span class="title">조출시간</span>
	                                                <span class="time">{{minuteToHHMM(e.otbMinute, 'detail')}}</span>
	                                            </li>
	                                            <li>
	                                                <span class="title">잔업시간</span>
	                                                <span class="time">{{minuteToHHMM(e.otaMinute, 'detail')}}</span>
	                                            </li>
	                                            <li>
	                                                <span class="title">휴일시간</span>
	                                                <span class="time">{{minuteToHHMM(e.holidayMinute, 'detail')}}</span>
	                                            </li>
	                                        </ul>
	                                    </li>
	                                </ul>
	                                <template v-if="e.hasOwnProperty('details')">
	                                <p class="title time-desc-title">출,퇴근시간</p>
	                                <ul class="time-desc-wrap">
	                                    <li v-for="d in e.details">
	                                        <div class="date">{{moment(d.ymd).format('YYYY-MM-DD')}}({{d.weekday}}) 
	                                        	<template v-if="d.planSdate && d.planEdate">
	                                        	{{moment(d.ymd+' '+d.planSdate).format('HH:mm')}}~{{moment(d.ymd+' '+d.planEdate).format('HH:mm')}}
	                                        	</template>
	                                        </div>
	                                        <ul class="time-desc">
	                                            <li><span class="title">근무시간</span><span class="time">{{minuteToHHMM(d.workMinute, 'detail')}}</span></li>
	                                            <li><span class="title">조출시간</span><span class="time">{{minuteToHHMM(d.otbMinute, 'detail')}}</span></li>
	                                            <li><span class="title">잔업시간</span><span class="time">{{minuteToHHMM(d.otaMinute, 'detail')}}</span></li>
	                                            <li><span class="title">휴일시간</span><span class="time">{{minuteToHHMM(d.holidayMinute, 'detail')}}</span></li>
	                                        </ul>
	                                    </li>
	                                </ul>
	                                </template>
	                            </div>
	                        </li>
	            		</ul>
            		</div>           
                </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 근무 상세보기 modal end -->
 	<div class="container-fluid bg-white mgr-wrap">
 	<div class="ibsheet-wrapper">
 		<form id="sheetForm" name="sheetForm">
			<div class="sheet_search outer">
				<div>
					<table>
						<tr>
							<td>
							    <span class="magnifier"><i class="fas fa-search"></i></span>
							    <span class="search-title">Search</span>
							</td>
							<td>
								<span class="label">기준일 </span>
								<input type="text" id="sYmd" name="sYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
							</td>
							<td>
								<a href="javascript:doAction1('Search');" class="button">조회</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
			<tr>
				<td>
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">근무제 적용</div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet1", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
			<tr>
				<td>
					<div class="pt-2">
						<div class="innertab inner">
							<div id="tabs" class="tab">
								<ul class="outer tab_bottom">
									<li><a href="#tabs-1">그룹별</a></li>
									<li><a href="#tabs-2">개인별</a></li>
								</ul>
								<div id="tabs-1">
									<div  class="layout_tabs">
										<div class="inner sheet_title_wrap clearfix">
											<div class="float-left title" id="searchAppText">그룹별 대상자 관리</div>
											<ul class="float-right btn-wrap" id="sheet2Btn">
												<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
												<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
											</ul>
										</div>
										<script type="text/javascript">createIBSheet("sheet2", "100%", sheetH40,"kr"); </script>
									</div>
								</div>
								<div id="tabs-2">
									<div  class="layout_tabs">
										<div class="inner sheet_title_wrap clearfix">
											<div class="float-left title" id="searchAppText">개인별 대상자 관리</div>
											<ul class="float-right btn-wrap" id="sheet3Btn">
												<li><a href="javascript:doAction3('Insert')" class="basic authA">입력</a></li>
												<li><a href="javascript:doAction3('Save')" class="basic authA">저장</a></li>
											</ul>
										</div>
										<script type="text/javascript">createIBSheet("sheet3", "100%", sheetH40, "kr"); </script>
									</div>
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	</div>
</div>

<script type="text/javascript">

   	$(function() {
		$('#sYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무명칭",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applyNm",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제도기준",		Type:"Combo",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",    Hidden:0, 	Width:90,   Align:"Center", ColMerge:0, SaveName:"useSymd",      	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"종료일",			Type:"Date",    Hidden:0, 	Width:90,   Align:"Center", ColMerge:0, SaveName:"useEymd",      	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:100 },
			{Header:"반복기준",			Type:"Combo",   Hidden:0, 	Width:70,   Align:"Center", ColMerge:0, SaveName:"repeatTypeCd",   	KeyField:0, Format:"",   	PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:50 },
			{Header:"반복횟수",			Type:"Int",    	Hidden:0, 	Width:70,   Align:"Center", ColMerge:0, SaveName:"repeatCnt",      	KeyField:0, Format:"",   	PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:2 },
			{Header:"소정근무시간(분)",	Type:"Int",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"workMinute",	  	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"연장근무시간(분)",	Type:"Int",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"otMinute",		KeyField:0,	Format:"",		PointCount:2,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"대상자조회",		Type:"Image", 	Hidden:0,  	Width:70,  	Align:"Center", ColMerge:0, SaveName:"selectImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:1    },
			{Header:"근무계획조회",		Type:"Image", 		Hidden:0,  	Width:60,  	Align:"Center", ColMerge:0, SaveName:"planImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:100   },
			{Header:"확정여부",		Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0, SaveName:"applyYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"확정상태",		Type:"Html", 		Hidden:0,  	Width:60,  	Align:"Center", ColMerge:0, SaveName:"endImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:100   },
			{Header:"확정상태",		Type:"Int", 		Hidden:1,  	Width:60,  	Align:"Center", ColMerge:0, SaveName:"cnt",  			KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:1    },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제시작요일",	Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0, SaveName:"weekDay",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제유형",		Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0, SaveName:"workTypeCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetUseDefaultTime(0);
		sheet1.SetCountPosition(8);
		
		sheet1.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");
		sheet1.SetImageList(1,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");
		
		var repeatTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "REPEAT_TYPE_CD"), "선택");	
		sheet1.SetColProperty("repeatTypeCd", {ComboText:repeatTypeCdList[0], ComboCode:repeatTypeCdList[1]} );
		
		
		//근무제도
		var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "선택");
		sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1], comboEtc:flexibleList[2]} );
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",		Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyGroupId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"소속코드",  Type:"Combo",    	Hidden:0, 	Width:100,  Align:"Center", ColMerge:0, SaveName:"orgCd",     			KeyField:0, Format:"",  PointCount:0, UpdateEdit:0, InsertEdit:1, EditLen:100 },
			{Header:"소속명",    Type:"Text",    	Hidden:1, 	Width:100,  Align:"Center", ColMerge:0, SaveName:"orgNm",     			KeyField:0, Format:"",  PointCount:0, UpdateEdit:0, InsertEdit:1, EditLen:100 },
			{Header:"직무코드",	Type:"Combo",		Hidden:1, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"jobCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"직책",	    Type:"Combo",		Hidden:0, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"dutyCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"직위",	    Type:"Combo",		Hidden:0, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"posCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"직급",	    Type:"Combo",		Hidden:0, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"classCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무조",	Type:"Combo",		Hidden:0,	Width:80,	Align:"Center", ColMerge:0, SaveName:"workteamCd",			KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0, SaveName:"note",	 			KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUseDefaultTime(0);
		sheet2.SetUnicodeByte(3);
        
		//코드
		var jobCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "JOB_CD"), "전체");
		sheet2.SetColProperty("jobCd", {ComboText:"전체|"+jobCdList[0], ComboCode:"|"+jobCdList[1]} );
		var dutyCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DUTY_CD"), "전체");
		sheet2.SetColProperty("dutyCd", {ComboText:"전체|"+dutyCdList[0], ComboCode:"|"+dutyCdList[1]} );
		var posCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "POS_CD"), "전체");
		sheet2.SetColProperty("posCd", {ComboText:"전체|"+posCdList[0], ComboCode:"|"+posCdList[1]} );
		var classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "CLASS_CD"), "전체");
		sheet2.SetColProperty("classCd", {ComboText:"전체|"+classCdList[0], ComboCode:"|"+classCdList[1]} );
		var workteamCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/workteamMgr/workteamCd", "",false).DATA, "");
		sheet2.SetColProperty("workteamCd", {ComboText:"전체|"+workteamCdList[0], ComboCode:"|"+workteamCdList[1]} );
		var orgCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/orgCode/comboList", "",false).DATA, "선택");
		sheet2.SetColProperty("orgCd", {ComboText:"전체|"+orgCdList[0], ComboCode:"|"+orgCdList[1]} );
		
		//조직
        // setSheetAutocompleteOrg("sheet2", "orgNm");
		
		var initdata3 = {};
		initdata3.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata3.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata3.Cols = [
            {Header:"No",		Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyEmpId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0, SaveName:"orgNm",		  		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center", ColMerge:0, SaveName:"sabun",		  		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:13 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"empNm",		  		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0, SaveName:"note",	 			KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet3, initdata3);
		sheet3.SetEditable(true);
		sheet3.SetVisible(true);
		sheet3.SetUseDefaultTime(0);
		sheet3.SetUnicodeByte(3);
		
		//이름
        setSheetAutocompleteEmp( "sheet3", "sabun" );
        
        var initdata4 = {};
		
		initdata4.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata4.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata4.Cols = [
			{Header:"No",		Type:"Seq",		Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"소속",		Type:"Text",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사번",		Type:"Text",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"성명",		Type:"Text",    Hidden:0, 	Width:90,   Align:"Center", ColMerge:0, SaveName:"empNm",      	KeyField:0, Format:"",  PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"직책",		Type:"Text",    Hidden:0, 	Width:90,   Align:"Center", ColMerge:0, SaveName:"dutyNm",      KeyField:0, Format:"",  PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"직급",		Type:"Text",    Hidden:0, 	Width:70,   Align:"Center", ColMerge:0, SaveName:"classNm",     KeyField:0, Format:"",  PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:100 },
			{Header:"직위",		Type:"Text",    Hidden:0, 	Width:70,   Align:"Center", ColMerge:0, SaveName:"posNm",      	KeyField:0, Format:"",  PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet4, initdata4);
		sheet4.SetEditable(false);
		sheet4.SetVisible(true);

		sheetInit();
		doAction1("Search");
	});
   	
   	var workPlanVue = new Vue({
		el: "#workPlanModal",
	    data : {
	    	elasDetail: []
  		},
  		mounted: function(){
  	  		
  		},
  		methods: {
  			getElasDetail : function(Row){
  				var $this = this;
  				
  				$("#loading").show();
  				
  				var flexibleApplyId = sheet1.GetCellValue(Row, "flexibleApplyId");
  				
  				var param = {
  					flexibleApplyId: flexibleApplyId
	    		};
	    		
	    		Util.ajax({
					url: "${rc.getContextPath()}/flexibleApply/elasDetail",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null) {
							$this.elasDetail = data;
							$("#workPlanModal").modal("show");
						}
					},
					error: function(e) {
						$("#loading").hide();
						$this.apprList = [];
					}
				});
  				
  			},
         	accordionDropdown: function(target) {
         		var $el = $('#accordion');
         		$this = $(target),
                $next = $this.next();

                $next.slideToggle();
                $this.parent().toggleClass('open');
                
                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');

         	}
  		}
	});
   	
	var newIframe;
	var oldIframe;
	var iframeIdx;
	
	$(function() {
		newIframe = $('#tabs-1 layout_tabs');
		iframeIdx = 0;

		$( "#tabs" ).tabs({
			beforeActivate: function(event, ui) {
				iframeIdx = ui.newTab.index();
				newIframe = $(ui.newPanel).find('layout_tabs');
				oldIframe = $(ui.oldPanel).find('layout_tabs');
				showIframe();
			}
		});
	});
	
	function showIframe() {
		if(iframeIdx == 0) {
			$("#tabs-1").show();
			$("#tabs-2").hide();
		} else if(iframeIdx == 1) {
			$("#tabs-1").hide();
			$("#tabs-2").show();
		}
	}

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/flexibleApply/list" , $("#sheetForm").serialize());
			break;
		case "Insert":
			sheet1.DataInsert(0) ;
			break;
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|applyNm|useSymd", false, true)){break;}
			
			if(validateFlex()) {
				IBS_SaveName(document.sheetForm,sheet1);
				sheet1.DoSave("${rc.getContextPath()}/flexibleApply/save", $("#sheetForm").serialize()); break;
			}
			break;
		}
	}
	
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "flexibleApplyId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleApply/grpList" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleApply/grpSave", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var flexibleApplyId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			if(flexibleApplyId == ""){
				alert("근무제도 기준을 저장후 대상자를 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(0) ;
				sheet2.SetCellValue(row, "flexibleApplyId" , flexibleApplyId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		}
	}
	
	function doAction3(sAction) {
		switch (sAction) {
		case "Search":
			var param = "flexibleApplyId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			sheet3.DoSearch( "${rc.getContextPath()}/flexibleApply/empList" , param);
			break;
			
		case "Insert":
			var flexibleApplyId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			if(flexibleApplyId == ""){
				alert("근무제도 기준을 저장후 대상자를 입력하셔야 합니다");
			} else {
				var row = sheet3.DataInsert(0) ;
				sheet3.SetCellValue(row, "flexibleApplyId" , flexibleApplyId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet3);
			sheet3.DoSave("${rc.getContextPath()}/flexibleApply/empSave", $("#sheetForm").serialize()); break;
			break;
		}
	}
	
	function doAction4(sAction) {
		switch (sAction) {
		case "Search":
			var param = "flexibleApplyId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			sheet4.DoSearch( "${rc.getContextPath()}/flexibleApply/empPopuplist" , param);
			break;
		}
	}
	
	function setEndConfirm(flexibleApplyId){
		var row = sheet1.FindText("flexibleApplyId", flexibleApplyId, 0);
		var workTypeCd = sheet1.GetCellValue(row, "workTypeCd");
		var param = "flexibleApplyId=" + flexibleApplyId + "&workTypeCd=" + workTypeCd;
		
		if(!confirm("확정하시겠습니까?")) {
			return;
		}
		$("#loading").show();
		var rtn = ajaxCall("${rc.getContextPath()}/flexibleApply/apply", param ,false);
		if(rtn != null && rtn != "") {
			$("#loading").hide();
			if(rtn.status == "FAIL"){
				alert(rtn.message);
			} else {
				
				alert(sheet1.GetCellValue(row, "applyNm") + " 근무 확정완료 되었습니다.");
				doAction1("Search");
			}			
		} else {
			$("#loading").hide();
			alert("확정할 내용이 없습니다.");
			
		}
	}
	
	function getReturnValue(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
   		sheet3.SetCellValue(gPRow, "sabun",returnValue.sabun);
		sheet3.SetCellValue(gPRow, "name",returnValue.empNm);
        sheet3.SetCellValue(gPRow, "orgNm",returnValue.orgNm);
	}
	
	function getOrgReturnValue(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
		sheet2.SetCellValue(gPRow, "orgCd",returnValue.orgCd);
        sheet2.SetCellValue(gPRow, "orgNm",returnValue.orgNm);
	}
	
	function setButten(applyYn, sheetNm){
		// 적용완료되엇으면 수정불가
		if(applyYn == "Y"){
			if(sheetNm == "sheet2"){
				sheet2.SetEditable(0);
				$("#sheet2Btn").hide();
			} else {
				sheet3.SetEditable(0);
				$("#sheet3Btn").hide();
			}
		} else {
			if(sheetNm == "sheet2"){
				sheet2.SetEditable(1);
				$("#sheet2Btn").show();
			} else {
				sheet3.SetEditable(1);
				$("#sheet3Btn").show();
			}
		}
	}
	
	function setRepeatSelect(row, flexibleStdMgrId){
		var test = "";
		/*
		var repeatTypeCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleApply/workType", "flexibleStdMgrId="+flexibleStdMgrId,false).DATA, "선택");
		var info = {"ComboCode":repeatTypeCdList[1],"ComboText":repeatTypeCdList[0]};
   	 	sheet1.CellComboItem(row,"repeatTypeCd",info);
   	 	*/
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			} else {
				for(i=1;i<=sheet1.RowCount();i++){
			   	  	// 확정완료는 수정불가
					var applyYn = sheet1.GetCellValue(i, "applyYn");
			   	  	if(applyYn == "Y"){
			   	  		sheet1.SetRowEditable(i, 0);
			   	  	} else {
			   	  		sheet1.SetRowEditable(i, 1);
				   		// 반복여부
			   	  		var repeatTypeCd = sheet1.GetCellValue(i, "repeatTypeCd", 0);
			   	  		if(repeatTypeCd == "" || repeatTypeCd == "NO"){
				   	  		sheet1.SetCellEditable(i, "repeatCnt", 0);
							sheet1.SetCellEditable(i, "useEymd", 1);
			   	  		} else {
				   	  		sheet1.SetCellEditable(i, "repeatCnt", 1);
							sheet1.SetCellEditable(i, "useEymd", 0);
			   	  		}
			   	  	}
			   	}
			}
			sheetResize();
		} catch (ex) {
			alert("OnSearchEnd Event Error : " + ex);
		}
	}
	
	// 저장 후 메시지
	function sheet1_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			doAction1("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(OldRow != NewRow){
			var flexibleStdMgrId = sheet1.GetCellValue(NewRow, "flexibleStdMgrId");
	   	  	setRepeatSelect(NewRow, flexibleStdMgrId);
			sheet2.RemoveAll();
			sheet3.RemoveAll();
			doAction2('Search');
			doAction3('Search');
		}
	}
	
	//셀 클릭시 이벤트
	function sheet1_OnClick(Row, Col, Value) {
		
		try{
			if(Row > 0){
				if(sheet1.ColSaveName(Col) == "selectImg" ) {
					var applyNm = sheet1.GetCellValue( Row, "applyNm");
					var useSymd = sheet1.GetCellValue( Row, "useSymd");
					var useEymd = sheet1.GetCellValue( Row, "useEymd");
					
					$("#popupTitle").text(applyNm + " " + formatDate(useSymd,'-') + "~" + formatDate(useEymd,'-'));
					
					sheet4.RemoveAll();
					doAction4('Search');
					$("#ApplyEmpPopModal").modal("show");
				} else if(sheet1.ColSaveName(Col) == "planImg" && sheet1.GetCellValue( Row, "workTypeCd") == 'ELAS' ) {
					workPlanVue.getElasDetail(Row);
				}
				
			}
		}catch(ex){
			alert("OnClick Event Error : " + ex);
		}
	}
	
	//셀 값변경 이벤트
	function sheet1_OnChange(Row, Col, Value) {
		var colNm = sheet1.ColSaveName(Col);
		var repeatTypeCd = sheet1.GetCellValue(Row, "repeatTypeCd");
		var status = sheet1.GetCellValue(Row, "sStatus");
		if(colNm == "flexibleStdMgrId" && status == "I"){
			var flexibleStdMgrId = sheet1.GetCellValue(Row, "flexibleStdMgrId");
   	  		setRepeatSelect(Row, flexibleStdMgrId);
   	  		
   	  		//근무제 정보
   	  		getFlexibleStd(Row, flexibleStdMgrId);
		}
		if(colNm == "repeatTypeCd"){
			if(repeatTypeCd == "NO"){
				//반복이 없으면
				sheet1.SetCellEditable(Row, "repeatCnt", 0);
				sheet1.SetCellValue(Row, "repeatCnt", "", 0);
				sheet1.SetCellEditable(Row, "useEymd", 1);
			} else {
				sheet1.SetCellEditable(Row, "repeatCnt", 1);
				sheet1.SetCellEditable(Row, "useEymd", 0);
				sheet1.SetCellValue(Row, "useEymd", "", 0);
			}
		}
		
		if(colNm == "useSymd" || colNm == "repeatTypeCd" || colNm == "repeatCnt"){
			var symd = sheet1.GetCellValue(Row, "useSymd");
			var repeatCnt = sheet1.GetCellValue(Row, "repeatCnt");
			
			if(symd != "") {
				if(repeatTypeCd != "" && repeatCnt > 0){
					// 종료일 조회
					var param = "symd=" + symd + "&repeatTypeCd=" + repeatTypeCd + "&repeatCnt=" + repeatCnt;
					var rtn = ajaxCall("${rc.getContextPath()}/flexibleApply/getEymd", param ,false).DATA;
					if(rtn != null && rtn != "") {
						sheet1.SetCellValue(Row, "useEymd", rtn.eymd, 0);
					}
				}
			}
			
		}
	}

	
	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			var applyYn = sheet1.GetCellValue( sheet1.GetSelectRow(), "applyYn");
			setButten(applyYn, "sheet2");
			sheetResize();
		} catch (ex) {
			alert("OnSearchEnd Event Error : " + ex);
		}
	}

	// 저장 후 메시지
	function sheet2_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			doAction2("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	// 조회 후 에러 메시지
	function sheet3_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			var applyYn = sheet1.GetCellValue( sheet1.GetSelectRow(), "applyYn");
			setButten(applyYn, "sheet3");
			sheetResize();
		} catch (ex) {
			alert("OnSearchEnd Event Error : " + ex);
		}
	}

	// 저장 후 메시지
	function sheet3_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			doAction3("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	// 조회 후 에러 메시지
	function sheet4_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}

			sheetResize();
		} catch (ex) {
			alert("OnSearchEnd Event Error : " + ex);
		}
	}
	
	function getFlexibleStd(row, flexibleStdMgrId){
		var param = {
			flexibleStdMgrId: flexibleStdMgrId
		};
		
		Util.ajax({
			url: "${rc.getContextPath()}/flexibleStd",
			type: "GET",
			contentType: 'application/json',
			data: param,
			dataType: "json",
			async: false,
			success: function(data) {
				if(data!=null) {
					sheet1.SetCellValue( row, "weekDay", moment(data.useSymd).day());
					sheet1.SetCellValue( row, "workTypeCd", data.workTypeCd);
					
					//탄근제의 경우 반복없음, 종료일 담당자가 입력하게끔
					if(data.workTypeCd == 'ELAS') {
						sheet1.SetCellValue( row, "repeatTypeCd", "NO");
						sheet1.SetCellEditable( row, "repeatTypeCd", 0);
						sheet1.SetCellEditable( row, "repeatCnt", 0);
						sheet1.SetCellEditable( row, "useEymd", 1);
					}
				}
			},
			error: function(e) {
				console.log(e);
			}
		});
	}
	
	function validateFlex(){
		var isValid = true;
		var arr = ['일','월','화','수','목','금','토'];
		
		//탄근제  패턴 시작일과 신청 시작일의 요일이 같은지 확인
		for(var i=1; i<=sheet1.LastRow(); i++) {
			var status = sheet1.GetCellValue( i, "sStatus");
			var workTypeCd = sheet1.GetCellValue( i, "workTypeCd");
			
			if(status=='I' && workTypeCd=='ELAS') {
				var useSymd = sheet1.GetCellValue( i, "useSymd");
				var useEymd = sheet1.GetCellValue( i, "useEymd");
				var weekDay = sheet1.GetCellValue( i, "weekDay");
				
				//2주 이상 3개월 이내 탄근제 인지 체크
				var minYmd = moment(moment(useSymd).add(14, 'days')).subtract(1, 'days');
				var maxYmd = moment(moment(useSymd).add(3, 'months')).subtract(1, 'days');
				
				if(moment(useEymd).format('YYYYMMDD')!=moment(minYmd).format('YYYYMMDD') 
						&& moment(useEymd).format('YYYYMMDD')!=moment(maxYmd).format('YYYYMMDD')) {
					alert("탄력근무제는 2주 이내, 3개월 이내만 시행 가능합니다.\n근무제 종료일을 다시 지정해 주세요.");
					return false;
				} 
				
		  		if(moment(useSymd).day() != weekDay) {
		  			alert("탄력근무제의 시작 요일은 "+arr[weekDay]+"요일 입니다.\n근무제 시작일을 다시 지정해 주세요.");
					return false;
		  		}
			}
		}
		
		return isValid;
	}
</script>