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
        <div class="content inner-wrap">
            <div class="inner-wrap">
                <h2 class="title">연장근무신청<br>
                    <span class="point-color">(* 빨간 색으로 표기된 항목은 필수 입력 항목입니다.)</span>
                </h2>
                <div class="">
                    <form>
                        <div class="form-group row">
                            <label for="inputTitle" class="col-sm-2 col-form-label">제목</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="inputTitle" placeholder="연장근무신청">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputWorkDay" class="col-sm-2 col-form-label"><span class=" necessary"></span>근무일</label>
                            <div class="col-sm-5">
                                <div class="input-group date" id="datetimepicker" data-provide="datepicker">
                                    <input type="text" class="form-control " id="inputWorkDate" placeholder="" 
                                        required="" value="" >
                                    <div class="input-group-append" >
                                        <span class="input-group-text " id="inputWorkDateAppend"><i class="far fa-calendar-alt"></i></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-5"></div>
                        </div>
                        <div class="form-group row">
                            <label for="workTime" class="col-sm-2 col-form-label">근무시간</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="inputPassword" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="">
                            </div>
                            <div class="col-sm-1 text-center">~</div>
                            <div class="col-sm-2 ">
                                <input type="text" class="form-control" id="inputPassword" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="">
                            </div>
                            <div class="col-sm-5"></div>
                        </div>
                        <div class="form-group row">
                            <label for="inputWorkTime01" class="col-sm-2 col-form-label">출근시간 전 (조출)</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="inputWorkTime01" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="">
                            </div>
                            <div class="col-sm-1 text-center">~</div>
                            <div class="col-sm-2 ">
                                <input type="text" class="form-control" id="inputWorkTime02" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="">
                            </div>
                            <div class="col-sm-5"></div>
                        </div>
                        <div class="form-group row">
                            <label for="inputWorkTime01" class="col-sm-2 col-form-label">퇴근시간 후 (잔업)</label>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="inputWorkTime01" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="">
                            </div>
                            <div class="col-sm-1 text-center">~</div>
                            <div class="col-sm-2 ">
                                <input type="text" class="form-control" id="inputWorkTime02" placeholder=""
                                    aria-describedby="inputGroupPrepend" required="">
                            </div>
                            <div class="col-sm-5"></div>
                        </div>
                        <div class="form-group row">
                            <label for="inputWorkDay" class="col-sm-2 col-form-label">잔여연장가능시간</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="" placeholder="" readonly>
                            </div>
                            <label for="inputWorkDay" class="col-sm-2 col-form-label">신청시간</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" id="" placeholder="" >
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="reason" class="col-sm-2 col-form-label"><span class=" necessary"></span>사유</label>
                            <div class="col-sm-10">
                                <textarea class="form-control" id="reason" rows="3"></textarea>
                            </div>
                        </div>
                        <div class="row btn-wrap">
                            <div class="col-sm-12 text-center">
                                <button type="submit" class="btn btn-secondary">임시저장</button>
                                <button type="submit" class="btn btn-primary">신청</button>
                                <button type="submit" class="btn btn-outline-primary">닫기</button>
                            </div>
                        </div>
                    </form>
                </div>
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
