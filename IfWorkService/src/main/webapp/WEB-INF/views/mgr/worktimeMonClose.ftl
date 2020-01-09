<div id="monClose">
 	<div class="container-fluid mgr-wrap bg-white">
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
									<span class="label">마감년월 </span>
									<input type="text" id="searchYm" name="searchYm" class="date2 required" value="${today?date("yyyy-MM")?string("yyyyMM")}" data-toggle="datetimepicker" data-target="#searchYm" placeholder="연도-월" autocomplete="off"/>
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
								<div class="float-left title">근무마감 월별조회</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Down2Excel');" class="basic authA">다운로드</a></li>
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
   	
   		$('#searchYm').datetimepicker({
            format: 'YYYY-MM',
            language: 'ko'
        });
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No|No",				Type:"Seq",		Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"소속|소속",				Type:"Text",	Hidden:0,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번|사번",				Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",				Type:"Text",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무년월|근무년월",		Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"ym",			KeyField:0,	Format:"Ym",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도|근무제도",		Type:"Combo",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workTypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일|시작일",			Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"symd",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일|종료일",			Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"eymd",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|기본시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"baseMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|고정OT시간",	Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"baseOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|기본시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"workMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|고정OT시간",	Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"otMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|야간시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"otnMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|휴일시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"holMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|휴일연장시간",	Type:"Int",		Hidden:0,	Width:85,	Align:"Center",	ColMerge:0,	SaveName:"holOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|기본시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aWorkMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|고정OT시간",	Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|야간시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aOtnMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|휴일시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aHolMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|휴일연장시간",	Type:"Int",		Hidden:0,	Width:85,	Align:"Center",	ColMerge:0,	SaveName:"aHolOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"무급시간|무급시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aNonpayMinute",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"유급시간|유급시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aPayMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"지각시간|지각시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"lateMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"조퇴시간|조퇴시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"leaveMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결근시간|결근시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"absenceMinute",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(4);
		
		//근무제도
		var workTypeList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "WORK_TYPE_CD"), "");
		sheet1.SetColProperty("workTypeCd", {ComboText:workTypeList[0], ComboCode:workTypeList[1]} );
		
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/worktimeClose/monList" , $("#sheetForm").serialize());
			break;
		case "Down2Excel":	sheet1.Down2Excel(); break;
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