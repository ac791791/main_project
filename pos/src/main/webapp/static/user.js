
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/user";
}

function addToggle(){
    document.getElementById("user-form").reset();
    $('#add-user-modal').modal('toggle');
}


var backupPage=1;
var totalPages=1;

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   document.getElementById("user-form").reset();
	        var submitBtn = document.getElementById("add-user");
            submitBtn.disabled = true;
            document.getElementById("inputPage").value=1;
            getLimitedUserList(1);
            successMessage("1 record Added");
             $('#add-user-modal').modal('toggle');

	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
       });

	return false;
}

function changePassword(email){

    $("#edit-user-form input[name=email]").val(email);
    console.log("Email "+email);

	var url = getUserUrl();

    event.preventDefault();
	//Set the values to update
	var $form = $("#edit-user-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		successMessage("Password Changed");
	   		$('#edit-user-modal').modal('toggle');

	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                   handleAjaxError(jqXHR, textStatus, errorThrown);
              }
	});

	return false;
}

function getTotalPages(){


    var url = getUserUrl()+"/totalUsers";
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {

    	        totalPages=Math.ceil(data/10);
    	   		document.getElementById("inputTotalPages").value=totalPages;
    	   		check();
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});
}


function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}

function getLimitedUserList(page){

    getTotalPages();

    var url = getUserUrl()+"/getLimited/"+page;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayUserList(data);
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

}
function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	         getTotalPages();
             if(backupPage>totalPages){
            	  backupPage=totalPages;
             }
             getLimitedUserList(backupPage);
             check();
             successMessage("1 user deleted");

	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}

function checkInputs() {
  var email = document.getElementById("inputEmail").value;
  var password = document.getElementById("inputPassword").value;
  var submitBtn = document.getElementById("add-user");
  if (email.length > 0 || password.length > 0) {
    submitBtn.disabled = false;
  } else {
    submitBtn.disabled = true;
  }
}
function displayPage(){

      var page = document.getElementById("inputPage").value;
      if(page==""){
          var p=backupPage;
      }
      else if(page>totalPages){
          var p = totalPages;
          document.getElementById("inputPage").value=p;
      }
      else if(page=="0"){
              p=1;
             document.getElementById("inputPage").value=p;
         }
      else{
          var p =page;
      }

      getLimitedUserList(p);
      backupPage=p;

  }

  function check(){

       getTotalPages();
      var page = document.getElementById("inputPage").value;
      var previousBtn=document.getElementById("previous-page");
      var nextBtn=document.getElementById("next-page");

      var maxPage= document.getElementById("inputTotalPages").value;



          if(page=="1"){
              previousBtn.disabled=true;
              nextBtn.disabled=false;
          }

          else if(page==totalPages){
              previousBtn.disabled=false;
              nextBtn.disabled=true;
          }
          else{
              previousBtn.disabled=false;
              nextBtn.disabled=false;
          }

          if(maxPage=="1" || page==""){
                  previousBtn.disabled=true;
                  nextBtn.disabled=true;
              }


  }

  function nextPage(){

      var previousValue=document.getElementById("inputPage").value;
      var p=parseInt(previousValue);

      document.getElementById("inputPage").value = p + 1;

      displayPage();

  }

  function previousPage(){

      var previousValue=document.getElementById("inputPage").value;
      var p=parseInt(previousValue);

      document.getElementById("inputPage").value=p-1;
      displayPage();
  }


function handleAjaxError(xhr, textStatus, errorThrown) {
  var errorMessage = "An error occurred while processing your request.";
  if (xhr.responseJSON && xhr.responseJSON.message) {
    errorMessage = xhr.responseJSON.message;
  }

  $('.toast-body').text(errorMessage);
  $('#error-modal').addClass('show');
  $('.error').toast({delay: 5000});
  $('.error').toast('show');
  $('#success-modal').removeClass('show');

}
  function successMessage(message){

      $('.toast-body').text(message);
      $('#success-modal').addClass('show');
      $('.success').toast({delay: 2000});
      $('.success').toast('show');
      $('#error-modal').removeClass('show');

  }
//UI DISPLAY METHODS

function displayUserList(data){

	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="deleteButtons" onclick="deleteUser(' + e.id + ')">delete</button>'
		var row = '<tr>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + e.role + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


//INITIALIZATION CODE
function init(){
	$('#user-form').submit(addUser);
	$('#add-user-button').click(addToggle);
    $('#next-page').click(nextPage);
    $('#previous-page').click(previousPage);

}

$(document).ready(init);
$(document).ready(getLimitedUserList(1));




