/**
mySheet0 * @author ParkMoohun
 *
 * Common Javascript
 *
 */


/**
 * Form 전송시 input이 하나인 경우 자체 submit을 하여 오류 발생 submit을 하지 못하도록 차단
 *
 */
/*
$(document).ready(function() {
	$("form").each( function() {
		$(this).attr("onsubmit","return false;");
	});
});
*/
$("*").keypress(function (e) {
		e = e || event;
		var tag = e.srcElement ? e.srcElement.tagName : e.target.nodeName;

		// "INPUT" 을가져옴  TEXTARAE 아님
		if (tag != "INPUT") {
				return true;
		} else {
			if (e.keyCode == 13){
				return false;
			}
		}
		});
//employeeHeader 에서 사용되는 공통함수
//function setEmpPage(){}


function setValidate( form,msg ) {
	form.validate({
		onkeyup:false,
		onclick:false,
		onfocusout:false,
		messages: msg,
		showErrors:function(errorMap, errorList){
			if(!$.isEmptyObject(errorList)){
				if( errorList.length > 0 ) {
					alert(errorList[0].message);
					$("#"+errorList[0].element.id).focus();
				}
			}
		}
	});
}

/**
 * Jsp Code를 String 형태로 추출
 *
 * @param url
 * @returns
 */
function pageToHtml(url) {
	var html = $.ajax({
		url : "<c:url value='" + url + "' />",
		async : false
	}).responseText;
	return html;
}

/**
 * XSS_Replace
 *
 * @param str
 * @returns String
 */
function XSS_Replace(str) {
	return str	.replace(/</g, '&lt;')
				.replace(/>/g, '&gt;')
				.replace(/'/g, '&#039;')
				.replace(/"/g, '&quot;');
}

/**
 * 공통 코드 조회
 *
 * @param url
 * @param grpCd
 * @returns Object
 */
function codeList(url, grpCd, async) {
	var data = ajaxCall(url, "grpCodeCd=" + grpCd,false).codeList;
	if (data == 'undefine' || data == null)
		return null;
	return data.length>0?data:null;
}

/**
 * 공통 코드 조회에서 조회된 데이터를 IBsheet에서 Combo형태로 쓰는 형태로 구성 [0] name: A|B|C|D|E [1] cd:
 * 1|2|3|4|5 [2] <option value="cd">name<option>
 *
 * @param obj
 * @param str
 * @returns Array
 */
function convCode(obj, str) {
// JNS 수정 : 코드 리스트가 없을 경우 empty Data 생성후 리턴
// modify Date : 2014-01-20
//	if (null == obj || obj == 'undefine') return false;
//	if (obj.length < 1) return false;
	var convArray = new Array("", "", "");
	if (null == obj || obj == 'undefine'){

		convArray[0] = "";
		convArray[1] = "";
		convArray[2] = "<option value=''>" + str + "</option>";
		return convArray;
	}
	if (obj.length < 1){

		convArray[0] = "";
		convArray[1] = "";
		convArray[2] = "<option value=''>" + str + "</option>";
		return convArray;
	}


	if(str != "") convArray[2] += "<option value=''>" + str + "</option>";

	for (i = 0; i < obj.length; i++) {
		convArray[0] += obj[i].codeNm + "|";
		convArray[1] += obj[i].code + "|";
		convArray[2] += "<option value='" + obj[i].code + "'>" + obj[i].codeNm + "</option>";
		convArray[3] += "<option value='" + obj[i].code + "'>[" + obj[i].code + "]"+ obj[i].codeNm + "</option>";
		convArray[4] += "["+ obj[i].code +"]"+ obj[i].codeNm + "|";
	}
	convArray[0] = convArray[0].substr(0, convArray[0].length - 1);
	convArray[1] = convArray[1].substr(0, convArray[1].length - 1);
	convArray[4] = convArray[4].substr(0, convArray[4].length - 1);

	return convArray;
}

/**
 * 공통 코드 조회에서 조회된 데이터를 IBsheet에서 Combo형태로 쓰는 형태로 구성 [0] name: A|B|C|D|E [1] cd:
 * 1|2|3|4|5 [2] <option value="cd">name<option>
 *
 * @param obj
 * @param str
 * @returns Array
 */
function stfConvCode(obj, str) {

	var convArray = new Array("", "", "");
	if (null == obj || obj == 'undefine'){

		convArray[0] = "";
		convArray[1] = "";
		convArray[2] = "<option value=''>" + str + "</option>";
		return convArray;
	}
	if (obj.length < 1){

		convArray[0] = "";
		convArray[1] = "";
		convArray[2] = "<option value=''>" + str + "</option>";
		return convArray;
	}



	if(str != "") convArray[2] += "<option value=''>" + str + "</option>";

	for (i = 0; i < obj.length; i++) {
		convArray[0] += obj[i].codeNm + "|";
		convArray[1] += obj[i].code + "|";
		convArray[2] += "<option value='" + obj[i].code + "'>" + obj[i].codeNm + "</option>";
	}
	convArray[0] = convArray[0].substr(0, convArray[0].length - 1);
	convArray[1] = convArray[1].substr(0, convArray[1].length - 1);

	return convArray;
}


/**
 * 공통 코드 조회에서 조회된 데이터를 IBsheet에서 Combo형태로 쓰는 형태로 구성 [0] name: A|B|C|D|E [1] cd:
 * 1|2|3|4|5 [2] <option value="cd">name<option> IDX의 값에 따라서 Default Selected 됨
 * 사용 안함은 -1
 *
 * @param obj
 * @param str
 * @returns Array
 */
function convCodeIdx(obj, str, idx) {
	if (null == obj || obj == 'undefine') return false;
	if (obj.length < 1) return false;

	var convArray = new Array("", "", "");

	if(str !="" && idx == 0 ) {
		convArray[2] += "<option value='' Selected>" + str + "</option>";
	}else if(str !="" && idx != 0 ){
		convArray[2] += "<option value='' >" + str + "</option>";
	}
	for (i = 0; i < obj.length; i++) {

		if( idx != -1 && idx-1 == i && str == ""){
			convArray[2] += "<option value='" + obj[i].code + "' Selected>" + obj[i].codeNm + "</option>";
		}else{
			convArray[2] += "<option value='" + obj[i].code + "'>" + obj[i].codeNm + "</option>";
		}

		convArray[0] += obj[i].codeNm + "|";
		convArray[1] += obj[i].code + "|";
	}
	convArray[0] = convArray[0].substr(0, convArray[0].length - 1);
	convArray[1] = convArray[1].substr(0, convArray[1].length - 1);

	return convArray;
}

/**
 * 공통 코드 조회에서 조회된 데이터를 IBsheet에서 Combo형태로 쓰는 형태로 구성 [0] name: A|B|C|D|E [1] cd:
 * 1|2|3|4|5 [2] <option value="cd">name<option>
 *
 * @param obj
 * @param str
 * @returns Array
 */
function convCodeCols(obj, cols, str) {
// JNS 수정 : 코드 리스트가 없을 경우 empty Data 생성후 리턴
// modify Date : 2014-01-20
//	if (null == obj || obj == 'undefine') return false;
//	if (obj.length < 1) return false;
	var convArray = new Array("", "", "");
	var arrCol = cols.split(' ').join("").split(",");

	if (null == obj || obj == 'undefine'){

		convArray[0] = "";
		convArray[1] = "";
		convArray[2] = "<option value='' ";
		for(ii = 0; ii < arrCol.length; ii++) {	convArray[2] += " "+ arrCol[ii] +"=''";	}
		convArray[2] += " >" + str + "</option>";
		return convArray;
	}
	if (obj.length < 1){

		convArray[0] = "";
		convArray[1] = "";
		convArray[2] = "<option value='' ";
		for(ii = 0; ii < arrCol.length; ii++) {	convArray[2] += " "+ arrCol[ii] +"=''";	}
		convArray[2] += " >" + str + "</option>";
		return convArray;
	}

	if(str != "") {
		convArray[2] += "<option value='' ";
		for(ii = 0; ii < arrCol.length; ii++) {	convArray[2] += " "+ arrCol[ii] +"=''";	}
		convArray[2] += " >" + str + "</option>";
	}

	for (i = 0; i < obj.length; i++) {
		convArray[0] += obj[i].codeNm + "|";
		convArray[1] += obj[i].code + "|";

		convArray[2] += "<option value='" + obj[i].code + "' ";
		for(ii = 0; ii < arrCol.length; ii++) {	convArray[2] += " "+ arrCol[ii] +"='"+ eval("obj[i]."+arrCol[ii]) +"'";	}
		convArray[2] += " >" + obj[i].codeNm + "</option>";

		convArray[3] += "<option value='" + obj[i].code + "' ";
		for(ii = 0; ii < arrCol.length; ii++) {	convArray[3] += " "+ arrCol[ii] +"='"+ eval("obj[i]."+arrCol[ii]) +"'";	}
		convArray[3] += " >[" + obj[i].code + "]"+ obj[i].codeNm + "</option>";

		convArray[4] += "["+ obj[i].code +"]"+ obj[i].codeNm + "|";
	}
	convArray[0] = convArray[0].substr(0, convArray[0].length - 1);
	convArray[1] = convArray[1].substr(0, convArray[1].length - 1);
	convArray[4] = convArray[4].substr(0, convArray[4].length - 1);

	return convArray;
}



/**
 * 공통 summit 호출
 *
 * @param url
 * @param params
 * @param async
 * @returns Object
 */
function submitCall(formObj, target, method, action) {
	var token = "";
	var map = ajaxCall("/SecurityToken.do","prgCd="+action,false);
	if( typeof map != 'undefined' ) token = encodeURIComponent(map.map.token);

	if( $("#token").length == 0 ){
		var o_hidden = $('<input type="hidden" value="'+token+'" name="token" id="token">');
		$(formObj).append(o_hidden);
	}else{
		$("#token").val(token);
	}

	formObj	.attr("target",target)
			.attr("method",method)
			.attr("action",action)
			.submit();
}
/**
 * /common/js/jquery/jquery.attr.js  에서 호출
 * -. Tab 클릭 시 토큰을 발생시켜 화면에 토큰을 같이 전송 함
 */
function catchTabSubmitCall(obj, action){

	var target = $(obj).attr("name");
	if( typeof target == "undefined" ){
		target = obj.replace(/#/gi, "").replace(/ /gi, "");
		//console.log("■■■■■■■■■■  new name : "+ $(obj).attr("name"));
		//console.log("■■■■■■■■■■  new name : "+ target);

		var o_parent = $(obj).parent();
		var o_html = "<iframe id=\""+target+"\" name=\""+target+"\" frameborder=\"0\" class=\"tab_iframes\"></iframe>"; // src에 ${ctx}/common/hidden.jsp 가 들어가면 세션ID가 변경 되어서 세션 중복체크가 안됨
		$(obj).remove();
		$(o_parent).append(o_html);

	}

	var p_hidden = "";
	var new_form = document.createElement("form");


	if( action.indexOf("?") > -1 ){
		var arr1 = action.split("?");
		var arr2 = arr1[1].split("&");

		action = arr1[0];
		//console.log("■■■■■■■■■■  arr2 : "+ arr2.length);
		for(var i=0, len=arr2.length; i<len; i++){
			if( arr2[i].indexOf("cmd=") > -1 ){
				action += "?"+arr2[i];
			}else if( arr2[i] != "" ){
				var arr3 = arr2[i].split("=");
				if( arr3.length == 2 ){
					p_hidden = $("<input type='hidden' id='"+arr3[0]+"' name='"+arr3[0]+"' value='" + arr3[1] +"' />");
					$(new_form).append(p_hidden);
				}

			}
		}
	}

	var token = "";
	var map = ajaxCall("/SecurityToken.do","prgCd="+action,false);
	if( typeof map != 'undefined' ) token = encodeURIComponent(map.map.token);

	//console.log("■■■■■■■■■■  action : "+ action );
	//console.log("■■■■■■■■■■  target : "+ target );
	//console.log("■■■■■■■■■■  token : "+ token );

	p_hidden = $('<input type="hidden" value="'+token+'" name="token" id="token">');
	$(new_form).append(p_hidden);


	$(new_form).attr({"method":"post", "target":target,"action":action});
	$(new_form).appendTo('body');
	$(new_form).submit();

	//console.log("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");

}

/**
 * 공통 ajax 호출
 *
 * @param url
 * @param params
 * @param async
 * @returns Object
 */
function ajaxCall(url, params,async) {
	var obj 	= new Object();
	headerReSetTimer();
	$.ajax({
		url 		: url,
		type 		: "post",
		dataType 	: "json",
		async 		: async,
		data 		: params,
		success : function(data) {
			obj = data;
		},
		error : function(jqXHR, ajaxSettings, thrownError) {
			ajaxJsonErrorAlert(jqXHR, ajaxSettings, thrownError);
		}
	});

	return obj;
}

/**
 * 공통 ajax 호출
 *
 * @param url
 * @param params
 * @param async
 * @returns Object
 */
function ajaxCall2(url, params,async, beforeFn, successFn) {
	var obj 	= new Object();
	$.ajax({
		url 		: url,
		type 		: "post",
		dataType 	: "json",
		async 		: async,
		data 		: params,
		beforeSend	: function(request) {
			if(typeof beforeFn != "undefined" && $.isFunction(beforeFn)) {
				beforeFn();
			}
		},
		success : function(data) {
			obj = data;
			if(obj != null && obj.Result != null) {
				if(typeof successFn != "undefined" && $.isFunction(successFn)) {
					successFn(data);
				}
			}
		},
		error : function(jqXHR, ajaxSettings, thrownError) {
			ajaxJsonErrorAlert(jqXHR, ajaxSettings, thrownError);
		}
	});
	return obj;
}

/**
 * ajax 호출 시 세션 끊겼을 경우 처리
 *
 * @param e
 * @param xhr
 * @param settings
 * @param exception
 */
function ajaxError(e, xhr, settings, exception) {
	switch (xhr.status) {
	case 0:// Abort
		break;
	case 401:// Unauthorized
		try {
			location = $.parseJSON(xhr.responseText).loginURL;
		} catch (e) {
			location.reload();
		}
		break;
	default:
		if (typeof settings.errorMessage == 'function') {
			var errorMessage = settings.errorMessage.call(settings, xhr, exception);
			if (errorMessage)
				alert(errorMessage);
		} else if (settings.errorMessage) {
			alert(settings.errorMessage);
		}
	}
}


/**
 * 사용안함 .. ic
 */
function sheetDupCheck(sheetObj, saveName){
	var save = sheetObj.GetSaveString({AllSave:0, UrlEncode:0, Mode:2, Delim:"|"});
	var saveAll = sheetObj.GetSaveString(1,0,3,"",2);
	var saveAllStatus = sheetObj.GetSaveString(1,0,3,"",2);

	var stx,etx,addLen;

	stx = save.indexOf(saveName,0);
	etx = save.indexOf("&" , save.indexOf(saveName,0) );
	addLen = saveName.length+1;
	var saveCut = save.substring( stx+addLen, etx);

	stx= saveAll.indexOf(saveName,0);
	etx = saveAll.indexOf("&" , saveAll.indexOf(saveName,0) );
	addLen = saveName.length+1;
	var saveAllCut = saveAll.substring( stx+addLen, etx);

	stx= saveAll.indexOf("sStatus",0);
	etx = saveAll.indexOf("&" , saveAll.indexOf("sStatus",0) );
	addLen = "sStatus".length+1;
	var saveAllStatus = saveAll.substring( stx+addLen, etx);

	var splitSave= saveCut.split("|");
	var splitSaveAll = saveAllCut.split("|");
	var splitSaveAllStatus = saveAllStatus.split("|");

	var sss = "";
	var cnt = 0;
	var findRow = 0;


	for(i=0; i<splitSave.length; i++){
		for(x=0; x<splitSaveAll.length; x++){
// if(cnt > 1){
// alert( saveName+"의 중복된 값이 존재 합니다. \n중복값:"+splitSave[i] );
// sheetObj.SetSelectRow(findRow);
// sheetObj.SetSelectCol(saveName);
// return findRow;
// }
			if($.trim(splitSave[i]) ==$.trim(splitSaveAll[x]) ){
				sss += splitSaveAll[x] +"==" +splitSave[i]+"\n";
				cnt++;
				if(splitSaveAllStatus[x]=="I" || splitSaveAllStatus[x]=="U"){
					findRow = x+1;
				}
			}
		}
		cnt = 0;
	}

	sheetObj.SetRowBackColor(t[j],"#FF7F50");

	return findRow;
}

/**
 *
 */
function dupChk(objSheet, keyCol, delchk, firchk){

	var duprows = objSheet.ColValueDupRows(keyCol, delchk, true);

// var arrRow = duprows1.split(",");
// alert(arrRow);
// for(i=0; i<arrRow; i++){
// objSheet.SetRowBackColor(arrRow[i],"#F4F4EE");
// }
	var arrRow=[];
	var arrCol=duprows.split("|");


	if(arrCol[1] && arrCol[1]!=""){
		arrRow=arrCol[1].split(",");
		for(j=0;j<arrRow.length;j++){
			objSheet.SetRowBackColor(arrRow[j],"#FACFED");
			if (firchk && j==0) {objSheet.SetSelectRow(arrRow[j]);}
		}

	}else{
		var j =0;
	}

	if(j>0){
		alert("중복된 값이 존재 합니다. ["+j+"건]");
		return false;
	}
	return true;

}

function ajaxJson500ErrorAlert(){
		ajaxJsonGoErrorPage();
}

function ajaxJsonGoErrorPage() {
// location.href = "<c:url value='/Info.do?code=ajaxError'/>";

	// alert("Ajax Error");
		return;
}


// 창크기를 드레그를 통해 변경하는 경우 OnResize이벤트가 과도하게 발생하는 것을 시간 간격으로
// 발생하게 끔 조절해 준다. (jquery가 필요함)
(function($,sr){
	// debouncing function from John Hann
	// http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
	var debounce = function (func, threshold, execAsap)
	{
		var timeout;
		return function debounced () {
			var obj = this, args = arguments;
			function delayed () {
				if (!execAsap)
					func.apply(obj, args);
				timeout = null;
			};

			if (timeout) clearTimeout(timeout);
			else if (execAsap) func.apply(obj, args);

			timeout = setTimeout(delayed, threshold || 100);   // 시간 설정. 얼마의 시간
																// 동안 Resize이벤트를
																// 무시할 것인지..
			};
	};
		// smartresize
	jQuery.fn[sr] = function(fn)
	{
		return fn ? this.bind('resize', debounce(fn)) : this.trigger(sr);
	};
})(jQuery,'smartresize');

var globalWindowPopup = null;
var globalWindowRtnFn = null;

function isPopup() {
	if(globalWindowPopup && !globalWindowPopup.closed) {
		alert("이미 작업중이신 창이 존재합니다.");
		globalWindowPopup.focus();
		return false;
	} else {
		return true;
	}

	return true;
}

/*
**
* IBSheet Header Savename을 ,로 구분된 String으로 반환
* 2013.11.26 C.B.K
*/
function sheetSaveName(initData){
var saveNames = "";
for(var i=0; i<initData.Cols.length; i++){
saveNames += initData.Cols[i].SaveName + "," ;
}
return saveNames.substring(0, saveNames.length - 1);
}


//팝업 띄우는 스크립트
//window 개체를 넘기려면 arg 부분에 "window,self" 로 입력
/*
function openPopup(url,arg,width,height,rtnFn){

	if(typeof rtnFn != "undefined" && $.isFunction(rtnFn)) {
		globalWindowRtnFn = rtnFn;
	} else {
		globalWindowRtnFn = null;
	}

	var popOptions = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; center: yes; resizable: yes; status: no; scroll: no;minimize:yes;maximize:yes";
	top.$('<div></div>',{
		id:"cover",
		"class":"cover"
	}).html("").appendTo('body');

	$("#cover").height(top.$(document).height());
	$("#cover").css("top",0);
	if(arg.constructor.toString().indexOf("Window") > 0 || arg.constructor.toString().indexOf("String")>0){
		var arg 	= new Array();
	}
	arg["opener"] = this;
	arg["url"] = url;

	var win = "";
	try{
		var verchk = getInternetExplorerVersion();

		if(verchk){
			throw new Error(200, "zero");
		}

		globalWindowPopup = window.showModalDialog("/Pop.do",arg,popOptions );

		//모달팝업에서 오류 시
		if(globalWindowPopup != "" && globalWindowPopup != undefined  ){
			try{
				var rtnObj = JSON.parse(globalWindowPopup);
				if( rtnObj.securityKey == "Security"){
					moveInfoPage(rtnObj.code);
				}
			}catch(je){}
		}

	}catch(e){
		//var obj_length = Object.keys(arg).length;
		var getparm = "";
		var i = 0;

		for(key in arg) {
			if([key] !="contains"){
				value = arg[key];
				if(	Object.prototype.toString.call(value)=="[object Object]" ||
					Object.prototype.toString.call(value)=="[object Window]" ||
					Object.prototype.toString.call(value)=="[object Array]"){
					getparm = getparm+ "";
				}
				else{
					getparm = getparm + ((i==0) ? "\"" : ",\"") +[key]+"\":\""+escape(value)+"\"";
					i++;
				}
			}
		}

		getparm = getparm.replace(/undefined/gi, "");

		var new_form = document.createElement("form");
		$(new_form).attr({'method': 'post'});

		var parent_element =  "<input type='hidden' id='Data' name='Data' value='" + getparm +"' />";

		$(new_form).appendTo('body');
		$(new_form).append(parent_element);

		var winHeight = document.body.clientHeight;	// 현재창의 높이
		var winWidth = document.body.clientWidth;	// 현재창의 너비

		var winX = window.screenX || window.screenLeft || 0;// 현재창의 x좌표
		var winY = window.screenY || window.screenTop || 0;	// 현재창의 y좌표

		var popX = winX + (winWidth - width)/2;
		var popY = winY + (winHeight - height)/2;
		var target = escape(url);
		target  = target.replace(/[^(a-zA-Z0-9)]/gi, "");

		globalWindowPopup = window.open("",target,"width="+width+"px,height="+height+"px,top="+popY+",left="+popX+",scrollbars=no,resizable=yes,menubar=no" );
		$(new_form).attr({"target":target,"action":"Popg.do"}).submit();
		globalWindowPopup.focus();
	}

	try{
		top.$("#cover").remove();
	}catch(e){

	}

	//top.$("#cover").remove();
	return globalWindowPopup;
}
*/

function openPopup(url,arg,width,height,rtnFn){

	if(typeof rtnFn != "undefined" && $.isFunction(rtnFn)) {
		globalWindowRtnFn = rtnFn;
	} else {
		globalWindowRtnFn = null;
	}

	var popOptions = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; center: yes; resizable: yes; status: no; scroll: no;minimize:yes;maximize:yes";
	top.$('<div></div>',{
		id:"cover",
		"class":"cover"
	}).html("").appendTo('body');

	$("#cover").height(top.$(document).height());
	$("#cover").css("top",0);
	if(arg.constructor.toString().indexOf("Window") > 0 || arg.constructor.toString().indexOf("String")>0){
		var arg 	= new Array();
	}
	arg["opener"] = this;
	arg["url"] = url;

	var win = "";
	//var obj_length = Object.keys(arg).length;
	var getparm = "";
	var i = 0;

	for(key in arg) {
		if([key] !="contains"){
			value = arg[key];
			if(	Object.prototype.toString.call(value)=="[object Object]" ||
				Object.prototype.toString.call(value)=="[object Window]" ||
				Object.prototype.toString.call(value)=="[object Array]"){
				getparm = getparm+ "";
			}
			else{
				getparm = getparm + ((i==0) ? "\"" : ",\"") +[key]+"\":\""+escape(value)+"\"";
				i++;
			}
		}
	}

	getparm = getparm.replace(/undefined/gi, "");

	var new_form = document.createElement("form");
	$(new_form).attr({'method': 'post'});

	var parent_element =  "<input type='hidden' id='Data' name='Data' value='" + getparm +"' />";

	$(new_form).appendTo('body');
	$(new_form).append(parent_element);

	var winHeight = document.body.clientHeight;	// 현재창의 높이
	var winWidth = document.body.clientWidth;	// 현재창의 너비

	var winX = window.screenX || window.screenLeft || 0;// 현재창의 x좌표
	var winY = window.screenY || window.screenTop || 0;	// 현재창의 y좌표

	var popX = winX + (winWidth - width)/2;
	var popY = winY + (winHeight - height)/2;
	var target = escape(url);
	target  = target.replace(/[^(a-zA-Z0-9)]/gi, "");

	globalWindowPopup = window.open("",target,"width="+width+"px,height="+height+"px,top="+popY+",left="+popX+",scrollbars=no,resizable=yes,menubar=no" );
	$(new_form).attr({"target":target,"action":"Popg.do"}).submit();
	globalWindowPopup.focus();

    setHeaderPageObj(url, globalWindowPopup);

	try{
		top.$("#cover").remove();
	}catch(e){

	}

	//top.$("#cover").remove();
	return globalWindowPopup;
}


//팝업 스크립트 Direct .. 사용안함 ic
function winPopup(url,arg,width,height){

	var popOptions = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; center: yes; resizable: yes; status: no; scroll: no;minimize:yes;maximize:yes";
	top.$('<div></div>',{id:"cover","class":"cover"}).html("").appendTo('body');

	$("#cover").height(top.$(document).height());
	$("#cover").css("top",0);

	var win = window.showModalDialog(url,arg,popOptions );
	top.$("#cover").remove();
	return win;
}

//프린트 팝업
function winPrintPopup(url,arg,width,height){
	var popOptions = "dialogWidth:"+width+"px; dialogHeight:"+height+"px; dialogLeft:0px;dialogTop:00px;center:no; dialogHide:no; resizable: no; status: no; scroll: no;minimize:no;maximize:no";
	var win = window.showModalDialog(url,arg,popOptions );
	return win;
}

var timeout;
// 시트 사이즈 초기화
function sheetInit() {
	// 외부 높이 계산
	var outer_height = getOuterHeight();
	var inner_height = 0;
	var sheet_count = 1;
	var value = 0;
	$(".ibsheet").each(function() {
		if( $(this).attr("fixed") == "false" ) {
			// 내부 높이 계산
			inner_height = getInnerHeight($(this));
			// sheet_count 시트의 높이값 설정
			sheet_count = parseInt( 100 / parseInt($(this).attr("realHeight")) );
			$(this).attr("sheet_count",sheet_count);
			value = ($(window).height() - outer_height ) / sheet_count - inner_height;
			if( value < 100 ) value = 100;
			$(this).height( value );
		}
	});

	clearTimeout(timeout);
	addTimeOut();
	//timeout = setTimeout(addTimeOut, 50);

}

function getOuterHeight() {
	var outerHeight = 0;
	if( $(".popup_main").length > 0 ) outerHeight += 90;
	$(".outer").each(function() {
		outerHeight += $(this).height();
		outerHeight += Number($(this).css("padding-top").replace(/[^0-9]/g,''));
		outerHeight += Number($(this).css("padding-bottom").replace(/[^0-9]/g,''));
		outerHeight += Number($(this).css("margin-top").replace(/[^0-9]/g,''));
		outerHeight += Number($(this).css("margin-bottom").replace(/[^0-9]/g,''));
		outerHeight += Number($(this).css("border-top-width").replace(/[^0-9]/g,''));
		outerHeight += Number($(this).css("border-bottom-width").replace(/[^0-9]/g,''));
	});
	return parseInt(outerHeight);
}

function getInnerHeight(obj) {
	var innerHeight = 0;
	obj.parent().find(".inner").each(function() {
		innerHeight += $(this).height();
		innerHeight += Number($(this).css("padding-top").replace(/[^0-9]/g,''));
		innerHeight += Number($(this).css("padding-bottom").replace(/[^0-9]/g,''));
		innerHeight += Number($(this).css("margin-top").replace(/[^0-9]/g,''));
		innerHeight += Number($(this).css("margin-bottom").replace(/[^0-9]/g,''));
		innerHeight += Number($(this).css("border-top-width").replace(/[^0-9]/g,''));
		innerHeight += Number($(this).css("border-bottom-width").replace(/[^0-9]/g,''));
	});
	return parseInt(innerHeight);
}

function addTimeOut() {
	$(".ibsheet").each(function() {
		if( $(this).attr("id") ) {
			var obj = eval($(this).attr("id").split("DIV_").join(""));
			setSheetSize(obj);
		}
	});
}

// 시트 리사이즈
function sheetResize() {
	var outer_height = getOuterHeight();
	var inner_height = 0;
	var value = 0;

	$(".ibsheet").each(function() {
		inner_height = getInnerHeight($(this));
		if( $(this).attr("fixed") == "false" ) {
			value = parseInt(($(window).height() - outer_height ) / $(this).attr("sheet_count") - inner_height);
			if( value < 100 ) value = 100;
			$(this).height( value );
		}
	});

	clearTimeout(timeout);
	timeout = setTimeout(addTimeOut, 50);
}

/*
 * //경고창 생성 후 뿌려줌 function alert(msg, successFunction) { // 경고창 레이어 창 생성 if(
 * top.$("#dialog-alert").length == 0 ) { top.$('<div></div>',{ id:
 * 'dialog-alert', title: '확인' }).html('<p><span class="ui-icon ui-icon-info"
 * style="float: left; margin: 0 0 0 0;"></span><p class="content"></p></p>').appendTo('body'); }
 * top.$( "#dialog-alert p.content" ).html(msg); top.$( "#dialog-alert"
 * ).dialog({ modal: true, buttons: { "확인": function() { top.$( this ).dialog(
 * "close" ); if( successFunction ) successFunction.call(); top.$( this
 * ).remove(); } } }); }
 */

// 확인창 생성 후 뿌려줌
// function confirm(msg, successFunction, failFunction) {
// // 확인창 레이어 창 생성
// if( top.$("#dialog-confirm").length == 0 ) {
// top.$('<div></div>',{
// id: 'dialog-confirm',
// title: '확인'
// }).html('<p><span class="ui-icon ui-icon-info" style="float: left; margin: 0
// 0 0 0;"></span><p class="content">1</p></p>').appendTo('body');
// }
//
// top.$( "#dialog-confirm p.content" ).html(msg);
// top.$( "#dialog-confirm" ).dialog({
// resizable: false,
// modal: true,
// buttons: {
// "예": function() {
// top.$( this ).dialog( "close" );
// if( successFunction ) successFunction.call();
//
// },
// "아니요": function() {
// top.$( this ).dialog( "close" );
// if( failFunction ) failFunction.call();
// }
// }
// });
// }

// datepicker 기본설정
$.datepicker.setDefaults({
	showOn: "both",
	buttonImage: "/ifw/IBLeaders/Sheet/js/Main/calendar.gif",
	buttonImageOnly: true,
	buttonText: "달력",
	dateFormat: "yy-mm-dd",	// 날짜 포맷
	nextText: "다음",		// < 버튼 Alt
	prevText: "이전",		// > 버튼 Alt
	yearSuffix: "",		// 연도 뒤에 나오는 글짜
	firstDay: 0,			// 요일 순서
	showWeek: false,			// 주를 표시
	weekHeader: "주",		// 주 타이틀 텍스트
	showMonthAfterYear:true,	// 연도 뒤에 달 표시
	dayNames: [ "일", "월", "화", "수", "목", "금", "토" ] ,	// 요일
	dayNamesMin: [ "일", "월", "화", "수", "목", "금", "토" ] ,	// 요일
	monthNames: [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ],	// 월
	monthNamesShort: [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ],	// 월
	changeMonth: true,	// 달 변경 가능여부
	changeYear: true,	// 년도 변경 가능여부
	showButtonPanel: true,
	currentText: "오늘",
	closeText: "닫기",
	beforeShowDay: getDatePickerHoliday,	// 표시되지 않을 날짜
	beforeShow : onDatePickerDateChange,
	onChangeMonthYear:onDatePickerDateChange	// 날짜 변경시
});

// datepicker 휴일 처리 함수
var getDatePickerHolidays = {};
function getDatePickerHoliday(date) {
	var holiday = getDatePickerHolidays[$.datepicker.formatDate("dd",date)]; // 표시날이
																				// 휴일인지
																				// 체크
	if( date.getDay() == 0 ) return [false, 'new_sun'];		// 일요일
	else if( date.getDay() == 6 ) return [false, 'new_sat'];	// 토요일
	else if( holiday != undefined && holiday.type == 0) return [false, 'new_hol'];
	return [true, ''];
}

// datepicker 년,월 변경시 데이터 가져오기
function onDatePickerDateChange(year, month, inst ) {
	getDatePickerHolidays = {};
	/*
	 * var picker = this;
	 * $.getJSON("/html/sample/date.html?year="+year+"&month="+month,
	 * function(data) { getDatePickerHolidays = data; $(picker).datepicker(
	 * "refresh" ); })
	 */;
}


// 시트에서 달력 열기
var _CalSheet;
function calendarOpen(sheet) {
	_CalSheet = sheet;
	if( $("#calendar").length == 0 ) {
		$('<div></div>',{ id:'calendar'}).appendTo('body');
		$("#calendar").css("position","absolute");
		$("#calendar").mouseup(function(event){event.stopPropagation();});
		$("#calendar").datepicker({onSelect:calendarClose});
	}

	$(document).mouseup(function() {calendarClose();});
	$(document).keydown(function(e) {if( e.keyCode == 27 ) calendarClose();});
	$(".GMVScroll>div").scroll(function () {calendarClose();});
	$(".GMHScrollMid>div").scroll(function () {calendarClose();});

	var pleft = sheet.ColLeft(sheet.GetSelectCol());
	var ptop =  sheet.RowTop(sheet.GetSelectRow())+sheet.GetRowHeight(sheet.GetSelectRow()) ;
	var cWidth = $("#calendar").width();
	var cHeight = $("#calendar").height();
	var dWidth = $(window).width();
	var dHeight = $(window).height();

	if( dWidth < pleft + cWidth) pleft = dWidth - cWidth;
	if( dHeight < ptop + cHeight) ptop = sheet.RowTop(sheet.GetSelectRow()) - cHeight;
	if( ptop < 0 ) ptop = 0;

	var date = sheet.GetCellValue(sheet.GetSelectRow(),sheet.GetSelectCol());

	$("#calendar").css("top", ptop);
	$("#calendar").css("left", pleft);
	if( date.length == 8 ) $('#calendar').datepicker('setDate',new Date(date.substr(0,4),parseInt(date.substr(4,2))-1,date.substr(6,2)));
	$("#calendar").show();
}


// 시트에서 달력 닫기
function calendarClose(dateText) {
	$(document).unbind("mouseup");
	$(document).unbind("keydown");
	$(".GMVScroll>div").unbind("scroll");
	$(".GMHScrollMid>div").unbind("scroll");
	if(dateText) _CalSheet.SetCellValue(_CalSheet.GetSelectRow(), _CalSheet.GetSelectCol(),dateText);
	$("#calendar").hide();
}

function fGetXY(aTag){
		var oTmp = aTag;
		var pt = new Point(0,0);
		do {
			pt.x += oTmp.offsetLeft;
			pt.y += oTmp.offsetTop;
			oTmp = oTmp.offsetParent;
		} while(oTmp.tagName!="BODY");
		return pt;
	}

function Point(iX, iY){
	this.x = iX;
	this.y = iY;
}

var g_event;
$(document).keypress(function(e) {
	if( typeof(e) != "undefined" )
				g_event = e;
		else
				g_event = event;
	$(document).unbind("keypress");
});

//숫자(','제외) FORMAT처리
function makeNumber(obj,type) {
	if (typeof(event) == "undefined") event = g_event;  // ie외 브라우저의 event 값 캐취

	if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 13 || (event.keyCode >= 37 && event.keyCode <= 40))
				return false;

	var ls_amt1 = obj.value;
	var ls_amt2 = "";

	switch (type) {
		case "A":
			// 숫자만
			for(var i=0; i<ls_amt1.length+1; i++) {
					if (ls_amt1.substring(i,i+1) >= "0" &&
							ls_amt1.substring(i,i+1) <= "9") {

							ls_amt2 = ls_amt2 + ls_amt1.substring(i,i+1);
				}
			}
			break;
		case "B":
			// 숫자(-부호 포함)
			for(var i=0; i<ls_amt1.length+1; i++) {
					if (ls_amt1.substring(i,i+1) >= "0" &&
							ls_amt1.substring(i,i+1) <= "9" ||
							ls_amt1.substring(i,i+1) == "-") {

							ls_amt2 = ls_amt2 + ls_amt1.substring(i,i+1);
				}
			}
			break;
		case "C":
			// 숫자(소수점 포함)
			for(var i=0; i<ls_amt1.length+1; i++) {
					if (ls_amt1.substring(i,i+1) >= "0" &&
							ls_amt1.substring(i,i+1) <= "9" ||
							ls_amt1.substring(i,i+1) == ".") {

							ls_amt2 = ls_amt2 + ls_amt1.substring(i,i+1);
				}
			}
			break;
		case "D":
			// 숫자(-부호/소수점 포함)
			for(var i=0; i<ls_amt1.length+1; i++) {
					if (ls_amt1.substring(i,i+1) >= "0" &&
							ls_amt1.substring(i,i+1) <= "9" ||
							ls_amt1.substring(i,i+1) == "." ||
							ls_amt1.substring(i,i+1) == "-") {

							ls_amt2 = ls_amt2 + ls_amt1.substring(i,i+1);
				}
			}
			break;
	}

	obj.value = ls_amt2;
	return(true);
}

//날짜 포맷을 적용한다..
function formatDate(strDate, saper) {
	if(strDate == "" || strDate == null) {
		return "";
	}

	if(strDate.length == 10) {
		return strDate.substring(0,4)+saper+strDate.substring(5,7)+saper+strDate.substring(8,10);
	} else if(strDate.length == 8) {
		return strDate.substring(0,4)+saper+strDate.substring(4,6)+saper+strDate.substring(6,8);
	}
}

// autocomplete 셋팅
function setSheetAutocomplete(sheet,col,sabun) {
	var scriptTxt = "";
	scriptTxt += "<script>";
	scriptTxt += "function "+sheet+"_OnBeforeEdit(Row, Col) {";
	scriptTxt += "	try{";
	scriptTxt += "		autoCompleteInit("+col+","+sheet+",Row,Col);";
	scriptTxt += "	}catch(e){";
	scriptTxt += "	 	alert(e.message);";
	scriptTxt += "	}";
	scriptTxt += "}";
	scriptTxt += "";
	scriptTxt += "function "+sheet+"_OnAfterEdit(Row, Col) {";
	scriptTxt += "	try{";
	scriptTxt += "		autoCompleteDestroy("+sheet+");";
	scriptTxt += "	}catch(e){";
	scriptTxt += "		alert(e.message);";
	scriptTxt += "	}";
	scriptTxt += "}";
	scriptTxt += "";
	scriptTxt += "function "+sheet+"_OnKeyUp(Row, Col, KeyCode, Shift) {";
	scriptTxt += "	try{";
	scriptTxt += "		autoCompletePress("+col+",Row,Col,KeyCode);";
	scriptTxt += "	}catch(e){";
	scriptTxt += "		alert(e.message);";
	scriptTxt += "	}";
	scriptTxt += "}";
	scriptTxt += "</script>";
	document.write(scriptTxt);

	$(document).ready(function() {
		eval(sheet).SetEditArrowBehavior(2);
		$("<form></form>",{id:"empForm", name:"empForm"}).html('<input type="hidden" name="searchStatusCd" value="A" /><input type="hidden" name="searchUserId" id="searchUserId" value="'+sabun+'" />').appendTo('body');
	});
}

function getMultiSelect(val) {
	if(val == null || val == "" ) return "";
	return "'"+String(val).split(",").join("','")+"'";
}

// 인풋박스의 입력값을 byte단위로 제한하는 jquery 플러그인
// $("인풋").maxbyte(10)
(function($){
	$.fn.extend({
		maxbyte: function(options) {
			var defaults = {maxbyte:options};
			var options =  $.extend(defaults, options);
			return this.each(function() {
				var obj = $(this);

				obj.keyup(function(e) {
					var ls_str = $(this).val();
					var li_str_len = ls_str.length; //전체길이

					//변수초기화
					var li_max = options.maxbyte; //제한할 글자수 크기
					var i = 0;
					var li_byte = 0;   //한글일경우 3, 그외글자는 1을 더함
					var li_len = 0;    // substring하기 위해 사용
					var ls_one_char = "";  //한글자씩 검사
					var ls_str2 = "";      //글자수를 초과하면 제한한 글자전까지만 보여줌.

					for(i=0; i< li_str_len; i++)
					{
						ls_one_char = ls_str.charAt(i);   //한글자 추출
						if(escape(ls_one_char).length > 4) li_byte +=3;   //한글이면 2를 더한다
						else li_byte++;     //한글아니면 1을 다한다

						if(li_byte <= li_max) li_len = i + 1;
					}

					if(li_byte > li_max) {
						alert( li_max + "byte이상 입력할 수 없습니다.");
						ls_str2 = ls_str.substr(0, li_len);
						$(this).val(ls_str2);
					}
				});
				// 초기에 값입력된 값을 설정한다.
				obj.keyup();
			});
		}
	});
})(jQuery);


//주민번호 7번째 자리의 규칙 ########################
//1800년대: 남자 9, 여자 0
//1900년대: 남자 1, 여자 2
//2000년대: 남자 3, 여자 4
//2100년대: 남자 5, 여자 6
//외국인 등록번호: 남자 7, 여자 8

//주민번호, 외국인 등록번호의  validation 체크 함수
function isValid_socno(socno){

	var socnoStr = socno.toString();
	a = socnoStr.substring(0, 1);
	b = socnoStr.substring(1, 2);
	c = socnoStr.substring(2, 3);
	d = socnoStr.substring(3, 4);
	e = socnoStr.substring(4, 5);
	f = socnoStr.substring(5, 6);
	g = socnoStr.substring(6, 7);
	h = socnoStr.substring(7, 8);
	i = socnoStr.substring(8, 9);
	j = socnoStr.substring(9, 10);
	k = socnoStr.substring(10, 11);
	l = socnoStr.substring(11, 12);
	m = socnoStr.substring(12, 13);
	month = socnoStr.substring(2,4);
	day = socnoStr.substring(4,6);
	socnoStr1 = socnoStr.substring(0, 7);
	socnoStr2 = socnoStr.substring(7, 13);

	if(g == 5 || g == 6 || g == 7 || g == 8){

		var sum = 0;
		var odd = 0;

		buf = new Array(13);
		for (i = 0; i < 13; i++) buf[i] = parseInt(socno.charAt(i));

		odd = buf[7]*10 + buf[8];

		if (odd%2 != 0) {
		return false;
		}

		if ((buf[11] != 6)&&(buf[11] != 7)&&(buf[11] != 8)&&(buf[11] != 9)) {
		return false;
		}

		multipliers = [2,3,4,5,6,7,8,9,2,3,4,5];
		for (i = 0, sum = 0; i < 12; i++) sum += (buf[i] *= multipliers[i]);


		sum=11-(sum%11);

		if (sum>=10) sum-=10;

		sum += 2;

		if (sum>=10) sum-=10;

		if ( sum != buf[12]) {
		return false;
		}
		else {
		return true;
		}
	}
	else {



		// 월,일 Validation Check
		if(month <= 0 || month > 12) { return false; }
		if(day <= 0 || day > 31) { return false; }

		// 주민등록번호에 공백이 들어가도 가입이 되는 경우가 발생하지 않도록 한다.
		if (isNaN(socnoStr1) || isNaN(socnoStr2))  { return false; }

		temp=a*2+b*3+c*4+d*5+e*6+f*7+g*8+h*9+i*2+j*3+k*4+l*5;
		temp=temp%11;
		temp=11-temp;
		temp=temp%10;

		if(temp == m) {
			return true;
		} else {
			return false;
		}
	}
}

function Num_Comma(obj) {
		str = obj.value;
		s = new String(str);

		s=s.replace(/\D/g,"");

		if (s.substr(0,1)==0 ) {
									s = s.substr(1);
		}

		l=s.length-3;
		while(l>0) {
									s=s.substr(0,l)+","+s.substr(l);
									l-=3;
		}

		obj.value = s;
}

function cc(obj, color) {
		obj.style.backgroundColor = color;
}

function checknum(num)
{
	var s = new String(num.value);
	s=s.replace(/\D/g,"");
	var val="-0123456789";
	var string=num.value;
	var len=string.length;
	for(i=0;i<len;i++)
	{
		if(val.indexOf(string.substring(i,i+1))<0)
		{
			num.value = s;
			return;
		}
	}
}

function openWindow(url, name, width, height)   {
	var top     =       screen.height / 2 - height / 2 - 50;
	var left    =       screen.width / 2 - width / 2 ;
	var win = open(url, name, 'width='+width+',height='+height+',top='+top+',left='+left+',scrollbars=yes,resizable=yes,status=yes,toolbar=no,menubar=no');
}

// ****************************************************************************
// Char c가 영문자 인지 체크
// RETURN : true/false
// ***************************************************************************
function isLetterChar(c) {
	return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) );
}

// ****************************************************************************
// Char c가 숫자 인지 체크
// RETURN : true/false
// ***************************************************************************
function isDigitChar(c) {
	return ((c >= "0") && (c <= "9"));
}
// ****************************************************************************
// Char C가 whitechar 인지 체크
// ****************************************************************************
function isWhiteChar(c) {
	return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
}
// ****************************************************************************
// * char ch 가 한글인지 체크
// RETURN : true/false
// ****************************************************************************
function isKoreanChar( ch ) {
	var chStr = escape(ch); // ISO-Latin-1 문자셋으로 변경
	if (chStr.length < 2)
		return false;

	// 한글 ==> %uAC00 ~ %uD7A3
	if (chStr.substring(0, 2) == '%u') {
		if (chStr.substring(2,4) == '00')
			return false;
		else
			return true;         // 한글
	} else if (chStr.substring(0,1) == '%') {
		if (parseInt(chStr.substring(1,3), 16) > 127)
			return true;        // 한글
		else
			return false;
	} else return false;
}
// ************************************************
// str 이 공백이나 NULL 이면 TRUE 아니면 FALSE *
// ************************************************
function isEmpty(str) {
	if( str != null )
	{
		for( i=0 ; i < str.length ; i++)
		{
			if( !isWhiteChar( str.charAt(i) ) )
			return false;
		}
	}
	return ((str == null) || (str.length == 0));
}
// ****************************************************************************
// str 이 공백이나 텝 , 리턴 문자들로 실제문자가 없을경우 TRUE 아니면 FALSE *
// ****************************************************************************
function isWhitespace (str) {
	var whitespace = " \t\n\r";
	var i;
	if (isEmpty(str)) {
		return true;
	}
	for (i = 0; i < str.length; i++) {
		var chr = str.charAt(i);
		if (whitespace.indexOf(chr) == -1) {
			return false;
		}
	}
	return true;
}
// ***************************************************************************
// strnumber가 유효한 숫자타입인지 체크
// 파라메터 : strnumber(체크할 문자열)
// exceptstr(숫자이외에 허용 가능한 문자열)
// RETURN : true/false
// ****************************************************************************
function isNumber( strnumber, exceptstr) {
	var i, j;

	for( i=0; i<strnumber.length ; i++) {
		if(  isDigitChar( strnumber.charAt(i) ) )
			continue;
		for(j=0; j<exceptstr.length; j++) {
			if( strnumber.charAt(i) == exceptstr.charAt(j) )
				break;
		}
		if( j == exceptstr.length )
			return false;
	}

	return true;
}

// ****************************************************************************
// str 이 영문,숫자 조합으로 strSize 보다 작은지 체크
// RETURN : true/false
// ***************************************************************************
function isAlphaNumeric( str , strSize ) {
	var i;

	if ( str.length > strSize )
		return false

		for (i = 0; i < str.length; i++) {
			var c = str.charAt(i);
			if (!(isLetterChar(c) || isDigitChar(c)))
				return false;
		}

	return true;
}
// *****************************************************************************
// * HTML TAG 제거
// *****************************************************************************
function stripHTMLtag(string) {
	var objStrip = new RegExp();
	objStrip = /[<][^>]*[>]/gi;
	return string.replace(objStrip, "");
}

// *******************************************************************
// 년월 입력시 마지막 일자
function  getEndOfMonthDay( yy, mm )
{
	var max_days=0;
	if(mm == 1) {
		max_days = 31 ;
	} else if(mm == 2) {
		if ((( yy % 4 == 0) && (yy % 100 != 0)) || (yy % 400 == 0))
			max_days = 29;
		else
			max_days = 28;
	}
	else if (mm == 3)   max_days = 31;
	else if (mm == 4)   max_days = 30;
	else if (mm == 5)   max_days = 31;
	else if (mm == 6)   max_days = 30;
	else if (mm == 7)   max_days = 31;
	else if (mm == 8)   max_days = 31;
	else if (mm == 9)   max_days = 30;
	else if (mm == 10)  max_days = 31;
	else if (mm == 11)  max_days = 30;
	else if (mm == 12)  max_days = 31;
	else                return '';
	return max_days;
}
// *********************************************************************
// 날짜 유효성 검증하는 함수
function isValidDate(strDate)
{
	var retVal = true;
	if(strDate.length != 10){
		alert("날짜 형식이 잘못 되었습니다.\n ####-##-## or ####/##/## or ####.##.##");
		return false;
	}
	var inputDate = strDate.replace(/\-/g,'').replace(/\//g,'').replace(/\./g,'');
	var yyyy = inputDate.substring(0, 4);
	var mm   = inputDate.substring(4, 6);
	var dd   = inputDate.substring(6, 8);
	if (isNaN(yyyy) || parseInt(yyyy) < 1000){ alert("년도는 1000 이하일수 없습니다."); return false;}
	if (isNaN(mm) || parseFloat(mm) > 12 || parseFloat(mm) < 1){ alert("월의 값은 1부터 12사이의 값이 어야 합니다."); return false;}
	if (isNaN(dd) || parseFloat(dd) < 1 || (parseFloat(dd) > getEndOfMonthDay(parseFloat(yyyy.substring(2,4)), parseFloat(mm))) ) { alert("일자는 해당 달 범위안이 어야합니다. \n 1~31 or 1~28"); return false;}
	return true;
}

/**
 * 필수값 체크
 *
 * @param id: 체크할 ID속성
 * @param msg: 필수값 아닐 시 호출 msg
 * @returns boolen
 */
function nullCheck(id, msg) {

	if($("#"+id).val() == null || $("#"+id).val() == "" ) {
		alert(msg);
		$("#"+id).focus();
		return false;
	}

	return true;
}

//날짜 유효성 검증하는 함수
function isValidDateComma(strDate)
{
	var retVal = true;
	var inputDate = strDate.replace(/\./g,'');

	if(inputDate.length != 8){
		alert("날짜 형식이 잘못 되었습니다.\n ####.##.##");
		return false;
	}

	var yyyy = inputDate.substring(0, 4);
	var mm   = inputDate.substring(4, 6);
	var dd   = inputDate.substring(6, 8);
	if (isNaN(yyyy) || parseInt(yyyy) < 1000){ alert("년도는 1000 이하일수 없습니다."); return false;}
	if (isNaN(mm) || parseFloat(mm) > 12 || parseFloat(mm) < 1){ alert("월의 값은 1부터 12사이의 값이 어야 합니다."); return false;}
	if (isNaN(dd) || parseFloat(dd) < 1 || (parseFloat(dd) > getEndOfMonthDay(parseFloat(yyyy.substring(2,4)), parseFloat(mm))) ) { alert("일자는 해당 달 범위안이 어야합니다. \n 1~31 or 1~28"); return false;}
	return true;
}

//유효월 체크
function monthCheck(val){
	var msg = "유효한 월 형태가 아닙니다.\n 01~12까지 입력 가능 합니다.";
	var pattern = /[0-9]{2}/;
	if(!pattern.test(val)) {
		alert("월은 mm로 입력해 주세요");
		return false;
	}


	var month = parseInt(val);

	if( month < 1 || month > 12) {
		alert(msg);
		return false;
	}
	return true;
}

function chkPattern(str,type)	//형식 체크
{
	switch(type)
	{
		case "NUM": //숫자만
		pattern = /^[0-9]+$/;
			break;

	case "PHONE" :		// 전화번호 (####-####-####)
		pattern = /^[0-9]{2,4}-[0-9]{3,4}-[0-9]{4}$/;
		break;

	case "MOBILE" :		// 휴대전화 (0##-####-####)
		pattern = /^0[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$/;
		break;

	case "ZIPCODE" :	// 우편번호 (###-###)
		pattern = /^[0-9]{3}-[0-9]{3}$/;
		break;

		case "EMAIL": //메일
		//pattern = /^[._a-zA-Z0-9-]+@[._a-zA-Z0-9-]+\.[a-zA-Z]+$/;
		pattern = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9.-]+\.)+[a-zA-Z]{2,4}$/;
			break;

		case "DOMAIN": //영자 숫자와	.	다음도 영자
		pattern = /^[.a-zA-Z0-9-]+.[a-zA-Z]+$/;
			break;

		case "ENG": //영자만
			pattern = /^[a-zA-Z]+$/;
			break;

		case "ENGNUM": //영자와	숫자
			pattern = /^[a-zA-Z0-9]+$/;
			break;

		case "ACCOUNT": //숫자	와 '-'
			pattern = /^[0-9-]+$/;
			break;

		case "HOST": //영자	와 '-'
			pattern = /^[a-zA-Z-]+$/;
			break;
		case "ID": //첫글자는 영자 그 뒤엔 영어숫자 6이상 15자리	이하
			//pattern = /^[a-zA-Z]{1}[a-zA-Z0-9_-]{5,15}$/;
			pattern = /^[a-zA-Z]{1}[a-zA-Z0-9]{5,15}$/;
			break;

		case "ID2": //첫글자는	영자 그뒤엔	영어숫자 4이상 15자리	이하
			pattern = /^[a-zA-Z0-9._-]+$/;
			break;

		case "DATE": //	형식 : 2002-08-15
			pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
			break;

		case "DATE2": //	형식 : 2002-08-15
				pattern = /^[0-9]{4}.[0-9]{2}.[0-9]{2}$/;
				break;

	case "JUMIN" :		// 주민등록번호
		//pattern = /^[0-9]{6}-[0-9]{7}$/;
		pattern = /^[0-9]{13}$/;
		break;

	case "GRADE" :		// 주민등록번호
		//pattern = /^[0-9]{6}-[0-9]{7}$/;
		pattern = /^[0-9]+(.[0-9])?$/;
		break;

		default :
			return false;
	}
	return pattern.test(str);
}

//날짜 일수 구하기
function getDaysBetween(startDt,endDt) {
	if(startDt == "" || endDt == "") {
		return "";
	}

	var startDt = new Date(startDt.substring(0,4),startDt.substring(4,6)-1,startDt.substring(6,8));
	var endDt = new Date(endDt.substring(0,4),endDt.substring(4,6)-1,endDt.substring(6,8));

	return Math.floor(endDt.valueOf()/(24*60*60*1000)- startDt.valueOf()/(24*60*60*1000) + 1);

}


/*=======================================================================*
 * ajax POST 한글깨짐 방지
 *=======================================================================*/
function ajaxescape( value ) {
		return escape(encodeURIComponent(value)).replace(/\+/g, '%2B');
}

//Camel변환
function convCamel(str) {
	var before = str.toLowerCase();
	var after = "";
	var bs = before.split("_");

	if(bs.length < 2) {
		return bs;
	}
	for (var i=0; i<bs.length; i++) {
		if(bs[i].length > 0){
			if(i==0)
				after += bs[i].toLowerCase();
			else
				after += bs[i].toLowerCase().substr(0,1).toUpperCase()+bs[i].substr(1,bs[i].length-1);
		}
	}
	return after;
}

/* ----------------------------------------------------------------------------
 * 특정 날짜에 대해 지정한 값만큼 가감(+-)한 날짜를 반환
 *
 * 입력 파라미터 -----
 * pInterval : "yyyy" 는 연도 가감, "m" 은 월 가감, "d" 는 일 가감
 * pAddVal  : 가감 하고자 하는 값 (정수형)
 * pYyyymmdd : 가감의 기준이 되는 날짜
 * pDelimiter : pYyyymmdd 값에 사용된 구분자를 설정 (없으면 "" 입력)
 *
 * 반환값 ----
 * yyyymmdd 또는 함수 입력시 지정된 구분자를 가지는 yyyy?mm?dd 값
 *
 * 사용예 ---
 * 2008-01-01 에 3 일 더하기 ==> addDate("d", 3, "2008-08-01", "-");
 * 20080301 에 8 개월 더하기 ==> addDate("m", 8, "20080301", "");
--------------------------------------------------------------------------- */
function addDate(pInterval, pAddVal, pYyyymmdd, pDelimiter)
{
	var yyyy;
	var mm;
	var dd;
	var cDate;
	var oDate;
	var cYear, cMonth, cDay;

	if(pYyyymmdd == "") {
		return "";
	}

	if (pDelimiter != "") {
		pYyyymmdd = pYyyymmdd.replace(eval("/\\" + pDelimiter + "/g"), "");
	}

	yyyy = pYyyymmdd.substr(0, 4);
	mm  = pYyyymmdd.substr(4, 2);
	dd  = pYyyymmdd.substr(6, 2);

	if (pInterval == "yyyy") {
		yyyy = (yyyy * 1) + (pAddVal * 1);
	} else if (pInterval == "m") {
		mm  = (mm * 1) + (pAddVal * 1);
	} else if (pInterval == "d") {
		dd  = (dd * 1) + (pAddVal * 1);
	}

	cDate = new Date(yyyy, mm - 1, dd) // 12월, 31일을 초과하는 입력값에 대해 자동으로 계산된 날짜가 만들어짐.
	cYear = cDate.getFullYear();
	cMonth = cDate.getMonth() + 1;
	cDay = cDate.getDate();

	cMonth = cMonth < 10 ? "0" + cMonth : cMonth;
	cDay = cDay < 10 ? "0" + cDay : cDay;

	//if (pDelimiter != "") {
		return cYear + pDelimiter + cMonth + pDelimiter + cDay;
	//} else {
	//	return cYear + cMonth + cDay;
	//}
}

/*************************************************************************************
left padding s with c to a total of n chars
**************************************************************************************/
function lpad(s, c, n) {
	if (! s || ! c || s.length >= n) {
		return s;
	}
	var max = (n - s.length)/c.length;
	for (var i = 0; i < max; i++) {
		s = c + s;
	}
	return s;
}

function in_array (needle, haystack, argStrict) {
		// http://kevin.vanzonneveld.net
		// +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
		// +   improved by: vlado houba
		// +   input by: Billy
		// +   bugfixed by: Brett Zamir (http://brett-zamir.me)
		// *     example 1: in_array('van', ['Kevin', 'van', 'Zonneveld']);
		// *     returns 1: true
		// *     example 2: in_array('vlado', {0: 'Kevin', vlado: 'van', 1: 'Zonneveld'});
		// *     returns 2: false
		// *     example 3: in_array(1, ['1', '2', '3']);
		// *     returns 3: true
		// *     example 3: in_array(1, ['1', '2', '3'], false);
		// *     returns 3: true
		// *     example 4: in_array(1, ['1', '2', '3'], true);
		// *     returns 4: false

		var key = '',
			strict = !! argStrict;

		if (strict) {
			for (key in haystack) {
				if (haystack[key] === needle) {
					return true;
				}
			}
		} else {
			for (key in haystack) {
				if (haystack[key] == needle) {
					return true;
				}
			}
		}

		return false;
	}

function replaceAll(sValue, param1, param2) {
		return sValue.split(param1).join(param2);
}

function checkFromToDate(fromObj, toObj, fromTxt, toTxt, dateType) {
	if (dateType == "YYYYMM") {
		var fromDate = fromObj.val().replace(/\-/g,'').replace(/\//g,'');
		var toDate = toObj.val().replace(/\-/g,'').replace(/\//g,'');

		var fromYyyy = fromDate.substring(0, 4);
		var fromMm = fromDate.substring(4, 6);
		var toYyyy = toDate.substring(0, 4);
		var toMm = toDate.substring(4, 6);

		if (fromDate.length != 6) {
			alert(fromTxt + "을 바르게 입력하십시오.");
			fromObj.focus();
			return false;
		}
		if (toDate.length != 6) {
			alert(toTxt + "을 바르게 입력하십시오.");
			toObj.focus();
			return false;
		}
		if (isNaN(fromYyyy) || parseInt(fromYyyy) < 1000) {
			alert(fromTxt + "을 바르게 입력하십시오.");
			fromObj.focus();
			return false;
		}
		if (isNaN(fromMm) || parseFloat(fromMm) > 12 || parseFloat(fromMm) < 1) {
			alert(fromTxt + "을 바르게 입력하십시오.");
			fromObj.focus();
			return false;
		}
		if (isNaN(toYyyy) || parseInt(toYyyy) < 1000) {
			alert(toTxt + "을 바르게 입력하십시오.");
			toObj.focus();
			return false;
		}
		if (isNaN(toMm) || parseFloat(toMm) > 12 || parseFloat(toMm) < 1) {
			alert(toTxt + "을 바르게 입력하십시오.");
			toObj.focus();
			return false;
		}
		if (parseInt(fromDate) > parseInt(toDate)) {
			alert("시작년월이 종료년월보다 큽니다.");
			toObj.focus();
			return false;
		}

	} else if (dateType == "YYYYMMDD") {
		var fromDate = fromObj.val();
		var toDate = toObj.val();

		// 일자 유효성 체크
		var rtn = isValidDate(fromDate);
		if (rtn == false) {
			fromObj.focus();
			return false;
		}
		var rtn = isValidDate(toDate);
		if (rtn == false) {
			toObj.focus();
			return false;
		}

		fromDate = fromDate.replace(/\-/g,'').replace(/\//g,'');
		toDate = toDate.replace(/\-/g,'').replace(/\//g,'');

		if (parseInt(fromDate) > parseInt(toDate)) {
			alert("시작일이 종료일보다 큽니다.");
			toObj.focus();
			return false;
		}

	}

	return true;
}

/* location.href
 * location.replace
 * location.reload
 * => IE7,8,9 에서 referer 생성 못할때 생김
 * */

function redirect(url, target) {
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){
				var referLink = document.createElement('a');
				referLink.href = url;
				referLink.target = target;
				document.body.appendChild(referLink);
				referLink.click();
		} else {
		(target=="_blank") ? window.open(url,target): $(location).attr('href',url);
		}
}
/*
 * Camel Notation, Underscore
 * under2camel('abcd_efg')=>abcdEfg
 * under2camel('ABCD_EFG')=>abcdEfg
 */
var under2camel=function(str){
		return str.toLowerCase().replace(/(\_[a-z])/g, function(arg){
				return arg.toUpperCase().replace('_','');
		});
};

/*
 * Camel Notation, Underscore
 * camel2under('abcdEfg')=>ABCD_EFG
 * camel2under('abcdEFg')=>ABCD_E_FG
 */
var camel2under=function(str){
		return str.replace(/([A-Z])/g, function(arg){
				return "_"+arg.toLowerCase();
		}).toUpperCase();
};

//소숫점 반올림 (값, 자릿수)
function numRound(n, pos) {
	var digits = Math.pow(10, pos);

	var sign = 1;
	if (n < 0) {
		sign = -1;
	}

	// 음수이면 양수처리후 반올림 한 후 다시 음수처리
	n = n * sign;
	var num = Math.round(n * digits) / digits;
	num = num * sign;

	var str = num.toFixed(20);

	str = str.replace(/0+$/, '').replace(/\.$/, '');

	return str;
}

/**
 * Progress Bar 생성 소멸
 * bln : true , bln : false
 */
function progressBar(bln){
	if(bln == true){
		var bodyWidth = $("body").width();
		var bodyHeight = $("body").height();
//		var objframe =$("<iframe id='popFrame' style='width:800px; height:800px; background-color:white;' onclick='popClose();'></iframe>");
		var objframe =$("<iframe id='popFrame' style='width:800px; height:800px; background-color:white;' 						></iframe>");

		var objDiv = $("<div />", {
			id: 'loadingDiv1',
			click: function(){popClose();},
			style: "position:absolute; width:"+ bodyWidth + "px; height:" + bodyHeight + "px; top:0; z-index:997; text-align:center; vertical-align:middle; background-color:gray;opacity: 0.4;"
		});

		var objContent = $("<div />", {
			id:"loadingDiv2",
			style:"position:absolute; top:"+(bodyHeight / 2 - 25)+"px; left:"+(bodyWidth / 2 - 150)+"px; background-color:white; width:300px; height:50px; opacity: 1.0;  z-index:998;text-align:center; vertical-align:middle;"
		});

		var img = $("<img />", {
			id:"loadingImg",
			style:"opacity: 1.0;  z-index:999; margin-top:5px",
			src:"/common/images/common/InfLoading.gif"
		});

		objContent.append(img);
		objContent.append("<br/><span id='loadingText'>Loading....</span>");

		//objDiv.append(objContent) ;

		$("body").append(objDiv);
		$("body").append(objContent);
	} else {
			$("#loadingText").remove();
			$("#loadingImg").remove();
			$('#loadingDiv1').remove();
			$('#loadingDiv2').remove();
	}
}

function getInternetExplorerVersion() {
	var rv = true;
		var trident = navigator.userAgent.match(/Trident\/(\d)/i);
/*
IE 8 = trident/4.0
IE 9 = trident/5.0
IE 10 = trident/6.0
 */
		if(trident != null && trident != "null" && trident != "") {
				var ie_num= (String(trident)).split(',');

				if(ie_num[1] >= 6){ //IE9부터
					rv = true;
				} else {
					rv = false; //IE8이하 에서만 모달 팝업 사용
				}
		}

		return rv;
}


//회사변경
function setCompanyWidget() {
	$("#companyWidgetMain").click(function() {return false;});
	$("#companyMgr").click(function(){$(document).click();$("#companyWidgetMain").show();$(document).click(function() { $("#companyCancel").click(); });return false;});
	$("#companyCancel").click(function(){$(document).unbind("click");$("#companyWidgetMain").hide();return false;});
}

//회사 조회
function createCompanyList(gLn)	{
	var	companyList	=	ajaxCall("/chgCompanyPopup.do",	""	,false).list;
	if(companyList!=""){
		$("#companyMgr").css("display","inline");
		$(companyList).each(function(idx,str){
			var className = str.enterCd ==gLn?"on":"";
			$("#companyList").append("<li companyCd="+str.enterCd+" class="+className+"><span>&nbsp;"+ str.enterNm +"</span></li>");
		});

		$("#companyList li").click(function()	{
			var user = ajaxCall("/chgCompany.do","company="+	$(this).attr("companyCd"),false);
			if (user.isUser != "exist") {
				alert("해당 사용자로  정상적으로  로그인 할수 없습니다.");
			}
			redirect("/Main.do",	"_top");
		});
	}else{
		$("#companyMgr").css("display","none");
	}
}

//input 생성
function createInput(form,id) {
			$("<input></input>",{id:id,name:id,type:"hidden"}).appendTo($("#"+form));
}


//from 생성
function createForm(id) {
	$form =$("#"+id)
	if($form.length > 0){
		//$form.empty();
		$form.remove();
	}
	$form = $("<form><form/>"). attr({id:id,name:id,method:'POST'});
	$(document.body).append($form);
}


/*
$(function(){var s= location.href;addListener(document.body, "copy", onStartCopy);});
function onStartCopy() {try {clearSelection();}catch (e) {}}
function clearSelection() {if (document.selection && document.selection.empty) {document.selection.empty();} else if (window.getSelection) {var sel = window.getSelection();sel.removeAllRanges();}}
function addListener( element, name, observer, useCapture ) {useCapture = useCapture || false;if (element.addEventListener) {element.addEventListener(name, observer, useCapture);}else if (element.attachEvent) {element.attachEvent('on' + name, observer);}}
function removeListener( element, name, observer, useCapture ) {useCapture = useCapture || false;if (element.removeEventListener) {element.removeEventListener(name, observer, useCapture);}else if (element.detachEvent) {element.detachEvent('on' + name, observer);}}
*/

/**
 * 세션 만료 info 페이지로 이동.
 * -. 팝업이면 모든 팝업을 닫음.
 */
function moveInfoPage(code){
	if( top.opener != null && top.opener != undefined ){ //팝업 여부
		top.opener.moveInfoPage(code);
		top.close();
	}else{
		top.location.replace("/Info.do?code="+code);
	}
}

/**
 * ex) alertTimer("급여계산 되었습니다.", 1000, globalWindowPopup, function(){globalWindowPopup.close(); doAction1("SearchPeople");});
 */
function alertTimer(pMsg, pTime, pWin, pFn) {
	var time = pTime || 100;
	var type = $.type( pFn );
	var win = window;

	if(pWin && !pWin.closed) {
		win = pWin;
	}

	setTimeout(function() {
		win.focus();
		win.alert(pMsg);
		if(type == "function") {
			pFn();
		} else if(type == "string") {
			$.globalEval(pFn);
		}
	}, pTime);
}

/**
 *  sur 찾는 함수
 */
function getMainSurl() {
	var that = window.top;
	if(that.parent) {
		if(that.parent.$("#surl").length > 0) {
			return that.parent.$("#surl").val();
		} else {
			if(that.parent.opener) {
				return that.parent.opener.getMainSurl();
			} else {
				return that.parent.getMainSurl();
			}
		}
	}
}

function setHeaderPageObj(key, obj) {
	var that = window.top;
	if(that.parent) {
		if(typeof that.parent.setPageObj != 'undefined') {
			that.parent.setPageObj(key, obj);
		} else {
			if(that.parent.opener) {
				that.parent.opener.setHeaderPageObj(key, obj);
			} else {
				that.parent.setHeaderPageObj(key, obj);
			}
		}
	}
}

function headerReSetTimer() {
//	var that = window.top;
//	if(that.parent) {
//		if(typeof that.parent.resetTimer != 'undefined') {
//			that.parent.resetTimer();
//		} else {
//			if(that.parent.opener) {
//				that.parent.opener.headerReSetTimer();
//			} else {
//				that.parent.headerReSetTimer();
//			}
//		}
//	}
}

function enablePage(isLastPopup) {
	$(".lockDiv", "body").removeClass("lockDiv");
}

function disablePage(isLastPopup) {
	var disableDiv = $("<div>").addClass("lockDiv");
	$("body").append(disableDiv);

	var topPageFocus = function(win) {
		var rtnWin = win;

		if(win.top.opener) {
			rtnWin = topPageFocus(win.top.opener);
		}

		return rtnWin;
	}

	if(isLastPopup) {
		var win = topPageFocus(window.top.opener);
		win.focus();
//		window.top.opener.focus();
	}
}

/**
 * IBsheet의 Employee auto_complete
 *
 * setSheetAutocompleteEmp 함수로 초기화.
 * 선택된 사원 data는 getReturnValue 함수에서 처리 (pGubun = sheetAutocompleteEmp)
 *
 * @param sheet (string) : 자동완성을 사용할 sheet명
 * @param colSaveName (string) : 자동완성을 사용할 컬럼 saveName
 * @param renderItem (function) : 자동완성시 출력될 박스 포맷. Undefined일 경우 employeeRenderItem function 사용.
 * @returns
 *
 * by sjkim
 */

function setSheetAutocompleteEmp(sheet, colSaveName, renderItem , callBackFunc ) {

	var col = eval(sheet).SaveNameCol(colSaveName);

    var scriptTxt = "";
    scriptTxt += "<script>";
    scriptTxt += "function " + sheet + "_OnBeforeEdit(Row, Col) {";
    scriptTxt += "	try{";
    scriptTxt += "		autoCompleteInit(" + col + "," + sheet + ",Row,Col,"+ renderItem +"," + callBackFunc + ");";
    scriptTxt += "	}catch(e){";
    scriptTxt += "	 	alert(e.message);";
    scriptTxt += "	}";
    scriptTxt += "}";
    scriptTxt += "";
    scriptTxt += "function " + sheet + "_OnAfterEdit(Row, Col) {";
    scriptTxt += "	try{";
    scriptTxt += "		autoCompleteDestroy(" + sheet + ");";
    scriptTxt += "	}catch(e){";
    scriptTxt += "		alert(e.message);";
    scriptTxt += "	}";
    scriptTxt += "}";
    scriptTxt += "";
    scriptTxt += "function " + sheet + "_OnKeyUp(Row, Col, KeyCode, Shift) {";
    scriptTxt += "	try{";
    scriptTxt += "		autoCompletePress(" + col + ",Row,Col,KeyCode);";
    scriptTxt += "	}catch(e){";
    scriptTxt += "		alert(e.message);";
    scriptTxt += "	}";
    scriptTxt += "}";
    scriptTxt += "</script>";

    $(document).ready(function() {
        eval(sheet).SetEditArrowBehavior(2);
        $("<form></form>", {
            id: "empForm1",
            name: "empForm1"
        }).html('<input type="hidden" name="searchStatusCd" value="AA" /> <input type="hidden" id="searchEmpType" name="searchEmpType" value="I"/>').appendTo('body')
        .append(scriptTxt);
    });
}

var intervalDestory;
// autocomplete 생성
function autoCompleteInit(opt, sheet, Row, Col, renderItem , callBackFunc) {
    if (Col != opt) return;

    //자동완성 List form
    var autocompRenderItem
    var callBackFunctionItem;
    if(renderItem != undefined && renderItem != null ) {
    	autocompRenderItem = new Function ( "return "+ renderItem )();
    } else {
    	autocompRenderItem = employeeRenderItem1;
    }

    if ( callBackFunc != undefined ){

    	callBackFunctionItem = callBackFunc
    } else {
    	callBackFunctionItem = "getReturnValue";
    }

    if ($("#autoCompleteDiv").length == 0) {
        $('<div></div>', {
            id: "autoCompleteDiv"
        }).html("<input id='searchKeyword1' name='searchKeyword' type='text' />").appendTo('#empForm1');

        var inputId = "searchKeyword1";
        
        $("#searchKeyword1").autocomplete({
            source: function(request, response) {
                $.ajax({
                    url: "/ifw/emp/list",
                    dateType: "json",
                    type: "post",
                    data: $("#empForm1").serialize(),
                    success: function(data) {
                        response($.map(data.DATA, function(item) {
                            return {
                                label: item.sabun + ",  " + item.enterCd,
                                searchNm: $("#searchKeyword1").val(),
                                //enterNm: item.enterNm, // 회사명
                                empHisId: item.empHisId,
                                enterCd: item.enterCd, // 회사코드
                                name: item.empNm, // 사원명
                                sabun: item.sabun, // 사번
                                //empYmd: item.empYmd, // 입사일
                                orgCd: item.orgCd, // 조직코드
                                orgNm: item.orgNm, // 조직명
                                dutyCd: item.dutyCd, // 직책코드
                                dutyNm: item.dutyNm, // 직책명
                                classCd: item.classCd, // 직급코드
                                classNm: item.classNm, // 직급명
                                posCd: item.posCd, // 직위코드
                                posNm: item.posNm, // 직위명
                                //resNo: item.resNo, // 주민번호
                                //resNoStr: item.resNoStr, // 주민번호 앞자리
                                statusCd: item.statusCd, // 재직/퇴직
                                //statusNm: item.statusNm, // 재직/퇴직
                                value: item.empNm,
                                callBackFunc : callBackFunctionItem
                            };
                        }));
                    }
                });
            },
    		autoFocus: true,
            minLength: 1,
            focus: function() {
                return false;
            },
            open: function() {
                $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
            },
            close: function() {
                $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
            }
        }).data("uiAutocomplete")._renderItem = autocompRenderItem;
    };

    //autocomplete 선택되었을 때 Event Handler
    $("#autoCompleteDiv").off("autocompleteselect");
    $("#autoCompleteDiv").on("autocompleteselect", function(event, ui) {
    	var row = Row;
        sheet.SetCellText(Row, Col, ui.item.value);

        $("#autoCompleteInput").val("");
        autoCompleteDestroy(sheet);

        //상세 데이터 가져오기
        var params = "empHisId="+ ui.item.empHisId
        var data = ajaxCall("/ifw/emp",params,false);

        //직원을 선택시 Data Return

        var returnFunc1 = new Function ( "return "+ ui.item.callBackFunc )()

        if(typeof returnFunc1 != "undefined") {
        	gPRow = row;
        	pGubun = "sheetAutocompleteEmp";
        	returnFunc1( data.DATA );
        }

    });

    $(".GMVScroll>div").scroll(function() {
        destroyAutoComplete(sheet);
    });
    $(".GMHScrollMid>div").scroll(function() {
        destroyAutoComplete(sheet);
    });

    var pleft = sheet.ColLeft(sheet.GetSelectCol());
    var ptop = sheet.RowTop(sheet.GetSelectRow()) + sheet.GetRowHeight(sheet.GetSelectRow());
    //건수정보 표시줄의 높이 만큼.
    if (sheet.GetCountPosition() == 1 || sheet.GetCountPosition() == 2) ptop += 13;

    var point = fGetXY(document.getElementById("DIV_" + sheet.id));

    var left = point.x + pleft;
    var top = point.y + ptop - 30;

    var cWidth = 520;
    var cHeight = 104;
    var dWidth = $(window).width();
    var dHeight = $(window).height();

    if (dWidth < left + cWidth) left = dWidth - cWidth;
    if (dHeight < top + cHeight) top = top - cHeight - 28;
    if (top < 0) top = 0;

    $("#autoCompleteDiv").css("left", left + "px");
    $("#autoCompleteDiv").css("top", top + "px");
    clearTimeout(intervalDestory);
    sheet.$beforeEditEnterBehavior = sheet.GetEditEnterBehavior();
    sheet.SetEditEnterBehavior("none");
}

//autocomplete 키보드 이벤트
function autoCompletePress(opt, Row, Col, code) {
    if (Col != opt) return;

    //IBsheet에서 입력된 값을 가져와 자동완성에 넘김
    var e = jQuery.Event("keydown");
    e.keyCode = code;
    $("#searchKeyword1").trigger(e);
    
    //IBsheet input tag의 속성 - id:_editInput0 class:GMEditInput
    if( $("#_editInput0").length != 0 ) {
    	$("#searchKeyword1").val($("#_editInput0").val());
    } else {
    	//id:_editInput0 가 없는 경우도 있다. 그럴 경우 class:GMEditInput 로 검색
    	$("#searchKeyword1").val($(".GMEditInput").val());
    }
}

// autocomplete 제거
function autoCompleteDestroy(sheet) {
	clearTimeout(intervalDestory);
    intervalDestory = setTimeout(function() {
        destroyAutoComplete(sheet);
    }, 200);
}

//autocomplete 제거
function destroyAutoComplete(sheet) {
    $(".GMVScroll>div").unbind("scroll");
    $(".GMHScrollMid>div").unbind("scroll");

    $("#autoCompleteInput").autocomplete("destroy");
    $("#autoCompleteDiv").remove();

    //sheet.SetEditEnterBehavior("tab");
    sheet.SetEditEnterBehavior(sheet.$beforeEditEnterBehavior);
}

//autocomplete 리스트 포맷
function employeeRenderItem1(ul, item) {
    return $("<li />")
        .data("item.autocomplete", item)
        .append("<a class='autocomplete' style='width:240px;'>" +
            "<span style='width:40px;'>" + String(item.name).split(item.searchNm).join('<b>' + item.searchNm + '</b>') + "</span>" +
            "<span style='width:90px;'>" + item.sabun + "</span>" +
            "<span style='width:80px;'>" + item.orgNm + "</span>" +
            "</a>").appendTo(ul);
}


function getMultiSelect(val) {
    if (val == null || val == "") return "";
    return "'" + String(val).split(",").join("','") + "'";
}

function paramMapToJson(args) {
	var json = "";
	var i = 0;

	for(key in args) {
		if(key != "contains"){
			if(typeof args[key] == "object") {
				alert("arguments 값이 object 입니다.");
				return;
			}

			if(typeof args[key] != 'undefined' && args[key] != null) {
				args[key] = args[key].toString().replace(/\"/gi,'\\"');
				args[key] = args[key].toString().replace(/'/gi,"\'");
				args[key] = args[key].toString().replace(/\r\n/gi,"\n");
				args[key] = args[key].toString().replace(/\n/gi,"\\n");
				args[key] = args[key].toString().replace(/\t/gi,"    ");
			}

			json = json + ((i==0) ? "\"" : ",\"") +[key]+"\":\""+args[key]+"\"";
		}
		i++;
	}

	return json;
}

/**
 * IBsheet의 orgList auto_complete
 *
 * setSheetAutocompleteOrg 함수로 초기화.
 * 선택된 조직 data는 getOrgReturnValue 함수에서 처리 (pGubun = sheetAutocompleteOrg)
 *
 * @param sheet (string) : 자동완성을 사용할 sheet명
 * @param colSaveName (string) : 자동완성을 사용할 컬럼 saveName
 * @param renderItem (function) : 자동완성시 출력될 박스 포맷. Undefined일 경우 orgRenderItem function 사용.
 * @returns
 *
 * by sjkim
 */

function setSheetAutocompleteOrg(sheet, colSaveName, renderItem , callBackFunc ) {
	
	var col = eval(sheet).SaveNameCol(colSaveName);

    var scriptTxt = "";
    scriptTxt += "<script>";
    scriptTxt += "function " + sheet + "_OnBeforeEdit(Row, Col) {";
    scriptTxt += "	try{";
    scriptTxt += "		autoCompleteOrgInit(" + col + "," + sheet + ",Row,Col,"+ renderItem +"," + callBackFunc + ");";
    scriptTxt += "	}catch(e){";
    scriptTxt += "	 	alert(e.message);";
    scriptTxt += "	}";
    scriptTxt += "}";
    scriptTxt += "";
    scriptTxt += "function " + sheet + "_OnAfterEdit(Row, Col) {";
    scriptTxt += "	try{";
    scriptTxt += "		autoCompleteOrgDestroy(" + sheet + ");";
    scriptTxt += "	}catch(e){";
    scriptTxt += "		alert(e.message);";
    scriptTxt += "	}";
    scriptTxt += "}";
    scriptTxt += "";
    scriptTxt += "function " + sheet + "_OnKeyUp(Row, Col, KeyCode, Shift) {";
    scriptTxt += "	try{";
    scriptTxt += "		autoCompleteOrgPress(" + col + ",Row,Col,KeyCode);";
    scriptTxt += "	}catch(e){";
    scriptTxt += "		alert(e.message);";
    scriptTxt += "	}";
    scriptTxt += "}";
    scriptTxt += "</script>";
    
    $(document).ready(function() {
        eval(sheet).SetEditArrowBehavior(2);
        $("<form></form>", {
            id: "orgForm1",
            name: "orgForm1"
        }).html('<input type="hidden" id="searchOrgType" name="searchOrgType" value="I"/>').appendTo('body')
        .append(scriptTxt);
    });
}
var intervalOrgDestory;
//autocomplete 생성
function autoCompleteOrgInit(opt, sheet, Row, Col, renderItem , callBackFunc) {
 if (Col != opt) return;

 //자동완성 List form
 var autocompRenderItem;
 var callBackFunctionItem;
 if(renderItem != undefined && renderItem != null ) {
 	autocompRenderItem = new Function ( "return "+ renderItem )();
 } else {
 	autocompRenderItem = orgRenderItem1;
 }
 if ( callBackFunc != undefined ){

 	callBackFunctionItem = callBackFunc
 } else {
 	callBackFunctionItem = "getOrgReturnValue";
 }
 if ($("#autoCompleteOrgDiv").length == 0) {
     $('<div></div>', {
         id: "autoCompleteOrgDiv"
     }).html("<input id='orgKeyword' name='orgKeyword' type='text' />").appendTo('#orgForm1');

     var inputId = "orgKeyword";
     $("#orgKeyword").autocomplete({
         source: function(request, response) {
             $.ajax({
                 url: "/ifw/orgCode/list",
                 dateType: "json",
                 type: "post",
                 data: $("#orgForm1").serialize(),
                 success: function(data) {
                	 console.log(data.DATA);
                     response($.map(data.DATA, function(item) {
                         return {
                             label: item.orgNm + ",  " + item.enterCd,
                             searchNm: $("#orgKeyword").val(),
                             orgCd: item.orgCd, // 조직코드
                             orgNm: item.orgNm, // 조직명
                             value: item.orgNm,
                             callBackFunc : callBackFunctionItem
                         };
                     }));
                 }
             });
         },
 		autoFocus: true,
         minLength: 1,
         focus: function() {
             return false;
         },
         open: function() {
        	 console.log("orgopen");
        	 console.log($(this));
             $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
         },
         close: function() {
             $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
         }
         
     }).data("uiAutocomplete")._renderItem = autocompRenderItem;
 };
 //autocomplete 선택되었을 때 Event Handler
 $("#autoCompleteOrgDiv").off("autocompleteselect");
 $("#autoCompleteOrgDiv").on("autocompleteselect", function(event, ui) {
 	var row = Row;
     sheet.SetCellText(Row, Col, ui.item.value);

     $("#autoCompleteOrgInput").val("");
     autoCompleteOrgDestroy(sheet);

     //상세 데이터 가져오기
     var orgInfo = new Object();
     orgInfo.orgCd = ui.item.orgCd;
     orgInfo.orgNm = ui.item.orgNm;
     
     var data = JSON.stringify(orgInfo);

     //직원을 선택시 Data Return

     var returnFunc1 = new Function ( "return "+ ui.item.callBackFunc )()

     if(typeof returnFunc1 != "undefined") {
     	gPRow = row;
     	pGubun = "sheetAutocompleteOrg";
     	returnFunc1( data );
     }

 });
	 $(".GMVScroll>div").scroll(function() {
	     destroyAutoOrgComplete(sheet);
	 });
	 $(".GMHScrollMid>div").scroll(function() {
	     destroyAutoOrgComplete(sheet);
	 });
	 var pleft = sheet.ColLeft(sheet.GetSelectCol());
	 var ptop = sheet.RowTop(sheet.GetSelectRow()) + sheet.GetRowHeight(sheet.GetSelectRow());
	 //건수정보 표시줄의 높이 만큼.
	 if (sheet.GetCountPosition() == 1 || sheet.GetCountPosition() == 2) ptop += 13;
	
	 var point = fGetXY(document.getElementById("DIV_" + sheet.id));
	
	 var left = point.x + pleft;
	 var top = point.y + ptop - 30;
	
	 var cWidth = 520;
	 var cHeight = 104;
	 var dWidth = $(window).width();
	 var dHeight = $(window).height();
	
	 if (dWidth < left + cWidth) left = dWidth - cWidth;
	 if (dHeight < top + cHeight) top = top - cHeight - 28;
	 if (top < 0) top = 0;
	 $("#autoCompleteOrgDiv").css("left", left + "px");
	 $("#autoCompleteOrgDiv").css("top", top + "px");
	 clearTimeout(intervalOrgDestory);
	 sheet.$beforeEditEnterBehavior = sheet.GetEditEnterBehavior();
	 sheet.SetEditEnterBehavior("none");
}

//autocomplete 키보드 이벤트
function autoCompleteOrgPress(opt, Row, Col, code) {
 if (Col != opt) return;

 //IBsheet에서 입력된 값을 가져와 자동완성에 넘김
 var e = jQuery.Event("keydown");
 e.keyCode = code;
 $("#orgKeyword").trigger(e);
 alert($("#_editInput0").val());
 alert($(".GMEditInput").val());
 //IBsheet input tag의 속성 - id:_editInput0 class:GMEditInput
 if( $("#_editInput0").length != 0 ) {
 	$("#orgKeyword").val($("#_editInput0").val());
 } else {
 	//id:_editInput0 가 없는 경우도 있다. 그럴 경우 class:GMEditInput 로 검색
 	$("#orgKeyword").val($(".GMEditInput").val());
 }
 alert($("#orgKeyword").val());
}

//autocomplete 제거
function autoCompleteOrgDestroy(sheet) {
	clearTimeout(intervalOrgDestory);
	
 intervalOrgDestory = setTimeout(function() {
     destroyAutoCompleteOrg(sheet);
 }, 200);
}

//autocomplete 제거
function destroyAutoCompleteOrg(sheet) {
 $(".GMVScroll>div").unbind("scroll");
 $(".GMHScrollMid>div").unbind("scroll");

 $("#autoCompleteOrgInput").autocomplete("destroy");
 $("#autoCompleteOrgDiv").remove();

 //sheet.SetEditEnterBehavior("tab");
 sheet.SetEditEnterBehavior(sheet.$beforeEditEnterBehavior);
}

//autocomplete 리스트 포맷
function orgRenderItem1(ul, item) {
 return $("<li />")
     .data("item.autocomplete", item)
     .append("<a class='autocomplete' style='width:240px;'>" +
         "<span style='width:40px;'>" + String(item.orgNm).split(item.searchNm).join('<b>' + item.searchNm + '</b>') + "</span>" +
         "<span style='width:90px;'>" + item.orgCd + "</span>" +
         "</a>").appendTo(ul);
}