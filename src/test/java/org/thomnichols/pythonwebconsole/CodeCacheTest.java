package org.thomnichols.pythonwebconsole;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.python.core.PyCode;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.python.util.PythonObjectInputStream;

public class CodeCacheTest {
	String newline = System.getProperty("line.separator");
	
	//@Test
	public void testSerializePyCode() throws Exception {
		Properties props = new Properties();
	    props.setProperty("python.path", "target/test-classes/Lib.zip");
	    PythonInterpreter.initialize(System.getProperties(), props, new String[0] );
		PythonInterpreter interpreter = new PythonInterpreter( null, new PySystemState() );
		
		String src = IOUtils.toString( getClass().getResourceAsStream( "/testimports.py" ) );
		PyCode code = null; //interpreter.compile( src );
		
		// Serialize the PyCode instance:
		ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( outBytes );
		oos.writeObject( code );
		oos.close();
		ObjectInputStream ois = new PythonObjectInputStream( new ByteArrayInputStream( outBytes.toByteArray() ) );
		code = (PyCode) ois.readObject();
		ois.close();
		
		// attempt to execute the deserialized PyCode:
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		interpreter.setOut( out );
		interpreter.setErr( out );
		interpreter.exec( code );
		assert ("def" + newline).equals( out.toString() );
	}
}
