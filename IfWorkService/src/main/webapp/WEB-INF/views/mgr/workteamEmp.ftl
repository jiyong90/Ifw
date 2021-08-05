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
							<span class="label">기준일 </span>
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
								<div class="float-left title">근무조대상자관리 &nbsp;<span id="Tooltip-workTeamEmp" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
<#--									<li><a href="javascript:doAction1('Upload')" class="basic authA">업로드</a></li>-->
<#--									<li><a href="javascript:doAction1('Down2Excel')" class="basic authA">다운로드</a></li>-->
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

   		$("#sYmd, #eYmd").off("change.datetimepicker").on("change.datetimepicker", function(e) {
	    	var sYmd = $("#sYmd").val();
	    	var eYmd = $("#eYmd").val();
	    	if(sYmd != '' && eYmd != '') {
		    	if(eYmd < sYmd) {
					swtAlert("시작일보다 크거나 같아야 합니다.");
					$("#eYmd").val($("#sYmd").val());
					return;
				}
		    }
		});
        
        new jBox('Tooltip', {
       	    attach: '#Tooltip-workTeamEmp',
       	    target: '#Tooltip-workTeamEmp',
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
       	    content: '근무조를 변경할 경우 종료일을 조정하여 저장한 후 새로 입력합니다. 종료일을 과거로 저장할 경우 인정시간은 삭제되니 주의해주세요.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22, FrozenCol:0, DataRowMerge:0};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No|No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제|삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태|상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id|id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workteamEmpId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사번|사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"부서명|부서명",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"직급|직급",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"classCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			//{Header:"사원구분|사원구분",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empType",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무조|근무조",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workteamMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"적용기간|시작일자",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:1,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"적용기간|종료일자",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:1,	Format:"Ymd",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고|비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"상태|상태",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"STATUS",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
		];
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		//근무조
		var workteamCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/workteamMgr/workteamCd", "",false).DATA, "");
		sheet1.SetColProperty("workteamMgrId", {ComboText:"|"+workteamCdList[0], ComboCode:"|"+workteamCdList[1]} );

		//부서명
		var orgCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/orgCode/comboList", "",false).DATA, "");
		sheet1.SetColProperty("orgCd", {ComboText:"|"+orgCdList[0], ComboCode:"|"+orgCdList[1]} );

		//직급
		var classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "CLASS_CD"), "");
		sheet1.SetColProperty("classCd", {ComboText:"|"+classCdList[0], ComboCode:"|"+classCdList[1]} );
		
		//이름
        setSheetAutocompleteEmp( "sheet1", "empNm" );
        
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/workteam/list" , $("#sheetForm").serialize());
			break;
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|sabun|workteamMgrId|symd|eymd", false, true)){break;}
			
        	for(var i=1;i<sheet1.RowCount(); i++){
        		if(sheet1.GetCellValue(i, "symd") > sheet1.GetCellValue(i, "eymd")) {
        			swtAlert("종료일은 시작일보다 늦어야 합니다.");
        			return;
        		}
        	}
        	
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/workteam/save", $("#sheetForm").serialize()); break;
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
	   	    for(i=1;i<sheet1.RowCount()+2;i++){
//	   	    	console.log(sheet1.GetCellValue(i, "sabun"));
	   	  		sheet1.SetCellEditable(i, 1,0);
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
	
	
	function getReturnValue(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
   		sheet1.SetCellValue(gPRow, "sabun",returnValue.sabun);
		sheet1.SetCellValue(gPRow, "empNm",returnValue.empNm);
        sheet1.SetCellValue(gPRow, "orgCd",returnValue.orgCd);
        sheet1.SetCellValue(gPRow, "classCd",returnValue.classCd);
	}


	/**
	 * 엑셀에 로드 된 데이터 검증
	 * @param result
	 * @param code
	 * @param msg
	 */
	function sheet3_OnLoadExcel(result, code, msg) {
		console.log(result, code, msg);
		var totalCnt = sheet1.RowCount();
		var newRowCnt = sheet1.RowCount("I");
		var newRowNum = totalCnt - newRowCnt + 1;

		var _Row = sheet1.GetSelectionRows();

	}
</script>