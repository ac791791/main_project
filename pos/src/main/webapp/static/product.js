

var backupPage=1;
var totalPages=1;

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}
function addToggle(){
    document.getElementById("product-form").reset();
    $('#add-product-modal').modal('toggle');
}


//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var brandCategoryId = $('#brand-category-select').children("option:selected").val();
	$("#product-form input[name=brandCategory]").val(brandCategoryId);

	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
            var submitBtn = document.getElementById("add-product");
            submitBtn.disabled=true;
            document.getElementById("inputPage").value=1;
	   		getLimitedProductList(1);
	   		document.getElementById("product-form").reset();
	   		successMessage("1 record added");
	   		$('#add-product-modal').modal('toggle');
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});

	return false;
}

function updateProduct(event){

	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        document.getElementById("product-edit-form").reset();
	        document.getElementById("inputPage").value=backupPage;
	   		getLimitedProductList(backupPage);
	   		successMessage("Updated");
	   		$('#edit-product-modal').modal('toggle');
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});

	return false;
}


function getTotalPages(){

    var url = getProductUrl()+"/totalProducts";
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



function getLimitedProductList(page){

    check();
    getTotalPages();
    var url = getProductUrl()+"/getLimited/"+page;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayProductList(data);
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

}



// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
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
        getLimitedProductList(1);
        successMessage(fileData.length-errorData.length+" records added");
		return;
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	var json = JSON.stringify(row);
	var url = getProductUrl();

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

//UI DISPLAY METHODS

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	var addButton = document.getElementById('add-product-button');
    	if(addButton==null){
    	    var role="operator";
    	 }
    	 else{
            var role ="supervisor";
        }
	for(var i in data){
		var e = data[i];

        if (role === 'supervisor') {
		var buttonHtml = ' <button class="tableButtons" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
		}
        else{
            var buttonHtml='<button class="disabledTableButtons" disabled> Edit </button>';
        }
		var row = '<tr>'
		if(e.barcode.length>15){
                	row+= '<td title='+e.barcode+'>' + (e.barcode).slice(0,15)+'...' + '</td>'
         }
         else{
		    row+= '<td>' + e.barcode + '</td>'
		    }
		if(e.brand.length>15){
        	row+= '<td title='+e.brand+'>' + (e.brand).slice(0,15)+'...' + '</td>'
        }
        else{
        	row+= '<td>' + e.brand + '</td>'
        }
        if(e.category.length>15){
             row+= '<td title='+e.category+'>' + (e.category).slice(0,15)+'...' + '</td>'
        }
        else{
             row+= '<td>' + e.category + '</td>'
        }
		if(e.name.length>15){
             row+= '<td title='+e.name+'>' + (e.name).slice(0,15)+'...' + '</td>'
        }
        else{
              row+= '<td>' + e.name + '</td>'
        }
		row+= '<td>'  + parseFloat(e.mrp).toFixed(2) + '</td>'
		row+= '<td>' + buttonHtml + '</td>'
		row+= '</tr>';
        $tbody.append(row);
	}
}

function checkInputs() {
    var barcode = document.getElementById("inputBarcode").value;
    var name = document.getElementById("inputName").value;
    var mrp = document.getElementById("inputMrp").value;
    var submitBtn = document.getElementById("add-product");
    if (barcode.length > 0 || name.length > 0 || mrp>0) {
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
    check();
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

    getLimitedProductList(p);
    backupPage=p;

}
function check(){
    var page = document.getElementById("inputPage").value;

     var previousBtn=document.getElementById("previous-page");
     var nextBtn=document.getElementById("next-page");

     var maxPage= document.getElementById("inputTotalPages").value;


        if(page=="1"){
            console.log("1");
            previousBtn.disabled=true;
            nextBtn.disabled=false;
        }

        else if(page==totalPages){
            console.log("2");
            previousBtn.disabled=false;
            nextBtn.disabled=true;
        }
        else{
            console.log("3");
            previousBtn.disabled=false;
            nextBtn.disabled=false;
        }

        if(maxPage=="1" || page==""){
                console.log("4");
                console.log("totalPages "+totalPages);
                console.log("page "+page);
                previousBtn.disabled=true;
                nextBtn.disabled=true;
            }


}

function nextPage(){

    var previousValue=document.getElementById("inputPage").value;
    var p=parseInt(previousValue);

    document.getElementById("inputPage").value=p+1;
    displayPage();

}

function previousPage(){

    var previousValue=document.getElementById("inputPage").value;
    var p=parseInt(previousValue);

    document.getElementById("inputPage").value=p-1;
    displayPage();
}



function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
	var uploadBtn = document.getElementById("process-data");
        uploadBtn.disabled=true;
}

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=brand]").val(data.brand);
	$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=brandCategory]").val(data.brandCategory);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=mrp").val(data.mrp);
    $("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}


function displayBrandCategorySelect(brandCategoryList){
    var brandCategorySelect = $('#brand-category-select');
    brandCategorySelect.empty();
    brandCategorySelect.append("<option value=0 disabled selected>Choose  Brand/Category</option>");
    for(brand of brandCategoryList){
        brandCategorySelect.append("<option value="+brand.id+">"+(brand.brand).slice(0,20)+" :: "+(brand.category).slice(0,20)+"</option>")
    }
}

function setupBrandCategorySelect(){
    var url = getBrandUrl();
    $.ajax({
       url: url,
       type: 'GET',
       success: function(brandData) {
        displayBrandCategorySelect(brandData);
       },
       error: handleAjaxError
    });

}


//INITIALIZATION CODE
function init(){
	$('#product-form').submit(addProduct);
	$('#product-edit-form').submit(updateProduct);
	$('#add-product-button').click(addToggle);
    $('#next-page').click(nextPage);
    $('#previous-page').click(previousPage);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)

}

$(document).ready(init);
$(document).ready(setupBrandCategorySelect);
$(document).ready(getLimitedProductList(1));




