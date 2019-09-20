<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
	<!-- 근태 for HR custom  -->
	<link rel="stylesheet" href="${rc.getContextPath()}/soldev/css/layout4HR.css">
</head>
<body>
<script> var loginUrl = "http://smarthrd.servicezone.co.kr/Info.do?code=905"; </script>
    <div class="wrapper">
        <div id="content">
            <#include "/${pageName}.ftl">
            <!-- <#include "/footer.ftl"> -->
        </div>
    </div>
</body>

</html>
