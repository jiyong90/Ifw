<div id="timeCdMgr">
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
		<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
			<tr>
				<td>
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">근무제 적용</div>
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
					<div class="pt-2">
						<div class="innertab inner">
							<div id="tabs" class="tab">
								<ul class="outer tab_bottom">
									<li><a href="#tabs-1">그룹별</a></li>
									<li><a href="#tabs-2">개인별</a></li>
								</ul>
								<div id="tabs-1">
									<div  class="layout_tabs">
										<div class="inner sheet_title_wrap clearfix">
											<div class="float-left title" id="searchAppText">그룹별 대상자 관리</div>
											<ul class="float-right btn-wrap">
												<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
												<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
											</ul>
										</div>
										<script type="text/javascript">createIBSheet("sheet2", "100%", sheetH40,"kr"); </script>
									</div>
								</div>
								<div id="tabs-2">
									<div  class="layout_tabs">
										<div class="inner sheet_title_wrap clearfix">
											<div class="float-left title" id="searchAppText">개인별 대상자 관리</div>
											<ul class="float-right btn-wrap">
												<li><a href="javascript:doAction3('Insert')" class="basic authA">입력</a></li>
												<li><a href="javascript:doAction3('Save')" class="basic authA">저장</a></li>
											</ul>
										</div>
										<script type="text/javascript">createIBSheet("sheet3", "100%", sheetH40,"kr"); </script>
									</div>
								</div>
							</div>
						</div>
					</div>
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
        
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무명칭",		Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applyNm",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제도기준",		Type:"Combo",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",    Hidden:0, 	Width:90,   Align:"Center", ColMerge:0, SaveName:"useSymd",      	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"종료일",			Type:"Date",    Hidden:0, 	Width:90,   Align:"Center", ColMerge:0, SaveName:"useEymd",      	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"반복여부",		Type:"CheckBox",    Hidden:0, 	Width:70,   Align:"Center", ColMerge:0, SaveName:"repeatYn",      	KeyField:0, Format:"",   	PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:100 },
			{Header:"소정근무시간(분)",	Type:"Int",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"workMinute",	  	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"연장근무시간(분)",	Type:"Int",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"otMinute",		KeyField:0,	Format:"",		PointCount:2,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"대상자조회",		Type:"Image", 	Hidden:0,  	Width:70,  	Align:"Center", ColMerge:0, SaveName:"selectImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:1    },
			{Header:"확정여부",		Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0, SaveName:"applyYn",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"확정상태",		Type:"Html", 		Hidden:0,  	Width:60,  	Align:"Center", ColMerge:0, SaveName:"endImg",  		KeyField:0, Format:"",      PointCount:0,   UpdateEdit:1,   InsertEdit:0,   EditLen:1    },
			{Header:"비고",			Type:"Text",		Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetUseDefaultTime(0);
		sheet1.SetCountPosition(8);
		
		sheet1.SetImageList(0,"${rc.getContextPath()}/IBLeaders/Sheet/icon/icon_popup.png");
		
		//sheet1.SetColProperty("workTime", {ComboText:"선택|1주|2주|3주|4주", ComboCode:"|1week|2week|3week|4week"} );
		//근무제도
		var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "선택");
		sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",		Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyGroupId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"소속코드",  Type:"Text",    	Hidden:0, 	Width:100,  Align:"Center", ColMerge:0, SaveName:"orgCd",     			KeyField:0, Format:"",  PointCount:0, UpdateEdit:0, InsertEdit:0, EditLen:100 },
			{Header:"소속명",    Type:"Text",    	Hidden:0, 	Width:100,  Align:"Center", ColMerge:0, SaveName:"orgNm",     			KeyField:0, Format:"",  PointCount:0, UpdateEdit:0, InsertEdit:1, EditLen:100 },
			{Header:"직무코드",	Type:"Combo",		Hidden:1, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"jobCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"직책",	    Type:"Combo",		Hidden:0, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"dutyCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"직위",	    Type:"Combo",		Hidden:0, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"posCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"직급",	    Type:"Combo",		Hidden:0, 	Width:80,	Align:"Center", ColMerge:0, SaveName:"classCd",				KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무조",	Type:"Combo",		Hidden:0,	Width:80,	Align:"Center", ColMerge:0, SaveName:"workteamCd",			KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0, SaveName:"note",	 			KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUseDefaultTime(0);
		sheet2.SetUnicodeByte(3);
        
		//코드
		var jobCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "JOB_CD"), "전체");
		sheet2.SetColProperty("jobCd", {ComboText:"전체|"+jobCdList[0], ComboCode:"|"+jobCdList[1]} );
		var dutyCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DUTY_CD"), "전체");
		sheet2.SetColProperty("dutyCd", {ComboText:"전체|"+dutyCdList[0], ComboCode:"|"+dutyCdList[1]} );
		var posCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "POS_CD"), "전체");
		sheet2.SetColProperty("posCd", {ComboText:"전체|"+posCdList[0], ComboCode:"|"+posCdList[1]} );
		var classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "CLASS_CD"), "전체");
		sheet2.SetColProperty("classCd", {ComboText:"전체|"+classCdList[0], ComboCode:"|"+classCdList[1]} );
		var workteamCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/workteamMgr/workteamCd", "",false).DATA, "");
		sheet2.SetColProperty("workteamCd", {ComboText:"전체|"+workteamCdList[0], ComboCode:"|"+workteamCdList[1]} );
		
		//조직
        // setSheetAutocompleteOrg("sheet2", "orgNm");
		
		var initdata3 = {};
		initdata3.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata3.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata3.Cols = [
            {Header:"No",		Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyEmpId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleApplyId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0, SaveName:"orgNm",		  		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center", ColMerge:0, SaveName:"sabun",		  		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:13 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"name",		  		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0, SaveName:"note",	 			KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet3, initdata3);
		sheet3.SetEditable(true);
		sheet3.SetVisible(true);
		sheet3.SetUseDefaultTime(0);
		sheet3.SetUnicodeByte(3);
		
		//이름
        setSheetAutocompleteEmp( "sheet3", "name" );
        

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
	
// 조직코드 조회용

function sheet2_OnBeforeEdit(Row, Col) {	
	try{		
		autoCompleteOrgInit(6,sheet2,Row,Col,undefined,undefined);	
	}catch(e){	 	
		alert(e.message);	
	}
}
function sheet2_OnAfterEdit(Row, Col) {	
	try{		
		autoCompleteOrgDestroy(sheet2);	
	}catch(e){		
		alert(e.message);	
		}
}
function sheet2_OnKeyUp(Row, Col, KeyCode, Shift) {	
	try{		
		autoCompleteOrgPress(6,Row,Col,KeyCode);	
	}catch(e){		
		alert(e.message);	
	}
}

var intervalOrgDestory;
// autocomplete 생성
function autoCompleteOrgInit(opt, sheet, Row, Col, renderItem , callBackFunc) {
    if (Col != opt) return;

    //자동완성 List form
    var autocompRenderItem
    var callBackFunctionItem;
    if(renderItem != undefined && renderItem != null ) {
    	autocompRenderItem = new Function ( "return "+ renderItem )();
    } else {
    	autocompRenderItem = orgRenderItem1;
    }

    if ( callBackFunc != undefined ){

    	callBackFunctionItem = callBackFunc
    } else {
    	callBackFunctionItem = "getOrgReturnValue";
    }

    if ($("#autoCompleteOrgDiv").length == 0) {
        $('<div></div>', {
            id: "autoCompleteOrgDiv"
        }).html("<input id='searchKeyword2' name='searchKeyword' type='text' />").appendTo('#orgForm1');

        var inputId = "searchKeyword2";
        
        $("#searchKeyword2").autocomplete({
            source: function(request, response) {
                $.ajax({
                    url: "/ifw/orgCode/list",
                    dateType: "json",
                    type: "post",
                    data: $("#orgForm1").serialize(),
                    success: function(data) {
                        response($.map(data.DATA, function(item) {
                            return {
                                label: item.orgNm + ",  " + item.enterCd,
                                searchNm: $("#searchKeyword2").val(),
                                orgCd: item.orgCd, // 조직코드
                                orgNm: item.orgNm, // 조직명
                                value: item.orgNm,
                                callBackFunc : callBackFunctionItem
                            };
                        }));
                    }
                });
            },
    		autoFocus: true,
            minLength: 1,
            focus: function() {
                return false;
            },
            open: function() {
                $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
            },
            close: function() {
                $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
            }
        }).data("uiAutocomplete")._renderItem = autocompRenderItem;
    };

    //autocomplete 선택되었을 때 Event Handler
    $("#autoCompleteOrgDiv").off("autocompleteselect");
    $("#autoCompleteOrgDiv").on("autocompleteselect", function(event, ui) {
    	var row = Row;
        sheet.SetCellText(Row, Col, ui.item.value);

        $("#autoCompleteOrgInput").val("");
        autoCompleteOrgDestroy(sheet);

        //상세 데이터 가져오기
        var orgInfo = new Object();
        orgInfo.orgCd = ui.item.orgCd;
        orgInfo.orgNm = ui.item.orgNm;
        
        var data = JSON.stringify(orgInfo);

        //직원을 선택시 Data Return

        var returnFunc1 = new Function ( "return "+ ui.item.callBackFunc )()

        if(typeof returnFunc1 != "undefined") {
        	gPRow = row;
        	pGubun = "sheetAutocompleteOrg";
        	returnFunc1( data );
        }

    });

    $(".GMVScroll>div").scroll(function() {
        destroyAutoOrgComplete(sheet);
    });
    $(".GMHScrollMid>div").scroll(function() {
        destroyAutoOrgComplete(sheet);
    });

    var pleft = sheet.ColLeft(sheet.GetSelectCol());
    var ptop = sheet.RowTop(sheet.GetSelectRow()) + sheet.GetRowHeight(sheet.GetSelectRow());
    //건수정보 표시줄의 높이 만큼.
    if (sheet.GetCountPosition() == 1 || sheet.GetCountPosition() == 2) ptop += 13;

    var point = fGetXY(document.getElementById("DIV_" + sheet.id));

    var left = point.x + pleft;
    var top = point.y + ptop - 30;

    var cWidth = 520;
    var cHeight = 104;
    var dWidth = $(window).width();
    var dHeight = $(window).height();

    if (dWidth < left + cWidth) left = dWidth - cWidth;
    if (dHeight < top + cHeight) top = top - cHeight - 28;
    if (top < 0) top = 0;

    $("#autoCompleteOrgDiv").css("left", left + "px");
    $("#autoCompleteOrgDiv").css("top", top + "px");
    clearTimeout(intervalOrgDestory);
    sheet.$beforeEditEnterBehavior = sheet.GetEditEnterBehavior();
    sheet.SetEditEnterBehavior("none");
}

//autocomplete 키보드 이벤트
function autoCompleteOrgPress(opt, Row, Col, code) {
    if (Col != opt) return;

    //IBsheet에서 입력된 값을 가져와 자동완성에 넘김
    var e = jQuery.Event("keydown");
    e.keyCode = code;
    $("#searchKeyword2").trigger(e);
    
    //IBsheet input tag의 속성 - id:_editInput0 class:GMEditInput
    if( $("#_editInput0").length != 0 ) {
    	$("#searchKeyword2").val($("#_editInput0").val());
    } else {
    	//id:_editInput0 가 없는 경우도 있다. 그럴 경우 class:GMEditInput 로 검색
    	$("#searchKeyword2").val($(".GMEditInput").val());
    }
}

// autocomplete 제거
function autoCompleteOrgDestroy(sheet) {
	clearTimeout(intervalOrgDestory);
    intervalOrgDestory = setTimeout(function() {
        destroyAutoCompleteOrg(sheet);
    }, 200);
}

//autocomplete 제거
function destroyAutoCompleteOrg(sheet) {
    $(".GMVScroll>div").unbind("scroll");
    $(".GMHScrollMid>div").unbind("scroll");

    $("#autoCompleteOrgInput").autocomplete("destroy");
    $("#autoCompleteOrgDiv").remove();

    //sheet.SetEditEnterBehavior("tab");
    sheet.SetEditEnterBehavior(sheet.$beforeEditEnterBehavior);
}

//autocomplete 리스트 포맷
function orgRenderItem1(ul, item) {
    return $("<li />")
        .data("item.autocomplete", item)
        .append("<a class='autocomplete' style='width:240px;'>" +
            "<span style='width:40px;'>" + String(item.orgNm).split(item.searchNm).join('<b>' + item.searchNm + '</b>') + "</span>" +
            "<span style='width:90px;'>" + item.orgCd + "</span>" +
            "</a>").appendTo(ul);
}
	
	
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
			sheet1.DoSearch( "${rc.getContextPath()}/flexibleApply/list" , $("#sheetForm").serialize());
			break;
		case "Insert":
			sheet1.DataInsert(0) ;
			break;
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|applyNm|useSymd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/flexibleApply/save", $("#sheetForm").serialize()); break;
			break;
		case "Apply":
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/flexibleApply/Apply", $("#sheetForm").serialize()); break;
			break;
		}
	}
	
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "flexibleApplyId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleApply/grpList" , param);
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleApply/grpSave", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			var flexibleApplyId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			if(flexibleApplyId == ""){
				alert("근무제도 기준을 저장후 대상자를 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(0) ;
				sheet2.SetCellValue(row, "flexibleApplyId" , flexibleApplyId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		}
	}
	
	function doAction3(sAction) {
		switch (sAction) {
		case "Search":
			var param = "flexibleApplyId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			sheet3.DoSearch( "${rc.getContextPath()}/flexibleApply/empList" , param);
			break;
			
		case "Insert":
			var flexibleApplyId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleApplyId");
			if(flexibleApplyId == ""){
				alert("근무제도 기준을 저장후 대상자를 입력하셔야 합니다");
			} else {
				var row = sheet3.DataInsert(0) ;
				sheet3.SetCellValue(row, "flexibleApplyId" , flexibleApplyId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
			}
			break;
		
		case "Save":
			IBS_SaveName(document.sheetForm,sheet3);
			sheet3.DoSave("${rc.getContextPath()}/flexibleApply/empSave", $("#sheetForm").serialize()); break;
			break;
		}
	}
	function getReturnValue(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
   		sheet3.SetCellValue(gPRow, "sabun",returnValue.sabun);
		sheet3.SetCellValue(gPRow, "name",returnValue.empNm);
        sheet3.SetCellValue(gPRow, "orgNm",returnValue.orgNm);
	}
	
	function getOrgReturnValue(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
		sheet2.SetCellValue(gPRow, "orgCd",returnValue.orgCd);
        sheet2.SetCellValue(gPRow, "orgNm",returnValue.orgNm);
	}

	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (Msg != "") {
				alert(Msg);
			}
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
			doAction1("Search");
		} catch (ex) {
			alert("OnSaveEnd Event Error " + ex);
		}
	}
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(OldRow != NewRow){
			sheet2.RemoveAll();
			sheet3.RemoveAll();
			doAction2('Search');
			doAction3('Search');
		}
	}
	
	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
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
</script>