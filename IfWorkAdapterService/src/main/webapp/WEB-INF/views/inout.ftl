<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>근태관리</title>
    <link rel="stylesheet" href="${rc.getContextPath()}/company/hyundaiNGV/assets/css/reset.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/company/hyundaiNGV/assets/css/style.css">
    <#include "/metaScript.ftl">
    
</head>
<body>
	<div id="topDiv">
    <div class="wrapper work-off" v-if="inoutType=='HOL' || inoutType=='IN' ">
        <div class="inner-wrapper">
            <div class="ico-wrap">
                <img src="${rc.getContextPath()}/company/hyundaiNGV/assets/img/icon_workday.png" alt="">
            </div>
            <div class="contents-wrap">
                <p class="date">{{formattedYmd}}</p>
                <p class="desc">오늘은 <strong>{{desc}}</strong> 입니다.</p>
                <!-- <a href="#" class="link">근무시간 관리 시스템 바로가기</a> -->
            </div>
            <div class="btn-wrap">
                <button :class="{ 'btn':true, 'btn-on': !isHol }" @click="clickIn">출근하기</button>
            </div>
        </div>
    </div>
    
    <div class="wrapper work-day" v-if="inoutType=='END' || inoutType=='OUT' ">
        <div class="top-bg">
            <div class="inner-wrapper">
                <div class="ico-wrap">
                    <img src="${rc.getContextPath()}/company/hyundaiNGV/assets/img/icon_workon.png" alt="">
                </div>
                <div class="info-wrap">
                    <p class="date">{{formattedYmd}}</p>
                    <p class="desc"><strong>{{desc}}</strong> 입니다.</p>
                </div>
            </div>
        </div>
        <div class="inner-wrapper">
            <div class="contents-wrap" v-if="entrySdate">
                <span class="title">출근</span>
                <span class="date">{{entrySdate}}</span>
                <!-- <span class="time">09:00</span> -->
            </div>
            <div class="contents-wrap" v-if="entryEdate">
                <span class="title">퇴근</span>
                <span class="date">{{entryEdate}}</span>
                <!-- <span class="time">18:00</span> -->
            </div>
            <a href="/ife/wtms/hdngv" target="blank" class="link">근무시간 관리 시스템 바로가기</a>
            <div class="btn-wrap">
                <button class="btn btn-off" v-if="inoutType=='OUT'" @click="clickOut">퇴근하기</button>
                <button class="btn btn-cancel" v-if="inoutType=='END'" @click="clickCancel">퇴근취소</button>
            </div>
        </div>
    </div>
    </div> <!-- #topDiv end -->
    <script type="text/javascript">
		$(function () {
			var inoutVue = new Vue({
		   		el: "#topDiv",
			    data : {
			    	isHol: true,
			    	ymd: '${ymd}',
			    	entrySdate: '${entrySdate}',
			    	entryEdate: '${entryEdate}',
			    	label: '${label}',
			    	desc: '${desc}',
			    	inoutType: '${inoutType}', 
			    	empId : '${sabun}',
		    		enterCd : '${enterCd}'
		  		},
		  		computed : {
		  			formattedYmd : function(){
		  				var $ymd = this.ymd;
		  				if($ymd.length == 8){
		  					return $ymd.substring(0,4) + '.' + $ymd.substring(5,6) + '.' + $ymd.substring(7,8); 
		  				}else{
		  					return '';
		  				}
		  			}
		  		},
			    mounted: function(){
			    	var $this = this;
					if(this.inoutType == "IN"){
						this.isHol = false;	
					}
					 
			    },
			    methods: {
			    	clickIn : function(){
			    		var $this =  this;
			    		var param = { 
			    			ymd : $this.ymd,
			    			enterCd : $this.enterCd,
			    			sabun : $this.empId
			    		};
			    		
			    		Util.ajax({
							url: "${rc.getContextPath()}/api/${tsId}/in",
							type: "POST",
							contentType: 'application/json',
							data: JSON.stringify(param),
							dataType: "json",
							success: function(data) { 
								console.log(data);
								if(data!=null && data.status=='OK') {
									alert(data.message);
								} else {
									alert(data.message);
								}
							},
							error: function(e) {
								console.log(e);
								$("#loading").hide();
								$("#alertText").html("저장 시 오류가 발생했습니다.");
		  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
		  	  	         		$("#alertModal").modal("show"); 
							}
						}); 
			    	},
			    	clickOut: function(){
			    		var $this =  this;
			    		var param = { 
			    			ymd : $this.ymd,
			    			enterCd : $this.enterCd,
			    			sabun : $this.empId
			    		};
			    		
			    		Util.ajax({
							url: "${rc.getContextPath()}/api/${tsId}/out",
							type: "POST",
							contentType: 'application/json',
							data: JSON.stringify(param),
							dataType: "json",
							success: function(data) {
								console.log(data);
								if(data!=null && data.status=='OK') {
									alert(data.message);
								} else {
									alert(data.message);
								}
							},
							error: function(e) {
								console.log(e);
								$("#loading").hide();
								$("#alertText").html("저장 시 오류가 발생했습니다.");
		  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
		  	  	         		$("#alertModal").modal("show"); 
							}
						}); 
			    	},
			    	clickCancel: function(){
			    		var $this =  this;
			    		var param = { 
			    			ymd : $this.ymd,
			    			enterCd : $this.enterCd,
			    			sabun : $this.empId
			    		};
			    		
			    		Util.ajax({
							url: "${rc.getContextPath()}/api/${tsId}/cancel",
							type: "POST",
							contentType: 'application/json',
							data: JSON.stringify(param),
							dataType: "json",
							success: function(data) { 
								console.log(data);
								if(data!=null && data.status=='OK') {
									alert(data.message);
								} else {
									alert(data.message);
								}
							},
							error: function(e) {
								console.log(e);
								$("#loading").hide();
								$("#alertText").html("저장 시 오류가 발생했습니다.");
		  	  	         		$("#alertModal").on('hidden.bs.modal',function(){});
		  	  	         		$("#alertModal").modal("show"); 
							}
						}); 
			    	}
			    }
			});
			
		});
	</script>
</body>
</html>

<!--
{"result":{"ymd":"2020.01.03","entryEdate":null,"entrySdate":null,"label":"출근하기","inoutType":"IN","desc":"근무일"},"message":"","status":"OK"}
 
 -->