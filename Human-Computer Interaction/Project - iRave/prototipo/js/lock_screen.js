$("#lock").click(function(){
    lock_screen();
});

function lock_screen(){
  var $p = $("#main_screen");
    $p.stop()
      .hide(1000, function() {
          $("#lock_screen").show(1000);
                     
          
      });
}