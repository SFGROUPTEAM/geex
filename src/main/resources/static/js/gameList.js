$(document).ready(function () {
    showAllGameInfo();
});

//根据游戏分类遍历游戏，并生成分页
function showAllGameInfo() {
    /*<![CDATA[*/
    var gameInfoList = [[${gameInfoList}]];
    var str = '';
    //生成分页相关并记录到隐藏域
    var eachPageNum = 20;//每页显示数量
    var tempNum = Math.ceil([[${gameInfoListCount}]] / eachPageNum);//计算总页数
    $("#allPage").val(tempNum);
    $("#currentPage").val(1);//当前页面
    $("#currentMinPage").val(1);//当前最小显示页码
    $("#gameNum").val([[${gameInfoListCount}]]);
    var strPage = '<li><a onclick=\'pageChange(1)\'>«</a></li>';
    for (var a = 1; a <= tempNum; a++) {
        if (a > 7) {
            strPage = strPage + "<li><a  onclick='more()'>…</a></li>";
            break;
        }
        strPage = strPage + "<li><a   id='a" + a + "' onclick='pageChange(" + a + ")'>" + a + "</a></li>"
        $("#currentMaxPage").val(a);//当前最大显示页码
    }
    strPage = strPage + "<li><a onclick='pageChange(" + tempNum + ")'>»</a></li>";

    //遍历游戏
    $.each(gameInfoList, function (i, obj) {
        str = str + "<div class = \"game00\"><div class=\"gamePic\"><img src=\"" + obj.gamepic + "\"><h6>" + obj.name + "</h6></div>" +
            "<div class=\"gameRemark\"><p>" + obj.remark + "</p></div><div class=\"gameURL\"><input type='button' value='查看装备' onclick=\"javascrtpt:window.location.href='/Player/gamedetail.html?gameid=" + obj.id + "'\"/></div></div>";
    });//[[#{Common.surplus}]]  [[#{Common.ge}]]
    $("#gameList").html(str);
    $("#pagination").html(strPage);
    //增加第一页选中状态
    if ($("#currentPage").val() != null && $("#currentPage").val() != '') {
        $("#a1").attr("class", "active")
    }
    //显示页面信息
    $("#pageInfo").val("共" + $("#gameNum").val() + "条记录，共" + $("#allPage").val() + "页，当前为第" + $("#currentPage").val() + "页")
    /*]]>*/
//        //显示选中的类别分页
//        $(".title li").attr("class", "");
//        if ($("#currentCategory").val() != null && $("#currentCategory").val() != '') {
//            $(".title li[name=" + temp + "]").attr("class", "active")
//        }
}

//分页操作相关

//变更显示页面
function pageChange(obj) {
    //点击当前页页码
    if (obj == $("#currentPage").val()) {
        $("html,body").animate({scrollTop: 0}, 500);
        return;
    }
    //点击首页尾页时刷新分页
    var allPage = $("#allPage").val();
    if (parseInt(obj) == 1) {
        var temp = 1;
        var strPage = '<li><a onclick=\'pageChange(1)\'>«</a>';
        $("#currentMinPage").val(temp);//当前最小显示页码
        for (var a = 1; a <= allPage; a++) {
            if (a > (parseInt(temp) + 6)) {
                strPage = strPage + "<li><a  onclick='more()'>…</a></li>";
                break;
            }
            strPage = strPage + "<li><a   id='a" + a + "' onclick='pageChange(" + a + ")'>" + a + "</a></li>";
            $("#currentMaxPage").val(a);
        }
        strPage = strPage + "<li><a onclick='pageChange(" + allPage + ")'>»</a></li>";
        $("#pagination").html(strPage);
    } else if (parseInt(obj) == parseInt(allPage)) {
        var temp = parseInt(obj) - 6;
        var strPage = '<li><a onclick=\'pageChange(1)\'>«</a>';
        //判断是否会到首页
        if (temp <= 1) {
            temp = 1;
        } else {
            strPage = strPage + "</li><li><a onclick='less()'>…</a></li>";
        }
        $("#currentMinPage").val(temp);//当前最小显示页码
        for (var b = parseInt(temp); b <= allPage; b++) {
            if (b > (parseInt(temp) + 6)) {
                strPage = strPage + "<li><a  onclick='more()'>…</a></li>";
                break;
            }
            strPage = strPage + "<li><a   id='a" + b + "' onclick='pageChange(" + b + ")'>" + b + "</a></li>";
            $("#currentMaxPage").val(b);
        }
        strPage = strPage + "<li><a onclick='pageChange(" + allPage + ")'>»</a></li>";
        $("#pagination").html(strPage);
    }

    //请求数据库
    /*<![CDATA[*/
    var eachPageNum = 20;//每页显示数量
    $("#currentPage").val(obj);
    var str = '';
    $.ajax({
        type: 'POST',
        url: '/getGameListByPageNumAndCategory?categoryid=' + $("#categoryid").val() + '&pagenum=' + obj,
        datatype: 'json',
        cache: false,
        success: function (data) {
            var data1 = JSON.parse(data);
            $.each(data1[0].gameInfoList, function (i, obj) {
                str = str + "<div class = \"game00\"><div class=\"gamePic\"><img src=\"" + obj.gamepic + "\"><h6>" + obj.name + "</h6></div>" +
                    "<div class=\"gameRemark\"><p>" + obj.remark + "</p></div><div class=\"gameURL\"><input type='button' value='查看装备' onclick=\"javascrtpt:window.location.href='/Player/gamedetail.html?gameid=" + obj.id + "'\"/></div></div>";
                //[[#{Common.surplus}]]  [[#{Common.ge}]]
            });
            $("#gameList").html(str);
            //刷新选中状态
            $(".page .pagination li a").attr("class", "");
            if ($("#currentPage").val() != null && $("#currentPage").val() != '') {
                $("#a" + obj + "").attr("class", "active")
            }
            //显示页面信息
            $("#pageInfo").val("共" + $("#gameNum").val() + "条记录，共" + $("#allPage").val() + "页，当前为第" + $("#currentPage").val() + "页")
            $("html,body").animate({scrollTop: 0}, 500);
        }
    });
    /*]]>*/
}



function queryGameByCategoryAndName() {
    $.ajax({
        type: 'POST',
        url: '/getGameListByPageNumAndCategory?categoryid=' + $("#categoryid").val() + '&pagenum=1&name=' + $("#game_name_remark").val(),
        datatype: 'json',
        cache: false,
        success: function (data) {
            var data1 = JSON.parse(data);
            var str = '';
            $.each(data1[0].gameInfoList, function (i, obj) {
                str = str + "<div class = \"game00\"><div class=\"gamePic\"><img src=\"" + obj.gamepic + "\"><h6>" + obj.name + "</h6></div>" +
                    "<div class=\"gameRemark\"><p>" + obj.remark + "</p></div><div class=\"gameURL\"><input type='button' value='查看装备' onclick=\"javascrtpt:window.location.href='/Player/gamedetail.html?gameid=" + obj.id + "'\"/></div></div>";
                //[[#{Common.surplus}]]  [[#{Common.ge}]]
            });
            $("#gameList").html(str);

            //更新分页信息
            var num= data1[0].gameInfoListCount;
            var eachPageNum = 20;//每页显示数量
            var tempNum = Math.ceil(num / eachPageNum);//计算总页数
            $("#allPage").val(tempNum);
            $("#currentPage").val(1);//当前页面
            $("#currentMinPage").val(1);//当前最小显示页码
            $("#gameNum").val(num);
            var strPage = '<li><a onclick=\'pageChange(1)\'>«</a></li>';
            for (var a = 1; a <= tempNum; a++) {
                if (a > 7) {
                    strPage = strPage + "<li><a  onclick='more()'>…</a></li>";
                    break;
                }
                strPage = strPage + "<li><a   id='a" + a + "' onclick='pageChange(" + a + ")'>" + a + "</a></li>"
                $("#currentMaxPage").val(a);//当前最大显示页码
            }
            strPage = strPage + "<li><a onclick='pageChange(" + tempNum + ")'>»</a></li>";
            $("#pagination").html(strPage);
            //刷新选中状态
            $(".page .pagination li a").attr("class", "");
            if ($("#currentPage").val() != null && $("#currentPage").val() != '') {
                $("#a1").attr("class", "active")
            }
            //显示页面信息
            $("#pageInfo").val("共" + $("#gameNum").val() + "条记录，共" + $("#allPage").val() + "页，当前为第" + $("#currentPage").val() + "页")
            //高亮显示
            if($("#game_name_remark").val()!=null &&$("#game_name_remark").val()!=''){
                String.prototype.replaceAll = function(search, replacement) {
                    var target = this;
                    return target.replace(new RegExp(search, 'g'), replacement);
                };
                $(".gamePic h6").html($(".gamePic h6").html().replaceAll($("#game_name_remark").val(),"<a style='color: red'>"+$("#game_name_remark").val()+"</a>"));
                $(".gameRemark p").html($(".gameRemark p").html().replaceAll($("#game_name_remark").val(),"<a style='color: red'>"+$("#game_name_remark").val()+"</a>"));
//                    var str=$("#gameList").html();
//                    var n=str.replaceAll($("#game_name_remark").val(),"<a style='color: red'>"+$("#game_name_remark").val()+"</a>");
//                    $("#gameList").html(n)
            }
        }
    });
}