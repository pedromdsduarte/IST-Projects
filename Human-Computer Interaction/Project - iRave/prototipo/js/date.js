var today=new Date();
var h=today.getHours();
var m=today.getMinutes();
var d = new Date();
var options = { weekday: 'short', month: 'long', day: '2-digit' };
document.getElementById("date").innerHTML = d.toLocaleDateString('pt', options);

m = checkTime(m);
document.getElementById('txt').innerHTML = h+":"+m;
function checkTime(i) {
    if (i<10) {i = "0" + i;} 
    return i;
}

function checkHour(){
    var today=new Date();
    var h=today.getHours();
    var m=today.getMinutes();
    document.getElementById('txt').innerHTML = h+":"+m;
}