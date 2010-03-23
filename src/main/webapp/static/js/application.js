Ojay.changeAlias('$$'); // like Prototype.js
$ = function(id) { return Ojay.byId(id).node; };

var app = {};

var Util = {
	/** Fire an event on a DOM element.
	 *  from http://jehiah.cz/archive/firing-javascript-events-properly
	 */
	fireEvent : function( element, event ) {
		if ( document.createEvent ) { // dispatch for firefox + others
			var evt = document.createEvent( 'HTMLEvents' );
			evt.initEvent( event, true, true ); // event type,bubbling,cancelable
			return ! element.dispatchEvent( evt );
		}
		else { // dispatch for IE
			var evt = document.createEventObject();
			return element.fireEvent('on' + event , evt );
		}
	},
	
	getText : function( elem ) {
		return elem.textContent || elem.innerText || elem.innerHTML;
	},
	
	linkRegex : /(https?:\/\/([\w\d\.\-]+):?\d*\/?[\w\d\.\-\?\/%=&#]*)/g,
	
	findLinks : function( text ) {
		return text.replace( Util.linkRegex, '<a href="$1">[$2]</a>' );
	}
};

// Dummy object for platforms w/o a debug console.
if ( typeof( console ) == "undefined" ) {
	console = {
		log : function() {},
		debug : function() {},
		info : function() {},
		warn : function() {},
		error : function() {}
	}
}
// For IE8 and earlier Webkit builds:
if ( ! console.debug ) console.debug = console.log;
