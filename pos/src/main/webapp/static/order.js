function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orders";
}
function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderItems";
}
function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/generateInvoice";
}

var backupPage=1;
var totalPages=1;

// Later will be updated with maximum orderId and will be used in Deleting a order while canceling.
var maxOrderId=0;


//BUTTON ACTIONS


function addOrderItem(){

    var $form = $("#order-edit-form");
    var orderId = $("#order-edit-form input[name=orderId]").val();
    	var json = toJson($form);
    	var url = getOrderItemUrl();

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		document.getElementById("order-edit-form").reset();
    	   		var submitBtn = document.getElementById("edit-add-button");
    	   		submitBtn.disabled=true;
                getOrderItemUpdateList();
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

    	return false;
}
function createOrderItem(){

    var $form = $("#order-add-form");
    var orderId = $("#order-edit-form input[name=orderId]").val();
    	var json = toJson($form);
    	var url = getOrderItemUrl();

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		document.getElementById("order-add-form").reset();
    	   		var submitBtn = document.getElementById("addItem-button");
    	   		submitBtn.disabled=true;
                getOrderItemAddList();
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

    	return false;
}


function createOrder(){

    var $tbody = $('#add-order-table').find('tbody');
    $tbody.empty();
    document.getElementById("order-add-form").reset();
    var url=getOrderUrl();
    $.ajax({
    	   url: url,
    	   type: 'POST',

    	   success: function(response) {
    	         getRecentOrderId();
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

    	return false;

}

function getTotalPages(){


    var url = getOrderUrl()+"/total";
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






function getLimitedOrderList(pageNo){

    getTotalPages();

    var url = getOrderUrl()+"/getLimited/"+pageNo;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayOrderList(data);
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

}

function getRecentOrderId(){
    var url=getOrderUrl()+"/recentOrder";
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		maxOrderId=data.id;
    	   		$("#order-add-form input[name=orderId]").val(data.id);
    	   		$('#add-order-modal').modal('toggle');

    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

}
function getOrderItemList(id){
    $("#orderItem-view-form input[name=orderId]").val(id);
    $('#view-order-modal').modal('toggle');
	var url = getOrderItemUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItemList(data);
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}
function getOrderItemUpdateList(){

    var orderId = $("#order-edit-form input[name=orderId]").val();
	var url = getOrderItemUrl() + "/" + orderId;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItemUpdateList(data);
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}

function getOrderItemAddList(){

    var orderId = maxOrderId;
	var url = getOrderItemUrl() + "/" + orderId;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItemAddList(data);
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}





function deleteOrder(id){
	var url = getOrderUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	        getTotalPages();
            if(backupPage>totalPages){
                   backupPage=totalPages;
             }
            getLimitedOrderList(backupPage);
            check();

	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}
function deleteOrderItem(id){
    var url=getOrderItemUrl()+"/"+id;

    $.ajax({
    	   url: url,
    	   type: 'DELETE',
    	   success: function(data) {

    	   		getOrderItemUpdateList(id);
    	   		getOrderItemAddList();
    	   },
    	   error: function(jqXHR, textStatus, errorThrown) {
                                       handleAjaxError(jqXHR, textStatus, errorThrown);
                               }
    	});

}

function updateOrderItem(event){

	//Get the ID
	var id = $("#orderItem-edit-form input[name=id]").val();
	var barcode = $("#orderItem-edit-form input[name=barcode]").val();


	var url = getOrderItemUrl() + "/" + id;


	//Set the values to update
	var $form = $("#orderItem-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        document.getElementById("orderItem-edit-form").reset();
	   		getOrderItemUpdateList();
            getOrderItemAddList();
            $('#edit-orderItem-modal').modal('hide');
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});

	return false;
}


function checkInputs1() {
  var barcode = document.getElementById("addBarcode").value;
  var quantity = document.getElementById("addQuantity").value;
  var mrp = document.getElementById("addMrp").value;
  var submitBtn = document.getElementById("addItem-button");
  if (barcode.length > 0 || quantity > 0 || mrp>0) {
    submitBtn.disabled = false;
  } else {
    submitBtn.disabled = true;
  }
}

function checkInputs2() {
  var barcode = document.getElementById("updateInputBarcode").value;
  var quantity = document.getElementById("updateInputQuantity").value;
  var mrp = document.getElementById("updateInputMrp").value;
  var submitBtn = document.getElementById("edit-add-button");
  if (barcode.length > 0 || quantity > 0 || mrp>0) {
    submitBtn.disabled = false;
  } else {
    submitBtn.disabled = true;
  }
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

      getLimitedOrderList(p);
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

function displayOrderList(data){


	var $tbody = $('#order-table').find('tbody');
    	$tbody.empty();
    	data.sort((a, b) => b.id - a.id);
    	for(var i in data){
    		var e = data[i];
    	var dateArray = e.time;

    	if(e.invoiceStatus==1){
    	    var status="<i>Invoiced<i>";
    	}
    	else{
    	    var status="Ordered";
    	}


        var date = new Date(dateArray[0], dateArray[1], dateArray[2], dateArray[3], dateArray[4], dateArray[5]);
        const options = { weekday: 'short', month: 'short', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric', timeZone: 'Asia/Kolkata' };
        const formattedDate = date.toLocaleString('en-US', options);
        // Output: "Mon, Feb 27, 2023, 12:27:11 PM"
        console.log(formattedDate+"2nd");
                if(e.invoiceStatus==1){
                    var buttonHtml = '<button class="disabledTableButtons" disabled>Delete</button>'
                    buttonHtml += ' <button class="disabledTableButtons" disabled>Edit</button>'
                }
                else{
                    var buttonHtml = '<button class="deleteButtons" onclick="confirmDeleteOrder(' + e.id + ')">Delete</button>'
                    buttonHtml += ' <button class="tableButtons"  onclick="displayOrder('+e.id+')">Edit</button>'
                 }

        		buttonHtml += ' <button class="tableButtons"  onclick="getOrderItemList(' + e.id + ')">View</button>'
    		var row = '<tr>'
    		+ '<td>' + e.id + '</td>'
    		+ '<td>' + formattedDate + '</td>'
    		+ '<td>' + status + '</td>'
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row);
}
}
function confirmDeleteOrder(id) {
  if (confirm("Are you sure you want to delete this Order?")) {
    deleteOrder(id);
  }
  }
function confirmDeleteOrderItem(id) {
  if (confirm("Are you sure you want to delete this OrderItem?")) {
    deleteOrderItem(id);
  }
  }

function displayOrderItemList(data){


	var $tbody = $('#view-order-table').find('tbody');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];

    		var row = '<tr>'
    		+ '<td>' + e.barcode + '</td>'
    		+ '<td>' + e.quantity + '</td>'
    		+ '<td>' + parseFloat(e.sellingPrice).toFixed(2) + '</td>'
    		+ '</tr>';

            $tbody.append(row);
}
}

function displayOrderItemUpdateList(data){


	var $tbody = $('#update-order-table').find('tbody');
    	$tbody.empty();

    	for(var i in data){
    		var e = data[i];


        		var buttonHtml = '<button class="deleteButtons" onclick="confirmDeleteOrderItem(' + e.id + ')">Delete</button>'
        		buttonHtml += ' <button class="tableButtons" onclick="displayEditOrderItem(' + e.id + ')" >Edit</button>'
    		var row = '<tr>'
    		+ '<td>' + e.barcode + '</td>'
    		+ '<td>' + e.quantity + '</td>'
    		+ '<td>' + parseFloat(e.sellingPrice).toFixed(2) + '</td>'
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';

            $tbody.append(row);
}
}



function displayOrderItemAddList(data){


	var $tbody = $('#add-order-table').find('tbody');
    	$tbody.empty();

    	for(var i in data){
    		var e = data[i];


        		var buttonHtml = '<button class="deleteButtons" onclick="confirmDeleteOrderItem(' + e.id + ')">Delete</button>'
        		buttonHtml += ' <button class="tableButtons" onclick="displayEditOrderItem(' + e.id + ')" >Edit</button>'
    		var row = '<tr>'
    		+ '<td>' + e.barcode + '</td>'
    		+ '<td>' + e.quantity + '</td>'
    		+ '<td>' + parseFloat(e.sellingPrice).toFixed(2) + '</td>'
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';

            $tbody.append(row);
}
        var addOrderBtn = document.getElementById("add-order");
        if ($tbody.children().length === 0) {

                    addOrderBtn.disabled=true;
        }
        else{
            addOrderBtn.disabled=false;
        }
}

function displayEditOrderItem(id){
	var url = getOrderItemUrl() + "/single/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItem(data);
	   },
	   error: handleAjaxError
	});
}

function displayOrderItem(data){
	$("#orderItem-edit-form input[name=quantity]").val(data.quantity);
	$("#orderItem-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$("#orderItem-edit-form input[name=id]").val(data.id);
	$("#orderItem-edit-form input[name=barcode]").val(data.barcode);
    $("#orderItem-edit-form input[name=orderId]").val(data.orderId);
	$('#edit-orderItem-modal').modal('toggle');
}



function displayOrder(id){

    document.getElementById("order-edit-form").reset();
	$("#order-edit-form input[name=orderId]").val(id);
	$('#edit-order-modal').modal('toggle');
	getOrderItemUpdateList();
	getOrderItemAddList();
}

function closeAddModal() {
     $('#add-order-modal').modal('hide');
     getLimitedOrderList(1);
     document.getElementById("inputPage").value=1;


}

function cancelOrder(){
    deleteOrder(maxOrderId);
    document.getElementById("order-add-form").reset();
    var $tbody = $('#add-order-table').find('tbody');
     $tbody.empty();


    $('#add-order-modal').modal('hide');


}

function downloadInvoice()
{
     var $tbody = $('#view-order-table').find('tbody');
     if ($tbody.children().length === 0) {
        successMessage("Nothing to download")
     }
     else{
         var orderId = $('#orderItem-view-form input[name=orderId]').val();
         console.log("orderId "+orderId);
         var baseUrl = $("meta[name=baseUrl]").attr("content")
         orderId=parseInt(orderId)
         var url = baseUrl + "/api/generateInvoice" + "/" + orderId;

         window.open(url);

     }
     document.getElementById("inputPage").value=backupPage;
     getLimitedOrderList(backupPage);
}


//INITIALIZATION CODE
function init(){

    $('#orderItem-edit-form').submit(updateOrderItem);
    $('#next-page').click(nextPage);
    $('#previous-page').click(previousPage);
	$('#order-edit-form').submit(addOrderItem);
	$('#order-add-form').submit(createOrderItem);
	$('#create-order').click(createOrder);
	$('#cancel-order').click(cancelOrder);
	$('#cancelTop-order').click(cancelOrder);
	$('#add-order').click(closeAddModal);
	$('#download-invoice').click(downloadInvoice);



}


$(document).ready(init);
$(document).ready(getLimitedOrderList(1));
