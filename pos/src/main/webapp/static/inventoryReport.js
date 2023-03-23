function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/inventoryReport";
}
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getInventoryReportList(){

    var $form = $("#inventoryReport-form");

    var brand = $('#brand-select').children("option:selected").val();
    $("#inventoryReport-form input[name=brand]").val(brand);

    var category = $('#category-select').children("option:selected").val();
    $("#inventoryReport-form input[name=category]").val(category);

    var json = toJson($form);
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
           'Content-Type': 'application/json'
         },
	   success: function(data) {
	   		displayInventoryReportList(data);
	   },
	   error: handleAjaxError
	});
}

function displayInventoryReportList(data){
	var $tbody = $('#inventoryReport-table').find('tbody');

	$tbody.empty();
	var num=1;
	var total=0;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + num++ + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
        total=total+e.quantity;
	}
	var totalRow = '<tr>'
    		+ '<td>' + "" + '</td>'
    		+ '<td>' + "" + '</td>'
    		+ '<td>' + "" + '</td>'
    		+ '<td>' + "" + '</td>'
    		+ '<th>' + "Total" + '</th>'
    		+ '<td>'  + total + '</td>'
    		+ '</tr>';

	$tbody.append(totalRow);
	successMessage(num-1+" records found");
}

function generateTsvData(table) {
  let data = '';
  let rows = table.querySelectorAll('tr');
  for (let i = 0; i < rows.length; i++) {
    let cells = rows[i].querySelectorAll('td, th');
    for (let j = 0; j < cells.length; j++) {
      let cellData = cells[j].textContent.replace(/\r?\n|\r/g, '');
      data += cellData + '\t';
    }
    data += '\n';
  }
  return data;
}


function downloadInventoryReport(){
 let table = document.getElementById('inventoryReport-table');
  let tsvData = generateTsvData(table);
  let blob = new Blob([tsvData], {type: 'text/tab-separated-values;charset=utf-8'});
  let url = URL.createObjectURL(blob);
  let a = document.createElement('a');
  a.href = url;
  const currentDate = new Date();
  const currentDateTimeString = currentDate.toLocaleString();
  a.download = 'InventoryReport '+currentDateTimeString+'.tsv';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

function displayBrandSelect(brandCategoryList){
    var brandSelect = document.getElementById('brand-select');
    brandSelect.innerHTML = "<option value='' selected>Choose Brand</option>";
    var uniqueBrands = [];
    for (var i = 0; i < brandCategoryList.length; i++) {
        var brand = brandCategoryList[i].brand;
        if (!uniqueBrands.includes(brand)) {
            uniqueBrands.push(brand);
            var option = document.createElement('option');
            option.value = brand;
            option.text = brand.slice(0, 20);
            brandSelect.add(option);
        }
    }
}

function displayCategorySelect(brandCategoryList){
    var categorySelect = document.getElementById('category-select');
    categorySelect.innerHTML = "<option value='' selected>Choose Category</option>";
    var uniqueCategories = [];
    for (var i = 0; i < brandCategoryList.length; i++) {
        var category = brandCategoryList[i].category;
        if (!uniqueCategories.includes(category)) {
            uniqueCategories.push(category);
            var option = document.createElement('option');
            option.value = category;
            option.text = category.slice(0, 20);
            categorySelect.add(option);
        }
    }
}


function setupBrandCategorySelect(){
    var url = getBrandUrl();
    $.ajax({
       url: url,
       type: 'GET',
       success: function(brandData) {
        displayBrandSelect(brandData);
        displayCategorySelect(brandData);
       },
       error: handleAjaxError
    });

}

function successMessage(message){

    $('.toast-body').text(message);
    $('#success-modal').addClass('show');
    $('.success').toast({delay: 2000});
    $('.success').toast('show');


}


function init(){
    $('#inventory-report').click(getInventoryReportList);
    $('#download-inventory-report').click(downloadInventoryReport);
}

$(document).ready(init);
$(document).ready(getInventoryReportList);
$(document).ready(setupBrandCategorySelect);