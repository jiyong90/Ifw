<#include "/websocket.ftl">
<nav class="navbar navbar-expand-lg navbar-light">
    <!-- collapse button -->
    <!-- <button type="button" id="sidebarCollapse" class="btn btn-info">
        <i class="fas fa-bars"></i>
    </button> -->

    <h1 class="logo">
    <a href="${rc.getContextPath()}/console/${tsId}/">
        <img src="${rc.getContextPath()}/soldev/img/gnb_logo.png" alt="로고">
        </a>
    </h1>
    <div id="navTop" class="ml-auto">
        <ul class="nav gnb-nav-wrap">
            <li class="nav-item">
                <a class="" href="#"><span class="ico-wrap"><i class="sp_ico calendar">&#xe900;</i></span></a>
            </li>
            <li class="nav-item">
            	<a href="#" id="alertLink"><span class="ico-wrap"><i class="sp_ico alarm">&#xe802;</i><span :class="{'new':inboxCount > 0}"></span></span></a>
            </li>
            <li class="nav-item">
                <a class="" href="${rc.getContextPath()}/logout/${tsId}"><span class="ico-wrap"><i class="sp_ico power">&#xe801;</i></span></a>
            </li>
            <!-- <li class="nav-item">
                <a class="nav-link" href="#"><i class="fas fa-power-off"></i></a>
            </li> -->
        </ul>
    </div>
</nav>
<script type="text/javascript">

$(document).ready(function(){
	var navTopVue = new Vue({
		el : '#navTop',
		data : {
			inboxCount : '',
			data : []
		},
		mounted : function() {
			var $this = this;
			Util.ajax({
				url: "${rc.getContextPath()}/noti/inbox/count",
				type: "GET",
				contentType : 'application/json',
				dataType : "json",
				success: function(data) {
					//console.log(data);
					
					if(data!=null && data.status=='OK') {
						$this.inboxCount = data.inboxCount;
						//$this.data = data;
						
						if(data.workPlan) {
							$this.inboxCount++;
							inboxVue.inboxCount = $this.inboxCount;
							inboxVue.workPlan = data.workPlan;
						}
					}
					
				}, error: function(data) { alert(data.message); }
			})
		},
		methods : {
			webSocketCallback : function(paramMap){
				var $this = this;
				if(paramMap.body){
					var data = JSON.parse(paramMap.body);
					console.log(paramMap);
					console.log(data);
					$this.inboxCount = 1;
					$this.title = data.title;
					alert("알림도착 : " + $this.title);
				}
			}
		}
	});

	connect("/api/${tenantId}/${enterCd}/${empNo}/navTop", navTopVue.webSocketCallback);
	
});
</script>