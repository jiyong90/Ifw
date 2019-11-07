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
                        <div class="icon-wrap mb-4">
                            <i class="far fa-sad-tear"></i>
                        </div>
                        <p class="desc" id="message">
                           
                        </p>
<!--     <a class="btn-main" href="#" title="페이지 메인으로 이동" target="_blank">메인으로 가기&nbsp;&#10095;</a>-->
                    </div>
                </section>
            </div>

<script>
var result;

if (${status} == "100") 
	result = "테넌트가 존재핮 않습니다. url을 확인해주세요.";
else if(${status} == "120")
	result = "HR 로그인을 다시 하신 후 다시 시도해주세요.";
else if(${status} == "120")
	result = "사용자 정보가 존재하지 않습니다.";
else
	result = "뿅";
document.getElementById("message").innerHTML=result;
</script>
</body>
</html>

