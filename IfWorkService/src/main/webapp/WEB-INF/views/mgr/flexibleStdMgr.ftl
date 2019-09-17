<div id="flexibleStdMgr">
 	<div class="container-fluid pt-3 pb-3 bg-white">
 	<div class="ibsheet-wrapper">
		<form id="sheetForm" name="sheetForm">
			<div class="sheet_search outer">
				<div>
					<table>
						<tr>
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
		<div class="row no-gutters">
			<div class="col-5 pr-3">
				<div class="inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">근무제도관리</div>
						<ul class="float-right btn-wrap">
							<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
							<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
						</ul>
					</div>
				</div>
				<script type="text/javascript">createIBSheet("sheet1", "100%", "calc(100vh - 232px)","kr"); </script>
			</div>
			<div class="col-7 pt-2">
				<div class="innertab inner">
					<div id="tabs" class="tab">
						<ul class="outer tab_bottom">
							<li><a href="#tabs-1">근무제기준</a></li>
							<li><a href="#tabs-2">근무제패턴</a></li>
						</ul>
						<div id="tabs-1">
							<div  class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<div class="float-left title" id="searchAppText">근무제기준</div>
									<ul class="float-right btn-wrap">
										<li><a href="javascript:doAction1('Save2')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<div id="view_sele">
									<table class="default">
										<tbody>
											<tr>
												<th>근무가능시각</th>
												<td colspan="3">
													
													<input type="text" id="workShm" name="workShm" class="date2 required" data-toggle="datetimepicker" data-target="#workShm" autocomplete="off"/>
													~
													<input type="text" id="workEhm" name="workEhm" class="date2 required" data-toggle="datetimepicker" data-target="#workEhm" autocomplete="off"/>
												</td>
											</tr>
											<tr>
												<th>코어근무시각</th>
												<td>
													<input type="text" id="coreShm" name="coreShm" class="date2 required" data-toggle="datetimepicker" data-target="#coreShm" autocomplete="off"/>
													~
													<input type="text" id="coreEhm" name="coreEhm" class="date2 required" data-toggle="datetimepicker" data-target="#coreEhm" autocomplete="off"/>
												</td>
												<th>코어시간체크여부</th>
												<td>
													<input type="checkbox" id="coreChkYn" name="coreChkYn" data-target="#coreChkYn"/>
												</td>
											</tr>
											<tr>
												<th>기본근무선소진여부</th>
												<td>
													<input type="checkbox" id="exhaustionYn" name="exhaustionYn" data-target="#exhaustionYn"/>
												</td>
												<th>공휴일제외여부</th>
												<td>
													<input type="checkbox" id="holExceptYn" name="holExceptYn" data-target="#holExceptYn"/>
												</td>
											</tr>
											<tr>
												<th>간주근무시간</th>
												<td>
													<select id="regardTimeCdId">
					                                    <option>Default select</option>
					                                </select>
												</td>
												<th>일 기본근무시간</th>
												<td>
													<input type="text" id="defaultWorkMinute" name="defaultWorkMinute" data-target="#defaultWorkMinute" />
												</td>
											</tr>
											<tr>
												<th>근무요일지정</th>
												<td colspan="3">
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 월
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 화
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 수
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 목
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 금
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 토
													<input type="checkbox" id="workDaysOpt" name="workDaysOpt" data-target="#workDaysOpt"/> 일
												</td>
											</tr>
											<tr>
												<th>신청기간지정</th>
												<td colspan="3">
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 1주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 2주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 3주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 4주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 1개월
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 2개월
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" data-target="#usedTermOpt"/> 3개월
												</td>
											</tr>
											<tr>
												<th>비고</th>
												<td colspan="3">
													<textarea id="note" cols=50 rows=3>
													</textarea>
												</td>
											</tr>
										</tbody>
										
									</table>
								</div>
									
							</div>
						</div>
						<div id="tabs-2">
							<div  class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<div class="float-left title" id="searchAppText">반복패턴</div>
									<ul class="float-right btn-wrap">
										<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
										<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<script type="text/javascript">createIBSheet("sheet2", "100%", "calc(100vh - 270px)","kr"); </script>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		</from>
	</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
   		//resize
		$(window).smartresize(sheetResize);
   	
   		$('#sYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
        
        $('#workShm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#workEhm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        $('#coreShm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#coreEhm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무시간",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"workTypeCd",  	KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
			{Header:"근무명칭",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"flexibleNm",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",        Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"useSymd",        	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"종료일",			Type:"Date",        Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"useEymd",        	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:100 },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		//근무제도코드
		
		var workTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "WORK_TYPE_CD"), "선택");
		sheet1.SetColProperty("workTypeCd", {ComboText:workTypeCdList[0], ComboCode:workTypeCdList[1]} );
		 
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workPattDetId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"순서",			Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"seq",	 KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"근무시간",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"timeCdMgrId",  KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
			{Header:"비고",			Type:"Text",	 	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);
        
		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "선택");
		sheet2.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );

		sheetInit();
		doAction1("Search");
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
			sheet1.DoSearch( "${rc.getContextPath()}/flexibleStd/listWeb" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|workTypeCd|flexibleNm|useSymd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/flexibleStd/saveWeb", $("#sheetForm").serialize()); break;
			break;
			
		case "Save2":
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/flexibleStd/saveOpt", $("#sheetForm").serialize()); break;
			break;	
			
		case "Insert":
			sheet1.DataInsert(-1) ;
			break;
		}
	}
	
	//근무제패턴저장
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "flexibleStdMgrId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleStd/listPatt" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleStd/savePatt", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var flexibleStdMgrId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId");
			if(flexibleStdMgrId == ""){
				alert("근무제도 저장 후 근무제패턴을 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(-1) ;
				sheet2.SetCellValue(row, "flexibleStdMgrId" , flexibleStdMgrId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		}
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			
			if (Msg != "") {
				alert(Msg);
			}
			sheet2.RemoveAll();
			sheetResize();
			if(iframeIdx == 0) {
				showIframe();
			} else {
				$( "#tabs" ).tabs({active:0});
			}

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
		
		if(OldRow != NewRow && sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus") != "I"){
			sheet2.RemoveAll();
			doAction2('Search');
			// 옵션마스터용 화면그리고 값을 셋팅해야함.
		}
	}
	
	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			if (Msg != "") {
				alert(Msg);
			}

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
</script>