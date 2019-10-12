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