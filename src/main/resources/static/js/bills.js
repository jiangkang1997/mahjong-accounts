let tableId;
let user;

window.onload = function () {
    getUser();
    getTableId();
};

function getUser() {
    //获取用户信息
    $.ajax({
        type: "POST",
        url: "/user/getUser",
        async: false,
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                user = data.data;
            } else {
                layer.alert("页面初始化失败:" + data.message, {icon: 2});
            }
        }
    });
}

function getTableId() {
    //获取桌id
    $.ajax({
        type: "POST",
        url: "/table/getTableId",
        async: false,
        data: {
            userId: user.userId
        },
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                tableId = data.data;
            } else {
                layer.alert("页面初始化失败:" + data.message, {icon: 2});
            }
        }
    });
}

layui.use('table', function(){
    let table = layui.table;
    table.render({
        elem: '#accountTable'
        ,page : true
        ,limit : 15
        ,url: '/account/getAccount' //数据接口
        ,where:{
            tableId:tableId
        }
        ,cols: [[ //表头
            {field: 'check',  width:"5%", fixed: 'left',type:"checkbox"}
            ,{field: 'userName', title: '用户名', width:"20%"}
            ,{field: 'profit', title: '结算金额', width:"50%", sort: true}
        ]]
        ,response: {
            statusName: 'code' //规定数据状态的字段名称，默认：code
            , statusCode: 0 //规定成功的状态码，默认：0
            , msgName: 'description' //规定状态信息的字段名称，默认：msg
            , dataName: 'data' //规定数据列表的字段名称，默认：data
        }
    });
});