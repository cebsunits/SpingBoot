/**角色初始化方法*/
$(function(){
    //返回
    $('a[name="backToRoleList"]').click=goBack;
    //保存
    saveRole();
    //校验
    validate();

    //bootstrapValidator导致表单ajax提交出现两次提交Bug解决
    $("form.validation-form").each(function() {
        var $form = $(this);
        $form.bootstrapValidator().on('success.form.bv', function(e) {
            // 阻止默认事件提交
            e.preventDefault();
        });
    });

});

//监听保存你按钮信息
function saveRole(){
    //防止表单自动提交
    $("#formAdd").ajaxForm(function (data) {
        if(data.success){
            $("#container").load(data);
        }else{
            toastr.warning("数据提交失败！");
        }
    });
}

/**返回*/
function goBack(event){
    event.preventDefault();
    var url=$(this).attr('href');
    $("#container").load(url);
}
/**验证*/
function validate(){
    $('#formAdd').bootstrapValidator({
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
            role:{
                validators:{
                    notEmpty:{
                        message:'角色英文名不能为空'
                    },
                    stringLength:{
                        min:1,
                        max:16,
                        message:"字符长度必须在1位到16位之间"
                    },
                    remote:{
                        message:"角色已存在",
                        url:"/role/checkRoleExists",
                        data:'',//默认传递该字段的值到后台
                        delay:2000
                    }
                }
            },
            roleName:{
                validators:{
                    notEmpty:{
                        message:'角色中文名不能为空'
                    },

                    stringLength:{
                        min:1,
                        max:50,
                        message:"字符长度必须在1位到50位之间"
                    }
                }
            }

        }
    });
}
