<div id="401redirectHrPage">
<div class="container-fluid pt-3 pb-3 bg-white">
<div class="ibsheet-wrapper">
<span id="message"></span>
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
</div>
</div>
</div>