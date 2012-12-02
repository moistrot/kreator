$(document).ready(function () {
    var refer = document.referrer;
    try {
        var r = new Date().getTime();
        $.ajax({
            type:"get",
            url:"/resources/images/1x1.gif",
            data:"r=" + r + "&ure=" + refer + "&test_src=" + testSrc,
            success:function (htm) {}
        });
    } catch (ex) {

    }
});