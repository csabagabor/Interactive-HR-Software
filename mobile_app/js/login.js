$(document).ready(function(){

$("#login-alert").hide();

 $("#login-button").click(function(){
   $("#login-alert").hide();
     var email = $("#email").val();
     var password = $("#password").val();

     var send = {
       email : email,
       password : password,
       rememberMe : true
     }

     login(send);
    return false;
 });


async function login(send) {
  await $.ajax({
          url: URL + 'api/auth/login',
          dataType: 'json',
          type: 'post',
          contentType: 'application/json',
          data: JSON.stringify(send),
          processData: false,
          success: function (data, textStatus, jQxhr) {
            var token = data.token;

            var user = parseJwt(token);
            console.log(user);
            if(user.role === 'ROLE_USER'){
             localStorage.setItem("token", token);
             window.location.href = 'home.html';
           }else {
             alert("only for regular users for now");
           }
          },
          error: function (jqXhr, textStatus, errorThrown) {
            $("#login-alert").html("Bad password or email");
            $("#login-alert").show();
          }
      });
}

//taken from https://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-library

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};


})

