// ----------------------- GLOBAL VARS ----------------------- \\
var current_screen = "lock_screen";
var scrolled=0;
var unlock=0;
var timeOut = 0;
var current_menu_selected = 0;
var playing_num = 0;
var help_var = 0;
var playing = ["Palco Mundo: Darude - Sandstorm","Palco Optimus: Ana Malhoa - Turbinada","Palco Magic: Quim Barreiros - Cabritinha","Palco Green: Hardwell - Young Again"];
// VARIAVEIS DA SHOP
var prices = [[1.50,2.00,3.50,5.00,2.50,2.00], [1.00,1.00,1.50,2.00,4.50,4.50] ];
var amount = [[0,0,0,0,0,0],[0,0,0,0,0,0]];
var history_amount = [[0,0,0,0,0,0],[0,0,0,0,0,0]];
var go_pick = 0;
var get_pick = 0;
var shopping = 0;
var fullscreen= 0;
// VARIAVEIS DOS ECRAS
var click = ["compras","go-map","share-photo","see-photo","camera-icon","compras-comprar","compras-historico","remove-failed","social-remove-confirmation","compras-entrega","social-add-sucess","compras-confirmacao","compras-sucesso","compras-cancelar","mapa","mapa-search","social-add-confirmation","mapa-map","friends","buildings","next-buildings-list","confirm-gps","gps-cancelar","temporary-unavailable","social","social-friends","social-add","social-list-friends","social-friend-profile","mapa-direction","mapa-friends","back-search","social-messages","social-feed","check-friend","social-myprofile","localate"];
var clickables = new Array();
clickables["lock_screen"] = ["lock_screen","lock_screen"];
clickables["main_screen"] = ["main_screen","main_screen"];
clickables["compras"] = ["main_screen","shop_screen"];
clickables["compras-comprar"] = ["shop_screen","shop_screen1"];
clickables["compras-historico"] = ["shop_screen","shop_screen2"];
clickables["compras-entrega"] = ["shop_screen1","shop_screen5"];
clickables["compras-confirmacao"] = ["shop_screen4","shop_sucess_screen"];
clickables["compras-sucesso"] = ["shop_screen5","shop_screen4"];
clickables["compras-cancelar"] = ["main_screen","cancelled_screen"];
clickables["mapa"] = ["main_screen","gps_menu"];
clickables["mapa-friends"] = ["gps_search","gps_search_friends"];
clickables["back-search"] = ["gps_menu","gps_search"];
clickables["mapa-search"] = ["gps_menu","gps_search"];
clickables["mapa-map"] = ["gps_menu","gps_map"];
clickables["friends"] = ["gps_search","gps_search_friends"];
clickables["buildings"] = ["gps_search","gps_search_buildings"];
clickables["next-buildings-list"] = ["gps_search_buildings","gps_search_stands"];
clickables["confirm-gps"] = ["gps_search_stands","gps_confirm"];
clickables["gps-cancelar"] = ["main_screen","gps_cancelled_screen"];
clickables["temporary-unavailable"] = ["main_screen","temporary_unavailable"];
clickables["social"] = ["main_screen","social_screen"];
clickables["social-friends"] = ["social_screen","social_friends_screen"];
clickables["social-add"] = ["social_friends_screen","add_friend_screen"];
clickables["social-list-friends"] = ["social_friends_screen","list_friend_screen"];
clickables["social-friend-profile"] = ["list_friend_screen","friend_profile"];
clickables["social-messages"] = ["social_screen","choose_friend_message"];
clickables["social-feed"] = ["social_screen","friends_feed"];
clickables["social-myprofile"] = ["social_friends_screen","my_profile"];
clickables["social-message-friend"] = ["choose_friend_message","friend_message"];
clickables["localate"] = ["shop_sucess_screen","gps_map"];
clickables["check-friend"] = ["gps_search_friends","gps_map"];
clickables["social-add-confirmation"] = ["add_friend_screen","friend_add_confirmation"];
clickables["social-add-sucess"] = ["social_friends_screen","add_friend_sucess"];
clickables["social-remove-confirmation"] = ["friend_profile","friend_remove_confirmation"];
clickables["remove-failed"] = ["social_friends_screen","temporary_unavailable"];
clickables["camera-icon"]=["main_screen","camera"];
clickables["see-photo"]=["friends_feed","see_photo"];
clickables["share-photo"]=["social_screen","friends_feed"];
clickables["share-foto"]=["social_screen","friends_feed"];
clickables["go-map"]=["friend_profile","gps_map"];

// VARIAVEIS DO GPS
var building_names = ["Bancas","Bares","Palcos","WC's"];
var building_selected = [0,0,0,0];
var bar_names =["AfterParty","Carlsberg","RedBull","Superbock"];
var bar_selected = [0,0,0,0];
var stage_names =["Mundo","Green","Optimus","Superbock"];
var stage_selected = [0,0,0,0];
var wc_names =["Wc Amarelo","Wc Azul","Wc Branco","Wc Laranja"];
var wc_selected = [0,0,0,0];
var banca_names =["Control","Fnac","MegaHits","Vodafone"];
var banca_selected = [0,0,0,0];
var current_building_selected = "";
var current_x_selected = "";
var maps_bancas = ["control_map.gif","control_map.gif","control_map.gif","control_map.gif"];
var maps_bar = ["control_map.gif","control_map.gif","control_map.gif","control_map.gif"];
var maps_stage = ["control_map.gif","control_map.gif","control_map.gif","control_map.gif"];
var maps_wc = ["control_map.gif","control_map.gif","control_map.gif","control_map.gif"];
var friend_selected = [0,0,0];
// VARIAVEIS SOCIAL
var friends=["Gonçalo","Pedro","João"];
var age=[19,19,19];
var local=["Sintra","Santarém","Lisboa"];
var photo=["goncalo.png","pedro.png","jota.png"];
var current_profile=0;
var binary_friends=[1,1,1,0,0,0];
var friends_array=[ {id:"goncalo", name:"Gonçalo", surname:"Fialho", age:19,local:"Sintra",at:"Bar Superbock",  photo:"goncalo.png",messages:""},
                    {id:"pedro", name:"Pedro",   surname:"Santos", age:19,local:"Santarém",at:"Bar RedBull",photo:"pedro.png",  messages:""},
                    {id:"joao",name:"João",    surname:"Pedro",  age:19,local:"Lisboa",at:"Banca Control",  photo:"jota.png",   messages:""},
                    {id:"andreia",name:"Andreia", surname:"Matias", age:21,local:"Porto",at:"Palco Mundo",   photo:"andreia.png",   messages:""},
                    {id:"rita",name:"Rita", surname:"Mendes", age:20,local:"Aveiro",at:"Banca MegaHits",   photo:"rita.png",   messages:""},
                    {id:"sofia",name:"Sofia", surname:"Ribeiro", age:18,local:"Faro",at:"Palco Magic",   photo:"sofia.png",   messages:""}];
                    
var current_friends=[friends_array[0],friends_array[1],friends_array[2]];
var current_list_user = 0;

// VARIAVEIS MENSAGENS
var current_message_user = 0;
var message_group = [0,0,0,0];
var message_group1 = [0,0,0,0,0];
var message_text_group1 = ["Olá","Adeus","Beijinho","Abraço","Obrigado"];
var message_group2 = [0,0,0,0,0];
var message_text_group2 = ["Sff","Traz-me comida","Vem ter comigo","Dá-me boleia","Liga-me"];
var message_group3 = [0,0,0,0,0];
var message_text_group3 = ["Tudo bem?","Onde estás?","Posso ir aí?","Demoras muito?","Estás a gostar?"];
var message_group4 = [0,0,0,0];
var message_text_group4 = ["Sim","Não","Ok","Talvez"];
var message_text = [message_text_group1,message_text_group2,message_text_group3,message_text_group4];
var message_selected = [message_group1,message_group2,message_group3,message_group4];
var message_friends = [[""],[""],[""]];
var send_to = 0;

//FEED TEXTS
var feels = ["A sentir-se","A ver","A comer","A beber"];
var feelings = ["Feliz","Bem","Contente","Cansado","Na boa","Rebelde"];
var watching = ["Ana Malhoa","Darude","Hardwell","Ivete Sangalo","Metallica","Miley Cyrus"];
var eating = ["Batatas","Bolachas","Burguer","Cachorro","Pipocas","Snack"];
var drinking = ["Água","Café","Cerveja","Coca-Cola","Laranjada","Limonada"];
var all_feels = [feelings,watching,eating,drinking];
var feels_binary = [0,0,0,0];
var all_feels_binary = [[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0],[0,0,0,0,0,0]];
var feed_text = "";

//FEED PHOTOS
var photo_choosed = 0;

/*
// MAPA
var el = document.getElementById('draggableElement');
var leftEdge = el.parentNode.clientWidth - el.clientWidth;
var topEdge = el.parentNode.clientHeight - el.clientHeight;
var dragObj = new dragObject(el, null, new Position(leftEdge, topEdge), new Position(0, 0));

*/
// REFRESH FEED
function refresh(){
    var state_or_photo = Math.floor((Math.random() * 100) + 1) -1;
    if(state_or_photo < 70){
        var i = Math.floor((Math.random() * current_friends.length) + 1) -1;
        var random_feel = Math.floor((Math.random() * feels.length) + 1) -1;
        var text_feel = Math.floor((Math.random() * all_feels_binary[random_feel].length) + 1) -1;
        var text='<div id="'+current_friends[i].id+'" class="friend"><img class="friend_icon" src="images/'+current_friends[i].photo+'"><div class="search_name">'+current_friends[i].name+' '+current_friends[i].surname+'</div><div class="feeling">'+feels[random_feel]+' '+all_feels[random_feel][text_feel]+'</div></div>';
        text+=feed_text;
        feed_text = text;
        $('.friends_feed').empty();
        $('.friends_feed').append(text);
    }else{
        var j = Math.floor((Math.random() * current_friends.length) + 1) -1;
        var random_photo = Math.floor((Math.random() * 5) + 1) ;
        var text_photo ='<div onclick="generate_photo_screen('+'\''+current_friends[j].photo+'\''+','+'\''+current_friends[j].name+' '+current_friends[j].surname +'\''+','+'\''+ random_photo+'\''+');" id="'+current_friends[j].id+'" class="friend"><img class="friend_icon" src="images/'+current_friends[j].photo+'"><div class="search_name">'+current_friends[j].name+'</div><div class="feeling_photo">Ver Foto</div><img class="photo_icon" src="images/photo'+random_photo+'.jpg"></div>';
        text_photo+=feed_text;
        feed_text = text_photo;
        $('.friends_feed').empty();
        $('.friends_feed').append(text_photo);
    }
}

function data(){
    var today=new Date();
    var h=today.getHours();
    var m=today.getMinutes();
    var d = new Date();
    var options = { weekday: 'short', month: 'long', day: '2-digit' };
    document.getElementById("date").innerHTML = d.toLocaleDateString('pt', options);
}
window.setInterval(function(){
    refresh();
    data();
},10000);

// CAROUSSEL
(function($j) {

    var $jdescriptions = $j('#carousel-descriptions').children('li'),
	$jcontrols = $j('#carousel-controls').find('span'),
	$jcarousel = $j('#carousel')
		.roundabout({childSelector:"img", minOpacity:1, autoplay:false })
		.on('focus', 'img', function() {
			var slideNum = $jcarousel.roundabout("getChildInFocus");
			
			$jdescriptions.add($jcontrols).removeClass('current');
			$j($jdescriptions.get(slideNum)).addClass('current');
			$j($jcontrols.get(slideNum)).addClass('current');
        });

    $jcontrols.on('click dblclick', function() {
	    var slideNum = -1,
		i = 0, len = $jcontrols.length;
	    for (; i<len; i++) {
    		if (this === $jcontrols.get(i)) {
    			slideNum = i;
    			break;
    		}
	    }
	    if (slideNum >= 0) {
    		$jcontrols.removeClass('current');
    		$j(this).addClass('current');
	        $jcarousel.roundabout('animateToChild', slideNum);
	    }
    });

}(jQuery));

// CAROUSSEL1
(function($j) {

    var $jdescriptions1 = $j('#carousel1-descriptions').children('li'),
	$jcontrols1 = $j('#carousel1-controls').find('span'),
	$jcarousel1= $j('#carousel1')
		.roundabout({childSelector:"img", minOpacity:1, autoplay:false })
		.on('focus', 'img', function() {
			var slideNum = $jcarousel1.roundabout("getChildInFocus");
			
			$jdescriptions1.add($jcontrols1).removeClass('current');
			$j($jdescriptions1.get(slideNum)).addClass('current');
			$j($jcontrols1.get(slideNum)).addClass('current');
        });

    $jcontrols1.on('click dblclick', function() {
	    var slideNum = -1,
		i = 0, len = $jcontrols1.length;
	    for (; i<len; i++) {
    		if (this === $jcontrols1.get(i)) {
    			slideNum = i;
    			break;
    		}
	    }
	    if (slideNum >= 0) {
    		$jcontrols1.removeClass('current');
    		$j(this).addClass('current');
	        $jcarousel1.roundabout('animateToChild', slideNum);
	    }
    });

}(jQuery));

// CAROUSSEL2
(function($j) {

    var $jdescriptions1 = $j('#carousel2-descriptions').children('li'),
	$jcontrols1 = $j('#carousel2-controls').find('span'),
	$jcarousel1= $j('#carousel2')
		.roundabout({childSelector:"img", minOpacity:1, autoplay:false })
		.on('focus', 'img', function() {
			var slideNum = $jcarousel1.roundabout("getChildInFocus");
			
			$jdescriptions1.add($jcontrols1).removeClass('current');
			$j($jdescriptions1.get(slideNum)).addClass('current');
			$j($jcontrols1.get(slideNum)).addClass('current');
        });

    $jcontrols1.on('click dblclick', function() {
	    var slideNum = -1,
		i = 0, len = $jcontrols1.length;
	    for (; i<len; i++) {
    		if (this === $jcontrols1.get(i)) {
    			slideNum = i;
    			break;
    		}
	    }
	    if (slideNum >= 0) {
    		$jcontrols1.removeClass('current');
    		$j(this).addClass('current');
	        $jcarousel1.roundabout('animateToChild', slideNum);
	    }
    });

}(jQuery));


// ----------------------- SWIPE ----------------------- \\

$(function() {
    $("#food").dragOn();
    $("#drink1").dragOn();
    $("#confirmation").dragOn();
    $("#history_screen").dragOn();
    $(".buildings-list").dragOn();
    $(".friends_container").dragOn();
    $("#ex2").dragOn();
    $(".message_container").dragOn();
    $(".default1").dragOn();
    $('.help_text').dragOn();
    $('.friends_feed').dragOn();
    $('#list_feelings_screen2').dragOn();
  /*  $("#buildings-list0").dragOn();
    $("#buildings-list1").dragOn();
    $("#buildings-list2").dragOn();
    $("#buildings-list3").dragOn();*/
});

// ----------------------- CLICKS ----------------------- \\
$(function(){

    for(var i in click){
        (function(i,click,clickables){
        $("#"+click[i]).click('click',function() {
            if(click[i]=='compras'){
                if(current_menu_selected==0){
                    $("#"+clickables[click[i]][0]).stop().hide(350,function() {
                        $("#"+clickables[click[i]][1]).css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                    current_screen=click[i];
                    console.log(click[i]); 
                    $('.help_screen').hide();
                    help_var=0;
                }else{
                    current_menu_selected=0;
                }
            }else if(click[i]=='social'){
                if(current_menu_selected==1){
                    $("#"+clickables[click[i]][0]).stop().hide(350,function() {
                        $("#"+clickables[click[i]][1]).css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                    current_screen=click[i];
                    console.log(click[i]); 
                    $('.help_screen').hide();
                    help_var=0;
                }else{
                    current_menu_selected=1;
                }
            }else if(click[i]=='mapa'){
                if(current_menu_selected==2){
                    $("#"+clickables[click[i]][0]).stop().hide(350,function() {
                        $("#"+clickables[click[i]][1]).css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                    current_screen=click[i];
                    console.log(click[i]); 
                                        $('.help_screen').hide();
                    help_var=0;
                }else{
                    current_menu_selected=2;
                }
            }else if(click[i]=='camera-icon'){
                if(current_menu_selected==3){
                    $("#"+clickables[click[i]][0]).stop().hide(350,function() {
                        $("#"+clickables[click[i]][1]).css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                    go_camera();
                    current_screen=click[i];
                    console.log(click[i]); 
                    $('.help_screen').hide();
                    help_var=0;
                }else{
                    current_menu_selected=3;
                }
            }else{
            if ($('.you_sure').css('display') == 'none'){
                $("#"+clickables[click[i]][0]).stop()
                    .hide(350,function() {
                        $("#"+clickables[click[i]][1]).css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                current_screen=click[i];
                console.log(click[i]); 
                $('.help_screen').hide();
                help_var=0;
                }
            }
        });
        }(i,click,clickables));

    }
});


// ------------------ TOUCH SWIPE --------------------- \\
/*
$(function() {
  $(".screen").swipe( {
    //Generic swipe handler for all directions
    swipe:function(event, direction, distance, duration, fingerCount, fingerData) {
        if(direction=="up"){
         //   scroll_down($("#food"),distance);
         //   scroll_down($("#drink"),distance);
        //    scroll_down($("#confirmation"),distance);
            scroll_down($("#history_screen"),distance);
            scroll_down($("#buildings-list"),distance);
            scroll_down($("#buildings-lists0"),distance);
            scroll_down($("#buildings-lists1"),distance);
            scroll_down($("#buildings-lists2"),distance);
            scroll_down($("#buildings-lists3"),distance);
            
        }
        if(direction=="down"){
         //   scroll_up($("#food"),distance);
         //   scroll_up($("#drink"),distance);
         //   scroll_up($("#confirmation"),distance);
            scroll_up($("#history_up"),distance);
            scroll_up($("#buildings-list"),distance);
            scroll_up($("#buildings-lists0"),distance);
            scroll_up($("#buildings-lists1"),distance);
            scroll_up($("#buildings-lists2"),distance);
            scroll_up($("#buildings-lists3"),distance);
        }
    },
    //Default is 75px, set to 0 for demo so any distance triggers swipe
     threshold:10
  });
});
*/



//Palco
window.setInterval(function() {
    if(playing_num==4){
        playing_num=0;
    }
    $('#playing_song').text(playing[playing_num]);
    playing_num++;
}, 10000);

$("#unlock").mousedown(function () {
    clearTimeout(timeOut);
    
    
    timeOut = setTimeout(function () {
        
    var $p = $("#lock_screen");
    $p.stop()
      .css("background-color","black")
      .hide(500, function() {
            $("#main_screen").css("background-color","black")
                .show(400);
                current_screen = "main_screen";
          
      });
    unlock = 1;
    }, 300);

});

$("#lock").click(function(){
    if(current_screen!="lock_screen")
        lock_screen();
});





    $("#cancelled_screen").hide();
    $("#main_screen").hide();
    $("#shop_screen").hide();
    $("#shop_screen1").hide();
    $("#shop_screen2").hide();
    $("#shop_screen3").hide();
    $("#shop_screen4").hide();
    $("#shop_screen5").hide();
    $("#shop_sucess_screen").hide();
    $(".you_sure").hide();
    $("#gps_menu").hide();
    $("#gps_search").hide();
    $("#gps_map").hide();
    $("#gps_search_buildings").hide();
    $("#gps_search_stands").hide();
    $("#next-buildings-list").hide();
    $(".map_container").hide();
    $("#gps_confirm").hide();
    $("#gps_cancelled_screen").hide();
    $("#temporary_unavailable").hide();
    $("#social_screen").hide();
    $("#social_friends_screen").hide();
    $("#add_friend_screen").hide();
    $("#list_friend_screen").hide();
    $("#friend_profile").hide();
    $("#friend_message").hide();
    $("#gps_search_friends").hide();
    $("#my_profile").hide();
    $(".list_messages").hide();
    $("#list_messages_screen4").hide();
    $("#list_messages_screen2").hide();
    $("#list_messages_screen3").hide();
    $("#list_messages_screen5").hide();
    $("#choose_friend_message").hide();
    $("#drink1").hide();
    $('.help_screen').hide();
    $('#friend_add_confirmation').hide();
    $('#add_friend_sucess').hide();
    $('#friend_remove_confirmation').hide();
    $('#remove_sucess').hide();
    $('.list_feelings').hide();
    $('#friends_feed').hide();
    $(".share").hide();
    $('#camera').hide();
    $('#see_photo').hide();
    
    
function help(){
    if(help_var==0){
        $('.help_screen').fadeIn(150);
        help_var=1;
    }else{
        $('.help_screen').fadeOut(150);
        $('.help_screen').hide();
        help_var=0;
    }
}


function lock_screen(){
    if ($('.you_sure').css('display') == 'none' && current_screen!="lock_screen"){
        $("#"+ clickables[current_screen][1]).stop().hide(400, function() {
          $("#lock_screen").show(400);
        });
        current_screen = "lock_screen";
    }
}
    

function return_back(){
    var $p = $("#"+clickables[current_screen][1]);
    if(help_var==1){
        $('.help_screen').hide();
        help_var=0;
        return 1;
    }
    if(current_screen=="compras-confirmacao"){
        return 1;
    }
    if($('.list_feelings').css('display')!='none'){
        $('.list_feelings').fadeOut(500);
        return 1;
    }
    if(current_screen=="remove-sucess"){
        $("#remove_sucess").stop()
            .hide(350,function() {
                $("#social_friends_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
            });
    }
    if ($('.you_sure').css('display') == 'none' && $('.list_messages').css('display') == 'none' && current_screen!="main_screen" && current_screen!="lock_screen" && current_screen!="mapa-direction"){
        $p.stop().hide(350,function() {
            $("#"+clickables[current_screen][0]).show(350);
        });
        console.log("#"+clickables[current_screen][0]);
        $(".map_container").hide();
        $p.promise().done(function(){
            $('.help_screen').hide();
            help_var=0;
            for(var i in Object.keys(clickables) ){
                var po =Object.keys(clickables);
                if(clickables[current_screen][0] == clickables[po[i]][1]){
                    current_screen = po[i];
                    break;
                }
            }
        });
    }
}

function scroll_down($div,distance){
    scrolled=scrolled+distance;
	$div.animate({
	        scrollTop:  scrolled
	   },800);

}
function scroll_reset($div){
    scrolled=0;
    $div.animate({
        scrollTop:scrolled
    },800);
}


function scroll_up($div,distance){
    scrolled=scrolled-distance;
	$div.animate({
	        scrollTop:  scrolled
    }, 800);
    
}
    
    

function addFoodItem(id){
    var value = parseInt($("#itemFood"+id+" > .quantity").text(), 10) + 1;
    if(value<20){
        $("#itemFood"+id+" > .quantity").text(value);    
        amount[0][id-1] = value;
    }
}

function removeFoodItem(id){
    var value = parseInt($("#itemFood"+id+" > .quantity").text(), 10) - 1;
    if(value!=-1){
        $("#itemFood"+id+" > .quantity").text(value);  
        amount[0][id-1] = value;
    }
}

function addItem(id){
    var value = parseInt($("#item"+id+" > .quantity").text(), 10) + 1;
    if(value<20){
        $("#item"+id+" > .quantity").text(value);   
        amount[1][id-1] = value;
    }
}

function removeItem(id){
    var value = parseInt($("#item"+id+" > .quantity").text(), 10) - 1;
    if(value!=-1){
        $("#item"+id+" > .quantity").text(value);   
        amount[1][id-1] = value;
    }
}

function price_text(){
    var price = 0.00;
    for(var i = 0; i < 2; i++){
        for(var j=0; j<6;j++){
            price+= amount[i][j]*prices[i][j];
        }
    }
    $("#price3").text(price.toFixed(2)+"€");
    if(price==0){
        $("#esconde").hide();
        $(".didnt-select").show();
    }else{
        $(".didnt-select").hide();
        $("#esconde").show();
        $('#compras-confirmacao').hide();
    }
}

function price_text1(){
    var price = 0.00;
    for(var i = 0; i < 2; i++){
        for(var j=0; j<6;j++){
            price+= amount[i][j]*prices[i][j];
        }
    }
    $("#price4").text(price.toFixed(2)+"€");
}


function list_products_requested(){
    var text ="";
    $(".item_confirmation_box").remove();
    for(var i = 0; i < 2; i++){
        for(var j=0; j<6;j++){
            if(amount[i][j]!=0)
                if(i==0){
                    text = '<div class="item_confirmation_box" id="citemFood'+(j+1)+'"> <div class="quantity_confirmation">' + amount[i][j].toString() + '</div><div class="name_confirmation">' + $("#itemFood"+(j+1)+" > .name").text() + '</div><div class="price_confirmation">' + amount[i][j]*prices[i][j] + "€</div></div>";
                    $('.confirmation').append(text);  
                    text = "";
                }else if(i==1){
                    text = '<div class="item_confirmation_box" id="citemDrink'+(j+1)+'"> <div class="quantity_confirmation">' + amount[i][j].toString() + '</div><div class="name_confirmation">' + $("#item"+(j+1)+" > .name").text() + '</div><div class="price_confirmation">' + amount[i][j]*prices[i][j] + "€</div></div>";
                    $('.confirmation').append(text);  
                    text = "";
                }
        }
    }
}

function go_pick1(){
    if(go_pick==0){
        if(get_pick==1){
            $("#pick_here").css("border-style", "none");
            get_pick=0;
            $('#compras-confirmacao').hide();
        }
        $("#pick_store").css("border-style", "solid");
        $("#pick_store").css("border-radius", "20%");
        $("#pick_store").css("border-color", "white");
        $("#pick_store").css("border-width", "1px");
        go_pick=1;
        $('#compras-confirmacao').show();
        $('#ir_buscar').show();
        $('#maos').hide();
    }else{
        $("#pick_store").css("border-style", "none");
        go_pick=0;
        $('#compras-confirmacao').hide();
    }
}
function get_pick1(){
    if(get_pick==0){
        if(go_pick==1){
            $("#pick_store").css("border-style", "none");
            go_pick=0;
        }
        $("#pick_here").css("border-style", "solid");
        $("#pick_here").css("border-radius", "20%");
        $("#pick_here").css("border-color", "white");
        $("#pick_here").css("border-width", "1px");
        get_pick=1;
        $('#compras-confirmacao').show();
        $('#ir_buscar').hide();
        $('#maos').show();
    }else{
        $("#pick_here").css("border-style", "none");
        get_pick=0;
        $('#compras-confirmacao').hide();
    }
}


function check_method(){
    if(go_pick==0 && get_pick==1){
        
    }
}

function show_yousure(x){
    $('.you_sure').fadeIn(500);
    
}
function hide_yousure(x){
    $('.you_sure').fadeOut(500);
    $('.you_sure').hide();
}

function shop_canceled(x){
    shopping=0;
    
    for(var i =0; i<6 ;i++){
        amount[0][i] = 0;
        $("#itemFood"+i+" > .quantity").text('0');  
    }
    for( i =0; i<6 ;i++){
        amount[1][i] = 0;
        $("#item"+i+" > .quantity").text('0');  
    }
    go_pick=0;
    get_pick=0;
    $("#pick_here").css("border-style", "none");
    $("#pick_store").css("border-style", "none");

    $('#'+x+' > .you_sure').hide();
    $('#'+x).hide(350,function(){
        $("#cancelled_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
    });
    current_screen="compras-cancelar";
    $('.you_sure').hide();
}


function reset_foodquantity(){
    for(var i =0; i<6 ;i++){
        history_amount[0][i] = history_amount[0][i] + amount[0][i];
        amount[0][i] = 0;
        $("#itemFood"+i+" > .quantity").text('0');  
    }
    for( i =0; i<6 ;i++){
        history_amount[1][i] = history_amount[1][i] + amount[1][i];
        amount[1][i] = 0;
        $("#item"+i+" > .quantity").text('0');  
    }
    go_pick=0;
    get_pick=0;
    $("#pick_here").css("border-style", "none");
    $("#pick_store").css("border-style", "none");
    
}

function go_main(x){
    if(shopping==1)
        show_yousure(1);
    if(current_screen=="remove-sucess"){
        $("#remove_sucess").stop().hide(350,function() {
            $("#social_friends_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
        });
        current_screen="social-friends";
        return 1;
    }
    if ($('.you_sure').css('display') == 'none' && current_screen!="lock_screen" && current_screen!="main_screen"){
        x = clickables[current_screen][1];
        $('#'+x).hide(350,function(){
            $("#main_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
        });
        current_screen="main_screen";
    }
}

function sucess_shop(){
    reset_foodquantity();
    current_screen="main_screen";
    $('#shop_sucess_screen').hide(350,function(){
        $("#main_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
    });
    
}

function update_shoplist(){
    var text ="";
    $(".item_history_box").remove();
    for(var i = 0; i < 2; i++){
        for(var j=0; j<6;j++){
            if(history_amount[i][j]!=0)
                if(i==0){
                    text = '<div class="item_confirmation_box" id="citemFood'+(j+1)+'"> <div class="quantity_confirmation">' + history_amount[i][j].toString() + '</div><div class="name_confirmation">' + $("#itemFood"+(j+1)+" > .name").text() + '</div><div class="price_confirmation">' + history_amount[i][j]*prices[i][j] + "€</div></div>";
                    $('.history_screen').append(text);
                    text = "";
                }else if(i==1){
                    text = '<div class="item_confirmation_box" id="citemDrink'+(j+1)+'"> <div class="quantity_confirmation">' + history_amount[i][j].toString() + '</div><div class="name_confirmation">' + $("#item"+(j+1)+" > .name").text() + '</div><div class="price_confirmation">' + history_amount[i][j]*prices[i][j] + "€</div></div>";
                    $('.history_screen').append(text);  
                    text = "";
                }
        }
    }
    var price = 0.00;
    for(i = 0; i < 2; i++){
        for(j=0; j<6;j++){
            price+= history_amount[i][j]*prices[i][j];
        }
    }
    $("#total").text(price.toFixed(2)+"€");
}

function select_building(x){
    $("#next-buildings-list").show();
    if(building_selected[x-1]==1){
        $("#building"+(x)).css("border-style", "none");
        $("#next-buildings-list").hide();
        building_selected[x-1]=0;
        return 1;
    }
    for(var i=0; i<4; i++){
        $("#building"+(i+1)).css("border-style", "none");
        building_selected[i] = 0;
    }
    building_selected[x-1] = 1;
    $("#building"+x).css("border-style", "solid");
    $("#building"+x).css("border-width", "1px");
    $("#building"+x).css("border-radius", "20%");
    current_building_selected = building_names[x-1];
}

function reset_buildingtype(){
    for (var i = 0; i < 4; i++) {
        building_selected[i] = 0;
        $("#building"+(i+1)).css("border-style", "none");
    }
    $("#next-buildings-list").hide();
}

function building_type(){
    for (var i = 0; i < 4; i++) {
        $("#buildings-lists"+(i)).hide();
        if(building_selected[i]==1)
            $("#buildings-lists"+(i)).show();
    }

    
}

function select_banca(x){
    $("#confirm-gps").show();
    if(banca_selected[x-1]==1){
        $("#banca"+(x)).css("border-style", "none");
        $("#confirm-gps").hide();
                banca_selected[x-1]=0;
        return 1;
    }
    for(var i=0; i<4; i++){
        $("#banca"+(i+1)).css("border-style", "none");
        banca_selected[i] = 0;
    }
    banca_selected[x-1] = 1;
    $("#banca"+x).css("border-style", "solid");
    $("#banca"+x).css("border-width", "1px");
    $("#banca"+x).css("border-radius", "20%");
    current_x_selected = banca_names[x-1];
    
}


function select_bar(x){
    $("#confirm-gps").show();
    if(bar_selected[x-1]==1){
        $("#bar"+(x)).css("border-style", "none");
        $("#confirm-gps").hide();
                bar_selected[x-1]=0;
        return 1;
    }
    for(var i=0; i<4; i++){
        $("#bar"+(i+1)).css("border-style", "none");
        bar_selected[i] = 0;
    }
    bar_selected[x-1] = 1;
    $("#bar"+x).css("border-style", "solid");
    $("#bar"+x).css("border-width", "1px");
    $("#bar"+x).css("border-radius", "20%");
    current_x_selected = bar_names[x-1];
    
}



function select_stage(x){
    $("#confirm-gps").show();
    if(stage_selected[x-1]==1){
        $("#stage"+(x)).css("border-style", "none");
        $("#confirm-gps").hide();
                stage_selected[x-1]=0;
        return 1;
    }
    for(var i=0; i<4; i++){
        $("#stage"+(i+1)).css("border-style", "none");
        stage_selected[i] = 0;
    }
    stage_selected[x-1] = 1;
    $("#stage"+x).css("border-style", "solid");
    $("#stage"+x).css("border-width", "1px");
    $("#stage"+x).css("border-radius", "20%");
    current_x_selected = stage_names[x-1];
    
}


function select_wc(x){
    $("#confirm-gps").show();
    if(wc_selected[x-1]==1){
        $("#wc"+(x)).css("border-style", "none");
        $("#confirm-gps").hide();
                wc_selected[x-1]=0;
        return 1;
    }
    for(var i=0; i<4; i++){
        $("#wc"+(i+1)).css("border-style", "none");
        wc_selected[i] = 0;
    }
    wc_selected[x-1] = 1;
    $("#wc"+x).css("border-style", "solid");
    $("#wc"+x).css("border-width", "1px");
    $("#wc"+x).css("border-radius", "20%");
    current_x_selected = wc_names[x-1];
}

function confirm_text(){
    console.log(current_building_selected);
    $("#text_gps").text("Ir para "+current_building_selected+" "+current_x_selected+"?");
}

function gps_canceled(x){
    $('#'+x).hide(350,function(){
        $("#gps_cancelled_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
    });
    current_screen="gps-cancelar";
    $('.you_sure').hide();
}


function unavailable(x){
    $('#'+x).hide(350,function(){
        $("#temporary_unavailable").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
    });
    current_screen="temporary-unavailable";
}

function cancel_add(){
    $('#add_friend_screen').hide(350,function(){
        $("#social_friends_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
    });
    $('.you_sure').hide();
    current_screen="social-friends";
}

function go_profile(x){
    for(var i=0;i<friends_array.length;i++){
            if(friends_array[i].id==x)
                break;
    }
    current_profile = i;
    if(current_list_user==i){
        current_profile=i;
        $("#friend_name").text(friends_array[i].name);
        $(".age").text(friends_array[i].age+" Anos");
        $(".local").text(friends_array[i].local);
        $(".profile_pic").attr("src","images/"+friends_array[i].photo);
        $("#send_message_from_profile").attr("onclick","go_send_profile_message("+'\'' +friends_array[i].id+'\''+ ")");
        $("#delete_user").attr("onclick","check_remove_friend("+'\'' +friends_array[i].id+'\''+ ")");
        $("#list_friend_screen").stop()
                    .hide(350,function() {
                        $("#friend_profile").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                current_screen="social-friend-profile";
                console.log(current_screen); 
    }else{
        current_list_user=i;
    }
}


function change_map(){
    for(var i=0;i<4;i++){
        if(building_selected[i]==1)
            break;
    }
    if(i==0){
        for(i=0;i<4;i++){
            if(banca_selected[i]==1){
                $('#map_image').attr('src','images/'+maps_bancas[i]);
                $('.zoomImg').attr('src','images/'+maps_bancas[i]);
                return 1;
            }
        }
    }
    if(i==1){
        for(i=0;i<4;i++){
            if(bar_selected[i]==1){
                $('#map_image').attr('src','images/'+maps_bar[i]);
                $('.zoomImg').attr('src','images/'+maps_bar[i]);
                return 1;
            }
        }
    }
    if(i==2){
        for(i=0;i<4;i++){
            if(stage_selected[i]==1){
                $('#map_image').attr('src','images/'+maps_stage[i]);
                $('.zoomImg').attr('src','images/'+maps_stage[i]);
                return 1;
            }
        }
    }
    if(i==3){
        for(i=0;i<4;i++){
            if(wc_selected[i]==1){
                $('#map_image').attr('src','images/'+maps_wc[i]);
                $('.zoomImg').attr('src','images/'+maps_wc[i]);
                return 1;
            }
        }
    }
}

function select_friend_gps(x){
    if(friend_selected[x-1]==1){
        $("#friend"+x).css("border-color", "white");
        $("#friend"+x).css("background-color", "transparent");
        $("#cancel_friend_gps").hide();
        friend_selected[x-1]=0;
        return 1;
    }
    for(var i=0; i<current_friends.length; i++){
        friend_selected[i] = 0;
        $('#friend'+(i+1)).css("background-color","transparent");
        $('#friend'+(i+1)).css("border-color","white");
    }
    friend_selected[x-1] = 1;
    $("#friend"+x).css("border-color", "grey");
    $("#friend"+x).css("background-color", "grey");
    $("#cancel_friend_gps").show();
   
}

function change_message_group(x){
    if(message_group[x-1]==0){
        for(var i=0;i<4;i++){
            message_group[i] = 0;
            $('#message_group'+(i+1)).css('background-color','white');
        }
        message_group[x-1] = 1;
        $('#message_group'+x).css('background-color','grey');
        $('#continue_message_group').show();
    }else{
        message_group[x-1] = 0;
        $('#message_group'+x).css('background-color','white');
        $('#continue_message_group').hide();
    }
}

function change_message_group1(x){
    if(message_group1[x-1]==0){
        for(var i=0;i<message_group1.length;i++){
            message_group1[i] = 0;
            $('#greeting'+(i+1)).css('background-color','white');
        }
        message_group1[x-1] = 1;
        $('#greeting'+x).css('background-color','grey');
        $('#send1').show();
    }else{
        message_group1[x-1] = 0;
        $('#greeting'+x).css('background-color','white');
        $('#send1').hide();
    }
}

function change_message_group2(x){
    if(message_group2[x-1]==0){
        for(var i=0;i<message_group2.length;i++){
            message_group2[i] = 0;
            $('#request'+(i+1)).css('background-color','white');
        }
        message_group2[x-1] = 1;
        $('#request'+x).css('background-color','grey');
        $('#send2').show();
    }else{
        message_group2[x-1] = 0;
        $('#request'+x).css('background-color','white');
        $('#send2').hide();
    }
}


function change_message_group3(x){
    if(message_group3[x-1]==0){
        for(var i=0;i<message_group3.length;i++){
            message_group4[i] = 0;
            $('#ask'+(i+1)).css('background-color','white');
        }
        message_group3[x-1] = 1;
        $('#ask'+x).css('background-color','grey');
        $('#send3').show();
    }else{
        message_group3[x-1] = 0;
        $('#ask'+x).css('background-color','white');
        $('#send3').hide();
    }
}

function change_message_group4(x){
    if(message_group4[x-1]==0){
        for(var i=0;i<message_group4.length;i++){
            message_group4[i] = 0;
            $('#answer'+(i+1)).css('background-color','white');
        }
        message_group4[x-1] = 1;
        $('#answer'+x).css('background-color','grey');
        $('#send4').show();
    }else{
        message_group4[x-1] = 0;
        $('#answer'+x).css('background-color','white');
        $('#send4').hide();
    }
}


function continue_message(){
    $('#list_messages_screen1').hide();
    for(var i=0;i<4;i++){
        if(message_group[i]==1)
            break;
    }
    $('#list_messages_screen'+(i+2)).show();
    $('#send1').hide();
    $('#send2').hide();
    $('#send3').hide();
    $('#send4').hide();
}

function send_message(x){
    var text = "<div class='message'><img src='images/doge.png' class='icon_message1'><div class='bubble1'>";
    for(var i=0;i<message_selected[x-1].length;i++){
        if(message_selected[x-1][i]==1)
            break;
    }
    text+=message_text[x-1][i]+'</div></div>';
    friends_array[send_to].messages+=text;
    $('.message_container').append(text);
    $(".list_messages").fadeOut(500);
}
function send(){
    $(".list_messages").fadeIn(500);
    $("#continue_message_group").hide();
    for(var i=0;i<message_selected.length;i++){
        for(var j=0;j<message_selected[i].length;j++)   
            message_selected[i][j] = 0;
    }
    for(var i=0;i<5;i++){
        $('#message_group'+i).css('background-color','white');
        $('#answer'+i).css('background-color','white');
        $('#ask'+i).css('background-color','white');
        $('#request'+i).css('background-color','white');
        $('#greeting'+i).css('background-color','white');
        $('#list_messages_screen'+(i+2)).hide();
    }
    $('#continue_message_group').hide();
    $('#list_messages_screen1').show();
}

function go_send_message(x){
    if(current_message_user==x){
        for(var i=0;i<friends_array.length;i++){
            if(friends_array[i].id==x)
                break;
        }
        send_to = i;
        $('.message_container').empty();
        $('.message_container').append(friends_array[send_to].messages);
        $(".list_messages").hide();
        $("#choose_friend_message").stop()
                    .hide(350,function() {
                        $("#friend_message").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
                current_screen="social-message-friend";
                console.log(current_screen); 
    }else{
        current_message_user=x;
    }
}


function go_send_profile_message(x){
    for(var i=0;i<friends_array.length;i++){
        if(friends_array[i].id==x)
            break;
    }
    send_to = i;
    $('.message_container').empty();
    $('.message_container').append(friends_array[send_to].messages);
    $(".list_messages").hide();
    $("#friend_profile").stop()
                .hide(350,function() {
                    $("#friend_message").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                });
        current_screen="social-message-friend";
        console.log(current_screen); 
}


function choose_shop(x){
    if(x==0){
        $('#food').show();
        $('#drink1').hide();
        $('#choose_food').css('background-color','grey');
        $('#choose_drink').css('background-color','white');
    }else{
        $('#food').hide();
        $('#drink1').show();
        $('#choose_food').css('background-color','white');
        $('#choose_drink').css('background-color','grey');
    }
}

function gps_search_friends_list(){
    var text="";
    for(var i=0; i<current_friends.length;i++){
        text+='<div onclick="select_friend_gps('+(i+1)+')" id="friend'+(i+1)+'"class="friend"><img class="friend_icon" src="images/'+current_friends[i].photo+'"><div class="search_name">'+current_friends[i].name+' '+current_friends[i].surname+'</div><div class="search_location">'+current_friends[i].at+'</div></div>';
    }
    $('.friends_container').empty();
    $('.friends_container').append(text);
}

function friends_messages_list(){
    var text='<div id="carousel2">';
    for(var i=0; i<current_friends.length;i++){
        text+='<img onclick="go_send_message('+'\''+current_friends[i].id+'\''+')" id="'+current_friends[i].id+'" src="images/'+current_friends[i].photo+'"alt="" class="slide"/>';
    }
    text+='</div>';
    $('#carousel2').remove();
    $('#choose_friend_message').append(text);

    text='<ul id="carousel2-descriptions">';
    text+='<li class="desc current">'+current_friends[0].name+'</li>';
    for(var i=1;i<current_friends.length;i++){
        text+='<li class="desc">'+current_friends[i].name+'</li>';
    }
    text+='</ul>';
    $('#carousel2-descriptions').remove();
    $('#choose_friend_message').append(text);
    
    (function($j) {
        var $jdescriptions1 = $j('#carousel2-descriptions').children('li'),
    	$jcontrols1 = $j('#carousel2-controls').find('span'),
    	$jcarousel1= $j('#carousel2')
    		.roundabout({childSelector:"img", minOpacity:1, autoplay:false })
    		.on('focus', 'img', function() {
    			var slideNum = $jcarousel1.roundabout("getChildInFocus");
    			
    			$jdescriptions1.add($jcontrols1).removeClass('current');
    			$j($jdescriptions1.get(slideNum)).addClass('current');
    			$j($jcontrols1.get(slideNum)).addClass('current');
            });
    
        $jcontrols1.on('click dblclick', function() {
    	    var slideNum = -1,
    		i = 0, len = $jcontrols1.length;
    	    for (; i<len; i++) {
        		if (this === $jcontrols1.get(i)) {
        			slideNum = i;
        			break;
        		}
    	    }
    	    if (slideNum >= 0) {
        		$jcontrols1.removeClass('current');
        		$j(this).addClass('current');
    	        $jcarousel1.roundabout('animateToChild', slideNum);
    	    }
        });
    }(jQuery));
}


function friends_list(){
    var text='<div id="carousel1">';
    for(var i=0; i<current_friends.length;i++){
        text+='<img onclick="go_profile('+'\''+current_friends[i].id+'\''+')" id="'+current_friends[i].id+'" src="images/'+current_friends[i].photo+'"alt="" class="slide"/>';
    }
    text+='</div>';
    $('#carousel1').remove();
    $('#list_friend_screen').append(text);

    text='<ul id="carousel1-descriptions">';
    text+='<li class="desc current">'+current_friends[0].name+'</li>';
    for(var i=1;i<current_friends.length;i++){
        text+='<li class="desc">'+current_friends[i].name+'</li>';
    }
    text+='</ul>';
    $('#carousel1-descriptions').remove();
    $('#list_friend_screen').append(text);
 
    (function($j) {

    var $jdescriptions1 = $j('#carousel1-descriptions').children('li'),
	$jcontrols1 = $j('#carousel1-controls').find('span'),
	$jcarousel1= $j('#carousel1')
		.roundabout({childSelector:"img", minOpacity:1, autoplay:false })
		.on('focus', 'img', function() {
			var slideNum = $jcarousel1.roundabout("getChildInFocus");
			
			$jdescriptions1.add($jcontrols1).removeClass('current');
			$j($jdescriptions1.get(slideNum)).addClass('current');
			$j($jcontrols1.get(slideNum)).addClass('current');
        });

    $jcontrols1.on('click dblclick', function() {
	    var slideNum = -1,
		i = 0, len = $jcontrols1.length;
	    for (; i<len; i++) {
    		if (this === $jcontrols1.get(i)) {
    			slideNum = i;
    			break;
    		}
	    }
	    if (slideNum >= 0) {
    		$jcontrols1.removeClass('current');
    		$j(this).addClass('current');
	        $jcarousel1.roundabout('animateToChild', slideNum);
	    }
    });

}(jQuery));

}
function check_add_friend(){
    if(current_friends.length<6 && current_screen=="social-add"){
        for(var i=0;i<binary_friends.length;i++){
            if(binary_friends[i]==0)
                break;
        }
        $("#friend_name1").text(friends_array[i].name);
        $(".age").text(friends_array[i].age+" Anos");
        $(".local").text(friends_array[i].local);
        $(".profile_pic").attr("src","images/"+friends_array[i].photo);
        $("#send_message_from_profile").attr("onclick","go_send_profile_message("+'\'' +friends_array[i].id+'\''+ ")");
        $("#add_friend_screen").stop()
                    .hide(350,function() {
                        $("#friend_add_confirmation").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
        current_screen="social-add-confirmation";
        console.log(current_screen); 
    }else{
        console.log("Impossível adicionar amigo");
    }
}

function add_friend(){
    if(current_friends.length<6 && current_screen=="social-add-confirmation"){
        for(var i=0;i<binary_friends.length;i++){
            if(binary_friends[i]==0)
                break;
        }
        current_friends.push(friends_array[i]);
        binary_friends[i]=1;
        $("#friend_name2").text(current_friends[current_friends.length-1].name);
        $("#friend_add_confirmation").stop()
                    .hide(350,function() {
                        $("#add_friend_sucess").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
        current_screen="social-add-sucess";
        console.log(current_screen);    
    }else{
        console.log("Impossível adicionar amigo");
    }
}

function sucess_add(){
    $('.you_sure').hide();
    $("#add_friend_sucess").stop()
                    .hide(350,function() {
                        $("#social_friends_screen").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
        current_screen="social-friends";
        console.log(current_screen);    
    
}

function check_remove_friend(x){
    for(var i=0;i<friends_array.length;i++){
        if(friends_array[i].id==x)
            break;
    }
    $("#friend_name3").text(friends_array[i].name);
        $(".age").text(friends_array[i].age+" Anos");
        $(".local").text(friends_array[i].local);
        $(".profile_pic").attr("src","images/"+friends_array[i].photo);
        $('#yes_remove').attr("onclick","remove_friend("+'\'' +friends_array[i].id+'\''+ ")");
        $("#friend_profile").stop()
                    .hide(350,function() {
                        $("#friend_remove_confirmation").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                    });
        current_screen="social-remove-confirmation";
        console.log(current_screen); 

}

function remove_friend(x){
    for(var i=0;i<current_friends.length;i++){
        if(current_friends[i].id==x)
            break;
    }
    if(current_friends.length==3){
        console.log('Para algumas funcionalidades funcionarem legitimamente é necessário ter pelo menos 3 amigos.');
        $("#friend_remove_confirmation").stop()
            .hide(350,function() {
                $("#temporary_unavailable").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
            });
        current_screen="remove-failed";
        console.log(current_screen);
        return 1;
    }
    current_friends.splice(i,1);
    binary_friends[i] = 0;
    $('.you_sure').hide();
    $("#friend_remove_confirmation").stop()
                .hide(350,function() {
                    $("#remove_sucess").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                });
    current_screen="remove-sucess";
    console.log(current_screen);
}

function share_status(){
    $('.list_feelings').fadeIn(500);
    $('#list_feelings_screen2').hide();
    $("#continue_feeling_group").hide();
    for(var i=0;i<feels_binary.length;i++){
        feels_binary[i]=0;
        for(var j=0;j<all_feels[i].length;j++)   
            all_feels_binary[i][j] = 0;
    }
    for(i=0;i<feels_binary.length;i++){
        feels_binary[i] = 0;
        $('#feeling_group'+i).css('background-color','white');
    }
}

function change_feeling_group(x){
    if(feels_binary[x]==0){
        for(var i=0;i<feels_binary.length;i++){
            feels_binary[i] = 0;
            $('#feeling_group'+i).css('background-color','white');
        }
        feels_binary[x] = 1;
        $('#feeling_group'+x).css('background-color','grey');
        $('#continue_feeling_group').show();
    }else{
        feels_binary[x] = 0;
        $('#feeling_group'+x).css('background-color','white');
        $('#continue_feeling_group').hide();
    }
}

function continue_feeling(){
    for(var i=0;i<feels_binary.length;i++)
        if(feels_binary[i]==1) 
            break;
    var text="";
    for(var j=0;j<all_feels_binary[i].length;j++)
        text+= '<div id="feeling'+j+'" onclick="change_feeling_group1('+j+')" class="default_messages">'+all_feels[i][j]+'</div>';
    $('.feelings_box').empty();
    $('.feelings_box').append(text);
    $('#list_feelings_screen1').hide();
    $("#share").hide();
    $('#feel_text').text(feels[i]+":");
    $('#list_feelings_screen2').show();
    
}

function change_feeling_group1(x){
    for(var p=0;p<feels_binary.length;p++)
        if(feels_binary[p]==1) 
            break;
    if(all_feels_binary[p][x]==0){
        for(var i=0;i<all_feels_binary[p].length;i++){
            all_feels_binary[p][i] = 0;
            $('#feeling'+i).css('background-color','white');
        }
        all_feels_binary[p][x] = 1;
        $('#feeling'+x).css('background-color','grey');
        $('#share').show();
    }else{
        all_feels_binary[p][x] = 0;
        $('#feeling'+x).css('background-color','white');
        $('#share').hide();
    }
}

function share(){
    for(var i=0;i<feels_binary.length;i++)
        if(feels_binary[i]==1)
            break;
    for(var j=0;j<all_feels_binary[i].length;j++)
        if(all_feels_binary[i][j]==1)
            break;
    
    var text='<div id="doge" class="friend"><img class="friend_icon" src="images/doge.png"><div class="search_name">Doge</div><div class="feeling">'+feels[i]+' '+all_feels[i][j]+'</div></div>';
    text+=feed_text;
    feed_text = text;
    $('.friends_feed').empty();
    $('.friends_feed').append(text);
    $('#list_feelings_screen2').hide();
    $('#list_feelings_screen1').show();
    $('.list_feelings').fadeOut(500);
}

function take_photo(){
    $("#photo_img").animate({width: '60%'}, { duration: 1500, queue: false });
    $("#photo_img").animate({height: '60%'}, { duration: 1500, queue: false });
    $(".share").fadeIn(2000);
    $('#take_photo').text('Voltar');
    $("#photo_img").promise().done(function() {
        $('#take_photo').attr('onclick','back_photo();');
    });
}

function back_photo(){
    if($('.you_sure').css('display')!='none')  
        return 1;
    photo_choosed = Math.floor((Math.random() * current_friends.length) + 1);
    $('#photo_img').attr('src','images/photo'+ photo_choosed +'.jpg');
    $('#photo_img').css('width','100%');
    $('#photo_img').css('height','100%');
    $('.share').hide();
    $('#take_photo').text('Tirar foto');
    $('#take_photo').attr('onclick','take_photo();');
}

function go_camera(){
    photo_choosed = Math.floor((Math.random() * current_friends.length) + 1);
    $('#photo_img').attr('src','images/photo'+ photo_choosed +'.jpg');
}

function generate_photo_screen(icon,name,photo){
    $('.image_photo').attr('src','images/photo'+photo+'.jpg');
    $('.icon_photo_info').attr('src','images/'+icon);
    $('.name_photo_info').text(name);
    $('at_photo_info').text(friends_array[Math.floor((Math.random() * friends_array.length) + 1)-1].at);
    $("#friends_feed").stop()
                .hide(350,function() {
                    $("#see_photo").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                });
    current_screen="see-photo";
    console.log(current_screen);
}

function share_photo(){
    $('.you_sure').hide();
    $('#photo_img').css('width','100%');
    $('#photo_img').css('height','100%');
    $('.share').hide();
    
    var text_photo ='<div onclick="generate_photo_screen(\'doge.png\',\'Doge\',\''+photo_choosed+'\');" id="doge" class="friend"><img class="friend_icon" src="images/doge.png"><div class="search_name">Doge</div><div class="feeling_photo">Ver Foto</div><img class="photo_icon" src="images/photo'+photo_choosed+'.jpg"></div>';
    text_photo+=feed_text;
    feed_text = text_photo;
    $('.friends_feed').empty();
    $('.friends_feed').append(text_photo);
    
    
    $("#camera").stop()
                .hide(350,function() {
                    $("#friends_feed").css("background","radial-gradient(#3F3F3F, #040404)").show(400);
                });
    current_screen="share-foto";
    console.log(current_screen);
}

/* 
    Ver foto -> aparece o mapa se for ao gps
    Ver localização loja -> mudar imagem
    
*/

function fullscreen_x(){
    if(fullscreen==0){
        $(".image_photo").animate({width: '100%'}, { duration: 1500, queue: false });
        $(".image_photo").animate({height: '100%'}, { duration: 1500, queue: false });
        $('.photo_info').css('opacity','0.65');
        $('.photo_info').css('background-color','black');
        fullscreen=1;
    }else{
        $(".image_photo").animate({width: '60%'}, { duration: 1500, queue: false });
        $(".image_photo").animate({height: '60%'}, { duration: 1500, queue: false });
        $('.photo_info').css('background-color','transparent');
        $('.photo_info').css('opacity','1');
        fullscreen=0;
    }
}