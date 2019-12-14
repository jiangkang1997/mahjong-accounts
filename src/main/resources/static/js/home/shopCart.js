$(document).ready(function () {

    var num=1;//商品数量
    var goodsDiv=$(".single_goods")
    var goodsNum=$(".goodsNum")//选取商品数量表单元素
    var totalPrice=$(".single_goods .goods_all_price")//选取总价元素
    var unitPrice=$(".uitPrice")//获取商品单价元素
    var price//存储商品单价的数值
    var totalPriceNum=0;//存储商品总价
    var divIndex=0;//标识是第几个盒子
    var upObj=$(".up");
    var downObj=$(".down")
    var goodsTotalNum=0;//所有商品的数量
    var selectedDivIndex=0;
    var checkBox=$(".select")
    var goodsTotalNumObj=$(".num")
    var goodsTotalPriceObj=$(".all_price .price")
    var goodsTotalPrice=0;
    var submitButton=$(".clear_button input")
    var generalElection=$(".general_election input")
   /* var deleteIndex=0;
    var deleteSpan=$(".operate span")
    var deleteSpan2=$(".delete span")*/
    upObj.bind("click",function (e) {

        var target=e.srcElement ? e.srcElement : e.target;
        divIndex=upObj.index(target)
        var request=new XMLHttpRequest()
        var shopCartId=$(".goods_id_input").eq(divIndex).attr("value")
        $.ajax({
            type:"get",
            url:"/store/shoppingcart.do?action=numAdd&shoppingCartId="+shopCartId,
            datatype:"text",
            success:function (data) {

                goodsNum.eq(divIndex).attr("value",data)
                price=parseInt(unitPrice.eq(divIndex).text())
                totalPriceNum=price*parseInt(data)

                totalPrice.eq(divIndex).text(totalPriceNum)
                update()
            },
            error:function (jqXHR) {
                alert("发生错误！"+jqXHR.status)
            }
        })
        /*num=parseInt(goodsNum.eq(divIndex).attr("value"))
        price=parseInt(unitPrice.eq(divIndex).text())
        num++;
        totalPriceNum=(price*num)
        goodsNum.eq(divIndex).attr("value",num)
        totalPrice.eq(divIndex).text(totalPriceNum)
        update()*/

    })
    downObj.bind("click",function (e) {

        var target=e.srcElement ? e.srcElement : e.target;
        divIndex=downObj.index(target)
        var request=new XMLHttpRequest()
        var shopCartId=$(".goods_id_input").eq(divIndex).attr("value")
        num=parseInt(goodsNum.eq(divIndex).attr("value"))
        if(num<=1) return;
        $.ajax({
            type:"get",
            url:"/store/shoppingcart.do?action=numSub&shoppingCartId="+shopCartId,
            datatype:"text",
            success:function (data) {
                goodsNum.eq(divIndex).attr("value",data)
                price=parseInt(unitPrice.eq(divIndex).text())
                totalPriceNum=price*parseInt(data)
                totalPrice.eq(divIndex).text(totalPriceNum)
                update()
            },
            error:function (jqXHR) {
                alert("发生错误！"+jqXHR.status)
            }
        })

    })
    checkBox.bind("click",function (e) {
        update()
    })
    generalElection.click(function () {
        if($(this).prop("checked")==true){
            checkBox.each(function () {
                $(this).prop("checked","true")
            })
            update()
        }
        else {
            checkBox.each(function () {
                $(this).prop("checked",null)
            })
            update()
        }
    })
  /*  $(".operate span").bind("click",function () {
        deleteIndex=deleteSpan.index($(this))
        console.log(deleteIndex)
        goodsDiv.eq(deleteIndex).remove()
        update()
    })
    deleteSpan2.bind("click",function () {
        var index=0;
        checkBox.each(function () {
            if($(this).prop("checked")==true){
                index=checkBox.index($(this))
                goodsDiv.eq(index).remove()
            }
            update()

        })
    })*/
    function beforeSubmit() {
        var goodsIds="";
        var index=0;
        checkBox.each(function () {
            if($(this).prop("checked")==true){
                index=checkBox.index($(this))
                goodsIds+=$(".goods_id_input").eq(index).attr("value")+"/"
            }
        })
        $(".delete_input").attr("value",goodsIds)
        /*alert(goodsIds)*/
    }
    $(".delete #delete_submit").submit(function () {
        beforeSubmit()
    })
    function update() {
        goodsTotalNum=0
        goodsTotalPrice=0;
        var checkedNum=0;
        checkBox.each(function () {
            if($(this).prop("checked")==true){
                selectedDivIndex=checkBox.index($(this))
                checkedNum++;
                var tempStr=goodsNum.eq(selectedDivIndex).attr("value")
                var tempStr2=totalPrice.eq(selectedDivIndex).text()

                goodsTotalPrice+=parseInt(tempStr2)
                goodsTotalNum+=parseInt(tempStr)
            }
        })
        if(checkedNum>0){
            submitButton.css("backgroundColor","red")
        }
        else {
            submitButton.css("backgroundColor","#B0B0B0")
        }
        goodsTotalNumObj.text(goodsTotalNum)
        goodsTotalPriceObj.text(goodsTotalPrice)
    }

})
