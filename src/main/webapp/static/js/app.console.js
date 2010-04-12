/* Code for the main (console) page.
 * 
 */

app.evalScript = function(btn,evt) {
		var form = $('consoleForm');
		try {
			if ( app.editor ) $('src').value = app.editor.getCode();
		} catch ( ex ) { console.warn("error getting code from editor", ex); }
		var postData = $$.Forms.getQueryString(form);
		$$('#header .busy').show();
		YAHOO.util.Connect.asyncRequest( form.method, form.action, {
			success: function( resp ) {
				console.debug( "response", resp );
		  	$$('#header .busy').hide();
				var data = resp.responseText.parseJSON();
				app.outputText.setClass(data.status);
				app.outputText.node.value = data.output;
			}, 
			failure: function( resp ) {
		  	$$('#header .busy').hide();
				var msg = resp.statusText ? resp.statusText : 
					resp.status ? resp.status : "Unknown error: " + resp;
				app.outputText.setContent( "[Javacript error] :\n" + msg )
					.setClass('error');
			}
		}, postData );
		evt.stopEvent();
	};
	
/**
 * Perform special functions for certain keys:
 * [tab]        - Insert tab character
 * [ctrl-enter] - submit script for evaluation
 */
app.consoleKeyHandler = function(o,e) {
		o = o.node;
		var tab = "    "; // not '\t' -- its usually 8 spaces, which is too big
		var kC = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
		// tab handler taken from http://pallieter.org/Projects/insertTab/
		if ( kC == 9 && !e.shiftKey && !e.ctrlKey && !e.altKey ) {
			var oS = o.scrollTop;
			if ( o.setSelectionRange ) {
				var sS = o.selectionStart;
				var sE = o.selectionEnd;
				o.value = o.value.substring(0, sS) + tab + o.value.substr(sE);
				o.setSelectionRange(sS + tab.length, sS + tab.length);
				o.focus();
			}
			else if ( o.createTextRange ) {
				document.selection.createRange().text = tab;
				e.returnValue = false;
			}
			o.scrollTop = oS;
			e.stopEvent();
		}
		
		// ctrl-enter to submit!
		if ( (kC == 14 || kC == 13) && (e.shiftKey || e.ctrlKey) ) {
			Util.fireEvent( $('runBtn'),'click' );
			console.debug('exec!');
			e.stopEvent();
		}
	};

app.handleShareSubmit = function() {
		var form = $('shareForm');
		form['source'].value = app.editor ? 
				app.editor.getCode() : $('consoleForm')['src'].value;
		var postData = $$.Forms.getQueryString(form);
		$$('#header .busy').show();
		YAHOO.util.Connect.asyncRequest( 'POST', form.action, { 
			success: function( resp ) {
				console.log( resp );
				var location = resp.getResponseHeader.Location;
				if ( location ) window.location = location;
				else alert( resp.statusText );
				$$('#header .busy').hide();
			}, 
			failure: function(resp) {
	      var msg = resp.statusText ? resp.statusText : 
	        resp.status ? resp.status : "Unknown error: " + o;
	      alert( "Error saving your script!\n" + msg );
	  		$$('#header .busy').hide();
		  }
		}, postData );
		
		// remember the user data in JSON in a cookie:
		var rememberData = {author: form.author.value};
		var date = new Date();
		date.setTime( date.getTime() + 2592000000 ); // 30 days
		YAHOO.util.Cookie.set( "shareForm", 
				YAHOO.lang.JSON.stringify(rememberData), 
				{expires: date, path: "/"} );
	};
	
app.shareDialog = new YAHOO.widget.Dialog( "shareDialog", {
	  fixedcenter: true,
	  visible: false,
	  modal: true,
	  constraintoviewpoint: true,
	  buttons: [ { text: "Share!", handler: app.handleShareSubmit, isDefault:true },
	             { text: "Cancel", handler: function () { this.cancel(); } } ]
	});

app.shareDialog.beforeShowEvent.subscribe( function(dlg,evt) {
		// get JSON from cookie:
		var data = YAHOO.util.Cookie.get( "shareForm" );
		if ( ! data ) data = YAHOO.util.Cookie.get( "commentForm" );
		if ( data ) {
			data = data.parseJSON();
			var form = $('shareForm');
			form.author.value = data.author;
		}
	
	  if ( typeof( Recaptcha ) == 'undefined' ) return; // only loaded if not debug
	  Recaptcha.create( $('recaptcha_pub_key').innerHTML, 
	    'recaptcha_container', { theme: "white" } );
	});
	
app.shareDialog.validate = function () {
	  var data = this.getData();
	  if ( ! data.author || data.author.trim() == "" ) {
	    alert("Please enter your name");
	    return false;
	  }
	  if ( ! data.title || data.title.trim() == "" ) {
	  	alert( "Please enter a title" );
	    return false;
	  }
	  return true;
	};
	
Ojay.onDOMReady( function() {
	$$('#runBtn').on('click',app.evalScript);
	$$('textarea').every(function(ta) { ta.set({wrap:'off',spellcheck:'false'}); });
	app.consoleText = $$('#console textarea');
	app.consoleText.on('keypress',app.consoleKeyHandler);
	app.consoleText.node.value = app.consoleText.node.value.trim();
	app.outputText = $$('#output textarea');
	app.outputText.node.value = app.outputText.node.value.trim();
	
	app.shareDialog.render();
	$$('#shareBtn').on('click',function(btn,evt) {
		app.shareDialog.show();
		evt.stopEvent();
	});
	
	try {
		app.editor = CodeMirror.fromTextArea("src", {
		  path: "/static/codemirror/",
		  parserfile: ["parsepython.min.js"], basefiles: ['bundle.min.js'],
		  stylesheet: "/static/codemirror/pythoncolors.min.css",
		  indentUnit: 4, textWrapping: false, height: '336px'
		});
	} catch ( ex ) { console.warn( ex ); }

	// auto-run the loaded script
	if ( window.location.hash == "#run" ) {
		if ( app.editor ) app.editor.options.initCallback = 
			Util.fireEvent.partial( $('runBtn'),'click' );
		else Util.fireEvent( $('runBtn'),'click' );
	}
});
