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
                <img v-if="inoutType=='IN'" src="${rc.getContextPath()}/company/hyundaiNGV/assets/img/icon_workday.png" alt="">
                <img v-if="inoutType=='HOL'" src="${rc.getContextPath()}/company/hyundaiNGV/assets/img/icon_holiday.png" alt="">
            </div>
            <div class="contents-wrap">
                <p class="date">{{formattedYmd}}</p>
                <p class="desc">오늘은 <strong>{{desc}}</strong> 입니다.</p>
            	<a href="/ife/wtms/hdngvsso" target="blank" class="link">근무시간 관리 시스템 바로가기</a>
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
                    <img v-if="inoutType=='OUT'" src="${rc.getContextPath()}/company/hyundaiNGV/assets/img/icon_workon.png" alt="">
                    <img v-if="inoutType=='END'" src="${rc.getContextPath()}/company/hyundaiNGV/assets/img/icon_workoff.png" alt="">
                </div>
                <div class="info-wrap">
                    <p class="date">{{formattedYmd}}</p>
                    <p class="desc"><strong>{{desc}}</strong> 입니다.</p>
                </div>
            </div>
        </div>
        <div class="contents-outer-wrap">
            <div class="contents-wrap">
            	<template  v-if="entrySymd">
	                <span class="title">출근</span>
	                <span class="date">{{entrySymd}}</span>
	                <span class="time">{{entryStime}}</span>
                </template>
            </div>
            <div class="contents-wrap" >
            	<template  v-if="entryEymd">
	                <span class="title">퇴근</span>
	                <span class="date">{{entryEymd}}</span>
	                <span class="time">{{entryEtime}}</span>
                </template>
            </div>
            <div class="contents-wrap" >
            	<template  v-if="exceptYmd">
	                <span class="title" v-if="exceptType=='BACK'">휴식중</span>
	                <span class="title" v-if="exceptType=='GO'">휴식종료</span>
	                <span class="date">{{exceptYmd}}</span>
	                <span class="time">{{exceptTime}}</span>
                </template>
            </div>
            <div class="btn-wrap">
            <a href="/ife/wtms/hdngvsso" target="blank" class="link">근무시간 관리 시스템 바로가기</a>
                <button class="btn btn-off" v-if="inoutType=='OUT'" @click="clickOut">퇴근하기</button>
                <button class="btn btn-go" v-if="inoutType=='OUT' && exceptType=='GO'" @click="clickExcept">휴식</button>
                <button class="btn btn-back" v-if="inoutType=='OUT' && exceptType=='BACK'" @click="clickExcept">휴식취소</button>
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
			    	entrySymd: '${entrySymd}',
			    	entryStime: '${entryStime}',
			    	entryEymd: '${entryEymd}',
			    	entryEtime: '${entryEtime}',
			    	label: '${label}',
			    	desc: '${desc}',
			    	inoutType: '${inoutType}', 
			    	exceptType: '${exceptType}',
			    	exceptYmd: '${exceptYmd}',
			    	exceptTime: '${exceptTime}',
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
								} else {
									alert(data.message);
								}
								if(data.result != null && data.result != undefined){
									//{result={ymd=20200108, entryEdate=, entrySdate=2020-01-08 10:22:00, label=퇴근하기, inoutType=OUT, desc=근무중}
									$this.ymd = data.result.ymd; 
									$this.entrySymd = data.result.entrySymd;
									$this.entryStime = data.result.entryStime;
									$this.entryEymd = data.result.entryEymd;
									$this.entryEtime = data.result.entryEtime;
									$this.label = data.result.label;
									$this.inoutType = data.result.inoutType;
									$this.exceptType = data.result.exceptType;
									$this.exceptYmd = data.result.exceptYmd;
									$this.exceptTime = data.result.exceptTime; 
									$this.desc = data.result.desc;
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
									
								} else {
									alert(data.message);
								}
								if(data.result != null && data.result != undefined){
									//{result={ymd=20200108, entryEdate=, entrySdate=2020-01-08 10:22:00, label=퇴근하기, inoutType=OUT, desc=근무중}
									$this.ymd = data.result.ymd;
									$this.entrySymd = data.result.entrySymd;
									$this.entryStime = data.result.entryStime;
									$this.entryEymd = data.result.entryEymd;
									$this.entryEtime = data.result.entryEtime;
									$this.label = data.result.label;
									$this.inoutType = data.result.inoutType;
									$this.exceptType = data.result.exceptType;
									$this.exceptYmd = data.result.exceptYmd;
									$this.exceptTime = data.result.exceptTime;  
									$this.desc = data.result.desc;
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
									
								} else {
									alert(data.message);
								}
								if(data.result != null && data.result != undefined){
									//{result={ymd=20200108, entryEdate=, entrySdate=2020-01-08 10:22:00, label=퇴근하기, inoutType=OUT, desc=근무중}
									$this.ymd = data.result.ymd;
									$this.entrySymd = data.result.entrySymd;
									$this.entryStime = data.result.entryStime;
									$this.entryEymd = data.result.entryEymd;
									$this.entryEtime = data.result.entryEtime;
									$this.label = data.result.label;
									$this.inoutType = data.result.inoutType;
									$this.exceptType = data.result.exceptType;
									$this.exceptYmd = data.result.exceptYmd;
									$this.exceptTime = data.result.exceptTime;  
									$this.desc = data.result.desc;
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
			    	clickExcept: function(){
			    		var $this =  this;
			    		var param = { 
			    			ymd : $this.ymd,
			    			enterCd : $this.enterCd,
			    			sabun : $this.empId
			    		};
			    		
			    		Util.ajax({
							url: "${rc.getContextPath()}/api/${tsId}/except",
							type: "POST",
							contentType: 'application/json',
							data: JSON.stringify(param),
							dataType: "json",
							success: function(data) { 
								console.log(data);
								if(data!=null && data.status=='OK') {
									
								} else {
									alert(data.message);
								}
								if(data.result != null && data.result != undefined){
									//{result={ymd=20200108, entryEdate=, entrySdate=2020-01-08 10:22:00, label=퇴근하기, inoutType=OUT, desc=근무중}
									$this.ymd = data.result.ymd;
									$this.entrySymd = data.result.entrySymd;
									$this.entryStime = data.result.entryStime;
									$this.entryEymd = data.result.entryEymd;
									$this.entryEtime = data.result.entryEtime;
									$this.label = data.result.label;
									$this.inoutType = data.result.inoutType;
									$this.exceptType = data.result.exceptType;
									$this.exceptYmd = data.result.exceptYmd;
									$this.exceptTime = data.result.exceptTime;  
									$this.desc = data.result.desc;
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