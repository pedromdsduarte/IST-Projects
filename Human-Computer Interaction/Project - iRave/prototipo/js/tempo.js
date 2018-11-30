
$(document).ready(function() {
  $.simpleWeather({
    location: 'Lisbon, PT',
    woeid: '',
    unit: 'c',
    success: function(weather) {
      html = '<p><iclass="weather"></i>'+weather.temp+'&deg;'+weather.units.temp+' ' +weather.wind.speed+' '+weather.units.speed+'</p>';
      $("#weather").html(html);

    },
    error: function(error) {
      $("#weather").html('<p>'+error+'</p>');
    }
  });
});
