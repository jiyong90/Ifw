<aside id="alertSidebar" class="control-sidebar" v-clock>
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
		 <li v-if="inboxList.length==0 && apprCount==0">
	         <span class="ico-wrap"><i class="far fa-bell-slash"></i></span>
	         <div class="desc">
	             알림을 모두 확인했습니다.
	         </div>
	     </li>
         <li v-if="apprCount != 0">
        	<span class="status SELE_F"></span>
        	<div class="desc">결재할 문서가 <a href="#" onclick="location.href='${rc.getContextPath()}/${type}/${tsId}/views/approvalList?applType=02';">{{apprCount}}건</a> 있습니다. </div>
       	</li>
        <li v-for="(f, fIdx) in inboxList">
        	<span class="status ELAS"></span>
        	<div class="desc">
        		{{f.title}} <template v-if="f.contents">[{{f.contents}}]</template>
        	</div>
        	<button class="btn-close" @click="notiCheckYn(f.id)">&#215;</button>
        </li>
	</ul>
</aside>

<script type="text/javascript">
	$(function(){
		$('#alertLink').on('click', function () {
			inboxVue.getInboxList();
		    $('#alertSidebar').toggleClass('active');
		});
		
		$('.alert-list-wrap').slimScroll({
	        height: '100%'
	    });
	});

	var inboxVue = new Vue({
		el : '#alertSidebar',
		data : {
			inboxCount: 0,
			apprCount: 0,
			inboxList : [],
			workPlan: {}
		},
		watch: {
			/* workPlan : function(val, oldVal) {
				//근무계획작성 버튼 컨트롤
				if(val.toDoPlanDays && Number(val.toDoPlanDays)!=0) {
					if($("#workPlanBtn").length || $("#workPlanBtn").length==1)
						workPlanYn = true;
				} else {
					if(!$("#workPlanBtn").length || $("#workPlanBtn").length==0)
						workPlanYn = false;
				}
					
			} */
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
							$this.inboxList = response.inboxList;
							$this.apprCount = response.apprCount;
						}
					}, 
					error: function(e) { 
						console.log(e);
					}
				});
			},
			goToWorkTimeCalendar: function(){
				modalVue.getPlanFlexitimeList('N');
				//location.href='${rc.getContextPath()}/console/${tsId}/views/workCalendar?calendarType=Day&date='+moment(this.workPlan.sYmd).format('YYYYMMDD');
			},
			notiCheckYn: function(id){
				var $this = this;
				var param = {
	       				id : id
	   		    	};
				
				Util.ajax({
					url: "${rc.getContextPath()}/noti/inbox/check",
					type: "POST",
					data: JSON.stringify(param),
					contentType : 'application/json',
					dataType : "json",
					success: function(response) {
						$this.getInboxList();
					}, 
					error: function(e) { 
						console.log(e);
					}
				});
			},
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

