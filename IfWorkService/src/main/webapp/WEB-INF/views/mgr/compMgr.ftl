<div id="taaCodeMgr">
 	<div class="container-fluid pt-3 pb-3 bg-white" style="height: calc(100vh - 72px);">
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
								<div class="float-left title">보상휴가 기준관리</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
								</ul>
							</div>
						</div>
						<script type="text/javascript"> createIBSheet("sheet1", "100%", fullsheetH, "kr"); </script>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
   		//resize
   		$(window).smartresize(sheetResize);
   		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",	Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compMgrId",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사업장",		Type:"Combo",		Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"businessPlaceCd",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사용구분",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	 ColMerge:0, SaveName:"compTimeType", 	KeyField:1, Format:"",  	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:10 },
   			{Header:"사용기준",	Type:"Combo",		Hidden:0,	Width:110,	Align:"Center",	 ColMerge:0, SaveName:"timeType",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:10 },
   			{Header:"사용기한",	Type:"Text",		Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"timeLimit",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:5 },
   			{Header:"사용기한단위",	Type:"Combo",		Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"limitType",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:10 },
   			{Header:"신청기준",	Type:"Combo",		Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"useType",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:10 },
   			{Header:"시작일",		Type:"Date",        Hidden:0, 	Width:90,   Align:"Center",  ColMerge:0, SaveName:"symd",			KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:10 },
			{Header:"종료일",		Type:"Date",      	Hidden:0, 	Width:90,   Align:"Center",  ColMerge:0, SaveName:"eymd",			KeyField:0, Format:"Ymd",   PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:10 },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		//사업장
		var businessPlaceCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BUSINESS_PLACE_CD"), "선택");
		sheet1.SetColProperty("businessPlaceCd", {ComboText:businessPlaceCdList[0], ComboCode:businessPlaceCdList[1]} );
		// 사용구분
        var compTimeType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "COMP_TIME_TYPE"), "선택");
        sheet1.SetColProperty("compTimeType", {ComboText:"|"+compTimeType[0], ComboCode:"|"+compTimeType[1]} );
		// 사용기준
        var timeType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "COMP_USED_TYPE"), "선택");
        sheet1.SetColProperty("timeType", {ComboText:"|"+timeType[0], ComboCode:"|"+timeType[1]} );
		// 사용기한단위
        var limitType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "COMP_LIMIT_TYPE"), "선택");
        sheet1.SetColProperty("limitType", {ComboText:"|"+limitType[0], ComboCode:"|"+limitType[1]} );
		// 신청기준
        var useType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "COMP_APPL_TYPE"), "선택");
        sheet1.SetColProperty("useType", {ComboText:"|"+useType[0], ComboCode:"|"+useType[1]} );
		

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/compMgr/list" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|businessPlaceCd|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/compMgr/save", $("#sheetForm").serialize()); break;

			break;
		case "Insert":
			sheet1.DataInsert(0) ;
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
</script>