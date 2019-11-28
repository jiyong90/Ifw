<div id="flexibleEmp">
	<!-- 유연근무변경/취소 modal start -->
	<div class="modal fade" id="flexibleModifyPopModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">유연근무 변경/취소</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div id="view_sele">
						<table class="default">
							<tbody>
								<tr>
									<th>사번/성명</th>
									<td colspan="3">
										<span id="orgSabun"></span> / <span id="orgEmpNm"></span>
									</td>
								</tr>
								<tr>
									<th>근무제도</th>
									<td>
										<span id="orgWorkTypeNm"></span>
									</td>
									<th>근무제도명</th>
									<td>
										<span id="orgFlexibleNm"></span>
									</td>
								</tr>
								<tr>
									<th>적용기간</th>
									<td colspan="3">
										<span id="orgSymd"></span> ~ <span id="orgEymd"></span>
									</td>
								</tr>
								<tr>
									<th>구분</th>
									<td colspan="3">
										<select id="changeType">
		                                    <option value="DEL" selected="selected">취소</option>
		                                    <option value="MOD">변경</option>
		                                </select>
									</td>
								</tr>
								<tr>
									<th>변경적용기간</th>
									<td colspan="3">
										<input type="text" id="chgSymd" name="chgSymd" class="date2 required" data-toggle="datetimepicker" data-target="#chgSymd" placeholder="연도-월-일" autocomplete="off" disabled/>
										 ~ 
										<input type="text" id="chgEymd" name="chgEymd" class="date2 required" data-toggle="datetimepicker" data-target="#chgEymd" placeholder="연도-월-일" autocomplete="off" disabled/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="inner sheet_title_wrap clearfix">
						<ul class="float-right btn-wrap" id="sheet3Btn">
							<li><a href="javascript:chgApply()" class="basic authA">적용</a></li>
						</ul>
					</div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>
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
									<span class="label">근무제도 </span>
									<select id="searchWorkTypeCd" name="searchWorkTypeCd" class="box" onchange="javascript:doAction1('Search');"></select>
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
								<div class="float-left title">개인별 근무제도 조회</div>
								<!-- <ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
								</ul> -->
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
	var modifyFlexibleEmpId;
	var flexibleStdMgrId;
   	$(function() {
   		//resize
		$(window).smartresize(sheetResize);
   	
	    $('#sYmd, #eYmd, #chgSymd, #chgEymd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:0,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:1,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:1,	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"oid",		Type:"Text",		Hidden:1,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"flexibleEmpId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"stdmgrId",	Type:"Text",		Hidden:1,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"flexibleStdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workTypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제도명",	Type:"Text",		Hidden:0,	Width:150,	Align:"Left",	ColMerge:0,	SaveName:"flexibleNm",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일",		Type:"Date",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"symd",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"종료일",		Type:"Date",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"eymd",		KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무변경/취소",		Type:"Html",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"modify",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		//근무제도
		var flexibleList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "WORK_TYPE_CD"), "전체");
		sheet1.SetColProperty("workTypeCd", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );
		$("#searchWorkTypeCd").html(flexibleList[2]);

		
		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/flexibleEmp/listWeb" , $("#sheetForm").serialize());
			break;
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
	// 팝업오픈
	function setModify(flexibleEmpId){
		var findVal = flexibleEmpId + "";
		var row = sheet1.FindText(3, findVal);
		modifyFlexibleEmpId = flexibleEmpId;
		flexibleStdMgrId = sheet1.GetCellValue(row, "sabflexibleStdMgrIdun");
		$("#orgSabun").text(sheet1.GetCellValue(row, "sabun"));
		$("#orgEmpNm").text(sheet1.GetCellValue(row, "empNm"));
		$("#orgWorkTypeNm").text(sheet1.GetCellText(row, "workTypeCd"));
		$("#orgFlexibleNm").text(sheet1.GetCellValue(row, "flexibleNm"));
		$("#orgSymd").text(formatDate(sheet1.GetCellValue(row, "symd"), "-"));
		$("#orgEymd").text(formatDate(sheet1.GetCellValue(row, "eymd"), "-"));
		$("#flexibleModifyPopModal").modal("show");
		
	}
	// 변경적용
	function chgApply(){
		console.log("chgApply" + modifyFlexibleEmpId);
		var msg1 = "";
		var msg2 = "";
		var $this = this;
		var changeType = $("#changeType").val();
		var chgSymd = "";
		var chgEymd = "";
		var sabun = $("#orgSabun").text();
		var orgSymd = replaceAll($("#orgSymd").text(), "-", "");
		var orgEymd = replaceAll($("#orgEymd").text(), "-", "");
		
		msg1 = $("#orgEmpNm").text() + "님의 " + $("#orgFlexibleNm").text() + " 유연근무제도를 ";
		
		if(changeType == "MOD"){
			chgSymd = replaceAll($("#chgSymd").val(), "-", "");
			chgEymd = replaceAll($("#chgEymd").val(), "-", "");
			msg1 = msg1 + "변경하시겠습니까?";
		}else{
			msg1 = msg1 + "취소하시겠습니까?";
		}
		
		if(confirm(msg1)){

			var param = {
				  flexibleEmpId : modifyFlexibleEmpId
	 			, flexibleStdMgrId : flexibleStdMgrId
				, changeType : changeType
				, sabun : sabun
	 			, sYmd : chgSymd
	 			, eYmd : chgEymd
	 			, orgSymd : orgSymd
	 			, orgEymd : orgEymd
	 		};
			console.log(param);
	    	Util.ajax({
				url: "${rc.getContextPath()}/flexibleEmp/changeChk",
				type: "POST",
				contentType: 'application/json',
				data: JSON.stringify(param),
				dataType: "json",
				success: function(data) {
					console.log(data);					
					if(data!=null) {
						if(data.data.retType == "END"){
							alert("적용완료되었습니다.");
							$("#flexibleModifyPopModal").modal("hide");
							doAction1("Search");
							// 팝업닫고 재조회
						} else if(data.data.retType == "MSG"){
							// 안내할 메시지가 있음 반영여부 확인 
							if(confirm(data.data.retMsg)){
								// 오류가 있어도 반영한다면 다시 부르자 갱신로직
								if(changeType == "DEL"){
									chgSymd = orgSymd;
									chgEymd = orgEymd;
								}
								var param2 = {
									  flexibleEmpId : modifyFlexibleEmpId
						 			, flexibleStdMgrId : flexibleStdMgrId
									, changeType : changeType
									, sabun : sabun
						 			, symd : chgSymd
						 			, eymd : chgEymd
						 			, orgSymd : orgSymd
						 			, orgEymd : orgEymd
						 			, hisId : data.data.retId
						 		};
								console.log(param2);
								Util.ajax({
									url: "${rc.getContextPath()}/flexibleEmp/changeFlexible",
									type: "POST",
									contentType: 'application/json',
									data: JSON.stringify(param2),
									dataType: "json",
									success: function(data) {
										if(data!=null) {
											if(data.data.retType == "END"){
												alert("적용완료되었습니다.");
												// 팝업닫고 재조회
												$("#flexibleModifyPopModal").modal("hide");
												doAction1("Search");
											} else {
												alert(data.data.retMsg);
											}
										}
									},error: function(e) {
										console.log(e);
										alert("근무적용중 오류가 발생하였습니다.");
									}
								});
							}
						}
					} else {
						alert("근무적용검증중 오류가 발생하였습니다.");
					}
					
				},
				error: function(e) {
					console.log(e);
					alert("근무변경정보 조회에 오류가 발생하였습니다.");
				}
			}); 
		}
	}
	
	$("#changeType").change(function(){
		if($("#changeType").val() == "DEL"){
			// 유연근무취소
			$("#chgSymd, #chgEymd").val("");
			$("#chgSymd, #chgEymd").attr("disabled",true);
		} else {
			$("#chgSymd, #chgEymd").removeAttr("disabled");
		}
    });
</script>