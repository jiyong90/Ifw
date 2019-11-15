<#include "/websocket.ftl">
<nav class="navbar navbar-expand-lg navbar-light">
    <!-- collapse button -->
    <!-- <button type="button" id="sidebarCollapse" class="btn btn-info">
        <i class="fas fa-bars"></i>
    </button> -->

    <h1 class="logo">
    <a href="${rc.getContextPath()}/console/${tsId}/">
        <img src="${mainLogoImg}" alt="로고">
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
<div class="modal fade show" id="modalExample" tabindex="-1" role="dialog">
<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content rounded-0">
        <div class="modal-header">
            <h5 class="modal-title">{{title}}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="modal-body">
            <form class="needs-validation" novalidate>
                <div class="modal-app-wrap">
                {{contents}}
                </div>
                <div class="btn-wrap text-center">
                    <button type="button" class="btn btn-secondary rounded-0" data-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-default rounded-0">확인</button>
                </div>
            </form>
        </div>
    </div>
</div>
</div>
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
					if(data!=null && data.status=='OK') {
						//console.log(data);
						$this.inboxCount = data.inboxCount;
						$this.inboxCount += data.apprCount;
						
						if(data.workPlan) {
							if(data.workPlan.hasOwnProperty("toDoPlanDays") && Number(data.workPlan.toDoPlanDays)!=0)
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
					//console.log(paramMap);
					//console.log(data);
					$this.inboxCount = 1;
					$this.title = data.title;
					$this.contents = data.contents;
					alert("알림도착 : " + $this.title);
				}
			}
		}
	});

	connect("/api/${tenantId}/${enterCd}/${empNo}/navTop", navTopVue.webSocketCallback);
	
});
</script>