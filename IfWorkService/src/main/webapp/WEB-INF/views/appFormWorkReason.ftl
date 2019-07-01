<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body class="">
    <!-- <div class="navbar navbar-expand-lg navbar-light bg-light">
        <nav class="container">
            <h1>신청 - 공통 레이아웃</h1>
            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </nav>
    </div> -->
    <div class="container-fluid">
    	<div class="container">
    		<div class="content inner-wrap">
            	<form class="application-form">
                    <div class="modal-header">
                        <h5 class="modal-title">신청서</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <h2 class="title">근태 사유서 신청<br>
                            <span class="sub-desc point-color">(* 표기된 항목은 필수 입력 항목입니다.)</span>
                        </h2>
                        <div class="form-group row align-items-center">
                            <label for="title" class="col-sm-4 col-md-3 col-lg-2 col-form-label">제목</label>
                            <div class="col-sm-8 col-md-9 col-lg-10">
                                <input type="text" class="form-control" id="title" placeholder="근태 사유서 신청">
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="workDay" class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span
                                    class=" require"></span>근무일</label>
                            <div class="col-sm-5 col-md-5 col-lg-5">
                                <div class="input-group date" id="workDay" data-provide="datepicker">
                                    <input type="text" class="form-control " id="inputWorkDate" placeholder=""
                                        required="" value="">
                                    <div class="input-group-append">
                                        <span class="input-group-text " id="inputWorkDateAppend"><i
                                                class="far fa-calendar-alt"></i></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="planWorkTime" class="col-sm-4 col-md-3 col-lg-2 col-form-label">계획근무시간</label>
                            <div class="col-sm-5 col-md-5 col-lg-5">
                                <input type="text" class="form-control" id="planWorkTime" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="" readonly>
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="inputWorkTime" class="col-sm-4 col-md-3 col-lg-2 col-form-label">입력근무시간</label>
                            <div class="col-sm-5 col-md-5 col-lg-5">
                                <input type="text" class="form-control" id="inputWorkTime" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="" readonly>
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="changeStartWorktime" class="col-sm-4 col-md-3 col-lg-2 col-form-label">변경출근시각</label>
                            <div class="col-sm-5">
                                <div class="input-group date" data-provide="datepicker">
                                    <input type="text" class="form-control " id="changeStartWorktime" placeholder=""
                                        required="" value="">
                                    <div class="input-group-append">
                                        <span class="input-group-text " id="changeStartWorktimeAppend"><i
                                                class="far fa-clock"></i></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="changeEndWorktime" class="col-sm-4 col-md-3 col-lg-2 col-form-label">변경퇴근시각</label>
                            <div class="col-sm-5">
                                <div class="input-group date" data-provide="datepicker">
                                    <input type="text" class="form-control " id="changeEndWorktime" placeholder=""
                                        required="" value="">
                                    <div class="input-group-append">
                                        <span class="input-group-text " id="changeEndWorktimeAppend"><i
                                                class="far fa-clock"></i></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="reason" class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span class="require"></span>사유</label>
                            <div class="col-sm-8 col-md-9 col-lg-10">
                                <textarea class="form-control" id="reason" rows="3"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-secondary">임시저장</button>
                        <button type="submit" class="btn btn-primary">신청</button>
                        <button type="submit" class="btn btn-outline-primary modal-close">닫기</button>
                    </div>
                </form>
        	</div>
    	</div>
    </div>
    <script type="text/javascript">
        $(function () {
                $('#inputWorkDate').datetimepicker({
                    language: 'ko'
                });

        });
    </script>
</body>

</html>
