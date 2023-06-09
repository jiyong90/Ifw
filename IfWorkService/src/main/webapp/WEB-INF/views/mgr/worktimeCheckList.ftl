<div id="worktimeCheckList">
	<!-- 근무 상세보기 modal start -->
	<div class="modal fade" id="worktimeDetailModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-xl-more" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">근무 상세</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
						<tr>
							<td>
								<div class="inner">
									<div class="sheet_title_wrap clearfix">
										<div id="popupTitle" class="float-left title"></div>
										<ul class="float-right btn-wrap">
											<li><a href="javascript:doAction2('Down2Excel')" class="basic authR">다운로드</a></li>
										</ul>
									</div>
								</div>
								<script type="text/javascript"> createIBSheet("sheet2", "100%", sheetH90, "kr"); </script>
							</td>
						</tr>
					</table>
					
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 결재의견 modal end -->
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
									<span class="label">근무 기준 </span>
									<select id="searchType" name="searchType" class="box">
									</select>
								</td>
								<td>
									<span class="label">기준일 </span>
									<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
								</td>
								<td>
									<span class="label">근무시간 </span>
									<select id="searchValue" name="searchValue" class="box">
										<option value="ALL">합산</option>
										<option value="BASE">기본</option>
										<option value="OT">연장</option>
									</select>
									<input type="number" id="searchMinute" name="searchMinute" />
									<select id="searchCondition" name="searchCondition" class="box">
										<option value=""></option>
										<option value="more">이상</option>
										<option value="under">미만</option>
									</select>
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
								<div class="float-left title">근무시간 초과자 조회 &nbsp;<span id="Tooltip-worktimeCheckList" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
								<div class="legend-wrap">
									<ul class="legend-list">
										<li><span class="flag excess"></span><span>초과</span></li>
										<li><span class="flag under"></span><span>미만</span></li>
									</ul>
								</div>
								<ul class="float-right btn-wrap">
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
   	
	    $('#sYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
	    $("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    
	  	//근무기준
		var workSearchTypeCd = convCode(codeList("${rc.getContextPath()}/code/list", "WORK_SEARCH_TYPE_CD"), ""); 
        $("#searchType").append(workSearchTypeCd[2]);
		
		new jBox('Tooltip', {
       	    attach: '#Tooltip-worktimeCheckList',
       	    target: '#Tooltip-worktimeCheckList',
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
       	    content: '근무시간을 조회합니다.'
       	    		+ '<br>● 근무기준 주별인 경우 주간근무기준(기본근무,근무조,시차근무,탄력근무) 대상자가 조회됩니다.'
       	    		+ '<br>● 근무기준 기간별인 경우 기간근무기준(선택근무) 대상자가 조회됩니다.'
       	    		+ '<br>● 기준시간은 제도별 기간의 근무 가능 기준시간입니다.'
       	    		+ '<br>● 인정시간은 제도별 기간의 인정근무시간입니다.'
       	    		+ '<br>● 근무기준 기간의 근무상세내역은 근무상세 팝업을 클릭하여 조회합니다.'
       	    		+ '<br>● 근무시간 조건은 기준선택시 해당기준 대상자만 조회됩니다.'
       	    		,
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22,FrozenCol:0,DataRowMerge:0};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No|No",		Type:"Seq",			Hidden:0,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제|삭제",		Type:"DelCheck",	Hidden:1,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태|상태",		Type:"Status",		Hidden:1,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"소속|소속",		Type:"Text",		Hidden:0,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번|사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도|근무제도",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workTypeNm",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무기간|시작일",	Type:"Date",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"flexibleSdate",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무기간|종료일",	Type:"Date",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"flexibleEdate",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무기간|시작일",	Type:"Date",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"weekSdate",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무기간|종료일",	Type:"Date",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"weekEdate",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|기본",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"stdWorkMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|연장",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"stdOtMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|합산",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"stdMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|기본",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"apprWorkMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|연장",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"apprOtMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|합산",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"apprMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"차이시간(분)|차이시간(분)",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"diffMinute",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무상세|근무상세",	Type:"Image",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"worktimeDetail",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:1 },
			{Header:"비고|비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:01,	EditLen:100 }
			
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		sheet1.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22,FrozenCol:0,DataRowMerge:0};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No|No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제|삭제",			Type:"DelCheck",	Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태|상태",			Type:"Status",		Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"근무일|근무일",	Type:"Date",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"ymd",			KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			//{Header:"근무제도|근무제도",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:1,	SaveName:"flexibleNm",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근시각|출근시각",	Type:"Text",	 	Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"entrySdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"퇴근시각|퇴근시각",	Type:"Text",	 	Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"entryEdate",	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"시간구분|시간구분",		Type:"Combo",		Hidden:0,	Width:70,	Align:"Left",	ColMerge:0,	SaveName:"timeTypeCd",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근태|근태",  	Type:"Combo",     	Hidden:0,   Width:70,  	Align:"Center",  ColMerge:0, SaveName:"taaCd",   	 KeyField:0,    Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:100  },
            {Header:"계획|시작시각",		Type:"Text",	 	Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"planSdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획|종료시각",		Type:"Text",	 	Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"planEdate",	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획|근무시간",		Type:"Text",	      	Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"planMinute",	 KeyField:0,	Format:"", 		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정|시작시각",		Type:"Text",	    Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"apprSdate", 	 KeyField:0,	Format:"YmdHm",	 	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정|종료시각",		Type:"Text",	    Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"apprEdate", 	 KeyField:0,	Format:"YmdHm",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정|근무시간",		Type:"Text",	      	Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"apprMinute",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"비고|비고",			Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];


        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(false);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);

		//시간구분
		var timeTypeCdList = convCode(codeList("${rc.getContextPath()}/code/list", "TIME_TYPE_CD"), "선택"); 
        sheet2.SetColProperty("timeTypeCd", {ComboText:"|"+timeTypeCdList[0], ComboCode:"|"+timeTypeCdList[1]} );
        
		//근태코드
		var taaCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/taaCode/list", "",false).DATA, "");
		sheet2.SetColProperty("taaCd", {ComboText:"|"+taaCdList[0], ComboCode:"|"+taaCdList[1]} );
		
		
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/worktime/check/list" , $("#sheetForm").serialize());
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
			var param = "sYmd="+sheet1.GetCellValue( sheet1.GetSelectRow(), "weekSdate") + "&eYmd="+sheet1.GetCellValue( sheet1.GetSelectRow(), "weekEdate") + "&sabun="+sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun");
			sheet2.DoSearch( "${rc.getContextPath()}/worktime/detail" , param);
			break;
		case "Down2Excel":
			var downcol = makeHiddenSkipCol(sheet2);
			var param = {DownCols:downcol, SheetDesign:1, Merge:1};
			sheet2.Down2Excel(param); 
			break;
		}
   	}

 	// 조회 전
	function sheet1_OnBeforeSearch(Code, Msg, StCode, StMsg) {
		var searchType = $("#searchType").val();
 		if(searchType == 'WEEK') {
 			sheet1.SetColHidden("flexibleSdate", 1);
 			sheet1.SetColHidden("flexibleEdate", 1);
 			sheet1.SetColHidden("weekSdate", 0);
 			sheet1.SetColHidden("weekEdate", 0);
 		} else if(searchType == 'TERM') {
 			sheet1.SetColHidden("weekSdate", 1);
 			sheet1.SetColHidden("weekEdate", 1);
 			sheet1.SetColHidden("flexibleSdate", 0);
 			sheet1.SetColHidden("flexibleEdate", 0);
 		}
	}
   	
	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
			var row = sheet1.LastRow();
			
			if($("#searchCondition").val()!='') {
				for(var i=2; i<=row; i++){
					var diffMinute = sheet1.GetCellValue(i,"diffMinute");
					
					if (diffMinute > 0){
						sheet1.SetRowBackColor(i,"#da9694");					
					} else if (diffMinute < 0){
						sheet1.SetRowBackColor(i,"#fabf8f");
					} 
				}
			}
			
		} catch (ex) {
			alert("OnSearchEnd Event Error " + ex);
		}
	}
	
	//셀 클릭시 이벤트
	function sheet1_OnClick(Row, Col, Value) {
		try{
			if(Row > 0 && sheet1.ColSaveName(Col) == "worktimeDetail" ){
				var empNm = sheet1.GetCellValue( sheet1.GetSelectRow(), "empNm");
				var weekSdate = sheet1.GetCellValue( sheet1.GetSelectRow(), "weekSdate");
				var weekEdate = sheet1.GetCellValue( sheet1.GetSelectRow(), "weekEdate");
				
				$("#popupTitle").text(empNm + " " + formatDate(weekSdate,'-') + "~" + formatDate(weekEdate,'-'));
				
				sheet2.RemoveAll();
				doAction2('Search');
				$("#worktimeDetailModal").modal("show");
			}
		}catch(ex){
			alert("OnClick Event Error : " + ex);
		}
	}
</script>