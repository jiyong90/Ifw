<div class="container-fluid bg-white mgr-wrap except70 overflow-hidden">
 	<p class="page-title mb-2"><i class="fas fa-poll-h"></i>근무 시간 변경</p>
 	<div class="row">
 		<div id="workPlanChangeMgr" class="col-4" v-cloak>
 			<div class="function-list-wrap">
 				<div class="title">근무일 선택</div>
	            <div class="col col-md-3 col-lg-3" data-target-input="nearest">
	                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="ymd" data-toggle="datetimepicker" data-target="#ymd" placeholder="연도-월-일" autocomplete="off" required>
	            </div>
            </div>
 			<form>
 				<div class="input-group mb-2">
                     <input id="searchKeyword" type="text" class="form-control rounded-0" placeholder="검색어를 입력해주세요" aria-label="검색어를 입력해주세요"
                         aria-describedby="search" @keyup.enter="getTargetList">
                     <div class="input-group-append">
                         <button class="btn btn-primary rounded-0" type="button" @click="getTargetList"><i class="fas fa-search"></i></button>
                     </div>
                 </div>
            </form>
            <div class="inner-wrap">
                <div class="function-list-wrap form-element">
                    <div class="title">
                    	<input type="checkbox" id="allChk" name="allTarget" value="" @click="allCheck($event.target.checked)">
                    	<label for="allChk">대상자 선택</label>
                    </div>
                    <ul class="fun-list over-height">
                        <li v-for="t in targetList">
                            <input type="checkbox" :id="t.sabun" name="targetChks" value="" @click="checkTarget(t,$event)" :checked="isCheck(t.sabun)">
                            <label :for="t.sabun">
                                <span class="group">{{t.orgNm}}</span>
                                <span class="num">{{t.sabun}}</span>
                                <span class="name">{{t.empNm}}</span>
                            </label>
                        </li>
                    </ul>
                </div>
            </div>
		</div>
		<div class="col-8">
			<table border="0" cellspacing="0" cellpadding="0" class="sheet_main">
				<tr>
					<td>
						<div class="inner">
							<div class="sheet_title_wrap clearfix">
								<div id="popupTitle" class="float-left">근무 계획</div>
							</div>
						</div>
						<script type="text/javascript"> createIBSheet("sheet1", "100%", halfsheetH, "kr"); </script>
					</td>
				</tr>
			</table>
			<div>
				<div class="inner">
					<div class="sheet_title_wrap clearfix">
						<div id="popupTitle" class="float-left">근무 시간 변경</div>
						<select id="timeCd" class="box">
						</select>
					</div>
					<ul class="float-right btn-wrap">
						<li><a href="javascript:workPlanChangeMgrVue.changeWorkPlan()" class="basic authR">변경</a></li>
					</ul>
				</div>
			</div>
		</div>
    </div>
</div>    
<script type="text/javascript">
	$(function () {
		$('#ymd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
		
		var initdata1 = {};
		initdata1.Cfg = {SearchMode:smLazyLoad,MergeSheet:msHeaderOnly,Page:22,FrozenCol:0,DataRowMerge:0};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata1.Cols = [
            {Header:"No|No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제|삭제",			Type:"DelCheck",	Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태|상태",			Type:"Status",		Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
   			{Header:"소속|소속",		Type:"Text",		Hidden:0,	Width:120,	Align:"Left",	ColMerge:0,	SaveName:"orgNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사번|사번",		Type:"Text",		Hidden:0,	Width:80,	Align:"Center",	ColMerge:0,	SaveName:"sabun",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"성명|성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무제도|근무제도",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleNm",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"근무시간|근무시간",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"timeCdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:0,	EditLen:100 },
			{Header:"시간구분|시간구분",		Type:"Combo",		Hidden:0,	Width:70,	Align:"Left",	ColMerge:0,	SaveName:"timeTypeCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
            {Header:"계획|시작시각",		Type:"Text",	 	Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"planSdate", 	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획|종료시각",		Type:"Text",	 	Hidden:0,	Width:120,	Align:"Center",	 ColMerge:0, SaveName:"planEdate",	 KeyField:0,	Format:"YmdHm",	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"계획|근무시간",		Type:"Text",	      	Hidden:0,	Width:60,	Align:"Center",	 ColMerge:0, SaveName:"planMinute",	 KeyField:0,	Format:"", 		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 }
        ];

        IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		//근무시간
		var timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=",false).DATA, "");
		sheet1.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );

		//시간구분
		var timeTypeCdList = convCode(codeList("${rc.getContextPath()}/code/list", "TIME_TYPE_CD"), "선택"); 
        sheet1.SetColProperty("timeTypeCd", {ComboText:"|"+timeTypeCdList[0], ComboCode:"|"+timeTypeCdList[1]} );
       
		sheetInit();
		
		//시간구분
		var timeCd = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=N",false).DATA, "선택"); 
        $("#timeCd").append(timeCd[2]);
		
	});
	
	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			var ymd = moment($("#ymd").val()).format('YYYYMMDD');
			var param = "ymd="+ymd+"&sabuns="+JSON.stringify(workPlanChangeMgrVue.applSabuns)+"&timeCdMgrId="+$("#timeCd").val();
			sheet1.DoSearch( "${rc.getContextPath()}/worktime/change/target" , param);
			break;
		}
	}

   	var workPlanChangeMgrVue = new Vue({
   		el: "#workPlanChangeMgr",
  		    data : {
  		    	targetList: [],
  		    	applSabuns: []
  		    },
  		    watch: {
  		    	applSabuns : function(val, oldVal) {
  		    		var $this = this;
  		    		if($("#ymd").val()!='' && val.length>0) 
  		    			doAction1("Search");
  		    	} 
  		    },
  		    mounted: function(){
  		    	this.getTargetList();
  		    },
  		    methods : {
  		    	getTargetList: function(){ //팀원 조회
  		    		var $this = this;
  		    		
  		    		$("#loading").show();
  		    	
  		    		$("#allChk").prop("checked",false);
  		    		$this.targetList = [];
  		    		
  		    		var searchKeyword = $("#searchKeyword").val();
  		    		
  		    		Util.ajax({
  		    			url: "${rc.getContextPath()}/emp/list?searchKeyword="+searchKeyword,
						type: "POST",
						contentType: 'application/json',
						dataType: "json",
						success: function(data) {
							//console.log(data);
							$("#loading").hide();
							if(data!=null && data.status=='OK') {
								$this.targetList = data.DATA;
							} 
						},
						error: function(e) {
							$("#loading").hide();
							console.log(e);
							$this.targetList = [];
						}
					});
  		    	},
  		    	checkTarget: function(emp, e){
  		    		var $this = this;	 
  		    		
  		    		if(e.target.checked) {
  		    			$this.applSabuns.push(emp.sabun);
  		    		} else {
  		    			
  		    			if($this.applSabuns.indexOf(emp.sabun)!=-1) {
  		    				$this.uncheckTarget(emp.sabun);
  		    			}

  		    		}
  		    	},
  		    	uncheckTarget : function(sabun) {
  		    		this.applSabuns.splice(this.applSabuns.indexOf(sabun), 1);
  		    	},
  		    	isCheck: function(sabun){
  		    		if(this.applSabuns.indexOf(sabun)!=-1) 
  		    			return true;
  		    	},
  		    	allCheck : function(isChecked){ 
  		    		var $this = this;
  					if(isChecked){
  			    		$.each($this.targetList,function(i,obj){
  			    			if($this.applSabuns.indexOf(obj.sabun)==-1) {
  			    				$this.applSabuns.push(obj.sabun);
  			    			}
  			   			});
  			    		
  					}else{
  						$.each($this.targetList,function(i,obj){
  			    			if($this.applSabuns.indexOf(obj.sabun)!=-1)
  			    				$this.uncheckTarget(obj.sabun);
  			   			});
  					}
  		    	},
  		    	getWorkPlanChangeTarget : function(){
  		    		var $this = this;
  		    		if(Object.keys($this.applSabuns).length>0) {
  		    			doAction1("Search");
  		    		} 
  		    	},
  		    	changeWorkPlan: function(){
  		    		var $this = this;
  		    		$("#loading").show();
  		    		
  		    		var isSave = true;
  		    		var msg;
  		    		var ymd = $("#ymd").val();
  		    		var timeCdMgrId = $("#timeCd").val();
  		    		
  		    		if(ymd==null || ymd=='') {
  		    			msg = "근무일을 선택해 주세요.";
  		    			isSave = false;
  		    		} else if($this.applSabuns==null || $this.applSabuns.length==0) {
  		    			msg = "대상자를 선택해 주세요.";
  		    			isSave = false;
  		    		} else if(timeCdMgrId==null || timeCdMgrId=='') {
  		    			msg = "변경할 근무시간을 선택해 주세요.";
  		    			isSave = false;
  		    		} 

  		    		if(isSave){
  		    			var param = {
	  		    			ymd: moment(ymd).format('YYYYMMDD'),
	  		    			timeCdMgrId: timeCdMgrId,
	  		    			sabuns: JSON.stringify($this.applSabuns)
	  		    		};
	  		    		
	  		    		Util.ajax({
	  		    			url: "${rc.getContextPath()}/worktime/change",
							type: "POST",
							contentType: 'application/json',
							dataType: "json",
							data: JSON.stringify(param),
							success: function(data) {
								$("#loading").hide();
								if(data!=null && data.status=='OK') {
									console.log(data);
								} else {
									$("#alertText").html(data.message);
									$("#alertModal").on('hidden.bs.modal',function(){
										$("#alertModal").off('hidden.bs.modal');
									});
									$("#alertModal").modal("show"); 
								}
								
							},
							error: function(e) {
								$("#loading").hide();
								console.log(e);
							}
						});
  		    		} else {
  		    			$("#alertText").html(msg);
						$("#alertModal").on('hidden.bs.modal',function(){
							$("#alertModal").off('hidden.bs.modal');
						});
						$("#alertModal").modal("show"); 
  		    		}
  		    	}
  		    	
  	         }
   	});

   	//날짜,시간 변경 시 근로시간 계산
    $('#ymd').unbind("change.datetimepicker").on("change.datetimepicker", function(e){
   		if($("#ymd").val()!='') {
   			$("#popupTitle").empty();
   			$("#popupTitle").prepend($("#ymd").val()+"의 근무 계획");
   			workPlanChangeMgrVue.getWorkPlanChangeTarget();
   		}
    }); 
	
</script>
