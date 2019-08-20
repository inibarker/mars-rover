package me.barker.ignacio.sample.aba.mission.mars;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class MarsMissionInterfaceTest {

    private MarsMissionInterface underTest;

    @Before
    public void setUp() {
        underTest = new MarsMissionInterface();
    }

    @Test(expected = NotImplementedException.class)
    public void testOperate() {
        underTest.operate(null);
    }

    @Test(expected = NotImplementedException.class)
    public void testReport() {
        underTest.report();
    }
}