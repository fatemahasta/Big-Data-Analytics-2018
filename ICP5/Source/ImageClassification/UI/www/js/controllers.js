angular.module('starter.controllers', [])



.controller('homeCtrl', function($scope) {
    
  $scope.getImageClassification = function() {
    console.log("Predict Image");
      
      
    function getBase64Image(img) {
    var canvas = document.createElement("canvas");
    canvas.width = img.width;
    canvas.height = img.height;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0);
    var dataURL = canvas.toDataURL("image/png");
    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
  }

  $('.thumbnail img')
    .click(function () {
      var btm_img = $(this).attr('src');
      $('#img_plc').attr('src', btm_img);
    });
   
      $('#txt_area').hide();
    $('#meow').show();
       console.log("I am here");
    var tmp_img = document.createElement("img");
    tmp_img.src = 'http://'+location.host+'/'+$('#img_plc').attr('src'); 
     console.log("I am here "+location.host);
   
    var base64 = getBase64Image(tmp_img);
    $.ajax({
      type: "POST",
      url: "http://localhost:8080/get_custom",
      data: base64,
      success: function (result) {
        $('#meow').hide();
        $('#txt_area').text(result);
        $('#txt_area').show();
      }
    });
      
  };
});
