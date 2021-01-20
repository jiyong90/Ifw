<div id="flexibleStdMgr">
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
								<span class="label">기준일 </span>
								<input type="text" id="sYmd" name="sYmd" class="date2 required datetimepicker-input" data-toggle="datetimepicker" data-target="#sYmd" placeholder="연도-월-일" autocomplete="off"/>
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
			<div class="col-5 pr-3">
				<div class="inner">
					<div class="sheet_title_wrap clearfix">
						<div class="float-left title">근무제도관리&nbsp;<span id="Tooltip-flexibleStd" class="tooltip-st"><i class="far fa-question-circle"></i></span></div>
						<ul class="float-right btn-wrap">
							<li><a href="javascript:doAction1('Insert')" class="basic authA">입력</a></li>
							<li><a href="javascript:doAction1('Save')" class="basic authA">저장</a></li>
						</ul>
					</div>
				</div>
				<script type="text/javascript">createIBSheet("sheet1", "100%", fullsheetH,"kr"); </script>
			</div>
			<div class="col-7 pt-2">
				<div class="innertab inner" style="display:none;">
					<div id="tabs" class="tab">
						<ul class="outer tab_bottom">
							<li><a href="#tabs-1">근무제기준</a></li>
							<li><a href="#tabs-2">근무제패턴</a></li>
						</ul>
						<div id="tabs-1">
							<div  class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<div class="float-left title" id="searchAppText">근무제기준</div>
									<ul class="float-right btn-wrap" id="optionBtn">
										<li><a href="javascript:doAction1('Save2')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<div id="view_sele">
									<table class="default">
										<colgroup>
											<col width="20%">
											<col width="30%">
											<col width="20%">
											<col width="30%">
										</colgroup>
										<tbody>
											<tr id="trHoliday">
												<th>공휴일제외여부 <span id="Tooltip-holExceptYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="checkbox" id="holExceptYn" name="holExceptYn" />
													<label for="holExceptYn">체크시 공휴일 근무제외</label>
												</td>
												<th><span class="required"></span>인정근무 단위시간(분) <span id="Tooltip-unitMinute" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="text" id="unitMinute" name="unitMinute"/>
													<input type="hidden" id="taaTimeYn" name="taaTimeYn"/>
												</td>
											</tr>
											<tr id="trDayType">
												<th><span class="required"></span>출근자동처리기준<span id="Tooltip-dayOpenType" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<select id="dayOpenType"></select>
													
												</td>
												<th><span class="required"></span>퇴근자동처리기준<span id="Tooltip-dayCloseType" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<select id="dayCloseType"></select>
												</td>
											</tr>
											<tr id="trBaseCheck">
												<th>고정OT소진 사용여부 <span id="Tooltip-defaultWorkUseYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<input type="checkbox" id="defaultWorkUseYn" name="defaultWorkUseYn" />
													<label for="defaultWorkUseYn">체크시 고정OT 소진기준작성 </label>	
												</td>
											</tr>
											<tr id="trFixOt">
												<th><span></span>고정OT 소진방법 <span id="Tooltip-fixotUseType" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<select id="fixotUseType">														
					                                </select>
												</td>
												<th><span></span>고정OT 소진한계시간(분) <span id="Tooltip-fixotUseLimit" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="text" id="fixotUseLimit" name="fixotUseLimit"/>
												</td>
											</tr>
											<tr id="trRega">
												<th><span class="required"></span>간주근무시간 <span id="Tooltip-regardTimeCdId" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<select id="regardTimeCdId">
					                                </select>
												</td>
											</tr>
											<tr id="trWorkTime">
												<th>근무가능시각 <span id="Tooltip-workTime" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="text" id="workShm" name="workShm" class="date2 datetimepicker-input" data-toggle="datetimepicker" data-target="#workShm" placeholder="시:분" autocomplete="off"/>
													~
													<input type="text" id="workEhm" name="workEhm" class="date2 datetimepicker-input" data-toggle="datetimepicker" data-target="#workEhm" placeholder="시:분" autocomplete="off"/>
												</td>
												<th>근태일 근무가능여부 <span id="Tooltip-taaWorkYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="checkbox" id="taaWorkYn" name="taaWorkYn" />
													<label for="taaWorkYn">체크시 근태일 근무가능</label>
												</td>
											</tr>
											<tr id="trCoreChk">
												<th>코어시간체크여부</th>
												<td colspan="3">
													<input type="checkbox" id="coreChkYn" name="coreChkYn" /> 
													<label for="coreChkYn">체크시 코어시간 기준작성</label>
												</td>
											</tr>
											<tr id="trCoreTime">
												<th>코어근무시각</th>
												<td colspan="3">
													<input type="text" id="coreShm" name="coreShm" class="date2 datetimepicker-input" data-toggle="datetimepicker" data-target="#coreShm" placeholder="시:분" autocomplete="off"/>
													~
													<input type="text" id="coreEhm" name="coreEhm" class="date2 datetimepicker-input" data-toggle="datetimepicker" data-target="#coreEhm" placeholder="시:분" autocomplete="off"/>
												</td>
											</tr>
											<tr id="trBaseFirst">
												<th><span></span>기본근무선소진여부 <span id="Tooltip-exhaustionYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<select id="exhaustionYn">
					                                    <option value="Y">사용</option>
					                                    <option value="N">미사용</option>
					                                </select>
												</td>
											</tr>
											<tr id="trTodayPlanEdit">
												<th>당일근무변경여부 <span id="Tooltip-todayPlanEditYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<input type="checkbox" id="todayPlanEditYn" name="todayPlanEditYn" />
													<label for="todayPlanEditYn">체크시 당일 근무 변경가능</label> 
												</td>
											</tr>
											<tr id="trUnplan">
												<th>근무계획미등록여부 <span id="Tooltip-unplannedYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="checkbox" id="unplannedYn" name="unplannedYn" />
													<label for="unplannedYn">계획없이 타각시간 근무가능</label> 
												</td>
											</tr>
											<tr id="trApplYn">
												<th>신청여부 <span id="Tooltip-applYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<input type="checkbox" id="applYn" name="applYn" />
													<label for="applYn">체크시 신청가능</label> 
												</td>
											</tr>
											<tr id="trUsedTerm">
												<th>사용기간지지정 <span id="Tooltip-usedTermOpt" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<input type="checkbox" id="usedTermOpt1w" name="usedTermOpt" value="1_week" title="1주" /> 
													<label for="usedTermOpt1w">1주</label>
													<input type="checkbox" id="usedTermOpt2w" name="usedTermOpt" value="2_week" title="2주"/>
													<label for="usedTermOpt2w">2주</label>
													<input type="checkbox" id="usedTermOpt3w" name="usedTermOpt" value="3_week" title="3주"/>
													<label for="usedTermOpt3w">3주</label>
													<input type="checkbox" id="usedTermOpt4w" name="usedTermOpt" value="4_week" title="4주"/>
													<label for="usedTermOpt4w">4주</label>
													<input type="checkbox" id="usedTermOpt1m" name="usedTermOpt" value="1_month" title="1개월"/> 
													<label for="usedTermOpt1m">1개월</label>
													<input type="checkbox" id="usedTermOpt2m" name="usedTermOpt" value="2_month" title="2개월"/>
													<label for="usedTermOpt2m">2개월</label>
													<input type="checkbox" id="usedTermOpt3m" name="usedTermOpt" value="3_month" title="3개월"/>
													<label for="usedTermOpt3m">3개월</label>
												</td>
											</tr>																						
											<tr id="trApplyEntry">
												<th>출근타각인정여부 <span id="Tooltip-applyEntrySdateYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="checkbox" id="applyEntrySdateYn" name="applyEntrySdateYn" />
													<label for="applyEntrySdateYn">체크시 출근 타각시간으로 인정</label> 
												</td>
												<th>퇴근타각인정여부 <span id="Tooltip-applyEntryEdateYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td>
													<input type="checkbox" id="applyEntryEdateYn" name="applyEntryEdateYn" />
													<label for="applyEntryEdateYn">체크시 퇴근 타각시간으로 인정</label> 
												</td>
											</tr>
											<tr id="trCreateOtIfOutOfPlanYn">
												<th>계획시간 외 연장생성<span id="Tooltip-createOtIfOutOfPlanYn" class="tooltip-st"><i class="far fa-question-circle"></i></span></th>
												<td colspan="3">
													<input type="checkbox" id="createOtIfOutOfPlanYn" name="createOtIfOutOfPlanYn" />
													<label for="createOtIfOutOfPlanYn">체크시 계획시간 대비 타각시간 정보로 연장근무 시간을 만들어준다.</label> 
												</td>
											</tr>
											<!-- 20200220 주석처리
											<tr id="trApplTerm">
												<th>신청기간지정</th>
												<td colspan="3">
													<input type="radio" id="applTermOptday" name="applTermOpt" value="today"  title="당일 이내"/>
													<label for="applTermOptday">당일 이내</label>
													<input type="radio" id="applTermOpt1w" name="applTermOpt" value="1_week" title="1주일 이내"/> 
													<label for="applTermOpt1w">1주일 이내</label>
													<input type="radio" id="applTermOpt2w" name="applTermOpt" value="2_week" title="2주일 이내"/> 
													<label for="applTermOpt2w">2주일 이내</label>
													<input type="radio" id="applTermOpt3w" name="applTermOpt" value="3_week" title="3주일 이내"/>
													<label for="applTermOpt3w">3주일 이내</label>
													<input type="radio" id="applTermOpt4w" name="applTermOpt" value="4_week" title="4주일 이내"/>
													<label for="applTermOpt4w">4주일 이내</label> 
												</td>
											</tr>
											-->
											<tr>
												<th>비고</th>
												<td colspan="3">
													<textarea id="note" cols="50" rows="3">
													</textarea>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div id="tabs-2">
							<div  class="layout_tabs">
								<div class="inner sheet_title_wrap clearfix">
									<div class="float-left title" id="pattText">반복패턴</div>&nbsp;<span id="Tooltip-flexiblePatt" class="tooltip-st"><i class="far fa-question-circle"></i></span>
									<ul class="float-right btn-wrap" id="pattBtn">
										<li><a href="javascript:doAction2('Insert')" class="basic authA">입력</a></li>
										<li><a href="javascript:doAction2('Save')" class="basic authA">저장</a></li>
									</ul>
								</div>
								<script type="text/javascript">createIBSheet("sheet2", "100%", sheetH90,"kr"); </script>
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
	var timeCdMgrIdList;

   	$(function() {
   		//resize
		$(window).smartresize(sheetResize);
   	
   		$('#sYmd').datetimepicker({
            format: 'YYYY-MM-DD',
            language: 'ko'
        });
   		$('#sYmd').val(moment().format('YYYY-MM-DD'));
        
        $('#workShm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
                       
        $('#workEhm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        $('#coreShm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'left',
                vertical: 'bottom'
            }
        });
        
        $('#coreEhm').datetimepicker({
            //format: 'LT',
            format: 'HH:mm',
            use24hours: true,
            language: 'ko',
            widgetPositioning: {
                horizontal: 'right',
                vertical: 'bottom'
            }
        }); 
        new jBox('Tooltip', {
       	    attach: '#Tooltip-flexibleStd',
       	    target: '#Tooltip-flexibleStd',
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
       	    content: '근무제도 기준을 관리합니다.'
       	    		+ '<br>● 근무제도유형, 근무명칭, 시작일은 저장 후 변경이 불가능합니다.'
       	    		+ '<br>● 입력시 삭제가 불가능합니다.'
       	    		,
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
        new jBox('Tooltip', {
       	    attach: '#Tooltip-flexiblePatt',
       	    target: '#Tooltip-flexiblePatt',
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
       	    content: '근무제도별 근무패턴을 관리합니다.'
       	    		+ '<br>● 순서는 1부터 순서대로 입력합니다. 순서대로 근무스케줄이 생성됩니다.'
       	    		+ '<br>● 근무시간은 근무시간표 관리에 등록된 항목이 표시됩니다.'
       	    		+ '<br>● 반복패턴 순서1에 해당하는 기준은 근무제도관리의 시작일 요일입니다.'
       	    		,
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	});
        new jBox('Tooltip', {
       	    attach: '#Tooltip-usedTermOpt',
       	    target: '#Tooltip-usedTermOpt',
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
       	    content: '사용자 기간지정은 개인 당 사전에 계획된 근무 기간을 등록하는 것을 뜻합니다.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });        
        new jBox('Tooltip', {
       	    attach: '#Tooltip-holExceptYn',
       	    target: '#Tooltip-holExceptYn',
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
       	    content: '토/일요일, 공휴일 휴일처리 여부',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });        
        new jBox('Tooltip', {
       	    attach: '#Tooltip-unitMinute',
       	    target: '#Tooltip-unitMinute',
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
       	    content: '타각시 근무시간 절사 기준 <br> ex) 10분 등록시, 출근계획시간 09:00 일때 09:03분 타각시 인정근무 단위 10분단위인 09:10분으로 절사하여 근무인정',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-dayOpenType',
       	    target: '#Tooltip-dayOpenType',
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
       	    content: '사용안함 : 임직원 타각기록 적용 <br> 기본근무시간자동 : 일마감시 기본근무 계획시간 출근 인정 <br> 연장근무시간자동 : 일마감시 연장근무 계획시간 출근 인정 <br> 일마감 시점에 기본근무/연장근무 항목이 기록완료된 것을 인정함 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-dayCloseType',
       	    target: '#Tooltip-dayCloseType',
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
       	    content: '사용안함 : 임직원 타각기록 적용 <br> 기본근무시간자동 : 일마감시 기본근무 계획시간 퇴근 인정 <br> 연장근무시간자동 : 일마감시 연장근무 계획시간 퇴근 인정 <br> 일마감 시점에 기본근무/연장근무 항목이 기록완료된 것을 인정함 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-defaultWorkUseYn',
       	    target: '#Tooltip-defaultWorkUseYn',
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
       	    content: '고정연장근무 자동등록시 체크항목 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-fixotUseType',
       	    target: '#Tooltip-fixotUseType',
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
       	    content: '일별소진 : 선택근무 제외 근무제도만 사용가능 <br> 매일 기본근무 종료 후 고정연장근무시간이 근무계획에 추가됨 <br> 일괄소진 : 선택근무 근무제도만 사용가능 <br> 선택근무 기본근무 소진 후 고정연장근무시간까지 근무기록됨 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-fixotUseLimit',
       	    target: '#Tooltip-fixotUseLimit',
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
       	    content: '고정OT 소진 체크시 한계시간 <br> 고정OT 소진방법이 일별소진인 경우 매일 추가되는 연장근무시간 <br> 고정OT 소진방법이 일괄소진인 경우 선택근무제 기간동안 사용할 연장근무시간 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-regardTimeCdId',
       	    target: '#Tooltip-regardTimeCdId',
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
       	    content: '근무일 간주근무(출장, 교육 등) 근무시간 기록 기준 시간표 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-workTime',
       	    target: '#Tooltip-workTime',
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
       	    content: '선근제 근무계획 등록시 등록가능한 근무가능시간 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-taaWorkYn',
       	    target: '#Tooltip-taaWorkYn',
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
       	    content: '출장, 교육등 종일근태일 근무가능여부 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-exhaustionYn',
       	    target: '#Tooltip-exhaustionYn',
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
       	    content: '사용 : 선택근무기간 약정근무시간 소진(계획등록사항 인정) 후 연장근무 신청가능 <br> 미사용 : 선택근무기간 약정근무시간 소진 이전 연장근무 신청가능 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        
        new jBox('Tooltip', {
       	    attach: '#Tooltip-createOtIfOutOfPlanYn',
       	    target: '#Tooltip-createOtIfOutOfPlanYn',
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
       	    content: '기본근무 계획이 있을 경우,<br/>체크 시 계획된 기본근무 대비 타각시간으로 연장근무 정보를 생성한다.',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });

        new jBox('Tooltip', {
       	    attach: '#Tooltip-todayPlanEditYn',
       	    target: '#Tooltip-todayPlanEditYn',
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
       	    content: '선근제 근무계획 등록시 당일 근무 변경가능 여부 <br> 체크시 당일을 포함한 미래 근무계획 변경가능 <br> 미체크시 당일을 제외한 미래 근무계획 변경가능 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-unplannedYn',
       	    target: '#Tooltip-unplannedYn',
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
       	    content: '근무계획 미등록 체크시 <br> 근무계획 없이 출퇴근 타각으로 근무 가능함 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-applYn',
       	    target: '#Tooltip-applYn',
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
       	    content: '근무제도 개인 신청 가능 여부 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-applyEntrySdateYn',
       	    target: '#Tooltip-applyEntrySdateYn',
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
       	    content: '체크시 계획시간 이전 출근시 출근 타각시간으로 근무인정 ',
       	    onOpen: function () {
       	      this.source.addClass('active');
       	    },
       	    onClose: function () {
       	      this.source.removeClass('active');
       	    }
       	  });
        new jBox('Tooltip', {
       	    attach: '#Tooltip-applyEntryEdateYn',
       	    target: '#Tooltip-applyEntryEdateYn',
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
       	    content: '체크시 계획시간 이후 퇴근시 퇴근 타각시간으로 근무인정 ',
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
			{Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
			{Header:"id",				Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"tenantId",			Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"tenantId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"enterCd",			Type:"Text",	Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"enterCd",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"근무제도유형",  	Type:"Combo",   Hidden:0,   Width:70,   Align:"Center", ColMerge:0, SaveName:"workTypeCd",  	KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
			{Header:"근무명칭",			Type:"Text",	Hidden:0,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"flexibleNm",		KeyField:1,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"시작일",			Type:"Date",    Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"useSymd",        	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:100 },
			{Header:"종료일",			Type:"Date",    Hidden:0,   Width:90,   Align:"Center", ColMerge:0, SaveName:"useEymd",        	KeyField:1, Format:"Ymd",   PointCount:0,   UpdateEdit:1,   InsertEdit:1,   EditLen:100 },
			{Header:"공휴일제외여부",	Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"holExceptYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"인정근무 단위시간",	Type:"Int",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"unitMinute", 		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:5 },			
			{Header:"고정OT 소진여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"defaultWorkUseYn", KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"고정OT 소진방법",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"fixotUseType",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"고정OT 한계시간",	Type:"Int",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"fixotUseLimit",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:5 },			
			{Header:"간주근무시간",		Type:"Int",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"regardTimeCdId",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:20 },
			{Header:"근무가능시작",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"workShm",			KeyField:0,	Format:"Hm",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"근무가능종료",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"workEhm",			KeyField:0,	Format:"Hm",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"코어시간체크여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"coreChkYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"코어근무시작",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"coreShm",			KeyField:0,	Format:"Hm",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"코어근무종료",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"coreEhm",			KeyField:0,	Format:"Hm",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:4 },
			{Header:"선소진여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"exhaustionYn",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"사용기간지정",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"usedTermOpt",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"신청기간지정",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applTermOpt",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"신청기간지정",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applTermOpt",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 },
			{Header:"당일근무변경여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"todayPlanEditYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"출근타각인정여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applyEntrySdateYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"퇴근타각인정여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applyEntryEdateYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"근태시간포함여부",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"taaTimeYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"근태일근무여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"taaWorkYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"출근자동처리",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"dayOpenType",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"퇴근자동처리",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"dayCloseType",	KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"계획외연장근무생성",	Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"createOtIfOutOfPlanYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:1 },
			{Header:"기준요일",			Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"baseDay",			KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"계획없음여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"unplannedYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"신청여부",		Type:"Text",	Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"applYn",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:50 },
			{Header:"비고",				Type:"Text",		Hidden:1,	Width:100,	Align:"Left",	ColMerge:0,	SaveName:"note",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:2000 }
		]; 
		
		IBS_InitSheet(sheet1, initdata1);
		sheet1.SetEditable(true);
		sheet1.SetVisible(true);
		sheet1.SetUnicodeByte(3);
		
		//근무제도코드
		var workTypeCdList = stfConvCode(codeList("${rc.getContextPath()}/code/list", "WORK_TYPE_CD"), "선택");
		sheet1.SetColProperty("workTypeCd", {ComboText:workTypeCdList[0], ComboCode:workTypeCdList[1]} );
		
		//근무시간
		var regardTimeCdId = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=N",false).DATA, "선택");
		$("#regardTimeCdId").html(regardTimeCdId[2]);
		
		//출퇴근자동처리기준
		var dayType = stfConvCode(codeList("${rc.getContextPath()}/code/list", "DAY_ENTRY_TYPE"), "선택");
		$("#dayOpenType").html(dayType[2]);
		$("#dayCloseType").html(dayType[2]);
		
		var initdata2 = {};
		initdata2.Cfg = {SearchMode:smLazyLoad,Page:22};
		initdata2.HeaderMode = {Sort:1,ColMove:1,ColResize:1,HeaderCheck:0};
		
        initdata2.Cols = [
            {Header:"No",			Type:"Seq",			Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sNo" },
			{Header:"삭제",			Type:"DelCheck",	Hidden:0,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sDelete",	Sort:0 },
   			{Header:"상태",			Type:"Status",		Hidden:0 ,	Width:45,	Align:"Center",	ColMerge:0,	SaveName:"sStatus",	Sort:0 },
   			{Header:"subGrp",    	Type:"Int",       	Hidden:1,  	Width:0,    Align:"Left",    ColMerge:0,   SaveName:"subGrp",   KeyField:0,   CalcLogic:"",   Format:"",            PointCount:0,   UpdateEdit:0,   InsertEdit:1,   EditLen:20 },
			{Header:"id",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"workPattDetId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
			{Header:"upid",			Type:"Text",		Hidden:1,	Width:100,	Align:"Center",	ColMerge:0,	SaveName:"flexibleStdMgrId",		KeyField:0,	Format:"",		PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"순서",			Type:"Int",	      	Hidden:0,	Width:50,	Align:"Center",	 ColMerge:0, SaveName:"seq",	 KeyField:1,	PointCount:0,	UpdateEdit:0,	InsertEdit:1,	EditLen:100 },
            {Header:"근무시간",  		Type:"Combo",     	Hidden:0,   Width:70,  Align:"Center",  ColMerge:0, SaveName:"timeCdMgrId",  KeyField:1,    Format:"",    PointCount:0,  UpdateEdit:0,  InsertEdit:1,  EditLen:100  },
            {Header:"출근시각",		Type:"Text",  		Hidden:1,	Width:100,	Align:"Center",	 ColMerge:0, SaveName:"planShm", 		KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"퇴근시각",		Type:"Text",  		Hidden:1,	Width:100,	Align:"Center",	 ColMerge:0, SaveName:"planEhm",		KeyField:0,	Format:"Hm",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"조출시간(분)",	Type:"AutoSum",  		Hidden:1,	Width:100,	Align:"Center",	 ColMerge:0, SaveName:"otbMinute",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"잔업시간(분)",	Type:"AutoSum",  		Hidden:1,	Width:100,	Align:"Center",	 ColMerge:0, SaveName:"otaMinute",		KeyField:0,	Format:"",	PointCount:0,	UpdateEdit:0,	InsertEdit:0,	EditLen:100 },
			{Header:"기본시간(분)",	Type:"AutoSum",     Hidden:1,  Width:80,   Align:"Center",  ColMerge:0, SaveName:"planMinute",  KeyField:0, Format:"",      PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:3 , ExcludeEmpty:1},
			{Header:"휴일시간(분)",	Type:"AutoSum",     	Hidden:1,  Width:80,   Align:"Center",  ColMerge:0, SaveName:"otMinute",   KeyField:0, Format:"",      PointCount:0,   UpdateEdit:0,   InsertEdit:0,   EditLen:3 , ExcludeEmpty:1},
			{Header:"비고",			Type:"Text",	 	Hidden:0,	Width:80,	Align:"Left",	 ColMerge:0, SaveName:"note",	 KeyField:0,	PointCount:0,	UpdateEdit:1,	InsertEdit:1,	EditLen:100 }
			
        ];
        IBS_InitSheet(sheet2, initdata2);
		sheet2.SetEditable(true);
		sheet2.SetVisible(true);
		sheet2.SetUnicodeByte(3);
		
		//근무시간
		timeCdMgrIdList = stfConvCode(ajaxCall("${rc.getContextPath()}/timeCdMgr/timeCodeList", "holYn=",false).DATA, "선택");
		sheet2.SetColProperty("timeCdMgrId", {ComboText:timeCdMgrIdList[0], ComboCode:timeCdMgrIdList[1]} );

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
	
	function showIframe() {
		if(iframeIdx == 0) {
			$("#tabs-1").show();
			$("#tabs-2").hide();
		} else if(iframeIdx == 1) {
			$("#tabs-1").hide();
			$("#tabs-2").show();
			sheetResize();
		}
	}

   	function doAction1(sAction) {
		switch (sAction) {
		case "Search":
			sheet1.DoSearch( "${rc.getContextPath()}/flexibleStd/listWeb" , $("#sheetForm").serialize());
			break;
		
		case "Save":
			if(!dupChk(sheet1,"tenantId|enterCd|workTypeCd|flexibleNm|useSymd", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet1);
			sheet1.DoSave("${rc.getContextPath()}/flexibleStd/saveWeb", $("#sheetForm").serialize());
			break;
			
		case "Save2":
			// 필수값 체크
	        if ( !checkList() ) {
	            return false;
	        }
			var row = sheet1.GetSelectRow();
			
			if($('#trHoliday').is(':visible')){
				var chkYn = getCheckYn("holExceptYn");
				sheet1.SetCellValue(row, "holExceptYn", chkYn);
			}
			if($('#trBaseCheck').is(':visible')){
	        	var chkYn = getCheckYn("defaultWorkUseYn");
				sheet1.SetCellValue(row, "defaultWorkUseYn", chkYn);
	        }
	        if($('#trFixOt').is(':visible')){
	        	sheet1.SetCellValue(row, "fixotUseType", $("#fixotUseType").val());
	        	sheet1.SetCellValue(row, "fixotUseLimit", $("#fixotUseLimit").val());
	        }
	        if($('#trWorkTime').is(':visible')){
	        	sheet1.SetCellValue(row, "workShm", $("#workShm").val());
	        	sheet1.SetCellValue(row, "workEhm", $("#workEhm").val());
	        	var chkYn = getCheckYn("taaWorkYn");
	        	sheet1.SetCellValue(row, "taaWorkYn", chkYn);
	        }
	        // if($('#trCoreChk').is(':visible')){
	        	var chkYn = getCheckYn("coreChkYn");
				sheet1.SetCellValue(row, "coreChkYn", chkYn);
	        // }
	        if($('#trCoreTime').is(':visible')){
	        	sheet1.SetCellValue(row, "coreShm", $("#coreShm").val());
	        	sheet1.SetCellValue(row, "coreEhm", $("#coreEhm").val());
	        }
	        if($('#trBaseFirst').is(':visible')){
	        	sheet1.SetCellValue(row, "exhaustionYn", $("#exhaustionYn").val());
	        }
	        if($('#trUnplan').is(':visible')){
	        	var chkYn = getCheckYn("unplannedYn");
	        	sheet1.SetCellValue(row, "unplannedYn", chkYn);
	        }
	        if($('#trApplyEntry').is(':visible')){
	        	var applyEntrySdateYn = getCheckYn("applyEntrySdateYn");
	        	var applyEntryEdateYn = getCheckYn("applyEntryEdateYn");
	        	sheet1.SetCellValue(row, "applyEntrySdateYn", applyEntrySdateYn);
	        	sheet1.SetCellValue(row, "applyEntryEdateYn", applyEntryEdateYn);
	        }

	        if($('#trCreateOtIfOutOfPlanYn').is(':visible')){
	        	var createOtIfOutOfPlanYn = getCheckYn("createOtIfOutOfPlanYn");
	        	sheet1.SetCellValue(row, "createOtIfOutOfPlanYn", createOtIfOutOfPlanYn);
	        }
	        if($('#trUsedTerm').is(':visible')){
	        	var usedTermArr = new Array();
				$('input[name="usedTermOpt"]').each(function() {
				    if(this.checked){
				    	var objItem = new Object();				    	
						objItem.lable = this.title;
						objItem.value = this.value;
						usedTermArr.push(objItem);
				    }
				});
				var usedTermOpt = JSON.stringify(usedTermArr);
				sheet1.SetCellValue(row, "usedTermOpt", usedTermOpt);
	        }
	        if($('#trApplYn').is(':visible')){
	        	var chkYn = getCheckYn("applYn");
	        	sheet1.SetCellValue(row, "applYn", chkYn);
	        }
	        if($('#trTodayPlanEdit').is(':visible')){
	        	var chkYn = getCheckYn("todayPlanEditYn");
	        	sheet1.SetCellValue(row, "todayPlanEditYn", chkYn);
	        }
	        /* 20200220 주석처리
	        //신청기간지정
	        if($('#trApplTerm').is(':visible')){
	        	var applTermOptArr = new Array();
				$('input[name="applTermOpt"]').each(function() {
				    if(this.checked){
				    	var objItem = new Object();				    	
						objItem.lable = this.title;
						objItem.value = this.value;
						applTermOptArr.push(objItem);
				    }
				});
				var applTermOpt = JSON.stringify(applTermOptArr);
				sheet1.SetCellValue(row, "applTermOpt", applTermOpt);
	        }
	        */
	        if($("#dayOpenType").val() == "" || $("#dayOpenType").val() == "null" || $("#dayOpenType").val() === null){
				alert("출근자동처리기준을 선택하세요");
				return;
			} else {
				sheet1.SetCellValue(row, "dayOpenType", $("#dayOpenType").val());
			}
			if($("#dayCloseType").val() == "" || $("#dayCloseType").val() == "null" || $("#dayCloseType").val() === null){
				alert("퇴근자동처리기준을 선택하세요");
				return;
			} else {
				sheet1.SetCellValue(row, "dayCloseType", $("#dayCloseType").val());
			}
	        if($("#regardTimeCdId").val() == "" || $("#regardTimeCdId").val() == "null" || $("#regardTimeCdId").val() === null){
				alert("간주근무시간을 선택하세요");
				return;
			} else {
				sheet1.SetCellValue(row, "regardTimeCdId", $("#regardTimeCdId").val());
			}
			sheet1.SetCellValue(row, "unitMinute", $("#unitMinute").val());
			sheet1.SetCellValue(row, "taaTimeYn", $("#taaTimeYn").val());
			sheet1.SetCellValue(row, "note", $("#note").val());
			doAction1("Save");
			break;	
			
		case "Insert":
			sheet1.DataInsert(-1) ;
			break;
		}


	}
	
	//근무제패턴저장
	function doAction2(sAction) {
		switch (sAction) {
		case "Search":
			var title = "반복패턴  (순서 1에 해당하는 요일은  <font color='red'>"+sheet1.GetCellValue( sheet1.GetSelectRow(), "baseDay")+"</font>입니다)";
			$("#pattText").html(title);
			var param = "flexibleStdMgrId="+sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId");
			sheet2.DoSearch( "${rc.getContextPath()}/flexibleStd/listPatt" , param);
			break;
		
		case "Save":
			if(!dupChk(sheet1,"flexibleStdMgrId|seq", false, true)){break;}
			IBS_SaveName(document.sheetForm,sheet2);
			sheet2.DoSave("${rc.getContextPath()}/flexibleStd/savePatt", $("#sheetForm").serialize());
			break;
			
		case "Insert":
			var flexibleStdMgrId = sheet1.GetCellValue( sheet1.GetSelectRow(), "flexibleStdMgrId");
			if(flexibleStdMgrId == ""){
				alert("근무제도 저장 후 근무제패턴을 입력하셔야 합니다");
			} else {
				var row = sheet2.DataInsert(-1) ;
				sheet2.SetCellValue(row, "flexibleStdMgrId" , flexibleStdMgrId);
				// alert(sheet2.GetRowEditable(row));
				// alert(sheet2.GetColEditable(row, "codeCd") + ", " + sheet2.GetColEditable(row, "codeNm"));
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
			$(".innertab").show();
			sheet2.RemoveAll();
			//sheetResize();
			if(iframeIdx == 0) {
				showIframe();
			} else {
				$( "#tabs" ).tabs({active:0});
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
	
	// Cell select
	function sheet1_OnSelectCell(OldRow, OldCol, NewRow, NewCol,isDelete) {
		if(sheet1.GetCellValue( sheet1.GetSelectRow(), "sStatus") != "I"){
			$('.layout_tabs').show();
			$("#pattBtn").show();	// 패턴저장 버튼 숨김
			$("#optionBtn").show(); //옵션저장 버튼 숨김
			if(OldRow != NewRow){
				
				// 옵션마스터용 값을 셋팅해야함.
				var workTypeCd = sheet1.GetCellValue( NewRow, "workTypeCd");
				$("input:checkbox[name='holExceptYn']").prop("checked", false);
				$("input:checkbox[name='defaultWorkUseYn']").prop("checked", false);
				$("input:checkbox[name='coreChkYn']").prop("checked", false);
				$("input:checkbox[name='usedTermOpt']").prop("checked", false);
				$("input:checkbox[name='applTermOpt']").prop("checked", false);
				$("input:checkbox[name='applYn']").prop("checked", false);
				$("input:checkbox[name='todayPlanEditYn']").prop("checked", false);
				
				// 공휴일제외여부
				if(sheet1.GetCellValue( NewRow, "holExceptYn") == "Y"){
					$("input:checkbox[name='holExceptYn']").prop("checked", true);
					
				}
			
				$("#dayOpenType").val(sheet1.GetCellValue( NewRow, "dayOpenType")).prop("selected", true);
				$("#dayCloseType").val(sheet1.GetCellValue( NewRow, "dayCloseType")).prop("selected", true);
				
				$("#trApplyEntry").hide();
				$("#trCreateOtIfOutOfPlanYn").hide();
				// 고정OT
				if(workTypeCd == "ELAS"){
					$("#trBase").hide();
					$("#trFixOt").hide();
					$("#trBaseCheck").hide();
					$("#fixotUseType").val("");
					$("#fixotUseLimit").val("");
					
					sheet2.SetColHidden("planShm", 0);
		 			sheet2.SetColHidden("planEhm", 0);
		 			sheet2.SetColHidden("otbMinute", 0);
		 			sheet2.SetColHidden("otaMinute", 0);
		 			sheet2.SetColHidden("planMinute", 0);
		 			sheet2.SetColHidden("otMinute", 0);
		 			
		 			var info = [{StdCol:"subGrp" , SumCols:"otbMinute|otaMinute|planMinute|otMinute", ShowCumulate:0, CaptionCol:5}];
		 			sheet2.ShowSubSum(info);
		 			sheet2.SetSumRowHidden(0);
		 			
				} else {
					$("#trBaseCheck").show();
					
					// 고정ot소진 사용여부		
					//20200220 start
					if(workTypeCd == "SELE_F" || workTypeCd == "SELE_C"){	
						$("#fixotUseType").empty();
					    $("#fixotUseType").append("<option value='ALL'>일괄 소진</option>");					    					   
					    
					} else {
						$("#fixotUseType").empty();						
						$("#fixotUseType").append("<option value='DAY'>일별 소진</option>");
						
					}
					//20200220 end
					
					if(sheet1.GetCellValue( NewRow, "defaultWorkUseYn") == "Y"){
						$("input:checkbox[name='defaultWorkUseYn']").prop("checked", true);
						setDefaultWorkUseYn(true);
						$("#trFixOt").show();
						$("#fixotUseType").val(sheet1.GetCellValue( NewRow, "fixotUseType")).prop("selected", true);
						$("#fixotUseLimit").val(sheet1.GetCellValue( NewRow, "fixotUseLimit"));
					} else {
						setDefaultWorkUseYn(false);
					}
					
					sheet2.SetColHidden("planShm", 1);
		 			sheet2.SetColHidden("planEhm", 1);
		 			sheet2.SetColHidden("otbMinute", 1);
		 			sheet2.SetColHidden("otaMinute", 1);
		 			sheet2.SetColHidden("planMinute", 1);
		 			sheet2.SetColHidden("otMinute", 1);
		 			
		 			sheet2.HideSubSum();
		 			sheet2.SetSumRowHidden(1);

				}
				
				//선택근무
				if(workTypeCd == "SELE_F" || workTypeCd == "SELE_C"){
					$("#taaTimeYn").val("Y");
					
					//근무가능시각
					$("#trWorkTime").show();									
					
					//20200225 시간 format 수정
					//$("#workShm").val(sheet1.GetCellValue( NewRow, "workShm"));
					//$("#workEhm").val(sheet1.GetCellValue( NewRow, "workEhm"));
										
					// 시간 format
					var workShm = sheet1.GetCellValue( NewRow, "workShm");
					var workEhm = sheet1.GetCellValue( NewRow, "workEhm");
					if( workShm != "" && workEhm != "" ) {						
						workShm = workShm.substring(0,2)+":"+workShm.substring(2,4);						
						workEhm = workEhm.substring(0,2)+":"+workEhm.substring(2,4);
											
						$("#workShm").val(workShm);
						$("#workEhm").val(workEhm);	
					} else {
						$("#workShm").val("");
						$("#workEhm").val("");	
					}
										
					if(sheet1.GetCellValue( NewRow, "taaWorkYn") == "Y"){
						$("input:checkbox[name='taaWorkYn']").prop("checked", true);
					} else {
						$("input:checkbox[name='taaWorkYn']").prop("checked", false);
					}
					$("#trBaseFirst").show();
					//$("#exhaustionYn").addClass("required");
					$("#trBaseFirst").find("th span").eq(0).addClass("required");
					$("#exhaustionYn").val(sheet1.GetCellValue( NewRow, "exhaustionYn")).prop("selected", true);
					
					if(workTypeCd == "SELE_C"){
						$("#trCoreChk").hide();
						$("#trUnplan").hide();
						
						// 20200521 부분선근제는 무조건 코어시간 체크해야함
						// if(sheet1.GetCellValue( NewRow, "coreChkYn") == "Y"){
							$("input:checkbox[name='coreChkYn']").prop("checked", true);
							setCoreChkYn(true);
						// } else {
						// 	setCoreChkYn(false);
						//}
						
						//코어근무시각
						$("#trCoreTime").show();
						//20200225 시간 format 수정
						//$("#coreShm").val(sheet1.GetCellValue( NewRow, "coreShm"));
						//$("#coreEhm").val(sheet1.GetCellValue( NewRow, "coreEhm"));
						
						var coreShm = sheet1.GetCellValue( NewRow, "coreShm");
						var coreEhm = sheet1.GetCellValue( NewRow, "coreEhm");
						if( coreShm != "" && coreEhm != "" ) {													
							coreShm = coreShm.substring(0,2)+":"+coreShm.substring(2,4);							
							coreEhm = coreEhm.substring(0,2)+":"+coreEhm.substring(2,4);
												
							$("#coreShm").val(coreShm);
							$("#coreEhm").val(coreEhm);
						} else {
							$("#coreShm").val("");
							$("#coreEhm").val("");
						}				
						
					} else {
						$("#trCoreChk").hide();
						$("#trCoreTime").hide();									
						
						$("#trApplyEntry").show();
						if(sheet1.GetCellValue( NewRow, "applyEntrySdateYn") == "Y"){
							$("input:checkbox[name='applyEntrySdateYn']").prop("checked", true);
						} else {
							$("input:checkbox[name='applyEntrySdateYn']").prop("checked", false);
						}
						if(sheet1.GetCellValue( NewRow, "applyEntryEdateYn") == "Y"){
							$("input:checkbox[name='applyEntryEdateYn']").prop("checked", true);
						} else {
							$("input:checkbox[name='applyEntryEdateYn']").prop("checked", false);
						}
						
						// 근무계획없음 가능여부
						$("#trUnplan").show();
						if(sheet1.GetCellValue( NewRow, "unplannedYn") == "Y"){
							$("input:checkbox[name='unplannedYn']").prop("checked", true);
						} else {
							$("input:checkbox[name='unplannedYn']").prop("checked", false);
						}
						
						$("#trCreateOtIfOutOfPlanYn").show();
						if(sheet1.GetCellValue( NewRow, "createOtIfOutOfPlanYn") == "Y"){
							$("input:checkbox[name='createOtIfOutOfPlanYn']").prop("checked", true);
						} else {
							$("input:checkbox[name='createOtIfOutOfPlanYn']").prop("checked", false);
						}
						
					}
					
					if(sheet1.GetCellValue( NewRow, "todayPlanEditYn") == "Y"){
						$("input:checkbox[name='todayPlanEditYn']").prop("checked", true);
					}
					$("#trTodayPlanEdit").show();
					
					
					
				} else {
					$("#taaTimeYn").val("N");
					$("#trWorkTime").hide();
					$("#trCoreTime").hide();
					$("#trBaseFirst").hide();
					$("#trUnplan").hide();
					$("#trCoreChk").hide();
					//$("#exhaustionYn").removeClass("required");
					$("#trBaseFirst").find("th span").eq(0).removeClass("required");
					$("#workShm").val("");
					$("#workEhm").val("");
					$("#coreShm").val("");
					$("#coreEhm").val("");
					$("#coreChkYn").val("");
					$("#exhaustionYn").val("");
					$("#unplannedYn").val("");
					$("#taaWorkYn").val("");
					$("#trTodayPlanEdit").hide();
				}
				
				// 신청기간
				if(workTypeCd == "BASE" || workTypeCd == "WORKTEAM"){
					$("#trUsedTerm").hide();
					$("#trApplYn").hide();
					$("#trApplTerm").hide();
				} else {
					$("#trUsedTerm").show();
					var usedTermOpt = sheet1.GetCellValue( NewRow, "usedTermOpt");
					if(usedTermOpt != ""){
					var dataUseTermOpt = JSON.parse(usedTermOpt);
						for(var i=0; i<dataUseTermOpt.length; i++) {
							var value = dataUseTermOpt[i].value;
							$("input:checkbox[name=usedTermOpt][value=" + value + "]").prop("checked", true);
						}
					}
					$("#trApplYn").show();
					if(sheet1.GetCellValue( NewRow, "applYn") == "Y"){						
						$("input:checkbox[name='applYn']").prop("checked", true);
						
						setApplYn(true);
												
					//20200220 else 추가	
					} else {						
						setApplYn(false);											
					}
					
					/* 20200220 주석처리
					$("#trApplTerm").show();
					var applTermOpt = sheet1.GetCellValue( NewRow, "applTermOpt");
					if(applTermOpt != ""){
						var dataApplTermOpt = JSON.parse(applTermOpt);
						for(var i=0; i<dataApplTermOpt.length; i++) {
							var value = dataApplTermOpt[i].value;
							$("input:checkbox[name=applTermOpt][value=" + value + "]").prop("checked", true);
						}
					}
					*/
				}
				// 간주근무
				$("#regardTimeCdId").val(sheet1.GetCellValue( NewRow, "regardTimeCdId")).prop("selected", true);
				$("#unitMinute").val(sheet1.GetCellValue( NewRow, "unitMinute"));
					
				$("#note").val(sheet1.GetCellValue( NewRow, "note"));
				
				// 근무패턴 조회
				sheet2.RemoveAll();
				doAction2('Search');
			}
		} else {
			// 신규일때는 속성을 입력할수 없음 
			sheet2.RemoveAll();	// 패턴 클리어
			$("#pattBtn").hide();	// 패턴저장 버튼 숨김
			
			$("#optionBtn").hide(); //옵션저장 버튼 숨김
			setOptionClear();	// 옵션항목 data 클리어
			
			$('.layout_tabs').hide();
		}
	}
	
	// 조회 후 에러 메시지
	function sheet2_OnSearchEnd(Code, Msg, StCode, StMsg) {
		try {
			if (StCode == 401) {
				window.parent.location.href = loginUrl;
			}
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
	
	function sheet2_OnChange(Row, Col, Value) {  
		if(sheet2.ColSaveName(Col) == "planShm" || sheet2.ColSaveName(Col) == "planEhm" || sheet2.ColSaveName(Col) == "timeCdMgrId"){
			var shm = sheet2.GetCellValue(Row, "planShm");
			var ehm = sheet2.GetCellValue(Row, "planEhm");
			var timeCdMgrId = sheet2.GetCellValue(Row, "timeCdMgrId");
			var param = "timeCdMgrId=" + timeCdMgrId
					  + "&shm=" + shm
					  + "&ehm=" + ehm;
			var rtn = ajaxCall("${rc.getContextPath()}/flexibleStd/pattern/workHour", param ,false).DATA;
			if(rtn != null && rtn != "") {
				if(shm.length == 4 && ehm.length == 4){
					if(rtn.holidayYn == "Y"){
						sheet2.SetCellValue(Row, "otMinute", rtn.calcMinute);
						sheet2.SetCellValue(Row, "planMinute", "");
					} else {
						sheet2.SetCellValue(Row, "otMinute", "");
						sheet2.SetCellValue(Row, "planMinute", rtn.calcMinute);
					}
				} else {
					sheet2.SetCellValue(Row, "planMinute", "");
					sheet2.SetCellValue(Row, "otMinute", "");
				}
				
				if(sheet2.ColSaveName(Col) == "timeCdMgrId" && rtn.holidayYn == "Y") {
					sheet2.SetCellValue(Row, "planShm", "");
					sheet2.SetCellValue(Row, "planEhm", "");
					sheet2.SetCellValue(Row, "planMinute", "");
					sheet2.SetCellValue(Row, "otMinute", "");
				}
			}
		}
	}
	
	function checkList(){
		var ch = true;
        // 화면의 개별 입력 부분 필수값 체크
        $("#tabs-1 .required").each(function(index){
            //if($(this).val() == null || $(this).val() == ""){
            if($(this).parent().next().children().val() == null || $(this).parent().next().children().val() == ""){
                alert($(this).parent().text()+"은 필수값입니다.");
                $(this).focus();
                ch =  false;
                return false;
            }
        });
        return ch;
	}
	function getCheckYn(objId){
		var chkYn = "N";
		if($("input:checkbox[name="+objId+"]").is(":checked")){
			 chkYn = "Y";
		}
		return chkYn;
	}
	
	//고정OT소진 사용여부에 따른 SET
	function setDefaultWorkUseYn(chk){
		if(chk){
        	$("#trFixOt").show();
        	//$("#fixotUseType").addClass("required");
        	//$("#fixotUseLimit").addClass("required");
        	
        	$("#trFixOt").find("th").each(function(t){
        		$(this).find("span").eq(0).addClass("required");
        	}); 
        	
		} else {
            $("#trFixOt").hide();
           	$("#fixotUseType").val("");
			$("#fixotUseLimit").val("");
        	//$("#fixotUseType").removeClass("required");
        	//$("#fixotUseLimit").removeClass("required");
        	
			$("#trFixOt").find("th").each(function(t){
        		$(this).find("span").eq(0).removeClass("required");
        	}); 
		}
	}
	 $("#defaultWorkUseYn").change(function(){
	 	setDefaultWorkUseYn($("#defaultWorkUseYn").is(":checked"));
    });
    
	//코어시간체크여부에 따른 SET
    function setCoreChkYn(chk){
		if(chk){
			$("#trCoreTime").show();
		} else {
			$("#trCoreTime").hide();
            $("#coreShm").val("");
           	$("#coreEhm").val("");
		}
	}
	function setOptionClear(){
		$("#pattText").html("반복패턴");
		// input Text 클리어
		var $inputTextList = $("#view_sele").find("input[type='text']");
		$inputTextList.each(function(){
		    $(this).val("");
		});
		// input checkbox 클리어
		var $inputcheckList = $("#view_sele").find("input[type='checkbox']");
		$inputcheckList.each(function(){
		    $(this).prop("checked", false);
		});
		var $inputselectList = $("#view_sele").find("select");
		$inputselectList.each(function(){
		    $(this).val("").prop("selected", true);
		});
		$("#note").val("");
	}
	
	$("#coreChkYn").change(function(){
	 	setCoreChkYn($("#coreChkYn").is(":checked"));
    });
	 
	//20200220 추가 
	//신청여부에 따른 SET
	function setApplYn(chk){		
		if(chk){
			$("#trUsedTerm").show();
			//$("#trApplTerm").show();
			
		} else {
			$("#trUsedTerm").hide();
			$("#trApplTerm").hide();
			
			// input checkbox 클리어
			$("input:checkbox[name='usedTermOpt']").prop("checked", false);			
			$("input:radio[name='applTermOpt']").prop("checked", false);
											
		}
	}
	$("#applYn").change(function(){
		setApplYn($("#applYn").is(":checked"));
    });    
	
</script>
