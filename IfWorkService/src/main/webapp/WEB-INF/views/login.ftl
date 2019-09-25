<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
    <title>근태관리 시스템</title>
</head>
<body class="login text-center" style="background-image:URL('${loginBackgroundImg}');">
    <form class="form-login" action="${rc.getContextPath()}${AUTH_CONFIG.getLoginFormSubmitEndpoint().getUrl()}" method="post">
        <!-- <img class="mb-4 logo" src="soldev/img/bootstrap-solid.svg" alt=""> -->
        <img class="mb-0 logo" src="${loginLogoImg}" alt="기업로고">
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
        <input type="text" id="loginId" name="${AUTH_CONFIG.getLoginIdParameterName()}" class="form-control" placeholder="아이디를 입력해주세요." required="" autofocus="">
        <label for="inputPassword" class="sr-only">비밀번호를 입력해주세요.</label>
        <input type="password" id="inputPassword" name="${AUTH_CONFIG.getPasswordParameterName()}" class="form-control" placeholder="비밀번호를 입력해주세요." required="">
        <div class="checkbox mb-3">
            <label><input type="checkbox" id="keepLogin" value="remember-me"> 아이디 저장</label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        <#if certificateError??>
		<span style="color: red; font-style: italic;">${certificateError}</span><br>
		</#if>
        <p class="mt-5 mb-3 text-muted">Copyright © 2019 ISUSYSTEM.<br>All rights reserved.</p>
    </form>
    <#include "/metaScript.ftl">
    <script type="text/javascript">
	    $(document).ready(function(){
			// 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
			var enterCd = localStorage.getItem('enterCd');
			
			if(enterCd!=null && enterCd!='' && enterCd!='undefined')
				$("#enterCd").val(enterCd); 
			
		    var key = getCookie("key");
		    $("#loginId").val(key); 
		     
		    if($("#enterCd").val() != "" && $("#loginId").val() != ""){ // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
		        $("#keepLogin").attr("checked", true); // ID 저장하기를 체크 상태로 두기.
		    }
		     
		    $("#keepLogin").change(function(){ // 체크박스에 변화가 있다면,
		        if($("#keepLogin").is(":checked")){ // ID 저장하기 체크했을 때,
		        	//console.log('setCookie!');
		            setCookie("key", $("#loginId").val(), 7); // 7일 동안 쿠키 보관
		        }else{ // ID 저장하기 체크 해제 시,
		            deleteCookie("key");
		        }
		    });
		    
		    $("#enterCd").change(function(){
		     	localStorage.setItem('enterCd', $('#enterCd').val());
		    });
		    	
		    // ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
		    $("#loginId").keyup(function(){ // ID 입력 칸에 ID를 입력할 때,
		        if($("#keepLogin").is(":checked")){ // ID 저장하기를 체크한 상태라면,
		            setCookie("key", $("#loginId").val(), 7); // 7일 동안 쿠키 보관
		        }
		    });
		    
		});
	    
	    function setCookie(cookieName, value, exdays){
    	    var exdate = new Date();
    	    exdate.setDate(exdate.getDate() + exdays);
    	    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    	    document.cookie = cookieName + "=" + cookieValue;
    	}
    	 
    	function deleteCookie(cookieName){
    	    var expireDate = new Date();
    	    expireDate.setDate(expireDate.getDate() - 1);
    	    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
    	}
    	 
    	function getCookie(cookieName) {
    	    cookieName = cookieName + '=';
    	    var cookieData = document.cookie;
    	    var start = cookieData.indexOf(cookieName);
    	    var cookieValue = '';
    	    if(start != -1){
    	        start += cookieName.length;
    	        var end = cookieData.indexOf(';', start);
    	        if(end == -1)end = cookieData.length;
    	        cookieValue = cookieData.substring(start, end);
    	    }
    	    return unescape(cookieValue);
    	}
    </script>
</body>
</html>
