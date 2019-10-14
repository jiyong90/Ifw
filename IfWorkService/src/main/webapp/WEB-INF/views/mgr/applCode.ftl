<div id="applCode">
 	<div class="container-fluid pt-3 pb-3 bg-white" style="calc(100vh - 72px);">
	 	<div class="ibsheet-wrapper">
			<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
						<table>
							<tr>
								<td>
									<a href="javascript:doAction1('Search');" class="button">조회</a>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</form>
			<div class="row no-gutters">
				<div class="col-5 pr-3">
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">신청서코드관리</div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
								<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
							</ul>
						</div>
					</div>
					<script type="text/javascript"> createIBSheet("sheet1", "100%", fullsheetH, "kr"); </script>
				</div>
				<div class="col-5 pr-3">
					<div class="inner">
						<div class="sheet_title_wrap clearfix">
							<div class="float-left title">신청서별 기준</div>
							<ul class="float-right btn-wrap">
								<li><a href="javascript:doAction1('SaveSub')" class="basic authA">저장</a></li>
							</ul>
						</div>
						<div id="view_appl">
							<table class="default">
								<tbody>
									<tr id="trApplMinute">
										<th>신청 시간 단위</th>
										<td>
											<select id="timeUnit">
			                                    <option value="1">1분</option>
			                                    <option value="10">10분</option>
			                                    <option value="30">30분</option>
			                                    <option value="60">60분</option>
			                                </select>
										</td>
										<th>신청 지정 분</th>
										<td>
											<select id="timeUnit">
			                                    <option value="1">1분</option>
			                                    <option value="0">00분</option>
			                                    <option value="10">10분</option>
			                                    <option value="30">30분</option>
			                                </select>
			                                <!--
											<input type="checkbox" id="useMinutes" name="useMinutes" value="0" title="00분"/> 00분
											<input type="checkbox" id="useMinutes" name="useMinutes" value="10" title="10분"/> 10분
											<input type="checkbox" id="useMinutes" name="useMinutes" value="30" title="30분"/> 30분
											-->
										</td>
									</tr>
									<tr id="trApplTime">
										<th>신청 가능 시각</th>
										<td colspan="3">
											<input type="text" id="inShm" name="inShm" class="required" />
											~
											<input type="text" id="inEhm" name="inEhm" class="required" />
										</td>
									</tr>
									<tr id="trHol">
										<th>휴일대체 사용여부</th>
										<td>
											<select id="subsYn">
			                                    <option value="Y">사용</option>
			                                    <option value="N">사용안함</option>
			                                </select>
										</td>
										<th>휴일대체 선택대상</th>
										<td>
											<select id="subsRuleId">
			                                    <option value="">사용안함</option>
			                                </select>
										</td>
									</tr>
									<tr id="trSubs">
										<th>휴일대체 사용기간<br>(근무일이전)</th>
										<td>
											<input type="text" id="subsSday" name="subsSday" class="required"/>일
										</td>
										<th>휴일대체 사용기간<br>(근무일이후)</th>
										<td>
											<input type="text" id="subsEday" name="subsEday" class="required"/>일
										</td>
									</tr>
									<tr>
										<th>비고</th>
										<td colspan="3">
											<textarea id="note" cols=80 rows=3>
											</textarea>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
   	$(function() {
   		//resize
   		$(window).smartresize(sheetResize);
   		// 일단 숨기고시작하기
   		$("#trApplMinute").hide();
		$("#trApplTime").hide();
		$("#trHol").hide();
   		
		var initdata1 = {};
		initdata1.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata1.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};

		initdata1.Cols = [
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:1,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"applCodeId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",		Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"신청서코드",		Type:"Text",		Hidden:0,	Width:90,	Align:"Center",	ColMerge:0,	SaveName:"applCd",			KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:50 },
   			{Header:"신청서명",		Type:"Text",		Hidden:0,	Width:70,	Align:"Left",	ColMerge:0,	SaveName:"applNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
   			{Header:"결재단계",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"applLevelCd",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
   			{Header:"수신단계",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"recLevelCd",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:1,  InsertEdit:1,  EditLen:100  },
   			{Header:"신청시간단위",  	Type:"Text",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"timeUnit",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:50  },
   			{Header:"신청사용분",  	Type:"Text",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"useMinutes",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:2000  },
   			{Header:"입력시작시각",  	Type:"Text",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"inShm",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:4  },
   			{Header:"입력종료시각",  	Type:"Text",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"inEhm",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:4  },
   			{Header:"대휴사용여부",  	Type:"Text",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"subsYn",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:1  },
   			{Header:"대휴선택대상",  	Type:"Int",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"subsRuleId",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:20  },
   			{Header:"대휴사용시작",  	Type:"Int",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"subsSday",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:5  },
   			{Header:"대휴사용종료",  	Type:"Int",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"subsEday",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:5  },
   			{Header:"비고",  			Type:"Text",     	Hidden:1,   Width:10,  Align:"Center",  ColMerge:0, SaveName:"note",  	KeyField:0, Format:"",    	PointCount:0,  UpdateEdit:0,  InsertEdit:0,  EditLen:2000  }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		var levelCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "LEVEL_CD"), "선택");
		sheet1.SetColProperty("applLevelCd", {ComboText:"없음|"+levelCdList[0], ComboCode:"|"+levelCdList[1]} );
		sheet1.SetColProperty("recLevelCd", {ComboText:"없음|"+levelCdList[0], ComboCode:"|"+levelCdList[1]} );

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/applCode/list" , $("#sheetForm").serialize());
			break;
		case "Save":
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave( "${rc.getContextPath()}/applCode/save" , $("#sheetForm").serialize());
			break;
		case "SaveSub":
			// 마스터값을 sheet로 셋팅후 저장하기
			var row = sheet1.GetSelectRow();
			if($('#trApplMinute').is(':visible')){
				var useMinutesArr=new Array();
				$('input[name="useMinutes"]').each(function() {
				    if(this.checked){
				    	var objUseMinute = new Object();
						objUseMinute.lable = this.title;
						objUseMinute.value = this.value;
						useMinutesArr.push(objUseMinute);
				    }
				    
				});
				var useMinutes = JSON.stringify(useMinutesArr);//json으로 바꿈
				sheet1.SetCellValue(row, "timeUnit", $("#timeUnit").val());
				sheet1.SetCellValue(row, "useMinutes", useMinutes);
			}
			if($('#trApplTime').is(':visible')){
				sheet1.SetCellValue(row, "inShm", $("#inShm").val());
				sheet1.SetCellValue(row, "inEhm", $("#inEhm").val());
			}
			if($('#trHol').is(':visible')){
				sheet1.SetCellValue(row, "subsYn", $("#subsYn").val());
				sheet1.SetCellValue(row, "subsRuleId", $("#subsRuleId").val());
			}
			if($('#trSubs').is(':visible')){
				sheet1.SetCellValue(row, "subsSday", $("#subsSday").val());
				sheet1.SetCellValue(row, "subsEday", $("#subsEday").val());
			}
			sheet1.SetCellValue(row, "note", $("#note").val());
			doAction1("Save");
			
			break;
		}
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
	
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(OldRow != NewRow && sheet1.GetCellValue( NewRow, "sStatus") != "I"){
			var applCd = sheet1.GetCellValue( NewRow, "applCd");
			// 옵션항목 보이고, 숨기기 처리
			if(applCd == "OT"){
				// 연장/휴일근무신청서
				$("#trApplMinute").show();
				$("#trApplTime").show();
				$("#trHol").show();
				$("#trSubs").show();
			} else if(applCd == "SUBS_CHG"){
				// 연장/휴일근무신청서
				$("#trApplMinute").hide();
				$("#trApplTime").hide();
				$("#trHol").hide();
				$("#trSubs").show();
			} else {
				$("#trApplMinute").hide();
				$("#trApplTime").hide();
				$("#trHol").hide();
				$("#trSubs").hide();
			}
			// 데이터 셋팅
			if($('#trApplMinute').is(':visible')){
				$("#timeUnit").val(sheet1.GetCellValue( NewRow, "timeUnit")).prop("selected", true);
				$("input:checkbox[name='useMinutes']").prop("checked", false);
				var useMinutes = sheet1.GetCellValue( NewRow, "useMinutes");
				var dataUseMinutes = JSON.parse(useMinutes);
				for(var i=0; i<dataUseMinutes.length; i++) {
					var objId = "useMinutes" + dataUseMinutes[i].value;
					$("input:checkbox[id="+objId+"]").prop("checked", true);
				}
			}
			if($('#trApplTime').is(':visible')){
				$("#inShm").val(sheet1.GetCellValue( NewRow, "inShm"));
				$("#inEhm").val(sheet1.GetCellValue( NewRow, "inEhm"));
			}
			if($('#trHol').is(':visible')){
				$("#subsYn").val(sheet1.GetCellValue( NewRow, "subsYn")).prop("selected", true);
				getRuleList(NewRow, "subsRuleId");
			}
			if($('#trSubs').is(':visible')){
				$("#subsSday").val(sheet1.GetCellValue( NewRow, "subsSday"));
				$("#subsEday").val(sheet1.GetCellValue( NewRow, "subsEday"));
			}
			$("#note").val(sheet1.GetCellValue( NewRow, "note"));
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
	
	function getRuleList(NewRow, id){
		
		if($('#'+id+' > option').length<=1) {
			Util.ajax({
				url: "${rc.getContextPath()}/rule/list",
				type: "GET",
				contentType: 'application/json',
				dataType: "json",
				success: function(data) {
					if(data!=null && data.status=='OK') {
						var rules = data.DATA;
						var selectOptions="";
						
						$('#'+id).find("option:not(:first)").remove();
						rules.map(function(rule){
							selectOptions += "<option value='"+rule.ruleId+"'>"+rule.ruleNm+"</option>";
						});
						
						$('#'+id).append( selectOptions );
						$("#"+id).val(sheet1.GetCellValue( NewRow, id));
					}
				},
				error: function(e) {
					console.log(e);
				}
			});
		} else{
			$("#"+id).val(sheet1.GetCellValue( NewRow, id));
		}
		
	}
</script>