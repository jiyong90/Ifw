<div id="worktimeCheckAllList">
	<!-- 결재의견 modal end -->
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
									<span class="label">근무일 </span>
									<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
									 ~
									<input type="text" id="eYmd" name="eYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
								</td>
								<td>
									<span class="label">사번/성명 </span>
									<input type="text" id="searchKeyword" name="searchKeyword" />
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
								<div class="float-left title">기간 근무시간 조회</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Down2Excel')" class="basic authR">다운로드</a></li>
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

		$('#sYmd, #eYmd').datetimepicker({
			format: 'YYYY-MM-DD',
			language: 'ko'
		});

		$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
		$("#eYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");

		//근무기준
		var workSearchTypeCd = convCode(codeList("${rc.getContextPath()}/code/list", "WORK_SEARCH_TYPE_CD"), "");
		$("#searchType").append(workSearchTypeCd[2]);

		var initdata1 = {};
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msPrevColumnMerge,FrozenCol:0,DataRowMerge:2000};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		initdata1.Cols = [
			// {Header:"No",			Type:"Seq",		Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,		SaveName:"sNo" },
			{Header:"근무일",		    Type:"Date",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:1,		SaveName:"ymd",			 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		    	Type:"Text",	Hidden:0,	Width:150,	Align:"Center",	ColMerge:1,		SaveName:"orgNm", 	 	 KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		    	Type:"Text",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:1,		SaveName:"sabun", 	 	 KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"이름",		    	Type:"Text",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:1,		SaveName:"empNm", 	 	 KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도",		    Type:"Text",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:1,		SaveName:"flexibleNm", 	 KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근시각",		    Type:"Date",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:1,		SaveName:"entrySdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근시각",		    Type:"Date",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:1,		SaveName:"entryEdate",	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무구분",		    Type:"Text",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,		SaveName:"timeTypeNm",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근태",  		    Type:"Text",    Hidden:0,   Width:120,  Align:"Center",	ColMerge:0,		SaveName:"taaNm",   	 KeyField:0,    Format:"",    	PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:100 },
			{Header:"계획시작시각",	    Type:"Date",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,		SaveName:"planSdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"계획종료시각",	    Type:"Date",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,		SaveName:"planEdate",	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"계획근무시간(분)",	Type:"Text",    Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,		SaveName:"planMinute",	 KeyField:0,	Format:"", 		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정시작시각",	    Type:"Date",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,		SaveName:"apprSdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정종료시각",	    Type:"Date",	Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,		SaveName:"apprEdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정근무시간(분)",	Type:"Text",    Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,		SaveName:"apprMinute",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
		];

		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		orgCodes = ajaxCall("${rc.getContextPath()}/code/getOrgCode", "ymd="+$("#sYmd").val(), false).DATA;
		var orgCodeList = stfConvCode(orgCodes, "선택");
		$("#orgCode").html(orgCodeList[2]);

		sheetInit();
		// doAction1("Search");
	});

	function doAction1(sAction) {
		switch (sAction) {
			case "Search":
				sheet1.DoSearch( "${rc.getContextPath()}/worktime/checkAll/list" , $("#sheetForm").serialize());
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
			alert("OnSearchEnd Event Error " + ex);
		}
	}

	//셀 클릭시 이벤트
	function sheet1_OnClick(Row, Col, Value) {
		try{

		}catch(ex){
			alert("OnClick Event Error : " + ex);
		}
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}

			var preValue = "";
			var backColor = "";
			for(i=0;i<=sheet1.RowCount();i++){
				var curValue = sheet1.GetCellValue(i, "ymd");

				if(i > 0) {
					backColor = sheet1.GetRowBackColor(i-1);
					if(preValue != curValue){
						if(backColor == "#daf8f6") {
							backColor = "#d2edff";
						} else {
							backColor = "#daf8f6";
						}
					}
				} else {
					backColor = "#f7f7fa";
				}
				sheet1.SetRowBackColor(i,backColor);
				preValue = sheet1.GetCellValue(i, "ymd");
			}

			// sheet1.RangeBackColor(0, 0, sheet1.RowCount(), 1, "#f7f7fa");
			sheet1.RenderSheet(2);
		} catch (ex) {
			alert("OnSearchEnd Event Error " + ex);
		}
	}
</script>