var $roleTable=$("#roleTable");
$(function () {
    //初始化bootstrap table
    initTable();
    //添加off("click")避免点击一次触发两次情况
    $('#btnQuery').off("click").on("click",function () {
        refresh();
    });
    $('#btn_add').off("click").on("click",function () {
        add();
    });
    $('#btn_delete').off("click").on("click",function () {
        batchRemove();
    });
});


function initTable() {
    $roleTable.bootstrapTable({
        url:"/sys/role/list",
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
        sortName: 'createTime',             //排序字段
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        idField: 'roleId',                  //id
        showExport: true,                     //是否显示导出
        exportDataType: "all",              //basic', 'all', 'selected'.
        // exportTypes: ['txt','csv', 'excel','xlsx'],
        exportOptions:{
            ignoreColumn: [0,6],            //忽略某一列的索引
            fileName: '数据导出',              //文件名称设置
            worksheetName: 'Sheet1',          //表格工作区名称
            tableName: '角色列表',
            excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
            //onMsoNumberFormat: DoOnMsoNumberFormat
        },
        buttonsAlign:"right",  //按钮位置
        columns: [
            {
                field:'selected',
                checkbox: true,
                formatter: function (value, row, index) {
                    if (row.role == 'admin'){
                        return {
                            disabled:true,
                            checked: false//设置选中
                        };
                    }
                    return value;
                }
            }, {
                field:'role',
                title: '角色代码',
                align: 'center',
                sortable: true
            }, {
                field: 'roleName',
                title: '角色名称',
                sortable: true
            }, {
                field: 'description',
                title: '角色描述'
            }, {
                field: 'createDate',
                title: '创建时间',
                sortable: true
            },  {
                field: 'available',
                title: '状态',
                sortable: true,
                formatter:function (value,row,index) {
                    if(row.role=='admin') return "锁定";
                    if(value) return "正常";
                    else return "禁用";
                }
            },  {
                field: 'roleId',
                title: '操作',
                sortable: true,
                formatter:function (value,row,index) {
                    let e = '<a class="btn btn-primary btn-sm" href="#" title="编辑" onclick="edit(\''
                        + row.roleId
                        + '\')"><i class="fa fa-edit"></i>编辑</a> ';

                    let d = '<a class="btn btn-danger btn-sm" href="#" title="删除" onclick="toDelete(\''
                        + row.roleId
                        + '\',\''+row.role+'\')"><i class="fa fa-remove"></i>删除</a> ';

                    let grant = '<a class="btn btn-secondary btn-sm" href="#" title="权限" onclick="authority(\''
                        + row.roleId
                        + '\')"><i class="fa fa-cog"></i>权限</a> ';
                    let display="";

                    if(s_remove_h!=undefined&&s_remove_h){
                        display=display+d;
                    }
                    if(s_edit_h!=undefined&&s_edit_h){
                        display=display+e;
                    }
                    if(s_grant_h!=undefined&&s_grant_h){
                        display=display+grant;
                    }

                    return display;
                }
            }
        ],
        queryParamsType: '',
        queryParams: function (params) {
            console.log("queryParams---"+params)
            //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
            var temp = {
                pageSize : params.pageSize, // 每页显示数量
                pageNumber :params.pageNumber,//开始索引
                sortName: params.sortName,      //排序列名
                sortOrder: params.sortOrder,  //排位命令（desc，asc）
                searchText: $('#search').val()
            };
            return temp;
        },
        responseHandler:function(result){
            console.log(result);
            return {
                "total":result.total,
                "rows":result.list
            };
        },
        rowStyle: function (row, index) {
            //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
            var strclass = "";
            if (row.role == "admin") {
                strclass = 'danger';//
            }
            else {
                return {};
            }
            return { classes: strclass }
        },
        onSort:function(name, order){
            $roleTable.bootstrapTable('refreshOptions', {
                sortName:name,
                sortOrder:order
            });
        },
        onLoadSuccess: function (result) {
            console.log("数据加载成功！"+result);
        },
        onLoadError: function () {
            console.log("数据加载失败！");
        },
        onDblClickRow: function (row, $element) {
        }
    });
}


//刷新
function refresh() {
    var opt = {
        query : {
            searchText: $('#search').val()
        }
    };
    $roleTable.bootstrapTable('refresh',opt);
}
//新增
function add() {
    var url = "/sys/role/roleAdd";
    openDialog("角色新增",url,"800px","600px");
    // $('#container').load(url);
}

//批量删除
function batchRemove(){
    /**隐藏模态框*/
    $("#deleteModal").modal('hide');
    var selectedLine = $roleTable.bootstrapTable('getSelections');
    if (selectedLine.length < 1) {
        toastr.warning("请选中一行数据");
    }else {
        var idList = selectedLine[0].roleId;
        for (var i = 1; i < selectedLine.length; i++) {
            idList += ','+ selectedLine[i].roleId;
        }
        var deleteUrl = "/sys/role/batchDelete";
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

//编辑
function edit(roleId) {
    var url = "/sys/role/roleAdd?roleId="+roleId;
    openDialog("角色编辑",url,"800px","600px");
    // $('#container').load(url);
}
//删除
function toDelete(roleId,roleName){
    if(roleName=="admin"){
        NotifyWarning("不能删除超级管理员角色！");
        return;
    }
    var deleteUrl = "/sys/role/delete";
    if(confirm("确认删除？")){
        $.ajax({
            type:"post",
            url:deleteUrl,
            data:{roleId:roleId},
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
//角色
function authority(roleId) {
    var url = "/sys/role/auth?roleId="+roleId;
    openDialog("角色授权",url,"800px","600px");
}
/**重新刷新*/
function reLoad(msg){
    if(msg!=null&&msg!=undefined){
        NotifySuccess(msg);
    }
    /**重新初始化数据*/
    initTable();
}