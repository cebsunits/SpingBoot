/**角色初始化方法*/
$(function(){
    //校验
    validate();
    /***重复提交表单问题*/
    $("form.validation-form").each(function() {
        let $form = $(this);
        $form.bootstrapValidator();
        $form.on('success.form.bv', function(e) {
         // 阻止默认事件提交
         e.preventDefault();
         //保存
         save();
        });
    });
});
function save(){
    //角色id不为空时，不验证role字段
    let roleId=$("#roleId").val();
    let $form=$("#formAdd");
    if(roleId!=""){
        $form.bootstrapValidator('removeField','role');
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
                        url:"/sys/role/checkRoleExists",
                        data:function (validator) {
                            return {
                                role:$("#role").val(),
                                oldRole:$("#oldRole").val()
                            }
                        },//默认传递该字段的值到后台
                        delay:500
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
