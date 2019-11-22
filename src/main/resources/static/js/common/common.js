//引入消息提醒框，置于底部
$(function() {
    //引入消息提醒框，置于底部
    toastr.options.positionClass = 'toast-bottom-right';
});
//弹出窗口
function openDialogView(title, url, width, height){
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端，就使用自适应大小弹窗
        width = 'auto';
        height = 'auto';
    } else {//如果是PC端，根据用户设置的width和height显示。

    }
    //特点
    var features="height="+height+",width="+width;
    var replace=true;
    /**打开新窗口*/
    var winObj =  top.open(url,title,features,replace);
    var loop = setInterval(function() {
        if(winObj.closed) {
            clearInterval(loop);
            //alert('closed');
            parent.location.reload();
        }
    }, 1);
}

/**打开窗口*/
function openDialog(title, url, width, height){
    /**如果是移动端则进行自适应匹配显示*/
    if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){
        width='auto';
        height='auto';
    }
    //彈出框
    layui.use('layer',function () {
        var layer=layui.layer;
        layer.open({
            type:2,
            title:title,
            content:url,
            area:[width,height],
            maxmin:true,
            shadeClose:false
        });
    });
}

/**关闭窗口*/
function windowClose(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}