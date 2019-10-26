import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

public class InspectorTest extends TestCase {
    private PrintStream sysOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        sysOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void revertStreams() {
        System.setOut(sysOut);
    }

    @Test
    public void testImmediateClassName(){

    }

    @Test
    public void testSuperClassName(){

    }

    @Test
    public void testSuperClassRecursion(){

    }

    @Test
    public void TestInterface(){

    }
}
