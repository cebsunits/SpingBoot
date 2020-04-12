var index = parent.layer.getFrameIndex(window.name);
var deptTree;
$(function() {
    //获取树结构
    getTreeData();
    $('#btn-search').click=search();
    $('#btn-search').keyup=search();
    $('#btn-clear-search').click=clearSearch();
})
//获取树信息
function getTreeData() {
    //ajax请求数据方法
    $.ajax({
        type: "POST",
        url: "/sys/dept/deptTree",//url请求的地址
        cache: false,  //禁用缓存
        data: {},  //传入组装的参数
        dataType: "json",
        success: function (result) {
            if(result.success){
                toastr.success('查询成功！');
                deptTree= loadTree(result.data);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            toastr.warning('查询失败！');
            alert("status:" + XMLHttpRequest.status + ",readyState:" + XMLHttpRequest.readyState + ",textStatus:" + textStatus);
        }
    });
}
//加载树
function loadTree(data) {
    deptTree= $("#deptTree").treeview(
        {
            data:data,
            level:5,
            nodeIcon: "glyphicon",
            selectedIcon: 'glyphicon',
            showCheckbox:true,
            multiSelect: true,
            selectable:true,
            //一个节点被选择。
            onNodeSelected:function onNodeSelected(event, data){
                console.log("onNodeSelected"+data)
            },
            //取消选择一个节点。
            onNodeUnselected:function nodeUnselected(event, data){
                console.log("onNodeUnselected"+data)
            },
            //一个节点被checked。
            onNodeChecked :function nodeChecked (event, data){
                console.log("onNodeChecked"+data)
                console.log(JSON.stringify(data))
            },
            //一个节点被unchecked。
            onNodeUnchecked:function nodeUnchecked(event, data){
                console.log("onNodeUnchecked"+data)
                console.log(JSON.stringify(data))
            }
        }
    );
    return deptTree;
}
//获取查询结果
function search(){
    if(deptTree!=undefined){
        var pattern = $.trim($('#input-search').val());
        var options = {
            ignoreCase: false,
            exactMatch: false,
            revealResults: true
        };
        var results = deptTree.treeview('search', [pattern, options]);
        var output = '<p>' + results.length + ' 匹配的搜索结果</p>';
        $.each(results,
            function(index, result) {
                output += '<p>- <span style="color:red;">' + result.text + '</span></p>';
            });
        $('#search-output').html(output);
    }
}
//清空
function clearSearch() {
    if(deptTree!=undefined){
        deptTree.treeview('clearSearch');
        $('#input-search').val('');
        $('#search-output').html('');
        $('#deptTree').treeview('collapseAll', {
            silent: false //设置初始化节点关闭
        });
    }
}
//点击提交时把选择的数据返回给父页面
function selectDepts() {
    let depts=$("#deptTree").treeview("getChecked");
    let deptIds="";
    let deptNames="";
    for(let i=0;i<depts.length;i++){
        if(deptIds==""){
            deptIds=depts[i].id;
        }else{
            deptIds=deptIds+","+depts[i].id;
        }
        if(deptNames==""){
            deptNames=depts[i].text;
        }else{
            deptNames=deptNames+","+depts[i].text;
        }
    }
    parent.$('#deptIds').val(deptIds);
    parent.$('#deptName').val(deptNames);
    parent.layer.close(index);
}