
$fileTable=$("#fileTable");

$(document).ready(function () {
    initTable();
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
function initTable() {
    $fileTable.bootstrapTable({
        url:"/common/fileUpload/list",
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
        idField: 'fileId',                  //id
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
                checkbox: true
            }, {
                field:'fileName',
                title: '文件名称',
                align: 'center',
                sortable: true
            }, {
                field: 'filePath',
                title: '文件路径',
                sortable: true
            }, {
                field: 'fileType',
                title: '文件类型'
            }, {
                field: 'fileSize',
                title: '文件大小'
            }
            , {
                field: 'createDate',
                title: '上传时间',
                sortable: true
            },  {
                field: 'userId',
                title: '操作',
                sortable: true,
                formatter:function (value,row,index) {
                    let e = '<a class="btn btn-primary btn-sm" href="#" title="查看" onclick="view(\''
                        + row.fileId
                        + '\')"><i class="fa fa-edit"></i>查看</a> ';
                    //
                    let del = '<a class="btn btn-danger btn-sm" href="#" title="删除" onclick="toDelete(\''
                        + row.fileId
                        + '\')"><i class="fa fa-remove"></i>删除</a> ';
                    //
                    let download = '<a class="btn btn-danger btn-sm" href="#" title="删除" onclick="downloadFile(\''
                        + row.fileId
                        + '\')"><i class="fa fa-remove"></i>下载</a> ';
                    let display="";

                    if(s_view_h!=undefined&&s_view_h){
                        display=display+e;
                    }
                    if(s_remove_h!=undefined&&s_remove_h){
                        display=display+del;
                    }
                    if(s_download_h!=undefined&&s_download_h){
                        display=display+download;
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
                fileName: $('#fileName').val()
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
            var strclass = "info";
            return { classes: strclass }
        },
        onSort:function(name, order){
            $userTable.bootstrapTable('refreshOptions', {
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

//监听保存你按钮信息
function save(){
    let $form=$("#fileForm");
    /**组装为formData*/
    let formData=new FormData($form[0]);
    /**验证表单*/
    if($form.data("bootstrapValidator").isValid()){
        /**ajax提交表单***/
        $.ajax({
            url:$form.attr('action'),
            data:formData,
            type:"post",
            contentType:false,
            processData:false,
            dataType:"json",
            cache: false,
            success:function(result){
                if (result.success) {
                    NotifySuccess(result.message);
                    refresh();
                } else {
                    NotifyWarning(result.message);
                }
            }
        });
    }
}

/**重新调用初始化查询方法即可*/
function refresh() {
    var opt = {
        query : {
            fileName: $('#fileName').val()
        }
    };
    $fileTable.bootstrapTable('refresh',opt);
}

/**查看*/
function view(fileId){
    openDialog("查看文档","/common/fileUpload/view",'800px','600px');
}
/**删除*/
function toDelete(fileId){
    var deleteUrl = "/common/fileUpload/remove";
    if(confirm("确认删除？")){
        $.ajax({
            type:"post",
            url:deleteUrl,
            data:{fileId:fileId},
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
function batchDelete(){
    let selectedLine = $fileTable.bootstrapTable('getSelections');
    if (selectedLine.length < 1) {
        NotifyWarning("请选中一行数据");
    }else {
        let idList = selectedLine[0].fileId;
        for (let i = 1; i < selectedLine.length; i++) {
            idList += ',' + selectedLine[i].fileId;
        }
        if(confirm("确定删除选中的文件？")){
            $.ajax(
                {
                    type: "POST",
                    url: "/common/fileUpload/batchRemove",//url请求的地址
                    cache: false,  //禁用缓存
                    data: {ids:ids},  //传入组装的参数
                    dataType: "json",
                    traditional:true,
                    success: function (result) {
                        if(result.success){
                            NotifySuccess('删除成功！');
                            refresh();
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        NotifyWarning('删除失败！'+errorThrown);
                    }
                }
            );
        }
    }
}
/**下载*/
function downloadFile(fileId){
    let url = "/common/fileUpload/download";
    let form=document.createElement("form");
    form.style="display:none";
    form.method="post";
    form.action=url;
    form.target="";
    let inputHidden=document.createElement("input");
    inputHidden.name="fileId";
    inputHidden.value=fileId;
    form.appendChild(inputHidden);
    document.body.append(form);
    form.submit();
    document.body.removeChild(form);
}
/**批量下载*/
function downloads(){
    let selectedLine = $fileTable.bootstrapTable('getSelections');
    if (selectedLine.length < 1) {
        NotifyWarning("请选中一行数据");
    }else {
        let idList=new Array();
        for (let i = 0; i < selectedLine.length; i++) {
            idList.push(selectedLine[i].fileId);
        }
        let url = "/common/fileUpload/batchDownload";
        let form=$("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        let input1=$("<input>");
        input1.attr("type","hidden");
        input1.attr("name","ids")
        input1.attr("value",idList);
        form.append(input1);
        $("body").append(form);
        form.submit();
        form.remove();
    }
}
/**显示选择的文件名*/
function displayFileName(){

    $("#files").on('change',function (element) {
        let fName="<span class='file-name'>"+files.name+"</span>";
        $("#fileNames").append(fName);
    });
}