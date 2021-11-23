<div id="holidayMgr">
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
									<span class="label">기간</span>
									<input type="text" id="symd" name="symd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#symd" placeholder="연도-월-일" autocomplete="off"/>
									~
									<input type="text" id="eymd" name="eymd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#eymd" placeholder="연도-월-일" autocomplete="off"/>
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
								<div class="float-left title">공휴일관리</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>									
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
									<li><a href="javascript:doAction1('Upload')" class="basic authA">업로드</a></li>
									<li><a href="javascript:doAction1('Down2Excel');" class="basic">다운로드</a></li>
									<li><a href="javascript:doAction1('Rebuild');" class="basic">근무계획 재생성</a></li>
									<li><a href="javascript:doAction1('Reholiday');" class="basic">공휴일 적용</a></li> 
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
   		
   		$('#symd, #eymd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
   		$("#symd").val(moment(moment().format('YYYY')+"0101").format("YYYY-MM-DD"));
   		$("#eymd").val(moment(moment().format('YYYY')+"1231").format("YYYY-MM-DD"));
   		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사업장",			Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"bisinessPlaceCd",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"공휴일",			Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"holidayYmd",		KeyField:1,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"공휴일명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"holidayNm",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"양력여부",		Type:"CheckBox",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sunYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,
				InsertEdit:1,	EditLen:100 },
			{Header:"명절여부",		Type:"CheckBox",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"festiveYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"휴일생성",		Type:"CheckBox",	Hidden:Number("0"),	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"editYn",		    KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"유급여부",		Type:"CheckBox",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"payYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		//사업장
		var businessPlaceCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BUSINESS_PLACE_CD"), "선택");
		sheet1.SetColProperty("bisinessPlaceCd", {ComboText:businessPlaceCdList[0], ComboCode:businessPlaceCdList[1]} );

		sheetInit();
		doAction1("Search");
	});

	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/holiday/list" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			// 체크된 로우 검색
			var cnt1 = sheet1.CheckedRows(1); //삭제체크
			var cnt2 = sheet1.CheckedRows(10); // 휴일생성 체크
			
			if(cnt2 >= 1  ){
				alert("공휴일 적용은 휴일생성 버튼을 이용하시기 바랍니다.");
				return;
			}
			
			if(cnt1 >= 1){
				//삭제 저장
				if(confirm("공휴일 적용을 하셨을경우 해당일은 기본근무로 돌아갑니다.")){
				
				if(!dupChk(sheet1,"tenantId|enterCd|bisinessPlaceCd|holidayYmd", false, true)){break;}
				IBS_SaveName(document.sheetForm,sheet1);
				sheet1.DoSave("${rc.getContextPath()}/holiday/save", $("#sheetForm").serialize()); break;
				
				
				}else{
					return;
				}
			}else{
				//신규 및 업데이트 저장
				
				
				if(!dupChk(sheet1,"tenantId|enterCd|bisinessPlaceCd|holidayYmd", false, true)){break;}
				IBS_SaveName(document.sheetForm,sheet1);
				sheet1.DoSave("${rc.getContextPath()}/holiday/save", $("#sheetForm").serialize()); break;
				
				alert("bye");
			}
			
			break;
		case "Insert":
			var row = sheet1.DataInsert(0);
			sheet1.SetCellValue(row, "sunYn", "Y");
			sheet1.SetCellValue(row, "festiveYn", "N");
			sheet1.SetCellValue(row, "payYn", "Y");
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
		case "Rebuild" :
			var symdVal = moment($("#symd").val()).format("YYYY");
			var eymdVal = moment($("#eymd").val()).format("YYYY");
			
			if(symdVal != eymdVal){
				isuAlert("조회 시작년도, 종료년도는 같아야합니다.");
				break;
			}
			var yyyy = $("#symd").val();
			yyyy = yyyy.substring(0,4);
			var param = "?ymd=" + $("#symd").val();
			if(confirm(yyyy + "년 근무계획을 재생성 하시겠습니까?")){
				Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/empResetAsync" + param,
					type: "GET",
					contentType: 'application/json',
					dataType: "json",
					success: function(data) {
						if(data!=null && data.status=='OK') {
							isuAlert("근무계획 재작성 요청완료.");
						}else {
							isuAlert(data.message);
						}
	
					},
					error: function(e) {
						isuAlert("근무계획 재작성 에러 발생");
					}
				});
			}
			break;
		case "Reholiday" :
			
			var cnt1 = sheet1.CheckedRows(10); // 휴일생성 체크
			
			if(cnt1 < 1 ){
				alert("공휴일 적용만 가능합니다.");
				return;
			}
			
			if(!dupChk(sheet1,"tenantId|enterCd|bisinessPlaceCd|holidayYmd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/holiday/change"  , $("#sheetForm").serialize()); break;
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
	
	function sheet1_OnLoadExcel(result, code, msg) {

		var totalCnt = sheet1.RowCount();
		var newRowCnt = sheet1.RowCount("I");
		var newRowNum = totalCnt - newRowCnt + 1;

		for(var i = newRowNum; i <= (newRowNum + newRowCnt); i++){
			sheet1.SetCellValue(i, "sunYn", 'Y');
		}

		//빈 row 제거
		var delRows = [];
		for(var i=1; i<=sheet1.LastRow(); i++) {
			var bisinessPlaceCd = sheet1.GetCellValue(i, "bisinessPlaceCd").replace(/ /gi,"");
			var holidayYmd = sheet1.GetCellValue(i, "holidayYmd").replace(/ /gi,"");

			if(!bisinessPlaceCd && !holidayYmd)
				delRows.push(i);
		}
		
		var str="";
		delRows.map(function(r, idx) {
			if(idx!=0)
				str += "|";
			str+=r;
		});
		
		if(str!='')
			sheet1.RowDelete(str);
	}
</script>