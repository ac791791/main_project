
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/users";
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



function getTotalPages(){


    var url = getUserUrl()+"/total";
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

function getLimitedUserList(pageNo){

    getTotalPages();

    var url = getUserUrl()+"/getLimited/"+pageNo;
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

    var passwordInput = document.getElementById("inputPassword");
    var password = passwordInput.value;
    var passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+|[\]{};':"\\<>?,./~])/;
    var passwordIsValid = passwordPattern.test(password) && password.length >= 8;
    if (!passwordIsValid) {
      passwordInput.setCustomValidity("Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.");
    } else {
      passwordInput.setCustomValidity("");
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



//UI DISPLAY METHODS

function displayUserList(data){

	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];

		var role=e.role;

		if(role=="operator"){
		    var buttonHtml = '<button class="deleteButtons" onclick="deleteUser(' + e.id + ')">Delete</button>'
		}
		 else{
            var buttonHtml='<button class="disabledTableButtons" disabled> Delete </button>';
        }

		var row = '<tr>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' + role + '</td>'
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




