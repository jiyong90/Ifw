<div>
	<#include "/applLineComponent.ftl">
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
	                <form id="requestData" class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                        	<div class="inner-wrap position-relative row">
			                	<div class="loading-spinner" style="display:none;"></div>
			                	<div class="col-12 col-sm-12 col-md-12">
				                	<p class="page-sub-title mb-0">신청가능시간</p>
				                    <span class="time-wrap">
				                        <i class="fas fa-clock"></i><span class="time point">{{minuteToHHMM(comptime.REST_MINUTE, 'detail')}}</span> 
				                    </span>
			                    </div>
			                    <hr class="separate-bar">
		                    </div>

                            <div class="inner-wrap">
                                <div class="desc row">
	                                <div class="form-group col-12">
	                                    <label class="title" for="otReason">근태</label>
										<select id="taaCd" class="form-control" >
										</select>
	                                </div>
                                </div>
                            </div>

                            <div class="inner-wrap">
                                <div class="desc row">
	                                <div class="col-sm-12 col-md-12 col-lg-2 pr-lg-0">
                                        <div class="title" id="overtime">보상휴가시간</div>
                                        <span class="time-wrap">
                                            <i class="fas fa-clock"></i>
                                            <span class="time" >{{minuteToHHMM(useTime,'detail')}}</span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="sDate" data-toggle="datetimepicker" data-target="#sDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="eDate" data-toggle="datetimepicker" data-target="#eDate" placeholder="연도-월-일" autocomplete="off" required>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="reason">사유</label>
                                    <textarea class="form-control" id="reason" rows="3"
                                        placeholder="팀장 확인 시에 필요합니다." required></textarea>
                                </div>
                            </div> 
                            <appl-line :bind-data="applLine"></appl-line>
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
	    <!-- 보상휴가신청 modal end  -->
	    
	    <!-- 보상휴가취소신청 modal start -->
		<div class="modal fade" id="compCancelModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false">
	        <div class="modal-dialog modal-lg" role="document">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h5 class="modal-title">보상휴가 취소신청</h5>
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                </div>
	                <div class="modal-body">
	                <form id="cancelData" class="needs-validation" novalidate>
                        <div class="modal-app-wrap">
                            <div class="inner-wrap">
                                <div class="desc row">
	                                <div class="form-group col-12">
	                                    <label class="title" for="otReason">근태</label>
										<select id="can_taaCd" class="form-control" disabled="disabled" >
										</select>
	                                </div>
                                </div>
                            </div>
                            <div class="inner-wrap">
                                <div class="desc row">
	                                <div class="col-sm-12 col-md-12 col-lg-2 pr-lg-0">
                                        <div class="title" id="overtime">보상휴가시간</div>
                                        <span class="time-wrap">
                                            <i class="fas fa-clock"></i>
                                            <span class="time" >{{minuteToHHMM(can_useTime,'detail')}}</span>
                                        </span>
                                    </div>
                                    <div class="col-sm-12 col-md-12 col-lg-10 mt-2 mt-lg-3 xl-mt-3 ">
                                        <div class="form-row">
                                            <div class="d-sm-none d-lg-block ml-md-auto"></div>
                                            <div class="col" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="can_sDate" data-toggle="datetimepicker" data-target="can_sDate" placeholder="연도-월-일" autocomplete="off" required readonly="readonly">
                                            </div>
                                            <span class="d-sm-block d-md-block d-lg-inline-block text-center pl-2 pr-2 mt-1">~</span>
                                            <div class="col" data-target-input="nearest">
                                                <input type="text" class="form-control datetimepicker-input form-control-sm mr-2" id="can_eDate" data-toggle="datetimepicker" data-target="can_eDate" placeholder="연도-월-일" autocomplete="off" required readonly="readonly">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="form-row no-gutters">
                                <div class="form-group col-12">
                                    <label for="can_reason">취소사유</label>
                                    <textarea class="form-control" id="can_reason" rows="3"
                                        placeholder="" required></textarea>
                                </div>
                            </div> 
                            <appl-line :bind-data="applLine"></appl-line>
                        </div> 
                    </form>
	                </div>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
	                    <button type="button" id="apprBtn" class="btn btn-default" @click="cancel">신청</button>
	                </div>
	            </div>
	        </div>
	    </div>
	    <!-- 보상휴가신청 modal end  -->
	    
	    </div>
	 	<div class="ibsheet-wrapper">
	 		<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
						<table>
							<tr>
								<td>
								    <span class="magnifier"><i class="fas fa-search"></i></span>
								</td>
								<td>
									<span class="label">기준일</span>
									<input type="text" id="ymd" name="ymd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#ymd" placeholder="연도-월-일" autocomplete="off"/>
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
							<div class="float-left title" id="title1">보상휴가 신청가능시간</div>
			                    <ul class="float-right btn-wrap">
									<li><a href="javascript:showCompRequestPopup()" class="basic authR">보상휴가신청</a></li>
								</ul>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="inner">
							<div class="sheet_title_wrap clearfix">
							<div class="float-left title">보상휴가 사용내역</div>
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
   		components : {
			'appl-line': applLine
	    },
   		data : {
   			holidayYn: 'N',
	    	workday: '${today}', //근무일
	    	reasons: [], //연장/휴일 근로 사유
	    	subYmds: [], //대체휴일
	    	comptime: {}, //보상휴가 시간
	    	comptimeAppl: {},
	    	applCode: {}, //신청서 정보
	    	targetList: [],
	    	applSabuns: {},
	    	checklist: {},
	    	targets: {},
	    	applLine: [], 
	    	useTime: '0',
	    	can_useTime: '0',
	    	compApplId : null
   		},
	    mounted: function(){
	    	var $this = this;
	    	this.getPossibleUseTime();
	    },
	    methods : {
	    	showCompRequestPopup : function(){
	    		$('#compRequestModal').modal("show");
	    	},
	    	apply : function(){  //신청

	    		var $this = this;

	    		var sDate = $('#sDate').val().replace(/-/gi,"");
	    		var eDate = $('#eDate').val().replace(/-/gi,"");

	    		if(sDate != "" && eDate != "" && sDate > eDate) {
	    			$("#alertText").html("시작일이 종료일보다 클 수 없습니다.");
	           		$("#alertModal").on('hidden.bs.modal',function(){
	           			$("#alertModal").off('hidden.bs.modal');
	           		});
	           		$("#alertModal").modal("show"); 
	    			$('#sDate').val("");
	    			$('#eDate').val("");
	    			return;
	    		}

	    		if($this.comptime.REST_MINUTE == 0) {
	    			$("#alertText").html("신청가능한 보상휴가 시간이 없습니다.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         		});
  	         		$("#alertModal").modal("show"); 
  	         		return;
			    }
			    
	    		if($this.useTime == 0) {
	    			$("#alertText").html("계획된 근무일이 없습니다.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         		});
  	         		$("#alertModal").modal("show"); 
  	         		return;
			    }

	    		if($this.useTime > $this.comptime.REST_MINUTE) {
	    			$("#alertText").html("신청가능 시간을 초과 하였습니다.");
  	         		$("#alertModal").on('hidden.bs.modal',function(){
  	         			$("#alertModal").off('hidden.bs.modal');
  	         		});
  	         		$("#alertModal").modal("show"); 
  	         		return;
			    }
	    		var param = {
	    			workTypeCd : 'COMP',
	    			taaCd : $("#taaCd").val(),
	    			sDate : $("#sDate").val().replace(/-/gi,""),
		   			eDate : $("#eDate").val().replace(/-/gi,""),
		   			reason: $("#reason").val()
		 		};
		 		
				console.log(param);
				$.ajax({
					url: "${rc.getContextPath()}/compAppl/request",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						console.log(data.DATA);
						if(data!=null) {
							if(data.status == "OK") {
								$("#alertText").html("신청되었습니다.");
			  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	         			$("#alertModal").off('hidden.bs.modal');
			  	         		});
			  	         		$("#alertModal").modal("show");
			  	         		$('#compRequestModal').modal("hide"); 
								doAction1("Search");
							} else if(data.status == "FAIL") {
								$("#alertText").html("보상휴가 신청시 오류가 발생하였습니다.");
			  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	         			$("#alertModal").off('hidden.bs.modal');
			  	         		});
			  	         		$("#alertModal").modal("show"); 
			  	         		return;
							}
							
							
		  	         		return;
						}
						
					},
					error: function(e) {
						console.log(e);
						$("#alertText").html("보상휴가 신청시 오류가 발생하였습니다.");
	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	         			$("#alertModal").off('hidden.bs.modal');
	  	         		});
	  	         		$("#alertModal").modal("show"); 
	  	         		return;
					}
				}); 
	    	},
	    	getApplLine: function(applCd) { //결재라인 정보
	       		var $this = this;
	       		var param = {
						applCd: applCd
					};
					
					//결재라인
					Util.ajax({
						url: "${rc.getContextPath()}/appl/line",
						type: "GET",
						contentType: 'application/json',
						data: param,
						dataType: "json",
						success: function(data) {
							if(data!=null && data.status=='OK') {
								$this.applLine = data.applLine;
							}
							
						},
						error: function(e) {
							console.log(e);
							$this.applLine = [];
						}
					});
				return applLine;
	       	},
	       	getPossibleUseTime : function() {
	       		var $this = this;
	       		var param = {};
	       		Util.ajax({
					url: "${rc.getContextPath()}/compAppl/getPossibleUseTime",
					type: "POST",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						if(data!=null && data.status=='OK') {
							$this.comptime = data.comptime;
							var title = "보상휴가 신청가능시간 : <font color='red'>"+minuteToHHMM($this.comptime.REST_MINUTE, 'detail')+"</font>";
							$("#title1").html(title);
						}
						
					},
					error: function(e) {
						console.log(e);
						$this.comptime = {};
					}
				});
			},
			getApplCode: function(){ //신청서 정보
				var $this = this;
	    		
				var param = {
   		    		applCd : 'COMP'
   		    	};
		    		
		    	Util.ajax({
					url: "${rc.getContextPath()}/appl/code",
					type: "GET",
					contentType: 'application/json',
					data: param,
					dataType: "json",
					success: function(data) {
						$this.applCode = {};
						if(data!=null) {
							$this.applCode = data;
						}
					},
					error: function(e) {
						$this.applCode = {};
					}
				});
         	},
         	getWorkDay: function(taaCd, sDate, eDate){
	    		var $this = this;
				var param = {
						sDate : sDate,
		    			eDate : eDate
			 		};
					console.log(param);
			    	Util.ajax({
						url: "${rc.getContextPath()}/compAppl/getWorkDay",
						type: "POST",
						contentType: 'application/json',
						data: JSON.stringify(param),
						dataType: "json",
						async: false,
						success: function(data) {
							console.log(data);					
							if(data!=null) {
								console.log(data);
								if(data.workDayMap.WORK_DAY != 0) {
									if(taaCd == "COMP_D") {
										$this.useTime = 8 * data.workDayMap.WORK_DAY * 60;
									} else if(taaCd == "COMP_A" || taaCd == "COMP_P") {
										$this.useTime = 4 * data.workDayMap.WORK_DAY * 60;
									} else {
										$this.useTime = 0;
									}
								}
								
							} else {
								alert("보상휴가 신청시 오류가 발생하였습니다.");
							}
							
						},
						error: function(e) {
							console.log(e);
							alert("보상휴가 신청시 오류가 발생하였습니다.");
						}
					});
	    	},
	    	showCompCancelPopup : function(){
	    		$('#compCancelModal').modal("show");
	    	},
	    	cancel : function(){  //취소신청

	    		var $this = this;

	    		var row = sheet1.GetSelectionRows($this.compApplId);
	    		var status;
	    		if(row > 0){
	    			var rowData = sheet1.GetRowData(row);
	    			status = rowData.status;
	    		}

	    		var param = {
	    			workTypeCd : 'COMP_CAN',
	    			taaCd     : $("#can_taaCd").val(),
	    			sDate     : $("#can_sDate").val().replace(/-/gi,""),
		   			eDate     : $("#can_eDate").val().replace(/-/gi,""),
		   			reason    : $("#can_reason").val(),
		   			compApplId: $this.compApplId,
		   			status    : status
		 		};
		 		
				console.log(param);
				$.ajax({
					url: "${rc.getContextPath()}/compCanAppl/request",
					type: "POST",
					contentType: 'application/json',
					data: JSON.stringify(param),
					dataType: "json",
					success: function(data) {
						console.log(data.DATA);
						if(data!=null) {
							if(data.status == "OK") {
								$("#alertText").html("신청되었습니다.");
			  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	         			$("#alertModal").off('hidden.bs.modal');
			  	         		});
			  	         		$("#alertModal").modal("show");
			  	         		$('#compCancelModal').modal("hide"); 
								doAction1("Search");
							} else if(data.status == "FAIL") {
								$("#alertText").html("보상휴가취소 신청시 오류가 발생하였습니다.");
			  	         		$("#alertModal").on('hidden.bs.modal',function(){
			  	         			$("#alertModal").off('hidden.bs.modal');
			  	         		});
			  	         		$("#alertModal").modal("show"); 
			  	         		return;
							}
							
							
		  	         		return;
						}
						
					},
					error: function(e) {
						console.log(e);
						$("#alertText").html("보상휴가취소 신청시 오류가 발생하였습니다.");
	  	         		$("#alertModal").on('hidden.bs.modal',function(){
	  	         			$("#alertModal").off('hidden.bs.modal');
	  	         		});
	  	         		$("#alertModal").modal("show"); 
	  	         		return;
					}
				}); 
	    	}
	    }
	});
	    
	
   	$(function() {
   		
        $('#ymd, #sDate, #eDate,#can_sDate, #can_eDate').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko',
            useCurrent: false
        });
        
        $("#ymd").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
        $("#sDate").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
        $("#eDate").val("${today?date("yyyy-MM-dd")?string("yyyy-MM-dd")}");
		var initdata1 = {};
		
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",		Type:"Seq",			Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"근태코드",  	Type:"Combo",     	Hidden:0,   Width:70,  	Align:"Center", ColMerge:0, SaveName:"taaCd",      KeyField:0,  Format:"",    	PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:100 },
			{Header:"보상신청번호",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compApplId", KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"시작일",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compSymd",   KeyField:0,	Format:"Ymd",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"종료일",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compEymd",   KeyField:0,	Format:"Ymd",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사용시간",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"compMinute", KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"사유",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"reason",	   KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"취소여부",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"cancleYn",   KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"취소신청",	Type:"Html",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"endImg",	   KeyField:0,	Format:"",	    PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"상태코드",	Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"status",     KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
		]; 
		
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);	//sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		sheet1.SetCountPosition(8);

		//근태코드
		var params = {searchKeyword:"COMP"};  //보상휴가 코드만 조회
		var taaCdList = stfConvCode(ajaxCall("${rc.getContextPath()}/taaCode/list", params, false).DATA, "");
		// sheet1.SetColProperty("taaCd", {ComboText:"|"+taaCdList[0], ComboCode:"|"+taaCdList[1]} );
		$('select#taaCd').html(taaCdList[2]);
		$('select#can_taaCd').html(taaCdList[2]);
		sheet1.SetColProperty("taaCd", {ComboText:taaCdList[0], ComboCode:taaCdList[1]} );
		
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
			compModalVue.getApplLine('COMP');
			break;
		}
	}
 
	// 조회 후 에러 메시지
	function sheet1_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			} else {
				compModalVue.getPossibleUseTime();
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
   	
   	//날짜,시간 변경 시 근로시간 계산
    $('#sDate, #eDate').off("change.datetimepicker").on("change.datetimepicker", function(e){
 		setDateStatus();
    });

    $('select#taaCd').change(function() {
    	setDateStatus();
    });
    

    function setDateStatus() {
    	var taaCd = $('select#taaCd').val();
		var sDate = $('#sDate').val().replace(/-/gi,"");
		var eDate = $('#eDate').val().replace(/-/gi,"");

		if(taaCd == "COMP_A" || taaCd == "COMP_P") {
        	$('#eDate').attr("readonly",true);

        	if(sDate != "" && eDate == "") {
        		$('#eDate').val($('#sDate').val());
       		} else if(sDate == "" && eDate != "") {
        		$('#sDate').val($('#eDate').val());
       		} else if(sDate != "" && eDate != "") {
        		$('#eDate').val($('#sDate').val());
       		}
        } else if(taaCd == "COMP_D") {
			$("#eDate").attr("readonly",false); 
        }

    	sDate = $('#sDate').val().replace(/-/gi,"");
		eDate = $('#eDate').val().replace(/-/gi,"");

		
    	if(sDate != "" && eDate != "" && sDate <= eDate) {
    		compModalVue.getWorkDay(taaCd, sDate, eDate);
    	}else if(sDate != "" && eDate != "" && sDate > eDate) {
    		compModalVue.useTime = 0;
    	}  
    }

	function setCompCancel(compApplId) {
		var row = sheet1.GetSelectionRows();
		if(row > 0){
			var rowData = sheet1.GetRowData(row);
			if(rowData.cancleYn == "N") {
				$('#can_sDate').val(DateToString(StringToDate(rowData.compSymd), 'yyyy-MM-dd'));
				$('#can_eDate').val(DateToString(StringToDate(rowData.compEymd), 'yyyy-MM-dd'));
				$('#can_reason').val("");
				$('#can_taaCd').val(rowData.taaCd);
				this.compModalVue.can_useTime = rowData.compMinute;				
				this.compModalVue.compApplId = compApplId;				

				compModalVue.showCompCancelPopup();
			}
		}
	}
	
</script>