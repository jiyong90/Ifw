<div id="empCalendarMgr">
 	<div class="container-fluid bg-white mgr-wrap" >
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
						<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
						~
						<input type="text" id="eYmd" name="eYmd" class="date2 required datetimepicker-input"  data-toggle="datetimepicker" data-target="#eYmd" placeholder="연도-월-일" autocomplete="off"/>
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
						<div class="float-left title">근무캘린더 &nbsp;<span id="Tooltip-calendarAll" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
							<ul class="float-right btn-wrap">
								<!-- li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li -->
								<li><a href="javascript:doAction1('Down2Excel')" class="basic authR">다운로드</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet1", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
			<tr>
				<td>
					<div id="detailVue" class="inner">
						<div class="sheet_title_wrap clearfix">
						<div class="float-left title">근무상세결과 &nbsp;
							<span id="Tooltip-flexibleEmpCaldays" class="tooltip-st"><i class="far fa-question-circle"></i></span> &nbsp;&nbsp;
							<template v-if="flexibleNm">
                    		<span>근무제도 : {{flexibleNm}} &nbsp;&nbsp; , 근무적용기간 : {{flexibleSymd}} ~ {{flexibleEymd}}</span>
                    		</template>
							
						</div>
							<ul class="float-right btn-wrap" id="optionBtn">
								<li><a href="javascript:doAction2('finishDay')" class="basic authA">일마감</a></li>
								<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript">createIBSheet("sheet2", "100%", halfsheetH,"kr"); </script>
				</td>
			</tr>
		</table>
		<div id="finishDay">
			<div class="modal fade" id="requestModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
		        <div class="modal-dialog modal-lg" role="document">
		            <div class="modal-content">
		                <div class="modal-header">
		                    <h5 class="modal-title">일마감</h5>
		                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		                        <span aria-hidden="true">&times;</span>
		                    </button>
		                </div>
		                <div class="modal-body">
		                	<div class="modal-app-wrap">
		                		<div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
		                			<div class="loading-spinner" style="display:none;"></div>
									<div class="title" >부서/성명/사번</div>
									<div class="main-desc">{{orgNm}} / {{empNm}} / {{sabun}}</div>
								</div>
		                		<div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
		                			<div class="loading-spinner" style="display:none;"></div>
									<div class="title" >근무제도</div>
									<div class="main-desc">{{flexibleNm}}</div>
								</div>
		                		<div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
									<div class="title" >근무제도기간</div>
									<div class="main-desc">{{flexibleSymd}} ~ {{flexibleEymd}}</div>
								</div>
								<div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
									<div class="title">일마감 기간</div>
								</div>
								<div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
									<div class="form-row">
									<div class="d-sm-none d-lg-block ml-md-auto"></div>
									<div class="col" data-target-input="nearest">
									<input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="fsYmd" data-toggle="datetimepicker" data-target="#fsYmd" placeholder="연도-월-일" autocomplete="off" required readonly="readonly">
									</div>
									<span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
									<div class="col" data-target-input="nearest">
									<input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="feYmd" data-toggle="datetimepicker" data-target="#feYmd" placeholder="연도-월-일" autocomplete="off" required>
									</div>
									</div>
								</div>
							</div>
		                </div>
		                <div class="modal-footer">
		                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
		                    <button type="button" id="apprBtn" class="btn btn-default"  @click="confirmFinshDay">확인</button>
		                </div>
		            </div>
		        </div>
			</div>
		</div>
	</div>
	</div>
</div>

		


<script type="text/javascript">
	var curRow = 0;
	var detailVue = new Vue({
			el: "#detailVue",
	    data : {
	    	selectedFlexibleEmpStd: {},
	    	flexibleNm : '',
	    	flexibleTerm : '',
	    	flexibleSymd : '',
	    	flexibleEymd : '',
	    	orgNm : '',
	    	sabun : '',
	    	empNm : ''
			},
	    mounted: function(){
	    	var $this = this;
	    }
	});
	
	var finishDayVue = new Vue({
   		el: "#finishDay",
	    data : {
	    	selectedFlexibleEmpStd: {},
	    	flexibleNm : '',
	    	flexibleTerm : '',
	    	flexibleSymd : '',
	    	flexibleEymd : '',
	    	orgNm : '',
	    	sabun : '',
	    	empNm : ''
  		},
	    mounted: function(){
	    	var $this = this;
	    },
	    methods : {
	    	getFlexibleEmpStd : function(){ //사용할 근무제 리스트
	    		var $this = this;
	    		var param = {
	    				"sabun" 		   : sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun"),
						"flexibleEmpId"    : sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleEmpId"),
						"flexibleStdMgrId" : sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId"),
						"ymd"              : sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd")
	    	    		}
	    		
	    		Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/checkFinDay",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						if(data!=null) {
							$this.selectedFlexibleEmpStd = data;
							$this.flexibleNm = data.flexibleNm;
							$this.flexibleSymd = moment(data.sYmd).format('YYYY-MM-DD');
							$this.flexibleEymd = moment(data.eYmd).format('YYYY-MM-DD');
							$this.orgNm = sheet1.GetCellValue( sheet1.GetSelectRow(), "orgNm");
							$this.sabun = sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun");
							$this.empNm = sheet1.GetCellValue( sheet1.GetSelectRow(), "empNm");
							
							detailVue.selectedFlexibleEmpStd = data;
							detailVue.flexibleNm = data.flexibleNm;
							detailVue.flexibleSymd = $this.flexibleSymd;
							detailVue.flexibleEymd = $this.flexibleEymd;
							detailVue.orgNm = $this.orgNm;
							detailVue.sabun = $this.sabun;
							detailVue.empNm = $this.empNm;
						} 
					},
					error: function(e) {
						 
						return;
					}
				}); 
		    },
    		showRequest : function() {
    			$('#requestModal').modal("show");
		    },
		    confirmFinshDay : function() {
		    	if(!confirm("일마감 하시겠습니까?")) {
					return;
				}
		    	var ymd = sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd");
				var sabun = sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun");

				var param = {
							 "ymd" : ymd
							,"sabun" : sabun
							,"paramSdate" : moment($("#fsYmd").val()).format('YYYYMMDD')
							,"paramEdate" : moment($("#feYmd").val()).format('YYYYMMDD')
							}
				$("#loading").show();

				Util.ajax({
					url: "${rc.getContextPath()}/flexibleEmp/finishDay",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						$("#loading").hide();
						if(data!=null) {
							$("#alertText").html(data.message);
							$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
							$("#alertModal").modal("show");
							curRow = sheet1.GetSelectRow();
							doAction1("Search");
													
						} 
					},
					error: function(e) {
						$("#loading").hide();
						$("#alertText").html("일마감 처리중 오류가 발생하였습니다.");
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show"); 
					}
				}); 
				
			}
	    }
   	});
	
   	$(function() {
   		$('#sYmd, #eYmd, #fsYmd, #feYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
        
   		$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    $("#eYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    $("#fsYmd").val("");
	    $("#feYmd").val("");

	    $("#feYmd").off("change.datetimepicker").on("change.datetimepicker", function(e) {
	    	var fsYmd = $("#fsYmd").val();
	    	var feYmd = $("#feYmd").val();
	    	if(fsYmd != '' && feYmd != '') {
		    	if(feYmd < fsYmd) {
					$("#alertText").html("일마감 시작일보다 크거나 같아야 합니다.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         		});
  	         		$("#alertModal").modal("show"); 
  	         		$("#feYmd").val($("#fsYmd").val());
  	         		return;
			    }

		    	if(finishDayVue.flexibleEymd < feYmd) {
					$("#alertText").html("근무제도 기간보다 클 수 없습니다.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         		});
  	         		$("#alertModal").modal("show"); 
  	         		$("#feYmd").val($("#fsYmd").val());
  	         		return;
			    }
			    
		    }
		});

	    new jBox('Tooltip', {
       	    attach: '#Tooltip-calendarAll',
       	    target: '#Tooltip-calendarAll',
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
       	    content: '개인별 근무시간을 관리합니다.'
	    		   + '<br>● 근무마감여부가 비어있거나 N인 경우 근무상세결과를 수정할수 있습니다.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
	    
	    new jBox('Tooltip', {
       	    attach: '#Tooltip-flexibleEmpCaldays',
       	    target: '#Tooltip-flexibleEmpCaldays',
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
       	    content: '개인별 근무 상세시간을 관리합니다.'
   	    		   + '<br>● 시간구분이 기본근무, 연장근무, 야간근무인 경우만 입력/수정가능합니다.'
   	    		   + '<br>● 근무정보는 계획시간만 입력/수정가능합니다.'
   	    		   + '<br>● 근무정보의 인정시간은 계획시간 및 타각시간을 기준으로 자동 갱신됩니다.'
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
			//{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workCalendarId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"소속",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgNm"		,	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무일",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ymd",				KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근태코드",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"taaNms",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"근무합산시간",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"totTime",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근시각",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entrySdate",		KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"출근구분",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entryStypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근시각",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entryEdate",		KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근구분",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"entryEtypeCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"코어시작시간",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"coreShm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"코어종료시간",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"coreEhm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무마감여부",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workCloseYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"유연근무대상자ID",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleEmpId",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },			
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);	//sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(8);

		//근무제도
		var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "");
		sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );
		
		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=",false).DATA, "");
		// var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "");
		sheet1.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );
		
		//타각구분
		var entryTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "ENTRY_TYPE_CD"), "");
		sheet1.SetColProperty("entryStypeCd", {ComboText:entryTypeCdList[0], ComboCode:entryTypeCdList[1]} );
		sheet1.SetColProperty("entryEtypeCd", {ComboText:entryTypeCdList[0], ComboCode:entryTypeCdList[1]} );
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"empId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleEmpId",		KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workDayResultId",		KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"사번",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"일자",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"ymd",			KeyField:0,	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시간구분",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeTypeCd",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근태코드",  		Type:"Combo",     	Hidden:0,   Width:70,  	Align:"Center",  ColMerge:0, SaveName:"taaCd",   	 KeyField:0,    Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:100  },
            {Header:"계획시작시각",		Type:"Text",	 	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"planSdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획종료시각",		Type:"Text",	 	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"planEdate",	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획근무시간",		Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"planMinute",	 KeyField:0,	Format:"", 		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정시작시각",		Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprSdate", 	 KeyField:0,	Format:"YmdHm",	 	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정종료시각",		Type:"Text",	    Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprEdate", 	 KeyField:0,	Format:"YmdHm",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"인정근무시간",		Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprMinute",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
			//{Header:"비고",			Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];


        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
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
			sheet1.DoSearch( "${rc.getContextPath()}/calendar/all" , $("#sheetForm").serialize());
			break;
	/*
		case "Save":
			if(!dupChk(sheet1,"businessPlaceCd|symd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/basework/save", $("#sheetForm").serialize()); break;

			break;
	*/
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
			var param = "ymd="+sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd") + "&sabun="+sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun") + "&timeCdMgrId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleEmp/caldays" , param);
			break;
		
		case "Save":
        	for(var i=sheet2.HeaderRows();i<sheet2.RowCount()+sheet2.HeaderRows(); i++){
        		if(sheet2.GetCellValue(i, "sStatus") == "I" || sheet2.GetCellValue(i, "sStatus") == "U"){ 
        			var s = sheet2.GetCellValue(i, "planSdate");
					s = s.substring(8, s.length);

					var e = sheet2.GetCellValue(i, "planEdate");
					e = e.substring(8, e.length);
					
					var row = sheet1.GetSelectRow();
					
	        		if(sheet2.GetCellValue(i, "timeTypeCd") == "SELE_E" || sheet2.GetCellValue(i, "timeTypeCd") == "SELE_F") {
						
			        	var cores = sheet1.GetCellValue(row, "coreShm");
			        	var coree = sheet1.GetCellValue(row, "coreEhm");
			        	
			        	if(cores != "" && coree != ""){
	
				        	if(s <= cores && e >= coree) {
				        	} else {
				        		alert("기본 근무시간에는 코어 시간이 포함되어야 합니다. (코어시간 " + cores.substring(0,2) + ":" + cores.substring(2,4) + "~" + coree.substring(0,2) + ":" + coree.substring(2,4) + ")");
				        		return;
				        	}
			        	}
	        		}
	        		if(sheet2.GetCellValue(i, "timeTypeCd") == "OT") {
						if(s > "2200" || s < "0600"){
							alert("연장근무시간은 06:00 ~ 22:00시 사이에 등록가능합니다.");
							sheet2.SetCellValue(i, "planSdate", "");
							return;
						}
						if(e > "2200" || e < "0600"){
							alert("연장근무시간은 06:00 ~ 22:00시 사이에 등록가능합니다.");
							sheet2.SetCellValue(i, "planEdate", "");
							return;
						}
	        		}
	        		if(sheet2.GetCellValue(i, "timeTypeCd") == "NIGHT") {
	        			var ymd = sheet1.GetCellValue(row, "ymd");
	        			var symd = sheet2.GetCellValue(i, "planSdate");
	        			symd = symd.substring(0, 8);
	        			var eymd = sheet2.GetCellValue(i, "planEdate");
	        			eymd = eymd.substring(0, 8);
	        			console.log("ymd : " + ymd);
	        			console.log("symd : " + symd);
	        			console.log("eymd : " + eymd);
						if((ymd == symd && s < "2200") || (ymd != symd && s > "0600")){
							alert("야간근무시간은 22:00 ~ 익일 06:00시 사이에 등록가능합니다.");
							sheet2.SetCellValue(i, "planSdate", "");
							return;
						}
						if((ymd == eymd && e < "2200") || (ymd != eymd && e > "0600")){
							alert("야간근무시간은 22:00 ~ 익일 06:00시 사이에 등록가능합니다.");
							sheet2.SetCellValue(i, "planEdate", "");
							return;
						}
	        		}
        		}
        	}
        	
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleEmp/save/caldays", $("#sheetForm").serialize()); break;
			break;
			
		case "Insert":
			/*
			for(var i=sheet2.HeaderRows(); i < sheet2.RowCount()+sheet2.HeaderRows(); i++){
 	            if(sheet2.GetCellValue(i, "timeTypeCd") == "BASE") {
	            	alert("관리자는 기본 근무만 추가할 수 있습니다. 이미 해당일에 기본근무가 존재합니다.");
	            	return;
 	            }
 	            if(sheet2.GetCellValue(i, "taaCd") == "") {
	            	alert("근태코드로 인해 기본 근무를 추가할 수 없습니다.");
	            	return;
 	            }
            }
            
            
			sheet2.SetCellValue(row, "timeTypeCd", "BASE");
			*/
			var row = sheet2.DataInsert(0);
			sheet2.SetCellValue(row, "sabun", sheet1.GetCellValue( sheet1.GetSelectRow(), "sabun"));
			sheet2.SetCellValue(row, "ymd", sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd"));
			sheet2.SetCellValue(row, "timeCdMgrId", sheet1.GetCellValue( sheet1.GetSelectRow(), "timeCdMgrId"));
			break;
			
		case "finishDay":			
			finishDayVue.showRequest();			
		}
	}
	
	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		sheet1.SetSelectRow(curRow);
		$('#requestModal').modal("hide");
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
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
			
			$("#fsYmd").val(moment(sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd")).format('YYYY-MM-DD'));
			$("#feYmd").val(moment(sheet1.GetCellValue( sheet1.GetSelectRow(), "ymd")).format('YYYY-MM-DD'));
			
			//20200228 추가
			var workCloseYn = sheet1.GetCellValue( NewRow, "workCloseYn");
			if( workCloseYn == "Y" ){
				$("#optionBtn").hide();
			} else {
				$("#optionBtn").show();
			}
			this.finishDayVue.getFlexibleEmpStd();
		}					
	}
	
	//셀 값변경 이벤트
	function sheet2_OnChange(Row, Col, Value) {
		var colNm = sheet2.ColSaveName(Col);
		if(colNm == "timeTypeCd"){
			// 근무시간구분 선택시 체크(기본근무, 연장근무, 야간근무만 입력할수 있음)
			if(Value != "BASE" && Value != "OT" && Value != "NIGHT"){
				sheet2.SetCellValue(Row, Col, "", 0);
				alert("근무시간 변경은 기본근무/연장근무/야간근무만 등록 가능합니다.");
			}
		}
	}
	
</script>