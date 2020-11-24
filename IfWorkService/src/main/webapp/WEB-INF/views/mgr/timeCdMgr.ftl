<div id="timeCdMgr">
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
							<div class="float-left title">근무유형관리 &nbsp;<span id="Tooltip-timeCdMgrList" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet1", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
			<tr id="trBreakMgr" style="display:none;">
				<td>
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
						<div class="float-left title">휴식시간관리 &nbsp;<span id="Tooltip-timeCdMgrBreakList" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet2", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
			<tr id="trBreakTime" style="display:none;">
				<td>
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
						<div class="float-left title">휴식시간관리</div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction3('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction3('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet3", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
		</table>
	</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
		$('#sYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
		
		new jBox('Tooltip', {
       	    attach: '#Tooltip-timeCdMgrList',
       	    target: '#Tooltip-timeCdMgrList',
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
       	    content: '근무시간표(또는 근무코드)는 근무 시간을 일괄로 통제하기 위해 미리 정의된 근무 시간 계획을 관리합니다.'
       	    		+ '<br>● 휴일여부 체크시 근무시간표의 휴일로 정의됩니다.'
       	    		+ '<br>● 휴일여부 미체크시 근무시간표의 평일(근무일)로 정의됩니다. 평일인 경우 공휴시 근무코드를 필수로 선택합니다.'
       	    		+ '<br>● 근무제도 적용 이후 근무시간표 변경은 자동반영이 불가능합니다.'
       	    		,
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
		
		new jBox('Tooltip', {
       	    attach: '#Tooltip-timeCdMgrBreakList',
       	    target: '#Tooltip-timeCdMgrBreakList',
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
       	    content: '근무유형에 따른 휴식시간을 관리합니다.'
   	    		   + '<br>● 휴게시간차감기준이 휴식시간범위 인경우 휴식시간 범위를 등록하고 근무시간내 휴식시각이 표함된 경우 차감되어 근무시간계산됩니다.'
   	    		   + '<br>● 휴게시간차감기준이 기준시간 초과시 휴식시간만큼 차감됩니다.'
   	    		   + '<br> 단, 기준시간 < 근무시간 > 기준시간+휴식시간 인 근무시간은 기준시간으로 인정됩니다.',
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
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무유형코드",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCd",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무유형명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeNm",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",        Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"symd",         	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"종료일",			Type:"Date",        Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"eymd",         	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"휴게시간\n차감기준",		Type:"Combo",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"breakTypeCd",    KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"휴일여부",		Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"holYn",		    KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"공휴시\n근무코드",		Type:"Combo",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"holTimeCdMgrId",		    KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"기본근무\n시작시각",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"workShm",			KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"기본근무\n종료시각",	Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"workEhm",			KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"지각체크\n여부",		Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"lateChkYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"조퇴체크\n여부",		Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"leaveChkYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"결근체크\n여부",		Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"absenceChkYn",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetUseDefaultTime(0);
		sheet1.SetCountPosition(8);
		
		var timeCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=Y",false).DATA, "");
		sheet1.SetColProperty("holTimeCdMgrId", 	{ComboText:"|"+timeCdList[0], ComboCode:"|"+timeCdList[1]} );
		
		var holBreakTypeList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BREAK_TYPE_CD"), "선택");
		sheet1.SetColProperty("breakTypeCd", 	{ComboText:"|"+holBreakTypeList[0], ComboCode:"|"+holBreakTypeList[1]} );
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeBreakMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"휴식코드",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"breakTimeCd",  KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
            {Header:"시작시각",		Type:"Text",	  Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"shm", 	 KeyField:1,	Format:"Hm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"종료시각",		Type:"Text",	  Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"ehm",	 KeyField:1,	Format:"Hm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"순서",			Type:"Int",	      Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"seq",	 KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",			Type:"Text",	  Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUseDefaultTime(0);
		sheet2.SetUnicodeByte(3);
        
		//휴식코드
		var breakCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BREAK_TIME_CD"), "선택");
		sheet2.SetColProperty("breakTimeCd", {ComboText:breakCdList[0], ComboCode:breakCdList[1]} );
		
		var initdata3 = {};
		initdata3.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata3.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata3.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeBreakTimeId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"기준시간(분)",	Type:"Int",			Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workMinute",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"휴식시간(분)",  Type:"Int",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"breakMinute",  KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
			{Header:"비고",			Type:"Text",	  Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];
        IBS_InitSheet(sheet3, initdata3);
		sheet3.SetEditable(true);
		sheet3.SetVisible(true);
		sheet3.SetUseDefaultTime(0);
		sheet3.SetUnicodeByte(3);
        
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/timeCdMgr/list" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|timeCd|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/timeCdMgr/save", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			sheet1.DataInsert(0);
			break;
		}
	}
	
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "timeCdMgrId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId");
			sheet2.DoSearch( "${rc.getContextPath()}/timeCdMgr/breakList" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/timeCdMgr/breakSave", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var timeCdMgrId = sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId");
			if(timeCdMgrId == ""){
				alert("근무유형 저장 후 휴식시간을 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(0) ;
				sheet2.SetCellValue(row, "timeCdMgrId" , timeCdMgrId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		}
	}
	
	function doAction3(sAction) {
		switch (sAction) {
		case "Search":
			var param = "timeCdMgrId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId");
			sheet3.DoSearch( "${rc.getContextPath()}/timeCdMgr/breakTimeList" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet3);
			sheet3.DoSave("${rc.getContextPath()}/timeCdMgr/breakTimeSave", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var timeCdMgrId = sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId");
			if(timeCdMgrId == ""){
				alert("근무유형 저장 후 휴식시간을 입력하셔야 합니다");
			} else {
				var row = sheet3.DataInsert(0) ;
				sheet3.SetCellValue(row, "timeCdMgrId" , timeCdMgrId);
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
			if (Msg != "") {
				alert(Msg);
			}
			if(sheet1.SearchRows() > 0) {
				// 하단 화면을 보여줘야함.
				var rowCnt = sheet1.LastRow();
				for (var i=1; i<=rowCnt; i++) {
					if (sheet1.GetCellValue(i, "holYn") == "Y" ) {
						sheet1.SetCellEditable(i, "holTimeCdMgrId", false);
					}
				}
			}
			sheet2.RemoveAll();
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
			// 휴일코드가 추가되면 재갱신이 필요함
			var timeCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=Y",false).DATA, "");
			sheet1.SetColProperty("holTimeCdMgrId", 	{ComboText:"|"+timeCdList[0], ComboCode:"|"+timeCdList[1]} );
			doAction1("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	// 값 변경시 발생
	function sheet1_OnChange(Row, Col, Value) {
		// 공휴일여부
		if ( sheet1.ColSaveName(Col) == "holYn"){
			if (Value == "Y"){
				sheet1.SetCellValue(Row, "holTimeCdMgrId", "");
				sheet1.SetCellEditable(Row, "holTimeCdMgrId", false);
			} else {
				sheet1.SetCellEditable(Row, "holTimeCdMgrId", true);
			}
		}	
	}
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(OldRow != NewRow){
			sheet2.RemoveAll();
			sheet3.RemoveAll();
			var breakTypeCd = sheet1.GetCellValue(NewRow,"breakTypeCd");
			setBerakSheet(breakTypeCd);
		}
	}
	
	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
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
	function sheet2_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			doAction2("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	// 조회 후 에러 메시지
	function sheet3_OnSearchEnd(Code, Msg, StCode, StMsg) {
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
	function sheet3_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			doAction3("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}

	function setBerakSheet(breakTypeCd){
		if(breakTypeCd != ''){
			if(breakTypeCd == "MGR"){
				$("#trBreakTime").hide();
				$("#trBreakMgr").show();
				doAction2("Search");
			} else {
				$("#trBreakMgr").hide();
				$("#trBreakTime").show();
				doAction3("Search");
			}
		}
	}
</script>