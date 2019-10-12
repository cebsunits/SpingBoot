$(function(){
    //监听保存你按钮信息
    $("#btn_save").click=save;
    //监听返回按钮信息
    $("#btn_back").click=returnBack;



})
//监听保存你按钮信息
function save(){
    $("#deptForm").submit(function(ev){
        ev.preventDefault();
    });
    $("#btn_save").on("click",function(){
       var bootstrapValidator=$("#deptForm").data('bootstrapValidator');
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()){
            $("#deptForm").submit();
        }else {
            return;
        }
    });
}

/**返回*/
function returnBack(){
    var url="/dept/deptList";
    $('#container').load(url);
}
/**验证*/
function validate(){
    $('#deptForm').bootstrapValidator({
        message:'This value is not valid',
        feedbackIcons:{
            valid:'glyphicon glyphicon-ok',
            invalid:'glyphicon glyphicon-remove',
            validating:'glyphicon glyphicon-refresh'
        },
        fields:{
            deptName:{
                validators:{
                    notEmpty:{
                        message:'部门名称不能为空'
                    }
                }
            }
        }
    })
}

/**谈窗框显示*/
function display(){
    $("[data-toggle='popover']").popover();
}
