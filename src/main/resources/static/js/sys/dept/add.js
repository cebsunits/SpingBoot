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
//监听保存你按钮信息
function save(){
    let $form=$("#deptForm");
    if($form.data("bootstrapValidator").isValid()){
        /**ajax提交表单***/
        $.post($form.attr('action'),$form.serialize(),function(result){
            if (result.success) {
                parent.reLoad(result.message);
                windowClose();
            } else {
                NotifyWarning(result.message);
            }
        });
    }
}
/**验证*/
function validate(){
    $('#deptForm').bootstrapValidator({
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
            deptName:{
                validators:{
                    notEmpty:{
                        message:'部门名称不能为空'
                    },
                    stringLength:{
                        min:1,
                        max:16,
                        message:"字符长度必须在1位到255位之间"
                    }
                }
            },
            deptCode:{
                validators:{
                    notEmpty:{
                        message:'部门编码不能为空'
                    },
                    stringLength:{
                        min:1,
                        max:50,
                        message:"字符长度必须在1位到50位之间"
                    },
                    remote:{
                        message:"部门编码已存在",
                        url:"/sys/dept/checkExists",
                        data:function (validator) {
                            return {
                                deptCode:$("#deptCode").val(),
                                oldDeptCode:$("#oldDeptCode").val(),
                            }
                        },//默认传递该字段的值到后台
                        delay:500
                    }
                }
            }

        }
    });
}
