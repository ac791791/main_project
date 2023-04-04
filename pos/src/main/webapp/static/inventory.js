
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function addToggle(){
    document.getElementById("inventory-form").reset();
    $('#add-inventory-modal').modal('toggle');
}

var backupPage=1;
var totalPages=1;

//BUTTON ACTIONS

function updateInventory(event){

	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        document.getElementById("inventory-edit-form").reset();
	        document.getElementById("inputPage").value=backupPage;
	   		getLimitedInventoryList(backupPage);
	   		successMessage("Updated");
	   		$('#edit-inventory-modal').modal('toggle');
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});

	return false;
}
function topUpdateInventory(event){
	//Get the ID

	var url = getInventoryUrl();

	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
            var submitBtn = document.getElementById("topUpdate-inventory");
            submitBtn.disabled = true;
            document.getElementById("inputPage").value=backupPage;
            getLimitedInventoryList(backupPage);
            document.getElementById("inventory-form").reset();
            successMessage("Inventory Added")
            $('#add-inventory-modal').modal('toggle');
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});

	return false;
}

function getTotalPages(){


    var url = getInventoryUrl()+"/totalInventory";
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


function getInventoryList(){
    document.getElementById("inventory-form").reset();
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}

function getLimitedInventoryList(pageNo){

    getTotalPages();
    check();
    var url = getInventoryUrl()+"/getLimited/"+pageNo;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayInventoryList(data);
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

}


function checkInputs() {
  var barcode = document.getElementById("inputBarcode").value;
  var quantity = document.getElementById("inputQuantity").value;
  var submitBtn = document.getElementById("topUpdate-inventory");
  if (barcode.length > 0 || quantity > 0) {
    submitBtn.disabled = false;
  } else {
    submitBtn.disabled = true;
  }
}
function enableUpload(){
        var uploadBtn = document.getElementById("process-data");
        uploadBtn.disabled=false;

 }

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
	var fileSize = file.size;
    if(fileSize>5000){
        errorMessage("Can't upload file greater than 5000 rows");
        return;
    }
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
	   document.getElementById("inputPage").value=1;
       getLimitedInventoryList(1);
       successMessage(fileData.length-errorData.length+" records added");

		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getInventoryUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'PUT',
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
    if(errorData.length==0 ){
    errorMessage("Nothing to download");
    }
    else{
	writeFileData(errorData);
	}
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var addButton = document.getElementById('add-inventory');
    if(addButton==null){
        var role="operator";
     }
     else{
        var role ="supervisor";
    }
	for(var i in data){
		var e = data[i];

		var row = '<tr>'
		if(e.barcode.length>30){
             row+= '<td title='+e.barcode+'>' + (e.barcode).slice(0,30)+'...' + '</td>'
        }
        else{
		    row+= '<td>' + e.barcode + '</td>'
		}
		if(e.name.length>30){
             row+= '<td title='+e.name+'>' + (e.name).slice(0,30)+'...' + '</td>'
        }
        else{
             row+= '<td>' + e.name + '</td>'
        }
		row+= '<td>' + e.quantity + '</td>'
		if (role === 'supervisor') {
        	var buttonHtml = ' <button class="tableButtons" onclick="displayEditInventory(' + e.id + ')">Edit</button>'
            row+= '<td>' + buttonHtml + '</td>'
        }

		row+= '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(id){

	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: handleAjaxError
	});
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

      getLimitedInventoryList(p);
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




function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
	var uploadBtn = document.getElementById("process-data");
        uploadBtn.disabled=true;
}

function displayInventory(data){
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$('#edit-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#inventory-form').submit(topUpdateInventory);
	$('#inventory-edit-form').submit(updateInventory);
	$('#add-inventory').click(addToggle);
    $('#next-page').click(nextPage);
    $('#previous-page').click(previousPage);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getLimitedInventoryList(1));


