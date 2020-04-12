// 第三步初始化Datatables
$(document).ready(function () {
    load();
});
function load() {
    $("#table_menu").bootstrapTreeTable({
        id: 'menuId',
        code: 'menuId',
        parentCode: 'parentId',
        type: "POST", // 请求数据的ajax类型
        url: '/sys/menu/list', // 请求数据的ajax的url
        ajaxParams: {sort:'menuSort'}, // 请求数据的ajax的data属性
        expandColumn: '1',// 在哪一列上面显示展开按钮
        striped: true, // 是否各行渐变色
        bordered: true, // 是否显示边框
        expandAll: false, // 是否全部展开
        //列表表头字段
        columns: [
            {
                title: '编号',
                field: 'menuId',
                visible: false,
                align: 'center',
                valign: 'center',
                width: '10%'
            },
            {
                title: '名称',
                valign: 'center',
                field: 'menuName',
                width: '20%'
            },

            {
                title: '图标',
                field: 'menuIcon',
                align: 'center',
                valign: 'center',
                width : '5%',
                formatter: function (row,item, index) {
                    let menuIcon=row.menuIcon;
                    if(menuIcon!=null&&menuIcon!=undefined){
                        return '<i class="'+row.menuIcon
                            + ' fa-lg"></i>';
                    }
                    return "";
                }
            },
            {
                title: '类型',
                field: 'menuType',
                align: 'center',
                valign: 'center',
                width : '10%',
                formatter: function (item, index) {
                    if (item.menuType === "0") {
                        return '<span class="label label-primary">目录</span>';
                    }
                    if (item.menuType === "1") {
                        return '<span class="label label-success">菜单</span>';
                    }
                    if (item.menuType === "2") {
                        return '<span class="label label-warning">按钮</span>';
                    }
                }
            },
            {
                title: '链接地址',
                valign: 'center',
                width : '20%',
                field: 'menuUrl'
            },
            {
                title: '权限标识',
                valign: 'center',
                width : '20%',
                field: 'permission'
            },
            {
                title: '操作',
                field: 'id',
                align: 'center',
                valign: 'center',
                formatter: function (item, index) {
                    var e = '<a class="btn btn-primary btn-sm '
                        + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
                        + item.menuId
                        + '\')"><i class="fa fa-edit"></i></a> ';
                    var p = '<a class="btn btn-primary btn-sm '
                        + '" href="#" mce_href="#" title="添加下级" onclick="toAdd(\''
                        + item.menuId
                        + '\')" ><i class="fa fa-plus"></i></a> ';
                    var d = '<a class="btn btn-warning btn-sm '
                        + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
                        + item.menuId
                        + '\')"><i class="fa fa-remove"></i></a> ';

                    let display="";
                    if(s_edit_h!=undefined&&s_edit_h){
                        display=e;
                    }
                    if(s_remove_h!=undefined&&s_remove_h){
                        display=display+d;
                    }
                    if(s_add_h!=undefined&&s_add_h){
                        display=display+p;
                    }
                    // return d+e+p;
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
    var url="/sys/menu/form?menuId="+menuId;
    openDialog("编辑菜单",url,'800px', '600px');
}
//删除
function remove(menuId){
    //ajax请求数据方法
    if(confirm("确定删除选中的记录？")){
        $.ajax({
            type: "POST",
            url: "/sys/menu/delete",//url请求的地址
            cache: false,  //禁用缓存
            data: {
                menuId:menuId
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
    $("#deleteModal").modal('hide');
    let selectedLine = $roleTable.bootstrapTreeTable('getSelections');
    if (selectedLine.length < 1) {
        NotifyWarning("请选中一行数据");
    }else {
        let idList=new Array();
        for (let i = 0; i < selectedLine.length; i++) {
            idList.push(selectedLine[i].menuId);
        }
        let deleteUrl = "/sys/menu/batchDelete";
        $.ajax({
            type:"post",
            url:deleteUrl,
            data:{roleIdList:idList},
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
    openDialog("新增菜单","/sys/menu/form?parentId="+parentId,'800px', '600px');
}