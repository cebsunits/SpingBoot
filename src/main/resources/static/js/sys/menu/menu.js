$(function () {
   //验证
    validateRule();
    /**图标*/
    $("#ico-btn").click(function () {
        // openDialog("图标管理","/tagIcon.html","480px","80%");
        //彈出框
        layui.use('layer',function () {
            var layer=layui.layer;
            layer.open({
                type:2,
                title:"图标管理",
                content:"/tagIcon/iconSelect?value="+$("#menuIcon").val(),
                area:["480px","80%"],
                maxmin:true,
                shadeClose:false
            });
        });
    });
});
$.validator.setDefaults({
    submitHandler: function () {
        menuSave();
    }
});
/**获取子页面data数据*/
function getChildrenData(data) {
    console.log(data);
    if(data!=undefined){
        $("#menuIcon").addClass(data.value);
        $("#menuIcon").val(data.text);
    }
}
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
                parent.location.reload();
                /**关闭窗口*/
                windowClose();
            }else{
                toastr.warning(result.message);
            }
        },
    });
}
//jquery验证方式
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#menuForm").validate({
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