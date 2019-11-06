<div id="flexibleStdMgr">
 	<div class="container-fluid pt-3 pb-3 bg-white">
 	<div class="ibsheet-wrapper">
		<form id="sheetForm" name="sheetForm">
			<div class="sheet_search outer">
				<div>
					<table>
						<tr>
							<td>
								<span class="label">기준일 </span>
								<input type="text" id="sYmd" name="sYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
							</td>
							<td>
								<a href="javascript:doAction1('Search');" class="button">조회</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<div class="row no-gutters">
			<div class="col-5 pr-3">
				<div class="inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">근무제도관리</div>
						<ul class="float-right btn-wrap">
							<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
							<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
						</ul>
					</div>
				</div>
				<script type="text/javascript">createIBSheet("sheet1", "100%", fullsheetH,"kr"); </script>
			</div>
			<div class="col-7 pt-2">
				<div class="innertab inner">
					<div id="tabs" class="tab">
						<ul class="outer tab_bottom">
							<li><a href="#tabs-1">근무제기준</a></li>
							<li><a href="#tabs-2">근무제패턴</a></li>
						</ul>
						<div id="tabs-1">
							<div  class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<div class="float-left title" id="searchAppText">근무제기준</div>
									<ul class="float-right btn-wrap" id="optionBtn">
										<li><a href="javascript:doAction1('Save2')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<div id="view_sele">
									<table class="default">
										<tbody>
											<tr id="trHoliday">
												<th>공휴일제외여부</th>
												<td>
													<input type="checkbox" id="holExceptYn" name="holExceptYn" /> 체크시 공휴일 근무제외
												</td>
												<th>인정근무 단위시간(분)</th>
												<td>
													<input type="text" id="unitMinute" name="unitMinute"/>
													<input type="hidden" id="taaTimeYn" name="taaTimeYn"/>
												</td>
											</tr>
											<tr id="trDayType">
												<th>출근자동처리기준</th>
												<td>
													<select id="dayOpenType"></select>
													
												</td>
												<th>출근퇴근처리기준</th>
												<td>
													<select id="dayCloseType"></select>
												</td>
											</tr>
											<tr id="trBaseCheck">
												<th>고정OT소진 사용여부</th>
												<td colspan="3">
													<input type="checkbox" id="defaultWorkUseYn" name="defaultWorkUseYn" /> 체크시 고정OT 소진기준작성 
												</td>
											</tr>
											<tr id="trFixOt">
												<th>고정OT 소진방법</th>
												<td>
													<select id="fixotUseType">
														<option value="">사용안함</option>
					                                    <option value="DAY">일별 소진</option>
					                                    <option value="ALL">일괄 소진</option>
					                                </select>
												</td>
												<th>고정OT 소진한계시간(분)</th>
												<td>
													<input type="text" id="fixotUseLimit" name="fixotUseLimit"/>
												</td>
											</tr>
											<tr id="trRega">
												<th>간주근무시간</th>
												<td colspan="3">
													<select id="regardTimeCdId">
					                                </select>
												</td>
											</tr>
											<tr id="trWorkTime">
												<th>근무가능시각</th>
												<td>
													<input type="text" id="workShm" name="workShm" class="date2" data-toggle="datetimepicker" />
													~
													<input type="text" id="workEhm" name="workEhm" class="date2" data-toggle="datetimepicker" />
												</td>
												<th>근태일 근무가능여부</th>
												<td>
													<input type="checkbox" id="taaWorkYn" name="taaWorkYn" /> 체크시 근태일 근무가능
												</td>
											</tr>
											<tr id="trCoreChk">
												<th>코어시간체크여부</th>
												<td colspan="3">
													<input type="checkbox" id="coreChkYn" name="coreChkYn" /> 체크시 코어시간 기준작성
												</td>
											</tr>
											<tr id="trCoreTime">
												<th>코어근무시각</th>
												<td colspan="3">
													<input type="text" id="coreShm" name="coreShm" class="date2" data-toggle="datetimepicker"/>
													~
													<input type="text" id="coreEhm" name="coreEhm" class="date2" data-toggle="datetimepicker"/>
												</td>
											</tr>
											<tr id="trBaseFirst">
												<th>기본근무선소진여부</th>
												<td colspan="3">
													<select id="exhaustionYn">
					                                    <option value="Y">사용</option>
					                                    <option value="N">미사용</option>
					                                </select>
												</td>
											</tr>
											<tr id="trUnplan">
												<th>계획없이 타각가능여부</th>
												<td>
													<input type="checkbox" id="unplannedYn" name="unplannedYn" /> 계획이 없는날 타각수정신청 가능
												</td>
											</tr>
											<tr id="trUsedTerm">
												<th>사용기간지지정 <span id="Tooltip-7" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="1_week" title="1주"/> 1주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="2_week" title="2주"/> 2주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="3_week" title="3주"/> 3주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="4_week" title="4주"/> 4주
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="1_month" title="1개월"/> 1개월
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="2_month" title="2개월"/> 2개월
													<input type="checkbox" id="usedTermOpt" name="usedTermOpt" value="3_month" title="3개월"/> 3개월
												</td>
											</tr>
											<tr id="trApplTerm">
												<th>신청기간지정</th>
												<td colspan="3">
													<input type="radio" id="applTermOpt" name="applTermOpt" value="today"  title="당일 이내"/> 당일 이내
													<input type="radio" id="applTermOpt" name="applTermOpt" value="1_week" title="1주일 이내"/> 1주일 이내
													<input type="radio" id="applTermOpt" name="applTermOpt" value="2_week" title="2주일 이내"/> 2주일 이내
													<input type="radio" id="applTermOpt" name="applTermOpt" value="3_week" title="3주일 이내"/> 3주일 이내
													<input type="radio" id="applTermOpt" name="applTermOpt" value="4_week" title="4주일 이내"/> 4주일 이내
												</td>
											</tr>
											<tr>
												<th>비고</th>
												<td colspan="3">
													<textarea id="note" cols="50" rows="3">
													</textarea>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div id="tabs-2">
							<div  class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<div class="float-left title" id="pattText">반복패턴</div>
									<ul class="float-right btn-wrap" id="pattBtn">
										<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
										<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<script type="text/javascript">createIBSheet("sheet2", "100%", sheetH90,"kr"); </script>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		</from>
	</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
   		
   	 new jBox('Tooltip', {
   	    attach: '#Tooltip-7',
   	    target: '#Tooltip-7',
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
   	    content: '사용자 기간지정은 개인 당 사전에 계획된 근무 기간을 등록하는 것을 뜻합니다.',
   	    onOpen: function () {
   	      this.source.addClass('active');
   	    },
   	    onClose: function () {
   	      this.source.removeClass('active');
   	    }
   	  });
   	 
   		//resize
		$(window).smartresize(sheetResize);
   	
   		$('#sYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
        
        $('#workShm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#workEhm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        $('#coreShm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#coreEhm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",				Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",			Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",			Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제도유형",  	Type:"Combo",   Hidden:0,   Width:70,   Align:"Center", ColMerge:0, SaveName:"workTypeCd",  	KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
			{Header:"근무명칭",			Type:"Text",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"flexibleNm",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",    Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"useSymd",        	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"종료일",			Type:"Date",    Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"useEymd",        	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:100 },
			{Header:"공휴일제외여부",	Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"holExceptYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"인정근무 단위시간",	Type:"Int",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"unitMinute", 		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:5 },			
			{Header:"고정OT 소진여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"defaultWorkUseYn", KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"고정OT 소진방법",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"fixotUseType",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"고정OT 한계시간",	Type:"Int",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"fixotUseLimit",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:5 },			
			{Header:"간주근무시간",		Type:"Int",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"regardTimeCdId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:20 },
			{Header:"근무가능시작",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"workShm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"근무가능종료",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"workEhm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"코어시간체크여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"coreChkYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"코어근무시작",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"coreShm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"코어근무종료",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"coreEhm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"선소진여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"exhaustionYn",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"사용기간지정",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"usedTermOpt",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"신청기간지정",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applTermOpt",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"신청기간지정",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applTermOpt",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"근태시간포함여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"taaTimeYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"근태일근무여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"taaWorkYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"출근자동처리",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"dayOpenType",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"퇴근자동처리",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"dayCloseType",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"기준요일",			Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"baseDay",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"계획없음여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"unplannedYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"비고",				Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		//근무제도코드
		var workTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "WORK_TYPE_CD"), "선택");
		sheet1.SetColProperty("workTypeCd", {ComboText:workTypeCdList[0], ComboCode:workTypeCdList[1]} );
		
		//근무시간
		var regardTimeCdId = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=N",false).DATA, "선택");
		$("#regardTimeCdId").html(regardTimeCdId[2]);
		
		//출퇴근자동처리기준
		var dayType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DAY_ENTRY_TYPE"), "선택");
		$("#dayOpenType").html(dayType[2]);
		$("#dayCloseType").html(dayType[2]);
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workPattDetId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"순서",			Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"seq",	 KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"근무시간",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"timeCdMgrId",  KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
			{Header:"비고",			Type:"Text",	 	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);
        
		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "선택");
		sheet2.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );

		sheetInit();
		doAction1("Search");
	});
	
	var newIframe;
	var oldIframe;
	var iframeIdx;
	
	$(function() {
		newIframe = $('#tabs-1 layout_tabs');
		iframeIdx = 0;

		$( "#tabs" ).tabs({
			beforeActivate: function(event, ui) {
				iframeIdx = ui.newTab.index();
				newIframe = $(ui.newPanel).find('layout_tabs');
				oldIframe = $(ui.oldPanel).find('layout_tabs');
				showIframe();
			}
		});
	});
	
	function showIframe() {
		if(iframeIdx == 0) {
			$("#tabs-1").show();
			$("#tabs-2").hide();
		} else if(iframeIdx == 1) {
			$("#tabs-1").hide();
			$("#tabs-2").show();
		}
	}

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/flexibleStd/listWeb" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|workTypeCd|flexibleNm|useSymd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/flexibleStd/saveWeb", $("#sheetForm").serialize());
			break;
			
		case "Save2":
			// 필수값 체크
	        if ( !checkList() ) {
	            return false;
	        }
			var row = sheet1.GetSelectRow();
			
			if($('#trHoliday').is(':visible')){
				var chkYn = getCheckYn("holExceptYn");
				sheet1.SetCellValue(row, "holExceptYn", chkYn);
			}
			if($('#trBaseCheck').is(':visible')){
	        	var chkYn = getCheckYn("defaultWorkUseYn");
				sheet1.SetCellValue(row, "defaultWorkUseYn", chkYn);
	        }
	        if($('#trFixOt').is(':visible')){
	        	sheet1.SetCellValue(row, "fixotUseType", $("#fixotUseType").val());
	        	sheet1.SetCellValue(row, "fixotUseLimit", $("#fixotUseLimit").val());
	        }
	        if($('#trWorkTime').is(':visible')){
	        	sheet1.SetCellValue(row, "workShm", $("#workShm").val());
	        	sheet1.SetCellValue(row, "workEhm", $("#workEhm").val());
	        	var chkYn = getCheckYn("taaWorkYn");
	        	sheet1.SetCellValue(row, "taaWorkYn", chkYn);
	        }
	        if($('#trCoreChk').is(':visible')){
	        	var chkYn = getCheckYn("coreChkYn");
				sheet1.SetCellValue(row, "coreChkYn", chkYn);
	        }
	        if($('#trCoreTime').is(':visible')){
	        	sheet1.SetCellValue(row, "coreShm", $("#coreShm").val());
	        	sheet1.SetCellValue(row, "coreEhm", $("#coreEhm").val());
	        }
	        if($('#trBaseFirst').is(':visible')){
	        	sheet1.SetCellValue(row, "exhaustionYn", $("#exhaustionYn").val());
	        }
	        if($('#trUnplan').is(':visible')){
	        	var chkYn = getCheckYn("unplannedYn");
	        	sheet1.SetCellValue(row, "unplannedYn", chkYn);
	        }
	        if($('#trUsedTerm').is(':visible')){
	        	var usedTermArr = new Array();
				$('input[name="usedTermOpt"]').each(function() {
				    if(this.checked){
				    	var objItem = new Object();				    	
						objItem.lable = this.title;
						objItem.value = this.value;
						usedTermArr.push(objItem);
				    }
				});
				var usedTermOpt = JSON.stringify(usedTermArr);
				sheet1.SetCellValue(row, "usedTermOpt", usedTermOpt);
	        }
	        if($('#trApplTerm').is(':visible')){
	        	var applTermOptArr = new Array();
				$('input[name="applTermOpt"]').each(function() {
				    if(this.checked){
				    	var objItem = new Object();				    	
						objItem.lable = this.title;
						objItem.value = this.value;
						applTermOptArr.push(objItem);
				    }
				});
				var applTermOpt = JSON.stringify(applTermOptArr);
				sheet1.SetCellValue(row, "applTermOpt", applTermOpt);
	        }
	        if($("#dayOpenType").val() == "" || $("#dayOpenType").val() == "null" || $("#dayOpenType").val() === null){
				alert("출근자동처리기준을 선택하세요");
				return;
			} else {
				sheet1.SetCellValue(row, "dayOpenType", $("#dayOpenType").val());
			}
			if($("#dayCloseType").val() == "" || $("#dayCloseType").val() == "null" || $("#dayCloseType").val() === null){
				alert("퇴근자동처리기준을 선택하세요");
				return;
			} else {
				sheet1.SetCellValue(row, "dayCloseType", $("#dayCloseType").val());
			}
	        if($("#regardTimeCdId").val() == "" || $("#regardTimeCdId").val() == "null" || $("#regardTimeCdId").val() === null){
				alert("간주근무시간을 선택하세요");
				return;
			} else {
				sheet1.SetCellValue(row, "regardTimeCdId", $("#regardTimeCdId").val());
			}
			sheet1.SetCellValue(row, "unitMinute", $("#unitMinute").val());
			sheet1.SetCellValue(row, "taaTimeYn", $("#taaTimeYn").val());
			sheet1.SetCellValue(row, "note", $("#note").val());
			doAction1("Save");
			break;	
			
		case "Insert":
			sheet1.DataInsert(-1) ;
			break;
		}
	}
	
	//근무제패턴저장
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var title = "반복패턴  (순서 1에 해당하는 요일은  <font color='red'>"+sheet1.GetCellValue( sheet1.GetSelectRow(), "baseDay")+"</font>입니다)";
			$("#pattText").html(title);
			var param = "flexibleStdMgrId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleStd/listPatt" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleStd/savePatt", $("#sheetForm").serialize());
			break;
			
		case "Insert":
			var flexibleStdMgrId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId");
			if(flexibleStdMgrId == ""){
				alert("근무제도 저장 후 근무제패턴을 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(-1) ;
				sheet2.SetCellValue(row, "flexibleStdMgrId" , flexibleStdMgrId);
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
			
			if (Msg != "") {
				alert(Msg);
			}
			sheet2.RemoveAll();
			sheetResize();
			if(iframeIdx == 0) {
				showIframe();
			} else {
				$( "#tabs" ).tabs({active:0});
			}

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
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus") != "I"){
			$("#pattBtn").show();	// 패턴저장 버튼 숨김
			$("#optionBtn").show(); //옵션저장 버튼 숨김
			if(OldRow != NewRow){
				
				// 옵션마스터용 값을 셋팅해야함.
				var workTypeCd = sheet1.GetCellValue( NewRow, "workTypeCd");
				$("input:checkbox[name='holExceptYn']").prop("checked", false);
				$("input:checkbox[name='defaultWorkUseYn']").prop("checked", false);
				$("input:checkbox[name='coreChkYn']").prop("checked", false);
				$("input:checkbox[name='usedTermOpt']").prop("checked", false);
				$("input:checkbox[name='applTermOpt']").prop("checked", false);
				
				// 공휴일제외여부
				if(sheet1.GetCellValue( NewRow, "holExceptYn") == "Y"){
					$("input:checkbox[id='holExceptYn']").prop("checked", true);
					
				}
				$("#dayOpenType").val(sheet1.GetCellValue( NewRow, "dayOpenType")).prop("selected", true);
				$("#dayCloseType").val(sheet1.GetCellValue( NewRow, "dayCloseType")).prop("selected", true);
				
				// 고정OT
				if(workTypeCd == "ELSE"){
					$("#trBase").hide();
					$("#trFixOt").hide();
					$("#fixotUseType").val("");
					$("#fixotUseLimit").val("");
				} else {
					// 고정ot소진 사용여부
					if(sheet1.GetCellValue( NewRow, "defaultWorkUseYn") == "Y"){
						$("input:checkbox[id='defaultWorkUseYn']").prop("checked", true);
						setDefaultWorkUseYn(true);
						$("#trFixOt").show();
						$("#fixotUseType").val(sheet1.GetCellValue( NewRow, "fixotUseType")).prop("selected", true);
						$("#fixotUseLimit").val(sheet1.GetCellValue( NewRow, "fixotUseLimit"));
					} else {
						setDefaultWorkUseYn(false);
					}
				}
				
				// 근무가능시각
				if(workTypeCd == "SELE_F" || workTypeCd == "SELE_C"){
					$("#taaTimeYn").val("Y");
					$("#trWorkTime").show();
					$("#workShm").val(sheet1.GetCellValue( NewRow, "workShm"));
					$("#workEhm").val(sheet1.GetCellValue( NewRow, "workEhm"));
					if(sheet1.GetCellValue( NewRow, "taaWorkYn") == "Y"){
						$("input:checkbox[id='taaWorkYn']").prop("checked", true);
					} else {
						$("input:checkbox[id='taaWorkYn']").prop("checked", false);
					}
					$("#trBaseFirst").show();
					$("#exhaustionYn").addClass("required");
					$("#exhaustionYn").val(sheet1.GetCellValue( NewRow, "exhaustionYn")).prop("selected", true);
					$("#trUnplan").show();
					if(sheet1.GetCellValue( NewRow, "unplannedYn") == "Y"){
						$("input:checkbox[id='unplannedYn']").prop("checked", true);
					} else {
						$("input:checkbox[id='unplannedYn']").prop("checked", false);
					}
					if(workTypeCd == "SELE_C"){
						$("#trCoreChk").show();
						if(sheet1.GetCellValue( NewRow, "coreChkYn") == "Y"){
							$("input:checkbox[id='coreChkYn']").prop("checked", true);
							setCoreChkYn(true);
						} else {
							setCoreChkYn(false);
						}
						$("#trCoreTime").show();
						$("#coreShm").val(sheet1.GetCellValue( NewRow, "coreShm"));
						$("#coreEhm").val(sheet1.GetCellValue( NewRow, "coreEhm"));
					} else {
						$("#trCoreChk").hide();
						$("#trCoreTime").hide();
					}
				} else {
					$("#taaTimeYn").val("N");
					$("#trWorkTime").hide();
					$("#trCoreTime").hide();
					$("#trBaseFirst").hide();
					$("#trUnplan").hide();
					$("#trCoreChk").hide();
					$("#exhaustionYn").removeClass("required");
					$("#workShm").val("");
					$("#workEhm").val("");
					$("#coreShm").val("");
					$("#coreEhm").val("");
					$("#coreChkYn").val("");
					$("#exhaustionYn").val("");
					$("#unplannedYn").val("");
					$("#taaWorkYn").val("");
				}
				
				// 신청기간
				if(workTypeCd == "BASE" || workTypeCd == "WORKTEAM"){
					$("#trUsedTerm").hide();
					$("#trApplTerm").hide();
				} else {
					$("#trUsedTerm").show();
					var usedTermOpt = sheet1.GetCellValue( NewRow, "usedTermOpt");
					if(usedTermOpt != ""){
					var dataUseTermOpt = JSON.parse(usedTermOpt);
						for(var i=0; i<dataUseTermOpt.length; i++) {
							var value = dataUseTermOpt[i].value;
							$("input:checkbox[id=usedTermOpt][value=" + value + "]").prop("checked", true);
						}
					}
					$("#trApplTerm").show();
					var applTermOpt = sheet1.GetCellValue( NewRow, "applTermOpt");
					if(applTermOpt != ""){
						var dataApplTermOpt = JSON.parse(applTermOpt);
						for(var i=0; i<dataApplTermOpt.length; i++) {
							var value = dataApplTermOpt[i].value;
							$("input:checkbox[id=applTermOpt][value=" + value + "]").prop("checked", true);
						}
					}
				}
				// 간주근무
				$("#regardTimeCdId").val(sheet1.GetCellValue( NewRow, "regardTimeCdId")).prop("selected", true);
				$("#unitMinute").val(sheet1.GetCellValue( NewRow, "unitMinute"));
					
				$("#note").val(sheet1.GetCellValue( NewRow, "note"));
				
				// 근무패턴 조회
				sheet2.RemoveAll();
				doAction2('Search');
			}
		} else {
			// 신규일때는 속성을 입력할수 없음 
			sheet2.RemoveAll();	// 패턴 클리어
			$("#pattBtn").hide();	// 패턴저장 버튼 숨김
			
			$("#optionBtn").hide(); //옵션저장 버튼 숨김
			setOptionClear();	// 옵션항목 data 클리어
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
	
	function checkList(){
		var ch = true;
        // 화면의 개별 입력 부분 필수값 체크
        $(".required").each(function(index){
            if($(this).val() == null || $(this).val() == ""){
                alert($(this).parent().prev().text()+"은 필수값입니다.");
                $(this).focus();
                ch =  false;
                return false;
            }
        });
        return ch;
	}
	function getCheckYn(objId){
		var chkYn = "N";
		if($("input:checkbox[id="+objId+"]").is(":checked")){
			 chkYn = "Y";
		}
		return chkYn;
	}
	
	function setDefaultWorkUseYn(chk){
		if(chk){
        	$("#trFixOt").show();
        	$("#fixotUseType").addClass("required");
        	$("#fixotUseLimit").addClass("required");
		} else {
            $("#trFixOt").hide();
           	$("#fixotUseType").val("");
			$("#fixotUseLimit").val("");
        	$("#fixotUseType").removeClass("required");
        	$("#fixotUseLimit").removeClass("required");
		}
	}
	 $("#defaultWorkUseYn").change(function(){
	 	setDefaultWorkUseYn($("#defaultWorkUseYn").is(":checked"));
    });
    
    function setCoreChkYn(chk){
		if(chk){
			$("#trCoreTime").show();
		} else {
			$("#trCoreTime").hide();
            $("#coreShm").val("");
           	$("#coreEhm").val("");
		}
	}
	function setOptionClear(){
		$("#pattText").html("반복패턴");
		// input Text 클리어
		var $inputTextList = $("#view_sele").find("input[type='text']");
		$inputTextList.each(function(){
		    $(this).val("");
		});
		// input checkbox 클리어
		var $inputcheckList = $("#view_sele").find("input[type='checkbox']");
		$inputcheckList.each(function(){
		    $(this).prop("checked", false);
		});
		var $inputselectList = $("#view_sele").find("select");
		$inputselectList.each(function(){
		    $(this).val("").prop("selected", true);
		});
		$("#note").val("");
	}
	
	 $("#coreChkYn").change(function(){
	 	setCoreChkYn($("#coreChkYn").is(":checked"));
    });
</script>