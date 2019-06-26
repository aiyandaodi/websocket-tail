/**
 * 取得页面传递参数。（一般情况下，直接使用request参数即可）
 * 
 * @returns {Object} 参数的键值对对象
 */
getRequest = function() {
	var url = location.search; // 获取url中"?"符后的字串

	var theRequest = {};
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		var strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
		}
	}

	return theRequest;
};

function getDockerLogs(){
	var request = getRequest();
	var docker = request["docker"];
	//alert(docker);
	if(!docker){
        alert("请选择要查看日志的容器");
        return;
    }
	var lines = request["lines"];
	//alert(lines);
	if(!lines){
        alert("请选择要查看日志行数");
        return;
    }
	// 指定websocket路径
	var host = window.location.host
	var websocket = new WebSocket('ws://'+ host +'/log' + '/' + docker + '/' + lines);
	websocket.onmessage = function(event) {
		// 接收服务端的实时日志并添加到HTML页面中
		$("#log-container div").append(event.data);
		// 滚动条滚动到最低部
		$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
	};
}