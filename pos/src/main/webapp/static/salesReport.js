
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/salesReport";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}
function getSalesReportList(){

    var $form = $("#salesReport-form");
    var brand = $('#brand-select').children("option:selected").val();
        $("#salesReport-form input[name=brand]").val(brand);

        var category = $('#category-select').children("option:selected").val();
        $("#salesReport-form input[name=category]").val(category);
    var json = toJson($form);
	var url = getSalesReportUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
           'Content-Type': 'application/json'
         },
	   success: function(data) {
	   		displaySalesReportList(data);
	   },
	   error: function(jqXHR, textStatus, errorThrown) {
                                   handleAjaxError(jqXHR, textStatus, errorThrown);
                           }
	});
}

function displaySalesReportList(data){


	var $tbody = $('#salesReport-table').find('tbody');

	$tbody.empty();
	var num=1;
	var total=0;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + num++ + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + parseFloat(e.revenue).toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
        total=total+e.revenue;
	}
	var totalRow = '<tr>'
        		+ '<td>' + "" + '</td>'
        		+ '<td>' + "" + '</td>'
        		+ '<td>' + "" + '</td>'
        		+ '<th>' + "Total" + '</th>'
        		+ '<td>'  + parseFloat(total).toFixed(2) + '</td>'
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

function downloadSalesReport(){
 let table = document.getElementById('salesReport-table');
  let tsvData = generateTsvData(table);
  let blob = new Blob([tsvData], {type: 'text/tab-separated-values;charset=utf-8'});
  let url = URL.createObjectURL(blob);
  let a = document.createElement('a');
  a.href = url;
  const currentDate = new Date();
  const currentDateTimeString = currentDate.toLocaleString();
  a.download = 'SalesReport '+currentDateTimeString+'.tsv';
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



function init(){
	$('#sales-report').click(getSalesReportList);
    $('#download-sales-report').click(downloadSalesReport);

}

$(document).ready(init);
$(document).ready(getSalesReportList);
$(document).ready(setupBrandCategorySelect);
$(function(){
    var dtToday = new Date();

    var month = dtToday.getMonth() + 1;
    var day = dtToday.getDate();
    var year = dtToday.getFullYear();

    if(month < 10)
        month = '0' + month.toString();
    if(day < 10)
        day = '0' + day.toString();

    var maxDate = year + '-' + month + '-' + day;
    $('.date').attr('max', maxDate);
});
