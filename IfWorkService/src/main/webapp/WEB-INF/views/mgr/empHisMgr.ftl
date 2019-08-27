<div id="empListMgr">
 	<div class="container-fluid pt-3 pb-3 bg-white">
	 	<div class="ibsheet-wrapper">
			<form id="sheetForm" name="sheetForm">
				<div class="sheet_search outer">
					<div>
					<table>
					<tr>
						<td>
							<span>기준일 </span>
							<input type="text" id="sYmd" name="sYmd" class="date2 required" value="${today?date("yyyy-MM-dd")?string("yyyyMMdd")}" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
						</td>
						<td>
							<span>사번/성명</span>
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
								<div class="float-left title">사원정보</div>
								<ul class="float-right btn-wrap">
									<!-- <li><a href="javascript:doAction1('Insert');" class="basic authA">입력</a></li> -->
									<!-- <li><a href="javascript:doAction1('Save');" class="basic authA">저장</a></li> -->
								</ul>
							</div>
						</div>
						<script type="text/javascript">createIBSheet("sheet1", "100%", "100%","kr"); </script>
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
			{Header:"No",		Type:"Seq",	Hidden:Number("0"),	Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
//			{Header:"삭제",		Type:"DelCheck",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
//			{Header:"상태",		Type:"Status",	Hidden:Number("0"),Width:"45",	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"사번",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"sabun",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"성명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empNm",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"영문명",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"empEngNm",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"발령시작일",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"symd",			KeyField:0,	Format:"Ymd",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"발령종료일",	Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"eymd",			KeyField:0,	Format:"Ymd",		PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"재직상태",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"statusCd",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"조직명",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"orgCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"사업장",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"businessPlaceCd",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"직책",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"dutyCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"직위",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"posCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"직급",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"classCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"직군",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"jobGroupCd",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"직무",		Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"jobCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"급여유형",	Type:"Combo",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"payTypeCd",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"조직장여부",	Type:"CheckBox",	Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"leaderYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 },
			{Header:"비고",		Type:"Text",		Hidden:0,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"note",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(false);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);

		//재직상태
		var statusCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "STATUS_CD"), "선택");
		sheet1.SetColProperty("statusCd", {ComboText:statusCdList[0], ComboCode:statusCdList[1]} );

		//조직명
		var orgCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "ORG_CD"), "선택");
		sheet1.SetColProperty("orgCd", {ComboText:orgCdList[0], ComboCode:orgCdList[1]} );

		//사업장
		var businessPlaceCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "BUSINESS_PLACE_CD"), "선택");
		sheet1.SetColProperty("businessPlaceCd", {ComboText:businessPlaceCdList[0], ComboCode:businessPlaceCdList[1]} );

		//직책
		var dutyCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DUTY_CD"), "선택");
		sheet1.SetColProperty("dutyCd", {ComboText:dutyCdList[0], ComboCode:dutyCdList[1]} );

		//직위
		var posCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "POS_CD"), "선택");
		sheet1.SetColProperty("posCd", {ComboText:posCdList[0], ComboCode:posCdList[1]} );

		//직급
		var classCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "CLASS_CD"), "선택");
		sheet1.SetColProperty("classCd", {ComboText:classCdList[0], ComboCode:classCdList[1]} );

		//직군
		var jobGroupCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "JOB_GROUP_CD"), "선택");
		sheet1.SetColProperty("jobGroupCd", {ComboText:jobGroupCdList[0], ComboCode:jobGroupCdList[1]} );

		//직무
		var jobCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "JOB_CD"), "선택");
		sheet1.SetColProperty("jobCd", {ComboText:jobCdList[0], ComboCode:jobCdList[1]} );

		//급여유형
		var payTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "PAY_TYPE_CD"), "선택");
		sheet1.SetColProperty("payTypeCd", {ComboText:payTypeCdList[0], ComboCode:payTypeCdList[1]} );

		sheetInit();
		doAction1("Search");
	});

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/emp/list" , $("#sheetForm").serialize());
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
</script>