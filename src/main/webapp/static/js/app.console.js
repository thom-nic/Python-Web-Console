/* Code for the main (console) page.
 * 
 */

app.evalScript = function(btn,evt) {
		var form = $('consoleForm');
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
		form['source'].value = $('consoleForm')['src'].value;
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
	  if ( typeof( Recaptcha ) == 'undefined' ) return; // only loaded if not debug
	  Recaptcha.create( $('recaptcha_pub_key').innerHTML, 
	    'recaptcha_container', { theme: "white" } );
	});
	
app.shareDialog.validate = function () {
	  var data = this.getData();
	  if (data.name == "") {
	      alert("Please enter your name");
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
	
	// auto-run the loaded script
	if ( window.location.hash == "#run" ) Util.fireEvent( $('runBtn'),'click' );
});
