<div id="empListMgr">
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
									<input type="text" id="searchKeyword" name="searchKeyword" class="text" />
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
								<div class="float-left title">연차 기준 관리 &nbsp;<span id="Tooltip-workTeamMgr" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
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
			content: '연차기준관리 툴팁입니다. ',
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
			{Header:"상태",			Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"삭제",			Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"annMgrId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"annMgrId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"휴가구분",		Type:"Combo",		Width:60,	Align:"Center",	ColMerge:0,	SaveName:"taaTypeCd",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,InsertEdit:1, EditLen:100 },
			{Header:"(-)허용여부",	Type:"Combo",		ComboText: "N|Y",  Width:100,	Align:"Center",	ColMerge:0,	SaveName:"minusYn",	KeyField:1,	Format:"",
				PointCount:0,	UpdateEdit:1,	InsertEdit:1,
				EditLen:100 },
			{Header:"비고",			Type:"Text",		Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,
				EditLen:100 }
		];

		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		//	휴가구분
		var taaTypeCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/wtmAnnualMgr/taaTypeCd", "",false).DATA, "선택");
		sheet1.SetColProperty("taaTypeCd", {ComboText:taaTypeCdList[0], ComboCode:taaTypeCdList[1]} );

		sheetInit();
		doAction1("Search");
	});

	function doAction1(sAction) {
		switch (sAction) {
			case "Search":
				sheet1.DoSearch( "${rc.getContextPath()}/wtmAnnualMgr/list" , $("#sheetForm").serialize());
				break;
			case "Save":
				if(!dupChk(sheet1,"taaTypeCd", false, true)){break;}
				IBS_SaveName(document.sheetForm,sheet1);
				sheet1.DoSave("${rc.getContextPath()}/wtmAnnualMgr/save", $("#sheetForm").serialize()); break;

				break;
			case "Insert":
				sheet1.DataInsert(0) ;
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
				swtAlert(Msg);
			}

			sheetResize();
		} catch (ex) {
			swtAlert("OnSearchEnd Event Error : " + ex);
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