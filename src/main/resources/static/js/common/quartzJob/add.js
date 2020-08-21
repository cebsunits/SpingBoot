$(function(){

	$("#startTime").datetimepicker({
		format:'yyyy-mm-dd hh:ii:ss'//日期格式化
	});
	/**监听cron规则*/
	cron();
	/**校验规则*/
	validateRule();
	/***重复提交表单问题*/
	$("form.validation-form").each(function() {
		let form = $(this);
		form.bootstrapValidator();
		form.on('success.form.bv', function(e) {
			// 阻止默认事件提交
			e.preventDefault();
			//保存
			let form=$("#signupForm");
			if(form.data("bootstrapValidator").isValid()){
				save();
			}
		});
	});
});
/**检验类型*/
function cron(){
	$("#cronExpression").cronGen({
		direction : 'bottom'
	});
}

//保存
function save() {
	let form=$("#signupForm");
	let url="/common/quartzJob/save";
	/**ajax提交表单***/
	$.post(url,form.serialize(),function(result){
		if (result.success) {
			NotifySuccess(result.message);
			window.parent.location.reload();
			windowClose();
		} else {
			NotifyWarning(result.message);
		}
	});
}
function validateRule() {
	$("#signupForm").bootstrapValidator({
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
				jobName:{
					validators:{
						notEmpty:{
							message:'任务名称不能为空'
						},
						stringLength:{
							min:1,
							max:128,
							message:"字符长度必须在1位到128位之间"
						}
					}
				},
				jobGroup:{
					validators:{
						notEmpty:{
							message:'任务组不能为空'
						},
						stringLength:{
							min:1,
							max:50,
							message:"字符长度必须在1位到50位之间"
						}
					}
				},
				cronType:{
					validators:{
						notEmpty:{
							message:'任务类型不能为空'
						}
					}
				}
		}
	})
}