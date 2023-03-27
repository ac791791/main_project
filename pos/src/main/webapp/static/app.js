
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	alert(response.message);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

function openChangePasswordModal(){
    $('#edit-user-modal').modal('toggle');
        document.getElementById("edit-user-form").reset();
}

function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/user";
}

function changePassword(){

    var email = document.getElementById("myLink").textContent;


    $("#edit-user-form input[name=email]").val(email);


	var url = getUserUrl();


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

function init(){
	$('#edit-user-form').submit(changePassword);

}


$(document).ready(init);


