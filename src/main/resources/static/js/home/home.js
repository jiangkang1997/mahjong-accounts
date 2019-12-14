/**
 * Created by Administrator on 2017/11/22.
 */

var imgnum=0;
var n=0;

$(document).ready(function(){
    imgnum=$(".carousel_ul li").length;


    $(".nav li").bind({
        mouseover:function(){
            $(this).children(".child_menu").css("display","list-item")
        },
        mouseout:function(){

            $(this).children(".child_menu").css("display","none")

        }
    })

    window.setInterval(lunbo,1500)
})

function lunbo(){

    $(".carousel_ul li").eq((n+1)%imgnum).css({
        display:"list-item",
        opacity:"0"
    }).animate({opacity:"1"},{duration:600,easing:"swing",queue:false})
    $(".carousel_ul li").eq(n).animate({opacity:"0"},{duration:600,easing:"swing",queue:false,complete: function(){
        $(this).css("display","none")
        $(".circle li").eq(n).css("backgroundColor","black")
        $(".circle li").eq((n+1)%imgnum).css("backgroundColor","white")

    }
    })

    n++;
    if(n==imgnum){
        n=0;
    }

}