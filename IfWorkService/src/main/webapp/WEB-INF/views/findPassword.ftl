<!DOCTYPE html>
<html lang="ko">
<head>
	<#include "/metadata.ftl">
	<#include "/metaScript.ftl">
</head>
<body class="changePW text-center" style="background-image:URL('${loginBackgroundImg}');">
	<!-- alert modal start -->
	<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" style="z-index:1600;">
        <div class="modal-dialog " role="document">
            <div class="modal-content">
                <!-- <div class="modal-header">
    	                <h5 class="modal-title"></h5>
    	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
    	                    <span aria-hidden="true">&times;</span>
    	                </button>
    	            </div> -->
                <div class="modal-body">
                    <p id="alertText" class="text-center"></p>
                </div>
                <div class="modal-body text-center">
                    <button type="button" class="btn btn-default" data-dismiss="modal">확인</button>
                </div>
            </div>
        </div>
    </div>
    <!-- alert modal end -->
    <form class="form-changePW" id="changPw">
    	<h1 class="h3 mb-3 title">비밀번호 변경하기</h1>
    	<#if companyList?exists && companyList?has_content>
    	<div class="select-wrap mb-3">
        	<label for="enterCd" class="sr-only">회사명을 선택해주세요.</label>
        	<select id="enterCd" class="form-control">
        		<#list companyList as company>
        			<#list company?keys as key>
        			<option value="${key}">${company[key]}</option>
        			</#list>
        		</#list>
        	</select>
        </div>
       	</#if>
    	<div class="row no-gutters">
            <#if passwordCertificate?? && passwordCertificate?exists && passwordCertificate=='PHONE' >
            	<label for="userid" class="sr-only">핸드폰번호를 입력해주세요.</label>
            	<div class="col-8 pr-1">
	        		<input type="text" id="userid" class="form-control" v-model="userInfo" placeholder="핸드폰번호 (-포함하여 입력)">
	        	</div>
	        <#else>
	        	<label for="userid" class="sr-only">이메일을 입력해주세요.</label>
	        	<div class="col-8 pr-1">
	        		<input type="text" id="userid" class="form-control" v-model="userInfo" placeholder="이메일">
	        	</div>
	        </#if>
	        <div class="col-4 pl-1">
	        	<button type="button" class="btn btn-lg btn-dark btn-code btn-block btn-send" @click="userInfoConfirm">인증코드받기</button>
	        </div>
        </div>
        <label for="inputCode" class="sr-only">인증코드</label>
        <input type="text" id="code" class="form-control" v-model="confirmCode" placeholder="인증코드" required="">
        <label for="password" class="sr-only">새 비밀번호를 입력해주세요.</label>
        <input type="password" id="password" class="form-control" v-model="password" placeholder="새 비밀번호" required="">
        <label for="passwordChk" class="sr-only">새 비밀번호 확인</label>
        <input type="password" id="passwordChk" class="form-control mb-0" v-model="confirmPw" placeholder="새 비밀번호 확인" required="">
        <div class="btn-wrap row no-gutters">
            <div class="col-6 pr-1">
                <button type="button" class="btn btn-lg btn-light btn-cancel btn-block" @click="location.href='${rc.getContextPath()}/login/${tsId}';">취소</button>
            </div>
            <div class="col-6 pl-1">
                <button type="button" class="btn btn-lg btn-primary btn-change btn-block" @click="checkPw">변경하기</button>
            </div>
        </div>
    </form>
    <#if copyright?exists && copyright?has_content>
       <p class="mt-5 mb-3 text-muted">${copyright}</p>
    </#if>
  <script>
	var changPwVue = new Vue({
	    el: '#changPw',
	    data: {
	    	passwordCertificate:''
	    	,userInfo:''
			,confirmCode:''
			,codeChk:false
			,password:''
			,confirmPw:''
		},
		mounted: function(){
			<#if passwordCertificate?? && passwordCertificate?exists >
				this.passwordCertificate = "${passwordCertificate}";
			</#if>
		},
	    methods: {
	    	userInfoConfirm : function(){
 				var $this = this;
 				
 				if($("#enterCd").val()==""){
					$('#alertText').html("회사를 선택해 주세요.");
					$('#alertModal').modal("show");
					return false;
    			}

    			if($this.userInfo==""){
					$('#alertText').html($this.passwordCertificate=='PHONE'?"핸드폰번호를 입력해 주세요.":"이메일을 입력해 주세요.");
					$('#alertModal').modal("show");
					return false;
    			}
    			
    			if(($this.passwordCertificate=='PHONE' && !$this.validatePhone($this.userInfo)) 
    				|| ($this.passwordCertificate!='PHONE' && !$this.validateEmail($this.userInfo)) ) {
    				return;
    			} 
    			
    			var paramMap = {
    				enterCd : $("#enterCd").val(),
    				userInfo : $this.userInfo
	    		}; 
 				
	    		Util.ajax({
	    			type : "GET",
	    			url : "${rc.getContextPath()}/login/${tsId}/check/user/info",
	    			contentType: 'application/json',
	    		    dataType:"json",
					data: paramMap,
	    			success : function(response){
	    				if(!response){ 
	    					$('#alertText').html("등록된 정보가 없습니다.");
	    					$('#alertModal').modal("show");
	    					return false;
	    					
	    				} else { //가입정보 있음
		    		    	$this.sendCode();
	    				} 
	    			},
	    			error:function(e){
	    				alert(e.responseText);
	    			}
	    		});
   			
			},
			sendCode : function(){
				var $this = this;
				
				var paramMap = {
    				enterCd : $("#enterCd").val(),
    				userInfo : $this.userInfo
	    		}; 
				
				Util.ajax({
	    			type : "POST",
        			url : "${rc.getContextPath()}/login/${tsId}/sendCertificateCodeForChangePw",
        			contentType: 'application/json',
        		    dataType:"json",
        		    data : JSON.stringify(paramMap),
        			success : function(response){
	    				if(response.status!="OK"){
	    					$('#alertText').html(response.message);
	    					$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
							});
	    					$('#alertModal').modal("show");
	    					return false;
	    				}else{
	    					$('#alertText').html("인증코드가 발송되었습니다.");
	    					$("#alertModal").on('hidden.bs.modal',function(){
								$("#alertModal").off('hidden.bs.modal');
								$('#userid').attr("readonly", true);   
								$(".btn-send").text("인증코드재발송");
							});
	    					$('#alertModal').modal("show");
	    				}
        				
        			},
	    			error:function(e){
	    				alert(e.responseText);
	    			}
	    		});
			},
			checkPw : function(){
 				var $this = this;
 				
    			if($this.userInfo==""){
					$('#alertText').html($this.passwordCertificate=='PHONE'?"핸드폰번호를 입력해 주세요.":"이메일을 입력해 주세요.");
					$('#alertModal').modal("show");
					return false;
    			}

    			if(($this.passwordCertificate=='PHONE' && !$this.validatePhone($this.userInfo)) 
       				|| ($this.passwordCertificate!='PHONE' && !$this.validateEmail($this.userInfo)) ) {
       				return;
       			} 
    			
    			if($this.confirmCode==""){
					$('#alertText').html("인증코드를 입력해주세요.");
					$('#alertModal').modal("show");
					return false;
    			}
    			
    			if($this.password==""){
    				$('#alertText').html("비밀번호를 입력해주세요.");
					$('#alertModal').modal("show");
					return false;
    			}
    			
    			if(!/^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$^*+=-_|\?<>()~`]).{8,16}$/.test($this.password)){
    				$('#alertText').html("8~16자의 영문 대소문자, 숫자 및 특수문자를 사용해야 합니다.");
					$('#alertModal').modal("show");
					return false;
    			}

    			if($this.confirmPw==""){
    				$('#alertText').html("비밀번호가 확인해주세요.");
					$('#alertModal').modal("show");
					return false;
    			}
    			
    			if($this.confirmPw!=$this.password){
    				$('#alertText').html("비밀번호가 일치하지 않습니다.");
					$('#alertModal').modal("show");
					return false;
    			}

    			$this.changePw();
			},
			changePw : function(){
				var $this = this;
				var paramMap = {
					userInfo : $this.userInfo
					,enterCd : $("#enterCd").val()
    				,otp : $this.confirmCode
    				,password : $this.password 
	    		}; 
				
	    		Util.ajax({
	    			type : "POST",
	       			url : "${rc.getContextPath()}/login/${tsId}/changePassword",
	       			contentType: 'application/json',
	       		    dataType:"json",
	       		    data : JSON.stringify(paramMap),
	       			success : function(response){
	       				if(response.status!=null && response.status=='OK') {
		       				$('#alertText').html("비밀번호가 변경되었습니다.");
		   					$('#alertModal').modal("show");
		   					$('#alertModal').on('hide.bs.modal', function () {
		   						$("#alertModal").off('hidden.bs.modal');
		   	   					location.href='${rc.getContextPath()}/login/${tsId}';
		   					});
	       				} else {
	       					$('#alertText').html(response.message);
		   					$('#alertModal').modal("show");
		   					$('#alertModal').on('hide.bs.modal', function () {
		   						$("#alertModal").off('hidden.bs.modal');
		   					});
	       				}
	       			},
	    			error:function(e){
	    				alert(e.responseText);
	    			}
	    		});
			},
    		validateEmail : function(mail){
    		 	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)){
    		    		return (true);
    		 	}
    		 	$('#alertText').html("이메일 형식이 맞지 않습니다.");
				$('#alertModal').modal("show");
    		    return (false);
    		},
    		validatePhone : function(phone){
    			if (/^\d{3}-\d{3,4}-\d{4}$/.test(phone)){
		    		return (true);
			 	}
			 	$('#alertText').html("핸드폰번호 형식이 맞지 않습니다.<br>- 를 포함한 숫자만 입력하세요.");
				$('#alertModal').modal("show");
			    return (false);
    		}
	    }
	});
    </script>
</body>

</html>
