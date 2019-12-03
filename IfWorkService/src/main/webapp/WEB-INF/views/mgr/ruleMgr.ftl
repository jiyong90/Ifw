<div>
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
								<a href="javascript:doAction1('Search');" class="button">조회</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</form>
		<div class="row no-gutters">
			<div class="col-4 pr-3">
				<div class="inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">규칙관리</div>
						<ul class="float-right btn-wrap">
							<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
							<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
						</ul>
					</div>
				</div>
				<script type="text/javascript">createIBSheet("sheet1", "100%", fullsheetH,"kr"); </script>
			</div>
			<div class="col-4 pr-3">
				<div class="innertab inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">대상자</div>
					</div>
					<div id="includeTab" class="tab">
						<ul class="outer tab_bottom">
							<li><a name="ORG" href="#tabs-1">조직</a></li>
							<li><a name="JIKWEE" href="#tabs-1">직위</a></li>
							<li><a name="JIKCHAK" href="#tabs-1">직책</a></li>
							<li><a name="JIKGUB" href="#tabs-1">직급</a></li>
							<li><a name="JOB" href="#tabs-1">직무</a></li>
							<li><a name="EMP" href="#tabs-1">사원</a></li>
						</ul>
						<div id="tabs-1">
							<div class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<ul class="float-right btn-wrap">
										<li><a href="javascript:doAction1('InsertIncludeTarget')" class="basic authA">입력</a></li>
										<li><a href="javascript:doAction1('SaveIncludeTarget')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<script type="text/javascript">createIBSheet("sheet2", "100%", sheetH80,"kr"); </script>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-4">
				<div class="innertab inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">대상자 제외</div>
					</div>
					<div id="excludeTab" class="tab">
						<ul class="outer tab_bottom">
							<li><a name="EMP" href="#tabs-2">사원</a></li>
						</ul>
						<div id="tabs-2">
							<div class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<ul class="float-right btn-wrap">
										<li><a href="javascript:doAction1('InsertExcludeTarget')" class="basic authA">입력</a></li>
										<li><a href="javascript:doAction1('SaveExcludeTarget')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<script type="text/javascript">createIBSheet("sheet3", "100%", sheetH80,"kr"); </script>
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
	var newIframe;
	var oldIframe;
	var iframeIdx;
	
	var newIframe2;
	var oldIframe2;
	var iframeIdx2;
	
   	$(function() {
   		//resize
		$(window).smartresize(sheetResize);
   		
		newIframe = $('#tabs-1 layout_tabs');
		iframeIdx = 0;
		$( "#includeTab" ).tabs({
			beforeActivate: function(event, ui) {
				iframeIdx = ui.newTab.index();
				newIframe = $(ui.newPanel).find('layout_tabs');
				oldIframe = $(ui.oldPanel).find('layout_tabs');
				//showIframe();
			},
			activate: function(event, ui) {
				getIncludeTarget();
				sheetResize();
			}
		});
		
		newIframe2 = $('#tabs-2 layout_tabs');
		iframeIdx2 = 0;
		$( "#excludeTab" ).tabs({
			beforeActivate: function(event, ui) {
				iframeIdx2 = ui.newTab.index();
				newIframe2 = $(ui.newPanel).find('layout_tabs');
				oldIframe2 = $(ui.oldPanel).find('layout_tabs');
				//showIframe();
			}
		});
      
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ruleId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"규칙명",			Type:"Text",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"ruleNm",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"규칙값",			Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"ruleValue",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0 },
			{Header:"비고",			Type:"Text",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetFocusEditMode(0); //단순 포커스 상태, edit모드일 경우 다른 sheet에서 자동완성 시 edit 값이 파라미터로 넘어감
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ruleId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"조직코드",		Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"orgCd",	 	KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
            //{Header:"조직명",  		Type:"Text",     	Hidden:0,   Width:70,   Align:"Center", ColMerge:0, SaveName:"orgNm",  		KeyField:0, Format:"",    PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
            {Header:"조직명",  		Type:"Combo",     	Hidden:0,   Width:70,   Align:"Center", ColMerge:0, SaveName:"orgNm",  		KeyField:0, Format:"",    PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
            {Header:"코드",			Type:"Text",	    Hidden:1,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"code",	 	KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
            {Header:"코드명",  		Type:"Combo",     	Hidden:1,   Width:70,   Align:"Center", ColMerge:0, SaveName:"codeNm",  	KeyField:0, Format:"",    PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
            //{Header:"사번",			Type:"Text",	   	Hidden:1,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"sabun",	 	KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
            {Header:"사번",			Type:"Text",	   	Hidden:1,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"sabun",	 	KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
            {Header:"성명",  			Type:"Text",     	Hidden:1,   Width:70,   Align:"Center", ColMerge:0, SaveName:"empNm",  		KeyField:0, Format:"",    PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
			{Header:"비고",			Type:"Text",	 	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0, SaveName:"note",	KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);
		sheet2.SetFocusEditMode(1);

		
		//성명
        //setSheetAutocompleteEmp( "sheet2", "empNm", null, getSheet2EmpInfo);
		
		var initdata3 = {};
		initdata3.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata3.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata3.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ruleId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            //{Header:"사번",			Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"sabun",	 	KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
            {Header:"사번",			Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"sabun",	 	KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
            {Header:"성명",  			Type:"Text",     	Hidden:0,   Width:70,   Align:"Center", ColMerge:0, SaveName:"empNm",  		KeyField:0, Format:"",    PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
			{Header:"비고",			Type:"Text",	 	Hidden:0,	Width:80,	Align:"Left",	ColMerge:0, SaveName:"note",	KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet3, initdata3);
		sheet3.SetEditable(true);
		sheet3.SetVisible(true);
		sheet3.SetUnicodeByte(3);
		sheet3.SetFocusEditMode(1);

		
		//성명
        //setSheetAutocompleteEmp( "sheet3", "empNm", null, getSheet3EmpInfo);

		sheetInit();
		doAction1("Search");
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
			sheet1.DoSearch( "${rc.getContextPath()}/rule/list" , $("#sheetForm").serialize());
			break;
		case "Insert":
			sheet1.DataInsert(-1) ;
			break;
		case "Save":
			console.log(document.sheetForm);
			if(!dupChk(sheet1,"tenantId|enterCd|ruleNm", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/rule/save", $("#sheetForm").serialize());
			break;
			
		case "InsertIncludeTarget":
			var status = sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus");	
			if(status!=-1 && status=='R')
				sheet2.DataInsert(-1) ;
			else 
				alert("규칙관리를 저장 후 대상자를 입력하셔야 합니다.");
			break;
		case "SaveIncludeTarget":
			var saveJson = sheet2.GetSaveJson(1);
			
			var existValue;
			var ruleValue = sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleValue");	
			if(ruleValue!=null && ruleValue!='' && ruleValue!=undefined) {
				existValue = JSON.parse(ruleValue);	
			}
			
			if(saveJson!=null && saveJson!='' && saveJson!=undefined && saveJson.hasOwnProperty('data')) {
				var data = saveJson.data;
				//console.log(data);
				var selectedTabIdx = $("#includeTab").tabs('option', 'active');
				var selectedTab = $("#includeTab").find("li:eq("+selectedTabIdx+") a").attr("name");
				
				var targetList = [];
				
				data.map(function(j){
					if(j.sStatus!='D') {
						var k;
						var v;
						
						if(selectedTab=='ORG') {
							k = j.orgCd;
							v = j.orgNm;
						} else if(selectedTab=='JIKWEE' || selectedTab=='JIKCHAK' || selectedTab=='JIKGUB' || selectedTab=='JOB') {
							k = j.code;
							v = j.codeNm;
						} else if(selectedTab=='EMP') {
							k = j.sabun;
							v = j.empNm;
						}
						
						var target = {
							k : k,
							v : v,
							updateDate : moment('${today}').format('YYYYMMDD')
						};
						
						targetList.push(target);
					}
				});
				
				var rule = {};
				rule[selectedTab] = targetList;
				
				var newRuleValue = {};
				if(existValue['EXCLUDE']!=null && existValue.hasOwnProperty('EXCLUDE')) {
					newRuleValue['EXCLUDE'] = existValue['EXCLUDE']
				}
				
				if(existValue==null || !existValue.hasOwnProperty('INCLUDE') || existValue['INCLUDE']==null || existValue['INCLUDE']=='' || existValue['INCLUDE']==undefined) {
					newRuleValue['INCLUDE'] = rule;
					
					sheet1.SetCellValue( sheet1.GetSelectRow(), "ruleValue", JSON.stringify(newRuleValue));
					sheet1.SetCellValue( sheet1.GetSelectRow(), "sStatus", "U");
					
				} else {
					$.each(existValue['INCLUDE'], function(k, v){
						if(!rule.hasOwnProperty(k))
							rule[k] = v;
					});
					newRuleValue['INCLUDE'] = rule;
					
					sheet1.SetCellValue( sheet1.GetSelectRow(), "ruleValue", JSON.stringify(newRuleValue));
					sheet1.SetCellValue( sheet1.GetSelectRow(), "sStatus", "U");
				}
				
				
				doAction1("Save");
			} 
			
			break;
			
		case "InsertExcludeTarget":
			var status = sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus");	
			if(status!=-1 && status=='R')
				sheet3.DataInsert(-1) ;
			else 
				alert("규칙관리를 저장 후 대상자를 입력하셔야 합니다.");
			
			break;	
		case "SaveExcludeTarget":
			var saveJson = sheet3.GetSaveJson(1);
			
			var existValue;
			var ruleValue = sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleValue");	
			if(ruleValue!=null && ruleValue!='' && ruleValue!=undefined) {
				existValue = JSON.parse(ruleValue);	
			}
			
			if(saveJson!=null && saveJson!='' && saveJson!=undefined && saveJson.hasOwnProperty('data')) {
				var data = saveJson.data;
				var selectedTabIdx = $("#excludeTab").tabs('option', 'active');
				var selectedTab = $("#excludeTab").find("li:eq("+selectedTabIdx+") a").attr("name");
				
				var targetList = [];
				
				data.map(function(j){
					if(j.sStatus!='D') {
						var k;
						var v;
						
						if(selectedTab=='EMP') {
							k = j.sabun;
							v = j.empNm;
						}
						
						var target = {
							k : k,
							v : v,
							updateDate : moment('${today}').format('YYYYMMDD')
						};
						
						targetList.push(target);
					}
				});
				
				var rule = {};
				rule[selectedTab] = targetList;
				
				var newRuleValue = {};
				if(existValue['INCLUDE']!=null && existValue.hasOwnProperty('INCLUDE')) {
					newRuleValue['INCLUDE'] = existValue['INCLUDE']
				}
				
				if(existValue==null || ruleValue['EXCLUDE']==null || ruleValue['EXCLUDE']=='' || ruleValue['EXCLUDE']==undefined || !ruleValue.hasOwnProperty('EXCLUDE')) {
					newRuleValue['EXCLUDE'] = rule;
					
					sheet1.SetCellValue( sheet1.GetSelectRow(), "ruleValue", JSON.stringify(newRuleValue));
					sheet1.SetCellValue( sheet1.GetSelectRow(), "sStatus", "U");
					
				} else {
					$.each(existValue['EXCLUDE'], function(k, v){
						if(!rule.hasOwnProperty(k))
							rule[k] = v;
					});
					newRuleValue['EXCLUDE'] = rule;
					
					sheet1.SetCellValue( sheet1.GetSelectRow(), "ruleValue", JSON.stringify(newRuleValue));
					sheet1.SetCellValue( sheet1.GetSelectRow(), "sStatus", "U");
				}
				
				doAction1("Save");
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
		
		if(OldRow != NewRow && sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus") != "I"){
			getIncludeTarget();
			getExcludeTarget();
		}
	}
	
	function sheet2_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {

		var colName = sheet2.ColSaveName(NewCol);
		
		if ( colName == "empNm" ){
			setSheetAutocompleteEmp( "sheet2", "empNm" , null , getSheet2EmpInfo );
		}
	}
	
	function sheet3_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {

		var colName = sheet3.ColSaveName(NewCol);
		
		if ( colName == "empNm" ){
			setSheetAutocompleteEmp( "sheet3", "empNm" , null , getSheet3EmpInfo );
		}
	}
	
	function sheet2_OnChange(Row, Col, Value) {
		// 코드 컬럼에 코드값 넣어주기
		if ( sheet2.ColSaveName(Col) == "codeNm"){
			var selectedTabIdx = $("#includeTab").tabs('option', 'active');
			var selectedTab = $("#includeTab").find("li:eq("+selectedTabIdx+") a").attr("name");
			
			var codeNm = sheet2.GetCellValue(Row, Col);
			var codeNms = sheet2.GetComboInfo(Row, Col, "Text").split('|');
			var idx = codeNms.indexOf(codeNm);
			
			var classCdList;
			if(selectedTab=='JIKWEE') {
				classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "POS_CD"), "");
			} else if(selectedTab=='JIKCHAK') {
				classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DUTY_CD"), "");
			} else if(selectedTab=='JIKGUB') {
				classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "CLASS_CD"), "");
			} 
			
			var codes = classCdList[1].split('|');
			
			sheet2.SetCellValue(Row, "code", codes[idx]);
		} else if ( sheet2.ColSaveName(Col) == "orgNm"){
			var selectedTabIdx = $("#includeTab").tabs('option', 'active');
			var selectedTab = $("#includeTab").find("li:eq("+selectedTabIdx+") a").attr("name");
			
			var orgNm = sheet2.GetCellValue(Row, Col);
			var orgNms = sheet2.GetComboInfo(Row, Col, "Text").split('|');
			var idx = orgNms.indexOf(orgNm);
			
			var classCdList;
			if(selectedTab=='ORG') {
				classCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/orgCode/comboList", "", false).DATA, "");
			} 
			
			var codes = classCdList[1].split('|');
			
			sheet2.SetCellValue(Row, "orgCd", codes[idx]);
		}
	}
	
	function getIncludeTarget(){
		var ruleValue = sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleValue");

		sheet2.RemoveAll();
		var selectedTabIdx = $("#includeTab").tabs('option', 'active');
		var selectedTab = $("#includeTab").find("li:eq("+selectedTabIdx+") a").attr("name");
		
		var includeRule;
		var includeTargets;
		if(ruleValue!=null && ruleValue!='' && ruleValue!=undefined) {
			ruleValue = JSON.parse(sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleValue"));
		
			if(ruleValue.hasOwnProperty('INCLUDE')) {
				includeRule = ruleValue['INCLUDE'];
			
				if(selectedTab=='ORG' && includeRule!=null && includeRule!='' && includeRule!=undefined && includeRule.hasOwnProperty('ORG')) {
					includeTargets = includeRule['ORG'];
				} else if(selectedTab=='JIKWEE' && includeRule!=null && includeRule!='' && includeRule!=undefined && includeRule.hasOwnProperty('JIKWEE')) {
					includeTargets = includeRule['JIKWEE'];
				} else if(selectedTab=='JIKCHAK' && includeRule!=null && includeRule!='' && includeRule!=undefined && includeRule.hasOwnProperty('JIKCHAK')) {
					includeTargets = includeRule['JIKCHAK'];
				} else if(selectedTab=='JIKGUB' && includeRule!=null && includeRule!='' && includeRule!=undefined && includeRule.hasOwnProperty('JIKGUB')) {
					includeTargets = includeRule['JIKGUB'];
				} else if(selectedTab=='JOB' && includeRule!=null && includeRule!='' && includeRule!=undefined && includeRule.hasOwnProperty('JOB')) {
					includeTargets = includeRule['JOB'];
				} else if(selectedTab=='EMP' && includeRule!=null && includeRule!='' && includeRule!=undefined && includeRule.hasOwnProperty('EMP')) {
					includeTargets = includeRule['EMP'];
				}
			}
		}
		
		if(selectedTab=='ORG') {
			sheet2.SetColHidden("code", 1);
			sheet2.SetColHidden("codeNm", 1);
			sheet2.SetColHidden("sabun", 1);
			sheet2.SetColHidden("empNm", 1);
			
			if(selectedTab=='ORG') {
				classCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/orgCode/comboList", "", false).DATA, "");
			} 
			sheet2.SetColProperty("orgNm", {ComboText:"|"+classCdList[0], ComboCode:"|"+classCdList[0]} );
			
			sheet2.SetColHidden("orgCd", 0);
			sheet2.SetColHidden("orgNm", 0);
			
		} else if(selectedTab=='JIKWEE' || selectedTab=='JIKCHAK' || selectedTab=='JIKGUB' || selectedTab=='JOB') {
			sheet2.SetColHidden("sabun", 1);
			sheet2.SetColHidden("empNm", 1);
			sheet2.SetColHidden("orgCd", 1);
			sheet2.SetColHidden("orgNm", 1);
			
			var classCdList;
			if(selectedTab=='JIKWEE') {
				classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "POS_CD"), "");
			} else if(selectedTab=='JIKCHAK') {
				classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DUTY_CD"), "");
			} else if(selectedTab=='JIKGUB') {
				classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "CLASS_CD"), "");
			}
			sheet2.SetColProperty("codeNm", {ComboText:"|"+classCdList[0], ComboCode:"|"+classCdList[0]} );
			
			sheet2.SetColHidden("code", 0);
			sheet2.SetColHidden("codeNm", 0);
		} else if(selectedTab=='EMP') {
			sheet2.SetColHidden("orgCd", 1);
			sheet2.SetColHidden("orgNm", 1);
			sheet2.SetColHidden("code", 1);
			sheet2.SetColHidden("codeNm", 1);
			sheet2.SetColHidden("sabun", 0);
			sheet2.SetColHidden("empNm", 0);
		}
		
		//저장된 값 ibSheet 에 보여줌
		if(includeTargets!=null && includeTargets.length>0) {
			includeTargets.map(function(t) {
				var row = sheet2.DataInsert(-1) ;
				var key = t.k;
				var value = t.v;
				var updateDate = t.updateDate;
				
				if(selectedTab=='ORG') {
					sheet2.SetCellValue(row, "orgCd", key);
					sheet2.SetCellValue(row, "orgNm", value);
				} else if(selectedTab=='JIKWEE' || selectedTab=='JIKCHAK' || selectedTab=='JIKGUB' || selectedTab=='JOB') {
					sheet2.SetCellValue(row, "code", key);
					sheet2.SetCellValue(row, "codeNm", value);
				} else if(selectedTab=='EMP') {
					sheet2.SetCellValue(row, "sabun", key);
					sheet2.SetCellValue(row, "empNm", value);
				}
				sheet2.SetCellValue(row, "sStatus", "");
			});
		}
			
	}
	
	function getExcludeTarget(){

		var ruleValue = sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleValue");

		sheet3.RemoveAll();
		var selectedTabIdx = $("#excludeTab").tabs('option', 'active');
		var selectedTab = $("#excludeTab").find("li:eq("+selectedTabIdx+") a").attr("name");
		
		var excludeRule;
		var excludeTargets = null;
		if(ruleValue!=null && ruleValue!='' && ruleValue!=undefined) {
			ruleValue = JSON.parse(sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleValue"));
			
			if(ruleValue.hasOwnProperty('EXCLUDE')) {
				excludeRule = ruleValue['EXCLUDE'];
				
				if(selectedTab=='EMP' && excludeRule!=null && excludeRule!='' && excludeRule!=undefined && excludeRule.hasOwnProperty('EMP')) {
					excludeTargets = excludeRule['EMP'];
				}
			}
		}		
		
		if(excludeTargets!=null && excludeTargets.length>0) {
			excludeTargets.map(function(t) {
				var row = sheet3.DataInsert(-1) ;
				var key = t.k;
				var value = t.v;
				var updateDate = t.updateDate;
				
				if(selectedTab=='EMP') {
					sheet3.SetCellValue(row, "sabun", key);
					sheet3.SetCellValue(row, "empNm", value);
				}
				sheet3.SetCellValue(row, "sStatus", "");
			});
		}
	}
	
	function getSheet2EmpInfo(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
   		sheet2.SetCellValue(gPRow, "sabun",returnValue.sabun);
	}
	
	function getSheet3EmpInfo(returnValue) {
		//var rv = $.parseJSON('{' + returnValue+ '}');
   		sheet3.SetCellValue(gPRow, "sabun",returnValue.sabun);
	}
	
</script>