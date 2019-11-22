$(function () {
   //验证
    validateRule();
    /**图标*/
    $("#ico-btn").click(function () {
        openDialog("图标管理","/tagIcon.html","480px","80%");
    });

    $("#saveBtn").click(function () {
        $("#menuForm").submit();
    });
});
$.validator.setDefaults({
    submitHandler: function () {
        menuSave();
    }
});
//保存
function menuSave() {
    var data=$('#menuForm').serialize();
    $.ajax({
        type: "POST",
        url: "/sys/menu/save",
        data: data,
        success: function (result) {
            console.log(result);
            //请求成功时
            if(result.success){
                parent.toastr.success(result.message);
                parent.reLoad();
                var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                parent.layer.close(index);
            }else{
                toastr.warning(result.message);
            }
        },
    });
}
//jquery验证方式
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#loginForm").validate({
        rules: {
            menuName: {
                required: true
            }
        },
        messages: {
            menuName: {
                required: icon + "请输入菜单名",
            }
        }
    })
}