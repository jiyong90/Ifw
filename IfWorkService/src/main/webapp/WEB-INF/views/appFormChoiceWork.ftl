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
                        <h2 class="title">선택근무제 신청<br>
                            <span class="sub-desc point-color">(* 표기된 항목은 필수 입력 항목입니다.)</span>
                        </h2>
                        <div class="form-group row align-items-center">
                            <label for="workStartDay" class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span
                                    class=" require"></span>선택근무제 시작일</label>
                            <div class="col-sm-3 col-md-4 col-lg-4">
                                <div class="input-group date"  data-provide="datepicker">
                                    <input type="text" class="form-control " id="workStartDay" placeholder=""
                                        required="" value="">
                                    <div class="input-group-append">
                                        <span class="input-group-text " id="workStartDayAppend"><i
                                                class="far fa-calendar-alt"></i></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-1 text-center">~</div>
                            <div class="col-sm-3 col-md-4 col-lg-4">
                                <div class="input-group date" data-provide="datepicker">
                                    <input type="text" class="form-control " id="workStartDay" placeholder=""
                                        required="" value="" readonly>
                                    <div class="input-group-append">
                                        <span class="input-group-text " id="workStartDayAppend"><i
                                                class="far fa-calendar-alt"></i></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="period" class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span class="require"></span>적용기간</label>
                            <div class="col-sm-5 col-md-5 col-lg-5">
                                <select class="form-control">
                                    <option>Default select</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group row align-items-center">
                            <label for="sumWorkTime" class="col-sm-4 col-md-3 col-lg-2 col-form-label"><span class="require"></span>총 근로시간</label>
                            <div class="col-sm-4 col-md-4 col-lg-4">
                                <input type="text" class="form-control" id="sumWorkTime" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="" readonly>
                            </div>
                            <div class="col-sm-2">시간</div>
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
    <#include "/metaScript.ftl">
    <script type="text/javascript">
        $(function () {
                $('#inputWorkDate').datetimepicker({
                    language: 'ko'
                });

        });
    </script>
</body>

</html>
