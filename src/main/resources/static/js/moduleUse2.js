/**
 * Created by wangjingshen on 2017/11/3.
 */

layui.use(['form', 'laydate','layedit','laypage','layer'], function () {
    var form = layui.form;
    //var element = layui.element;
    var laydate = layui.laydate;
    var layedit = layui.layedit;
    var laypage = layui.laypage;
    var layer = layui.layer;
    //
    // 富文本编辑器
    layedit.set({
        uploadImage: {
            url: '' //接口url
            ,type: '' //默认post
        }
    });
    //日期
    laydate.render({
        elem: '#date'
        ,lang: 'en'
    });
    laydate.render({
        elem: '#date1'
        ,lang: 'en'
    });
    //建立编辑器
    layedit.build('RichTextBox',{
        height:100
    });layedit.build('RichTextBoxPlateform',{
        height:100
    });
    //时间控件显示 
   /* laydate.render({
        elem: '#calender'
        //,mark:logTime
        ,position: 'static'
        ,lang: 'en'
        ,showBottom: false
        ,change: function(value, date){ //监听日期被切换
        	//layer.msg('ttt');
        	//add();
        	
        },
        done: function(value, date, endDate){
        	//add(value+" 00:00:00");
        	logList(value+" 00:00:00");
            if(date.year == 2018 && date.month == 11 && date.date == 30){
              dateIns.hint('一不小心就月底了呢');
            }
          }
    
    });*/
   //    分页
});
var is = false;
$(".layui-nav .layui-nav-item").mouseover(function(){
	var _this = this;
	$(_this).find("dl").addClass("layui-show");
});
$(".layui-nav .layui-nav-item dl").mouseover(function(){
	is = true;
	var _this = this;
	//alert($(_this).parent().index());
	$(".layui-nav .layui-nav-item dl").each(function(){
		if($(this).parent().index()!=$(_this).parent().index())
			$(this).removeClass("layui-show");
	});
});
$(".layui-nav .layui-nav-item dl").mouseout(function(){
	is = false;
	var _this = this;
	setTimeout(function(){
		if(!is)
			$(_this).removeClass("layui-show");
		
	},1000);
});
$(".layui-nav .layui-nav-item").mouseout(function(){
	var _this = this;
	h = setTimeout(function(){
//		alert("aa");
		if(!is)
			$(_this).find("dl").removeClass("layui-show");
	},1000);
});
