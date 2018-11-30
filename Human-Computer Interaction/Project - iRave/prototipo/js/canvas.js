function drawTextAlongArc(context, str, centerX, centerY, radius, angle) {
        var len = str.length, s;
        context.save();
        context.translate(centerX, centerY);
        context.rotate(-1 * angle / 2);
        context.rotate(-1 * (angle / len) / 2);
        for(var n = 0; n < len; n++) {
          context.rotate(angle / len);
          context.save();
          context.translate(0, -1 * radius);
          s = str[n];
          context.fillText(s, 0, 0);
          context.restore();
        }
        context.restore();
      }
            
        window.onload = function() {
 
        // Get the canvas element and its drawing context
        var canvas = document.getElementById('diagonalLines');
        var context = canvas.getContext('2d');
        context.lineWidth=3;
        context.beginPath();
        context.moveTo(28, 28);
        context.lineTo(42, 42);
        context.stroke();
        
        context.beginPath();
        context.moveTo(165, 165);
        context.lineTo(152, 152);
        context.stroke();
        
        context.beginPath();
        context.moveTo(28, 165);
        context.lineTo(42, 152);
        context.stroke();
        
        context.beginPath();
        context.moveTo(165, 28);
        context.lineTo(152, 42);
        context.stroke();
        
        context.beginPath();
        context.arc(96, 96, 78, 0, 2 * Math.PI, false);
        context.lineWidth = 3;
        context.stroke();
        



};    