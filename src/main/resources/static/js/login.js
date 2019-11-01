//初始化加载
$(document).ready(function () {
    //引入消息提醒框，置于底部
    toastr.options.positionClass = 'toast-bottom-right';
    //监听登录按钮
    $("#btn_login").click=login;
    //校验规则
    validate();
    $("body").keydown(keyDownLogon);

    $("#imgVerify").click();
});

$.validator.setDefaults({
    submitHandler: function () {
        login();
    }
});
//登录
function login() {
    var loginName=$("#loginName").val();
    var password=$("#password").val();
    var key = CryptoJS.enc.Utf8.parse(loginName);
    var newPassword = CryptoJS.enc.Utf8.parse(password);
    var encrypted = CryptoJS.MD5(newPassword);
    var verifyCode1=$("#verifyCode").val();
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
                toastr.success(result.message);
                parent.location.href=ctx+"/index";
            }else{
                toastr.warning(result.message);
                parent.location.href=ctx+"/login";
            }
        },
    });
}

function keyDownLogon() {
    if (event.keyCode == "13") {
        $("#btn_login").trigger('click');
    }
}
//jquery验证方式
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

/**bootstrap验证方式*/
function validate(){
    $('#userForm').bootstrapValidator({
        live: 'enabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
        excluded: [':disabled', ':hidden', ':not(:visible)'],//排除无需验证的控件，比如被禁用的或者被隐藏的
        // submitButtons: '#btn_save',//指定提交按钮，如果验证失败则变成disabled
        message:'This value is not valid',
        feedbackIcons:{
            required: 'glyphicon glyphicon-asterisk requiredStar',
            valid:'glyphicon glyphicon-ok',
            invalid:'glyphicon glyphicon-remove',
            validating:'glyphicon glyphicon-refresh'
        },
        fields:{
            loginName:{
                validators:{
                    notEmpty:{
                        message:'请输入您的用户名'
                    }
                }
            },
            password:{
                validators:{
                    notEmpty:{
                        message:'请输入您的密码'
                    }
                }
            },
            verifyCode:{
                validators:{
                    notEmpty:{
                        message:'请输入验证码'
                    }
                }
            }
        }
    });
}
//验证码
function getVerifyCode(obj){
    obj.src="/getVerifyCode?d="+new Date()*1;
}