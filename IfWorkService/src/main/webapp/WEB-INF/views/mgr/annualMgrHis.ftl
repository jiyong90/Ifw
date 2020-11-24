<div id="empListMgr">
	<div class="container-fluid bg-white mgr-wrap">
		<div class="ibsheet-wrapper">
			<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
						<table>
							<td>
								<span class="magnifier"><i class="fas fa-search"></i></span>
								<span class="search-title">Search</span>
							</td>
							<td>
								<span class="label">기준기간 </span>
								<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
								~
								<input type="text" id="eYmd" name="eYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
							</td>
							<td>
								<span class="label">휴가 구분</span>
								<select id="searchType" name="searchType" class="box">
								</select>
							</td>
							<td>
								<span class="label">사번/성명 </span>
								<input type="text" id="searchKeyword" name="searchKeyword" />
							</td>
							<td>
								<a href="javascript:doAction1('Search');" class="button">조회</a>
							</td>
						</table>
					</div>
				</div>
			</form>
			<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
				<tr>
					<td>
						<div class="inner">
							<div class="sheet_title_wrap clearfix">
								<div class="float-left title">연차 내역 관리 &nbsp;<span id="Tooltip-workTeamMgr" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Upload')" class="basic authA">업로드</a></li>
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
									<li><a href="javascript:doAction1('Down2Excel');" class="basic">다운로드</a></li>
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
		//resize
		$(window).smartresize(sheetResize);

		$('#sYmd').datetimepicker({
			format: 'YYYY-MM-DD',
			language: 'ko'
		});

		$('#eYmd').datetimepicker({
			format: 'YYYY-MM-DD',
			language: 'ko'
		});

		$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");

		new jBox('Tooltip', {
			attach: '#Tooltip-workTeamMgr',
			target: '#Tooltip-workTeamMgr',
			theme: 'TooltipBorder',
			trigger: 'click',
			adjustTracker: true,
			closeOnClick: 'body',
			closeButton: 'box',
			animation: 'move',
			position: {
				x: 'left',
				y: 'top'
			},
			outside: 'y',
			pointer: 'left:20',
			offset: {
				x: 25
			},
			content: '연차내역관리 툴팁입니다. ',
			onOpen: function () {
				this.source.addClass('active');
			},
			onClose: function () {
				this.source.removeClass('active');
			}
		});

		var initdata1 = {};

		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"상태",			Type:"Status",		Hidden:Number("0"), Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"삭제",			Type:"DelCheck",	Hidden:Number("0"), Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"annCreateId",	Type:"Text",		Hidden:1,			Width:100,	Align:"Center",	ColMerge:0,	SaveName:"annCreateId",	KeyField:1,	Format:"" 	, PointCount:0,	UpdateEdit:0, InsertEdit:0,	EditLen:100 },
			{Header:"소속",			Type:"Text",		Hidden:0,			Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"	  ,	KeyField:1,	Format:"" 	, PointCount:0,	UpdateEdit:0, InsertEdit:0,	EditLen:100 },
			{Header:"사번",			Type:"Text",		Hidden:0,			Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun"	  , KeyField:1,	Format:"" 	, PointCount:0,	UpdateEdit:0, InsertEdit:0,	EditLen:100 },
			{Header:"성명",			Type:"Text",		Hidden:0,			Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm"	  , KeyField:1,	Format:"" 	, PointCount:0,	UpdateEdit:0, InsertEdit:1,	EditLen:100 },
			{Header:"휴가구분",		Type:"Combo",		Hidden:0,			Width:60,	Align:"Center",	ColMerge:0,	SaveName:"taaTypeCd"  , KeyField:1,	Format:"" 	, PointCount:0,	UpdateEdit:1, InsertEdit:1,	EditLen:100 },
			{Header:"년도",			Type:"Text",        Hidden:0,   		Width:90,   Align:"Center", ColMerge:0, SaveName:"yy"		  , KeyField:1, Format:"" 	, PointCount:0, UpdateEdit:1, InsertEdit:1, EditLen:100 },
			{Header:"사용시작일",	Type:"Date",        Hidden:0,   		Width:90,   Align:"Center", ColMerge:0, SaveName:"symd"		  , KeyField:1, Format:"Ymd", PointCount:0, UpdateEdit:1, InsertEdit:1, EditLen:100 },
			{Header:"사용종료일",	Type:"Date",        Hidden:0,   		Width:90,   Align:"Center", ColMerge:0, SaveName:"eymd"		  , KeyField:1, Format:"Ymd", PointCount:0, UpdateEdit:1, InsertEdit:1, EditLen:100 },
			{Header:"발생일수",		Type:"Text",		Hidden:0,     		Width:60,	Align:"Center",	ColMerge:0,	SaveName:"createCnt"  ,	KeyField:1,	Format:""	, PointCount:0,	UpdateEdit:1, InsertEdit:1, EditLen:100 },
			{Header:"사용일수",		Type:"Text",		Hidden:0, 			Width:60,	Align:"Center",	ColMerge:0,	SaveName:"usedCnt"    ,	KeyField:0,	Format:""	, PointCount:0,	UpdateEdit:0, InsertEdit:0, EditLen:100 },
			{Header:"비고",			Type:"Text",		Hidden:0,			Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note"       ,	KeyField:0,	Format:""   , PointCount:0,	UpdateEdit:1, InsertEdit:1,	EditLen:100 }
		];

		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		// 휴가구분
		var taaTypeCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/wtmAnnualMgr/taaTypeCd", "",false).DATA, "선택");
		sheet1.SetColProperty("taaTypeCd", {ComboText:taaTypeCdList[0], ComboCode:taaTypeCdList[1]} );

		$("#searchType").html(taaTypeCdList[2]);

		//이름
		setSheetAutocompleteEmp( "sheet1", "empNm" );

		sheetInit();
		doAction1("Search");
	});

	function doAction1(sAction) {
		switch (sAction) {
			case "Search":
				sheet1.DoSearch( "${rc.getContextPath()}/wtmAnnualCreate/list" , $("#sheetForm").serialize());
				break;
			case "Save":
				if(!dupChk(sheet1,"sabun|taaTypeCd|yy|symd|eymd", false, true)){break;}
				IBS_SaveName(document.sheetForm,sheet1);
				sheet1.DoSave("${rc.getContextPath()}/wtmAnnualCreate/save", $("#sheetForm").serialize()); break;
				break;
			case "Insert":
				sheet1.DataInsert(0) ;
				break;
			case "Upload":
				var params = {Mode:"HeaderMatch", StartRow:1, WorkSheetNo:1, Append:1};
				sheet1.LoadExcel(params);
				break;
			case "Down2Excel":
				var downcol = makeHiddenSkipCol(sheet1);
				var param  = {DownCols:downcol,SheetDesign:1,Merge:1};
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

	function getReturnValue(returnValue) {
		// var rv = $.parseJSON(returnValue);
		console.log(returnValue);
		sheet1.SetCellValue(gPRow, "sabun",returnValue.sabun);
		sheet1.SetCellValue(gPRow, "empNm",returnValue.empNm);
		sheet1.SetCellValue(gPRow, "orgNm",returnValue.orgNm);
	}
</script>