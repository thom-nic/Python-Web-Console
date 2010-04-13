/* Items from the script page
 * 
 */
app.failureResponseHandler = function(resp) {
	  var msg = resp.statusText ? resp.statusText : 
	    resp.status ? resp.status : "Unknown error: " + o;
	  alert( "Error submitting your request!\n" + msg );
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
		
		// remember the user data in JSON in a cookie:
		var rememberData = {author: form.author.value, email: form.email.value};
		var date = new Date();
		date.setTime( date.getTime() + 2592000000 ); // 30 days
		YAHOO.util.Cookie.set( "commentForm", 
				YAHOO.lang.JSON.stringify(rememberData), 
				{expires: date, path: "/"} );
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
		// get JSON from cookie:
		var data = YAHOO.util.Cookie.get( "commentForm" );
		if ( data ) {
			data = data.parseJSON();
			console.debug( "got cookie", data );
			var form = $( 'commentForm' );
			form.author.value = data.author;
			form.email.value = data.email;
		}
	
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
	  if ( ! data.issue || data.issue.trim() == "") {
	      alert("Please enter something!");
	      return false;
	  }
	  return true;
	};
	
app.deleteHandler = function(link,evt) {
		evt.stopDefault();
		if ( ! confirm("Are you sure you want to delete this script??" ) ) return;
		$$('#header .busy').show();
		console.debug( "link", link );
		YAHOO.util.Connect.asyncRequest( "DELETE", link.node.href, { 
			success: function( resp ) {
				alert( "This script has been deleted" );
				window.location.pathname = "/";
			},
			failure: app.failureResponseHandler
		});
	};
	
app.commentDeleteHandler = function(link,evt) {
		evt.stopDefault();
		if ( ! confirm("Are you sure you want to delete this comment??" ) ) return;
		$$('#header .busy').show();
		console.debug( "link", link );
		YAHOO.util.Connect.asyncRequest( "DELETE", link.node.href, { 
			success: function( resp ) {
				alert( "Comment has been deleted" );
				window.location.reload();
			},
			failure: app.failureResponseHandler
		});
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
	var deleteLink = $$('#deleteLink');
	if ( deleteLink.length ) deleteLink.on( 'click', app.deleteHandler );
	var commentDeleteLinks = $$('#comments .deleteLink');
	if ( commentDeleteLinks ) commentDeleteLinks.on( 'click', app.commentDeleteHandler );

	//SyntaxHighlighter.defaults['light'] = true;
  SyntaxHighlighter.defaults['wrap-lines'] = false;
  SyntaxHighlighter.all();
	
  $$('#comments .bd').forEach( function(node) { 
	  node.node.innerHTML= Util.findLinks(node.node.innerHTML) ;
//	  console.debug(node.node.innerHTML);
	});
});