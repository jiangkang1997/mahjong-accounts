
setTimeout('ToLogin()',1);

function ToLogin() {
    $.ajax({
        success:function(){
            var loginFrameJS ="";
            loginFrameJS+="<h1>Welcome</h1>"+
            "<input type='text' id='userName' name='userName' placeholder='请输入用户名'><br>"+
                "<input type='password' id='password' name='password' placeholder='请输入密码'><br>"+
                "<button onclick='Login()' id='login'>登录</button>"+
                "<button  onclick='toRegister()' id='toRegister'>注册</button>";

            $("#loginFrame").html(loginFrameJS);
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

function checkUser() {
    var userName = $('#userName').val();
    var password = $('#password').val();
    var rePassword = $('#rePassword').val();
    var jud = /[@#\$%\^&\*]+/;
    if (userName == undefined || userName == null || userName == "") {
        alert("用户名不能为空");
    }
    if (userName.length < 4 || userName.length > 16) {
        alert("用户名位必须4-16字符");
    }
    if (jud.test(userName)) {
        alert("不得含有特殊字符");
    }
    if (password.length < 6 || password.length > 16){
        alert("密码必须6-16字符");
    }
    if (password !=rePassword){
        alert("两次密码不一致！");
    }
    return false;
}

function Login() {
    //if(!checkUser()) return;
    console.log("13123");
    $.ajax({
        type:"POST",
        url:"/user/login",
        data:{
            "userName":$('#userName').val(),
            "password":$('#password').val(),
        },
        dataType : 'json',
        success:function(data){
            if (data.code == 0) {
                window.location.href = 'index';
            }else{
                var r=confirm(data.message);
                if (r==true){
                    ToLogin();
                }
                    ToLogin();
            }
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

function toRegister() {
    $.ajax({
        success:function(){
            var loginFrameJS ="";
            loginFrameJS+="<h1>Welcome</h1>"+
                "<input type='text' id='userName' name='userName' placeholder='请输入用户名'><br>"+
                "<input type='password' id='password' name='password' placeholder='请输入密码'><br>"+
                "<input type='password' id='rePassword' name='rePassword' placeholder='请确认密码'><br>"+
                "<button  onclick='ToLogin()' id='login'>登录</button>"+
                "<button  onclick='IsUserExist()' id='toRegister'>注册</button>";

            $("#loginFrame").html(loginFrameJS);
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

function IsUserExist() {
    //if(!checkUser()) return;
    $.ajax({
        type:"POST",
        url:"/user/isUserExist",
        data:{
            "userName":$('#userName').val(),
        },
        dataType : 'json',
        success:function(data){
            if(data.data){
                var r=confirm("用户名已存在");
                ToRegister();
            }else{
               Register();
            }
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

function Register() {
    $.ajax({
        type:"POST",
        url:"/user/register",
        data:{
            "userName":$('#userName').val(),
            "password":$('#password').val(),
        },
        dataType : 'json',
        success:function(data){
           if (data.code == -1 && data.message != "success"){
               var r=confirm("用户名已存在");
               ToRegister();
           }else if (data.code == 0 && data.message == "success"){
               var r=confirm("注册成功");
               window.location.href = 'index';
           }
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

