
setTimeout('ToLogin()',1);

 //登录
var welcome = "<h1>Welcome</h1>";
var yhm = "<input type='text' id='userName' name=' userName' placeholder='请输入用户名'><br>";
var mima = "<input type='password' id='password' name='password' placeholder='请输入密码'><br>";
var denglu = "<button  style='margin-right:25px;' onclick='Login()' id='login'>登录</button>";
var toZhuce =  "<button  style='margin-left:25px;' onclick='toRegister()' id='toRegister'>注册</button>";

//注册
var remima = "<input type='password' id='rePassword' name='rePassword' placeholder='请确认密码'><br>";
var toDenglu = "<button style='margin-right:25px;' onclick='ToLogin()' id='login'>登录</button>";
var zhuce = "<button style='margin-left:25px;'  onclick='IsUserExist()' id='toRegister'>注册</button>";

function ToLogin() {
    $.ajax({
        success:function(){
            $("#loginFrame").html(welcome+yhm+mima+denglu+toZhuce);
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
    }else
    if (userName.length < 4 || userName.length > 16) {
        alert("用户名位必须4-16字符");
    }else
    if (jud.test(userName)) {
        alert("不得含有特殊字符");
    }else
    if (password.length < 6 || password.length > 16){
        alert("密码必须6-16字符");
    }else{
        return true;
    }
    return false;
}

function Login() {
    if(!checkUser()) return;
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
                window.location.href = '/page/home';
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
            $("#loginFrame").html(welcome+yhm+mima+remima+toDenglu+zhuce);
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

function IsUserExist() {
    if(!checkUser()) return;
    $.ajax({
        type:"POST",
        url:"/user/isUserExist",
        data:{
            "userName":$('#userName').val(),
        },
        dataType : 'json',
        success:function(data){
            if ($('#password').val() != $('#rePassword').val()){
                alert("两次密码不一致！");
            }else
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
               window.location.href = '/page/login';
           }
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

