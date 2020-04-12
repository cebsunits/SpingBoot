$(function(){
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
    /***重复提交表单问题*/
    $("form.validation-form").each(function() {
        let $form = $(this);
        $form.bootstrapValidator();
        $form.on('success.form.bv', function(e) {
            // 阻止默认事件提交
            e.preventDefault();
            //保存
            saveUser();
        });
    });
})

//监听保存你按钮信息
function saveUser(){
    //角色id不为空时，不验证role字段
    let userId=$("#userId").val();
    let loginName=$("#loginName").val();
    let password=$("#password").val();
    let md5Password = CryptoJS.MD5(password+loginName).toString();
    /**更改密码*/
    $("#password").val(md5Password);
    let $form=$("#userForm");
    if(userId!=""){
        $form.bootstrapValidator('removeField','userName');
    }
    if($form.data("bootstrapValidator").isValid()){
        /**ajax提交表单***/
        $.post($form.attr('action'),$form.serialize(),function(result){
            if (result.success) {
                NotifySuccess(result.message);
                window.parent.location.reload();
                windowClose();
            } else {
                NotifyWarning(result.message);
            }
        });
    }
}

/**验证用户名是否可更改*/
function validateLoginName(){
    var userId=$("#userId").val();
    if(userId!=undefined && userId!=""){
        $("#userName").attr("readOnly",true);
    }
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
                        url:"/sys/user/checkUserExists",
                        data:{
                            oldLoginName:$("#oldLoginName").val(),
                            loginName:$("#loginName").val()
                        },//默认传递该字段的值到后台
                        delay:500
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
    openDialog("部门信息","/sys/dept/deptTreeView",'600px', '400px');
}