package me.barker.ignacio.sample.aba.mission.control;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class TextInterfaceTest {

    private TextInterface underTest = new TextInterface();

    @Before
    public void setUp() {
        underTest = new TextInterface();
    }

    @Test(expected = NotImplementedException.class)
    public void testReport() {
        new TextInterface().report(null);
    }

    @Test(expected = NotImplementedException.class)
    public void testNextCommand() {
        underTest.nextCommand();
    }

    @Test(expected = NotImplementedException.class)
    public void testLastCommand() {
        underTest.lastCommand();
    }
}