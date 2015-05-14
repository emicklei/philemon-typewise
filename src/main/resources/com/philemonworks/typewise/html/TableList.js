<script type='text/javascript'>//<![CDATA[
// These functions exist for com.philemonworks.typewise.html.SortableTableListRenderer
// see http://www.philemonworks.com for more info
function typewise_rowSelected(tr,rowclass){	
	this.rowSelected(tr,rowclass);
	// tr->tbody->table
	var table = tr.parentNode.parentNode.id;
	var rowselected = tr.parentNode.selectedrow;
	if (rowselected == null) {
		document.forms[0][table+'_selectedrow'].value = null;
	} else {
		document.forms[0][table+'_selectedrow'].value = tr.id;
	}	
} 
function typewise_columnClicked(th,columnIndex,sortmethod) {
	// th->tr->tbody->table
	var table = th.parentNode.parentNode.parentNode.id;
	alert(document.forms[0][table+'_selectedrow'].value);
	//this.formEvent(table,0);
}
//]]>
</script>