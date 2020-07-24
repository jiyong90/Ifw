<div>
 	<div class="container-fluid bg-white mgr-wrap">
 		<div id="compModal">
 		<!-- 보상휴가신청 modal start -->
		<div class="modal fade" id="compRequestModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
	        <div class="modal-dialog modal-lg" role="document">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h5 class="modal-title">보상휴가신청</h5>
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                </div>
	                <div class="modal-body">
	                    <form class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                        	<div class="inner-wrap position-relative row">
			                	<div class="loading-spinner" style="display:none;"></div>
			                	<div class="col-4 col-sm-3 col-md-3 col-lg-2">
				                	<p class="page-sub-title mb-0">신청가능시간</p>
				                    <span class="time-wrap">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(0, 'detail')}}</span> 
				                    </span>
			                    </div>
			                    <hr class="separate-bar">
		                    </div>
                            <div class="inner-wrap">
                                <div class="desc row">
	                                <div class="form-group col-12">
	                                    <label for="otReason">근태</label>
										<select id="taaCd" class="form-control" >
										</select>
	                                </div>
                                    <div class="col-sm-12 col-md-12 col-lg-2 pr-lg-0">
                                        <div class="title" id="overtime">보상휴가시간</div>
                                        <span class="time-wrap">
                                            <i class="fas fa-clock"></i>
                                            <span class="time" >{{minuteToHHMM(0,'detail')}}</span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sDate" data-toggle="datetimepicker" data-target="#sDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sTime" data-toggle="datetimepicker" data-target="#sTime" autocomplete="off" required>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eDate" data-toggle="datetimepicker" data-target="#eDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <div class="col col-md col-lg" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eTime" data-toggle="datetimepicker" data-target="#eTime" autocomplete="off" required>
                                            </div>
                                        </div>
                                        <!-- <div class="guide" v-if="overtime.breakMinute && overtime.breakMinute!=0">*해당일 총 휴게시간은 {{minuteToHHMM(overtime.breakMinute,'detail')}} 입니다.</div>
                                        <div class="guide" v-if="targets != null && targets['${empNo}'] != null && targets['${empNo}'].guideMessage != null">{{targets['${empNo}'].guideMessage}}</div> -->
                                    </div>
                                </div>
                            </div>
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="otReason">사유</label>
                                    <textarea class="form-control" id="otReason" rows="3"
                                        placeholder="팀장 확인 시에 필요합니다." required></textarea>
                                </div>
                            </div> 
                        </div> 
                    </form>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	                    <button type="button" id="apprBtn" class="btn btn-default" @click="apply">신청</button>
	                </div>
	            </div>
	        </div>
	    </div>
	    <!-- 결재의견 modal end -->
	    </div>
	 	<div class="ibsheet-wrapper">
	 		<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
					<table>
					<tr>
						<td>
						    <span class="magnifier"><i class="fas fa-search"></i></span>
						    <!-- <span class="search-title">Search</span> -->
						</td>
						<td>
							<span class="label">기준일</span>
							<input type="text" id="ymd" name="ymd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
						</td>
						<!-- <td>
							<span class="label">사번/성명</span>
							<input id="searchKeyword"  name="searchKeyword"  type="text" class="text" />
						</td> -->
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
							<div class="float-left title">보상휴가 사용내역</div>
								<ul class="float-right btn-wrap">
									<!-- li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li -->
									<!-- <li><a href="javascript:doAction1('Down2Excel')" class="basic authR">다운로드</a></li> -->
									<li><a href="javascript:showCompRequestPopup()" class="basic authR">보상휴가신청</a></li>
								</ul>
							</div>
						</div>
						<script type="text/javascript">createIBSheet("sheet1", "100%", fullsheetH,"kr"); </script>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript">
	var compModalVue = new Vue({
   		el: "#compModal",
   		data : {
   			a : ""
   		},
	    mounted: function(){
	    	$("#ymd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    	
	    	this.getRestMinute();
	    },
	    methods : {
	    	showCompRequestPopup : function(){
	    		$('#compRequestModal').modal("show");
	    	},
	    	apply : function(){
	    		
	    	},
	    	getRestMinute : function() { //잔여 연장근로시간 조회
         		var $this = this;

        		$(".loading-spinner").show();
        		
        		var baseYmd = $("#ymd").val().replace(/-/gi,"");
        		
           		var param = {
             			ymd: baseYmd
             		};
            	alert(JSON.stringify(param));
            	//alert(2);
        		Util.ajax({
        			url: "${rc.getContextPath()}/compAppl/info",
        			type: "GET",
        			contentType: 'application/json',
        			dataType: "json",
        			data : param,
        			async: false,
        			success: function(res) {
        				$(".loading-spinner").hide();
        				
        				alert(JSON.stringify(res));
        				if(res!=null) {
        					alert(res.result.restMinute);
        				} 
        			},
        			error: function(e) {
        				$(".loading-spinner").hide();
        				$this.targets = {};
        			}
        		});
        		 
         	}
	    }
	});
	
   	$(function() {
   		$('#sDate, #eDate, #ymd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		
        $('#sTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#eTime').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        
        
        
        
        /*
   		$('#sYmd, #eYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
   		$("#sYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
	    $("#eYmd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
        */
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			//{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
			{Header:"상태",		Type:"Status",		Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workCalendarId",	KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근태코드",  	Type:"Combo",     	Hidden:0,   Width:70,  	Align:"Center",  ColMerge:0, SaveName:"taaCd",   	 KeyField:0,    Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:100  },
			{Header:"시작일",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compSymd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compEymd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사용시간",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compMinute",				KeyField:0,	Format:"Ymd",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사유",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"reason",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"취소여부",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"cancleYn",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"취소신청",	Type:"Html",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"retire",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
		]; 
		
		//근태코드
		var taaCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/taaCode/list", "",false).DATA, "");
		sheet1.SetColProperty("taaCd", {ComboText:"|"+taaCdList[0], ComboCode:"|"+taaCdList[1]} );
		
		$('select#taaCd').html(taaCdList[2]);
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);	//sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(8);

		//근무제도
		//var flexibleList = stfConvCode(ajaxCall("${rc.getContextPath()}/flexibleStd/all", "",false).DATA, "");
		//sheet1.SetColProperty("flexibleStdMgrId", {ComboText:flexibleList[0], ComboCode:flexibleList[1]} );
		
		//근무시간
		//var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=",false).DATA, "");
		// var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/list", "",false).DATA, "");
		//sheet1.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );
		
		//타각구분
		//var entryTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "ENTRY_TYPE_CD"), "");
		//sheet1.SetColProperty("entryStypeCd", {ComboText:entryTypeCdList[0], ComboCode:entryTypeCdList[1]} );
		//sheet1.SetColProperty("entryEtypeCd", {ComboText:entryTypeCdList[0], ComboCode:entryTypeCdList[1]} );
		/*
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
			{Header:"인정근무시간",		Type:"Int",	      	Hidden:0,	Width:80,	Align:"Center",	 ColMerge:0, SaveName:"apprMinute",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"비고",			Type:"Text",	  	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
        ];


        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);

		//시간구분
		var timeTypeCdList = convCode(codeList("${rc.getContextPath()}/code/list", "TIME_TYPE_CD"), ""); 
        sheet2.SetColProperty("timeTypeCd", {ComboText:"|"+timeTypeCdList[0], ComboCode:"|"+timeTypeCdList[1]} );
        
		//근태코드
		var taaCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/taaCode/list", "",false).DATA, "");
		sheet2.SetColProperty("taaCd", {ComboText:"|"+taaCdList[0], ComboCode:"|"+taaCdList[1]} );
		*/
		sheetInit();
		doAction1("Search");
	});

   	
   	function showCompRequestPopup(){
		$('#compRequestModal').modal("show");
   	}
   	
   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/compAppl/list" , $("#sheetForm").serialize());
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
			//sheet2.RemoveAll();
			//doAction2('Search');
		}
	}
	 

   	//날짜,시간 변경 시 근로시간 계산
    $('#sDate, #eDate, #sTime, #eTime').off("change.datetimepicker").on("change.datetimepicker", function(e){
    	
    	//시작일자 변경될 때만 휴일여부 조회
		if($(this).get(0) === $("#sDate").get(0)) {
			//compModalVue.getRestOtMinute();
		}
    	
   		if($("#sDate").val()!='' && $("#eDate").val()!='' && $("#sTime").val()!='' && $("#eTime").val()!='') {
   			var sTime = $("#sTime").val().replace(/:/gi,"");
   			var eTime = $("#eTime").val().replace(/:/gi,"");
   			//restWorkMinute
   			/*
       		compModalVue.overtime = compModalVue.calcMinute(moment(compModalVue.workday).format('YYYYMMDD'), sTime, eTime);
   			if(compModalVue.targets['${empNo}'] != null && compModalVue.targets['${empNo}'].restWorkMinute != null){
   		
   				console.log(compModalVue.targets['${empNo}']);
   				console.log(compModalVue.overtime);
	       		if( compModalVue.targets['${empNo}'].restWorkMinute >= compModalVue.overtime.calcMinute){
	       			compModalVue.isOtUse = false;
	       		}else{
	       			//잔여소정근로시간보다 근무시간이 더 클 경우 연장근로시간에 대해 대체휴가 탭을 활성화 한다. 
	       			compModalVue.otTime = compModalVue.overtime.calcMinute - compModalVue.targets['${empNo}'].restWorkMinute;
	       		}
   			}else{
   				compModalVue.otTime = compModalVue.overtime.calcMinute;
   			}
   			*/
       		
   		}
    });
   	
   	
</script>