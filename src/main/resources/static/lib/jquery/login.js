
setTimeout('ToLogin()',1);

function ToLogin() {
    $.ajax({
        success:function(){
            var loginFrameJS ="";
            loginFrameJS+="<h1>Welcome</h1>"+
            "<input type='text' id='userName' name='userName' placeholder='Username'><br>"+
                "<input type='password' id='password' name='password' placeholder='Password'><br>"+
                "<button type='submit' onclick='Login()' id='login'>Login</button>"+
                "<button type='submit' onclick='toRegister()' id='toRegister'>toRegister</button>";

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
    if (userName = "undefined" || userName == null || userName == "") {
        alert("用户名不能为空");
    }else if (userName.length>11){
        alert("用户名过长");
    }
    return false;
}

function Login() {
    //if(!checkUser()) return;
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
                "<input type='text' id='userName' name='userName' placeholder='Username'><br>"+
                "<input type='password' id='password' name='password' placeholder='Password'><br>"+
                "<input type='password' id='rePassword' name='rePassword' placeholder='Password'><br>"+
                "<button type='submit' onclick='ToLogin()' id='login'>ToLogin</button>"+
                "<button type='submit' onclick='IsUserExist()' id='toRegister'>Register</button>";

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
            console.log(data)
            if(data.code == -1){
                var r=confirm("用户名不能为空");
                if (r==true){
                    ToRegister();
                }
                ToRegister();
            } else if (data.code == 0 || data.data) {
                Register();
            }
            Register();
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
            if (data.code == 0) {
                window.location.href = 'index';
            }else{
                alert(data.message);
            }
        },
        error:function(xhr,status,error){
            console.log(xhr);
            console.log(status);
            console.log(error);
        }
    });
}

