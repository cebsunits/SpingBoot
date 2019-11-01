$(function(){
    //引入消息提醒框，置于底部
    toastr.options.positionClass = 'toast-bottom-right';
    //日期格式选择
    $("#expiredDate").datepicker({
        language: 'zh-CN', //语言
        autoclose: true, //选择后自动关闭
        clearBtn: true,//清除按钮
        format: "yyyy-mm-dd"//日期格式
    });
    //如果用户ID不为空，则不允许更改用户名
    validateLoginName();
    //表单验证
    validate();
    //ajaxsubmit
    var options = {
        type: 'POST',
        success:showResponse,
        dataType: 'json',
        error : function(xhr, status, err) {
            toastr.warning('操作失败！');
        }
    };
    //监听保存你按钮信息
    $("#btn_save").click=saveUser(options);
})
//响应
function showResponse(responseText, statusText, xhr, $form) {
    if (responseText.success == true) {
        window.top.close();
    } else {
        toastr.warning('保存失败！');
    }
}
//监听保存你按钮信息
function saveUser(options){
    // var Password = $("#password").val();
    // var md5Password=$.MD5(Password);
    // //更改密码为加密后密码
    // options.data.password=md5Password;
    //防止表单自动提交
    $("#userForm").submit(function(ev){
        ev.preventDefault();
    });
    $("#btn_save").on("click",function(){
        $("#userForm").bootstrapValidator('validate');//提交验证
        var bootstrapValidator=$("#userForm").data('bootstrapValidator');
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()){
            $("#userForm").on("submit",function(){
                $(this).ajaxSubmit(options);
                //防止表单自动提交
                return false;
            });
        }else {
            return;
        }
    });
}

/**返回*/
function goBack(){
    window.top.close();
}
/**验证用户名是否可更改*/
function validateLoginName(){
    var userId=$("#userId").val();
    if(userId!=undefined && userId!=""){
        $("#userName").attr("readOnly",true);
    }
}
//保存方法
function save(){
    //ajax请求数据方法
    $.ajax({
        type: "POST",
        url: "/user/save",//url请求的地址
        cache: false,  //禁用缓存
        data: $("#userName").serialize(),  //传入组装的参数
        dataType: "json",
        success: function (result) {
            if(result.success){
                toastr.success('保存成功！');
                window.parent.opener.location.reload();
                window.top.close();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            toastr.warning('保存失败！');
            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);
        }
    });
}
/**验证*/
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
            userName:{
                validators:{
                    notEmpty:{
                        message:'用户名不能为空'
                    },
                    stringLength:{
                        min:1,
                        max:255,
                        message:"字符长度必须在1位到255位之间"
                    }
                }
            },
            loginName:{
                validators:{
                    notEmpty:{
                        message:'登录名不能为空'
                    },
                    remote:{
                        message:"登录名重复",
                        url:"/user/checkUserExists",
                        data:'',//默认传递该字段的值到后台
                        delay:2000
                    },
                    stringLength:{
                        min:1,
                        max:50,
                        message:"字符长度必须在1位到50位之间"
                    }
                }
            },
            password:{
                validators:{
                    notEmpty:{
                        message:'密码不能为空'
                    },
                    stringLength:{
                        min:1,
                        max:255,
                        message:"字符长度必须在1位到255位之间"
                    },
                    identical:{
                        field:"passwordConfirm",
                        message: '两次密码必须一致'
                    }
                }
            },
            passwordConfirm:{
                validators:{
                    notEmpty:{
                        message:'密码不能为空'
                    },
                    identical:{
                        field:"password",
                        message: '两次密码必须一致'
                    }
                }
            }
        }
    });
}

/**查询部门信息*/
function queryDept(){
    openDialogView("部门信息","/dept/deptTreeView",'800px', '600px');
}

function queryRole(){

}