$(document).ready(function(){
	
	//获取JS传递的语言参数
	var utils = new Utils();
	var args = utils.getScriptArgs();	
	
	
	//隐藏Loading/注册失败 DIV
	$(".loading").hide();
	$(".login-error").hide();
	registError = $("<label class='error repeated'></label>");
	
	//加载国际化语言包资源
	utils.loadProperties(args.lang);
	
	//输入框激活焦点、移除焦点
	jQuery.focusblur = function(focusid) {
		var focusblurid = $(focusid);
		var defval = focusblurid.val();
		focusblurid.focus(function(){
			var thisval = $(this).val();
			if(thisval==defval){
				$(this).val("");
			}
		});
		focusblurid.blur(function(){
			var thisval = $(this).val();
			if(thisval==""){
				$(this).val(defval);
			}
		});
	 
	};
	/*下面是调用方法*/
	$.focusblur("#email");
	
	//获取表单验证对象[填写验证规则]
	var validate = $("#signupForm").validate({
		rules: {
			email: {
				required: true,
				email: true
			},
			password: {
				required: true,
				minlength: 4,
				maxlength: 16
			},
			passwordAgain: {
				required: true,
				minlength: 4,
				maxlength: 16,
				equalTo: "#password"
			},
            fullname: {
				required: true
			},
			phone: {
				required: true
			},
			tel: {
				required: true,
				digits:true
			},
			qq: {
				required: true,
				digits:true
			},
            shortname: {
                required: true,
            },
            address: {
                required: true,
            }
		},
		messages: {
			email: {
				required: $.i18n.prop("请输入邮箱"),
				email: $.i18n.prop("请输入正确的邮箱")
			},
			password: {
				required: $.i18n.prop("请输入密码"),
				minlength: jQuery.format($.i18n.prop("密码最小4位")),
				maxlength: jQuery.format($.i18n.prop("密码最大16位"))
			},
			passwordAgain: {
				required: $.i18n.prop("请确认密码"),
				minlength: jQuery.format($.i18n.prop("密码最小4位")),
				maxlength: jQuery.format($.i18n.prop("密码最大16位")),
				equalTo: jQuery.format($.i18n.prop("密码输入不一致"))
			},
            fullname: {
				required: $.i18n.prop("请输入联系人")
			},
            phone: {
				required: $.i18n.prop("请输入电话"),
				digits: $.i18n.prop("电话格式输入错误")
			},
			qq: {
				required: $.i18n.prop("请输入qq"),
				digits: $.i18n.prop("qq格式输入错误")
			},
            shortname: {
                required: $.i18n.prop("请输入公司名称"),
            },
            address: {
                required: $.i18n.prop("请输入地址"),
            }

		}
	});
	
	
	//输入框激活焦点、溢出焦点的渐变特效
	if($("#email").val()){
		$("#email").prev().fadeOut();
	};
	$("#email").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#email").blur(function(){
		if(!$("#email").val()){
			$(this).prev().fadeIn();
		};		
	});
	if($("#password").val()){
		$("#password").prev().fadeOut();
	};
	$("#password").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#password").blur(function(){
		if(!$("#password").val()){
			$(this).prev().fadeIn();
		};		
	});
	if($("#passwordAgain").val()){
		$("#passwordAgain").prev().fadeOut();
	};
	$("#passwordAgain").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#passwordAgain").blur(function(){
		if(!$("#passwordAgain").val()){
			$(this).prev().fadeIn();
		};		
	});
	/*if($("#contact").val()){
		$("#contact").prev().fadeOut();
	};
	$("#contact").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#contact").blur(function(){
		if(!$("#contact").val()){
			$(this).prev().fadeIn();
		};		
	});*/
	if($("#fullname").val()){
		$("#fullname").prev().fadeOut();
	};
	$("#fullname").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#fullname").blur(function(){
		if(!$("#fullname").val()){
			$(this).prev().fadeIn();
		};		
	});
	if($("#phone").val()){
		$("#phone").prev().fadeOut();
	};
	$("#phone").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#phone").blur(function(){
		if(!$("#phone").val()){
			$(this).prev().fadeIn();
		};		
	});
	if($("#qq").val()){
		$("#qq").prev().fadeOut();
	};
	$("#qq").focus(function(){
		$(this).prev().fadeOut();
	});
	$("#qq").blur(function(){
		if(!$("#qq").val()){
			$(this).prev().fadeIn();
		};		
	});

    if($("#shortname").val()){
        $("#shortname").prev().fadeOut();
    };
    $("#shortname").focus(function(){
        $(this).prev().fadeOut();
    });
    $("#shortname").blur(function(){
        if(!$("#shortname").val()){
            $(this).prev().fadeIn();
        };
    });
    if($("#address").val()){
        $("#address").prev().fadeOut();
    };
    $("#address").focus(function(){
        $(this).prev().fadeOut();
    });
    $("#address").blur(function(){
        if(!$("#address").val()){
            $(this).prev().fadeIn();
        };
    });
	
	//ajax提交注册信息
	$("#submit").bind("click", function(){
		regist(validate);
	});
	
	$("body").each(function(){
		$(this).keydown(function(){
			if(event.keyCode == 13){
				regist(validate);
			}
		});
	});
	
});

function regist(validate){	
	//校验Email, password，校验如果失败的话不提交
	if(validate.form()){
		if($("#checkBox").attr("checked")){
			var md5 = new MD5();
			$.ajax({
				url: "./register",
				type: "post",
				data: {
                    email: $("#email").val(),
					password: md5.MD5($("#password").val()),
					fullname: $("#fullname").val(),
					phone: $("#phone").val(),
					qq: $("#qq").val(),
                    shortname: $("#shortname").val(),
                    address: $("#address").val()
				},
				dataType: "json",
                beforeSend: function(){
                    $('.loading').show();
                },
				success: function(data){
					$('.loading').hide();
					console.log(data);
					if(data.hasOwnProperty("retVal")){
						if(data.retVal == "00"){
							//注册成功
                            //layer.alert("注册成功，供应商编号为："+data.retMsg);
                            alert("注册成功，供应商编号为："+data.retMsg);
							window.location.href = "register_form";
						}else if(data.retVal == "01"){
                            $(".login-error").show();
                            $(".login-error").html($.i18n.prop(data.retMsg));
							//数据库链接失败
						}
					}
				}
			});
		}else{
			//勾选隐私政策和服务条款
			$(".login-error").show();
			$(".login-error").html($.i18n.prop("请阅读并同意，隐私政策、服务条款"));
		}
	}
}

var Utils = function(){};

Utils.prototype.loadProperties = function(lang){
	jQuery.i18n.properties({// 加载资浏览器语言对应的资源文件
		name:'ApplicationResources',
		language: lang,
		path:'resources/i18n/',
		mode:'map',
		callback: function() {// 加载成功后设置显示内容
		} 
	});	
};

Utils.prototype.getScriptArgs = function(){//获取多个参数
    var scripts=document.getElementsByTagName("script"),
    //因为当前dom加载时后面的script标签还未加载，所以最后一个就是当前的script
    script=scripts[scripts.length-1],
    src=script.src,
    reg=/(?:\?|&)(.*?)=(.*?)(?=&|$)/g,
    temp,res={};
    while((temp=reg.exec(src))!=null) res[temp[1]]=decodeURIComponent(temp[2]);
    return res;
};
