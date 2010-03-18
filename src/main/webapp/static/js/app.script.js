/* Items from the script page
 * 
 */

app.handleReportSubmit = function() {
		var form = $('reportForm');
		var postData = $$.Forms.getQueryString(form);
		$$('#header .busy').show();
		YAHOO.util.Connect.asyncRequest( form.method, form.action, { 
			success: function( resp ) {
				console.log( resp );
				$$('#header .busy').hide();
				alert(resp.responseText);
				app.reportDialog.hide();
			}, 
			failure: function(resp) {
	      var msg = resp.statusText ? resp.statusText : 
	        resp.status ? resp.status : "Unknown error: " + o;
	      alert( "Error saving your script!\n" + msg );
	  		$$('#header .busy').hide();
		  }
		}, postData );
	};
	
app.reportDialog = new YAHOO.widget.Dialog( "reportDialog", {
	  fixedcenter: true,
	  visible: false,
	  modal: true,
	  constraintoviewpoint: true,
	  buttons: [ { text: "Report!", handler: app.handleReportSubmit, isDefault:true },
	             { text: "Cancel", handler: function () { this.cancel(); } } ]
	});
	
app.reportDialog.validate = function () {
	  var data = this.getData();
	  if ( ! data.content || data.content.trim() == "") {
	      alert("Please enter something!");
	      return false;
	  }
	  return true;
	};
	
Ojay.onDOMReady( function() {
	app.reportDialog.render();
	$$('#reportLink').on('click',function(btn,evt) {
		app.reportDialog.show();
		evt.stopEvent();
	});
});