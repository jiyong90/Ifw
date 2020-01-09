<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body style="background-color: #e2e5ed;" >

<div id="401redirectHrPage">
<div class="container-fluid">
                <section class="error-page">
                    <div class="container">
                        <div class="icon-wrap mb-5">
                            <i class="far fa-sad-tear"></i>
                        </div>
                        <p class="desc" id="message">
                           
                        </p>
<!--     <a class="btn-main" href="#" title="페이지 메인으로 이동" target="_blank">메인으로 가기&nbsp;&#10095;</a>-->
                    </div>
                </section>
            </div>

<script>
var result = "일시적으로 서비스를 사용할 수 없는 상태입니다.";

if (${status} == "100") 
	result = "테넌트 ID가 존재하지 않습니다. url을 확인해주세요.";
else if(${status} == "120")
	result = "HR 재로그인 후 다시 시도해주세요.";
else if(${status} == "130")
	result = "사용자 정보가 존재하지 않습니다.";
else if(${status} == "140")
	result = "세션이 만료되었습니다.";
else if(${status} == "150")
	result = "세션이 만료되었습니다.";
else if(${status} == "160")
	result = "잘못된 호출 URL입니다.";
else if(${status} == "170")
	result = "클라이언트가 존재하지 않습니다.";
else if(${status} == "180")
	result = "인증 URL이 존재하지 않습니다.";
document.getElementById("message").innerHTML=result;
</script>
</body>
</html>

