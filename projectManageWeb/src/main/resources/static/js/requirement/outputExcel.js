var idTmr;function getExplorer(){var e=window.navigator.userAgent;return e.indexOf("MSIE")>=0?"ie":e.indexOf("Firefox")>=0?"Firefox":e.indexOf("Chrome")>=0?"Chrome":e.indexOf("Opera")>=0?"Opera":e.indexOf("Safari")>=0?"Safari":void 0}function method1(e){if("ie"==getExplorer()){var t=document.getElementById(e),o=new ActiveXObject("Excel.Application"),n=o.Workbooks.Add(),a=n.Worksheets(1),l=document.body.createTextRange();l.moveToElementText(t),l.select(),l.execCommand("Copy"),a.Paste(),o.Visible=!0;try{var r=o.Application.GetSaveAsFilename("Excel.xls","Excel Spreadsheets (*.xls), *.xls")}catch(e){print("Nested catch caught "+e)}finally{n.SaveAs(r),n.Close(savechanges=!1),o.Quit(),o=null,idTmr=window.setInterval("Cleanup();",1)}}else tableToExcel(e)}function Cleanup(){window.clearInterval(idTmr),CollectGarbage()}var tableToExcel=function(e,t){e.nodeType||(e=document.getElementById(e));var o,n,a={worksheet:t||"Worksheet",table:e.innerHTML};window.location.href="data:application/vnd.ms-excel;base64,"+(n=a,o='<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><meta http-equiv="Content-Type" charset=utf-8"><head>\x3c!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--\x3e</head><body><table border="0">{table}</table></body></html>'.replace(/{(\w+)}/g,function(e,t){return n[t]}),window.btoa(unescape(encodeURIComponent(o))))};