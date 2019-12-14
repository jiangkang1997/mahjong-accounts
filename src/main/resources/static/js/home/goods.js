/**
 * Created by Administrator on 2017/11/25.
 */
$(document).ready(function(){

    var num_obj=$("#num")
    var num=1;
    $(".top_img").bind("click",function(){

        num++;
        num_obj.attr("value",num)
    })
    $(".bottom_img").bind("click",function(){
        if(num>1){
            num--;
            num_obj.attr("value",num)
        }

    })
})