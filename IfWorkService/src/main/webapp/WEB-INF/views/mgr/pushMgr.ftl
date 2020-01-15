<div id="empListMgr">
	<!-- 알림 메시지 modal start -->
	<div class="modal fade" id="pushMsgModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog modal-xl-more" role="document">
            <div class="modal-content rounded-0">
                <div class="modal-header">
                    <h5 class="modal-title">알림메시지 작성</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
						<tr>
							<td>
								<textarea id="pushMsg" name="pushMsg" style="width:99%;height:200px"></textarea>
							</td>
						</tr>
					</table>
					
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default rounded-0" onClick="javascript:doAction1('SaveMsg');">저장</button>
                    <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 알림 메시지  modal end -->
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
								<div class="float-left title">알림관리</div>
								<ul class="float-right btn-wrap">
									<li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li>
									<li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li>
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
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"pushMgrId",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사업장",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"businessPlaceCd",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"알림대상",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"pushObj",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"알림기준",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"stdType",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"기준시간(분)",	Type:"Int",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"stdMinute",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"알림제목",		Type:"Text",	Hidden:0,	Width:150,	Align:"Left",	ColMerge:0,	SaveName:"title",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"알림메시지",	Type:"Image",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"pushDetail",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"알림메시지",	Type:"Text",		Hidden:1,	Width:50,	Align:"Left",	ColMerge:0,	SaveName:"pushMsg",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"푸시여부",	Type:"CheckBox",	Hidden:0,	Width:70,	Align:"Left",	ColMerge:0,	SaveName:"mobileYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"문자여부",	Type:"CheckBox",	Hidden:1,	Width:70,	Align:"Left",	ColMerge:0,	SaveName:"smsYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"이메일여부",	Type:"CheckBox",	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0,	SaveName:"emailYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일자",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:1,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"종료일자",	Type:"Date",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");

		//사업장
		var businessPlaceCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BUSINESS_PLACE_CD"), "선택");
		sheet1.SetColProperty("businessPlaceCd", {ComboText:businessPlaceCdList[0], ComboCode:businessPlaceCdList[1]} );
		
		var pushObj = stfConvCode(codeList("${rc.getContextPath()}/code/list", "MSG_TARGET_CD"), "선택");
		sheet1.SetColProperty("pushObj", {ComboText:"|"+pushObj[0], ComboCode:"|"+pushObj[1]} );
		
		var stdType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "MSG_STD_TYPE_CD"), "선택");
		sheet1.SetColProperty("stdType", {ComboText:"|"+stdType[0], ComboCode:"|"+stdType[1]} );

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/pushMgr/list" , $("#sheetForm").serialize());
			break;
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|businessPlaceCd|pushObj|stdType|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/pushMgr/save", $("#sheetForm").serialize()); break;

			break;
		case "SaveMsg":
			var pushMgrId = sheet1.GetCellValue( sheet1.GetSelectRow(), "pushMgrId");
			
			var param = {
				pushMgrId : pushMgrId,
    			pushMsg: $("#pushMsg").val()
    		};
	    		
	    	Util.ajax({
	    		url: "${rc.getContextPath()}/pushMgr/save/msg",
				type: "POST",
				contentType: 'application/json',
				dataType: "json",
				data: JSON.stringify(param),
				success: function(data) {
					$("#loading").hide();
					if(data!=null && data.status=='OK') {
						$("#alertText").html("저장되었습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
							$("#pushMsgModal").modal("hide"); 
							doAction1("Search");
						});
					} else {
						$("#alertText").html(data.message);
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
					}
					
					$("#alertModal").modal("show"); 
					
				},
				error: function(e) {
					$("#loading").hide();
					console.log(e);
				}
			});
			
			break;
		case "Insert":
			sheet1.DataInsert(0) ;
			break;
		}
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			} else {
				
				var row = sheet1.LastRow();
				for(var i=2; i<=row; i++){
					editUseYn(i);
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
	
	function sheet1_OnClick(Row, Col, Value) {
		try{
			if(Row > 0 && sheet1.ColSaveName(Col) == "pushDetail"){
				if(sheet1.GetCellValue(Row, "sStatus") == "I"){
					alert("저장 후 알림 내용 등록 및 수정 하시기 바랍니다.");
					return;
				} else {
					var pushMsg = sheet1.GetCellValue( sheet1.GetSelectRow(), "pushMsg");
					$("#pushMsg").val(pushMsg);
					$("#pushMsgModal").modal("show"); 
				}
				 
			}
		}catch(ex){
			alert("OnClick Event Error : " + ex);
		}
	}
	

	function sheet1_OnChange(Row, Col, Value) {
		if ( sheet1.ColSaveName(Col) == "pushObj" || sheet1.ColSaveName(Col) == "stdType"){
			editUseYn(sheet1.GetSelectRow());
		}
	}
	
	function editUseYn(row) {
		var pushObj = sheet1.GetCellValue(row, "pushObj");
		var stdType = sheet1.GetCellValue(row, "stdType");
		
		if(pushObj=='COMM' && stdType=='PW') {
			var passwordCertificate = 'EMAIL';
			<#if passwordCertificate?? && passwordCertificate?exists >
				passwordCertificate = "${passwordCertificate}";
			</#if>
			
			if(passwordCertificate=='PHONE') {
    			sheet1.SetCellValue(row, "smsYn", "Y");
    		} else if(passwordCertificate=='EMAIL') {
    			sheet1.SetCellValue(row, "emailYn", "Y");
    		}
			
			sheet1.SetCellEditable(row, "smsYn", 0);
			sheet1.SetCellEditable(row, "mobileYn", 0);
			sheet1.SetCellEditable(row, "emailYn", 0);
		} else {
			sheet1.SetCellEditable(row, "smsYn", 1);
			sheet1.SetCellEditable(row, "mobileYn", 1);
			sheet1.SetCellEditable(row, "emailYn", 1);
		}
	}
	
</script>