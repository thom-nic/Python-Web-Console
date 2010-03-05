package org.thomnichols.pythonwebconsole;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleService {
	
	static final Logger log = LoggerFactory.getLogger( ConsoleService.class ); 
	
	static final String[] RESTRICTED_PACKAGES = { 
			"com.google", "org.datanucleus", 
			"org.springframework" };
	
	static { 
		Properties pre = new Properties();
		//pre.setProperty( PySystemState.PYTHON_CONSOLE_ENCODING, null );
		System.getProperties().remove("file.encoding");
		PyConsoleClassLoader classloader = new PyConsoleClassLoader( 
				new URL[0], Thread.currentThread().getContextClassLoader() );
		PySystemState.initialize(pre,null,new String[]{""}, classloader ); 
	}
	
	/**
	 * Evaluate the python source and return the output.
	 * @param source
	 * @return
	 */
	public String exec( String source ) {
		log.debug( "Executing python code: {}", source );
		
/*		PyConsoleClassLoader classloader = new PyConsoleClassLoader( 
				new URL[0], Thread.currentThread().getContextClassLoader() );
		ScriptEngine engine = new ScriptEngineManager(classloader).getEngineByName( "python" );
		engine.eval( source );
*/
		PythonInterpreter interpreter = new PythonInterpreter( null, new PySystemState() );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		interpreter.setOut( out );
		interpreter.setErr( out );
		interpreter.exec( source );
		log.debug( "Output: {}", out );
		return out.toString();
	}
	
	static class PyConsoleClassLoader extends URLClassLoader {
		public PyConsoleClassLoader( URL[] urls ) {
			super( urls );
		}
		public PyConsoleClassLoader( URL[] urls, ClassLoader parent ) {
			super( urls, parent );
		}
		
		@Override
		protected void addURL( URL url ) {
			//super.addURL( url );
			throw new RuntimeException( "Cannot extend classloader at runtime" ); 
		}
		
		@Override
		protected Class<?> findClass( String name )
				throws ClassNotFoundException {
			// TODO use whitelist instead of blacklist
			for ( String packageName : RESTRICTED_PACKAGES )
				if ( name.startsWith( packageName ) ) {
					log.warn( "Attempt to access restricted class: {}", name );
					throw new ClassNotFoundException( name );
				}
			return super.findClass( name );
		}
	}
}
