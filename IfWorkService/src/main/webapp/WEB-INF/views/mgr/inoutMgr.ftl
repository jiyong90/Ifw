<div id="inoutMgr">
 	<div class="container-fluid pt-3 pb-3 bg-white">
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
									<span class="label">근무기간 </span>
									<input type="text" id="sYmd" name="sYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
									~
									<input type="text" id="eYmd" name="eYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
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
								<div class="float-left title">근무시간수정</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
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
        
   		$('#eYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
        
   		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:2, MergeSheet:msHeaderOnly,FrozenCol:9};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No|No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제|삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태|상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id|id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workCalendarId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속|소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번|사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무일|근무일",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ymd",				KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"메시지|메시지",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"msg",				KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도|근무제도",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무상태|근무상태",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"관리자|출근",		Type:"Text",	 Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"chgSdate", 		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:16 },
			{Header:"관리자|퇴근",		Type:"Text",	 Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"chgEdate",		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:16 },
			{Header:"변경사유|변경사유",	Type:"Text",	 Hidden:0,  Width:200,  Align:"Left",    ColMerge:0, SaveName:"reason", 		KeyField:1, Format:"",      PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:1000 ,	MultiLineText:1 },
			{Header:"원본|출근시간",	Type:"Text",	 Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"entrySdate", 	KeyField:0,	Format:"YmdHms",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:16 },
			{Header:"원본|출근구분",	Type:"Combo",	 Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"entryStypeCd", 	KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:16 },
			{Header:"원본|퇴근시간",	Type:"Text",	 Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"entryEdate",		KeyField:0,	Format:"YmdHms",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:16 },
			{Header:"원본|퇴근구분",	Type:"Combo",	 Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"entryEtypeCd",	KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:16 },
			{Header:"수정자|수정자",	Type:"Text",	 Hidden:0,	Width:100,	Align:"Left",	 ColMerge:0, SaveName:"updateId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"수정일|수정일",	Type:"Text",	 Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"updateDate", 	KeyField:0,	Format:"YmdHms",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:16 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(3);

		//출근구분
		var entrytypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "ENTRY_TYPE_CD"), "");
		sheet1.SetColProperty("entryStypeCd", {ComboText:"|"+entrytypeCdList[0], ComboCode:"|"+entrytypeCdList[1]} );
		sheet1.SetColProperty("entryEtypeCd", {ComboText:"|"+entrytypeCdList[0], ComboCode:"|"+entrytypeCdList[1]} );
		
		//이름
        setSheetAutocompleteEmp( "sheet1", "empNm" );
		
		sheetInit();
		doAction1("Search");
	});

	// 셀에서 키보드가 눌렀을때 발생하는 이벤트
	function sheet1_OnChange(Row, Col, Value) {  
		if(sheet1.ColSaveName(Col) == "sabun" || sheet1.ColSaveName(Col) == "ymd"){
			var sabun = sheet1.GetCellValue(Row, "sabun");
			var ymd = sheet1.GetCellValue(Row, "ymd");
			if(sabun != '' && ymd != ''){
				getDayWorkInfo(Row, sabun, ymd);
			}
		}
	}
	
	function getDayWorkInfo(row, sabun, ymd) {
		if(sabun != '' && ymd != ''){
			var param = "sabun=" + sabun
						+ "&ymd=" + ymd;
			var rtn = ajaxCall("${rc.getContextPath()}/calendar/dayInfo", param ,false).DATA;
			if(rtn != null && rtn != "") {
				sheet1.SetCellValue(row, "entrySdate", rtn.entrySdate);
				sheet1.SetCellValue(row, "entryStypeCd", rtn.entryStypeCd);
				sheet1.SetCellValue(row, "entryEdate", rtn.entryEdate);
				sheet1.SetCellValue(row, "entryEtypeCd", rtn.entryEtypeCd);
				sheet1.SetCellValue(row, "msg", "OK");
			} else {
				//alert("입력한 "+sabun+" 사원의 근무시간이 존재하지 않습니다.");
				sheet1.SetCellValue(row, "msg", "데이터없음");
			}
		}
	}
	
   	function doAction1(sAction) {
		switch (sAction) {
	 	case "Insert":
       		sheet1.DataInsert(0);
			break;
		case "Search":
			if($("#sYmd").val() == '' || $("#eYmd").val() == '') {
				alert("근무기간을 입력하세요."); 
				reuturn;
			}
			sheet1.DoSearch( "${rc.getContextPath()}/inOutChange/list" , $("#sheetForm").serialize());
			break;
		case "Save":
			var saveYn = sheetDataChk();
			if(saveYn) {
				IBS_SaveName(document.sheetForm,sheet1);
				sheet1.DoSave( "${rc.getContextPath()}/inOutChange/save", $("#sheetForm").serialize());
			}
			break;
		}
	}

	// 저장전 데이터 검증
	function sheetDataChk() {
		var saveYn = false;
		console.log(sheetDataChk);
		for(var i=sheet1.HeaderRows();i<sheet1.RowCount()+sheet1.HeaderRows(); i++){
			if(sheet1.GetCellValue(i, "sStatus") == "I") {

				if((sheet1.GetCellValue(i,"empNm") == "" && sheet1.GetCellValue(i,"sabun") == "") ){
					alert("사원정보가 없습니다. 사원 이름을 다시 검색해 주세요.");
					sheet1.SelectCell(i, "empNm");
					return saveYn;
				}
				
				if(sheet1.GetCellValue(i,"ymd") == ""){
					alert("근무일이 비었습니다.");
					sheet1.SelectCell(i, "ymd");
					return saveYn;
				}

				if((sheet1.GetCellValue(i,"chgSdate") == "" && sheet1.GetCellValue(i,"chgEdate") == "")){
					alert("근무시간이 없습니다. 근무시간 등록 후 입력바랍니다.");
					sheet1.SelectCell(i, "chgSdate");
					return saveYn;
				}

				if(((sheet1.GetCellValue(i,"chgSdate") != "" && sheet1.GetCellValue(i,"chgEdate") != "")) 
						&& (sheet1.GetCellValue(i,"chgSdate") > sheet1.GetCellValue(i,"chgEdate"))){
					alert("출근시간은 퇴근시간보다 이전이어야 합니다.");
					sheet1.SelectCell(i, "chgSdate");
					return saveYn;
				}

				if(sheet1.GetCellValue(i, "msg") != 'OK') {
					alert(sheet1.GetCellValue(i, "sabun")+" 사번의 " + sheet1.GetCellValue(i, "ymd") + " 항목을 저장할 수 없습니다.");
					return saveYn;
				}

			}
		}
		
		saveYn = true;
		return saveYn;
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

	// 저장 후 메시지
	function sheet1_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			console.log("1" + Code);
			console.log("2" + Msg);
			console.log("3" + StCode);
			console.log("4" + StMsg);
			if (Msg != "") {
				alert(Msg);
			}
			doAction1("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	function getReturnValue(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
   		sheet1.SetCellValue(gPRow, "sabun",returnValue.sabun);
		sheet1.SetCellValue(gPRow, "empNm",returnValue.empNm);
        sheet1.SetCellValue(gPRow, "orgCd",returnValue.orgCd);
        sheet1.SetCellValue(gPRow, "classCd",returnValue.classCd);
	}
</script>