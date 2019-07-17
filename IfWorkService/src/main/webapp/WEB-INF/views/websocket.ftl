<script src="${rc.getContextPath()}/sockjs/sockjs.min.js"></script>
<script src="${rc.getContextPath()}/sockjs/stomp.js"></script>
<script type="text/javascript">
	var stompClient = {};
	
	function setConnected(connected) {
	    $("#connect").prop("disabled", connected);
	    $("#disconnect").prop("disabled", !connected);
	    if (connected) {
	        $("#conversation").show();
	    }
	    else {
	        $("#conversation").hide();
	    }
	} 
	
	function connect(key, sendToUrl, callback) {
	    var socket = new SockJS('${rc.getContextPath()}/we');
	    stompClient[key] = Stomp.over(socket);
	    stompClient[key].connect({}, function (frame) {
	        setConnected(true);
	        console.log('Connected: ' + frame);
	        stompClient[key].subscribe(sendToUrl, function (paramMap) {
	            console.log(":::::::::::::::::::::::::");
	            console.log("paramMap " + paramMap);
	            callback(paramMap);
	        });
	    });
	}
	
	function disconnect(key) {
	    if (stompClient !== null && stompClient[key] !== null) {
	        stompClient[key].disconnect();
	    }
	    //setConnected(false);
	    console.log(key + " Disconnected");
	}
	 
	function connect(sendToUrl, callback) {
	    var socket = new SockJS('${rc.getContextPath()}/we');
	    stompClient = Stomp.over(socket);
	    stompClient.connect({}, function (frame) {
	        setConnected(true);
	        stompClient.subscribe(sendToUrl, function (paramMap) {
	            console.log(":::::::::::::::::::::::::");
	            console.log("paramMap " + paramMap);
	            callback(paramMap);
	        });
	    });
	}
	
	function disconnect() {
	    if (stompClient !== null) {
	        stompClient.disconnect();
	    }
	    setConnected(false);
	    console.log("Disconnected");
	}
	 
</script>