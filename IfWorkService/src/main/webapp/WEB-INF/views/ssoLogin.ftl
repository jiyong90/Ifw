<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/scriptZip.ftl">
</head>
<body>
	
	<form id="lForm" class="form-login" method="post" action="${userAuthorizationUri}">
		<div id="loginDiv"> 
			<template v-for="(v, k) in paramMap">
				<input type="hidden" :name="k" class="form-control" :value="v" />
			</template>
		</div>
	</form>
	
    <script type="text/javascript">
	    $(document).ready(function(){
	    	
	    	var authRuleVue = new Vue({
	   			el: "#loginDiv",
	   		    data : {
	   	 			paramMap: {}
	   	  		},
	   	  		mounted: function(){
		   	  		//권한별 기능
	   	 			this.paramMap = JSON.parse("${loginParam?js_string}");
	   	 			this.$nextTick(function() {
	   	 				$("#lForm").submit();
	   	 			});
	   	  		}
	    	});
	    	 
	    });
    </script>
</body>
</html>
