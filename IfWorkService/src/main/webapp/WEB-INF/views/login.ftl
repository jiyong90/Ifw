<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body class="login text-center">
    <form class="form-login" action="${rc.getContextPath()}${AUTH_CONFIG.getLoginFormSubmitEndpoint().getUrl()}" method="post">
        <!-- <img class="mb-4 logo" src="soldev/img/bootstrap-solid.svg" alt=""> -->
        <img class="mb-0 logo" src="${rc.getContextPath()}/soldev/img/gnb_logo_sm.png" alt="기업로고">
        <h1 class="h3 mb-3 font-weight-normal">이수시스템</h1>
        <#if companyList?exists && companyList?has_content>
        	<select id="enterCd" name="enterCd" class="form-control" style="margin:0 0 20px;">
        		<#list companyList as company>
        			<#list company?keys as key>
        			<option value="${key}">${company[key]}</option>
        			</#list>
        		</#list>
        	</select>
       	</#if>
        <label for="inputEmail" class="sr-only">아이디를 입력해주세요.</label>
        <input type="text" id="inputEmail" name="${AUTH_CONFIG.getLoginIdParameterName()}" class="form-control" placeholder="아이디를 입력해주세요." required="" autofocus="">
        <label for="inputPassword" class="sr-only">비밀번호를 입력해주세요.</label>
        <input type="password" id="inputPassword" name="${AUTH_CONFIG.getPasswordParameterName()}" class="form-control" placeholder="비밀번호를 입력해주세요." required="">
        <div class="checkbox mb-3">
            <label><input type="checkbox" value="remember-me"> 아이디 저장</label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        <#if certificateError??>
		<span style="color: red; font-style: italic;">${certificateError}</span><br>
		</#if>
        <p class="mt-5 mb-3 text-muted">Copyright © 2019 ISUSYSTEM.<br>All rights reserved.</p>
    </form>
    <#include "/metaScript.ftl">
    <script type="text/javascript">
        $(function () {
         

        });
    </script>
</body>
</html>
