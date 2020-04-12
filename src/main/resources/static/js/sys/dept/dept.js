var $deptTable=$("#deptTable");
// 第三步初始化Datatables
$(document).ready(function () {
    load();
});
function load() {
    $("#deptTable").bootstrapTreeTable({
        id: 'deptId',
        code: 'deptId',
        parentCode: 'parentId',
        type: "POST", // 请求数据的ajax类型
        url: '/sys/dept/list', // 请求数据的ajax的url
        ajaxParams: {sort:'deptSort',deptName:$("#deptName").val()}, // 请求数据的ajax的data属性
        expandColumn: '1',// 在哪一列上面显示展开按钮
        striped: true, // 是否各行渐变色
        bordered: true, // 是否显示边框
        expandAll: false, // 是否全部展开
        //列表表头字段
        columns: [
            {
                title: '编号',
                field: 'deptId',
                visible: false,
                align: 'center',
                valign: 'center',
                width: '10%'
            },
            {
                title: '部门名称',
                valign: 'center',
                field: 'deptName',
                width: '20%'
            },

            {
                title: '部门编码',
                field: 'deptCode',
                align: 'center',
                valign: 'center'
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                valign: 'center',
                formatter: function (item, index) {
                    var e = '<a class="btn btn-primary btn-sm '
                        + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                        + item.deptId
                        + '\')"><i class="fa fa-edit"></i></a> ';
                    var p = '<a class="btn btn-primary btn-sm '
                        + '" href="#" mce_href="#" title="添加下级" onclick="toAdd(\''
                        + item.deptId
                        + '\')"><i class="fa fa-plus"></i></a> ';
                    var d = '<a class="btn btn-warning btn-sm '
                        + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                        + item.deptId
                        + '\')"><i class="fa fa-remove"></i></a> ';

                    let display="";
                    if(s_edit_h!=undefined&&s_edit_h){
                        display=display+e;
                    }
                    if(s_remove_h!=undefined&&s_remove_h){
                        display=display+d;
                    }
                    if(s_add_h!=undefined&&s_add_h){
                        display=display+p;
                    }
                    return display;
                }
            }]
    });
}

function reLoad(message) {
    if(message!=null&&message!=undefined){
        NotifySuccess(message);
    }
    load();
}

//编辑
function edit(menuId){
    var url="/sys/dept/add?deptId="+menuId;
    openDialog("编辑菜单",url,'800px', '600px');
}
//删除
function remove(deptId){
    //ajax请求数据方法
    if(confirm("确定删除选中的记录？")){
        $.ajax({
            type: "POST",
            url: "/sys/dept/delete",//url请求的地址
            cache: false,  //禁用缓存
            data: {
                deptId:deptId
            },  //传入组装的参数
            dataType: "json",
            success: function (result) {
                if(result.success){
                    NotifySuccess(result.message);
                    reLoad();
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                NotifyWarning(result.message);
                alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);
            }
        });

    }
}

//批量删除
function batchRemove(){
    /**隐藏模态框*/
    let selectedLine = $deptTable.bootstrapTreeTable('getSelections');
    if (selectedLine.length < 1) {
        NotifyWarning("请选中一行数据");
    }else {
        let idList=new Array();
        for (let i = 0; i < selectedLine.length; i++) {
            idList.push(selectedLine[i].menuId);
        }
        let deleteUrl = "/sys/dept/batchDelete";
        $.ajax({
            type:"post",
            url:deleteUrl,
            data:{deptIds:idList},
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
//新增
function toAdd(parentId){
    if(parentId=="0"){
        parentId="";
    }
    let url="/sys/dept/add?parentId="+parentId;
    openDialog("新增部门",url,"800px","600px");
}