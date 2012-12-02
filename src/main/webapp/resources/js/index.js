function ad(i) {
	$("#nums a").eq(i).addClass("cur").siblings("a").removeClass("cur");
	var imgh = 300;
	$("#flashAd").find("ul").animate({
		marginTop:"-"+i*imgh+"px"
	})
}

//显示收藏弹出层
function showFavBox(elem) {
    var objV=getObjWh(elem);
    var tbT=objV.split("|")[0]+"px";
    var tbL=objV.split("|")[1]+"px";
    $("#"+elem).css({top:tbT,left:tbL});
    var fn = function() {
        var objV=getObjWh(elem);
        var tbT=objV.split("|")[0]+"px";
        var tbL=objV.split("|")[1]+"px";
        $("#"+elem).css({top:tbT,left:tbL});
    }
    $(window).bind({
        scroll:fn,
        resize:fn
    })
}
var time_start = new Date().getTime();
var stayTime=0;
function timeTotal (){
    var time_now = new Date();
    stayTime = Math.round((time_now.getTime() - time_start)/1000);
    var total = window.setTimeout('timeTotal()',1000);
    if(stayTime==60&&getCookie("ff_index")!="yes") {
        showFavBox("favBox");
        maskBox();
        clearTimeout(total);
    }
}

//焦点图

function flashAd() {
	var star=0;
	var len = $("#flashAd").find("li").length;
	var num="";
	for(var i=0;i<len;i++) {
		num+='<a href="javascript:void(0)">'+Number(i+1)+'</a>';
	}
	$("#nums").html(num);
	$("#nums a:eq(0)").addClass("cur");
	$("#nums a").click(function(){
		sw = $("#nums a").index(this);
		ad(sw);
	});
	var myTime = setInterval(function(){
		ad(star)
		star++;
		if(star==len){star=0;}
	} , 3000);
	$("#flashAd").hover(function(){
	if(myTime){
	   clearInterval(myTime);
	}
	},function(){
		myTime = setInterval(function(){
	  	ad(star)
	  	star++;
	  	if(star==len){star=0;}
		} , 3000);
	});
}
//回到顶部
function Top() {
	$("html, body").animate({
		scrollTop : 0
	}, 120);
}
//回到顶部按钮
function gotoTop() {
	var fn = function() {
		var st = $(document).scrollTop();
		var winh = $(window).height();
		$(".home,.suggest,.addfav").css("right",($(window).width()-956)/2-50);
		(st > 150) ? $(".home").fadeIn() : $(".home").fadeOut();
		if (!window.XMLHttpRequest) {
			 $(".home").css("top", st + winh - 220);
             $(".addfav").css("top", st + winh - 150);
			 $(".suggest").css("top", st + winh - 150);

		}
	};
	$(window).bind("load", fn);
	$(window).bind("scroll", fn);
	$(window).bind("resize", fn);
}
$(function() {
	flashAd();
	gotoTop();
	$("#recruit").jCarouselLite({
			btnPrev: ".scroll_l",
			btnNext: ".scroll_r",
			auto: false,
			speed: 300,
			visible:4,
			scroll:1,
			onMouse:true,
			vertical:false
   		});
   	$("#slider").jCarouselLite({
			btnPrev: ".prevBtn",
			btnNext: ".nextBtn",
			auto: false,
			speed: 300,
			visible:1,
			scroll:1,
			onMouse:true,
			vertical:false
   		});
   	//黄金眼广告鼠标放上按钮效果
   	$(".sideAd").hover(function() {
   		$(".prevBtn,.nextBtn").show();
   	},function() {
   		$(".prevBtn,.nextBtn").hide();
   	});
   	//最受关注商家效果
   	$(".attention").each(function() {
   		var lis = $(this).find("li");
   		lis.mouseover(function() {
   			$(this).addClass("cur").siblings().removeClass("cur");
   		})
   	})
    timeTotal();
})