
$(document).ready(function () {
    var login_option=$(".login>span");
    var login_img=$(".login .customer_img img")
    var login_img2=$(".login .customer_img")
    var isHide=true;
    function hideMsg() {
        window.setTimeout(function () {
            if(isHide){
                $(".personal_msg").css("display","none")
            }
        },300)
    }
    if(isOnline){

        login_img2.css("display","block")
        login_option.css("display","none")

        login_img.bind({
            mouseover :function(){
                $(".personal_msg").css("display","block")
                isHide=false
            },
            mouseout:function(){
                isHide=true
                hideMsg()

            }
        })
        $(".personal_msg").mouseover(function () {
            isHide=false
        }).mouseout(function () {
            isHide=true
            window.setTimeout(function () {
                if(isHide){
                    $(".personal_msg").css("display","none")
                }
            },300)
        })
    }
    else{
        login_img2.css("display","none")
        login_option.css("display","block")
    }
})
