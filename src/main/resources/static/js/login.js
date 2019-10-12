//初始化加载
$(document).ready(function () {
    //监听登录按钮
    $("#btn_login").on("click",function(){
        $("#loginForm").submit();
    })
    //校验规则
    validateRule();

    $("body").keydown(keyDownLogon);

    $("#imgVerify").click();
})

$.validator.setDefaults({
    submitHandler: function () {
        login();
    }
});
//登录
function login() {
    var key = CryptoJS.enc.Utf8.parse(loginName);
    var newPassword = CryptoJS.enc.Utf8.parse(password);
    var encrypted = CryptoJS.MD5(newPassword);
    // .encrypt(newPassword, key, {mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
    var encryptedPwd = encrypted.toString();
    var data={
        loginName:loginName,
        password:encryptedPwd,
        verifyCode:verifyCode1
    }
    $.ajax({
        type: "POST",
        url: ctx + "login",
        data: data,
        success: function (result) {
            console.log(result)
            //请求成功时
            if(result.success){
                swal({
                    title : result.msg,
                    type : "success",
                    closeOnConfirm : true
                });
                parent.location.href=result.url;
            }else{
                swal({
                    title : result.msg,
                    type : "warning",
                    closeOnConfirm : false
                });
            }
        },
    });
}

function keyDownLogon() {
    if (event.keyCode == "13") {
        $("#login").trigger('click');
    }
}
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#loginForm").validate({
        rules: {
            loginName: {
                required: true
            },
            password: {
                required: true
            },
            verifyCode: {
                required: true
            }
        },
        messages: {
            loginName: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            },
            verifyCode: {
                required: icon + "请输入验证码",
            }
        }
    })
}

//验证码
function getVerifyCode(obj){
    obj.src="/getVerifyCode?d="+new Date()*1;
}