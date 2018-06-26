//页码增加
function more() {
    var currentMax = $("#currentMaxPage").val();
    var allPage = $("#allPage").val();
    var strPage = '<li><a onclick=\'pageChange(1)\'>«</a></li><li><a onclick=\'less()\'>…</a></li>';
    //判断是否会到尾页
    if (parseInt(currentMax) + 7 > allPage) {
        currentMax = allPage - 6;
    }
    $("#currentMinPage").val(currentMax);//当前最小显示页码
    for (var a = parseInt(currentMax); a <= allPage; a++) {
        if (a > (parseInt(currentMax) + 6)) {
            strPage = strPage + "<li><a  onclick='more()'>…</a></li>";
            break;
        }
        strPage = strPage + "<li><a   id='a" + a + "' onclick='pageChange(" + a + ")'>" + a + "</a></li>";
        $("#currentMaxPage").val(a);
    }
    strPage = strPage + "<li><a onclick='pageChange(" + allPage + ")'>»</a></li>";
    $("#pagination").html(strPage);
    //刷新选中状态
    $(".page .pagination li a").attr("class", "");
    if ($("#currentPage").val() != null && $("#currentPage").val() != '') {
        $("#a" + $("#currentPage").val() + "").attr("class", "active")
    }
}

//页码减少
function less() {
    var currentMin = $("#currentMinPage").val();
    var allPage = $("#allPage").val();
    var strPage = '<li><a onclick=\'pageChange(1)\'>«</a>';//
    currentMin = parseInt(currentMin) - 6;
    //判断是否会到首页
    if (currentMin <= 1) {
        currentMin = 1;
    } else {
        strPage = strPage + "</li><li><a onclick='less()'>…</a></li>";
    }
    $("#currentMinPage").val(currentMin);//当前最小显示页码
    for (var a = parseInt(currentMin); a <= allPage; a++) {
        if (a > (parseInt(currentMin) + 6)) {
            strPage = strPage + "<li><a  onclick='more()'>…</a></li>";
            break;
        }
        strPage = strPage + "<li><a   id='a" + a + "' onclick='pageChange(" + a + ")'>" + a + "</a></li>";
        $("#currentMaxPage").val(a);
    }
    strPage = strPage + "<li><a onclick='pageChange(" + allPage + ")'>»</a></li>";
    $("#pagination").html(strPage);
    //刷新选中状态
    $(".page .pagination li a").attr("class", "");

    if ($("#currentPage").val() != null && $("#currentPage").val() != '') {
        $("#a" + $("#currentPage").val() + "").attr("class", "active")
    }
}