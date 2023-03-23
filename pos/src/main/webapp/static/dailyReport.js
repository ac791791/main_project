function getDailyReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/dailyReport";
}


function getDailyReportList(){

    var url = getDailyReportUrl();
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayDailyReportList(data);
    	   },
    	   error: handleAjaxError
    	});

}

function displayDailyReportList(data){
	var $tbody = $('#dailyReport-table').find('tbody');

	$tbody.empty();
	var num=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + num++ + '</td>'
		+ '<td>' + e.date + '</td>'
		+ '<td>' + e.totalInvoice + '</td>'
		+ '<td>' + e.totalItems + '</td>'
		+ '<td>' + parseFloat(e.totalRevenue).toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}


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
function downloadDailyReport(){
 let table = document.getElementById('dailyReport-table');
  let tsvData = generateTsvData(table);
  let blob = new Blob([tsvData], {type: 'text/tab-separated-values;charset=utf-8'});
  let url = URL.createObjectURL(blob);
  let a = document.createElement('a');
  a.href = url;
  const currentDate = new Date();
  const currentDateTimeString = currentDate.toLocaleString();
  a.download = 'DailyReport '+currentDateTimeString+'.tsv';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

function init(){
   $('#download-daily-report').click(downloadDailyReport);
}

$(document).ready(init);
$(document).ready(getDailyReportList);