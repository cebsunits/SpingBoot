let prefix = "/common/quartzJob"
let exampleTable=$("#exampleTable");
$(function() {
	//初始化bootstrap table
	load();
});

function load() {
	exampleTable.bootstrapTable({
		url : prefix + "/list", // 服务器数据的加载地址
		cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		method: 'post',
		contentType : "application/x-www-form-urlencoded;charset=UTF-8", //post方式必须设置,get方式设置application/json
		dataType: 'json',
		striped: true,                      //是否显示行间隔色
		toolbar: '#toolbar',                //工具按钮用哪个容器
		sidePagination: 'server',           //分页方式：client客户端分页，server服务端分页（*）
		pagination: true,                   //是否显示分页（*）
		pageNumber: 1,                      //初始化加载第一页，默认第一页
		pageSize: 10,                        //每页的记录行数（*）
		pageList: [10, 25, 50],          //可供选择的每页的行数（*）
//            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
		showColumns: true,                  //是否显示所有的列（选择显示的列）
		minimumCountColumns: 2,             //最少允许的列数
		showRefresh: true,                  //是否显示刷新按钮
		clickToSelect: true,                //是否启用点击选中行
//            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
		sortable: true,                     //是否启用排序
		sortOrder: "desc",                   //排序方式
		sortName: 'createDate',             //排序字段
		cardView: false,                    //是否显示详细视图
		detailView: false,                  //是否显示父子表
		idField: 'jobId',                  //id
		showExport: true,                     //是否显示导出
		exportDataType: "all",              //basic', 'all', 'selected'.
		// exportTypes: ['txt','csv', 'excel','xlsx'],
		exportOptions:{
			ignoreColumn: [0,6],            //忽略某一列的索引
			fileName: '数据导出',              //文件名称设置
			worksheetName: 'Sheet1',          //表格工作区名称
			tableName: '计划任务列表',
			excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
			//onMsoNumberFormat: DoOnMsoNumberFormat
		},
		buttonsAlign:"right",  //按钮位置
		queryParams : function(params) {
			//说明：传入后台的参数包括pageNumber开始索引，pageSize步长，sort排序列，order：desc或者,以及所有列的键值对
			var temp = {
				pageSize : params.pageSize, // 每页显示数量
				pageNumber :params.pageNumber,//开始索引
				sortName: params.sortName,      //排序列名
				sortOrder: params.sortOrder,  //排位命令（desc，asc）
				searchName: $('#searchName').val()
			};
			return temp;
		},
		// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
		// queryParamsType = 'limit' ,返回参数必须包含
		// limit, offset, search, sort, order 否则, 需要包含:
		// pageSize, pageNumber, searchText, sortName,
		// sortOrder.
		// 返回false将会终止请求
		columns : [
				{
					checkbox : true
				},
			{
					field : 'jobId',
					title : '任务ID'
				},
			{
					field : 'jobName',
					title : '任务名称'
				},
			{
					field : 'jobGroup',
					title : '任务分组'
				},
			{
					field : 'jobStatus',
					title : '任务状态 是否启动任务'
				},
			{
					field : 'cronType',
					title : '执行类型，单个还是定时'
				},
			{
					field : 'startTime',
					title : '执行开始时间'
				},
			{
					field : 'cronExpression',
					title : 'cron表达式'
				},
			{
					field : 'description',
					title : '描述'
				},
			{
					field : 'beanClass',
					title : '任务执行时调用哪个类的方法'
				},
			{
					field : 'isConcurrent',
					title : '任务是否有状态'
				},
			{
					field : 'createId',
					title : '创建人'
				},
			{
					field : 'createDate',
					title : '创建时间'
				},
			{
					field : 'updateId',
					title : '更改人'
				},
			{
					field : 'updateDate',
					title : '更改时间'
				},
				{
					title : '操作',
					field : 'id',
					align : 'center',
					formatter : function(value, row, index) {
						var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ row.jobId
								+ '\')"><i class="fa fa-edit"></i></a> ';
						var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
								+ row.jobId
								+ '\')"><i class="fa fa-remove"></i></a> ';
						return e + d ;
					}
				} ],
		//当后台返回不是标准的json对象时，需要转换属性
		responseHandler:function(result){
			console.log(result);
			return {
				"total":result.total,
				"rows":result.list
			};
		}
	});
}
/**查询**/
function refresh() {
	var opt = {
		query : {
			searchText: $('#searchName').val()
		}
	};
	exampleTable.bootstrapTable('refresh',opt);
}
//新增
function add() {
	let url=prefix + '/add'
	openDialog("新增",url,'800px', '600px');
}
//编辑
function edit(id) {
	let url = prefix + '/edit/'+id;
	openDialog("编辑",url,"800px","600px");
}
//删除
function remove(id) {
	var deleteUrl = prefix+"/remove";
	if(confirm("确认删除？")){
		$.ajax({
			type:"post",
			url:deleteUrl,
			data:{'jobId':id},
			dataType:"json",
			success:function (result) {
				NotifySuccess(result.message);
				refresh();
			},
			error:function (result) {
				NotifyWarning(result.message);
			}
		});
	}
}
//批量删除
function batchRemove() {
	var rows = exampleTable.bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		NotifyWarning("请选择要删除的数据");
		return;
	}
	if(confirm("确认删除？")){
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['jobId'];
		});
		//删除
		let url= prefix + '/batchRemove';
		$.ajax({
			type:"post",
			url:url,
			data:{jobIds:ids},
			dataType:"json",
			success:function (result) {
				NotifySuccess(result.message);
				refresh();
			},
			error:function (result) {
				NotifyWarning(result.message);
			}
		});
	}
}