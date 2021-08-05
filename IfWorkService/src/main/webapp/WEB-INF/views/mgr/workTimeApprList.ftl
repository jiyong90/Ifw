<div id="workTimeApprList">
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
						<span class="label">조회기간 </span>
						<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
									~
									<input type="text" id="eYmd" name="eYmd" class="date2 required datetimepicker-input"  data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
					</td>
					<td>
						<span class="label">사번/성명</span>
						<input id="searchKeyword"  name="searchKeyword"  type="text" class="text" />
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
						<div class="float-left title">인정근무조회 &nbsp;</div>
							<ul class="float-right btn-wrap">
								<!-- li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li -->
								<li><a href="javascript:doAction1('Down2Excel')" class="basic authR">다운로드</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet1", "100%", fullsheetH,"kr"); </script>
				</td>
			</tr>
		</table>
	</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
   		$('#sYmd, #eYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
   		$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    $("#eYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad, MergeSheet:msPrevColumnMerge, Page:22};
		initdata1.HeaderMode = {Sort:0,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			//{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			//{Header:"id|id",			 Type:"Text",	Hidden:1,	Width:100,	Align:"Center",		ColMerge:0,		SaveName:"workCalendarId",	KeyField:1,		Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		 Type:"Text",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:1,		SaveName:"sabun",			KeyField:0,		Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		 Type:"Text",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:1,		SaveName:"empNm",			KeyField:0,		Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"부서명",	 Type:"Text",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:0,		SaveName:"orgNm",			KeyField:0,		Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"날짜", 		 Type:"Date", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"ymd", 			KeyField:0,		Format:"Ymd",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근태코드", Type:"Text",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:0,		SaveName:"taaNms",			KeyField:0,		Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도", Type:"Combo",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:0,		SaveName:"timeCdMgrId",		KeyField:0,		Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근타각시각", Type:"Text",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:0,		SaveName:"entrySdate",		KeyField:0,		Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근타각시각", Type:"Text",	Hidden:0,	Width:100,	Align:"Center",		ColMerge:0,		SaveName:"entryEdate",		KeyField:0,		Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간(분)", Type:"Int", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"diffEntryDate", 	KeyField:0, 	Format:"", 	PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, EditLen:100 },
			{Header:"정상근무(분)", Type:"Int", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"apprMinute", 		KeyField:0, 	Format:"", 	PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, 	EditLen:100},
			{Header:"연장근무\n시작시각", Type:"Text", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"planSdate", 		KeyField:0, 	Format:"Hm", 	PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, 	EditLen:100},
			{Header:"연장근무\n퇴근시각", Type:"Text", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"planEdate", 		KeyField:0, 	Format:"Hm", 	PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, 	EditLen:100},
			{Header:"연장근무시간(분)", Type:"Int", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"diffPlanDate", 	KeyField:0, 	Format:"", 	PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, 	EditLen:100},
			{Header:"연장근무(분)", Type:"Int", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"apprPlanDate", 	KeyField:0, 	Format:"", 		PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, 	EditLen:100},
			{Header:"총인정시간(분)", Type:"Int", 	Hidden:0, 	Width:100, 	Align:"Center", 	ColMerge:0, 	SaveName:"sumApprDate", 	KeyField:0, 	CalcLogic:"|apprMinute|+|apprPlanDate|", Format:"", 		PointCount:0, 	UpdateEdit:0, 	InsertEdit:0, 	EditLen:100}
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);	//sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(8);

		//근무제도
		//var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "");
		//sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );
		
		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=",false).DATA, "");
		// var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "");
		sheet1.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );
		
		//타각구분
		//var entryTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "ENTRY_TYPE_CD"), "");
		//sheet1.SetColProperty("entryStypeCd", {ComboText:entryTypeCdList[0], ComboCode:entryTypeCdList[1]} );
		//sheet1.SetColProperty("entryEtypeCd", {ComboText:entryTypeCdList[0], ComboCode:entryTypeCdList[1]} );
		
		var info = [{StdCol:"sabun" , SumCols:"diffEntryDate|apprMinute|diffPlanDate|apprPlanDate|sumApprDate", ShowCumulate:0, CaptionText:"기간별 합계","CaptionCol": "sabun"}];
			sheet1.ShowSubSum(info);
			sheet1.SetSumRowHidden(0);
		
		sheetInit();
		// doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch("${rc.getContextPath()}/workTimeApprList/list" , $("#sheetForm").serialize());
			break;
	/*
		case "Save":
			if(!dupChk(sheet1,"businessPlaceCd|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/basework/save", $("#sheetForm").serialize()); break;

			break;
	*/
		case "Insert":
			sheet1.DataInsert(0) ;
			break;
		case "Down2Excel":
			var downcol = makeHiddenSkipCol(sheet1);
			var param = {DownCols:downcol, SheetDesign:1, Merge:1};
			sheet1.Down2Excel(param); 
			break;
		}
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			
		} catch (ex) {
			swtAlert("OnSearchEnd Event Error " + ex);
		}
	}

	// 저장 후 메시지
	function sheet1_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				swtAlert(Msg);
			}
			doAction1("Search");
		} catch (ex) {
			swtAlert("OnSaveEnd Event Error " + ex);
		}
	}
	
</script>