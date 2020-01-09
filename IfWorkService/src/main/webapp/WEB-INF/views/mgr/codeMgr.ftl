<div id="timeCdMgr">
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
						<span class="label">기준일 </span>
						<input type="text" id="sYmd" name="sYmd" class="date2 date-custom required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
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
						<div class="float-left title">그룹코드관리</div>
						<ul class="float-right btn-wrap">
							<li><a href="javascript:doAction1('Insert')" class="basic button">입력</a></li>
							<li><a href="javascript:doAction1('Save')" class="basic">저장</a></li>
						</ul>
						</div>
					</div>
					
					<script type="text/javascript">createIBSheet("sheet1", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
			<tr>
				<td>
					<div class="inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">공통코드관리</div>
							<ul class="float-right btn-wrap" id="sheet2Btn">
								<li><a href="javascript:doAction2('Insert')" class="basic button">입력</a></li>
								<li><a href="javascript:doAction2('Save')" class="basic">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">
						createIBSheet("sheet2", "100%", halfsheetH,"kr"); 
					</script>
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
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"oId",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"codeGrpId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"그룹코드",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"grpCodeCd",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"그룹코드명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"grpCodeNm",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"수정여부",		Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"editYn",		    KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"사용여부",		Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"usedYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"codeId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"그룹코드",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"grpCodeCd",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"코드",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"codeCd",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"코드명",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"codeNm",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"종료일",			Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"순차",			Type:"Int",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"seq",			KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:5 },
			{Header:"비고",			Type:"Text",	  Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
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
			sheet1.DoSearch( "${rc.getContextPath()}/code/listGrp" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|grpCodeCd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/code/saveGrp", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var row = sheet1.DataInsert(0) ;
			// alert(sheet1.GetRowEditable(row)); 
			break;
		}
	}
	
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "grpCodeCd=" + sheet1.GetCellValue( sheet1.GetSelectRow(), "grpCodeCd");
			param = param + "&sYmd=" + $("#sYmd").val();
			sheet2.DoSearch( "${rc.getContextPath()}/code/listWeb" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/code/save", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var grpCodeCd = sheet1.GetCellValue( sheet1.GetSelectRow(), "grpCodeCd");
			if(grpCodeCd == ""){
				alert("그룹코드 저장 후 공통코드를 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(0) ;
				sheet2.SetCellValue(row, "grpCodeCd" , grpCodeCd);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		}
	}

	// 조회 후 에러 메시지
    function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
       try {
          if (StCode == 401) {
        	  window.parent.location.href = loginUrl;
	   	  } 
	   	  for(i=1;i<=sheet1.RowCount();i++){
	   	  	var editYn = sheet1.GetCellValue(i, "editYn");
	   	  	console.log("i : " + i + ", editYn : " + editYn);
	   	  	if(editYn == "Y"){
	   	  		sheet1.SetRowEditable(i, 1);
	   	  	} else {
	   	  		sheet1.SetRowEditable(i, 0);
	   	  	}
	   	  }
       } catch (ex) {
			alert(ex);
       }
	}
	// 저장 후 메시지
	function sheet1_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			if (Msg != "") {
				alert(Msg);
			}
			doAction1("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		console.log("sheet1_OnSelectCell !!");
		if(OldRow != NewRow){
			if(sheet1.GetCellValue(NewRow, "editYn") == "Y"){
				sheet2.SetEditable(1);
				$("#sheet2Btn").show();
			} else {
				sheet2.SetEditable(0);
				$("#sheet2Btn").hide();
			}
			doAction2('Search');
		}
	}
	
	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
		} catch (ex) {
			alert("OnSearchEnd Event Error " + ex);
		}
	}

	// 저장 후 메시지
	function sheet2_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			if (Msg != "") {
				alert(Msg);
			}
			doAction2("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
</script>