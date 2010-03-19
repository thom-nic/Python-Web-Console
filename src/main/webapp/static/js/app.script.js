/* Items from the script page
 * 
 */
app.failureResponseHandler = function(resp) {
	  var msg = resp.statusText ? resp.statusText : 
	    resp.status ? resp.status : "Unknown error: " + o;
	  alert( "Error saving your script!\n" + msg );
		$$('#header .busy').hide();
	};

app.handleCommentSubmit = function() {
		var form = $('commentForm');
		var postData = $$.Forms.getQueryString(form);
		$$('#header .busy').show();
		YAHOO.util.Connect.asyncRequest( form.method, form.action, { 
			success: function( resp ) {
//				console.log( resp );
//				$$('#header .busy').hide();
//				app.commentDialog.hide();
				// lazy; just reload the page :)
				window.location.reload();
			}, 
			failure: app.failureResponseHandler
		}, postData );
	};
	
app.commentDialog = new YAHOO.widget.Dialog( "commentDialog", {
	  fixedcenter: true,
	  visible: false,
	  modal: true,
	  constraintoviewpoint: true,
	  buttons: [ { text: "Submit!", handler: app.handleCommentSubmit, isDefault:true },
	             { text: "Cancel", handler: function () { this.cancel(); } } ]
	});
	
app.commentDialog.validate = function () {
	  var data = this.getData();
	  if ( ! data.content || data.content.trim() == "") {
	      alert("Please enter something!");
	      return false;
	  }
	  if ( ! data.title || data.title.trim() == "") {
	      alert("Please enter a subject!");
	      return false;
	  }
	  return true;
	};
	
app.commentDialog.beforeShowEvent.subscribe( function(dlg,evt) {
	  if ( typeof( Recaptcha ) == 'undefined' ) return; // only loaded if not debug
	  Recaptcha.create( $('recaptcha_pub_key').innerHTML, 
	    'recaptcha_container', { theme: "white" } );
	});

	
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
			failure: app.failureResponseHandler
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
	var clickHandler = function(dialog,btn,evt) {
		dialog.show();
		evt.stopEvent();
	};
	app.commentDialog.render();
	$$('#commentLink').on('click',clickHandler.partial(app.commentDialog));
	app.reportDialog.render();
	$$('#reportLink').on('click',clickHandler.partial(app.reportDialog));
	$$('#commentDialog, #reportDialog').removeClass('hidden');
	$$('#comments .bd').forEach( function(node) { 
	  node.node.innerHTML= Util.findLinks(node.node.innerHTML) ;
//	  console.debug(node.node.innerHTML);
	});
});