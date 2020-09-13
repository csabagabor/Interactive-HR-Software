function logout(){
  localStorage.removeItem("token");
  window.location.href = "index.html";
}


$(document).ready(function () {

  $("#logout-button").click(function () {
     logout();
  });

  $("#business-request-button").click(function () {

      $.ajax({
              url: URL + 'api/commands/business-request',
              dataType: 'json',
              type: 'get',
              headers: {
                 "Authorization": "Bearer " + localStorage.getItem("token")
             },
              contentType: 'application/json',
              data: null,
              processData: false,
              success: function (data, textStatus, jQxhr) {
              },
              error: function (jqXhr, textStatus, errorThrown) {
                //logout();
              }
          });

  });


  $("#timecard-button").click(function () {
    $.ajax({
            url: URL + 'api/commands/timecard-fill',
            dataType: 'json',
            type: 'put',
            headers: {
               "Authorization": "Bearer " + localStorage.getItem("token")
           },
            contentType: 'application/json',
            data: null,
            processData: false,
            success: function (data, textStatus, jQxhr) {
            },
            error: function (jqXhr, textStatus, errorThrown) {
              //logout();
            }
        });
  });
})
