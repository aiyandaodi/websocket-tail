function getPara(){
           var docker = $("#docker").val();
           if(docker == "0"){
               alert("请选择要查看日志的容器");
               return;
           }
           var lines = $("#lines").val();
           if(lines == "0"){
               alert("请选择要查看日志行数");
               return;
           }
           window.location.href = "show.html" + "?docker=" + docker + "&lines=" + lines;
}