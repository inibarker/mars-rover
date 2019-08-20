package me.barker.ignacio.sample.aba.mission.mars;

import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import reactor.test.StepVerifier;

public class MarsMissionInterfaceTest {

    private static final ControlCommand TEST_CONTROL_COMMAND = ControlCommand.MissionCommand.REPORT;

    private MarsMissionInterface underTest;

    @Before
    public void setUp() {
        underTest = new MarsMissionInterface();
    }

    @Test(expected = NotImplementedException.class)
    public void testOperate() {
        StepVerifier.create(underTest.operate(TEST_CONTROL_COMMAND))
            .verifyComplete();
    }

    @Test(expected = NotImplementedException.class)
    public void testReport() {
        StepVerifier.create(underTest.report())
            .verifyComplete();
    }
}