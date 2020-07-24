<div id="workTimeCloseMgr">
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
						<span class="label">마감년도 </span>
						<input type="text" id="sYY" name="sYY" class="date2 required" value="${today?date("yyyy")?string("yyyy")}" data-toggle="datetimepicker" data-target="#sYY" placeholder="연도" autocomplete="off"/>									
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
						<div class="float-left title">마감기준관리 &nbsp;<span id="Tooltip-worktimeClose" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
								
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
						<div class="float-left title">마감대상자조회 &nbsp;<span id="Tooltip-worktimeCloseEmp" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>							
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet2", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
		</table>
	</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
   		$('#sYY').datetimepicker({
            format: 'YYYY',
            language: 'ko'
        });
        
		new jBox('Tooltip', {
       	    attach: '#Tooltip-worktimeClose',
       	    target: '#Tooltip-worktimeClose',
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
       	    content: '근무내용을 마감 생성합니다.'
       	    	   + '<br>● 마감기준명, 시작일, 종료일은 입력 후 수정이 불가능합니다.'
       	    	   + '<br>● 마감 기준 저장 후 근무마감여부의 [마감] 버튼을 클릭합니다. 마감시 시작일~종료일 사이의 근무정보가 있는 직원의 근무시간을 합산합니다.'
       	    	   + '<br>● 근무마감여부 "완료"된 마감기준은 삭제가 불가능합니다.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
   		
   		new jBox('Tooltip', {
       	    attach: '#Tooltip-worktimeCloseEmp',
       	    target: '#Tooltip-worktimeCloseEmp',
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
       	    content: '마감대상자를 조회합니다.'
    	    	   + '<br>● 마감기준관리의 [마감] 버튼을 1회 이상 클릭 후 조회됩니다.'
    	    	   + '<br>● 근무시간이 변동된 경우 대상자별 개별 재마감이 가능합니다.'
    	    	   + '<br>● 근무마감이 완료된 경우, 대상자별 마감을 할 수 없습니다.'
    	    	   ,
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
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"worktimeCloseId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"마감기준명",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"closeNm",	        KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",		Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"종료일",		Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },			
			{Header:"근무마감여부",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"closeYn",		    KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무마감여부",	Type:"Html", 		Hidden:0,  	Width:60,  	Align:"Center", ColMerge:0, SaveName:"endImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:100 },
			//{Header:"확정상태",	Type:"Int", 		Hidden:1,  	Width:60,  	Align:"Center", ColMerge:0, SaveName:"cnt",  			KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:1   },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }					
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(8);
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			//{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },			
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"worktimeCloseId",	KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		    Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		    Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		    Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"개별재생성",		Type:"Html",		Hidden:0,	Width:60,	Align:"Center",	ColMerge:0,	SaveName:"endImg",			KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }			
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
			sheet1.DoSearch( "${rc.getContextPath()}/worktimeClose/closeList" , $("#sheetForm").serialize());
			break;	
		case "Save":
			if(!dupChk(sheet1,"closeNm|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/worktimeClose/save", $("#sheetForm").serialize()); break;

			break;	
		case "Insert":
			sheet1.DataInsert(0) ;
			break;
		case "Down2Excel":
			var downcol = makeHiddenSkipCol(sheet1);
			var param = {DownCols:downcol, SheetDesign:1, Merge:1};
			sheet1.Down2Excel(param); 
			break;
		}
	}

	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "worktimeCloseId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "worktimeCloseId");
			sheet2.DoSearch( "${rc.getContextPath()}/worktimeClose/closeEmpList" , param);
			break;
		
		}
	}
	
	function setEndConfirm(worktimeCloseId, sabun){		
		// var row = sheet1.FindText("worktimeCloseId", worktimeCloseId, 0);
		var worktimeCloseId2 = worktimeCloseId + "";
		var row = sheet1.FindText("worktimeCloseId", worktimeCloseId2, 0);
		
		var sYmd = sheet1.GetCellValue(row, "symd");
		var eYmd = sheet1.GetCellValue(row, "eymd");
		
		if(!confirm("마감하시겠습니까?")) {
			return;
		}
		$("#loading").show();
		var param = {
				worktimeCloseId: worktimeCloseId
				, sYmd: sYmd
				, eYmd: eYmd
				, sabun: sabun
			};
		console.log(param);
		$.ajax({
			url: "${rc.getContextPath()}/worktimeClose/workTimeCloseIf",
			type: "POST",
			contentType: 'application/json',
			data: JSON.stringify(param),
			dataType: "json",
			success: function(data) {
				$("#loading").hide();
				console.log(data);
				if(data!=null) {
					if(data.message != '')
						alert(data.message);
					else 
						alert(sheet1.GetCellValue(row, "closeNm") + " 근무마감 완료되었습니다.");
					
					doAction1("Search");
				}
			},
			error: function(e) {
				$("#loading").hide();
				alert("마감할 내용이 없습니다.");
				console.log(e);
			}
		});
	}
		
	
	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			
			} else {
				for( i=1; i<=sheet1.RowCount(); i++ ) {
			   	  	// 확정완료는 수정불가
					var closeYn = sheet1.GetCellValue(i, "closeYn");
			   	  	if(closeYn == "Y") {
			   	  		sheet1.SetRowEditable(i, 0);
			   	  	} else {
			   	  		sheet1.SetRowEditable(i, 1);				   		
			   	  	}
			   	}
			}
			
		} catch (ex) {
			alert("OnSearchEnd Event Error " + ex);
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

	// 저장 후 메시지
	function sheet2_OnSaveEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
			doAction1("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(OldRow != NewRow){
			sheet2.RemoveAll();
			doAction2('Search');
			
			//20200228 추가
			var workCloseYn = sheet1.GetCellValue( NewRow, "closeYn");
			if( workCloseYn == "Y" ){
				$("#optionBtn").hide();
				sheet2.SetColHidden("endImg", 1);
			} else {
				$("#optionBtn").show();
				sheet2.SetColHidden("endImg", 0);
			}
		}					
	}
	
</script>