let layer;
let user;
let playerSet = new Set();
let playerArray = [];
let tableId;
let winnerIndex = 0;

window.onload=function(){
    layui.use('layer', function () {
        layer = layui.layer;
    });
    getUser();
    getTableId();
    document.getElementById("winner").setAttribute("value",user.userName);
    //与服务器建立websocket连接
    let url ="ws://127.0.0.1:8080/websocket/"+tableId+"/"+user.userId+"/"+user.userName;
    let ws = new WebSocket(url);
    ws.onopen = function () {
        console.log("建立 websocket 连接...");
    };
    ws.onmessage = function(event){
        let data = JSON.parse(event.data);
        if(data.code == 0){
            layer.msg(data.message,{icon: 6});
            getPlayer();
        }else {
            layer.alert(data.message,{icon: 2});
        }
    };
    ws.onclose = function(){
        $('#message_content').append('用户['+username+'] 已经离开聊天室!' + '\n');
        console.log("关闭 websocket 连接...");
    }
};

function getUser() {
    //获取用户信息
    $.ajax({
        type: "POST",
        url: "/user/getUser",
        async: false,
        dataType : "json",
        success : function(data) {
            if (data.code == 0){
                user = data.data;
            }else{
                layer.alert("页面初始化失败:"+data.message,{icon: 2});
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
        data:{
            userId:user.userId
        },
        dataType : "json",
        success : function(data) {
            if (data.code == 0){
                tableId = data.data;
            }else{
                layer.alert("页面初始化失败:"+data.message,{icon: 2});
            }
        }
    });
}

function getPlayer() {
    $.ajax({
        type: "POST",
        url: "/table/getPlayer",
        async: false,
        data:{
            tableId:tableId
        },
        dataType : "json",
        success : function(data) {
            if (data.code == 0){
                let userNames = data.data;
                let size = playerSet.size;
                for (let i=0;i<userNames.length;i++){
                    playerSet.add(userNames[i]);
                }
                if(playerSet.size !== size){
                    initPlayerArray();
                }
            }else{
                layer.alert("获取玩家信息失败:"+data.message,{icon: 2});
            }
        }
    });
}

function initPlayerArray() {
    playerArray = [];
    playerSet.forEach(function (element, sameElement, set) {
        playerArray.push(element);
    });
}

function onWinnerChange() {
    winnerIndex++;
    if(winnerIndex === playerArray.length){
        winnerIndex = 0;
    }
    document.getElementById("winner").setAttribute("value",playerArray[winnerIndex]);
}

function onRedoubleSub(that) {
    let numLabel = $(that).next();
    let num = numLabel.attr("value");
    num--;
    if(num < 0){
        num = 0;
    }
    $(numLabel).attr("value",num);
}

function onRedoubleAdd(that) {
    let numLabel = $(that).prev();
    let num = numLabel.attr("value");
    num++;
    if(num > 4){
        num = 4;
    }
    $(numLabel).attr("value",num);
}