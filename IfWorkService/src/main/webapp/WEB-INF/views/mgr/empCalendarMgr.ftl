<div id="empCalendarMgr">
 	<div class="container-fluid">
 	<div class="ibsheet-wrapper">
 		<form id="sheetForm" name="sheetForm">
			<div class="sheet_search outer">
				<div>
				<table>
				<tr>
					<td>
						<span>근무기간 </span>
						<input type="text" id="sYmd" name="sYmd" class="date2" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}"/> ~
						<input type="text" id="eYmd" name="eYmd" class="date2" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}"/>
					</td>
					<td>
						<span>사번/성명</span>
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
						<div class="sheet_title">
							<li id="txt" class="txt">근무캘린더<span id="searchAppText1"  style="margin-left:10px;"></span></li>
							<ul>
								<li class="btn">
									<a href="javascript:doAction1('Save')" class="basic authA">저장</a>
								</li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet1", "100%", "40%","kr"); </script>
				</td>
			</tr>
			<tr>
				<td>
					<div class="inner">
						<div class="sheet_title">
							<li id="txt" class="txt">근무상세결과<span id="searchAppText2"  style="margin-left:10px;"></span></li>
							<ul>
								<li class="btn">
									<a href="javascript:doAction2('Insert')" class="basic authA">입력</a>
									<a href="javascript:doAction2('Save')" class="basic authA">저장</a>
								</li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet2", "100%", "30%","kr"); </script>
				</td>
			</tr>
		</table>
	</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workCalendarId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무일",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ymd",				KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"근무합산시간",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"totTime",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근시각",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entrySdate",		KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근구분",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entryStypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근구분",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entryEdate",		KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근시각",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entryEtypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(8);

		//근무제도
		var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "");
		sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );
		
		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "");
		sheet1.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );
		17
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"empId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleEmpId",		KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workDayResultId",		KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시간구분",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeTypeCd",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"근태코드",  		Type:"Combo",     	Hidden:0,   Width:70,  	Align:"Center",  ColMerge:0, SaveName:"taaCd",   	 KeyField:0,    Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:100  },
            {Header:"계획시작시각",		Type:"Date",	 	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"planSdate", 	 KeyField:0,	Format:"YmdHms",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획종료시각",		Type:"Date",	 	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"planEdate",	 KeyField:0,	Format:"YmdHms",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획근무시간",		Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"planMinute",	 KeyField:0,	Format:"", 		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정시작시각",		Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprSdate", 	 KeyField:0,	Format:"",	 	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"인정종료시각",		Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprEdate", 	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"인정근무시간",		Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprMinute",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"비고",			Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];


        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);

		//시간구분
		var timeTypeCdList = convCode(codeList("${rc.getContextPath()}/code/list", "TIME_TYPE_CD"), "선택"); 
        sheet2.SetColProperty("timeTypeCd", {ComboText:"|"+timeTypeCdList[0], ComboCode:"|"+timeTypeCdList[1]} );
        
		//근태코드
		var taaCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/taaCode/list", "",false).DATA, "");
		sheet2.SetColProperty("taaCd", {ComboText:"|"+taaCdList[0], ComboCode:"|"+taaCdList[1]} );
		
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/calendar/all" , $("#sheetForm").serialize());
			break;
		case "Save":
			if(!dupChk(sheet1,"businessPlaceCd|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/basework/save", $("#sheetForm").serialize()); break;

			break;
		case "Insert":
			sheet1.DataInsert(0) ;
			break;
		}
	}

	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "ymd="+sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd") + "&sabun="+sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleEmp/caldays" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleEmp/save/caldays", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			sheet2.DataInsert(0);
			//var row = sheet2.DataInsert(0) ;
			//sheet2.SetCellValue(row, "timeCdMgrId" , sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId"));
			break;
		}
	}
	
	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
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
			doAction2('Search');
		}
	}
</script>