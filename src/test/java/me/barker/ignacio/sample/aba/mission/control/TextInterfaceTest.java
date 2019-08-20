package me.barker.ignacio.sample.aba.mission.control;

import org.junit.Before;
import org.junit.Test;

public class TextInterfaceTest {

    private TextInterface underTest;

    @Before
    public void setUp() {
        underTest = new TextInterface(new TextInterfaceConfigurationProperties());
    }

    @Test
    public void testReport() {
        underTest.report(new MissionStatusStub());
    }

}