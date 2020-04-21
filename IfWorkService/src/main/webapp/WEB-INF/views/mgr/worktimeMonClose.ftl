<div id="monClose">
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
									<span class="label">마감기준</span>
									<select id="worktimeCloseId" name="worktimeCloseId" class="box" onchange="javascript:selectWorktime();"></select>
								</td>
								<td>
									<span class="label">사번/성명</span>
									<input id="searchKeyword"  name="searchKeyword"  type="text" class="text" />
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
								<div class="float-left title">근무마감 월별조회</div>
								<ul class="float-right btn-wrap">
									<li id="btnCloseConfirm" name="btnCloseConfirm"><a href="javascript:doAction1('CloseConfirm');" class="basic authA">마감확정</a></li>
									<li><a href="javascript:doAction1('Down2Excel');" class="basic authA">다운로드</a></li>
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
var closeList = {};
   	$(function() {
   		//resize
		$(window).smartresize(sheetResize);
   		$("#btnCloseConfirm").hide();
   	        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No|No",				Type:"Seq",		Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"소속|소속",				Type:"Text",	Hidden:0,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번|사번",				Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",				Type:"Text",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도|근무제도",		Type:"Combo",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workTypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일|시작일",			Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"symd",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일|종료일",			Type:"Text",	Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"eymd",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|기본시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"baseMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기준시간|연장시간",	Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"baseOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|기본시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"workMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|연장시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"otMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|야간시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"otnMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|휴일시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"holMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|휴일연장시간",	Type:"Int",		Hidden:0,	Width:85,	Align:"Center",	ColMerge:0,	SaveName:"holOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|기본시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aWorkMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|연장시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|야간시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aOtnMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|휴일시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aHolMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"정산시간|휴일연장시간",	Type:"Int",		Hidden:0,	Width:85,	Align:"Center",	ColMerge:0,	SaveName:"aHolOtMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"무급시간|무급시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aNonpayMinute",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"유급시간|유급시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"aPayMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"지각시간|지각시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"lateMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"조퇴시간|조퇴시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"leaveMinute",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"결근시간|결근시간",		Type:"Int",		Hidden:0,	Width:70,	Align:"Center",	ColMerge:0,	SaveName:"absenceMinute",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(4);
		
		//근무제도
		var workTypeList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "WORK_TYPE_CD"), "");
		sheet1.SetColProperty("workTypeCd", {ComboText:workTypeList[0], ComboCode:workTypeList[1]} );
		
		//마감기준
		closeList = ajaxCall("${rc.getContextPath()}/worktimeClose/WorktimeCloseCode", "", false).DATA
		var flexibleList = stfConvCode(closeList, "선택");
		$("#worktimeCloseId").html(flexibleList[2]);
		
		sheetInit();
		
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/worktimeClose/monList" , $("#sheetForm").serialize());
			break;
		case "CloseConfirm":	
			if(!confirm("확정하시겠습니까?")) {
				return;
			}	
			var worktimeCloseId = $("#worktimeCloseId").val();
			$("#loading").show();
			var param = {
					worktimeCloseId: worktimeCloseId
				};
			console.log(param);
			$.ajax({
				url: "${rc.getContextPath()}/worktimeClose/save/confirm",
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
							alert("근무마감 확정 완료되었습니다.");
						
						doAction1("Search");
					}
				},
				error: function(e) {
					$("#loading").hide();
					alert("근무마감 확정 완료되었습니다.");
					console.log(e);
				}
			});
			break;
		case "Down2Excel":	sheet1.Down2Excel(); break;
		}
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
	
	function selectWorktime(){
		// 마감 조회조건이 자동조회하자
		// 1.마감여부 조회
		Util.ajax({
	    	url: "${rc.getContextPath()}/worktimeClose/closeYn?worktimeCloseId="+$("#worktimeCloseId").val(),
			type: "GET",
			contentType: 'application/json',
			dataType: "json",
			success: function(data) {
				// console.log(data);
				var closeYn = "";
				if(data!=null && data.status=='OK') {
					closeYn = data.DATA.closeYn;
				} 
				if(closeYn == "Y"){
					$("#btnCloseConfirm").hide();
				} else {
					$("#btnCloseConfirm").show();
				}
			},
			error: function(e) {
				console.log(e);
				$("#btnCloseConfirm").hide();
			}
		});
		
		// 1.마감월 조회
		doAction1('Search');
	}

</script>