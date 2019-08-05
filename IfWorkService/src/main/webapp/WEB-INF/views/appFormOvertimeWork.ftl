<div class="content inner-wrap">
	<form action="" class="application-form">
		<div class="modal-header">
			<h5 class="modal-title">신청서</h5>
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>
		<div class="modal-body">
			<h2 class="title">
				연장근무신청<br> <span class="sub-desc point-color">(* 표기된 항목은
					필수 입력 항목입니다.)</span>
			</h2>
			<div class="form-group row align-items-center">
				<label for="title" class="col-sm-4 col-md-3 col-lg-2 col-form-label">제목</label>
				<div class="col-sm-8 col-md-9 col-lg-10">
					<input type="text" class="form-control" id="title"
						placeholder="연장근무신청">
				</div>
			</div>
			<div class="form-group row align-items-center">
				<label for="workDay"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span
					class=" require"></span>근무일</label>
				<div class="col-sm-5 col-md-5 col-lg-5">
					<div class="input-group date" id="workDay"
						data-provide="datepicker">
						<input type="text" class="form-control " id="inputWorkDate"
							placeholder="" required="" value="">
						<div class="input-group-append">
							<span class="input-group-text " id="inputWorkDateAppend"><i
								class="far fa-calendar-alt"></i></span>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group row align-items-center">
				<label for="workTime"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label">근무시간</label>
				<div class="col-sm-2 col-md-2 col-lg-2">
					<input type="text" class="form-control" id="workTime1"
						placeholder="" aria-describedby="inputGroupPrepend" required="">
				</div>
				<div class="col-sm-1 col-md-1 col-lg-1 text-center">~</div>
				<div class="col-sm-2 col-md-2 col-lg-2">
					<input type="text" class="form-control" id="workTim2"
						placeholder="" aria-describedby="inputGroupPrepend" required="">
				</div>
			</div>
			<div class="form-group row align-items-center">
				<label for="workTimeBefore"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label">출근시간 전
					(조출)</label>
				<div class="col-sm-2 col-md-2 col-lg-2">
					<input type="text" class="form-control" id="workTimeBefore1"
						placeholder="" aria-describedby="inputGroupPrepend" required="">
				</div>
				<div class="col-sm-1 col-md-1 col-lg-1 text-center">~</div>
				<div class="col-sm-2 col-md-2 col-lg-2">
					<input type="text" class="form-control" id="workTimeBefore2"
						placeholder="" aria-describedby="inputGroupPrepend" required="">
				</div>
			</div>
			<div class="form-group row align-items-center">
				<label for="workTimeAfter"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label">퇴근시간 후
					(잔업)</label>
				<div class="col-sm-2 col-md-2 col-lg-2">
					<input type="text" class="form-control" id="workTimeAfter1"
						placeholder="" aria-describedby="inputGroupPrepend" required="">
				</div>
				<div class="col-sm-1 col-md-1 col-lg-1 text-center">~</div>
				<div class="col-sm-2 col-md-2 col-lg-2 ">
					<input type="text" class="form-control" id="workTimeAfter2"
						placeholder="" aria-describedby="inputGroupPrepend" required="">
				</div>
			</div>
			<div class="form-group row align-items-center">
				<label for="remainWorkTime"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label">잔여연장가능시간</label>
				<div class="col-sm-5">
					<input type="text" class="form-control" id="remainWorkTime"
						placeholder="" readonly>
				</div>
				<div class="col-sm-3">
					<p class="text-danger">
						(<span></span>/52)
					</p>
				</div>
			</div>
			<div class="form-group row align-items-center">
				<label for="applyWorkTime"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label">신청시간</label>
				<div class="col-sm-8 col-md-9 col-lg-10">
					<input type="text" class="form-control" id="applyWorkTime"
						placeholder="">
				</div>
			</div>
			<div class="form-group row">
				<label for="reason"
					class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span
					class=" require"></span>사유</label>
				<div class="col-sm-8 col-md-9 col-lg-10">
					<textarea class="form-control" id="reason" rows="3"></textarea>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-secondary">임시저장</button>
			<button type="submit" class="btn btn-primary">신청</button>
			<button type="submit" class="btn btn-outline-primary modal-close"
				data-dismiss="modal">닫기</button>
		</div>
	</form>
</div>
<script type="text/javascript">
    $(function () {
	    $('#inputWorkDate').datetimepicker({
	        language: 'ko'
	    });
    });
</script>
