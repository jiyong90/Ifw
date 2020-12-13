<div id="otApplList">
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
								<div class="float-left title">연장/휴일근무 신청내역조회 </div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Save')" class="basic">저장</a></li>
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
	    
	    new jBox('Tooltip', {
       	    attach: '#Tooltip-otApplList',
       	    target: '#Tooltip-otApplList',
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
       	    content: '근무일별 개인 연장근무 신청 내역을 조회합니다.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
   		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msPrevColumnMerge,Page:100};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"결재번호",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:1,	SaveName:"applId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결재상태",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:1,	SaveName:"applStatusCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무구분",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무일",		Type:"Text",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ymd",				KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작시각",	Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"otSdate",			KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료시각",	Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"otEdate",			KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"신청시간(분)",Type:"Text",		Hidden:0,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"otMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사유코드",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"reasonCd",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사유",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"reason",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"대체휴일시작시각",	Type:"Text",Hidden:1,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"subsSdate",	    KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"대체휴일종료시각",	Type:"Text",Hidden:1,	Width:120,	Align:"Center",	ColMerge:0,	SaveName:"subsEdate",		KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		<#if subsYn?? && subsYn!='' && subsYn?exists >
			if('${subsYn}'=='Y') {
				sheet1.SetColHidden("subsSdate", 0);
				sheet1.SetColHidden("subsEdate", 0);
			}
		</#if>
		
		//사업장
		var reasonCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "REASON_CD"), "");
		sheet1.SetColProperty("reasonCd", {ComboText:reasonCdList[0], ComboCode:reasonCdList[1]} );
		var applStatusCd = stfConvCode(codeList("${rc.getContextPath()}/code/list", "APPL_STATUS_CD"), "");
		sheet1.SetColProperty("applStatusCd", {ComboText:applStatusCd[0], ComboCode:applStatusCd[1]} );

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/applList/otList" , $("#sheetForm").serialize());
			break;
		case "Down2Excel":
			var downcol = makeHiddenSkipCol(sheet1);
			var param = {DownCols:downcol, SheetDesign:1, Merge:1};
			sheet1.Down2Excel(param); 
			break;
		case "Save":
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave( "${rc.getContextPath()}/otAppl/saveApplSts", $("#sheetForm").serialize());
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
		   	  	var applStatusCd = sheet1.GetCellValue(i, "applStatusCd");
		   	  	//결재상태가 '결재처리중' 또는 '처리완료' 상태일때만 변경 가능하도록
		   	  	if(applStatusCd == "21"){  
		   	  		sheet1.SetCellEditable(i, "applStatusCd", true);
		   	  		var info = {"ComboText":"결재처리중|결재반려|처리완료","ComboCode":"21|22|99"}; 
		   	  		sheet1.CellComboItem(i, "applStatusCd",info);
		   	  	}else if(applStatusCd == "99"){  
		   	  		sheet1.SetCellEditable(i, "applStatusCd", true);
		   	  		var info = {"ComboText":"처리완료|취소처리완료","ComboCode":"99|44"}; 
		   	  		sheet1.CellComboItem(i, "applStatusCd",info);
		   	  	} else {
		   	  		sheet1.SetCellEditable(i, "applStatusCd", false);
		   	  	}
		   	}
			sheet1.RenderSheet(2);
			
		} catch (ex) {
			alert("OnSearchEnd Event Error " + ex);
		}
	}
	
	// 셀에서 키보드가 눌렀을때 발생하는 이벤트
	function sheet1_OnChange(Row, Col, Value) { 
		if(sheet1.ColSaveName(Col) == "applStatusCd"){
			var org_applStatusCd = sheet1.CellSearchValue(Row, Col);
			var applStatusCd = sheet1.GetCellValue(Row, "applStatusCd");
			
			if(org_applStatusCd == "21") { //결재처리중 > 1.결재반려 2.결재완료
				if(applStatusCd != "21" && applStatusCd != "22" && applStatusCd != "99") {
					alert("변경할 수 없는 상태 값("+applStatusCd+") 입니다.");
					sheet1.SelectCell(Row, "applStatusCd");
					//조회시 최초의 값으로 변경
					sheet1.SetCellValue(Row, "applStatusCd", org_applStatusCd);
					return applStatusCd;
				}
			} else if(org_applStatusCd == "99") { //결재완료 > 1.취소처리완료 
				if(applStatusCd != "99" && applStatusCd != "44") {
					alert("변경할 수 없는 상태 값("+applStatusCd+") 입니다.");
					sheet1.SelectCell(Row, "applStatusCd");
					//조회시 최초의 값으로 변경
					sheet1.SetCellValue(Row, "applStatusCd", org_applStatusCd);
					return applStatusCd;
				}
			}
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
		 
</script>