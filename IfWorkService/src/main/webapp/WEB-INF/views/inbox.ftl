<aside id="alertSidebar" class="control-sidebar" style="display:none;" v-clock>
	<ul class="alert-list-wrap" v-if="inboxCount>0">
		<li v-if="workPlan.toDoPlanDays && Number(workPlan.toDoPlanDays)!=0">
            <span class="status SELE_F"></span>
            <div class="desc">
            	선근제 신청이 완료되었습니다.<br>
                            출,퇴근시간을 지정해주세요.
            </div>
            <div class="btn-wrap">
                <button type="button" class="btn btn-sm btn-inline btn-outline-secondary" @click="goToWorkTimeCalendar">작성하기</button>
            </div>
        </li>
	</ul>
	<ul class="alert-list-wrap" v-else>
		<li>
           <span class="ico-wrap"><i class="far fa-bell-slash"></i></span>
           <div class="desc">알림을 모두 확인했습니다.</div>
        </li>
	</ul>
</aside>

<script type="text/javascript">
	$(function(){
		$('#alertLink').on('click', function () {
		    $('#alertSidebar').toggle('active');
		});
	});

	var inboxVue = new Vue({
		el : '#alertSidebar',
		data : {
			inboxCount: 0,
			inboxList : [],
			workPlan: {}
		},
		mounted : function() {
			this.getInboxList();
		},
		methods : {
			getInboxList: function(){
				var $this = this;
				Util.ajax({
					url: "${rc.getContextPath()}/noti/inbox/list",
					type: "GET",
					contentType : 'application/json',
					dataType : "json",
					success: function(response) {
						if(response!=null && response.status=='OK') {
							$this.inboxList = response.indboxList;
						}
					}, 
					error: function(e) { 
						console.log(e);
					}
				});
			},
			goToWorkTimeCalendar: function(){
				location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Day&date='+moment(this.workPlan.sYmd).format('YYYYMMDD');
			}
			/* ,
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
			} */
		}
	});
	
	//connect("/api/${tenantId}/${enterCd}/${empNo}/noti", inboxVue.webSocketCallback);
</script>

