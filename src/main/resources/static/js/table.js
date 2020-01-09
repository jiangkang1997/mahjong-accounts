let layer;
let user;
let playerMap = new Map();
let playerArray = [];
let tableId;
let winnerIndex = 0;

window.onload = function () {
    layui.use('layer', function () {
        layer = layui.layer;
    });
    getUser();
    getTableId();
    //与服务器建立websocket连接
    let url = "ws://127.0.0.1:8080/websocket/" + tableId + "/" + user.userId + "/" + user.userName;
    let ws = new WebSocket(url);
    ws.onopen = function () {
        console.log("建立 websocket 连接...");
    };
    ws.onmessage = function (event) {
        let data = JSON.parse(event.data);
        if (data.code == 0) {
            layer.msg(data.message, {icon: 6});
            getPlayer();
        } else {
            layer.alert(data.message, {icon: 2});
        }
    };
    ws.onclose = function () {
        $('#message_content').append('用户[' + username + '] 已经离开聊天室!' + '\n');
        console.log("关闭 websocket 连接...");
    }
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

function getPlayer() {
    $.ajax({
        type: "POST",
        url: "/table/getPlayer",
        async: false,
        data: {
            tableId: tableId
        },
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                let users = data.data;
                let size = playerMap.size;
                for (let i = 0; i < users.length; i++) {
                    playerMap.set(users[i].userId, users[i]);
                }
                if (playerMap.size !== size) {
                    initPlayerArray();
                }
                document.getElementById("winner").setAttribute("value", playerArray[winnerIndex].userName);
            } else {
                layer.alert("获取玩家信息失败:" + data.message, {icon: 2});
            }
        }
    });
}

function initPlayerArray() {
    playerArray = [];
    playerMap.forEach(
        function (value, key, m) {
            playerArray.push(value);
        }
    );
    let redouble_name_labels = $("label.redouble_content_name_label");
    let private_gang_name_labels = $("label.private_gang_winner_name_label");
    let public_gang_winner_name_selects = $("select.public_gang_winner_name_select");
    let public_gang_loser_name_selects = $("select.public_gang_loser_name_select");
    for (let i = 0; i < playerArray.length; i++) {
        $(redouble_name_labels[i]).html(playerArray[i].userName);
        $(redouble_name_labels[i]).parent().next().next().attr("name", playerArray[i].userId);
        $(private_gang_name_labels[i]).html(playerArray[i].userName);
        $(private_gang_name_labels[i]).parent().next().next().attr("name", playerArray[i].userId);
        $(public_gang_winner_name_selects).append("<option value=\"" + playerArray[i].userId + "\">" + playerArray[i].userName + "</option>");
        $(public_gang_loser_name_selects).append("<option value=\"" + playerArray[i].userId + "\">" + playerArray[i].userName + "</option>");
    }
}

function onWinnerChange() {
    winnerIndex++;
    if (winnerIndex === playerArray.length) {
        winnerIndex = 0;
    }
    document.getElementById("winner").setAttribute("value", playerArray[winnerIndex].userName);
}

function onSub(that) {
    let numLabel = $(that).next();
    let num = numLabel.attr("value");
    num--;
    if (num < 0) {
        num = 0;
    }
    $(numLabel).attr("value", num);
}

function onAdd(that) {
    let numLabel = $(that).prev();
    let num = numLabel.attr("value");
    num++;
    if (num > 4) {
        num = 4;
    }
    $(numLabel).attr("value", num);
}

function onPublicGangAdd() {
    let html = "<div class=\"public_gang_content_div\">\n" +
        "            <div class=\"public_gang_winner_name_div\">\n" +
        "                <select class=\"public_gang_winner_name_select\"></select>\n" +
        "            </div>\n" +
        "            <div style=\"width: 80px;height: 50px;float: left\">\n" +
        "                <label style=\"font-size: 30px\">杠了</label>\n" +
        "            </div>\n" +
        "            <div class=\"public_gang_loser_name_div\">\n" +
        "                <select class=\"public_gang_loser_name_select\" style=\"height: 50px;width: 75px\"></select>\n" +
        "            </div>\n" +
        "        </div>"
    document.getElementById("public_gang_contents").innerHTML += html;
    initPlayerArray();
}

function commit() {
    let redoubleMap = {};
    let redouble_num_labels = $("input.redouble_num");
    for (let i = 0; i < redouble_num_labels.length; i++) {
        redoubleMap[$(redouble_num_labels[i]).attr("name")] = $(redouble_num_labels[i]).val();
    }
    let gangArray = [];
    let private_gang_num_labels = $("input.private_gang_num");
    for (let i = 0; i < private_gang_num_labels.length; i++) {
        let private_gang_num = $(private_gang_num_labels[i]).val();
        if (private_gang_num !== "0") {
            for (let i0 = 0; i0 < private_gang_num; i0++) {
                let gang = {};
                gang["isPublic"] = false;
                gang["winner"] =  $(private_gang_num_labels[i]).attr("name");
                gang["loser"] =  null;
                gangArray.push(gang);
            }
        }
    }
    let public_gang_winners = $("select.public_gang_winner_name_select");
    for (let i = 0; i < public_gang_winners.length; i++) {
        let winnerId = $(public_gang_winners[i]).val();
        let loserId = $(public_gang_winners[i]).parent().next().next().children("select.public_gang_loser_name_select").val();
        let gang = {};
        gang["isPublic"] = true;
        gang["winner"] =  winnerId;
        gang["loser"] =  loserId;
        gangArray.push(gang);
    }
    $.ajax({
        type: "POST",
        url: "/account/submit",
        async: false,
        data: {
            providerId:user.userId,
            winnerId:playerArray[winnerIndex].userId,
            tableId:tableId,
            redouble:JSON.stringify(redoubleMap),
            gangs: JSON.stringify(gangArray),
        },
        dataType: "json",
        success: function (data) {
            if (data.code == 0) {
                layer.msg("提交成功，等待其他人提交", {icon: 1});
            } else {
                layer.alert("获取玩家信息失败:" + data.message, {icon: 2});
            }
        }
    });
}