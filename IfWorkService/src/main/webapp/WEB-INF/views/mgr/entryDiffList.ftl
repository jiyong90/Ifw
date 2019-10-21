<div id="entryCheckList">
 	<div class="container-fluid pt-3 pb-3 bg-white">
	 	<div class="ibsheet-wrapper">
	 		<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
					<table>
					<tr>
						<td>
							<span class="label">근무기간 </span>
							<input type="text" id="sYmd" name="sYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
										~
							<input type="text" id="eYmd" name="eYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
						</td>
						<td>
							<span class="label">차이시간 </span>
							<select id="searchValue" name="searchValue" class="box">
								<option value="ALL">출/퇴근</option>
								<option value="IN">출근</option>
								<option value="OUT">퇴근</option>
							</select>
							<input type="text" id="searchMinute" name="searchMinute" />
							<select id="searchCondition" name="searchCondition" class="box">
								<option value=""></option>
								<option value="more">이상</option>
								<option value="under">미만</option>
							</select>
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
								<div class="float-left title">출/퇴근 차이자 조회</div>
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
   	
	    $('#sYmd, #eYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22,FrozenCol:0,DataRowMerge:0};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No|No",		Type:"Seq",			Hidden:0,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태|상태",		Type:"Status",		Hidden:1,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id|id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workCalendarId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속|소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번|사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무일|근무일",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ymd",				KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도|근무제도",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|근무시간",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"출근|계획시각",	Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"planSdate",		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근|타각시각",	Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"entrySdate",		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근|차이시간",	Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"diffSminute",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근|계획시각",	Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"planEdate",		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근|타각시간",	Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"entryEdate",		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근|차이시간",	Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"diffEMinute",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"비고|비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "");
		sheet1.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );
		
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/entry/diff/list" , $("#sheetForm").serialize());
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
			alert("OnSearchEnd Event Error " + ex);
		}
	}
	
</script>