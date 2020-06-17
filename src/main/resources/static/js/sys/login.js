//初始化加载
$(document).ready(function () {
    //监听登录按钮
    $("#btn_login").on('click', function () {
        $("#loginForm").submit();
    });
    //校验规则
    validateRule();
    //监听回车按钮
    $("body").keydown(function (event) {
        keyDownLogon(event);
    });
    //更改验证码
    $("#imgVerify").click();
});

$.validator.setDefaults({
    submitHandler: function (form) {
        login();
    }
});
//登录
function login() {
    console.log("login------")
    let loginName=$("#loginName").val();
    let password=$("#password").val();
    if(loginName==""||loginName=="undefined"){
        NotifyWarning("用户名为空");
        return;
    }
    if(password==""||password=="undefined"){
        NotifyWarning("密码为空");
        return;
    }
    let md5Password = CryptoJS.MD5(password+loginName).toString();
    /**更改密码*/
    $("#password").val(md5Password);
    var data=$('#loginForm').serialize();
    $.ajax({
        type: "POST",
        url: "/login",
        data: data,
        success: function (result) {
            console.log(result);
            //请求成功时
            if(result.success){
                NotifySuccess(result.message);
                parent.location.href="/index";
            }else{
                //更改验证码
                $("#imgVerify").click();
                $("#verifyCode").val("");
                $("#password").val("");
                //定位到密码位置
                $("#password").focus();
                NotifyWarning(result.message);
            }
        },
    });
}

function keyDownLogon(event) {
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
//验证码
function getVerifyCode(obj){
    obj.src="/getVerifyCode?d="+new Date()*1;
}