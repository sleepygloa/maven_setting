var s_userId = '';


////json Parameter 사용
//(function defaultAjaxSetting() {
//	$.ajaxSetup({
////		global: false,
//		timeout: 6000000,
//		type : "POST",
//		dataType : "json",
////		contentType: 'application/json; charset=utf-8',
//		cache: false,
//		beforeSend : function(xhr){
//    	},error : function(jqXHR, textStatus, errorThrown) {
//    		console.log(jqXHR);
//    		if(jqXHR.status != ""){
//    			if(jqXHR.status === 404 ) {
//    				alert("일시적이 오류가 발생했습니다.\n담장자에게 문의하세요.");
//    			}else if(jqXHR.status === 999 ) {
//    				alert(jqXHR.responseText);
//    				//TODO 로그인 POPUP창 개발후 출력
//    			}else if(jqXHR.status === 997 ) {
//    				var jsonData = $.parseJSON(jqXHR.responseText);
//    				alert(jsonData.errMsg);
//    			}else if(jqXHR.status === 1000 ) {
//    				alert(jqXHR.responseText);
//    			}
//    		}
//        }, complete : function(){
//        }, success : function(){
//        }
//	});
//})();

var MainJs = function(){
	"use strict";

//	var deviceHeight = $(window).height();
//	var deviceWidth = $(window).width();
//	$('.content-wrapper').css('height', deviceHeight - 52);
//	$('.content-wrapper').css('width', deviceWidth);
	
	return {
		init : function(){

			
			
//			getSession();
			
			getMenu();

//			getEvents();

		}
	}

	function getSession(){
		$.ajax({
			url		: '/login/getSession',
			success : function(data){
				if(data.s_userId == null){
					s_userId = undefined;
					console.log("not login");
					$('#headerLoginCircle').css('display', 'none');
					$('#headerLogoutCircle').css('display', 'block');
				}else{
					s_userId = data.s_userId;
					console.log("logined");
					$('#headerLoginCircle').css('display', 'block');
					$('#headerLogoutCircle').css('display', 'none');
				}
			}
			
		});
	};
	
	function getEvents(){
		
		
		
		$(document).on('click', 'a.nav-link', function(){
			
			var length = $(this).children().length;
			
			if(length == 3){
				var value = $(this).children('input').val();
				
				$('#sidebar').parent().removeClass('active');
				$.ajax({
					url  : "/main/toProgram",
					data : {
						proCd : value
					},
					type : "POST",
					success : function(data){
						$('#content_wrapper').empty();
						$('#content_wrapper').html(data);
					}
				});
			}
			
		});

		$('#headerLoginCircle').click(function(){
			alert('login');
			$.ajax({
				url 	: '/login/logout',
				async	: false,
				success : function(data){
//					$('#headerLoginCircle').css('display', 'none');
//					$('#headerLogoutCircle').css('display', 'block');
					window.location.href='/';
				}
			});
		});
		
		$('#headerLogoutCircle').click(function(){
			$.ajax({
				url 	: '/login/loadingLoginPg',
				async	: false,
				success : function(data){
					$('#content_wrapper').empty();
					$('#content_wrapper').html(data);
				}
			});
		});
		
	}
	
	function getMenu(){
		
		
		
        $.ajax({
            url      : "/ctrl/settings/menu/getMenu",	
//            data     : jsonData,
            dataType : 'json',
            type     : 'POST',
            contentType : 'application/json; charset=utf-8',
            success  : function(data) {
				console.log('1111');
//				$('#sidebarMenu').empty();
				//session
				console.log(data);
				
				
				//leftMenu
				var menuStr = '';
				var pIdx = 0;
				var pCnt = 0;
				var menuDataList = data.list;
				$.each(menuDataList, function(i, v){
					var menuData = menuDataList[i];
					if(pIdx != menuData.MENU_PARENT_SEQ){
						
						if(menuData.COUNT != 1){
							pIdx = menuData.MENU_PARENT_SEQ;
							menuStr += '<li class="nav-item">'
								+ '<a class="nav-link" data-toggle="collapse" href="#'+menuData.PRO_CD+'" aria-expanded="false" aria-controls="'+menuData.PRO_CD+'" style="padding:0px 0px 0px 13px;">'
								+ '<img src="images/icons/9.png" alt="">'
								+ '<span class="menu-title">'+menuData.MENU_NM+'<i class="fa fa-sort-down"></i></span>'
								+ '</a>'
								+ '<div class="collapse" id="'+menuData.PRO_CD+'">'
								+ '<ul class="nav flex-column sub-menu" style="padding:0px 0px 0px 15px;">';
							pCnt++;
						}else{
							menuStr += '<li class="nav-item">'
								+ '<a class="nav-link" data-toggle="collapse" href="#'+menuData.PRO_CD+'" aria-expanded="false" aria-controls="'+menuData.PRO_CD+'">'
								+ '<input type="hidden" value="'+menuData.PRO_CD+'" />'
								+ '<img src="images/icons/1.png" alt="">'
								+ '<span class="menu-title">'+menuData.MENU_NM+'</span>'
								+ '</a></li>'
						}
					}else{
						menuStr += '<li class="nav-item" style="margin:2.5px 0px;">'
							+'<a class="nav-link" href="#" style="padding:0px 0px 0px 13px;">'
							+'<input type="hidden" value="'+menuData.PRO_CD+'" />'
							+'<img src="'+menuData.MENU_ICO+'" alt="">'
							+'<span class="menu-title">'+menuData.MENU_NM+'</span>'
							+'</a>'
							+'</li>';
						pCnt++;
						if(pCnt == menuData.COUNT){
							menuStr += '</ul></div></li>';
							pCnt = 0;
						}
					}
					
				});

				$('#sidebarMenu').append(menuStr);
			}
		});
	}


}();

$(document).ready(function(){
	MainJs.init();
});

