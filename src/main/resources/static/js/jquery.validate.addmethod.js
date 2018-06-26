/*
 * Author: Brian.wang
 */

jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}/*, "请正确填写您的手机号码"*/);

jQuery.validator.addMethod("checkPhoneCode", function(value, element) {
    return checkPhoneCode();
}, "手机验证码不正确");

jQuery.validator.addMethod("requireUserName", function(value, element) {
    var length = value.length;
    return (length > 0);
}, "请填写用户名");