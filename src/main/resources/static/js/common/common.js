//弹出窗口
function openDialogView(title, url, width, height){
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端，就使用自适应大小弹窗
        width = 'auto';
        height = 'auto';
    } else {//如果是PC端，根据用户设置的width和height显示。

    }
    //特点
    let features="height="+height+",width="+width;
    let replace=true;
    /**打开新窗口*/
    let winObj =  top.open(url,title,features,replace);
    let loop = setInterval(function() {
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
        let layer=layui.layer;
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
    let index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}


/**
 * 右下角成功消息提示
 * @msg    提示内容
 */
function NotifySuccess(msg, title, timeout) {
    if (msg == null || msg == undefined || msg == '') {
        return;
    }
    NotifyShow(msg, title, "success", timeout);
}

/**
 * 右下角失败消息提示
 * @msg    提示内容
 */
function NotifyError(msg, title, timeout) {
    if (msg == null || msg == undefined || msg == '') {
        return;
    }
    NotifyShow(msg, title, "error", timeout);
}


/**
 * 右下角失败消息提示
 * @msg    提示内容
 */
function NotifyWarning(msg, title, timeout) {
    if (msg == null || msg == undefined || msg == '') {
        return;
    }
    NotifyShow(msg, title, "warning", timeout);
}

function NotifyShow(msg, title, type, timeout) {
    toastr.options = {
        closeButton: true,
        debug: false,
        positionClass: "toast-bottom-right",
        onclick: function () {
            toastr.clear();
        },
        autoDismiss: true,
        showDuration: "300",
        hideDuration: "300",
        timeOut: timeout || "2000",
        extendedTimeOut: "1000",
        showEasing: "swing",
        hideEasing: "linear",
        showMethod: "fadeIn",
        hideMethod: "fadeOut",
        maxOpened: 1
    };
    if (type === "warning") {
        toastr.warning(msg ? msg : "", title ? title : "警告");
    }
    if (type === "error") {
        toastr.error(msg ? msg : "", title ? title : "错误");
    }
    if (type === "success") {
        toastr.success(msg ? msg : "", title ? title : "提示");
    }
}