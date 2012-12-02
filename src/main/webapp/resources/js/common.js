//设置cookies
function setCookie(name, value) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 365 * 24 * 60 * 60 * 1000 );
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();

}
//读取cookies
function getCookie(name) {
    var arr,
        reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) return unescape(arr[2]);
    else return null;

}
//删除cookies
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();

}
/*获取文档宽高*/
function GetBrowserDocument(){
    var _dcw = document.documentElement.clientHeight;
    var _dow = document.documentElement.clientHeight;
    var _bcw = document.body.clientHeight;
    var _bow = document.body.clienttHeight;

    if(_dcw == 0) return document.body;
    if(_dcw == _dow) return document.documentElement;

    if(_bcw == _bow && _dcw != 0)
        return document.documentElement;
    else
        return document.body;
}
function getObjWh(obj){
    var sl=document.documentElement.scrollLeft;//滚动条距左边的距
    var ch=document.documentElement.clientHeight;//屏幕的高度
    var st=document.documentElement.scrollTop;//滚动条距顶部的距离
    if(isSafari=navigator.userAgent.indexOf("Safari")>0) {
        st = document.body.scrollTop;
        sl = document.body.scrollLeft;
    }
    var cw=document.documentElement.clientWidth;//屏幕的宽度
    var objH=$("#"+obj).height();//浮动对象的高度
    var objW=$("#"+obj).width();//浮动对象的宽度
    var objT=Number(st)+(Number(ch)-Number(objH))/2;
    var objL=Number(sl)+(Number(cw)-Number(objW))/2;
    return objT+"|"+objL;
}
/*遮罩层*/
function maskBox(){
    var _h = GetBrowserDocument().scrollHeight;
    var _w = GetBrowserDocument().scrollWidth;
    $("body").append("<div id='mask'></div>");
    $("#mask").css({"height":_h,"width":_w});

}
/*选项卡*/
function Tabs(a, b) {
    $(a).click(function() {
        i = $(a).index(this);
        $(this).addClass("cur").siblings().removeClass("cur");
        for (var j = 0; j < $(a).length; j++) {
            $(b).eq(j).addClass("hide");
        }
        $(b).eq(i).removeClass("hide");
    });
}

function getContent(obj, companyId) {
    var top = $(obj).attr("topCount");
    var content;
    if(top) {
        content = $("#" + top + "_content_" + companyId);
    } else {
        content = $('#content_'+companyId)
    }
    return content;

}
function closeMsg(ele) {
    if(ele) {
        $("#"+ele).hide();
    }
}
function addbookmarksite(title, url) {
    if(arguments.length==0) {
        title = window.document.title;
        url=window.location.href;
    }
    if (document.all) {
        window.external.AddFavorite(url, title);
    } else if (window.sidebar) {
        window.sidebar.addPanel(title, url, "")
    } else {
        alert("对不起，您的浏览器不支持此操作!\n请您使用菜单栏或Ctrl+D收藏本站。")
    }

}
$(function() {
    //交易提示
    if($("#txtScroll")) {
        $("#txtScroll").jCarouselLite({
            btnPrev: "",
            btnNext: "",
            auto: 1500,
            speed: 800,
            visible:1,
            scroll:1,
            onMouse:true,
            vertical:true
        });
    }
    Tabs("#comment li", ".comment_con");
    Tabs("#pm li", ".pm");
    Tabs("#list_comment li",".comment_con");

    $(".city").hover(function() {
            $(this).addClass("hover");
        },
        function() {
            $(this).removeClass("hover");
        })
    $(".menu").each(function() {
        $(this).hover(function() {
                $(this).addClass("on");
            },
            function() {
                $(this).removeClass("on");
            })
    })
    $(".info,.pm_list").hover(
        function() {
            $(this).find(".tips").css("display","block");
        },
        function() {
            $(this).find(".tips").css("display","none");
        })
    //展开
    $(".cate_open").each(function() {
        $(this).toggle(
            function() {
                $(this).addClass("cate_close")
                    .attr("title","收起")
                    .parents(".category_expand")
                    .siblings("p")
                    .css({"height":"auto","white-space":"normal"});
            },
            function() {
                $(this).removeClass("cate_close")
                    .attr("title","展开")
                    .parents(".category_expand")
                    .siblings("p")
                    .css({"height":"20px","white-space":"nowrap"});
            }
        );
    })

    $(".list_seller").each(function() {
        var item = $(this).find(".server_item a");
        var box = $(this).find(".server_box");
        var con = $(this).find(".server_con");
        var up = $(this).find(".up");
        var list = $(this).find(".server_list");
        var lbtn = $(this).find(".l_btn");
        var rbtn = $(this).find(".r_btn");
        var serveitem = $(this).find(".server_item");
        var warp = $(this).find(".server_wrap");
        var info_con = $(this).find(".info_con");
        var openMoreTel = $(this).find(".open_more_tel");
        var open_server = $(this).find(".open_server");
        var parentDiv = $(this).find(".seller_con");
        openMoreTel.click(function() {
            var moretelH= $(this).parents(".more_phone");
            if(moretelH.height()==73) {
                moretelH.height("100%");
                $(this).text("收起");
            } else {
                $(this).text("更多分部");
                moretelH.height(73);
            }
        });
        if(serveitem.width()<warp.width()) {
            lbtn.css("display","none");
            rbtn.css("display","none");
        }
        $(this).mouseover(function() {
            if(up.css("display")=="none") {
                list.css("visibility","visible");
            }
        });
        //鼠标放上显示tab
        $(this).mouseleave(function() {
            if(up.css("display")=="none") {
                list.css("visibility","hidden");
            }
            if(up.css("display")=="block") {
                list.css("visibility","visible");
            }
        });
        if(box.offset()==null) {
            return false;
        }

        //var flag=0;
        item.bind({
            click:function() {
                i = item.index(this);
                item.removeClass("cur");
                item.eq(i).addClass("cur");
                get_content($(this), box);
                box.show();
                $(this).parents(".list_seller").css("border-color","#ffb7c1");
                $('html,body').animate({scrollTop: parentDiv.offset().top-17},500);
                up.fadeIn("fast").css("display","block");
                con.slideDown("slow");
            }
        });
        //点击商家名称和广告语
        open_server.each(function() {
            $(this).click(function() {
                if(con.css("display")=="block") {
                    con.slideUp("slow",function() {
                        up.fadeOut("fast");
                        $(this).parents(".list_seller").css("border-color","#fff");
                        $(this).parents(".recommend").find(".list_seller").css("border-color","#FFFDF4");
                        open_server.removeClass("on");
                    });
                } else {
                    var k = item.index(serveitem.find(".cur"));
                    if(k==-1) {
                        item.eq(0).click();
                    } else {
                        item.eq(k).click();
                    }
                    box.show();
                    $('html,body').animate({scrollTop: parentDiv.offset().top-17},500);
                    up.fadeIn("fast").css("display","block");
                    con.slideDown("slow");
                }
            });
        })
        //收回
        up.click(function() {
            con.slideUp("slow",function() {
                up.fadeOut("fast");
                $(this).parents(".list_seller").css("border-color","#fff");
                $(this).parents(".recommend").find(".list_seller").css("border-color","#FFFDF4");
            });
        });
        //向左滚动
        k=1;
        n=-1;
        rbtn.click(function() {
            if(!serveitem.is(":animated")) {
                lbtn.css("display","inline-block");
                serveitem.animate({
                    left:"-"+k*70+"px"
                },function() {
                    if(Math.abs(Number(serveitem.css("left").replace("px", "")))>serveitem.width()-630) {
                        rbtn.css("display","none");
                        k=1;
                    }
                    k++;
                });
                n=-1;
            }
        })
        //向右滚动
        lbtn.click(function() {
            if(!serveitem.is(":animated")) {
                rbtn.css("display","inline-block");
                serveitem.animate({
                    left:Number(serveitem.css("left").replace("px", ""))+70+"px"
                },function() {
                    if(Math.abs(Number(serveitem.css("left").replace("px", "")))==0) {
                        lbtn.css("display","none");
                        n=-1;
                    }
                    n++;
                });
                k=1;
            }
        });
    })
    //左侧快捷导航
    $(".side_nav").hover(function() {
            $(this).addClass("hover");
        },
        function() {
            $(this).removeClass("hover");
        });

    //最新咨询
    if($("#consult")[0]) {
        $("#consult").jCarouselLite({
            btnPrev: "",
            btnNext: "",
            auto: 1500,
            speed: 800,
            visible:9,
            scroll:1,
            onMouse:true,
            vertical:true
        });
    }

})