<div id="empListMgr">
 	<div class="container-fluid pt-3 pb-3 bg-white">
	 	<div class="ibsheet-wrapper">
	 		<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
						<table>
							<tr>
								<td>
									<span>기준일 </span>
									<input type="text" id="sYmd" name="sYmd" class="date2" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}"/>
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
								<div class="float-left title">기본근무시간관리</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
								</ul>
							</div>
						</div>
						<script type="text/javascript">createIBSheet("sheet1", "100%", "100%","kr"); </script>
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
			{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"baseWorkMgrId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사업장",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"businessPlaceCd",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일자",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"종료일자",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		//사업장
		var businessPlaceCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BUSINESS_PLACE_CD"), "선택");
		sheet1.SetColProperty("businessPlaceCd", {ComboText:businessPlaceCdList[0], ComboCode:businessPlaceCdList[1]} );

		//근무제도
		var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "");
		sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );

		
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/basework/list" , $("#sheetForm").serialize());
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