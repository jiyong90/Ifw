<div id="401redirectHrPage">
<div class="container-fluid pt-3 pb-3 bg-white">
<div class="ibsheet-wrapper">
<span id="message"></span>
<script>
var result;

if (${status} == "100") 
	result = "테넌트가 존재핮 않습니다. url을 확인해주세요.";
else
	result = "에러";

document.getElementById("message").innerHTML=result;
</script>
</div>
</div>
</div>