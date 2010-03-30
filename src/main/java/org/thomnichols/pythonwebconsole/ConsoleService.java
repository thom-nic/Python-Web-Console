package org.thomnichols.pythonwebconsole;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.concurrent.Future;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

public class ConsoleService {
	
	final Logger log = LoggerFactory.getLogger( getClass() ); 
	
	static final String libZipLocation = "WEB-INF/Lib.zip";
	
	static final String[] RESTRICTED_PACKAGES = { 
			"com.google", "org.datanucleus", 
			"org.springframework" };
	
	static final ApiProxy.Delegate<Environment> PROXY_DELEGATE = 
			new ApiProxy.Delegate<Environment>() {
		@Override
		public void log( Environment arg0, LogRecord arg1 ) {
			// TODO could redirect logging here if desired.
		}

		@Override
		public Future<byte[]> makeAsyncCall( Environment arg0,
				String arg1, String arg2, byte[] arg3, ApiConfig arg4 ) {
			return null;
		}

		@Override
		public byte[] makeSyncCall( Environment arg0, String arg1,
				String arg2, byte[] arg3 ) throws ApiProxyException {
			return null;
		}
	};
	
	static { 
		System.getProperties().remove( "file.encoding" );
		Properties system = (Properties)System.getProperties().clone();
//		system.remove( "file.encoding" );
		Properties post = new Properties();
		post.setProperty( "python.path", libZipLocation );
/*		PyConsoleClassLoader classloader = new PyConsoleClassLoader( 
				new URL[0], Thread.currentThread().getContextClassLoader() );
*/		PythonInterpreter.initialize( system, post, new String[0] ); 
	}
	
	/**
	 * Evaluate the python source and return the output.
	 * @param source
	 * @return
	 */
	public String exec( String source ) {
		log.debug( "Executing python code: {}", source );
		
		Environment env = ApiProxy.getCurrentEnvironment();
		ApiProxy.Delegate<?> originalDelegate = ApiProxy.getDelegate();
		try {
			ApiProxy.clearEnvironmentForCurrentThread();
			ApiProxy.setDelegate( PROXY_DELEGATE );
			PySystemState state = new PySystemState();
			state.setClassLoader( new PyConsoleClassLoader( 
				new URL[0], Thread.currentThread().getContextClassLoader() ) );
			PythonInterpreter interpreter = new PythonInterpreter( null, state );
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			interpreter.setOut( out );
			interpreter.setErr( out );
			interpreter.exec( source );
//			interpreter.exec( "import sys\nsys.path.insert(0, '" + 
//					libZipLocation + "')\n" + source );
//			log.debug( "Output: {}", out );
			return out.toString();
		}
		finally {
			ApiProxy.setDelegate( originalDelegate );
			ApiProxy.setEnvironmentForCurrentThread(env);
		}
	}
	
	static class PyConsoleClassLoader extends URLClassLoader {
		final Logger log = LoggerFactory.getLogger( getClass() ); 
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
			log.debug( "Console loading class: {}", name );
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
