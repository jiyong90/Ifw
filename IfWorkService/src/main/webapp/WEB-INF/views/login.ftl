<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
</head>
<body class="login text-center"  style="background-image:URL('${loginBackgroundImg}');">
    <form id="lForm" class="form-login" method="post" action="${userAuthorizationUri}" onsubmit="return updateValue()">
        <!-- <img class="mb-4 logo" src="soldev/img/bootstrap-solid.svg" alt=""> -->
        <img class="mb-0 logo" src="${loginLogoImg}" alt="기업로고">
        <!-- <h1 class="h3 mb-3 font-weight-normal">이수시스템</h1> -->
        
        <template v-if="loginForm!=null && loginForm.length>0">
        	<template v-for="f in loginForm">
        		<template v-if="f.type=='select'">
        			<div class="select-wrap mb-3">
			        	<label :for="f.key" class="sr-only">{{f.text}}</label>
			        	<select :id="f.key" :name="f.key" class="form-control" v-model="form[f.key]">
			        		<template v-if="f.hasOwnProperty('items')">
			        			<template v-for="i in f.items">
			        			<option v-for="(v,k) in i" :value="k">{{v}}</option>
			        			</template>
			        		</template>
			        	</select>
			        </div>
        		</template>
        		<template v-else>
	        		<label :for="f.key" class="sr-only">{{f.text}}</label>
	        		<input :type="f.type" :id="f.key" :name="f.key" class="form-control" v-model="form[f.key]" :placeholder="f.text" required="">
        		</template>
        	</template>
        </template>
        <template v-else>
        	<#if companyList?exists && companyList?has_content>
	        <div class="select-wrap mb-3">
	        	<label for="loginEnterCd" class="sr-only">회사명을 선택해주세요.</label>
	        	<select id="loginEnterCd" name="loginEnterCd" class="form-control" v-model="form['loginEnterCd']">
	        		<#list companyList as company>
	        			<#list company?keys as key>
	        			<option value="${key}">${company[key]}</option>
	        			</#list>
	        		</#list>
	        	</select>
	        </div>
	       	</#if>
	        <label for="inputEmail" class="sr-only">아이디를 입력해주세요.</label>
	        <input type="text" id="loginUserId" name="loginUserId" class="form-control" v-model="form['loginUserId']" placeholder="아이디를 입력해주세요." required="" autofocus="">
	        <label for="loginPassword" class="sr-only">비밀번호를 입력해주세요.</label>
	        <input type="password" id="loginPassword" name="loginPassword" class="form-control" v-model="form['loginPassword']" placeholder="비밀번호를 입력해주세요." required="" onkeyup="enterkey()" >
        </template>
        
        <input type="hidden" id="password" name="password" class="form-control" >
        <input type="text" id="username" name="username" class="form-control" value="" hidden>
        <!-- <input type="text" id="grant_type" name="grant_type" class="form-control" value="password" hidden>
        <input type="text" id="redirect_uri" name="redirect_uri" class="form-control" value="${redirect_uri}" hidden> -->
        <div class="checkbox mb-3 form-element">
            <input type="checkbox" value="remember-me" id="keepLogin">
            <label for="keepLogin">아이디 저장</label>
        	<#if interfaceYn?? && interfaceYn?exists && interfaceYn=='N' >
            <a href="#" class="find-pw" onclick="location.href='${rc.getContextPath()}/login/${tsId}/findPassword';">비밀번호 재설정</a>
        	</#if>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        <#if certificateError??>
		<span style="color: red; font-style: italic;">${certificateError}</span><br>
		</#if>
		<#if copyright?exists && copyright?has_content>
        <p class="mt-5 mb-3 text-muted">${copyright}</p>
        </#if>
    </form>
    <#include "/metaScript.ftl">
    <script type="text/javascript">
	    $(document).ready(function(){
			// 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
			var enterCd = localStorage.getItem('loginEnterCd');
			
			if(loginEnterCd!=null && loginEnterCd!='' && loginEnterCd!='undefined')
				$("#loginEnterCd").val(loginEnterCd); 
			
		    var key = getCookie("key");
		    $("#loginId").val(key); 
		     
		    if($("#loginEnterCd").val() != "" && $("#loginId").val() != ""){ // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
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
		    
		    $("#loginEnterCd").change(function(){
		     	localStorage.setItem('loginEnterCd', $('#loginEnterCd').val());
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
    	
    	function updateValue() {
   			var loginUserId = loginVue.form.loginUserId;
   			var loginEnterCd = loginVue.form.loginEnterCd;
   			var password = loginVue.form.loginPassword;
   		 	
		  	var username = "${tsId}@"+loginEnterCd+"@"+loginUserId;
   			
   			$("#password").val(password);
  		  	$("#username").val(username);
  		  	
    		return true;
    	}
    	/*
//    	function login() {
    	$("form").on("submit", function() {
    		  var action = "${userAuthorizationUri}?client_id=${tsId}&redirect_uri=${redirect_uri}&response_type=code&scope=read";
    		  
    		  $("#lForm").attr("action", action);

    		  return true;
//    		  var a = $("#loginEnterCd").val();
//    		  var b = $("#loginUserId").val();
//    		  var c = $("#username").val();
//    		  var d = $("#password").val();
//    		  
//    		  var redirect_uri = "${redirect_uri}";
//    		  var tsId = "${tsId}";
//    		  console.log(redirect_uri);
//    		  console.log(tsId);

//    		  $.ajax({
//		          url: "${userAuthorizationUri}" + "?loginEnterCd="+a+"&loginUserId="+b+"&username="+c+"&password="+d+"&client_id="+tsId+"&redirect_uri"+redirect_uri+"&response_type=code&scope=read&loginPassword="+d,
//		          type: "POST",
//		          contentType: "application/x-www-form-urlencoded"
//			 	});
   	    	});
    	//}
    	*/
    	var loginVue = new Vue({
    		el: "#lForm",
    	    data : {
    	    	loginForm : [],
    	    	form : {
    	    		loginUserId: '',
        	    	loginEnterCd: '',
        	    	loginPassword: ''
    	    	}
    	    },
    	    mounted: function(){
    	    	<#if loginForm?? && loginForm!='' && loginForm?exists >
					this.loginForm = JSON.parse("${loginForm?js_string}");
				</#if>
    	    }
    	});

    	
    	function enterkey() { 
    		if (window.event.keyCode == 13) { // 엔터키가 눌렸을 때
    			$("#lForm").submit();
    		} 
    	}
    </script>
</body>
</html>
