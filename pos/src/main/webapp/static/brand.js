
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}
function addToggle(){
    document.getElementById("brand-form").reset();
    $('#add-brand-modal').modal('toggle');
}

var bulkList=[];

var backupPage=1;
var totalPages=1;


//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
            var submitBtn = document.getElementById("add-brand");
            submitBtn.disabled = true;
	   		document.getElementById("inputPage").value=1;
            successMessage("1 record added");
            getLimitedBrandList(1);
            $('#add-brand-modal').modal('toggle');
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                     handleAjaxError(jqXHR, textStatus, errorThrown);
             }
	});

	return false;
}



function updateBrand(event){

	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

    event.preventDefault();
	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        document.getElementById("brand-edit-form").reset();
	   		document.getElementById("inputPage").value=backupPage;
	   		getLimitedBrandList(backupPage);
	   		successMessage("Updated");
	   		$('#edit-brand-modal').modal('toggle');

	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                   handleAjaxError(jqXHR, textStatus, errorThrown);
              }
	});

	return false;
}

function getTotalPages(){


    var url = getBrandUrl()+"/total";
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {

    	        totalPages=Math.ceil(data/10);
    	   		document.getElementById("inputTotalPages").value=totalPages;
    	   		check();
    	   },
    	   error: handleAjaxError
    	});
}


function getLimitedBrandList(pageNo){

    getTotalPages();
    check();
    var url = getBrandUrl()+"/getLimited/"+pageNo;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayBrandList(data);
    	   },
    	   error: handleAjaxError
    	});

}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];

	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;

	uploadRows();



}

function uploadRows(){

	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    getLimitedBrandList(1);
        document.getElementById("inputPage").value=1;
        successMessage(fileData.length-errorData.length+" records added");
        document.getElementById("brandFile").value = "";
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();
	   },
	   error: function(response){
	   		row.error=JSON.parse(response.responseText).message;
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){

	if(errorData.length==0){
        successMessage("Nothing to download");
        }
        else{
    	writeFileData(errorData);
    	}
}

function checkInputs() {
  var brand = document.getElementById("inputBrand").value;
  var category = document.getElementById("inputCategory").value;
  var submitBtn = document.getElementById("add-brand");
  if (brand.length > 0 || category.length > 0) {
    submitBtn.disabled = false;
  } else {
    submitBtn.disabled = true;
  }
 }

 function enableUpload(){
        var uploadBtn = document.getElementById("process-data");
        uploadBtn.disabled=false;

 }

  function displayPage(){
        getTotalPages();
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

      getLimitedBrandList(p);
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

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	var addButton = document.getElementById('add-brand-button');
	if(addButton==null){
	    var role="operator";
	 }
	 else{
        var role ="supervisor";
    }

	for(var i in data){
		var e = data[i];

        if (role === 'supervisor') {
		    var buttonHtml = ' <button class="tableButtons" onclick="displayEditBrand(' + e.id + ')">Edit</button>'
		}
		else{
		    var buttonHtml='<button class="disabledTableButtons" disabled> Edit </button>';
		}

		var row = '<tr>'
		if(e.brand.length>40){
		    row+= '<td title='+e.brand+'>' + (e.brand).slice(0,40)+'...' + '</td>'
		}
		else{
		    row+= '<td>' + e.brand + '</td>'
		}
		if(e.category.length>40){
        	row+= '<td title='+e.category+'>' + (e.category).slice(0,40)+'...' + '</td>'
        }
        else{
        	row+= '<td>' + e.category + '</td>'
        }
		row+= '<td>' + buttonHtml + '</td>'
		row+= '</tr>';
        $tbody.append(row);
	}
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-brand-modal').modal('toggle');
	var uploadBtn = document.getElementById("process-data");
    uploadBtn.disabled=true;

}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}



//INITIALIZATION CODE
function init(){
	$('#brand-form').submit(addBrand);
	$('#brand-edit-form').submit(updateBrand);
	$('#next-page').click(nextPage);
    $('#previous-page').click(previousPage);
    $('#add-brand-button').click(addToggle);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getLimitedBrandList(1));


