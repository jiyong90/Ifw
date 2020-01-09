<div id="taaCodeMgr">
 	<div class="container-fluid mgr-wrap bg-white" style="height: calc(100vh - 70px);">
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
									<span class="label">년도 </span>
									<input type="text" id="sYy" name="sYy" class="required" value="${today?date("yyyy")?string("yyyy")}" />
								</td>
								<td>
									<span class="label">소속 </span>
									<input type="text" id="sOrgNm" name="sOrgNm" class="required"/>
								</td>
								
								<td>
									<span class="label">사번/성명 </span>
									<input type="text" id="sName" name="sName" class="required" />
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
				<div class="col-6 pr-3">
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">보상휴가 시간조회</div>
						</div>
					</div>
					<script type="text/javascript"> createIBSheet("sheet1", "100%", fullsheetH, "kr"); </script>
				</div>
				<div class="col-6">
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">보상휴가 생성 상세내역</div>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet2", "100%", fullsheetH,"kr"); </script>
				</div>
			</div>
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
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
   			{Header:"사번",		Type:"Text",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:13 },
   			{Header:"성명",		Type:"Text",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"empNm",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"발생시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	ColMerge:0,	SaveName:"totMinute",	KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 },
			{Header:"사용시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	ColMerge:0,	SaveName:"useMinute",	KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata2.Cols = [
			{Header:"No",		Type:"Seq",	Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"근무일",		Type:"Date",        Hidden:0, 	Width:90,   Align:"Center",  ColMerge:0, SaveName:"ymd",		KeyField:0, Format:"Ymd",	PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:10 },
			{Header:"근무시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"otMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 },
			{Header:"가산시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"addMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 },
			{Header:"발생합산시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"totMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 },
			{Header:"잔여시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"restMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 },
			{Header:"보상시간(분)",	Type:"Int",		Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"allowMinute",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:10 },
   			{Header:"사용시작일",	Type:"Date",        Hidden:0, 	Width:90,   Align:"Center",  ColMerge:0, SaveName:"symd",		KeyField:0, Format:"Ymd",	PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:10 },
			{Header:"사용종료일",	Type:"Date",      	Hidden:0, 	Width:90,   Align:"Center",  ColMerge:0, SaveName:"eymd",		KeyField:0, Format:"Ymd",	PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:10 }
		]; 
		
		IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/compCreate/list" , $("#sheetForm").serialize());
			break;
		}
	}
	
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "sabun="+sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun")
			          + "&sYy="+$("#sYy").val();
			sheet2.DoSearch( "${rc.getContextPath()}/compCreate/listDet" , param);
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
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		
		if(OldRow != NewRow && sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus") != "I"){
			sheet2.RemoveAll();
			doAction2('Search');
		}
	}

	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}

			sheetResize();
		} catch (ex) {
			alert("OnSearchEnd Event Error : " + ex);
		}
	}
</script>