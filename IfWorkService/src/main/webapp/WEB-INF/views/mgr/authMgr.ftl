<div>
 	<div class="container-fluid mgr-wrap bg-white" style="height: calc(100vh - 70px);">
 		<p class="page-title">권한관리</p>
        <div class="row no-gutters">
        	<div class="col-3 pr-3">
			 	<div class="ibsheet-wrapper">
			 		<form id="sheetForm" name="sheetForm" style="display:none;">
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
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">권한관리</div>
							<ul class="float-right btn-wrap" style="display:none;">
								<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet1", "100%", fullsheetH,"kr"); </script>
				</div>
			</div>
			<div class="col-4 pr-3 setting">
                <div class="inner">
                    <div class="sheet_title_wrap clearfix">
                        <div class="float-left title">권한별 기능</div>
                    </div>
                </div>
                <div id="authRule" class="function-list-wrap">
                	<template v-for="f in functions">
                    <div class="title">{{f.text}}</div>
                    <ul class="fun-list"> 
                        <li v-for="(s,idx) in f.subItems">
                            <input type="checkbox" :id="s.key" name="authFuntion" value="" :title="s.text" @change="chkFunc">
                        	<label :for="s.key">{{s.text}}</label>
                            <ol class="desc-list" v-html="s.desc"></ol>
                        </li>
                    </ul>
                    </template>
                </div>
            </div>
            <div class="col-5">
			 	<div class="ibsheet-wrapper">
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">권한 대상자 관리</div>
							<ul class="float-right btn-wrap" style="display:none;">
								<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet2", "100%", fullsheetH,"kr"); </script>
				</div>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript"> 
	$(function() {
		$(window).smartresize(sheetResize);
		
   		var authRuleVue = new Vue({
   			el: "#authRule",
   		    data : {
   		    	functions: [],
   				authRule: []
   	  		},
   	  		mounted: function(){
	   	  		//권한별 기능
	   	 		<#if authFunctions?? && authFunctions!='' && authFunctions?exists >
	   	 			this.functions = JSON.parse("${authFunctions?js_string}"); 
   	    		</#if>
   	    		
   	    		<#if authRule?? && authRule!='' && authRule?exists >
					this.authRule = JSON.parse("${authRule?js_string}");
					if(this.authRule!=null && this.authRule.length>0)
						$(".float-right.btn-wrap").show();
				</#if>
   	  		},
   	  		methods: {
   	  			chkFunc : function(){
   	  				var selRow = sheet1.GetSelectRow();
		   	 		
   	  				var ruleText = [];
		   	 		$("input:checkbox[name=authFuntion]:checked").each(function(){
		   	 			var id = $(this).prop("id");
		   	 			if(ruleText.indexOf(id)==-1)
		   	 				ruleText.push(id);
		   	 		});
		   	 		
		   	 		sheet1.SetCellValue(selRow, "ruleText", JSON.stringify(ruleText));
   	  			}
   	  		}
   		});
		
   		
		var initdata1 = {};
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"authId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"authId",KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"권한명",			Type:"Text",		Hidden:0,	Width:60,	Align:"Left",	ColMerge:0,	SaveName:"authNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"권한기능",		Type:"Text",		Hidden:1,	Width:60,	Align:"Center",	ColMerge:0,	SaveName:"ruleText",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
   			{Header:"비고",  			Type:"Text",     	Hidden:0,   Width:60,   Align:"Left", ColMerge:0, SaveName:"note",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:2000  }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",		Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",		Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",		Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"userAuthId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"authId",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"authId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"authId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"empId",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0, SaveName:"orgNm",		  		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center", ColMerge:0, SaveName:"sabun",		  		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:13 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0, SaveName:"empNm",		  		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUseDefaultTime(0);
		sheet2.SetUnicodeByte(3);
		
		//이름
        //setSheetAutocompleteEmp( "sheet2", "empNm", null, getSheetEmpInfo);
		
		sheetInit();
		
		if(authRuleVue.authRule!=null && authRuleVue.authRule.length>0)
			doAction1("Search");
	});

	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/authMgr/list" , $("#sheetForm").serialize());
			break;
		case "Insert":
			$("input:checkbox[name=authFuntion]").prop("checked",false);
			sheet1.DataInsert(-1) ;
			break;
		case "Save":
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave( "${rc.getContextPath()}/authMgr/save" , $("#sheetForm").serialize());
			break;
		}
	}
	
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var param = "authId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "authId");
			sheet2.DoSearch( "${rc.getContextPath()}/authMgr/list/user" , param);
			break;
		case "Insert":
			var row = sheet2.DataInsert(-1) ;
			sheet2.SetCellValue(row, "authId", sheet1.GetCellValue( sheet1.GetSelectRow(), "authId"));
			break;
		case "Save":
			if(!dupChk(sheet2,"sabun", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave( "${rc.getContextPath()}/authMgr/save/user" , $("#sheetForm").serialize());
			break;
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
		
		if(OldRow != NewRow && sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus") != "I"){
			var rules = sheet1.GetCellValue( sheet1.GetSelectRow(), "ruleText");

			$("input:checkbox[name=authFuntion]").prop("checked",false);
			if(rules!=null && rules!='') {
				rules = JSON.parse(rules);
				rules.map(function(r){
					$("#"+r).prop("checked",true);
				});
			}
			
			doAction2('Search');
		}
	}
	
	function getSheetEmpInfo(returnValue) {
		//console.log(returnValue);
   		sheet2.SetCellValue(gPRow, "sabun",returnValue.sabun);
		sheet2.SetCellValue(gPRow, "empNm",returnValue.empNm);
        sheet2.SetCellValue(gPRow, "orgNm",returnValue.orgNm);
        sheet2.SetCellValue(gPRow, "empId",returnValue.empId);
	}
	
</script>