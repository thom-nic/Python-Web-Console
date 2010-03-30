package org.thomnichols.pythonwebconsole;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class ConsoleServiceTest {
	
	String newline = System.getProperty("line.separator");

	@Test public void testPlainInterpreter() throws Exception {
		Properties props = new Properties();
	    props.setProperty("python.path", "target/test-classes/Lib.zip");
	    PythonInterpreter.initialize(System.getProperties(), props, new String[0] );
		PythonInterpreter interpreter = new PythonInterpreter( null, new PySystemState() );
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		interpreter.setOut( out );
		interpreter.setErr( out );
		String src = IOUtils.toString( getClass().getResourceAsStream( "/testimports.py" ) );
//		src = "import sys\nsys.path.insert(0, 'target/test-classes/Lib.zip')\n" + src; 
		interpreter.exec( src );
		assert ("def" + newline).equals( out.toString() );
	}
	
//	@Test
	public void testExec() throws Exception {
		String src = IOUtils.toString( getClass().getResourceAsStream( "/testimports.py" ) );
		src = "import sys\nsys.path.insert(0, 'Lib.zip')\n" + src; 
		String output = new ConsoleService().exec( src );
		assert "def".equals( output );
	}
}
