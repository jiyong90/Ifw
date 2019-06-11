<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body class="login text-center">
    <form class="form-login">
        <img class="mb-4 logo" src="soldev/img/bootstrap-solid.svg" alt="">
        <h1 class="h3 mb-3 font-weight-normal">이수시스템</h1>
        <label for="inputEmail" class="sr-only">아이디를 입력해주세요.</label>
        <input type="text" id="inputEmail" class="form-control" placeholder="아이디를 입력해주세요." required="" autofocus="">
        <label for="inputPassword" class="sr-only">비밀번호를 입력해주세요.</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="비밀번호를 입력해주세요." required="">
        <div class="checkbox mb-3">
            <label><input type="checkbox" value="remember-me"> 아이디 저장</label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        <p class="mt-5 mb-3 text-muted">Copyright © 2019 ISUSYSTEM. All rights reserved</p>
    </form>
    <script type="text/javascript">
        $(function () {
         

        });
    </script>
</body>
</html>
