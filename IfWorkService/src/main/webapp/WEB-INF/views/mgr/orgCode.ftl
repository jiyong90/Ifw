<div id="orgCode">
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
							<span class="label">기준일 </span>
							<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
						</td>
						<td>
							<span class="label">조직코드/조직명</span>
							<input id="orgKeyword"  name="orgKeyword"  type="text" class="text" />
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
								<div class="float-left title">조직정보</div>
								<ul id="btnDiv" class="float-right btn-wrap" style="display:none;">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
								</ul>
							</div>
						</div>
						<script type="text/javascript"> createIBSheet("sheet1", "100%", fullsheetH , "kr"); </script>
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
   		
   		var interfaceYn = '';
   		<#if interfaceYn?? && interfaceYn!='' && interfaceYn?exists >
   			interfaceYn = '${interfaceYn}';
		</#if>
		
		if('${interfaceYn}'=='N') {
			$("#btnDiv").show();
		}
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",	Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:0,Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",	Hidden:0,Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgCodeId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"조직코드",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgCd",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"조직명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:1,	Format:"Ymd",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"종료일",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:1,	Format:"Ymd",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"조직타입",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgType",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		if('${interfaceYn}'=='N') {
			sheet1.SetEditable(true);
			sheet1.SetColHidden("sDelete", 0);
			sheet1.SetColHidden("sStatus", 0);
		} else {
			sheet1.SetEditable(false);
			sheet1.SetColHidden("sDelete", 1);
			sheet1.SetColHidden("sStatus", 1);
		}

		//조직타입
		var orgTypeList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "ORG_TYPE"), "선택");
		sheet1.SetColProperty("orgType", {ComboText:orgTypeList[0], ComboCode:orgTypeList[1]} );

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/orgCode/list" , $("#sheetForm").serialize());
			break;
		case "Insert":
			sheet1.DataInsert(-1) ;
			break;
		case "Save":
			if(!dupChk(sheet1,"orgCd|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/orgCode/save", $("#sheetForm").serialize()); break;
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